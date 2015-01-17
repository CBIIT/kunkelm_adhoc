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

\copy rs3_from_plp_frags from /home/mwkunkel/rs3_from_plp_400k_frags.txt csv header delimiter as E'\t' null as ''
\copy rs3_from_plp_frags from /home/mwkunkel/rs3_from_plp_remainder_frags.txt csv header delimiter as E'\t' null as ''

\copy rs3_from_plp_nsc from /home/mwkunkel/rs3_from_plp_400k_nsc.txt csv header delimiter as E'\t'
\copy rs3_from_plp_nsc from /home/mwkunkel/rs3_from_plp_remainder_nsc.txt csv header delimiter as E'\t'

\copy rs3_from_plp_ctab from /home/mwkunkel/rs3_from_plp_400k_ctab.txt csv header delimiter as E'\t' null as ''
\copy rs3_from_plp_ctab from /home/mwkunkel/rs3_from_plp_remainder_ctab.txt csv header delimiter as E'\t' null as ''

-- make sure that there isn't any overlap of fragmentindex in the two files

select min(fragmentindex), max(fragmentindex)  
from rs3_from_plp_frags 
where nsc <= 400000;

select min(fragmentindex), max(fragmentindex)  
from rs3_from_plp_frags 
where nsc >= 400000;

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

create index rfps_nsc on rs3_from_plp_frags(nsc);

create index rfps_fragmentindex on rs3_from_plp_frags(fragmentindex);

create index rfps_can_smi on rs3_from_plp_frags(can_smi);

create index rfps_can_taut on rs3_from_plp_frags(can_taut);

create index rfps_can_taut_strip_stereo on rs3_from_plp_frags(can_taut_strip_stereo);

create index rfps_atomarray on rs3_from_plp_frags(atomarray);

create index rfpc_nsc on rs3_from_plp_ctab(nsc);

create index rfpc_fragmentindex on rs3_from_plp_ctab(fragmentindex);

create index rfpn_nsc on rs3_from_plp_nsc(nsc);

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

alter table rs3_from_plp_frags add salt_smiles varchar(1024);

alter table rs3_from_plp_frags add salt_id bigint;

update rs3_from_plp_frags
set salt_smiles = cmpd_known_salt.can_taut_strip_stereo, salt_id = cmpd_known_salt.id
from cmpd_known_salt
where rs3_from_plp_frags.can_taut_strip_stereo = cmpd_known_salt.can_taut_strip_stereo;

-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------
-- ---------------------------------------------------------

