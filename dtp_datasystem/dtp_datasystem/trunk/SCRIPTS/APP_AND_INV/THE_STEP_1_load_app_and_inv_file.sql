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

\copy app_and_inv from '/home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/APP_AND_INV/Combined IOA and AOD List 02_12_2016_MWK_FIXED_with_sarc_and_sclc_compounds.csv' csv header

create index aai_nsc_idx on app_and_inv(nsc);
