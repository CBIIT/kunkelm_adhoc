
--THIS ONLY NEEDS DOING IF STANDARDIZED_SMILES has been regenerated in PLP

drop table if exists standardized_smiles_plp;

create table standardized_smiles_plp(
name varchar(1024),
nsc int,
structure_source varchar(1024),
smiles_from_prod varchar(1024),
prod_can_smi varchar(1024),
removedsalts varchar(1024),
parent_can_smi varchar(1024),
taut_can_smi varchar(1024),
taut_nostereo_can_smi varchar(1024),
xeno int,
hf int,
nci60 int,
distribution varchar(1024),
conf varchar(1024),
inventory double precision,
mw double precision,
mf varchar(1024),
alogp double precision,
logd double precision,
hba int,
hbd int,
sa double precision,
psa double precision,
cas int, 
count_related int,
related varchar(1024),
nsc_mol_fk int
);

\copy standardized_smiles_plp from /home/mwkunkel/PROJECTS/ACTIVE/DIS/DCTDDataSystem/DATA_LOAD/STANDARDIZED_SMILES.csv csv header quote as '"'

drop sequence if exists standardized_smiles_seq;

create sequence standardized_smiles_seq;

--drop table if exists standardized_smiles;
--
--create table standardized_smiles (
--id bigint not null,
--name character varying(1024),
--nsc integer,
--structure_source character varying(1024),
--smiles_from_prod character varying(1024),
--prod_can_smi character varying(1024),
--removed_salts character varying(1024),
--parent_can_smi character varying(1024),
--taut_can_smi character varying(1024),
--taut_nostereo_can_smi character varying(1024),
--xeno integer,
--hf integer,
--nci60 integer,
--distribution character varying(1024),
--conf character varying(1024),
--inventory double precision,
--mw double precision,
--mf character varying(1024),
--alogp double precision,
--logd double precision,
--hba integer,
--hbd integer,
--sa double precision,
--psa double precision,
--cas character varying(1024),
--prefix character varying(1024) not null,
--count_related integer,
--related character varying(1024),
--rdkit_mol_fk bigint unique,
--primary key (id)
--);

insert into standardized_smiles(
id,
name,
nsc,
structure_source,
smiles_from_prod,
prod_can_smi,
removed_salts,
parent_can_smi,
taut_can_smi,
taut_nostereo_can_smi,
xeno,
hf,
nci60,
distribution,
conf,
inventory,
mw,
mf,
alogp,
logd,
hba,
hbd,
sa,
psa,
cas,
prefix,
count_related,
related,
rdkit_mol_fk)
select
nextval('standardized_smiles_seq'),
name,
nsc,
structure_source,
smiles_from_prod,
prod_can_smi,
removedsalts,
parent_can_smi,
taut_can_smi,
taut_nostereo_can_smi,
xeno,
hf,
nci60,
distribution,
conf,
inventory,
mw,
mf,
alogp,
logd,
hba,
hbd,
sa,
psa,
cas,
'S',
count_related,
related,
nsc_mol_fk
from standardized_smiles_plp;

create index ss_nsc on standardized_smiles(nsc);

-- FIRST THING!  Figure out which have valid smiles according to RDKit

alter table rdkit_mol
alter column mol
type mol
using mol_from_smiles(mol::cstring);

insert into rdkit_mol (id, nsc, mol)
select id, nsc, mol_from_smiles(parent_can_smi::cstring) 
from standardized_smiles
where is_valid_smiles(parent_can_smi::cstring);

dctddatasystem->Time: 526821.931 ms
#dctd->Time: 343686.147 ms

--

insert into cmpd_fragment_p_chem(id, mw, mf, alogp, logd, hba, hbd, sa, psa)
select id, mw, mf, alogp, logd, hba, hbd, sa, psa 
from standardized_smiles;

insert into cmpd_bio_assay(id, nci60, hf, xeno)
select id, nci60, hf, xeno 
from standardized_smiles;

-- cmpd is an abstract class => single attribute id

insert into cmpd(id)
select id
from standardized_smiles;

insert into nsc_cmpd(id, name, nsc_cmpd_id, prefix, nsc, conf, cas, distribution, cmpd_parent_fragment_fk, cmpd_bio_assay_fk)
select id, 'NSC'||nsc, id, prefix, nsc, conf, cas distribution, id, null, id 
from standardized_smiles;

--alter cmpd_fragment_structure to mol type

alter table cmpd_fragment_structure
alter column mol
type mol
using mol_from_smiles(mol::cstring);

--load only successful structures (the content of rdkit_mol) 

insert into cmpd_fragment_structure (id, smiles, mol)
select id, mol, mol 
from rdkit_mol;

--index for substructure searches
create index cmpd_fragment_structure_mol on cmpd_fragment_structure using gist(mol);

--there is a pchem entry for every ss
--only need loj for structures
--THIS WILL NOT OBTAIN for "REAL" CMPD structures

