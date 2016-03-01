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
public class Replicator {

    public static final Integer BATCH_SIZE = 50; //50000;

    public static void useMetadata(
            Connection sourceConn,
            Connection destConn,
            String tblName,
            String whereClause)
            throws Exception {

        long startTime = 0;
        long elapsedTime = 0;

        ResultSet rs = null;

        Statement sourceStmt = null;
        Statement destStmt = null;

        PreparedStatement prepStmt = null;

        startTime = System.currentTimeMillis();

        try {

            sourceConn.setAutoCommit(false);

            // setting this on the statement
            // ALSO set on ResultSet below
            sourceStmt = sourceConn.createStatement();
            sourceStmt.setFetchSize(BATCH_SIZE);
            sourceStmt.setFetchDirection(ResultSet.FETCH_FORWARD);

            destConn.setAutoCommit(true);
            destStmt = destConn.createStatement();

            System.out.println();
            System.out.println();
            System.out.println("Now processing table: " + tblName);
            System.out.println("-----whereClause: " + whereClause);

            System.out.println("-----Deleting from destination: " + tblName);
            int result = destStmt.executeUpdate("delete from " + tblName);

            System.out.println("-----Selecting from source: " + tblName);

            String selStr = "";

            // K L U D G E !
            if (tblName.equals("publicrelease")) {
                selStr = "select * from structure_edit.publicrelease";
            } else {
                selStr = "select * from " + tblName + whereClause;
            }

            System.out.println("-----" + selStr);
            rs = sourceStmt.executeQuery(selStr);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            ResultSetMetaData md = rs.getMetaData();

            int columnCount = md.getColumnCount();

            StringBuilder iB = new StringBuilder();
            iB.append("insert into " + tblName + "( ");
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

            System.out.println("-----Preparing for batch insert to destination: " + tblName);
            System.out.println(iB.toString());

            prepStmt = destConn.prepareStatement(iB.toString());

            int cumCnt = 0;

            //have to catch those few tables that have lots of rows.
            int prepStmtCounter = 0;
            while (rs.next()) {
                prepStmtCounter += 1;
                for (int i = 1; i <= columnCount; i++) {
                    prepStmt.setObject(i, rs.getObject(i));
                }
                prepStmt.addBatch();
                if (prepStmtCounter == BATCH_SIZE) {

                    elapsedTime = System.currentTimeMillis() - startTime;
                    startTime = System.currentTimeMillis();

                    cumCnt += prepStmtCounter;
                    System.out.println("-----prepStmt.batch size is " + BATCH_SIZE + ".  Executing. cumCnt: " + cumCnt + " elapsedTime: " + elapsedTime);
                    prepStmt.executeBatch();
                    prepStmtCounter = 0;
                }
            }
            // stragglers
            prepStmt.executeBatch();

            rs.close();
            prepStmt.close();
            sourceStmt.close();
            destStmt.close();

            rs = null;
            prepStmt = null;
            sourceStmt = null;
            destStmt = null;

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
                if (prepStmt != null) {
                    prepStmt.close();
                    prepStmt = null;
                }
                if (sourceStmt != null) {
                    sourceStmt.close();
                    sourceStmt = null;
                }
                if (destStmt != null) {
                    destStmt.close();
                    destStmt = null;
                }

            } catch (Exception e) {
                System.out.println("Exception in finally clause in useMetadata: " + e);
                e.printStackTrace();
            }
        }
    }

    public static void nuke(
            Connection destConn,
            String tblName)
            throws Exception {

        Statement destStmt = null;

        try {

            destConn.setAutoCommit(true);

            destStmt = destConn.createStatement();

            System.out.println("-----Deleting from table: " + tblName);
            int result = destStmt.executeUpdate("delete from " + tblName);

            destStmt.close();
            destStmt = null;

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
                if (destStmt != null) {
                    destStmt.close();
                    destStmt = null;
                }

            } catch (Exception e) {
                System.out.println("Exception in finally clause in useMetadata: " + e);
                e.printStackTrace();
                throw (e);
            }
        }
    }

}
