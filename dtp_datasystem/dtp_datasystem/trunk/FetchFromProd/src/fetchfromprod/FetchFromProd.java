package fetchfromprod;

import java.io.File;
import java.io.FileInputStream;
import java.io.StringWriter;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.iterator.IteratingSDFReader;

/**
 *
 * @author mwkunkel
 */
public class FetchFromProd {

    private static final Logger lgr = Logger.getLogger("GLOBAL");
    private static final int BATCH_FETCH_SIZE = 1000;
    private static final int BATCH_INSERT_SIZE = 1000;

    public static void main(String[] args) {

        Connection srcConn = null;
        Connection pgConn = null;

        try {

            System.out.println("Starting main.");

            System.out.println("Registering drivers.");
//            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            DriverManager.registerDriver(new org.postgresql.Driver());

            System.out.println("Opening srcConn");
            srcConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");
//            srcConn = DriverManager.getConnection("jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");

            System.out.println("Opening pgConn");
            pgConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");

//    MUST DISABLE AUTOCOMMIT for fetchForward cursor (batch fetches) to work
//    EACH ResultSet MUST also have fetchDirection set FETCH_FORWARD
            srcConn.setAutoCommit(false);
            pgConn.setAutoCommit(false);

//            System.out.println();
//            System.out.println("Starting: fetchRawCmpd");
//            fetchRawCmpd(pgConn, srcConn);

//            doSetupForSDFileParse(pgConn);//
//            doParse(pgConn, "/home/mwkunkel/Open_2D_Oct2014.sdf");
//
            NewParseAndFragment parser = new NewParseAndFragment();
            parser.processFragments(srcConn, pgConn);
// _ __ _   _ _ __  
//| '__| | | | '_ \ 
//| |  | |_| | | | |
//|_|   \__,_|_| |_|
//                  
//                   _                ____ ____  _  ___           _ _     _ 
//  __ _ _ __   __ _| |_   _ _______ / ___|  _ \| |/ / |__  _   _(_) | __| |
// / _` | '_ \ / _` | | | | |_  / _ \ |   | | | | ' /| '_ \| | | | | |/ _` |
//| (_| | | | | (_| | | |_| |/ /  __/ |___| |_| | . \| |_) | |_| | | | (_| |
// \__,_|_| |_|\__,_|_|\__, /___\___|\____|____/|_|\_\_.__/ \__,_|_|_|\__,_|
//                     |___/                                                
//           _   _         ____   ____ ____  ___ ____ _____ ____  
// ___  __ _| | (_)_ __   / ___| / ___|  _ \|_ _|  _ \_   _/ ___| 
/// __|/ _` | | | | '_ \  \___ \| |   | |_) || || |_) || | \___ \ 
//\__ \ (_| | | | | | | |  ___) | |___|  _ < | ||  __/ | |  ___) |
//|___/\__, |_| |_|_| |_| |____/ \____|_| \_\___|_|    |_| |____/ 
//        |_|                                                     
//
//            System.out.println();
//            System.out.println("Starting: fetchBioDataCounts");
//            fetchBioDataCounts(pgConn, srcConn);
//
//            System.out.println();
//            System.out.println("Starting: fetchMtxt");
//            fetchMtxt(pgConn, srcConn);
//
//            System.out.println();
//            System.out.println("Starting: fetchInventory");
//            fetchInventory(pgConn, srcConn);
//
//            System.out.println();
//            System.out.println("Starting: fetchChemNames");
//            fetchChemNames(pgConn, srcConn);
//
//            System.out.println();
//            System.out.println("Starting: fetchPubChemId");
//            fetchPubChemId(pgConn, srcConn);
//
//            System.out.println();
//            System.out.println("Starting: fetchProjects");
//            fetchProjects(pgConn, srcConn);
//
//            System.out.println();
//            System.out.println("Starting: fetchPlatedSets");
//            fetchPlatedSets(pgConn, srcConn);
//
//            System.out.println();
//            System.out.println("Starting: fetchProjects");
//            fetchProjects(pgConn, srcConn);
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
            if (pgConn != null) {
                System.out.println("Closing pgConn.");
                try {
                    pgConn.close();
                    pgConn = null;
                } catch (SQLException sqle) {
                    System.out.println("Error in closing pgConn " + sqle.getMessage());
                    Exception ex;
                    while ((ex = sqle.getNextException()) != null) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }
    }

    public static void fetchBioDataCounts(
            Connection pgConn,
            Connection srcConn) throws Exception {

        try {

            // these all fetch nsc and "the_count"
            // pairs of parameters: name of table, query to populate
            String[] paramArray = new String[]{
                "prod_count_hf",
                "select ed.nsc, count(*) the_count "
                + "from invivo.exp_decode ed, ops$oradis.dis_cmpd c "
                + "where ed.assay_type = 9 "
                + "and c.prefix = 'S' "
                + "and ed.prefix = c.prefix "
                + "and ed.nsc = c.nsc "
                + "group by ed.nsc",
                "prod_count_nci60",
                "select ctl.nsc, count(distinct ctl.expid) the_count "
                + "from c_tline ctl, ops$oradis.dis_cmpd c "
                + "where c.prefix = 'S' "
                + "and ctl.prefix = c.prefix "
                + "and ctl.nsc = c.nsc "
                + "group by ctl.nsc",
                "prod_count_xeno",
                "select ed.nsc, count(*) the_count "
                + "from invivo.exp_decode ed, ops$oradis.dis_cmpd c "
                + "where ed.assay_type in (3, 5) "
                + "and c.prefix = 'S' "
                + "and ed.prefix = c.prefix "
                + "and ed.nsc = c.nsc "
                + "group by ed.nsc"};

            String tableName = "";
            String fetchBioDataQueryString = "";

            for (int i = 0; i < paramArray.length; i++) {
                if (i % 2 == 0) {
                    tableName = paramArray[i];
                    fetchBioDataQueryString = paramArray[i + 1];
                    genericBioDataCount(pgConn, srcConn, tableName, fetchBioDataQueryString);
                }
            }

        } catch (Exception e) {
            System.out.println("Caught Exception " + e);
            throw new Exception("Exception in fetchBioDataCounts " + e);
        }
    }

    protected static void genericBioDataCount(
            Connection pgConn,
            Connection srcConn,
            String tableName,
            String fetchBioDataQueryString
    ) throws Exception {

        System.out.println("tableName is: " + tableName);
        System.out.println("fetchBioDataQueryString is: " + fetchBioDataQueryString);

        Statement oraStmt = null;
        Statement pgStmt = null;
        PreparedStatement pgPrepStmt = null;
        ResultSet resSet = null;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = srcConn.createStatement();

            String sqlString = "drop table if exists " + tableName;
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create table " + tableName + "(nsc int, the_count int)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            System.out.println(fetchBioDataQueryString);
            resSet = oraStmt.executeQuery(fetchBioDataQueryString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            String prepStmtString = "insert into " + tableName + "(nsc, the_count) values(?,?)";
            pgPrepStmt = pgConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                int nsc = resSet.getInt("nsc");
                int theCount = resSet.getInt("the_count");

                pgPrepStmt.setInt(1, nsc);
                pgPrepStmt.setInt(2, theCount);

                pgPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        lgr.info("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    pgPrepStmt.executeBatch();
                    pgConn.commit();
                    batCnt = 0;
                }
            }
            pgPrepStmt.executeBatch();
            pgConn.commit();

            sqlString = "drop index if exists " + tableName + "_nsc";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create index " + tableName + "_nsc on " + tableName + "(nsc)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            elapsedTime = System.currentTimeMillis() - startTime;
            System.out.println("-------------elapsedTime: " + elapsedTime);

        } catch (Exception e) {
            handleCatch(e, oraStmt, pgStmt, pgPrepStmt, resSet);
            throw new Exception("Exception in genericFetch " + e.getMessage());
        } finally {
            handleFinally(oraStmt, pgStmt, pgPrepStmt, resSet);
        }
    }

    protected static void consolidateBioDataCounts(
            Connection pgConn,
            Connection srcConn) throws Exception {

        Statement oraStmt = null;
        Statement pgStmt = null;
        PreparedStatement pgPrepStmt = null;
        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        // conslidate into a single prod_bio_counts table
        sqlString = "drop table if exists prod_bio_counts";

        System.out.println(sqlString);
        pgStmt.execute(sqlString);

        sqlString = "create table prod_bio_counts "
                + "as "
                + "select "
                + "prod_src.nsc, "
                + "prod_count_nci60.the_count as prod_count_nci60, "
                + "prod_count_hf.the_count as prod_count_hf, "
                + "prod_count_xeno.the_count as prod_count_xeno "
                + "from prod_src "
                + "left outer join prod_count_nci60 on prod_src.nsc = prod_count_nci60.nsc "
                + "left outer join prod_count_hf on prod_src.nsc = prod_count_hf.nsc "
                + "left outer join prod_count_xeno on prod_src.nsc = prod_count_xeno.nsc";

        System.out.println(sqlString);
        pgStmt.execute(sqlString);

    }

    public static void fetchMtxt(
            Connection pgConn,
            Connection srcConn) throws Exception {

        Statement oraStmt = null;
        Statement pgStmt = null;
        PreparedStatement pgPrepStmt = null;
        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = srcConn.createStatement();

            sqlString = "drop table if exists prod_mtxt";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create table prod_mtxt (nsc int, mtxt varchar(4096))";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "select cmpd.nsc, ct.text "
                    + "from ops$oradis.dis_cmpd cmpd, ops$oradis.cmpd_topic ct "
                    + "where cmpd.prefix = 'S' "
                    + "and cmpd.cmpd_id = ct.cmpd_id "
                    + "and ct.topic = 'MTXT'";
            System.out.println(sqlString);
            resSet = oraStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_mtxt(nsc, mtxt) values(?,?)";
            pgPrepStmt = pgConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                int nsc = resSet.getInt("nsc");
                String mtxt = resSet.getString("text");

                pgPrepStmt.setInt(1, nsc);
                pgPrepStmt.setString(2, mtxt);

                pgPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        lgr.info("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    pgPrepStmt.executeBatch();
                    pgConn.commit();
                    batCnt = 0;
                }
            }
            pgPrepStmt.executeBatch();
            pgConn.commit();

            sqlString = "drop index if exists prod_mtxt_nsc";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create index prod_mtxt_nsc on prod_mtxt(nsc)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

        } catch (Exception e) {
            handleCatch(e, oraStmt, pgStmt, pgPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(oraStmt, pgStmt, pgPrepStmt, resSet);
        }
    }

