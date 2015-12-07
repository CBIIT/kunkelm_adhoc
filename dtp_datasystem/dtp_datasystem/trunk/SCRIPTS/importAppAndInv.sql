drop table if exists app_and_inv;

create table app_and_inv(
generic_name varchar,
preferred_name varchar,
alias_names varchar,
originator varchar,
nsc int,
cas varchar,
primary_target varchar,
other_targets varchar,
type varchar,
project_code varchar
);

\copy app_and_inv from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/APP_AND_INV/APP_AND_INV.02DEC2015.csv csv header
--\copy app_and_inv from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/APP_AND_INV/appAndInv.09OCT2015.csv csv header
create index aai_nsc_idx on app_and_inv(nsc);

-- fetching SMILES from datasystem
-- ONLY run in datasystemdb

drop table if exists app_and_inv_with_smiles;

create table app_and_inv_with_smiles
as
select ai.*, ct.can_smi
from app_and_inv ai
left outer join cmpd_table ct
on ai.nsc = ct.nsc;

--
--
-- examine nsc without can_smi
--
--

drop table if exists temp;

create table temp
as
select nsc from app_and_inv_with_smiles
where can_smi is null;

select nsc from temp
intersect
select nsc from missing_nsc;


























alter table nsc_ident alter column drug_name drop not null;
alter table nsc_ident alter column target drop not null;
alter table nsc_ident alter column smiles drop not null;

--                    _     _            _   
-- _ __  ___  ___    (_) __| | ___ _ __ | |_ 
--| '_ \/ __|/ __|   | |/ _` |/ _ \ '_ \| __|
--| | | \__ \ (__    | | (_| |  __/ | | | |_ 
--|_| |_|___/\___|___|_|\__,_|\___|_| |_|\__|
--              |_____|                      

-- Scrub ONLY where matches by nsc in app_and_inv.
-- Scrubs ALL three fields.
-- This will force ANY fields that may have been changed ad hoc for specific nscs
-- IF that nsc is in app_and_inv!

update nsc_ident
set target = null, drug_name = null, smiles = null
where nsc in (
	select nsc from app_and_inv_with_smiles
);	

select count(*) from nsc_ident where target is null;
select count(*) from nsc_ident where drug_name is null;
select count(*) from nsc_ident where smiles is null;

-- trying generic_name

update nsc_ident 
set target = aaiws.target,
drug_name = aaiws.generic_name,
smiles = aaiws.smiles
from app_and_inv_with_smiles aaiws
where nsc_ident.nsc = aaiws.nsc;

select count(*) from nsc_ident where target is null;
select count(*) from nsc_ident where drug_name is null;
select count(*) from nsc_ident where smiles is null;

-- trying prefered_name for any remaining nulls

update nsc_ident
set drug_name = aaiws.preferred_name
from app_and_inv_with_smiles aaiws
where nsc_ident.nsc = aaiws.nsc
and nsc_ident.drug_name is null;

select count(*) from nsc_ident where drug_name is null;

--                                                 _  
--  ___ ___  _ __ ___  _ __   ___  _   _ _ __   __| |
-- / __/ _ \| '_ ` _ \| '_ \ / _ \| | | | '_ \ / _` |
--| (_| (_) | | | | | | |_) | (_) | |_| | | | | (_| |
-- \___\___/|_| |_| |_| .__/ \___/ \__,_|_| |_|\__,_|
--                    |_|                            


update compound
set target = null, drug_name = null, smiles = null
where nsc in (
	select nsc from app_and_inv_with_smiles
);	

select count(*) from compound where target is null;
select count(*) from compound where drug_name is null;
select count(*) from compound where smiles is null;

-- trying generic_name

update compound 
set target = aaiws.target,
drug_name = aaiws.generic_name,
smiles = aaiws.smiles
from app_and_inv_with_smiles aaiws
where compound.nsc = aaiws.nsc;

select count(*) from compound where target is null;
select count(*) from compound where drug_name is null;
select count(*) from compound where smiles is null;

-- trying prefered_name for any remaining nulls

update compound
set drug_name = aaiws.preferred_name
from app_and_inv_with_smiles aaiws
where compound.nsc = aaiws.nsc
and compound.drug_name is null;

select count(*) from compound where drug_name is null;

