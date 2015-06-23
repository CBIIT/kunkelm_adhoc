--export the compare_cell_lines from pptpdb and import them into pptp_working_db

\c pptpdb

--into the COMPARE tables
drop table if exists export_test_result cascade;
drop table if exists export_test_result_type cascade;
drop table if exists export_cell_line_data_set_ident cascade;
drop table if exists export_cell_line_data_set cascade;
drop table if exists export_compare_cell_line; 
drop table if exists export_mol_targ_ident;

create table export_compare_cell_line as 
select 
c.id,
c.id as line_number,
c.name as line_name,
c.id as cell_id,
1 as line_sort_order,
c.panel_type_fk as panel_number,
p.display_name as panel_name,
1 as panel_sort_order,
p.display_name as panel_cde
from cell_line_type c, panel_type p
where c.panel_type_fk = p.id;

create table export_test_result_type(
id int,
display_name varchar(128),
direction varchar(128));

insert into export_test_result_type values(1,'60 cell screen','inverse');
insert into export_test_result_type values(2,'molecular target','forward');
insert into export_test_result_type values(3,'user-uploaded data forward','forward');
insert into export_test_result_type values(4,'user-uploaded data inverse','inverse');

--create mol_targ_idents

create table export_mol_targ_ident as 
select 
id, 
probe_set_name as molt_id, 
genecard,
'MOL_TARG_PPTP_AFFY'::varchar(128) as target_set,
probe_set_name
from affy_identifier_type;

--create cell_line_data_set_idents

create table export_cell_line_data_set_ident as 
select
id,
COALESCE(probe_set_name, '') ||' '|| COALESCE(genecard, '') ||' '|| COALESCE(accession, '') as identifier_for_display,
COALESCE(probe_set_name, '') ||' '|| COALESCE(genecard, '') ||' '|| COALESCE(accession, '') as descriptor_for_display,
'MOL_TARG_PPTP_AFFY'::varchar(128) as target_set
from affy_identifier_type;

--create cell_line_data_sets

create table export_cell_line_data_set as 
select
affy_identifier_type_fk as id,
count(signal) as count_experiments,
1 as count_compare_cell_lines,
0 as standard_deviation, --stddev(signal) as standard_deviation,
avg(signal) as mean,
'PUBLIC'::varchar(128) as namecode,
affy_identifier_type_fk as cell_line_data_set_id,
'PUBLIC'::varchar(32) as discreet,
2 as test_result_type_fk,
affy_identifier_type_fk as cell_line_data_set_ident_fk
from affy_data
--where detection = 'P' 
group by affy_identifier_type_fk;

-- create test_result

create table export_test_result as 
select
id, 
cell_line_type_fk as cell_id,
signal as value,
1 as count_experiments,
0 as standard_deviation,
affy_identifier_type_fk as cell_line_data_set_fk,
cell_line_type_fk as cell_line_fk
from affy_data;
--where detection = 'P';

drop table if exists compare_collation_for_export;

create table compare_collation_for_export (endpoint varchar(32), value double precision, compare_cell_line varchar(32), compound varchar(32)); 

insert into compare_collation_for_export
select 'NSC_efs_t_over_c', efs_t_over_c, clt.pptp_identifier, ct.pptp_identifier 
from summary, compound_type ct, cell_line_type clt, cell_line_group clg, mtd_study mtds			 
where group_role_type_fk = 2
and efs_t_over_c is not null
and cell_line_group_fk = clg.id
and clt.id = clg.cell_line_type_fk
and clg.mtd_study_fk = mtds.id
and mtds.compound_type_fk = ct.id;

insert into compare_collation_for_export
select 'NSC_median_r_t_v',median_r_t_v, clt.pptp_identifier, ct.pptp_identifier
from summary, compound_type ct, cell_line_type clt, cell_line_group clg, mtd_study mtds 
where group_role_type_fk = 2
and median_r_t_v is not null
and cell_line_group_fk = clg.id
and clt.id = clg.cell_line_type_fk
and clg.mtd_study_fk = mtds.id
and mtds.compound_type_fk = ct.id; 

insert into compare_collation_for_export
select 'NSC_t_over_c',t_over_c, clt.pptp_identifier, ct.pptp_identifier
from summary, compound_type ct, cell_line_type clt, cell_line_group clg, mtd_study mtds 
where group_role_type_fk = 2
and t_over_c is not null
and cell_line_group_fk = clg.id
and clt.id = clg.cell_line_type_fk
and clg.mtd_study_fk = mtds.id
and mtds.compound_type_fk = ct.id; 

