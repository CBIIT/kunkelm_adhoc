#!/usr/bin/perl

# delimited files from Pipeline Pilot have a default column order 
# that is MOSTLY dependent on the order in which widgets appear 
# BUT ALSO on which field(s) are null early in the pipeline(s)

# this checks the postgres create table statement field names used for file import
# against the actual column headers

# if OK then the create table statement is executed
# otherwise the expected and found names are printed

use strict;

my $fragString = "rs3_from_plp_frags";
my $nscString = "rs3_from_plp_nsc";

# fragment level data

my $fragFileName = "/home/mwkunkel/$fragString.txt";
my $fragTableName = "$fragString";

# array of expected column names for sanity check with header row in PLP files

my @fragColumnNames = (
"NSC",
"MF",
"MW",
"CAS",
"Name",
"Comment",
"fragmentIndex",
"atomArray",
"smiles",
"can_smi",
"can_taut",
"can_taut_strip_stereo",
"InChI",
"InChI_AuxInfo",
"Molecular_Formula",
"Molecular_Weight",
"Num_Atoms",
"Num_Bonds",
"Num_Hydrogens",
"Num_PositiveAtoms",
"Num_NegativeAtoms",
"Num_RingBonds",
"Num_RotatableBonds",
"Num_AromaticBonds",
"Num_Rings",
"Num_AromaticRings",
"Num_RingAssemblies",
"Num_MetalAtoms",
"Num_StereoAtoms",
"Num_StereoBonds",
"Num_SingleBonds",
"Num_DoubleBonds",
"Num_TripleBonds",
"FormalCharge",
"CoordDimension",
"IsChiral",
"ALogP",
"LogD",
"Molecular_Solubility",
"Molecular_SurfaceArea",
"Num_H_Acceptors",
"Num_H_Donors",
"CanonicalTautomer",
"NumberOfTautomers",
"BadValenceAtoms"
);

my $dropFragTableSql = "drop table if exists $fragTableName";

my $createFragTableSql = qq[
create table $fragTableName(
NSC int,
MF varchar(4096),
MW double precision,
CAS varchar(4096),
Name varchar(4096),
Comment varchar(4096),
fragmentIndex int,
atomArray varchar(4096),
smiles varchar(4096),
can_smi varchar(4096),
can_taut varchar(4096),
can_taut_strip_stereo varchar(4096),
InChI varchar(4096),
InChI_AuxInfo text,
Molecular_Formula varchar(4096),
Molecular_Weight double precision,
Num_Atoms int,
Num_Bonds int,
Num_Hydrogens int,
Num_PositiveAtoms int,
Num_NegativeAtoms int,
Num_RingBonds int,
Num_RotatableBonds int,
Num_AromaticBonds int,
Num_Rings int,
Num_AromaticRings int,
Num_RingAssemblies int,
Num_MetalAtoms int,
Num_StereoAtoms int,
Num_StereoBonds int,
Num_SingleBonds int,
Num_DoubleBonds int,
Num_TripleBonds int,
FormalCharge int,
CoordDimension int,
IsChiral boolean,
ALogP double precision,
LogD double precision,
Molecular_Solubility double precision,
Molecular_SurfaceArea double precision,
Num_H_Acceptors int,
Num_H_Donors int,
CanonicalTautomer varchar(4096),
NumberOfTautomers int,
BadValenceAtoms varchar(4096)
)
];

# nsc level data

my $nscFileName = "/home/mwkunkel/$nscString.txt";
my $nscTableName = "$nscString";

my @nscColumnNames = (
"NSC",
"MF",
"MW",
"CAS",
"Name",
"Comment",
"Molecular_Formula",
"Molecular_Weight",
"Num_Atoms",
"Num_Bonds",
"Num_Hydrogens",
"Num_PositiveAtoms",
"Num_NegativeAtoms",
"Num_Fragments",
"Num_MetalAtoms",
"FormalCharge"
	);

my $dropNscTableSql = "drop table if exists $nscTableName";

my $createNscTableSql = qq[
create table $nscTableName(
NSC int,
MF varchar(4096),
MW double precision,
CAS varchar(4096),
Name varchar(4096),
Comment varchar(4096),
Molecular_Formula varchar(4096),
Molecular_Weight double precision,
Num_Atoms int,
Num_Bonds int,
Num_Hydrogens int,
Num_PositiveAtoms int,
Num_NegativeAtoms int,
Num_Fragments int,
Num_MetalAtoms int,
FormalCharge int
)
];

use DBI;

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "start time $theTime\n";

# connect
#my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1, AutoCommit => 0 });
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});


#--------------------------------------------------------------------
#--------------------------------------------------------------------
#--------------------------------------------------------------------
# open the frag input file and make sure that the first line matches the expected fragFileHeaders

open ( INPUTFILE, "<$fragFileName") or die "Can't open $fragFileName\n";
my $firstLine = <INPUTFILE>;
chomp($firstLine);

