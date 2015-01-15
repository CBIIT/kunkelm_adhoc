-- IMPORT FROM PLP

-- table creation and file load handled by perl script loadFromPipelinePilot.PL
-- makes sure that expected column headers match the Pipeline Pilot files

-- the perl script does NOT actually make the load

\copy rs3_from_plp_nsc from '/home/mwkunkel/rs3_from_plp_400k_nsc.txt' csv header delimiter as E'\t'
\copy rs3_from_plp_nsc from '/home/mwkunkel/rs3_from_plp_remainder_nsc.txt' csv header delimiter as E'\t'
create index rs3_from_plp_nsc_nsc on rs3_from_plp_nsc(nsc);

\copy strc_from_plp_nsc from '/home/mwkunkel/strc_from_plp_nsc.txt' csv header delimiter as E'\t'
create index strc_from_plp_nsc_nsc on strc_from_plp_nsc(nsc);

vacuum analyze verbose rs3_from_plp_nsc;
vacuum analyze verbose strc_from_plp_nsc;

--  resolve

drop table if exists resolve1;

create table resolve1
as
select
-- lookup in ops$oradis.dis_cmpd
rs3.nsc, rs3.mf, rs3.mw, rs3.num_fragments as rs3_num_fragments, strc.num_fragments as strc_num_fragments,

-- calculated from rs3_structure_object ctab
rs3.molecular_formula as rs3_mf, rs3.mw as rs3_mw, rs3.num_atoms as rs3_num_atoms, rs3.num_bonds as rs3_num_bonds, rs3.num_hydrogens as rs3_num_hydrogens,

-- calculated from ops$oradis.dis_cmpd ctab
strc.molecular_formula as strc_mf, strc.mw as strc_mw, strc.num_atoms as strc_num_atoms, strc.num_bonds as strc_num_bonds, strc.num_hydrogens as strc_num_hydrogens,

strc.num_fragments - rs3.num_fragments as diff_num_fragments,
strc.num_atoms - rs3.num_atoms as diff_num_atoms,
strc.num_bonds - rs3.num_bonds as diff_num_bonds

from rs3_from_plp_nsc rs3, strc_from_plp_nsc strc
where rs3.nsc = strc.nsc;

create index resolve1_nsc on resolve1(nsc);

vacuum analyze verbose resolve1;

drop table if exists resolve2;

create table resolve2 
as
select 
r.*, i.inventory, n.the_count as count_nci60
from resolve1 r
left outer join prod_inventory i on r.nsc = i.nsc
left outer join prod_count_nci60 n on r.nsc = n.nsc;

select diff_num_atoms, diff_num_bonds, diff_num_fragments, count(*) 
from resolve1
group by 1,2,3
order by 4 desc;

select nsc, mf, rs3_mf, strc_mf, mw, rs3_mw, strc_mw
--select count(*)
from resolve1
where rs3_num_fragments = 1
and strc_num_fragments = 2
and diff_num_bonds = 0
and rs3_mf != strc_mf
and strc_mf not like '%*%'
and mf like '%.%';

-- R groups are different in rs3_mf and strc_mf
select * from resolve1 where rs3_mf like '%R#%' and strc_mf like '%*%';
