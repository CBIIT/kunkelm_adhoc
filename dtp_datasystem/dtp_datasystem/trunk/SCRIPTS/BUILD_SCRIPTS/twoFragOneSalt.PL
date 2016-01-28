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

my @sqlList;

push @sqlList, qq[
drop table if exists temp_agg
];

push @sqlList, qq[
create table temp_agg 
as
select 
nsc, 
array_to_string(array_agg(molecular_formula), '.') as molecular_formula, 
sum(molecular_weight) as formula_weight, 
sum(formalcharge) as formal_charge,
array_to_string(array_agg(formalcharge), ',') as formal_charges
from comb_cat
where category like '%two_strc_one_salt'
and molecular_formula is not null
group by nsc
];

push @sqlList, qq[
create index temp_agg_nsc on temp_agg(nsc)
];

push @sqlList, qq[
drop table if exists temp
];

push @sqlList, qq[
create table temp 
as
select 
ta.nsc, 
ta.molecular_formula, 
ta.formula_weight, 
rs3.mf as prod_mf, 
rs3.mw as prod_fw, 
ta.formula_weight - rs3.mw as diff_mw, 
rs3.formalcharge as formal_charge,
ta.formal_charges
from temp_agg ta 
  left outer join rs3_from_plp_nsc rs3 on ta.nsc = rs3.nsc
  left outer join rs3_from_plp_nsc frags on ta.nsc = frags.nsc
];

push @sqlList, qq[
drop table if exists hal_nak_salts
];

push @sqlList, qq[
create table hal_nak_salts(
diff_mw double precision,
formal_charge int, 
all_salts_count int,
halogen_salts_count int,
na_k_salts_count int)
];

# execute

foreach my $sqlStmt (@sqlList){
	my $sth = $dbh->prepare($sqlStmt);
	print "$sqlStmt\n\n";
	$sth->execute() or die "Couldn't execute statement: $sqlStmt " . $sth->errstr;	
}

#       _ _                   _   _           _                            
#  __ _| | |   __ _ _ __   __| | | |__   __ _| | ___   __ _  ___ _ __  ___ 
# / _` | | |  / _` | '_ \ / _` | | '_ \ / _` | |/ _ \ / _` |/ _ \ '_ \/ __|
#| (_| | | | | (_| | | | | (_| | | | | | (_| | | (_) | (_| |  __/ | | \__ \
# \__,_|_|_|  \__,_|_| |_|\__,_| |_| |_|\__,_|_|\___/ \__, |\___|_| |_|___/
#                                                     |___/                

# cycle through a series of diff_mw

my @mwList = (0.2, 0.4, 0.6, 0.8, 1.0, 1.2, 1.4, 1.6, 1.8, 2.0);

@sqlList = ();

for my $thisMW(@mwList){

push @sqlList, qq[
insert into hal_nak_salts
select 
$thisMW,
all_salts.formal_charge, 
all_salts."count" as all_salts_count, 
halogen_salts."count" as halogen_salts_count,
na_k_salts."count" as na_k_salts_count

from

(select 
formal_charge, 
count(*)
from temp
where diff_mw between -$thisMW and $thisMW
group by 
formal_charge 
order by 
formal_charge 
) as all_salts

left outer join

(select 
formal_charge, 
count(*)
from temp
where diff_mw between -$thisMW and $thisMW
and prod_mf ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH|HCl\.|ClH\.|HBr\.|BrH\.|HI\.|IH)'
group by 
formal_charge 
order by 
formal_charge 
) as halogen_salts

on all_salts.formal_charge = halogen_salts.formal_charge

left outer join

(select 
formal_charge, 
count(*)
from temp
where diff_mw between -$thisMW and $thisMW
and prod_mf ~ '(\.Na|\.K|Na\.|K\.)'
group by 
formal_charge
order by 
formal_charge
) as na_k_salts

on all_salts.formal_charge = na_k_salts.formal_charge;
];
}

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

