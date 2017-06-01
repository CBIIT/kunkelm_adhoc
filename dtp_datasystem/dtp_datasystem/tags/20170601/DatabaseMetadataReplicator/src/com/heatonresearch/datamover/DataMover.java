package com.heatonresearch.datamover;

import com.heatonresearch.datamover.db.*;

import java.util.*;
import java.sql.*;

/**
 * Generic data mover class. This class is designed to move data from one
 * database to another. To do this, first the tables are created in the target
 * database, then all data from the source database is copied.
 *
 * @author Jeff Heaton (http://www.heatonresearch.com)
 *
 */
public class DataMover {

    /**
     * The source database.
     */
    private Database source;
    /**
     * The target database.
     */
    private Database target;
    /**
     * The list of tables, from the source database.
     */
    private List<String> tables = new ArrayList<String>();

    public Database getSource() {
        return source;
    }

    public void setSource(Database source) {
        this.source = source;
    }

    public Database getTarget() {
        return target;
    }

    public void setTarget(Database target) {
        this.target = target;
    }

    /**
     * Create the specified table. To do this the source database will be
     * scanned for the table's structure. Then the table will be created in the
     * target database.
     *
     * @param table The name of the table to create.
     * @throws DatabaseException If a database error occurs.
     */
    public void createTable(String table) throws DatabaseException {
        String sql;

        // if the table already exists, then drop it
        if (target.tableExists(table)) {
            sql = source.generateDrop(table);
            target.execute(sql);
        }

        // now create the table
        sql = source.generateCreate(table);
        target.execute(sql);
    }

    /**
     * Create all of the tables in the database. This is done by looping over
     * the list of tables and calling createTable for each.
     *
     * @throws DatabaseException If an error occurs.
     */
    private void createTables() throws DatabaseException {
        System.out.println("Create tables.");
        Collection<String> list = source.listTables();
        for (String table : list) {
            try {
                System.out.println("Create table: " + table);
                createTable(table);
                tables.add(table);
            } catch (DatabaseException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Copy the data from one table to another. To do this both a SELECT and
     * INSERT statement must be created.
     *
     * @param table The table to copy.
     * @throws DatabaseException If a database error occurs.
     */
    private void copyTable(String table) throws DatabaseException {
        StringBuffer selectSQL = new StringBuffer();
        StringBuffer insertSQL = new StringBuffer();
        StringBuffer values = new StringBuffer();

        Collection<String> columns = source.listColumns(table);

        System.out.println("Begin copy: " + table);

        selectSQL.append("SELECT ");
        insertSQL.append("INSERT INTO ");
        insertSQL.append(table);
        insertSQL.append("(");

        boolean first = true;
        for (String column : columns) {
            if (!first) {
                selectSQL.append(",");
                insertSQL.append(",");
                values.append(",");
            } else {
                first = false;
            }

            selectSQL.append(column);
            insertSQL.append(column);
            values.append("?");
        }
        selectSQL.append(" FROM ");
        selectSQL.append(table);

        insertSQL.append(") VALUES (");
        insertSQL.append(values);
        insertSQL.append(")");

        // now copy
        PreparedStatement statement = null;
        ResultSet rs = null;

        try {
            statement = target.prepareStatement(insertSQL.toString());
            rs = source.executeQuery(selectSQL.toString());

            int rows = 0;

            while (rs.next()) {
                rows++;
                for (int i = 1; i <= columns.size(); i++) {
                    statement.setString(i, rs.getString(i));
                }
                statement.execute();
            }

            System.out.println("Copied " + rows + " rows.");
            System.out.println("");
        } catch (SQLException e) {
            throw (new DatabaseException(e));
        } finally {
            try {
                if (statement != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                throw (new DatabaseException(e));
            }
            try {
                if (rs != null) {
                    statement.close();
                }
            } catch (SQLException e) {
                throw (new DatabaseException(e));
            }
        }
    }

    private void copyTableData() throws DatabaseException {
        for (String table : tables) {
            copyTable(table);
        }
    }

    public void exportDatabse() throws DatabaseException {
        createTables();
        copyTableData();
    }
}
