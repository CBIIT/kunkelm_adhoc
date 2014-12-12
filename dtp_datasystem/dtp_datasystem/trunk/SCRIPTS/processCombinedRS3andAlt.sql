\c datasystemdb

drop table if exists combined_rs3_and_alt;

create table combined_rs3_and_alt(
NSC int,
MF varchar(1024),
MW double precision,
RS3_MF varchar(1024),
RS3_MW double precision,
RS3_Num_Atoms int,
RS3_Num_Fragments int, 
ERROR_MSG varchar(1024),
Alt_MF varchar(1024),
Alt_MW double precision,
Alt_Num_Atoms int,
Alt_Num_Fragments int
);

\copy combined_rs3_and_alt from /home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/combinedRS3andAlt.tsv csv header delimiter as E'\t' 

select count(*) from combined_rs3_and_alt
where alt_num_atoms = rs3_num_atoms + 1
and alt_num_fragments = rs3_num_fragments + 1
and round(mw) = round(alt_mw);