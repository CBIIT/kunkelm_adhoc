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

\copy app_and_inv from '/home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/APP_AND_INV/APP_AND_INV_25_MAY_2016.tsv' csv header null as '' delimiter as E'\t'

update app_and_inv 
set nsc = trim(both ' ' from nsc);

update app_and_inv 
set cas = trim(both ' ' from cas);

update app_and_inv
set cas = 'no cas' where cas is null;

create index aai_nsc_idx on app_and_inv(nsc);
