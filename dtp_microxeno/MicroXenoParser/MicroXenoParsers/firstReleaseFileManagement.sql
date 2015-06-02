\c microxenodb

--
--
--
--
--

drop table if exists files_on_drive;

create table files_on_drive(
file_name varchar(1024)
);

\copy files_on_drive from /tmp/fileList.csv csv

alter table files_on_drive add ea_id varchar(128);
update files_on_drive set ea_id = substring(file_name from 1 for (position('_H133' in file_name) - 1)); 

--
--
--
--
--

drop table if exists full_release;

create table full_release(
serial_number varchar(1024),
ea_id varchar(1024),
specimen_id varchar(1024),
cell_type varchar(1024),
tumor_type varchar(1024),
histology varchar(1024),
sample_type varchar(1024),
passage varchar(1024),
replicates varchar(1024),
serum_conc varchar(1024),
array_source varchar(1024),
array_type varchar(1024),
comments varchar(1024)
);

\copy full_release from '/home/mwkunkel/PROJECTS/CURRENT/MICROXENO_DATA/Master array workbook-SA_02-28-2013.csv' csv header quote as '"'

--
--
--
--
--

drop table if exists first_release;

create table first_release(

ea_id varchar(1024),
specimen_id varchar(1024),
tumor_name varchar(1024),
tumor_type varchar(1024),
histology varchar(1024),
sample_type varchar(1024),
passage varchar(1024),
replicate varchar(1024),
mouse_strain varchar(1024),
array_source varchar(1024),
array_type varchar(1024),
comments varchar(1024)
);

\copy first_release from /home/mwkunkel/PROJECTS/CURRENT/MICROXENO_DATA/MicroXenoFirstRelease.csv csv header quote as '"'

update first_release set tumor_name = 'COLO 205' where tumor_name like 'COLO 205%';
update first_release set tumor_name = 'HCC-2998' where tumor_name like 'HCC-2998%';
update first_release set tumor_name = 'HCT-15' where tumor_name like 'HCT-15%';
update first_release set tumor_name = 'SR' where tumor_name like 'SR%';
update first_release set tumor_name = 'SW-620' where tumor_name like 'SW-620%';
update first_release set tumor_name = 'U251' where tumor_name like 'U251%';
update first_release set tumor_name = 'A549/Asc-1' where tumor_name like 'A549%Asc%';
update first_release set passage = 'P00' where passage = 'P0';
update first_release set passage = 'P04' where passage = 'P05';

select ea_id, tumor_name, replicates, passage from first_release 
where (tumor_name, replicates, passage)
in
(
select tumor_name, replicates, passage 
from first_release where passage != 'N/A' group by 1,2,3 having count(*) > 1
)
order by 2, 3, 4;

--
--
--
--
--

drop table if exists crosshyb;

create table crosshyb(
ea_id varchar(1024),
sequence_number  varchar(1024),
specimen_id varchar(1024),
tumor_name varchar(1024),
tumor_type varchar(1024),
histology varchar(1024),
sample_type varchar(1024),
passage varchar(1024),
replicates varchar(1024),
xenograft_mouse_strain varchar(1024),
array_source varchar(1024),
array_type varchar(1024),
comments varchar(1024));

\copy crosshyb from /home/mwkunkel/PROJECTS/CURRENT/MICROXENO_DATA/MouseHumanCrossHyb.csv csv header quote as '"'

drop table if exists crosshyb_files;

create table crosshyb_files( file_name varchar(1024), ea_id varchar(1024));

\copy crosshyb_files from /newdrive/MICROXENO/crosshyb_file_list.txt

alter table crosshyb add file_name varchar(1024);

update crosshyb 
set file_name = crosshyb_files.file_name 
from crosshyb_files
where crosshyb.ea_id = crosshyb_files.ea_id;

