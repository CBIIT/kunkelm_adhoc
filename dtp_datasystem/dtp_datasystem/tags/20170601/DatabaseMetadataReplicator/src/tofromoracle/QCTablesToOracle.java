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
public class QCTablesToOracle {

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

            srcConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");

            System.out.println("Opening destConn");
            destConn = DriverManager.getConnection("jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");

//    MUST DISABLE AUTOCOMMIT for fetchForward cursor (batch fetches) to work
//    EACH ResultSet MUST also have fetchDirection set FETCH_FORWARD

            srcConn.setAutoCommit(false);
            destConn.setAutoCommit(false);

            System.out.println();

            pushWranglingWithNsc(destConn, srcConn);
            destConn.commit();

            pushWranglingWithCategories(destConn, srcConn);
            destConn.commit();

            pushWranglingGoodToGo(destConn, srcConn);
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

    public static void pushWranglingWithNsc(
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
                sqlString = "drop table wrangling_with_nsc";
                System.out.println(sqlString);
                destStmt.execute(sqlString);
            } catch (Exception e) {
                // Silent if table doesn't exist
            }

            sqlString = "create table wrangling_with_nsc("
                    + " nsc int, "
                    + " prod_mf varchar2(1024), "
                    + " prod_mw double precision, "
                    + " mf varchar2(1024), "
                    + " fw double precision, "
                    + " fc int, "
                    + " mf_neut varchar2(1024), "
                    + " fw_neut double precision, "
                    + " fc_neut int, "
                    + " count_frags int, "
                    + " count_lbl int, "
                    + " lbls varchar2(1024), "
                    + " count_strc int, "
                    + " count_rgrp_frags int, "
                    + " salt int, "
                    + " total_type_frags int, "
                    + " inventory double precision, "
                    + " prod_count_nci60 integer, "
                    + " norm_errors varchar2(1024), "
                    + " diff_mw double precision,"
                    + " diff_neut_mw double precision)";
            System.out.println(sqlString);
            destStmt.execute(sqlString);

            sqlString = "select nsc,prod_mf,prod_mw,mf,fw,fc,mf_neut,fw_neut,fc_neut,count_frags,count_lbl,lbls,count_strc,count_rgrp_frags,salt,total_type_frags,inventory,prod_count_nci60,norm_errors,diff_mw, diff_neut_mw from wrangling_with_nsc";
            System.out.println(sqlString);
            resSet = srcStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into wrangling_with_nsc(nsc,prod_mf,prod_mw,mf,fw,fc,mf_neut,fw_neut,fc_neut,count_frags,count_lbl,lbls,count_strc,count_rgrp_frags,salt,total_type_frags,inventory,prod_count_nci60,norm_errors,diff_mw,diff_neut_mw) "
                    + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            destPrepStmt = destConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                destPrepStmt.setInt(1, resSet.getInt("nsc"));
                destPrepStmt.setString(2, resSet.getString("prod_mf"));
                destPrepStmt.setDouble(3, resSet.getDouble("prod_mw"));
                destPrepStmt.setString(4, resSet.getString("mf"));
                destPrepStmt.setDouble(5, resSet.getDouble("fw"));
                destPrepStmt.setInt(6, resSet.getInt("fc"));
                destPrepStmt.setString(7, resSet.getString("mf_neut"));
                destPrepStmt.setDouble(8, resSet.getDouble("fw_neut"));
                destPrepStmt.setInt(9, resSet.getInt("fc_neut"));
                destPrepStmt.setInt(10, resSet.getInt("count_frags"));
                destPrepStmt.setInt(11, resSet.getInt("count_lbl"));
                destPrepStmt.setString(12, resSet.getString("lbls"));
                destPrepStmt.setInt(13, resSet.getInt("count_strc"));
                destPrepStmt.setInt(14, resSet.getInt("count_rgrp_frags"));
                destPrepStmt.setString(15, resSet.getString("salt"));
                destPrepStmt.setInt(16, resSet.getInt("total_type_frags"));
                destPrepStmt.setDouble(17, resSet.getDouble("inventory"));
                destPrepStmt.setInt(18, resSet.getInt("prod_count_nci60"));
                destPrepStmt.setString(19, resSet.getString("norm_errors"));
                destPrepStmt.setDouble(20, resSet.getDouble("diff_mw"));
                destPrepStmt.setDouble(21, resSet.getDouble("diff_neut_mw"));

                destPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        System.out.println("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    destPrepStmt.executeBatch();
                    destConn.commit();
                    batCnt = 0;
                }
            }
            destPrepStmt.executeBatch();
            destConn.commit();

            sqlString = "grant all on wrangling_with_nsc to OPS$SVETLIK with grant option";
            System.out.println(sqlString);
            destStmt.execute(sqlString);

        } catch (Exception e) {
            e.printStackTrace();
            handleCatch(e, destStmt, srcStmt, destPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(destStmt, srcStmt, destPrepStmt, resSet);
        }
    }

    public static void pushWranglingWithCategories(
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
                sqlString = "drop table wrangling_with_categories";
                System.out.println(sqlString);
                destStmt.execute(sqlString);
            } catch (Exception e) {
                // Silent if table doesn't exist
            }

            sqlString = "create table wrangling_with_categories("
                    + " category varchar2(1024), "
                    + " nsc int, "
                    + " prod_mf varchar2(1024), "
                    + " prod_mw double precision, "
                    + " mf varchar2(1024), "
                    + " fw double precision, "
                    + " fc int, "
                    + " mf_neut varchar2(1024), "
                    + " fw_neut double precision, "
                    + " fc_neut int, "
                    + " count_frags int, "
                    + " count_lbl int, "
                    + " lbls varchar2(1024), "
                    + " count_strc int, "
                    + " count_rgrp_frags int, "
                    + " salt int, "
                    + " total_type_frags int, "
                    + " inventory double precision, "
                    + " prod_count_nci60 integer, "
                    + " norm_errors varchar2(1024), "
                    + " diff_mw double precision,"
                    + " diff_neut_mw double precision)";
            System.out.println(sqlString);
            destStmt.execute(sqlString);

            sqlString = "select category,nsc,prod_mf,prod_mw,mf,fw,fc,mf_neut,fw_neut,fc_neut,count_frags,count_lbl,lbls,count_strc,count_rgrp_frags,salt,total_type_frags,inventory,prod_count_nci60,norm_errors,diff_mw, diff_neut_mw from wrangling_with_categories";
            System.out.println(sqlString);
            resSet = srcStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into wrangling_with_categories(category,nsc,prod_mf,prod_mw,mf,fw,fc,mf_neut,fw_neut,fc_neut,count_frags,count_lbl,lbls,count_strc,count_rgrp_frags,salt,total_type_frags,inventory,prod_count_nci60,norm_errors,diff_mw,diff_neut_mw) "
                    + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            destPrepStmt = destConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                destPrepStmt.setString(1, resSet.getString("category"));
                destPrepStmt.setInt(2, resSet.getInt("nsc"));
                destPrepStmt.setString(3, resSet.getString("prod_mf"));
                destPrepStmt.setDouble(4, resSet.getDouble("prod_mw"));
                destPrepStmt.setString(5, resSet.getString("mf"));
                destPrepStmt.setDouble(6, resSet.getDouble("fw"));
                destPrepStmt.setInt(7, resSet.getInt("fc"));
                destPrepStmt.setString(8, resSet.getString("mf_neut"));
                destPrepStmt.setDouble(9, resSet.getDouble("fw_neut"));
                destPrepStmt.setInt(10, resSet.getInt("fc_neut"));
                destPrepStmt.setInt(11, resSet.getInt("count_frags"));
                destPrepStmt.setInt(12, resSet.getInt("count_lbl"));
                destPrepStmt.setString(13, resSet.getString("lbls"));
                destPrepStmt.setInt(14, resSet.getInt("count_strc"));
                destPrepStmt.setInt(15, resSet.getInt("count_rgrp_frags"));
                destPrepStmt.setString(16, resSet.getString("salt"));
                destPrepStmt.setInt(17, resSet.getInt("total_type_frags"));
                destPrepStmt.setDouble(18, resSet.getDouble("inventory"));
                destPrepStmt.setInt(19, resSet.getInt("prod_count_nci60"));
                destPrepStmt.setString(20, resSet.getString("norm_errors"));
                destPrepStmt.setDouble(21, resSet.getDouble("diff_mw"));
                destPrepStmt.setDouble(22, resSet.getDouble("diff_neut_mw"));

                destPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        System.out.println("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    destPrepStmt.executeBatch();
                    destConn.commit();
                    batCnt = 0;
                }
            }
            destPrepStmt.executeBatch();
            destConn.commit();

            sqlString = "grant all on wrangling_with_categories to OPS$SVETLIK with grant option";
            System.out.println(sqlString);
            destStmt.execute(sqlString);

        } catch (Exception e) {
            e.printStackTrace();
            handleCatch(e, destStmt, srcStmt, destPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(destStmt, srcStmt, destPrepStmt, resSet);
        }
    }

    public static void pushWranglingGoodToGo(
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
                sqlString = "drop table wrangling_good_to_go";
                System.out.println(sqlString);
                destStmt.execute(sqlString);
            } catch (Exception e) {
                // Silent if table doesn't exist
            }

            sqlString = "create table wrangling_good_to_go("
                    + " judgement varchar2(1024), "
                    + " category varchar2(1024), "
                    + " nsc int, "
                    + " prod_mf varchar2(1024), "
                    + " prod_mw double precision, "
                    + " mf varchar2(1024), "
                    + " fw double precision, "
                    + " fc int, "
                    + " mf_neut varchar2(1024), "
                    + " fw_neut double precision, "
                    + " fc_neut int, "
                    + " count_frags int, "
                    + " count_lbl int, "
                    + " lbls varchar2(1024), "
                    + " count_strc int, "
                    + " count_rgrp_frags int, "
                    + " salt int, "
                    + " total_type_frags int, "
                    + " inventory double precision, "
                    + " prod_count_nci60 integer, "
                    + " norm_errors varchar2(1024), "
                    + " diff_mw double precision, "
                    + " diff_neut_mw double precision)";

            System.out.println(sqlString);
            destStmt.execute(sqlString);

            sqlString = "select judgement,category,nsc,prod_mf,prod_mw,mf,fw,fc,mf_neut,fw_neut,fc_neut,count_frags,count_lbl,lbls,count_strc,count_rgrp_frags,salt,total_type_frags,inventory,prod_count_nci60,norm_errors,diff_mw, diff_neut_mw from wrangling_good_to_go";
            System.out.println(sqlString);
            resSet = srcStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into wrangling_good_to_go(judgement,category,nsc,prod_mf,prod_mw,mf,fw,fc,mf_neut,fw_neut,fc_neut,count_frags,count_lbl,lbls,count_strc,count_rgrp_frags,salt,total_type_frags,inventory,prod_count_nci60,norm_errors,diff_mw,diff_neut_mw)"
                    + " values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
            destPrepStmt = destConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                destPrepStmt.setString(1, resSet.getString("judgement"));
                destPrepStmt.setString(2, resSet.getString("category"));
                destPrepStmt.setInt(3, resSet.getInt("nsc"));
                destPrepStmt.setString(4, resSet.getString("prod_mf"));
                destPrepStmt.setDouble(5, resSet.getDouble("prod_mw"));
                destPrepStmt.setString(6, resSet.getString("mf"));
                destPrepStmt.setDouble(7, resSet.getDouble("fw"));
                destPrepStmt.setInt(8, resSet.getInt("fc"));
                destPrepStmt.setString(9, resSet.getString("mf_neut"));
                destPrepStmt.setDouble(10, resSet.getDouble("fw_neut"));
                destPrepStmt.setInt(11, resSet.getInt("fc_neut"));
                destPrepStmt.setInt(12, resSet.getInt("count_frags"));
                destPrepStmt.setInt(13, resSet.getInt("count_lbl"));
                destPrepStmt.setString(14, resSet.getString("lbls"));
                destPrepStmt.setInt(15, resSet.getInt("count_strc"));
                destPrepStmt.setInt(16, resSet.getInt("count_rgrp_frags"));
                destPrepStmt.setString(17, resSet.getString("salt"));
                destPrepStmt.setInt(18, resSet.getInt("total_type_frags"));
                destPrepStmt.setDouble(19, resSet.getDouble("inventory"));
                destPrepStmt.setInt(20, resSet.getInt("prod_count_nci60"));
                destPrepStmt.setString(21, resSet.getString("norm_errors"));
                destPrepStmt.setDouble(22, resSet.getDouble("diff_mw"));
                destPrepStmt.setDouble(23, resSet.getDouble("diff_neut_mw"));

                destPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        System.out.println("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    destPrepStmt.executeBatch();
                    destConn.commit();
                    batCnt = 0;
                }
            }
            destPrepStmt.executeBatch();
            destConn.commit();

            sqlString = "grant all on wrangling_good_to_go to OPS$SVETLIK with grant option";
            System.out.println(sqlString);
            destStmt.execute(sqlString);

        } catch (Exception e) {
            e.printStackTrace();
            handleCatch(e, destStmt, srcStmt, destPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(destStmt, srcStmt, destPrepStmt, resSet);
        }
    }

}
