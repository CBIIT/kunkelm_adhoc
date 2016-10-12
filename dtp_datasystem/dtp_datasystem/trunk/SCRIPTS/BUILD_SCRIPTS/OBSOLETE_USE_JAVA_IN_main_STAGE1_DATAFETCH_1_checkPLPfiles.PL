#!/usr/bin/perl

# delimited files from Pipeline Pilot have a default column order 
# that is MOSTLY dependent on the order in which widgets appear 
# BUT ALSO on which field(s) might have been null early in the pipeline(s)

# this checks the postgres create table statement field names used for file import
# against the actual column headers

# the plp files are split in half:  file names with 400K have nsc <= 400000, 
# file names with remainder have the rest
#

# if OK then the drop and create table statements are executed
# otherwise the expected and found names are printed

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

use DBI;

## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

print "Use CheckPLPFiles.java in datasystem.Main\n";
print "Use CheckPLPFiles.java in datasystem.Main\n";
print "Use CheckPLPFiles.java in datasystem.Main\n";
print "Use CheckPLPFiles.java in datasystem.Main\n";
print "Use CheckPLPFiles.java in datasystem.Main\n";
print "Use CheckPLPFiles.java in datasystem.Main\n";
print "Use CheckPLPFiles.java in datasystem.Main\n";
print "Use CheckPLPFiles.java in datasystem.Main\n";
print "Use CheckPLPFiles.java in datasystem.Main\n";

die("Use CheckPLPFiles.java in datasystem.Main\n");

# connect
#my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1, AutoCommit => 0 });
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=127.0.0.1", "mwkunkel", "donkie11", {'RaiseError' => 1});

## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------

my @fragFileList = qw(rs3_from_plp_frags);
my @nscFileList = qw(rs3_from_plp_nsc);
my @ctabFileList = qw(rs3_from_plp_ctab);

## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------

######  #####     ##     ####           #####     ##     #####    ##
#       #    #   #  #   #    #          #    #   #  #      #     #  #
#####   #    #  #    #  #               #    #  #    #     #    #    #
#       #####   ######  #  ###          #    #  ######     #    ######
#       #   #   #    #  #    #          #    #  #    #     #    #    #
#       #    #  #    #   ####           #####   #    #     #    #    #

my $dropFragTableSql = "drop table if exists rs3_from_plp_frags";

my $createFragTableSql = qq[
create table rs3_from_plp_frags(
NSC int,
PROD_MF varchar,
PROD_MW double precision,
CAS varchar,
Name varchar,
Comment varchar,
fragmentIndex int,
cleanresult varchar,
labelArray varchar,
smiles varchar,
can_smi varchar,
can_taut varchar,
can_taut_strip_stereo varchar,
InChI varchar,
InChI_AuxInfo text,
Molecular_Formula varchar,
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
CanonicalTautomer varchar,
NumberOfTautomers int,
ErrorText varchar,	
TransformationReactions varchar,
AtomsChangedInReactions varchar,
InChI_Message varchar,
RegisterCheckErrors varchar,
Chemistry varchar,
Attribute varchar,
TooManyTautomers varchar,
CleanMessage varchar,
ChargeTautomer varchar,
UnneutralizedParent varchar,
BadIsotopeAtoms varchar,
NonLinearCentralTripleBonds varchar,
internal_null_coords varchar,
CheshireMessage varchar,
ErrorDetails varchar,
Num_ComplexedFragments varchar,
NonLinearTerminalTripleBonds varchar,
NonLinearAlleneAtoms varchar
)
];

# array of expected column names for sanity check with header row in PLP files

