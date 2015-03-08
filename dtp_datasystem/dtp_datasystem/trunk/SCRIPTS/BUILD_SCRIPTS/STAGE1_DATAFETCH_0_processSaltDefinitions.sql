-- 09 Sept 2014

drop table if exists raw_salt_definitions;

create table raw_salt_definitions(
source varchar(1024),
smiles varchar(1024),
name varchar(1024)
);

\copy raw_salt_definitions from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/rawSaltDefinitions.txt csv header delimiter as E'\t'

-- import salts processed as fragments through Pipeline Pilot

drop table if exists processed_salts;

create table processed_salts(
source varchar(1024),
smiles varchar(1024),	
name varchar(1024),	
atomArray varchar(1024),	
can_smi varchar(1024),	
can_taut varchar(1024),
can_taut_strip_stereo varchar(1024),	
Molecular_Formula varchar(1024),	
Molecular_Weight double precision,	
CanonicalTautomer varchar(1024),	
NumberOfTautomers int);

\copy processed_salts from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/processedSalts.txt csv header delimiter as E'\t'

-- same salts

drop table if exists unique_salts;

create table unique_salts
as
select can_smi, can_taut, can_taut_strip_stereo, Molecular_Formula, Molecular_Weight, 
count(*), 
array_to_string(array_agg(distinct name), ',') as names, 
array_to_string(array_agg(distinct source), ',') as sources 
from processed_salts
group by 1, 2, 3, 4, 5 order by 2 desc;

create index unique_salts_can_taut_strip_stereo on unique_salts(can_taut_strip_stereo);

vacuum analyze unique_salts;

drop sequence if exists cmpd_known_salt_seq;

create sequence cmpd_known_salt_seq;

delete from cmpd_known_salt;

insert into cmpd_known_salt(id, can_smi, can_taut, can_taut_strip_stereo, salt_name, salt_mf, salt_mw, salt_comment)
select nextval('cmpd_known_salt_seq'), 'no salt', 'no salt', 'no salt', 'no salt', null, 0, 'no salt';

insert into cmpd_known_salt(id, can_smi, can_taut, can_taut_strip_stereo, salt_name, salt_mf, salt_mw, salt_comment)
select nextval('cmpd_known_salt_seq'), can_smi, can_taut, can_taut_strip_stereo, names, Molecular_Formula, Molecular_Weight, sources
from unique_salts;

alter table cmpd_known_salt add fixed varchar;

update cmpd_known_salt set fixed = replace(salt_name, '#', '');
update cmpd_known_salt set salt_name = fixed;
update cmpd_known_salt set fixed = btrim(salt_name, ' ');
update cmpd_known_salt set salt_name = fixed;

update cmpd_known_salt set fixed = regexp_replace(salt_comment, 'Schrodinger (\S+)ic salt', 'Schr \1');
update cmpd_known_salt set salt_comment = fixed;
update cmpd_known_salt set fixed = regexp_replace(salt_comment, 'Schrodinger solvent', 'Schr solv');
update cmpd_known_salt set salt_comment = fixed;

update cmpd_known_salt set fixed = replace(salt_comment, 'from NCGCCanonicalForm.java', 'NCGC');

update cmpd_known_salt set salt_comment = fixed;
