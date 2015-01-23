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

# connect
#my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1, AutoCommit => 0 });
my $dbh = DBI->connect("DBI:Pg:dbname=datasystemdb;host=localhost", "mwkunkel", "donkie11", {'RaiseError' => 1});

## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------
## -----------------------------------------------------------------------------------

my @fragFileList = qw(rs3_from_plp_400k_frags rs3_from_plp_remainder_frags);
my @nscFileList = qw(rs3_from_plp_400k_nsc rs3_from_plp_remainder_nsc);
my @ctabFileList = qw(rs3_from_plp_400k_ctab rs3_from_plp_remainder_ctab);

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

my $overallFragOK = 0;

foreach my $fragFileBase(@fragFileList){
	
	my $fragFileName = "/home/mwkunkel/$fragFileBase.txt";
	
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
	
	# comparison of list sizes
	
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
		
		$overallFragOK = $overallFragOK + 1;
		
		print "$fragFileName MATCHES expected headers.\n";
		
	} else {
		
		print "$fragFileName DOES NOT MATCH expected headers.\n";
		
		printf "%-30s | %-30s\n", "Table Column Name", "File Header";		
		foreach (my $i = 0; $i < @fragColumnNames; $i++) {		
			if (lc $fragColumnNames[$i] ne lc $fragFileHeaders[$i]) {				
				printf "%-30s | %-30s\n", "-----FAIL----- " . $fragColumnNames[$i] , "-----FAIL----- " . $fragFileHeaders[$i];
			} else {
				printf "%-30s | %-30s\n", $fragColumnNames[$i], $fragFileHeaders[$i];
				
			}
		}
		
		print "\n\n\n";
		
	}
	
	close INPUTFILE;
	
}

if ($overallFragOK == 2){
	
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

my $overallNscOK = 0;

foreach my $nscFileBase(@nscFileList){
	
	my $nscFileName = "/home/mwkunkel/$nscFileBase.txt";
	
	# open the nsc input file and make sure that the first line matches the expected nscFileHeaders
	
	open ( INPUTFILE, "<$nscFileName") or die "Can't open $nscFileName\n";
	my $firstLine = <INPUTFILE>;
	chomp($firstLine);
	
	$firstLine =~ s/[\r\n]//g;
	
	# print "firstLine: $firstLine\n";
	my @nscFileHeaders = split(/\t/, $firstLine);
	
	# print join(",", @nscColumnNames);
	# print "\n";
	# print join(",", @nscFileHeaders);
	# print "\n";
	
	my $equals = 0;
	
	if (@nscColumnNames != @nscFileHeaders) {
		$equals = 0;
	} else {
		$equals = 1;
		for my $i ( 0 .. $#nscColumnNames) {		
			if (lc($nscColumnNames[$i]) ne lc($nscFileHeaders[$i])) {				
				my $lccol = lc($nscColumnNames[$i]);
				my $lcfil = lc($nscFileHeaders[$i]);				
				$equals = 0;
			}
		}
	}
	
	if ($equals == 1){
		
		$overallNscOK = $overallNscOK + 1;
		
		print "$nscFileName MATCHES expected headers.\n";
		
		
	} else {
		
		print "$nscFileName DOES NOT MATCH expected headers.\n";
		
		printf "%-30s | %-30s\n", "Table Column Name", "File Header";		
		foreach (my $i = 0; $i < @nscColumnNames; $i++) {		
			if (lc $nscColumnNames[$i] ne lc $nscFileHeaders[$i]) {				
				printf "%-30s | %-30s\n",  "-----FAIL----- " . $nscColumnNames[$i] , "-----FAIL----- " . $nscFileHeaders[$i];
			} else {
				printf "%-30s | %-30s\n", $nscColumnNames[$i], $nscFileHeaders[$i];
			}
		}
		
		print "\n\n\n";
		
	}
	
	close INPUTFILE;
	
}

if ($overallNscOK == 2){
		
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

my $overallCtabOK = 0;

foreach my $ctabFileBase (@ctabFileList){
	
	my $ctabFileName = "/home/mwkunkel/$ctabFileBase.txt";
	my $ctabTableName = "$ctabFileBase";
	
	# open the ctab input file and make sure that the first line matches the expected ctabFileHeaders
	
	open ( INPUTFILE, "<$ctabFileName") or die "Can't open $ctabFileName\n\n\n";
	my $firstLine = <INPUTFILE>;
	chomp($firstLine);
	
	$firstLine =~ s/[\r\n]//g;
	
	# print "firstLine: $firstLine\n";
	my @ctabFileHeaders = split(/,/, $firstLine);
	
	# print join(",", @ctabColumnNames);
	# print "\n";
	# print join(",", @ctabFileHeaders);
	# print "\n";
	
	my $equals = 0;
	
	if (@ctabColumnNames != @ctabFileHeaders) {
		$equals = 0;
	} else {
		$equals = 1;
		foreach (my $i = 0; $i < @ctabColumnNames; $i++) {		
			if (lc $ctabColumnNames[$i] ne lc $ctabFileHeaders[$i]) {
				$equals = 0;
				last;
			}
		}
	}
	
	if ($equals == 1){
		
		$overallCtabOK = $overallCtabOK + 1; 
		
		print "$ctabFileName MATCHES expected headers.\n";
		
	} else {
		
		print "$ctabFileName DOES NOT MATCH expected headers.\n";
		
		printf "%-30s | %-30s\n", "Table Column Name", "File Header";		
		foreach (my $i = 0; $i < @ctabColumnNames; $i++) {		
			if (lc $ctabColumnNames[$i] ne lc $ctabFileHeaders[$i]) {
				printf "%-30s | %-30s\n",  "-----FAIL----- " . $ctabColumnNames[$i] , "-----FAIL----- " . $ctabFileHeaders[$i];
			} else {
				printf "%-30s | %-30s\n", $ctabColumnNames[$i], $ctabFileHeaders[$i];
			}
		}
		
		print "\n\n\n";
		
	}
	
	close INPUTFILE;
	
}

if ($overallCtabOK == 2){
	
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

