/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package writetoaccord;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import oracle.jdbc.OracleDriver;
import org.postgresql.Driver;

//==> /media/KINGSTON/mwk_hts_compound_lot.tsv <==
//lot_id    compound_id    sample_id    salt_code
//
//==> /media/KINGSTON/mwk_hts_compound.tsv <==
//compound_id    description
//
//==> /media/KINGSTON/mwk_hts_salt.tsv <==
//salt_code_number    salt_name    salt_description    salt_weight    salt_formula
//
//==> /media/KINGSTON/mwk_rs3_structure_object.tsv <==
//structure_id    object_type    object_contents
//
//==> /media/KINGSTON/mwk_rs3_structure.tsv <==
//structure_id    registration_date    process_date    class_type
/**
 *
 * @author mwkunkel
 */
public class WriteToAccord {

    static int BATCH_FETCH_SIZE = 10000;

    private static java.sql.Date getCurrentDate() {
        java.util.Date today = new java.util.Date();
        return new java.sql.Date(today.getTime());
    }

    public static void main(String[] args) {

        Connection pgConn = null;
        Connection oraConn = null;

        Driver pgDriver = null;
        OracleDriver oraDriver = null;

        try {

            pgDriver = new org.postgresql.Driver();
            oraDriver = new oracle.jdbc.OracleDriver();

            DriverManager.registerDriver(pgDriver);
            DriverManager.registerDriver(oraDriver);

            pgConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");
            oraConn = DriverManager.getConnection("jdbc:oracle:thin:@ncias-d1310-v.nci.nih.gov:1521/aei.nci.nih.gov", "RS3", "testit2014");

            // MUST DISABLE AUTOCOMMIT for fetchForward cursor (batch fetches) to work
            // EACH ResultSet MUST also have fetchDirection set FETCH_FORWARD
            
            oraConn.setAutoCommit(false);
            pgConn.setAutoCommit(false);

            cleanOut(oraConn);
            populateHtsSalts(pgConn, oraConn);
            populateHtsCompound(pgConn, oraConn);
            populateHtsCompoundLots(pgConn, oraConn);
            populateRS3structure(pgConn, oraConn);
            populateRS3structureObject(pgConn, oraConn);

        } catch (Exception e) {
            System.out.println("Caught Exception " + e + " in WriteToAccord.main");
            e.printStackTrace();
        } finally {
            if (oraConn != null) {
                try {
                    oraConn.close();
                    oraConn = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraConn");
                }
            }
            if (pgConn != null) {
                try {
                    pgConn.close();
                    pgConn = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing pgConn");
                }
            }

            try {
                DriverManager.deregisterDriver(pgDriver);
                DriverManager.deregisterDriver(oraDriver);
            } catch (SQLException ex) {
                System.out.println("Error deregistering drivers.");
            }
        }
    }

    public static void cleanOut(Connection oraConn) throws Exception {

        Statement oraStmt = null;

        String[] sqlArray = {
            "delete from mwk_rs3_structure_object",
            "delete from mwk_rs3_structure",
            "delete from mwk_hts_compound_lot",
            "delete from mwk_hts_compound",
            "delete from mwk_hts_salt"
        };

        try {

            oraStmt = oraConn.createStatement();

            for (String sqlString : Arrays.asList(sqlArray)) {
                System.out.println(sqlString);
                oraStmt.execute(sqlString);
                oraConn.commit();
            }

        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
            throw e;
        } finally {
            if (oraStmt != null) {
                try {
                    oraStmt.close();
                    oraStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraStmt");
                }
            }
        }
    }

