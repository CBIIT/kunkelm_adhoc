#!/usr/bin/perl

use strict;

# load module
use DBI;

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "start time $theTime\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

my @allTbls = qw(
	one_strc
	one_strc_salt
	one_lbl
	multi_lbl
	one_strc_one_lbl
	one_strc_multi_lbl
	two_strc_any_lbl_one_salt
	two_strc_any_lbl_two_salt
	two_strc_any_lbl
	multi_strc_any_lbl
	);

my $sql = qq[drop table if exists qc];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

my $sql = qq[create table qc (source varchar(1024), comparator varchar(1024), target varchar(1024), the_count int)];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

# counts from all categories

for my $cnt(0 .. @allTbls - 1){
	
	#------------
	
	my $sql = qq[drop table if exists temp];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[create table temp 
	as
	select nsc, mf, mw, sum(molecular_weight) as fw
	from $allTbls[$cnt]
	group by 1, 2, 3];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	#------------
	
	my $sql = qq[insert into qc 
	select '$allTbls[$cnt]', 'count nsc', 'count nsc', count(distinct nsc) 
	from temp];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc 
	select '$allTbls[$cnt]', 'count nsc', 'count nsc inv', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0)];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc 
	select '$allTbls[$cnt]', 'count nsc', 'count nsc nci60', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_count_nci60 where the_count > 0)];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc 
	select '$allTbls[$cnt]', 'count nsc', 'count nsc both', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0 intersect select nsc from prod_count_nci60 where the_count > 0)];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	#------------
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 0', count(distinct nsc) 
	from temp 
	where round(mw) = round(fw)];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 0 inv', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0) 
	and round(mw) = round(fw)];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 0 nci60', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0)
	and round(mw) = round(fw)];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 0 both', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0 intersect select nsc from prod_count_nci60 where the_count > 0)
	and round(mw) = round(fw)];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;
	
	#------------
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 2', count(distinct nsc) 
	from temp 
	where abs( round(mw) - round(fw) ) < 2];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 2 inv', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0)
	and abs( round(mw) - round(fw) ) < 2];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 2 nci60', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_count_nci60 where the_count > 0)
	and abs( round(mw) - round(fw) ) < 2];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 2 both', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0 intersect select nsc from prod_count_nci60 where the_count > 0)
	and abs( round(mw) - round(fw) ) < 2];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	#------------
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 5', count(distinct nsc) 
	from temp 
	where abs( round(mw) - round(fw) ) < 5];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 5 inv', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0)
	and abs( round(mw) - round(fw) ) < 5];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 5 nci60', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_count_nci60 where the_count > 0)
	and abs( round(mw) - round(fw) ) < 5];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mw DIFF 5 both', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0 intersect select nsc from prod_count_nci60 where the_count > 0)
	and abs( round(mw) - round(fw) ) < 5];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	#------------
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf W99', count(distinct nsc) 
	from temp 
	where mf  = 'W99'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf W99 inv', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0)
	and mf  = 'W99'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf W99 nci60', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_count_nci60 where the_count > 0)
	and mf  = 'W99'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf W99 both', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0 intersect select nsc from prod_count_nci60 where the_count > 0)
	and mf  = 'W99'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	#------------
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf with .', count(distinct nsc) 
	from temp 
	where mf  like '%.%'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf with . inv', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0)
	and mf  like '%.%'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf with . nci60', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_count_nci60 where the_count > 0)
	and mf  like '%.%'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf with . both', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0 intersect select nsc from prod_count_nci60 where the_count > 0)
	and mf like '%.%'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	
	#------------
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf ~ .*\(.*\)[xn0-9]', count(distinct nsc) 
	from temp 
	where mf ~ '.*\\(.*\\)[xn0-9]'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf ~ .*\(.*\)[xn0-9] inv', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0)
	and mf ~ '.*\\(.*\\)[xn0-9]'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf ~ .*\(.*\)[xn0-9] nci60', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_count_nci60 where the_count > 0) 
	and mf ~ '.*\\(.*\\)[xn0-9]'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
	my $sql = qq[insert into qc
	select '$allTbls[$cnt]', 'count nsc', 'mf ~ .*\(.*\)[xn0-9] both', count(distinct nsc) 
	from temp where nsc in (select nsc from prod_inventory where inventory > 0 intersect select nsc from prod_count_nci60 where the_count > 0)
	and mf ~ '.*\\(.*\\)[xn0-9]'];
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
}

# crosstab the results

# set up for command line

my $tblName = "qc";
my $colName = "target";
my $rowName = "source";

# get the distinct column headers

my $tempSql = qq[select distinct $colName from $tblName order by 1];

my $sth = $dbh->prepare($tempSql);

$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

my @identList;	
while(my $ref = $sth->fetchrow_hashref()) {		
	push (@identList, $ref->{$colName});
}

# overlaps of categories

my $sql = qq[drop table if exists qc_overlap];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

my $sql = qq[create table qc_overlap (source varchar(1024), target varchar(1024), the_count int)];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

for my $cnt(0 .. @allTbls - 1){	
	for my $innerCnt($cnt .. @allTbls - 1){
		
		if ($cnt != $innerCnt){
			
			my $sql = qq[
			insert into qc_overlap
			select '$allTbls[$cnt]', '$allTbls[$innerCnt]',
			count(i.*)
			from (
			select nsc from $allTbls[$cnt]
			intersect
			select nsc from $allTbls[$innerCnt]
			) as i];
			print "$sql\n";
			my $sth = $dbh->prepare($sql);
			$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
			
		}
	}			
}

# clean up
$dbh->disconnect();

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "finish time $theTime\n";

