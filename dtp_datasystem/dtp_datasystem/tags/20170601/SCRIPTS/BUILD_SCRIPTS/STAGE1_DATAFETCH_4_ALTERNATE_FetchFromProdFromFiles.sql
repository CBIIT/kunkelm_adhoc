\set ON_ERROR_STOP on

\c datasystemdb

drop table if exists prod_chem_name;
create table prod_chem_name(
cmpd_id int,
nsc int,
chem_name varchar,
chem_name_type varchar);

drop table if exists prod_legacy_cmpd;
create table prod_legacy_cmpd(
nsc int,
cas varchar,
conf varchar,
distribution_code varchar,
molecular_formula varchar,
molecular_weight double precision,
ctab text);

drop table if exists prod_count_hf;
create table prod_count_hf(
nsc int,
the_count int);

drop table if exists prod_count_nci60;
create table prod_count_nci60(
nsc int,
the_count int);

drop table if exists prod_count_xeno;
create table prod_count_xeno(
nsc int,
the_count int);

drop table if exists prod_inventory;
create table prod_inventory(
nsc int,
inventory double precision);

drop table if exists prod_mtxt;
create table prod_mtxt(
nsc int,
mtxt varchar);

drop table if exists prod_plated_sets;
create table prod_plated_sets(
nsc int,
plate_set varchar);

drop table if exists prod_projects;
create table prod_projects(
nsc int,
project_code varchar,
description varchar);

\copy prod_chem_name from /home/mwkunkel/prod_chem_name.csv csv header quote as '"'  
\copy prod_legacy_cmpd from /home/mwkunkel/prod_legacy_cmpd.csv csv header quote as '"'       
\copy prod_count_hf from /home/mwkunkel/prod_count_hf.csv csv header quote as '"'   
\copy prod_count_nci60 from /home/mwkunkel/prod_count_nci60.csv csv header quote as '"'  
\copy prod_count_xeno from /home/mwkunkel/prod_count_xeno.csv csv header quote as '"'   
\copy prod_inventory from /home/mwkunkel/prod_inventory.csv csv header quote as '"'    
\copy prod_mtxt from /home/mwkunkel/prod_mtxt.csv csv header quote as '"'
\copy prod_plated_sets from /home/mwkunkel/prod_plated_sets.csv csv header quote as '"'
\copy prod_projects from /home/mwkunkel/prod_projects.csv csv header quote as '"'

create index prod_chem_name_nsc on prod_chem_name(nsc);  
create index prod_count_nci60_nsc on prod_count_nci60(nsc);  
create index prod_mtxt_nsc on prod_mtxt(nsc);
create index prod_legacy_cmpd_nsc on prod_legacy_cmpd(nsc);       
create index prod_count_xeno_nsc on prod_count_xeno(nsc);   
create index prod_plated_sets_nsc on prod_plated_sets(nsc);
create index prod_count_hf_nsc on prod_count_hf(nsc);   
create index prod_inventory_nsc on prod_inventory(nsc);    
create index prod_projects_nsc on prod_projects(nsc);

vacuum analyze prod_chem_name;
vacuum analyze prod_legacy_cmpd;
vacuum analyze prod_count_hf;
vacuum analyze prod_count_nci60;
vacuum analyze prod_count_xeno;
vacuum analyze prod_inventory;
vacuum analyze prod_mtxt;
vacuum analyze prod_plated_sets;
vacuum analyze prod_projects;

--\copy prod_chem_name to /home/mwkunkel/prod_chem_name.csv csv header quote as '"'  
--\copy prod_legacy_cmpd to /home/mwkunkel/prod_legacy_cmpd.csv csv header quote as '"'       
--\copy prod_count_hf to /home/mwkunkel/prod_count_hf.csv csv header quote as '"'   
--\copy prod_count_nci60 to /home/mwkunkel/prod_count_nci60.csv csv header quote as '"'  
--\copy prod_count_xeno to /home/mwkunkel/prod_count_xeno.csv csv header quote as '"'   
--\copy prod_inventory to /home/mwkunkel/prod_inventory.csv csv header quote as '"'    
--\copy prod_mtxt to /home/mwkunkel/prod_mtxt.csv csv header quote as '"'
--\copy prod_plated_sets to /home/mwkunkel/prod_plated_sets.csv csv header quote as '"'
--\copy prod_projects to /home/mwkunkel/prod_projects.csv csv header quote as '"'

create table prod_bio_counts 
as 
select 
rs3_from_plp_nsc.nsc, 
prod_count_nci60.the_count as prod_count_nci60, 
prod_count_hf.the_count as prod_count_hf, 
prod_count_xeno.the_count as prod_count_xeno 
from rs3_from_plp_nsc 
left outer join prod_count_nci60 on rs3_from_plp_nsc.nsc = prod_count_nci60.nsc 
left outer join prod_count_hf on rs3_from_plp_nsc.nsc = prod_count_hf.nsc 
left outer join prod_count_xeno on rs3_from_plp_nsc.nsc = prod_count_xeno.nsc;