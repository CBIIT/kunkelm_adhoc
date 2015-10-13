drop table if exists temp_atompairbv_fp;

create table temp_atompairbv_fp 
as
select nsc, atompairbv_fp(mol) as fp
from rdkit_mol where nsc in ( select nsc
from app_and_inv );

drop table if exists temp_featmorganbv_fp;

create table temp_featmorganbv_fp 
as
select nsc, featmorganbv_fp(mol) as fp
from rdkit_mol where nsc in ( select nsc
from app_and_inv );

drop table if exists temp_layered_fp;

create table temp_layered_fp 
as
select nsc, layered_fp(mol) as fp
from rdkit_mol where nsc in ( select nsc
from app_and_inv );

drop table if exists temp_maccs_fp;

create table temp_maccs_fp 
as
select nsc, maccs_fp(mol) as fp
from rdkit_mol where nsc in ( select nsc
from app_and_inv );

drop table if exists temp_morganbv_fp;

create table temp_morganbv_fp 
as
select nsc, morganbv_fp(mol) as fp
from rdkit_mol where nsc in ( select nsc
from app_and_inv );

drop table if exists temp_rdkit_fp;

create table temp_rdkit_fp 
as
select nsc, rdkit_fp(mol) as fp
from rdkit_mol where nsc in ( select nsc
from app_and_inv );

drop table if exists temp_torsionbv_fp;

create table temp_torsionbv_fp 
as
select nsc, torsionbv_fp(mol) as fp
from rdkit_mol where nsc in ( select nsc
from app_and_inv );

atompairbv_fpfeatmorganbv_fplayered_fpmaccs_fpmorganbv_fprdkit_fptorsionbv_fpdrop table if exists temp_tanimoto_atompairbv_fp;

create table temp_tanimoto_atompairbv_fp 
as
select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto
from temp_atompairbv_fp uno, temp_atompairbv_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_featmorganbv_fp;

create table temp_tanimoto_featmorganbv_fp 
as
select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto
from temp_featmorganbv_fp uno, temp_featmorganbv_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_layered_fp;

create table temp_tanimoto_layered_fp 
as
select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto
from temp_layered_fp uno, temp_layered_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_maccs_fp;

create table temp_tanimoto_maccs_fp 
as
select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto
from temp_maccs_fp uno, temp_maccs_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_morganbv_fp;

create table temp_tanimoto_morganbv_fp 
as
select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto
from temp_morganbv_fp uno, temp_morganbv_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_rdkit_fp;

create table temp_tanimoto_rdkit_fp 
as
select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto
from temp_rdkit_fp uno, temp_rdkit_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_torsionbv_fp;

create table temp_tanimoto_torsionbv_fp 
as
select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto
from temp_torsionbv_fp uno, temp_torsionbv_fp duo where uno.nsc < duo.nsc;

atompairbv_fpfeatmorganbv_fplayered_fpmaccs_fpmorganbv_fprdkit_fptorsionbv_fpselect count(*)
from temp_tanimoto_atompairbv_fp;

select count(*)
from temp_tanimoto_featmorganbv_fp;

select count(*)
from temp_tanimoto_layered_fp;

select count(*)
from temp_tanimoto_maccs_fp;

select count(*)
from temp_tanimoto_morganbv_fp;

select count(*)
from temp_tanimoto_rdkit_fp;

select count(*)
from temp_tanimoto_torsionbv_fp;

drop table if exists temp_tanimoto_collate;

create table temp_tanimoto_collate 
as
select all_pair_nsc.uno, all_pair_nsc.duo 
, temp_tanimoto_atompairbv_fp.tanimoto as atompairbv_fp_tanimoto
, temp_tanimoto_featmorganbv_fp.tanimoto as featmorganbv_fp_tanimoto
, temp_tanimoto_layered_fp.tanimoto as layered_fp_tanimoto
, temp_tanimoto_maccs_fp.tanimoto as maccs_fp_tanimoto
, temp_tanimoto_morganbv_fp.tanimoto as morganbv_fp_tanimoto
, temp_tanimoto_rdkit_fp.tanimoto as rdkit_fp_tanimoto
, temp_tanimoto_torsionbv_fp.tanimoto as torsionbv_fp_tanimoto

from all_pair_nsc 
left outer join temp_tanimoto_atompairbv_fp on all_pair_nsc.uno = temp_tanimoto_atompairbv_fp.nsc1 and all_pair_nsc.duo = temp_tanimoto_atompairbv_fp.nsc2
left outer join temp_tanimoto_featmorganbv_fp on all_pair_nsc.uno = temp_tanimoto_featmorganbv_fp.nsc1 and all_pair_nsc.duo = temp_tanimoto_featmorganbv_fp.nsc2
left outer join temp_tanimoto_layered_fp on all_pair_nsc.uno = temp_tanimoto_layered_fp.nsc1 and all_pair_nsc.duo = temp_tanimoto_layered_fp.nsc2
left outer join temp_tanimoto_maccs_fp on all_pair_nsc.uno = temp_tanimoto_maccs_fp.nsc1 and all_pair_nsc.duo = temp_tanimoto_maccs_fp.nsc2
left outer join temp_tanimoto_morganbv_fp on all_pair_nsc.uno = temp_tanimoto_morganbv_fp.nsc1 and all_pair_nsc.duo = temp_tanimoto_morganbv_fp.nsc2
left outer join temp_tanimoto_rdkit_fp on all_pair_nsc.uno = temp_tanimoto_rdkit_fp.nsc1 and all_pair_nsc.duo = temp_tanimoto_rdkit_fp.nsc2
left outer join temp_tanimoto_torsionbv_fp on all_pair_nsc.uno = temp_tanimoto_torsionbv_fp.nsc1 and all_pair_nsc.duo = temp_tanimoto_torsionbv_fp.nsc2;

\copy temp_tanimoto_atompairbv_fp to /tmp/temp_tanimoto_atompairbv_fp.csv csv header;

\copy temp_tanimoto_featmorganbv_fp to /tmp/temp_tanimoto_featmorganbv_fp.csv csv header;

\copy temp_tanimoto_layered_fp to /tmp/temp_tanimoto_layered_fp.csv csv header;

\copy temp_tanimoto_maccs_fp to /tmp/temp_tanimoto_maccs_fp.csv csv header;

\copy temp_tanimoto_morganbv_fp to /tmp/temp_tanimoto_morganbv_fp.csv csv header;

\copy temp_tanimoto_rdkit_fp to /tmp/temp_tanimoto_rdkit_fp.csv csv header;

\copy temp_tanimoto_torsionbv_fp to /tmp/temp_tanimoto_torsionbv_fp.csv csv header
