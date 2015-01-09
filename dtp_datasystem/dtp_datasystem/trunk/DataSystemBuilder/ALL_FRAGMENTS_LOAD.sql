-- load ANY/ALL nsc

-- after the data build from prod
-- and running Script1_RDKit.sql

-- from PROJECTS/CURRENT/dtp_datasystem/datasystemuml/web/target:

-- schema-drop.sql
-- schema-create.sql

insert into cmpd_fragment_p_chem(id, mw, mf, hba, hbd, psa)
select frag_id, mw, mf, hba, hbd, tpsa
from rdkit_mol_frag;

-- have to bump up the field size to 2048
-- change uml model to use text datatype
drop table cmpd_fragment_structure cascade;

create table cmpd_fragment_structure(
id bigint, 
smiles varchar(2048), 
inchi varchar(2048), 
mol mol, 
inchi_aux varchar(2048), 
ctab text, 
primary key (id));

insert into cmpd_fragment_structure(id, smiles, inchi, mol, ctab)
select frag_id, smiles, inchi, frag_mol, ctab
from rdkit_mol_frag;

--index for substructure searches
create index cmpd_fragment_structure_mol on cmpd_fragment_structure using gist(mol);

vacuum analyze verbose cmpd_fragment_structure;

-- biodata

create table prod_bio_counts 
as 
select 
prod_src.nsc, 
prod_count_nci60.the_count as prod_count_nci60, 
prod_count_hf.the_count as prod_count_hf, 
prod_count_xeno.the_count as prod_count_xeno 
from prod_src 
left outer join prod_count_nci60 on prod_src.nsc = prod_count_nci60.nsc 
left outer join prod_count_hf on prod_src.nsc = prod_count_hf.nsc 
left outer join prod_count_xeno on prod_src.nsc = prod_count_xeno.nsc;

insert into cmpd_bio_assay(id, nci60, hf, xeno)
select nsc, prod_count_nci60, prod_count_hf, prod_count_xeno 
from prod_bio_counts; 

-- inventory

--       Table "public.cmpd_inventory"
--   Column    |       Type       | Modifiers 
---------------+------------------+-----------
-- id          | bigint           | not null
-- inventory   | double precision | 
--

insert into cmpd_inventory(id, inventory)
select nsc, inventory 
from prod_inventory;

-- cmpd is an abstract class => single attribute id

insert into cmpd(id)
select nsc
from prod_src;

-- drop and recreate nsc_cmpd and cmpd_fragment so that constraints are not in the way during inserts
-- increase the capacity of pseudo_atoms from 1024 to 4096

drop table nsc_cmpd cascade;

create table nsc_cmpd (
id bigint not null,
name character varying(1024),
nsc_cmpd_id bigint unique,
prefix character varying(1024) not null,
nsc integer unique,
conf character varying(1024) not null,
distribution character varying(1024) not null,
cas character varying(1024),
pseudo_atoms character varying(1024),
mtxt character varying(1024),
salt_name character varying(1024),
salt_smiles character varying(1024),
salt_id bigint,
count_fragments integer not null,
cmpd_parent_fragment_fk bigint,
cmpd_bio_assay_fk bigint,
cmpd_inventory_fk bigint,
primary key (id)
);

drop table cmpd_fragment cascade;

create table cmpd_fragment (
id bigint not null,
cmpd_fragment_structure_fk bigint,
cmpd_known_salt_fk bigint,
cmpd_fragment_p_chem_fk bigint,
nsc_cmpd_fk bigint,
primary key (id)
);

drop table if exists count_fragments;

create table count_fragments
as
select nsc, count(*) from rdkit_mol_frag
group by nsc;

drop table if exists concat_pseudoatoms;

create table concat_pseudoatoms
as
select nsc, string_agg(atom_label, ' ')
from mol_frag_pseudo_atom
group by nsc;


-- use intermediate (temp) table
-- joins MUCH FASTER here than updating

drop table if exists temp;

create table temp 
as 
select src.nsc as id, 
'NSC'||src.nsc as name, 
src.nsc as nsc_cmpd_id, 
'S'::varchar(4) as prefix, 
src.nsc, 
src.conf, 
src.cas, 
src.distribution_code as distribution, 
pws.frag_id as cmpd_parent_fragment_fk, 
bio.id as cmpd_bio_assay_fk, 
inv.id as cmpd_inventory_fk, 
pws.matched_salt as salt_name, 
pws.salt_smiles,
cf."count" as count_fragments,
pseud."string_agg" as pseudo_atoms,
mt.mtxt 

