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
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

##################################################################
##################################################################
##################################################################

# get the distinct column headers

my $tempSql = qq[select distinct target from qc order by 1];
my $sth = $dbh->prepare($tempSql);
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my @identList;	
while(my $ref = $sth->fetchrow_hashref()) {		
	push (@identList, $ref->{"target"});
}

# run the crosstab

my $ctSql = qq[drop table if exists qc_ct;

create table qc_ct as
select * from crosstab(
	'select source, target, the_count from qc order by 1, 2',
	'select distinct target from qc order by 1'
)
AS ct(
source varchar(32),
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

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";

