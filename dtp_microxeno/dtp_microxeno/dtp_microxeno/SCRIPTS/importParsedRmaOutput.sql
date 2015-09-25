drop table if exists import_rma;

create table import_rma(
ea_file varchar(128),
probe varchar(128),
value double precision
);

\copy import_rma from /home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/AFFY_OUTPUT/rma.summary.txt.processed csv

create index ir_ea_file_idx on import_rma(ea_file);
create index ir_probe_idx on import_rma(probe);

vacuum analyze import_rma;