from prod_src src
 
left outer join cmpd_bio_assay bio on src.nsc = bio.id
left outer join cmpd_inventory inv on src.nsc = inv.id
left outer join parent_with_salt pws on pws.nsc = src.nsc

left outer join count_fragments cf on src.nsc = cf.nsc
left outer join concat_pseudoatoms pseud on src.nsc = pseud.nsc

left outer join prod_mtxt mt on src.nsc = mt.nsc;

-------------------------------------
-------------------------------------
-------------------------------------
-------------------------------------
-------------------------------------

insert into nsc_cmpd(id,
name,
nsc_cmpd_id,
prefix,
nsc,
conf,
cas,
distribution,
cmpd_parent_fragment_fk,
cmpd_bio_assay_fk,
cmpd_inventory_fk,
salt_name,
salt_smiles,
count_fragments,
pseudo_atoms,
mtxt
)
select id,
name,
nsc_cmpd_id,
prefix,
nsc,
conf,
cas,
distribution,
null,
cmpd_bio_assay_fk,
cmpd_inventory_fk,
salt_name,
salt_smiles,
count_fragments,
pseudo_atoms,
mtxt
from temp;

insert into cmpd_fragment(id, nsc_cmpd_fk, cmpd_fragment_p_chem_fk, cmpd_fragment_structure_fk) 
select frag_id, nsc, frag_id, frag_id 
from rdkit_mol_frag;

-- update the parent_fragment

update nsc_cmpd 
set cmpd_parent_fragment_fk = temp.cmpd_parent_fragment_fk
from temp
where nsc_cmpd.nsc = temp.nsc;

--recreate constraints

alter table NSC_CMPD 
add constraint NSC_CMPD_CMPD_INVENTORY_FKC 
foreign key (CMPD_INVENTORY_FK) 
references CMPD_INVENTORY;

alter table NSC_CMPD 
add constraint NSC_CMPDIFKC 
foreign key (ID) 
references CMPD;

alter table NSC_CMPD 
add constraint NSC_CMPD_CMPD_PARENT_FRAGMENTC 
foreign key (CMPD_PARENT_FRAGMENT_FK) 
references CMPD_FRAGMENT;

alter table NSC_CMPD 
add constraint NSC_CMPD_CMPD_BIO_ASSAY_FKC 
foreign key (CMPD_BIO_ASSAY_FK) 
references CMPD_BIO_ASSAY;

alter table nsc_cmpd add constraint cpf_u unique (cmpd_parent_fragment_fk);
alter table nsc_cmpd add constraint cba_u unique (cmpd_bio_assay_fk);
alter table nsc_cmpd add constraint ci_u unique (cmpd_inventory_fk);

alter table CMPD_FRAGMENT 
add constraint CMPD_FRAGMENT_NSC_CMPD_FKC 
foreign key (NSC_CMPD_FK) 
references NSC_CMPD;

alter table CMPD_FRAGMENT 
add constraint CMPD_FRAGMENT_CMPD_KNOWN_SALTC 
foreign key (CMPD_KNOWN_SALT_FK) 
references CMPD_KNOWN_SALT;

alter table CMPD_FRAGMENT 
add constraint CMPD_FRAGMENT_CMPD_FRAGMENT_SC 
foreign key (CMPD_FRAGMENT_STRUCTURE_FK) 
references CMPD_FRAGMENT_STRUCTURE;

alter table CMPD_FRAGMENT 
add constraint CMPD_FRAGMENT_CMPD_FRAGMENT_PC 
foreign key (CMPD_FRAGMENT_P_CHEM_FK) 
references CMPD_FRAGMENT_P_CHEM;

alter table cmpd_fragment add constraint cfs_u unique (cmpd_fragment_structure_fk);
alter table cmpd_fragment add constraint cfp_s unique (cmpd_fragment_p_chem_fk);


--
--
--
--
--

-- aliases, projects, sets, plates
-- potential one-to-many by compound

-- alias_types

drop table if exists temp;

create table temp
as
select distinct chem_name_type
from prod_chem_name;

insert into cmpd_alias_type(id, alias_type)
select nextval('cmpd_alias_type_seq'), chem_name_type
from temp;

-- chem_name is NOT unique because of one-to-many from name to chem_name_type 

drop table if exists temp;

create table temp
as
select nextval('cmpd_alias_seq') as id, cn.nsc, n.id as nsc_cmpds_fk, chem_name, chem_name_type, t.id as cmpd_alias_type_fk
from prod_chem_name cn, cmpd_alias_type t, nsc_cmpd n
where cn.chem_name_type = t.alias_type
and cn.nsc = n.nsc;

