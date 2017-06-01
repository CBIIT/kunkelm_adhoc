insert into cmpd_table(
id,
prefix,
nsc,
cas,
name,
discreet,
conf,
distribution,
nsc_cmpd_id,
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
salt_smiles,
salt_name,
salt_mf,
salt_mw,
parent_stoichiometry,
salt_stoichiometry,
inchi,
inchi_aux,
ctab,
can_smi,
can_taut,
can_taut_strip_stereo,
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
mtxt,
general_comment,
purity_comment,
stereochemistry_comment,
identifier_string,
descriptor_string,
nsc_cmpd_type,
formula_molecular_formula,
formula_weight,
parent_molecular_formula,
parent_molecular_weight,
prod_formula_weight,
prod_molecular_formula,
cmpd_formal_charge
)

select

nsc.id,
nsc.prefix,
nsc.nsc,
nsc.cas,
nsc.name,
nsc.discreet,
nsc.conf,
nsc.distribution,
nsc.nsc_cmpd_id,
null, --ad_hoc_cmpd_id,
null, --original_ad_hoc_cmpd_id,

-- inventory

invent.inventory,

-- bio_assay

bioass.nci60,
bioass.hf,
bioass.xeno,

-- from temp_formatted_strings

tfs.formatted_targets_string,
tfs.formatted_sets_string,
tfs.formatted_projects_string,
tfs.formatted_plates_string,
tfs.formatted_aliases_string,

-- temp_formatted_fragments

tff.formatted_fragments_string,

pseudo_atoms,

-- salts

salt.can_smi, --null, --salt_smiles,
salt.salt_name, --null, --salt_name,
salt.salt_mf, --null, --salt_mf,
salt.salt_mw, --null, --salt_mw,

1, --parent_stoichiometry,
1, --salt_stoichiometry,

-- structure -- PARENT only

strc.inchi,
strc.inchi_aux,
strc.ctab,
strc.can_smi,
strc.can_taut,
strc.can_taut_strip_stereo,

-- pchem -- PARENT only

pchem.log_d,
pchem.count_hyd_bond_acceptors,
pchem.count_hyd_bond_donors,
pchem.surface_area,
pchem.solubility,
pchem.count_rings,
pchem.count_atoms,
pchem.count_bonds,
pchem.count_single_bonds,
pchem.count_double_bonds,
pchem.count_triple_bonds,
pchem.count_rotatable_bonds,
pchem.count_hydrogen_atoms,
pchem.count_metal_atoms,
pchem.count_heavy_atoms,
pchem.count_positive_atoms,
pchem.count_negative_atoms,
pchem.count_ring_bonds,
pchem.count_stereo_atoms,
pchem.count_stereo_bonds,
pchem.count_ring_assemblies,
pchem.count_aromatic_bonds,
pchem.count_aromatic_rings,
pchem.formal_charge,
pchem.the_a_log_p,

-- annotation

annot.mtxt,
annot.general_comment,
annot.purity_comment,
annot.stereochemistry_comment,

nsc.identifier_string,
nsc.descriptor_string,

typ.nsc_cmpd_type,

nsc.formula_molecular_formula,
nsc.formula_weight,
nsc.parent_molecular_formula,
nsc.parent_molecular_weight,
nsc.prod_formula_weight,
nsc.prod_molecular_formula,
nsc.formal_charge

from 

nsc_cmpd nsc

-- need intermediate join based on cmpd_parent_fragment_fk!

left outer join cmpd_fragment frag on nsc.cmpd_parent_fragment_fk = frag.id
left outer join cmpd_fragment_p_chem pchem on frag.cmpd_fragment_p_chem_fk = pchem.id
left outer join cmpd_fragment_structure strc on frag.cmpd_fragment_structure_fk = strc.id

left outer join cmpd_annotation annot on nsc.cmpd_annotation_fk = annot.id
left outer join cmpd_inventory invent on nsc.cmpd_inventory_fk = invent.id
left outer join cmpd_bio_assay bioass on nsc.cmpd_bio_assay_fk = bioass.id

left outer join cmpd_fragment salt_frag on nsc.cmpd_salt_fragment_fk = salt_frag.id
left outer join cmpd_known_salt salt on salt_frag.cmpd_known_salt_fk = salt.id

-- temp tables

left outer join temp_formatted_strings tfs on nsc.nsc = tfs.nsc -- this is 1-to-1
left outer join temp_formatted_fragments tff on nsc.nsc = tff.nsc
left outer join temp_ident_descr temp_ident_descr on nsc.nsc = temp_ident_descr.nsc -- this is 1-to-1
left outer join nsc_cmpd_type typ on nsc.nsc_cmpd_type_fk = typ.id,

nsc_to_load load

where nsc.nsc = load.nsc

                                                                            QUERY PLAN                                                                             
