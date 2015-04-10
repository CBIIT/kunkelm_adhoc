
--InChI errors for aromatic bonds are all NSC >2000000

-- create these indexes if they don't exist

create index mol_frag_nsc on mol_frag(nsc);
create index mol_frag_frag_id on mol_frag(frag_id);
vacuum analyze verbose mol_frag;

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

drop index if exists rdkmf_frag_id;
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
select mfv.nsc, mfv.frag_id, mf.smiles, mf.inchi, mf.ctab, mfv.is_valid as mol_frag_valid, rdkmfv.valid_ctab as rdkit_valid
from mol_frag_validity mfv, mol_frag mf, rdkit_mol_frag_validity rdkmfv
where mfv.frag_id = rdkmfv.frag_id
and mfv.frag_id = mf.frag_id;

--select mol_frag_valid, rdkit_valid, count(*) from resolved_validity where mol_frag_valid != rdkit_valid group by 1,2;
select mol_frag_valid, rdkit_valid, inchi from resolved_validity where mol_frag_valid = 'f' and rdkit_valid = 't';
 
--
--
--

--drop table if exists inchi_formula; 
--create table inchi_formula as select frag_id, inchi, array_to_string(regexp_matches(inchi, 'InChI=1S/(.*?)[/|$]'),';') as inchi_formula from mol_frag;
--create index inchi_formula_frag_id on inchi_formula(frag_id);
--
--drop table if exists inchi_connect; 
--create table inchi_connect as select frag_id, inchi, array_to_string(regexp_matches(inchi, '(/c.*?)[/|$]'),';') as inchi_connect from mol_frag;
--create index inchi_connect_frag_id on inchi_connect(frag_id);
--
--drop table if exists inchi_hydrogens; 
--create table inchi_hydrogens as select frag_id, inchi, array_to_string(regexp_matches(inchi, '(/h.*?)[/|$]'),';') as inchi_hydrogens from mol_frag; 
--create index inchi_hydrogens_frag_id on inchi_hydrogens(frag_id);
--
--drop table if exists inchi_charges; 
--create table inchi_charges as select frag_id, inchi, array_to_string(regexp_matches(inchi, '(/[p|q].*?)[/|$]'),';') as inchi_charges from mol_frag;
--create index inchi_charges_frag_id on inchi_charges(frag_id);
--
--drop table if exists inchi_stereo; 
--create table inchi_stereo as select frag_id, inchi, array_to_string(regexp_matches(inchi, '(/[s|t].*?)[/|$]'),';') as inchi_stereo from mol_frag;--
--create index inchi_stereo_frag_id on inchi_stereo(frag_id);
--
--
--
--

-- instead of updating rdkit_mol_frag, create temp
-- to do rdkit property calcs for frag,
-- rdkit smiles, 
-- fetch InChI, and various substrings from mol_frag

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

--inchi_formula.inchi_formula,
--inchi_connect.inchi_connect,
--inchi_hydrogens.inchi_hydrogens,
--inchi_charges.inchi_charges,
--inchi_stereo.inchi_stereo

from rdkit_mol_frag, mol_frag
where rdkit_mol_frag.frag_id = mol_frag.frag_id;

--left outer join inchi_formula on rdkit_mol_frag.frag_id = inchi_formula.frag_id
--left outer join inchi_connect on rdkit_mol_frag.frag_id = inchi_connect.frag_id
--left outer join inchi_hydrogens on rdkit_mol_frag.frag_id = inchi_hydrogens.frag_id
--left outer join inchi_charges on rdkit_mol_frag.frag_id = inchi_charges.frag_id
--left outer join inchi_stereo on rdkit_mol_frag.frag_id = inchi_stereo.frag_id;

-- swap temp into the real table
drop table if exists rdkit_mol_frag_backup;
alter table rdkit_mol_frag rename to rdkit_mol_frag_backup;
alter table temp rename to rdkit_mol_frag; 

-- index the structures
create index rdkit_mol_frag_mol on rdkit_mol_frag using gist(frag_mol);
create index rdkit_mol_frag_frag_id on rdkit_mol_frag(frag_id);
create index rdkit_mol_frag_nsc on rdkit_mol_frag(nsc);
create index rdkit_mol_frag_smiles on rdkit_mol_frag(smiles);
create index rdkit_mol_frag_inchi on rdkit_mol_frag(inchi);
create index rdkit_mol_frag_inchi_formula on rdkit_mol_frag(inchi_formula);
create index rdkit_mol_frag_inchi_connect on rdkit_mol_frag(inchi_connect);
create index rdkit_mol_frag_inchi_hydrogens on rdkit_mol_frag(inchi_hydrogens);
create index rdkit_mol_frag_inchi_charges on rdkit_mol_frag(inchi_charges);
create index rdkit_mol_frag_inchi_stereo on rdkit_mol_frag(inchi_stereo);

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
select count(*) from nsc_one_frag;
select count(*) from nsc_one_frag_not_salt;
select count(*) from one_frag;

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

drop table if exists nsc_two_frag_no_salts;
create table nsc_two_frag_no_salts as select nsc from two_frag group by nsc having count(known_salt) = 0;
create index ntfns_nsc on nsc_two_frag_no_salts(nsc);

drop table if exists two_frag_no_salts;
create table two_frag_no_salts as select rdk.* from rdkit_mol_frag rdk, nsc_two_frag_no_salts ntfbs where rdk.nsc = ntfbs.nsc;
create index tfns_nsc on two_frag_no_salts(nsc);


