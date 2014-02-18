
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

\copy standardized_smiles_plp from /home/mwkunkel/PROJECTS/ACTIVE/DIS/DrugTracker/DATA_LOAD/STANDARDIZED_SMILES.csv csv header quote as '"'

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

--alter mol column of rdkit_mol

--alter table rdkit_mol
--alter column mol
--type mol
--using mol_from_smiles(mol::cstring);
--
--insert into rdkit_mol(id, nsc, mol) 
--select 
--id, nsc, mol_from_smiles(parent_can_smi::cstring)
--from standardized_smiles
--where is_valid_smiles(parent_can_smi::cstring);
        
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--upload the APPROVED, APPROVEDNONONC and the INVESTIGATIONAL data files
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-- APPROVED
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

drop table if exists loading_approved;

--"Agent","Alias Names","Originator","NSC Number","CAS #","Target","Type","Project Code","On Approved Drugs Plate","On Investigational Agent Plate"

create table loading_approved(
agent varchar(1024),
alias varchar(1024),
originator varchar(1024),
nsc int,
cas varchar(1024),
target varchar(1024),
type varchar(1024),
project_code varchar(1024),
onapprovedplate varchar(1024),
oninvestigationalplate varchar(1024)
);

--modify line 54 of input file to remove trailing 3/4 symbol in RP-54780�

\copy loading_approved from '/home/mwkunkel/PROJECTS/ACTIVE/DIS/DrugTracker/DATA_LOAD/APPROVED.csv' csv header quote as '"'

update loading_approved set type = 'APPROVED';

--POMALIDOMIDE is null for oninvestigationalplate

update loading_approved set oninvestigationalplate = 'NO' where oninvestigationalplate is null;
update loading_approved set onapprovedplate = upper(onapprovedplate);
update loading_approved set oninvestigationalplate = upper(oninvestigationalplate);

--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-- APPROVED_NONONC
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

drop table if exists loading_approved_nononc;

--"Agent","Alias Names","Originator","NSC Number","CAS #","Target","Type","Project Code","On Approved Drugs Plate","On Investigational Agent Plate"

create table loading_approved_nononc(
agent varchar(1024),
alias varchar(1024),
originator varchar(1024),
nsc int,
cas varchar(1024),
target varchar(1024),
type varchar(1024),
project_code varchar(1024),
onapprovedplate varchar(1024),
oninvestigationalplate varchar(1024)
);

\copy loading_approved_nononc from '/home/mwkunkel/PROJECTS/ACTIVE/DIS/DrugTracker/DATA_LOAD/APPROVEDNONONCOLOGY.csv' csv header quote as '"'

update loading_approved_nononc set type = 'APPROVED NON-ONCOLOGY';

-- there are entries in the oninvestigationplate that read 'IOA3' which I am translating to NO since all other entries are Yes
update loading_approved_nononc set oninvestigationalplate = 'NO' where oninvestigationalplate = 'IOA3';

update loading_approved_nononc set onapprovedplate = upper(onapprovedplate);
update loading_approved_nononc set oninvestigationalplate = upper(oninvestigationalplate);

--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-- INVESTIGATIONAL
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

drop table if exists loading_investigational;

create table loading_investigational(
generic_name varchar(1024),
agent varchar(1024),
alias varchar(1024),
originator varchar(1024),
nsc int,
cas varchar(1024),
target varchar(1024),
type varchar(1024),
project_code varchar(1024),
oninvestigationalplate varchar(1024),
purchorsynth varchar(1024),
contractor varchar(1024),
status varchar(1024),
acq_date varchar(1024)
);

\copy loading_investigational from '/home/mwkunkel//PROJECTS/ACTIVE/DIS/DrugTracker/DATA_LOAD/INVESTIGATIONAL.csv' csv header quote as '"' null as ''

update loading_investigational set type = 'INVESTIGATIONAL';
update loading_investigational set oninvestigationalplate = 'NO' where oninvestigationalplate = 'IOA3';
update loading_investigational set oninvestigationalplate = upper(oninvestigationalplate);

--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
-- COMBINED LOADING TABLE
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

drop sequence if exists loading_seq;
create sequence loading_seq;

drop table if exists loading;

create table loading(
id bigint,
agent varchar(1024),
alias varchar(1024),
originator varchar(1024),
nsc int,
cas varchar(1024),
target varchar(1024),
type varchar(1024),
project_code varchar(1024),
onapprovedplate varchar(1024),
oninvestigationalplate varchar(1024)
);

insert into loading (id, agent, alias, originator, nsc, cas, target, type, project_code, onapprovedplate, oninvestigationalplate)
select nextval('loading_seq') , agent, alias, originator, nsc, cas, target, type, project_code, onapprovedplate, oninvestigationalplate
from loading_approved;

insert into loading (id, agent, alias, originator, nsc, cas, target, type, project_code, onapprovedplate, oninvestigationalplate)
select nextval('loading_seq') , agent, alias, originator, nsc, cas, target, type, project_code, onapprovedplate, oninvestigationalplate
from loading_approved_nononc;

insert into loading (id, agent, alias, originator, nsc, cas, target, type, project_code, onapprovedplate, oninvestigationalplate)
select nextval('loading_seq') , agent, alias, originator, nsc, cas, target, type, project_code, 'NO', oninvestigationalplate
from loading_investigational;

update loading set agent = trim(both ' ' from agent);
update loading set originator = trim(both ' ' from originator);

update loading set type = upper(type);

--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--run upload.PL to extract and separate the content of the alias and target columns since they are degnerate
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx
--xxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxx

