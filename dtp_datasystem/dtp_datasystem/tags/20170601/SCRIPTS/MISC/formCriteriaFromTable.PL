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
Double|molecularWeight|Molecular Weight
String|molecularFormula|Molecular Formula
Integer|countHydBondAcceptors|HBond Acceptors count
Integer|countHydBondDonors|HBond Donors count
Integer|formalCharge|Formal Charge
Double|theALogP|aLogP
Double|logD|logD
Double|surfaceArea|Surface Area
Double|solubility|Solubility
Integer|countAtoms|Atoms count
Integer|countHydrogenAtoms|Atoms Hydrogen count
Integer|countMetalAtoms|Atoms Metal count
Integer|countHeavyAtoms|Atoms Heavy count
Integer|countPositiveAtoms|Atoms Positive count
Integer|countNegativeAtoms|Atoms Negative count
Integer|countStereoAtoms|Atoms Stereo count
Integer|countBonds|Bonds count
Integer|countAromaticBonds|Bonds Aromatic count
Integer|countSingleBonds|Bonds Single count
Integer|countDoubleBonds|Bonds Double count
Integer|countTripleBonds|Bonds Triple count
Integer|countRotatableBonds|Bonds Rotatable count
Integer|countRingBonds|Bonds Ring count
Integer|countStereoBonds|Bonds Stereo count
Integer|countAromaticRings|Rings Aromatic count
Integer|countRingAssemblies|Ring Assemblies count
Integer|countRings|Rings count
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
open ( FORM_PART, ">FORM_PART") or die "Can't open FORM_PART\n";
open ( outputPartColumnFormat, ">outputPartColumnFormat") or die "Can't open outputPartColumnFormat\n";
open ( outputPartLabelValue, ">outputPartLabelValue") or die "Can't open outputPartLabelValue\n";
open ( outputPartAddToColumnModelList, ">outputPartAddToColumnModelList") or die "Can't open outputPartAddToColumnModelList\n";
open ( hibernatePart, ">hibernatePart") or die "Can't open hibernatePart\n";

#--------------------------------------------------------------------
#--------------------------------------------------------------------
#--------------------------------------------------------------------

my @cumulativeFieldNames = ();

#MWK for now, only pChemVars foreach my $dataSet($pChemVars, $strcVars, $nscCmpdVars){

foreach my $dataSet($pChemVars){
	
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
  
  my @lines = split(/\n/, $dataSet);
  
  foreach my $line(@lines){
  	
  	chomp($line);
  	
  	my @splitLine = split(/\|/, $line);
  	my $varType = $splitLine[0];
  	chomp($varType);
  	my $varName = $splitLine[1];
  	chomp($varName);
    my $displayName = $splitLine[2];
    chomp($displayName);
  	print "$varType --- $varName --- $displayName\n";
  	
  	if ( $varType eq "Integer"){
  		
  		print "---------min_$varName max_$varName\n";
  		
  		print javaPart "private $varType min_$varName;\n";
  		print javaPart "private $varType max_$varName;\n";
  		
  		print FORM_PART qq[
  		<p:panel header="$displayName" style="display:inline-block;border: none;">
  		<h:outputLabel for="min_$varName\_id" value="$displayName MIN"/>
  		<br/>
  		<h:inputText value="#{listContentController.searchCriteriaBean.min_$varName}" id="min_$varName\_id"/>
  		<br/>
  		<h:outputLabel for="max_$varName\_id" value="$displayName MAX"/>
  		<br/>
  		<h:inputText value="#{listContentController.searchCriteriaBean.max_$varName}" id="max_$varName\_id"/>
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
  		f = scbClass.getDeclaredField("min_$varName");
  		f.setAccessible(true);                        
  		minInt = (Integer)f.get(qo);
  		
  		f = scbClass.getDeclaredField("max_$varName");
  		f.setAccessible(true);
  		maxInt = (Integer)f.get(qo);
  		
  		if (minInt != null && maxInt != null) {
  		if (minInt.compareTo(maxInt) == 0) {
  		if (minInt - 0 != 0) {
  		c.add(Restrictions.eq("pchem.$varName", minInt));
  		}
  		} else {
  		// between
  		c.add(Restrictions.between("pchem.$varName", minInt, maxInt));
  		}
  		} else if (minInt != null) {
  		// gt min
  		c.add(Restrictions.gt("pchem.$varName", minInt));
  		} else if (maxInt != null) {
  		// lt max
  		c.add(Restrictions.lt("pchem.$varName", maxInt));
  		}  		
  		];
  		
  	} elsif ($varType eq "Double"){
  		
  		print "---------min_$varName max_$varName\n";
  		
  		print javaPart "private $varType min_$varName;\n";
  		print javaPart "private $varType max_$varName;\n";
  		
  		print FORM_PART qq[
  		<p:panel header="$displayName" style="display:inline-block;border: none;">
  		<h:outputLabel for="min_$varName\_id" value="$displayName MIN"/>
  		<br/>
  		<h:inputText value="#{listContentController.searchCriteriaBean.min_$varName}" id="min_$varName\_id"/>
  		<br/>
  		<h:outputLabel for="max_$varName\_id" value="$displayName MAX"/>
  		<br/>
  		<h:inputText value="#{listContentController.searchCriteriaBean.max_$varName}" id="max_$varName\_id"/>
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
  		f = scbClass.getDeclaredField("min_$varName");
  		f.setAccessible(true);                        
  		minDbl = (Double)f.get(qo);
  		
  		f = scbClass.getDeclaredField("max_$varName");
  		f.setAccessible(true);
  		maxDbl = (Double)f.get(qo);
  		
  		if (minDbl != null && maxDbl != null) {
  		if (minDbl.compareTo(maxDbl) == 0) {
  		if (minDbl - 0 != 0) {
  		c.add(Restrictions.eq("pchem.$varName", minDbl));
  		}
  		} else {
  		// between
  		c.add(Restrictions.between("pchem.$varName", minDbl, maxDbl));
  		}
  		} else if (minDbl != null) {
  		// gt min
  		c.add(Restrictions.gt("pchem.$varName", minDbl));
  		} else if (maxDbl != null) {
  		// lt max
  		c.add(Restrictions.lt("pchem.$varName", maxDbl));
  		}
  		];
  		
  	} elsif ($varType eq "String"){
  		
  		print "---------$varName\n";
  		print javaPart "private $varType $varName;\n";
  		
  		print FORM_PART qq[
  		<p:panel header="$displayName" style="display:inline-block;border: none;">
  		<h:outputLabel for="$varName\_id" value="$varName"/>
  		<br/>
  		<h:inputText value="#{listContentController.searchCriteriaBean.$varName}" id="$varName\_id"/>                
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
  		if (scb.$varName != null){
  		// eq TODO handle this
  		}
  		];
  		
  	} else {
  		print "---------------------UNKNOWN varType---------->$varType<-----------\n";
  	}
  	
  	
  }
  
  print outputPartAddToColumnModelList join(', ', @cumulativeFieldNames);
  
}

close javaPart;
close FORM_PART;
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


