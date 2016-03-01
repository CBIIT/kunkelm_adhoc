drop table if exists nsc_should_have_legacy;

create table nsc_should_have_legacy
as
select distinct nsc
from qc_with_nsc
except 
select distinct nsc
from qc_with_nsc 
where source in ('no_lbl_one_strc_no_salt', 'no_lbl_two_strc_two_salt')
and target = 'mw DIFF 0';

drop table if exists nsc_need_legacy;

create table nsc_need_legacy
as
select distinct nsc
from nsc_should_have_legacy
except 
select distinct nsc
from cmpd_legacy_cmpd;

-- what are the categories

select source, target, comparator, count(distinct nsc) as the_count
from qc_with_nsc
where nsc in (
	select nsc from nsc_need_legacy
)	
group by 1, 2, 3;

