#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use DATA_SYSTEM_BUILD_PARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList getDatasystemTableList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 0, 'PrintError' => 1});

my @tableNameList = getDatasystemTableList();

@tableNameList = grep {$_ ne "cmpd_known_salt" and $_ ne "cmpd_legacy_cmpd" } @tableNameList;

foreach my $tableName(@tableNameList){
	print "tableName: $tableName\n";
}

print "\n\n\n";

my @categoryCriteriaList = getCategoryCriteriaList();

foreach my $categoryCriteria(@categoryCriteriaList){
	print "categoryCriteria: $categoryCriteria\n";
}

print "\n\n\n";

my @qcCriteriaList = getQualityControlCriteriaList();

foreach my $qcCriteria(@qcCriteriaList){
	print "qcCriteria: $qcCriteria\n";
}

print "\n\n\n";

my @categoryList = getCategoryList(); 

foreach my $category(@categoryList){
	print "category: $category\n";
}


# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";
