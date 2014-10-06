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

my $tempSql = qq[\\copy rs3_from_plp_frags from '/home/mwkunkel/rs3_from_plp_400k_frags.txt' csv header delimiter as E'\\t'];
print "$tempSql\n"; # my $sth = $dbh->prepare($tempSql);
# $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[\\copy rs3_from_plp_frags from '/home/mwkunkel/rs3_from_plp_remainder_frags.txt' csv header delimiter as E'\\t'];
print "$tempSql\n"; # my $sth = $dbh->prepare($tempSql);
# $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## select min(fragmentindex), max(fragmentindex)  
## from rs3_from_plp_frags 
## where nsc <= 400000;

## select min(fragmentindex), max(fragmentindex)  
## from rs3_from_plp_frags 
## where nsc >= 400000;

## update rs3_from_plp_frags
## set fragmentindex = fragmentindex + 1000000
## where nsc >= 400000;

my @indexFieldList = ("nsc", "fragmentindex", "can_smi", "can_taut", "can_taut_strip_stereo", "atomarray");

foreach my $indexField(@indexFieldList){
	
	my $tempSql = qq[create index rfps_$indexField on rs3_from_plp_frags($indexField)];
	print "$tempSql\n"; # my $sth = $dbh->prepare($tempSql);
	# $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;
	
}

my $tempSql = qq[vacuum analyze verbose rs3_from_plp_frags];
print "$tempSql\n"; # my $sth = $dbh->prepare($tempSql);
# $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

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

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## UPDATE the frags table for salts

my $tempSql = qq[alter table rs3_from_plp_frags add salt_smiles varchar(1024)];
print "$tempSql\n"; # my $sth = $dbh->prepare($tempSql);
# $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
update rs3_from_plp_frags
set salt_smiles = same_salts.can_taut_strip_stereo
from same_salts
where rs3_from_plp_frags.can_taut_strip_stereo = same_salts.can_taut_strip_stereo];
print "$tempSql\n"; # my $sth = $dbh->prepare($tempSql);
# $sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## 82127

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

