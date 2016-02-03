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
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #####   #####   ######            ##     ####    ####
## #    #  #    #  #                #  #   #    #  #    #
## #    #  #    #  #####   #####   #    #  #       #
## #####   #####   #               ######  #  ###  #  ###
## #       #   #   #               #    #  #    #  #    #
## #       #    #  ######          #    #   ####    ####

# statements in sqlList

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##    ##    #       #               ######  #####     ##     ####    ####
##   #  #   #       #               #       #    #   #  #   #    #  #
##  #    #  #       #               #####   #    #  #    #  #        ####
##  ######  #       #               #       #####   ######  #  ###       #
##  #    #  #       #               #       #   #   #    #  #    #  #    #
##  #    #  ######  ######          #       #    #  #    #   ####    ####

my @sqlList;

push @sqlList,qq[
drop table if exists count_frags
];

push @sqlList,qq[
create table count_frags
as
select nsc, mf as prod_mf, mw as prod_mw, count(*) as count_frags, array_to_string(array_agg(molecular_formula), '.') as mf, sum(molecular_weight) as fw
from rs3_from_plp_frags
group by nsc, mf, mw
];

push @sqlList,qq[
create index cf_nsc on count_frags(nsc)
];

##  #       #####   #        ####
##  #       #    #  #       #
##  #       #####   #        ####
##  #       #    #  #            #
##  #       #    #  #       #    #
##  ######  #####   ######   ####

push @sqlList,qq[
drop table if exists count_lbl_frags
];

push @sqlList,qq[
create table count_lbl_frags
as
select nsc, mf as prod_mf, mw as prod_mw, count(*) as count_frags
from rs3_from_plp_frags
where labelarray is not null
group by nsc, mf, mw
];

push @sqlList,qq[
create index clf_nsc on count_lbl_frags(nsc)
];

##   ####    #####  #####    ####
##  #          #    #    #  #    #
##   ####      #    #    #  #
##       #     #    #####   #
##  #    #     #    #   #   #    #
##   ####      #    #    #   ####


push @sqlList,qq[
drop table if exists count_strc_frags
];

push @sqlList,qq[
create table count_strc_frags
as
select nsc, mf as prod_mf, mw as prod_mw, count(*) as count_frags
from rs3_from_plp_frags
where can_smi is not null and labelarray is null
group by nsc, mf, mw
];

push @sqlList,qq[
create index csf_nsc on count_strc_frags(nsc)
];

##  #####            ####   #####    ####   #    #  #####    ####
##  #    #          #    #  #    #  #    #  #    #  #    #  #
##  #    #          #       #    #  #    #  #    #  #    #   ####
##  #####           #  ###  #####   #    #  #    #  #####        #
##  #   #           #    #  #   #   #    #  #    #  #       #    #
##  #    #           ####   #    #   ####    ####   #        ####

push @sqlList,qq[
drop table if exists count_rgrp_frags
];

push @sqlList,qq[
create table count_rgrp_frags
as
select nsc, mf as prod_mf, mw as prod_mw, count(*) as count_frags
from rs3_from_plp_frags
where can_smi like '%[R]%' and labelarray is null
group by nsc, mf, mw
];

push @sqlList,qq[
create index crf_nsc on count_rgrp_frags(nsc)
];

##   ####     ##    #        #####   ####
##  #        #  #   #          #    #
##   ####   #    #  #          #     ####
##       #  ######  #          #         #
##  #    #  #    #  #          #    #    #
##   ####   #    #  ######     #     ####

push @sqlList,qq[
drop table if exists count_salt_frags
];

push @sqlList,qq[
create table count_salt_frags
as
select nsc, mf as prod_mf, mw as prod_mw, count(*) as count_frags
from rs3_from_plp_frags
where salt_smiles is not null
group by nsc, mf, mw
];

push @sqlList,qq[
create index csaltf_nsc on count_salt_frags(nsc)
];

# run the sql statements

foreach my $sqlStmt(@sqlList){
	my $sth = $dbh->prepare($sqlStmt);
	print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	
}

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##   ####    ####   #       #         ##     #####  ######
##  #    #  #    #  #       #        #  #      #    #
##  #       #    #  #       #       #    #     #    #####
##  #       #    #  #       #       ######     #    #
##  #    #  #    #  #       #       #    #     #    #
##   ####    ####   ######  ######  #    #     #    ######
## 
## 
##  #####    #   #          #    #   ####    ####
##  #    #    # #           ##   #  #       #    #
##  #####      #            # #  #   ####   #
##  #    #     #            #  # #       #  #
##  #    #     #            #   ##  #    #  #    #
##  #####      #            #    #   ####    ####

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

@sqlList = ();

push @sqlList,qq[
drop table if exists problem_nsc
];

