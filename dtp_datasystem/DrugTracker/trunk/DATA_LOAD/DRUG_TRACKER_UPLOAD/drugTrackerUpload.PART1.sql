truncate acquisition cascade;
truncate drug_tracker cascade;
truncate nsc_drug_tracker cascade;
truncate ad_hoc_drug_tracker cascade;
truncate aliases2drug_trackers cascade;
truncate alias cascade;
truncate drug_trackers2targets cascade;
truncate target cascade;
truncate drug_tracker cascade;
truncate drug_status cascade;
truncate drug_tracker_plate cascade;
truncate nsc_novum_list_member cascade;
truncate novum_list_member cascade;
truncate novum_list cascade;

--THIS ONLY NEEDS DOING IF STANDARDIZED_SMILES has been regenerated in PLP


--drop table if exists standardized_smiles_plp;
--
--create table standardized_smiles_plp(
--NAME varchar(1024),
--NSC int,
--STRUCTURE_SOURCE varchar(1024),
--SMILES_FROM_PROD varchar(1024),
--PROD_CAN_SMI varchar(1024),
--REMOVEDSALTS varchar(1024),
--PARENT_CAN_SMI varchar(1024),
--TAUT_CAN_SMI varchar(1024),
--TAUT_NOSTEREO_CAN_SMI varchar(1024),
--XENO int,
--HF int,
--NCI60 int,
--DISTRIBUTION varchar(1024),
--CONF varchar(1024),
--INVENTORY double precision,
--MW double precision,
--MF varchar(1024),
--ALOGP double precision,
--LOGD double precision,
--HBA int,
--HBD int,
--SA double precision,
--PSA double precision,
--CAS int, 
--COUNT_RELATED int,
--RELATED varchar(1024),
--NSC_MOL_FK int
--);
--
--\copy standardized_smiles_plp from /home/mwkunkel/STANDARDIZED_SMILES.csv csv header quote as '"'
--

drop sequence if exists standardized_smiles_seq;

create sequence standardized_smiles_seq;

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

--
----drop and recreate rdkit_mol for real data type of mol instead of androMDA model String
--
--drop table if exists rdkit_mol;
--
--create table RDKIT_MOL (
--ID BIGINT not null,
--NSC INTEGER,
--MOL mol not null,
--primary key (ID)
--);
--
insert into rdkit_mol(id, nsc, mol) 
select 
id, nsc, mol_from_smiles(parent_can_smi::cstring)
from standardized_smiles
where is_valid_smiles(parent_can_smi::cstring);

--
--create index rdkit_mol_nsc on rdkit_mol(nsc);
--create index rdkit_mol_mol_idx on rdkit_mol using gist(mol);
--
--drop table if exists STANDARDIZED_SMILES cascade;
--
--create table STANDARDIZED_SMILES (
--        ID BIGINT not null,
--        NAME CHARACTER VARYING(1024),
--        NSC INTEGER,
--        STRUCTURE_SOURCE CHARACTER VARYING(1024),
--        SMILES_FROM_PROD CHARACTER VARYING(1024),
--        PROD_CAN_SMI CHARACTER VARYING(1024),
--        REMOVEDSALTS CHARACTER VARYING(1024),
--        PARENT_CAN_SMI CHARACTER VARYING(1024),
--        TAUT_CAN_SMI CHARACTER VARYING(1024),
--        TAUT_NOSTEREO_CAN_SMI CHARACTER VARYING(1024),
--        XENO INTEGER,
--        HF INTEGER,
--        NCI60 INTEGER,
--        DISTRIBUTION CHARACTER VARYING(1024),
--        CONF CHARACTER VARYING(1024),
--        INVENTORY DOUBLE PRECISION,
--        MW DOUBLE PRECISION,
--        MF CHARACTER VARYING(1024),
--        ALOGP DOUBLE PRECISION,
--        LOGD DOUBLE PRECISION,
--        HBA INTEGER,
--        HBD INTEGER,
--        SA DOUBLE PRECISION,
--        PSA DOUBLE PRECISION,
--        CAS CHARACTER VARYING(1024),
--        PREFIX CHARACTER VARYING(1024) not null,
--        COUNT_RELATED INTEGER,
--        RELATED CHARACTER VARYING(1024),
--        RDKIT_MOL_FK BIGINT unique,
--        NSC_MOL_FK BIGINT unique,
--        primary key (ID)
--    );
--
--drop sequence if exists standardized_smiles_seq;
--
--create sequence standardized_smiles_seq;
--    
--insert into standardized_smiles(
--id,name,nsc,structure_source,smiles_from_prod,prod_can_smi,removedsalts,parent_can_smi,taut_can_smi,taut_nostereo_can_smi,xeno,hf,nci60,distribution,conf,inventory,mw,mf,alogp,logd,hba,hbd,sa,psa,cas,prefix,count_related,related,rdkit_mol_fk)
--select nextval('standardized_smiles_seq'),
--name,ss.nsc,structure_source,smiles_from_prod,prod_can_smi,removedsalts,parent_can_smi,taut_can_smi,taut_nostereo_can_smi,xeno,hf,nci60,distribution,conf,inventory,mw,mf,alogp,logd,hba,hbd,sa,psa,cas,'S',count_related,related,rm.id
--from STANDARDIZED_SMILES_PLP ss
--left outer join rdkit_mol rm on ss.nsc = rm.nsc;
--
--create index std_smi_nsc_idx on STANDARDIZED_SMILES (NSC);
--
--create index std_smi_cas_idx on STANDARDIZED_SMILES (CAS);
--
--    alter table STANDARDIZED_SMILES 
--        add constraint STANDARDIZED_SMILES_RDKIT_MOLC 
--        foreign key (RDKIT_MOL_FK) 
--        references RDKIT_MOL;
--
----    alter table STANDARDIZED_SMILES 
--        add constraint STANDARDIZED_SMILES_NSC_MOL_FC 
--        foreign key (NSC_MOL_FK) 
--        references NSC_MOL; 
        
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--upload the APPROVED and the INVESTIGATIONAL data files
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

