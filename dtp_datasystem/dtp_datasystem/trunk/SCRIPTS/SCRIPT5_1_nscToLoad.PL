#!/usr/bin/perl

use strict;
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

  #####   ####           #        ####     ##    #####
    #    #    #          #       #    #   #  #   #    #
    #    #    #          #       #    #  #    #  #    #
    #    #    #          #       #    #  ######  #    #
    #    #    #          #       #    #  #    #  #    #
    #     ####           ######   ####   #    #  #####


my @setsToLoad = (
	"one_strc",
	"two_strc_any_lbl_one_salt",
	"one_strc_one_lbl",
	"one_strc_multi_lbl",
	"one_lbl",
	"multi_lbl",
	"multi_strc_any_lbl",
	"two_strc_any_lbl_two_salt",
	"two_strc_any_lbl"
	);

my $tempSql = qq[drop table if exists temp];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[create table temp(nsc int, cmpd_type varchar(1024))];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

foreach my $setToLoad(@setsToLoad){
	
	my $tempSql = qq[insert into temp select distinct nsc, '$setToLoad' from $setToLoad];
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;
	
}

my $tempSql = qq[drop table if exists nsc_to_load];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table nsc_to_load as
select distinct nsc, cmpd_type 
from temp];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[create index ntl_nsc on nsc_to_load(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[create index ntl_cmpd_type on nsc_to_load(cmpd_type)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

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

