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
public class FetchProjectsAndSets {

    static int BATCH_FETCH_SIZE = 1000;

    public static void fetchProjects(Connection postgresConn, Connection oracleConn) throws Exception {

        Statement oracleStmt = null;
        Statement postgresStmt = null;
        PreparedStatement postgresPrepStmt = null;
        ResultSet rs = null;
        String sqlString;
        String prepStmtString;
        long startTime = 0;
        long elapsedTime = 0;
        int batchCounter = 0;
        int totalBatchCounter = 0;
        int cumulativeCounter = 0;
        int batchInsertSize = 100000;

        try {
            postgresStmt = postgresConn.createStatement();
            oracleStmt = oracleConn.createStatement();

            sqlString = "drop table if exists prod_projects";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "create table prod_projects (nsc int, project_code varchar(1024), project_type varchar(1024))";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "select nsc, projectcode, projecttype from OPS$ORADIS.chem_project";

            System.out.println(sqlString);

            rs = oracleStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_projects(nsc, project_code, project_type) values(?,?,?)";
            postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(postgresPrepStmt);

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;

                int nsc = rs.getInt("nsc");
                String projectCode = rs.getString("projectcode");
                String projectType = rs.getString("projecttype");

                postgresPrepStmt.setInt(1, nsc);
                postgresPrepStmt.setString(2, projectCode);
                postgresPrepStmt.setString(3, projectType);

                postgresPrepStmt.addBatch();

                if (batchCounter > BATCH_FETCH_SIZE) {
                    totalBatchCounter += batchCounter;
                    cumulativeCounter += batchCounter;
                    if (totalBatchCounter > batchInsertSize) {
                        totalBatchCounter = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        System.out.println("strc batchSize: " + BATCH_FETCH_SIZE + " cumulativeCounter: " + cumulativeCounter + " batchSize: " + batchInsertSize + " in " + elapsedTime + " msec");
                    }
                    postgresPrepStmt.executeBatch();
                    postgresConn.commit();
                    batchCounter = 0;
                }
            }
            postgresPrepStmt.executeBatch();
            postgresConn.commit();

            sqlString = "drop index if exists prod_projects_nsc";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_projects_nsc on prod_projects(nsc)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "drop index if exists prod_projects_project_code";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_projects_project_code on prod_projects(project_code)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);


        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
            throw new Exception("Exception in fetchCmpd " + e);
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

    public static void fetchPlatedSets(Connection postgresConn, Connection oracleConn) throws Exception {

        // have to add distinct filter since DIS tracks different interations of same plate

        Statement oracleStmt = null;
        Statement postgresStmt = null;
        PreparedStatement postgresPrepStmt = null;
        ResultSet rs = null;
        String sqlString;
        String prepStmtString;
        long startTime = 0;
        long elapsedTime = 0;
        int batchCounter = 0;
        int totalBatchCounter = 0;
        int cumulativeCounter = 0;
        int batchInsertSize = 100000;

        try {
            postgresStmt = postgresConn.createStatement();
            oracleStmt = oracleConn.createStatement();

            sqlString = "drop table if exists prod_plated_sets";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "create table prod_plated_sets (nsc int, plate_set varchar(1024))";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "select distinct nsc, plateset from ops$oradis.oradis_dis_well";

            System.out.println(sqlString);

            rs = oracleStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_plated_sets(nsc, plate_set) values(?,?)";
            postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(postgresPrepStmt);

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;

                int nsc = rs.getInt("nsc");
                String plateSet = rs.getString("plateset");

                postgresPrepStmt.setInt(1, nsc);
                postgresPrepStmt.setString(2, plateSet);

                postgresPrepStmt.addBatch();

                if (batchCounter > BATCH_FETCH_SIZE) {
                    totalBatchCounter += batchCounter;
                    cumulativeCounter += batchCounter;
                    if (totalBatchCounter > batchInsertSize) {
                        totalBatchCounter = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        System.out.println("strc batchSize: " + BATCH_FETCH_SIZE + " cumulativeCounter: " + cumulativeCounter + " batchSize: " + batchInsertSize + " in " + elapsedTime + " msec");
                    }
                    postgresPrepStmt.executeBatch();
                    postgresConn.commit();
                    batchCounter = 0;
                }
            }
            postgresPrepStmt.executeBatch();
            postgresConn.commit();

            sqlString = "drop index if exists prod_plated_sets_nsc";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_plated_sets_nsc on prod_plated_sets(nsc)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
            throw new Exception("Exception in fetchCmpd " + e);
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