push @sqlList,qq[
create table problem_nsc
as
select
f.nsc, f.prod_mf, f.prod_mw, f.mf, f.fw,
f.count_frags as count_frags,
lbl.count_frags as count_lbl,
strc.count_frags as count_strc,
(lbl.count_frags + strc.count_frags) as total_type_frags
from
count_frags f
left outer join count_lbl_frags lbl on f.nsc = lbl.nsc
left outer join count_strc_frags strc on f.nsc = strc.nsc
left outer join count_salt_frags salt on f.nsc = salt.nsc
where f.count_frags != lbl.count_frags + strc.count_frags
];

push @sqlList,qq[
create index pn_nsc on problem_nsc(nsc)
];

push @sqlList,qq[
drop table if exists frag_stats
];

push @sqlList,qq[
create table frag_stats
as
select
f.nsc, f.prod_mf, f.prod_mw, f.mf, f.fw,
CASE when f.count_frags is null THEN 0 ELSE f.count_frags END as count_frags,
CASE when lbl.count_frags is null THEN 0 ELSE lbl.count_frags END as count_lbl,
CASE when strc.count_frags is null THEN 0 ELSE strc.count_frags END as count_strc,
CASE when salt.count_frags is null THEN 0 ELSE salt.count_frags END as salt
from
count_frags f
left outer join count_lbl_frags lbl on f.nsc = lbl.nsc
left outer join count_strc_frags strc on f.nsc = strc.nsc
left outer join count_salt_frags salt on f.nsc = salt.nsc
];

push @sqlList,qq[
create index fs_nsc on frag_stats(nsc)
];

push @sqlList,qq[
drop table if exists frag_stats_with_totals
];

push @sqlList,qq[
create table frag_stats_with_totals
as
select
*,
(count_lbl + count_strc) as total_type_frags
from frag_stats
];

push @sqlList,qq[
create index fswt_nsc on frag_stats_with_totals(nsc)
];

foreach my $sqlStmt (@sqlList){
	my $sth = $dbh->prepare($sqlStmt);
	print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	
}

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  #####   #####    ####    ####   ######   ####    ####
##  #    #  #    #  #    #  #    #  #       #       #
##  #    #  #    #  #    #  #       #####    ####    ####
##  #####   #####   #    #  #       #            #       #
##  #       #   #   #    #  #    #  #       #    #  #    #
##  #       #    #   ####    ####   ######   ####    ####
## 
## 
##   ####     ##     #####  ######   ####    ####   #####      #    ######   ####
##  #    #   #  #      #    #       #    #  #    #  #    #     #    #       #
##  #       #    #     #    #####   #       #    #  #    #     #    #####    ####
##  #       ######     #    #       #  ###  #    #  #####      #    #            #
##  #    #  #    #     #    #       #    #  #    #  #   #      #    #       #    #
##   ####   #    #     #    ######   ####    ####   #    #     #    ######   ####

# MWK 25Jan2016, ALL categories into a single table

@sqlList = ();

# drop/create table

push @sqlList,qq[
drop table if exists comb_cat
];

push @sqlList,qq[
create table comb_cat
as
select 'category'::varchar as category, rs3.*
from rs3_from_plp_frags rs3
limit 0
];

my @infoList = getCategoryCriteriaList();

# insert for each category

foreach my $thisInfo (@infoList){
	
  print $thisInfo . "\n";
  
  my ($category, $count_frags, $count_lbl, $count_strc,	$count_salt) = split(/ /, $thisInfo);
  
  push @sqlList,qq[
  insert into comb_cat
  select '$category', rs3.*
  from rs3_from_plp_frags rs3, frag_stats_with_totals fs
  where fs.count_frags = fs.total_type_frags
  and rs3.nsc = fs.nsc  
  and fs.count_frags $count_frags
  and fs.count_lbl $count_lbl
  and fs.count_strc $count_strc
  and fs.salt $count_salt
  ];  
}

push @sqlList,qq[
create index comb_cat_category on comb_cat(category)
];

push @sqlList,qq[
create index comb_cat_nsc on comb_cat(nsc)
];

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

## #    #     #     ####    ####      #    #    #   ####
## ##  ##     #    #       #          #    ##   #  #    #
## # ## #     #     ####    ####      #    # #  #  #
## #    #     #         #       #     #    #  # #  #  ###
## #    #     #    #    #  #    #     #    #   ##  #    #
## #    #     #     ####    ####      #    #    #   ####

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

@sqlList = ();

push @sqlList,qq[
drop table if exists missing_nsc
];

push @sqlList,qq[
create table missing_nsc
as
select frag.*
from frag_stats_with_totals frag
where nsc not in (
select distinct nsc from comb_cat
)
];

push @sqlList,qq[
drop table if exists missing_nsc_frags
];

push @sqlList,qq[
create table missing_nsc_frags
as
select *
from rs3_from_plp_frags
where nsc in (
select nsc from missing_nsc
)
];

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

