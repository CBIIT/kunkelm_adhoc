\c microxenodb;

-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------
-------------------------------------------------------------------------------

--ONLY WANT DOING IF AFFY or UNIGENE are changed

----------------------affy identifiers

drop table if exists hg_u133_plus2;

create table hg_u133_plus2(
probe_set_name varchar(1024),
gene_symbol varchar(1024),
representative_public_id varchar(1024));

--this file is output from the parseAffyAnnotations method in main in MicroXenoParser project
\copy hg_u133_plus2 from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/HG_U133_Plus_2_PARSED.csv csv header

create index hg_u133_plus2_probe_set_name on hg_u133_plus2(probe_set_name);
create index hg_u133_plus2_representative_public_id on hg_u133_plus2(representative_public_id);

vacuum analyze hg_u133_plus2;

----------------------unigene identifiers

drop table if exists unigene236;

create table unigene236(
cluster varchar(1024),
description varchar(1024),
gene_symbol varchar(1024),
accession varchar(1024));

--this file is output from the parseUnigene method in main in MicroXenoParser project
\copy unigene236 from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/Unigene236.tab

create index unigene236_accession on unigene236(accession);

vacuum analyze unigene236;

----------------------resolve affymetrix id against Unigene

drop table if exists temp;

create table temp as
select hg.probe_set_name, hg.representative_public_id, hg.gene_symbol, u.accession, u.gene_symbol as unigene_gene
from hg_u133_plus2 hg left outer join unigene236 u
on hg.representative_public_id = u.accession;

-- sanity checks

select count(*) from temp where accession is null;
select count(*) from temp where accession is not null;
select count(*) from temp;

----------------------populate affymetrix_identifier 

drop table if exists affymetrix_identifier cascade;

CREATE TABLE affymetrix_identifier (
id bigint NOT NULL,
probe_set_name character varying(1024) NOT NULL,
accession character varying(1024),
genecard character varying(1024)
);

drop sequence if exists affymetrix_identifier_seq;
create sequence affymetrix_identifier_seq;

insert into affymetrix_identifier (id, probe_set_name, accession, genecard)
select nextval('affymetrix_identifier_seq'), probe_set_name, accession, gene_symbol
from temp;

create index affymetrix_id_probe_set_name on affymetrix_identifier (probe_set_name);
create index affymetrix_id_accession on affymetrix_identifier (accession);
create index affymetrix_id_genecard on affymetrix_identifier (genecard);
alter table affymetrix_identifier add primary key (id);

----------------------SANITY CHECKS BEFORE UNDERTAKING DATA BUILD

drop table if exists ea_data_raw_upload;

create table ea_data_raw_upload(
file_name varchar(128),
probe_set varchar(128),
stat_pairs integer, 
stat_pairs_used integer, 
signal double precision, 
detection varchar(128), 
detection_p_value double precision); 

--UPLOAD the .csv files to the table via sql from /tmp/script.sql from createSql in Main in MicroXenoParser

--select count(*) from ea_data_raw_upload;

alter table ea_data_raw_upload add ea_id varchar(128);
update ea_data_raw_upload set ea_id = substring(file_name from 1 for (position('_H133' in file_name) - 1)); 

create index adru_probe_set on ea_data_raw_upload(probe_set);
create index adru_ea_id on ea_data_raw_upload(ea_id);

vacuum analyze ea_data_raw_upload;

-----------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------
-----------------------------------------------------------------------------------------------

-- list of first release cell lines 

--drop table if exists first_release;
--
--create table first_release(
--tumor_name varchar(1024),
--innoculation_count int,
--hormones varchar(1024),
--brei varchar(1024),
--p1_matrigel varchar(1024),
--atcc_name varchar(1024),
--atcc_catalog varchar(1024),
--distributable varchar(1024),
--dtp_catalog varchar(1024),
--comment varchar(1024),
--the_count varchar(1024),
--Melinda  varchar(1024));
--
--\copy first_release from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/first_release_cell_lines.csv csv header delimiter as E'\t' quote as '"'

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
comments varchar(1024));

\copy first_release from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/MicroXenoFirstRelease.csv csv header quote as '"' null as ''

----------------------parse out tumor types and tumors for fk relationships

drop table if exists tumor_type cascade;

drop table if exists temp;

create table temp 
as 
select distinct tumor_type 
from first_release 
order by tumor_type asc;

drop sequence if exists tumor_type_seq;
create sequence tumor_type_seq;

create table tumor_type as select nextval('tumor_type_seq') as "id", tumor_type from temp;

create index tumor_type_tumor_type on tumor_type(tumor_type);

alter table tumor_type add primary key (id);

drop table if exists tumor cascade;

drop table if exists temp;
create table temp as select distinct tumor_name, tumor_type from first_release order by 1, 2;
drop sequence if exists tumor_seq;
create sequence tumor_seq;
create table tumor as 
select nextval('tumor_seq') as "id", tumor_name, tt.id as "tumor_type_fk" 
from temp t, tumor_type tt
where t.tumor_type = tt.tumor_type;

create index tumor_tumor_name on tumor(tumor_name);

alter table tumor add primary key (id);

