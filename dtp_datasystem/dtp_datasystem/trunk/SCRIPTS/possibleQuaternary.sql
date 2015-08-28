 create table possible_quaternary
as
select nsc 
from rdkit_mol 
where mol_from_smarts('[*][#7+]([*])([*])[*]') <@ mol;


