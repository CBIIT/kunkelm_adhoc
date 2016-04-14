\c microxenodb;

--                                          ##
--   ##    ######  ######   #   #          #  #            #    #   ####
--  #  #   #       #         # #            ##             #    #  #    #
-- #    #  #####   #####      #            ###             #    #  #
-- ######  #       #          #           #   # #          #    #  #  ###
-- #    #  #       #          #           #    #           #    #  #    #
---#    #  #       #          #            #### #           ####    ####


--ONLY WANT DOING IF AFFY or UNIGENE are changed

drop table if exists hg_u133_plus2;

create table hg_u133_plus2(
probe_set_id varchar,
gene_title varchar,
gene_symbol varchar,
accession varchar);

--this file is output from the parseAffyAnnotations method in main in MicroXenoParser project
\copy hg_u133_plus2 from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/HG_U133_Plus_2_MIN_PARSED.csv csv header quote as '"'

create index hg_u133_plus2_probe_set_id on hg_u133_plus2(probe_set_id);
create index hg_u133_plus2_accession on hg_u133_plus2(accession);

vacuum analyze hg_u133_plus2;

----------------------unigene identifiers

drop table if exists unigene236;

create table unigene236(
cluster varchar,
description varchar,
gene_symbol varchar,
accession varchar);

--this file is output from the parseUnigene method in main in MicroXenoParser project
\copy unigene236 from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/Unigene236.csv csv header quote as '"'

create index unigene236_accession on unigene236(accession);

vacuum analyze unigene236;

----------------------resolve affymetrix id against Unigene

drop table if exists temp;

create table temp as
select hg.probe_set_id, hg.accession as hg_accession, hg.gene_symbol as hg_gene_symbol, u.accession as u_accession, u.gene_symbol as u_gene_symbol
from hg_u133_plus2 hg left outer join unigene236 u
on hg.accession = u.accession;

-- sanity checks

select count(*) from temp where u_accession is null;
select count(*) from temp where u_accession is not null;
select count(*) from temp;

-- ######    ##            #        ####    ####   #    #  #    #  #####
-- #        #  #           #       #    #  #    #  #   #   #    #  #    #
-- #####   #    #          #       #    #  #    #  ####    #    #  #    #
-- #       ######          #       #    #  #    #  #  #    #    #  #####
-- #       #    #          #       #    #  #    #  #   #   #    #  #
-- ######  #    #          ######   ####    ####   #    #   ####   #

drop table if exists ea_lookup;

create table ea_lookup(
release	varchar,
ea_file	varchar,
mouse_strain varchar,
tumor_type varchar,	
tumor_name varchar,	
passage varchar,	
replicate varchar
);

\copy ea_lookup from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/FIRST_AND_SECOND_RELEASES_EA_LOOKUP.csv csv header

update ea_lookup set tumor_type = upper(trim( both ' ' from tumor_type));
update ea_lookup set tumor_name = trim( both ' ' from tumor_name);

update ea_lookup set tumor_name = 'COLO 205' where tumor_name like 'COLO 205%';
update ea_lookup set tumor_name = 'HCC-2998' where tumor_name like 'HCC-2998%';
update ea_lookup set tumor_name = 'HCT-15' where tumor_name like 'HCT-15%';
update ea_lookup set tumor_name = 'SR' where tumor_name like 'SR%';
update ea_lookup set tumor_name = 'SW-620' where tumor_name like 'SW-620%';
update ea_lookup set tumor_name = 'U251' where tumor_name like 'U251%';
update ea_lookup set tumor_name = 'A549/Asc-1' where tumor_name like 'A549%Asc%';

update ea_lookup set passage = 'P00' where passage = 'P0';
update ea_lookup set passage = 'P04' where passage = 'P05';

create index ealu_ea_file on ea_lookup(ea_file);
create index ealu_mouse_strain on ea_lookup(mouse_strain);
create index ealu_tumor_name on ea_lookup(tumor_name);
create index ealu_passage on ea_lookup(passage);
create index ealu_replicate on ea_lookup(replicate);

delete from tumor_type;
delete from tumor;

drop table if exists temp;

create table temp
as
select distinct tumor_type 
from ea_lookup
order by tumor_type;

drop sequence if exists temp_seq;
create sequence temp_seq;

insert into tumor_type(id, tumor_type)
select nextval('temp_seq'), tumor_type
from temp;

drop table if exists temp;

create table temp
as
select distinct tumor_name, tumor_type
from ea_lookup
order by tumor_name;

drop sequence if exists temp_seq;
create sequence temp_seq;

insert into tumor(id, tumor_name, tumor_type_fk)
select nextval('temp_seq'), t.tumor_name, tt.id
from temp t, tumor_type tt
where t.tumor_type = tt.tumor_type;

--    #    #    #  #####    ####   #####    #####          #####   #    #    ##
--    #    ##  ##  #    #  #    #  #    #     #            #    #  ##  ##   #  #
--    #    # ## #  #    #  #    #  #    #     #            #    #  # ## #  #    #
--    #    #    #  #####   #    #  #####      #            #####   #    #  ######
--    #    #    #  #       #    #  #   #      #            #   #   #    #  #    #
--    #    #    #  #        ####   #    #     #            #    #  #    #  #    #

drop table if exists import_rma;

create table import_rma(
ea_file varchar,
probe_set_id varchar,
tumor_name varchar,
passage varchar,
replicate varchar,
value double precision
);

\copy import_rma from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/AFFY_OUTPUT/rma.summary.txt.processed csv header

create index ir_probe_set_id_idx on import_rma(probe_set_id);
create index ir_tumor_name on import_rma(tumor_name);

vacuum analyze import_rma;

