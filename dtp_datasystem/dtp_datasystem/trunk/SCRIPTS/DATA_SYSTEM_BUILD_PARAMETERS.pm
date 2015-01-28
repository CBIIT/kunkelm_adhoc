package DATA_SYSTEM_BUILD_PARAMETERS;

use strict;
use Exporter;
use vars qw($VERSION @ISA @EXPORT @EXPORT_OK);

$VERSION     = 1.00;
@ISA         = qw(Exporter);
@EXPORT      = ();
@EXPORT_OK   = qw(getDatasystemTableList getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);


my @datasystemTableList;

push @datasystemTableList, "ad_hoc_cmpd";
push @datasystemTableList, "ad_hoc_cmpd_fragment";
push @datasystemTableList, "ad_hoc_cmpd_fragment_p_chem";
push @datasystemTableList, "ad_hoc_cmpd_fragment_structure";
push @datasystemTableList, "cmpd";
push @datasystemTableList, "cmpd_alias";
push @datasystemTableList, "cmpd_alias_type";
push @datasystemTableList, "cmpd_aliases2nsc_cmpds";
push @datasystemTableList, "cmpd_annotation";
push @datasystemTableList, "cmpd_bio_assay";
push @datasystemTableList, "cmpd_fragment";
push @datasystemTableList, "cmpd_fragment_p_chem";
push @datasystemTableList, "cmpd_fragment_structure";
push @datasystemTableList, "cmpd_inventory";
push @datasystemTableList, "cmpd_known_salt";
push @datasystemTableList, "cmpd_legacy_cmpd";
push @datasystemTableList, "cmpd_list";
push @datasystemTableList, "cmpd_list_member";
push @datasystemTableList, "cmpd_named_set";
push @datasystemTableList, "cmpd_named_sets2nsc_cmpds";
push @datasystemTableList, "cmpd_plate";
push @datasystemTableList, "cmpd_plates2nsc_cmpds";
push @datasystemTableList, "cmpd_project";
push @datasystemTableList, "cmpd_projects2nsc_cmpds";
push @datasystemTableList, "cmpd_pub_chem_sid";
push @datasystemTableList, "cmpd_pub_chem_sids2nsc_cmpds";
push @datasystemTableList, "cmpd_related";
push @datasystemTableList, "cmpd_relation_type";
push @datasystemTableList, "cmpd_table";
push @datasystemTableList, "cmpd_target";
push @datasystemTableList, "cmpd_targets2nsc_cmpds";
push @datasystemTableList, "nsc_cmpd";
push @datasystemTableList, "nsc_cmpd_type";
push @datasystemTableList, "rdkit_mol";
# ancillary tables that need to be kept around
push @datasystemTableList, "create_constraint_statements";
push @datasystemTableList, "drop_constraint_statements";
push @datasystemTableList, "frag_stats";
push @datasystemTableList, "frag_stats_with_totals";
push @datasystemTableList, "nsc_to_load";
push @datasystemTableList, "missing_nsc";
push @datasystemTableList, "missing_nsc_frags";
push @datasystemTableList, "problem_nsc";
push @datasystemTableList, "qc";
push @datasystemTableList, "qc_overlap";
push @datasystemTableList, "qc_with_nsc";
push @datasystemTableList, "qc_ct";
push @datasystemTableList, "rdkit_validity";

my @categoryCriteriaList;

