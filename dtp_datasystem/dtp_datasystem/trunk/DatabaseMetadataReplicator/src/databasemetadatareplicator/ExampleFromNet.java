/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author mwkunkel
 */
public class ExampleFromNet {

    /**
     * * Created by IntelliJ IDEA. User: jesse Date: 11/21/11 Time: 1:34 PM
     *
     */
    public abstract class SchemaGenerator {

        private final Logger log = Logger.getLogger(SchemaGenerator.class.getName());

        public String createDatabaseSql(String databasename) {
            return "CREATE DATABASE " + databasename;


        }

        /**
         * This should return the String required to create a new table
         *
         * @param tableName The name of the table in the source database. This
         * is also used as the name of the table to generate.
         * @param metaData A JDBC metadata object from the source database
         * (where the schema is being created from, not where it's being written
         * to)
  *
         */
        public String createTableSql(DatabaseMetaData metaData, String tableName) throws SQLException {
            ResultSet columnsMetadata = metaData.getColumns(null, null, tableName, null);
            Set<String> pkNames = new HashSet<String>(3);

            try {
                ResultSet pkResultSet = metaData.getPrimaryKeys(null, null, tableName);
                while (pkResultSet.next()) {
                    pkNames.add(pkResultSet.getString(4));

                }
            } catch (Exception e) {
                log.log(Level.WARNING, "Could not determine primary keys; will need to be manually configured", e);

            }

            StringBuffer sql = new StringBuffer();
            sql.append("CREATE TABLE " + tableName + " (");
            String delim = "";
            while (columnsMetadata.next()) {
                sql.append(delim);
                delim = ", ";

                String columnName = columnsMetadata.getString(4);
                String sqlTypename = columnsMetadata.getString(6);
                int typeCode = columnsMetadata.getInt(5);
                String targetTypename = targetTypename(sqlTypename, typeCode);
                if (targetTypename == null) {
                    log.info("Unknown typename for type code " + typeCode + "; SQL type name is " + sqlTypename);
                    continue;

                }
                int columnSize = columnsMetadata.getInt(7);
                boolean nullsProhibited = "NO".equalsIgnoreCase(columnsMetadata.getString(18));
                int precision = columnsMetadata.getInt(9);
                appendFieldCreationClause(sql, columnName, targetTypename, columnSize, precision, nullsProhibited, pkNames.contains(columnName), metaData.getIdentifierQuoteString(), columnsMetadata);

            }
            sql.append(")");
            return sql.toString();


        }

        public void appendFieldCreationClause(StringBuffer buffer, String columnName, String targetTypename, int columnSize, int precision, boolean nullsProhibited, boolean isPrimaryKey, String identifierQuoteString, ResultSet columnMetaData) {
            if (isPrimaryKey) {
                targetTypename = "INT";
                precision = 0;

            }
            buffer.append(identifierQuoteString + columnName + identifierQuoteString + " " + targetTypename);
            if (columnSize > 0) {
                buffer.append("(" + columnSize);
                if (precision > 0) {
                    buffer.append("," + precision);

                }
                buffer.append(")");

            }
            if ("id".equalsIgnoreCase(columnName)) {
                buffer.append(" UNIQUE");

            }
            if (nullsProhibited) {
                buffer.append(" NOT NULL");

            }
            if (isPrimaryKey) {
                buffer.append(" AUTO_INCREMENT");
                buffer.append(" PRIMARY KEY");

            }


        }

        public String targetTypename(String sqlTypename, int typeCode) {
            return sqlTypename;
        }
    }
}