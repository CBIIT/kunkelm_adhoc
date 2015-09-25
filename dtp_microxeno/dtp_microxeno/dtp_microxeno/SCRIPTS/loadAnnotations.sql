drop table if exists release_tumor_name_mouse;

create table release_tumor_name_mouse(
release varchar,
tumor_name varchar,
mouse varchar
);

\copy release_tumor_name_mouse from /tmp/tumorMouse.tsv delimiter as E'\t'

drop table if exists temp;

create table temp
as
select 
distinct release, tumor_name, mouse
from release_tumor_name_mouse;

select tumor_name, count(distinct release) 
from temp
group by 1
having count(distinct release) > 1;

select tumor_name, count(distinct mouse), array_to_string(array_agg(distinct mouse), ',') 
from temp
group by 1
having count(distinct mouse) > 1;

select * from release_tumor_name_mouse
where tumor_name = 'SJSA-1';

-- designators for tumor, passage, replicate
-- distilled from Excel workbooks from Sergio

drop table if exists annotations_import;

create table annotations_import(
release	varchar,
ea_file	varchar,
mouse	varchar,	
tumor_type	varchar,	
tumor_name	varchar,	
passage	varchar,
replicate	varchar
);

\copy annotations_import from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/FirstAndSecondReleases.tsv csv header delimiter as E'\t'

alter table annotations_import add ea_id varchar;

update annotations_import 
set ea_id = ea_file
where release = 'FIRST_RELEASE';

update annotations_import 
set ea_id = left(ea_file, position('_H133' in ea_file) - 1)
where release = 'SECOND_RELEASE';

-- cel files

drop table if exists cel_files;

create table cel_files(
cel_file_name varchar
);

\copy cel_files from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/celFileList

alter table cel_files add ea_id varchar;

update cel_files 
set ea_id = left(cel_file_name, position('_H133' in cel_file_name) - 1);

select ea_id from cel_files
except
select ea_id from annotations_import;

select ea_id from annotations_import
except
select ea_id from cel_files;

