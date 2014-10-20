\c datasystemdb

drop table if exists temp_salt_smiles;

create table temp_salt_smiles
as
select distinct salt_smiles
from two_strc_any_lbl_one_salt;