--
--drop table if exists distinct_ea_id;
--
--create table distinct_ea_id as 
--select ea_id 
--from ea_data_raw_upload 
--group by ea_id;
--
--drop table if exists ea_id_failures; 
--
--create table ea_id_failures as
--select ea_id 
--from distinct_ea_id 
--except 
--select ea_id 
--from first_release;
--
--drop table if exists distinct_probe_set;
--
--create table distinct_probe_set as 
--select probe_set 
--from ea_data_raw_upload 
--group by probe_set;
--
--drop table if exists probe_set_failures; 
--
--create table probe_set_failures as
--select probe_set 
--from distinct_probe_set 
--except 
--select probe_set_name 
--from affymetrix_identifier;
--

vacuum analyze affymetrix_identifier;
vacuum analyze ea_data_raw_upload;

create index first_release_tumor_name on first_release(tumor_name);
create index first_release_ea_id on first_release(ea_id);

vacuum analyze first_release;

----------------------create affymetrix_data

drop sequence if exists affymetrix_data_seq;
create sequence affymetrix_data_seq;

drop table affymetrix_data cascade;

create table affymetrix_data (
id bigint not null,
ea_id character varying(1024) not null,        
present_absent character varying(1024) not null,
the_pvalue double precision not null,
value double precision not null,
replicate character varying(1024) not null,
passage character varying(1024) not null,
tumor_fk bigint not null,
affymetrix_identifier_fk bigint not null
);

insert into affymetrix_data (id, ea_id, present_absent, the_pvalue, value, replicate, passage, tumor_fk, affymetrix_identifier_fk)
select
nextval('affymetrix_data_seq'), fr.ea_id, adru.detection, adru.detection_p_value, adru.signal, fr.replicate, fr.passage, t.id, ai.id
from ea_data_raw_upload adru, first_release fr, affymetrix_identifier ai, tumor t 
where adru.ea_id = fr.ea_id
and fr.tumor_name = t.tumor_name 
and adru.probe_set = ai.probe_set_name;


drop table if exists temp;

create table temp 
as
select t.tumor_name, ai.probe_set_name, ai.accession, ai.genecard, ad.ea_id, ad.present_absent, ad.the_pvalue, ad.value, ad.replicate, ad.passage
from affymetrix_data ad, affymetrix_identifier ai, tumor t
where ad.affymetrix_identifier_fk = ai.id
and ad.tumor_fk = t.id
and ai.genecard = 'FXYD5'




















-- create index SOLELY TO USE for calculating averages
-- for accessing substantial fractions of the data, indexes as implemented by PG are LESS EFFICIENT than seq scan
--create index affymetrix_data_agg_idx on affymetrix_data(passage, tumor_fk, affymetrix_identifier_fk);

----------------------calculate averages by passage at 4 different p values 0.05, 0.01, 0.005, 0.001 

drop table if exists aggregate_by_passage;

create table aggregate_by_passage (
the_pvalue double precision not null,
avg_value double precision not null,
passage character varying(1024) not null,
tumor_fk bigint not null,
affymetrix_identifier_fk bigint not null
);

insert into aggregate_by_passage
select 0.001, avg(value), passage, tumor_fk, affymetrix_identifier_fk
from affymetrix_data
where present_absent = 'P'
and the_pvalue <= 0.001
group by passage, tumor_fk, affymetrix_identifier_fk;

insert into aggregate_by_passage
select 0.005, avg(value), passage, tumor_fk, affymetrix_identifier_fk
from affymetrix_data
where present_absent = 'P'
and the_pvalue <= 0.005
group by passage, tumor_fk, affymetrix_identifier_fk;

insert into aggregate_by_passage
select 0.01, avg(value), passage, tumor_fk, affymetrix_identifier_fk
from affymetrix_data
where present_absent = 'P'
and the_pvalue <= 0.01
group by passage, tumor_fk, affymetrix_identifier_fk;

insert into aggregate_by_passage
select 0.05, avg(value), passage, tumor_fk, affymetrix_identifier_fk
from affymetrix_data
where present_absent = 'P'
and the_pvalue <= 0.05
group by passage, tumor_fk, affymetrix_identifier_fk;

----------------------drop the index used for aggregates

drop index if exists affymetrix_data_agg_idx;

----------------------make the inserts

insert into affymetrix_data (id, ea_id, present_absent, the_pvalue, value, replicate, passage, tumor_fk, affymetrix_identifier_fk)
select
nextval('affymetrix_data_seq'), 'calculated values', 'P', the_pvalue, avg_value, 'PASSAGE AVERAGE', passage, tumor_fk, affymetrix_identifier_fk
from aggregate_by_passage;

----------------------recreate the indexes

alter table affymetrix_data add primary key (id);

create index tumor_fk_index on AFFYMETRIX_DATA (TUMOR_FK);
create index atd_replicate_index on AFFYMETRIX_DATA (REPLICATE);
create index affymetrix_identifier_fk_index on AFFYMETRIX_DATA (AFFYMETRIX_IDENTIFIER_FK);
create index atd_passage_index on AFFYMETRIX_DATA (PASSAGE);
create index atd_present_absent_index on AFFYMETRIX_DATA (PRESENT_ABSENT);

alter table AFFYMETRIX_DATA 
add constraint AFFYMETRIX_DATA_TUMOR_FKC 
foreign key (TUMOR_FK) 
references TUMOR;

alter table AFFYMETRIX_DATA 
add constraint AFFYMETRIX_DATA_AFFYMETRIX_IDC 
foreign key (AFFYMETRIX_IDENTIFIER_FK) 
references AFFYMETRIX_IDENTIFIER;

