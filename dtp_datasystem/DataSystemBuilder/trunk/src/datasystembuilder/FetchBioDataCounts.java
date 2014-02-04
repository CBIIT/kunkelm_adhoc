/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasystembuilder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mwkunkel
 */
public class FetchBioDataCounts {

  static int BATCH_FETCH_SIZE = 1000;

  public static void fetchBioDataCounts(Connection postgresConn, Connection oracleConn) throws Exception {

    try {

      String[] paramArray = new String[]{
        "prod_count_hf",
        "select ed.nsc, count(*) the_count from invivo.exp_decode ed, ops$oradis.dis_cmpd c where ed.assay_type = 9 and c.prefix = 'S' and ed.prefix = c.prefix and ed.nsc = c.nsc group by ed.nsc",
        "prod_count_nci60",
        "select ctl.nsc, count(distinct ctl.expid) the_count from c_tline ctl, ops$oradis.dis_cmpd c where c.prefix = 'S' and ctl.prefix = c.prefix and ctl.nsc = c.nsc group by ctl.nsc",
        "prod_count_xeno",
        "select ed.nsc, count(*) the_count from invivo.exp_decode ed, ops$oradis.dis_cmpd c where ed.assay_type in (3, 5) and c.prefix = 'S' and ed.prefix = c.prefix and ed.nsc = c.nsc group by ed.nsc"
      };

      String tableName = "";
      String queryString = "";

      for (int i = 0; i < paramArray.length; i++) {
        if (i % 2 == 0) {
          tableName = paramArray[i];
          queryString = paramArray[i + 1];          
          genericFetch(postgresConn, oracleConn, queryString, tableName);
        }
      }

      String[] sqlArray = new String[]{
        "drop table if exists prod_bio_counts",
        "create table prod_bio_counts "
        + "as "
        + "select "
        + "prod_src.nsc, "
        + "prod_count_nci60.the_count as prod_count_nci60, "
        + "prod_count_hf.the_count as prod_count_hf, "
        + "prod_count_xeno.the_count as prod_count_xeno "
        + "from prod_src "
        + "left outer join prod_count_nci60 on prod_src.nsc = prod_count_nci60.nsc "
        + "left outer join prod_count_hf on prod_src.nsc = prod_count_hf.nsc "
        + "left outer join prod_count_xeno on prod_src.nsc = prod_count_xeno.nsc"
      };
      
      



    } catch (Exception e) {
      System.out.println("Caught Exception " + e);
      e.printStackTrace();
      throw new Exception("Exception in fetchBioDataCounts " + e);
    }

  }

  protected static void genericFetch(Connection postgresConn,
          Connection oracleConn,
          String queryString,
          String tableName) throws Exception {
      
      System.out.println("queryString is: " + queryString);
      System.out.println("tableName is: " + tableName);

    Statement oracleStmt = null;
    Statement postgresStmt = null;
    PreparedStatement postgresPrepStmt = null;

    ResultSet rs = null;

    long startTime = 0;
    long elapsedTime = 0;
    int batchCounter = 0;
    int totalBatchCounter = 0;
    int cumulativeCounter = 0;
    int batchInsertSize = 100000;

    try {

      postgresStmt = postgresConn.createStatement();
      oracleStmt = oracleConn.createStatement();

      String sqlString = "drop table if exists " + tableName;

      System.out.println(sqlString);
      postgresStmt.execute(sqlString);

      sqlString = "create table " + tableName + "(nsc int, the_count int)";
      System.out.println(sqlString);
      postgresStmt.execute(sqlString);

      System.out.println(queryString);

      rs = oracleStmt.executeQuery(queryString);
      rs.setFetchDirection(ResultSet.FETCH_FORWARD);

      String prepStmtString = "insert into " + tableName + "(nsc, the_count) values(?,?)";
      postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
      System.out.println(sqlString);
      System.out.println(postgresPrepStmt);

      startTime = System.currentTimeMillis();

      while (rs.next()) {
        batchCounter++;

        int nsc = rs.getInt("nsc");
        int theCount = rs.getInt("the_count");

        postgresPrepStmt.setInt(1, nsc);
        postgresPrepStmt.setInt(2, theCount);

        postgresPrepStmt.addBatch();

        if (batchCounter > BATCH_FETCH_SIZE) {
          totalBatchCounter += batchCounter;
          cumulativeCounter += batchCounter;
          if (totalBatchCounter > batchInsertSize) {
            totalBatchCounter = 0;
            elapsedTime = System.currentTimeMillis() - startTime;
            startTime = System.currentTimeMillis();
            System.out.println("batchSize: " + BATCH_FETCH_SIZE + " cumulativeCounter: " + cumulativeCounter + " batchSize: " + batchInsertSize + " in " + elapsedTime + " msec");
          }
          postgresPrepStmt.executeBatch();
          postgresConn.commit();
          batchCounter = 0;
        }
      }
      postgresPrepStmt.executeBatch();
      postgresConn.commit();

      sqlString = "drop index if exists " + tableName + "_nsc";
      System.out.println(sqlString);
      postgresStmt.execute(sqlString);

      sqlString = "create index " + tableName + "_nsc on " + tableName + "(nsc)";
      System.out.println(sqlString);
      postgresStmt.execute(sqlString);

    } catch (Exception e) {
      System.out.println("Caught Exception " + e);
      e.printStackTrace();
      throw new Exception("Exception in genericFetch " + e);
    } finally {
      if (oracleStmt != null) {
        try {
          oracleStmt.close();
          oracleStmt = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing oracleStmt");
        }
      }
      if (postgresStmt != null) {
        try {
          postgresStmt.close();
          postgresStmt = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresStmt");
        }
      }
      if (postgresPrepStmt != null) {
        try {
          postgresPrepStmt.close();
          postgresPrepStmt = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresPrepStmt");
        }
      }
    }
  }
 
}
