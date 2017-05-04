/*
 
 
 
 */
package main;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author mwkunkel
 */
public class Main {

    private static final Boolean DEBUG = Boolean.TRUE;
    private static final Boolean TESTING = Boolean.FALSE;
    private static final int BATCH_FETCH_SIZE = 10000;
    private static final int BATCH_INSERT_SIZE = 10000;

    public static void main(String[] args) throws ClassNotFoundException, SQLException {

        Connection srcConn = null;
        Connection targetConn = null;

        try {

            System.out.println("Starting main.");

            System.out.println("--------Registering drivers.");
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            DriverManager.registerDriver(new org.postgresql.Driver());

            System.out.println("--------Opening srcConn");
            // srcConn = DriverManager.getConnection("jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");
            srcConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");

            System.out.println("--------Opening targetConn");
            // targetConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");
            targetConn = DriverManager.getConnection("jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");

//    MUST DISABLE AUTOCOMMIT for fetchForward cursor (batch fetches) to work
//    EACH ResultSet MUST also have fetchDirection set FETCH_FORWARD
            srcConn.setAutoCommit(false);
            targetConn.setAutoCommit(false);

//--------------------------------------------------------------------
//--------------------------------------------------------------------
//--------------------------------------------------------------------
//--------------------------------------------------------------------
//            loadFragPChemToOracle(srcConn, targetConn);

//--------------------------------------------------------------------
//--------------------------------------------------------------------
//--------------------------------------------------------------------
//--------------------------------------------------------------------
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
            if (targetConn != null) {
                System.out.println("Closing targetConn.");
                try {
                    targetConn.close();
                    targetConn = null;
                } catch (SQLException sqle) {
                    System.out.println("Error in closing targetConn " + sqle.getMessage());
                    Exception ex;
                    while ((ex = sqle.getNextException()) != null) {
                        System.out.println(ex.getMessage());
                    }
                }
            }
        }

    }