-- qc

select count(*) from nsc_two_frag;
select count(*) from two_frag;
select count(distinct nsc) from two_frag;
select count(*) from nsc_two_frag_one_salt;
select count(*) from two_frag_one_salt;
select count(distinct nsc) from two_frag_one_salt;

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

--create index parent_with_salt_inchi_formula on parent_with_salt(inchi_formula);
--create index parent_with_salt_inchi_connect on parent_with_salt(inchi_connect);
--create index parent_with_salt_inchi_hydrogens on parent_with_salt(inchi_hydrogens);
--create index parent_with_salt_inchi_charges on parent_with_salt(inchi_charges);
--create index parent_with_salt_inchi_stereo on parent_with_salt(inchi_stereo);

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
select distinct 
ref.inchi, 
ref.matched_salt, 
ref.nsc as ref_nsc, 
ref.matched_salt as ref_salt, 
rel.nsc as rel_nsc, 
rel.matched_salt as rel_salt
from parent_with_salt ref, parent_with_salt rel
where ref.inchi like 'InChI=%'
and ref.inchi = rel.inchi
and ref.matched_salt  = rel.matched_salt
-- catch the identities here!
and ref.nsc != rel.nsc;

--TESTOSTERONE
--and ref.inchi = 'InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3';

insert into related(relation, ref_nsc, ref_salt, rel_nsc, rel_salt) 
select 'identical', ref_nsc, ref_salt, rel_nsc, rel_salt
from temp;

-- matched_salt NOT EQUAL (!=) matched_salt

drop table if exists temp;

create table temp 
as
select distinct 
ref.inchi,
ref.matched_salt, 
ref.nsc as ref_nsc, 
ref.matched_salt as ref_salt, 
rel.nsc as rel_nsc, 
rel.matched_salt as rel_salt
from parent_with_salt ref, parent_with_salt rel
where ref.inchi like 'InChI=%'
and ref.inchi = rel.inchi
and ref.matched_salt != rel.matched_salt;

--TESTOSTERONE
--and ref.inchi = 'InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3';

insert into related(relation, ref_nsc, ref_salt, rel_nsc, rel_salt) 
select 'salt', ref_nsc, ref_salt, rel_nsc, rel_salt
from temp;

drop index if exists related_ref_nsc;
create index related_ref_nsc on related(ref_nsc);

drop index if exists related_rel_nsc;
create index related_rel_nsc on related(rel_nsc);

drop index if exists related_relation;
create index related_relation on related(relation);

vacuum analyze verbose related;


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

-- TESTOSTERONE

-- datasystemdb=# select inchi from mol_Frag where nsc = 9700;
--------                                                      inchi                                                      
-------------------------------------------------------------------------------------------------------------------------
-------- InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3
--------(1 row)
--------
--------datasystemdb=# select nsc, inchi from mol_Frag where inchi = (select inchi from mol_Frag where nsc = 9700);
--------   nsc   |                                                      inchi                                                      
-----------------+-----------------------------------------------------------------------------------------------------------------
--------    9700 | InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3
--------   26499 | InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3
--------   50917 | InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3
--------  523833 | InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3
--------  755838 | InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3
--------  764937 | InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3
-------- 2509410 | InChI=1S/C19H28O2/c1-18-9-7-13(20)11-12(18)3-4-14-15-5-6-17(21)19(15,2)10-8-16(14)18/h11,14-17,21H,3-10H2,1-2H3
--------(7 rows)
------

-- comparisons to PLP

drop table if exists plp_related;
create table plp_related(nsc int, related_nsc int, relation varchar(1024), crap varchar(1024));

\copy plp_related from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/DataSystemBuilder/RELATED_COMPOUNDS.csv csv header

drop index if exists plp_related_nsc;
create index plp_related_nsc on plp_related(nsc);

drop index if exists plp_related_related_nsc;
create index plp_related_related_nsc on plp_related(related_nsc);

drop index if exists plp_related_relation;
create index plp_related_relation on plp_related(relation);

vacuum analyze verbose plp_related;

-- strip out any imported plp_related relations that include nsc that are not 
-- part of parent_with_salt (assumption is that these would be mostly multi-frag and polymers)

create table temp 
as
select pr.nsc, pr.related_nsc, pr.relation
from plp_related pr, parent_with_salt pws, parent_with_salt pws2
where pr.nsc = pws.nsc
and pr.related_nsc = pws2.nsc;

-- substitute this in for the original related table

drop table plp_related;
alter table temp rename to plp_related;

--rebuild the indexes

drop index if exists plp_related_nsc;
create index plp_related_nsc on plp_related(nsc);

drop index if exists plp_related_related_nsc;
create index plp_related_related_nsc on plp_related(related_nsc);

drop index if exists plp_related_relation;
create index plp_related_relation on plp_related(relation);

vacuum analyze verbose plp_related;

-- paired nsc in one, but not the other

drop table if exists rp_nl;

create table rp_nl
as
select nsc, related_nsc
from plp_related
except
select ref_nsc, rel_nsc
from related;

drop table if exists related_plp_not_local;

create table related_plp_not_local
as
select r.nsc, r.related_nsc, pws.inchi as ref_inchi, pws2.inchi as rel_inchi
from rp_nl r, parent_with_salt pws, parent_with_salt pws2
where r.nsc = pws.nsc
and r.related_nsc = pws2.nsc;









drop table if exists rl_np;

create table rl_np
as
select ref_nsc, rel_nsc
from related
except
select nsc, related_nsc
from plp_related;

