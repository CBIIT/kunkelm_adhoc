\c dctddb

drop table if exists prod_bio_counts;  
drop table if exists prod_chem_name;   
drop table if exists prod_cmpd;        
drop table if exists prod_count_hf;    
drop table if exists prod_count_nci60; 
drop table if exists prod_count_xeno;  
drop table if exists prod_inventory;   
drop table if exists prod_plated_sets; 
drop table if exists prod_projects;    
drop table if exists prod_src;         
drop table if exists prod_strc;        

-- cmpd info and structures

create table prod_cmpd (cas integer, cmpd_id integer, conf varchar(32), distribution_code varchar(32), mf varchar(1024), mw double precision, nsc integer, prefix varchar(32));
create table prod_strc (strc text, strc_id integer);

\copy prod_cmpd from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/DataSystemBuilder/PROD_DATA_FILES/prod_cmpd.csv csv header
\copy prod_strc from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/DataSystemBuilder/PROD_DATA_FILES/prod_strc.csv csv header

create index prod_cmpd_cmpd_id on prod_cmpd(cmpd_id);
create index prod_strc_strc_id on prod_strc(strc_id);

create table prod_src as select prod_cmpd.*, prod_strc.* from prod_cmpd left outer join prod_strc on prod_cmpd.cmpd_id = prod_strc.strc_id;
create index prod_src_nsc on prod_src(nsc);

-- inventory and annotations

create table prod_inventory (nsc int, inventory double precision);
create table prod_chem_name (cmpd_id bigint, nsc int, chem_name varchar(1024), chem_name_type varchar(1024));
create table prod_projects (nsc int, project_code varchar(1024), project_type varchar(1024));
create table prod_plated_sets (nsc int, plate_set varchar(1024));

\copy prod_inventory from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/DataSystemBuilder/PROD_DATA_FILES/prod_inventory.csv csv header
\copy prod_chem_name from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/DataSystemBuilder/PROD_DATA_FILES/prod_chem_name.csv csv header
\copy prod_projects from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/DataSystemBuilder/PROD_DATA_FILES/prod_projects.csv csv header
\copy prod_plated_sets from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/DataSystemBuilder/PROD_DATA_FILES/prod_plated_sets.csv csv header

create index prod_inventory_nsc on prod_inventory(nsc);
create index prod_chem_name_nsc on prod_chem_name(nsc);
create index prod_projects_nsc on prod_projects(nsc);
create index prod_plated_sets_nsc on prod_plated_sets(nsc);

-- bio counts

create table prod_count_nci60 (nsc int, the_count int);
create table prod_count_hf (nsc int, the_count int);
create table prod_count_xeno (nsc int, the_count int);

\copy prod_count_nci60 from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/DataSystemBuilder/PROD_DATA_FILES/prod_count_nci60.csv csv header
\copy prod_count_hf from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/DataSystemBuilder/PROD_DATA_FILES/prod_count_hf.csv csv header
\copy prod_count_xeno from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/DataSystemBuilder/PROD_DATA_FILES/prod_count_xeno.csv csv header

create index prod_count_nci60_nsc on prod_count_nci60(nsc);
create index prod_count_hf_nsc on prod_count_hf(nsc);
create index prod_count_xeno_nsc on prod_count_xeno(nsc);

create table prod_bio_counts 
as 
select 
prod_src.nsc, 
prod_count_nci60.the_count as prod_count_nci60, 
prod_count_hf.the_count as prod_count_hf, 
prod_count_xeno.the_count as prod_count_xeno 
from prod_src 
left outer join prod_count_nci60 on prod_src.nsc = prod_count_nci60.nsc 
left outer join prod_count_hf on prod_src.nsc = prod_count_hf.nsc 
left outer join prod_count_xeno on prod_src.nsc = prod_count_xeno.nsc;

vacuum analyze verbose;

