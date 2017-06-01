\set ON_ERROR_FAIL on

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

\copy rs3_from_plp_first_round_tautomer_failures from /home/mwkunkel/rs3_from_plp_first_round_tautomer_failures.csv csv header delimiter as ',' null as ''
\copy rs3_from_plp_frags from /home/mwkunkel/rs3_from_plp_frags.csv csv header delimiter as ',' null as ''
\copy rs3_from_plp_nsc from /home/mwkunkel/rs3_from_plp_nsc.csv csv header delimiter as ',' null as ''
\copy rs3_from_plp_strc_parse_fail from /home/mwkunkel/rs3_from_plp_strc_parse_fail.tsv csv header delimiter as E'\t' null as ''
\copy rs3_from_plp_frags_ctab from /home/mwkunkel/rs3_from_plp_frags_ctab.tsv csv header delimiter as E'\t' null as ''
\copy rs3_from_plp_validate_structure from /home/mwkunkel/rs3_from_plp_validate_structure.tsv csv header delimiter as E'\t' null as ''

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

--    #    #    #  #####   ######  #    #  ######   ####
--    #    ##   #  #    #  #        #  #   #       #
--    #    # #  #  #    #  #####     ##    #####    ####
--    #    #  # #  #    #  #         ##    #            #
--    #    #   ##  #    #  #        #  #   #       #    #
--    #    #    #  #####   ######  #    #  ######   ####

create index rfpf_nsc on rs3_from_plp_frags(nsc);
create index rfpf_fragmentindex on rs3_from_plp_frags(fragmentindex);
create index rfpf_nsc_fragmentindex on rs3_from_plp_frags(nsc,fragmentindex);
create index rfpf_can_smi on rs3_from_plp_frags(can_smi);
create index rfpf_can_taut on rs3_from_plp_frags(can_taut);
create index rfpf_can_taut_strip_stereo on rs3_from_plp_frags(can_taut_strip_stereo);
create index rfpf_atomarray on rs3_from_plp_frags(atomarray);
create index rfpf_salts on rs3_from_plp_frags(salts);
create index rfpf_saltsmiles on rs3_from_plp_frags(saltsmiles);
create index rfpf_solvents on rs3_from_plp_frags(solvents);
create index rfpf_solventsmiles on rs3_from_plp_frags(solventsmiles);

create index rfpfc_nsc on rs3_from_plp_frags_ctab(nsc);
create index rfpfc_fragmentindex on rs3_from_plp_frags_ctab(fragmentindex);
create index rfpfc__nsc_fragmentindex on rs3_from_plp_frags_ctab(nsc,fragmentindex);

create index rfpn_nsc on rs3_from_plp_nsc(nsc);

vacuum analyze rs3_from_plp_frags;
vacuum analyze rs3_from_plp_frags_ctab;
vacuum analyze rs3_from_plp_nsc;

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
-- ######  #####     ##     ####
-- #       #    #   #  #   #    #
-- #####   #    #  #    #  #
-- #       #####   ######  #  ###
-- #       #   #   #    #  #    #
-- #       #    #  #    #   ####
--
--
--  ####     ##    #        #####   ####
-- #        #  #   #          #    #
--  ####   #    #  #          #     ####
--      #  ######  #          #         #
-- #    #  #    #  #          #    #    #
--  ####   #    #  ######     #     ####

alter table rs3_from_plp_frags drop if exists salt_smiles;
alter table rs3_from_plp_frags drop if exists salt_id;

alter table rs3_from_plp_frags add salt_smiles varchar(1024);
alter table rs3_from_plp_frags add salt_id bigint;

update rs3_from_plp_frags
set salt_smiles = cmpd_known_salt.can_smi, salt_id = cmpd_known_salt.id
from cmpd_known_salt
--where rs3_from_plp_frags.saltsmiles = cmpd_known_salt.can_smi;
where rs3_from_plp_frags.can_taut = cmpd_known_salt.can_taut;

-- #    #  #####   #####     ##     #####  ######
-- #    #  #    #  #    #   #  #      #    #
-- #    #  #    #  #    #  #    #     #    #####
-- #    #  #####   #    #  ######     #    #
-- #    #  #       #    #  #    #     #    #
--  ####   #       #####   #    #     #    ######
--
--
-- #    #  #    #   ####   #    #  #    #
-- #   #   ##   #  #    #  #    #  ##   #
-- ####    # #  #  #    #  #    #  # #  #
-- #  #    #  # #  #    #  # ## #  #  # #
-- #   #   #   ##  #    #  ##  ##  #   ##
-- #    #  #    #   ####   #    #  #    #
--
--
--  ####     ##    #        #####   ####
-- #        #  #   #          #    #
--  ####   #    #  #          #     ####
--      #  ######  #          #         #
-- #    #  #    #  #          #    #    #
--  ####   #    #  ######     #     ####
--
--
-- #    #     #     #####  #    #
-- #    #     #       #    #    #
-- #    #     #       #    ######
-- # ## #     #       #    #    #
-- ##  ##     #       #    #    #
-- #    #     #       #    #    #
--
--
--  ####    ####   #    #  #    #   #####   ####
-- #    #  #    #  #    #  ##   #     #    #
-- #       #    #  #    #  # #  #     #     ####
-- #       #    #  #    #  #  # #     #         #
-- #    #  #    #  #    #  #   ##     #    #    #
--  ####    ####    ####   #    #     #     ####


drop table if exists salt_with_count;

create table salt_with_count
as
select cks.id, count(*) as the_count
from cmpd_known_salt cks, rs3_from_plp_frags frag
where cks.id = frag.salt_id
group by cks.id;

update cmpd_known_salt cks
set count_occurences = swc.the_count
from salt_with_count swc
where cks.id = swc.id;

update cmpd_known_salt
set count_occurences = 0
where count_occurences is null;
