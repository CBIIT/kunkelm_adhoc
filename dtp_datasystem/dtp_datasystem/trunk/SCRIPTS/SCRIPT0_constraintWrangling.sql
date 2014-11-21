-- #####   #####    ####   #####
-- #    #  #    #  #    #  #    #
-- #    #  #    #  #    #  #    #
-- #    #  #####   #    #  #####
-- #    #  #   #   #    #  #
-- #####   #    #   ####   #

--http://blog.hagander.net/archives/131-Automatically-dropping-and-creating-constraints.html

--SELECT 'ALTER TABLE '||nspname||'&quot;.&quot;'||relname||'&quot; DROP CONSTRAINT &quot;'||conname||'&quot;;'

-- DO NOT DROP pkey CONSTRAINTS!
-- SELECT 'ALTER TABLE '||nspname||'.'||relname||' DROP CONSTRAINT '||conname||' cascade;' as statement 

drop table if exists drop_constraint_statements;

create table drop_constraint_statements 
as
SELECT 'ALTER TABLE '||nspname||'.'||relname||' DROP CONSTRAINT '||conname||' cascade;' as statement 
 FROM pg_constraint 
 INNER JOIN pg_class ON conrelid = pg_class.oid 
 INNER JOIN pg_namespace ON pg_namespace.oid = pg_class.relnamespace
 WHERE conname not like '%pkey'
 AND relname in (
'ad_hoc_cmpd',
'ad_hoc_cmpd_fragment',
'ad_hoc_cmpd_fragment_p_chem',
'ad_hoc_cmpd_fragment_structure',
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
'cmpd_legacy_cmpd',
'cmpd_list',
'cmpd_list_member',
'cmpd_lists2share_with_users',
'cmpd_named_set',
'cmpd_named_sets2nsc_cmpds',
'cmpd_plate',
'cmpd_plates2nsc_cmpds',
'cmpd_project',
'cmpd_projects2nsc_cmpds',
'cmpd_pub_chem_sid',
'cmpd_pub_chem_sids2nsc_cmpds',
'cmpd_related',
'cmpd_relation_type',
'cmpd_table',
'cmpd_target',
'cmpd_targets2nsc_cmpds',
'data_system_role',
'data_system_roles2data_system_',
'data_system_user',
'nsc_cmpd',
'nsc_cmpd_type',
'rdkit_mol'
)
 ORDER BY CASE WHEN contype = 'f' THEN 0 ELSE 1 END, contype, nspname, relname, conname;

\copy drop_constraint_statements to /tmp/drop_constraint_statements.csv csv header

--  ####   #####   ######    ##     #####  ######
-- #    #  #    #  #        #  #      #    #
-- #       #    #  #####   #    #     #    #####
-- #       #####   #       ######     #    #
-- #    #  #   #   #       #    #     #    #
--  ####   #    #  ######  #    #     #    ######

--SELECT 'ALTER TABLE &quot;'||nspname||'&quot;.&quot;'||relname||'&quot; ADD CONSTRAINT &quot;'||conname||'&quot; '|| 

drop table if exists create_constraint_statements;

create table create_constraint_statements
as
SELECT 'ALTER TABLE '||nspname||'.'||relname||' ADD CONSTRAINT '||conname||' '||pg_get_constraintdef(pg_constraint.oid)||';' as statement
 FROM pg_constraint
 INNER JOIN pg_class ON conrelid = pg_class.oid
 INNER JOIN pg_namespace ON pg_namespace.oid = pg_class.relnamespace
 WHERE conname not like '%pkey'
 AND relname in (
'ad_hoc_cmpd',
'ad_hoc_cmpd_fragment',
'ad_hoc_cmpd_fragment_p_chem',
'ad_hoc_cmpd_fragment_structure',
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
'cmpd_legacy_cmpd',
'cmpd_list',
'cmpd_list_member',
'cmpd_lists2share_with_users',
'cmpd_named_set',
'cmpd_named_sets2nsc_cmpds',
'cmpd_plate',
'cmpd_plates2nsc_cmpds',
'cmpd_project',
'cmpd_projects2nsc_cmpds',
'cmpd_pub_chem_sid',
'cmpd_pub_chem_sids2nsc_cmpds',
'cmpd_related',
'cmpd_relation_type',
'cmpd_table',
'cmpd_target',
'cmpd_targets2nsc_cmpds',
'data_system_role',
'data_system_roles2data_system_',
'data_system_user',
'nsc_cmpd',
'nsc_cmpd_type',
'rdkit_mol'
) 
 ORDER BY CASE WHEN contype = 'f' THEN 0 ELSE 1 END DESC, contype DESC, nspname DESC, relname DESC, conname DESC;

\copy create_constraint_statements to /tmp/create_constraint_statements.csv csv header