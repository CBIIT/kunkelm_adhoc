\c pptpdb

drop sequence if exists hibernate_seq;
create sequence hibernate_seq;

--stats

drop table if exists temp_upload_stats;

CREATE TABLE temp_upload_stats (
compound character varying(1024),
cell_line character varying(1024),
test_number integer,
stage character varying(1024),
group_role character varying(1024),
"day" integer,
mouse_number integer,
tumor_volume double precision,
log_tumor_volume double precision
);

\copy temp_upload_stats from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/csvFiles/statsForUpload.csv.processed' csv null as 'null'
\copy temp_upload_stats from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/csvFiles/eventsForUpload.csv.processed' csv null as 'null'

--events

--drop table if exists temp_upload_events;
--
--CREATE TABLE temp_upload_events (
--compound character varying(1024),
--cell_line character varying(1024),
--test_number integer,
--stage character varying(1024),
--group_role character varying(1024),
--"day" integer,
--mouse_number integer,
--tumor_volume double precision,
--log_tumor_volume double precision
--);
--
--\copy temp_upload_events from '/home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/csvFiles/eventsForUpload.csv.processed' csv null as 'null'

--censored

drop table if exists temp_upload_censored;

create table temp_upload_censored (
compound varchar(1024),
cell_line varchar(1024),
test_number integer,
group_role varchar(1024),
the_n1 integer,
the_d1 integer,
the_e1 integer,
the_n2 integer,
surviving_percent integer,
count_mouse_events integer,
median_days_to_event double precision,
the_p_value_flag character varying(1024),
the_p_value double precision,
efs_t_over_c_flag character varying(1024),
efs_t_over_c double precision,
median_r_t_v_flag character varying(1024),
median_r_t_v double precision,
average_r_t_v_at_day_for_t_ove double precision,
t_over_c double precision,
day_for_t_over_c integer,
the_t_over_c_p_value_flag character varying(1024),
the_t_over_c_p_value double precision,
count_p_d integer,
count_p_d1 integer,
count_p_d2 integer,
count_s_d integer,
count_p_r integer,
count_c_r integer,
count_m_c_r integer,
response_median_score double precision,
overall_group_median_response varchar(1024)
);

\copy temp_upload_censored from /home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/csvFiles/censoredForUpload.csv.processed csv null as 'null'

--summary

drop table if exists temp_upload_summary;

create table temp_upload_summary (
compound varchar(1024),
cell_line varchar(1024),
test_number integer,
the_t_over_c_response varchar(1024),
the_e_f_s_response varchar(1024),
the_objective_response varchar(1024)
);

\copy temp_upload_summary from /home/mwkunkel/DATA_DEPOT_IMPORTANT/PPTP/csvFiles/summaryForUpload.csv.processed with delimiter ',' null as 'null'

-- data build starts by processing the stats files
-- any other data (event times, summaries, etc.) will be
-- a subset of the mouse info in stats

--truncate all tables
truncate table mtd_study cascade; 
truncate table cell_line_group cascade; 
truncate table mouse_group cascade; 
truncate table summary cascade; 
truncate table mouse cascade; 
truncate table mouse_event_times cascade;
truncate table mouse_crosstab cascade;

-- use info from master to create extended version of temp_upload_stats

-- mtd_study_key, cell_line_group_key, and mouse_group_key have placeholders

drop table if exists temp_upload_stats_extended;

create table temp_upload_stats_extended (
compound_identifier character varying(1024) not null,
dose double precision not null,
concentration_unit character varying(1024) not null,
treatment_route character varying(1024) not null,
schedule character varying(1024) not null,
vehicle character varying(1024) not null,
implant_site character varying(1024) not null,
cell_line_identifier character varying(1024) not null,
mouse_type character varying(1024) not null,
test_number integer not null,				
group_role character varying(1024) not null,				
compound_type_key bigint not null,
concentration_unit_type_key bigint not null,
treatment_route_type_key bigint not null,
schedule_type_key bigint not null,
vehicle_type_key bigint not null,
implant_site_type_key bigint not null,
cell_line_type_key bigint not null,
mouse_type_key bigint not null,				
group_role_type_key bigint not null,				 
day integer not null,
mouse_number integer not null, 
tumor_volume double precision not null, 
log_tumor_volume double precision,
mtd_study_key bigint not null,
cell_line_group_key bigint not null,
mouse_group_key bigint not null
);

