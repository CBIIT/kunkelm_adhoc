-- 16 SEPT 2016

drop table if exists salts_from_dz;

create table salts_from_dz(
salt_name varchar,
Molecular_Formula varchar,
Molecular_Weight double precision,
FormalCharge int,
Num_Atoms int,
can_smi varchar,
can_taut varchar
);

\copy salts_from_dz from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/BUILD_SCRIPTS/salts_from_dz.tsv csv header delimiter as E'\t'

-- degeneracy by can_taut and charge

drop table if exists unique_salts;

create table unique_salts
as
select 
can_taut, count(*),
array_to_string(array_agg(distinct salt_name), ', ') as salt_name, 
molecular_formula,
molecular_weight,
formalcharge,
array_to_string(array_agg(distinct can_smi), ', ') as can_smi
from salts_from_dz
group by can_taut, molecular_formula, molecular_weight, formalcharge;

create index unique_salts_can_taut on unique_salts(can_taut);

vacuum analyze unique_salts;

drop sequence if exists cmpd_known_salt_seq;

create sequence cmpd_known_salt_seq;

delete from cmpd_known_salt;

-- dummy for 'no salt'

insert into cmpd_known_salt(id, can_smi, can_taut, can_taut_strip_stereo, salt_name, salt_mf, salt_mw, salt_comment, salt_charge)
select nextval('cmpd_known_salt_seq'), 'no salt', 'no salt', 'no salt', 'no salt', null, 0, 'no salt', 0;

insert into cmpd_known_salt(id, can_smi, can_taut, can_taut_strip_stereo, salt_name, salt_mf, salt_mw, salt_comment, salt_charge)
select nextval('cmpd_known_salt_seq'), can_smi, can_taut, null, salt_name, molecular_formula, molecular_weight, 'DZ', formalcharge
from unique_salts;

