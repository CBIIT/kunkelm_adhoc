#!/usr/bin/perl

use strict;
use warnings;

use DBI;

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "start time $theTime\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});


## #####   #####           #        ####    ####   #####    #   #
## #    #  #    #           #      #    #  #    #  #    #    # #
## #    #  #####             #     #       #    #  #    #     #
## #    #  #    #             #    #       #    #  #####      #
## #    #  #    #              #   #    #  #    #  #          #
## #####   #####                #   ####    ####   #          #

=pod
# Have to copy/paste the below to load the data into the database
# PostGres \copy doesn't work via DBI

\copy rs3_from_plp_ctab from /home/mwkunkel/rs3_from_plp_400k_ctab.txt csv header
\copy rs3_from_plp_ctab from /home/mwkunkel/rs3_from_plp_remainder_ctab.txt csv header

\copy rs3_from_plp_frags from /home/mwkunkel/rs3_from_plp_400k_frags.txt csv header delimiter as E'\t' null as ''

second file was written by PLP using comma delimiters...

\copy rs3_from_plp_frags from /home/mwkunkel/rs3_from_plp_remainder_frags.txt csv header

\copy rs3_from_plp_nsc from /home/mwkunkel/rs3_from_plp_400k_nsc.txt csv header delimiter as E'\t'
\copy rs3_from_plp_nsc from /home/mwkunkel/rs3_from_plp_remainder_nsc.txt csv header delimiter as E'\t'

have to make sure that the pipeline pilot index didn't contain any overlaps from the two data fetch sessions

select min(fragmentindex), max(fragmentindex)  
from rs3_from_plp_frags 
where nsc <= 400000;

select min(fragmentindex), max(fragmentindex)  
from rs3_from_plp_frags 
where nsc >= 400000;
=cut

##    #    #    #  #####   ######  #    #  ######   ####
##    #    ##   #  #    #  #        #  #   #       #
##    #    # #  #  #    #  #####     ##    #####    ####
##    #    #  # #  #    #  #         ##    #            #
##    #    #   ##  #    #  #        #  #   #       #    #
##    #    #    #  #####   ######  #    #  ######   ####

my @indexFieldList = ("nsc", "fragmentindex", "can_smi", "can_taut", "can_taut_strip_stereo", "atomarray");

foreach my $indexField(@indexFieldList){
	
	my $sqlStmt = qq[create index rfps_$indexField on rs3_from_plp_frags($indexField)];	
	print "$sqlStmt\n\n"; 
	my $sth = $dbh->prepare($sqlStmt);
	#$sth->execute() or die "Couldn't execute statement: my $sqlStmt " . $sth->errstr;
	
}

@indexFieldList = ("nsc", "fragmentindex");

foreach my $indexField(@indexFieldList){
	
	my $sqlStmt = qq[create index rfpc_$indexField on rs3_from_plp_ctab($indexField)];	
	print "$sqlStmt\n\n"; 
	my $sth = $dbh->prepare($sqlStmt);
	#$sth->execute() or die "Couldn't execute statement: my $sqlStmt " . $sth->errstr;
	
}

@indexFieldList = ("nsc");

foreach my $indexField(@indexFieldList){
	
	my $sqlStmt = qq[create index rfpn_$indexField on rs3_from_plp_nsc($indexField)];
	
	print "$sqlStmt\n\n"; 
	my $sth = $dbh->prepare($sqlStmt);
	#$sth->execute() or die "Couldn't execute statement: my $sqlStmt " . $sth->errstr;
	
}

my $sqlStmt = qq[vacuum analyze verbose rs3_from_plp_frags];
print "$sqlStmt\n\n"; 
my $sth = $dbh->prepare($sqlStmt);
#$sth->execute() or die "Couldn't execute statement: my $sqlStmt " . $sth->errstr;

## #    #  #####   #####     ##     #####  ######
## #    #  #    #  #    #   #  #      #    #
## #    #  #    #  #    #  #    #     #    #####
## #    #  #####   #    #  ######     #    #
## #    #  #       #    #  #    #     #    #
##  ####   #       #####   #    #     #    ######
##
##
##  ####     ##    #        #####   ####
## #        #  #   #          #    #
##  ####   #    #  #          #     ####
##      #  ######  #          #         #
## #    #  #    #  #          #    #    #
##  ####   #    #  ######     #     ####

## UPDATE the frags table for salts

$sqlStmt = qq[alter table rs3_from_plp_frags add salt_smiles varchar(1024)];
print "$sqlStmt\n\n"; 
$sth = $dbh->prepare($sqlStmt);
#$sth->execute() or die "Couldn't execute statement: my $sqlStmt " . $sth->errstr;

$sqlStmt = qq[alter table rs3_from_plp_frags add salt_id bigint];
print "$sqlStmt\n\n"; 
$sth = $dbh->prepare($sqlStmt);
#$sth->execute() or die "Couldn't execute statement: my $sqlStmt " . $sth->errstr;


$sqlStmt = qq[
update rs3_from_plp_frags
set salt_smiles = cmpd_known_salt.can_taut_strip_stereo, salt_id = cmpd_known_salt.id
from cmpd_known_salt
where rs3_from_plp_frags.can_taut_strip_stereo = cmpd_known_salt.can_taut_strip_stereo];
print "$sqlStmt\n\n"; 
$sth = $dbh->prepare($sqlStmt);
#$sth->execute() or die "Couldn't execute statement: my $sqlStmt " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #    #  #####            #####   ####           #    #  ######  #####   ######
## #    #  #    #             #    #    #          #    #  #       #    #  #
## #    #  #    #             #    #    #          ######  #####   #    #  #####
## #    #  #####              #    #    #          #    #  #       #####   #
## #    #  #                  #    #    #          #    #  #       #   #   #
##  ####   #                  #     ####           #    #  ######  #    #  ######
##
##
## #####   #####   ######  #####           ######   ####   #####
## #    #  #    #  #       #    #          #       #    #  #    #
## #    #  #    #  #####   #    #          #####   #    #  #    #
## #####   #####   #       #####           #       #    #  #####
## #       #   #   #       #               #       #    #  #   #
## #       #    #  ######  #               #        ####   #    #
##
##
##  ####    #####    ##     #####   ####
## #          #     #  #      #    #
##  ####      #    #    #     #     ####
##      #     #    ######     #         #
## #    #     #    #    #     #    #    #
##  ####      #    #    #     #     ####

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

# clean up
$dbh->disconnect();

($second, $minute, $hour, $dayOfMonth, $month, $yearOffset, $dayOfWeek, $dayOfYear, $daylightSavings) = localtime();
$year = 1900 + $yearOffset;
$theTime = "$hour:$minute:$second";
print "finish time $theTime\n";