    public static void populateHtsSalts(Connection pgConn, Connection oraConn) throws Exception {

        Statement pgStmt = null;
        Statement oraStmt = null;
        PreparedStatement oraPrepStmt = null;
        ResultSet rs = null;

        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batchCounter = 0;
        int totalBatchCounter = 0;
        int cumulativeCounter = 0;
        int batchInsertSize = 10000;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = oraConn.createStatement();

            sqlString = "select distinct salt_code_number, salt_name, salt_description, salt_weight, salt_formula from mwk_hts_salt";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            rs = pgStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into mwk_hts_salt(salt_code, salt_name, salt_desc, salt_weight, salt_formula, multiplier) values(?, ?, ?, ?, ?, ?)";
            oraPrepStmt = oraConn.prepareStatement(prepStmtString);
            System.out.println(prepStmtString);

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                
                batchCounter++;

                long saltCodeNumber = rs.getLong("salt_code_number");

                String saltName = rs.getString("salt_name");
                String truncatedSaltName = "";
                if (saltName.length() > 40) {
                    truncatedSaltName = saltName.substring(0, 39);
                } else {
                    truncatedSaltName = saltName;
                }

                String saltDescription = rs.getString("salt_description");
                Double saltWeight = rs.getDouble("salt_weight");
                String saltFormula = rs.getString("salt_formula");

                oraPrepStmt.setLong(1, saltCodeNumber);
                oraPrepStmt.setString(2, truncatedSaltName);
                oraPrepStmt.setString(3, saltDescription);
                oraPrepStmt.setDouble(4, saltWeight);
                oraPrepStmt.setString(5, saltFormula);
                oraPrepStmt.setDouble(6, 1);

                oraPrepStmt.addBatch();

                if (batchCounter > BATCH_FETCH_SIZE) {

                    totalBatchCounter += batchCounter;
                    cumulativeCounter += batchCounter;

                    if (totalBatchCounter > batchInsertSize) {
                        totalBatchCounter = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        System.out.println("batchFetchSize: " + BATCH_FETCH_SIZE + " cumulativeCounter: " + cumulativeCounter + " batchInsertSize: " + batchInsertSize + " in " + elapsedTime + " msec");
                    }
                    oraPrepStmt.executeBatch();
                    oraConn.commit();
                    batchCounter = 0;
                }
            }
            
