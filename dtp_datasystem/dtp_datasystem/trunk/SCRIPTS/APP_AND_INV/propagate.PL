#!/usr/bin/perl

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use lib('./');

use DBI;

sub ltrim { my $s = shift; $s =~ s/^\s+//;       return $s };
sub rtrim { my $s = shift; $s =~ s/\s+$//;       return $s };
sub  trim { my $s = shift; $s =~ s/^\s+|\s+$//g; return $s };

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "--start: $startDate\n";

my @info = qw(
DBI:Pg:dbname=sarcomadb;host=localhost mwkunkel donkie11 compound,nscIdent
DBI:Pg:dbname=sclcdb;host=localhost mwkunkel donkie11 compound,nscIdent
DBI:Pg:dbname=datasystemdb;host=localhost mwkunkel donkie11 nsc_cmpd
DBI:Pg:dbname=sarcomacomparedb;host=localhost mwkunkel donkie11 syntheticIdent
);

my $i;
my $j;

my $db;
my $u;
my $pw;
my $tbls;

$i = 0;
$j = $i + 3;
while ($i < $#info){

$db = $info[$i];
$u = $info[$i + 1];
$pw = $info[$i + 2];
$tbls = $info[$i + 3];

print "$i $db $u $pw $tbls\n";

$i = $i + 3;

}


my @sqlList = ();
my $sql;

#  ####   ######   #####  #    #  #####
# #       #          #    #    #  #    #
#  ####   #####      #    #    #  #    #
#      #  #          #    #    #  #####
# #    #  #          #    #    #  #
#  ####   ######     #     ####   #

# connect
my $dbh = DBI->connect("DBI:Pg:dbname=oncologydrugsdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

# create app_and_inv_smiles

push @sqlList, qq[
drop table if exists app_and_inv_smiles
];

push @sqlList, qq[
create table app_and_inv_smiles 
as
select nsc.nsc, gnam.value as generic_name, pnam.value as preferred_name, trg.value as primary_target, ct.can_smi as smiles
from curated_nsc nsc
left outer join curated_name gnam on nsc.generic_name_fk = gnam.id
left outer join curated_name pnam on nsc.preferred_name_fk = pnam.id
left outer join curated_target trg on nsc.primary_target_fk = trg.id
left outer join cmpd_table ct on nsc.nsc = ct.nsc
];

foreach $sql(@sqlList){
	my $sth = $dbh->prepare($sql);
	print "$sql\n\n";
	$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;		
}

# using list of lists

$sql = qq[select nsc, generic_name, preferred_name, primary_target, smiles from app_and_inv_smiles];
my $sth = $dbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

my $lol = $sth->fetchall_arrayref();

# clean up
$dbh->disconnect();

# target of update

#$dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});
#$dbh = DBI->connect("DBI:Pg:dbname=smallcell;host=ncidb-d115-d;port=5473", "sclc", "DPt09232Sm2LL204Ung", {'RaiseError' => 1});
#$dbh = DBI->connect("DBI:Pg:dbname=sarcoma;host=ncidb-d115-d;port=5473", "sarcomadev", "sar20920384220dev", {'RaiseError' => 1});
#$dbh = DBI->connect("DBI:Pg:dbname=oncology;host=ncidb-d115-d;port=5474", "oncology", "OnC0L029302802K1t", {'RaiseError' => 1});

$dbh = DBI->connect("DBI:Pg:dbname=sarcomacomparedb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

#my $sql = qq[
#update compound
#set drug_name = ?,
#target = ?,
#smiles = ?
#where nsc = ?
#];

my $sql = qq[
update synthetic_ident si
set drug_name = ?,
target = ?,
smiles = ?
from nsc_ident ni
where si.id = ni.id 
and ni.nsc = ?
];

my $sth = $dbh->prepare($sql);
print "$sql\n\n";
	
my $nsc;
my $genericName;
my $preferredName;
my $primaryTarget;
my $smiles;

my $drugName;

for my $array_ref ( @$lol ) {
	
	($nsc, $genericName, $preferredName, $primaryTarget, $smiles) = @$array_ref;
	
	if (defined($genericName)){
		$drugName = $genericName;
	} elsif (defined($preferredName)){
		$drugName = $preferredName;
	} else {
		undef $drugName;
	}
	
$sth->execute($drugName, $primaryTarget, $smiles, $nsc) or die "Couldn't execute statement: $sql " . $sth->errstr;	
	
}

# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "--start : $startDate\n";
print "--finish: $finishDate\n";

