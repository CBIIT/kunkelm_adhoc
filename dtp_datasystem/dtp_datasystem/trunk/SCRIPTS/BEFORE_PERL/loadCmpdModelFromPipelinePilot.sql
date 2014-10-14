

-- #     #         #     #         #    #          #######
-- ##    #         #     #         #   #           #      
-- # #   #         #     #         #  #            #      
-- #  #  #         #     #         ###             #####  
-- #   # #         #     #         #  #            #
-- #    ##         #     #         #   #           #      
-- #     #          #####          #    #          #######

-- truncate cmpd_fragment_p_chem cascade;
-- truncate cmpd_fragment_structure cascade;
-- truncate cmpd_fragment cascade;
-- truncate nsc_cmpd cascade;
-- truncate cmpd cascade;
-- truncate cmpd_bio_assay cascade;
-- truncate cmpd_inventory cascade;
-- truncate cmpd_annotation cascade;
-- truncate cmpd_alias cascade;
-- truncate cmpd_aliases2nsc_cmpds cascade;
-- truncate cmpd_project cascade;
-- truncate cmpd_projects2nsc_cmpds cascade;
-- truncate cmpd_plate cascade;
-- truncate cmpd_plates2nsc_cmpds cascade;
-- truncate cmpd_target cascade;
-- truncate cmpd_targets2nsc_cmpds cascade;
-- truncate cmpd_table cascade;

---- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

--    #    #    #  #####    ####   #####    #####          #####   #       #####
--    #    ##  ##  #    #  #    #  #    #     #            #    #  #       #    #
--    #    # ## #  #    #  #    #  #    #     #            #    #  #       #    #
--    #    #    #  #####   #    #  #####      #            #####   #       #####
--    #    #    #  #       #    #  #   #      #            #       #       #
--    #    #    #  #        ####   #    #     #            #       ######  #

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #####   #    #  #    #          #####   ######  #####   #
-- #    #  #    #  ##   #          #    #  #       #    #  #
-- #    #  #    #  # #  #          #    #  #####   #    #  #
-- #####   #    #  #  # #          #####   #       #####   #
-- #   #   #    #  #   ##          #       #       #   #   #
-- #    #   ####   #    #          #       ######  #    #  ######

-- SANITY and QC checking for db table column names and orders
-- and table creation handled by perl script checkPLPfiles.PL

-- the perl script does NOT actually make the load

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #####   #####           #        ####    ####   #####    #   #
-- #    #  #    #           #      #    #  #    #  #    #    # #
-- #    #  #####             #     #       #    #  #    #     #
-- #    #  #    #             #    #       #    #  #####      #
-- #    #  #    #              #   #    #  #    #  #          #
-- #####   #####                #   ####    ####   #          #

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

\copy rs3_from_plp_frags from '/home/mwkunkel/rs3_from_plp_400k_frags.txt' csv header delimiter as E'\t'
\copy rs3_from_plp_frags from '/home/mwkunkel/rs3_from_plp_remainder_frags.txt' csv header delimiter as E'\t'

--select min(fragmentindex), max(fragmentindex)  
--from rs3_from_plp_frags 
--where nsc <= 400000;

-- select min(fragmentindex), max(fragmentindex)  
-- from rs3_from_plp_frags 
-- where nsc >= 400000;

-- update rs3_from_plp_frags
-- set fragmentindex = fragmentindex + 1000000
-- where nsc >= 400000;


create index rfpf_nsc on rs3_from_plp_frags(nsc);
create index rfpf_fragmentindex on rs3_from_plp_frags(fragmentindex);
create index rfpf_can_smi on rs3_from_plp_frags(can_smi);
create index rfpf_can_taut on rs3_from_plp_frags(can_taut);
create index rfpf_can_taut_strip_stereo on rs3_from_plp_frags(can_taut_strip_stereo);
create index rfpf_atomarray on rs3_from_plp_frags(atomarray);

vacuum analyze verbose rs3_from_plp_frags;

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #    #  #####   #####     ##     #####  ######
-- #    #  #    #  #    #   #  #      #    #
-- #    #  #    #  #    #  #    #     #    #####
-- #    #  #####   #    #  ######     #    #
-- #    #  #       #    #  #    #     #    #
--  ####   #       #####   #    #     #    ######
--
--
--  ####     ##    #        #####   ####
-- #        #  #   #          #    #
--  ####   #    #  #          #     ####
--      #  ######  #          #         #
-- #    #  #    #  #          #    #    #
--  ####   #    #  ######     #     ####

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- UPDATE the frags table for salts

alter table rs3_from_plp_frags add salt_smiles varchar(1024);

update rs3_from_plp_frags
set salt_smiles = unique_salts.can_taut_strip_stereo
from unique_salts
where rs3_from_plp_frags.can_taut_strip_stereo = unique_salts.can_taut_strip_stereo;

-- 82127

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #    #  #####            #####   ####           #    #  ######  #####   ######
-- #    #  #    #             #    #    #          #    #  #       #    #  #
-- #    #  #    #             #    #    #          ######  #####   #    #  #####
-- #    #  #####              #    #    #          #    #  #       #####   #
-- #    #  #                  #    #    #          #    #  #       #   #   #
--  ####   #                  #     ####           #    #  ######  #    #  ######
--
--
-- #####   #####   ######  #####           ######   ####   #####
-- #    #  #    #  #       #    #          #       #    #  #    #
-- #    #  #    #  #####   #    #          #####   #    #  #    #
-- #####   #####   #       #####           #       #    #  #####
-- #       #   #   #       #               #       #    #  #   #
-- #       #    #  ######  #               #        ####   #    #
--
--
--  ####    #####    ##     #####   ####
-- #          #     #  #      #    #
--  ####      #    #    #     #     ####
--      #     #    ######     #         #
-- #    #     #    #    #     #    #    #
--  ####      #    #    #     #     ####

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #        ####     ##    #####
-- #       #    #   #  #   #    #
-- #       #    #  #    #  #    #
-- #       #    #  ######  #    #
-- #       #    #  #    #  #    #
-- ######   ####   #    #  #####

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------



-- #    #   ####    ####            #####   ####
-- ##   #  #       #    #             #    #    #
-- # #  #   ####   #                  #    #    #
-- #  # #       #  #                  #    #    #
-- #   ##  #    #  #    #             #    #    #
-- #    #   ####    ####  #######     #     ####  #######
--
--
-- #        ####     ##    #####
-- #       #    #   #  #   #    #
-- #       #    #  #    #  #    #
-- #       #    #  ######  #    #
-- #       #    #  #    #  #    #
-- ######   ####   #    #  #####

--categories

--one_strc
--two_strc_any_lbl_one_salt
--one_strc_one_lbl
--one_strc_multi_lbl
--one_lbl
--multi_lbl
--multi_strc_any_lbl
--two_strc_any_lbl_two_salt
--two_strc_any_lbl

drop table if exists temp;

create table temp(nsc int, cmpd_type varchar(1024));

insert into temp select distinct nsc, 'one_strc' from one_strc; -- limit 50;
insert into temp select distinct nsc, 'two_strc_any_lbl_one_salt' from two_strc_any_lbl_one_salt; -- limit 50;
insert into temp select distinct nsc, 'one_strc_one_lbl' from one_strc_one_lbl; -- limit 50;
insert into temp select distinct nsc, 'one_strc_multi_lbl' from one_strc_multi_lbl; -- limit 50;
insert into temp select distinct nsc, 'one_lbl' from one_lbl; -- limit 50;
insert into temp select distinct nsc, 'multi_lbl' from multi_lbl; -- limit 50;
insert into temp select distinct nsc, 'multi_strc_any_lbl' from multi_strc_any_lbl; -- limit 50;
insert into temp select distinct nsc, 'two_strc_any_lbl_two_salt' from two_strc_any_lbl_two_salt; -- limit 50;
insert into temp select distinct nsc, 'two_strc_any_lbl' from two_strc_any_lbl; -- limit 50;