insert into cmpd_alias(id, alias, cmpd_alias_type_fk)
select id, chem_name, cmpd_alias_type_fk 
from temp;

insert into cmpd_aliases2nsc_cmpds(nsc_cmpds_fk, cmpd_aliases_fk)
select nsc_cmpds_fk, id
from temp;

-- projects

--            Table "public.cmpd_project"
--    Column    |          Type           | Modifiers 
----------------+-------------------------+-----------
-- id           | bigint                  | not null
-- project_code | character varying(1024) | not null
-- project_name | character varying(1024) | not null

drop table if exists temp;

create table temp
as
select distinct project_code, description
from prod_projects;

insert into cmpd_project(id, project_code, project_name)
select nextval('cmpd_project_seq'), project_code, description
from temp;

drop table if exists temp;

create table temp
as
select prod.nsc, prod.project_code, nsc.id as nsc_cmpds_fk, proj.id as cmpd_projects_fk
from prod_projects prod, nsc_cmpd nsc, cmpd_project proj
where prod.nsc = nsc.nsc
and prod.project_code = proj.project_code;

insert into cmpd_projects2nsc_cmpds(nsc_cmpds_fk, cmpd_projects_fk)
select nsc_cmpds_fk, cmpd_projects_fk
from temp;


-- plates

drop table if exists temp;

create table temp
as
select distinct plate_set
from prod_plated_sets
where plate_set is not null;

insert into cmpd_plate(id, plate_name)
select nextval('cmpd_plate_seq'), plate_set
from temp;

drop table if exists temp;

create table temp
as
select pps.nsc, pps.plate_set, n.id as nsc_cmpds_fk, p.id as cmpd_plates_fk
from prod_plated_sets pps, nsc_cmpd n, cmpd_plate p
where pps.nsc = n.nsc
and pps.plate_set = p.plate_name;

insert into cmpd_plates2nsc_cmpds(nsc_cmpds_fk, cmpd_plates_fk)
select nsc_cmpds_fk,cmpd_plates_fk
from temp;

















vacuum analyze verbose;


alter table ad_hoc_cmpd owner to dctd_user;
alter table ad_hoc_cmpd_fragment owner to dctd_user;
alter table ad_hoc_cmpd_fragment_p_chem owner to dctd_user;
alter table ad_hoc_cmpd_fragment_structure owner to dctd_user;
alter table cmpd owner to dctd_user;
alter table cmpd_alias owner to dctd_user;
alter table cmpd_aliases2nsc_cmpds owner to dctd_user;
alter table cmpd_alias_type owner to dctd_user;
alter table cmpd_bio_assay owner to dctd_user;
alter table cmpd_fragment owner to dctd_user;
alter table cmpd_fragment_p_chem owner to dctd_user;
alter table cmpd_fragment_structure owner to dctd_user;
alter table cmpd_inventory owner to dctd_user;
alter table cmpd_known_salt owner to dctd_user;
alter table cmpd_list owner to dctd_user;
alter table cmpd_list_member owner to dctd_user;
alter table cmpd_plate owner to dctd_user;
alter table cmpd_plates2nsc_cmpds owner to dctd_user;
alter table cmpd_project owner to dctd_user;
alter table cmpd_projects2nsc_cmpds owner to dctd_user;
alter table cmpd_pub_chem_sid owner to dctd_user;
alter table cmpd_pub_chem_sids2nsc_cmpds owner to dctd_user;
alter table cmpd_related owner to dctd_user;
alter table cmpd_relation_type owner to dctd_user;
alter table cmpd_set owner to dctd_user;
alter table cmpd_sets2nsc_cmpds owner to dctd_user;
alter table cmpd_target owner to dctd_user;
alter table cmpd_targets2nsc_cmpds owner to dctd_user;
alter table cmpd_view owner to dctd_user;
alter table nsc_cmpd owner to dctd_user;
alter table rdkit_mol owner to dctd_user;

