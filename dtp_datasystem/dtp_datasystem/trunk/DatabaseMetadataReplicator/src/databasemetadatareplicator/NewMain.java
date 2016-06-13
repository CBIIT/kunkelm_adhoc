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
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mwkunkel
 */
public class NewMain {

    static Map<String, ConnectionInfo> connMap = new HashMap<String, ConnectionInfo>();

    static {

        connMap.put("microxenodb_local", new ConnectionInfo(
                "microxenodb_local",
                "jdbc:postgresql://localhost:5432/microxenodb",
                "mwkunkel",
                "donkie11"
        ));

        connMap.put("microxenodb_dev", new ConnectionInfo(
                "microxenodb_dev",
                "jdbc:postgresql://ncidb-d115-d.nci.nih.gov:5473/microxeno",
                "microxeno",
                "M1cr0x0293025en0"
        ));

        connMap.put("datasystemdb_local", new ConnectionInfo(
                "datasystemdb_local",
                "jdbc:postgresql://localhost:5432/datasystemdb",
                "mwkunkel",
                "donkie11"
        ));

        connMap.put("fakedatasystemdb_local", new ConnectionInfo(
                "fakedatasystemdb_local",
                "jdbc:postgresql://localhost:5432/fakedatasystemdb",
                "mwkunkel",
                "donkie11"
        ));

        connMap.put("oncologydrugsdb_local", new ConnectionInfo(
                "oncologydrugsdb_local",
                "jdbc:postgresql://localhost:5432/oncologydrugsdb",
                "mwkunkel",
                "donkie11"
        ));

        connMap.put("oncologydrugsdb_dev", new ConnectionInfo(
                "oncologydrugsdb_dev",
                "jdbc:postgresql://ncidb-d115-d:5474/oncology",
                "oncology",
                "OnC0L029302802K1t"
        ));

        connMap.put("publiccomparedb_local", new ConnectionInfo(
                "publiccomparedb_local",
                "jdbc:postgresql://localhost:5432/publiccomparedb",
                "mwkunkel",
                "donkie11"
        ));

        connMap.put("publiccomparedb_dev", new ConnectionInfo(
                "publiccomparedb_dev",
                "jdbc:postgresql://ncidb-d115-d:5473/pubcompare",
                "publiccompare",
                "P3b092094wlC0m3"
        ));

        connMap.put("privatecomparedb_local", new ConnectionInfo(
                "privatecomparedb_local",
                "jdbc:postgresql://localhost:5432/privatecomparedb",
                "mwkunkel",
                "donkie11"
        ));

        connMap.put("sarcomacomparedb_local", new ConnectionInfo(
                "sarcomacomparedb_local",
                "jdbc:postgresql://localhost:5432/sarcomacomparedb",
                "mwkunkel",
                "donkie11"
        ));

        Set<String> dbNameSet = connMap.keySet();
        ArrayList<String> dbNameList = new ArrayList<String>(dbNameSet);
        Collections.sort(dbNameList);

        for (String dbName : dbNameList) {
            connMap.get(dbName).asProperties();
        }

    }

    static ArrayList<TableAndWhereClause> microxeno_tawc = new ArrayList<TableAndWhereClause>();

    static {

        microxeno_tawc.add(new TableAndWhereClause("affymetrix_Identifier", ""));
        microxeno_tawc.add(new TableAndWhereClause("tumor", ""));
        microxeno_tawc.add(new TableAndWhereClause("tumor_type", ""));
        microxeno_tawc.add(new TableAndWhereClause("flat_data", ""));

    }

    static ArrayList<TableAndWhereClause> datasystem_tawc = new ArrayList<TableAndWhereClause>();

