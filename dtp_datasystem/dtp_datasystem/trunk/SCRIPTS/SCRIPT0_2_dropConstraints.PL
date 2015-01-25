#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use CATEGORYPARAMETERS qw(getDatasystemTableList getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 0});

# TO DROP CONSTRAINTS

my $sql = qq[
select stmt from drop_constraint_statements
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql" . $sth->errstr;	

my $stmt;
$sth -> bind_columns(\$stmt);

my @stmtList = ();

while($sth -> fetch()){	
	# print "duringFetch: $stmt\n";	
  push @stmtList, $stmt;
}

foreach my $sql(@stmtList){
  my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or warn "Couldn't execute statement: $sql" . $sth->errstr;	
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";
print "finish: $finishDate\n";


