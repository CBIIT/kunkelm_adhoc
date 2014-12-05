
drop table if exists rdkit_validity;

create table rdkit_validity 
as 
select nsc, 
is_valid_smiles(can_smi::cstring) as valid_can_smi, 
is_valid_smiles(can_taut::cstring) as valid_can_taut,
is_valid_smiles(can_taut_strip_stereo::cstring) as valid_can_taut_strip_stereo
from cmpd_table
where can_smi is not null 
and can_smi != '[RH]';


select valid_can_smi, valid_can_taut, valid_can_taut_strip_stereo, count(*) 
from rdkit_validity 
group by 1, 2, 3 order by 4 desc;

--datasystemdb-# group by 1, 2, 3 order by 4 desc;
-- valid_can_smi | valid_can_taut | valid_can_taut_strip_stereo | count  
-----------------+----------------+-----------------------------+--------
-- t             | t              | t                           | 668637
-- f             | f              | f                           |   5738
-- f             | f              | t                           |      8
--(3 rows)

create index rdkv_nsc on rdkit_validity(nsc);
vacuum analyze verbose rdkit_validity;

drop table if exists rdkit_mol;

create table rdkit_mol(
id bigint,
nsc int,
mol mol);

insert into rdkit_mol(id, nsc, mol)
select
ct.nsc, ct.nsc, mol_from_smiles(ct.can_taut::cstring)
from cmpd_table ct, rdkit_validity v
where ct.nsc = v.nsc
and v.valid_can_taut = 't';

--INSERT 0 667938
--Time: 240173.706 ms

create index rdkit_mol_mol on rdkit_mol using gist(mol);

-- write failed fragments, for forensics
drop table if exists rdkit_fail;

create table rdkit_fail 
as 
select ct.nsc, ct.can_smi, molecular_formula
from cmpd_table ct, rdkit_validity v 
where rs3.nsc = v.nsc
and v.valid_can_smi = 'f';

create index rdkf_fragmentindex on rdkit_fail(fragmentindex);

-- substructure test

select can_smi, can_taut from cmpd_table where nsc = 9700;
               can_smi               |              can_taut               
-------------------------------------+-------------------------------------
 CC12CCC3C(CCC4=CC(=O)CCC34C)C1CCC2O | CC12CCC3C(CCC4=CC(=O)CCC34C)C1CCC2O

select nsc, mol from rdkit_mol  where 'CC12CCC3C(CCC4=CC(=O)CCC34C)C1CCC2O' <@ mol;



743380:

JCP: OCc2cn(c1ccccc12)Cc3ccccc3
ChemDoodle: OCC2CN(CC1CCCCC1)C3CCCCC23

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