    static {

        datasystem_tawc.add(new TableAndWhereClause("ad_hoc_cmpd", "DO NOT REPLICATE"));
        datasystem_tawc.add(new TableAndWhereClause("ad_hoc_cmpd_fragment", "DO NOT REPLICATE"));
        datasystem_tawc.add(new TableAndWhereClause("ad_hoc_cmpd_fragment_p_chem", "DO NOT REPLICATE"));
        datasystem_tawc.add(new TableAndWhereClause("ad_hoc_cmpd_fragment_structure", "DO NOT REPLICATE"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_known_salt", ""));
        datasystem_tawc.add(new TableAndWhereClause("nsc_cmpd_type", ""));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_alias_type", ""));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_relation_type", ""));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_fragment_type", ""));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_inventory", " where id in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_annotation", " where id in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_bio_assay", " where id in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_legacy_cmpd", " where id in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_table", " where nsc in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd", " where id in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("nsc_cmpd", " where nsc in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_fragment", " where nsc_cmpd_fk in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_fragment_p_chem", " where id in (select cmpd_fragment_p_chem_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_export))"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_fragment_structure", " where id in (select cmpd_fragment_structure_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_export))"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_alias", " where id in (select cmpd_aliases_fk from cmpd_aliases2nsc_cmpds where cmpd_aliases2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_aliases2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_list", "DO NOT REPLICATE"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_list_member", "DO NOT REPLICATE"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_named_set", " where id in (select cmpd_named_sets_fk from cmpd_named_sets2nsc_cmpds where cmpd_named_sets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_named_sets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_plate", " where id in (select cmpd_plates_fk from cmpd_plates2nsc_cmpds where cmpd_plates2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_plates2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_project", " where id in (select cmpd_projects_fk from cmpd_projects2nsc_cmpds where cmpd_projects2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_projects2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_pub_chem_sid", " where id in (select cmpd_pub_chem_sids_fk from cmpd_pub_chem_sids2nsc_cmpds where cmpd_pub_chem_sids2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_pub_chem_sids2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_target", " where id in (select cmpd_targets_fk from cmpd_targets2nsc_cmpds where cmpd_targets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_targets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("cmpd_related", " where nsc_cmpd_fk in (select nsc from nsc_for_export)"));
        datasystem_tawc.add(new TableAndWhereClause("rdkit_mol", " where nsc in (select nsc from nsc_for_export)"));
        /*
        
        
        
   curated   
        
        
        
        
         */
        datasystem_tawc.add(new TableAndWhereClause("curated_name", ""));
        datasystem_tawc.add(new TableAndWhereClause("curated_nsc", ""));
        datasystem_tawc.add(new TableAndWhereClause("curated_nsc_smiles", ""));
        datasystem_tawc.add(new TableAndWhereClause("curated_nsc_to_secondary_targe", ""));
        datasystem_tawc.add(new TableAndWhereClause("curated_nscs2projects", ""));
        datasystem_tawc.add(new TableAndWhereClause("curated_originator", ""));
        datasystem_tawc.add(new TableAndWhereClause("curated_project", ""));
        datasystem_tawc.add(new TableAndWhereClause("curated_target", ""));

    }

    static ArrayList<TableAndWhereClause> compare_tawc = new ArrayList<TableAndWhereClause>();

    static {
        compare_tawc.add(new TableAndWhereClause("affymetrix_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
        compare_tawc.add(new TableAndWhereClause("build_date", ""));
        compare_tawc.add(new TableAndWhereClause("cell_line_data_set", " where id in (select id from cell_line_data_set_for_export)"));
        compare_tawc.add(new TableAndWhereClause("cell_line_data_set_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
        compare_tawc.add(new TableAndWhereClause("cell_line_data_sets2named_targ", " where cell_line_data_sets_fk in (select id from cell_line_data_set_for_export)"));
        compare_tawc.add(new TableAndWhereClause("compare_cell_line", ""));
        compare_tawc.add(new TableAndWhereClause("compare_result", "DO NOT REPLICATE"));
        // still need to handle this!
        compare_tawc.add(new TableAndWhereClause("conc_resp_assay", " where nsc_compound_fk in (select id from nsc_compound where nsc in (select nsc from nsc_for_export))"));
        compare_tawc.add(new TableAndWhereClause("conc_resp_element", " where conc_resp_assay_fk in (select id from conc_resp_assay where nsc_compound_fk in (select id from nsc_compound where nsc in (select nsc from nsc_for_export)))"));

        compare_tawc.add(new TableAndWhereClause("dtp_cell_line_data_set", " where id in (select id from cell_line_data_set_for_export)"));

        // handle generalization of test_result               
        compare_tawc.add(new TableAndWhereClause("dtp_test_result", " where id in (select id from test_result where cell_line_data_set_fk in (select id from cell_line_data_set_for_export))"));

        compare_tawc.add(new TableAndWhereClause("grid_compare_columns", "DO NOT REPLICATE"));
        compare_tawc.add(new TableAndWhereClause("grid_compare_job", "DO NOT REPLICATE"));
        compare_tawc.add(new TableAndWhereClause("grid_compare_rows", "DO NOT REPLICATE"));
        compare_tawc.add(new TableAndWhereClause("job", "DO NOT REPLICATE"));
        compare_tawc.add(new TableAndWhereClause("mol_targ_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
        compare_tawc.add(new TableAndWhereClause("named_target_set", ""));
        compare_tawc.add(new TableAndWhereClause("nano_string_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
        compare_tawc.add(new TableAndWhereClause("nat_prod_ident", "DO NOT REPLICATE"));

        // still need to handle this!        
        compare_tawc.add(new TableAndWhereClause("nsc_compound", " where nsc in (select nsc from nsc_for_export)"));

        compare_tawc.add(new TableAndWhereClause("nsc_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
        compare_tawc.add(new TableAndWhereClause("require_use_ignore", "DO NOT REPLICATE"));
        compare_tawc.add(new TableAndWhereClause("standard_compare_job", "DO NOT REPLICATE"));
        compare_tawc.add(new TableAndWhereClause("synthetic_ident", " where id in (select id from cell_line_data_set_ident_for_export)"));
        compare_tawc.add(new TableAndWhereClause("test_result", " where cell_line_data_set_fk in (select id from cell_line_data_set_for_export)"));
        compare_tawc.add(new TableAndWhereClause("test_result_type", ""));
        compare_tawc.add(new TableAndWhereClause("uploaded_cell_line_data_set", "DO NOT REPLICATE"));
        compare_tawc.add(new TableAndWhereClause("uploaded_ident", "DO NOT REPLICATE"));
        compare_tawc.add(new TableAndWhereClause("uploaded_test_result", "DO NOT REPLICATE"));
    }

    public static void main(String[] args) {

        // COMPARE
        //
//        //Conn srcInfo = connMap.get("privatecomparedb_local");
//        ConnectionInfo srcInfo = connMap.get("sarcomacomparedb_local");
//        ConnectionInfo destInfo = connMap.get("publiccomparedb_local");
//        ArrayList<TableAndWhereClause> tawcList = compare_tawc;
        ConnectionInfo srcInfo = connMap.get("datasystemdb_local");
        ConnectionInfo destInfo = connMap.get("sarcomacomparedb_local");
        
//        ConnectionInfo destInfo = connMap.get("fakedatasystemdb_local");
        ArrayList<TableAndWhereClause> tawcList = datasystem_tawc;

        Connection srcConn = null;
        Connection destConn = null;

        try {

            DriverManager.registerDriver(new org.postgresql.Driver());
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

            srcConn = DriverManager.getConnection(srcInfo.dbUrl, srcInfo.dbUser, srcInfo.dbPass);
            destConn = DriverManager.getConnection(destInfo.dbUrl, srcInfo.dbUser, srcInfo.dbPass);

            // propagateCompare(srcConn, destConn);
            // propagateDataSystem(srcConn, destConn);
            propagateCuratedNsc(srcConn, destConn);

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

    public static void propagateCuratedNsc(Connection srcConn, Connection destConn) throws Exception {

        Statement srcStmt = null;
        Statement destStmt = null;
        ResultSet rs = null;
        PreparedStatement destPrepStmt = null;

        try {

            srcStmt = srcConn.createStatement();
            destStmt = destConn.createStatement();

            // source (probably almost always datasystemdb_local
            String sqlStr = "drop table if exists curated_nsc_smiles";

            System.out.println(sqlStr);
            srcStmt.executeUpdate(sqlStr);

            sqlStr = "create table curated_nsc_smiles "
                    + " as "
                    + " select nsc.nsc, gnam.value as generic_name, pnam.value as preferred_name, trg.value as primary_target, ct.can_smi as smiles "
                    + " from curated_nsc nsc "
                    + " left outer join curated_name gnam on nsc.generic_name_fk = gnam.id "
                    + " left outer join curated_name pnam on nsc.preferred_name_fk = pnam.id "
                    + " left outer join curated_target trg on nsc.primary_target_fk = trg.id "
                    + " left outer join cmpd_table ct on nsc.nsc = ct.nsc ";

            System.out.println(sqlStr);
            srcStmt.executeUpdate(sqlStr);

            // dest
            sqlStr = "drop table if exists curated_nsc_smiles";

            System.out.println(sqlStr);
            destStmt.executeUpdate(sqlStr);

            sqlStr = "create table curated_nsc_smiles( "
                    + " nsc int, "
                    + " generic_name varchar, "
                    + " preferred_name varchar, "
                    + " primary_target varchar, "
                    + " smiles varchar)";

            System.out.println(sqlStr);
            destStmt.executeUpdate(sqlStr);

            // update
            sqlStr = "insert into curated_nsc_smiles(nsc, generic_name, preferred_name, primary_target, smiles) values (?,?,?,?,?)";

            System.out.println(sqlStr);
            destPrepStmt = destConn.prepareStatement(sqlStr);

            sqlStr = "select nsc, generic_name, preferred_name, primary_target, smiles from curated_nsc_smiles";

            System.out.println(sqlStr);
            rs = srcStmt.executeQuery(sqlStr);

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
            
            // the update statement depends on which connection has been called

            sqlStr = "update synthetic_ident "
                    + " set drug_name = curated_nsc_smiles.generic_name, "
                    + " target = curated_nsc_smiles.primary_target, "
                    + " smiles = curated_nsc_smiles.smiles "
                    + " from curated_nsc_smiles, nsc_ident "
                    + " where synthetic_ident.id = nsc_ident.id "
                    + " and nsc_ident.nsc = curated_nsc_smiles.nsc"
                    + " and synthetic_ident.drug_name is null";
            
            System.out.println(sqlStr);
            destStmt.executeUpdate(sqlStr);
            
            sqlStr = "update synthetic_ident "
                    + " set drug_name = curated_nsc_smiles.preferred_name, "
                    + " target = curated_nsc_smiles.primary_target, "
                    + " smiles = curated_nsc_smiles.smiles "
                    + " from curated_nsc_smiles, nsc_ident "
                    + " where synthetic_ident.id = nsc_ident.id "
                    + " and nsc_ident.nsc = curated_nsc_smiles.nsc"
                    + " and synthetic_ident.drug_name is null";
            
            System.out.println(sqlStr);
            destStmt.executeUpdate(sqlStr);
            
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

    public static void propagateCompare(Connection srcConn, Connection destConn) throws Exception {

        ArrayList<TableAndWhereClause> tawcList = compare_tawc;

        try {

            prepareCompareIdents(srcConn);

            // archive constraints
            IndexAndConstraintManagement.saveConstraints(destConn, compare_tawc);

            // drop constraints
            IndexAndConstraintManagement.dropConstraints(destConn);

            for (TableAndWhereClause tawc : tawcList) {

                // scrape out anything remaining
                Replicator.nuke(destConn, tawc.tableName);

                // replicate the tables
                if (!tawc.whereClause.equals("DO NOT REPLICATE")) {
                    Replicator.useMetadata(srcConn, destConn, tawc.tableName, tawc.whereClause);
                }

            }

            // recreate constraints
            IndexAndConstraintManagement.createConstraints(destConn);

            System.out.println("Done! in propagateCompare");

        } catch (Exception e) {
            System.out.println("Caught Exception " + e + " in propagateCompare");
            e.printStackTrace();
            throw e;
        } finally {

        }
    }

    public static void propagateDataSystem(Connection srcConn, Connection destConn) throws Exception {

        ArrayList<TableAndWhereClause> tawcList = datasystem_tawc;

        try {

            // archive constraints
            IndexAndConstraintManagement.saveConstraints(destConn, tawcList);

            // drop constraints
            IndexAndConstraintManagement.dropConstraints(destConn);

            for (TableAndWhereClause tawc : tawcList) {

                // scrape out anything remaining
                Replicator.nuke(destConn, tawc.tableName);

                // replicate the tables
                if (!tawc.whereClause.equals("DO NOT REPLICATE")) {
                    Replicator.useMetadata(srcConn, destConn, tawc.tableName, tawc.whereClause);
                }

            }

            // recreate constraints
            IndexAndConstraintManagement.createConstraints(destConn);

            System.out.println("Done! in propagateDataSystem");

        } catch (Exception e) {
            System.out.println("Caught Exception " + e + " in propagateDataSystem");
            e.printStackTrace();
            throw e;
        } finally {

        }
    }

    public static void prepareCompareIdents(Connection conn)
            throws Exception {

        Statement stmt = null;

        try {

            conn.setAutoCommit(true);
            stmt = conn.createStatement();

            // set up for stacking ids for nsc, and mol_targ
            //
            // EXPECTS to find nsc_for_export(prefix, nsc)
            // EXPECTS to find molt_id_for_eport(molt_id)
            String sqlStr = "drop table if exists cell_line_data_set_ident_for_export";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            sqlStr = "drop table if exists cell_line_data_set_for_export";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            sqlStr = "create table cell_line_data_set_ident_for_export(id bigint)";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            sqlStr = "create table cell_line_data_set_for_export(id bigint)";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            sqlStr = "insert into cell_line_data_set_ident_for_export(id) "
                    + "select id from nsc_ident "
                    + "where (prefix, nsc) in (select prefix, nsc from nsc_for_export)";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            sqlStr = "insert into cell_line_data_set_ident_for_export(id) "
                    + "select id from mol_targ_ident "
                    + "where molt_id in (select molt_id from molt_id_for_export)";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            sqlStr = "insert into cell_line_data_set_for_export(id) "
                    + "select id from cell_line_data_set "
                    + "where cell_line_data_set_ident_fk in (select id from cell_line_data_set_ident_for_export)";
            System.out.println(sqlStr);
            stmt.executeUpdate(sqlStr);

            System.out.println("DONE!");

        } catch (SQLException se) {
            System.out.println("SQL Exception in prepareIdents:");
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
