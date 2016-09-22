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

my ($sourcedb, $sourcehost, $sourceuser, $sourcepw, $targetdb, $targethost, $targetuser, $targetpw, $cmpdOrSynthIdent) = @ARGV;

if (not defined $sourcedb or not defined $sourcehost or not defined $sourceuser or not defined $sourcepw or not defined $targetdb or not defined $targethost or not defined $targetuser or not defined $targetpw or not defined $cmpdOrSynthIdent){
        print "Expected arguments: <sourcedb> <sourcehost> <sourceuser> <sourcepw> <targetdb> <targethost> <targetuser> <targetpw> <cmpdOrSynthIdent as compound or syntheticIdent>\n"; 
	die "Expected arguments: <sourcedb> <sourcehost> <sourceuser> <sourcepw> <targetdb> <targethost> <targetuser> <targetpw> <cmpdOrSynthIdent as compound or syntheticIdent>\n";
}

print "sourcedb: $sourcedb\n";
print "sourcehost: $sourcehost\n";
print "sourceuser: $sourceuser\n";
print "sourcepw: $sourcepw\n";
print "targetdb: $targetdb\n";
print "targethost: $targethost\n";
print "targetuser: $targetuser\n";
print "targetpw: $targetpw\n";
print "cmpdOrSynthIdent: $cmpdOrSynthIdent\n"; 

#  ####   ######   #####  #    #  #####
# #       #          #    #    #  #    #
#  ####   #####      #    #    #  #    #
#      #  #          #    #    #  #####
# #    #  #          #    #    #  #
#  ####   ######     #     ####   #

# connections
my $sourcedbh = DBI->connect("DBI:Pg:dbname=$sourcedb;host=$sourcehost", "$sourceuser", "$sourcepw", {'RaiseError' => 1});
my $targetdbh = DBI->connect("DBI:Pg:dbname=$targetdb;host=127.0.0.1", "$targetuser", "$targetpw", {'RaiseError' => 1});

# create app_and_inv_smiles

my $sql= qq[
drop table if exists app_and_inv_smiles
];

my $sth = $sourcedbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

my $sql = qq[
create table app_and_inv_smiles 
as
select nsc.nsc, gnam.value as generic_name, pnam.value as preferred_name, trg.value as primary_target, ct.can_smi as smiles
from curated_nsc nsc
left outer join curated_name gnam on nsc.generic_name_fk = gnam.id
left outer join curated_name pnam on nsc.preferred_name_fk = pnam.id
left outer join curated_target trg on nsc.primary_target_fk = trg.id
left outer join cmpd_table ct on nsc.nsc = ct.nsc
];

my $sth = $sourcedbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;

# using list of lists

$sql = qq[
select nsc, generic_name, preferred_name, primary_target, smiles from app_and_inv_smiles
];
my $sth = $sourcedbh->prepare($sql);
print "$sql\n\n";
$sth->execute() or die "Couldn't execute statement: $sql " . $sth->errstr;	

my $lol = $sth->fetchall_arrayref();

# target of update

my $prepsql;

if ($cmpdOrSynthIdent eq "compound"){
	
	$prepsql = qq[
	update compound
	set drug_name = ?,
	target = ?,
	smiles = ?
	where nsc = ?
	];
	
} elsif ($cmpdOrSynthIdent eq "syntheticIdent"){
	
	$prepsql = qq[
	update synthetic_ident si
	set drug_name = ?,
	target = ?,
	smiles = ?
	from nsc_ident ni
	where si.id = ni.id 
	and ni.nsc = ?
	];
	
} else {
	die "cmpdOrSynthIdent must be either compound or syntheticIdent";	
}

print "preparing targetdbh: $prepsql\n\n";
my $sth = $targetdbh->prepare($prepsql);

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
$sourcedbh->disconnect();
$targetdbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "--start : $startDate\n";
print "--finish: $finishDate\n";


