#!/usr/bin/perl

use strict;
use warnings;


use DBI;

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "start time $theTime\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

## #     #         #     #         #    #          #######
## ##    #         #     #         #   #           #      
## # #   #         #     #         #  #            #      
## #  #  #         #     #         ###             #####  
## #   # #         #     #         #  #            #
## #    ##         #     #         #   #           #      
## #     #          #####          #    #          #######

my @tablesToNuke = (
	"count_frags",
	"count_lbl_frags",
	"count_strc_frags",
	"count_salt_frags",
	"problem_nsc",
	"frag_stats",
	"frag_stats_with_totals",
	"one_strc",
	"one_strc_salt",
	"one_lbl",
	"multi_lbl",
	"one_strc_one_lbl",
	"one_strc_multi_lbl",
        "two_strc_no_lbl_one_salt",
	"two_strc_any_lbl_one_salt",
	"two_strc_any_lbl_two_salt",
	"two_strc_any_lbl",
	"multi_strc_any_lbl",
	"missing_nsc"); 

foreach my $tableToNuke(@tablesToNuke){
	
	my $tempSql = qq[drop table if exists $tableToNuke cascade];
	my $sth = $dbh->prepare($tempSql);
	print "$tempSql\n\n";
	$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;	
	
}

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####    ####   #    #  #    #   #####   ####           #####    #   #
## #    #  #    #  #    #  ##   #     #    #               #    #    # #
## #       #    #  #    #  # #  #     #     ####           #####      #
## #       #    #  #    #  #  # #     #         #          #    #     #
## #    #  #    #  #    #  #   ##     #    #    #          #    #     #
##  ####    ####    ####   #    #     #     ####           #####      #
##
##
## #    #   ####    ####
## ##   #  #       #    #
## # #  #   ####   #
## #  # #       #  #
## #   ##  #    #  #    #
## #    #   ####    ####

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## all frags

my $tempSql = qq[drop table if exists count_frags];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table count_frags
as
select nsc, mf as prod_mf, mw as prod_mw, count(*) as count_frags, array_to_string(array_agg(molecular_formula), '.') as mf, sum(molecular_weight) as fw
from rs3_from_plp_frags
group by nsc, mf, mw];

