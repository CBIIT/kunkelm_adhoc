package shared;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Logger;

/**
 *
 * @author mwkunkel
 */
public class FetchFromProd {

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
