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
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});

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

# push @whereClauseList, "nsc between 900000 and 1000000";

my @sqlList = ();

push @sqlList,  qq[
drop table if exists nsc_one_lbl_only
];

push @sqlList,  qq[
create table nsc_one_lbl_only
as
select 
nsc.nsc, annot.pseudo_atoms, inv.inventory, bio.nci60, annot.mtxt, leg.prod_molecular_formula, leg.prod_molecular_weight
from nsc_cmpd nsc
left outer join cmpd_annotation annot on nsc.cmpd_annotation_fk = annot.id
left outer join  cmpd_inventory inv on nsc.cmpd_inventory_fk = inv.id
left outer join cmpd_bio_assay bio on nsc.cmpd_bio_assay_fk = bio.id
left outer join cmpd_legacy_cmpd leg on nsc.cmpd_legacy_cmpd_fk = leg.id
where nsc.nsc in (
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
	
	push @sqlList,  qq[
	delete from nsc_one_lbl_only
	where nsc in (
	select nsc from nsc_one_lbl_sna
	)
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
	
	push @sqlList,  qq[
	delete from nsc_one_lbl_only
	where nsc in (
	select nsc from nsc_one_lbl_sna
	)
	];	
	
} 







# multi labels

push @sqlList,  qq[
drop table if exists nsc_multi_lbl_only
];

push @sqlList,  qq[
create table nsc_multi_lbl_only
as
select 
nsc.nsc, array_to_string(array_agg(annot.pseudo_atoms), ',') as agg_lbls, inv.inventory, bio.nci60, annot.mtxt, leg.prod_molecular_formula, leg.prod_molecular_weight
from nsc_cmpd nsc
left outer join cmpd_annotation annot on nsc.cmpd_annotation_fk = annot.id
left outer join  cmpd_inventory inv on nsc.cmpd_inventory_fk = inv.id
left outer join cmpd_bio_assay bio on nsc.cmpd_bio_assay_fk = bio.id
left outer join cmpd_legacy_cmpd leg on nsc.cmpd_legacy_cmpd_fk = leg.id
where nsc.nsc in (
select nsc from qc_with_nsc where ( source = 'two_lbl_no_strc_no_salt' or source = 'multi_lbl_no_strc_no_salt' )
)
group by 1, 3, 4, 5, 6, 7
];


push @sqlList,  qq[
drop table if exists nsc_multi_lbl_sna;
];

push @sqlList,  qq[
create table nsc_multi_lbl_sna
as		
select 'Structure%Not%Available multifrag', nmlo.*
from nsc_multi_lbl_only nmlo
where agg_lbls ilike '%structure%'
and agg_lbls ilike '%not%'
and agg_lbls ilike '%available%'
];

push @sqlList,  qq[
delete from nsc_multi_lbl_only
where nsc in (
select nsc from nsc_multi_lbl_sna
)
];












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

