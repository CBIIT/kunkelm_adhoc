#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use DATA_SYSTEM_BUILD_PARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my @sqlList = ();

push @sqlList, qq[
drop table if exists nsc_to_load
];

push @sqlList, qq[
create table nsc_to_load as
select distinct nsc, category as cmpd_type
from comb_cat

where nsc not in (183489, 189352)

];

push @sqlList, qq[
create index ntl_nsc on nsc_to_load(nsc)
];

push @sqlList, qq[
create index ntl_cmpd_type on nsc_to_load(cmpd_type)
];

foreach my $tempSql(@sqlList){
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	
}

my $tempSql = "vacuum analyze nsc_to_load";
my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";

