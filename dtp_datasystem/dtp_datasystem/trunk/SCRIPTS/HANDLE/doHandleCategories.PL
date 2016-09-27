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

# _                 _                          __            _ _   
#| | _____      __ | |__   __ _ _ __   __ _   / _|_ __ _   _(_) |_ 
#| |/ _ \ \ /\ / / | '_ \ / _` | '_ \ / _` | | |_| '__| | | | | __|
#| | (_) \ V  V /  | | | | (_| | | | | (_| | |  _| |  | |_| | | |_ 
#|_|\___/ \_/\_/   |_| |_|\__,_|_| |_|\__, | |_| |_|   \__,_|_|\__|
#                                     |___/                        

# top-level categories













my @categories = ();
push @categories, "one_lbl_only, qc_with_nsc.category = ''one_lbl_no_strc_no_salt''";
push @categories, "multi_lbl_only, qc_with_nsc.category = ''two_lbl_no_strc_no_salt or qc_with_nsc.category = ''multi_lbl_no_strc_no_salt''";
push @categories, "one_strc_no_salt, qc_with_nsc.category like ''\%one_strc_no_salt''";
push @categories, "two_strc_one_salt, qc_with_nsc.category like ''\%two_strc_one_salt''"; 

# sub categories

my @subcategories = ();
push @subcategories, "pass";
push @subcategories, "rewiew_lbls";
push @subcategories, "review_mtxt";
push @subcategories, "mf_fail";
push @subcategories, "mw_fail";
push @subcategories, "charge_fail";

my @sqlList = ();

push @sqlList,qq[
drop table if exists handle
];

push @sqlList,qq[
create table handle(
category varchar,
category_sql varchar,
subcategory varchar,
subcategory_sql varchar
)
];

foreach my $cat(@categories){
	
	my ($catLabel,$catLogic) = split(/,/,$cat);	
	
	foreach my $subcat(@subcategories){
	
		my ($subCatLabel,$subCatLogic) = split(/,/,$subcat);
	
		push @sqlList, "insert into handle values('$catLabel', '$catLogic', '$subcat')";		
	}
}

# execute

foreach my $sqlStmt (@sqlList){
	my $sth = $dbh->prepare($sqlStmt);
	print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	
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

