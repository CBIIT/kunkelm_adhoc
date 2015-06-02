drop table if exists temp;

create table temp 
as
select 
ai.probe_set_id, ai.accession, ai.genecard,
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
probe_set_id character varying(1024) not null,
accession character varying(1024),
genecard character varying(1024),
tumor_name character varying(1024) not null,
tumor_type character varying(1024) not null,
ea_id character varying(1024) not null,
passage character varying(1024) not null,
replicate character varying(1024) not null,
value double precision,
primary key (id)
);

drop sequence if exists flat_data_seq;

create sequence flat_data_seq;

insert into flat_data(
id,
probe_set_id,
accession,
genecard,
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
genecard,
tumor_name,
tumor_type,
ea_id,
passage,
replicate,
value
from temp;

create index fd_genecard_idx on flat_data (genecard);
create index fd_tumor_name_idx on flat_data (tumor_name);
create index fd_genecard_tumor_name on flat_data(genecard, tumor_name);

--create index fd_replicate_idx on flat_data (replicate);
--create index fd_ea_id_idx on flat_data (ea_id);
--create index fd_probe_set_id_idx on flat_data (probe_set_id);
--create index fd_passage_idx on flat_data (passage);
--create index fd_tumor_type_idx on flat_data (tumor_type);
--create index fd_accession_idx on flat_data (accession);

