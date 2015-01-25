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
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});


my @expectedTables = getDatasystemTableList();
my @foundTables = ();
my @tablesForDelete = ();

my @tables = $dbh->tables();

foreach my $tbl(@tables){
  my ($schemaName, $tableName)= split(/\./, $tbl);
  print "schema: $schemaName table: $tableName\n";
  if ($schemaName eq "public"){
    push @foundTables, $tableName;
  }
}

foreach my $tbl(@foundTables){	
  if ($tbl ~~ @expectedTables){
  	print "$tbl is expected\n";
  } else {
  	push @tablesForDelete, $tbl;
  }  
}

foreach my $tbl(@tablesForDelete){
	my $sql = "drop table $tbl";
	my $sth = $dbh->prepare($sql);
	print "$sql\n\n";
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";
print "finish: $finishDate\n";
