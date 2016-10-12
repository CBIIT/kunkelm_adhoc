-- when either updating the cmpd_known_salt table
-- OR qc_with_nsc
-- OR etc.

update cmpd_fragment set cmpd_known_salt_fk = rs3_from_plp_frags.salt_id 
from rs3_from_plp_frags 
where cmpd_fragment.id = rs3_from_plp_frags.fragmentindex 
and rs3_from_plp_frags.salt_id is not null;	
 
alter table cmpd_table add salt_id bigint;

update cmpd_table
set salt_id = rs3_from_plp_frags.salt_id
from rs3_from_plp_frags
where cmpd_table.nsc = rs3_from_plp_frags.nsc
and rs3_from_plp_frags.salt_id is not null
and cmpd_table.nsc in (
select nsc from qc_with_nsc where source like '%two_strc_one_salt'
);

update cmpd_table
set salt_smiles = cmpd_known_salt.can_taut,
	salt_name = cmpd_known_salt.salt_name,
	salt_mf = cmpd_known_salt.salt_mf,
	salt_mw = cmpd_known_salt.salt_mw
from cmpd_known_salt
where cmpd_table.salt_id = cmpd_known_salt.id;