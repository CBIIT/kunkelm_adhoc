--
-- mol_frag
-- 

drop table if exists summary;
create table summary(descr varchar(128), val double precision);

insert into summary(descr,val) 
select 'count prod_src nsc', count(distinct nsc) from prod_src;

insert into summary(descr,val) 
select 'count * in mol_frag', count(*) from mol_frag;

insert into summary(descr,val) 
select 'count distinct frag_id in mol_frag', count(distinct frag_id) from mol_frag;

insert into summary(descr,val) 
select 'count nsc in mol_frag', count(distinct nsc) from mol_frag;

insert into summary(descr,val) 
select 'count nsc in mol_cannot_parse', count(distinct nsc) from mol_cannot_parse;

insert into summary(descr,val) 
select 'count nsc in mol_frag_pseudo_atom', count(distinct nsc) from mol_frag_pseudo_atom;

insert into summary(descr,val) 
select 'count nsc in mol_with_r_group', count(distinct nsc) from mol_frag_r_group;

insert into summary(descr,val) 
select 'count nsc in mol_frag_null_atom_type', count(distinct nsc) from mol_frag_null_atom_type;

-- nsc that have ANY problems

drop table if exists nsc_with_problems;
create table nsc_with_problems 
as 
select distinct nsc 
from mol_frag 
where ((processing_messages like '%ERR r group%' and processing_messages NOT like '%ERR psuedo%') or processing_messages like '%ERR null atom type%');

drop table if exists nsc_with_problems_and_counts;
create table nsc_with_problems_and_counts
as
select nwp.nsc, pi.inventory, pbc.prod_count_nci60, pbc.prod_count_hf, pbc.prod_count_xeno
from nsc_with_problems nwp 
left outer join prod_inventory pi
on nwp.nsc = pi.nsc
left outer join prod_bio_counts pbc
on nwp.nsc = pbc.nsc;

drop table if exists nsc_with_no_problems;
create table nsc_with_no_problems as select distinct nsc from mol_frag except select nsc from nsc_with_problems;

insert into summary(descr,val) 
select 'count nsc with no problems', count(distinct nsc) from nsc_with_no_problems;

insert into summary(descr,val) 
select 'count nsc with problems', count(distinct nsc) from nsc_with_problems;

-- rdkit

insert into summary(descr,val) 
select 'rdkit_mol_frag', count(distinct nsc) from rdkit_mol_frag;

insert into summary(descr,val) 
select 'rdkit_mol_fail', count(distinct nsc) from rdkit_mol_frag_fail;
  create index rdkit_mol_frag_sid on rdkit_mol_frag(nsc);
  create index rdkit_mol_frag_fail_sid on rdkit_mol_frag_fail(nsc);

insert into summary(descr,val) 
select 'distinct nsc in rdkit', count(distinct foo.nsc) from (
select nsc from rdkit_mol_frag union 
select nsc from rdkit_mol_frag_fail) as "foo";

insert into summary(descr,val) 
select 'all good frag in rdkit', count(distinct nsc) from rdkit_mol_frag where nsc not in (
select nsc from rdkit_mol_frag_fail);

insert into summary(descr,val) 
select 'all bad frag in rdkit', count(distinct nsc) from rdkit_mol_frag_fail where nsc not in (
select nsc from rdkit_mol_frag);

select * from summary;

--
--
--

-- from rdkit

drop table if exists fw;
create table fw 
as 
select nsc, sum(mw) 
from rdkit_mol_frag 
group by nsc;

drop table if exists count_frag;
create table count_frag 
as 
select nsc, count(*), array_to_string(array_agg(smiles),'.') as smiles 
from rdkit_mol_frag 
group by nsc;

drop table if exists count_known_salt;
create table count_known_salt 
as 
select nsc, count(*) 
from rdkit_mol_frag 
where known_salt = 't'
group by nsc;

drop table if exists count_frag_r_group;
create table count_frag_r_group 
as 
select nsc, count(*) 
from mol_frag_r_group 
group by nsc;

drop table if exists count_pseudo;
create table count_pseudo
as
select nsc, count(*) 
from mol_frag_pseudo_atom
group by nsc;

drop table if exists count_null_type;
create table count_null_type
as
select nsc, count(*)
from mol_frag_null_atom_type
group by nsc;

-- need to include processing_messasges here

drop table if exists count_summary;

create table count_summary
as
select prod_src.nsc,
  prod_src.mf as prod_mw,
  fw."sum" as fw,
  prod_src.mw as prod_fw,  
  count_frag."count" as count_frag, 
  count_frag.smiles,
  count_known_salt."count" as count_known_salt,
  count_frag_r_group."count" as count_r_group,
  count_pseudo."count" as count_pseudo, 
  count_null_type."count" as count_null
from prod_src left outer join fw
    on prod_src.nsc = fw.nsc
left outer join count_frag
    on prod_src.nsc = count_frag.nsc
left outer join count_known_salt
    on prod_src.nsc = count_known_salt.nsc
left outer join count_frag_r_group
    on prod_src.nsc = count_frag_r_group.nsc
left outer join count_pseudo
    on prod_src.nsc = count_pseudo.nsc
left outer join count_null_type
    on prod_src.nsc = count_null_type.nsc;

--
--
-- fw comparisons for prod_src and parent_with_salt
--
--

drop table if exists fw_comparison_single_fragment;

create table fw_comparison_single_fragment
as
select pws.nsc, pws.mf as pws_mf, round(pws.mw) as pws_mw, ps.mf as prod_mf, round(ps.mw) as prod_mw
from parent_with_salt pws, prod_src ps
where pws.nsc = ps.nsc
and pws.salt_smiles is null;

drop table fw_comparison_single_fragment_same_mf;

create table fw_comparison_single_fragment_same_mf
as
select *, abs(pws_mw - prod_mw) as delta_mw
from fw_comparison_single_fragment 
where pws_mf = prod_mf;

select count(*) 
from fw_comparison_single_fragment_same_mf 
where delta_mw <= 1;

select *
from fw_comparison_single_fragment_same_mf 
where delta_mw > 2 
order by delta_mw desc;

select count(*)
from fw_comparison_single_fragment
where pws_mf != prod_mf
and prod_mf not like '%.%';

select *
from fw_comparison_single_fragment
where pws_mf != prod_mf
and prod_mf not like '%.%';  -- to eliminate MF with hard-coded .

-- polymers, mixtures

select *
from fw_comparison_single_fragment
where ( prod_mf like '%n%' 
or
prod_mf like '%x%');


and prod_mf NOT like '%Zn%'
and prod_mf NOT like '%Sn%'
and prod_mf NOT like '%Mn%'