drop table if exists temp_fp;

create table temp_fp 
as 
select nsc, morganbv_fp(mol) as fp 
from rdkit_mol 
where mol is not null 
limit 100;

drop table if exists temp_tanimoto;

create table temp_tanimoto as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_fp uno, temp_fp duo;

select min(tanimoto), max(tanimoto), avg(tanimoto), count(tanimoto)
from temp_tanimoto;

