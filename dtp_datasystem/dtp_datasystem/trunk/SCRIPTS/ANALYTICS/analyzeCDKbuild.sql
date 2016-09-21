
select count(*) from mol_cannot_parse;
select count(*) from mol_frag;
select count(*) from mol_frag_null_atom_type;
select count(*) from mol_frag_pseudo_atom;
select count(*) from mol_frag_r_group;
select count(*) from mol_no_frag;

select count(distinct nsc) from mol_cannot_parse;
select count(distinct nsc) from mol_frag;
select count(distinct nsc) from mol_frag_null_atom_type;
select count(distinct nsc) from mol_frag_pseudo_atom;
select count(distinct nsc) from mol_frag_r_group;
select count(distinct nsc) from mol_no_frag;

select count(distinct frag_id) from mol_cannot_parse;
select count(distinct frag_id) from mol_frag;
select count(distinct frag_id) from mol_frag_null_atom_type;
select count(distinct frag_id) from mol_frag_pseudo_atom;
select count(distinct frag_id) from mol_frag_r_group;
select count(distinct frag_id) from mol_no_frag;



--drop table if exists full_mol_cannot_parse;
--createtable full_mol_cannot_parse as select * from mol_cannot_parse;
        
--drop table if exists full_mol_frag;
--createtable full_mol_frag as select * from mol_frag;
                
--drop table if exists full_mol_frag_null_atom_type;
--createtable full_mol_frag_null_atom_type as select * from mol_frag_null_atom_type;
 
--drop table if exists full_mol_frag_pseudo_atom;
--createtable full_mol_frag_pseudo_atom as select * from mol_frag_pseudo_atom;
    
--drop table if exists full_mol_frag_r_group;
--createtable full_mol_frag_r_group as select * from mol_frag_r_group;
        
--drop table if exists full_mol_no_frag;
--createtable full_mol_no_frag as select * from mol_no_frag;

drop table if exists resolve_mf_and_mw;

create table resolve_mf_and_mw
as
select pfs.nsc, prod_mf, prod_mw, array_to_string(array_agg(m_f.mf), '.') as mf, sum(m_f.mw) as mw
from parsed_from_sdfile pfs, full_mol_frag m_f
where pfs.nsc = m_f.nsc
--and prod_mf like '%.%'
group by pfs.nsc, prod_mf, prod_mw;

select nsc, prod_mf from parsed_from_sdfile
where prod_mf like '%R%'
and nsc in (
    select nsc from full_mol_frag_r_group
);