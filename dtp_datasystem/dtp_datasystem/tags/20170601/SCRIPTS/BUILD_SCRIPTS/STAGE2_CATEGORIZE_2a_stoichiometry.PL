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

my @sqlList = ();

##               Table "public.rs3_from_plp_nsc"
##            Column            |       Type        | Modifiers 
## -----------------------------+-------------------+-----------
##  nsc                         | integer           | 
##  prod_mf                     | character varying | 
##  prod_mw                     | double precision  | 
##  formalcharge                | integer           | 
##  num_fragments               | integer           | 
##  num_metalatoms              | integer           | 
##  num_rgroupfragments         | integer           | 
##  canonical_smiles            | character varying | 
##  molecular_formula_fragments | character varying | 
##  molecular_formula           | character varying | 
##  molecular_weight            | double precision  |                                    

push @sqlList, qq[
  drop table if exists problem_mf
];

push @sqlList, qq[
create table problem_mf 
as
select null::varchar as problem, nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments
from rs3_from_plp_nsc
limit 0
];

# null prod_mf
push @sqlList, qq[
insert into problem_mf(problem, nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments)
select 'null prod_mf', nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments
from rs3_from_plp_nsc
where
prod_mf is null
];

# decimal stoichiometry
push @sqlList, qq[
insert into problem_mf(problem, nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments)
select 'decimal stoichiometry', nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments
from rs3_from_plp_nsc
where
prod_mf is not null
and (
prod_mf ~ '\\.0\\.'
or
prod_mf ~ '^0\\.'
)
];

# fractional stoichiometry
push @sqlList, qq[
insert into problem_mf(problem, nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments)
select 'fractional stoichiometry', nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments
from rs3_from_plp_nsc
where
prod_mf is not null
and prod_mf ~ '\\d+/\\d+'
];

# )x or )n
push @sqlList, qq[
insert into problem_mf(problem, nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments)
select 'x or n', nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments
from rs3_from_plp_nsc
where
prod_mf is not null
and (
prod_mf ~ '\\)x'
or
prod_mf ~ '\\)n'
)
];

# ignoring W99 for now since it is tied to count_lbs, count_strc
# push @sqlList, qq[
# insert into problem_mf(problem, nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments)
# select 'W99', nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments
# from rs3_from_plp_nsc
# where
# prod_mf is not null
# and prod_mf ~ 'W99'
# ];

push @sqlList, qq[
drop table if exists good_mf
];

push @sqlList, qq[
create table good_mf 
as
select nsc, prod_mf, prod_mw, molecular_formula, molecular_formula_fragments, molecular_weight, num_fragments,
0::int as count_mf_frags, 'f'::boolean as has_stoichiometry
from rs3_from_plp_nsc
where nsc not in (
  select nsc from problem_mf
)
];

# count frags by .
push @sqlList, qq[
update good_mf
set count_mf_frags = array_length(string_to_array(prod_mf, '.'), 1)
where prod_mf is not null
];

# flag mf with stoichiometry
push @sqlList, qq[
update good_mf
set has_stoichiometry = 't'
where prod_mf is not null
and( 
prod_mf ~ '^\\d+'
or
prod_mf ~ '\\.\\d+'
)
];

my $adHocSql = qq[

-------------------------------------------------------------------
-------------------------------------------------------------------
-------------------------------------------------------------------
-------------------------------------------------------------------

select nsc, prod_mf
from good_mf
where prod_mf is null
limit 10;

select count(*)
from good_mf
where prod_mf is not null
and count_mf_frags != num_fragments;

select nsc, prod_mf, molecular_formula, count_mf_frags, num_fragments
from good_mf
where prod_mf is not null
and count_mf_frags != num_fragments;

select nsc, prod_mf
from good_mf
where has_stoichiometry = 't'
limit 50;

select '-->'||mf||'<--', count(*) 
from frags_stoich  
group by 1 order by 2 desc;

-------------------------------------------------------------------
-------------------------------------------------------------------
-------------------------------------------------------------------
-------------------------------------------------------------------

];

# extract array of possible stoichiometry values

push @sqlList, qq[
drop table if exists frags_stoich
];

push @sqlList, qq[
create table frags_stoich
as
select nsc, prod_mf, regexp_split_to_table(prod_mf, E'\\\\.') as frag, null::int as stoich, null::varchar as mf
from good_mf
where has_stoichiometry = 't'
];

my $adHocSql = qq[
  select mf, count(*)
  from frags_stoich
  where stoich is not null
  group by mf
  order by count(*) desc
];

push @sqlList, qq[
update frags_stoich
set stoich = (regexp_matches(frag, E'^([0-9]+)(.*)'))[1]::int,
  mf = (regexp_matches(frag, E'^([0-9]+)(.*)'))[2]
];

foreach my $sql(@sqlList){
	print "$sql\n";
	my $sth = $dbh->prepare($sql);
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";