drop table if exists nsc_to_load;

create table nsc_to_load as
select distinct nsc, cmpd_type 
from temp;

create index ntl_nsc on nsc_to_load(nsc);
create index ntl_cmpd_type on nsc_to_load(cmpd_type);

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #####   #####    ####   #####            ####    ####    #####  #####    #####
-- #    #  #    #  #    #  #    #          #    #  #          #    #    #     #
-- #    #  #    #  #    #  #    #          #        ####      #    #    #     #
-- #    #  #####   #    #  #####           #            #     #    #####      #
-- #    #  #   #   #    #  #               #    #  #    #     #    #   #      #
-- #####   #    #   ####   #                ####    ####      #    #    #     #

--http://blog.hagander.net/archives/131-Automatically-dropping-and-creating-constraints.html

--SELECT 'ALTER TABLE '||nspname||'&quot;.&quot;'||relname||'&quot; DROP CONSTRAINT &quot;'||conname||'&quot;;'
create table temp 
as
SELECT 'ALTER TABLE '||nspname||'.'||relname||' DROP CONSTRAINT '||conname||' cascade;'
 FROM pg_constraint 
 INNER JOIN pg_class ON conrelid=pg_class.oid 
 INNER JOIN pg_namespace ON pg_namespace.oid=pg_class.relnamespace
-- tables to process 
 WHERE relname in (
'cmpd', 
'cmpd_alias', 
'cmpd_alias_type', 
'cmpd_aliases2nsc_cmpds', 
'cmpd_annotation', 
'cmpd_bio_assay', 
'cmpd_fragment', 
'cmpd_fragment_p_chem', 
'cmpd_fragment_structure', 
'cmpd_inventory', 
'cmpd_known_salt', 
'cmpd_list', 
'cmpd_list_member', 
'cmpd_lists2share_with_users', 
'cmpd_plate', 
'cmpd_plates2nsc_cmpds', 
'cmpd_project', 
'cmpd_projects2nsc_cmpds', 
'cmpd_pub_chem_sid', 
'cmpd_pub_chem_sids2nsc_cmpds', 
'cmpd_related', 
'cmpd_relation_type', 
'cmpd_set', 
'cmpd_sets2nsc_cmpds', 
'cmpd_table', 
'cmpd_target', 
'cmpd_targets2nsc_cmpds'
)
 ORDER BY CASE WHEN contype='f' THEN 0 ELSE 1 END,contype,nspname,relname,conname

ALTER TABLE public.cmpd_alias DROP CONSTRAINT cmpd_alias_cmpd_alias_type_fkc;
ALTER TABLE public.cmpd_aliases2nsc_cmpds DROP CONSTRAINT cmpd_alias_nsc_cmpds_fkc;
ALTER TABLE public.cmpd_aliases2nsc_cmpds DROP CONSTRAINT nsc_cmpd_cmpd_aliases_fkc;
ALTER TABLE public.cmpd_fragment DROP CONSTRAINT cmpd_fragment_cmpd_fragment_pc;
ALTER TABLE public.cmpd_fragment DROP CONSTRAINT cmpd_fragment_cmpd_fragment_sc;
ALTER TABLE public.cmpd_fragment DROP CONSTRAINT cmpd_fragment_cmpd_known_saltc;
ALTER TABLE public.cmpd_fragment DROP CONSTRAINT cmpd_fragment_nsc_cmpd_fkc;
ALTER TABLE public.cmpd_list_member DROP CONSTRAINT cmpd_list_member_cmpd_fkc;
ALTER TABLE public.cmpd_list_member DROP CONSTRAINT cmpd_list_member_cmpd_list_fkc;
ALTER TABLE public.cmpd_lists2share_with_users DROP CONSTRAINT cmpd_list_share_with_users_fkc;
ALTER TABLE public.cmpd_lists2share_with_users DROP CONSTRAINT data_system_user_cmpd_lists_fc;
ALTER TABLE public.cmpd_plates2nsc_cmpds DROP CONSTRAINT cmpd_plate_nsc_cmpds_fkc;
ALTER TABLE public.cmpd_plates2nsc_cmpds DROP CONSTRAINT nsc_cmpd_cmpd_plates_fkc;
ALTER TABLE public.cmpd_projects2nsc_cmpds DROP CONSTRAINT cmpd_project_nsc_cmpds_fkc;
ALTER TABLE public.cmpd_projects2nsc_cmpds DROP CONSTRAINT nsc_cmpd_cmpd_projects_fkc;
ALTER TABLE public.cmpd_pub_chem_sids2nsc_cmpds DROP CONSTRAINT cmpd_pub_chem_sid_nsc_cmpds_fc;
ALTER TABLE public.cmpd_pub_chem_sids2nsc_cmpds DROP CONSTRAINT nsc_cmpd_cmpd_pub_chem_sids_fc;
ALTER TABLE public.cmpd_related DROP CONSTRAINT cmpd_related_cmpd_relation_tyc;
ALTER TABLE public.cmpd_related DROP CONSTRAINT cmpd_related_nsc_cmpd_fkm;
ALTER TABLE public.cmpd_sets2nsc_cmpds DROP CONSTRAINT cmpd_set_nsc_cmpds_fkc;
ALTER TABLE public.cmpd_sets2nsc_cmpds DROP CONSTRAINT nsc_cmpd_cmpd_sets_fkc;
ALTER TABLE public.cmpd_targets2nsc_cmpds DROP CONSTRAINT cmpd_target_nsc_cmpds_fkc;
ALTER TABLE public.cmpd_targets2nsc_cmpds DROP CONSTRAINT nsc_cmpd_cmpd_targets_fkc;
ALTER TABLE public.cmpd DROP CONSTRAINT cmpd_pkey;
ALTER TABLE public.cmpd_alias DROP CONSTRAINT cmpd_alias_pkey;
ALTER TABLE public.cmpd_alias_type DROP CONSTRAINT cmpd_alias_type_pkey;
ALTER TABLE public.cmpd_aliases2nsc_cmpds DROP CONSTRAINT cmpd_aliases2nsc_cmpds_pkey;
ALTER TABLE public.cmpd_annotation DROP CONSTRAINT cmpd_annotation_pkey;
ALTER TABLE public.cmpd_bio_assay DROP CONSTRAINT cmpd_bio_assay_pkey;
ALTER TABLE public.cmpd_fragment DROP CONSTRAINT cmpd_fragment_pkey;
ALTER TABLE public.cmpd_fragment_p_chem DROP CONSTRAINT cmpd_fragment_p_chem_pkey;
ALTER TABLE public.cmpd_fragment_structure DROP CONSTRAINT cmpd_fragment_structure_pkey;
ALTER TABLE public.cmpd_inventory DROP CONSTRAINT cmpd_inventory_pkey;
ALTER TABLE public.cmpd_known_salt DROP CONSTRAINT cmpd_known_salt_pkey;
ALTER TABLE public.cmpd_list DROP CONSTRAINT cmpd_list_pkey;
ALTER TABLE public.cmpd_list_member DROP CONSTRAINT cmpd_list_member_pkey;
ALTER TABLE public.cmpd_lists2share_with_users DROP CONSTRAINT cmpd_lists2share_with_users_pkey;
ALTER TABLE public.cmpd_plate DROP CONSTRAINT cmpd_plate_pkey;
ALTER TABLE public.cmpd_plates2nsc_cmpds DROP CONSTRAINT cmpd_plates2nsc_cmpds_pkey;
ALTER TABLE public.cmpd_project DROP CONSTRAINT cmpd_project_pkey;
ALTER TABLE public.cmpd_projects2nsc_cmpds DROP CONSTRAINT cmpd_projects2nsc_cmpds_pkey;
ALTER TABLE public.cmpd_pub_chem_sid DROP CONSTRAINT cmpd_pub_chem_sid_pkey;
ALTER TABLE public.cmpd_pub_chem_sids2nsc_cmpds DROP CONSTRAINT cmpd_pub_chem_sids2nsc_cmpds_pkey;
ALTER TABLE public.cmpd_related DROP CONSTRAINT cmpd_related_pkey;
ALTER TABLE public.cmpd_relation_type DROP CONSTRAINT cmpd_relation_type_pkey;
ALTER TABLE public.cmpd_set DROP CONSTRAINT cmpd_set_pkey;
ALTER TABLE public.cmpd_sets2nsc_cmpds DROP CONSTRAINT cmpd_sets2nsc_cmpds_pkey;
ALTER TABLE public.cmpd_table DROP CONSTRAINT cmpd_table_pkey;
ALTER TABLE public.cmpd_target DROP CONSTRAINT cmpd_target_pkey;
ALTER TABLE public.cmpd_targets2nsc_cmpds DROP CONSTRAINT cmpd_targets2nsc_cmpds_pkey;
ALTER TABLE public.cmpd_alias_type DROP CONSTRAINT cmpd_alias_type_alias_type_key;
ALTER TABLE public.cmpd_bio_assay DROP CONSTRAINT cmpd_bio_assay_sarcoma_key;
ALTER TABLE public.cmpd_fragment DROP CONSTRAINT cmpd_fragment_cmpd_fragment_p_chem_fk_key;
ALTER TABLE public.cmpd_fragment DROP CONSTRAINT cmpd_fragment_cmpd_fragment_structure_fk_key;
ALTER TABLE public.cmpd_list DROP CONSTRAINT cmpd_list_cmpd_list_id_key;
ALTER TABLE public.cmpd_list_member DROP CONSTRAINT cmpd_list_member_list_member_comment_key;
ALTER TABLE public.cmpd_plate DROP CONSTRAINT cmpd_plate_plate_name_key;
ALTER TABLE public.cmpd_project DROP CONSTRAINT cmpd_project_project_code_key;
ALTER TABLE public.cmpd_project DROP CONSTRAINT cmpd_project_project_name_key;
ALTER TABLE public.cmpd_related DROP CONSTRAINT cmpd_related_cmpd_relation_type_fk_key;
ALTER TABLE public.cmpd_related DROP CONSTRAINT cmpd_related_nsc_cmpd_fk_key;
ALTER TABLE public.cmpd_relation_type DROP CONSTRAINT cmpd_relation_type_relation_type_key;
ALTER TABLE public.cmpd_set DROP CONSTRAINT cmpd_set_set_name_key;
ALTER TABLE public.cmpd_table DROP CONSTRAINT cmpd_table_ad_hoc_cmpd_id_key;
ALTER TABLE public.cmpd_table DROP CONSTRAINT cmpd_table_nsc_cmpd_id_key;
ALTER TABLE public.cmpd_table DROP CONSTRAINT cmpd_table_nsc_key;
ALTER TABLE public.cmpd_target DROP CONSTRAINT cmpd_target_target_key;