-------------------------------------------------------------------------------------------------------------------------------------------------------------------
 Insert on cmpd_table  (cost=377448.63..2630804.10 rows=741855 width=2946)
   ->  Nested Loop  (cost=377448.63..2630804.10 rows=741855 width=2946)
         ->  Hash Left Join  (cost=377448.21..2283257.44 rows=741855 width=2946)
               Hash Cond: (nsc.nsc_cmpd_type_fk = typ.id)
               ->  Nested Loop Left Join  (cost=377445.77..2273054.50 rows=741855 width=2928)
                     ->  Nested Loop Left Join  (cost=377445.34..1929217.11 rows=741855 width=2928)
                           ->  Nested Loop Left Join  (cost=377444.92..1555863.72 rows=741855 width=2885)
                                 ->  Hash Left Join  (cost=377444.49..1187442.33 rows=741855 width=2751)
                                       Hash Cond: (salt_frag.cmpd_known_salt_fk = salt.id)
                                       ->  Nested Loop Left Join  (cost=377427.09..1183853.88 rows=741855 width=2712)
                                             ->  Hash Left Join  (cost=377426.66..819336.50 rows=741855 width=1079)
                                                   Hash Cond: (frag.cmpd_fragment_structure_fk = strc.id)
                                                   ->  Hash Left Join  (cost=210053.49..498164.39 rows=741855 width=344)
                                                         Hash Cond: (nsc.cmpd_parent_fragment_fk = frag.id)
                                                         ->  Hash Left Join  (cost=79287.19..291267.89 rows=741855 width=228)
                                                               Hash Cond: (nsc.cmpd_bio_assay_fk = bioass.id)
                                                               ->  Hash Left Join  (cost=53522.45..201401.05 rows=741855 width=224)
                                                                     Hash Cond: (nsc.cmpd_salt_fragment_fk = salt_frag.id)
                                                                     ->  Hash Left Join  (cost=24325.74..117964.43 rows=741855 width=224)
                                                                           Hash Cond: (nsc.cmpd_inventory_fk = invent.id)
                                                                           ->  Seq Scan on nsc_cmpd nsc  (cost=0.00..33042.55 rows=741855 width=224)
                                                                           ->  Hash  (cost=11429.55..11429.55 rows=741855 width=16)
                                                                                 ->  Seq Scan on cmpd_inventory invent  (cost=0.00..11429.55 rows=741855 width=16)
                                                                     ->  Hash  (cost=15416.43..15416.43 rows=792743 width=16)
                                                                           ->  Seq Scan on cmpd_fragment salt_frag  (cost=0.00..15416.43 rows=792743 width=16)
                                                               ->  Hash  (cost=12144.55..12144.55 rows=741855 width=20)
                                                                     ->  Seq Scan on cmpd_bio_assay bioass  (cost=0.00..12144.55 rows=741855 width=20)
                                                         ->  Hash  (cost=105373.01..105373.01 rows=792743 width=132)
                                                               ->  Hash Right Join  (cost=29970.72..105373.01 rows=792743 width=132)
                                                                     Hash Cond: (pchem.id = frag.cmpd_fragment_p_chem_fk)
                                                                     ->  Seq Scan on cmpd_fragment_p_chem pchem  (cost=0.00..25482.43 rows=792743 width=124)
                                                                     ->  Hash  (cost=15416.43..15416.43 rows=792743 width=24)
                                                                           ->  Seq Scan on cmpd_fragment frag  (cost=0.00..15416.43 rows=792743 width=24)
                                                   ->  Hash  (cost=82372.19..82372.19 rows=792719 width=751)
                                                         ->  Seq Scan on cmpd_fragment_structure strc  (cost=0.00..82372.19 rows=792719 width=751)
                                             ->  Index Scan using cmpd_annotation_pkey on cmpd_annotation annot  (cost=0.42..0.48 rows=1 width=1649)
                                                   Index Cond: (nsc.cmpd_annotation_fk = id)
                                       ->  Hash  (cost=13.29..13.29 rows=329 width=55)
                                             ->  Seq Scan on cmpd_known_salt salt  (cost=0.00..13.29 rows=329 width=55)
                                 ->  Index Scan using tfs_nsc on temp_formatted_strings tfs  (cost=0.42..0.49 rows=1 width=138)
                                       Index Cond: (nsc.nsc = nsc)
                           ->  Index Scan using tff_nsc on temp_formatted_fragments tff  (cost=0.42..0.49 rows=1 width=47)
                                 Index Cond: (nsc.nsc = nsc)
                     ->  Index Only Scan using tid_nsc on temp_ident_descr  (cost=0.42..0.45 rows=1 width=4)
                           Index Cond: (nsc = nsc.nsc)
               ->  Hash  (cost=1.64..1.64 rows=64 width=34)
                     ->  Seq Scan on nsc_cmpd_type typ  (cost=0.00..1.64 rows=64 width=34)
         ->  Index Only Scan using ntl_nsc on nsc_to_load load  (cost=0.42..0.46 rows=1 width=4)
               Index Cond: (nsc = nsc.nsc)
(49 rows)

create index cfrag_cmpd_known_salt_fk_idx on cmpd_fragment(cmpd_known_salt_fk);

cmpd_fragment.cmpd_fragment_type_fk

