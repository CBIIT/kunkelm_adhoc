/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.sql.*;
import java.util.ArrayList;

/**
 *
 * @author mwkunkel
 */
public class DatasystemReplicator {

    static final String[] tableNamesAndWhereClauses = new String[]{
        // ad_hoc_cmpd not exported
        //                "ad_hoc_cmpd", " where ",
        //                "ad_hoc_cmpd_fragment", " where ",
        //                "ad_hoc_cmpd_fragment_p_chem", " where ",
        //                "ad_hoc_cmpd_fragment_structure", " where ",

        "cmpd_known_salt", "",
        "nsc_cmpd_type", "",
        "cmpd_alias_type", "",
        "cmpd_relation_type", "",
        "cmpd_fragment_type", "",
        //
        "cmpd_inventory", " where id in (select nsc from for_export)",
        "cmpd_annotation", " where id in (select nsc from for_export)",
        "cmpd_bio_assay", " where id in (select nsc from for_export)",
        "cmpd_legacy_cmpd", " where nsc in (select nsc from for_export)",
        "cmpd_table", " where nsc in (select nsc from for_export)",
        // 
        "cmpd", " where id in (select nsc from for_export)",
        //
        "nsc_cmpd", " where nsc in (select nsc from for_export)",
        //
        "cmpd_fragment", " where nsc_cmpd_fk in (select nsc from for_export)",
        "cmpd_fragment_p_chem", " where id in (select cmpd_fragment_p_chem_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from for_export))",
        "cmpd_fragment_structure", " where id in (select cmpd_fragment_structure_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from for_export))",
        "cmpd_alias", " where id in (select cmpd_aliases_fk from cmpd_aliases2nsc_cmpds where cmpd_aliases2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_aliases2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        // 
        //  cmpd_list not exported
        //                "cmpd_list", " where ",
        //                "cmpd_list_member", " where ",
        //
        "cmpd_named_set", " where id in (select cmpd_named_sets_fk from cmpd_named_sets2nsc_cmpds where cmpd_named_sets2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_named_sets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        //
        "cmpd_plate", " where id in (select cmpd_plates_fk from cmpd_plates2nsc_cmpds where cmpd_plates2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_plates2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        //
        "cmpd_project", " where id in (select cmpd_projects_fk from cmpd_projects2nsc_cmpds where cmpd_projects2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_projects2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        "cmpd_pub_chem_sid", " where id in (select cmpd_pub_chem_sids_fk from cmpd_pub_chem_sids2nsc_cmpds where cmpd_pub_chem_sids2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_pub_chem_sids2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        "cmpd_target", " where id in (select cmpd_targets_fk from cmpd_targets2nsc_cmpds where cmpd_targets2nsc_cmpds.nsc_cmpds_fk in (select nsc from for_export))",
        "cmpd_targets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from for_export)",
        //
        "cmpd_related", " where nsc_cmpd_fk in (select nsc from for_export)",
        //
        "rdkit_mol", " where nsc in (select nsc from for_export)"
    };

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        Connection destConn = null;
        Connection sourceConn = null;

        try {

            DriverManager.registerDriver(new org.postgresql.Driver());

            sourceConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");
            destConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/virgin_datasystemdb", "mwkunkel", "donkie11");

            // archive constraint drop/create statements
            // ConstraintManagement.saveConstraints(destConn);
            
            // drop constraints before build
            ConstraintManagement.dropConstraints(destConn);

            // scrape out anything remaining
            fullNuke(sourceConn, destConn);
            
            // replicate the tables
            useMetadata(sourceConn, destConn);

            // recreate the constraints
            
            ConstraintManagement.createConstraints(destConn);

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

    public static void useMetadata(Connection sourceConn, Connection destConn)
            throws Exception {

        ResultSet rs = null;

        Statement sourceStmt = null;
        Statement destStmt = null;

        PreparedStatement prepStmt = null;

        try {

            sourceStmt = sourceConn.createStatement();
            sourceStmt.setFetchSize(1000);

            destConn.setAutoCommit(true);

            destStmt = destConn.createStatement();

            for (int tableCounter = 0; tableCounter < tableNamesAndWhereClauses.length; tableCounter += 2) {

                String curTbl = tableNamesAndWhereClauses[tableCounter];
                String curWhereClause = tableNamesAndWhereClauses[tableCounter + 1];

                System.out.println();
                System.out.println();
                System.out.println("Now processing table: " + curTbl);
                System.out.println("-----curWhereClause: " + curWhereClause);

                System.out.println("-----Deleting from destination: " + curTbl);
                int result = destStmt.executeUpdate("delete from " + curTbl);

                System.out.println("-----Selecting from source: " + curTbl);
                String selStr = "select * from " + curTbl + curWhereClause;
                System.out.println("-----" + selStr);
                rs = sourceStmt.executeQuery(selStr);

                ResultSetMetaData md = rs.getMetaData();

                int columnCount = md.getColumnCount();

                StringBuilder iB = new StringBuilder();
                iB.append("insert into " + curTbl + "( ");
                for (int i = 1; i <= columnCount; i++) {
                    iB.append(md.getColumnLabel(i));
                    if (i < columnCount) {
                        iB.append(", ");
                    }
                }
                iB.append(") values (");
                for (int i = 1; i <= columnCount; i++) {
                    iB.append("?");
                    if (i < columnCount) {
                        iB.append(", ");
                    }
                }
                iB.append(")");

                System.out.println("-----Preparing for batch insert to destination: " + curTbl);
                System.out.println(iB.toString());

                prepStmt = destConn.prepareStatement(iB.toString());

                //have to catch those few tables that have lots of rows.
                int prepStmtCounter = 0;
                while (rs.next()) {
                    prepStmtCounter += 1;
                    for (int i = 1; i <= columnCount; i++) {
                        prepStmt.setObject(i, rs.getObject(i));
                    }
                    prepStmt.addBatch();
                    if (prepStmtCounter == 1000) {
                        System.out.println("-----prepStmt.batch size is 1000.  Executing.");
                        prepStmt.executeBatch();
                        prepStmtCounter = 0;
                    }
                }

                prepStmt.executeBatch();

                rs.close();

            }

        } catch (SQLException se) {
            System.out.println("SQL Exception in useMetadata:");
            // Loop through the SQL Exceptions
            while (se != null) {
                System.out.println("State  : " + se.getSQLState());
                System.out.println("Message: " + se.getMessage());
                System.out.println("Error  : " + se.getErrorCode());
                se = se.getNextException();
            }
            throw new Exception(se);
        } catch (Exception e) {
            System.out.println("Exception in useMetadata:");
            System.out.println(e);
            throw (e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (sourceStmt != null) {
                    sourceStmt.close();
                    sourceStmt = null;
                }
                if (prepStmt != null) {
                    prepStmt.close();
                    prepStmt = null;
                }
            } catch (Exception e) {
                System.out.println("Exception in finally clause in useMetadata: " + e);
                e.printStackTrace();
                throw (e);
            }
        }
    }

    public static void fullNuke(Connection sourceConn, Connection destConn)
            throws Exception {

        ResultSet rs = null;

        Statement sourceStmt = null;
        Statement destStmt = null;

        PreparedStatement prepStmt = null;

        try {

            sourceStmt = sourceConn.createStatement();
            sourceStmt.setFetchSize(1000);

            destConn.setAutoCommit(true);

            destStmt = destConn.createStatement();

            for (int tableCounter = 0; tableCounter < tableNamesAndWhereClauses.length; tableCounter += 2) {

                String curTbl = tableNamesAndWhereClauses[tableCounter];
                String curWhereClause = tableNamesAndWhereClauses[tableCounter + 1];

                System.out.println("-----Deleting from destination: " + curTbl);
                int result = destStmt.executeUpdate("delete from " + curTbl);

            }

        } catch (SQLException se) {
            System.out.println("SQL Exception in useMetadata:");
            // Loop through the SQL Exceptions
            while (se != null) {
                System.out.println("State  : " + se.getSQLState());
                System.out.println("Message: " + se.getMessage());
                System.out.println("Error  : " + se.getErrorCode());
                se = se.getNextException();
            }
            throw new Exception(se);
        } catch (Exception e) {
            System.out.println(e);
            throw (e);
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                    rs = null;
                }
                if (sourceStmt != null) {
                    sourceStmt.close();
                    sourceStmt = null;
                }
                if (destStmt != null) {
                    destStmt.close();
                    destStmt = null;
                }
                if (prepStmt != null) {
                    prepStmt.close();
                    prepStmt = null;
                }
            } catch (Exception e) {
                System.out.println("Exception in finally clause in useMetadata: " + e);
                e.printStackTrace();
                throw (e);
            }
        }
    }

}