    public static void loadFragPChemToOracle(
            Connection srcConn,
            Connection targetConn) throws Exception {

        // CONTENT STARTS HERE
        System.out.println("--------Loading table names and select statements.");

        ArrayList<StringPair> theList = new ArrayList<StringPair>();

        StringBuilder sb = new StringBuilder();
        sb.append(" select * from cmpd_fragment_p_chem ");
        if (TESTING) {
            sb.append(" limit 10 ");
        }
        theList.add(new StringPair("cmpd_fragment_p_chem", sb.toString()));

        sb = new StringBuilder();
        sb.append(" select * from cmpd_fragment_structure ");
        if (TESTING) {
            sb.append(" limit 10 ");
        }
        theList.add(new StringPair("cmpd_fragment_structure", sb.toString()));

        sb = new StringBuilder();
        sb.append(" select * from cmpd_fragment ");
        if (TESTING) {
            sb.append(" limit 10 ");
        }
        theList.add(new StringPair("cmpd_fragment", sb.toString()));

        sb = new StringBuilder();
        sb.append(" select * from nsc_cmpd ");
        if (TESTING) {
            sb.append(" limit 10 ");
        }
        theList.add(new StringPair("nsc_cmpd", sb.toString()));

        sb = new StringBuilder();
        sb.append(" select * from qc_with_nsc ");
        if (TESTING) {
            sb.append(" limit 10 ");
        }
        theList.add(new StringPair("qc_with_nsc", sb.toString()));

        
//        sb.append(" select ");
//        sb.append(" nsc, ");
//        sb.append(" molecular_formula, ");
//        sb.append(" molecular_weight, ");
//        sb.append(" num_atoms, ");
//        sb.append(" num_bonds, ");
//        sb.append(" num_hydrogens, ");
//        sb.append(" num_positiveatoms, ");
//        sb.append(" num_negativeatoms, ");
//        sb.append(" num_ringbonds, ");
//        sb.append(" num_rotatablebonds, ");
//        sb.append(" num_aromaticbonds, ");
//        sb.append(" num_rings, ");
//        sb.append(" num_aromaticrings, ");
//        sb.append(" num_ringassemblies, ");
//        sb.append(" num_metalatoms, ");
//        sb.append(" num_stereoatoms, ");
//        sb.append(" num_stereobonds, ");
//        sb.append(" num_singlebonds, ");
//        sb.append(" num_doublebonds, ");
//        sb.append(" num_triplebonds, ");
//        sb.append(" formalcharge, ");
//        sb.append(" alogp, ");
//        sb.append(" logd, ");
//        sb.append(" molecular_solubility, ");
//        sb.append(" molecular_surfacearea, ");
//        sb.append(" num_h_acceptors, ");
//        sb.append(" num_h_donors, ");
//        sb.append(" badvalenceatoms ");
//        sb.append(" from rs3_from_plp_frags ");
//        sb.append(" where nsc in (select nsc from nsc_for_export) ");
//        sb.append(" and salt_id is null ");
//        if (TESTING) {
//            sb.append(" limit 10 ");
//        }
//        theList.add(new StringPair("pchem", sb.toString()));
//        sb = new StringBuilder();
//
//        sb.append(" select ");
//        sb.append(" nsc, ");
//        sb.append(" smiles, ");
//        sb.append(" can_smi, ");
//        sb.append(" can_taut, ");
//        sb.append(" can_taut_strip_stereo, ");
//        sb.append(" inchi, ");
//        sb.append(" inchi_auxinfo, ");
//        sb.append(" molecular_formula, ");
//        sb.append(" molecular_weight, ");
//        sb.append(" coorddimension, ");
//        sb.append(" ischiral, ");
//        sb.append(" canonicaltautomer, ");
//        sb.append(" numberoftautomers ");
//        sb.append(" from rs3_from_plp_frags ");
//        if (TESTING) {
//            sb.append(" limit 10 ");
//        }
//
//        theList.add(new StringPair("strc", sb.toString()));
//
//        // testing reverse fetches (from cmpd_fragment tables BACK to the original plp names
//        sb = new StringBuilder();
//
//        sb.append(" select ");
//        // sb.append(" nsc, ");
//        sb.append(" molecular_formula, ");
//        sb.append(" molecular_weight, ");
//        sb.append(" count_atoms as num_atoms, ");
//        sb.append(" count_bonds as num_bonds, ");
//        sb.append(" count_hydrogen_atoms as num_hydrogens, ");
//        sb.append(" count_positive_atoms as num_positiveatoms, ");
//        sb.append(" count_negative_atoms as num_negativeatoms, ");
//        sb.append(" count_ring_bonds as num_ringbonds, ");
//        sb.append(" count_rotatable_bonds as num_rotatablebonds, ");
//        sb.append(" count_aromatic_bonds as num_aromaticbonds, ");
//        sb.append(" count_rings as num_rings, ");
//        sb.append(" count_aromatic_rings as num_aromaticrings, ");
//        sb.append(" count_ring_assemblies as num_ringassemblies, ");
//        sb.append(" count_metal_atoms as num_metalatoms, ");
//        sb.append(" count_stereo_atoms as num_stereoatoms, ");
//        sb.append(" count_stereo_bonds as num_stereobonds, ");
//        sb.append(" count_single_bonds as num_singlebonds, ");
//        sb.append(" count_double_bonds as num_doublebonds, ");
//        sb.append(" count_triple_bonds as num_triplebonds, ");
//        sb.append(" formal_charge as formalcharge, ");
//        sb.append(" the_a_log_p as alogp, ");
//        sb.append(" log_d as logd, ");
//        sb.append(" solubility as molecular_solubility, ");
//        sb.append(" surface_area as molecular_surfacearea, ");
//        sb.append(" count_hyd_bond_acceptors as num_h_acceptors, ");
//        sb.append(" count_hyd_bond_donors as num_h_donors, ");
//        sb.append(" badvalenceatoms ");
//        sb.append(" from cmpd_fragment_p_chem ");
//        if (TESTING) {
//            sb.append(" limit 10 ");
//        }
//
//        theList.add(new StringPair("reverse_pchem", sb.toString()));
        System.out.println("--------Cycling through the queries.");
        System.out.println();

        for (StringPair sp : theList) {
            testMeta(srcConn, targetConn, sp.string1, sp.string2);
        }

    }

