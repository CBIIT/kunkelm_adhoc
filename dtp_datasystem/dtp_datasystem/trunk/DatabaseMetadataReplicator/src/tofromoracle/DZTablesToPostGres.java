/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools   Templates
 * and open the template in the editor.
 */
package tofromoracle;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import static shared.FetchFromProd.handleCatch;
import static shared.FetchFromProd.handleFinally;

/**
 *
 * @author mwkunkel
 */
public class DZTablesToPostGres {

    private static final int BATCH_FETCH_SIZE = 10000;
    private static final int BATCH_INSERT_SIZE = 10000;

    public static void main(String[] args) {

        Connection srcConn = null;
        Connection destConn = null;

        try {

            System.out.println("Starting main.");

            System.out.println("Registering drivers.");
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            DriverManager.registerDriver(new org.postgresql.Driver());

            System.out.println("Opening srcConn");
            srcConn = DriverManager.getConnection("jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");

            System.out.println("Opening destConn");
            destConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");

//    MUST DISABLE AUTOCOMMIT for fetchForward cursor (batch fetches) to work
//    EACH ResultSet MUST also have fetchDirection set FETCH_FORWARD
            srcConn.setAutoCommit(false);
            destConn.setAutoCommit(false);

//            pp2cleaned(destConn, srcConn);
//            destConn.commit();

            pp2cleanedErrors(destConn, srcConn);
            destConn.commit();
            
            pp2cleanedSalts(destConn, srcConn);
            destConn.commit();
            
            pp2cleanedSolvents(destConn, srcConn);
            destConn.commit();

        } catch (Exception e) {
            System.out.println("Caught Exception in main: " + e);
            e.printStackTrace();
        } finally {
            if (srcConn != null) {
                System.out.println("Closing srcConn.");
                try {
                    srcConn.close();
                    srcConn = null;
                } catch (SQLException sqle) {
                    System.out.println("Error in closing srcConn " + sqle.getMessage());
                    Exception ex;
                    while ((ex = sqle.getNextException()) != null) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
            if (destConn != null) {
                System.out.println("Closing destConn.");
                try {
                    destConn.close();
                    destConn = null;
                } catch (SQLException sqle) {
                    System.out.println("Error in closing destConn " + sqle.getMessage());
                    Exception ex;
                    while ((ex = sqle.getNextException()) != null) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }
    }

    public static void pp2cleaned(
            Connection destConn,
            Connection srcConn) throws Exception {

        Statement destStmt = null;
        Statement srcStmt = null;

        PreparedStatement destPrepStmt = null;

        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        try {

            srcStmt = srcConn.createStatement();
            destStmt = destConn.createStatement();

            try {
                sqlString = "drop table if exists pp2_cleaned";
                System.out.println(sqlString);
                destStmt.execute(sqlString);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

            sqlString = "create table pp2_cleaned( nsc int, smiles varchar, taut_smiles varchar, taut_nostereo_smiles varchar, parent_mw double precision, parent_molf varchar, full_mw double precision, full_molf varchar, organometallic varchar, h_fragment varchar, deleted_pseudoatoms int, dis_mw_check varchar, cleanup_class varchar, cleanup_version varchar, cleanup_status varchar, ctab text )";
            System.out.println(sqlString);
            destStmt.execute(sqlString);

            sqlString = "select nsc, smiles, taut_smiles, taut_nostereo_smiles, parent_mw, parent_molf, full_mw, full_molf, organometallic, h_fragment, deleted_pseudoatoms, dis_mw_check, cleanup_class, cleanup_version, cleanup_status, ctab from structure_edit.pp2_cleaned";
            System.out.println(sqlString);
            resSet = srcStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into pp2_cleaned(nsc, smiles, taut_smiles, taut_nostereo_smiles, parent_mw, parent_molf, full_mw, full_molf, organometallic, h_fragment, deleted_pseudoatoms, dis_mw_check, cleanup_class, cleanup_version, cleanup_status, ctab) "
                    + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            destPrepStmt = destConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                destPrepStmt.setInt(1, resSet.getInt("nsc"));
                destPrepStmt.setString(2, resSet.getString("smiles"));
                destPrepStmt.setString(3, resSet.getString("taut_smiles"));
                destPrepStmt.setString(4, resSet.getString("taut_nostereo_smiles"));
                destPrepStmt.setDouble(5, resSet.getDouble("parent_mw"));
                destPrepStmt.setString(6, resSet.getString("parent_molf"));
                destPrepStmt.setDouble(7, resSet.getDouble("full_mw"));
                destPrepStmt.setString(8, resSet.getString("full_molf"));
                destPrepStmt.setString(9, resSet.getString("organometallic"));
                destPrepStmt.setString(10, resSet.getString("h_fragment"));
                destPrepStmt.setInt(11, resSet.getInt("deleted_pseudoatoms"));
                destPrepStmt.setString(12, resSet.getString("dis_mw_check"));
                destPrepStmt.setString(13, resSet.getString("cleanup_class"));
                destPrepStmt.setString(14, resSet.getString("cleanup_version"));
                destPrepStmt.setString(15, resSet.getString("cleanup_status"));
                destPrepStmt.setString(16, resSet.getString("ctab"));

                destPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        System.out.println("cumCnt: " + cumCnt + " BATCH_FETCH_SIZE: " + BATCH_FETCH_SIZE +  " BATCH_INSERT_SIZE: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    destPrepStmt.executeBatch();
                    destConn.commit();
                    batCnt = 0;
                }
            }
            destPrepStmt.executeBatch();
            destConn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            handleCatch(e, destStmt, srcStmt, destPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(destStmt, srcStmt, destPrepStmt, resSet);
        }
    }

    public static void pp2cleanedErrors(
            Connection destConn,
            Connection srcConn) throws Exception {

        Statement destStmt = null;
        Statement srcStmt = null;

        PreparedStatement destPrepStmt = null;

        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        try {

            srcStmt = srcConn.createStatement();
            destStmt = destConn.createStatement();

            try {
                sqlString = "drop table if exists pp2_cleaned_errors";
                System.out.println(sqlString);
                destStmt.execute(sqlString);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;                
            }

            sqlString = "create table pp2_cleaned_errors( nsc int, errortype varchar, errorcomment varchar)";
            System.out.println(sqlString);
            destStmt.execute(sqlString);

            sqlString = "select nsc, errortype, errorcomment from structure_edit.pp2_cleaned_errors";
            System.out.println(sqlString);
            resSet = srcStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into pp2_cleaned_errors(nsc, errortype, errorcomment) "
                    + " values(?,?,?)";
            destPrepStmt = destConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                destPrepStmt.setInt(1, resSet.getInt("nsc"));
                destPrepStmt.setString(2, resSet.getString("errortype"));
                destPrepStmt.setString(3, resSet.getString("errorcomment"));

                destPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        System.out.println("cumCnt: " + cumCnt + " BATCH_FETCH_SIZE: " + BATCH_FETCH_SIZE +  " BATCH_INSERT_SIZE: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    destPrepStmt.executeBatch();
                    destConn.commit();
                    batCnt = 0;
                }
            }
            destPrepStmt.executeBatch();
            destConn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            handleCatch(e, destStmt, srcStmt, destPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(destStmt, srcStmt, destPrepStmt, resSet);
        }
    }

    public static void pp2cleanedSalts(
            Connection destConn,
            Connection srcConn) throws Exception {

        Statement destStmt = null;
        Statement srcStmt = null;

        PreparedStatement destPrepStmt = null;

        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        try {

            srcStmt = srcConn.createStatement();
            destStmt = destConn.createStatement();

            try {
                sqlString = "drop table if exists pp2_cleaned_salts";
                System.out.println(sqlString);
                destStmt.execute(sqlString);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

            sqlString = "create table pp2_cleaned_salts( nsc int, saltname varchar, saltcoeff double precision)";
            System.out.println(sqlString);
            destStmt.execute(sqlString);

            sqlString = "select nsc, saltname, saltcoeff from structure_edit.pp2_cleaned_salts";
            System.out.println(sqlString);
            resSet = srcStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into pp2_cleaned_salts(nsc, saltname, saltcoeff) "
                    + " values(?,?,?)";
            destPrepStmt = destConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                destPrepStmt.setInt(1, resSet.getInt("nsc"));
                destPrepStmt.setString(2, resSet.getString("saltname"));
                destPrepStmt.setDouble(3, resSet.getDouble("saltcoeff"));

                destPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        System.out.println("cumCnt: " + cumCnt + " BATCH_FETCH_SIZE: " + BATCH_FETCH_SIZE +  " BATCH_INSERT_SIZE: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    destPrepStmt.executeBatch();
                    destConn.commit();
                    batCnt = 0;
                }
            }
            destPrepStmt.executeBatch();
            destConn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            handleCatch(e, destStmt, srcStmt, destPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(destStmt, srcStmt, destPrepStmt, resSet);
        }
    }

    public static void pp2cleanedSolvents(
            Connection destConn,
            Connection srcConn) throws Exception {

        Statement destStmt = null;
        Statement srcStmt = null;

        PreparedStatement destPrepStmt = null;

        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        try {

            srcStmt = srcConn.createStatement();
            destStmt = destConn.createStatement();

            try {
                sqlString = "drop table if exists pp2_cleaned_solvents";
                System.out.println(sqlString);
                destStmt.execute(sqlString);
            } catch (Exception e) {
                e.printStackTrace();
                throw e;
            }

            sqlString = "create table pp2_cleaned_solvents( nsc int, solventname varchar, solventcoeff double precision)";
            System.out.println(sqlString);
            destStmt.execute(sqlString);

            sqlString = "select nsc, solventname, solventcoeff from structure_edit.pp2_cleaned_solvents";
            System.out.println(sqlString);
            resSet = srcStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into pp2_cleaned_solvents(nsc, solventname, solventcoeff) "
                    + " values(?,?,?)";
            destPrepStmt = destConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                destPrepStmt.setInt(1, resSet.getInt("nsc"));
                destPrepStmt.setString(2, resSet.getString("solventname"));
                destPrepStmt.setDouble(3, resSet.getDouble("solventcoeff"));

                destPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        System.out.println("cumCnt: " + cumCnt + " BATCH_FETCH_SIZE: " + BATCH_FETCH_SIZE +  " BATCH_INSERT_SIZE: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    destPrepStmt.executeBatch();
                    destConn.commit();
                    batCnt = 0;
                }
            }
            destPrepStmt.executeBatch();
            destConn.commit();

        } catch (Exception e) {
            e.printStackTrace();
            handleCatch(e, destStmt, srcStmt, destPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(destStmt, srcStmt, destPrepStmt, resSet);
        }
    }

}