--SELECT 'ALTER TABLE &quot;'||nspname||'&quot;.&quot;'||relname||'&quot; ADD CONSTRAINT &quot;'||conname||'&quot; '|| 
create table temp
as
SELECT 'ALTER TABLE '||nspname||'.'||relname||' ADD CONSTRAINT '||conname||' '||
   pg_get_constraintdef(pg_constraint.oid)||';'
 FROM pg_constraint
 INNER JOIN pg_class ON conrelid=pg_class.oid
 INNER JOIN pg_namespace ON pg_namespace.oid=pg_class.relnamespace
 -- tables to process
 WHERE relname in (
'cmpd', 
'cmpd_alias', 
'cmpd_alias_type', 
'cmpd_aliases2nsc_cmpds', 
'cmpd_annotation', 
'cmpd_bio_assay', 
'cmpd_fragment', 
'cmpd_fragment_p_chem', 
'cmpd_fragment_structure', 
'cmpd_inventory', 
'cmpd_known_salt', 
'cmpd_list', 
'cmpd_list_member', 
'cmpd_lists2share_with_users', 
'cmpd_plate', 
'cmpd_plates2nsc_cmpds', 
'cmpd_project', 
'cmpd_projects2nsc_cmpds', 
'cmpd_pub_chem_sid', 
'cmpd_pub_chem_sids2nsc_cmpds', 
'cmpd_related', 
'cmpd_relation_type', 
'cmpd_set', 
'cmpd_sets2nsc_cmpds', 
'cmpd_table', 
'cmpd_target', 
'cmpd_targets2nsc_cmpds'
) 
 ORDER BY CASE WHEN contype='f' THEN 0 ELSE 1 END DESC,contype DESC,nspname DESC,relname DESC,conname DESC;

