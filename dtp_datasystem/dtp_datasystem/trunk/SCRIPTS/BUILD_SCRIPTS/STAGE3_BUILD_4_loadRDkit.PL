#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');
use DATA_SYSTEM_BUILD_PARAMETERS qw(getDatasystemTableList getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

use DBI;

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

my @sqlList = ();

goto skipToHere;

push @sqlList, qq[
drop table if exists rdkit_validity
];

push @sqlList, qq[
create table rdkit_validity 
as 
select nsc, 
is_valid_smiles(can_smi::cstring) as valid_can_smi, 
is_valid_smiles(can_taut::cstring) as valid_can_taut,
is_valid_smiles(can_taut_strip_stereo::cstring) as valid_can_taut_strip_stereo,
is_valid_ctab(ctab::cstring) as valid_ctab
from cmpd_table
where can_smi is not null 
];

push @sqlList, qq[
create index rdkv_nsc on rdkit_validity(nsc)
];

push @sqlList, qq[
vacuum analyze rdkit_validity
];

skipToHere:

# drop and recreate the table
# the datasystem uml model doesn't know about rdkit mol data type
# so the uml entity maps to String
# "select mol" returns a SMILES cstring; so, this should be hibnerate-friendly
# if ever required

push @sqlList, qq[
drop table if exists rdkit_mol
];

push @sqlList, qq[
create table rdkit_mol(
id bigint,
nsc int,
mol mol,
mol_from_ctab mol)
];

push @sqlList, qq[
insert into rdkit_mol(id, nsc, mol, mol_from_ctab)
select
ct.nsc, ct.nsc, mol_from_smiles(ct.can_taut::cstring), mol_from_ctab(ctab::cstring)
from cmpd_table ct, rdkit_validity v
where ct.nsc = v.nsc
and v.valid_can_taut = 't'
and v.valid_ctab = 't'
];

push @sqlList, qq[
create index rdkit_mol_mol on rdkit_mol using gist(mol)
];

push @sqlList, qq[
create index rdkit_mol_mol_from_ctab on rdkit_mol using gist(mol_from_ctab)
];

push @sqlList, qq[
drop table if exists rdkit_fail
];


push @sqlList, qq[
create table rdkit_fail 
as 
select ct.nsc, ct.can_smi, parent_molecular_formula
from cmpd_table ct, rdkit_validity v 
where ct.nsc = v.nsc
and v.valid_can_smi = 'f'
];

push @sqlList, qq[
create index rdkf_nsc on rdkit_fail(nsc)
];

push @sqlList, qq[
drop table if exists rdkit_diff
];

push @sqlList, qq[
create table rdkit_diff 
as 
select mol, mol_from_ctab 
from rdkit_mol 
where mol != mol_from_ctab
];

foreach my $sql(@sqlList){
	my $sth = $dbh->prepare($sql);
	print "$sql\n\n";
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	
}

my $sql = qq[
select valid_can_smi, valid_can_taut, valid_can_taut_strip_stereo, valid_ctab, count(*) 
from rdkit_validity 
group by 1, 2, 3, 4 order by 5 desc
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

my $valCanSmi; my $validCanTaut; my $validCanTautStripStereo;
$sth -> bind_columns(\$valCanSmi, \$validCanTaut, \$validCanTautStripStereo);
print "valCanSmi validCanTaut validCanTautStripStereo\n";
while ($sth->fetch()){
	print "$valCanSmi $validCanTaut $validCanTautStripStereo\n";	
}


#drop table if exists rdkit_fps;

#create table rdkit_fps(
#id bigint,
#atompairfp bfp,
#featmorganfp bfp,
#morganfp bfp,
#torsionfp bfp
#);

#insert into rdkit_fps
#select 
#id, 
#atompairbv_fp(mol),
#featmorganbv_fp(mol),
#morganbv_fp(mol),
#torsionbv_fp(mol)
#from rdkit_mol;

#select nsc,
#tanimoto_dist(atompairbv_fp('c1ccc2c(c1)c(cn2Cc3ccccc3)CO'), rdkit_fps.atompairfp) as atompair,
#tanimoto_dist(featmorganbv_fp('c1ccc2c(c1)c(cn2Cc3ccccc3)CO'), rdkit_fps.featmorganfp) as featmorgan,
#tanimoto_dist(morganbv_fp('c1ccc2c(c1)c(cn2Cc3ccccc3)CO'), rdkit_fps.morganfp) as morgan,
#tanimoto_dist(torsionbv_fp('c1ccc2c(c1)c(cn2Cc3ccccc3)CO'), rdkit_fps.torsionfp) as torsion
#from rdkit_fps, rdkit_mol 
#where rdkit_fps.id = rdkit_mol.id
#limit 10;

#set rdkit.tanimoto_threshold = 0.8;

#select rdkit_mol.nsc 
#from rdkit_fps, rdkit_mol 
#where morganbv_fp('c1ccc2c(c1)c(cn2Cc3ccccc3)CO',2) % rdkit_fps.morganfp 
#and 
#rdkit_fps.id = rdkit_mol.id;

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";
print "finish: $finishDate\n";
