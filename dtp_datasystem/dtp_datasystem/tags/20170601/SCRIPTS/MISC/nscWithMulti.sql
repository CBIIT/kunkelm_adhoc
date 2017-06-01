drop table if exists multi_alias;

create table multi_alias
as
select nc.nsc, count(ca.*) as count_aliases
from nsc_cmpd nc, cmpd_aliases2nsc_cmpds map_to_many, cmpd_alias ca
where map_to_many.nsc_cmpds_fk = nc.id
and map_to_many.cmpd_aliases_fk = ca.id
group by nc.nsc order by 2 desc;

drop table if exists multi_project;

create table multi_project
as
select nc.nsc, count(ca.*) as count_projectes
from nsc_cmpd nc, cmpd_projects2nsc_cmpds map_to_many, cmpd_project ca
where map_to_many.nsc_cmpds_fk = nc.id
and map_to_many.cmpd_projects_fk = ca.id
group by nc.nsc order by 2 desc;

drop table if exists multi_plate;

create table multi_plate
as
select nc.nsc, count(ca.*) as count_platees
from nsc_cmpd nc, cmpd_plates2nsc_cmpds map_to_many, cmpd_plate ca
where map_to_many.nsc_cmpds_fk = nc.id
and map_to_many.cmpd_plates_fk = ca.id
group by nc.nsc order by 2 desc;

select nsc from multi_alias limit 20;

select nsc from multi_project limit 20;

select nsc from multi_plate limit 20;