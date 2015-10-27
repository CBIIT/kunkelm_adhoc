/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author mwkunkel
 */
public class GetPublicRelease {

    // datasystem to oncologydrugs
    static final String[] ds2od = new String[]{
        "jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov",
        "ops$kunkel",
        "donkie",
        "jdbc:postgresql://localhost:5432/privatecomparedb",
        "mwkunkel",
        "donkie11"
    };

    public static void main(String[] args) {

        String[] whichConnectionInfo = ds2od; // compare2od;

        Connection sourceConn = null;
        Connection destConn = null;

        Statement destStmt = null;
        Statement sourceStmt = null;

        try {

            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            DriverManager.registerDriver(new org.postgresql.Driver());

            sourceConn = DriverManager.getConnection(whichConnectionInfo[0], whichConnectionInfo[1], whichConnectionInfo[2]);
            destConn = DriverManager.getConnection(whichConnectionInfo[3], whichConnectionInfo[4], whichConnectionInfo[5]);

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
