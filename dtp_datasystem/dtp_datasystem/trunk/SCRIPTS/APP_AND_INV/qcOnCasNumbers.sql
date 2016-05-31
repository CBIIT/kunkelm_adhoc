\set ON_ERROR_STOP on

drop table if exists temp;

create table temp 
as
select curated_nsc.nsc as curated_nsc_nsc, curated_nsc.cas as curated_nsc_cas, gnam.value as generic_name, pnam.value as preferred_name, nsc_cmpd.cas as dis_cas 
from curated_nsc 
left outer join curated_name gnam on curated_nsc.generic_name_fk = gnam.id
left outer join curated_name pnam on curated_nsc.preferred_name_fk = pnam.id
left outer join nsc_cmpd on curated_nsc.nsc = nsc_cmpd.nsc; 

alter table temp 
add curated_nsc_stripped_cas varchar;

update temp
set curated_nsc_stripped_cas = replace(curated_nsc_cas, '-', '');

drop table if exists mismatched_cas;

create table mismatched_cas
as
--select curated_nsc_nsc, generic_name, preferred_name, '-->'||curated_nsc_cas||'<--' as curated_nsc_cas , dis_cas, '-->'||curated_nsc_stripped_cas||'<--' as curated_nsc_stripped_cas
select curated_nsc_nsc, generic_name, preferred_name, curated_nsc_cas, dis_cas, curated_nsc_stripped_cas
from temp 
where curated_nsc_stripped_cas != dis_cas
and dis_cas != '0';

drop table if exists missing_cas;

create table missing_cas
as
select curated_nsc_nsc, generic_name, preferred_name, curated_nsc_cas, dis_cas, curated_nsc_stripped_cas
from temp 
where curated_nsc_cas is null or dis_cas is null or curated_nsc_cas = '0' or dis_cas = '0';

\copy mismatched_cas to /tmp/mismatched_cas.csv csv header

\copy missing_cas to /tmp/missing_cas.csv csv header