my @fragColumnNames = qw[
	NSC
PROD_MF
PROD_MW
CAS
Name
Comment
fragmentIndex
CleanResult
labelArray
smiles
can_smi
can_taut
can_taut_strip_stereo
InChI
InChI_AuxInfo
Molecular_Formula
Molecular_Weight
Num_Atoms
Num_Bonds
Num_Hydrogens
Num_PositiveAtoms
Num_NegativeAtoms
Num_RingBonds
Num_RotatableBonds
Num_AromaticBonds
Num_Rings
Num_AromaticRings
Num_RingAssemblies
Num_MetalAtoms
Num_StereoAtoms
Num_StereoBonds
Num_SingleBonds
Num_DoubleBonds
Num_TripleBonds
FormalCharge
CoordDimension
IsChiral
ALogP
LogD
Molecular_Solubility
Molecular_SurfaceArea
Num_H_Acceptors
Num_H_Donors
CanonicalTautomer
NumberOfTautomers
ErrorText	
TransformationReactions
AtomsChangedInReactions
InChI_Message
RegisterCheckErrors
Chemistry
Attribute
TooManyTautomers
CleanMessage
ChargeTautomer
UnneutralizedParent
BadIsotopeAtoms
NonLinearCentralTripleBonds
internal_null_coords
CheshireMessage
ErrorDetails
Num_ComplexedFragments
NonLinearTerminalTripleBonds
NonLinearAlleneAtoms
	];

my $fragsOK = 0;

foreach my $fragFileBase(@fragFileList){
	
	my $fragFileName = "/home/mwkunkel/$fragFileBase.csv";
	
	# open the frag input file and make sure that the first line matches the expected fragFileHeaders
	
	open ( INPUTFILE, "<$fragFileName") or die "Can't open $fragFileName\n";
	my $firstLine = <INPUTFILE>;
	chomp($firstLine);
	
	$firstLine =~ s/[\r\n]//g;
	
	# print "firstLine: $firstLine\n";
	my @fragFileHeaders = split(/,/, $firstLine);
	
	# debug - all column names
	print "fragColumnNames - EXPECTED\n";
	print join("\t", @fragColumnNames);
	print "\n\n\n";
	print "fragFileHeaders - FOUND\n";
	print join("\t", @fragFileHeaders);
	print "\n\n\n";
	
	# comparison of list sizes
	
	if (@fragColumnNames != @fragFileHeaders) {
		$fragsOK = 0;
	} else {
		$fragsOK = 1;
		foreach (my $i = 0; $i < @fragColumnNames; $i++) {		
			if (lc $fragColumnNames[$i] ne lc $fragFileHeaders[$i]) {
				$fragsOK = 0;
				last;
			}
		}
	}
	
	if ($fragsOK == 1){
				
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "$fragFileName MATCHES expected headers.\n";
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "\n\n\n";
		
	} else {
		
		print "XXXXXX    XX       X    X\n";
		print "X        X  X      X    X\n";
		print "XXXXX   X    X     X    X\n";
		print "X       XXXXXX     X    X\n";
		print "X       X    X     X    X\n";
		print "X       X    X     X    XXXXXX\n";
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "$fragFileName DOES NOT MATCH expected headers.\n";
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "\n\n\n";
		
		
		printf "%-45s | %-45s\n", "Table Column Name", "File Header";		
		foreach (my $i = 0; $i < @fragColumnNames; $i++) {		
			if (lc $fragColumnNames[$i] ne lc $fragFileHeaders[$i]) {				
				printf "%-45s | %-45s\n", "-----FAIL----- " . $fragColumnNames[$i] , "-----FAIL----- " . $fragFileHeaders[$i];
			} else {
				printf "%-45s | %-45s\n", $fragColumnNames[$i], $fragFileHeaders[$i];
				
			}
		}
		
		print "\n\n\n";
		
	}
	
	close INPUTFILE;
	
}

if ($fragsOK == 1){
	
	print join(' and ', @fragFileList) . " were OK.  Dropping and re-creating rs3_from_plp_frags table.\n\n\n";
	
	#print "$dropFragTableSql\n";
	my $sth = $dbh->prepare($dropFragTableSql);
	$sth->execute() or die "Couldn't execute statement: $dropFragTableSql " . $sth->errstr;
	
	#print "$createFragTableSql\n";
	$sth = $dbh->prepare($createFragTableSql);
	$sth->execute() or die "Couldn't execute statement: $createFragTableSql " . $sth->errstr;
	
}