            oraPrepStmt.executeBatch();
            oraConn.commit();

        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
            throw e;
        } finally {
            if (oraStmt != null) {
                try {
                    oraStmt.close();
                    oraStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraStmt");
                }
            }
            if (pgStmt != null) {
                try {
                    pgStmt.close();
                    pgStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing pgStmt");
                }
            }
            if (oraPrepStmt != null) {
                try {
                    oraPrepStmt.close();
                    oraPrepStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraPrepStmt");
                }
            }
        }
    }

    public static void populateHtsCompound(Connection pgConn, Connection oraConn) throws Exception {

        Statement pgStmt = null;
        Statement oraStmt = null;
        PreparedStatement oraPrepStmt = null;
        ResultSet rs = null;

        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batchCounter = 0;
        int totalBatchCounter = 0;
        int cumulativeCounter = 0;
        int batchInsertSize = 10000;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = oraConn.createStatement();

            sqlString = "select compound_id, description from mwk_hts_compound "; // "; // where compound_id > 700000 and compound_id < 720000";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            rs = pgStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into mwk_hts_compound(compound_id, description) values(?, ?)";
            oraPrepStmt = oraConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(oraPrepStmt.toString());

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;

                long compoundId = rs.getLong("compound_id");
                String description = rs.getString("description");

                oraPrepStmt.setLong(1, compoundId);
                oraPrepStmt.setString(2, description);

                oraPrepStmt.addBatch();

                if (batchCounter > BATCH_FETCH_SIZE) {

                    totalBatchCounter += batchCounter;
                    cumulativeCounter += batchCounter;

                    if (totalBatchCounter > batchInsertSize) {
                        totalBatchCounter = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        System.out.println("batchFetchSize: " + BATCH_FETCH_SIZE + " cumulativeCounter: " + cumulativeCounter + " batchInsertSize: " + batchInsertSize + " in " + elapsedTime + " msec");
                    }
                    oraPrepStmt.executeBatch();
                    oraConn.commit();
                    batchCounter = 0;
                }
            }
            oraPrepStmt.executeBatch();
            oraConn.commit();

        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
            throw e;
        } finally {
            if (oraStmt != null) {
                try {
                    oraStmt.close();
                    oraStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraStmt");
                }
            }
            if (pgStmt != null) {
                try {
                    pgStmt.close();
                    pgStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing pgStmt");
                }
            }
            if (oraPrepStmt != null) {
                try {
                    oraPrepStmt.close();
                    oraPrepStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraPrepStmt");
                }
            }
        }
    }

    public static void populateHtsCompoundLots(Connection pgConn, Connection oraConn) throws Exception {

        Statement pgStmt = null;
        Statement oraStmt = null;
        PreparedStatement oraPrepStmt = null;
        ResultSet rs = null;

        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batchCounter = 0;
        int totalBatchCounter = 0;
        int cumulativeCounter = 0;
        int batchInsertSize = 10000;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = oraConn.createStatement();

            sqlString = "select lot_id, compound_id, sample_id, salt_code from mwk_hts_compound_lot "; // where compound_id > 700000 and compound_id < 720000";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            rs = pgStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into mwk_hts_compound_lot(lot_id, compound_id, sample_id, salt_code) values(?, ?, ?, ?)";
            oraPrepStmt = oraConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(oraPrepStmt.toString());

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;

                long lotId = rs.getLong("lot_id");
                long compoundId = rs.getLong("compound_id");
                long sampleId = rs.getLong("sample_id");
                long saltCode = rs.getLong("salt_code");

                oraPrepStmt.setLong(1, lotId);
                oraPrepStmt.setLong(2, compoundId);
                oraPrepStmt.setLong(3, sampleId);
                oraPrepStmt.setLong(4, saltCode);

                oraPrepStmt.addBatch();

                if (batchCounter > BATCH_FETCH_SIZE) {

                    totalBatchCounter += batchCounter;
                    cumulativeCounter += batchCounter;

                    if (totalBatchCounter > batchInsertSize) {
                        totalBatchCounter = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        System.out.println("batchFetchSize: " + BATCH_FETCH_SIZE + " cumulativeCounter: " + cumulativeCounter + " batchInsertSize: " + batchInsertSize + " in " + elapsedTime + " msec");
                    }
                    oraPrepStmt.executeBatch();
                    oraConn.commit();
                    batchCounter = 0;
                }
            }
            oraPrepStmt.executeBatch();
            oraConn.commit();

        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
            throw e;
        } finally {
            if (oraStmt != null) {
                try {
                    oraStmt.close();
                    oraStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraStmt");
                }
            }
            if (pgStmt != null) {
                try {
                    pgStmt.close();
                    pgStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing pgStmt");
                }
            }
            if (oraPrepStmt != null) {
                try {
                    oraPrepStmt.close();
                    oraPrepStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraPrepStmt");
                }
            }
        }
    }

    public static void populateRS3structure(Connection pgConn, Connection oraConn) throws Exception {

        Statement pgStmt = null;
        Statement oraStmt = null;
        PreparedStatement oraPrepStmt = null;
        ResultSet rs = null;

        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batchCounter = 0;
        int totalBatchCounter = 0;
        int cumulativeCounter = 0;
        int batchInsertSize = 10000;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = oraConn.createStatement();

            sqlString = "select structure_id from mwk_rs3_structure"; // where structure_id > 700000 and structure_id < 720000";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            rs = pgStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into mwk_rs3_structure(structure_id, registration_date, process_date, structure_class) values(?, ?, ?, ?)";
            oraPrepStmt = oraConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(oraPrepStmt.toString());

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;

                long structureId = rs.getLong("structure_id");

                oraPrepStmt.setLong(1, structureId);
                oraPrepStmt.setDate(2, getCurrentDate());
                oraPrepStmt.setDate(3, getCurrentDate());
                oraPrepStmt.setString(4, "fake");

                oraPrepStmt.addBatch();

                if (batchCounter > BATCH_FETCH_SIZE) {

                    totalBatchCounter += batchCounter;
                    cumulativeCounter += batchCounter;

                    if (totalBatchCounter > batchInsertSize) {
                        totalBatchCounter = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        System.out.println("batchFetchSize: " + BATCH_FETCH_SIZE + " cumulativeCounter: " + cumulativeCounter + " batchInsertSize: " + batchInsertSize + " in " + elapsedTime + " msec");
                    }
                    oraPrepStmt.executeBatch();
                    oraConn.commit();
                    batchCounter = 0;
                }
            }
            oraPrepStmt.executeBatch();
            oraConn.commit();

        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
            throw e;
        } finally {
            if (oraStmt != null) {
                try {
                    oraStmt.close();
                    oraStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraStmt");
                }
            }
            if (pgStmt != null) {
                try {
                    pgStmt.close();
                    pgStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing pgStmt");
                }
            }
            if (oraPrepStmt != null) {
                try {
                    oraPrepStmt.close();
                    oraPrepStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraPrepStmt");
                }
            }
        }
    }

    public static void populateRS3structureObject(Connection pgConn, Connection oraConn) throws Exception {

        Statement pgStmt = null;
        Statement oraStmt = null;
        PreparedStatement oraPrepStmt = null;
        ResultSet rs = null;

        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batchCounter = 0;
        int totalBatchCounter = 0;
        int cumulativeCounter = 0;
        int batchInsertSize = 10000;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = oraConn.createStatement();

            sqlString = "select structure_id, object_contents from mwk_rs3_structure_object"; // where structure_id > 700000 and structure_id < 720000";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            rs = pgStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into mwk_rs3_structure_object(structure_id, object_type, object_contents) values(?, ?, ?)";
            oraPrepStmt = oraConn.prepareStatement(prepStmtString);
            System.out.println(sqlString);
            System.out.println(oraPrepStmt.toString());

            startTime = System.currentTimeMillis();

            while (rs.next()) {
                batchCounter++;

                long structureId = rs.getLong("structure_id");
                String ctab = rs.getString("object_contents");

                InputStream input = new ByteArrayInputStream(ctab.getBytes());

                oraPrepStmt.setLong(1, structureId);
                oraPrepStmt.setString(2, "M");
                //oraPrepStmt.setBinaryStream(3, input);
                oraPrepStmt.setAsciiStream(3, input);

                oraPrepStmt.addBatch();

                if (batchCounter > BATCH_FETCH_SIZE) {

                    totalBatchCounter += batchCounter;
                    cumulativeCounter += batchCounter;

                    if (totalBatchCounter > batchInsertSize) {
                        totalBatchCounter = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        System.out.println("batchFetchSize: " + BATCH_FETCH_SIZE + " cumulativeCounter: " + cumulativeCounter + " batchInsertSize: " + batchInsertSize + " in " + elapsedTime + " msec");
                    }
                    oraPrepStmt.executeBatch();
                    oraConn.commit();
                    batchCounter = 0;
                }
            }
            oraPrepStmt.executeBatch();
            oraConn.commit();

        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            e.printStackTrace();
            throw e;
        } finally {
            if (oraStmt != null) {
                try {
                    oraStmt.close();
                    oraStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraStmt");
                }
            }
            if (pgStmt != null) {
                try {
                    pgStmt.close();
                    pgStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing pgStmt");
                }
            }
            if (oraPrepStmt != null) {
                try {
                    oraPrepStmt.close();
                    oraPrepStmt = null;
                } catch (SQLException ex) {
                    System.out.println("Error in closing oraPrepStmt");
                }
            }
        }
    }
}
