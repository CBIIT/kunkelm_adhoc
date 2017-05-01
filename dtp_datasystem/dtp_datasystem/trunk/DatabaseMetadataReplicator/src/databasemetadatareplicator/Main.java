/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mwkunkel
 */
public class Main {
  
  public static final String ONC = "oncologydrugs data";
  public static final String PUB = "public data";
  public static final String PRIV = "private data";

  public static void main(String[] args) {

    ConnectionInfo srcCompareInfo = connectionMap.get("privatecomparedb_local");
    ConnectionInfo srcDataSystemInfo = connectionMap.get("datasystemdb_local");
    ConnectionInfo destCompareInfo = connectionMap.get("oncologydrugsdb_local");
    ConnectionInfo destDataSystemInfo = connectionMap.get("oncologydrugsdb_local");

    Connection srcCompareConn = null;
    Connection srcDataSystemConn = null;
    Connection destCompareConn = null;
    Connection destDataSystemConn = null;

    try {

      DriverManager.registerDriver(new org.postgresql.Driver());
      // DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

      srcCompareInfo.asProperties();
      srcDataSystemInfo.asProperties();
      destCompareInfo.asProperties();
      destDataSystemInfo.asProperties();

      srcCompareConn = DriverManager.getConnection(srcCompareInfo.dbUrl, srcCompareInfo.dbUser, srcCompareInfo.dbPass);
      srcDataSystemConn = DriverManager.getConnection(srcDataSystemInfo.dbUrl, srcCompareInfo.dbUser, srcCompareInfo.dbPass);
      destCompareConn = DriverManager.getConnection(destCompareInfo.dbUrl, destCompareInfo.dbUser, destCompareInfo.dbPass);
      destDataSystemConn = DriverManager.getConnection(destDataSystemInfo.dbUrl, destDataSystemInfo.dbUser, destDataSystemInfo.dbPass);

// building new db from the androMDA uml ddl sql
      processDbDdl();
// first thing, propagate CuratedNsc with both update flags set to FALSE
// so that it ONLY propagates the CuratedNscWithSmiles table
// note that this self-propagates to the srcDataSystemConn 
      propagateCuratedNsc(srcDataSystemConn, srcDataSystemConn, Boolean.FALSE, Boolean.FALSE);
      propagateCuratedNsc(srcDataSystemConn, srcCompareConn, Boolean.FALSE, Boolean.FALSE);
// fetch public_release from PROD to srcDataSystem and srcCompare 
// the PROD connection is hard-coded in the method...
      fetchPublicRelease(srcDataSystemConn);
      fetchPublicRelease(srcCompareConn);

// wrangle COMPARE contents

      prepareForCompareExport(srcCompareConn, ONC);
      nukeAllDestinationTables(srcCompareConn, destCompareConn, compareTawc);
      propagateCompare(srcCompareConn, destCompareConn, compareTawc);

      prepareForDataSystemExport(srcDataSystemConn, ONC);
      nukeAllDestinationTables(srcDataSystemConn, destDataSystemConn, dataSystemTawc);
      propagateDataSystem(srcDataSystemConn, destDataSystemConn, dataSystemTawc);
      populateRdkitMol(destDataSystemConn);

  // propagateCuratedNsc to the two destinations
  propagateCuratedNsc(srcDataSystemConn, destDataSystemConn, destDataSystemInfo.doCompareTables, destDataSystemInfo.doDataSystemTables);  
  propagateCuratedNsc(srcDataSystemConn, destCompareConn, destCompareInfo.doCompareTables, destCompareInfo.doDataSystemTables);
    
// updateSequences(destDataSystemConn);
      System.out.println("Done! in NewMain");

      srcCompareConn.close();
      srcCompareConn = null;
      srcDataSystemConn.close();
      srcDataSystemConn = null;
      destCompareConn.close();
      destCompareConn = null;
      destDataSystemConn.close();
      destDataSystemConn = null;

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in DatasystemReplicator.main");
      e.printStackTrace();
    } finally {
      if (srcCompareConn != null) {
        try {
          srcCompareConn.close();
          srcCompareConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing srcCompareConn " + ex);
        }
      }
      if (srcDataSystemConn != null) {
        try {
          srcDataSystemConn.close();
          srcDataSystemConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing srcDataSystemConn " + ex);
        }
      }
      if (destCompareConn != null) {
        try {
          destCompareConn.close();
          destCompareConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing destCompareConn " + ex);
        }
      }
      if (destDataSystemConn != null) {
        try {
          destDataSystemConn.close();
          destDataSystemConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing destDataSystemConn " + ex);
        }
      }
    }
  }

  static Map<String, ConnectionInfo> connectionMap = new HashMap<String, ConnectionInfo>();

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

  static ArrayList<TableAndWhereClause> mx_tawc = new ArrayList<TableAndWhereClause>();

  static {

    mx_tawc.add(new TableAndWhereClause("affymetrix_Identifier", ""));
    mx_tawc.add(new TableAndWhereClause("tumor", ""));
    mx_tawc.add(new TableAndWhereClause("tumor_type", ""));
    mx_tawc.add(new TableAndWhereClause("flat_data", ""));

  }

  static ArrayList<TableAndWhereClause> dataSystemTawc = new ArrayList<TableAndWhereClause>();

