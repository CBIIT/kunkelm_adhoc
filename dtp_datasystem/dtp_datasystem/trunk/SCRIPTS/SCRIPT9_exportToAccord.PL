#!/usr/bin/perl

use strict;

# load module
use DBI;

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "start time $theTime\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

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

create table temp 
as 
select distinct nsc
from qc_with_nsc
where (
source='one_strc' and comparator='count nsc' and target='mw DIFF 0'
);

drop table if exists temp2;

create table temp2 
as 
select distinct nsc
from qc_with_nsc
where (
    source='two_strc_any_lbl_one_salt' and comparator='count nsc' and target='mw DIFF 0'
);

drop table if exists mwk_hts_compound;

create table mwk_hts_compound
as
select nsc as compound_id, 'NSC' || nsc as description from temp
union
select nsc as compound_id, 'NSC' || nsc as description from temp2;

drop table if exists temp2;

create table temp2
as
select distinct nsc, salt_id
from rs3_from_plp_frags
where nsc in (
select nsc from temp
);

update temp2
set salt_id = 1
where salt_id is null;

drop table if exists mwk_hts_compound_lot;

create table mwk_hts_compound_lot
as
select 1 as lot_id, nsc as compound_id, 1 as sample_id, salt_id as salt_code
from temp2;

drop table if exists mwk_rs3_structure_object;

create table mwk_rs3_structure_object
as
select nsc as structure_id, 'M' as object_type, ctab as object_contents
from cmpd_table
where nsc in (
    select nsc from temp2
);


drop table if exists mwk_rs3_structure;

create table mwk_rs3_structure
as
select nsc as structure_id, '24-Nov-2014' as registration_date, '24-Nov-2014' as process_date, 'fake' as class_type
from temp2;

\copy mwk_hts_salt to /tmp/mwk_hts_salt.tsv csv header delimiter as E'\t';
\copy mwk_hts_compound to /tmp/mwk_hts_compound.tsv csv header delimiter as E'\t';
\copy mwk_hts_compound_lot to /tmp/mwk_hts_compound_lot.tsv csv header delimiter as E'\t';
\copy mwk_rs3_structure to /tmp/mwk_rs3_structure.tsv csv header delimiter as E'\t';
\copy mwk_rs3_structure_object to /tmp/mwk_rs3_structure_object.tsv csv header delimiter as E'\t';


# clean up
$dbh->disconnect();

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "finish time $theTime\n";

