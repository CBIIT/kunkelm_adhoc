#!/usr/bin/perl

use strict;

# load module
use DBI;

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "start time $theTime\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

my @allTbls = qw(
	one_strc
	one_strc_salt
	one_lbl
	multi_lbl
	one_strc_one_lbl
	one_strc_multi_lbl
	two_strc_any_lbl_one_salt
	two_strc_any_lbl_two_salt
	two_strc_any_lbl
	multi_strc_any_lbl
	);

my $sql = qq[drop table if exists qc_with_nsc];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

my $sql = qq[create table qc_with_nsc (source varchar(1024), comparator varchar(1024), target varchar(1024), nsc int)];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

# counts from all categories

for my $cnt(0 .. @allTbls - 1){
	
	my $sql = qq[drop table if exists temp];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[create table temp 
	as
	select nsc, mf, mw, sum(molecular_weight) as fw
	from $allTbls[$cnt]
	group by 1, 2, 3];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc_with_nsc 
	select distinct '$allTbls[$cnt]', 'count nsc', 'count nsc', nsc 
	from temp];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc_with_nsc
	select distinct '$allTbls[$cnt]', 'count nsc', 'mw DIFF 0', nsc 
	from temp 
	where round(mw) = round(fw)];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc_with_nsc
	select distinct '$allTbls[$cnt]', 'count nsc', 'mw DIFF 2', nsc 
	from temp 
	where abs( round(mw) - round(fw) ) < 2];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc_with_nsc
	select distinct '$allTbls[$cnt]', 'count nsc', 'mw DIFF 5', nsc 
	from temp 
	where abs( round(mw) - round(fw) ) < 5];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc_with_nsc
	select distinct '$allTbls[$cnt]', 'count nsc', 'mf W99', nsc 
	from temp 
	where mf  = 'W99'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc_with_nsc
	select distinct '$allTbls[$cnt]', 'count nsc', 'mf with .', nsc 
	from temp 
	where mf  like '%.%'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc_with_nsc
	select distinct '$allTbls[$cnt]', 'count nsc', 'mf ~ .*\(.*\)[xn0-9]', nsc 
	from temp 
	where mf ~ '.*\\(.*\\)[xn0-9]'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
}

# indexes

my $sql = qq[create index qc_with_nsc_source on qc_with_nsc(source)];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

my $sql = qq[create index qc_with_nsc_comparator on qc_with_nsc(comparator)];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

my $sql = qq[create index qc_with_nsc_target on qc_with_nsc(target)];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

# clean up
$dbh->disconnect();

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "finish time $theTime\n";
