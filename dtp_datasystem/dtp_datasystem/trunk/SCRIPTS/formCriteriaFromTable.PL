#!/usr/bin/perl

# for a list of field names and types
# cranks out form input and output fields
# java variable declarations, .xhtml input fields, .xhtml output fields

# min/max criteria for integer and double precision fields
# string criteria for String fields

use strict;
use warnings;

use Time::HiRes qw(time);
use POSIX qw(strftime);

my $t = time;
my $startDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$startDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start: $startDate\n";

my $fragString = "rs3_from_plp_frags";
my $nscString = "rs3_from_plp_nsc";

# fragment level data

my $fragFileName = "/home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/$fragString.txt";
my $fragTableName = "$fragString";

# array of expected column names for sanity check with header row in PLP files

my $pChemVars = "
Double molecularWeight;
String molecularFormula;
Double logD;
Integer countHydBondAcceptors;
Integer countHydBondDonors;
Double surfaceArea;
Double solubility;
Integer countRings;
Integer countAtoms;
Integer countBonds;
Integer countSingleBonds;
Integer countDoubleBonds;
Integer countTripleBonds;
Integer countRotatableBonds;
Integer countHydrogenAtoms;
Integer countMetalAtoms;
Integer countHeavyAtoms;
Integer countPositiveAtoms;
Integer countNegativeAtoms;
Integer countRingBonds;
Integer countStereoAtoms;
Integer countStereoBonds;
Integer countRingAssemblies;
Integer countAromaticBonds;
Integer countAromaticRings;
Integer formalCharge;
Double theALogP;
Long id;
";

my $strcVars = "	
String smiles;
String inchi;
String inchiAux;
String ctab;
String canSmi;
String canTaut;
String canTautStripStereo;
Long id;
";

my $nscCmpdVars = "
String name;
Long nscCmpdId;
String prefix;
Integer nsc;
String conf;
String distribution;
String cas;
Integer countFragments;
String discreet;
String identifierString;
String descriptorString;
Double molecularWeight;
String molecularFormula;
NscCmpdType nscCmpdType;
CmpdFragment cmpdParentFragment;
Collection<CmpdAlias> cmpdAliases = new HashSet<CmpdAlias>();
CmpdBioAssay cmpdBioAssay;
Collection<CmpdPubChemSid> cmpdPubChemSids = new HashSet<CmpdPubChemSid>();
Collection<CmpdFragment> cmpdFragments = new HashSet<CmpdFragment>();
Collection<CmpdRelated> cmpdRelateds = new HashSet<CmpdRelated>();
CmpdInventory cmpdInventory;
Collection<CmpdTarget> cmpdTargets = new HashSet<CmpdTarget>();
Collection<CmpdPlate> cmpdPlates = new HashSet<CmpdPlate>();
Collection<CmpdNamedSet> cmpdNamedSets = new HashSet<CmpdNamedSet>();
Collection<CmpdProject> cmpdProjects = new HashSet<CmpdProject>();
CmpdAnnotations cmpdAnnotations;
CmpdLegacyCmpd cmpdLegacyCmpd;
";


open ( javaPart, ">javaPart") or die "Can't open javaPart\n";
open ( formatPart, ">formatPart") or die "Can't open formatPart\n";
open ( outputPartColumnFormat, ">outputPartColumnFormat") or die "Can't open outputPartColumnFormat\n";
open ( outputPartLabelValue, ">outputPartLabelValue") or die "Can't open outputPartLabelValue\n";
open ( outputPartAddToColumnModelList, ">outputPartAddToColumnModelList") or die "Can't open outputPartAddToColumnModelList\n";
open ( hibernatePart, ">hibernatePart") or die "Can't open hibernatePart\n";

#--------------------------------------------------------------------
#--------------------------------------------------------------------
#--------------------------------------------------------------------

my @cumulativeFieldNames = ();