$firstLine =~ s/[\r\n]//g;

# print "firstLine: $firstLine\n";
my @fragFileHeaders = split(/\t/, $firstLine);

# print join(",", @fragColumnNames);
# print "\n";
# print join(",", @fragFileHeaders);
# print "\n";

my $equals = 0;

if (@fragColumnNames != @fragFileHeaders) {
	$equals = 0;
} else {
	$equals = 1;
	foreach (my $i = 0; $i < @fragColumnNames; $i++) {		
		if (lc $fragColumnNames[$i] ne lc $fragFileHeaders[$i]) {
			$equals = 0;
			last;
		}
	}
}

if ($equals == 1){
	
	print "$fragFileName MATCHES expected headers.  Creating table $fragTableName. \n\n\n";
	
	# print "$dropFragTableSql\n";
	my $sth = $dbh->prepare($dropFragTableSql);
	$sth->execute() or die "Couldn't execute statement: $dropFragTableSql " . $sth->errstr;
	
	# print "$createFragTableSql\n";
	my $sth = $dbh->prepare($createFragTableSql);
	$sth->execute() or die "Couldn't execute statement: $createFragTableSql " . $sth->errstr;
	
} else {
	
 	print "$fragFileName DOES NOT MATCH expected headers.  Not creating table $fragTableName. \n\n\n";
 	
 	print "Table Column Name , File Header\n";
 	
 	foreach (my $i = 0; $i < @fragColumnNames; $i++) {		
		if (lc $fragColumnNames[$i] ne lc $fragFileHeaders[$i]) {
			print "$fragColumnNames[$i] , $fragFileHeaders[$i] FAIL FAIL FAIL\n";			
		} else {
			print "$fragColumnNames[$i] , $fragFileHeaders[$i]\n";
		}
	}
	
}

close INPUTFILE;

#--------------------------------------------------------------------
#--------------------------------------------------------------------
#--------------------------------------------------------------------
# open the nsc input file and make sure that the first line matches the expected nscFileHeaders

open ( INPUTFILE, "<$nscFileName") or die "Can't open $nscFileName\n";
my $firstLine = <INPUTFILE>;
chomp($firstLine);

$firstLine =~ s/[\r\n]//g;

# print "firstLine: $firstLine\n";
my @nscFileHeaders = split(/\t/, $firstLine);

 print join(",", @nscColumnNames);
 print "\n";
 print join(",", @nscFileHeaders);
 print "\n";

my $equals = 0;

if (@nscColumnNames != @nscFileHeaders) {
	$equals = 0;
} else {
	$equals = 1;
	for my $i ( 0 .. $#nscColumnNames) {		
		if (lc($nscColumnNames[$i]) ne lc($nscFileHeaders[$i])) {
			
			my $lccol = lc($nscColumnNames[$i]);
			my $lcfil = lc($nscFileHeaders[$i]);
			
			print "FAIL FAIL FAIL FAIL FAIL $lccol , $lcfil\n";
			
			$equals = 0;
			# last;
		}
	}
}

if ($equals == 1){
	
	print "$nscFileName MATCHES expected headers. Creating table $nscTableName. \n\n\n";
	
	# print "$dropNscTableSql\n";
	#------------------------------- my $sth = $dbh->prepare($dropNscTableSql);
	#------------------------------- $sth->execute() or die "Couldn't execute statement: $dropNscTableSql " . $sth->errstr;
	
	# print "$createNscTableSql\n";
	#------------------------------- my $sth = $dbh->prepare($createNscTableSql);
	#------------------------------- $sth->execute() or die "Couldn't execute statement: $createNscTableSql " . $sth->errstr;
	
} else {
	
 	print "$nscFileName DOES NOT MATCH expected headers.  Not creating table $nscTableName. \n\n\n";
 	
 	print "Table Column Name , File Header\n";
 	
 	foreach (my $i = 0; $i < @nscColumnNames; $i++) {		
		if (lc $nscColumnNames[$i] ne lc $nscFileHeaders[$i]) {
			print "$nscColumnNames[$i] , $nscFileHeaders[$i] FAIL FAIL FAIL\n";			
		} else {
			print "$nscColumnNames[$i] , $nscFileHeaders[$i]\n";
		}
	}
	
}

close INPUTFILE;

$dbh->commit();	

# clean up
$dbh->disconnect();

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "finish time $theTime\n";

# Perl trim function to remove whitespace from the start and end of the string
sub trim($)
{
	my $string = shift;
	$string =~ s/^\s+//;
	$string =~ s/\s+$//;
	return $string;
}
# Left trim function to remove leading whitespace
sub ltrim($)
{
	my $string = shift;
	$string =~ s/^\s+//;
	return $string;
}
# Right trim function to remove trailing whitespace
sub rtrim($)
{
	my $string = shift;
	$string =~ s/\s+$//;
	return $string;
}


