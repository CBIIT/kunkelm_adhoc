/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mwkunkel
 */
public class Configuration { 
    
  public static Map<String, ConnectionInfo> connectionMap = new HashMap<String, ConnectionInfo>();

  static {

    connectionMap.put("microxenodb_local", new ConnectionInfo(
            "microxenodb_local",
            "jdbc:postgresql://localhost:5432/microxenodb",
            "mwkunkel",
            "donkie11",
            Boolean.FALSE,
            Boolean.FALSE
    ));

    connectionMap.put("microxenodb_dev", new ConnectionInfo(
            "microxenodb_dev",
            "jdbc:postgresql://ncidb-d115-d.nci.nih.gov:5473/microxeno",
            "microxeno",
            "M1cr0x0293025en0",
            Boolean.FALSE,
            Boolean.FALSE
    ));
    
    connectionMap.put("publiccompoundsdb_local", new ConnectionInfo(
            "publiccompoundsdb_local",
            "jdbc:postgresql://localhost:5432/publiccompoundsdb",
            "mwkunkel",
            "donkie11",
            Boolean.FALSE,
            Boolean.TRUE
    ));

    connectionMap.put("datasystemdb_local", new ConnectionInfo(
            "datasystemdb_local",
            "jdbc:postgresql://localhost:5432/datasystemdb",
            "mwkunkel",
            "donkie11",
            Boolean.FALSE,
            Boolean.TRUE
    ));

    connectionMap.put("oncologydrugsdb_local", new ConnectionInfo(
            "oncologydrugsdb_local",
            "jdbc:postgresql://localhost:5432/oncologydrugsdb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.TRUE
    ));

    connectionMap.put("oncologydrugsdb_dev", new ConnectionInfo(
            "oncologydrugsdb_dev",
            "jdbc:postgresql://ncidb-d115-d:5474/oncology",
            "oncology",
            "OnC0L029302802K1t",
            Boolean.TRUE,
            Boolean.TRUE
    ));

    connectionMap.put("publiccomparedb_local", new ConnectionInfo(
            "publiccomparedb_local",
            "jdbc:postgresql://localhost:5432/publiccomparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("publiccomparedb_dev", new ConnectionInfo(
            "publiccomparedb_dev",
            "jdbc:postgresql://ncidb-d115-d:5473/pubcompare",
            "publiccompare",
            "P3b092094wlC0m3",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("privatecomparedb_local", new ConnectionInfo(
            "privatecomparedb_local",
            "jdbc:postgresql://localhost:5432/privatecomparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("comparedb_local", new ConnectionInfo(
            "privatecomparedb_local",
            "jdbc:postgresql://localhost:5432/comparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("sarcomacomparedb_local", new ConnectionInfo(
            "sarcomacomparedb_local",
            "jdbc:postgresql://localhost:5432/sarcomacomparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("zcomparedb_local", new ConnectionInfo(
            "zcomparedb_local",
            "jdbc:postgresql://localhost:5432/zcomparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("nineconccomparedb_local", new ConnectionInfo(
            "nineconccomparedb_local",
            "jdbc:postgresql://localhost:5432/nineconccomparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    Set<String> dbNameSet = connectionMap.keySet();
    ArrayList<String> dbNameList = new ArrayList<String>(dbNameSet);
    Collections.sort(dbNameList);

    for (String dbName : dbNameList) {
      connectionMap.get(dbName).asProperties();
    }

  }

  public static ArrayList<TblInfo> mx_tawc = new ArrayList<TblInfo>();

  static {

    mx_tawc.add(new TblInfo("affymetrix_Identifier", ""));
    mx_tawc.add(new TblInfo("tumor", ""));
    mx_tawc.add(new TblInfo("tumor_type", ""));
    mx_tawc.add(new TblInfo("flat_data", ""));

  }

  public static ArrayList<TblInfo> dataSystemTawc = new ArrayList<TblInfo>();

  static {

    dataSystemTawc.add(new TblInfo("ad_hoc_cmpd", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("ad_hoc_cmpd_fragment", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("ad_hoc_cmpd_fragment_p_chem", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("ad_hoc_cmpd_fragment_structure", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("cmpd_known_salt", ""));
    dataSystemTawc.add(new TblInfo("nsc_cmpd_type", ""));
    dataSystemTawc.add(new TblInfo("cmpd_alias_type", ""));
    dataSystemTawc.add(new TblInfo("cmpd_relation_type", ""));
    dataSystemTawc.add(new TblInfo("cmpd_fragment_type", ""));
    dataSystemTawc.add(new TblInfo("cmpd_inventory", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_annotation", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_bio_assay", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_legacy_cmpd", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_table", " where nsc in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("nsc_cmpd", " where nsc in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_fragment", " where nsc_cmpd_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_fragment_p_chem", " where id in (select cmpd_fragment_p_chem_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_fragment_structure", " where id in (select cmpd_fragment_structure_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_alias", " where id in (select cmpd_aliases_fk from cmpd_aliases2nsc_cmpds where cmpd_aliases2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_aliases2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_list", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("cmpd_list_member", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("cmpd_named_set", " where id in (select cmpd_named_sets_fk from cmpd_named_sets2nsc_cmpds where cmpd_named_sets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_named_sets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_plate", " where id in (select cmpd_plates_fk from cmpd_plates2nsc_cmpds where cmpd_plates2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_plates2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_project", " where id in (select cmpd_projects_fk from cmpd_projects2nsc_cmpds where cmpd_projects2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_projects2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_pub_chem_sid", " where id in (select cmpd_pub_chem_sids_fk from cmpd_pub_chem_sids2nsc_cmpds where cmpd_pub_chem_sids2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_pub_chem_sids2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_target", " where id in (select cmpd_targets_fk from cmpd_targets2nsc_cmpds where cmpd_targets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_targets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_related", " where nsc_cmpd_fk in (select nsc from nsc_for_export)"));

    // rdkit_mol has to be handled separately
//        dataSystemTawc.add(new TblInfo("rdkit_mol", " where nsc in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("tanimoto_scores", " where nsc1 in (select nsc from nsc_for_export) and nsc2 in (select nsc from nsc_for_export)"));
    /*        
   curated
     */
    dataSystemTawc.add(new TblInfo("curated_name", ""));
    dataSystemTawc.add(new TblInfo("curated_nsc", ""));
    dataSystemTawc.add(new TblInfo("curated_nsc_to_secondary_targe", ""));
    dataSystemTawc.add(new TblInfo("curated_nscs2projects", ""));
    dataSystemTawc.add(new TblInfo("curated_originator", ""));
    dataSystemTawc.add(new TblInfo("curated_project", ""));
    dataSystemTawc.add(new TblInfo("curated_target", ""));

  }

  public static ArrayList<TblInfo> compareTawc = new ArrayList<TblInfo>();

  static {

    compareTawc.add(new TblInfo("affy_dna_ident", " , cell_line_data_set_ident_for_export where affy_dna_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("affy_exon_ident", " , cell_line_data_set_ident_for_export where affy_exon_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("build_date", ""));
    compareTawc.add(new TblInfo("cell_line_data_sets2named_targ", " , named_target_set, target_set_names_for_export where cell_line_data_sets2named_targ.named_target_sets_fk = named_target_set.id and named_target_set.target_set_name = target_set_names_for_export.target_set_name"));
    compareTawc.add(new TblInfo("named_target_set", " , target_set_names_for_export where named_target_set.target_set_name = target_set_names_for_export.target_set_name"));
    compareTawc.add(new TblInfo("cell_line_data_set", " , cell_line_data_set_for_export where cell_line_data_set.id = cell_line_data_set_for_export.id"));
    compareTawc.add(new TblInfo("micro_array_ident", " , cell_line_data_set_ident_for_export where micro_array_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("micro_rna_ident", " , cell_line_data_set_ident_for_export where micro_rna_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("mol_targ_catch_all_ident", " , cell_line_data_set_ident_for_export where mol_targ_catch_all_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("mol_targ_ident", " , cell_line_data_set_ident_for_export where mol_targ_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("nano_string_ident", " , cell_line_data_set_ident_for_export where nano_string_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("nat_prod_ident", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("nsc_ident", " , cell_line_data_set_ident_for_export where nsc_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("synthetic_ident", " , cell_line_data_set_ident_for_export where synthetic_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("cell_line_data_set_ident", " , cell_line_data_set_ident_for_export where cell_line_data_set_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("compare_cell_line", ""));
    compareTawc.add(new TblInfo("compare_result", "DO NOT REPLICATE"));

    // open-ended by nsc
    compareTawc.add(new TblInfo("nsc_compound", " , nsc_for_export where nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc"));
    compareTawc.add(new TblInfo("conc_resp_assay", " , nsc_compound, nsc_for_export where nsc_compound_fk = nsc_compound.id and nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc and exp_id = 'AVGDATA'"));
    compareTawc.add(new TblInfo("conc_resp_element", " , conc_resp_assay, nsc_compound, nsc_for_export where conc_resp_element.conc_resp_assay_fk = conc_resp_assay.id and conc_resp_assay.nsc_compound_fk = nsc_compound.id and nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc and exp_id = 'AVGDATA'"));
    compareTawc.add(new TblInfo("dtp_cell_line_data_set", " , cell_line_data_set_for_export where dtp_cell_line_data_set.id = cell_line_data_set_for_export.id"));
    compareTawc.add(new TblInfo("dtp_test_result", " , test_result, cell_line_data_set_for_export where dtp_test_result.id = test_result.id and test_result.cell_line_data_set_fk = cell_line_data_set_for_export.id"));
    compareTawc.add(new TblInfo("grid_compare_columns", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("grid_compare_job", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("grid_compare_rows", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("job", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("job_for_req_cell_lines2require", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("ignore_cell_lines2job_for_ign_", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("standard_compare_job", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("test_result", " , cell_line_data_set_for_export where test_result.cell_line_data_set_fk = cell_line_data_set_for_export.id"));
    compareTawc.add(new TblInfo("test_result_type", ""));
    compareTawc.add(new TblInfo("uploaded_cell_line_data_set", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("uploaded_ident", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("uploaded_test_result", "DO NOT REPLICATE"));
  }

        
}
