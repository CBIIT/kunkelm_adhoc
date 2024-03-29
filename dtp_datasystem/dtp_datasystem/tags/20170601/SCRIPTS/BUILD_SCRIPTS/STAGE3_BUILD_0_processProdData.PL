#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 0, 'PrintError' => 1});

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##                      _         _                 
##  _ __ ___  _   _ ___| |_    __| |_ __ ___  _ __  
## | '_ ` _ \| | | / __| __|  / _` | '__/ _ \| '_ \ 
## | | | | | | |_| \__ \ |_  | (_| | | | (_) | |_) |
## |_| |_| |_|\__,_|___/\__|  \__,_|_|  \___/| .__/ 
##                                           |_|    
##                      _             _       _          __ _          _   _ 
##   ___ ___  _ __  ___| |_ _ __ __ _(_)_ __ | |_ ___   / _(_)_ __ ___| |_| |
##  / __/ _ \| '_ \/ __| __| '__/ _` | | '_ \| __/ __| | |_| | '__/ __| __| |
## | (_| (_) | | | \__ \ |_| | | (_| | | | | | |_\__ \ |  _| | |  \__ \ |_|_|
##  \___\___/|_| |_|___/\__|_|  \__,_|_|_| |_|\__|___/ |_| |_|_|  |___/\__(_)
## 

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## ######     #    #    #          #    #   #####  #    #   #####
## #          #     #  #           ##  ##     #     #  #      #
## #####      #      ##            # ## #     #      ##       #
## #          #      ##            #    #     #      ##       #
## #          #     #  #           #    #     #     #  #      #
## #          #    #    #          #    #     #    #    #     #

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my $tempSql = qq[
delete from prod_mtxt where mtxt like '%Please send 5 mg%'
];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##   #                                      #
##  ##              #####   ####            ##
## # #                #    #    #          # #
##   #                #    #    #            #
##   #                #    #    #            #
##   #                #    #    #            #
## #####              #     ####           #####

## these are all made to have an entry for EVERY nsc in nsc_to_load
## this will obviate the DOG SLOW updating of nsc_cmpd later
## since fk can be assigned based on nsc

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #####      #     ####     ##     ####    ####     ##     #   #
## #    #     #    #    #   #  #   #       #        #  #     # #
## #####      #    #    #  #    #   ####    ####   #    #     #
## #    #     #    #    #  ######       #       #  ######     #
## #    #     #    #    #  #    #  #    #  #    #  #    #     #
## #####      #     ####   #    #   ####    ####   #    #     #

my @sqlList = ();

push @sqlList, qq[
drop index if exists prod_count_nci60_nsc
];

push @sqlList, qq[
create index prod_count_nci60_nsc on prod_count_nci60(nsc)
];

push @sqlList, qq[
drop index if exists prod_count_hf_nsc
];

push @sqlList, qq[
create index prod_count_hf_nsc on prod_count_hf(nsc)
];


push @sqlList, qq[
drop index if exists prod_count_xeno_nsc
];

push @sqlList, qq[
create index prod_count_xeno_nsc on prod_count_xeno(nsc)
];

push @sqlList, qq[
drop table if exists prod_bio_counts
];

push @sqlList, qq[
vacuum analyze prod_count_nci60
];

push @sqlList, qq[
vacuum analyze prod_count_hf
];

push @sqlList, qq[
vacuum analyze prod_count_xeno
];

push @sqlList, qq[
create table prod_bio_counts 
as 
select 
ntl.nsc, 
CASE WHEN pc_nci60.the_count IS NULL THEN 0 ELSE pc_nci60.the_count END as prod_count_nci60,
CASE WHEN pc_hf.the_count IS NULL THEN 0 ELSE pc_hf.the_count END as prod_count_hf,
CASE WHEN pc_xeno.the_count IS NULL THEN 0 ELSE pc_xeno.the_count END as prod_count_xeno
from nsc_to_load ntl
left outer join prod_count_nci60 pc_nci60 on ntl.nsc = pc_nci60.nsc 
left outer join prod_count_hf pc_hf on ntl.nsc = pc_hf.nsc 
left outer join prod_count_xeno pc_xeno on ntl.nsc = pc_xeno.nsc
];

push @sqlList, qq[
insert into cmpd_bio_assay(id, nci60, hf, xeno)
select nsc, prod_count_nci60, prod_count_hf, prod_count_xeno 
from prod_bio_counts
]; 

#push @sqlList, qq[
#update nsc_cmpd 
#set cmpd_bio_assay_fk = cmpd_bio_assay.id 
#from cmpd_bio_assay
#where nsc_cmpd.nsc = cmpd_bio_assay.id
#];

foreach my $tempSql(@sqlList){
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
}

@sqlList = ();

## #    #    #  #    #  ######  #    #   #####   ####   #####    #   #
## #    ##   #  #    #  #       ##   #     #    #    #  #    #    # #
## #    # #  #  #    #  #####   # #  #     #    #    #  #    #     #
## #    #  # #  #    #  #       #  # #     #    #    #  #####      #
## #    #   ##   #  #   #       #   ##     #    #    #  #   #      #
## #    #    #    ##    ######  #    #     #     ####   #    #     #

