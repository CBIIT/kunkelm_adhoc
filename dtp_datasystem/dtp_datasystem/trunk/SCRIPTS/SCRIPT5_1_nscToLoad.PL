#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use CATEGORYPARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

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

my @setsToLoad = getCategoryList();

push @sqlList, qq[
drop table if exists temp
];

push @sqlList, qq[
create table temp(nsc int, cmpd_type varchar(1024))
];

foreach my $setToLoad(@setsToLoad){	
	push @sqlList, qq[
	insert into temp select distinct nsc, '$setToLoad' from $setToLoad
	];
}

push @sqlList, qq[
drop table if exists nsc_to_load
];

push @sqlList, qq[
create table nsc_to_load as
select distinct nsc, cmpd_type 
from temp
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
	#$sth->execute() or warn "Couldn't execute statement: $tempSql " . $sth->errstr;	
}

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