ALTER TABLE public.cmpd_target ADD CONSTRAINT cmpd_target_target_key UNIQUE (target);
ALTER TABLE public.cmpd_table ADD CONSTRAINT cmpd_table_nsc_key UNIQUE (nsc);
ALTER TABLE public.cmpd_table ADD CONSTRAINT cmpd_table_nsc_cmpd_id_key UNIQUE (nsc_cmpd_id);
ALTER TABLE public.cmpd_table ADD CONSTRAINT cmpd_table_ad_hoc_cmpd_id_key UNIQUE (ad_hoc_cmpd_id);
ALTER TABLE public.cmpd_set ADD CONSTRAINT cmpd_set_set_name_key UNIQUE (set_name);
ALTER TABLE public.cmpd_relation_type ADD CONSTRAINT cmpd_relation_type_relation_type_key UNIQUE (relation_type);
ALTER TABLE public.cmpd_related ADD CONSTRAINT cmpd_related_nsc_cmpd_fk_key UNIQUE (nsc_cmpd_fk);
ALTER TABLE public.cmpd_related ADD CONSTRAINT cmpd_related_cmpd_relation_type_fk_key UNIQUE (cmpd_relation_type_fk);
ALTER TABLE public.cmpd_project ADD CONSTRAINT cmpd_project_project_name_key UNIQUE (project_name);
ALTER TABLE public.cmpd_project ADD CONSTRAINT cmpd_project_project_code_key UNIQUE (project_code);
ALTER TABLE public.cmpd_plate ADD CONSTRAINT cmpd_plate_plate_name_key UNIQUE (plate_name);
ALTER TABLE public.cmpd_list_member ADD CONSTRAINT cmpd_list_member_list_member_comment_key UNIQUE (list_member_comment);
ALTER TABLE public.cmpd_list ADD CONSTRAINT cmpd_list_cmpd_list_id_key UNIQUE (cmpd_list_id);
ALTER TABLE public.cmpd_fragment ADD CONSTRAINT cmpd_fragment_cmpd_fragment_structure_fk_key UNIQUE (cmpd_fragment_structure_fk);
ALTER TABLE public.cmpd_fragment ADD CONSTRAINT cmpd_fragment_cmpd_fragment_p_chem_fk_key UNIQUE (cmpd_fragment_p_chem_fk);
ALTER TABLE public.cmpd_bio_assay ADD CONSTRAINT cmpd_bio_assay_sarcoma_key UNIQUE (sarcoma);
ALTER TABLE public.cmpd_alias_type ADD CONSTRAINT cmpd_alias_type_alias_type_key UNIQUE (alias_type);
ALTER TABLE public.cmpd_targets2nsc_cmpds ADD CONSTRAINT cmpd_targets2nsc_cmpds_pkey PRIMARY KEY (nsc_cmpds_fk, cmpd_targets_fk);
ALTER TABLE public.cmpd_target ADD CONSTRAINT cmpd_target_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_table ADD CONSTRAINT cmpd_table_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_sets2nsc_cmpds ADD CONSTRAINT cmpd_sets2nsc_cmpds_pkey PRIMARY KEY (nsc_cmpds_fk, cmpd_sets_fk);
ALTER TABLE public.cmpd_set ADD CONSTRAINT cmpd_set_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_relation_type ADD CONSTRAINT cmpd_relation_type_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_related ADD CONSTRAINT cmpd_related_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_pub_chem_sids2nsc_cmpds ADD CONSTRAINT cmpd_pub_chem_sids2nsc_cmpds_pkey PRIMARY KEY (nsc_cmpds_fk, cmpd_pub_chem_sids_fk);
ALTER TABLE public.cmpd_pub_chem_sid ADD CONSTRAINT cmpd_pub_chem_sid_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_projects2nsc_cmpds ADD CONSTRAINT cmpd_projects2nsc_cmpds_pkey PRIMARY KEY (nsc_cmpds_fk, cmpd_projects_fk);
ALTER TABLE public.cmpd_project ADD CONSTRAINT cmpd_project_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_plates2nsc_cmpds ADD CONSTRAINT cmpd_plates2nsc_cmpds_pkey PRIMARY KEY (nsc_cmpds_fk, cmpd_plates_fk);
ALTER TABLE public.cmpd_plate ADD CONSTRAINT cmpd_plate_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_lists2share_with_users ADD CONSTRAINT cmpd_lists2share_with_users_pkey PRIMARY KEY (cmpd_lists_fk, share_with_users_fk);
ALTER TABLE public.cmpd_list_member ADD CONSTRAINT cmpd_list_member_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_list ADD CONSTRAINT cmpd_list_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_known_salt ADD CONSTRAINT cmpd_known_salt_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_inventory ADD CONSTRAINT cmpd_inventory_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_fragment_structure ADD CONSTRAINT cmpd_fragment_structure_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_fragment_p_chem ADD CONSTRAINT cmpd_fragment_p_chem_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_fragment ADD CONSTRAINT cmpd_fragment_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_bio_assay ADD CONSTRAINT cmpd_bio_assay_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_annotation ADD CONSTRAINT cmpd_annotation_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_aliases2nsc_cmpds ADD CONSTRAINT cmpd_aliases2nsc_cmpds_pkey PRIMARY KEY (nsc_cmpds_fk, cmpd_aliases_fk);
ALTER TABLE public.cmpd_alias_type ADD CONSTRAINT cmpd_alias_type_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_alias ADD CONSTRAINT cmpd_alias_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd ADD CONSTRAINT cmpd_pkey PRIMARY KEY (id);
ALTER TABLE public.cmpd_targets2nsc_cmpds ADD CONSTRAINT nsc_cmpd_cmpd_targets_fkc FOREIGN KEY (cmpd_targets_fk) REFERENCES cmpd_target(id);
ALTER TABLE public.cmpd_targets2nsc_cmpds ADD CONSTRAINT cmpd_target_nsc_cmpds_fkc FOREIGN KEY (nsc_cmpds_fk) REFERENCES nsc_cmpd(id);
ALTER TABLE public.cmpd_sets2nsc_cmpds ADD CONSTRAINT nsc_cmpd_cmpd_sets_fkc FOREIGN KEY (cmpd_sets_fk) REFERENCES cmpd_set(id);
ALTER TABLE public.cmpd_sets2nsc_cmpds ADD CONSTRAINT cmpd_set_nsc_cmpds_fkc FOREIGN KEY (nsc_cmpds_fk) REFERENCES nsc_cmpd(id);
ALTER TABLE public.cmpd_related ADD CONSTRAINT cmpd_related_nsc_cmpd_fkm FOREIGN KEY (nsc_cmpd_fk) REFERENCES nsc_cmpd(id);
ALTER TABLE public.cmpd_related ADD CONSTRAINT cmpd_related_cmpd_relation_tyc FOREIGN KEY (cmpd_relation_type_fk) REFERENCES cmpd_relation_type(id);
ALTER TABLE public.cmpd_pub_chem_sids2nsc_cmpds ADD CONSTRAINT nsc_cmpd_cmpd_pub_chem_sids_fc FOREIGN KEY (cmpd_pub_chem_sids_fk) REFERENCES cmpd_pub_chem_sid(id);
ALTER TABLE public.cmpd_pub_chem_sids2nsc_cmpds ADD CONSTRAINT cmpd_pub_chem_sid_nsc_cmpds_fc FOREIGN KEY (nsc_cmpds_fk) REFERENCES nsc_cmpd(id);
ALTER TABLE public.cmpd_projects2nsc_cmpds ADD CONSTRAINT nsc_cmpd_cmpd_projects_fkc FOREIGN KEY (cmpd_projects_fk) REFERENCES cmpd_project(id);
ALTER TABLE public.cmpd_projects2nsc_cmpds ADD CONSTRAINT cmpd_project_nsc_cmpds_fkc FOREIGN KEY (nsc_cmpds_fk) REFERENCES nsc_cmpd(id);
ALTER TABLE public.cmpd_plates2nsc_cmpds ADD CONSTRAINT nsc_cmpd_cmpd_plates_fkc FOREIGN KEY (cmpd_plates_fk) REFERENCES cmpd_plate(id);
ALTER TABLE public.cmpd_plates2nsc_cmpds ADD CONSTRAINT cmpd_plate_nsc_cmpds_fkc FOREIGN KEY (nsc_cmpds_fk) REFERENCES nsc_cmpd(id);
ALTER TABLE public.cmpd_lists2share_with_users ADD CONSTRAINT data_system_user_cmpd_lists_fc FOREIGN KEY (cmpd_lists_fk) REFERENCES cmpd_list(id);
ALTER TABLE public.cmpd_lists2share_with_users ADD CONSTRAINT cmpd_list_share_with_users_fkc FOREIGN KEY (share_with_users_fk) REFERENCES data_system_user(id);
ALTER TABLE public.cmpd_list_member ADD CONSTRAINT cmpd_list_member_cmpd_list_fkc FOREIGN KEY (cmpd_list_fk) REFERENCES cmpd_list(id);
ALTER TABLE public.cmpd_list_member ADD CONSTRAINT cmpd_list_member_cmpd_fkc FOREIGN KEY (cmpd_fk) REFERENCES cmpd(id);
ALTER TABLE public.cmpd_fragment ADD CONSTRAINT cmpd_fragment_nsc_cmpd_fkc FOREIGN KEY (nsc_cmpd_fk) REFERENCES nsc_cmpd(id);
ALTER TABLE public.cmpd_fragment ADD CONSTRAINT cmpd_fragment_cmpd_known_saltc FOREIGN KEY (cmpd_known_salt_fk) REFERENCES cmpd_known_salt(id);
ALTER TABLE public.cmpd_fragment ADD CONSTRAINT cmpd_fragment_cmpd_fragment_sc FOREIGN KEY (cmpd_fragment_structure_fk) REFERENCES cmpd_fragment_structure(id);
ALTER TABLE public.cmpd_fragment ADD CONSTRAINT cmpd_fragment_cmpd_fragment_pc FOREIGN KEY (cmpd_fragment_p_chem_fk) REFERENCES cmpd_fragment_p_chem(id);
ALTER TABLE public.cmpd_aliases2nsc_cmpds ADD CONSTRAINT nsc_cmpd_cmpd_aliases_fkc FOREIGN KEY (cmpd_aliases_fk) REFERENCES cmpd_alias(id);
ALTER TABLE public.cmpd_aliases2nsc_cmpds ADD CONSTRAINT cmpd_alias_nsc_cmpds_fkc FOREIGN KEY (nsc_cmpds_fk) REFERENCES nsc_cmpd(id);
ALTER TABLE public.cmpd_alias ADD CONSTRAINT cmpd_alias_cmpd_alias_type_fkc FOREIGN KEY (cmpd_alias_type_fk) REFERENCES cmpd_alias_type(id);

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #####   #####    ####    ####   ######   ####    ####
-- #    #  #    #  #    #  #    #  #       #       #
-- #    #  #    #  #    #  #       #####    ####    ####
-- #####   #####   #    #  #       #            #       #
-- #       #   #   #    #  #    #  #       #    #  #    #
-- #       #    #   ####    ####   ######   ####    ####
--
--
-- #####   #####    ####   #####           #####     ##     #####    ##
-- #    #  #    #  #    #  #    #          #    #   #  #      #     #  #
-- #    #  #    #  #    #  #    #          #    #  #    #     #    #    #
-- #####   #####   #    #  #    #          #    #  ######     #    ######
-- #       #   #   #    #  #    #          #    #  #    #     #    #    #
-- #       #    #   ####   #####           #####   #    #     #    #    #

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

