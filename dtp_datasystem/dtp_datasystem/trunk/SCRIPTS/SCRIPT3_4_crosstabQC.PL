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

##################################################################
##################################################################
##################################################################

# set up for command line

my $tblName = "qc";
my $colName = "target";
my $rowName = "source";

# get the distinct column headers

my $tempSql = qq[select distinct $colName from $tblName order by 1];

my $sth = $dbh->prepare($tempSql);

$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my @identList;	
while(my $ref = $sth->fetchrow_hashref()) {		
	push (@identList, $ref->{$colName});
}

$sth->finish;

# run the crosstab

my $ctSql = qq[drop table if exists $tblName\_ct;

create table $tblName\_ct as
select * from crosstab(
	'select $rowName, $colName, the_count from $tblName order by 1, 2',
	'select distinct $colName from $tblName order by 1'
)
AS ct(
$rowName varchar(32),
];

my $lastIdent = $identList[$#identList];

foreach my $thisIdent(@identList){		
	if ($thisIdent eq $lastIdent){
		$ctSql = $ctSql . qq["$thisIdent" double precision);\n]; 	
	} else {
		$ctSql = $ctSql . qq["$thisIdent" double precision,\n];
	}		
}

print "$ctSql\n";

my $sth = $dbh->prepare($ctSql);
$sth->execute() or die "Couldn't execute statement: $ctSql " . $sth->errstr;	
$sth->finish;

# clean up
$dbh->disconnect();

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "finish time $theTime\n";

