drop table if exists app_and_inv;

create table app_and_inv(
nsc int,
cas varchar,
generic_name varchar,
preferred_name varchar,
alias_names varchar,
originator varchar,
project_code varchar,
primary_target varchar,
other_targets varchar
);

\copy app_and_inv from '/home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/APP_AND_INV/curatedNscs 2016Mar15at10-51-EDT.csv' csv header

create index aai_nsc_idx on app_and_inv(nsc);
