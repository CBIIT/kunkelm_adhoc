/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mwkunkel
 */
public class GrantInfo {

  public static final String TBLNONE = "none";
  public static final String TBLSEL = "select";
  public static final String TBLSELINS = "select, insert";
  public static final String TBLSELINSUPD = "select, insert, update";

  public static final String SEQNONE = "none";
  public static final String SEQSELUSE = "select, usage";
  public static final String SEQSELUSEUPD = "select, usage, update";

  public List<String> tableGrantSelect;
  public List<String> tableGrantSelectInsert;
  public List<String> tableGrantSelectInsertUpdate;
  public List<String> tableGrantNone;

  public List<String> sequenceGrantSelectUsage;
  public List<String> sequenceGrantSelectUsageUpdate;
  public List<String> sequenceGrantNone;

  public GrantInfo() {
    tableGrantSelect = new ArrayList<String>();
    tableGrantSelectInsert = new ArrayList<String>();
    tableGrantSelectInsertUpdate = new ArrayList<String>();
    tableGrantNone = new ArrayList<String>();

    sequenceGrantSelectUsage = new ArrayList<String>();
    sequenceGrantSelectUsageUpdate = new ArrayList<String>();
    sequenceGrantNone = new ArrayList<String>();
  }

//  public void printGrants(String whom) {
//    
//    for (String s : tableGrantSelect) {
//      System.out.println("revoke all on " + s + " from " + whom);
//      System.out.println("grant select on " + s + " to " + whom);
//    }
//    for (String s : tableGrantSelectInsert) {
//      System.out.println("revoke all on " + s + " from " + whom);
//      System.out.println("grant select, insert on " + s + " to " + whom);
//    }
//    for (String s : tableGrantSelectInsertUpdate) {
//      System.out.println("revoke all on " + s + " from " + whom);
//      System.out.println("grant select, insert, update on " + s + " to " + whom);
//    }
//    for (String s : tableGrantNone) {
//      System.out.println("revoke all on " + s + " from " + whom);      
//    }
//
//    for (String s : sequenceGrantSelectUsage) {
//      System.out.println("revoke all on " + s + " from " + whom);
//      System.out.println("grant select, usage on " + s + " to " + whom);
//    }
//    for (String s : sequenceGrantSelectUsageUpdate) {
//      System.out.println("revoke all on " + s + " from " + whom);
//      System.out.println("grant select, usage, update on " + s + " to " + whom);
//    }
//    for (String s : sequenceGrantNone) {
//      System.out.println("revoke all on " + s + " from " + whom);      
//    }
//
//  }
  
  public List<String> listGrants(String whom) {
    
    List<String> grantList = new ArrayList<String>();
    
    for (String s : tableGrantSelect) {
      grantList.add("revoke all on " + s + " from " + whom);
      grantList.add("grant select on " + s + " to " + whom);
    }
    for (String s : tableGrantSelectInsert) {
      grantList.add("revoke all on " + s + " from " + whom);
      grantList.add("grant select, insert on " + s + " to " + whom);
    }
    for (String s : tableGrantSelectInsertUpdate) {
      grantList.add("revoke all on " + s + " from " + whom);
      grantList.add("grant select, insert, update on " + s + " to " + whom);
    }
    for (String s : tableGrantNone) {
      grantList.add("revoke all on " + s + " from " + whom);
    }

    for (String s : sequenceGrantSelectUsage) {
      grantList.add("revoke all on " + s + " from " + whom);
      grantList.add("grant select, usage on " + s + " to " + whom);
    }
    for (String s : sequenceGrantSelectUsageUpdate) {
      grantList.add("revoke all on " + s + " from " + whom);
      grantList.add("grant select, usage, update on " + s + " to " + whom);
    }
    for (String s : sequenceGrantNone) {
      grantList.add("revoke all on " + s + " from " + whom);
    }

    return grantList;
    
  }

}

