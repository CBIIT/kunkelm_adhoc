drop table if exists dz_resolve;

create index pp2_cleaned_nsc_idx on pp2_cleaned(nsc);
create index pp2_cleaned_errors_nsc_idx on pp2_cleaned_errors(nsc);
create index pp2_cleaned_salts_nsc_idx on pp2_cleaned_salts(nsc);
create index pp2_cleaned_solvent_nsc_idx on pp2_cleaned_solvent(nsc);

alter table pp2_cleaned_salts
add salt_molecular_weight double precision;

alter table pp2_cleaned_salts
add salt_molecular_formula varchar;

alter table pp2_cleaned_salts
add salt_formal_charge double precision;

update pp2_cleaned_salts
set salt_molecular_weight = salts_from_dz.molecular_weight,
salt_molecular_formula = salts_from_dz.molecular_formula,
salt_formal_charge = salts_from_dz.formalcharge
from salts_from_dz 
where saltname = salts_from_dz.salt_name;

drop table if exists dz_resolve;

create table dz_resolve
as
select cln.*, 
err.errortype, err.errorcomment, 
salt.saltname, salt.saltcoeff, salt.salt_molecular_weight, salt.salt_molecular_formula, salt.salt_formal_charge,
solv.solventname, solv.solventcoeff,

rs3.prod_mf, rs3.prod_mw

from pp2_cleaned cln 
left outer join pp2_cleaned_errors err on cln.nsc = err.nsc
left outer join pp2_cleaned_salts salt on cln.nsc = salt.nsc
left outer join pp2_cleaned_solvents solv on cln.nsc = solv.nsc

left outer join rs3_from_plp_nsc rs3 on cln.nsc = rs3.nsc 

 where cleanup_status = 'All Calculations Finished'
 and dis_mw_check = 'Passed'
 and deleted_pseudoatoms = 0;

drop table if exists dz_agg;

create table dz_agg
as
select *, 

case when saltcoeff is null then parent_mw
	 when saltcoeff = 1 then parent_mw + salt_molecular_weight
	 else parent_mw + saltcoeff * salt_molecular_weight
end as agg_mw,

case when saltcoeff is null then parent_molf
	 when saltcoeff = 1 then parent_molf||'.'||salt_molecular_formula
	 else parent_molf||'.'||saltcoeff||'('||salt_molecular_formula||')'
end as agg_mf

from dz_resolve;

drop table if exists temp;

create table temp
as
select nsc, prod_mw, prod_mf, full_mw, full_molf, agg_mw, agg_mf, abs(prod_mw - full_mw) as diff_mw
from dz_agg 
where abs(prod_mw - full_mw) > 1;
