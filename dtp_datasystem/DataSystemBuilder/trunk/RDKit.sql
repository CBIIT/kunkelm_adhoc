\c dctddb

--InChI errors for aromatic bonds are all NSC >2000000

-- create these indexes if they don't exist
-- create index mol_frag_nsc on mol_frag(nsc);
-- create index mol_frag_frag_id on mol_frag(frag_id);

-- vacuum analyze verbose mol_frag;

-- validate fragments

-- process ALL fragments to check rdkit validity (parse-ability of ctab) by fragment
-- this can then be leveraged along with rules-based checks for validity by NSC

--
-- ONLY rdkit validity
--

drop table if exists rdkit_mol_frag_validity;
create table rdkit_mol_frag_validity as select nsc, frag_id, is_valid_ctab(ctab::cstring) as valid_ctab, is_valid_smiles(smiles::cstring) as valid_smiles from mol_frag;

-- outright failures percolate through as null values
-- update to false

update rdkit_mol_frag_validity set valid_ctab = FALSE where valid_ctab is null;
update rdkit_mol_frag_validity set valid_smiles = FALSE where valid_smiles is null;

-- nsc 93391 is problematic in that it passes valid, but fails mol_from_ctab
update rdkit_mol_frag_validity set valid_ctab = FALSE where nsc = 93391;
update rdkit_mol_frag_validity set valid_smiles = FALSE where nsc = 93391;

create index rdkit_mol_frag_validity_frag_id on rdkit_mol_frag_validity(frag_id);
create index rdkit_mol_frag_validity_nsc on rdkit_mol_frag_validity(nsc);

vacuum analyze verbose rdkit_mol_frag_validity;

-- write failed fragments, for forensics
drop table if exists rdkit_mol_frag_fail;
create table rdkit_mol_frag_fail as select mf.nsc, mf.frag_id, mf.mf, mf.smiles, mf.ctab from mol_frag mf, rdkit_mol_frag_validity v where mf.frag_id = v.frag_id and v.valid_ctab = FALSE;
create index rdkmff_frag_id on rdkit_mol_frag_fail(frag_id);

-- process valid fragments 
drop table if exists rdkit_mol_frag;
create table rdkit_mol_frag as select mf.nsc, mf.frag_id, mol_from_ctab(mf.ctab::cstring) as frag_mol, mf.mf, mf.ctab from mol_frag mf, rdkit_mol_frag_validity v where mf.frag_id = v.frag_id and v.valid_ctab = TRUE;
create index rdkmf_frag_id on rdkit_mol_frag(frag_id);

vacuum analyze verbose rdkit_mol_frag;

--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--
--

--
-- rules-based validity
--

-- individual fragments

drop table if exists mol_frag_validity;
create table mol_frag_validity(nsc int, frag_id int, cannot_parse boolean, has_r_group boolean, has_null_atom_type boolean, has_pseudo_atom boolean, is_valid boolean);

insert into mol_frag_validity(nsc, frag_id, cannot_parse, has_r_group, has_null_atom_type, has_pseudo_atom, is_valid)
select
nsc,
frag_id,
CASE WHEN processing_messages like '%cannot parse%' THEN TRUE ELSE FALSE END,
CASE WHEN processing_messages like '%r group%' AND processing_messages NOT like '%pseudo%' THEN TRUE ELSE FALSE END, 
CASE WHEN processing_messages like '%null atom type%' THEN TRUE ELSE FALSE END,
CASE WHEN processing_messages like '%pseudo%' THEN TRUE ELSE FALSE END,
-- resolve
CASE WHEN (processing_messages like '%r group%' OR processing_messages like '%null atom type%' OR processing_messages like '%cannot parse%') THEN FALSE ELSE TRUE END
from mol_frag;

create index mfv_nsc on mol_frag_validity(nsc);
create index mfv_frag_id on mol_frag_validity(frag_id);

vacuum analyze verbose mol_frag_validity;

-- select is_valid, count(*) from mol_frag_validity group by 1;

-- resolve this against rdkit_frag_validity

drop table if exists resolved_validity;

create table resolved_validity 
as
select mfv.nsc, mfv.frag_id, mf.inchi, mfv.is_valid as mol_frag_valid, rdkmfv.valid_ctab as rdkit_valid
from mol_frag_validity mfv, mol_frag mf, rdkit_mol_frag_validity rdkmfv
where mfv.frag_id = rdkmfv.frag_id
and mfv.frag_id = mf.frag_id;

