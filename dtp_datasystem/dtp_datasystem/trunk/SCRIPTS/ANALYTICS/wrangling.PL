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
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});

# execute

my $sql = qq[
drop table if exists wrangling_with_categories
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

$sql = qq[
create table wrangling_with_categories
as
select 'category'::varchar as category, *
from wrangling_with_nsc
limit 0
];

$sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

my @catWhereList = ();

push @catWhereList, ["900K" , "where nsc between 900000 and 999999"];
push @catWhereList, ["one_lbl_only" , "where count_frags = 1 and count_lbl = 1 and count_strc = 0 and nsc not between 900000 and 999999"];
push @catWhereList, ["multi_lbl_only" , "where count_frags > 1 and count_lbl > 1 and count_strc = 0 and nsc not between 900000 and 999999"];
push @catWhereList, ["one_strc", "where count_frags = 1 and count_strc = 1 and count_lbl = 0 and nsc not between 900000 and 999999"];
push @catWhereList, ["two_strc_one_salt", "where count_frags = 2 and count_strc = 2 and salt = 1 and count_lbl = 0 and nsc not between 900000 and 999999"];

foreach my $i (0 .. $#catWhereList) {
	$sql = "insert into wrangling_with_categories select '$catWhereList[$i][0]', * from wrangling_with_nsc $catWhereList[$i][1]";
	$sth = $dbh->prepare($sql);
	print "$sql\n\n";
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
}	

my $sql = qq[
drop table if exists wrangling_good_to_go;
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

$sql = qq[
create table wrangling_good_to_go
as
select 'judgement'::varchar as judgement, *
from wrangling_with_categories
limit 0
];

$sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

my @goodToGoWhereList = ();

push @goodToGoWhereList, ["900K", "where category = '900K'"];

push @goodToGoWhereList, ["one_lbl structure_not_available", "where category = 'one_lbl_only' 
and ( 
lbls ilike '%Structure%Not%Available%'
or lbls ilike '%Structure%Not%Known%'
or lbls ilike '%Recombinant%Protein%'
or lbls ilike '%NCI%Singles%'
or lbls ilike '%Code%No%Only%'
or lbls ilike '%Code%Number%'
or lbls ilike '%B%No%Only%'
)"];

push @goodToGoWhereList, ["multi_lbl structure_not_available", "where category = 'multi_lbl_only'
and (
(
lbls ilike '%structure%'
and lbls ilike '%not%'
and lbls ilike '%available%'
)
or lbls ilike 'code no. only'
or lbls ilike 'b no. only'
or lbls ilike 'unknown'
)"];

push @goodToGoWhereList, ["one_strc", "where category = 'one_strc' and (abs(diff_mw) < 1 or abs(diff_neut_mw) < 1) and (fc = 0 or fc_neut = 0)"];

push @goodToGoWhereList, ["two_strc_one_salt", "where category = 'two_strc_one_salt' and (abs(diff_mw) < 1 or abs(diff_neut_mw) < 1) and (fc = 0 or fc_neut = 0)"];

#push @goodToGoWhereList

foreach my $i (0 .. $#goodToGoWhereList) {
	$sql = "insert into wrangling_good_to_go select '$goodToGoWhereList[$i][0]', * from wrangling_with_categories $goodToGoWhereList[$i][1]";
	$sth = $dbh->prepare($sql);
	print "$sql\n\n";
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
}	

#select category, count(*) from wrangling_with_categories group by 1;
#select judgement, count(*) from wrangling_good_to_go group by 1;
#select nsc, count(*) from wrangling_good_to_go group by 1 having count(*) > 1; 

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";