push @categoryCriteriaList,"no_lbl_no_strc_no_salt >0 =0 =0 =0";
push @categoryCriteriaList,"no_lbl_no_strc_one_salt >0 =0 =0 =1";
push @categoryCriteriaList,"no_lbl_no_strc_two_salt >0 =0 =0 =2";
push @categoryCriteriaList,"no_lbl_no_strc_multi_salt >0 =0 =0 >2";
push @categoryCriteriaList,"no_lbl_one_strc_no_salt >0 =0 =1 =0";
push @categoryCriteriaList,"no_lbl_one_strc_one_salt >0 =0 =1 =1";
push @categoryCriteriaList,"no_lbl_one_strc_two_salt >0 =0 =1 =2";
push @categoryCriteriaList,"no_lbl_one_strc_multi_salt >0 =0 =1 >2";
push @categoryCriteriaList,"no_lbl_two_strc_no_salt >0 =0 =2 =0";
push @categoryCriteriaList,"no_lbl_two_strc_one_salt >0 =0 =2 =1";
push @categoryCriteriaList,"no_lbl_two_strc_two_salt >0 =0 =2 =2";
push @categoryCriteriaList,"no_lbl_two_strc_multi_salt >0 =0 =2 >2";
push @categoryCriteriaList,"no_lbl_multi_strc_no_salt >0 =0 >2 =0";
push @categoryCriteriaList,"no_lbl_multi_strc_one_salt >0 =0 >2 =1";
push @categoryCriteriaList,"no_lbl_multi_strc_two_salt >0 =0 >2 =2";
push @categoryCriteriaList,"no_lbl_multi_strc_multi_salt >0 =0 >2 >2";
push @categoryCriteriaList,"one_lbl_no_strc_no_salt >0 =1 =0 =0";
push @categoryCriteriaList,"one_lbl_no_strc_one_salt >0 =1 =0 =1";
push @categoryCriteriaList,"one_lbl_no_strc_two_salt >0 =1 =0 =2";
push @categoryCriteriaList,"one_lbl_no_strc_multi_salt >0 =1 =0 >2";
push @categoryCriteriaList,"one_lbl_one_strc_no_salt >0 =1 =1 =0";
push @categoryCriteriaList,"one_lbl_one_strc_one_salt >0 =1 =1 =1";
push @categoryCriteriaList,"one_lbl_one_strc_two_salt >0 =1 =1 =2";
push @categoryCriteriaList,"one_lbl_one_strc_multi_salt >0 =1 =1 >2";
push @categoryCriteriaList,"one_lbl_two_strc_no_salt >0 =1 =2 =0";
push @categoryCriteriaList,"one_lbl_two_strc_one_salt >0 =1 =2 =1";
push @categoryCriteriaList,"one_lbl_two_strc_two_salt >0 =1 =2 =2";
push @categoryCriteriaList,"one_lbl_two_strc_multi_salt >0 =1 =2 >2";
push @categoryCriteriaList,"one_lbl_multi_strc_no_salt >0 =1 >2 =0";
push @categoryCriteriaList,"one_lbl_multi_strc_one_salt >0 =1 >2 =1";
push @categoryCriteriaList,"one_lbl_multi_strc_two_salt >0 =1 >2 =2";
push @categoryCriteriaList,"one_lbl_multi_strc_multi_salt >0 =1 >2 >2";
push @categoryCriteriaList,"two_lbl_no_strc_no_salt >0 =2 =0 =0";
push @categoryCriteriaList,"two_lbl_no_strc_one_salt >0 =2 =0 =1";
push @categoryCriteriaList,"two_lbl_no_strc_two_salt >0 =2 =0 =2";
push @categoryCriteriaList,"two_lbl_no_strc_multi_salt >0 =2 =0 >2";
push @categoryCriteriaList,"two_lbl_one_strc_no_salt >0 =2 =1 =0";
push @categoryCriteriaList,"two_lbl_one_strc_one_salt >0 =2 =1 =1";
push @categoryCriteriaList,"two_lbl_one_strc_two_salt >0 =2 =1 =2";
push @categoryCriteriaList,"two_lbl_one_strc_multi_salt >0 =2 =1 >2";
push @categoryCriteriaList,"two_lbl_two_strc_no_salt >0 =2 =2 =0";
push @categoryCriteriaList,"two_lbl_two_strc_one_salt >0 =2 =2 =1";
push @categoryCriteriaList,"two_lbl_two_strc_two_salt >0 =2 =2 =2";
push @categoryCriteriaList,"two_lbl_two_strc_multi_salt >0 =2 =2 >2";
push @categoryCriteriaList,"two_lbl_multi_strc_no_salt >0 =2 >2 =0";
push @categoryCriteriaList,"two_lbl_multi_strc_one_salt >0 =2 >2 =1";
push @categoryCriteriaList,"two_lbl_multi_strc_two_salt >0 =2 >2 =2";
push @categoryCriteriaList,"two_lbl_multi_strc_multi_salt >0 =2 >2 >2";
push @categoryCriteriaList,"multi_lbl_no_strc_no_salt >0 >2 =0 =0";
push @categoryCriteriaList,"multi_lbl_no_strc_one_salt >0 >2 =0 =1";
push @categoryCriteriaList,"multi_lbl_no_strc_two_salt >0 >2 =0 =2";
push @categoryCriteriaList,"multi_lbl_no_strc_multi_salt >0 >2 =0 >2";
push @categoryCriteriaList,"multi_lbl_one_strc_no_salt >0 >2 =1 =0";
push @categoryCriteriaList,"multi_lbl_one_strc_one_salt >0 >2 =1 =1";
push @categoryCriteriaList,"multi_lbl_one_strc_two_salt >0 >2 =1 =2";
push @categoryCriteriaList,"multi_lbl_one_strc_multi_salt >0 >2 =1 >2";
push @categoryCriteriaList,"multi_lbl_two_strc_no_salt >0 >2 =2 =0";
push @categoryCriteriaList,"multi_lbl_two_strc_one_salt >0 >2 =2 =1";
push @categoryCriteriaList,"multi_lbl_two_strc_two_salt >0 >2 =2 =2";
push @categoryCriteriaList,"multi_lbl_two_strc_multi_salt >0 >2 =2 >2";
push @categoryCriteriaList,"multi_lbl_multi_strc_no_salt >0 >2 >2 =0";
push @categoryCriteriaList,"multi_lbl_multi_strc_one_salt >0 >2 >2 =1";
push @categoryCriteriaList,"multi_lbl_multi_strc_two_salt >0 >2 >2 =2";
push @categoryCriteriaList,"multi_lbl_multi_strc_multi_salt >0 >2 >2 >2";

my @categoryList;
foreach my $thisCatCrit (@categoryCriteriaList){	  
  my ($category, $count_frags, $count_lbl, $count_strc,	$salt) = split(/ /, $thisCatCrit);  
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

sub getDatasystemTableList { return @datasystemTableList; }
sub getCategoryCriteriaList { return @categoryCriteriaList; }
sub getCategoryList  { return @categoryList; }
sub getQualityControlCriteriaList  { return @qualityControlCriteriaList; }

1;