    public static void testMeta(
            Connection srcConn,
            Connection targetConn,
            String targetTableName,
            String selectQuery) throws Exception {

        if (DEBUG) {
            System.out.println(selectQuery);
        }

        Statement srcStmt = null;
        Statement targetStmt = null;
        PreparedStatement targetPrepStmt = null;
        ResultSet resSet = null;

        long batStartTime = 0;
        long batElapTime = 0;
        long cumBatTime = 0;

        long methodStartTime = 0;
        long methodElapTime = 0;

        int batCnt = 0;
        int cumCnt = 0;

        System.out.println("Starting testMeta for table: " + targetTableName);
        System.out.println();
        System.out.printf("%24s%24s%24s%n", "targetTableName", "BATCH_FETCH_SIZE", "BATCH_INSERT_SIZE");
        System.out.printf("%24s%24s%24s%n", targetTableName, BATCH_FETCH_SIZE, BATCH_INSERT_SIZE);
        System.out.println();
        System.out.printf("%24s%24s%24s%n", "targetTableName", "ACTION", "methodElapTime");
        System.out.printf("%24s%24s%24s%n", targetTableName, "Starting overallTimer", "");

        methodStartTime = System.currentTimeMillis();

        try {

            methodElapTime = System.currentTimeMillis() - methodStartTime;
            if (DEBUG) {
                System.out.println(selectQuery);
            }
            System.out.printf("%24s%24s%24s%n", targetTableName, "Running select query.", methodElapTime);

            srcStmt = srcConn.createStatement();
            targetStmt = targetConn.createStatement();

            resSet = srcStmt.executeQuery(selectQuery);
            resSet.setFetchDirection(ResultSet.FETCH_FORWARD);
            resSet.setFetchSize(BATCH_FETCH_SIZE);

            StringBuilder createStatementBuilder = new StringBuilder();
            StringBuilder preparedStatementBuilder = new StringBuilder();

            methodElapTime = System.currentTimeMillis() - methodStartTime;
            System.out.printf("%24s%24s%24s%n", targetTableName, "Creating DDL, prepStmt.", methodElapTime);

            createStatementBuilder.append("create table " + targetTableName + "(\n");
            preparedStatementBuilder.append("insert into " + targetTableName + "(\n");

            if (resSet != null) {

                ResultSetMetaData rsmd = resSet.getMetaData();
                for (int i = 0; i < rsmd.getColumnCount(); i++) {

                    if (DEBUG) {
                        System.out.println("name: " + rsmd.getColumnName(i + 1)
                                + " type: " + rsmd.getColumnType(i + 1)
                                + " typeName: " + rsmd.getColumnTypeName(i + 1)
                                + " precision: " + rsmd.getPrecision(i + 1)
                                + " scale: " + rsmd.getScale(i + 1)
                        );
                    }

                    //createStatementBuilder.append(rsmd.getColumnName(i + 1) + " " + toPgDataType(rsmd.getColumnTypeName(i + 1)));
                    createStatementBuilder.append(rsmd.getColumnName(i + 1) + " " + pgToOra(rsmd.getColumnTypeName(i + 1)));
                    preparedStatementBuilder.append(rsmd.getColumnName(i + 1));

                    // handle commas between fields
                    if (i != rsmd.getColumnCount() - 1) {
                        createStatementBuilder.append(",\n");
                        preparedStatementBuilder.append(",\n");
                    } else {
                        createStatementBuilder.append(")\n");
                        preparedStatementBuilder.append(") values (");
                    }
                }

                // add ? placeholders for the values
                for (int i = 0; i < rsmd.getColumnCount(); i++) {
                    preparedStatementBuilder.append("?");
                    if (i != rsmd.getColumnCount() - 1) {
                        preparedStatementBuilder.append(",");
                    } else {
                        preparedStatementBuilder.append(")");
                    }

                }

                // String dropTableString = "drop table if exists " + targetTableName;
                String dropTableString = "drop table " + targetTableName;
                String createTableString = createStatementBuilder.toString();
                String prepStmtString = preparedStatementBuilder.toString();

                if (DEBUG) {
                    System.out.println(dropTableString + "\n\n");
                    System.out.println(createTableString + "\n\n");
                    System.out.println(prepStmtString + "\n\n");
                }

                // drop and create the target table, prepare for inserts
                methodElapTime = System.currentTimeMillis() - methodStartTime;
                System.out.printf("%24s%24s%24s%n", targetTableName, "Droping table.", methodElapTime);
                targetStmt.execute(dropTableString);
                methodElapTime = System.currentTimeMillis() - methodStartTime;
                System.out.printf("%24s%24s%24s%n", targetTableName, "Creating table.", methodElapTime);
                targetStmt.execute(createTableString);
                methodElapTime = System.currentTimeMillis() - methodStartTime;
                System.out.printf("%24s%24s%24s%n", targetTableName, "Preparing for inserts.", methodElapTime);
                targetPrepStmt = targetConn.prepareStatement(prepStmtString);
                methodElapTime = System.currentTimeMillis() - methodStartTime;
                System.out.printf("%24s%24s%24s%n", targetTableName, "Processing resultsSet.", methodElapTime);
                System.out.println();

                System.out.printf("%16s%16s%16s%16s%16s%n", "cumCnt", "batCnt", "batElapTime", "cumBatTime", "methodElapTime");

                // have to set this outside the loop in case there are NO results...
                batStartTime = System.currentTimeMillis();

                while (resSet.next()) {

                    batCnt++;
                    cumCnt++;

                    for (int i = 0; i < rsmd.getColumnCount(); i++) {
                        targetPrepStmt.setObject(i + 1, resSet.getObject(i + 1));
                    }

                    targetPrepStmt.addBatch();

                    batStartTime = System.currentTimeMillis();

                    if (batCnt >= BATCH_INSERT_SIZE) {
                        targetPrepStmt.executeBatch();
                        targetConn.commit();
                        batElapTime = System.currentTimeMillis() - batStartTime;
                        cumBatTime += batElapTime;
                        methodElapTime = System.currentTimeMillis() - methodStartTime;
                        System.out.printf("%16s%16s%16s%16s%16s%n", cumCnt, batCnt, batElapTime, cumBatTime, methodElapTime);
                        // reset batch
                        batStartTime = System.currentTimeMillis();
                        batCnt = 0;
                    }

                }

                // catch any stragglers
                targetPrepStmt.executeBatch();
                targetConn.commit();
                batElapTime = System.currentTimeMillis() - batStartTime;
                cumBatTime += batElapTime;
                methodElapTime = System.currentTimeMillis() - methodStartTime;
                System.out.printf("%16s%16s%16s%16s%16s%n", "stragglers: " + cumCnt, batCnt, batElapTime, cumBatTime, methodElapTime);
                System.out.println();

            }

            System.out.println("Finishing testMeta for table: " + targetTableName);
            System.out.println();
            methodElapTime = System.currentTimeMillis() - methodStartTime;
            System.out.printf("%24s%24s%24s%n", "FINAL: cumCnt", "FINAL: cumBatTime", "FINAL: methodElapTime");
            System.out.printf("%24s%24s%24s%n", cumCnt, cumBatTime, methodElapTime);
            System.out.println();
            System.out.println();

        } catch (Exception e) {
            handleCatch(e, srcStmt, targetStmt, targetPrepStmt, resSet);
            throw e;
        } finally {
            handleFinally(srcStmt, targetStmt, targetPrepStmt, resSet);
        }
    }