insert into temp_upload_stats_extended
(compound_identifier,
dose,
concentration_unit,
treatment_route,
schedule,
vehicle,
implant_site,
cell_line_identifier,
mouse_type,
test_number,				
group_role,				
compound_type_key,
concentration_unit_type_key,
treatment_route_type_key,
schedule_type_key,
vehicle_type_key,
implant_site_type_key,
cell_line_type_key,
mouse_type_key,				
group_role_type_key,
day,
mouse_number, 
tumor_volume, 
log_tumor_volume,
mtd_study_key,
cell_line_group_key,
mouse_group_key
)
select 
tus.compound,
m.dose,
m.concentration_unit,
m.treatment_route,
m.schedule,
m.vehicle,
m.implant_site,
tus.cell_line,
m.mouse_type,
tus.test_number,
tus.group_role,
m.compound_type_key,
m.concentration_unit_type_key,
m.treatment_route_type_key,
m.schedule_type_key,
m.vehicle_type_key,
m.implant_site_type_key,
m.cell_line_type_key,
m.mouse_type_key,
m.group_role_type_key,
tus.day,
tus.mouse_number ,
tus.tumor_volume ,
tus.log_tumor_volume,
10101, --mtd_study_key
10101, --cell_line_group_key
10101  --mouse_group_key
from temp_upload_stats tus, master m
where
tus.compound=m.compound_identifier
and tus.cell_line=m.cell_line_identifier
and tus.test_number=m.test_number                    
and tus.group_role=m.group_role;               

--
--
-- mtd_study
--
--

drop table if exists temp;
create table temp as 
select distinct 
compound_type_key 
from temp_upload_stats_extended;

insert into mtd_study
(id,
compound_type_fk)
select
nextval('hibernate_seq'),
compound_type_key
from temp;

--update temp_upload_stats_extended

update temp_upload_stats_extended tuse
set mtd_study_key = mtds.id
from mtd_study mtds
where 
tuse.compound_type_key = mtds.compound_type_fk;

--
--
-- cell_line_group
--
--

drop table if exists temp;
create table temp as 
select distinct 
dose,
test_number,
cell_line_type_key,
mtd_study_key,
schedule_type_key,
vehicle_type_key,
concentration_unit_type_key,
mouse_type_key,
treatment_route_type_key,
implant_site_type_key
from temp_upload_stats_extended;

insert into cell_line_group 
(id,
dose,
test_number,
cell_line_type_fk,
mtd_study_fk,
schedule_type_fk,
vehicle_type_fk,
concentration_unit_type_fk,
mouse_type_fk,
treatment_route_type_fk,
implant_site_type_fk
) select
nextval('hibernate_seq'),
dose,
test_number,
cell_line_type_key,
mtd_study_key,
schedule_type_key,
vehicle_type_key,
concentration_unit_type_key,
mouse_type_key,
treatment_route_type_key,
implant_site_type_key
from temp;

update temp_upload_stats_extended tuse
set cell_line_group_key = clg.id
from cell_line_group clg
where 
tuse.dose= clg.dose
and tuse.test_number= clg.test_number
and tuse.cell_line_type_key= clg.cell_line_type_fk
and tuse.mtd_study_key= clg.mtd_study_fk
and tuse.schedule_type_key= clg.schedule_type_fk
and tuse.vehicle_type_key= clg.vehicle_type_fk
and tuse.concentration_unit_type_key= clg.concentration_unit_type_fk
and tuse.mouse_type_key= clg.mouse_type_fk
and tuse.treatment_route_type_key= clg.treatment_route_type_fk
and tuse.implant_site_type_key=clg.implant_site_type_fk;

--
--
-- mouse_group
--
--

drop table if exists temp;
create table temp as 
select distinct	
day,
group_role_type_key,
cell_line_group_key
from temp_upload_stats_extended;

insert into mouse_group
(id,
day,
group_role_type_fk,
cell_line_group_fk)
select
nextval('hibernate_seq'),
day,
group_role_type_key,
cell_line_group_key
from temp;

