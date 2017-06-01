select count(*) from nsc_cmpd where cmpd_salt_fragment_fk is null;

drop table if exists temp;

create table temp
as
select 
nsc.nsc, frag.id
from nsc_cmpd nsc, nsc_cmpd_type type, cmpd_fragment frag
where nsc.nsc_cmpd_type_fk = type.id
and type.nsc_cmpd_type like '%_two_strc_one_salt'
and frag.nsc_cmpd_fk = nsc.id
and frag.cmpd_known_salt_fk is not null;

create index temp_nsc on temp(nsc);

vacuum analyze temp;

-- a temp version of nsc_cmpd with cmpd_salt_fragment_fk

create table temp_nsc_cmpd
as
select
nsc_cmpd.id,
name,
nsc_cmpd_id,
prefix,
nsc_cmpd.nsc,
conf,
distribution,
cas,
count_fragments,
discreet,
identifier_string,
descriptor_string,
molecular_weight,
molecular_formula,
nsc_cmpd_type_fk,
cmpd_parent_fragment_fk,
cmpd_bio_assay_fk,
cmpd_inventory_fk,
cmpd_annotation_fk,
cmpd_legacy_cmpd_fk,
formula_molecular_formula,
formula_weight,
parent_molecular_weight,
parent_molecular_formula,
temp.id as cmpd_salt_fragment_fk

from nsc_cmpd 
    left join temp on nsc_cmpd.nsc = temp.nsc;

-- drop indexes

drop index if exists cFrag_cFragStrcFk;
drop index if exists cFrag_cFragPchemFk;
drop index if exists cFrag_nCmpdFk;
drop index if exists nCmpd_nCmpdTypeFk;
drop index if exists nCmpd_cParFragFk;
drop index if exists nCmpd_cSaltFragFk;
drop index if exists nCmpd_cBioAssFk;
drop index if exists nCmpd_cInvFk;
drop index if exists nCmpd_cAnnotFk;
drop index if exists nCmpd_cLegCFk;

truncate nsc_cmpd;

insert into nsc_cmpd(
id,
name,
nsc_cmpd_id,
prefix,
nsc,
conf,
distribution,
cas,
count_fragments,
discreet,
identifier_string,
descriptor_string,
nsc_cmpd_type_fk,
cmpd_parent_fragment_fk,
cmpd_bio_assay_fk,
cmpd_inventory_fk,
cmpd_annotation_fk,
cmpd_legacy_cmpd_fk,
formula_molecular_formula,
formula_weight,
parent_molecular_weight,
parent_molecular_formula,
prod_formula_weight,
prod_molecular_formula,
cmpd_salt_fragment_fk
)
select
id,
name,
nsc_cmpd_id,
prefix,
nsc,
conf,
distribution,
cas,
count_fragments,
discreet,
identifier_string,
descriptor_string,
nsc_cmpd_type_fk,
cmpd_parent_fragment_fk,
cmpd_bio_assay_fk,
cmpd_inventory_fk,
cmpd_annotation_fk,
cmpd_legacy_cmpd_fk,
formula_molecular_formula,
formula_weight,
parent_molecular_weight,
parent_molecular_formula,
prod_formula_weight,
prod_molecular_formula,
cmpd_salt_fragment_fk
from temp_nsc_cmpd;

--
-- cmpd_table
--

--
-- pre-can aggregates
--

drop table if exists temp_agg;

create table temp_agg as 
select

nc.id,

array_to_string(array_agg(all_pchem.molecular_formula),'.') as formula_molecular_formula,
sum(all_pchem.molecular_weight) as formula_weight

from nsc_cmpd nc

join cmpd_fragment all_frag on nc.id = all_frag.nsc_cmpd_fk
  join cmpd_fragment_p_chem all_pchem on all_frag.cmpd_fragment_p_chem_fk = all_pchem.id

group by nc.id;

create index temp_agg_id on temp_agg(id);

--
-- pre-can resolution of parent, salt and temp_agg
--

drop table if exists temp;

create table temp as 
select

nc.id,
nc.nsc,

salt.can_smi as salt_smiles,
salt.salt_name,
salt.salt_mf,
salt.salt_mw,

CASE 
WHEN par_pchem.molecular_formula is not null THEN   
  CASE 
  WHEN salt.salt_mf is not null THEN 
    par_pchem.molecular_formula||'.'||salt.salt_mf
  ELSE 
    par_pchem.molecular_formula
  END
ELSE
  ta.formula_molecular_formula
END 
as formula_molecular_formula,

CASE 
WHEN par_pchem.molecular_weight is not null THEN   
  CASE 
  WHEN salt.salt_mw is not null THEN 
    par_pchem.molecular_weight + salt.salt_mw
  ELSE 
    par_pchem.molecular_weight
  END 
ELSE
  ta.formula_weight
END 
as formula_weight,

par_pchem.molecular_formula as parent_molecular_formula,
par_pchem.molecular_weight as parent_molecular_weight,

prod.mw as prod_formula_weight,
prod.mf as prod_molecular_formula

from nsc_cmpd nc

left outer join cmpd_fragment par_frag on nc.cmpd_parent_fragment_fk = par_frag.id
  left outer join cmpd_fragment_p_chem par_pchem on par_frag.cmpd_fragment_p_chem_fk = par_pchem.id

left outer join cmpd_fragment salt_frag on nc.cmpd_salt_fragment_fk = salt_frag.id
	left outer join cmpd_known_salt salt on salt_frag.cmpd_known_salt_fk = salt.id

left outer join prod_cmpd prod on nc.nsc = prod.nsc

left outer join temp_agg ta on nc.id = ta.id;

create index temp_id on temp(id);