--select mol_frag_valid, rdkit_valid, count(*) from resolved_validity where mol_frag_valid != rdkit_valid group by 1,2;
 
-- instead of updating rdkit_mol_frag, create temp
-- to do rdkit property calcs for frag,
-- rdkit smiles, 
-- and to fetch InChI from mol_frag

drop table if exists temp;

create table temp 
as 
select rdkit_mol_frag.*,
mol_to_smiles(frag_mol)::varchar(2048) as smiles, 
mol_amw(frag_mol) as mw,
mol_hba(frag_mol) as hba,
mol_hbd(frag_mol) as hbd,
mol_numatoms(frag_mol) as count_atoms,
mol_numheavyatoms(frag_mol) as count_heavy,
mol_numrings(frag_mol) as count_rings,
mol_tpsa(frag_mol) as tpsa,
mol_logp(frag_mol) as logp,
mol_frag.inchi
from rdkit_mol_frag, mol_frag
where rdkit_mol_frag.frag_id = mol_frag.frag_id;

-- swap temp into the real table
alter table rdkit_mol_frag rename to rdkit_mol_frag_backup;
alter table temp rename to rdkit_mol_frag; 

-- index the structures
--create index rdkit_mol_frag_mol on rdkit_mol_frag using gist(frag_mol);
--create index rdkit_mol_frag_frag_id on rdkit_mol_frag(frag_id);
create index rdkit_mol_frag_nsc on rdkit_mol_frag(nsc);
create index rdkit_mol_frag_smiles on rdkit_mol_frag(smiles);
create index rdkit_mol_frag_inchi on rdkit_mol_frag(inchi);

vacuum analyze verbose rdkit_mol_frag;

-- do the salts
alter table rdkit_mol_frag add known_salt boolean;
alter table rdkit_mol_frag add known_salt_name varchar(1024);

update rdkit_mol_frag 
set known_salt = TRUE, known_salt_name = salt_name 
from known_salt 
where rdkit_mol_frag.smiles = known_salt.smiles;

-- one- and two- fragment molecules

-- create holding areas for single fragment and two-fragment where one is a salt

drop table if exists nsc_one_frag;
create table nsc_one_frag as select nsc from rdkit_mol_frag rdk group by nsc having count(*) = 1;
create index nof_nsc on nsc_one_frag(nsc);
--
drop table if exists nsc_one_frag_not_salt;
create table nsc_one_frag_not_salt as select rdk.nsc from rdkit_mol_frag rdk, nsc_one_frag nof where rdk.nsc = nof.nsc and rdk.known_salt is null;
create index nofns_nsc on nsc_one_frag_not_salt(nsc);
--
drop table if exists one_frag;
create table one_frag as select rdk.* from rdkit_mol_frag rdk, nsc_one_frag_not_salt nofns where rdk.nsc = nofns.nsc;
--
-- select count(*) from nsc_one_frag;
-- select count(*) from nsc_one_frag_not_salt;
-- select count(*) from one_frag;

-- two fragment molecules

drop table if exists nsc_two_frag;
create table nsc_two_frag as select nsc from rdkit_mol_frag rdk group by nsc having count(*) = 2;
create index ntf_nsc on nsc_two_frag(nsc);
--
drop table if exists two_frag;
create table two_frag as select rdk.* from rdkit_mol_frag rdk, nsc_two_frag ntf where rdk.nsc = ntf.nsc;
create index tf_nsc on two_frag(nsc);
--
drop table if exists nsc_two_frag_one_salt;
create table nsc_two_frag_one_salt as select nsc from two_frag group by nsc having count(known_salt) = 1;
create index ntfos_nsc on nsc_two_frag_one_salt(nsc);
--
drop table if exists two_frag_one_salt;
create table two_frag_one_salt as select rdk.* from rdkit_mol_frag rdk, nsc_two_frag_one_salt ntfos where rdk.nsc = ntfos.nsc;
create index tfos_nsc on two_frag_one_salt(nsc);

drop table if exists nsc_two_frag_both_salts;
create table nsc_two_frag_both_salts as select nsc from two_frag group by nsc having count(known_salt) = 2;
create index ntfbs_nsc on nsc_two_frag_both_salts(nsc);

drop table if exists two_frag_both_salts;
create table two_frag_both_salts as select rdk.* from rdkit_mol_frag rdk, nsc_two_frag_both_salts ntfbs where rdk.nsc = ntfbs.nsc;
create index tfbs_nsc on two_frag_both_salts(nsc);

