/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

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

    public static void main(String[] args) {

        ConnectionInfo srcInfo = connMap.get("privatecomparedb_local");
        ConnectionInfo destInfo = connMap.get("publiccomparedb_local");

        ArrayList<TableAndWhereClause> tawcList = cj_tawc;

        Connection srcConn = null;
        Connection destConn = null;

        try {

            DriverManager.registerDriver(new org.postgresql.Driver());
            // DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

            System.out.println();
            System.out.println("srcInfo: ");
            srcInfo.asProperties();

            System.out.println();
            System.out.println("destInfo: ");
            destInfo.asProperties();

            srcConn = DriverManager.getConnection(srcInfo.dbUrl, srcInfo.dbUser, srcInfo.dbPass);
            destConn = DriverManager.getConnection(destInfo.dbUrl, destInfo.dbUser, destInfo.dbPass);

//            IndexAndConstraintManagement.save_XXX_Constraints(destConn, tawcList);
//            IndexAndConstraintManagement.save_XXX_Indexes(destConn, tawcList);
//
//            prepareCompareIdentsForExport(srcConn);
//
//            IndexAndConstraintManagement.drop_XXX_Indexes(destConn);
//            IndexAndConstraintManagement.drop_XXX_Constraints(destConn);
//            
//            nukeAllDestinationTables(srcConn, destConn, tawcList);
//            
            propagateCompare(srcConn, destConn, tawcList);

//            propagateDataSystem(srcConn, destConn, tawcList);
//            propagateCuratedNsc(srcConn, destConn, destInfo.doCompareTables, destInfo.doDataSystemTables);
//            
//            IndexAndConstraintManagement.create_XXX_Indexes(destConn);
//            IndexAndConstraintManagement.create_XXX_Constraints(destConn);
            System.out.println("Done! in NewMain");

            srcConn.close();
            destConn.close();

            srcConn = null;
            destConn = null;

        } catch (Exception e) {
            System.out.println("Caught Exception " + e + " in DatasystemReplicator.main");
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
            if (srcConn != null) {
                try {
                    srcConn.close();
                    srcConn = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing srcConn");
                }
            }
        }
    }

    static Map<String, ConnectionInfo> connMap = new HashMap<String, ConnectionInfo>();

    static {

        connMap.put("microxenodb_local", new ConnectionInfo(
                "microxenodb_local",
                "jdbc:postgresql://localhost:5432/microxenodb",
                "mwkunkel",
                "donkie11",
                Boolean.FALSE,
                Boolean.FALSE
        ));

        connMap.put("microxenodb_dev", new ConnectionInfo(
                "microxenodb_dev",
                "jdbc:postgresql://ncidb-d115-d.nci.nih.gov:5473/microxeno",
                "microxeno",
                "M1cr0x0293025en0",
                Boolean.FALSE,
                Boolean.FALSE
        ));

        connMap.put("datasystemdb_local", new ConnectionInfo(
                "datasystemdb_local",
                "jdbc:postgresql://localhost:5432/datasystemdb",
                "mwkunkel",
                "donkie11",
                Boolean.FALSE,
                Boolean.TRUE
        ));

        connMap.put("oncologydrugsdb_local", new ConnectionInfo(
                "oncologydrugsdb_local",
                "jdbc:postgresql://localhost:5432/oncologydrugsdb",
                "mwkunkel",
                "donkie11",
                Boolean.TRUE,
                Boolean.TRUE
        ));

        connMap.put("oncologydrugsdb_dev", new ConnectionInfo(
                "oncologydrugsdb_dev",
                "jdbc:postgresql://ncidb-d115-d:5474/oncology",
                "oncology",
                "OnC0L029302802K1t",
                Boolean.TRUE,
                Boolean.TRUE
        ));

        connMap.put("publiccomparedb_local", new ConnectionInfo(
                "publiccomparedb_local",
                "jdbc:postgresql://localhost:5432/publiccomparedb",
                "mwkunkel",
                "donkie11",
                Boolean.TRUE,
                Boolean.FALSE
        ));

        connMap.put("publiccomparedb_dev", new ConnectionInfo(
                "publiccomparedb_dev",
                "jdbc:postgresql://ncidb-d115-d:5473/pubcompare",
                "publiccompare",
                "P3b092094wlC0m3",
                Boolean.TRUE,
                Boolean.FALSE
        ));

        connMap.put("privatecomparedb_local", new ConnectionInfo(
                "privatecomparedb_local",
                "jdbc:postgresql://localhost:5432/privatecomparedb",
                "mwkunkel",
                "donkie11",
                Boolean.TRUE,
                Boolean.FALSE
        ));

        connMap.put("sarcomacomparedb_local", new ConnectionInfo(
                "sarcomacomparedb_local",
                "jdbc:postgresql://localhost:5432/sarcomacomparedb",
                "mwkunkel",
                "donkie11",
                Boolean.TRUE,
                Boolean.FALSE
        ));

        Set<String> dbNameSet = connMap.keySet();
        ArrayList<String> dbNameList = new ArrayList<String>(dbNameSet);
        Collections.sort(dbNameList);

        for (String dbName : dbNameList) {
            connMap.get(dbName).asProperties();
        }

    }

    static ArrayList<TableAndWhereClause> mx_tawc = new ArrayList<TableAndWhereClause>();

    static {

        mx_tawc.add(new TableAndWhereClause("affymetrix_Identifier", ""));
        mx_tawc.add(new TableAndWhereClause("tumor", ""));
        mx_tawc.add(new TableAndWhereClause("tumor_type", ""));
        mx_tawc.add(new TableAndWhereClause("flat_data", ""));

    }

    static ArrayList<TableAndWhereClause> ds_tawc = new ArrayList<TableAndWhereClause>();

    static {

        ds_tawc.add(new TableAndWhereClause("ad_hoc_cmpd", "DO NOT REPLICATE"));
        ds_tawc.add(new TableAndWhereClause("ad_hoc_cmpd_fragment", "DO NOT REPLICATE"));
        ds_tawc.add(new TableAndWhereClause("ad_hoc_cmpd_fragment_p_chem", "DO NOT REPLICATE"));
        ds_tawc.add(new TableAndWhereClause("ad_hoc_cmpd_fragment_structure", "DO NOT REPLICATE"));
        ds_tawc.add(new TableAndWhereClause("cmpd_known_salt", ""));
        ds_tawc.add(new TableAndWhereClause("nsc_cmpd_type", ""));
        ds_tawc.add(new TableAndWhereClause("cmpd_alias_type", ""));
        ds_tawc.add(new TableAndWhereClause("cmpd_relation_type", ""));
        ds_tawc.add(new TableAndWhereClause("cmpd_fragment_type", ""));
        ds_tawc.add(new TableAndWhereClause("cmpd_inventory", " where id in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_annotation", " where id in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_bio_assay", " where id in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_legacy_cmpd", " where id in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_table", " where nsc in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd", " where id in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("nsc_cmpd", " where nsc in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_fragment", " where nsc_cmpd_fk in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_fragment_p_chem", " where id in (select cmpd_fragment_p_chem_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_export))"));
        ds_tawc.add(new TableAndWhereClause("cmpd_fragment_structure", " where id in (select cmpd_fragment_structure_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_export))"));
        ds_tawc.add(new TableAndWhereClause("cmpd_alias", " where id in (select cmpd_aliases_fk from cmpd_aliases2nsc_cmpds where cmpd_aliases2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        ds_tawc.add(new TableAndWhereClause("cmpd_aliases2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_list", "DO NOT REPLICATE"));
        ds_tawc.add(new TableAndWhereClause("cmpd_list_member", "DO NOT REPLICATE"));
        ds_tawc.add(new TableAndWhereClause("cmpd_named_set", " where id in (select cmpd_named_sets_fk from cmpd_named_sets2nsc_cmpds where cmpd_named_sets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        ds_tawc.add(new TableAndWhereClause("cmpd_named_sets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_plate", " where id in (select cmpd_plates_fk from cmpd_plates2nsc_cmpds where cmpd_plates2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        ds_tawc.add(new TableAndWhereClause("cmpd_plates2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_project", " where id in (select cmpd_projects_fk from cmpd_projects2nsc_cmpds where cmpd_projects2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        ds_tawc.add(new TableAndWhereClause("cmpd_projects2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_pub_chem_sid", " where id in (select cmpd_pub_chem_sids_fk from cmpd_pub_chem_sids2nsc_cmpds where cmpd_pub_chem_sids2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        ds_tawc.add(new TableAndWhereClause("cmpd_pub_chem_sids2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_target", " where id in (select cmpd_targets_fk from cmpd_targets2nsc_cmpds where cmpd_targets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        ds_tawc.add(new TableAndWhereClause("cmpd_targets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("cmpd_related", " where nsc_cmpd_fk in (select nsc from nsc_for_export)"));
        // rdkit_mol has to be handled separately
        // ds_tawc.add(new TableAndWhereClause("rdkit_mol", " where nsc in (select nsc from nsc_for_export)"));
        ds_tawc.add(new TableAndWhereClause("tanimoto_scores", " where nsc1 in (select nsc from nsc_for_export) and nsc2 in (select nsc from nsc_for_export)"));
        /*
        
   curated   
        
         */
        ds_tawc.add(new TableAndWhereClause("curated_name", ""));
        ds_tawc.add(new TableAndWhereClause("curated_nsc", ""));
        ds_tawc.add(new TableAndWhereClause("curated_nsc_to_secondary_targe", ""));
        ds_tawc.add(new TableAndWhereClause("curated_nscs2projects", ""));
        ds_tawc.add(new TableAndWhereClause("curated_originator", ""));
        ds_tawc.add(new TableAndWhereClause("curated_project", ""));
        ds_tawc.add(new TableAndWhereClause("curated_target", ""));

    }

//    static ArrayList<TableAndWhereClause> c_tawc = new ArrayList<TableAndWhereClause>();
//
//    static {
//
////        c_tawc.add(new TableAndWhereClause("affy_dna_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("affy_exon_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("build_date", ""));
////        c_tawc.add(new TableAndWhereClause("cell_line_data_set", " where id in (select id from cell_line_data_set_for_export)"));
////        c_tawc.add(new TableAndWhereClause("cell_line_data_set_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("compare_cell_line", ""));
////        c_tawc.add(new TableAndWhereClause("compare_result", "DO NOT REPLICATE"));
////        // still need to handle this!
////        c_tawc.add(new TableAndWhereClause("conc_resp_assay", " where nsc_compound_fk in (select id from nsc_compound where (prefix, nsc) in (select prefix, nsc from nsc_for_export))"));
////        c_tawc.add(new TableAndWhereClause("conc_resp_element", " where conc_resp_assay_fk in (select id from conc_resp_assay where nsc_compound_fk in (select id from nsc_compound where (prefix, nsc) in (select prefix, nsc from nsc_for_export)))"));
////        c_tawc.add(new TableAndWhereClause("dtp_cell_line_data_set", " where id in (select id from cell_line_data_set_for_export)"));
////        c_tawc.add(new TableAndWhereClause("dtp_test_result", " where id in (select id from test_result where cell_line_data_set_fk in (select id from cell_line_data_set_for_export))"));
////        c_tawc.add(new TableAndWhereClause("grid_compare_columns", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("grid_compare_job", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("grid_compare_rows", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("job", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("job_for_req_cell_lines2require", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("ignore_cell_lines2job_for_ign_", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("micro_array_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("micro_rna_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("mol_targ_catch_all_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("mol_targ_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("named_target_set", " where target_set_name in (select target_set_name from target_set_names_for_export)"));
////        c_tawc.add(new TableAndWhereClause("cell_line_data_sets2named_targ", " where cell_line_data_sets_fk in (select id from cell_line_data_set_for_export)"));
////        c_tawc.add(new TableAndWhereClause("compare_cell_line", ""));
////        c_tawc.add(new TableAndWhereClause("compare_result", "DO NOT REPLICATE"));
////        // still need to handle this!
//        c_tawc.add(new TableAndWhereClause("nsc_compound", " where (prefix, nsc) in (select prefix, nsc from nsc_for_export)"));
//        c_tawc.add(new TableAndWhereClause("conc_resp_assay", " where nsc_compound_fk in (select id from nsc_compound where (prefix, nsc) in (select prefix, nsc from nsc_for_export))"));
//        c_tawc.add(new TableAndWhereClause("conc_resp_element", " where conc_resp_assay_fk in (select id from conc_resp_assay where nsc_compound_fk in (select id from nsc_compound where (prefix, nsc) in (select prefix, nsc from nsc_for_export)))"));
////        c_tawc.add(new TableAndWhereClause("dtp_cell_line_data_set", " where id in (select id from cell_line_data_set_for_export)"));
////        // handle generalization of test_result               
////        // c_tawc.add(new TableAndWhereClause("dtp_test_result", " where cell_line_data_set_fk in (select id from cell_line_data_set_for_export))"));
////        c_tawc.add(new TableAndWhereClause("grid_compare_columns", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("grid_compare_job", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("grid_compare_rows", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("job", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("job_for_req_cell_lines2require", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("ignore_cell_lines2job_for_ign_", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("micro_array_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("micro_rna_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("mol_targ_catch_all_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("mol_targ_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("named_target_set", " where target_set_name in (select target_set_name from target_set_names_for_export)"));
////        c_tawc.add(new TableAndWhereClause("cell_line_data_sets2named_targ", " where named_target_sets_fk in (select id from named_target_set where target_set_name in (select target_set_name from target_set_names_for_export))"));
////        c_tawc.add(new TableAndWhereClause("nano_string_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("nat_prod_ident", "DO NOT REPLICATE"));
////  
////        c_tawc.add(new TableAndWhereClause("nsc_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("standard_compare_job", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("synthetic_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
////        c_tawc.add(new TableAndWhereClause("test_result", " where cell_line_data_set_fk in (select id from cell_line_data_set_for_export)"));
////        c_tawc.add(new TableAndWhereClause("test_result_type", ""));
////        c_tawc.add(new TableAndWhereClause("uploaded_cell_line_data_set", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("uploaded_ident", "DO NOT REPLICATE"));
////        c_tawc.add(new TableAndWhereClause("uploaded_test_result", "DO NOT REPLICATE"));
//    }
    static ArrayList<TableAndWhereClause> cj_tawc = new ArrayList<TableAndWhereClause>();

    static {

        cj_tawc.add(new TableAndWhereClause("affy_dna_ident", " , cell_line_data_set_ident_for_export where affy_dna_ident.id = cell_line_data_set_ident_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("affy_exon_ident", " , cell_line_data_set_ident_for_export where affy_exon_ident.id = cell_line_data_set_ident_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("build_date", ""));
        cj_tawc.add(new TableAndWhereClause("cell_line_data_sets2named_targ", " , named_target_set, target_set_names_for_export where cell_line_data_sets2named_targ.named_target_sets_fk = named_target_set.id and named_target_set.target_set_name = target_set_names_for_export.target_set_name"));
        cj_tawc.add(new TableAndWhereClause("named_target_set", " , target_set_names_for_export where named_target_set.target_set_name = target_set_names_for_export.target_set_name"));
        cj_tawc.add(new TableAndWhereClause("cell_line_data_set", " , cell_line_data_set_for_export where cell_line_data_set.id = cell_line_data_set_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("micro_array_ident", " , cell_line_data_set_ident_for_export where micro_array_ident.id = cell_line_data_set_ident_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("micro_rna_ident", " , cell_line_data_set_ident_for_export where micro_rna_ident.id = cell_line_data_set_ident_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("mol_targ_catch_all_ident", " , cell_line_data_set_ident_for_export where mol_targ_catch_all_ident.id = cell_line_data_set_ident_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("mol_targ_ident", " , cell_line_data_set_ident_for_export where mol_targ_ident.id = cell_line_data_set_ident_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("nano_string_ident", " , cell_line_data_set_ident_for_export where nano_string_ident.id = cell_line_data_set_ident_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("nat_prod_ident", "DO NOT REPLICATE"));
        cj_tawc.add(new TableAndWhereClause("nsc_ident", " , cell_line_data_set_ident_for_export where nsc_ident.id = cell_line_data_set_ident_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("synthetic_ident", " , cell_line_data_set_ident_for_export where synthetic_ident.id = cell_line_data_set_ident_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("cell_line_data_set_ident", " , cell_line_data_set_ident_for_export where cell_line_data_set_ident.id = cell_line_data_set_ident_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("compare_cell_line", ""));
        cj_tawc.add(new TableAndWhereClause("compare_result", "DO NOT REPLICATE"));
////////////        cj_tawc.add(new TableAndWhereClause("nsc_compound", " , nsc_for_export where nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc"));
////////////        cj_tawc.add(new TableAndWhereClause("conc_resp_assay", " , nsc_compound, nsc_for_export where nsc_compound_fk = nsc_compound.id and nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc"));
////////////        cj_tawc.add(new TableAndWhereClause("conc_resp_element", " , conc_resp_assay, nsc_compound, nsc_for_export where conc_resp_element.conc_resp_assay_fk = conc_resp_assay.id and conc_resp_assay.nsc_compound_fk = nsc_compound.id and nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc"));

        cj_tawc.add(new TableAndWhereClause("dtp_cell_line_data_set", " , cell_line_data_set_for_export where dtp_cell_line_data_set.id = cell_line_data_set_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("dtp_test_result", " , test_result, cell_line_data_set_for_export where dtp_test_result.id = test_result.id and test_result.cell_line_data_set_fk = cell_line_data_set_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("grid_compare_columns", "DO NOT REPLICATE"));
        cj_tawc.add(new TableAndWhereClause("grid_compare_job", "DO NOT REPLICATE"));
        cj_tawc.add(new TableAndWhereClause("grid_compare_rows", "DO NOT REPLICATE"));
        cj_tawc.add(new TableAndWhereClause("job", "DO NOT REPLICATE"));
        cj_tawc.add(new TableAndWhereClause("job_for_req_cell_lines2require", "DO NOT REPLICATE"));
        cj_tawc.add(new TableAndWhereClause("ignore_cell_lines2job_for_ign_", "DO NOT REPLICATE"));
        cj_tawc.add(new TableAndWhereClause("standard_compare_job", "DO NOT REPLICATE"));
        cj_tawc.add(new TableAndWhereClause("test_result", " , cell_line_data_set_for_export where test_result.cell_line_data_set_fk = cell_line_data_set_for_export.id"));
        cj_tawc.add(new TableAndWhereClause("test_result_type", ""));
        cj_tawc.add(new TableAndWhereClause("uploaded_cell_line_data_set", "DO NOT REPLICATE"));
        cj_tawc.add(new TableAndWhereClause("uploaded_ident", "DO NOT REPLICATE"));
        cj_tawc.add(new TableAndWhereClause("uploaded_test_result", "DO NOT REPLICATE"));
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

            srcSqlStr = "create table curated_nsc_smiles_src "
                    + " as "
                    + " select nsc.nsc, gnam.value as generic_name, pnam.value as preferred_name, trg.value as primary_target, ct.can_smi as smiles "
                    + " from curated_nsc nsc "
                    + " left outer join curated_name gnam on nsc.generic_name_fk = gnam.id "
                    + " left outer join curated_name pnam on nsc.preferred_name_fk = pnam.id "
                    + " left outer join curated_target trg on nsc.primary_target_fk = trg.id "
                    + " left outer join cmpd_table ct on nsc.nsc = ct.nsc ";

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
                    + " generic_name varchar, "
                    + " preferred_name varchar, "
                    + " primary_target varchar, "
                    + " smiles varchar)";

            System.out.println(destSqlStr);
            System.out.println();
            destStmt.executeUpdate(destSqlStr);

            // update            
            destSqlStr = "insert into curated_nsc_smiles_dest(nsc, generic_name, preferred_name, primary_target, smiles) values (?,?,?,?,?)";

            System.out.println(destSqlStr);
            System.out.println();
            destPrepStmt = destConn.prepareStatement(destSqlStr);

            srcSqlStr = "select nsc, generic_name, preferred_name, primary_target, smiles from curated_nsc_smiles_src";

            System.out.println(srcSqlStr);
            System.out.println();
            rs = srcStmt.executeQuery(srcSqlStr);

            while (rs.next()) {

                destPrepStmt.setInt(1, rs.getInt("nsc"));
                destPrepStmt.setString(2, rs.getString("generic_name"));
                destPrepStmt.setString(3, rs.getString("preferred_name"));
                destPrepStmt.setString(4, rs.getString("primary_target"));
                destPrepStmt.setString(5, rs.getString("smiles"));

                destPrepStmt.execute();

            }

            rs.close();
            destPrepStmt.close();

            // updating target tables
            if (doCompareTables) {

                // first, try generic_name
                // AS WRITTEN, ONLY UPDATES nulls - WON'T OVERWRITE! (sarcomadb, e.g.)
                destSqlStr = "update synthetic_ident "
                        + " set drug_name = curated_nsc_smiles_dest.generic_name, "
                        + " target = curated_nsc_smiles_dest.primary_target, "
                        + " smiles = curated_nsc_smiles_dest.smiles "
                        + " from curated_nsc_smiles_dest, nsc_ident "
                        + " where synthetic_ident.id = nsc_ident.id "
                        + " and nsc_ident.nsc = curated_nsc_smiles_dest.nsc"
                        + " and synthetic_ident.drug_name is null";

                System.out.println(destSqlStr);
                System.out.println();
                destStmt.executeUpdate(destSqlStr);

                // then, try preferred_name
                // AS WRITTEN, ONLY UPDATES nulls - WON'T OVERWRITE! (sarcomadb, e.g.)
                destSqlStr = "update synthetic_ident "
                        + " set drug_name = curated_nsc_smiles_dest.preferred_name, "
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
                    + " set name = curated_nsc_smiles_dest.generic_name "
                    + " from curated_nsc_smiles_dest "
                    + " where nsc_cmpd.nsc = curated_nsc_smiles_dest.nsc "
                    + " and nsc_cmpd.name is null",
                    //
                    "update cmpd_table "
                    + " set name = curated_nsc_smiles_dest.generic_name "
                    + " from curated_nsc_smiles_dest "
                    + " where cmpd_table.nsc = curated_nsc_smiles_dest.nsc "
                    + " and cmpd_table.name is null",
                    //
                    "update nsc_cmpd "
                    + " set name = curated_nsc_smiles_dest.preferred_name "
                    + " from curated_nsc_smiles_dest "
                    + " where nsc_cmpd.nsc = curated_nsc_smiles_dest.nsc "
                    + " and nsc_cmpd.name is null",
                    //
                    "update cmpd_table "
                    + " set name = curated_nsc_smiles_dest.preferred_name "
                    + " from curated_nsc_smiles_dest "
                    + " where cmpd_table.nsc = curated_nsc_smiles_dest.nsc "
                    + " and cmpd_table.name is null",
                    //
                    // targets
                    // targets
                    // targets
                    //
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
                    + " select  target from cmpd_target",
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
                    + " where cmpd_table.nsc = nsc_target_to_add.nsc;"

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

    public static void nukeAllDestinationTables(Connection srcConn, Connection destConn, ArrayList<TableAndWhereClause> tawcList) throws Exception {

        try {

            for (TableAndWhereClause tawc : tawcList) {
                Replicator.nuke(destConn, tawc.tableName);
            }

        } catch (Exception e) {
            System.out.println("Caught Exception " + e + " in nukeAllDestinationTables");
            e.printStackTrace();
            throw e;
        } finally {

        }
    }

    public static void propagateCompare(Connection srcConn, Connection destConn, ArrayList<TableAndWhereClause> tawcList) throws Exception {

        try {

            for (TableAndWhereClause tawc : tawcList) {

                // replicate the tables
                if (!tawc.whereClause.equals("DO NOT REPLICATE")) {
                    Replicator.useMetadata(srcConn, destConn, tawc.tableName, tawc.whereClause);
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

    public static void propagateDataSystem(Connection srcConn, Connection destConn, ArrayList<TableAndWhereClause> tawcList) throws Exception {

        try {

            for (TableAndWhereClause tawc : tawcList) {

                // replicate the tables
                if (!tawc.whereClause.equals("DO NOT REPLICATE")) {
                    Replicator.useMetadata(srcConn, destConn, tawc.tableName, tawc.whereClause);
                }

            }

            // recreate constraints
//            IndexAndConstraintManagement.createConstraints(destConn);
            System.out.println("Done! in propagateDataSystem");

        } catch (Exception e) {
            System.out.println("Caught Exception " + e + " in propagateDataSystem");
            e.printStackTrace();
            throw e;
        } finally {

        }
    }

    public static void populateRdkitMol(Connection destConn) throws Exception {

        Statement stmt = null;

        try {

            destConn.setAutoCommit(true);
            stmt = destConn.createStatement();

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

            sqlStr = "create table rdkit_mol(id bigin, nsc int, mol mol, mol_from_ctab mol)";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            sqlStr = "insert into rdkit_mol(id, nsc, mol, mol_from_ctab) select nsc, nsc, mol_from_smiles(can_taut), mol_from_ctab(ctab) from cmpd_table";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

        } catch (SQLException se) {
            System.out.println("SQL Exception in prepareCompareIdentsForExport:");
            // Loop through the SQL Exceptions
            while (se != null) {
                System.out.println("State  : " + se.getSQLState());
                System.out.println("Message: " + se.getMessage());
                System.out.println("Error  : " + se.getErrorCode());
                se = se.getNextException();
            }
            throw new Exception(se);
        } catch (Exception e) {
            System.out.println("Exception in saveConstraints:");
            System.out.println(e);
            throw (e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (Exception e) {
                System.out.println("Exception in finally clause in saveConstraints: " + e);
                e.printStackTrace();
                throw (e);
            }
        }

    }

    public static void prepareCompareIdentsForExport(Connection srcConn)
            throws Exception {

        System.out.println("In prepareCompareIdentsForExport");

        Statement stmt = null;

        try {

            srcConn.setAutoCommit(true);
            stmt = srcConn.createStatement();

            // EXPECTS to find nsc_for_export(prefix, nsc)
            // EXPECTS to find target_set_name_for_export(target_set_name);
/*
            
ONCOLOGY DRUGS
            
drop table if exists app_and_inv;

create table app_and_inv(
generic_name varchar,
preferred_name varchar,
alias_names varchar,
originator varchar,
nsc int,
cas varchar,
primary_target varchar,
other_targets varchar,
type varchar,
project_code varchar
);

\copy app_and_inv from '/home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/dtp_datasystem/SCRIPTS/APP_AND_INV/APP_AND_INV_25_MAY_2016.csv' csv header null as '' delimiter as E'\t'
            
drop table if exists nsc_for_export;
create table nsc_for_export(prefix varchar, nsc int);
insert into nsc_for_export(prefix, nsc) select 'S', nsc from app_and_inv;
                        
drop table if exists target_set_names_for_export;
create table target_set_names_for_export(target_set_name varchar);
            
insert into target_set_names_for_export(target_set_name) values ('MIR_ISRAEL');
insert into target_set_names_for_export(target_set_name) values ('WEINSTEIN_CROCE_MIR');
insert into target_set_names_for_export(target_set_name) values ('MOLTID_GC_SERIES_MICROARRAY_MOCK_TX_2H');
            
PUBLIC COMPARE            
            
drop table if exists nsc_for_export;
create table nsc_for_export(prefix varchar, nsc int);
insert into nsc_for_export(prefix, nsc) select 'S', nsc from publicrelease;
                        
drop table if exists target_set_names_for_export;
create table target_set_names_for_export(target_set_name varchar);
           
insert into target_set_names_for_export(target_set_name) values ('MOLTID_GC_SERIES_MICROARRAY_MOCK_TX_2H');
insert into target_set_names_for_export(target_set_name) values ('MOLTID_GC_SERIES_MICROARRAY_NOVARTIS');
insert into target_set_names_for_export(target_set_name) values ('MOLTID_GC_SERIES_MICROARRAY_GENELOGIC_U133');
insert into target_set_names_for_export(target_set_name) values ('MOLTID_GC_SERIES_MICROARRAY_CHIRON');
insert into target_set_names_for_export(target_set_name) values ('MIR_ISRAEL');
insert into target_set_names_for_export(target_set_name) values ('WEINSTEIN_CROCE_MIR');            
            
             */
            String[] dcArr = new String[]{
                "drop table if exists cell_line_data_set_ident_for_export",
                "drop table if exists cell_line_data_set_for_export",
                "create table cell_line_data_set_ident_for_export(id bigint)",
                "create table cell_line_data_set_for_export(id bigint)"
            };

            ArrayList<String> dcList = new ArrayList<String>(Arrays.asList(dcArr));

            for (String sql : dcList) {
                System.out.println(sql);
                stmt.executeUpdate(sql);
            }

            // NSC
            String sqlStr = "insert into cell_line_data_set_ident_for_export(id) "
                    + " select ni.id "
                    + " from nsc_ident ni, nsc_for_export nfe "
                    + " where ni.prefix = nfe.prefix"
                    + " and ni.nsc = nfe.nsc"
                    + " and ni.exp_id = 'AVGDATA'";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            // MolTarg from named_target_set
            sqlStr = "insert into cell_line_data_set_ident_for_export(id) "
                    + " select cell_line_data_sets_fk "
                    + " from named_target_set nts, cell_line_data_sets2named_targ clds2nts "
                    + " where clds2nts.named_target_sets_fk = nts.id "
                    + " and nts.target_set_name in ( "
                    + " select target_set_name from target_set_names_for_export "
                    + " )";

            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            sqlStr = "insert into cell_line_data_set_for_export(id) "
                    + "select id from cell_line_data_set "
                    + "where cell_line_data_set_ident_fk in (select id from cell_line_data_set_ident_for_export)";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            sqlStr = "create index cldsfe_id_idx on cell_line_data_set_for_export(id)";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            sqlStr = "create index cldsife_id_idx on cell_line_data_set_ident_for_export(id)";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            System.out.println("DONE!");

        } catch (SQLException se) {
            System.out.println("SQL Exception in prepareCompareIdentsForExport:");
            // Loop through the SQL Exceptions
            while (se != null) {
                System.out.println("State  : " + se.getSQLState());
                System.out.println("Message: " + se.getMessage());
                System.out.println("Error  : " + se.getErrorCode());
                se = se.getNextException();
            }
            throw new Exception(se);
        } catch (Exception e) {
            System.out.println("Exception in saveConstraints:");
            System.out.println(e);
            throw (e);
        } finally {
            try {
                if (stmt != null) {
                    stmt.close();
                    stmt = null;
                }
            } catch (Exception e) {
                System.out.println("Exception in finally clause in saveConstraints: " + e);
                e.printStackTrace();
                throw (e);
            }
        }

    }

}
