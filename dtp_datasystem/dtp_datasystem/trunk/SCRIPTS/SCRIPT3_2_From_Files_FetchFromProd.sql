\c datasystemdb

create table prod_chem_name(
nsc int,
chem_name varchar,
chem_name_type varchar);

create table prod_cmpd(
nsc int,
cas varchar,
conf varchar,
distribution_code varchar,
mf varchar,
mw double precision);

create table prod_count_hf(
nsc int,
the_count int);

create table prod_count_nci60(
nsc int,
the_count int);

create table prod_count_xeno(
nsc int,
the_count int);

create table prod_inventory(
nsc int,
inventory double precision);

create table prod_mtxt(
nsc int,
mtxt varchar);

create table prod_plated_sets(
nsc int,
plate_set varchar);

create table prod_projects(
nsc int,
project_code varchar,
description varchar);

\copy prod_chem_name from /home/mwkunkel/prod_chem_name.csv csv header  
\copy prod_cmpd from /home/mwkunkel/prod_cmpd.csv csv header       
\copy prod_count_hf from /home/mwkunkel/prod_count_hf.csv csv header   
\copy prod_count_nci60 from /home/mwkunkel/prod_count_nci60.csv csv header  
\copy prod_count_xeno from /home/mwkunkel/prod_count_xeno.csv csv header   
\copy prod_inventory from /home/mwkunkel/prod_inventory.csv csv header    
\copy prod_mtxt from /home/mwkunkel/prod_mtxt.csv csv header
\copy prod_plated_sets from /home/mwkunkel/prod_plated_sets.csv csv header
\copy prod_projects from /home/mwkunkel/prod_projects.csv csv header

create index prod_chem_name_nsc on prod_chem_name(nsc);  
create index prod_count_nci60_nsc on prod_count_nci60(nsc);  
create index prod_mtxt_nsc on prod_mtxt(nsc);
create index prod_cmpd_nsc on prod_cmpd(nsc);       
create index prod_count_xeno_nsc on prod_count_xeno(nsc);   
create index prod_plated_sets_nsc on prod_plated_sets(nsc);
create index prod_count_hf_nsc on prod_count_hf(nsc);   
create index prod_inventory_nsc on prod_inventory(nsc);    
create index prod_projects_nsc on prod_projects(nsc);

vacuum analyze prod_chem_name;
vacuum analyze prod_cmpd;
vacuum analyze prod_count_hf;
vacuum analyze prod_count_nci60;
vacuum analyze prod_count_xeno;
vacuum analyze prod_inventory;
vacuum analyze prod_mtxt;
vacuum analyze prod_plated_sets;
vacuum analyze prod_projects;