--holding table to avoid index/constraint tie ups

drop table if exists temp;

create table temp as 
select c.id, c.id as nsc_cmpd_fk, c.id as cmpd_fragment_p_chem_fk, rdk.id as cmpd_fragment_structure_fk 
from cmpd c left outer join rdkit_mol rdk
on c.id = rdk.id;

--drop constraints

alter table cmpd_fragment drop constraint "cmpd_fragment_pkey";
alter table cmpd_fragment drop constraint "cmpd_fragment_cmpd_fragment_p_chem_fk_key";
alter table cmpd_fragment drop constraint "cmpd_fragment_cmpd_fragment_structure_fk_key";
alter table cmpd_fragment drop constraint "cmpd_fragment_cmpd_fragment_pc";
alter table cmpd_fragment drop constraint "cmpd_fragment_cmpd_fragment_sc";
alter table cmpd_fragment drop constraint "cmpd_fragment_cmpd_known_saltc";
alter table cmpd_fragment drop constraint "cmpd_fragment_nsc_cmpd_fkc";
alter table nsc_cmpd drop CONSTRAINT "nsc_cmpd_cmpd_parent_fragmentc";

--insert 

insert into cmpd_fragment(id, nsc_cmpd_fk, cmpd_fragment_p_chem_fk, cmpd_fragment_structure_fk) 
select id, nsc_cmpd_fk, cmpd_fragment_p_chem_fk, cmpd_fragment_structure_fk 
from temp;

--recreate constraints

alter table cmpd_fragment add primary key(id);

alter table cmpd_fragment 
add constraint cmpd_fragment_nsc_cmpd_fkc 
foreign key (nsc_cmpd_fk) 
references nsc_cmpd;

alter table cmpd_fragment 
add constraint cmpd_fragment_cmpd_known_saltc 
foreign key (cmpd_known_salt_fk) 
references cmpd_known_salt;

alter table cmpd_fragment 
add constraint cmpd_fragment_cmpd_fragment_sc 
foreign key (cmpd_fragment_structure_fk) 
references cmpd_fragment_structure;

alter table cmpd_fragment 
add constraint cmpd_fragment_cmpd_fragment_pc 
foreign key (cmpd_fragment_p_chem_fk) 
references cmpd_fragment_p_chem;

alter table nsc_cmpd 
add constraint nsc_cmpd_cmpd_parent_fragmentc 
foreign key (cmpd_parent_fragment_fk) 
references cmpd_fragment;

-- update parents ONLY WORKS since all ss have one and only one fragment

update nsc_cmpd 
set cmpd_parent_fragment_fk = id;

vacuum analyze verbose;

--test

--743380 OCc1cn(Cc2cc(Cl)ccc2)c2c1cccc2

--TESTOSTERONE TESTS

select nsc_cmpd.id, nsc_cmpd.nsc, strc.mol
from cmpd_fragment_structure strc, nsc_cmpd
where strc.id = nsc_cmpd.id
and strc.mol @= (
 select mol from cmpd_fragment_structure where id in (
 select id from nsc_cmpd where nsc = 50917
 )
);

   id   |   nsc   |                             mol                              
--------+---------+--------------------------------------------------------------
 576217 |  755838 | C[C@]12CCC3C(CCC4=CC(=O)CC[C@@]43C)C1CC[C@@H]2O
 603557 |   50917 | C[C@@]12CC[C@@H]3[C@H](CCC4=CC(=O)CC[C@]43C)[C@H]1CC[C@H]2O
 273801 |   26499 | C[C@]12CC[C@H]3[C@@H](CCC4=CC(=O)CC[C@@]43C)[C@@H]1CC[C@H]2O
 459296 | 2509410 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
  98449 |  523833 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
  34297 |    9700 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
(6 rows)

Time: 171.444 ms

-- any nsc

select nsc_cmpd.id, nsc_cmpd.nsc, strc.mol 
from cmpd_fragment_structure strc, nsc_cmpd
where strc.id = nsc_cmpd.id
and strc.mol @> (
 select mol from cmpd_fragment_structure where id in (
 select id from nsc_cmpd where nsc = 743380
 )
);

   id   |  nsc   |                   mol                    
--------+--------+------------------------------------------
 372100 | 372811 | O=C(O)c1c(Cl)n(Cc2cc(Cl)ccc2Cl)c2ccccc21
 415455 | 743380 | OCc1cn(Cc2cccc(Cl)c2)c2ccccc12
 433386 | 745858 | O=C(O)c1cn(Cc2ccc(Cl)c(Cl)c2)c2ccccc12
 444078 | 756864 | OCc1c(Cl)n(Cc2cccc(Cl)c2)c2ccccc12
 444218 | 751172 | O=C(O)c1cn(Cc2cccc(Cl)c2)c2ccccc12
 450764 | 756850 | Cc1c(CO)c2ccccc2n1Cc1cccc(Cl)c1
(6 rows)

Time: 51.256 ms


