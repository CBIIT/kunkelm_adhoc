package fetchfromprod;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
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
import javax.imageio.ImageIO;
import org.apache.log4j.Logger;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.io.iterator.IteratingSDFReader;

/**
 *
 * @author mwkunkel
 */
public class LoadKekuleRenderedImages {

    private static final Logger lgr = Logger.getLogger("GLOBAL");
    private static final int BATCH_FETCH_SIZE = 1000;
    private static final int BATCH_INSERT_SIZE = 1000;

    public static void main(String[] args) {

        Connection srcConn = null;
        Connection pgConn = null;

        try {

            System.out.println("Starting main.");

            System.out.println("Registering drivers.");
            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            DriverManager.registerDriver(new org.postgresql.Driver());

            System.out.println("Opening srcConn");
//            srcConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");
            srcConn = DriverManager.getConnection("jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");

            System.out.println("Opening pgConn");
            pgConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");

//    MUST DISABLE AUTOCOMMIT for fetchForward cursor (batch fetches) to work
//    EACH ResultSet MUST also have fetchDirection set FETCH_FORWARD
            srcConn.setAutoCommit(false);
            pgConn.setAutoCommit(false);

            System.out.println();
            System.out.println("Starting: loadGifFiles");
            loadGifFiles(pgConn, srcConn);

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

    public static void loadGifFiles(
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

            sqlString = "drop table if exists kekule_gif_images";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            sqlString = "create table kekule_gif_images (nsc int, gif512 bytea)";
            System.out.println(sqlString);
            pgStmt.execute(sqlString);

            prepStmtString = "insert into kekule_gif_images(nsc, gif512) values(?,?)";
            pgPrepStmt = pgConn.prepareStatement(prepStmtString);

            startTime = System.currentTimeMillis();

            File path = new File("/home/mwkunkel/KEKULE_GIF_FILES");
            File[] files = new File[]{};

            try {

                files = path.listFiles();

                for (int i = 0; i < files.length; i++) {

                    String nscString = null;

                    String path512 = null;

                    File file512 = null;

                    byte[] image512bytes = null;

                    File thisFile = files[i];

                    if (thisFile.isFile()
                            && thisFile.getName().endsWith("GIF")
                            && !thisFile.getName().contains("TWO_THIRDS")) {

                        nscString = thisFile.getName().replace(".GIF", "");

                        path512 = "/home/mwkunkel/KEKULE_GIF_FILES/" + nscString + ".GIF";

                        file512 = new File(path512);

                        System.out.println("processing NSC: " + nscString);

                        BufferedImage img512 = ImageIO.read(file512);
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        ImageIO.write(img512, "gif", baos);
                        baos.flush();
                        image512bytes = baos.toByteArray();

                    }

                    if (nscString != null && image512bytes != null) {
                        Integer nscInt = Integer.valueOf(nscString);

                        pgPrepStmt.setInt(1, nscInt);
                        pgPrepStmt.setBytes(2, image512bytes);

                        pgPrepStmt.execute();
                        pgConn.commit();

                    }

                }

            } catch (NullPointerException npe) {
                npe.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }

        } catch (Exception e) {
            handleCatch(e, oraStmt, pgStmt, pgPrepStmt, resSet);
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
