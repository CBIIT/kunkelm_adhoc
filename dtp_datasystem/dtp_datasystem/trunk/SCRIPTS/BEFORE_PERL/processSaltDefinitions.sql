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

drop table if exists same_salts;

create table same_salts
as
select can_taut_strip_stereo, count(*), array_to_string(array_agg(name), ',') as names 
from processed_salts
group by 1 order by 2 desc;

create index same_salts_can_taut_strip_stereo on same_salts(can_taut_strip_stereo);

vacuum analyze verbose same_salts;