my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index cf_nsc on count_frags(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;


## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## frags that are lbl

my $tempSql = qq[drop table if exists count_lbl_frags];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table count_lbl_frags
as
select nsc, mf as prod_mf, mw as prod_mw, count(*) as count_frags
from rs3_from_plp_frags
where can_smi = '[RH]' and atomarray is not null
group by nsc, mf, mw];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index clf_nsc on count_lbl_frags(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## frags that are strc

my $tempSql = qq[drop table if exists count_strc_frags];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table count_strc_frags
as
select nsc, mf as prod_mf, mw as prod_mw, count(*) as count_frags
from rs3_from_plp_frags
where can_smi != '[RH]' and atomarray is null
group by nsc, mf, mw];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index csf_nsc on count_strc_frags(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## frags that are salts

my $tempSql = qq[drop table if exists count_salt_frags];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table count_salt_frags
as
select nsc, mf as prod_mf, mw as prod_mw, count(*) as count_frags
from rs3_from_plp_frags
where salt_smiles is not null
group by nsc, mf, mw];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index csaltf_nsc on count_salt_frags(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####    ####   #       #         ##     #####  ######          #####    #   #
## #    #  #    #  #       #        #  #      #    #               #    #    # #
## #       #    #  #       #       #    #     #    #####           #####      #
## #       #    #  #       #       ######     #    #               #    #     #
## #    #  #    #  #       #       #    #     #    #               #    #     #
##  ####    ####   ######  ######  #    #     #    ######          #####      #
##
##
## #    #   ####    ####
## ##   #  #       #    #
## # #  #   ####   #
## #  # #       #  #
## #   ##  #    #  #    #
## #    #   ####    ####


## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## nsc that are going to fail the count_frags = count_type_frags

my $tempSql = qq[drop table if exists problem_nsc];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
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

where f.count_frags != lbl.count_frags + strc.count_frags];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index pn_nsc on problem_nsc(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## collate the stats by nsc

my $tempSql = qq[drop table if exists frag_stats];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
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
left outer join count_salt_frags salt on f.nsc = salt.nsc];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index fs_nsc on frag_stats(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## do the count_frags and counts by cateory jibe?

my $tempSql = qq[drop table if exists frag_stats_with_totals];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table frag_stats_with_totals
as
select
*,
(count_lbl + count_strc) as total_type_frags
from frag_stats];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index fswt_nsc on frag_stats_with_totals(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## inspect failures
## select * from frag_stats_with_totals where count_frags != total_type_frags];

##  nsc   |               prod_mf                | prod_mw |                mf                 |     fw     | count_frags | count_lbl | count_strc | salt | total_type_frags
##--------+--------------------------------------+---------+-----------------------------------+------------+-------------+-----------+------------+------+------------------
##  99294 | C4H12N.C4H22B18Fe                    |     395 | C4H12N.Fe                         |  129.98978 |           7 |         1 |          2 |    1 |                3
##  99296 | C4H12N.C16H30B18Fe                   |     547 | Fe.C4H12N                         |  129.98978 |           5 |         1 |          2 |    1 |                3
## 150494 | C6H6NO6.4H.3Hg.2O.C6H6NO6            | 1014.03 | C6H6NO6.Hg.H.C6H6NO6.O.H.H.H      |  596.85104 |          13 |         5 |          4 |    1 |                9
## 236522 | C5H9NO2S.C5H9NO2S.C5H9NO2S.2Pt.2H    |  833.77 | Pt.C5H9NO2S.C5H9NO2S.H.C5H9NO2S.H |  638.68026 |          10 |         4 |          4 |    0 |                8
## 247554 | C6H17N2O9PPt                         |     487 | H.H3N.H.H3N.Pt.C6H11O9P           |  489.27482 |           7 |         1 |          4 |    2 |                5
## 265468 | C27H29NO11.H.H.5Fe.17Cl.C27H29NO11   |    1971 | Fe.H.Cl.C27H29NO11.H.C27H29NO11   | 1180.35238 |           9 |         3 |          4 |    2 |                7
## 267618 | C4H12CuN10.H2O4S                     |     362 | H2O4S.Cu.C4H11N8                  |  332.80821 |           6 |         1 |          3 |    2 |                4
## 276298 | C25H20Cl2N25Pd3S5.H                  |    1222 | Cl.Cl.Pd.Pd.C5H4N5S.H.Pd          |   557.3577 |           8 |         1 |          6 |    2 |                7
## 618654 | C2H4Cl3O2Te.H.H3N                    |     312 | H3N.C2H7Cl3O2Te.H                 |  315.07323 |           3 |         0 |          2 |    1 |                2
## 620257 | C23H28N3O.H.Cl4Pt                    |     700 | Cl4H4Pt.C23H28N3O.H               |  704.41761 |           3 |         0 |          2 |    0 |                2
## 623326 | C32H40O8.C33H42O8.C34H44O8           |    1700 | C34H44O8.C32H40O8.C32H40O8        | 1686.01873 |           7 |         3 |          3 |    0 |                6
## 626904 | C19H14AuClO2P.H                      |     539 | H.C19H16AuClO2P                   |  540.73039 |           2 |         0 |          1 |    0 |                1
## 628213 | C15H11N2O6Pt.H                       |     511 | H.C15H11N2O6Pt                    |  511.34357 |           2 |         0 |          1 |    0 |                1
## 628216 | C16H13N2O6Pt.H                       |     525 | C16H13N2O6Pt.H                    |  525.37015 |           2 |         0 |          1 |    0 |                1
## 628220 | C16H13N2O6Pt.H                       |     525 | C16H13N2O6Pt.H                    |  525.37015 |           2 |         0 |          1 |    0 |                1
## 631192 | C16H18O3                             |  258.32 | C16H16O3                          |  256.29644 |           3 |         0 |          1 |    0 |                1
## 631477 | C16H12Cl2N2O8Ru.C6H6ClN.H            |     661 | H.C6H6ClN.C16H12Cl2N2O8Ru         |  660.83055 |           3 |         0 |          2 |    0 |                2
## 631480 | C12H14Cl4N2Ru.H.C6H7N                |     523 | C6H7N.H.C12H18Cl4N2Ru             |  527.30113 |           3 |         0 |          2 |    1 |                2
## 633158 | C44H24MnN4O12S4.4H                   |     988 | C44H26MnN4O12S4.H                 |  986.90282 |           2 |         0 |          1 |    0 |                1
## 633162 | C48H24AuN4O8.2Na.H                   |    1029 | Na.C48H26AuN4O8.H                 |  1007.7063 |           3 |         0 |          2 |    1 |                2
## 633163 | C44H48InN4O12S4.Cl.4H                |    1107 | Cl.C44H50InN4O12S4.H              | 1106.42634 |           3 |         0 |          2 |    1 |                2
## 649277 | C45H82O12S.Na                        |     870 | Na.C45H78O12S                     |  866.14839 |           6 |         0 |          2 |    1 |                2
## 649278 | C45H86O10S.Na                        |  842.22 | Na.C45H82O10S                     |  838.18135 |           6 |         0 |          2 |    1 |                2
## 663767 | C38H33CoN8O8S2.H                     |     854 | C38H33CoN8O8S2.H                  |  853.78856 |           2 |         0 |          1 |    0 |                1
## 666158 | C14H12Cl4N4Ru.H.C7H6N2               |     598 | C7H6N2.C14H16Cl4N4Ru.H            |  602.32952 |           3 |         0 |          2 |    0 |                2
## 667096 | C12H11NO9Ti.H                        |     362 | C6H4NO2.OTi.C6H8O6                |  362.09197 |           4 |         0 |          3 |    2 |                3
## 667100 | C12H12O13V.2Na                       |     461 | C6H8O6.OV.Na                      |  266.05479 |           4 |         0 |          3 |    2 |                3
## 667101 | C34H34N16O16Ti2.2Na                  |    1064 | Na.C7H8N4O2.C6H8O6.OTi            |   443.1443 |           5 |         0 |          4 |    2 |                4
## 683677 | C22 H42 Cl2 Co N4 O4 . 3 Cl O4 . 2 H |     857 | ClO4.H.H.C22H42Cl2CoN4O4          |  657.89895 |           4 |         0 |          2 |    1 |                2
## 687936 | C23H22CoN8O.H                        |     486 | H.C23H21CoN8O                     |  485.40698 |           2 |         0 |          1 |    0 |                1
## 691974 | C40H38Ag2N6O14P2.6H                  |    1111 | C40H40Ag2N6O14P2.H                | 1107.46926 |           3 |         1 |          1 |    0 |                2
## 695782 | C6H13Cl2N2PtO2.H                     |     412 | H.C6H15Cl2N2O2Pt                  |  414.18743 |           2 |         0 |          1 |    0 |                1
## 715816 | W99                                  |         |                                   |            |           6 |         4 |          0 |    0 |                4
## 723129 | C14H28N2O6Sb.H                       |  443.14 | C14H28N2O6Sb.H                    |  443.14985 |           3 |         1 |          1 |    0 |                2
##(34 rows)

## inspect for missing

## select count(*) from rs3_from_plp_frags;
## select count(distinct nsc) from rs3_from_plp_frags;
## select count(*) from frag_stats;
## select count(*) from frag_stats_with_totals;

## inspect frag categories

## select count_frags, count_lbl, count_strc, salt, total_type_frags, count(*)
## from frag_stats_with_totals
## group by 1, 2, 3, 4, 5
##order by 1, 2, 3;

## count_frags | count_lbl | count_strc | salt | total_type_frags | count
##-------------+-----------+------------+------+------------------+--------
##           1 |         0 |          1 |    0 |                1 | 593585 <<<<<<<<<<<<<<<
##           1 |         0 |          1 |    1 |                1 |   1038 <<<<<<<<<<<<<<<
##           1 |         1 |          0 |    0 |                1 |  44064 <<<<<<<<<<<<<<<
##           2 |         0 |          1 |    0 |                1 |      8
##           2 |         0 |          2 |    0 |                2 |   6142 <<<<<<<<<<<<<<<
##           2 |         0 |          2 |    1 |                2 |  69151 <<<<<<<<<<<<<<<
##           2 |         0 |          2 |    2 |                2 |    253 <<<<<<<<<<<<<<<
##           2 |         1 |          1 |    0 |                2 |   1252
##           2 |         1 |          1 |    1 |                2 |     17
##           2 |         2 |          0 |    0 |                2 |    782
##           3 |         0 |          1 |    0 |                1 |      1
##           3 |         0 |          2 |    0 |                2 |      3
##           3 |         0 |          2 |    1 |                2 |      4
##           3 |         0 |          3 |    0 |                3 |    214
##           3 |         0 |          3 |    1 |                3 |    176
##           3 |         0 |          3 |    2 |                3 |    590
##           3 |         0 |          3 |    3 |                3 |      8
##           3 |         1 |          1 |    0 |                2 |      2
##           3 |         1 |          2 |    0 |                3 |    571
##           3 |         1 |          2 |    1 |                3 |   8510
##           3 |         1 |          2 |    2 |                3 |    160
##           3 |         2 |          1 |    0 |                3 |    205
##           3 |         2 |          1 |    1 |                3 |      4
##           3 |         3 |          0 |    0 |                3 |    313
##

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####   #    #  ######           ####    #####  #####    ####
## #    #  ##   #  #               #          #    #    #  #    #
## #    #  # #  #  #####            ####      #    #    #  #
## #    #  #  # #  #                    #     #    #####   #
## #    #  #   ##  #               #    #     #    #   #   #    #
##  ####   #    #  ######           ####      #    #    #   ####

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## single fragment structures

my $tempSql = qq[drop table if exists one_strc];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table one_strc
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc

and fs.count_frags = 1
and fs.count_lbl = 0
and fs.count_strc = 1
and fs.salt = 0
and fs.total_type_frags = 1];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index os_nsc on one_strc(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####   #    #  ######           ####    #####  #####    ####
## #    #  ##   #  #               #          #    #    #  #    #
## #    #  # #  #  #####            ####      #    #    #  #
## #    #  #  # #  #                    #     #    #####   #
## #    #  #   ##  #               #    #     #    #   #   #    #
##  ####   #    #  ######           ####      #    #    #   ####
##
##
##  ####     ##    #        #####
## #        #  #   #          #
##  ####   #    #  #          #
##      #  ######  #          #
## #    #  #    #  #          #
##  ####   #    #  ######     #

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## single fragment structures that are salts
## these MAY BE salts of something weird that got lost in parsing

my $tempSql = qq[drop table if exists one_strc_salt];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table one_strc_salt
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc

and fs.count_frags = 1
and fs.count_lbl = 0
and fs.count_strc = 1
and fs.salt = 1
and fs.total_type_frags = 1];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index oss_nsc on one_strc_salt(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####   #    #  ######          #       #####   #
## #    #  ##   #  #               #       #    #  #
## #    #  # #  #  #####           #       #####   #
## #    #  #  # #  #               #       #    #  #
## #    #  #   ##  #               #       #    #  #
##  ####   #    #  ######          ######  #####   ######

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my $tempSql = qq[drop table if exists one_lbl];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table one_lbl
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc

and fs.count_frags = 1
and fs.count_lbl = 1
and fs.count_strc = 0
and fs.salt = 0
and fs.total_type_frags = 1];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index ol_nsc on one_lbl(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## inspect labels

## select atomarray, count(*)
## from one_lbl
## group by 1
## order by 2 desc;

##                                   atomarray                                   | count
## ------------------------------------------------------------------------------+-------
##  \NStructure Not Available                                                    |  5792
##  CODE No. ONLY                                                                |   227
##  Structure  Not  Known                                                        |   211
##  Structure  Not  Available                                                    |   109
##  \NStructure Not Known                                                        |    66
##  \NPseudopentapetide library                                                  |    48
##  B~Number Transfer                                                            |    27
##  \NR' = H or -CH2Ph                                                           |    27
##  CODE NO. ONLY                                                                |    25
##  \NPurified Interferon Species                                                |    18
##  \Npurified interferon species                                                |    18
##  Inga densiflora extract fraction                                             |    15
##  Diethylaminoethyldextran hydrochloride                                       |    14
##  \NFraction from the plant St. John's wort                                    |    12
##  \NAurintricarboxylic Acid (ATA) fraction                                     |    12
##  \NSulfated polysaccharide                                                    |     8
##  From marine animal LISSODENDORYX ISODICTYALIS                                |     6
##  Lymphokine derivative                                                        |     6
##  Unknown compound                                                             |     5
##  B~No. ONLY                                                                   |     5
##  Plant extract                                                                |     5
##  \NCitorellamine Derivative                                                   |     5
##  Polysaccharide                                                               |     5
##  From PIMELEA PROSTRATA                                                       |     5
##  No Structure                                                                 |     5
##  Code No. ONLY                                                                |     5
##  \NFraction from plant St John's wort                                         |     5
##  Modified DIVEMA copolymer                                                    |     4
##  \NFraction from Artemisia annua                                              |     4
##  B No. ONLY                                                                   |     4
##  Unknown protein of PALYTHOA SP.                                              |     4
##  Daunorubicin derivative                                                      |     4
##  \NIndividual species of IFN-.alpha. or control                               |     4
##  NO STRUCTURE AVAILABLE                                                       |     4
##  Pyran copolymer                                                              |     4
##  \NPurified interferon species                                                |     4
##  \NRT Peptide                                                                 |     4

## inspect possible categories

## select mf, mw, atomarray
## from one_lbl
## where atomarray like '%rotei%';

## select mf, mw, atomarray
## from one_lbl
## where atomarray ilike '%MW=%';

## select mf, mw, atomarray
## from one_lbl
## where atomarray ilike '%epti%';

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #    #  #    #  #        #####     #            #       #####   #
## ##  ##  #    #  #          #       #            #       #    #  #
## # ## #  #    #  #          #       #            #       #####   #
## #    #  #    #  #          #       #            #       #    #  #
## #    #  #    #  #          #       #            #       #    #  #
## #    #   ####   ######     #       #            ######  #####   ######

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my $tempSql = qq[drop table if exists multi_lbl];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table multi_lbl
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc
and fs.count_frags > 1
and fs.count_frags = fs.count_lbl
and fs.count_frags = fs.total_type_frags];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index ml_nsc on multi_lbl(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## inspect aggregated labels

## drop table if exists temp;

## create table temp
## as
## select nsc, array_to_string(array_agg(atomarray), ',') as labels
## from multi_lbl
## group by 1;

## select labels, count(*)
## from temp
## group by 1
## order by 2 desc;

##                                      labels                                      | count
##----------------------------------------------------------------------------------+-------
## (,),n                                                                            |    17
## synthetic intermediate,  Dolastatin 3,  CODE No. ONLY                            |    16
## n,),(                                                                            |     9
## \NX  = H , Me , Et,\N9 compounds,\NRACEMIC                                       |     9
## from Combretum caffrum, B~Number transfer                                        |     8
## Polysaccharide,B NUMBER                                                          |     7
## \N9 compounds,\NRACEMIC,\NX  = CH2CN, CH2CO2H, CH2CONH2                          |     6
## \N7776 compounds,\NXxx is an equimolar mixture of,\NTyr, Leu, Lys, Arg, Glu, Ser |     5
## \N1296 compounds,\NXxx is an equimolar mixture of,\NTyr, Leu, Lys, Arg, Glu, Ser |     5
## \N9 compounds,\NRACEMIC,\NX  = -CH2CN, -CH2CO2H, -CH2CONH2                       |     5
## (From Cephalodiscus gilchristi),   B~Number transfer                             |     5
## \N9 compounds,\NRacemic,\NX  = CH2CN, CH2CO2H, CH2CONH2                          |     4
## Protein from STRONGYLOCENTROTUS DROBACHIENSIS,B No. ONLY                         |     4
## \N216 compounds,\NXxx is an equimolar mixture of,\NTyr, Leu, Lys, Arg, Glu, Ser  |     4
## \NX  = H , Me , Et,\N9 compounds,\NRacemic                                       |     4
## Anthraline derivative,  CODE No. ONLY                                            |     4
##  DOLABELLA ECAUDATA,Unknown compound from                                        |     4
## From DOLABELLA ECAUDATA,B No. ONLY                                               |     3
##       form number 884.,Structure deleted according to correction                 |     3
##    MF is C10H16N4O2Pt,Structure Diagram Not Available                            |     3
##      MF is Cl.W99,Structure Diagram Not Available                                |     3
##     MF is (C5H8O2)n,Structure Diagram Not Available                              |     3
##     MF is C37H50O9,Structure Diagram Not Available                               |     3
## CATHARANTHUS ROSEUS alkaloid,    B No. ONLY                                      |     3
##      MF is W99.W99,Structure Diagram Not Available                               |     3
##    MF is C13H24N2O7Pt,Structure Diagram Not Available                            |     2
## Available,Not,Diagram,C114H142N22O73P10.H3N,is,MF,Structure                      |     2
## From DOLABELLA ECAUDATA,   B No. ONLY                                            |     2
##    MF is C10H10Mn2N4S8,Structure Diagram Not Available                           |     2
## Quinocycline and/or Isoquinocycline,        W99                                  |     2
## Pd(II) complex,CODE No. ONLY                                                     |     2
##      alcohol extract,Polysaccharides of methanol~bacteria                        |     2
## Available,Not,C10H14Fe2N4S8,Diagram,is,MF,Structure                              |     2
##     MF is C17H22O5,Structure Diagram Not Available                               |     2
## Steroid from STICHOPUS CHLORONOTUS,B No. ONLY                                    |     2
##     MF is C6H10Ge2O7,Structure Diagram Not Available                             |     2
## Anthrone derivative,  CODE No. ONLY                                              |     2
## from Bugula neritina, B~Number Transfer                                          |     2
## Retinoic acid analog,  CODE No. ONLY                                             |     2
## Available,Not,C20H26O4,is,Diagram,MF,Structure                                   |     2

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####   #    #  ######           ####    #####  #####    ####
## #    #  ##   #  #               #          #    #    #  #    #
## #    #  # #  #  #####            ####      #    #    #  #
## #    #  #  # #  #                    #     #    #####   #
## #    #  #   ##  #               #    #     #    #   #   #    #
##  ####   #    #  ######           ####      #    #    #   ####
##
##
##  ####   #    #  ######          #       #####   #
## #    #  ##   #  #               #       #    #  #
## #    #  # #  #  #####           #       #####   #
## #    #  #  # #  #               #       #    #  #
## #    #  #   ##  #               #       #    #  #
##  ####   #    #  ######          ######  #####   ######

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my $tempSql = qq[drop table if exists one_strc_one_lbl];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table one_strc_one_lbl
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc
and fs.count_frags = 2
and fs.count_lbl = 1
and fs.count_strc = 1
and fs.total_type_frags = 2];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index osol_nsc on one_strc_one_lbl(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

##  ####   #    #  ######           ####    #####  #####    ####
## #    #  ##   #  #               #          #    #    #  #    #
## #    #  # #  #  #####            ####      #    #    #  #
## #    #  #  # #  #                    #     #    #####   #
## #    #  #   ##  #               #    #     #    #   #   #    #
##  ####   #    #  ######           ####      #    #    #   ####
##
##
## #    #  #    #  #        #####     #            #       #####   #
## ##  ##  #    #  #          #       #            #       #    #  #
## # ## #  #    #  #          #       #            #       #####   #
## #    #  #    #  #          #       #            #       #    #  #
## #    #  #    #  #          #       #            #       #    #  #
## #    #   ####   ######     #       #            ######  #####   ######

my $tempSql = qq[drop table if exists one_strc_multi_lbl];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table one_strc_multi_lbl
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc
and fs.count_lbl > 1
and fs.count_strc = 1];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index osml_nsc on one_strc_multi_lbl(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------


  #####  #    #   ####            ####    #####  #####    ####
    #    #    #  #    #          #          #    #    #  #    #
    #    #    #  #    #           ####      #    #    #  #
    #    # ## #  #    #               #     #    #####   #
    #    ##  ##  #    #          #    #     #    #   #   #    #
    #    #    #   ####            ####      #    #    #   ####


 #    #   ####           #       #####   #
 ##   #  #    #          #       #    #  #
 # #  #  #    #          #       #####   #
 #  # #  #    #          #       #    #  #
 #   ##  #    #          #       #    #  #
 #    #   ####           ######  #####   ######


  ####   #    #  ######           ####     ##    #        #####
 #    #  ##   #  #               #        #  #   #          #
 #    #  # #  #  #####            ####   #    #  #          #
 #    #  #  # #  #                    #  ######  #          #
 #    #  #   ##  #               #    #  #    #  #          #
  ####   #    #  ######           ####   #    #  ######     #

my $tempSql = qq[drop table if exists two_strc_no_lbl_one_salt];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table two_strc_no_lbl_one_salt
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc
and fs.count_strc = 2
and fs.count_frags = 2
and fs.count_lbl = 0
and fs.salt = 1];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index tsnlos_nsc on two_strc_no_lbl_one_salt(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #####  #    #   ####            ####    #####  #####    ####
##   #    #    #  #    #          #          #    #    #  #    #
##   #    #    #  #    #           ####      #    #    #  #
##   #    # ## #  #    #               #     #    #####   #
##   #    ##  ##  #    #          #    #     #    #   #   #    #
##   #    #    #   ####            ####      #    #    #   ####
##
##   ##    #    #   #   #          #       #####   #
##  #  #   ##   #    # #           #       #    #  #
## #    #  # #  #     #            #       #####   #
## ######  #  # #     #            #       #    #  #
## #    #  #   ##     #            #       #    #  #
## #    #  #    #     #            ######  #####   ######
##
##  ####   #    #  ######           ####     ##    #        #####
## #    #  ##   #  #               #        #  #   #          #
## #    #  # #  #  #####            ####   #    #  #          #
## #    #  #  # #  #                    #  ######  #          #
## #    #  #   ##  #               #    #  #    #  #          #
##  ####   #    #  ######           ####   #    #  ######     #

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my $tempSql = qq[drop table if exists two_strc_any_lbl_one_salt];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table two_strc_any_lbl_one_salt
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc
and fs.count_strc = 2
and fs.count_lbl > 0
and fs.salt = 1];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index tsalos_nsc on two_strc_any_lbl_one_salt(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #####  #    #   ####            ####    #####  #####    ####
##   #    #    #  #    #          #          #    #    #  #    #
##   #    #    #  #    #           ####      #    #    #  #
##   #    # ## #  #    #               #     #    #####   #
##   #    ##  ##  #    #          #    #     #    #   #   #    #
##   #    #    #   ####            ####      #    #    #   ####
##
##
##  #####  #    #   ####            ####     ##    #        #####
##    #    #    #  #    #          #        #  #   #          #
##    #    #    #  #    #           ####   #    #  #          #
##    #    # ## #  #    #               #  ######  #          #
##    #    ##  ##  #    #          #    #  #    #  #          #
##    #    #    #   ####            ####   #    #  ######     #


## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my $tempSql = qq[drop table if exists two_strc_any_lbl_two_salt];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table two_strc_any_lbl_two_salt
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc
and fs.count_strc = 2
and fs.salt = 2];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index tsalts_nsc on two_strc_any_lbl_two_salt(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #####  #    #   ####            ####    #####  #####    ####
##   #    #    #  #    #          #          #    #    #  #    #
##   #    #    #  #    #           ####      #    #    #  #
##   #    # ## #  #    #               #     #    #####   #
##   #    ##  ##  #    #          #    #     #    #   #   #    #
##   #    #    #   ####            ####      #    #    #   ####
##
##
##    ##    #    #   #   #          #       #####   #
##   #  #   ##   #    # #           #       #    #  #
##  #    #  # #  #     #            #       #####   #
##  ######  #  # #     #            #       #    #  #
##  #    #  #   ##     #            #       #    #  #
##  #    #  #    #     #            ######  #####   ######
## 
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my $tempSql = qq[drop table if exists two_strc_any_lbl];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table two_strc_any_lbl
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc
and fs.count_strc = 2
and fs.salt = 0];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index tsal_nsc on two_strc_any_lbl(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

## #    #  #    #  #        #####     #            ######  #####     ##     ####
## ##  ##  #    #  #          #       #            #       #    #   #  #   #    #
## # ## #  #    #  #          #       #            #####   #    #  #    #  #
## #    #  #    #  #          #       #            #       #####   ######  #  ###
## #    #  #    #  #          #       #            #       #   #   #    #  #    #
## #    #   ####   ######     #       #            #       #    #  #    #   ####

## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------
## ---------------------------------------------------------

my $tempSql = qq[drop table if exists multi_strc_any_lbl];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table multi_strc_any_lbl
as
select rs3.*
from rs3_from_plp_frags rs3, frag_stats_with_totals fs
where rs3.nsc = fs.nsc
and fs.count_strc > 2];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create index mf_nsc on multi_strc_any_lbl(nsc)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

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

