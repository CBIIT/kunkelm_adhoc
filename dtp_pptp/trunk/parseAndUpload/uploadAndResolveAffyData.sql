\c pptpdb

drop table if exists affy_data_raw_upload;

create table affy_data_raw_upload(
cell_line_id varchar(32),
probe_set varchar(32),
stat_pairs integer, 
stat_pairs_used integer, 
signal double precision, 
detection varchar(32), 
detection_p_value double precision); 

\copy affy_data_raw_upload from /tmp/big.processed csv

create index adru_probe_set on affy_data_raw_upload(probe_set);
create index adru_cell_line_id on affy_data_raw_upload(cell_line_id);

vacuum analyze affy_data_raw_upload;

--resolve the data

truncate affy_data;

insert into affy_data
select nextval('hibernate_seq'),
raw.stat_pairs,
raw.stat_pairs_used,
raw.signal,
raw.detection,
raw.detection_p_value,
affy.id,
cell.id 
from affy_data_raw_upload raw, affy_identifier_type affy, cell_line_type cell
where raw.cell_line_id = cell.dap_identifier
and raw.probe_set = affy.probe_set_name;
