--start: 20160306 13:46:47.083
drop table if exists temp;

create table temp
	as
	select entry, regexp_replace(lower(entry), '[ -]','') as fixed, count(*) 
	from fields_and_entries
	where field_name = 'generic_name'
	group by entry
	order by entry;

drop table if exists temp2;

create table temp2
	as 
	select fixed, array_to_string(array_agg(distinct entry), ','), count(distinct entry) 
	from temp
	group by fixed
	order by count(distinct entry) desc, fixed asc;

drop table if exists temp3;

create table temp3
	as
	select '--->'||entry||'<---'
	from fields_and_entries
	where field_name = 'generic_name'
	and regexp_replace(lower(entry), '[ -]','') in (
	select fixed from temp2
	where "count" > 1
	)
	order by entry;

\copy temp3 to /tmp/app_and_inv_generic_name_formatting.csv csv header

drop table if exists temp;

create table temp
	as
	select entry, regexp_replace(lower(entry), '[ -]','') as fixed, count(*) 
	from fields_and_entries
	where field_name = 'preferred_name'
	group by entry
	order by entry;

drop table if exists temp2;

create table temp2
	as 
	select fixed, array_to_string(array_agg(distinct entry), ','), count(distinct entry) 
	from temp
	group by fixed
	order by count(distinct entry) desc, fixed asc;

drop table if exists temp3;

create table temp3
	as
	select '--->'||entry||'<---'
	from fields_and_entries
	where field_name = 'preferred_name'
	and regexp_replace(lower(entry), '[ -]','') in (
	select fixed from temp2
	where "count" > 1
	)
	order by entry;

\copy temp3 to /tmp/app_and_inv_preferred_name_formatting.csv csv header

drop table if exists temp;

create table temp
	as
	select entry, regexp_replace(lower(entry), '[ -]','') as fixed, count(*) 
	from fields_and_entries
	where field_name = 'alias_names'
	group by entry
	order by entry;

drop table if exists temp2;

create table temp2
	as 
	select fixed, array_to_string(array_agg(distinct entry), ','), count(distinct entry) 
	from temp
	group by fixed
	order by count(distinct entry) desc, fixed asc;

drop table if exists temp3;

create table temp3
	as
	select '--->'||entry||'<---'
	from fields_and_entries
	where field_name = 'alias_names'
	and regexp_replace(lower(entry), '[ -]','') in (
	select fixed from temp2
	where "count" > 1
	)
	order by entry;

\copy temp3 to /tmp/app_and_inv_alias_names_formatting.csv csv header

drop table if exists temp;

create table temp
	as
	select entry, regexp_replace(lower(entry), '[ -]','') as fixed, count(*) 
	from fields_and_entries
	where field_name = 'other_targets'
	group by entry
	order by entry;

drop table if exists temp2;

create table temp2
	as 
	select fixed, array_to_string(array_agg(distinct entry), ','), count(distinct entry) 
	from temp
	group by fixed
	order by count(distinct entry) desc, fixed asc;

drop table if exists temp3;

create table temp3
	as
	select '--->'||entry||'<---'
	from fields_and_entries
	where field_name = 'other_targets'
	and regexp_replace(lower(entry), '[ -]','') in (
	select fixed from temp2
	where "count" > 1
	)
	order by entry;

\copy temp3 to /tmp/app_and_inv_other_targets_formatting.csv csv header

drop table if exists temp;

create table temp
	as
	select entry, regexp_replace(lower(entry), '[ -]','') as fixed, count(*) 
	from fields_and_entries
	where field_name = 'originator'
	group by entry
	order by entry;

drop table if exists temp2;

create table temp2
	as 
	select fixed, array_to_string(array_agg(distinct entry), ','), count(distinct entry) 
	from temp
	group by fixed
	order by count(distinct entry) desc, fixed asc;

drop table if exists temp3;

create table temp3
	as
	select '--->'||entry||'<---'
	from fields_and_entries
	where field_name = 'originator'
	and regexp_replace(lower(entry), '[ -]','') in (
	select fixed from temp2
	where "count" > 1
	)
	order by entry;

\copy temp3 to /tmp/app_and_inv_originator_formatting.csv csv header

drop table if exists temp;

create table temp
	as
	select entry, regexp_replace(lower(entry), '[ -]','') as fixed, count(*) 
	from fields_and_entries
	where field_name = 'primary_target'
	group by entry
	order by entry;

drop table if exists temp2;

create table temp2
	as 
	select fixed, array_to_string(array_agg(distinct entry), ','), count(distinct entry) 
	from temp
	group by fixed
	order by count(distinct entry) desc, fixed asc;

drop table if exists temp3;

create table temp3
	as
	select '--->'||entry||'<---'
	from fields_and_entries
	where field_name = 'primary_target'
	and regexp_replace(lower(entry), '[ -]','') in (
	select fixed from temp2
	where "count" > 1
	)
	order by entry;

\copy temp3 to /tmp/app_and_inv_primary_target_formatting.csv csv header

drop table if exists temp;

create table temp
	as
	select entry, regexp_replace(lower(entry), '[ -]','') as fixed, count(*) 
	from fields_and_entries
	where field_name = 'project_code'
	group by entry
	order by entry;

drop table if exists temp2;

create table temp2
	as 
	select fixed, array_to_string(array_agg(distinct entry), ','), count(distinct entry) 
	from temp
	group by fixed
	order by count(distinct entry) desc, fixed asc;

drop table if exists temp3;

create table temp3
	as
	select '--->'||entry||'<---'
	from fields_and_entries
	where field_name = 'project_code'
	and regexp_replace(lower(entry), '[ -]','') in (
	select fixed from temp2
	where "count" > 1
	)
	order by entry;

\copy temp3 to /tmp/app_and_inv_project_code_formatting.csv csv header

--start : 20160306 13:46:47.083
--finish: 20160306 13:46:47.111
