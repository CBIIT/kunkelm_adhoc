#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});

##################################################################
##################################################################
##################################################################

# the targets in qc are like:
# count nsc inv&nci60
# mf with . inventory
# mw DIFF 1 inventory

# get the targets as distinct column headers

my $tempSql = qq[select distinct target from qc order by 1];
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
my $targ;
$sth->bind_columns(\$targ);

my @identList;	

while($sth->fetch()){
  push @identList, $targ;
}

#while(my $ref = $sth->fetchrow_hashref()) {		
#	push (@identList, $ref->{"target"});
#}

# run the crosstab

my $ctSql = qq[drop table if exists qc_ct;

create table qc_ct as
select * from crosstab(
	'select source, target, the_count from qc order by 1, 2',
	'select distinct target from qc order by 1'
)
AS ct(
source varchar,
];

# the datatypes for the crosstab fields

# for managing commas
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

##################################################################
##################################################################
##################################################################

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";

