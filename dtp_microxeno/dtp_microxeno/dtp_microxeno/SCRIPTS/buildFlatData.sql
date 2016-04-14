drop table if exists temp;

create table temp 
as
select 
ai.probe_set_id, ai.accession, ai.gene_symbol, ai.gene_title,
ir.tumor_name,
tt.tumor_type,
ir.ea_file,
ir.passage,
ir.replicate,
ir.value
from import_rma ir, affymetrix_identifier ai, tumor t, tumor_type tt
where ir.probe_set_id = ai.probe_set_id
and ir.tumor_name = t.tumor_name
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
ea_file varchar not null,
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
ea_file,
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
ea_file,
passage,
replicate,
value
from temp;

create index fd_accession_idx on flat_data(accession);     
create index fd_ea_file_idx on flat_data(ea_file);         
create index fd_gene_symbol_idx on flat_data(gene_symbol);      
create index fd_probe_set_id_idx on flat_data(probe_set_id);  
create index fd_tumor_name_idx on flat_data(tumor_name);    
create index fd_tumor_type_idx on flat_data(tumor_type);   
create index fd_passage_idx on flat_data(passage);       


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
array_to_string(array_agg(ea_file), ',') as ea_files,
passage,
count(value) as count_replicates,
avg(value) as mean,
stddev(value) as standard_deviation
from flat_data

--where 
--probe_set_id = '1007_s_at'
--and tumor_name = '786-0'

-- group by SKIPS the 7th entry 'ea_files'
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
ea_files varchar not null,
passage varchar not null,
count_replicates int,
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
ea_files,
passage,
count_replicates,
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
ea_files,
passage,
count_replicates,
mean,
standard_deviation
from temp_avg_data;

create index fad_probe_set_id_idx on flat_avg_data(probe_set_id);  
create index fad_accession_idx on flat_avg_data(accession);     
create index fad_gene_symbol_idx on flat_avg_data(gene_symbol);      
create index fad_tumor_name_idx on flat_avg_data(tumor_name);    
create index fad_tumor_type_idx on flat_avg_data(tumor_type);    
create index fad_passage_idx on flat_avg_data(passage);       













