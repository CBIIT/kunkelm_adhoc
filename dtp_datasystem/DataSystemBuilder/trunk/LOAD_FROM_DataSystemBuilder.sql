-- after the data build from prod
-- and running RDKit.sql

-- from dtp_datasystem/uml/datasystem/web/target
-- schema-drop.sql
-- schema-create.sql

drop table if exists rdkit_mol;

create table rdkit_mol
as
select frag_id as id, nsc, frag_mol as mol
from parent_with_salt;

create index rdkit_mol_mol on rdkit_mol using gist(mol);

vacuum analyze verbose rdkit_mol;

-- set id = nsc

--            table "public.parent_with_salt"
--     column      |          type           | modifiers 
-------------------+-------------------------+-----------
-- nsc             | integer                 | 
-- frag_id         | integer                 | 
-- frag_mol        | mol                     | 
-- mf              | character varying(1024) | 
-- ctab            | text                    | 
-- smiles          | character varying(2048) | 
-- mw              | real                    | 
-- hba             | integer                 | 
-- hbd             | integer                 | 
-- count_atoms     | integer                 | 
-- count_heavy     | integer                 | 
-- count_rings     | integer                 | 
-- tpsa            | real                    | 
-- inchi           | text                    | 
-- known_salt      | boolean                 | 
-- known_salt_name | character varying(1024) | 
-- matched_salt    | character varying       | 
--indexes:
--    "parent_with_salt_inchi" btree (inchi)
--    "parent_with_salt_matched_salt" btree (matched_salt)
--

insert into cmpd_fragment_p_chem(id, mw, mf, hba, hbd, psa)
select nsc, mw, mf, hba, hbd, tpsa
from parent_with_salt;

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
select nsc, smiles, inchi, frag_mol, ctab
from parent_with_salt;

--index for substructure searches
create index cmpd_fragment_structure_mol on cmpd_fragment_structure using gist(mol);

vacuum analyze verbose cmpd_fragment_structure;

create table prod_bio_counts(
nsc int,
prod_count_nci60 int,
prod_count_hf int,
prod_count_xeno int
);

\copy prod_bio_counts from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/prod_bio_counts.csv csv header


insert into cmpd_bio_assay(id, nci60, hf, xeno)
select nsc, prod_count_nci60, prod_count_hf, prod_count_xeno 
from prod_bio_counts 
where nsc in (select nsc from parent_with_salt);

-- cmpd is an abstract class => single attribute id

insert into cmpd(id)
select nsc
from parent_with_salt;

-- drop and recreate nsc_cmpd and cmpd_fragment so that constraints are not in the way during inserts

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
        salt_name character varying(1024),
        salt_smiles character varying(1024),
        salt_id bigint,
        cmpd_parent_fragment_fk bigint unique,
        cmpd_bio_assay_fk bigint unique,
        primary key (id)
    );

drop table cmpd_fragment cascade;

create table cmpd_fragment (
id bigint not null,
cmpd_fragment_structure_fk bigint unique,
cmpd_known_salt_fk bigint,
cmpd_fragment_p_chem_fk bigint unique,
nsc_cmpd_fk bigint,
primary key (id)
);

-- use intermediate (temp) table
-- joins MUCH FASTER here than updating

drop table if exists temp;

create table temp as 
select pws.nsc as id, 'NSC'||pws.nsc as name, pws.nsc as nsc_cmpd_id, 's'::varchar(4) as prefix, pws.nsc, src.conf, src.cas, src.distribution_code as distribution, pws.nsc as cmpd_parent_fragment_fk, bio.id as cmpd_bio_assay_fk, matched_salt as salt_name, salt_smiles 
from parent_with_salt pws 
left outer join cmpd_bio_assay bio on pws.nsc = bio.id
left outer join prod_src src on pws.nsc = src.nsc;

insert into nsc_cmpd(id, name, nsc_cmpd_id, prefix, nsc, conf, cas, distribution, cmpd_parent_fragment_fk, cmpd_bio_assay_fk, salt_name, salt_smiles)
select id, name, nsc_cmpd_id, prefix, nsc, conf, cas, distribution, cmpd_parent_fragment_fk, cmpd_bio_assay_fk, salt_name, salt_smiles 
from temp;

insert into cmpd_fragment(id, nsc_cmpd_fk, cmpd_fragment_p_chem_fk, cmpd_fragment_structure_fk) 
select nsc, nsc, nsc, nsc 
from parent_with_salt;

--recreate constraints

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

--743380 occ1cn(cc2cc(cl)ccc2)c2c1cccc2

--testosterone tests

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
576217 |  755838 | c[c@]12ccc3c(ccc4=cc(=o)cc[c@@]43c)c1cc[c@@h]2o
603557 |   50917 | c[c@@]12cc[c@@h]3[c@h](ccc4=cc(=o)cc[c@]43c)[c@h]1cc[c@h]2o
273801 |   26499 | c[c@]12cc[c@h]3[c@@h](ccc4=cc(=o)cc[c@@]43c)[c@@h]1cc[c@h]2o
459296 | 2509410 | cc12ccc3c(ccc4=cc(=o)ccc43c)c1ccc2o
98449 |  523833 | cc12ccc3c(ccc4=cc(=o)ccc43c)c1ccc2o
34297 |    9700 | cc12ccc3c(ccc4=cc(=o)ccc43c)c1ccc2o
(6 rows)

time: 171.444 ms

select nsc_cmpd.nsc, rdkit.mol
from rdkit_mol rdkit, nsc_cmpd
where rdkit.nsc = nsc_cmpd.nsc
and rdkit.mol @= (
select mol from rdkit_mol where nsc = 50917
)
order by nsc;

  nsc   |                             mol                              
--------+--------------------------------------------------------------
   9700 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
  26499 | C[C@]12CC[C@H]3[C@@H](CCC4=CC(=O)CC[C@@]43C)[C@@H]1CC[C@H]2O
  50917 | C[C@@]12CC[C@@H]3[C@H](CCC4=CC(=O)CC[C@]43C)[C@H]1CC[C@H]2O
 523833 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
 755838 | C[C@]12CCC3C(CCC4=CC(=O)CC[C@@]43C)C1CC[C@H]2O
(5 rows)

Time: 81.926 ms

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
372100 | 372811 | o=c(o)c1c(cl)n(cc2cc(cl)ccc2cl)c2ccccc21
415455 | 743380 | occ1cn(cc2cccc(cl)c2)c2ccccc12
433386 | 745858 | o=c(o)c1cn(cc2ccc(cl)c(cl)c2)c2ccccc12
444078 | 756864 | occ1c(cl)n(cc2cccc(cl)c2)c2ccccc12
444218 | 751172 | o=c(o)c1cn(cc2cccc(cl)c2)c2ccccc12
450764 | 756850 | cc1c(co)c2ccccc2n1cc1cccc(cl)c1
(6 rows)

time: 51.256 ms


