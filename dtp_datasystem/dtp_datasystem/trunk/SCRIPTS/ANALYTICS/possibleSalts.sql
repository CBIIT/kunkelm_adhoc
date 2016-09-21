drop table if exists possible_salts;

create table possible_salts
as
select struc.can_taut, count(*) 
from nsc_cmpd nsc, cmpd_fragment frag, cmpd_fragment_structure struc, qc_with_nsc qc
where nsc.nsc = qc.nsc
and qc.source like '%two_strc_no_salt'
and nsc.id = frag.nsc_cmpd_fk
and frag.cmpd_fragment_structure_fk = struc.id
group by struc.can_taut
having count(*) > 25;

drop table if exists possible_salts_with_nsc;

create table possible_salts_with_nsc
as
select poss.can_taut, nsc.nsc, frag.id as frag_id 
from nsc_cmpd nsc, cmpd_fragment frag, cmpd_fragment_structure struc, possible_salts poss
where nsc.id = frag.nsc_cmpd_fk
and frag.cmpd_fragment_structure_fk = struc.id
and struc.can_taut = poss.can_taut;