--upload the APPROVED and the INVESTIGATIONAL data files

drop table if exists loading_approved;

create table loading_approved(
agent varchar(1024),
alias varchar(1024),
originator varchar(1024),
nsc int,
cas varchar(1024),
target varchar(1024),
approvedorinvestigat varchar(1024),
onapprovedplate varchar(1024)
);

--modify line 60 of input file to remove trailing weirdness in RP-54780�
--modify line 60 of input file to single nsc 754363; 761388

--  STILL NEED TO TRIM ws !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!

\copy loading_approved from '/home/mwkunkel//PROJECTS/ACTIVE/DIS/novumservices/DATA_LOAD/DRUG_TRACKER_UPLOAD/APPROVED_20FEB2013.csv' csv header quote as '"'


update loading_approved set approvedorinvestigat = 'APPROVED';
update loading_approved set onapprovedplate = 'APPROVED' where upper(onapprovedplate) = 'YES';
update loading_approved set onapprovedplate = 'APPROVED' where upper(onapprovedplate) = 'YES';
update loading_approved set onapprovedplate = 'NOT PLATED' where upper(onapprovedplate) = 'NO';

drop table if exists loading_investigational;

create table loading_investigational(
agent varchar(1024),
alias varchar(1024),
originator varchar(1024),
nsc int,
cas varchar(1024),
target varchar(1024),
purchorsynth varchar(1024),
contractor varchar(1024),
amount double precision,
assignedororderedordelivered varchar(1024), 
nci60 varchar(1024),
acq_date varchar(1024),
approvedorinvestigat varchar(1024),
onplate varchar(1024),
smiles varchar(1024)
);

--have to open the xls file and save the content to a csv file

\copy loading_investigational from '/home/mwkunkel//PROJECTS/ACTIVE/DIS/novumservices/DATA_LOAD/DRUG_TRACKER_UPLOAD/INVESTIGATIONAL_20FEB2013.csv' csv header quote as '"'

--ERROR:  invalid input syntax for type double precision: "1 g"
--CONTEXT:  COPY loading_investigational, line 213, column amount: "1 g"	MWK to 1

--ERROR:  invalid input syntax for type double precision: "1-5 g"
--CONTEXT:  COPY loading_investigational, line 279, column amount: "1-5 g"   MWK to 1.5

--ERROR:  invalid input syntax for type double precision: "8,5"
--CONTEXT:  COPY loading_investigational, line 312, column amount: "8,5"	MWK to 8.5

--ERROR:  invalid input syntax for type double precision: "10 g "
--CONTEXT:  COPY loading_investigational, line 319, column amount: "10 g "  MWK to 10

