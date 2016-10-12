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

# execute

foreach my $sqlStmt (@sqlList){
	my $sth = $dbh->prepare($sqlStmt);
	print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	
}




        Table "public.wrangling_with_nsc"
      Column      |       Type        | Modifiers 
------------------+-------------------+-----------
 nsc              | integer           | 
 prod_mf          | character varying | 
 prod_mw          | double precision  | 
 mf               | text              | 
 fw               | double precision  | 
 formal_charge    | bigint            | 
 count_frags      | bigint            | 
 count_lbl        | bigint            | 
 lbls             | text              | 
 count_strc       | bigint            | 
 salt             | bigint            | 
 total_type_frags | bigint            | 
 inventory        | double precision  | 
 prod_count_nci60 | integer           | 
 norm_errors      | text              | 
 diff_mw          | double precision  | 

 
 drop table if exists wrangle_categories;
 
create table wrangle_categories
as
select 'category'::varchar as category, *
from wrangling_with_nsc
limit 0;

insert into wrangle_categories
select 'one_lbl_only', *
from wrangling_with_nsc
where count_lbl = 1
and count_strc = 0;

insert into wrangle_categories
	select 'multi_lbl_only', *
 from wrangling_with_nsc
where count_lbl > 1
and count_strc = 0;

insert into wrangle_categories
select 'one_strc', * 
from wrangling_with_nsc
where count_strc = 1
and salt = 0;

insert into wrangle_categories
select 'two_strc_one_salt', *
from wrangling_with_nsc
where count_strc = 2
and salt = 1;

select
category, count(*)
from wrangle_categories
where 
(diff_mw is null or abs(diff_mw) < 1.0)
and (norm_errors is null or norm_errors = 'NoStructure')
and (formal_charge is null or formal_charge = 0)
group by category;




datasystemdb=# select
datasystemdb-# category, count(*)
datasystemdb-# from wrangle_categories group by 1;
     category      | count  
-------------------+--------
 one_lbl_only      |  43948
 multi_lbl_only    |   1372
 one_strc          | 603515
 two_strc_one_salt |  81509
(4 rows)



datasystemdb=# 
datasystemdb=# select
datasystemdb-# category, count(*)
datasystemdb-# from wrangle_categories
datasystemdb-# where 
datasystemdb-# (diff_mw is null or abs(diff_mw) < 1.0)
datasystemdb-# and (norm_errors is null or norm_errors = 'NoStructure')
datasystemdb-# group by category;
     category      | count  
-------------------+--------
 one_lbl_only      |  43629
 multi_lbl_only    |    538
 one_strc          | 589542
 two_strc_one_salt |  60524
(4 rows)

datasystemdb=# 
datasystemdb=# select
datasystemdb-# category, count(*)
datasystemdb-# from wrangle_categories
datasystemdb-# where 
datasystemdb-# (diff_mw is null or abs(diff_mw) < 2.0)
datasystemdb-# and (norm_errors is null or norm_errors = 'NoStructure')
datasystemdb-# and (formal_charge is null or formal_charge = 0)
datasystemdb-# group by category;
     category      | count  
-------------------+--------
 one_lbl_only      |  43629
 multi_lbl_only    |    537
 one_strc          | 588462
 two_strc_one_salt |  20027  << formal 
(4 rows)




















# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";