update temp_upload_stats_extended tuse
set mouse_group_key = mg.id
from mouse_group mg
where
tuse.day = mg.day
and tuse.group_role_type_key = mg.group_role_type_fk
and tuse.cell_line_group_key = mg.cell_line_group_fk;

--
--
-- MOUSE
--
--

insert into mouse
(ID,
MOUSE_NUMBER,
TUMOR_VOLUME,
LOG_TUMOR_VOLUME,
MOUSE_GROUP_FK)
select
nextval('hibernate_seq'),
mouse_number,
tumor_volume,
log_tumor_volume,
mouse_group_key
from temp_upload_stats_extended;

--
--
-- MOUSE CROSSTAB
--
--

drop table if exists temp;

create table temp as
select * 
from crosstab(
'select mouse_group_fk, mouse_number, tumor_volume from mouse order by 1,2',
'select distinct mouse_number from mouse order by 1')
as ct(
mouse_group_fk bigint, 
mouse1 double precision,
mouse2 double precision,
mouse3 double precision,
mouse4 double precision,
mouse5 double precision,
mouse6 double precision,
mouse7 double precision,
mouse8 double precision,
mouse9 double precision,
mouse10 double precision
);

insert into mouse_crosstab select * from temp;

update mouse_group set mouse_crosstab_fk = id;

--
--
-- CENSORED
--
--

--extend temp_upload_censored to include full info from master

drop table if exists temp_upload_censored_extended;

create table temp_upload_censored_extended (
compound varchar(1024),
cell_line varchar(1024),
test_number integer,
group_role varchar(1024),
the_n1 integer,
the_d1 integer,
the_e1 integer,
the_n2 integer,
surviving_percent integer,
count_mouse_events integer,
median_days_to_event double precision,
the_p_value_flag character varying(1024),
the_p_value double precision,
efs_t_over_c_flag character varying(1024),
efs_t_over_c double precision,
median_r_t_v_flag character varying(1024),
median_r_t_v double precision,
average_r_t_v_at_day_for_t_ove double precision,
t_over_c double precision,
day_for_t_over_c integer,
the_t_over_c_p_value_flag character varying(1024),
the_t_over_c_p_value double precision,
count_p_d integer,
count_p_d1 integer,
count_p_d2 integer,
count_s_d integer,
count_p_r integer,
count_c_r integer,
count_m_c_r integer,
response_median_score double precision,
overall_group_median_response varchar(1024),
dose double precision not null,
concentration_unit varchar(1024) not null,
treatment_route varchar(1024) not null,
schedule varchar(1024) not null,
vehicle varchar(1024) not null,
implant_site varchar(1024) not null,
mouse_type varchar(1024) not null,
compound_type_key bigint not null,
concentration_unit_type_key bigint not null,
treatment_route_type_key bigint not null,
schedule_type_key bigint not null,
vehicle_type_key bigint not null,
implant_site_type_key bigint not null,
cell_line_type_key bigint not null,
mouse_type_key bigint not null,
group_role_type_key bigint not null,

mtd_study_key bigint not null,
cell_line_group_key bigint not null,
mouse_group_key bigint not null
);