--  ####   #    #  ######           #####   ####            ####   #    #  ######
-- #    #  ##   #  #                  #    #    #          #    #  ##   #  #
-- #    #  # #  #  #####              #    #    #          #    #  # #  #  #####
-- #    #  #  # #  #                  #    #    #          #    #  #  # #  #
-- #    #  #   ##  #                  #    #    #          #    #  #   ##  #
--  ####   #    #  ######             #     ####            ####   #    #  ######
--
--
-- #####      #     ####     ##     ####    ####     ##     #   #
-- #    #     #    #    #   #  #   #       #        #  #     # #
-- #####      #    #    #  #    #   ####    ####   #    #     #
-- #    #     #    #    #  ######       #       #  ######     #
-- #    #     #    #    #  #    #  #    #  #    #  #    #     #
-- #####      #     ####   #    #   ####    ####   #    #     #

create index prod_count_nci60_nsc on prod_count_nci60(nsc);
create index prod_count_hf_nsc on prod_count_hf(nsc);
create index prod_count_xeno_nsc on prod_count_xeno(nsc);

drop table if exists prod_bio_counts;

create table prod_bio_counts 
as 
select 
ntl.nsc, 
prod_count_nci60.the_count as prod_count_nci60, 
prod_count_hf.the_count as prod_count_hf, 
prod_count_xeno.the_count as prod_count_xeno 
from nsc_to_load ntl
left outer join prod_count_nci60 on ntl.nsc = prod_count_nci60.nsc 
left outer join prod_count_hf on ntl.nsc = prod_count_hf.nsc 
left outer join prod_count_xeno on ntl.nsc = prod_count_xeno.nsc;

insert into cmpd_bio_assay(id, nci60, hf, xeno)
select nsc, prod_count_nci60, prod_count_hf, prod_count_xeno 
from prod_bio_counts 
where nsc in (
	select nsc from nsc_to_load
); 


-- #    #    #  #    #  ######  #    #   #####   ####   #####    #   #
-- #    ##   #  #    #  #       ##   #     #    #    #  #    #    # #
-- #    # #  #  #    #  #####   # #  #     #    #    #  #    #     #
-- #    #  # #  #    #  #       #  # #     #    #    #  #####      #
-- #    #   ##   #  #   #       #   ##     #    #    #  #   #      #
-- #    #    #    ##    ######  #    #     #     ####   #    #     #

insert into cmpd_inventory(id, inventory)
select nsc, inventory 
from prod_inventory 
where nsc in (
	select nsc from nsc_to_load
); 


--   ##    #    #  #    #   ####    #####    ##     #####     #     ####   #    #
--  #  #   ##   #  ##   #  #    #     #     #  #      #       #    #    #  ##   #
-- #    #  # #  #  # #  #  #    #     #    #    #     #       #    #    #  # #  #
-- ######  #  # #  #  # #  #    #     #    ######     #       #    #    #  #  # #
-- #    #  #   ##  #   ##  #    #     #    #    #     #       #    #    #  #   ##
-- #    #  #    #  #    #   ####      #    #    #     #       #     ####   #    #



--                Table "public.cmpd_annotation"
--         Column          |          Type           | Modifiers 
---------------------------+-------------------------+-----------
-- id                      | bigint                  | not null
-- general_comment         | character varying(1024) | 
-- purity_comment          | character varying(1024) | 
-- stereochemistry_comment | character varying(1024) | not null
-- mtxt                    | character varying(1024) | 
-- pseudo_atoms            | character varying(1024) | 

drop table if exists temp;

create table temp
as
select nsc, array_to_string(array_agg(atomarray), ',') as labels
from rs3_from_plp_frags
where atomarray is not null
and nsc in (
	select nsc from nsc_to_load
)
group by nsc; 

insert into cmpd_annotation(
id,
general_comment,
purity_comment,
stereochemistry_comment,
mtxt,
pseudo_atoms
)

select

ntl.nsc,
null,
null,
null,
pm.mtxt,
t.labels

from nsc_to_load ntl
left outer join prod_mtxt pm on ntl.nsc = pm.nsc
left outer join temp t on ntl.nsc = t.nsc;

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #    #    ##    #    #   #   #           #####   ####
-- ##  ##   #  #   ##   #    # #              #    #    #
-- # ## #  #    #  # #  #     #               #    #    #
-- #    #  ######  #  # #     #               #    #    #
-- #    #  #    #  #   ##     #               #    #    #
-- #    #  #    #  #    #     #               #     ####
--
--
-- #    #    ##    #    #   #   #
-- ##  ##   #  #   ##   #    # #
-- # ## #  #    #  # #  #     #
-- #    #  ######  #  # #     #
-- #    #  #    #  #   ##     #
-- #    #  #    #  #    #     #

-- aliases, projects, sets, plates, targets

--   ##    #          #      ##     ####   ######   ####
--  #  #   #          #     #  #   #       #       #
-- #    #  #          #    #    #   ####   #####    ####
-- ######  #          #    ######       #  #            #
-- #    #  #          #    #    #  #    #  #       #    #
-- #    #  ######     #    #    #   ####   ######   ####

drop table if exists temp;

create table temp
as
select distinct chem_name_type
from prod_chem_name
where nsc in (
	select nsc from nsc_to_load
); 

insert into cmpd_alias_type(id, alias_type)
select nextval('cmpd_alias_type_seq'), chem_name_type
from temp;

-- chem_name is NOT unique because of one-to-many from name to chem_name_type 

drop table if exists temp;

create table temp
as
select 
nextval('cmpd_alias_seq') as id, 
cn.nsc, 
n.nsc as nsc_cmpds_fk, 
chem_name, 
chem_name_type, 
t.id as cmpd_alias_type_fk
from prod_chem_name cn, cmpd_alias_type t, nsc_to_load n
where cn.chem_name_type = t.alias_type
and cn.nsc = n.nsc; 

insert into cmpd_alias(id, alias, cmpd_alias_type_fk)
select id, chem_name, cmpd_alias_type_fk 
from temp;

insert into cmpd_aliases2nsc_cmpds(nsc_cmpds_fk, cmpd_aliases_fk)
select nsc_cmpds_fk, id
from temp;

-- #####   #####    ####        #  ######   ####    #####   ####
-- #    #  #    #  #    #       #  #       #    #     #    #
-- #    #  #    #  #    #       #  #####   #          #     ####
-- #####   #####   #    #       #  #       #          #         #
-- #       #   #   #    #  #    #  #       #    #     #    #    #
-- #       #    #   ####    ####   ######   ####      #     ####

drop table if exists temp;

create table temp
as
select distinct project_code, description
from prod_projects
where nsc in (
	select nsc from nsc_to_load
); 

insert into cmpd_project(id, project_code, project_name)
select nextval('cmpd_project_seq'), project_code, description
from temp;

drop table if exists temp;

create table temp
as
select pp.nsc, pp.project_code, nsc.nsc as nsc_cmpds_fk, cp.id as cmpd_projects_fk
from prod_projects pp, nsc_to_load nsc, cmpd_project cp
where pp.nsc = nsc.nsc
and pp.project_code = cp.project_code; 

insert into cmpd_projects2nsc_cmpds(nsc_cmpds_fk, cmpd_projects_fk)
select nsc_cmpds_fk, cmpd_projects_fk
from temp;

-- #####   #         ##     #####  ######   ####
-- #    #  #        #  #      #    #       #
-- #    #  #       #    #     #    #####    ####
-- #####   #       ######     #    #            #
-- #       #       #    #     #    #       #    #
-- #       ######  #    #     #    ######   ####

