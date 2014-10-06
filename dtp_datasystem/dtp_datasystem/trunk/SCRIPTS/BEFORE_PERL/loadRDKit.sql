
drop table if exists rdkit_validity;

create table rdkit_validity 
as 
select nsc, fragmentindex, 
is_valid_smiles(can_smi::cstring) as valid_can_smi, 
is_valid_smiles(can_taut::cstring) as valid_can_taut,
is_valid_smiles(can_taut_strip_stereo::cstring) as valid_can_taut_strip_stereo
from rs3_from_plp_frags
where can_smi is not null and can_smi != '[RH]';


--datasystemdb=# select valid_can_smi, valid_can_taut, valid_can_taut_strip_stereo, count(*) from rdkit_validity group by 1, 2, 3 order by 4 desc;
-- valid_can_smi | valid_can_taut | valid_can_taut_strip_stereo | count  
-----------------+----------------+-----------------------------+--------
-- t             | t              | t                           | 765207
-- f             | f              | f                           |   7348
-- f             | f              | t                           |      8

create index rdkv_nsc on rdkit_validity(nsc);
create index rdkv_fragmentindex on rdkit_validity(fragmentindex);

vacuum analyze verbose rdkit_validity;

-- write failed fragments, for forensics
drop table if exists rdkit_fail;

create table rdkit_fail 
as 
select rs3.nsc, rs3.fragmentindex, can_smi, molecular_formula
from rs3_from_plp_frags rs3, rdkit_validity v 
where rs3.fragmentindex = v.fragmentindex 
and v.valid_can_smi = 'f';

create index rdkf_fragmentindex on rdkit_fail(fragmentindex);

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
