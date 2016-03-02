\set ON_ERROR_FAIL true

-- nuke

delete from curated_nsc cascade;
delete from curated_name cascade;
delete from curated_originator cascade;
delete from curated_project cascade;
delete from curated_target cascade;
delete from aliases2curated_nsc_to_aliases cascade;
delete from curated_nsc_to_secondary_targe cascade;
delete from curated_nscs2projects cascade;

-- global fixes
-- these are applied to fields_and_entries
-- since white space issues will have already been fixed

update fields_and_entries
set entry = 'AbbVie'
where entry = 'Abbvie'
and field_name = 'originator';

update fields_and_entries
set entry = 'ArQule'
where entry = 'Arqule'
and field_name = 'originator';

update fields_and_entries
set entry = 'AstraZeneca'
where entry = 'Astra Zeneca'
and field_name = 'originator';

update fields_and_entries
set entry = 'Bcr-Abl'
where entry = 'Bcr-abl'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'DNA Alkylating'
where entry = 'DNA alkylating'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'Gamma Secretase'
where entry = 'Gamma secretase'
and field_name in ('primary_target', 'other_targets');

update fields_and_entries
set entry = 'IGF1R'
where entry = 'IGF-1R'
and field_name in ('primary_target', 'other_targets');


update fields_and_entries
set entry = 'Stat3'
where entry in ('Stat 3', 'Stat-3')
and field_name in ('primary_target', 'other_targets');

-- distinct names

drop table if exists temp;

create table temp 
as
select distinct entry
from fields_and_entries
where field_name in ('alias_names', 'generic_name', 'preferred_name')
order by entry;

drop sequence if exists temp_seq;
create sequence temp_seq;

insert into curated_name(id, value, description, reference)
select
nextval('temp_seq'), entry, 'from app_and_inv load', 'from app_and_inv load'
from temp;

-- distinct originators

drop table if exists temp;

create table temp
as
select distinct entry
from fields_and_entries
where field_name = 'originator'
order by entry;

drop sequence if exists temp_seq;
create sequence temp_seq;

insert into curated_originator(id, value, description, reference)
select
nextval('temp_seq'), entry, 'from app_and_inv load', 'from app_and_inv load'
from temp;

-- distinct projects

-- this goes back to app_and_inv, NOT fields_and_entries...

drop table if exists temp;

create table temp
as
select distinct project_code, type
from app_and_inv
order by project_code;

drop sequence if exists temp_seq;
create sequence temp_seq;

insert into curated_project(id, value, description, reference)
select nextval('temp_seq'), project_code, type, 'from app_and_inv_load'
from temp;

-- distinct targets

drop table if exists temp;

create table temp
as
select distinct entry
from fields_and_entries
where field_name in ('primary_target', 'other_targets')
order by entry;

drop sequence if exists temp_seq;
create sequence temp_seq;

insert into curated_target(id, value, description, reference)
select
nextval('temp_seq'), entry, 'from app_and_inv_load', 'from app_and_inv_load'
from temp;

-- resolve 

drop table if exists temp;

create table temp
as
select distinct nsc, cas
from fields_and_entries;

-- recreate the original layout for singleton fields

drop table if exists temp2;

create table temp2
as
select 
t.nsc, 
t.cas, 
pn.entry as preferred_name,
gn.entry as generic_name,
pt.entry as primary_target,
o.entry as originator
from temp t
    left outer join fields_and_entries pn on t.nsc = pn.nsc and pn.field_name = 'preferred_name'
    left outer join fields_and_entries gn on t.nsc = gn.nsc and gn.field_name = 'generic_name'
    left outer join fields_and_entries pt on t.nsc = pt.nsc and pt.field_name = 'primary_target'
    left outer join fields_and_entries o on t.nsc = o.nsc and o.field_name = 'originator';

-- resolve to FK values

drop table if exists temp3;

create table temp3
as
select 
t.nsc, 
t.cas, 
pn.id as preferred_name_fk,
gn.id as generic_name_fk,
pt.id as primary_target_fk,
o.id as originator_fk
from temp2 t
    left outer join curated_name pn on t.preferred_name = pn.value
    left outer join curated_name gn on t.generic_name = gn.value 
    left outer join curated_target pt on t.primary_target = pt.value
    left outer join curated_originator o on t.originator = o.value;

-- load to curated_nsc
-- id is set to nsc to simplify the many-to-many mappings

insert into curated_nsc(id, cas, nsc, preferred_name_fk, generic_name_fk, primary_target_fk, originator_fk)
select nsc, cas, nsc, preferred_name_fk, generic_name_fk, primary_target_fk, originator_fk
from temp3;

-- many-to-many

insert into curated_nscs2projects(projects_fk, curated_nscs_fk)
select
cp.id, fae.nsc
from fields_and_entries fae, curated_project cp
where fae.entry = cp.value
and fae.field_name = 'project_code';

insert into aliases2curated_nsc_to_aliases(aliases_fk, curated_nsc_to_aliases_fk)
select
cn.id, fae.nsc
from fields_and_entries fae, curated_name cn
where fae.entry = cn.value
and fae.field_name in ('alias_names', 'generic_name', 'preferred_name');

insert into curated_nsc_to_secondary_targe(curated_nsc_to_secondary_ta_fk, secondary_targets_fk)
select
fae.nsc, ct.id
from fields_and_entries fae, curated_target ct
where fae.entry = ct.value
and fae.field_name = 'other_targets';
