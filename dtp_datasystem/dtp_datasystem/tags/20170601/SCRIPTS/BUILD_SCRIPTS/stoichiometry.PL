STAGE 2 2a stoichiometry

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

# fractional stoichiometries as decimal value

select nsc, prod_mf, molecular_formula, molecular_formula_fragments 
from rs3_from_plp_nsc
where 
prod_mf ~ E'^0\\.\\d+'
or
prod_mf ~ E'\\.0\\.\\d+'
or
prod_mf ~ E'\\.\\.'
order by nsc

# fractional stoichiometries as fractional value

select nsc, prod_mf, molecular_formula, molecular_formula_fragments 
from rs3_from_plp_nsc
where 
prod_mf ~ E'^\\d+/\\d+'
or
prod_mf ~ E'\\.\\d+/\\d+'
order by nsc






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



my $sql = qq[
drop table if exists frags_from_mf
];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

my $sql = qq[
create table frags_from_prod_mf
as
select nsc, prod_mf, prod_mw, mf, fw, fc, mf_neut, fw_neut, fc_neut,
count_frags,count_lbl,lbls,count_strc,count_rgrp_frags,salt
from frag_stats
];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

# flag decimal stoichiometry values which all seem to start with a digit or leading 0
my $sql = qq[
update frags_from_prod_mf
set prod_mf = regexp_replace(prod_mf, E'\\d+\\.', 'decimal')
];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

# count frags by .
my $sql = qq[
update frags_from_prod_mf
set count_mf_frags = (char_length(prod_mf) - char_length(replace(prod_mf, '\.', ''))) / char_length('\.');
];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

# extract array of possible stoichiometry values


my $sql = qq[
update frags_from_prod_mf
set

regexp_split_to_array(prod_mf, E'\\.'


];
print "$sql\n";
my $sth = $dbh->prepare($sql);
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;


# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";
