/*
 
 
 
 */
package fetchfromchembl;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import org.apache.log4j.Logger;

/**
 *
 * @author mwkunkel
 */
public class FetchFromChembl {

    private static final Logger lgr = Logger.getLogger("GLOBAL");

    private static final int BATCH_FETCH_SIZE = 10000;
    private static final int BATCH_INSERT_SIZE = 10000;

    public static void main(String[] args) {

        Connection oraConn = null;
        Connection pgConn = null;

        try {

            System.out.println("Starting main in FetchFromChembl.");

            System.out.println("Registering drivers.");

            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            DriverManager.registerDriver(new org.postgresql.Driver());

            System.out.println("Opening oraConn");
            oraConn = DriverManager.getConnection("jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");

            System.out.println("Opening pgConn");
            pgConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");

//    MUST DISABLE AUTOCOMMIT for fetchForward cursor (batch fetches) to work
//    EACH ResultSet MUST also have fetchDirection set FETCH_FORWARD
            oraConn.setAutoCommit(false);
            pgConn.setAutoCommit(false);

            System.out.println("Starting: fetchProjects");
            fetchMyChembl(pgConn, oraConn);

        } catch (Exception e) {
            System.out.println("Caught Exception in main: " + e);
            e.printStackTrace();
        } finally {
            if (oraConn != null) {
                System.out.println("Closing oraConn.");
                try {
                    oraConn.close();
                    oraConn = null;
                } catch (SQLException sqle) {
                    System.out.println("Error in closing oraConn " + sqle.getMessage());
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

    public static void fetchMyChembl(
            Connection pgConn,
            Connection oraConn) throws Exception {

        Statement oraStmt = null;
        Statement pgStmt = null;
        PreparedStatement pgPrepStmt = null;
        ResultSet resSet = null;
        String sql;
        String prepStmtString;

        long startTime = 0;
        long elapsedTime = 0;
        int batCnt = 0;
        int totBatCnt = 0;
        int cumCnt = 0;
        int totalBatchSize = BATCH_FETCH_SIZE - 1;

        try {

            pgStmt = pgConn.createStatement();

            oraStmt = oraConn.createStatement();
            oraStmt.setFetchDirection(ResultSet.FETCH_FORWARD);
            oraStmt.setFetchSize(BATCH_FETCH_SIZE);

//            sql = "drop table if exists my_chembl21";
//            System.out.println(sql);
//            pgStmt.execute(sql);
//
//            sql = "create table my_chembl21 ("
//                    + " chembl_id varchar, "
//                    + " can_smi varchar, "
//                    + " can_taut varchar, "
//                    + " can_taut_strip_stereo varchar, "
//                    + " inchi varchar,"
//                    + " inchikey varchar)";
//            System.out.println(sql);
//            pgStmt.execute(sql);
//
//            sql = "select chembl_id, can_smi, can_taut, can_taut_strip_stereo, inchi, inchikey from ops$kunkel.my_chembl21";
//            System.out.println(sql);
//
//            resSet = oraStmt.executeQuery(sql);
//            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);
//            resSet.setFetchSize(BATCH_FETCH_SIZE);
//
//            prepStmtString = "insert into my_chembl21(chembl_id, can_smi, can_taut, can_taut_strip_stereo, inchi, inchikey) values(?,?,?,?,?,?)";
//            pgPrepStmt = pgConn.prepareStatement(prepStmtString);
//
//            startTime = System.currentTimeMillis();
//
//            while (resSet.next()) {
//
//                batCnt++;
//
//                pgPrepStmt.setString(1, resSet.getString("chembl_id"));
//                pgPrepStmt.setString(2, resSet.getString("can_smi"));
//                pgPrepStmt.setString(3, resSet.getString("can_taut"));
//                pgPrepStmt.setString(4, resSet.getString("can_taut_strip_stereo"));
//                pgPrepStmt.setString(5, resSet.getString("inchi"));
//                pgPrepStmt.setString(6, resSet.getString("inchikey"));
//
//                pgPrepStmt.addBatch();
//
//                if (batCnt > BATCH_FETCH_SIZE) {
//                    totBatCnt += batCnt;
//                    cumCnt += batCnt;
//
//                    if (totBatCnt > totalBatchSize) {
//                        totBatCnt = 0;
//                        elapsedTime = System.currentTimeMillis() - startTime;
//
//                        startTime = System.currentTimeMillis();
//                        lgr.info("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + totalBatchSize + " in " + elapsedTime + " msec");
//                        System.out.println("batchSize: " + BATCH_FETCH_SIZE + " cumCnt: " + cumCnt + " batchSize: " + totalBatchSize + " in " + elapsedTime + " msec");
//                    }
//                    pgPrepStmt.executeBatch();
//                    pgConn.commit();
//                    batCnt = 0;
//                }
//            }
//            pgPrepStmt.executeBatch();
//            pgConn.commit();

            String[] idxCols = new String[]{
                "chembl_id",
                "can_smi",
                "can_taut",
                "can_taut_strip_stereo"
            };

            ArrayList<String> idxColList = new ArrayList<String>(Arrays.asList(idxCols));

            ArrayList<String> sqlList = new ArrayList<String>();

            for (String colName : idxColList) {
                sqlList.add("drop index if exists my_chembl21_" + colName + "_idx");
                sqlList.add("create index my_chembl21_" + colName + "_idx on my_chembl21(" + colName + ")");
            }

            for (String s : sqlList) {
                System.out.println(s);
                pgStmt.execute(s);
            }

        } catch (Exception e) {
            handleCatch(e, oraStmt, pgStmt, pgPrepStmt, resSet);
            e.printStackTrace();
            throw new Exception("Caught and handled Exception.");
        } finally {
            handleFinally(oraStmt, pgStmt, pgPrepStmt, resSet);
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
