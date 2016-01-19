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

left_join cmpd_fragment on nsc_cmpd_cmpd_frag

left join temp
on nsc_cmpd.nsc = temp.nsc;

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

insert into nsc_cmpd select * from temp_nsc_cmpd;

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
par_pchem.molecular_weight as parent_molecular_weight

from nsc_cmpd nc

left outer join cmpd_fragment par_frag on nc.cmpd_parent_fragment_fk = par_frag.id
  left outer join cmpd_fragment_p_chem par_pchem on par_frag.cmpd_fragment_p_chem_fk = par_pchem.id

left outer join cmpd_fragment salt_frag on nc.cmpd_salt_fragment_fk = salt_frag.id
	left outer join cmpd_known_salt salt on salt_frag.cmpd_known_salt_fk = salt.id

left outer join temp_agg ta on nc.id = ta.id;

create index temp_id on temp(id);

--
-- temp_cmpd_table
--

create table temp_cmpd_table as
select
ct.id,
ct.prefix,
ct.nsc,
ct.cas,
ct.name,
ct.discreet,
ct.conf,
ct.distribution,
ct.nsc_cmpd_id,
ad_hoc_cmpd_id,
original_ad_hoc_cmpd_id,
inventory,
nci60,
hf,
xeno,
formatted_targets_string,
formatted_sets_string,
formatted_projects_string,
formatted_plates_string,
formatted_aliases_string,
formatted_fragments_string,
pseudo_atoms,
mtxt,
t.salt_smiles,
t.salt_name,
t.salt_mf,
t.salt_mw,
parent_stoichiometry,
salt_stoichiometry,
inchi,
inchi_aux,
ctab,
ct.can_smi,
ct.can_taut,
ct.can_taut_strip_stereo,
ct.molecular_formula,
log_d,
count_hyd_bond_acceptors,
count_hyd_bond_donors,
surface_area,
solubility,
count_rings,
count_atoms,
count_bonds,
count_single_bonds,
count_double_bonds,
count_triple_bonds,
count_rotatable_bonds,
count_hydrogen_atoms,
count_metal_atoms,
count_heavy_atoms,
count_positive_atoms,
count_negative_atoms,
count_ring_bonds,
count_stereo_atoms,
count_stereo_bonds,
count_ring_assemblies,
count_aromatic_bonds,
count_aromatic_rings,
formal_charge,
the_a_log_p,
general_comment,
purity_comment,
stereochemistry_comment,
ct.identifier_string,
ct.descriptor_string,
ct.molecular_weight,
nsc_cmpd_type,
t.formula_molecular_formula,
t.formula_weight,
t.parent_molecular_formula,
t.parent_molecular_weight

from cmpd_table ct

left outer join temp t on (ct.id = t.id);
	
--
-- replace cmpd_table
--

alter table cmpd_table rename to original_cmpd_table;

create table cmpd_table 
as 
select *
from temp_cmpd_table;

create index cmpd_table_id on cmpd_table(id);
create index cmpd_table_nsc_idx on CMPD_TABLE (NSC);
create index cmpd_table_cas_idx on CMPD_TABLE (CAS);

vacuum analyze verbose cmpd_table;
