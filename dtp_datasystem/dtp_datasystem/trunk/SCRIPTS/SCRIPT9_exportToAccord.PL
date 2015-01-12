--#!/usr/bin/perl
--
--use strict;
--
--# load module
--use DBI;
--
--(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
--my $year = 1900 + $yearOffset;
--my $theTime = "$hour:$minute:$second";
--print "start time $theTime\n";
--
--# connect
--my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});
--
  ####    ####           #####    #   #          #    #   ####    ####
 #    #  #    #          #    #    # #           ##   #  #       #    #
 #    #  #               #####      #            # #  #   ####   #
 #  # #  #               #    #     #            #  # #       #  #
 #   #   #    #          #    #     #            #   ##  #    #  #    #
  ### #   ####  #######  #####      #   #######  #    #   ####    ####

drop table if exists mwk_hts_salt;

create table mwk_hts_salt 
as
select id as salt_code_number, salt_name, salt_name as salt_description, salt_mw as salt_weight, salt_mf as salt_formula, 1::int as multiplier
from cmpd_known_salt;

drop table if exists temp;

create table one_strc_mw_diff_0 
as 
select distinct nsc
from qc_with_nsc
where (
source='one_strc' and comparator='count nsc' and target='mw DIFF 0'
);

drop table if exists two_strc_one_salt_mw_diff_0;

create table two_strc_one_salt_mw_diff_0 
as 
select distinct nsc
from qc_with_nsc
where (
    source='two_strc_any_lbl_one_salt' and comparator='count nsc' and target='mw DIFF 0'
);

drop table if exists mwk_hts_compound;

create table mwk_hts_compound
as
select nsc as compound_id, 'NSC' || nsc as description from one_strc_mw_diff_0
union
select nsc as compound_id, 'NSC' || nsc as description from two_strc_one_salt_mw_diff_0;

drop table if exists combo

create table combo
as
select nsc, salt_id
from rs3_from_plp_frags
where salt_id is null
and nsc in (
select nsc from one_strc_mw_diff_0
);

update combo
set salt_id = 1
where salt_id is null;

insert into combo
select nsc, salt_id
from rs3_from_plp_frags
where salt_id is not null
and nsc in (
select nsc from two_strc_one_salt_mw_diff_0
);

drop table if exists mwk_hts_compound_lot;

create table mwk_hts_compound_lot
as
select 1 as lot_id, nsc as compound_id, 1 as sample_id, salt_id as salt_code
from two_strc_one_salt_mw_diff_0;

drop table if exists mwk_rs3_structure_object;

create table mwk_rs3_structure_object
as
select nsc as structure_id, 'M' as object_type, ctab as object_contents
from cmpd_table
where nsc in (
    select nsc from combo
);


drop table if exists mwk_rs3_structure;

create table mwk_rs3_structure
as
select nsc as structure_id, '24-Nov-2014' as registration_date, '24-Nov-2014' as process_date, 'fake' as class_type
from combo;

\copy mwk_hts_salt to /tmp/mwk_hts_salt.tsv csv header delimiter as E'\t';
\copy mwk_hts_compound to /tmp/mwk_hts_compound.tsv csv header delimiter as E'\t';
\copy mwk_hts_compound_lot to /tmp/mwk_hts_compound_lot.tsv csv header delimiter as E'\t';
\copy mwk_rs3_structure to /tmp/mwk_rs3_structure.tsv csv header delimiter as E'\t';
\copy mwk_rs3_structure_object to /tmp/mwk_rs3_structure_object.tsv csv header delimiter as E'\t';


--# clean up
--$dbh->disconnect();
--
--(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
--my $year = 1900 + $yearOffset;
--my $theTime = "$hour:$minute:$second";
--print "finish time $theTime\n";