-- qc

-- select count(*) from nsc_two_frag;
-- select count(*) from two_frag;
-- select count(distinct nsc) from two_frag;
-- select count(*) from nsc_two_frag_one_salt;
-- select count(*) from two_frag_one_salt;
-- select count(distinct nsc) from two_frag_one_salt;

--combine the one- and two-fragment parents into a single table

drop table if exists parent_with_salt;
create table parent_with_salt 
as 
select one_frag.*, 'free base' as matched_salt, null as salt_smiles 
from one_frag 
union 
select tfos.*, tfos_salt.known_salt_name as matched_salt, tfos_salt.smiles
from two_frag_one_salt tfos, two_frag_one_salt tfos_salt
where tfos.nsc = tfos_salt.nsc
and tfos.known_salt is null
and tfos_salt.known_salt = TRUE;

-- select count(*) from one_frag;
-- select count(distinct nsc) from one_frag;
-- select count(*) from two_frag_one_salt;
-- select count(distinct nsc) from two_frag_one_salt;
-- select count(*) from parent_with_salt;

--INDEX IT!

create index parent_with_salt_inchi on parent_with_salt(inchi);
create index parent_with_salt_matched_salt on parent_with_salt(matched_salt);
create index parent_with_salt_nsc on parent_with_salt(nsc);

vacuum analyze verbose parent_with_salt;

-- qc

-- select count(distinct nsc) from one_frag;
-- select count(distinct nsc) from two_frag_one_salt;
-- select count(distinct nsc) from parent_with_salt;

--

drop table if exists related;
create table related (relation varchar(32), ref_nsc int, ref_salt varchar(1024), rel_nsc int, rel_salt varchar(1024));

--identical, inchi = inchi, matched_salt = matched_salt

drop table if exists temp;

create table temp 
as
select distinct ref.inchi, ref.matched_salt, ref.nsc as ref_nsc, ref.matched_salt as ref_salt, rel.nsc as rel_nsc, rel.matched_salt as rel_salt
from parent_with_salt ref, parent_with_salt rel
where ref.inchi like 'InChI=%'
and ref.inchi = rel.inchi
and ref.matched_salt = rel.matched_salt
-- catch the identities here!
and ref.nsc != rel.nsc;

--TESTOSTERONE
--and ref.inchi = 'InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3';

insert into related(relation, ref_nsc, ref_salt, rel_nsc, rel_salt) 
select 'identical', ref_nsc, ref_salt, rel_nsc, rel_salt
from temp;

-- salt, inchi = inchi, matched_salt NOT EQUAL (!=) matched_salt

drop table if exists temp;

create table temp 
as
select distinct ref.inchi, ref.matched_salt, ref.nsc as ref_nsc, ref.matched_salt as ref_salt, rel.nsc as rel_nsc, rel.matched_salt as rel_salt
from parent_with_salt ref, parent_with_salt rel
where ref.inchi like 'InChI=%'
and ref.inchi = rel.inchi
and ref.matched_salt != rel.matched_salt;

--TESTOSTERONE
--and ref.inchi = 'InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3';

insert into related(relation, ref_nsc, ref_salt, rel_nsc, rel_salt) 
select 'salt', ref_nsc, ref_salt, rel_nsc, rel_salt
from temp;

-- related with mix of identical and salt

drop table if exists count_identical_by_nsc;
create table count_identical_by_nsc 
as
select ref_nsc as nsc, count(*) as count_identical 
from related
where relation = 'identical'
group by ref_nsc;

drop table if exists count_salt_by_nsc;
create table count_salt_by_nsc 
as
select ref_nsc as nsc, count(*) as count_salt 
from related
where relation = 'salt'
group by ref_nsc;

drop table if exists count_relation_summary;
create table count_relation_summary
as select count_identical_by_nsc.nsc, count_identical, count_salt 
from count_identical_by_nsc full outer join count_salt_by_nsc
on count_identical_by_nsc.nsc = count_salt_by_nsc.nsc;

drop table if exists temp;

create table temp 
as
select crs.nsc, crs.count_identical, crs.count_salt, pws.smiles, pws.matched_salt 
from count_relation_summary crs, parent_with_salt pws
where count_identical > 0 and count_salt > 0
and crs.nsc = pws.nsc
order by count_identical desc, count_salt desc;