alter sequence ad_hoc_cmpd_fragment_p_che_seq owner to dctd_user; 
alter sequence ad_hoc_cmpd_fragment_seq owner to dctd_user;       
alter sequence ad_hoc_cmpd_fragment_struc_seq owner to dctd_user; 
alter sequence cmpd_alias_seq owner to dctd_user;                 
alter sequence cmpd_alias_type_seq owner to dctd_user;            
alter sequence cmpd_bio_assay_seq owner to dctd_user;             
alter sequence cmpd_fragment_p_chem_seq owner to dctd_user;       
alter sequence cmpd_fragment_seq owner to dctd_user;              
alter sequence cmpd_fragment_structure_seq owner to dctd_user;    
alter sequence cmpd_inventory_seq owner to dctd_user;             
alter sequence cmpd_known_salt_seq owner to dctd_user;            
alter sequence cmpd_list_member_seq owner to dctd_user;           
alter sequence cmpd_list_seq owner to dctd_user;                  
alter sequence cmpd_plate_seq owner to dctd_user;                 
alter sequence cmpd_project_seq owner to dctd_user;               
alter sequence cmpd_pub_chem_sid_seq owner to dctd_user;          
alter sequence cmpd_related_seq owner to dctd_user;               
alter sequence cmpd_relation_type_seq owner to dctd_user;         
alter sequence cmpd_seq owner to dctd_user;                       
alter sequence cmpd_set_seq owner to dctd_user;                   
alter sequence cmpd_target_seq owner to dctd_user;                
alter sequence hibernate_sequence owner to dctd_user;             



--test
--
----743380 occ1cn(cc2cc(cl)ccc2)c2c1cccc2
--
----testosterone tests
--
select nsc_cmpd.id, nsc_cmpd.nsc, strc.mol
from cmpd_fragment_structure strc, nsc_cmpd
where strc.id = nsc_cmpd.id
and strc.mol @= (
select mol from cmpd_fragment_structure where id in (
select id from nsc_cmpd where nsc = 50917
)
);
--
--id   |   nsc   |                             mol                              
----------+---------+--------------------------------------------------------------
--576217 |  755838 | c[c@]12ccc3c(ccc4=cc(=o)cc[c@@]43c)c1cc[c@@h]2o
--603557 |   50917 | c[c@@]12cc[c@@h]3[c@h](ccc4=cc(=o)cc[c@]43c)[c@h]1cc[c@h]2o
--273801 |   26499 | c[c@]12cc[c@h]3[c@@h](ccc4=cc(=o)cc[c@@]43c)[c@@h]1cc[c@h]2o
--459296 | 2509410 | cc12ccc3c(ccc4=cc(=o)ccc43c)c1ccc2o
--98449 |  523833 | cc12ccc3c(ccc4=cc(=o)ccc43c)c1ccc2o
--34297 |    9700 | cc12ccc3c(ccc4=cc(=o)ccc43c)c1ccc2o
--(6 rows)
--
--time: 171.444 ms
--
--select nsc_cmpd.nsc, rdkit.mol
--from rdkit_mol rdkit, nsc_cmpd
--where rdkit.nsc = nsc_cmpd.nsc
--and rdkit.mol @= (
--select mol from rdkit_mol where nsc = 50917
--)
--order by nsc;
--
--  nsc   |                             mol                              
----------+--------------------------------------------------------------
--   9700 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
--  26499 | C[C@]12CC[C@H]3[C@@H](CCC4=CC(=O)CC[C@@]43C)[C@@H]1CC[C@H]2O
--  50917 | C[C@@]12CC[C@@H]3[C@H](CCC4=CC(=O)CC[C@]43C)[C@H]1CC[C@H]2O
-- 523833 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
-- 755838 | C[C@]12CCC3C(CCC4=CC(=O)CC[C@@]43C)C1CC[C@H]2O
--(5 rows)
--
--Time: 81.926 ms
--
---- any nsc
--
--select nsc_cmpd.id, nsc_cmpd.nsc, strc.mol 
--from cmpd_fragment_structure strc, nsc_cmpd
--where strc.id = nsc_cmpd.id
--and strc.mol @> (
--select mol from cmpd_fragment_structure where id in (
--select id from nsc_cmpd where nsc = 743380
--)
--);
--
--id   |  nsc   |                   mol                    
----------+--------+------------------------------------------
--372100 | 372811 | o=c(o)c1c(cl)n(cc2cc(cl)ccc2cl)c2ccccc21
--415455 | 743380 | occ1cn(cc2cccc(cl)c2)c2ccccc12
--433386 | 745858 | o=c(o)c1cn(cc2ccc(cl)c(cl)c2)c2ccccc12
--444078 | 756864 | occ1c(cl)n(cc2cccc(cl)c2)c2ccccc12
--444218 | 751172 | o=c(o)c1cn(cc2cccc(cl)c2)c2ccccc12
--450764 | 756850 | cc1c(co)c2ccccc2n1cc1cccc(cl)c1
--(6 rows)
--
--time: 51.256 ms
--
--
