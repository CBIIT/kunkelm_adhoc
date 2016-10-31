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

\copy app_and_inv from '/home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/APP_AND_INV/APP_AND_INV_25_MAY_2016.csv' csv header null as '' delimiter as E'\t'

update app_and_inv set generic_name = trim(both ' ' from generic_name); 
update app_and_inv set preferred_name = trim(both ' ' from preferred_name); 
update app_and_inv set alias_names = trim(both ' ' from alias_names); 
update app_and_inv set originator = trim(both ' ' from originator); 
update app_and_inv set cas = trim(both ' ' from cas); 
update app_and_inv set primary_target = trim(both ' ' from primary_target); 
update app_and_inv set other_targets = trim(both ' ' from other_targets); 
update app_and_inv set type = trim(both ' ' from type); 
update app_and_inv set project_code = trim(both ' ' from project_code);
update app_and_inv

-- HAVE TO SET cas since stacking is based on nsc, cas
set cas = 'no cas' where cas is null;

create index aai_nsc_idx on app_and_inv(nsc);
