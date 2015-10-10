/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author mwkunkel
 */
public class ConstraintManagement {

    public static void saveConstraints(Connection destConn)
            throws Exception {

        Statement destStmt = null;

        try {

            destConn.setAutoCommit(true);
            destStmt = destConn.createStatement();

            int result = destStmt.executeUpdate("drop table if exists create_constraint_statements");
            result = destStmt.executeUpdate("drop table if exists drop_constraint_statements");

            result = destStmt.executeUpdate("create table create_constraint_statements(stmt varchar)");
            result = destStmt.executeUpdate("create table drop_constraint_statements(stmt varchar)");

            String createConstraintsSql = "insert into create_constraint_statements(stmt)\n"
                    + "SELECT 'ALTER TABLE '||nspname||'.'||relname||' ADD CONSTRAINT '||conname||' '||pg_get_constraintdef(pg_constraint.oid)||';' as statement\n"
                    + "FROM pg_constraint\n"
                    + "INNER JOIN pg_class ON conrelid = pg_class.oid\n"
                    + "INNER JOIN pg_namespace ON pg_namespace.oid = pg_class.relnamespace\n"
                    + "WHERE conname not like '%pkey'\n"
                    //                    + "AND relname in (\n"
                    //                    + "' $commaJoinedTableList '\n"
                    //                    + ")\n"
                    + "ORDER BY CASE WHEN contype = 'f' THEN 0 ELSE 1 END DESC, contype DESC, nspname DESC, relname DESC, conname DESC";

            result = destStmt.executeUpdate(createConstraintsSql);

            String dropConstraintsSql = "insert into drop_constraint_statements(stmt)\n"
                    + "SELECT 'ALTER TABLE '||nspname||'.'||relname||' DROP CONSTRAINT '||conname||' cascade;' as statement \n"
                    + "FROM pg_constraint \n"
                    + "INNER JOIN pg_class ON conrelid = pg_class.oid \n"
                    + "INNER JOIN pg_namespace ON pg_namespace.oid = pg_class.relnamespace\n"
                    + "WHERE conname not like '%pkey'\n"
                    //                    + "AND relname in (\n"
                    //                    + "' $commaJoinedTableList '\n"
                    //                    + ") \n"
                    + "ORDER BY CASE WHEN contype = 'f' THEN 0 ELSE 1 END, contype, nspname, relname, conname";

            result = destStmt.executeUpdate(dropConstraintsSql);

            System.out.println("DONE!");

        } catch (SQLException se) {
            System.out.println("SQL Exception in saveConstraints:");
            // Loop through the SQL Exceptions
            while (se != null) {
                System.out.println("State  : " + se.getSQLState());
                System.out.println("Message: " + se.getMessage());
                System.out.println("Error  : " + se.getErrorCode());
                se = se.getNextException();
            }
            throw new Exception(se);
        } catch (Exception e) {
            System.out.println("Exception in saveConstraints:");
            System.out.println(e);
            throw (e);
        } finally {
            try {
                if (destStmt != null) {
                    destStmt.close();
                    destStmt = null;
                }
            } catch (Exception e) {
                System.out.println("Exception in finally clause in saveConstraints: " + e);
                e.printStackTrace();
                throw (e);
            }
        }

    }

    public static void dropConstraints(Connection destConn)
            throws Exception {

        ArrayList<String> sqlList = new ArrayList<String>();

        Statement destStmt = null;

        try {

            destConn.setAutoCommit(true);
            destStmt = destConn.createStatement();

            ResultSet rs = destStmt.executeQuery("select stmt from drop_constraint_statements");

            while (rs.next()) {
                String sql = rs.getString("stmt");
                sqlList.add(sql);
            }
            rs.close();

            for (String sql : sqlList) {
                System.out.println("executeUpdate: " + sql);
                int rtn = destStmt.executeUpdate(sql);
                System.out.println("int rtn is: " + rtn);
            }

            System.out.println("DONE!");

        } catch (SQLException se) {
            System.out.println("SQL Exception in dropConstraints:");
            // Loop through the SQL Exceptions
            while (se != null) {
                System.out.println("State  : " + se.getSQLState());
                System.out.println("Message: " + se.getMessage());
                System.out.println("Error  : " + se.getErrorCode());
                se = se.getNextException();
            }
            throw new Exception(se);
        } catch (Exception e) {
            System.out.println("Exception in dropConstraints:");
            System.out.println(e);
            throw (e);
        } finally {
            try {
                if (destStmt != null) {
                    destStmt.close();
                    destStmt = null;
                }
            } catch (Exception e) {
                System.out.println("Exception in finally clause in dropConstraints: " + e);
                e.printStackTrace();
                throw (e);
            }
        }

    }

    public static void createConstraints(Connection destConn)
            throws Exception {

        ArrayList<String> sqlList = new ArrayList<String>();

        Statement destStmt = null;

        try {

            destConn.setAutoCommit(true);
            destStmt = destConn.createStatement();

            ResultSet rs = destStmt.executeQuery("select stmt from create_constraint_statements");

            while (rs.next()) {
                String sql = rs.getString("stmt");
                sqlList.add(sql);
            }
            rs.close();

            for (String sql : sqlList) {
                System.out.println("executeUpdate: " + sql);
                int rtn = destStmt.executeUpdate(sql);
                 System.out.println("int rtn is: " + rtn);                
            }

            System.out.println("DONE!");

        } catch (SQLException se) {
            System.out.println("SQL Exception in createConstraints:");
            // Loop through the SQL Exceptions
            while (se != null) {
                System.out.println("State  : " + se.getSQLState());
                System.out.println("Message: " + se.getMessage());
                System.out.println("Error  : " + se.getErrorCode());
                se = se.getNextException();
            }
            throw new Exception(se);
        } catch (Exception e) {
            System.out.println("Exception in createConstraints:");
            System.out.println(e);
            throw (e);
        } finally {
            try {
                if (destStmt != null) {
                    destStmt.close();
                    destStmt = null;
                }
            } catch (Exception e) {
                System.out.println("Exception in finally clause in createConstraints: " + e);
                e.printStackTrace();
                throw (e);
            }
        }

    }

    /*
    
     \copy create_constraint_statements to /tmp/create
     \copy drop_constraint_statements to /tmp/drop
    
     */
}
