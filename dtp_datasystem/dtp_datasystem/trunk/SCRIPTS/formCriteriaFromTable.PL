#!/usr/bin/perl

# java variable declarations, .xhtml input fields, .xhtml output fields

# for 

# min/max criteria for integer and double precision fields
# string criteria for String fields


use strict;
use warnings;

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
static final long serialVersionUID = 5944515987538010440L;
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

(my $second, my $minute, my $hour, my $dayOfMonth, my $month, my $yearOffset, my $dayOfWeek, my $dayOfYear, my $daylightSavings) = localtime();
my $year = 1900 + $yearOffset;
my $theTime = "$hour:$minute:$second";
print "start time $theTime\n";

open ( JAVAPART, ">JAVAPART") or die "Can't open JAVAPART\n";
open ( FORMPART, ">FORMPART") or die "Can't open FORMPART\n";
open ( OUTPUTPARTCOLUMNS, ">OUTPUTPARTCOLUMNS") or die "Can't open OUTPUTPARTCOLUMNS\n";
open ( OUTPUTPARTLABELVALUE, ">OUTPUTPARTLABELVALUE") or die "Can't open OUTPUTPARTLABELVALUE\n";
open ( HIBERNATEPART, ">HIBERNATEPART") or die "Can't open HIBERNATEPART\n";

#--------------------------------------------------------------------
#--------------------------------------------------------------------
#--------------------------------------------------------------------

print "$pChemVars\n";

chomp($pChemVars);
my @lines = split(/;/, $pChemVars);
foreach my $line(@lines){
	chomp($line);
	my @splitLine = split(/\s/, $line);
	my $varType = $splitLine[1];
	my $varName = $splitLine[2];
	print "varType: $varType varName: $varName\n";
	
	if ( $varType eq "Integer"){
		
		print "---------min_$varName max_$varName\n";
		
		print JAVAPART "private $varType min_$varName;\n";
		print JAVAPART "private $varType max_$varName;\n";
		
		print FORMPART qq[
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
		
		print OUTPUTPARTCOLUMNS qq[
		<p:column headerText="$varName" resizeable="true" sortBy="#{listMember.cmpd.parentFragment.cmpdFragmentPChem.$varName}">                
		<h:outputText value="#{listMember.cmpd.parentFragment.cmpdFragmentPChem.$varName}" />
		</p:column>
		];
		
		print OUTPUTPARTLABELVALUE qq[
		<p:outputLabel value="$varName"/>                
		<h:outputText value="#{listMember.cmpd.parentFragment.cmpdFragmentPChem.$varName}" />
		];
		
		print HIBERNATEPART qq[		
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
		
		print JAVAPART "private $varType min_$varName;\n";
		print JAVAPART "private $varType max_$varName;\n";
		
		print FORMPART qq[
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
		
		print OUTPUTPARTCOLUMNS qq[
		<p:column headerText="$varName" resizeable="true" sortBy="#{listMember.cmpd.parentFragment.cmpdFragmentPChem.$varName}">                
		<h:outputText value="#{listMember.cmpd.parentFragment.cmpdFragmentPChem.$varName}">
		<f:convertNumber pattern="#0.00" />
		</h:outputText>
		</p:column>
		];
		
		print OUTPUTPARTLABELVALUE qq[
		<p:outputLabel value="$varName"/>
		<h:outputText value="#{listMember.cmpd.parentFragment.cmpdFragmentPChem.$varName}">
		<f:convertNumber pattern="#0.00" />
		</h:outputText>
		];
		
		print HIBERNATEPART qq[		
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
		print JAVAPART "private $varType $varName;\n";
		
		print FORMPART qq[
		<p:panel header="$varName" style="display:inline-block;border: none;">
		<h:outputLabel for="$varName\_id" value="$varName"/>
		<br/>
		<h:inputText value="#{pChemBean.$varName}" id="$varName\_id" cols="15" rows="5"/>                
		</p:panel>
		];
		
		print OUTPUTPARTCOLUMNS qq[
		<p:column headerText="$varName" resizeable="true" sortBy="#{listMember.cmpd.parentFragment.cmpdFragmentPChem.$varName}">                
		<h:outputText value="#{listMember.cmpd.parentFragment.cmpdFragmentPChem.$varName}" />
		</p:column>
		];
		
		print OUTPUTPARTLABELVALUE qq[
		<p:outputLabel value="$varName"/>
		<h:outputText value="#{listMember.cmpd.parentFragment.cmpdFragmentPChem.$varName}" />
		];
		
		print HIBERNATEPART qq[		
		if (this.$varName != null){
		// eq
		}
		];
		
		
	}
	
}

#print "$strcVars\n";
#print "$nscCmpdVars\n";

close JAVAPART;
close FORMPART;
close OUTPUTPARTCOLUMNS;
close OUTPUTPARTLABELVALUE;
close HIBERNATEPART;

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


