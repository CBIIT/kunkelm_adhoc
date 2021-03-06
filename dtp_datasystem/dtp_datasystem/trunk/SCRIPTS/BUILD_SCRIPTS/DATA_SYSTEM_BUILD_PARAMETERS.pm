package DATA_SYSTEM_BUILD_PARAMETERS;

use strict;
use Exporter;
use vars qw($VERSION @ISA @EXPORT @EXPORT_OK);

$VERSION     = 1.00;
@ISA         = qw(Exporter);
@EXPORT      = ();
@EXPORT_OK   = qw(getDatasystemTableList getCategoryCriteriaList getQualityControlCriteriaList getCategoryList);

# datasystem domain tables

my @datasystemTableList = ();

my @datasystemTableList = qw[
ad_hoc_cmpd
ad_hoc_cmpd_fragment
ad_hoc_cmpd_fragment_p_chem
ad_hoc_cmpd_fragment_structure
aliases2curated_nsc_to_aliases
cmpd
cmpd_alias
cmpd_alias_type
cmpd_aliases2nsc_cmpds
cmpd_annotation
cmpd_bio_assay
cmpd_fragment
cmpd_fragment_p_chem
cmpd_fragment_structure
cmpd_fragment_type
cmpd_inventory
cmpd_known_salt
cmpd_legacy_cmpd
cmpd_list
cmpd_list_member
cmpd_named_set
cmpd_named_sets2nsc_cmpds
cmpd_plate
cmpd_plates2nsc_cmpds
cmpd_project
cmpd_projects2nsc_cmpds
cmpd_pub_chem_sid
cmpd_pub_chem_sids2nsc_cmpds
cmpd_related
cmpd_relation_type
cmpd_table
cmpd_target
cmpd_targets2nsc_cmpds
curated_name
curated_nsc
curated_nsc_to_secondary_targe
curated_nscs2projects
curated_originator
curated_project
curated_target
nsc_cmpd
nsc_cmpd_type
rdkit_mol
tanimoto_scores
];

# ancillary tables that ought to be kept around
#push @datasystemTableList, "create_constraint_statements";
#push @datasystemTableList, "drop_constraint_statements";
#push @datasystemTableList, "frag_stats";
#push @datasystemTableList, "frag_stats_with_totals";
#push @datasystemTableList, "nsc_to_load";
#push @datasystemTableList, "missing_nsc";
#push @datasystemTableList, "missing_nsc_frags";
#push @datasystemTableList, "problem_nsc";
#push @datasystemTableList, "qc";
#push @datasystemTableList, "qc_overlap";
#push @datasystemTableList, "qc_with_nsc";
#push @datasystemTableList, "qc_ct";
#push @datasystemTableList, "rdkit_validity";

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
  my ($category, $count_frags, $count_lbl, $count_strc,	$count_rgrp, $count_salt) = split(/ /, $thisCatCrit);  
  push(@categoryList, $category);  
}

my @qcCritList;

# all nsc, no filter
push(@qcCritList,"count nsc,1=1");

# filter by diff_mw prod vs. build
push(@qcCritList,"diff_mw 0.0-0.2,abs(prod_mw-fw ) <= 0.2");
push(@qcCritList,"diff_mw 0.0-0.2 Hx hal,abs(prod_mw-fw ) <= 0.2 and prod_mf ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH)'");

push(@qcCritList,"diff_mw 0.2-1.0,abs(prod_mw-fw) > 0.2 and abs(prod_mw-fw) <= 1.0");
push(@qcCritList,"diff_mw 0.2-1.0 Hx hal,abs(prod_mw-fw) > 0.2 and abs(prod_mw-fw) <= 1.0 and prod_mf ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH)'");

push(@qcCritList,"diff_mw 1.0-2.0,abs(prod_mw-fw) > 1.0 and abs(prod_mw-fw) <= 2.0");
push(@qcCritList,"diff_mw 1.0-2.0 Hx hal,abs(prod_mw-fw) > 1.0 and abs(prod_mw-fw) <= 2.0 and prod_mf ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH)'");

push(@qcCritList,"diff_mw 2.0-3.0,abs(prod_mw-fw) > 2.0 and abs(prod_mw-fw) <= 3.0");
push(@qcCritList,"diff_mw 2.0-3.0 Hx hal,abs(prod_mw-fw) > 2.0 and abs(prod_mw-fw) <= 3.0 and prod_mf ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH)'");

push(@qcCritList,"diff_mw 3.0-4.0,abs(prod_mw-fw) > 3.0 and abs(prod_mw-fw) <= 4.0");
push(@qcCritList,"diff_mw 3.0-4.0 Hx hal,abs(prod_mw-fw) > 3.0 and abs(prod_mw-fw) <= 4. and prod_mf ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH)'");

push(@qcCritList,"diff_mw 4.0-5.0,abs(prod_mw-fw) > 4.0 and abs(prod_mw-fw) <= 5.0");
push(@qcCritList,"diff_mw 4.0-5.0 Hx hal,abs(prod_mw-fw) > 4.0 and abs(prod_mw-fw) <= 5.0 and prod_mf ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH)'");

push(@qcCritList,"diff_mw 5.0-inf,abs(prod_mw-fw) > 5.0");
push(@qcCritList,"diff_mw 5.0-inf Hx hal,abs(prod_mw-fw) > 5.0 and prod_mf ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH)'");

# categories of prod_md
push(@qcCritList,"prod_mf with .,prod_mf like '%.%'");
push(@qcCritList,"prod_mf W99,prod_mf = 'W99'");
push(@qcCritList,"prod_mf ~ .*\(.*\)[xn0-9],prod_mf ~ '.*\\(.*\\)[xn0-9]'");
push(@qcCritList,"prod_mf halogen Hx or xH,prod_mf ~ '(\.HCl|\.ClH|\.HBr|\.BrH|\.HI|\.IH)'");

push(@qcCritList,"r groups in output, mf like '%R#%'");

push(@qcCritList,"formal_charge != 0,formal_charge != 0");

sub getDatasystemTableList { return @datasystemTableList; }
sub getCategoryCriteriaList { return @categoryCriteriaList; }
sub getCategoryList  { return @categoryList; }
sub getQualityControlCriteriaList  { return @qcCritList; }

1;