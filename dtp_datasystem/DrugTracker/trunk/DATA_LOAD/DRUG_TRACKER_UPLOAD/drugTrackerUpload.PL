#!/usr/bin/perl

use strict;
#KEEP

# load module
use DBI;

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "start time $theTime\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=novumdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

#
#
#alias
#
#

# extract and split any alias

my $sql = qq[select id, nsc, alias from loading];
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
my @aliasList;	
while(my $ref = $sth->fetchrow_hashref()) {
	my $id = $ref->{'id'};
	my $nsc = $ref->{'nsc'};	
	my @splitAlias = split(';', $ref->{'alias'});	
	foreach my $alias (@splitAlias) {		
		if (length($alias) > 0){
			push (@aliasList, $id . ';' . $nsc . ';' . trim($alias));
    }
  }
}
$sth->finish;

foreach my $alias (@aliasList) {
	print "alias: $alias\n";   
}

my $sql = qq[drop table if exists temp_alias];
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

my $sql = qq[create table temp_alias(id int, nsc int, alias varchar(128))];
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

foreach my $alias (@aliasList) {	
	my @splitAlias = split(';', $alias);
	my $id = $splitAlias[0];
	my $nsc = $splitAlias[1];
	if (length($nsc)==0){ 
		$nsc = 'null';
	}
	my $aliasStr = trim($splitAlias[2]);	
	my $sql = qq[insert into temp_alias(id, nsc, alias) values ($id, $nsc, '$aliasStr')];
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
}

#
#
# target
#
#

# extract and split any target

my $sql = qq[select id, nsc, target from loading];
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
my @targetList;	
while(my $ref = $sth->fetchrow_hashref()) {
	my $id = $ref->{'id'};
	my $nsc = $ref->{'nsc'};		
	my @splitTarget = split(/,|;/, $ref->{'target'});	
	foreach my $target (@splitTarget) {		
		if (length($target) > 0){	    
			push (@targetList, $id . ';' . $nsc . ';' . trim($target));
    }
  }
}
$sth->finish;

foreach my $target (@targetList) {
	print "target: $target\n";   
}

my $sql = qq[drop table if exists temp_target];
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

my $sql = qq[create table temp_target(id int, nsc int, target varchar(128))];
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

foreach my $target (@targetList) {	
	my @splitTarget = split(';', $target);
	my $id = $splitTarget[0];
	my $nsc = $splitTarget[1];
	if (length($nsc)==0){ 
		$nsc = 'null';
	}
	my $targetStr = $splitTarget[2];	
	my $sql = qq[insert into temp_target(id, nsc, target) values ($id, $nsc, '$targetStr')];
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
}

# clean up
$dbh->disconnect();

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "finish time $theTime\n";

# Perl trim function to remove whitespace from the start and end of the string
sub trim($)
{
	my $string = shift;
	$string =~ s/^\s+//;
	$string =~ s/\s+$//;
	return $string;
}
# Left trim function to remove leading whitespace
sub ltrim($)
{
	my $string = shift;
	$string =~ s/^\s+//;
	return $string;
}
# Right trim function to remove trailing whitespace
sub rtrim($)
{
	my $string = shift;
	$string =~ s/\s+$//;
	return $string;
}


