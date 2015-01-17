#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use CATEGORYPARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

my @paramsList = getQualityControlCriteriaList();
my @allCategories = getCategoryList();

##     _                                   _ 
##  __| |_ __ ___  _ __     __ _ _ __   __| |
## / _` | '__/ _ \| '_ \   / _` | '_ \ / _` |
##| (_| | | | (_) | |_) | | (_| | | | | (_| |
## \__,_|_|  \___/| .__/   \__,_|_| |_|\__,_|
##                |_|                        
##                              _       
## _ __ ___  ___ _ __ ___  __ _| |_ ___ 
##| '__/ _ \/ __| '__/ _ \/ _` | __/ _ \
##| | |  __/ (__| | |  __/ (_| | ||  __/
##|_|  \___|\___|_|  \___|\__,_|\__\___|
##                                      
##       
##  __ _  ___ 
## / _` |/ __|
##| (_| | (__ 
## \__, |\___|
##    |_|   

my $sql = qq[
drop table if exists qc
];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

my $sql = qq[
create table qc (source varchar(1024), comparator varchar(1024), target varchar(1024), the_count int)
];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

##              _                                        __ 
##  __ _  ___  | |__  _   _    __ _  __ _  __ _    ___  / _|
## / _` |/ __| | '_ \| | | |  / _` |/ _` |/ _` |  / _ \| |_ 
##| (_| | (__  | |_) | |_| | | (_| | (_| | (_| | | (_) |  _|
## \__, |\___| |_.__/ \__, |  \__,_|\__, |\__, |  \___/|_|  
##    |_|             |___/         |___/ |___/             
##                       _ _   _                        
##  __ _  ___  __      _(_) |_| |__      _ __  ___  ___ 
## / _` |/ __| \ \ /\ / / | __| '_ \    | '_ \/ __|/ __|
##| (_| | (__   \ V  V /| | |_| | | |   | | | \__ \ (__ 
## \__, |\___|___\_/\_/ |_|\__|_| |_|___|_| |_|___/\___|
##    |_|   |_____|                |_____|              
##

my $sql = qq[
insert into qc (source, comparator, target, the_count)
select source, comparator, target||' '||invTest, count(*) 
from qc_with_nsc
group by source, comparator, target, invTest
];

print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

##                     _             
##  _____   _____ _ __| | __ _ _ __  
## / _ \ \ / / _ \ '__| |/ _` | '_ \ 
##| (_) \ V /  __/ |  | | (_| | |_) |
## \___/ \_/ \___|_|  |_|\__,_| .__/ 
##                            |_|    

my @outerCategories;
my @innerCategories;

foreach my $thisCategory (@allCategories){
	push (@outerCategories, $thisCategory);
	push (@innerCategories, $thisCategory);
}

my $sql = qq[
drop table if exists qc_overlap
];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

my $sql = qq[
create table qc_overlap (source varchar(1024), target varchar(1024), the_count int)
];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

foreach my $outerTbl (@outerCategories){
	
	foreach my $innerTbl (@innerCategories){
		
		print "$outerTbl $innerTbl \n";
		
		if ($outerTbl ne $innerTbl){
			
			my $sql = qq[
			insert into qc_overlap
			select '$outerTbl', '$innerTbl',
			count(i.*)
			from (
			select nsc from $outerTbl
			intersect
			select nsc from $innerTbl
			) as i
			];
			
			print "$sql\n";
			
			my $sth = $dbh->prepare($sql);
			$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
			
		}
	}			
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";

