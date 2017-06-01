/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

/**
 *
 * @author mwkunkel
 */
public class TblInfo {

    String tableName;
    String whereClause;

    public TblInfo(String tableName, String whereClause) {
        this.tableName = tableName;
        this.whereClause = whereClause;
    }

}
