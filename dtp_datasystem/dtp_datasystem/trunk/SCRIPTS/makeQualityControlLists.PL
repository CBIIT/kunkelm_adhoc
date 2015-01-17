#!/usr/bin/perl

use strict;
use warnings;


use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

my $tempSql;

#    #  #    #  #    #  ######
##   #  #    #  #   #   #
# #  #  #    #  ####    #####
#  # #  #    #  #  #    #
#   ##  #    #  #   #   #
#    #   ####   #    #  ######

$tempSql = qq[delete from cmpd_list_member where cmpd_list_fk in (select id from cmpd_list where list_name like 'QC SAMPLE LIST%')];
my $sth = $dbh->prepare($tempSql);
#print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

$tempSql = qq[delete from cmpd_list where list_name like 'saltLabel%'];
my $sth = $dbh->prepare($tempSql);
#print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

# get the distinct categories
# fetch from the qc table since there is only 
# one row for each unique source + comparator + target

$tempSql = qq[
select distinct source, comparator, target
from qc
];
my $sth = $dbh->prepare($tempSql);
#print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my @row;
while (@row = $sth->fetchrow_array) {  # retrieve one row
	print join(", ", @row), "\n";
	
	print "source: $row[0]\n";
	print "comparator: $row[1]\n";
	print "target: $row[2]\n";
	
	# use table temp
	
	$tempSql = qq[
	drop table if exists temp
	];
	my $sth = $dbh->prepare($tempSql);
	#print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
	
	# the nsc are fetched from qc_with_nsc

	$tempSql = qq[
	create table temp as
	select distinct nsc 
	from qc_with_nsc
	where source = '$row[0]'
	and comparator = '$row[1]'
	and target = '$row[2]'
        limit 500
	];
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
	
	# create cmpd_list
	
	# get the id
	
	my $cmpdListId;
	
	$tempSql = qq[select nextval('cmpd_list_seq')];
	my $sth = $dbh->prepare($tempSql);
	#print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
	while (my @row = $sth->fetchrow_array) {
		$cmpdListId = $row[0];
		print "cmpdListId: $cmpdListId\n";
	}
	
	$tempSql = qq[
	insert into cmpd_list
	(
	id,
	list_name,
	date_created,
	list_owner,
	share_with,
	cmpd_list_id,
	count_list_members,
	anchor_smiles,
	list_comment,
	anchor_comment
	)
	
	select
	
	$cmpdListId,
	'QC SAMPLE LIST $row[0] $row[1] $row[2]',
	now(),
	'mwkunkel',
	'mwkunkel',
	round(random() * 9223372036854775807),
	count(temp.*),
	null,
	null,
	null
	from temp
	];
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
	
	# now the cmpd_list_members
	
	$tempSql = qq[
	insert into cmpd_list_member
	(	
	id,
	list_member_comment, 
	cmpd_fk,
	cmpd_list_fk
	)
	
	select 
	
	nextval('cmpd_list_member_seq'),
	null,
	nsc,
	$cmpdListId
	from temp
	];
	my $sth = $dbh->prepare($tempSql);
	#print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
	
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";