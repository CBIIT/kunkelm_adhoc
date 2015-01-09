/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasystembuilder;

import java.sql.BatchUpdateException;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 *
 * @author mwkunkel
 */
public class FetchFromProd {

    static int BATCH_FETCH_SIZE = 1000;

    public static void fetchMtxt(Connection postgresConn, Connection oracleConn) {

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
        int batchInsertSize = 1000;

        try {
            postgresStmt = postgresConn.createStatement();
            oracleStmt = oracleConn.createStatement();

            sqlString = "drop table if exists prod_mtxt";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "create table prod_mtxt (nsc int, mtxt varchar(4096))";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "select cmpd.nsc, ct.text from ops$oradis.dis_cmpd cmpd, ops$oradis.cmpd_topic ct where cmpd.prefix = 'S' and cmpd.cmpd_id = ct.cmpd_id and ct.topic = 'MTXT'";
            rs = oracleStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_mtxt(nsc, mtxt) values(?,?)";
            postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(postgresPrepStmt);

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;

                int nsc = rs.getInt("nsc");
                String mtxt = rs.getString("text");
                postgresPrepStmt.setInt(1, nsc);
                postgresPrepStmt.setString(2, mtxt);
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
            sqlString = "drop index if exists prod_mtxt_nsc";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_mtxt_nsc on prod_mtxt(nsc)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
        } catch (BatchUpdateException bue) {
            Exception e = new Exception();
            while ((e = bue.getNextException()) != null) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
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

    public static void fetchLegacyImage(Connection postgresConn, Connection oracleConn) {

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
        int batchInsertSize = 1000;

        try {
            postgresStmt = postgresConn.createStatement();
            oracleStmt = oracleConn.createStatement();

            sqlString = "drop table if exists prod_legacy_image";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "create table prod_legacy_image (nsc int, image bytea)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "select nsc, image from ops$kunkel.strc_image";
            rs = oracleStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_legacy_image(nsc, image) values(?,?)";
            postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(postgresPrepStmt);

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;

                int nsc = rs.getInt("nsc");
                byte[] imageBytes = rs.getBytes("image");

                postgresPrepStmt.setInt(1, nsc);
                postgresPrepStmt.setBytes(2, imageBytes);

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
            sqlString = "drop index if exists pli_nsc";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index pli_nsc on prod_legacy_image(nsc)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
        } catch (BatchUpdateException bue) {
            Exception e = new Exception();
            while ((e = bue.getNextException()) != null) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
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

    public static void fetchInventory(Connection postgresConn, Connection oracleConn) throws Exception {

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
        int batchInsertSize = 1000;

        try {

            postgresStmt = postgresConn.createStatement();
            oracleStmt = oracleConn.createStatement();

            sqlString = "drop table if exists prod_inventory";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "create table prod_inventory (nsc int, inventory double precision)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            // older code, not correct
            // sqlString = "select inv.nsc, sum(inv.amtu_weight) as inventory from OPS$ORADIS.inv_amount_uncommited inv, OPS$ORADIS.dis_cmpd cmpd where cmpd.prefix = 'S' and cmpd.nsc = inv.nsc group by inv.nsc";

            sqlString = "select nsc, mg as inventory from ops$oradis.dis_nscs where prefix = 'S'";

            System.out.println(sqlString);

            rs = oracleStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_inventory(nsc, inventory) values(?,?)";
            postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(postgresPrepStmt);

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;

                int nsc = rs.getInt("nsc");
                Double inventory = rs.getDouble("inventory");

                postgresPrepStmt.setInt(1, nsc);
                postgresPrepStmt.setDouble(2, inventory);

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

            sqlString = "drop index if exists prod_inventory_nsc";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_inventory_nsc on prod_inventory(nsc)";
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

    public static void fetchChemNames(Connection postgresConn, Connection oracleConn) throws Exception {

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
        int batchInsertSize = 1000;

        try {
            postgresStmt = postgresConn.createStatement();
            oracleStmt = oracleConn.createStatement();

            sqlString = "drop table if exists prod_chem_name";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "create table prod_chem_name (cmpd_id bigint, nsc int, chem_name varchar(1024), chem_name_type varchar(1024))";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "select cmpd.cmpd_id, cmpd.nsc, cn.name, cn.name_type from OPS$ORADIS.dis_cmpd cmpd, OPS$ORADIS.cmpd_chem_name cn where cmpd.prefix = 'S' and cmpd.cmpd_id = cn.cmpd_id";

            System.out.println(sqlString);

            rs = oracleStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_chem_name(cmpd_id, nsc, chem_name, chem_name_type) values(?,?,?,?)";
            postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(postgresPrepStmt);

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;

                long cmpdId = rs.getLong("cmpd_id");
                int nsc = rs.getInt("nsc");
                String chemName = rs.getString("name");
                String chemNameType = rs.getString("name_type");

                postgresPrepStmt.setLong(1, cmpdId);
                postgresPrepStmt.setInt(2, nsc);
                postgresPrepStmt.setString(3, chemName);
                postgresPrepStmt.setString(4, chemNameType);

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

            sqlString = "drop index if exists prod_chem_name_cmpd_id";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_chem_name_cmpd_id on prod_chem_name(cmpd_id)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "drop index if exists prod_chem_name_nsc";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_chem_name_nsc on prod_chem_name(nsc)";
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

    public static void fetchPubChemId() throws Exception {
    }

    /**
     *
     * @param postgresConn
     * @param oracleConn
     * @throws Exception
     */
    public static void fetchCmpd(Connection postgresConn, Connection oracleConn) throws Exception {

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
        int totalBatchSize = 1000;
        try {
            postgresStmt = postgresConn.createStatement();
            oracleStmt = oracleConn.createStatement();
            sqlString = "drop table if exists prod_cmpd";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create table prod_cmpd (cas integer, cmpd_id integer, conf varchar(32), distribution_code varchar(32), mf varchar(1024), mw double precision, nsc integer, prefix varchar(32))";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "select CAS, CMPD_ID, CONF, DISTRIBUTION_CODE, MF, MW, NSC, PREFIX from OPS$ORADIS.DIS_CMPD where prefix = 'S' and nsc is not null and nsc != 0";
            rs = oracleStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);
            prepStmtString = "insert into prod_cmpd(CAS, CMPD_ID, CONF, DISTRIBUTION_CODE, MF, MW, NSC, PREFIX) values(?,?,?,?,?,?,?,?)";
            postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(postgresPrepStmt);
            startTime = System.currentTimeMillis();
            while (rs.next()) {
                batchCounter++;
                postgresPrepStmt.setInt(1, rs.getInt("CAS"));
                postgresPrepStmt.setInt(2, rs.getInt("CMPD_ID"));
                postgresPrepStmt.setString(3, rs.getString("CONF"));
                postgresPrepStmt.setString(4, rs.getString("DISTRIBUTION_CODE"));
                postgresPrepStmt.setString(5, rs.getString("MF"));
                postgresPrepStmt.setDouble(6, rs.getDouble("MW"));
                postgresPrepStmt.setInt(7, rs.getInt("NSC"));
                postgresPrepStmt.setString(8, rs.getString("PREFIX"));
                postgresPrepStmt.addBatch();
                if (batchCounter > BATCH_FETCH_SIZE) {
                    totalBatchCounter += batchCounter;
                    cumulativeCounter += batchCounter;
                    if (totalBatchCounter > totalBatchSize) {
                        totalBatchCounter = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        System.out.println("cmpd batchSize: " + BATCH_FETCH_SIZE + " cumulativeCounter: " + cumulativeCounter + " batchSize: " + totalBatchSize + " in " + elapsedTime + " msec");
                    }
                    postgresPrepStmt.executeBatch();
                    postgresConn.commit();
                    batchCounter = 0;
                }
            }
            postgresPrepStmt.executeBatch();
            postgresConn.commit();
            sqlString = "drop index if exists prod_cmpd_cmpd_id";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_cmpd_cmpd_id on prod_cmpd(cmpd_id)";
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
                    System.out.println("Error in closing oracleStmt in fetchCmpd");
                }
            }
            if (postgresStmt != null) {
                try {
                    postgresStmt.close();
                    postgresStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing postgresStmt in fetchCmpd");
                }
            }
            if (postgresPrepStmt != null) {
                try {
                    postgresPrepStmt.close();
                    postgresPrepStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing postgresPrepStmt in fetchCmpd");
                }
            }
        }
    }

    /**
     *
     * @param postgresConn
     * @param oracleConn
     * @throws Exception
     */
    public static void fetchRS3(Connection postgresConn, Connection oracleConn) {
        
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
        int batchInsertSize = 1000;

        try {
            postgresStmt = postgresConn.createStatement();
            oracleStmt = oracleConn.createStatement();

            sqlString = "drop table if exists prod_rs3";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "create table prod_rs3 (strc text, strc_id integer)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "select object_contents, structure_id as strc_id from rs3_structure_object where object_type = 'M'";
            rs = oracleStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_rs3(STRC, strc_id) values(?,?)";
            postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(postgresPrepStmt);

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;
                byte[] blobbytes = rs.getBytes("OBJECT_CONTENTS");
                String text = new String(blobbytes);
                postgresPrepStmt.setString(1, text);
                postgresPrepStmt.setInt(2, rs.getInt("strc_id"));
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
            sqlString = "drop index if exists prod_rs3_strc_id";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_rs3_strc_id on prod_rs3(strc_id)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
        } catch (BatchUpdateException bue) {
            Exception e = new Exception();
            while ((e = bue.getNextException()) != null) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
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

   public static void fetchKekule(Connection postgresConn, Connection oracleConn) {
        
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
        int batchInsertSize = 1000;

        try {
            postgresStmt = postgresConn.createStatement();
            oracleStmt = oracleConn.createStatement();

            sqlString = "drop table if exists prod_kekule";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "create table prod_kekule (strc text, strc_id integer)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "select object_contents, structure_id as strc_id from ops$oradis.oradis_structure_object where object_type = 'K'";
            rs = oracleStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_kekule (STRC, strc_id) values(?,?)";
            postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(postgresPrepStmt);

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;
                byte[] blobbytes = rs.getBytes("OBJECT_CONTENTS");
                String text = new String(blobbytes);
                postgresPrepStmt.setString(1, text);
                postgresPrepStmt.setInt(2, rs.getInt("strc_id"));
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
            sqlString = "drop index if exists prod_kekule_strc_id";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_kekule_strc_id on prod_kekule(strc_id)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
        } catch (BatchUpdateException bue) {
            Exception e = new Exception();
            while ((e = bue.getNextException()) != null) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
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
    
    public static void fetchStrc(Connection postgresConn, Connection oracleConn) {
        
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
        int batchInsertSize = 1000;

        try {
            postgresStmt = postgresConn.createStatement();
            oracleStmt = oracleConn.createStatement();

            sqlString = "drop table if exists prod_strc";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "create table prod_strc (strc text, strc_id integer)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            sqlString = "select structure, cmpd_id as strc_id from ops$oradis.dis_structure";
            rs = oracleStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_strc(STRC, strc_id) values(?,?)";
            postgresPrepStmt = postgresConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(postgresPrepStmt);

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;
                byte[] blobbytes = rs.getBytes("structure");
                String text = new String(blobbytes);
                postgresPrepStmt.setString(1, text);
                postgresPrepStmt.setInt(2, rs.getInt("strc_id"));
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
            sqlString = "drop index if exists prod_strc_strc_id";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
            sqlString = "create index prod_strc_strc_id on prod_strc(strc_id)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);
        } catch (BatchUpdateException bue) {
            Exception e = new Exception();
            while ((e = bue.getNextException()) != null) {
                System.out.println(e);
            }
        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
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

    public static void createUnifiedProdSrcTable(Connection postgresConn) throws Exception {

        Statement postgresStmt = null;

        String sqlString;

        long startTime = 0;
        long elapsedTime = 0;
        int batchCounter = 0;
        int totalBatchCounter = 0;
        int cumulativeCounter = 0;
        int batchInsertSize = 1000;

        try {

            postgresStmt = postgresConn.createStatement();

            sqlString = "drop table if exists prod_src";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            postgresConn.commit();

            sqlString = "create table prod_src as select prod_cmpd.*, prod_strc.* from prod_cmpd left outer join prod_strc on prod_cmpd.cmpd_id = prod_strc.strc_id";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

            postgresConn.commit();

            sqlString = "create index prod_src_nsc on prod_src(nsc)";
            System.out.println(sqlString);
            postgresStmt.execute(sqlString);

        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
            throw new Exception("Exception in fetchCmpd " + e);
        } finally {

            if (postgresStmt != null) {
                try {
                    postgresStmt.close();
                    postgresStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing postgresStmt");
                }
            }

        }
    }
}
