#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
#use DATA_SYSTEM_BUILD_PARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=oncologydrugsdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});

# execute

my $sql = qq[
drop table if exists wrangle_categories
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
#$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

my $sql = qq[
create table wrangle_categories
as
select 'category'::varchar as category, *
from wrangling_with_nsc
limit 0
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
#$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

#foreach my $sql (@sqlList){
#	my $sth = $dbh->prepare($sql);
#	print "$sql\n\n";
#	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
#}

my @catWhereList = ();

push @catWhereList, "one_lbl_only" ;
push @catWhereList, "where count_lbl = 1 and count_strc = 0";
push @catWhereList, "multi_lbl_only";
push @catWhereList, "where count_lbl > 1 and count_strc = 0";
push @catWhereList, "one_strc";
push @catWhereList, "where count_strc = 1 and salt = 0";
push @catWhereList, "two_strc_one_salt";
push @catWhereList, "where count_strc = 2 and salt = 1";

push @catWhereList, ["test_cat","test_where_clause"];

foreach my $i (0 .. $#catWhereList) {
	#print "$i - $catWhereList[$i]\n";
	
	print "$i: " . join(',', $catWhereList[$i]) . "\n";
}	

#insert into wrangle_categories
#select 'one_lbl_only', *
#from wrangling_with_nsc
#where count_lbl = 1
#and count_strc = 0;
#
#insert into wrangle_categories
#select 'multi_lbl_only', *
#from wrangling_with_nsc
#where count_lbl > 1
#and count_strc = 0;
#
#insert into wrangle_categories
#select 'one_strc', * 
#from wrangling_with_nsc
#where count_strc = 1
#and salt = 0;
#
#insert into wrangle_categories
#select 'two_strc_one_salt', *
#from wrangling_with_nsc
#where count_strc = 2
#and salt = 1;

#select category, count(*)
#from wrangle_categories
#where (diff_mw is null or abs(diff_mw) < 1.0)
#and (norm_errors is null or norm_errors = 'NoStructure')
#and (formal_charge is null or formal_charge = 0)
#group by category;

#select category, count(*)
#from wrangle_categories
#where 
#(diff_mw is null or abs(diff_mw) < 2.0)
#and (norm_errors is null or norm_errors = 'NoStructure')
#and (formal_charge is null or formal_charge = 0)
#group by category;



# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";