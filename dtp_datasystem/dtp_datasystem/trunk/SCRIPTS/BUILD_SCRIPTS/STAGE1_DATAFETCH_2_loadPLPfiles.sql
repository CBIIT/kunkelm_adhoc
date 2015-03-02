\c datasystemdb

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

\copy rs3_from_plp_frags from /home/mwkunkel/rs3_from_plp_400k_frags.csv csv header null as ''
\copy rs3_from_plp_frags from /home/mwkunkel/rs3_from_plp_remainder_frags.csv csv header null as ''

\copy rs3_from_plp_nsc from /home/mwkunkel/rs3_from_plp_400k_nsc.csv csv header null as ''
\copy rs3_from_plp_nsc from /home/mwkunkel/rs3_from_plp_remainder_nsc.csv csv header null as ''

\copy rs3_from_plp_ctab from /home/mwkunkel/rs3_from_plp_400k_ctab.csv csv header null as ''
\copy rs3_from_plp_ctab from /home/mwkunkel/rs3_from_plp_remainder_ctab.csv csv header null as ''

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
create index rfpf_can_smi on rs3_from_plp_frags(can_smi);
create index rfpf_can_taut on rs3_from_plp_frags(can_taut);
create index rfpf_can_taut_strip_stereo on rs3_from_plp_frags(can_taut_strip_stereo);
create index rfpf_atomarray on rs3_from_plp_frags(atomarray);

create index rfpn_nsc on rs3_from_plp_nsc(nsc);

create index rfpc_nsc on rs3_from_plp_ctab(nsc);
create index rfpc_fragmentindex on rs3_from_plp_ctab(fragmentindex);

vacuum analyze rs3_from_plp_frags;
vacuum analyze rs3_from_plp_nsc;
vacuum analyze rs3_from_plp_ctab;

vacuum analyze rs3_from_plp_ctab;

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

alter table rs3_from_plp_frags add salt_smiles varchar(1024);

alter table rs3_from_plp_frags add salt_id bigint;

update rs3_from_plp_frags
set salt_smiles = cmpd_known_salt.can_taut_strip_stereo, salt_id = cmpd_known_salt.id
from cmpd_known_salt
where rs3_from_plp_frags.can_taut_strip_stereo = cmpd_known_salt.can_taut_strip_stereo;

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