drop table if exists temp;

create table temp
as
select distinct plate_set
from prod_plated_sets
where plate_set is not null
and nsc in (
	select nsc from nsc_to_load
); 

insert into cmpd_plate(id, plate_name)
select nextval('cmpd_plate_seq'), plate_set
from temp;

drop table if exists temp;

create table temp
as
select pps.nsc, pps.plate_set, n.nsc as nsc_cmpds_fk, p.id as cmpd_plates_fk
from prod_plated_sets pps, nsc_to_load n, cmpd_plate p
where pps.nsc = n.nsc
and pps.plate_set = p.plate_name;

insert into cmpd_plates2nsc_cmpds(nsc_cmpds_fk, cmpd_plates_fk)
select nsc_cmpds_fk, cmpd_plates_fk
from temp;

--  #####    ##    #####    ####   ######   #####   ####
--    #     #  #   #    #  #    #  #          #    #
--    #    #    #  #    #  #       #####      #     ####
--    #    ######  #####   #  ###  #          #         #
--    #    #    #  #   #   #    #  #          #    #    #
--    #    #    #  #    #   ####   ######     #     ####

--drop table if exists temp;
--
--create table temp
--as
--select distinct target
--from prod_targets
--where target is not null
--and nsc in (
--	select nsc from nsc_to_load
--); 
--
--insert into cmpd_target(id, target)
--select nextval('target_seq'), target
--from temp;
--
--drop table if exists temp;
--
--create table temp
--as
--select pt.nsc, pt.target, n.id as nsc_cmpds_fk, p.id as cmpd_targets_fk
--from prod_targets pt, nsc_cmpd n, cmpd_target ct
--where pt.nsc = n.nsc
--and pt.target = ct.target;
--and pt.nsc in (
--	select nsc from nsc_to_load
--); 
--
--insert into cmpd_targets2nsc_cmpds(nsc_cmpds_fk, cmpd_targets_fk)
--select nsc_cmpds_fk,cmpd_targets_fk
--from temp;
























-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #####    ####   #    #  ######  #    #
-- #    #  #    #  #    #  #       ##  ##
-- #    #  #       ######  #####   # ## #
-- #####   #       #    #  #       #    #
-- #       #    #  #    #  #       #    #
-- #        ####   #    #  ######  #    #

insert into cmpd_fragment_p_chem(

id,
molecular_weight,   
molecular_formula,
log_d,             
count_hyd_bond_acceptors,
count_hyd_bond_donors,
surface_area,       
solubility,         
count_rings,        
count_atoms,        
count_bonds,        
count_single_bonds, 
count_double_bonds, 
count_triple_bonds, 
count_rotatable_bonds,
count_hydrogen_atoms,
count_metal_atoms,  
count_heavy_atoms,  
count_positive_atoms,
count_negative_atoms,
count_ring_bonds,   
count_stereo_atoms, 
count_stereo_bonds, 
count_ring_assemblies,
count_aromatic_bonds,
count_aromatic_rings,
formal_charge,      
the_a_log_p        

)

select 

fragmentindex,
molecular_weight,          
molecular_formula, 
logd,                     
num_h_acceptors,  
num_h_donors,     
molecular_surfacearea,              
molecular_solubility,                
num_rings,               
num_atoms,               
num_bonds,               
num_singlebonds,        
num_doublebonds,        
num_triplebonds,        
num_rotatablebonds,     
num_hydrogens,      
num_metalatoms,         
(num_atoms - num_hydrogens),         
num_positiveatoms,      
num_negativeatoms,      
num_ringbonds,          
num_stereoatoms,        
num_stereobonds,        
num_ringassemblies,     
num_aromaticbonds,     
num_aromaticrings,      
formalcharge,             
alogp               

from rs3_from_plp_frags
-- catching labels 
-- COULD THIS BE OBVIATED SOMEHOW?  Pre-strip lbls?
where can_smi != '[RH]' 
and nsc in (
	select nsc from nsc_to_load
);

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

--  ####    #####  #####   #    #   ####    #####  #    #  #####   ######
-- #          #    #    #  #    #  #    #     #    #    #  #    #  #
--  ####      #    #    #  #    #  #          #    #    #  #    #  #####
--      #     #    #####   #    #  #          #    #    #  #####   #
-- #    #     #    #   #   #    #  #    #     #    #    #  #   #   #
--  ####      #    #    #   ####    ####      #     ####   #    #  ######

insert into cmpd_fragment_structure (

id,
inchi,
inchi_aux,
ctab,
can_smi,
can_taut,
can_taut_strip_stereo

)

select 

fragmentindex,
inchi, 
inchi_auxinfo, 
null,
can_smi,
can_taut,
can_taut_strip_stereo
 
from rs3_from_plp_frags 
where can_smi != '[RH]'
and nsc in (
	select nsc from nsc_to_load
);

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

--  ####   #    #  #####   #####           ######  #####     ##     ####
-- #    #  ##  ##  #    #  #    #          #       #    #   #  #   #    #
-- #       # ## #  #    #  #    #          #####   #    #  #    #  #
-- #       #    #  #####   #    #          #       #####   ######  #  ###
-- #    #  #    #  #       #    #          #       #   #   #    #  #    #
--  ####   #    #  #       #####           #       #    #  #    #   ####

insert into cmpd_fragment(

id,
stoichiometry,
cmpd_fragment_structure_fk,
cmpd_known_salt_fk,
cmpd_fragment_p_chem_fk,
nsc_cmpd_fk

)

select

fragmentindex,
1.0,
fragmentindex,
null, --cmpd_known_salt_fk,!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
fragmentindex,
nsc

from rs3_from_plp_frags 
where can_smi != '[RH]'
and nsc in (
	select nsc from nsc_to_load
);

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- bioassay
-- target
-- alias
-- plate
-- project
-- inventory
-- bio_assay
-- pub_chem
-- set
-- annotation
-- prod_legacy_cmpd

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #####     ##    #####   ######  #    #   #####
-- #    #   #  #   #    #  #       ##   #     #
-- #    #  #    #  #    #  #####   # #  #     #
-- #####   ######  #####   #       #  # #     #
-- #       #    #  #   #   #       #   ##     #
-- #       #    #  #    #  ######  #    #     #

-- parent_fragment

drop table if exists temp_parent_fragment;

create table temp_parent_fragment
as
select * from rs3_from_plp_frags
where nsc in (
    select nsc from nsc_to_load
)
and (nsc, fragmentindex) in  
(
    select nsc, fragmentindex from one_strc
    union
    select nsc, fragmentindex from two_strc_any_lbl_one_salt where salt_smiles is null and can_smi != '[RH]'
    union
    select nsc, fragmentindex from one_strc_one_lbl where can_smi != '[RH]'
    union 
    select nsc, fragmentindex from one_strc_multi_lbl where can_smi != '[RH]'
);

create index tpf_nsc on temp_parent_fragment(nsc);


--    #    #####   ######  #    #   #####           ####    #####  #####
--    #    #    #  #       ##   #     #            #          #    #    #
--    #    #    #  #####   # #  #     #             ####      #    #    #
--    #    #    #  #       #  # #     #                 #     #    #####
--    #    #    #  #       #   ##     #            #    #     #    #   #
--    #    #####   ######  #    #     #   #######   ####      #    #    #


-- #####   ######   ####    ####   #####            ####    #####  #####
-- #    #  #       #       #    #  #    #          #          #    #    #
-- #    #  #####    ####   #       #    #           ####      #    #    #
-- #    #  #            #  #       #####                #     #    #####
-- #    #  #       #    #  #    #  #   #           #    #     #    #   #
-- #####   ######   ####    ####   #    # #######   ####      #    #    #

drop table if exists temp_ident_descr;

create table temp_ident_descr(
nsc int,
identifier_string varchar(1024),
descriptor_string varchar(1024)
);

insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' one_strc' , 'S'||uni.nsc||' one_strc'
from(
    select distinct nsc from one_strc where nsc in (select nsc from nsc_to_load)
) as uni;

insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' two_strc_any_lbl_one_salt' , 'S'||uni.nsc||' two_strc_any_lbl_one_salt'
from(
    select distinct nsc from two_strc_any_lbl_one_salt where nsc in (select nsc from nsc_to_load)
) as uni;

insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' one_strc_one_lbl' , 'S'||uni.nsc||' one_strc_one_lbl'
from(
    select distinct nsc from one_strc_one_lbl where nsc in (select nsc from nsc_to_load)
) as uni;

insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' one_strc_multi_lbl' , 'S'||uni.nsc||' one_strc_multi_lbl'
from(
    select distinct nsc from one_strc_multi_lbl where nsc in (select nsc from nsc_to_load)
) as uni;

insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' one_lbl' , 'S'||uni.nsc||' one_lbl'
from(
    select distinct nsc from one_lbl where nsc in (select nsc from nsc_to_load)
) as uni;

insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' multi_lbl' , 'S'||uni.nsc||' multi_lbl'
from(
    select distinct nsc from multi_lbl where nsc in (select nsc from nsc_to_load)
) as uni;

insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' multi_strc_any_lbl' , 'S'||uni.nsc||' multi_strc_any_lbl'
from(
    select distinct nsc from multi_strc_any_lbl where nsc in (select nsc from nsc_to_load)
) as uni;

insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' two_strc_any_lbl_two_salt' , 'S'||uni.nsc||' two_strc_any_lbl_two_salt'
from(
    select distinct nsc from two_strc_any_lbl_two_salt where nsc in (select nsc from nsc_to_load)
) as uni;

insert into temp_ident_descr
select uni.nsc, 'S'||uni.nsc||' two_strc_any_lbl' , 'S'||uni.nsc||' two_strc_any_lbl'
from(
    select distinct nsc from two_strc_any_lbl where nsc in (select nsc from nsc_to_load)
) as uni;

create index tid_nsc on temp_ident_descr(nsc);




-- #    #   ####    ####             ##     ####    ####
-- ##   #  #       #    #           #  #   #    #  #    #
-- # #  #   ####   #               #    #  #       #
-- #  # #       #  #               ######  #  ###  #  ###
-- #   ##  #    #  #    #          #    #  #    #  #    #
-- #    #   ####    ####           #    #   ####    ####

drop table if exists temp_nsc_agg;

create table temp_nsc_agg
as
select
nsc,
count(molecular_weight) as count_fragments,
sum(molecular_weight) as molecular_weight,
array_to_string(array_agg(molecular_formula), '.') as molecular_formula
from 
rs3_from_plp_frags
where can_smi != '[RH]'
and nsc in (
    select nsc from nsc_to_load    
)
group by nsc;

create index tna_nsc on temp_nsc_agg(nsc);

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

-- #    #   ####    ####            ####   #    #  #####   #####
-- ##   #  #       #    #          #    #  ##  ##  #    #  #    #
-- # #  #   ####   #               #       # ## #  #    #  #    #
-- #  # #       #  #               #       #    #  #####   #    #
-- #   ##  #    #  #    #          #    #  #    #  #       #    #
-- #    #   ####    ####  #######   ####   #    #  #       #####

insert into nsc_cmpd(

id,
name,
nsc_cmpd_id,
prefix,
nsc,
conf,
distribution,
cas,
count_fragments,
discreet,
identifier_string,
descriptor_string,
molecular_weight,
molecular_formula,
nsc_cmpd_type_fk,
cmpd_parent_fragment_fk,
cmpd_bio_assay_fk,
cmpd_inventory_fk,
cmpd_annotation_fk,
prod_legacy_cmpd_fk
)

select 

n.nsc,
n.name,
n.nsc,
'S',
n.nsc,
'fake', --n.conf,!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
'fake', --n.distribution,!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
n.cas,
tna.count_fragments,
'fake', --n.discreet,!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
tid.identifier_string, --identifier_string,
tid.descriptor_string, --descriptor_string,
tna.molecular_weight,
tna.molecular_formula,
1, --nsc_cmpd_type_fk,
tpf.fragmentindex, --cmpd_parent_fragment_fk
cba.id, --cmpd_bio_assay_fk,
ci.id, --cmpd_inventory_fk,
annot.id, --cmpd_annotation_fk,
null --prod_legacy_cmpd_fk

from rs3_from_plp_nsc n

left outer join temp_ident_descr tid on n.nsc = tid.nsc
left outer join temp_nsc_agg tna on n.nsc = tna.nsc
left outer join temp_parent_fragment tpf on n.nsc = tpf.nsc
left outer join cmpd_bio_assay cba on n.nsc = cba.id
left outer join cmpd_inventory ci on n.nsc = ci.id
left outer join cmpd_annotation annot on n.nsc = annot.id

where n.nsc in (
	select nsc from nsc_to_load
);

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

--  ####   #    #  #####   #####
-- #    #  ##  ##  #    #  #    #
-- #       # ## #  #    #  #    #
-- #       #    #  #####   #    #
-- #    #  #    #  #       #    #
--  ####   #    #  #       #####

insert into cmpd(id) select nsc from nsc_to_load;

-- vacuum analyze verbose;

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

--  ####   #####   ######    ##     #####           ####    ####    #####  #####
-- #    #  #    #  #        #  #      #            #    #  #          #    #    #
-- #       #    #  #####   #    #     #            #        ####      #    #    #
-- #       #####   #       ######     #            #            #     #    #####
-- #    #  #   #   #       #    #     #            #    #  #    #     #    #   #
--  ####   #    #  ######  #    #     #             ####    ####      #    #    #


-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------



-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

--  ####   #    #  #####   #####            #####    ##    #####   #       ######
-- #    #  ##  ##  #    #  #    #             #     #  #   #    #  #       #
-- #       # ## #  #    #  #    #             #    #    #  #####   #       #####
-- #       #    #  #####   #    #             #    ######  #    #  #       #
-- #    #  #    #  #       #    #             #    #    #  #    #  #       #
--  ####   #    #  #       #####  #######     #    #    #  #####   ######  ######

-- temp_formatted_strings
-- aliases, plates, projects, sets, targets

drop table if exists temp_formatted_strings;

