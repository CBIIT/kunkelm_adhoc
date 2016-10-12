#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use DATA_SYSTEM_BUILD_PARAMETERS qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});

my @allCategories = getCategoryList();
my @paramsList = getQualityControlCriteriaList();


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
##                       _ _   _                        
##  __ _  ___  __      _(_) |_| |__      _ __  ___  ___ 
## / _` |/ __| \ \ /\ / / | __| '_ \    | '_ \/ __|/ __|
##| (_| | (__   \ V  V /| | |_| | | |   | | | \__ \ (__ 
## \__, |\___|___\_/\_/ |_|\__|_| |_|___|_| |_|___/\___|
##    |_|   |_____|                |_____|    


my $sql = qq[
drop table if exists qc_with_nsc
];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

my $sql = qq[
create table qc_with_nsc (source varchar(1024), comparator varchar(1024), target varchar(1024), nsc int, invTest varchar(1024))
];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

##  __                                 _     
## / _|  ___   _ __  ___   __ _   ___ | |__  
##| |_  / _ \ | '__|/ _ \ / _` | / __|| '_ \ 
##|  _|| (_) || |  |  __/| (_| || (__ | | | |
##|_|   \___/ |_|   \___| \__,_| \___||_| |_|
##                                           
##             _                                   
##  ___  __ _ | |_  ___   __ _   ___   _ __  _   _ 
## / __|/ _` || __|/ _ \ / _` | / _ \ | '__|| | | |
##| (__| (_| || |_|  __/| (_| || (_) || |   | |_| |
## \___|\__,_| \__|\___| \__, | \___/ |_|    \__, |
##                       |___/               |___/ 

# MWK 25Jan2016, ALL category data (frags) now in a single table

# inv, nci60, etc. 

my @critList;
push @critList,"inv"; 
push @critList,"nci60";
push @critList,"inv&nci60";

foreach my $thisCategory (@allCategories){

# MWK 22Jan2015
# first, aggregate plp fragments for mf, fw and then 
# join to mw, mf, formalcharge from rs3_from_plp_nsc
	
	my $sql = qq[	
	drop table if exists temp_agg
	];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
  my $sql = qq[
	create table temp_agg 
	as
	select nsc, array_to_string(array_agg(mf), '.') as mf, sum(mw) as fw, array_to_string(array_agg(mf_neut), '.') as mf_neut, sum(mw_neut) as fw_neut
	from comb_cat where category = '$thisCategory'
	group by 1
	];
  my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[	
	drop table if exists temp
	];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

	my $sql = qq[
	create table temp 
	as
	select ta.nsc, ta.mf, ta.fw, rs3.prod_mf, rs3.prod_mw, rs3.molecular_formula, rs3.molecular_weight, rs3.formalcharge as formal_charge
	from temp_agg ta 
  left outer join rs3_from_plp_nsc rs3
  on ta.nsc = rs3.nsc;
	];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	foreach my $thisParams(@paramsList){
		
		my ($paramName,$logicClause) = split(/,/,$thisParams);
		
		##  __ _ _ __  _   _   _ __  ___  ___ 
		## / _` | '_ \| | | | | '_ \/ __|/ __|
		##| (_| | | | | |_| | | | | \__ \ (__ 
		## \__,_|_| |_|\__, | |_| |_|___/\___|
		##             |___/                  
		
		my $sql = qq[
		insert into qc_with_nsc(source, comparator, target, nsc, invTest) 
		select distinct '$thisCategory', 'count nsc', '$paramName', nsc, 'any nsc' 
		from temp
		where $logicClause
		];
		print "$sql\n";
		my $sth = $dbh->prepare($sql);
		$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
		
		## _                      _                   
		##(_)_ ____   _____ _ __ | |_ ___  _ __ _   _ 
		##| | '_ \ \ / / _ \ '_ \| __/ _ \| '__| | | |
		##| | | | \ V /  __/ | | | || (_) | |  | |_| |
		##|_|_| |_|\_/ \___|_| |_|\__\___/|_|   \__, |
		##                                      |___/
		
		my $sql = qq[
		insert into qc_with_nsc(source, comparator, target, nsc, invTest) 
		select distinct '$thisCategory', 'count nsc', '$paramName', nsc, 'inventory'
		from temp 
		where nsc in (select nsc from prod_inventory where inventory > 0)
		and $logicClause
		];
		print "$sql\n";
		my $sth = $dbh->prepare($sql);
		$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
		
		##            _  __    ___  
		## _ __   ___(_)/ /_  / _ \ 
		##| '_ \ / __| | '_ \| | | |
		##| | | | (__| | (_) | |_| |
		##|_| |_|\___|_|\___/ \___/ 
		##
		
		my $sql = qq[
		insert into qc_with_nsc(source, comparator, target, nsc, invTest) 
		select distinct '$thisCategory', 'count nsc', '$paramName', nsc, 'nci60' 
		from temp 
		where nsc in (select nsc from prod_count_nci60 where the_count > 0)
		and $logicClause
		];
		print "$sql\n";
		my $sth = $dbh->prepare($sql);
		$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
		
		## _               ___                _  __    ___  
		##(_)_ ____   __  ( _ )    _ __   ___(_)/ /_  / _ \ 
		##| | '_ \ \ / /  / _ \/\ | '_ \ / __| | '_ \| | | |
		##| | | | \ V /  | (_>  < | | | | (__| | (_) | |_| |
		##|_|_| |_|\_/    \___/\/ |_| |_|\___|_|\___/ \___/ 
		##                                                  
		
		my $sql = qq[
		insert into qc_with_nsc(source, comparator, target, nsc, invTest) 
		select distinct '$thisCategory', 'count nsc', '$paramName', nsc, 'inv&nci60'
		from temp 
		where nsc in (select nsc from prod_inventory where inventory > 0 intersect select nsc from prod_count_nci60 where the_count > 0)
		and $logicClause
		];
		print "$sql\n";
		my $sth = $dbh->prepare($sql);
		$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
		
	}
	
}

# indexes

my @sqlList;

push @sqlList, qq[
create index qc_with_nsc_source on qc_with_nsc(source)
];

push @sqlList, qq[
create index qc_with_nsc_comparator on qc_with_nsc(comparator)
];

push @sqlList, qq[
create index qc_with_nsc_target on qc_with_nsc(target)
];

push @sqlList, qq[
vacuum analyze qc_with_nsc
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