foreach my $dataSet($pChemVars, $strcVars, $nscCmpdVars){

#print "$dataSet\n";

chomp($dataSet);

my $fetchString = '';

if ($dataSet eq $pChemVars){
    print "\n\n-------------------------This is pChemVars\n";
    $fetchString = 'listMember.cmpd.parentFragment.cmpdFragmentPChem';
} elsif ($dataSet eq $strcVars){
    print "\n\n-------------------------This is strcVars\n";
    $fetchString = 'listMember.cmpd.parentFragment.cmpdFragmentStructure';
} elsif ($dataSet eq $nscCmpdVars){
   print "\n\n-------------------------This is nscCmpdVars\n"; 
    $fetchString = 'listMember.cmpd';
}

my @lines = split(/;/, $dataSet);

foreach my $line(@lines){
	
	chomp($line);
	
	my @splitLine = split(/\s/, $line);
	my $varType = $splitLine[1];
	my $varName = $splitLine[2];
	#print "varType: $varType varName: $varName\n";
	
	if ( $varType eq "Integer"){
		
		print "---------min_$varName max_$varName\n";
		
		print javaPart "private $varType min_$varName;\n";
		print javaPart "private $varType max_$varName;\n";
		
		print formatPart qq[
		<p:panel header="$varName" style="display:inline-block;border: none;">
		<h:outputLabel for="min_$varName\_id" value="$varName Minimum"/>
		<br/>
		<h:inputText value="#{pChemBean.min_$varName}" id="min_$varName\_id" cols="15" rows="5"/>
		<br/>
		<h:outputLabel for="max_$varName\_id" value="$varName Maximum"/>
		<br/>
		<h:inputText value="#{pChemBean.max_$varName}" id="max_$varName\_id" cols="15" rows="5"/>
		</p:panel>
		];	
		
		print outputPartColumnFormat qq[
		<p:column headerText="$varName" resizeable="true" sortBy="#{$fetchString.$varName}">                
		<h:outputText value="#{$fetchString.$varName}" />
		</p:column>
		];
		
		print outputPartLabelValue qq[
		<p:outputLabel value="$varName"/>                
		<h:outputText value="#{$fetchString.$varName}" />
		];
		
		print outputPartAddToColumnModelList qq[
		hm.put("$varName", "$fetchString.$varName");
		];
		
		push @cumulativeFieldNames, qq["$varName"];
		
		print hibernatePart qq[		
		if (this.min_$varName != null && this.max_$varName != null){
		// between
		} else if (this.min_$varName != null){
		// gt min
		} else if (this.max_$varName != null){
		// lt max
		}		
		];
		
	} elsif ($varType eq "Double"){
		
		print "---------min_$varName max_$varName\n";
		
		print javaPart "private $varType min_$varName;\n";
		print javaPart "private $varType max_$varName;\n";
		
		print formatPart qq[
		<p:panel header="$varName" style="display:inline-block;border: none;">
		<h:outputLabel for="min_$varName\_id" value="$varName Minimum"/>
		<br/>
		<h:inputText value="#{pChemBean.min_$varName}" id="min_$varName\_id" cols="15" rows="5"/>
		<br/>
		<h:outputLabel for="max_$varName\_id" value="$varName Maximum"/>
		<br/>
		<h:inputText value="#{pChemBean.max_$varName}" id="max_$varName\_id" cols="15" rows="5"/>
		</p:panel>
		];
		
		print outputPartColumnFormat qq[
		<p:column headerText="$varName" resizeable="true" sortBy="#{$fetchString.$varName}">                
		<h:outputText value="#{$fetchString.$varName}">
		<f:convertNumber pattern="#0.00" />
		</h:outputText>
		</p:column>
		];
		
		print outputPartLabelValue qq[
		<p:outputLabel value="$varName"/>
		<h:outputText value="#{$fetchString.$varName}">
		<f:convertNumber pattern="#0.00" />
		</h:outputText>
		];
		
		print outputPartAddToColumnModelList qq[
		hm.put("$varName", "$fetchString.$varName");
		];
		
		push @cumulativeFieldNames, qq["$varName"];
		
		print hibernatePart qq[		
		if (this.min_$varName != null && this.max_$varName != null){
		// between
		} else if (this.min_$varName != null){
		// gt min
		} else if (this.max_$varName != null){
		// lt max
		}
		];
		
	} elsif ($varType eq "String"){
		
		print "---------$varName\n";
		print javaPart "private $varType $varName;\n";
		
		print formatPart qq[
		<p:panel header="$varName" style="display:inline-block;border: none;">
		<h:outputLabel for="$varName\_id" value="$varName"/>
		<br/>
		<h:inputText value="#{pChemBean.$varName}" id="$varName\_id" cols="15" rows="5"/>                
		</p:panel>
		];
		
		print outputPartColumnFormat qq[
		<p:column headerText="$varName" resizeable="true" sortBy="#{$fetchString.$varName}">                
		<h:outputText value="#{$fetchString.$varName}" />
		</p:column>
		];
		
		print outputPartLabelValue qq[
		<p:outputLabel value="$varName"/>
		<h:outputText value="#{$fetchString.$varName}" />
		];
		
		print outputPartAddToColumnModelList qq[
		hm.put("$varName", "$fetchString.$varName");
		];
		
		push @cumulativeFieldNames, qq["$varName"];
		
		print hibernatePart qq[		
		if (this.$varName != null){
		// eq
		}
		];
		
		
	}

	
}

print outputPartAddToColumnModelList join(', ', @cumulativeFieldNames);

}

close javaPart;
close formatPart;
close outputPartColumnFormat;
close outputPartLabelValue;
close outputPartAddToColumnModelList;
close hibernatePart;

$t = time;
my $finishDate = strftime "%Y%m%d %H:%M:%S", localtime $t;
$finishDate .= sprintf ".%03d", ($t-int($t))*1000; # without rounding
print "start : $startDate\n";
print "finish: $finishDate\n";

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


