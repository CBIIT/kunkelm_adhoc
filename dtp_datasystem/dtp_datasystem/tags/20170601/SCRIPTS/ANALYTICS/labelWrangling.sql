drop table if exists label_counts;

create table label_counts
as select pseudo_atoms, count(*) 
from cmpd_annotation
where pseudo_atoms is not null
and length(pseudo_atoms) > 0
group by pseudo_atoms
order by count(*) desc;
