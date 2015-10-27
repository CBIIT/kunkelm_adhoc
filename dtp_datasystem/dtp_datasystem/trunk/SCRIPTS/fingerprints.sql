start: 20151019 08:09:01.444
drop table if exists temp_atompairbv_fp;

create table temp_atompairbv_fp as select nsc, atompairbv_fp(mol) as fp from rdkit_mol where nsc in ( select nsc from app_and_inv );

drop table if exists temp_featmorganbv_fp;

create table temp_featmorganbv_fp as select nsc, featmorganbv_fp(mol) as fp from rdkit_mol where nsc in ( select nsc from app_and_inv );

drop table if exists temp_layered_fp;

create table temp_layered_fp as select nsc, layered_fp(mol) as fp from rdkit_mol where nsc in ( select nsc from app_and_inv );

drop table if exists temp_maccs_fp;

create table temp_maccs_fp as select nsc, maccs_fp(mol) as fp from rdkit_mol where nsc in ( select nsc from app_and_inv );

drop table if exists temp_morganbv_fp;

create table temp_morganbv_fp as select nsc, morganbv_fp(mol) as fp from rdkit_mol where nsc in ( select nsc from app_and_inv );

drop table if exists temp_rdkit_fp;

create table temp_rdkit_fp as select nsc, rdkit_fp(mol) as fp from rdkit_mol where nsc in ( select nsc from app_and_inv );

drop table if exists temp_torsionbv_fp;

create table temp_torsionbv_fp as select nsc, torsionbv_fp(mol) as fp from rdkit_mol where nsc in ( select nsc from app_and_inv );

drop table if exists temp_tanimoto_atompairbv_fp;

create table temp_tanimoto_atompairbv_fp as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_atompairbv_fp uno, temp_atompairbv_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_featmorganbv_fp;

create table temp_tanimoto_featmorganbv_fp as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_featmorganbv_fp uno, temp_featmorganbv_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_layered_fp;

create table temp_tanimoto_layered_fp as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_layered_fp uno, temp_layered_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_maccs_fp;

create table temp_tanimoto_maccs_fp as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_maccs_fp uno, temp_maccs_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_morganbv_fp;

create table temp_tanimoto_morganbv_fp as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_morganbv_fp uno, temp_morganbv_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_rdkit_fp;

create table temp_tanimoto_rdkit_fp as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_rdkit_fp uno, temp_rdkit_fp duo where uno.nsc < duo.nsc;

drop table if exists temp_tanimoto_torsionbv_fp;

create table temp_tanimoto_torsionbv_fp as select uno.nsc as nsc1, duo.nsc nsc2, tanimoto_dist(uno.fp, duo.fp) as tanimoto from temp_torsionbv_fp uno, temp_torsionbv_fp duo where uno.nsc < duo.nsc;

select count(*) from temp_tanimoto_atompairbv_fp;

select count(*) from temp_tanimoto_featmorganbv_fp;

select count(*) from temp_tanimoto_layered_fp;

select count(*) from temp_tanimoto_maccs_fp;

select count(*) from temp_tanimoto_morganbv_fp;

select count(*) from temp_tanimoto_rdkit_fp;

select count(*) from temp_tanimoto_torsionbv_fp;

create table temp_tanimoto_collate as select all_pair_nsc.uno, all_pair_nsc.duo 
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

drop table if exists temp_tanimoto_stacked;

create table temp_tanimoto_stacked( uno int, duo int, fp varchar, tanimoto double precision );

insert into temp_tanimoto_stacked select nsc1, nsc2, 'atompairbv_fp', tanimoto from temp_tanimoto_atompairbv_fp where nsc1 < nsc2;

insert into temp_tanimoto_stacked select nsc1, nsc2, 'featmorganbv_fp', tanimoto from temp_tanimoto_featmorganbv_fp where nsc1 < nsc2;

insert into temp_tanimoto_stacked select nsc1, nsc2, 'layered_fp', tanimoto from temp_tanimoto_layered_fp where nsc1 < nsc2;

insert into temp_tanimoto_stacked select nsc1, nsc2, 'maccs_fp', tanimoto from temp_tanimoto_maccs_fp where nsc1 < nsc2;

insert into temp_tanimoto_stacked select nsc1, nsc2, 'morganbv_fp', tanimoto from temp_tanimoto_morganbv_fp where nsc1 < nsc2;

insert into temp_tanimoto_stacked select nsc1, nsc2, 'rdkit_fp', tanimoto from temp_tanimoto_rdkit_fp where nsc1 < nsc2;

insert into temp_tanimoto_stacked select nsc1, nsc2, 'torsionbv_fp', tanimoto from temp_tanimoto_torsionbv_fp where nsc1 < nsc2;

\copy temp_tanimoto_collate to /tmp/temp_tanimoto_collate.csv csv header;

\copy temp_tanimoto_atompairbv_fp to /tmp/temp_tanimoto_atompairbv_fp.csv csv header;

\copy temp_tanimoto_featmorganbv_fp to /tmp/temp_tanimoto_featmorganbv_fp.csv csv header;

\copy temp_tanimoto_layered_fp to /tmp/temp_tanimoto_layered_fp.csv csv header;

\copy temp_tanimoto_maccs_fp to /tmp/temp_tanimoto_maccs_fp.csv csv header;

\copy temp_tanimoto_morganbv_fp to /tmp/temp_tanimoto_morganbv_fp.csv csv header;

\copy temp_tanimoto_rdkit_fp to /tmp/temp_tanimoto_rdkit_fp.csv csv header;

\copy temp_tanimoto_torsionbv_fp to /tmp/temp_tanimoto_torsionbv_fp.csv csv header;

start: 20151019 08:09:01.444
finish: 20151019 08:09:01.469
