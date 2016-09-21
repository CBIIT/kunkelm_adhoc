#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
# use DATA_SYSTEM_BUILD_PARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
#my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});
my $dbh = DBI->connect("DBI:Pg:dbname=oncologydrugsdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});

#
# set up criteria for selection(s)
# 

my @ilikeList = qw[
%Structure%Not%Available%
%Structure%Not%Known%
%Recombinant%Protein%
%NCI%Singles%
%Code%No%Only%
%Code%Number%
];

my @whereClauseList = ();

push @whereClauseList, "nsc between 900000 and 1000000";
push @whereClauseList, "nsc between 0 and 10000";

my @sqlList = ();

push @sqlList,  qq[
drop table if exists nsc_one_lbl_only
];

push @sqlList,  qq[
create table nsc_one_lbl_only
as
select 
nsc.nsc, annot.pseudo_atoms, inv.inventory, bio.nci60, annot.mtxt
from nsc_cmpd nsc, cmpd_annotation annot, cmpd_inventory inv, cmpd_bio_assay bio
where nsc.cmpd_annotation_fk = annot.id
and nsc.cmpd_bio_assay_fk = bio.id
and nsc.cmpd_inventory_fk = inv.id
and nsc.nsc in (
select nsc from qc_with_nsc where source = 'one_lbl_no_strc_no_salt'
)
];

push @sqlList,  qq[
drop table if exists nsc_one_lbl_sna;
];

push @sqlList,  qq[
create table nsc_one_lbl_sna
as
select 'filler' as category, nolo.*
from nsc_one_lbl_only nolo
limit 0
];

# for each ilike

foreach my $ilike(@ilikeList){
	push @sqlList,  qq[
	insert into nsc_one_lbl_sna
	select '$ilike', nolo.*
	from nsc_one_lbl_only nolo
	where pseudo_atoms ilike '$ilike'
	];
}

# for each whereClause

foreach my $whereClause(@whereClauseList){
	push @sqlList,  qq[
	insert into nsc_one_lbl_sna
	select '$whereClause', nolo.*
	from nsc_one_lbl_only nolo
	where $whereClause
	];
} 

# execute

foreach my $sqlStmt (@sqlList){
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