push @sqlList, qq[
insert into cmpd_inventory(id, inventory)
select ntl.nsc, 
CASE WHEN inventory IS NULL THEN 0 ELSE inventory END 
from nsc_to_load ntl 
left outer join prod_inventory pi on ntl.nsc = pi.nsc
]; 

#push @sqlList, qq[
#update nsc_cmpd 
#set cmpd_inventory_fk = cmpd_inventory.id
#from cmpd_inventory
#where nsc_cmpd.nsc = cmpd_inventory.id
#];

foreach my $tempSql(@sqlList){
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
}

@sqlList = ();

##   ##    #    #  #    #   ####    #####    ##     #####     #     ####   #    #
##  #  #   ##   #  ##   #  #    #     #     #  #      #       #    #    #  ##   #
## #    #  # #  #  # #  #  #    #     #    #    #     #       #    #    #  # #  #
## ######  #  # #  #  # #  #    #     #    ######     #       #    #    #  #  # #
## #    #  #   ##  #   ##  #    #     #    #    #     #       #    #    #  #   ##
## #    #  #    #  #    #   ####      #    #    #     #       #     ####   #    #

push @sqlList, qq[
drop table if exists temp
];

push @sqlList, qq[
create table temp
as
select ntl.nsc, 
array_to_string(array_agg(labelarray), ', ') as labels
from nsc_to_load ntl
left outer join rs3_from_plp_frags frags
on ntl.nsc = frags.nsc
where labelarray is not null and length(labelarray) > 1
group by ntl.nsc
]; 

push @sqlList, qq[
insert into cmpd_annotation(
id,
general_comment,
purity_comment,
stereochemistry_comment,
mtxt,
pseudo_atoms
)
select
ntl.nsc,
null,
null,
null,
pm.mtxt,
t.labels
from nsc_to_load ntl
left outer join prod_mtxt pm on ntl.nsc = pm.nsc
left outer join temp t on ntl.nsc = t.nsc
];

#push @sqlList, qq[
#update nsc_cmpd 
#set cmpd_annotation_fk = cmpd_annotation.id 
#from cmpd_annotation
#where nsc_cmpd.nsc = cmpd_annotation.id
#];


foreach my $tempSql(@sqlList){
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
}

@sqlList = ();

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  #   #            #####   ####           #   #
##   # #               #    #    #           # #
## #######             #    #    #         #######
##   # #               #    #    #           # #
##  #   #              #    #    #          #   #
##                     #     ####

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##   ##    #          #      ##     ####   ######   ####
##  #  #   #          #     #  #   #       #       #
## #    #  #          #    #    #   ####   #####    ####
## ######  #          #    ######       #  #            #
## #    #  #          #    #    #  #    #  #       #    #
## #    #  ######     #    #    #   ####   ######   ####

push @sqlList, qq[
drop table if exists temp
];

push @sqlList, qq[
create table temp
as
select distinct chem_name_type
from prod_chem_name
where nsc in (
select nsc from nsc_to_load
)
]; 

push @sqlList, qq[
insert into cmpd_alias_type(id, alias_type)
select nextval('cmpd_alias_type_seq'), chem_name_type
from temp
];

## chem_name is NOT unique because of one-to-many from name to chem_name_type 

push @sqlList, qq[
drop table if exists temp
];

push @sqlList, qq[
create table temp
as
select 
nextval('cmpd_alias_seq') as id, 
cn.nsc, 
n.nsc as nsc_cmpds_fk, 
chem_name, 
chem_name_type, 
t.id as cmpd_alias_type_fk
from prod_chem_name cn, cmpd_alias_type t, nsc_to_load n
where cn.chem_name_type = t.alias_type
and cn.nsc = n.nsc
]; 

push @sqlList, qq[
insert into cmpd_alias(id, alias, cmpd_alias_type_fk)
select id, chem_name, cmpd_alias_type_fk 
from temp
];

push @sqlList, qq[
insert into cmpd_aliases2nsc_cmpds(nsc_cmpds_fk, cmpd_aliases_fk)
select nsc_cmpds_fk, id
from temp
];


foreach my $tempSql(@sqlList){
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
}

@sqlList = ();

## #####   #####    ####        #  ######   ####    #####   ####
## #    #  #    #  #    #       #  #       #    #     #    #
## #    #  #    #  #    #       #  #####   #          #     ####
## #####   #####   #    #       #  #       #          #         #
## #       #   #   #    #  #    #  #       #    #     #    #    #
## #       #    #   ####    ####   ######   ####      #     ####

push @sqlList, qq[
drop table if exists temp
];

push @sqlList, qq[
create table temp
as
select distinct project_code, description
from prod_projects
where nsc in (
select nsc from nsc_to_load
)
]; 

push @sqlList, qq[
insert into cmpd_project(id, project_code, project_name)
select nextval('cmpd_project_seq'), project_code, description
from temp
];

push @sqlList, qq[
drop table if exists temp
];