#    #   ####    ####           #####     ##     #####    ##
##   #  #       #    #          #    #   #  #      #     #  #
# #  #   ####   #               #    #  #    #     #    #    #
#  # #       #  #               #    #  ######     #    ######
#   ##  #    #  #    #          #    #  #    #     #    #    #
#    #   ####    ####           #####   #    #     #    #    #

my $dropNscTableSql = "drop table if exists rs3_from_plp_nsc";

my $createNscTableSql = qq[
create table rs3_from_plp_nsc(
NSC int,
MF varchar,
MW double precision,
CAS varchar,
Name varchar,
Comment varchar,
Molecular_Formula varchar,
Molecular_Weight double precision,
Num_Atoms int,
Num_Bonds int,
Num_Hydrogens int,
Num_PositiveAtoms int,
Num_NegativeAtoms int,
Num_Fragments int,
Num_MetalAtoms int,
FormalCharge int,
smiles varchar,
can_smi varchar
)
];

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
	"FormalCharge",
	"Smiles",
	"Canonical_Smiles"
	);

my $nscOK = 0;

foreach my $nscFileBase(@nscFileList){
	
	my $nscFileName = "/home/mwkunkel/$nscFileBase.csv";
	
	# open the nsc input file and make sure that the first line matches the expected nscFileHeaders
	
	open ( INPUTFILE, "<$nscFileName") or die "Can't open $nscFileName\n";
	my $firstLine = <INPUTFILE>;
	chomp($firstLine);
	
	$firstLine =~ s/[\r\n]//g;
	
	# print "firstLine: $firstLine\n";
	my @nscFileHeaders = split(/,/, $firstLine);
	
	print "nscColumnNames - EXPECTED\n";
	print join("\t", @nscColumnNames);
	print "\n\n\n";
	print "nscFileHeaders - FOUND\n";
	print join("\t", @nscFileHeaders);
	print "\n\n\n";
	
	if (@nscColumnNames != @nscFileHeaders) {
		$nscOK = 0;
	} else {
		$nscOK = 1;
		for my $i ( 0 .. $#nscColumnNames) {		
			if (lc($nscColumnNames[$i]) ne lc($nscFileHeaders[$i])) {				
				my $lccol = lc($nscColumnNames[$i]);
				my $lcfil = lc($nscFileHeaders[$i]);				
				$nscOK = 0;
			}
		}
	}
	
	if ($nscOK == 1){
		
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "$nscFileName MATCHES expected headers.\n";
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "\n\n\n";
		
	} else {
		
		print "XXXXXX    XX       X    X\n";
		print "X        X  X      X    X\n";
		print "XXXXX   X    X     X    X\n";
		print "X       XXXXXX     X    X\n";
		print "X       X    X     X    X\n";
		print "X       X    X     X    XXXXXX\n";
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "$nscFileName DOES NOT MATCH expected headers.\n";
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "\n\n\n";
		
		printf "%-45s | %-45s\n", "Table Column Name", "File Header";		
		foreach (my $i = 0; $i < @nscColumnNames; $i++) {		
			if (lc $nscColumnNames[$i] ne lc $nscFileHeaders[$i]) {				
				printf "%-45s | %-45s\n",  "-----FAIL----- " . $nscColumnNames[$i] , "-----FAIL----- " . $nscFileHeaders[$i];
			} else {
				printf "%-45s | %-45s\n", $nscColumnNames[$i], $nscFileHeaders[$i];
			}
		}
		
		print "\n\n\n";
		
	}
	
	close INPUTFILE;
	
}

if ($nscOK == 1){
	
	print join(' and ', @nscFileList) . " were OK.  Dropping and re-creating rs3_from_plp_nsc table.\n\n\n";
	
	#print "$dropNscTableSql\n";
	my $sth = $dbh->prepare($dropNscTableSql);
	$sth->execute() or die "Couldn't execute statement: $dropNscTableSql " . $sth->errstr;
	
	#print "$createNscTableSql\n";
	$sth = $dbh->prepare($createNscTableSql);
	$sth->execute() or die "Couldn't execute statement: $createNscTableSql " . $sth->errstr;
	
	
}