my $tempSql = qq[drop table if exists missing_nsc];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

my $tempSql = qq[
create table missing_nsc
as
select rs3.*
from rs3_from_plp_frags rs3
where nsc not in (
select distinct x.nsc
from
(
select nsc from one_strc
UNION
select nsc from one_strc_salt
UNION
select nsc from multi_strc_any_lbl
UNION
select nsc from multi_lbl
UNION
select nsc from one_lbl
UNION
select nsc from one_strc_one_lbl
UNION
select nsc from one_strc_multi_lbl
UNION
select nsc from two_strc_no_lbl_one_salt
UNION
select nsc from two_strc_any_lbl_one_salt
UNION
select nsc from two_strc_any_lbl_two_salt
UNION
select nsc from two_strc_any_lbl
)
as x
)];
my $sth = $dbh->prepare($tempSql);
print "$tempSql\n\n";
$sth->execute() or die "Couldn't execute statement: $tempSql " . $sth->errstr;

## inspect the frag_stats for only the missing nsc

## select count_frags, count_lbl, count_strc, salt, total_type_frags, count(*)
## from frag_stats_with_totals
## where nsc in (select nsc from missing_nsc)
## group by 1, 2, 3, 4, 5
## order by 1, 2, 3;

## count_frags | count_lbl | count_strc | salt | total_type_frags | count 
##-------------+-----------+------------+------+------------------+-------
##           2 |         0 |          1 |    0 |                1 |     8
##           3 |         0 |          1 |    0 |                1 |     1
##           3 |         1 |          1 |    0 |                2 |     2
##           6 |         4 |          0 |    0 |                4 |     1
##(4 rows)
##

## sum is 12

## select count(distinct nsc) from missing_nsc;

## count
##-------
##    12

# clean up
$dbh->disconnect();

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "finish time $theTime\n";














