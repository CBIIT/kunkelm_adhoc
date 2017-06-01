\set ON_ERROR_STOP on

-- nuke

delete from aliases2curated_nsc_to_aliases cascade;
delete from curated_nsc_to_secondary_targe cascade;
delete from curated_nscs2projects cascade;

delete from curated_nsc cascade;

delete from curated_name cascade;
delete from curated_originator cascade;
delete from curated_project cascade;
delete from curated_target cascade;

drop sequence if exists curated_nsc_seq;
create sequence curated_nsc_seq;        

drop sequence if exists curated_name_seq;
create sequence curated_name_seq;       

drop sequence if exists curated_originator_seq;
create sequence curated_originator_seq; 

drop sequence if exists curated_project_seq;
create sequence curated_project_seq;    

drop sequence if exists curated_target_seq;
create sequence curated_target_seq;  

-- distinct names

drop table if exists temp;

create table temp 
as
select distinct entry
from fields_and_entries
where field_name in ('alias_names', 'generic_name', 'preferred_name')
order by entry;

insert into curated_name(id, value, description, reference)
select
nextval('curated_name_seq'), entry, 'from app_and_inv load', 'Combined IOA and AOD List'
from temp;

-- distinct originators

drop table if exists temp;

create table temp
as
select distinct entry
from fields_and_entries
where field_name = 'originator'
order by entry;

insert into curated_originator(id, value, description, reference)
select
nextval('curated_originator_seq'), entry, 'from app_and_inv load', 'Combined IOA and AOD List'
from temp;

-- distinct projects

drop table if exists temp;

create table temp
as
select distinct entry
from fields_and_entries
where field_name = 'project_code'
order by entry;

insert into curated_project(id, value, description, reference)
select nextval('curated_project_seq'), entry, 'from app_and_inv_load', 'Combined IOA and AOD List'
from temp;

-- distinct targets

drop table if exists temp;

create table temp
as
select distinct entry
from fields_and_entries
where field_name in ('primary_target', 'other_targets')
order by entry;

insert into curated_target(id, value, description, reference)
select
nextval('curated_target_seq'), entry, 'from app_and_inv_load', 'Combined IOA and AOD List 02_12_2016'
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

-- curated_nsc

insert into curated_nsc(id, cas, nsc, preferred_name_fk, generic_name_fk, primary_target_fk, originator_fk)
select nextval('curated_nsc_seq'), cas, nsc, preferred_name_fk, generic_name_fk, primary_target_fk, originator_fk
from temp3;

-- many-to-many

insert into aliases2curated_nsc_to_aliases(aliases_fk, curated_nsc_to_aliases_fk)
select
distinct cn.id, cnsc.id
from fields_and_entries fae, curated_name cn, curated_nsc cnsc
where fae.entry = cn.value
and fae.nsc = cnsc.nsc
and fae.field_name in ('alias_names', 'generic_name', 'preferred_name');

insert into curated_nscs2projects(projects_fk, curated_nscs_fk)
select
cp.id, cnsc.id
from fields_and_entries fae, curated_project cp, curated_nsc cnsc
where fae.entry = cp.value
and fae.nsc = cnsc.nsc
and fae.field_name = 'project_code';

insert into curated_nsc_to_secondary_targe(curated_nsc_to_secondary_ta_fk, secondary_targets_fk)
select
cnsc.id, ct.id
from fields_and_entries fae, curated_target ct, curated_nsc cnsc
where fae.entry = ct.value
and fae.nsc = cnsc.nsc
and fae.field_name = 'other_targets';