##  ####    #####    ##    #####           #####     ##     #####    ##
## #    #     #     #  #   #    #          #    #   #  #      #     #  #
## #          #    #    #  #####           #    #  #    #     #    #    #
## #          #    ######  #    #          #    #  ######     #    ######
## #    #     #    #    #  #    #          #    #  #    #     #    #    #
##  ####      #    #    #  #####           #####   #    #     #    #    #

my $dropCtabTableSql = "drop table if exists rs3_from_plp_ctab";

my $createCtabTableSql = qq[
create table rs3_from_plp_ctab(
NSC int,
fragmentIndex int,
CTAB text
)
];

my @ctabColumnNames = (
	"NSC",
	"fragmentIndex",
	"CTAB"
	);

my $ctabOK = 0;

foreach my $ctabFileBase (@ctabFileList){
	
	my $ctabFileName = "/home/mwkunkel/$ctabFileBase.csv";
	my $ctabTableName = "$ctabFileBase";
	
	# open the ctab input file and make sure that the first line matches the expected ctabFileHeaders
	
	open ( INPUTFILE, "<$ctabFileName") or die "Can't open $ctabFileName\n\n\n";
	my $firstLine = <INPUTFILE>;
	chomp($firstLine);
	
	$firstLine =~ s/[\r\n]//g;
	
	# print "firstLine: $firstLine\n";
	my @ctabFileHeaders = split(/,/, $firstLine);
	
	print "ctabColumnNames - EXPECTED\n";
	print join("\t", @ctabColumnNames);
	print "\n\n\n";
	print "ctabFileHeaders - FOUND\n";
	print join("\t", @ctabFileHeaders);
	print "\n\n\n";
		
	if (@ctabColumnNames != @ctabFileHeaders) {
		$ctabOK = 0;
	} else {
		$ctabOK = 1;
		foreach (my $i = 0; $i < @ctabColumnNames; $i++) {		
			if (lc $ctabColumnNames[$i] ne lc $ctabFileHeaders[$i]) {
				$ctabOK = 0;
				last;
			}
		}
	}
	
	if ($ctabOK == 1){
		
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "$ctabFileName MATCHES expected headers.\n";
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "\n\n\n";
		
	} else {
		
		print "XXXXXX    XX       X    X\n";
		print "X        X  X      X    X\n";
		print "XXXXX   X    X     X    X\n";
		print "X       XXXXXX     X    X\n";
		print "X       X    X     X    X\n";
		print "X       X    X     X    XXXXXX\n";
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "$ctabFileName DOES NOT MATCH expected headers.\n";
		print "--------------------------------------\n";
		print "--------------------------------------\n";
		print "\n\n\n";
		
		
		printf "%-45s | %-45s\n", "Table Column Name", "File Header";		
		foreach (my $i = 0; $i < @ctabColumnNames; $i++) {		
			if (lc $ctabColumnNames[$i] ne lc $ctabFileHeaders[$i]) {
				printf "%-45s | %-45s\n",  "-----FAIL----- " . $ctabColumnNames[$i] , "-----FAIL----- " . $ctabFileHeaders[$i];
			} else {
				printf "%-45s | %-45s\n", $ctabColumnNames[$i], $ctabFileHeaders[$i];
			}
		}
		
		print "\n\n\n";
		
	}
	
	close INPUTFILE;
	
}

if ($ctabOK == 1){
	
	print join(' and ', @ctabFileList) . " were OK.  Dropping and re-creating rs3_from_plp_ctab table.\n\n\n";
	
	#print "$dropCtabTableSql\n";
	my $sth = $dbh->prepare($dropCtabTableSql);
	$sth->execute() or die "Couldn't execute statement: $dropCtabTableSql " . $sth->errstr;
	
	#print "$createCtabTableSql\n";
	$sth = $dbh->prepare($createCtabTableSql);
	$sth->execute() or die "Couldn't execute statement: $createCtabTableSql " . $sth->errstr;
	
	
}

## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------





# clean up
$dbh->disconnect();

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";

