drop table if exists temp;

create table temp 
as
select 
ai.probe_set_id, ai.accession, ai.gene_symbol, ai.gene_title,
t.tumor_name,
tt.tumor_type,
ad.ea_id,
ad.passage,
ad.replicate,
ad.value
from affymetrix_data ad, affymetrix_identifier ai, tumor t, tumor_type tt
where ad.affymetrix_identifier_fk = ai.id
and ad.tumor_fk = t.id
and t.tumor_type_fk = tt.id;

drop table if exists flat_data;

create table flat_data (
id bigint not null,
probe_set_id varchar not null,
accession varchar,
gene_symbol varchar,
gene_title varchar,
tumor_name varchar not null,
tumor_type varchar not null,
ea_id varchar not null,
passage varchar not null,
replicate varchar not null,
value double precision,
primary key (id)
);

drop sequence if exists flat_data_seq;

create sequence flat_data_seq;

insert into flat_data(
id,
probe_set_id,
accession,
gene_symbol,
gene_title,
tumor_name,
tumor_type,
ea_id,
passage,
replicate,
value)
select
nextval('flat_data_seq'),
probe_set_id,
accession,
gene_symbol,
gene_title,
tumor_name,
tumor_type,
ea_id,
passage,
replicate,
value
from temp;

create index fd_probe_set_id_idx on flat_data(probe_set_id);  
create index fd_accession_idx on flat_data(accession);     
create index fd_genecard_idx on flat_data(genecard);      
create index fd_tumor_name_idx on flat_data(tumor_name);    
create index fd_tumor_type_idx on flat_data(tumor_type);    
create index fd_ea_id_idx on flat_data(ea_id);         
create index fd_passage_idx on flat_data(passage);       
create index fd_replicate_idx on flat_data(replicate);  

-- #####     ##     ####    ####             ##    #    #   ####
-- #    #   #  #   #       #                #  #   #    #  #    #
-- #    #  #    #   ####    ####           #    #  #    #  #
-- #####   ######       #       #          ######  #    #  #  ###
-- #       #    #  #    #  #    #          #    #   #  #   #    #
-- #       #    #   ####    ####           #    #    ##     ####

drop table if exists temp_avg_data;

create table temp_avg_data 
as
select 
probe_set_id, accession, gene_symbol, gene_title,
tumor_name,
tumor_type,
array_to_string(array_agg(ea_id), ',') as ea_ids,
passage,
count(value) as count_replicates,
avg(value) as mean,
stddev(value) as standard_deviation
from flat_data

--where 
--probe_set_id = '1007_s_at'
--and tumor_name = '786-0'

-- group by SKIPS the 7th entry 'ea_ids'
group by 1, 2, 3, 4, 5, 6, 8;

drop table if exists flat_avg_data;

create table flat_avg_data (
id bigint not null,
probe_set_id varchar not null,
accession varchar,
gene_symbol varchar,
gene_title varchar,
tumor_name varchar not null,
tumor_type varchar not null,
ea_ids varchar not null,
passage varchar not null,
int count_replicates,
mean double precision,
standard_deviation double precision,
primary key (id)
);

drop sequence if exists flat_avg_data_seq;

create sequence flat_avg_data_seq;

insert into flat_avg_data(
id,
probe_set_id,
accession,
gene_symbol,
gene_title,
tumor_name,
tumor_type,
ea_ids,
passage,
mean,
standard_deviation)
select
nextval('flat_avg_data_seq'),
probe_set_id,
accession,
gene_symbol,
gene_title,
tumor_name,
tumor_type,
ea_ids,
passage,
avg(value)
value
from temp_avg_data;

create index fd_probe_set_id_idx on flat_avg_data(probe_set_id);  
create index fd_accession_idx on flat_avg_data(accession);     
create index fd_genecard_idx on flat_avg_data(genecard);      
create index fd_tumor_name_idx on flat_avg_data(tumor_name);    
create index fd_tumor_type_idx on flat_avg_data(tumor_type);    
create index fd_ea_id_idx on flat_avg_data(ea_id);         
create index fd_passage_idx on flat_avg_data(passage);       
create index fd_replicate_idx on flat_avg_data(replicate);  












