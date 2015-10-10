/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.sql.*;

/**
 *
 * @author mwkunkel
 */
public class MicroXenoReplicator {

    /**
     * @param args the command line arguments
     */
    
    public static void main(String[] args) {
        
        Connection destConn = null;
        Connection sourceConn = null;

        try {

            DriverManager.registerDriver(new org.postgresql.Driver());

            sourceConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/microxenodb", "mwkunkel", "donkie11");
            destConn = DriverManager.getConnection("jdbc:postgresql://itbport.nci.nih.gov:5432/microxenodb", "microxeno_user", "microxeno_pass");

            useMetadata(sourceConn, destConn);

        } catch (Exception e) {
            System.out.println("Caught Exception " + e + " in Main.main");
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

    public static void useMetadata(Connection sourceConn, Connection destConn) {

        ResultSet rs = null;

        Statement sourceStmt = null;
        Statement destStmt = null;

        PreparedStatement prepStmt = null;

        try {
            
            sourceStmt = sourceConn.createStatement();
            sourceStmt.setFetchSize(50000);

            destConn.setAutoCommit(true);
            destStmt = destConn.createStatement();

            String[] tableNames = new String[]{
                "affymetrix_identifier",
                "file_resolution",
                "tumor_type",
                "tumor",
                "affymetrix_data"
            };

            for (int tableCounter = 0; tableCounter < tableNames.length; tableCounter++) {

                System.out.println("Now processing table: " + tableNames[tableCounter]);

                int result = destStmt.executeUpdate("delete from " + tableNames[tableCounter]);

                rs = sourceStmt.executeQuery("select * from " + tableNames[tableCounter]);

                ResultSetMetaData md = rs.getMetaData();

                int columnCount = md.getColumnCount();

                StringBuilder iB = new StringBuilder();
                iB.append("insert into " + tableNames[tableCounter] + "( ");
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
                    if (prepStmtCounter == 50000) {
                        System.out.println("prepStmt.batch size is 50000.  Executing.");
                        prepStmt.executeBatch();
                        prepStmtCounter = 0;
                    }
                }

                prepStmt.executeBatch();

                rs.close();

            }

        } catch (SQLException se) {
            System.out.println("SQL Exception:");
            // Loop through the SQL Exceptions
            while (se != null) {
                System.out.println("State  : " + se.getSQLState());
                System.out.println("Message: " + se.getMessage());
                System.out.println("Error  : " + se.getErrorCode());
                se = se.getNextException();
            }
        } catch (Exception e) {
            System.out.println(e);
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
            }
        }
    }
}
