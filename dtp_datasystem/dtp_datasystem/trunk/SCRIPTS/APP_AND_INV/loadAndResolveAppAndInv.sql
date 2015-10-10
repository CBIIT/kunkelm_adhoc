drop table if exists app_and_inv;

create table app_and_inv(
generic_name varchar,
preferred_name varchar,
originator varchar,
nsc int,
cas varchar,
target varchar,
other_targets varchar,
app_or_inv varchar,
project_code varchar
);

\copy app_and_inv from ./appAndInv.09OCT2015.csv csv header

-- stats

drop table if exists temp;

create table temp
as
select target, regexp_replace(lower(target), '[ -]','') as fixed, count(*) 
from app_and_inv
group by target
order by target;

drop table if exists temp2;

create table temp2
as 
select fixed, array_to_string(array_agg(distinct target), ','), count(distinct target) 
from temp
group by fixed
order by count(distinct target) desc, fixed asc;

drop table if exists temp3;

create table temp3
as
select '--->'||target||'<---'
from app_and_inv
where regexp_replace(lower(target), '[ -]','') in (
	select fixed from temp2
	where "count" > 1
)
order by target;

\copy temp3 to /tmp/app_and_inv_target_formatting.csv csv header

--alter table nsc_ident alter column drug_name drop not null;
--alter table nsc_ident alter column target drop not null;
--
--update nsc_ident
--set drug_name = null, target = null;
--
----generic_name
--
--update nsc_ident
--set drug_name = app_and_inv.generic_name, target = app_and_inv.target
--from app_and_inv
--where nsc_ident.nsc = app_and_inv.nsc;
--
----if no generic then try preferred_name
--
--update nsc_ident
--set drug_name = app_and_inv.preferred_name
--from app_and_inv
--where nsc_ident.drug_name is null
--and nsc_ident.nsc = app_and_inv.nsc;
--
--select count(*) from nsc_ident where drug_name is null;
--