    public static void fetchInventory(
            Connection pgConn,
            Connection srcConn) throws Exception {

        Statement oraStmt = null;
        Statement pgStmt = null;
        PreparedStatement pgPrepStmt = null;
        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = srcConn.createStatement();

            sqlString = "drop table if exists prod_inventory";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create table prod_inventory (nsc int, inventory double precision)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "select nsc, mg as inventory "
                    + "from ops$oradis.dis_nscs "
                    + "where prefix = 'S'";
            System.out.println(sqlString);
            resSet = oraStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_inventory(nsc, inventory) values(?,?)";
            pgPrepStmt = pgConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                int nsc = resSet.getInt("nsc");
                Double inventory = resSet.getDouble("inventory");

                pgPrepStmt.setInt(1, nsc);
                pgPrepStmt.setDouble(2, inventory);

                pgPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        lgr.info("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    pgPrepStmt.executeBatch();
                    pgConn.commit();
                    batCnt = 0;
                }
            }
            pgPrepStmt.executeBatch();
            pgConn.commit();

            sqlString = "drop index if exists prod_inventory_nsc";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create index prod_inventory_nsc on prod_inventory(nsc)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

        } catch (Exception e) {
            handleCatch(e, oraStmt, pgStmt, pgPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(oraStmt, pgStmt, pgPrepStmt, resSet);
        }
    }

    public static void fetchChemNames(
            Connection pgConn,
            Connection srcConn) throws Exception {

        Statement oraStmt = null;
        Statement pgStmt = null;
        PreparedStatement pgPrepStmt = null;
        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = srcConn.createStatement();

            sqlString = "drop table if exists prod_chem_name";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create table prod_chem_name (cmpd_id bigint, nsc int, chem_name varchar, chem_name_type varchar)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "select cmpd.cmpd_id, cmpd.nsc, cn.name, cn.name_type "
                    + "from ops$oradis.dis_cmpd cmpd, ops$oradis.cmpd_chem_name cn "
                    + "where cmpd.prefix = 'S' "
                    + "and cmpd.cmpd_id = cn.cmpd_id";
            System.out.println(sqlString);
            resSet = oraStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_chem_name(cmpd_id, nsc, chem_name, chem_name_type) values(?,?,?,?)";
            pgPrepStmt = pgConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                long cmpdId = resSet.getLong("cmpd_id");
                int nsc = resSet.getInt("nsc");
                String chemName = resSet.getString("name");
                String chemNameType = resSet.getString("name_type");

                pgPrepStmt.setLong(1, cmpdId);
                pgPrepStmt.setInt(2, nsc);
                pgPrepStmt.setString(3, chemName);
                pgPrepStmt.setString(4, chemNameType);

                pgPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;
                        startTime = System.currentTimeMillis();
                        lgr.info("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    pgPrepStmt.executeBatch();
                    pgConn.commit();
                    batCnt = 0;
                }
            }
            pgPrepStmt.executeBatch();
            pgConn.commit();

            sqlString = "drop index if exists prod_chem_name_cmpd_id";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create index prod_chem_name_cmpd_id on prod_chem_name(cmpd_id)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "drop index if exists prod_chem_name_nsc";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create index prod_chem_name_nsc on prod_chem_name(nsc)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

        } catch (Exception e) {
            handleCatch(e, oraStmt, pgStmt, pgPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(oraStmt, pgStmt, pgPrepStmt, resSet);
        }
    }

    public static void fetchPubChemId(
            Connection pgConn,
            Connection srcConn) throws Exception {
    }

    public static void fetchRawCmpd(
            Connection pgConn,
            Connection srcConn) throws Exception {

        Statement oraStmt = null;
        Statement pgStmt = null;
        PreparedStatement pgPrepStmt = null;
        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;
        int totalBatchSize = 1000;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = srcConn.createStatement();

            sqlString = "drop table if exists prod_raw_cmpd";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create table prod_raw_cmpd ("
                    + "nsc integer, "
                    + "cas integer, "
                    + "conf varchar(32), "
                    + "distribution_code varchar(32), "
                    + "mf varchar, "
                    + "mw double precision,"
                    + "ctab varchar)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "select nsc, cas, conf, distribution_code, mf, mw, object_contents as ctab "
                    + "from ops$oradis.dis_cmpd, rs3_structure_object r "
                    + "where prefix = 'S' "
                    + "and nsc is not null "
                    + "and cmpd_id = structure_id "
                    + "and r.object_type = 'M'";
            System.out.println(sqlString);
            resSet = oraStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_raw_cmpd(nsc, cas, conf, distribution_code, mf, mw, ctab) values(?,?,?,?,?,?,?)";
            pgPrepStmt = pgConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                pgPrepStmt.setInt(1, resSet.getInt("nsc"));
                pgPrepStmt.setInt(2, resSet.getInt("cas"));
                pgPrepStmt.setString(3, resSet.getString("conf"));
                pgPrepStmt.setString(4, resSet.getString("distribution_code"));
                pgPrepStmt.setString(5, resSet.getString("mf"));
                pgPrepStmt.setDouble(6, resSet.getDouble("mw"));

                byte[] blobbytes = resSet.getBytes("ctab");
                String text = new String(blobbytes);

                pgPrepStmt.setString(7, text);

                pgPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > totalBatchSize) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        System.out.println("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + totalBatchSize + " in " + elapsedTime + " msec");
                        lgr.info("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + totalBatchSize + " in " + elapsedTime + " msec");
                    }
                    pgPrepStmt.executeBatch();
                    pgConn.commit();
                    batCnt = 0;
                }
            }
            pgPrepStmt.executeBatch();
            pgConn.commit();

            sqlString = "drop index if exists prod_raw_cmpd_nsc";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create index prod_raw_cmpd_nsc on prod_raw_cmpd(nsc)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

        } catch (Exception e) {
            handleCatch(e, oraStmt, pgStmt, pgPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(oraStmt, pgStmt, pgPrepStmt, resSet);
        }
    }

    public static void fetchProjects(
            Connection pgConn,
            Connection srcConn) throws Exception {

        Statement oraStmt = null;
        Statement pgStmt = null;
        PreparedStatement pgPrepStmt = null;
        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = srcConn.createStatement();

            sqlString = "drop table if exists prod_projects";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create table prod_projects (nsc int, project_code varchar, description varchar)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "select cp.nsc, cp.projectcode, lu.description "
                    + "from ops$oradis.chem_project cp, ops$oradis.lookup_project lu "
                    + "where cp.projectcode = lu.projectcode";
            System.out.println(sqlString);
            resSet = oraStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_projects(nsc, project_code, description) values(?,?,?)";
            pgPrepStmt = pgConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                int nsc = resSet.getInt("nsc");
                String projectCode = resSet.getString("projectcode");
                String projectType = resSet.getString("description");

                pgPrepStmt.setInt(1, nsc);
                pgPrepStmt.setString(2, projectCode);
                pgPrepStmt.setString(3, projectType);

                pgPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        lgr.info("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    pgPrepStmt.executeBatch();
                    pgConn.commit();
                    batCnt = 0;
                }
            }
            pgPrepStmt.executeBatch();
            pgConn.commit();

            sqlString = "drop index if exists prod_projects_nsc";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create index prod_projects_nsc on prod_projects(nsc)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "drop index if exists prod_projects_project_code";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create index prod_projects_project_code on prod_projects(project_code)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

        } catch (Exception e) {
            handleCatch(e, oraStmt, pgStmt, pgPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(oraStmt, pgStmt, pgPrepStmt, resSet);
        }
    }

    public static void fetchPlatedSets(
            Connection pgConn,
            Connection srcConn) throws Exception {

// have to add distinct filter since DIS tracks different interations of same plate
        Statement oraStmt = null;
        Statement pgStmt = null;
        PreparedStatement pgPrepStmt = null;
        ResultSet resSet = null;
        String sqlString;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;

        try {

            pgStmt = pgConn.createStatement();
            oraStmt = srcConn.createStatement();

            sqlString = "drop table if exists prod_plated_sets";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create table prod_plated_sets (nsc int, plate_set varchar)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "select distinct nsc, plateset from ops$oradis.oradis_dis_well";
            System.out.println(sqlString);
            resSet = oraStmt.executeQuery(sqlString);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);

            prepStmtString = "insert into prod_plated_sets(nsc, plate_set) values(?,?)";
            pgPrepStmt = pgConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            while (resSet.next()) {

                batCnt++;

                int nsc = resSet.getInt("nsc");
                String plateSet = resSet.getString("plateset");

                pgPrepStmt.setInt(1, nsc);
                pgPrepStmt.setString(2, plateSet);

                pgPrepStmt.addBatch();

                if (batCnt > BATCH_FETCH_SIZE) {
                    totBatCnt += batCnt;
                    cumCnt += batCnt;
                    if (totBatCnt > BATCH_INSERT_SIZE) {
                        totBatCnt = 0;
                        elapsedTime = System.currentTimeMillis() - startTime;

                        startTime = System.currentTimeMillis();
                        lgr.info("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + BATCH_INSERT_SIZE + " in " + elapsedTime + " msec");
                    }
                    pgPrepStmt.executeBatch();
                    pgConn.commit();
                    batCnt = 0;
                }
            }
            pgPrepStmt.executeBatch();
            pgConn.commit();

            sqlString = "drop index if exists prod_plated_sets_nsc";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create index prod_plated_sets_nsc on prod_plated_sets(nsc)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

        } catch (Exception e) {
            handleCatch(e, oraStmt, pgStmt, pgPrepStmt, resSet);
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(oraStmt, pgStmt, pgPrepStmt, resSet);
        }
    }

    public static void doParse(Connection pgConn, String fullFilePath) {

        PreparedStatement pgPrepStmt = null;

        Integer nsc = -10101;
        String cas = "no cas";
        String prod_mf = "no prod_mf";
        Double prod_mw = -10101d;
        String ctab = "no ctab";

        try {

            String prepStmtString = "insert into parsed_from_sdfile(nsc, cas, prod_mf, prod_mw, ctab) values( ?,?,?,?,?)";
            pgPrepStmt = pgConn.prepareStatement(prepStmtString);

            File sdfFile = new File(fullFilePath);

            IteratingSDFReader reader = new IteratingSDFReader(new FileInputStream(sdfFile), DefaultChemObjectBuilder.getInstance());

            StringWriter sw = new StringWriter();
            MDLV2000Writer writer = new MDLV2000Writer(sw);

            int molCnt = 0;

            while (reader.hasNext()) {

                molCnt++;

                IAtomContainer molecule = (IAtomContainer) reader.next();

//                Map<Object, Object> propMap = molecule.getProperties();
//                for (Object key : propMap.keySet()){
//                    System.out.println("propMap key: " + key);
//                }
                nsc = -10101;
                try {
                    nsc = Integer.parseInt(molecule.getProperty("NSC").toString());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                cas = "no cas";
                if (molecule.getProperty("CAS") != null) {
                    cas = molecule.getProperty("CAS");
                }

                prod_mf = "no prod_mf";
                if (molecule.getProperty("Molecular Formula in DIS") != null) {
                    prod_mf = molecule.getProperty("Molecular Formula in DIS");
                }

                prod_mw = -10101d;
                if (molecule.getProperty("Molecular Weight (nearest integer)") != null) {
                    try {
                        prod_mw = Double.parseDouble(molecule.getProperty("Molecular Weight (nearest integer)").toString());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                sw.getBuffer().setLength(0);
                writer.writeMolecule(molecule);
                ctab = sw.toString();

//                System.out.println("NSC: " + nsc);
//                System.out.println("CAS: " + cas);
//                System.out.println("Molecular Formula in DIS: " + prod_mf);
//                System.out.println("Molecular Weight (nearest integer): " + prod_mw);
//                System.out.println("ctab: ");
//                System.out.println(ctab);
                System.out.println("molCnt: " + molCnt + " NSC: " + nsc);
//                System.out.println(molCnt % BATCH_INSERT_SIZE);
                pgPrepStmt.setInt(1, nsc);
                pgPrepStmt.setString(2, cas);
                pgPrepStmt.setString(3, prod_mf);
                pgPrepStmt.setDouble(4, prod_mw);
                pgPrepStmt.setString(5, ctab);

                pgPrepStmt.addBatch();

                if (molCnt % BATCH_INSERT_SIZE == 0) {
                    System.out.println("---------------------------Executing batch insert.");
                    pgPrepStmt.executeBatch();
                    pgConn.commit();
                }

            }

            // stragglers
            pgPrepStmt.executeBatch();
            pgConn.commit();

        } catch (Exception e) {
            System.out.println("Outer exception in doParse.  NSC: " + nsc);
            e.printStackTrace();
        }

    }

    public static void doSetupForSDFileParse(Connection pgConn) {

        Statement pgStmt = null;

        try {

            pgStmt = pgConn.createStatement();

            String sqlString = "drop table if exists parsed_from_sdfile";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create table parsed_from_sdfile(nsc int, cas varchar, prod_mf varchar, prod_mw double precision, ctab varchar)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (pgStmt != null) {
                //System.out.println("---------Closing pgStmt.");
                try {
                    pgStmt.close();
                    pgStmt = null;
                } catch (SQLException sqle) {
                    System.out.println("---------Error in closing pgStmt " + sqle.getMessage());
                    Exception ex;
                    while ((ex = sqle.getNextException()) != null) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }

    }

    public static void handleCatch(
            Exception e,
            Statement oraStmt,
            Statement pgStmt,
            PreparedStatement pgPrepStmt,
            ResultSet resSet) {

        System.out.println("Starting handleCatch.");

        if (e instanceof SQLException) {
            System.out.println("---------Exception is SQLException.");
            SQLException sqle = (SQLException) e;
            System.out.println("---------" + sqle.getMessage());
            Exception ex;
            while ((ex = sqle.getNextException()) != null) {
                System.out.println("---------getNextException: " + ex);
            }
        }
        if (e instanceof BatchUpdateException) {
            System.out.println("---------Exception is BatchUpdateException.");
            BatchUpdateException bue = (BatchUpdateException) e;
            System.out.print("---------" + bue);
            Exception ex;
            while ((ex = bue.getNextException()) != null) {
                System.out.println("---------getNextException: " + ex);
            }
        }
        if (oraStmt != null) {
            System.out.println("---------Closing oraStmt.");
            try {
                if (oraStmt != null) {
                    oraStmt.close();
                }
                oraStmt = null;
            } catch (SQLException sqle) {
                System.out.println("---------Error in closing oraStmt " + sqle.getMessage());
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        if (pgStmt != null) {
            System.out.println("---------Closing pgStmt.");
            try {
                if (pgStmt != null) {
                    pgStmt.close();
                }
                pgStmt = null;
            } catch (SQLException sqle) {
                System.out.println("---------Error in closing pgStmt " + sqle.getMessage());
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        if (pgPrepStmt != null) {
            System.out.println("---------Closing pgPrepStmt.");
            try {
                if (pgPrepStmt != null) {
                    pgPrepStmt.close();
                }
                pgPrepStmt = null;
            } catch (SQLException sqle) {
                System.out.println("---------Error in closing pgPrepStmt " + sqle.getMessage());
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        if (resSet != null) {
            System.out.println("---------Closing resSet.");
            try {
                if (resSet != null) {
                    resSet.close();
                }
                resSet = null;
            } catch (SQLException sqle) {
                System.out.println("---------Error in closing rs");
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        System.out.println("Leaving handleCatch.");

    }

    public static void handleFinally(
            Statement oraStmt,
            Statement pgStmt,
            PreparedStatement pgPrepStmt,
            ResultSet resSet) {

        //System.out.println("Starting handleFinally.");
        if (oraStmt != null) {
            //System.out.println("---------Closing oraStmt.");
            try {
                oraStmt.close();
                oraStmt = null;
            } catch (SQLException sqle) {
                System.out.println("---------Error in closing oraStmt " + sqle.getMessage());
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        if (pgStmt != null) {
            //System.out.println("---------Closing pgStmt.");
            try {
                pgStmt.close();
                pgStmt = null;
            } catch (SQLException sqle) {
                System.out.println("---------Error in closing pgStmt " + sqle.getMessage());
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        if (pgPrepStmt != null) {
            //System.out.println("---------Closing pgPrepStmt.");
            try {
                pgPrepStmt.close();
                pgPrepStmt = null;
            } catch (SQLException sqle) {
                System.out.println("---------Error in closing pgPrepStmt " + sqle.getMessage());
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        if (resSet != null) {
            //System.out.println("---------Closing resSet.");
            try {
                resSet.close();
                resSet = null;
            } catch (SQLException sqle) {
                System.out.println("---------Error in closing rs " + sqle.getMessage());
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        System.out.println("Leaving handleFinally.");

    }

}