insert into temp_upload_censored_extended (
compound,
cell_line,
test_number,
group_role,
the_n1,
the_d1,
the_e1,
the_n2,
surviving_percent,
count_mouse_events,
median_days_to_event,
the_p_value_flag,
the_p_value,
efs_t_over_c_flag,
efs_t_over_c,
median_r_t_v_flag,
median_r_t_v,
average_r_t_v_at_day_for_t_ove,
t_over_c,
day_for_t_over_c,
the_t_over_c_p_value_flag,
the_t_over_c_p_value,
count_p_d,
count_p_d1,
count_p_d2,
count_s_d,
count_p_r,
count_c_r,
count_m_c_r,
response_median_score,
overall_group_median_response,
dose,
concentration_unit,
treatment_route,
schedule,
vehicle,
implant_site,
mouse_type,
compound_type_key,
concentration_unit_type_key,
treatment_route_type_key,
schedule_type_key,
vehicle_type_key,
implant_site_type_key,
cell_line_type_key,
mouse_type_key,
group_role_type_key,
mtd_study_key,
cell_line_group_key,
mouse_group_key
)
select
compound,
tuc.cell_line,
tuc.test_number,
tuc.group_role,
tuc.the_n1,
tuc.the_d1,
tuc.the_e1,
tuc.the_n2,
tuc.surviving_percent,
tuc.count_mouse_events,
tuc.median_days_to_event,
tuc.the_p_value_flag,
tuc.the_p_value,
tuc.efs_t_over_c_flag,
tuc.efs_t_over_c,
tuc.median_r_t_v_flag,
tuc.median_r_t_v,
tuc.average_r_t_v_at_day_for_t_ove,
tuc.t_over_c,
tuc.day_for_t_over_c,
tuc.the_t_over_c_p_value_flag,
tuc.the_t_over_c_p_value,
tuc.count_p_d,
tuc.count_p_d1,
tuc.count_p_d2,
tuc.count_s_d,
tuc.count_p_r,
tuc.count_c_r,
tuc.count_m_c_r,
tuc.response_median_score,
tuc.overall_group_median_response,
m.dose,
m.concentration_unit,
m.treatment_route,
m.schedule,
m.vehicle,
m.implant_site,
m.mouse_type,
m.compound_type_key,
m.concentration_unit_type_key,
m.treatment_route_type_key,
m.schedule_type_key,
m.vehicle_type_key,
m.implant_site_type_key,
m.cell_line_type_key,
m.mouse_type_key,
m.group_role_type_key,
10101,
10101,
10101
from temp_upload_censored tuc, master m
where
tuc.compound=m.compound_identifier
and tuc.cell_line=m.cell_line_identifier
and tuc.test_number=m.test_number                    
and tuc.group_role=m.group_role;

--update the keys for mtd_study and cell_line_group 

update temp_upload_censored_extended tuce
set mtd_study_key = mtds.id
from mtd_study mtds
where 
tuce.compound_type_key = mtds.compound_type_fk;

update temp_upload_censored_extended tuce
set cell_line_group_key = clg.id
from cell_line_group clg
where 
tuce.dose= clg.dose
and tuce.test_number= clg.test_number
and tuce.cell_line_type_key= clg.cell_line_type_fk
and tuce.mtd_study_key= clg.mtd_study_fk
and tuce.schedule_type_key= clg.schedule_type_fk
and tuce.vehicle_type_key= clg.vehicle_type_fk
and tuce.concentration_unit_type_key= clg.concentration_unit_type_fk
and tuce.mouse_type_key= clg.mouse_type_fk
and tuce.treatment_route_type_key= clg.treatment_route_type_fk
and tuce.implant_site_type_key=clg.implant_site_type_fk;

--insert the data

insert into summary(
id,
the_n1,
the_d1,
the_e1,
the_n2,
count_mouse_events,
median_days_to_event,
the_p_value_flag,
the_p_value,
efs_t_over_c_flag,
efs_t_over_c,
median_r_t_v_flag,
median_r_t_v,
t_over_c,
day_for_t_over_c,
the_t_over_c_p_value_flag,
the_t_over_c_p_value,
count_p_d,
count_p_d1,
count_p_d2,
count_s_d,
count_p_r,
count_c_r,
count_m_c_r,
response_median_score,
overall_group_median_response,
surviving_percent,
average_r_t_v_at_day_for_t_ove,
cell_line_group_fk,
group_role_type_fk
)
select
nextval('hibernate_seq'),
the_n1,
the_d1,
the_e1,
the_n2,
count_mouse_events,
median_days_to_event,
the_p_value_flag,
the_p_value,
efs_t_over_c_flag,
efs_t_over_c,
median_r_t_v_flag,
median_r_t_v,
t_over_c,
day_for_t_over_c,
the_t_over_c_p_value_flag,
the_t_over_c_p_value,
count_p_d,
count_p_d1,
count_p_d2,
count_s_d,
count_p_r,
count_c_r,
count_m_c_r,
response_median_score,
overall_group_median_response,
surviving_percent,
average_r_t_v_at_day_for_t_ove,
cell_line_group_key,
group_role_type_key
from temp_upload_censored_extended;

