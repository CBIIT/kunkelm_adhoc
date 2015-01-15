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

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #####   #####           #        ####    ####   #####    #   #
## #    #  #    #           #      #    #  #    #  #    #    # #
## #    #  #####             #     #       #    #  #    #     #
## #    #  #    #             #    #       #    #  #####      #
## #    #  #    #              #   #    #  #    #  #          #
## #####   #####                #   ####    ####   #          #

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## alter table rs3_from_plp_400k_ctab rename to rs3_from_plp_ctab;
## alter table rs3_from_plp_400k_frags rename to rs3_from_plp_frags;
## alter table rs3_from_plp_400k_nsc rename to rs3_from_plp_nsc;

## \copy rs3_from_plp_ctab from /home/mwkunkel/rs3_from_plp_400k_ctab.txt csv header
## \copy rs3_from_plp_ctab from /home/mwkunkel/rs3_from_plp_remainder_ctab.txt csv header

## \copy rs3_from_plp_frags from /home/mwkunkel/rs3_from_plp_400k_frags.txt csv header delimiter as E'\t' null as ''

## second file was written by PLP using comma delimiters...

## \copy rs3_from_plp_frags from /home/mwkunkel/rs3_from_plp_remainder_frags.txt csv header

## \copy rs3_from_plp_nsc from /home/mwkunkel/rs3_from_plp_400k_nsc.txt csv header delimiter as E'\t'
## \copy rs3_from_plp_nsc from /home/mwkunkel/rs3_from_plp_remainder_nsc.txt csv header delimiter as E'\t'

## have to make sure that the pipeline pilot index didn't contain any overlaps from the two data fetch sessions

## select min(fragmentindex), max(fragmentindex)  
## from rs3_from_plp_frags 
## where nsc <= 400000;

## select min(fragmentindex), max(fragmentindex)  
## from rs3_from_plp_frags 
## where nsc >= 400000;

    #    #    #  #####   ######  #    #  ######   ####
    #    ##   #  #    #  #        #  #   #       #
    #    # #  #  #    #  #####     ##    #####    ####
    #    #  # #  #    #  #         ##    #            #
    #    #   ##  #    #  #        #  #   #       #    #
    #    #    #  #####   ######  #    #  ######   ####

my @indexFieldList = ("nsc", "fragmentindex", "can_smi", "can_taut", "can_taut_strip_stereo", "atomarray");

foreach my $indexField(@indexFieldList){
	
	my $tempSql = qq[create index rfps_$indexField on rs3_from_plp_frags($indexField)];

	print "$tempSql\n"; 
        my $sth = $dbh->prepare($tempSql);
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;
	
}

my @indexFieldList = ("nsc", "fragmentindex");

foreach my $indexField(@indexFieldList){
	
	my $tempSql = qq[create index rfpc_$indexField on rs3_from_plp_ctab($indexField)];

	print "$tempSql\n"; 
        my $sth = $dbh->prepare($tempSql);
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;
	
}

my @indexFieldList = ("nsc");

foreach my $indexField(@indexFieldList){
	
	my $tempSql = qq[create index rfpn_$indexField on rs3_from_plp_nsc($indexField)];

	print "$tempSql\n"; 
        my $sth = $dbh->prepare($tempSql);
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;
	
}

my $tempSql = qq[vacuum analyze verbose rs3_from_plp_frags];
print "$tempSql\n"; 
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

 #    #  #####   #####     ##     #####  ######
 #    #  #    #  #    #   #  #      #    #
 #    #  #    #  #    #  #    #     #    #####
 #    #  #####   #    #  ######     #    #
 #    #  #       #    #  #    #     #    #
  ####   #       #####   #    #     #    ######


  ####     ##    #        #####   ####
 #        #  #   #          #    #
  ####   #    #  #          #     ####
      #  ######  #          #         #
 #    #  #    #  #          #    #    #
  ####   #    #  ######     #     ####

## UPDATE the frags table for salts

my $tempSql = qq[alter table rs3_from_plp_frags add salt_smiles varchar(1024)];

print "$tempSql\n"; 
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[alter table rs3_from_plp_frags add salt_id bigint];

print "$tempSql\n"; 
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;


my $tempSql = qq[
update rs3_from_plp_frags
set salt_smiles = cmpd_known_salt.can_taut_strip_stereo, salt_id = cmpd_known_salt.id
from cmpd_known_salt
where rs3_from_plp_frags.can_taut_strip_stereo = cmpd_known_salt.can_taut_strip_stereo];

print "$tempSql\n"; 
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

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

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "finish time $theTime\n";

