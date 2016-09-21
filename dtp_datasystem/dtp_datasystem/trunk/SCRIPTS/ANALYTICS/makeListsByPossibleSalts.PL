#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
#use DATA_SYSTEM_BUILD_PARAMETERS qw(getDatasystemTableList getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
#my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});
my $dbh = DBI->connect("DBI:Pg:dbname=oncologydrugsdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});


my $sqlStmt;

#    #  #    #  #    #  ######
##   #  #    #  #   #   #
# #  #  #    #  ####    #####
#  # #  #    #  #  #    #
#   ##  #    #  #   #   #
#    #   ####   #    #  ######

$sqlStmt = qq[
delete from cmpd_list_member 
where cmpd_list_fk 
in (
select id from cmpd_list where list_name like 'POSS_SALT%'
)
];
my $sth = $dbh->prepare($sqlStmt);
print "$sqlStmt\n\n";
$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	

$sqlStmt = qq[
delete from cmpd_list 
where list_name like 'POSS_SALT%'
];
my $sth = $dbh->prepare($sqlStmt);
print "$sqlStmt\n\n";
$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	

# fetch possible salts

my @canTautList = ();

$sqlStmt = qq[
select distinct can_taut from possible_salts	
];

my $sth = $dbh->prepare($sqlStmt);
print "$sqlStmt\n\n";
$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;
my $canTaut;
$sth->bind_columns(\$canTaut);
while ($sth->fetch()){
	print "canTaut: $canTaut\n";
	push @canTautList, $canTaut; 
}

foreach my $canTaut(@canTautList){
	
	print "Processing canTaut: $canTaut\n";
	
	print "---------CREATING a POSS_SALT list for $canTaut\n";
	
	$sqlStmt = qq[
	drop table if exists temp
	];
	my $sth = $dbh->prepare($sqlStmt);
	print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	
	
	# the nsc are fetched from possible_salts_with_nsc
	
	$sqlStmt = qq[
	create table temp as
	select distinct nsc 
	from possible_salts_with_nsc
	where can_taut = '$canTaut'
	limit 500
	];
	my $sth = $dbh->prepare($sqlStmt);
	print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	
	
	# create cmpd_list
	
	# get a new id	
	
	$sqlStmt = qq[select nextval('cmpd_list_seq')];
	my $sth = $dbh->prepare($sqlStmt);
	print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;
	my $cmpdListId;
	$sth->bind_columns(\$cmpdListId);
	while ($sth->fetch()) {
		print "---------cmpdListId for $canTaut is: $cmpdListId\n\n";
	}
	
	$sqlStmt = qq[
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
	'POSS_SALT $canTaut',
	now(),
	'kunkelm',
	'kunkelm',
	round(random() * 9223372036854775807),
	count(temp.*),
	'$canTaut',
	'POSS_SALT',
	'POSS_SALT'
	from temp
	];
	my $sth = $dbh->prepare($sqlStmt);
	# print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	
	
	# now the cmpd_list_members
	
	$sqlStmt = qq[
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
	my $sth = $dbh->prepare($sqlStmt);
	print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	
	
	
	
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";