--ERROR:  invalid input syntax for type double precision: "1 g "
--CONTEXT:  COPY loading_investigational, line 323, column amount: "1 g "

--first entry has no plate indicated
--set to No for now

update loading_investigational set onplate = 'NO' where onplate is null;
update loading_investigational set onplate = 'NO' where onplate = 'N0';
update loading_investigational set onplate = 'NO' where onplate = 'No';
update loading_investigational set onplate = 'YES' where onplate = 'Yes';

--trim onplate entries

update loading_investigational set onplate = trim(both ' ' from onplate);


update loading_investigational set approvedorinvestigat = 'INVESTIGATIONAL';
update loading_investigational set onplate = 'INVESTIGATIONAL' where upper(onplate) = 'YES';
update loading_investigational set onplate = 'NOT PLATED' where upper(onplate) = 'NO';

--               Table "public.loading_approved"
--         Column         |          Type           | Modifiers 
--------------------------+-------------------------+-----------
-- agent                  | character varying(1024) | 
-- alias                  | character varying(1024) | 
-- originator             | character varying(1024) | 
-- nsc                    | integer                 | 
-- cas                    | character varying(1024) | 
-- target                 | character varying(1024) | 
-- purchorsynth           | character varying(1024) | 
-- approvedorinvestigat   | character varying(1024) | 
-- onapprovedplate        | character varying(1024) | 
-- oninvestigationalplate | character varying(1024) | 
--
--               Table "public.loading_investigational"
--            Column            |          Type           | Modifiers 
--------------------------------+-------------------------+-----------
-- agent                        | character varying(1024) | 
-- alias                        | character varying(1024) | 
-- originator                   | character varying(1024) | 
-- nsc                          | integer                 | 
-- cas                          | character varying(1024) | 
-- target                       | character varying(1024) | 
-- purchorsynth                 | character varying(1024) | 
-- contractor                   | character varying(1024) | 
-- amount                       | double precision        | 
-- assignedororderedordelivered | character varying(1024) | 
-- nci60                        | character varying(1024) | 
-- acq_date                     | character varying(1024) | 
-- approvedorinvestigat         | character varying(1024) | 
-- onplate                      | character varying(1024) | 
-- smiles                       | character varying(1024) | 

drop sequence if exists loading_seq;

create sequence loading_seq;

drop table if exists loading;

create table loading as select nextval('loading_seq') as "id", loading_investigational.* from loading_investigational;

insert into loading (id, agent, alias, originator, nsc, cas, target, approvedorinvestigat, onplate)
select nextval('loading_seq') , agent, alias, originator, nsc, cas, target, approvedorinvestigat, onapprovedplate
from loading_approved;

--update loading set purchorsynth = 'PURCHASE' where purchorsynth = 'Purchase';
--update loading set purchorsynth = 'SYNTHESIS' where purchorsynth = 'Synthesis';
--update loading set purchorsynth = 'SYNTHESIS' where purchorsynth = 'Synthesis; Purchase';
--update loading set purchorsynth = 'NOT SPECIFIED' where (purchorsynth not in ('PURCHASE','SYNTHESIS') or purchorsynth is null);
--
--update loading set assignedororderedordelivered = 'ON ORDER' where assignedororderedordelivered = 'On Order';
--update loading set assignedororderedordelivered = 'IN PROCESS' where assignedororderedordelivered = 'In Process';
--update loading set assignedororderedordelivered = 'IN PROCESS' where assignedororderedordelivered = 'In Progress';
--update loading set assignedororderedordelivered = 'DELIVERED' where assignedororderedordelivered = 'Delivered';
--update loading set assignedororderedordelivered = 'DELIVERED' where assignedororderedordelivered = 'Deliverd';
--update loading set assignedororderedordelivered = 'AVAILABLE' where assignedororderedordelivered = 'Available';
--update loading set assignedororderedordelivered = 'NOT SPECIFIED' where assignedororderedordelivered not in ('ON ORDER','IN PROCESS','DELIVERED','AVAILABLE') or assignedororderedordelivered is null;

update loading set agent = trim(both ' ' from agent);
update loading set originator = trim(both ' ' from originator);
update loading set contractor = trim(both ' ' from contractor);

--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--run upload.PL to extract and separate the content of the alias and target columns since they are degnerate
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx


