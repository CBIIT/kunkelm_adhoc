#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use DATA_SYSTEM_BUILD_PARAMETERS qw(getDatasystemTableList getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});
my $tempSql;

#    #  #    #  #    #  ######
##   #  #    #  #   #   #
# #  #  #    #  ####    #####
#  # #  #    #  #  #    #
#   ##  #    #  #   #   #
#    #   ####   #    #  ######

$tempSql = qq[
delete from cmpd_list_member 
where cmpd_list_fk 
in (
select id from cmpd_list where list_name like 'QC%'
)
];
my $sth = $dbh->prepare($tempSql);
#print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

$tempSql = qq[
delete from cmpd_list 
where list_name like 'QC%'
];
my $sth = $dbh->prepare($tempSql);
#print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my @categoryList = getCategoryList();

foreach my $ctg(@categoryList){
	
	print "Processing category: $ctg\n";
	
	# is there anything in this category
	
	$tempSql = qq[
	select count(distinct nsc) as the_count
	from qc_with_nsc
	where source = '$ctg'
	];
	my $sth = $dbh->prepare($tempSql);
	#print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;
	my $theCount;
	$sth->bind_columns(\$theCount);
	while ($sth->fetch()){
		#print "theCount for $ctg is: $theCount\n";			
	}
	
	if ($theCount <= 0){
		
		print "---------theCount for $ctg is: $theCount\n";
		print "---------NOT creating a QC list for $ctg\n\n";
		
	} else {
		
		print "---------theCount for $ctg is: $theCount\n";
		print "---------CREATING a QC list for $ctg\n";
		
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
		where source = '$ctg'
		and nsc not in (183489, 189352)
		limit 500
		];
		my $sth = $dbh->prepare($tempSql);
		#print "$tempSql\n\n";
		$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
		
		# create cmpd_list
		
		# get a new id	
		
		$tempSql = qq[select nextval('cmpd_list_seq')];
		my $sth = $dbh->prepare($tempSql);
		#print "$tempSql\n\n";
		$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;
		my $cmpdListId;
		$sth->bind_columns(\$cmpdListId);
		while ($sth->fetch()) {
			print "---------cmpdListId for $ctg is: $cmpdListId\n\n";
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
		'QC $ctg',
		now(),
		'kunkelm',
		'kunkelm',
		round(random() * 9223372036854775807),
		count(temp.*),
		null,
		null,
		null
		from temp
		];
		my $sth = $dbh->prepare($tempSql);
		# print "$tempSql\n\n";
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
	
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";