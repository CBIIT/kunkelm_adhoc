drop table if exists possible_next_labels;

create table possible_next_labels
as
select id as nsc, mtxt, pseudo_atoms
from cmpd_annotation
where mtxt ilike '%NExT%'
and pseudo_atoms is not null and length(pseudo_atoms) > 0;