  static {

    dataSystemTawc.add(new TableAndWhereClause("ad_hoc_cmpd", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TableAndWhereClause("ad_hoc_cmpd_fragment", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TableAndWhereClause("ad_hoc_cmpd_fragment_p_chem", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TableAndWhereClause("ad_hoc_cmpd_fragment_structure", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_known_salt", ""));
    dataSystemTawc.add(new TableAndWhereClause("nsc_cmpd_type", ""));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_alias_type", ""));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_relation_type", ""));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_fragment_type", ""));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_inventory", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_annotation", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_bio_assay", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_legacy_cmpd", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_table", " where nsc in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("nsc_cmpd", " where nsc in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_fragment", " where nsc_cmpd_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_fragment_p_chem", " where id in (select cmpd_fragment_p_chem_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_fragment_structure", " where id in (select cmpd_fragment_structure_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_alias", " where id in (select cmpd_aliases_fk from cmpd_aliases2nsc_cmpds where cmpd_aliases2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_aliases2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_list", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_list_member", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_named_set", " where id in (select cmpd_named_sets_fk from cmpd_named_sets2nsc_cmpds where cmpd_named_sets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_named_sets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_plate", " where id in (select cmpd_plates_fk from cmpd_plates2nsc_cmpds where cmpd_plates2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_plates2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_project", " where id in (select cmpd_projects_fk from cmpd_projects2nsc_cmpds where cmpd_projects2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_projects2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_pub_chem_sid", " where id in (select cmpd_pub_chem_sids_fk from cmpd_pub_chem_sids2nsc_cmpds where cmpd_pub_chem_sids2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_pub_chem_sids2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_target", " where id in (select cmpd_targets_fk from cmpd_targets2nsc_cmpds where cmpd_targets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_targets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("cmpd_related", " where nsc_cmpd_fk in (select nsc from nsc_for_export)"));

    // rdkit_mol has to be handled separately
//        dataSystemTawc.add(new TableAndWhereClause("rdkit_mol", " where nsc in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TableAndWhereClause("tanimoto_scores", " where nsc1 in (select nsc from nsc_for_export) and nsc2 in (select nsc from nsc_for_export)"));
    /*
        
   curated   
        
     */
    dataSystemTawc.add(new TableAndWhereClause("curated_name", ""));
    dataSystemTawc.add(new TableAndWhereClause("curated_nsc", ""));
    dataSystemTawc.add(new TableAndWhereClause("curated_nsc_to_secondary_targe", ""));
    dataSystemTawc.add(new TableAndWhereClause("curated_nscs2projects", ""));
    dataSystemTawc.add(new TableAndWhereClause("curated_originator", ""));
    dataSystemTawc.add(new TableAndWhereClause("curated_project", ""));
    dataSystemTawc.add(new TableAndWhereClause("curated_target", ""));

  }

  static ArrayList<TableAndWhereClause> compareTawc = new ArrayList<TableAndWhereClause>();

  static {

    compareTawc.add(new TableAndWhereClause("affy_dna_ident", " , cell_line_data_set_ident_for_export where affy_dna_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TableAndWhereClause("affy_exon_ident", " , cell_line_data_set_ident_for_export where affy_exon_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TableAndWhereClause("build_date", ""));
    compareTawc.add(new TableAndWhereClause("cell_line_data_sets2named_targ", " , named_target_set, target_set_names_for_export where cell_line_data_sets2named_targ.named_target_sets_fk = named_target_set.id and named_target_set.target_set_name = target_set_names_for_export.target_set_name"));
    compareTawc.add(new TableAndWhereClause("named_target_set", " , target_set_names_for_export where named_target_set.target_set_name = target_set_names_for_export.target_set_name"));
    compareTawc.add(new TableAndWhereClause("cell_line_data_set", " , cell_line_data_set_for_export where cell_line_data_set.id = cell_line_data_set_for_export.id"));
    compareTawc.add(new TableAndWhereClause("micro_array_ident", " , cell_line_data_set_ident_for_export where micro_array_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TableAndWhereClause("micro_rna_ident", " , cell_line_data_set_ident_for_export where micro_rna_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TableAndWhereClause("mol_targ_catch_all_ident", " , cell_line_data_set_ident_for_export where mol_targ_catch_all_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TableAndWhereClause("mol_targ_ident", " , cell_line_data_set_ident_for_export where mol_targ_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TableAndWhereClause("nano_string_ident", " , cell_line_data_set_ident_for_export where nano_string_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TableAndWhereClause("nat_prod_ident", "DO NOT REPLICATE"));
    compareTawc.add(new TableAndWhereClause("nsc_ident", " , cell_line_data_set_ident_for_export where nsc_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TableAndWhereClause("synthetic_ident", " , cell_line_data_set_ident_for_export where synthetic_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TableAndWhereClause("cell_line_data_set_ident", " , cell_line_data_set_ident_for_export where cell_line_data_set_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TableAndWhereClause("compare_cell_line", ""));
    compareTawc.add(new TableAndWhereClause("compare_result", "DO NOT REPLICATE"));

    // open-ended by nsc
    compareTawc.add(new TableAndWhereClause("nsc_compound", " , nsc_for_export where nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc"));
    compareTawc.add(new TableAndWhereClause("conc_resp_assay", " , nsc_compound, nsc_for_export where nsc_compound_fk = nsc_compound.id and nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc"));
    compareTawc.add(new TableAndWhereClause("conc_resp_element", " , conc_resp_assay, nsc_compound, nsc_for_export where conc_resp_element.conc_resp_assay_fk = conc_resp_assay.id and conc_resp_assay.nsc_compound_fk = nsc_compound.id and nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc"));

// modified to add restrictions on lhc for legacy vs nine conc
//        compareTawc.add(new TableAndWhereClause("conc_resp_assay", " , nsc_compound, nsc_for_cra_export where nsc_compound_fk = nsc_compound.id and nsc_compound.prefix = nsc_for_cra_export.prefix and nsc_compound.nsc = nsc_for_cra_export.nsc and conc_resp_assay.log_hi_conc = nsc_for_cra_export.log_hi_conc and conc_resp_assay.conc_unit = nsc_for_cra_export.conc_unit and conc_resp_assay.exp_id = 'AVGDATA'"));
//        compareTawc.add(new TableAndWhereClause("conc_resp_element", " , conc_resp_assay, nsc_compound, nsc_for_cra_export where conc_resp_element.conc_resp_assay_fk = conc_resp_assay.id and conc_resp_assay.nsc_compound_fk = nsc_compound.id and nsc_compound.prefix = nsc_for_cra_export.prefix and nsc_compound.nsc = nsc_for_cra_export.nsc and conc_resp_assay.log_hi_conc = nsc_for_cra_export.log_hi_conc and conc_resp_assay.conc_unit = nsc_for_cra_export.conc_unit and conc_resp_assay.exp_id = 'AVGDATA'"));
    compareTawc.add(new TableAndWhereClause("dtp_cell_line_data_set", " , cell_line_data_set_for_export where dtp_cell_line_data_set.id = cell_line_data_set_for_export.id"));
    compareTawc.add(new TableAndWhereClause("dtp_test_result", " , test_result, cell_line_data_set_for_export where dtp_test_result.id = test_result.id and test_result.cell_line_data_set_fk = cell_line_data_set_for_export.id"));
    compareTawc.add(new TableAndWhereClause("grid_compare_columns", "DO NOT REPLICATE"));
    compareTawc.add(new TableAndWhereClause("grid_compare_job", "DO NOT REPLICATE"));
    compareTawc.add(new TableAndWhereClause("grid_compare_rows", "DO NOT REPLICATE"));
    compareTawc.add(new TableAndWhereClause("job", "DO NOT REPLICATE"));
    compareTawc.add(new TableAndWhereClause("job_for_req_cell_lines2require", "DO NOT REPLICATE"));
    compareTawc.add(new TableAndWhereClause("ignore_cell_lines2job_for_ign_", "DO NOT REPLICATE"));
    compareTawc.add(new TableAndWhereClause("standard_compare_job", "DO NOT REPLICATE"));
    compareTawc.add(new TableAndWhereClause("test_result", " , cell_line_data_set_for_export where test_result.cell_line_data_set_fk = cell_line_data_set_for_export.id"));
    compareTawc.add(new TableAndWhereClause("test_result_type", ""));
    compareTawc.add(new TableAndWhereClause("uploaded_cell_line_data_set", "DO NOT REPLICATE"));
    compareTawc.add(new TableAndWhereClause("uploaded_ident", "DO NOT REPLICATE"));
    compareTawc.add(new TableAndWhereClause("uploaded_test_result", "DO NOT REPLICATE"));
  }

  public static void propagateCuratedNsc(Connection srcConn, Connection destConn, Boolean doCompareTables, Boolean doDataSystemTables) throws Exception {

    Statement srcStmt = null;
    Statement destStmt = null;
    ResultSet rs = null;
    PreparedStatement destPrepStmt = null;

    try {

      srcStmt = srcConn.createStatement();
      destStmt = destConn.createStatement();

      // source (probably almost always datasystemdb_local
      String srcSqlStr = "drop table if exists curated_nsc_smiles_src";

      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      // collate targets and aliases
      srcSqlStr = "drop table if exists collated_targets";
      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      srcSqlStr = "create table collated_targets as "
              + " select nsc.nsc, "
              + " array_to_string(array_agg(sec.value), ';') as sec_targets "
              + " from curated_nsc nsc "
              + " left outer join curated_nsc_to_secondary_targe sec_join on nsc.id = sec_join.curated_nsc_to_secondary_ta_fk "
              + "      inner join curated_target sec on sec_join.secondary_targets_fk = sec.id "
              + " group by nsc.nsc";
      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      srcSqlStr = "drop table if exists collated_aliases";
      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      srcSqlStr = "create table collated_aliases as "
              + " select nsc.nsc, "
              + " array_to_string(array_agg(alia.value), ';') as aliases "
              + " from curated_nsc nsc "
              + " left outer join aliases2curated_nsc_to_aliases sec_join on nsc.id = sec_join.curated_nsc_to_aliases_fk "
              + "      inner join curated_name alia on sec_join.aliases_fk = alia.id "
              + " group by nsc.nsc";
      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      // COALESCE to pick generic_name first then preferred_name
      srcSqlStr = "create table curated_nsc_smiles_src "
              + " as "
              + " select nsc.nsc, "
              + " coalesce(gnam.value, pnam.value, '') as name, "
              + " trg.value as primary_target, "
              + " ct.can_smi as smiles "
              + " from curated_nsc nsc "
              + " left outer join curated_name gnam on nsc.generic_name_fk = gnam.id "
              + " left outer join curated_name pnam on nsc.preferred_name_fk = pnam.id "
              + " left outer join curated_target trg on nsc.primary_target_fk = trg.id "
              + " left outer join cmpd_table ct on nsc.nsc = ct.nsc ";
//         + " group by nsc.nsc";

      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      // dest            
      String destSqlStr = "drop table if exists curated_nsc_smiles_dest";

      System.out.println(destSqlStr);
      System.out.println();
      destStmt.executeUpdate(destSqlStr);

      destSqlStr = "create table curated_nsc_smiles_dest( "
              + " nsc int, "
              + " name varchar, "
              + " primary_target varchar, "
              + " smiles varchar)";

      System.out.println(destSqlStr);
      System.out.println();
      destStmt.executeUpdate(destSqlStr);

      // update            
      destSqlStr = "insert into curated_nsc_smiles_dest(nsc, name, primary_target, smiles) values (?,?,?,?)";

      System.out.println(destSqlStr);
      System.out.println();
      destPrepStmt = destConn.prepareStatement(destSqlStr);

      srcSqlStr = "select nsc, name, primary_target, smiles from curated_nsc_smiles_src";

      System.out.println(srcSqlStr);
      System.out.println();
      rs = srcStmt.executeQuery(srcSqlStr);

      while (rs.next()) {

        destPrepStmt.setInt(1, rs.getInt("nsc"));
        destPrepStmt.setString(2, rs.getString("name"));
        destPrepStmt.setString(3, rs.getString("primary_target"));
        destPrepStmt.setString(4, rs.getString("smiles"));

        destPrepStmt.execute();

      }

      rs.close();
      destPrepStmt.close();

      // updating target tables
      if (doCompareTables) {

        // AS WRITTEN, ONLY UPDATES nulls - WON'T OVERWRITE! (sarcomadb, e.g.)
        destSqlStr = "update synthetic_ident "
                + " set drug_name = curated_nsc_smiles_dest.name, "
                + " target = curated_nsc_smiles_dest.primary_target, "
                + " smiles = curated_nsc_smiles_dest.smiles "
                + " from curated_nsc_smiles_dest, nsc_ident "
                + " where synthetic_ident.id = nsc_ident.id "
                + " and nsc_ident.nsc = curated_nsc_smiles_dest.nsc"
                + " and synthetic_ident.drug_name is null";

        System.out.println(destSqlStr);
        System.out.println();
        destStmt.executeUpdate(destSqlStr);

      }

      if (doDataSystemTables) {

        // AS WRITTEN, ONLY UPDATES nulls - WON'T OVERWRITE! (sarcomadb, e.g.)
        String[] sqlArr = new String[]{
          //
          // NUCLEAR OPTION!
          // DANGER! DANGER! DANGER!
          "update nsc_cmpd set name = null",
          //
          // NUCLEAR OPTION!
          // DANGER! DANGER! DANGER!
          "update cmpd_table set name = null",
          //
          "update nsc_cmpd "
          + " set name = curated_nsc_smiles_dest.name "
          + " from curated_nsc_smiles_dest "
          + " where nsc_cmpd.nsc = curated_nsc_smiles_dest.nsc ",
          // + " and nsc_cmpd.name is null",
          //
          "update cmpd_table "
          + " set name = curated_nsc_smiles_dest.name "
          + " from curated_nsc_smiles_dest "
          + " where cmpd_table.nsc = curated_nsc_smiles_dest.nsc ",
          // + " and cmpd_table.name is null",
          //
          //  #####    ##    #####    ####   ######   #####   ####
          //    #     #  #   #    #  #    #  #          #    #
          //    #    #    #  #    #  #       #####      #     ####
          //    #    ######  #####   #  ###  #          #         #
          //    #    #    #  #   #   #    #  #          #    #    #
          //    #    #    #  #    #   ####   ######     #     ####

          "drop table if exists distinct_targets",
          //
          "create table distinct_targets "
          + " as select distinct primary_target "
          + " from  curated_nsc_smiles_dest",
          //
          "drop table if exists targets_to_add",
          //
          "create table targets_to_add  "
          + " as select primary_target "
          + " from distinct_targets  "
          + " except "
          + " select target from cmpd_target",
          //
          "insert into cmpd_target(id, target)  "
          + " select nextval('cmpd_target_seq'), primary_target  "
          + " from targets_to_add",
          //
          "drop table if exists nsc_target_to_add",
          //
          "create table nsc_target_to_add "
          + " as select nsc, primary_target "
          + " from curated_nsc_smiles_dest cns "
          + " except "
          + " select nsc, target "
          + " from nsc_cmpd nc, cmpd_targets2nsc_cmpds ct2nc, cmpd_target ct "
          + " where ct2nc.nsc_cmpds_fk = nc.id "
          + " and ct2nc.cmpd_targets_fk = ct.id",
          //
          "insert into cmpd_targets2nsc_cmpds(nsc_cmpds_fk, cmpd_targets_fk) "
          + " select nc.id, ct.id "
          + " from nsc_target_to_add ntta, nsc_cmpd nc, cmpd_target ct "
          + " where "
          + " ntta.nsc = nc.nsc "
          + " and ntta.primary_target = ct.target",
          //
          "update cmpd_table"
          + " set formatted_targets_string = primary_target"
          + " from nsc_target_to_add"
          + " where cmpd_table.nsc = nsc_target_to_add.nsc",
          //

          //   ##    #          #      ##     ####   ######   ####
          //  #  #   #          #     #  #   #       #       #
          // #    #  #          #    #    #   ####   #####    ####
          // ######  #          #    ######       #  #            #
          // #    #  #          #    #    #  #    #  #       #    #
          // #    #  ######     #    #    #   ####   ######   ####

          "drop table if exists distinct_aliases",
          //
          "create table distinct_aliases "
          + " as select distinct name as alias"
          + " from  curated_nsc_smiles_dest",
          //
          "drop table if exists aliases_to_add",
          //
          "create table aliases_to_add  "
          + " as select alias "
          + " from distinct_aliases  "
          + " except "
          + " select alias from cmpd_alias",
          //
          "insert into cmpd_alias(id, alias, cmpd_alias_type_fk)  "
          + " select nextval('cmpd_alias_seq'), alias, 53  "
          + " from aliases_to_add",
          //
          "drop table if exists nsc_alias_to_add",
          //
          "create table nsc_alias_to_add "
          + " as select nsc, name as alias "
          + " from curated_nsc_smiles_dest cns "
          + " except "
          + " select nsc, alias "
          + " from nsc_cmpd nc, cmpd_aliases2nsc_cmpds ct2nc, cmpd_alias ct "
          + " where ct2nc.nsc_cmpds_fk = nc.id "
          + " and ct2nc.cmpd_aliases_fk = ct.id",
          //
          "insert into cmpd_aliases2nsc_cmpds(nsc_cmpds_fk, cmpd_aliases_fk) "
          + " select nc.id, ct.id "
          + " from nsc_alias_to_add ntta, nsc_cmpd nc, cmpd_alias ct "
          + " where "
          + " ntta.nsc = nc.nsc "
          + " and ntta.alias = ct.alias",
          //
          "update cmpd_table"
          + " set formatted_aliases_string = alias"
          + " from nsc_alias_to_add"
          + " where cmpd_table.nsc = nsc_alias_to_add.nsc;"

        };

        for (String sqlStr : sqlArr) {
          System.out.println(sqlStr);
          System.out.println();
          destStmt.executeUpdate(sqlStr);
        }

      }

      srcStmt.close();
      destStmt.close();

      System.out.println("Done! in propagateCuratedNsc");

    } catch (Exception e) {
      System.out.println("Exception in propagateCuratedNsc:");
      System.out.println(e);
      throw (e);
    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (srcStmt != null) {
          srcStmt.close();
          srcStmt = null;
        }
        if (destStmt != null) {
          destStmt.close();
          destStmt = null;
        }
        if (destPrepStmt != null) {
          destPrepStmt.close();
          destPrepStmt = null;
        }
      } catch (Exception e) {
        System.out.println("Exception in finally clause in propagateCuratedNsc: " + e);
        e.printStackTrace();
        throw (e);
      }
    }

  }

  public static void nukeAllDestinationTables(Connection srcConn, Connection destDataSystemConn, ArrayList<TableAndWhereClause> tawcList) throws Exception {

    try {

      for (TableAndWhereClause tawc : tawcList) {
        Replicator.nuke(destDataSystemConn, tawc.tableName);
      }

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in nukeAllDestinationTables");
      e.printStackTrace();
      throw e;
    } finally {

    }
  }

  public static void propagateCompare(Connection srcConn, Connection destDataSystemConn, ArrayList<TableAndWhereClause> tawcList) throws Exception {

    try {

      for (TableAndWhereClause tawc : tawcList) {

        // replicate the tables
        if (!tawc.whereClause.equals("DO NOT REPLICATE")) {
          Replicator.useMetadata(srcConn, destDataSystemConn, tawc.tableName, tawc.whereClause);
        }

      }

      System.out.println("Done! in propagateCompare");

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in propagateCompare");
      e.printStackTrace();
      throw e;
    } finally {

    }
  }

  public static void propagateDataSystem(Connection srcConn, Connection destDataSystemConn, ArrayList<TableAndWhereClause> tawcList) throws Exception {

    try {

      for (TableAndWhereClause tawc : tawcList) {
        if (!tawc.whereClause.equals("DO NOT REPLICATE")) {
          Replicator.useMetadata(srcConn, destDataSystemConn, tawc.tableName, tawc.whereClause);
        }
      }

      System.out.println("Done! in propagateDataSystem");

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in propagateDataSystem");
      e.printStackTrace();
      throw e;
    } finally {

    }
  }

  public static void populateRdkitMol(Connection destDataSystemConn) throws Exception {

    Statement stmt = null;

    try {

      destDataSystemConn.setAutoCommit(true);
      stmt = destDataSystemConn.createStatement();

      String sqlStr = "drop table if exists rdkit_validity";

      sqlStr = "create table rdkit_validity "
              + " as "
              + " select nsc, is_valid_smiles(can_smi::cstring) as valid_can_smi, "
              + " is_valid_smiles(can_taut::cstring) as valid_can_taut, "
              + " is_valid_smiles(can_taut_strip_stereo::cstring) as valid_can_taut_strip_stereo, "
              + " is_valid_ctab(ctab::cstring) as valid_ctab "
              + " from cmpd_table where can_smi is not null";

      sqlStr = "create index rdkv_nsc on rdkit_validity(nsc)";

      sqlStr = "insert into rdkit_mol(id, nsc, mol, mol_from_ctab) "
              + " select ct.nsc, ct.nsc, mol_from_smiles(ct.can_taut::cstring), mol_from_ctab(ctab::cstring) "
              + " from cmpd_table ct, rdkit_validity v "
              + " where ct.nsc = v.nsc "
              + " and v.valid_can_taut = 't' "
              + " and v.valid_ctab = 't'";

      sqlStr = "drop table if exists rdkit_mol";
      System.out.println(sqlStr);
      stmt.executeUpdate(sqlStr);

      sqlStr = "create table rdkit_mol(id bigint, nsc int, mol mol, mol_from_ctab mol)";
      System.out.println(sqlStr);
      stmt.executeUpdate(sqlStr);

      sqlStr = "insert into rdkit_mol(id, nsc, mol, mol_from_ctab) select nsc, nsc, mol_from_smiles(can_taut::cstring), mol_from_ctab(ctab::cstring) from cmpd_table";
      System.out.println(sqlStr);
      stmt.executeUpdate(sqlStr);

      sqlStr = "create index rdkit_mol_mol on rdkit_mol using gist(mol)";
      System.out.println(sqlStr);
      stmt.executeUpdate(sqlStr);

    } catch (SQLException se) {
      System.out.println("SQL Exception in populateRdkitMol:");
      // Loop through the SQL Exceptions
      while (se != null) {
        System.out.println("State  : " + se.getSQLState());
        System.out.println("Message: " + se.getMessage());
        System.out.println("Error  : " + se.getErrorCode());
        se = se.getNextException();
      }
      throw new Exception(se);
    } catch (Exception e) {
      System.out.println("Exception in populateRdkitMol:");
      System.out.println(e);
      throw (e);
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
      } catch (Exception e) {
        System.out.println("Exception in finally clause in populateRdkitMol: " + e);
        e.printStackTrace();
        throw (e);
      }
    }

  }

  public static void prepareForCompareExport(Connection srcCompareConn, String oncPubPriv) throws Exception {

    System.out.println("In prepareForCompareExport");

    Statement cmprStmt = null;
    Statement dsStmt = null;

    try {

      srcCompareConn.setAutoCommit(true);
      cmprStmt = srcCompareConn.createStatement();

      // EXPECTS to find nsc_for_export(prefix, nsc)
      // EXPECTS to find target_set_name_for_export(target_set_name);
      String[] ntsArr = new String[]{
        "STANDARD_AGENTS_GI50",
        "STANDARD_AGENTS_TGI",
        "STANDARD_AGENTS_LC50",
        "SYNTHETIC_COMPOUNDS_GI50",
        "SYNTHETIC_COMPOUNDS_TGI",
        "SYNTHETIC_COMPOUNDS_LC50",
        "BOYD_SERIES_GI50",
        "NATURAL_PRODUCTS_GI50",
        "NATURAL_PRODUCTS_TGI",
        "NATURAL_PRODUCTS_LC50",
        "BOYD_SERIES_TGI",
        "DIVERSITY_SET_GI50",
        "DIVERSITY_SET_TGI",
        "DIVERSITY_SET_LC50",
        "BOYD_SERIES_LC50",
        "MCCLOUD_SERIES_GI50",
        "MCCLOUD_SERIES_TGI",
        "MCCLOUD_SERIES_LC50",
        "MOLTID_GC_SERIES_MICROARRAY_NOVARTIS",
        "MOLTID_GC_SERIES_MICROARRAY_STANFORD",
        "MOLTID_GC_SERIES_MICROARRAY_GENELOGIC_U95",
        "MOLTID_GC_SERIES_MICROARRAY_GENELOGIC_U133",
        "MOLTID_GC_SERIES_MICROARRAY_CHIRON",
        "MECHANISTIC_SET_GI50",
        "MECHANISTIC_SET_TGI",
        "MECHANISTIC_SET_LC50",
        "BEC_REFERRAL_SET_GI50",
        "BEC_REFERRAL_SET_TGI",
        "BEC_REFERRAL_SET_LC50",
        "MIR_ISRAEL",
        "ARRAYCGH_GRAY",
        "FEINBERG_METHYLATION",
        "KARYOTYPE",
        "GARRAWAY_SNP",
        "METABOLON",
        "MILLENIUM",
        "MOLTID_GC_SERIES_MICROARRAY_MOCK_TX_2H",
        "MOLTID_GC_SERIES_MICROARRAY_MOCK_TX_6H",
        "MOLTID_GC_SERIES_MICROARRAY_MOCK_TX_24H",
        "MOLTID_MT_SERIES",
        "MOLTID_CG_SERIES",
        "MOLTID_SA_SERIES",
        "MICRORNA",
        "MOLTID_GC_SERIES_MICROARRAY_ALL",
        "WEINSTEIN_CROCE_MIR",
        "REINHOLD_ARRAY_CGH_DNACOPYNUM",
        "MARKETED_DRUGS_GI50",
        "MARKETED_DRUGS_TGI",
        "MARKETED_DRUGS_LC50",
        "MOLTID_GC_SERIES_MICROARRAY_GENELOGIC_U133PLUS2",
        "MOLTID_DS_SERIES_SEQ_MUT"
      };

      ArrayList<String> sqlList = new ArrayList<String>();

      if (oncPubPriv.equals(ONC)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select prefix, nsc from curated_nsc_smiles_dest where prefix = 'S'");
        sqlList.add("drop table if exists target_set_names_for_export");
        sqlList.add("create table target_set_names_for_export(target_set_name varchar)");

      } else if (oncPubPriv.equals(PUB)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select 'S'::varchar as prefix, nsc from publicrelease");
        sqlList.add("drop table if exists target_set_names_for_export");
        sqlList.add("create table target_set_names_for_export(target_set_name varchar)");

      } else if (oncPubPriv.equals(PRIV)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select distinct prefix, nsc from nsc_ident");
        sqlList.add("drop table if exists target_set_names_for_export");
        sqlList.add("create table target_set_names_for_export(target_set_name varchar)");

      } else {
        throw new Exception("Unrecognized oncPubPriv");
      }

      sqlList.add("drop table if exists cell_line_data_set_ident_for_export");
      sqlList.add("drop table if exists cell_line_data_set_for_export");
      sqlList.add("create table cell_line_data_set_ident_for_export(id bigint)");
      sqlList.add("create table cell_line_data_set_for_export(id bigint)");

      for (String sql : sqlList) {
        System.out.println(sql);
        cmprStmt.executeUpdate(sql);
      }

      // NSC
      String sqlStr = "insert into cell_line_data_set_ident_for_export(id) "
              + " select ni.id "
              + " from nsc_ident ni, nsc_for_export nfe "
              + " where ni.prefix = nfe.prefix "
              + " and ni.nsc = nfe.nsc "
              + " and ni.endpoint in ('GI50', 'TGI', 'LC50')";
//         + " and ni.exp_id = 'AVGDATA'";
      System.out.println(sqlStr);
      cmprStmt.executeUpdate(sqlStr);

      // MolTarg from named_target_set
      sqlStr = "insert into cell_line_data_set_ident_for_export(id) "
              + " select cell_line_data_sets_fk "
              + " from named_target_set nts, cell_line_data_sets2named_targ clds2nts "
              + " where clds2nts.named_target_sets_fk = nts.id "
              + " and nts.target_set_name in ( "
              + " select target_set_name from target_set_names_for_export "
              + " ) ";

      System.out.println(sqlStr);
      cmprStmt.executeUpdate(sqlStr);

      sqlStr = "insert into cell_line_data_set_for_export(id) "
              + "select id from cell_line_data_set "
              + "where cell_line_data_set_ident_fk in (select id from cell_line_data_set_ident_for_export)";
      System.out.println(sqlStr);
      cmprStmt.executeUpdate(sqlStr);

      sqlStr = "create index cldsfe_id_idx on cell_line_data_set_for_export(id)";
      System.out.println(sqlStr);
      cmprStmt.executeUpdate(sqlStr);

      sqlStr = "create index cldsife_id_idx on cell_line_data_set_ident_for_export(id)";
      System.out.println(sqlStr);
      cmprStmt.executeUpdate(sqlStr);

      System.out.println("DONE!");

    } catch (SQLException se) {
      System.out.println("SQL Exception in prepareForCompareExport:");
      // Loop through the SQL Exceptions
      while (se != null) {
        System.out.println("State  : " + se.getSQLState());
        System.out.println("Message: " + se.getMessage());
        System.out.println("Error  : " + se.getErrorCode());
        se = se.getNextException();
      }
      throw new Exception(se);
    } catch (Exception e) {
      System.out.println("Exception in prepareForCompareExport:");
      System.out.println(e);
      throw (e);
    } finally {
      try {
        if (cmprStmt != null) {
          cmprStmt.close();
          cmprStmt = null;
        }
        if (dsStmt != null) {
          dsStmt.close();
          dsStmt = null;
        }
      } catch (Exception e) {
        System.out.println("Exception in finally clause in prepareForCompareExports: " + e);
        e.printStackTrace();
        throw (e);
      }
    }

  }

  public static void prepareForDataSystemExport(Connection srcCompareConn, String oncPubPriv) throws Exception {

    System.out.println("In prepareForDataSystemExport");

    Statement cmprStmt = null;
    Statement dsStmt = null;

    try {

      srcCompareConn.setAutoCommit(true);
      cmprStmt = srcCompareConn.createStatement();

      ArrayList<String> sqlList = new ArrayList<String>();

      if (oncPubPriv.equals(ONC)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select prefix, nsc from curated_nsc_smiles_dest where prefix = 'S'");

      } else if (oncPubPriv.equals(PUB)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select 'S'::varchar as prefix, nsc from publicrelease");

      } else if (oncPubPriv.equals(PRIV)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select nsc from nsc_cmpd");

      } else {

        throw new Exception("Unrecognized oncPubPriv");

      }

      System.out.println("DONE!");

    } catch (SQLException se) {
      System.out.println("SQL Exception in prepareForDataSystemExport:");
      // Loop through the SQL Exceptions
      while (se != null) {
        System.out.println("State  : " + se.getSQLState());
        System.out.println("Message: " + se.getMessage());
        System.out.println("Error  : " + se.getErrorCode());
        se = se.getNextException();
      }
      throw new Exception(se);
    } catch (Exception e) {
      System.out.println("Exception in prepareForDataSystemExport:");
      System.out.println(e);
      throw (e);
    } finally {
      try {
        if (cmprStmt != null) {
          cmprStmt.close();
          cmprStmt = null;
        }
        if (dsStmt != null) {
          dsStmt.close();
          dsStmt = null;
        }
      } catch (Exception e) {
        System.out.println("Exception in finally clause in In prepareForDataSystemExport: " + e);
        e.printStackTrace();
        throw (e);
      }
    }

  }

  public static void updateSequences(Connection postgresConn) throws Exception {

    String[] seqBaseNameArray = new String[]{
      "cell_line_data_set_ident",
      "cell_line_data_set",
      "compare_cell_line",
      "compare_result",
      "conc_resp_assay",
      "conc_resp_element",
      "job",
      "named_target_set",
      "nsc_compound",
      "test_result",
      "test_result_type"
    };

    String highValQuery;
    String setValQuery;

    int highVal = 0;

    Statement postgresQuery = null;
    PreparedStatement postgresPrepStmt = null;

    ResultSet rs = null;

    try {

      postgresQuery = postgresConn.createStatement();

      for (int i = 0; i < seqBaseNameArray.length; i++) {

        highValQuery = "select max(id) from " + seqBaseNameArray[i];
        setValQuery = "select setval('" + seqBaseNameArray[i] + "_seq', ?)";

        System.out.println(highValQuery);
        System.out.println(setValQuery);

        rs = postgresQuery.executeQuery(highValQuery);

        while (rs.next()) {
          highVal = rs.getInt("max");
        }

        System.out.println("Value of max(id) in table: " + seqBaseNameArray[i] + " is: " + highVal);

        if (highVal > 0) {
          postgresPrepStmt = postgresConn.prepareStatement(setValQuery);
          postgresPrepStmt.setInt(1, highVal);
          postgresPrepStmt.execute();
        }

      }

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in doFetchAndInsert");
      e.printStackTrace();
      throw e;
    } finally {
//Close connection
      if (postgresQuery != null) {
        try {
          postgresQuery.close();
          postgresQuery = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresQuery in doFetchAndInsert");
        }
      }
      if (rs != null) {
        try {
          rs.close();
          rs = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing rs in doFetchAndInsert");
        }
      }
    }

  }

  public static void processDbDdl() {

    File compareCreateFile = new File("/home/mwkunkel/PROJECTS/CURRENT/dtp_compare/compareuml/web/target/compare-core.20170410.schema-create.sql");
    File dataSystemCreateFile = new File("/home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/datasystemuml/web/target/datasystem-core.20170328.schema-create.sql");

    File compareDropFile = new File("/home/mwkunkel/PROJECTS/CURRENT/dtp_compare/compareuml/web/target/compare-core.20170410.schema-drop.sql");
    File dataSystemDropFile = new File("/home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/datasystemuml/web/target/datasystem-core.20170328.schema-drop.sql");

    ArrayList<String> compareCreateStrings = new ArrayList<String>();
    ArrayList<String> dataSystemCreateStrings = new ArrayList<String>();

    ArrayList<String> compareDropStrings = new ArrayList<String>();
    ArrayList<String> dataSystemDropStrings = new ArrayList<String>();

    try {

      processCreates(compareCreateFile, compareCreateStrings);
      processCreates(dataSystemCreateFile, dataSystemCreateStrings);

// processDrops(compareDropFile, compareDropStrings);
// processDrops(dataSystemDropFile, dataSystemDropStrings);
// for (String s : compareCreateStrings) {
//     System.out.println("compareCreateStrings: " + s);
// }
// for (String s : compareDropStrings) {
//     System.out.println("compareDropStrings: " + s);
// }
// for (String s : dataSystemCreateStrings) {
//     System.out.println("dataSystemCreateStrings: " + s);
// }
// for (String s : dataSystemDropStrings) {
//     System.out.println("dataSystemDropStrings: " + s);
// }
    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static void processCreates(File createFile, ArrayList<String> stmtList) {
    FileReader fr = null;
    BufferedReader br = null;
    String line;
    String fixedLine;
    StringBuilder sb = new StringBuilder();
    try {
      fr = new FileReader(createFile);
      br = new BufferedReader(fr);
      while ((line = br.readLine()) != null) {
        fixedLine = line.replaceAll("\t", " ");
        sb.append(" ").append(fixedLine).append(" ");
        if (fixedLine.contains(";")) {
          if (sb.toString().contains("create table")) {
            String fmtStr = sb.toString().replaceAll(",", ",\n");
            stmtList.add(fmtStr);
            System.out.println(fmtStr);
          }
          sb = new StringBuilder();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        br.close();
        fr.close();
      } catch (Exception e) {

      }
    }
  }

  public static void processDrops(File createFile, ArrayList<String> stmtList) {
    FileReader fr = null;
    BufferedReader br = null;
    String line;
    String fixedLine;
    StringBuilder sb = new StringBuilder();
    try {
      fr = new FileReader(createFile);
      br = new BufferedReader(fr);
      while ((line = br.readLine()) != null) {
        fixedLine = line.replaceAll("\t", " ");
        sb.append(" ").append(fixedLine).append(" ");
        if (fixedLine.contains(";")) {
          if (sb.toString().contains("drop table") || sb.toString().contains("drop sequence")) {
            String fmtStr = sb.toString().replaceAll(",", ",\n");
            stmtList.add(fmtStr);
            System.out.println(fmtStr);
          }
          sb = new StringBuilder();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        br.close();
        fr.close();
      } catch (Exception e) {

      }
    }
  }

  public static void fetchPublicRelease(Connection destConn) {

    Connection sourceConn = null;

    Statement destStmt = null;
    Statement sourceStmt = null;

    try {

      DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
      DriverManager.registerDriver(new org.postgresql.Driver());

      sourceConn = DriverManager.getConnection("jdbc:oracle:thin:@//dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");

      destConn.setAutoCommit(true);
      destStmt = destConn.createStatement();

      int result = destStmt.executeUpdate("drop table if exists publicrelease");
      result = destStmt.executeUpdate("create table publicrelease(nsc int)");

      Replicator.useMetadata(sourceConn, destConn, "publicrelease", "");

      System.out.println("Done! in Main");

      sourceConn.close();
      destConn.close();

      sourceConn = null;
      destConn = null;

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in GetPublicRelease.main");
      e.printStackTrace();
    } finally {
      if (destConn != null) {
        try {
          destConn.close();
          destConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing destConn");
        }
      }
      if (sourceConn != null) {
        try {
          sourceConn.close();
          sourceConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing sourceConn");
        }
      }
    }
  }

}
