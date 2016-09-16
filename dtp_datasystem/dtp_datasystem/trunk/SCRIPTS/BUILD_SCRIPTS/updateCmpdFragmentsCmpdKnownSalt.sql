update cmpd_fragments
set cmpd_known_salt_fk = null;

create index cks_can_taut on cmpd_known_salt(can_taut);
create index cks_charge on cmpd_known_salt(charge);

create index cfs_can_taut on cmpd_fragment_structure(can_taut);
create index cfp_formal_charge on cmpd_fragment_p_chem(formal_charge);

vacuum analyze verbose cmpd_known_salt;
vacuum analyze verbose cmpd_fragment_structure;

drop table if exists by_can_taut_only;

create table by_can_taut_only
as
select frag.id as frag_id, salt.*, struc.can_taut as frag_can_taut, pchem.molecular_weight, pchem.molecular_formula
from cmpd_known_salt salt, cmpd_fragment frag, cmpd_fragment_p_chem pchem, cmpd_fragment_structure struc
where frag.cmpd_fragment_p_chem_fk = pchem.id
and frag.cmpd_fragment_structure_fk = struc.id
and salt.can_taut = struc.can_taut;


datasystemdb=# select distinct salt_name from by_can_taut_only where frag_id in (select frag_id from by_can_taut_only group by 1 having count(*) > 1);
   salt_name   
---------------
 Hydrochloride
 Chloride
 Hydrofluoride
 Hydrobromide
 Iron (II)
 Bromide
 Iodide
 Iron (III)
 Hydroiodide
 Fluoride






drop table if exists by_can_taut_charge;

create table by_can_taut_charge
as
select frag.id as frag_id, salt.*, struc.can_taut as frag_can_taut, pchem.molecular_weight, pchem.molecular_formula
from cmpd_known_salt salt, cmpd_fragment frag, cmpd_fragment_p_chem pchem, cmpd_fragment_structure struc
where frag.cmpd_fragment_p_chem_fk = pchem.id
and frag.cmpd_fragment_structure_fk = struc.id
and salt.can_taut = struc.can_taut
and salt.salt_charge = pchem.formal_charge;

select salt_name, count(*) 
from by_can_taut_charge 
group by 1 order by 2 desc;