insert into compare_collation_for_export
select 'NSC_response_median_score', response_median_score, clt.pptp_identifier, ct.pptp_identifier
from summary, compound_type ct, cell_line_type clt, cell_line_group clg, mtd_study mtds 
where group_role_type_fk = 2
and response_median_score is not null
and cell_line_group_fk = clg.id
and clt.id = clg.cell_line_type_fk
and clg.mtd_study_fk = mtds.id
and mtds.compound_type_fk = ct.id; 

drop table if exists temp_table;

create table temp_table as
select distinct compound,endpoint
from compare_collation_for_export;

drop table if exists temp_intermediate_table;

create table temp_intermediate_table as
select nextval('hibernate_seq') as id, 
compound, 
endpoint
from temp_table;

alter table compare_collation_for_export add cell_line_data_set_fk bigint;

update compare_collation_for_export set cell_line_data_set_fk = temp_intermediate_table.id
from temp_intermediate_table
where compare_collation_for_export.compound = temp_intermediate_table.compound
and compare_collation_for_export.endpoint = temp_intermediate_table.endpoint;

alter table compare_collation_for_export add cell_id bigint;

update compare_collation_for_export set cell_id = cell_line_type.id
from cell_line_type
where compare_collation_for_export.compare_cell_line = cell_line_type.pptp_identifier;

--cell_line_data_set_ident

insert into export_cell_line_data_set_ident
select
id,
coalesce(compound, '') ||' '|| coalesce(endpoint, '') as identifier_for_display,
coalesce(compound, '') ||' '|| coalesce(endpoint, '') as descriptor_for_display,
endpoint as identifier_type
from temp_intermediate_table;

--mol_targ_ident

insert into export_mol_targ_ident
select 
id, 
coalesce(compound, '') ||' '|| coalesce(endpoint, '') as molt_id,
coalesce(compound, '') ||' '|| coalesce(endpoint, '') as genecard,
'MOL_TARG_PPTP_CMPD_'||endpoint as target_set
from temp_intermediate_table;

--cell_line_data_set

insert into export_cell_line_data_set
select
id,
1 as count_experiments,
1 as count_compare_cell_lines,
0 as standard_deviation, --stddev(signal) as standard_deviation,
0 as mean,
'PUBLIC'::varchar(32) as namecode,
id as cell_line_data_set_id,
'PUBLIC'::varchar(32) as discreet,
2 as test_result_type_fk,
id as cell_line_data_set_ident_fk
from temp_intermediate_table;
insert into export_test_result
select nextval('hibernate_seq') as id,
cell_id,
value,
1 as count_experiments,
0 as standard_deviation,
cell_id as cell_line_fk,
cell_line_data_set_fk
from compare_collation_for_export;

\copy export_mol_targ_ident to /tmp/export_mol_targ_ident.csv csv null as 'null'
\copy export_cell_line_data_set_ident to /tmp/export_cell_line_data_set_ident.csv csv null as 'null'
\copy export_cell_line_data_set to /tmp/export_cell_line_data_set.csv csv null as 'null'
\copy export_test_result to /tmp/export_test_result.csv csv null as 'null'
\copy export_compare_cell_line to /tmp/export_compare_cell_line.csv csv null as 'null' 
\copy export_test_result_type to /tmp/export_test_result_type.csv csv null as 'null'

--scripts below pull the data into a dedicated COMPARE database:

truncate compare_cell_line cascade;
truncate test_result_type cascade;
truncate cell_line_data_set_ident cascade;
truncate mol_targ_ident cascade;
truncate cell_line_data_set cascade;
truncate test_result cascade;

\copy compare_cell_line from /tmp/export_compare_cell_line.csv csv null as 'null' 
\copy test_result_type from /tmp/export_test_result_type.csv csv null as 'null'
\copy cell_line_data_set_ident from /tmp/export_cell_line_data_set_ident.csv csv null as 'null'
\copy mol_targ_ident from /tmp/export_mol_targ_ident.csv csv null as 'null'
\copy cell_line_data_set from /tmp/export_cell_line_data_set.csv csv null as 'null'
\copy test_result from /tmp/export_test_result.csv csv null as 'null'
