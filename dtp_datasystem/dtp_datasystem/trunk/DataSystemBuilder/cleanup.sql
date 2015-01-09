drop table if exists known_and_possible_salts;

create table known_and_possible_salts 
as 
select smiles, 
known_salt,
count(*) as the_count
from rdkit_mol_frag
group by 1,2
having count(*) > 10;

drop table if exists temp;

create table temp 
as
select smiles, the_count||' '||known_salt
from known_and_possible_salts;

\copy temp to '/tmp/known_and_poss_salts.smi' csv delimiter as ' '

drop table if exists temp;

create table temp 
as
select ks.smiles, 'name: '||ks.salt_name||' id:'||ks.id||' count:'||count(pws.*)
from known_salt ks left outer join parent_with_salt pws
on pws.salt_smiles = ks.smiles
group by ks.smiles, ks.salt_name, ks.id;

 \copy temp to /tmp/known_salt.smi csv delimiter ' '