/*

 #####     ##     #####    ##     ####    #   #   ####    #####  ######  #    #
 #    #   #  #      #     #  #   #         # #   #          #    #       ##  ##
 #    #  #    #     #    #    #   ####      #     ####      #    #####   # ## #
 #    #  ######     #    ######       #     #         #     #    #       #    #
 #    #  #    #     #    #    #  #    #     #    #    #     #    #       #    #
 #####   #    #     #    #    #   ####      #     ####      #    ######  #    #

                     List of relations
 Schema |              Name              | Type  |  Owner   
--------+--------------------------------+-------+----------
 public | ad_hoc_cmpd                    | table | mwkunkel
 public | ad_hoc_cmpd_fragment           | table | mwkunkel
 public | ad_hoc_cmpd_fragment_p_chem    | table | mwkunkel
 public | ad_hoc_cmpd_fragment_structure | table | mwkunkel
 public | aliases2curated_nsc_to_aliases | table | mwkunkel
 public | cmpd                           | table | mwkunkel
 public | cmpd_alias                     | table | mwkunkel
 public | cmpd_alias_type                | table | mwkunkel
 public | cmpd_aliases2nsc_cmpds         | table | mwkunkel
 public | cmpd_annotation                | table | mwkunkel
 public | cmpd_bio_assay                 | table | mwkunkel
 public | cmpd_fragment                  | table | mwkunkel
 public | cmpd_fragment_p_chem           | table | mwkunkel
 public | cmpd_fragment_structure        | table | mwkunkel
 public | cmpd_fragment_type             | table | mwkunkel
 public | cmpd_inventory                 | table | mwkunkel
 public | cmpd_known_salt                | table | mwkunkel
 public | cmpd_legacy_cmpd               | table | mwkunkel
 public | cmpd_list                      | table | mwkunkel
 public | cmpd_list_member               | table | mwkunkel
 public | cmpd_named_set                 | table | mwkunkel
 public | cmpd_named_sets2nsc_cmpds      | table | mwkunkel
 public | cmpd_plate                     | table | mwkunkel
 public | cmpd_plates2nsc_cmpds          | table | mwkunkel
 public | cmpd_project                   | table | mwkunkel
 public | cmpd_projects2nsc_cmpds        | table | mwkunkel
 public | cmpd_pub_chem_sid              | table | mwkunkel
 public | cmpd_pub_chem_sids2nsc_cmpds   | table | mwkunkel
 public | cmpd_related                   | table | mwkunkel
 public | cmpd_relation_type             | table | mwkunkel
 public | cmpd_table                     | table | mwkunkel
 public | cmpd_target                    | table | mwkunkel
 public | cmpd_targets2nsc_cmpds         | table | mwkunkel
 public | curated_name                   | table | mwkunkel
 public | curated_nsc                    | table | mwkunkel
 public | curated_nsc_to_secondary_targe | table | mwkunkel
 public | curated_nscs2projects          | table | mwkunkel
 public | curated_originator             | table | mwkunkel
 public | curated_project                | table | mwkunkel
 public | curated_target                 | table | mwkunkel
 public | nsc_cmpd                       | table | mwkunkel
 public | nsc_cmpd_type                  | table | mwkunkel
 public | rdkit_mol                      | table | mwkunkel
 public | tanimoto_scores                | table | mwkunkel
(44 rows)

                       List of relations
 Schema |              Name              |   Type   |  Owner   
--------+--------------------------------+----------+----------
 public | ad_hoc_cmpd_fragment_p_che_seq | sequence | mwkunkel
 public | ad_hoc_cmpd_fragment_seq       | sequence | mwkunkel
 public | ad_hoc_cmpd_fragment_struc_seq | sequence | mwkunkel
 public | cmpd_alias_seq                 | sequence | mwkunkel
 public | cmpd_alias_type_seq            | sequence | mwkunkel
 public | cmpd_bio_assay_seq             | sequence | mwkunkel
 public | cmpd_fragment_p_chem_seq       | sequence | mwkunkel
 public | cmpd_fragment_seq              | sequence | mwkunkel
 public | cmpd_fragment_structure_seq    | sequence | mwkunkel
 public | cmpd_inventory_seq             | sequence | mwkunkel
 public | cmpd_known_salt_seq            | sequence | mwkunkel
 public | cmpd_legacy_cmpd_seq           | sequence | mwkunkel
 public | cmpd_list_member_seq           | sequence | mwkunkel
 public | cmpd_list_seq                  | sequence | mwkunkel
 public | cmpd_named_set_seq             | sequence | mwkunkel
 public | cmpd_plate_seq                 | sequence | mwkunkel
 public | cmpd_project_seq               | sequence | mwkunkel
 public | cmpd_pub_chem_sid_seq          | sequence | mwkunkel
 public | cmpd_related_seq               | sequence | mwkunkel
 public | cmpd_relation_type_seq         | sequence | mwkunkel
 public | cmpd_seq                       | sequence | mwkunkel
 public | cmpd_target_seq                | sequence | mwkunkel
 public | curated_name_seq               | sequence | mwkunkel
 public | curated_nsc_seq                | sequence | mwkunkel
 public | curated_originator_seq         | sequence | mwkunkel
 public | curated_project_seq            | sequence | mwkunkel
 public | curated_target_seq             | sequence | mwkunkel
(27 rows)


  ####    ####   #    #  #####     ##    #####   ######
 #    #  #    #  ##  ##  #    #   #  #   #    #  #
 #       #    #  # ## #  #    #  #    #  #    #  #####
 #       #    #  #    #  #####   ######  #####   #
 #    #  #    #  #    #  #       #    #  #   #   #
  ####    ####   #    #  #       #    #  #    #  ######


                     List of relations
 Schema |              Name              | Type  |  Owner   
--------+--------------------------------+-------+----------
 public | affy_dna_ident                 | table | mwkunkel
 public | affy_exon_ident                | table | mwkunkel
 public | build_date                     | table | mwkunkel
 public | cell_line_data_set             | table | mwkunkel
 public | cell_line_data_set_ident       | table | mwkunkel
 public | cell_line_data_sets2named_targ | table | mwkunkel
 public | compare_cell_line              | table | mwkunkel
 public | compare_result                 | table | mwkunkel
 public | conc_resp_assay                | table | mwkunkel
 public | conc_resp_element              | table | mwkunkel
 public | davies_garraway_ident          | table | mwkunkel
 public | dtp_cell_line_data_set         | table | mwkunkel
 public | dtp_test_result                | table | mwkunkel
 public | grid_compare_columns           | table | mwkunkel
 public | grid_compare_job               | table | mwkunkel
 public | grid_compare_rows              | table | mwkunkel
 public | hgu133                         | table | mwkunkel
 public | ignore_cell_lines2job_for_ign_ | table | mwkunkel
 public | israel_ident                   | table | mwkunkel
 public | job                            | table | mwkunkel
 public | job_for_req_cell_lines2require | table | mwkunkel
 public | micro_array_ident              | table | mwkunkel
 public | micro_rna_ident                | table | mwkunkel
 public | mol_targ_catch_all_ident       | table | mwkunkel
 public | mol_targ_ident                 | table | mwkunkel
 public | named_target_set               | table | mwkunkel
 public | nano_string_ident              | table | mwkunkel
 public | nat_prod_ident                 | table | mwkunkel
 public | nsc_alias                      | table | mwkunkel
 public | nsc_compound                   | table | mwkunkel
 public | nsc_ident                      | table | mwkunkel
 public | petricoin_ident                | table | mwkunkel
 public | protein_ident                  | table | mwkunkel
 public | standard_compare_job           | table | mwkunkel
 public | synthetic_ident                | table | mwkunkel
 public | test_result                    | table | mwkunkel
 public | test_result_type               | table | mwkunkel
 public | uploaded_cell_line_data_set    | table | mwkunkel
 public | uploaded_ident                 | table | mwkunkel
 public | uploaded_test_result           | table | mwkunkel
 public | weinstein_croce_ident          | table | mwkunkel
(41 rows)

                      List of relations
 Schema |             Name             |   Type   |  Owner   
--------+------------------------------+----------+----------
 public | cell_line_data_set_ident_seq | sequence | mwkunkel
 public | cell_line_data_set_seq       | sequence | mwkunkel
 public | compare_cell_line_seq        | sequence | mwkunkel
 public | compare_result_seq           | sequence | mwkunkel
 public | conc_resp_assay_seq          | sequence | mwkunkel
 public | conc_resp_element_seq        | sequence | mwkunkel
 public | job_seq                      | sequence | mwkunkel
 public | named_target_set_seq         | sequence | mwkunkel
 public | nsc_compound_seq             | sequence | mwkunkel
 public | test_result_seq              | sequence | mwkunkel
 public | test_result_type_seq         | sequence | mwkunkel
(11 rows)

*/