\c datasystemdb

drop table if exists nsc_for_export;

create table nsc_for_export (nsc int);

insert into nsc_for_export(nsc)

select distinct nsc
from qc_with_nsc
where (
    source='no_lbl_one_strc_no_salt' and comparator='count nsc' and target='mw DIFF 0'
)

UNION

select distinct nsc
from qc_with_nsc
where (
    source='no_lbl_two_strc_no_salt' and comparator='count nsc' and target='mw DIFF 0'
);