create table temp_formatted_strings
as
select
nsc.id, 
nsc.nsc,
string_agg(alia.alias,'xxx') as formatted_aliases_string,
--formatted_plates includes distinct because data are degenerate by set (not tracking plate row/column in dctddb)
string_agg(distinct plat.plate_name,'xxx') as formatted_plates_string,
string_agg(distinct proj.project_code,'xxx') as formatted_projects_string,
string_agg(cset.set_name,'xxx') as formatted_sets_string,
string_agg(targ.target,'xxx') as formatted_targets_string
from
nsc_cmpd nsc
left outer join cmpd_aliases2nsc_cmpds alia2c on (alia2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_alias alia on (alia2c.cmpd_aliases_fk = alia.id)
left outer join cmpd_plates2nsc_cmpds plat2c on (plat2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_plate plat on (plat2c.cmpd_plates_fk = plat.id)
left outer join cmpd_projects2nsc_cmpds proj2c on (proj2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_project proj on (proj2c.cmpd_projects_fk = proj.id)
left outer join cmpd_sets2nsc_cmpds sets2c on (sets2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_set cset on (sets2c.cmpd_sets_fk = cset.id)
left outer join cmpd_targets2nsc_cmpds targ2c on (targ2c.nsc_cmpds_fk = nsc.id) left outer join cmpd_target targ on (targ2c.cmpd_targets_fk = targ.id)
group by 
nsc.id, 
nsc.nsc;

create index tfs_nsc on temp_formatted_strings(nsc);

-- temp_formatted_fragments

drop table if exists temp_formatted_fragments;

create table temp_formatted_fragments
as
select
nsc.id, 
nsc.nsc,
string_agg(strc.can_smi,'xxx') as formatted_fragments_string
from
nsc_cmpd nsc, cmpd_fragment frag, cmpd_fragment_structure strc
where nsc.id = frag.nsc_cmpd_fk
and frag.cmpd_fragment_structure_fk = strc.id
and nsc.nsc in (
    select nsc from nsc_to_load
)
group by 
nsc.id, 
nsc.nsc;

create index tff_nsc on temp_formatted_fragments(nsc);


































insert into cmpd_table(

id,
prefix,
nsc,
cas,
name,
discreet,
conf,
distribution,
nsc_cmpd_id,
ad_hoc_cmpd_id,
original_ad_hoc_cmpd_id,
inventory,
nci60,
hf,
xeno,
formatted_targets_string,
formatted_sets_string,
formatted_projects_string,
formatted_plates_string,
formatted_aliases_string,
formatted_fragments_string,
pseudo_atoms,
salt_smiles,
salt_name,
salt_mf,
salt_mw,
parent_stoichiometry,
salt_stoichiometry,
inchi,
inchi_aux,
ctab,
can_smi,
can_taut,
can_taut_strip_stereo,
molecular_weight,
molecular_formula,
log_d,
count_hyd_bond_acceptors,
count_hyd_bond_donors,
surface_area,
solubility,
count_rings,
count_atoms,
count_bonds,
count_single_bonds,
count_double_bonds,
count_triple_bonds,
count_rotatable_bonds,
count_hydrogen_atoms,
count_metal_atoms,
count_heavy_atoms,
count_positive_atoms,
count_negative_atoms,
count_ring_bonds,
count_stereo_atoms,
count_stereo_bonds,
count_ring_assemblies,
count_aromatic_bonds,
count_aromatic_rings,
formal_charge,
the_a_log_p,
mtxt,
general_comment,
purity_comment,
stereochemistry_comment,
identifier_string,
descriptor_string,
molecular_weight,
molecular_formula,
nsc_cmpd_type

)

select

nsc.id,
nsc.prefix,
nsc.nsc,
nsc.cas,
nsc.name,
nsc.discreet,
nsc.conf,
nsc.distribution,
nsc.nsc_cmpd_id,
null, --ad_hoc_cmpd_id,
null, --original_ad_hoc_cmpd_id,

-- inventory

invent.inventory,

-- bio_assay

bioass.nci60,
bioass.hf,
bioass.xeno,

-- from temp_formatted_strings

tfs.formatted_targets_string,
tfs.formatted_sets_string,
tfs.formatted_projects_string,
tfs.formatted_plates_string,
tfs.formatted_aliases_string,

-- temp_formatted_fragments

tff.formatted_fragments_string,

pseudo_atoms,

null, --salt_smiles,
null, --salt_name,
null, --salt_mf,
null, --salt_mw,
1, --parent_stoichiometry,
1, --salt_stoichiometry,

-- structure -- should be PARENT only

strc.inchi,
strc.inchi_aux,
strc.ctab,
strc.can_smi,
strc.can_taut,
strc.can_taut_strip_stereo,

-- pchem -- should be PARENT only

pchem.molecular_weight,
pchem.molecular_formula,
pchem.log_d,
pchem.count_hyd_bond_acceptors,
pchem.count_hyd_bond_donors,
pchem.surface_area,
pchem.solubility,
pchem.count_rings,
pchem.count_atoms,
pchem.count_bonds,
pchem.count_single_bonds,
pchem.count_double_bonds,
pchem.count_triple_bonds,
pchem.count_rotatable_bonds,
pchem.count_hydrogen_atoms,
pchem.count_metal_atoms,
pchem.count_heavy_atoms,
pchem.count_positive_atoms,
pchem.count_negative_atoms,
pchem.count_ring_bonds,
pchem.count_stereo_atoms,
pchem.count_stereo_bonds,
pchem.count_ring_assemblies,
pchem.count_aromatic_bonds,
pchem.count_aromatic_rings,
pchem.formal_charge,
pchem.the_a_log_p,

-- annotation

annot.mtxt,
annot.general_comment,
annot.purity_comment,
annot.stereochemistry_comment,

nsc.identifier_string,
nsc.descriptor_string,

molecular_weight,
molecular_formula,

typ.nsc_cmpd_type

from 

nsc_cmpd nsc

-- need intermediate join based on cmpd_parent_fragment_fk!

left outer join cmpd_fragment frag on nsc.cmpd_parent_fragment_fk = frag.id
    left outer join cmpd_fragment_p_chem pchem on frag.cmpd_fragment_p_chem_fk = pchem.id
    left outer join cmpd_fragment_structure strc on frag.cmpd_fragment_structure_fk = strc.id
left outer join cmpd_annotation annot on nsc.cmpd_annotation_fk = annot.id
left outer join cmpd_inventory invent on nsc.cmpd_inventory_fk = invent.id
left outer join cmpd_bio_assay bioass on nsc.cmpd_bio_assay_fk = bioass.id

-- from temp tables

left outer join temp_formatted_strings tfs on nsc.nsc = tfs.nsc
left outer join temp_formatted_fragments tff on nsc.nsc = tff.nsc
left outer join temp_ident_descr temp_ident_descr on nsc.nsc = temp_ident_descr.nsc

left outer join nsc_cmpd_type typ on nsc.nsc_cmpd_type_fk = typ.id

where nsc.nsc in (
	select nsc from nsc_to_load
); 


--test
----743380 OCc1cn(Cc2cc(Cl)ccc2)c2c1cccc2

----TESTOSTERONE TESTS
--
-- select nsc_cmpd.id, nsc_cmpd.nsc, strc.mol
-- from cmpd_fragment_structure strc, nsc_cmpd
-- where strc.id = nsc_cmpd.id
-- and strc.mol @= (
-- select mol from cmpd_fragment_structure where id in (
-- select id from nsc_cmpd where nsc = 50917
-- )
-- );
--
--   id   |   nsc   |                             mol                              
----------+---------+--------------------------------------------------------------
-- 576217 |  755838 | C[C@]12CCC3C(CCC4=CC(=O)CC[C@@]43C)C1CC[C@@H]2O
-- 603557 |   50917 | C[C@@]12CC[C@@H]3[C@H](CCC4=CC(=O)CC[C@]43C)[C@H]1CC[C@H]2O
-- 273801 |   26499 | C[C@]12CC[C@H]3[C@@H](CCC4=CC(=O)CC[C@@]43C)[C@@H]1CC[C@H]2O
-- 459296 | 2509410 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
--  98449 |  523833 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
--  34297 |    9700 | CC12CCC3C(CCC4=CC(=O)CCC43C)C1CCC2O
--(6 rows)
--
-- any nsc
--
-- select nsc_cmpd.id, nsc_cmpd.nsc, strc.mol 
-- from cmpd_fragment_structure strc, nsc_cmpd
-- where strc.id = nsc_cmpd.id
-- and strc.mol @> (
-- select mol from cmpd_fragment_structure where id in (
-- select id from nsc_cmpd where nsc = 743380
-- )
-- );
--
--   id   |  nsc   |                   mol                    
----------+--------+------------------------------------------
-- 372100 | 372811 | O=C(O)c1c(Cl)n(Cc2cc(Cl)ccc2Cl)c2ccccc21
-- 415455 | 743380 | OCc1cn(Cc2cccc(Cl)c2)c2ccccc12
-- 433386 | 745858 | O=C(O)c1cn(Cc2ccc(Cl)c(Cl)c2)c2ccccc12
-- 444078 | 756864 | OCc1c(Cl)n(Cc2cccc(Cl)c2)c2ccccc12
-- 444218 | 751172 | O=C(O)c1cn(Cc2cccc(Cl)c2)c2ccccc12
-- 450764 | 756850 | Cc1c(CO)c2ccccc2n1Cc1cccc(Cl)c1
--(6 rows)
-