package CATEGORYPARAMETERS;

use strict;
use Exporter;
use vars qw($VERSION @ISA @EXPORT @EXPORT_OK);

$VERSION     = 1.00;
@ISA         = qw(Exporter);
@EXPORT      = ();
@EXPORT_OK   = qw(getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);


my @categoryCriteriaList;
push(@categoryCriteriaList,"one_strc	=1	=0	=1	=0");
push(@categoryCriteriaList,"one_strc_salt	=1	=0	=1	=1");
push(@categoryCriteriaList,"one_lbl	=1	=1	=0	=0");
push(@categoryCriteriaList,"multi_lbl	>1	>1	=0	=0");
push(@categoryCriteriaList,"one_strc_one_lbl	=2	=1	=1	=0");
push(@categoryCriteriaList,"one_strc_multi_lbl	>1	>1	=1	=0");
push(@categoryCriteriaList,"two_strc_no_lbl_one_salt	=2	=0	=2	=1");
push(@categoryCriteriaList,"two_strc_no_lbl_two_salt	=2	=0	=2	=2");
push(@categoryCriteriaList,"two_strc_any_lbl_one_salt	>2	>0	=2	=1");
push(@categoryCriteriaList,"two_strc_any_lbl_two_salt	>2	>0	=2	=2");
push(@categoryCriteriaList,"two_strc_any_lbl	>2	>0	=2	=0");
push(@categoryCriteriaList,"mult_strc_no_lbl	>2	=0	>2	=0");
push(@categoryCriteriaList,"multi_strc_any_lbl	>2	>0	>2	=0");

my @categoryList;
foreach my $thisCatCrit (@categoryCriteriaList){	  
  my ($category, $count_frags, $count_lbl, $count_strc,	$salt) = split(/\t/, $thisCatCrit);  
  push(@categoryList, $category);  
}

my @qualityControlCriteriaList;
push(@qualityControlCriteriaList,"count nsc,1=1");
push(@qualityControlCriteriaList,"mw DIFF 0,round(mw) = round(fw)");
push(@qualityControlCriteriaList,"mw DIFF 1,abs( round(mw) - round(fw) ) >1");
push(@qualityControlCriteriaList,"mw DIFF 2,abs( round(mw) - round(fw) ) >2");
push(@qualityControlCriteriaList,"mw DIFF 5,abs( round(mw) - round(fw) ) >5");
push(@qualityControlCriteriaList,"mf with .,mf like '%.%'");
push(@qualityControlCriteriaList,"mf W99,mf = 'W99'");
push(@qualityControlCriteriaList,"mf ~ .*\(.*\)[xn0-9],mf ~ '.*\\(.*\\)[xn0-9]'");

sub getCategoryCriteriaList { return @categoryCriteriaList; }
sub getCategoryList  { return @categoryList; }
sub getQualityControlCriteriaList  { return @qualityControlCriteriaList; }

1;