push @sqlList, qq[
create table temp
as
select pp.nsc, pp.project_code, nsc.nsc as nsc_cmpds_fk, cp.id as cmpd_projects_fk
from prod_projects pp, nsc_to_load nsc, cmpd_project cp
where pp.nsc = nsc.nsc
and pp.project_code = cp.project_code
]; 

push @sqlList, qq[
insert into cmpd_projects2nsc_cmpds(nsc_cmpds_fk, cmpd_projects_fk)
select nsc_cmpds_fk, cmpd_projects_fk
from temp
];


foreach my $tempSql(@sqlList){
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
}

@sqlList = ();	

## #####   #         ##     #####  ######   ####
## #    #  #        #  #      #    #       #
## #    #  #       #    #     #    #####    ####
## #####   #       ######     #    #            #
## #       #       #    #     #    #       #    #
## #       ######  #    #     #    ######   ####

push @sqlList, qq[
drop table if exists temp
];

push @sqlList, qq[
create table temp
as
select distinct plate_set
from prod_plated_sets
where plate_set is not null
and nsc in (
select nsc from nsc_to_load
)
]; 

push @sqlList, qq[
insert into cmpd_plate(id, plate_name)
select nextval('cmpd_plate_seq'), plate_set
from temp
];

push @sqlList, qq[
drop table if exists temp
];

push @sqlList, qq[
create table temp
as
select pps.nsc, pps.plate_set, n.nsc as nsc_cmpds_fk, p.id as cmpd_plates_fk
from prod_plated_sets pps, nsc_to_load n, cmpd_plate p
where pps.nsc = n.nsc
and pps.plate_set = p.plate_name
];

push @sqlList, qq[
insert into cmpd_plates2nsc_cmpds(nsc_cmpds_fk, cmpd_plates_fk)
select nsc_cmpds_fk, cmpd_plates_fk
from temp
];


foreach my $tempSql(@sqlList){
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
}

@sqlList = ();	

##  #####    ##    #####    ####   ######   #####   ####
##    #     #  #   #    #  #    #  #          #    #
##    #    #    #  #    #  #       #####      #     ####
##    #    ######  #####   #  ###  #          #         #
##    #    #    #  #   #   #    #  #          #    #    #
##    #    #    #  #    #   ####   ######     #     ####

push @sqlList, qq[
##drop table if exists temp
##];
##
##push @sqlList, qq[
##create table temp
##as
##select distinct target
##from prod_targets
##where target is not null
##and nsc in (
##select nsc from nsc_to_load
##)
##]; 
##
##push @sqlList, qq[
##insert into cmpd_target(id, target)
##select nextval('cmpd_target_seq'), target
##from temp
##];
##
##push @sqlList, qq[
##drop table if exists temp
##];
##
##push @sqlList, qq[
##create table temp
##as
##select pt.nsc, pt.target, n.id as nsc_cmpds_fk, ct.id as cmpd_targets_fk
##from prod_targets pt, nsc_cmpd n, cmpd_target ct
##where pt.nsc = n.nsc
##and pt.target = ct.target
##and pt.nsc in (
##select nsc from nsc_to_load
##)
##];
##
##push @sqlList, qq[
##insert into cmpd_targets2nsc_cmpds(nsc_cmpds_fk, cmpd_targets_fk)
##select nsc_cmpds_fk,cmpd_targets_fk
##from temp
##];
##
##
##foreach my $tempSql(@sqlList){
##	my $sth = $dbh->prepare($tempSql);
##	print "$tempSql\n\n";
##	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
##}

@sqlList = ();	

##   ####   ######   #####   ####
##  #       #          #    #
##   ####   #####      #     ####
##       #  #          #         #
##  #    #  #          #    #    #
##   ####   ######     #     ####

##push @sqlList, qq[
##drop table if exists temp
##];
##
##push @sqlList, qq[
##create table temp
##as
##select distinct plate_set
##from prod_plated_sets
##where plate_set is not null
##and nsc in (
##select nsc from nsc_to_load
##)
##]; 
##
##push @sqlList, qq[
##insert into cmpd_plate(id, plate_name)
##select nextval('cmpd_plate_seq'), plate_set
##from temp
##];
##
##push @sqlList, qq[
##drop table if exists temp
##];
##
##push @sqlList, qq[
##create table temp
##as
##select pps.nsc, pps.plate_set, n.nsc as nsc_cmpds_fk, p.id as cmpd_plates_fk
##from prod_plated_sets pps, nsc_to_load n, cmpd_plate p
##where pps.nsc = n.nsc
##and pps.plate_set = p.plate_name
##];
##
##push @sqlList, qq[
##insert into cmpd_plates2nsc_cmpds(nsc_cmpds_fk, cmpd_plates_fk)
##select nsc_cmpds_fk, cmpd_plates_fk
##from temp
##];
##
##
##foreach my $tempSql(@sqlList){
##	my $sth = $dbh->prepare($tempSql);
##	print "$tempSql\n\n";
##	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
##}
##
##@sqlList = ();	

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


