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
select nsc, prod_mf, prod_mw, count(*) as count_frags, array_to_string(array_agg(mf), '.') as mf, sum(mw) as fw, sum(fc) as fc, array_to_string(array_agg(mf_neut), '.') as mf_neut, sum(mw_neut) as fw_neut, sum(fc_neut) as fc_neut
from rs3_from_plp_frags
group by nsc, prod_mf, prod_mw
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
select nsc, prod_mf, prod_mw, count(*) as count_frags, array_to_string(array_agg(labelarray), ',') as lbls
from rs3_from_plp_frags
where labelarray is not null
group by nsc, prod_mf, prod_mw
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
select nsc, prod_mf, prod_mw, count(*) as count_frags
from rs3_from_plp_frags
where can_smi is not null and labelarray is null
group by nsc, prod_mf, prod_mw
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
select nsc, prod_mf, prod_mw, count(*) as count_frags
from rs3_from_plp_frags
where can_smi like '%[R]%' and labelarray is null
group by nsc, prod_mf, prod_mw
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
select nsc, prod_mf, prod_mw, count(*) as count_frags
from rs3_from_plp_frags
where salt_smiles is not null
group by nsc, prod_mf, prod_mw
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
f.nsc, f.prod_mf, f.prod_mw, f.mf, f.fw, f.fc, f.mf_neut, f.fw_neut, f.fc_neut, f.count_frags as count_frags,
lbl.count_frags as count_lbl,
lbl.lbls, 
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
f.nsc, f.prod_mf, f.prod_mw, f.mf, f.fw, f.fc, f.mf_neut, f.fw_neut, f.fc_neut,
CASE when f.count_frags is null THEN 0 ELSE f.count_frags END as count_frags,
CASE when lbl.count_frags is null THEN 0 ELSE lbl.count_frags END as count_lbl,
lbl.lbls,
CASE when strc.count_frags is null THEN 0 ELSE strc.count_frags END as count_strc,
CASE when rgrp.count_frags is null THEN 0 ELSE rgrp.count_frags END as count_rgrp_frags,
CASE when salt.count_frags is null THEN 0 ELSE salt.count_frags END as salt
from
count_frags f
left outer join count_lbl_frags lbl on f.nsc = lbl.nsc
left outer join count_strc_frags strc on f.nsc = strc.nsc
left outer join count_rgrp_frags rgrp on f.nsc = rgrp.nsc
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

##------------------------------------------------------------------------------
##------------------------------------------------------------------------------
##------------------------------------------------------------------------------
##------------------------------------------------------------------------------
##------------------------------------------------------------------------------
##------------------------------------------------------------------------------
##------------------------------------------------------------------------------
##------------------------------------------------------------------------------

@sqlList = ();

push @sqlList,qq[
drop table if exists norm_errors
];

push @sqlList,qq[
drop table if exists temp_norm
];

push @sqlList,qq[
create table temp_norm
as
select nsc, 
array_to_string(array_agg(distinct('badVal:'||badvalenceatoms)), ',') as bv,
array_to_string(array_agg(distinct('nrmErr:'||normalizationerrors)), ',') as ne,
array_to_string(array_agg(distinct('regErr:'||registercheckerrors)), ',') as re
from rs3_from_plp_frags
where badvalenceatoms is not null
or normalizationerrors is not null
or registercheckerrors is not null
group by nsc;
];

push @sqlList,qq[
update temp_norm set bv = null where length(bv) = 0
];

push @sqlList,qq[
update temp_norm set ne = null where length(ne) = 0
];

push @sqlList,qq[
update temp_norm set re = null where length(re) = 0
];

push @sqlList,qq[
create table norm_errors
as
select nsc, 
coalesce(bv,'')||coalesce(ne||',','')||coalesce(re,'') as norm_errors
from temp_norm
];

push @sqlList,qq[
drop table if exists prod_bio_counts
];

push @sqlList,qq[
create table prod_bio_counts 
as 
select 
rs3_from_plp_nsc.nsc, 
prod_count_nci60.the_count as prod_count_nci60, 
prod_count_hf.the_count as prod_count_hf, 
prod_count_xeno.the_count as prod_count_xeno 
from rs3_from_plp_nsc 
left outer join prod_count_nci60 on rs3_from_plp_nsc.nsc = prod_count_nci60.nsc 
left outer join prod_count_hf on rs3_from_plp_nsc.nsc = prod_count_hf.nsc 
left outer join prod_count_xeno on rs3_from_plp_nsc.nsc = prod_count_xeno.nsc
];

push @sqlList,qq[
drop table if exists wrangling_with_nsc
];

push @sqlList,qq[
create table wrangling_with_nsc
as
select fswt.*, inv.inventory, bio.prod_count_nci60, nerr.norm_errors, prod_mw - fw as diff_mw, prod_mw - fw_neut as diff_neut_mw
from frag_stats_with_totals fswt
left outer join prod_inventory inv on fswt.nsc = inv.nsc 
left outer join prod_bio_counts bio on fswt.nsc = bio.nsc
left outer join norm_errors nerr on fswt.nsc = nerr.nsc
];

push @sqlList,qq[
create index wrang_nsc on wrangling_with_nsc(nsc)
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

#select nsc, prod_mf, mf, count_frags, total_type_frags from missing_nsc order by 1 asc;
#select nsc, fragmentindex, prod_mf, molecular_formula, can_smi, errordetails from missing_nsc_frags;

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