    public static String pgToOra(String dataTypeIn) {

        String rtn = "";

        switch (dataTypeIn) {

            // pg data types
            case "int4":
                rtn = "int";
                break;
            case "int8":
                rtn = "int";
                break;
            case "varchar":
                rtn = "varchar2(1024)";
                break;
            case "float8":
                rtn = "float";
                break;
            case "bool":
                rtn = "boolean";
                break;
            case "text":
                rtn = "clob";
                break;
            default:
                System.out.println("Unrecognized dataType: " + dataTypeIn);
        }

        return rtn;

    }

    public static String oraToPg(String dataTypeIn) {

        String rtn = "";

        switch (dataTypeIn) {

            case "CHAR":
                rtn = "varchar";
                break;

            case "NUMBER":
                rtn = "int";
                break;

            case "VARCHAR2":
                rtn = "varchar";
                break;

            default:
                System.out.println("Unrecognized dataType: " + dataTypeIn);
        }

        return rtn;

    }

    public static void handleCatch(
            Exception e,
            Statement srcStmt,
            Statement targetStmt,
            PreparedStatement pgPrepStmt,
            ResultSet resSet) {

        System.out.println("Starting handleCatch.");

        if (e instanceof SQLException) {
            System.out.println("---------Exception is SQLException.");
            SQLException sqle = (SQLException) e;
            System.out.println("SQLException.getMessage(): " + sqle.getMessage());
            Exception ex;
            while ((ex = sqle.getNextException()) != null) {
                System.out.println("SQLException.getNextException: " + ex);
            }
        }
        if (e instanceof BatchUpdateException) {
            System.out.println("---------Exception is BatchUpdateException.");
            BatchUpdateException bue = (BatchUpdateException) e;
            System.out.print("BatchUpdateException.getMessage(): " + bue.getMessage());
            Exception ex;
            while ((ex = bue.getNextException()) != null) {
                System.out.println("BatchUpdateException.getNextException: " + ex);
            }
        }
        if (srcStmt != null) {
            System.out.println("---------Closing oraStmt.");
            try {
                if (srcStmt != null) {
                    srcStmt.close();
                }
                srcStmt = null;
            } catch (SQLException sqle) {
                System.out.println("---------Error in closing oraStmt " + sqle.getMessage());
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        if (targetStmt != null) {
            System.out.println("---------Closing pgStmt.");
            try {
                if (targetStmt != null) {
                    targetStmt.close();
                }
                targetStmt = null;
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
            Statement srcStmt,
            Statement targetStmt,
            PreparedStatement pgPrepStmt,
            ResultSet resSet) {

        if (DEBUG) {
            System.out.println("Starting handleFinally.");
        }

        if (srcStmt != null) {
            //System.out.println("---------Closing oraStmt.");
            try {
                srcStmt.close();
                srcStmt = null;
            } catch (SQLException sqle) {
                System.out.println("---------In handleFinally, error in closing oraStmt " + sqle.getMessage());
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }
        if (targetStmt != null) {
            //System.out.println("---------Closing pgStmt.");
            try {
                targetStmt.close();
                targetStmt = null;
            } catch (SQLException sqle) {
                System.out.println("---------In handleFinally, error in closing pgStmt " + sqle.getMessage());
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
                System.out.println("---------In handleFinally, error in closing pgPrepStmt " + sqle.getMessage());
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
                System.out.println("---------In handleFinally, error in closing rs " + sqle.getMessage());
                Exception ex;
                while ((ex = sqle.getNextException()) != null) {
                    System.out.println(ex.getMessage());
                }
            }
        }

        if (DEBUG) {
            System.out.println("Leaving handleFinally.");
        }

    }

}
