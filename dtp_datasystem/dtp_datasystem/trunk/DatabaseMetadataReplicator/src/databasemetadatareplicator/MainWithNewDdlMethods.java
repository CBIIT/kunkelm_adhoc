/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 *
 * @author mwkunkel
 */
public class MainWithNewDdlMethods {

  public static final String ONC = "oncologydrugs data";
  public static final String PUB = "public data";
  public static final String PRIV = "private data";

  public static void main(String[] args) {

    ConnectionInfo srcCompareInfo = connectionMap.get("oncologydrugsdb_local");
    ConnectionInfo srcDataSystemInfo = connectionMap.get("oncologydrugsdb_local");
    ConnectionInfo destCompareInfo = connectionMap.get("oncologydrugsdb_local");
    ConnectionInfo destDataSystemInfo = connectionMap.get("oncologydrugsdb_local");

    Connection srcCompareConn = null;
    Connection srcDataSystemConn = null;
    Connection destCompareConn = null;
    Connection destDataSystemConn = null;

    try {

      DriverManager.registerDriver(new org.postgresql.Driver());
      // DriverManager.registerDriver(new oracle.jdbc.OracleDriver());

      srcCompareInfo.asProperties();
      srcDataSystemInfo.asProperties();
      destCompareInfo.asProperties();
      destDataSystemInfo.asProperties();

      srcCompareConn = DriverManager.getConnection(srcCompareInfo.dbUrl, srcCompareInfo.dbUser, srcCompareInfo.dbPass);
      srcDataSystemConn = DriverManager.getConnection(srcDataSystemInfo.dbUrl, srcCompareInfo.dbUser, srcCompareInfo.dbPass);
      destCompareConn = DriverManager.getConnection(destCompareInfo.dbUrl, destCompareInfo.dbUser, destCompareInfo.dbPass);
      destDataSystemConn = DriverManager.getConnection(destDataSystemInfo.dbUrl, destDataSystemInfo.dbUser, destDataSystemInfo.dbPass);

      Boolean doCompare = Boolean.TRUE;
      Boolean doDataSystem = Boolean.TRUE;

      ArrayList<String> actionList = new ArrayList<String>();
      actionList.add("parseDdl");
      actionList.add("dropTables");
      actionList.add("createTables");
//      actionList.add("propagateCuratedNscForExportPrep");
//      actionList.add("propagatePublicRelease");
//      actionList.add("prepareForExport");
//      actionList.add("propagateCompare");
//      actionList.add("propagateDataSystem");
//      actionList.add("propagateCuratedNsc");
//      actionList.add("updateSequences");

//        GrantBean cGrants = compareGrants();
////        cGrants.printGrants("oncology_user");
//        
//        statementRunner(destCompareConn, cGrants.listGrants("oncology_user"));
//        
//        GrantBean dsGrants = dataSystemGrants();
////        dsGrants.printGrants("oncology_user");
//        
//        statementRunner(destDataSystemConn, dsGrants.listGrants("oncology_user"));
//            
// #####     ##    #####    ####   ######          #####   #####   #        ####
// #    #   #  #   #    #  #       #               #    #  #    #  #       #
// #    #  #    #  #    #   ####   #####           #    #  #    #  #        ####
// #####   ######  #####        #  #               #    #  #    #  #            #
// #       #    #  #   #   #    #  #               #    #  #    #  #       #    #
// #       #    #  #    #   ####   ######          #####   #####   ######   ####
      File compareCreateFile = new File("/home/mwkunkel/PROJECTS/CURRENT/dtp_compare/compareuml/web/target/compare-core.20170410.schema-create.sql");
      File compareDropFile = new File("/home/mwkunkel/PROJECTS/CURRENT/dtp_compare/compareuml/web/target/compare-core.20170410.schema-drop.sql");

      File dataSystemCreateFile = new File("/home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/datasystemuml/web/target/datasystem-core.20170328.schema-create.sql");
      File dataSystemDropFile = new File("/home/mwkunkel/PROJECTS/CURRENT/dtp_datasystem/datasystemuml/web/target/datasystem-core.20170328.schema-drop.sql");

      DdlBean compareBean = new DdlBean();
      processDllFile(compareCreateFile, compareBean);
      processDllFile(compareDropFile, compareBean);

      DdlBean dataSystemBean = new DdlBean();
      processDllFile(dataSystemCreateFile, dataSystemBean);
      processDllFile(dataSystemDropFile, dataSystemBean);

      compareBean.printBean("compareBean");
      dataSystemBean.printBean("dataSystemBean");
//
// #####   #####    ####   #####            #####  #####   #        ####
// #    #  #    #  #    #  #    #             #    #    #  #       #
// #    #  #    #  #    #  #    #             #    #####   #        ####
// #    #  #####   #    #  #####              #    #    #  #            #
// #    #  #   #   #    #  #                  #    #    #  #       #    #
// #####   #    #   ####   #                  #    #####   ######   ####
//            
      if (actionList.contains("dropTables")) {
        statementRunner(destCompareConn, compareBean.dropConstraintStatements);
        statementRunner(destCompareConn, compareBean.dropTableStatements);
        statementRunner(destCompareConn, compareBean.dropSequenceStatements);

        statementRunner(destDataSystemConn, dataSystemBean.dropConstraintStatements);
        statementRunner(destDataSystemConn, dataSystemBean.dropTableStatements);
        statementRunner(destDataSystemConn, dataSystemBean.dropSequenceStatements);
      }
//            
//  ####   #####   ######    ##     #####  ######        #####  #####   #        ####
// #    #  #    #  #        #  #      #    #               #    #    #  #       #
// #       #    #  #####   #    #     #    #####           #    #####   #        ####
// #       #####   #       ######     #    #               #    #    #  #            #
// #    #  #   #   #       #    #     #    #               #    #    #  #       #    #
//  ####   #    #  ######  #    #     #    ######          #    #####   ######   ####
//
      if (actionList.contains("createTables")) {
//        statementRunner(destCompareConn, compareBean.createTableStatements);
        statementRunner(destDataSystemConn, dataSystemBean.createTableStatements);

//        statementRunner(destCompareConn, compareBean.createSequenceStatements);
        statementRunner(destDataSystemConn, dataSystemBean.createSequenceStatements);
      }
      //
// #####   #####    ####   #####            ####   #    #  #####     ##     #####
// #    #  #    #  #    #  #    #          #    #  #    #  #    #   #  #      #
// #    #  #    #  #    #  #    #          #       #    #  #    #  #    #     #
// #####   #####   #    #  #####           #       #    #  #####   ######     #
// #       #   #   #    #  #               #    #  #    #  #   #   #    #     #
// #       #    #   ####   #                ####    ####   #    #  #    #     #
// first thing, propagate CuratedNsc with both update flags set to FALSE
// so that it ONLY propagates the CuratedNscWithSmiles table
// note that this self-propagates to the srcDataSystemConn 

      if (actionList.contains("propagateCuratedNscForExportPrep")) {
        propagateCuratedNsc(srcDataSystemConn, srcDataSystemConn, Boolean.FALSE, Boolean.FALSE);
        propagateCuratedNsc(srcDataSystemConn, srcCompareConn, Boolean.FALSE, Boolean.FALSE);
      }
//      
// #####   #    #  #####   #          #     ####           #####   ######  #
// #    #  #    #  #    #  #          #    #    #          #    #  #       #
// #    #  #    #  #####   #          #    #               #    #  #####   #
// #####   #    #  #    #  #          #    #               #####   #       #
// #       #    #  #    #  #          #    #    #          #   #   #       #
// #        ####   #####   ######     #     ####           #    #  ######  ######
// fetch public_release from PROD to srcDataSystem and srcCompare 
// the PROD connection is hard-coded in the method...

      if (actionList.contains("propagatePublicRelease")) {
        fetchPublicRelease(srcDataSystemConn);
        fetchPublicRelease(srcCompareConn);
      }
//      
// #####   #####   ######  #####         ######  #    #  #####    ####   #####    #####
// #    #  #    #  #       #    #        #        #  #   #    #  #    #  #    #     #
// #    #  #    #  #####   #    #        #####     ##    #    #  #    #  #    #     #
// #####   #####   #       #####         #         ##    #####   #    #  #####      #
// #       #   #   #       #             #        #  #   #       #    #  #   #      #
// #       #    #  ######  #             ######  #    #  #        ####   #    #     #

      if (actionList.contains("prepareForExport")) {
        prepareForCompareExport(srcCompareConn, ONC);
        prepareForDataSystemExport(srcDataSystemConn, ONC);
      }

//            
// #####    ####            ####    ####   #    #  #####     ##    #####   ######
// #    #  #    #          #    #  #    #  ##  ##  #    #   #  #   #    #  #
// #    #  #    #          #       #    #  # ## #  #    #  #    #  #    #  #####
// #    #  #    #          #       #    #  #    #  #####   ######  #####   #
// #    #  #    #          #    #  #    #  #    #  #       #    #  #   #   #
// #####    ####            ####    ####   #    #  #       #    #  #    #  ######
//
      if (actionList.contains("propagateCompare")) {
        nukeAllDestinationTables(srcCompareConn, destCompareConn, compareTawc);
        propagateCompare(srcCompareConn, destCompareConn, compareTawc);
      }
//            
// #####    ####           #####     ##     #####    ##     ####    #   #   ####
// #    #  #    #          #    #   #  #      #     #  #   #         # #   #
// #    #  #    #          #    #  #    #     #    #    #   ####      #     ####
// #    #  #    #          #    #  ######     #    ######       #     #         #
// #    #  #    #          #    #  #    #     #    #    #  #    #     #    #    #
// #####    ####           #####   #    #     #    #    #   ####      #     ####
//
      if (actionList.contains("propagateDataSystem")) {
        nukeAllDestinationTables(srcDataSystemConn, destDataSystemConn, dataSystemTawc);
        propagateDataSystem(srcDataSystemConn, destDataSystemConn, dataSystemTawc);
        populateRdkitMol(destDataSystemConn);
      }

//      
//                                          ##
//  ####    ####    #####  #####           #  #               #    #####   #    #
// #    #  #          #    #    #           ##                #    #    #   #  #
// #        ####      #    #    #          ###                #    #    #    ##
// #            #     #    #####          #   # #             #    #    #    ##
// #    #  #    #     #    #   #          #    #              #    #    #   #  #
//  ####    ####      #    #    #          #### #             #    #####   #    #
//      
//
// #####   #####    ####   #####            ####   #    #  #####     ##     #####
// #    #  #    #  #    #  #    #          #    #  #    #  #    #   #  #      #
// #    #  #    #  #    #  #    #          #       #    #  #    #  #    #     #
// #####   #####   #    #  #####           #       #    #  #####   ######     #
// #       #   #   #    #  #               #    #  #    #  #   #   #    #     #
// #       #    #   ####   #                ####    ####   #    #  #    #     #
//
      if (actionList.contains("propagateCuratedNsc")) {
        propagateCuratedNsc(srcDataSystemConn, destDataSystemConn, destDataSystemInfo.doCompareTables, destDataSystemInfo.doDataSystemTables);
        propagateCuratedNsc(srcDataSystemConn, destCompareConn, destCompareInfo.doCompareTables, destCompareInfo.doDataSystemTables);
      }
//      
// #    #  #####   #####     ##     #####  ######           ####   ######   ####
// #    #  #    #  #    #   #  #      #    #               #       #       #    #
// #    #  #    #  #    #  #    #     #    #####            ####   #####   #    #
// #    #  #####   #    #  ######     #    #                    #  #       #  # #
// #    #  #       #    #  #    #     #    #               #    #  #       #   #
//  ####   #       #####   #    #     #    ######           ####   ######   ### #

      if (actionList.contains("updateSequences")) {
        updateSequences(destDataSystemConn);
      }

      System.out.println("Done! in NewMain");

      srcCompareConn.close();
      srcCompareConn = null;
      srcDataSystemConn.close();
      srcDataSystemConn = null;
      destCompareConn.close();
      destCompareConn = null;
      destDataSystemConn.close();
      destDataSystemConn = null;

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in DatasystemReplicator.main");
      e.printStackTrace();
    } finally {
      if (srcCompareConn != null) {
        try {
          srcCompareConn.close();
          srcCompareConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing srcCompareConn " + ex);
        }
      }
      if (srcDataSystemConn != null) {
        try {
          srcDataSystemConn.close();
          srcDataSystemConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing srcDataSystemConn " + ex);
        }
      }
      if (destCompareConn != null) {
        try {
          destCompareConn.close();
          destCompareConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing destCompareConn " + ex);
        }
      }
      if (destDataSystemConn != null) {
        try {
          destDataSystemConn.close();
          destDataSystemConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing destDataSystemConn " + ex);
        }
      }
    }
  }

  public static void statementRunner(Connection conn, List<String> statements) throws Exception {

    Statement stmt = null;

    try {

      stmt = conn.createStatement();

      for (String s : statements) {
        System.out.println(s);
        int rtn = stmt.executeUpdate(s);

      }

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in doFetchAndInsert");
      e.printStackTrace();
      throw e;
    } finally {
//Close connection
      if (stmt != null) {
        try {
          stmt.close();
          stmt = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresQuery in doFetchAndInsert");
        }
      }
    }
  }

  public static void propagateCuratedNsc(Connection srcConn, Connection destConn, Boolean doCompareTables, Boolean doDataSystemTables) throws Exception {

    Statement srcStmt = null;
    Statement destStmt = null;
    ResultSet rs = null;
    PreparedStatement destPrepStmt = null;

    try {

      srcStmt = srcConn.createStatement();
      destStmt = destConn.createStatement();

      // source (probably almost always datasystemdb_local
      String srcSqlStr = "drop table if exists curated_nsc_smiles_src";

      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      // collate targets and aliases
      srcSqlStr = "drop table if exists collated_targets";
      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      srcSqlStr = "create table collated_targets as "
              + " select nsc.nsc, "
              + " array_to_string(array_agg(sec.value), ';') as sec_targets "
              + " from curated_nsc nsc "
              + " left outer join curated_nsc_to_secondary_targe sec_join on nsc.id = sec_join.curated_nsc_to_secondary_ta_fk "
              + "      inner join curated_target sec on sec_join.secondary_targets_fk = sec.id "
              + " group by nsc.nsc";
      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      srcSqlStr = "drop table if exists collated_aliases";
      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      srcSqlStr = "create table collated_aliases as "
              + " select nsc.nsc, "
              + " array_to_string(array_agg(alia.value), ';') as aliases "
              + " from curated_nsc nsc "
              + " left outer join aliases2curated_nsc_to_aliases sec_join on nsc.id = sec_join.curated_nsc_to_aliases_fk "
              + "      inner join curated_name alia on sec_join.aliases_fk = alia.id "
              + " group by nsc.nsc";
      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      // COALESCE to pick generic_name first then preferred_name
      srcSqlStr = "create table curated_nsc_smiles_src "
              + " as "
              + " select nsc.nsc, "
              + " coalesce(gnam.value, pnam.value, '') as name, "
              + " trg.value as primary_target, "
              + " ct.can_smi as smiles "
              + " from curated_nsc nsc "
              + " left outer join curated_name gnam on nsc.generic_name_fk = gnam.id "
              + " left outer join curated_name pnam on nsc.preferred_name_fk = pnam.id "
              + " left outer join curated_target trg on nsc.primary_target_fk = trg.id "
              + " left outer join cmpd_table ct on nsc.nsc = ct.nsc ";
//         + " group by nsc.nsc";

      System.out.println(srcSqlStr);
      System.out.println();
      srcStmt.executeUpdate(srcSqlStr);

      // dest            
      String destSqlStr = "drop table if exists curated_nsc_smiles_dest";

      System.out.println(destSqlStr);
      System.out.println();
      destStmt.executeUpdate(destSqlStr);

      destSqlStr = "create table curated_nsc_smiles_dest( "
              + " nsc int, "
              + " name varchar, "
              + " primary_target varchar, "
              + " smiles varchar)";

      System.out.println(destSqlStr);
      System.out.println();
      destStmt.executeUpdate(destSqlStr);

      // update            
      destSqlStr = "insert into curated_nsc_smiles_dest(nsc, name, primary_target, smiles) values (?,?,?,?)";

      System.out.println(destSqlStr);
      System.out.println();
      destPrepStmt = destConn.prepareStatement(destSqlStr);

      srcSqlStr = "select nsc, name, primary_target, smiles from curated_nsc_smiles_src";

      System.out.println(srcSqlStr);
      System.out.println();
      rs = srcStmt.executeQuery(srcSqlStr);

      while (rs.next()) {

        destPrepStmt.setInt(1, rs.getInt("nsc"));
        destPrepStmt.setString(2, rs.getString("name"));
        destPrepStmt.setString(3, rs.getString("primary_target"));
        destPrepStmt.setString(4, rs.getString("smiles"));

        destPrepStmt.execute();

      }

      rs.close();
      destPrepStmt.close();

      // updating target tables
      if (doCompareTables) {

        // AS WRITTEN, ONLY UPDATES nulls - WON'T OVERWRITE! (sarcomadb, e.g.)
        destSqlStr = "update synthetic_ident "
                + " set drug_name = curated_nsc_smiles_dest.name, "
                + " target = curated_nsc_smiles_dest.primary_target, "
                + " smiles = curated_nsc_smiles_dest.smiles "
                + " from curated_nsc_smiles_dest, nsc_ident "
                + " where synthetic_ident.id = nsc_ident.id "
                + " and nsc_ident.nsc = curated_nsc_smiles_dest.nsc"
                + " and synthetic_ident.drug_name is null";

        System.out.println(destSqlStr);
        System.out.println();
        destStmt.executeUpdate(destSqlStr);

      }

      if (doDataSystemTables) {

        // AS WRITTEN, ONLY UPDATES nulls - WON'T OVERWRITE! (sarcomadb, e.g.)
        String[] sqlArr = new String[]{
          //
          // NUCLEAR OPTION!
          // DANGER! DANGER! DANGER!
          "update nsc_cmpd set name = null",
          //
          // NUCLEAR OPTION!
          // DANGER! DANGER! DANGER!
          "update cmpd_table set name = null",
          //
          "update nsc_cmpd "
          + " set name = curated_nsc_smiles_dest.name "
          + " from curated_nsc_smiles_dest "
          + " where nsc_cmpd.nsc = curated_nsc_smiles_dest.nsc ",
          // + " and nsc_cmpd.name is null",
          //
          "update cmpd_table "
          + " set name = curated_nsc_smiles_dest.name "
          + " from curated_nsc_smiles_dest "
          + " where cmpd_table.nsc = curated_nsc_smiles_dest.nsc ",
          // + " and cmpd_table.name is null",
          //
          //  #####    ##    #####    ####   ######   #####   ####
          //    #     #  #   #    #  #    #  #          #    #
          //    #    #    #  #    #  #       #####      #     ####
          //    #    ######  #####   #  ###  #          #         #
          //    #    #    #  #   #   #    #  #          #    #    #
          //    #    #    #  #    #   ####   ######     #     ####

          "drop table if exists distinct_targets",
          //
          "create table distinct_targets "
          + " as select distinct primary_target "
          + " from  curated_nsc_smiles_dest",
          //
          "drop table if exists targets_to_add",
          //
          "create table targets_to_add  "
          + " as select primary_target "
          + " from distinct_targets  "
          + " except "
          + " select target from cmpd_target",
          //
          "insert into cmpd_target(id, target)  "
          + " select nextval('cmpd_target_seq'), primary_target  "
          + " from targets_to_add",
          //
          "drop table if exists nsc_target_to_add",
          //
          "create table nsc_target_to_add "
          + " as select nsc, primary_target "
          + " from curated_nsc_smiles_dest cns "
          + " except "
          + " select nsc, target "
          + " from nsc_cmpd nc, cmpd_targets2nsc_cmpds ct2nc, cmpd_target ct "
          + " where ct2nc.nsc_cmpds_fk = nc.id "
          + " and ct2nc.cmpd_targets_fk = ct.id",
          //
          "insert into cmpd_targets2nsc_cmpds(nsc_cmpds_fk, cmpd_targets_fk) "
          + " select nc.id, ct.id "
          + " from nsc_target_to_add ntta, nsc_cmpd nc, cmpd_target ct "
          + " where "
          + " ntta.nsc = nc.nsc "
          + " and ntta.primary_target = ct.target",
          //
          "update cmpd_table"
          + " set formatted_targets_string = primary_target"
          + " from nsc_target_to_add"
          + " where cmpd_table.nsc = nsc_target_to_add.nsc",
          //

          //   ##    #          #      ##     ####   ######   ####
          //  #  #   #          #     #  #   #       #       #
          // #    #  #          #    #    #   ####   #####    ####
          // ######  #          #    ######       #  #            #
          // #    #  #          #    #    #  #    #  #       #    #
          // #    #  ######     #    #    #   ####   ######   ####

          "drop table if exists distinct_aliases",
          //
          "create table distinct_aliases "
          + " as select distinct name as alias"
          + " from  curated_nsc_smiles_dest",
          //
          "drop table if exists aliases_to_add",
          //
          "create table aliases_to_add  "
          + " as select alias "
          + " from distinct_aliases  "
          + " except "
          + " select alias from cmpd_alias",
          //
          "insert into cmpd_alias(id, alias, cmpd_alias_type_fk)  "
          + " select nextval('cmpd_alias_seq'), alias, 53  "
          + " from aliases_to_add",
          //
          "drop table if exists nsc_alias_to_add",
          //
          "create table nsc_alias_to_add "
          + " as select nsc, name as alias "
          + " from curated_nsc_smiles_dest cns "
          + " except "
          + " select nsc, alias "
          + " from nsc_cmpd nc, cmpd_aliases2nsc_cmpds ct2nc, cmpd_alias ct "
          + " where ct2nc.nsc_cmpds_fk = nc.id "
          + " and ct2nc.cmpd_aliases_fk = ct.id",
          //
          "insert into cmpd_aliases2nsc_cmpds(nsc_cmpds_fk, cmpd_aliases_fk) "
          + " select nc.id, ct.id "
          + " from nsc_alias_to_add ntta, nsc_cmpd nc, cmpd_alias ct "
          + " where "
          + " ntta.nsc = nc.nsc "
          + " and ntta.alias = ct.alias",
          //
          "update cmpd_table"
          + " set formatted_aliases_string = alias"
          + " from nsc_alias_to_add"
          + " where cmpd_table.nsc = nsc_alias_to_add.nsc;"

        };

        for (String sqlStr : sqlArr) {
          System.out.println(sqlStr);
          System.out.println();
          destStmt.executeUpdate(sqlStr);
        }

      }

      srcStmt.close();
      destStmt.close();

      System.out.println("Done! in propagateCuratedNsc");

    } catch (Exception e) {
      System.out.println("Exception in propagateCuratedNsc:");
      System.out.println(e);
      throw (e);
    } finally {
      try {
        if (rs != null) {
          rs.close();
          rs = null;
        }
        if (srcStmt != null) {
          srcStmt.close();
          srcStmt = null;
        }
        if (destStmt != null) {
          destStmt.close();
          destStmt = null;
        }
        if (destPrepStmt != null) {
          destPrepStmt.close();
          destPrepStmt = null;
        }
      } catch (Exception e) {
        System.out.println("Exception in finally clause in propagateCuratedNsc: " + e);
        e.printStackTrace();
        throw (e);
      }
    }

  }

  public static void nukeAllDestinationTables(Connection srcConn, Connection destDataSystemConn, ArrayList<TblInfo> tawcList) throws Exception {

    try {

      for (TblInfo tawc : tawcList) {
        Replicator.nuke(destDataSystemConn, tawc.tableName);
      }

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in nukeAllDestinationTables");
      e.printStackTrace();
      throw e;
    } finally {

    }
  }

  public static void propagateCompare(Connection srcConn, Connection destConn, ArrayList<TblInfo> tawcList) throws Exception {

    try {

      for (TblInfo tawc : tawcList) {

        // replicate the tables
        if (!tawc.whereClause.equals("DO NOT REPLICATE")) {
          Replicator.useMetadata(srcConn, destConn, tawc.tableName, tawc.whereClause);
        }

      }

      System.out.println("Done! in propagateCompare");

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in propagateCompare");
      e.printStackTrace();
      throw e;
    } finally {

    }
  }

  public static void propagateDataSystem(Connection srcConn, Connection destDataSystemConn, ArrayList<TblInfo> tawcList) throws Exception {

    try {

      for (TblInfo tawc : tawcList) {
        if (!tawc.whereClause.equals("DO NOT REPLICATE")) {
          Replicator.useMetadata(srcConn, destDataSystemConn, tawc.tableName, tawc.whereClause);
        }
      }

      System.out.println("Done! in propagateDataSystem");

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in propagateDataSystem");
      e.printStackTrace();
      throw e;
    } finally {

    }
  }

  public static void populateRdkitMol(Connection destDataSystemConn) throws Exception {

    Statement stmt = null;

    try {

      destDataSystemConn.setAutoCommit(true);
      stmt = destDataSystemConn.createStatement();

      String sqlStr = "drop table if exists rdkit_validity";

      sqlStr = "create table rdkit_validity "
              + " as "
              + " select nsc, is_valid_smiles(can_smi::cstring) as valid_can_smi, "
              + " is_valid_smiles(can_taut::cstring) as valid_can_taut, "
              + " is_valid_smiles(can_taut_strip_stereo::cstring) as valid_can_taut_strip_stereo, "
              + " is_valid_ctab(ctab::cstring) as valid_ctab "
              + " from cmpd_table where can_smi is not null";

      sqlStr = "create index rdkv_nsc on rdkit_validity(nsc)";

      sqlStr = "insert into rdkit_mol(id, nsc, mol, mol_from_ctab) "
              + " select ct.nsc, ct.nsc, mol_from_smiles(ct.can_taut::cstring), mol_from_ctab(ctab::cstring) "
              + " from cmpd_table ct, rdkit_validity v "
              + " where ct.nsc = v.nsc "
              + " and v.valid_can_taut = 't' "
              + " and v.valid_ctab = 't'";

      sqlStr = "drop table if exists rdkit_mol";
      System.out.println(sqlStr);
      stmt.executeUpdate(sqlStr);

      sqlStr = "create table rdkit_mol(id bigint, nsc int, mol mol, mol_from_ctab mol)";
      System.out.println(sqlStr);
      stmt.executeUpdate(sqlStr);

      sqlStr = "insert into rdkit_mol(id, nsc, mol, mol_from_ctab) select nsc, nsc, mol_from_smiles(can_taut::cstring), mol_from_ctab(ctab::cstring) from cmpd_table";
      System.out.println(sqlStr);
      stmt.executeUpdate(sqlStr);

      sqlStr = "create index rdkit_mol_mol on rdkit_mol using gist(mol)";
      System.out.println(sqlStr);
      stmt.executeUpdate(sqlStr);

    } catch (SQLException se) {
      System.out.println("SQL Exception in populateRdkitMol:");
      // Loop through the SQL Exceptions
      while (se != null) {
        System.out.println("State  : " + se.getSQLState());
        System.out.println("Message: " + se.getMessage());
        System.out.println("Error  : " + se.getErrorCode());
        se = se.getNextException();
      }
      throw new Exception(se);
    } catch (Exception e) {
      System.out.println("Exception in populateRdkitMol:");
      System.out.println(e);
      throw (e);
    } finally {
      try {
        if (stmt != null) {
          stmt.close();
          stmt = null;
        }
      } catch (Exception e) {
        System.out.println("Exception in finally clause in populateRdkitMol: " + e);
        e.printStackTrace();
        throw (e);
      }
    }

  }

  public static void prepareForCompareExport(Connection srcCompareConn, String oncPubPriv) throws Exception {

    System.out.println("In prepareForCompareExport");

    Statement cmprStmt = null;
    Statement dsStmt = null;

    try {

      srcCompareConn.setAutoCommit(true);
      cmprStmt = srcCompareConn.createStatement();

      // EXPECTS to find nsc_for_export(prefix, nsc)
      // EXPECTS to find target_set_name_for_export(target_set_name);
      String[] ntsArr = new String[]{
        "STANDARD_AGENTS_GI50",
        "STANDARD_AGENTS_TGI",
        "STANDARD_AGENTS_LC50",
        "SYNTHETIC_COMPOUNDS_GI50",
        "SYNTHETIC_COMPOUNDS_TGI",
        "SYNTHETIC_COMPOUNDS_LC50",
        "BOYD_SERIES_GI50",
        "NATURAL_PRODUCTS_GI50",
        "NATURAL_PRODUCTS_TGI",
        "NATURAL_PRODUCTS_LC50",
        "BOYD_SERIES_TGI",
        "DIVERSITY_SET_GI50",
        "DIVERSITY_SET_TGI",
        "DIVERSITY_SET_LC50",
        "BOYD_SERIES_LC50",
        "MCCLOUD_SERIES_GI50",
        "MCCLOUD_SERIES_TGI",
        "MCCLOUD_SERIES_LC50",
        "MOLTID_GC_SERIES_MICROARRAY_NOVARTIS",
        "MOLTID_GC_SERIES_MICROARRAY_STANFORD",
        "MOLTID_GC_SERIES_MICROARRAY_GENELOGIC_U95",
        "MOLTID_GC_SERIES_MICROARRAY_GENELOGIC_U133",
        "MOLTID_GC_SERIES_MICROARRAY_CHIRON",
        "MECHANISTIC_SET_GI50",
        "MECHANISTIC_SET_TGI",
        "MECHANISTIC_SET_LC50",
        "BEC_REFERRAL_SET_GI50",
        "BEC_REFERRAL_SET_TGI",
        "BEC_REFERRAL_SET_LC50",
        "MIR_ISRAEL",
        "ARRAYCGH_GRAY",
        "FEINBERG_METHYLATION",
        "KARYOTYPE",
        "GARRAWAY_SNP",
        "METABOLON",
        "MILLENIUM",
        "MOLTID_GC_SERIES_MICROARRAY_MOCK_TX_2H",
        "MOLTID_GC_SERIES_MICROARRAY_MOCK_TX_6H",
        "MOLTID_GC_SERIES_MICROARRAY_MOCK_TX_24H",
        "MOLTID_MT_SERIES",
        "MOLTID_CG_SERIES",
        "MOLTID_SA_SERIES",
        "MICRORNA",
        "MOLTID_GC_SERIES_MICROARRAY_ALL",
        "WEINSTEIN_CROCE_MIR",
        "REINHOLD_ARRAY_CGH_DNACOPYNUM",
        "MARKETED_DRUGS_GI50",
        "MARKETED_DRUGS_TGI",
        "MARKETED_DRUGS_LC50",
        "MOLTID_GC_SERIES_MICROARRAY_GENELOGIC_U133PLUS2",
        "MOLTID_DS_SERIES_SEQ_MUT"
      };

      ArrayList<String> sqlList = new ArrayList<String>();

      if (oncPubPriv.equals(ONC)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select 'S'::varchar as prefix, nsc from curated_nsc_smiles_dest");
        sqlList.add("drop table if exists target_set_names_for_export");
        sqlList.add("create table target_set_names_for_export(target_set_name varchar)");

      } else if (oncPubPriv.equals(PUB)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select 'S'::varchar as prefix, nsc from publicrelease");
        sqlList.add("drop table if exists target_set_names_for_export");
        sqlList.add("create table target_set_names_for_export(target_set_name varchar)");

      } else if (oncPubPriv.equals(PRIV)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select distinct prefix, nsc from nsc_ident");
        sqlList.add("drop table if exists target_set_names_for_export");
        sqlList.add("create table target_set_names_for_export(target_set_name varchar)");

      } else {
        throw new Exception("Unrecognized oncPubPriv");
      }

      sqlList.add("drop table if exists cell_line_data_set_ident_for_export");
      sqlList.add("drop table if exists cell_line_data_set_for_export");
      sqlList.add("create table cell_line_data_set_ident_for_export(id bigint)");
      sqlList.add("create table cell_line_data_set_for_export(id bigint)");

      for (String sql : sqlList) {
        System.out.println(sql);
        cmprStmt.executeUpdate(sql);
      }

      // NSC
      String sqlStr = "insert into cell_line_data_set_ident_for_export(id) "
              + " select ni.id "
              + " from nsc_ident ni, nsc_for_export nfe "
              + " where ni.prefix = nfe.prefix "
              + " and ni.nsc = nfe.nsc "
              + " and ni.endpoint in ('GI50', 'TGI', 'LC50') "
              + " and ni.exp_id = 'AVGDATA'";
      System.out.println(sqlStr);
      cmprStmt.executeUpdate(sqlStr);

      // MolTarg from named_target_set
      sqlStr = "insert into cell_line_data_set_ident_for_export(id) "
              + " select cell_line_data_sets_fk "
              + " from named_target_set nts, cell_line_data_sets2named_targ clds2nts "
              + " where clds2nts.named_target_sets_fk = nts.id "
              + " and nts.target_set_name in ( "
              + " select target_set_name from target_set_names_for_export "
              + " ) ";

      System.out.println(sqlStr);
      cmprStmt.executeUpdate(sqlStr);

      sqlStr = "insert into cell_line_data_set_for_export(id) "
              + "select id from cell_line_data_set "
              + "where cell_line_data_set_ident_fk in (select id from cell_line_data_set_ident_for_export)";
      System.out.println(sqlStr);
      cmprStmt.executeUpdate(sqlStr);

      sqlStr = "create index cldsfe_id_idx on cell_line_data_set_for_export(id)";
      System.out.println(sqlStr);
      cmprStmt.executeUpdate(sqlStr);

      sqlStr = "create index cldsife_id_idx on cell_line_data_set_ident_for_export(id)";
      System.out.println(sqlStr);
      cmprStmt.executeUpdate(sqlStr);

      System.out.println("DONE!");

    } catch (SQLException se) {
      System.out.println("SQL Exception in prepareForCompareExport:");
      // Loop through the SQL Exceptions
      while (se != null) {
        System.out.println("State  : " + se.getSQLState());
        System.out.println("Message: " + se.getMessage());
        System.out.println("Error  : " + se.getErrorCode());
        se = se.getNextException();
      }
      throw new Exception(se);
    } catch (Exception e) {
      System.out.println("Exception in prepareForCompareExport:");
      System.out.println(e);
      throw (e);
    } finally {
      try {
        if (cmprStmt != null) {
          cmprStmt.close();
          cmprStmt = null;
        }
        if (dsStmt != null) {
          dsStmt.close();
          dsStmt = null;
        }
      } catch (Exception e) {
        System.out.println("Exception in finally clause in prepareForCompareExports: " + e);
        e.printStackTrace();
        throw (e);
      }
    }

  }

  public static void prepareForDataSystemExport(Connection srcCompareConn, String oncPubPriv) throws Exception {

    System.out.println("In prepareForDataSystemExport");

    Statement cmprStmt = null;
    Statement dsStmt = null;

    try {

      srcCompareConn.setAutoCommit(true);
      cmprStmt = srcCompareConn.createStatement();

      ArrayList<String> sqlList = new ArrayList<String>();

      if (oncPubPriv.equals(ONC)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select 'S'::varchar as prefix, nsc from curated_nsc_smiles_dest");

      } else if (oncPubPriv.equals(PUB)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select 'S'::varchar as prefix, nsc from publicrelease");

      } else if (oncPubPriv.equals(PRIV)) {

        sqlList.add("drop table if exists nsc_for_export");
        sqlList.add("create table nsc_for_export as select nsc from nsc_cmpd");

      } else {

        throw new Exception("Unrecognized oncPubPriv");

      }

      for (String sql : sqlList) {
        System.out.println(sql);
        cmprStmt.executeUpdate(sql);
      }

      System.out.println("DONE!");

    } catch (SQLException se) {
      System.out.println("SQL Exception in prepareForDataSystemExport:");
      // Loop through the SQL Exceptions
      while (se != null) {
        System.out.println("State  : " + se.getSQLState());
        System.out.println("Message: " + se.getMessage());
        System.out.println("Error  : " + se.getErrorCode());
        se = se.getNextException();
      }
      throw new Exception(se);
    } catch (Exception e) {
      System.out.println("Exception in prepareForDataSystemExport:");
      System.out.println(e);
      throw (e);
    } finally {
      try {
        if (cmprStmt != null) {
          cmprStmt.close();
          cmprStmt = null;
        }
        if (dsStmt != null) {
          dsStmt.close();
          dsStmt = null;
        }
      } catch (Exception e) {
        System.out.println("Exception in finally clause in In prepareForDataSystemExport: " + e);
        e.printStackTrace();
        throw (e);
      }
    }

  }

  public static void updateSequences(Connection postgresConn) throws Exception {

    String[] seqBaseNameArray = new String[]{
      "cell_line_data_set_ident",
      "cell_line_data_set",
      "compare_cell_line",
      "compare_result",
      "conc_resp_assay",
      "conc_resp_element",
      "job",
      "named_target_set",
      "nsc_compound",
      "test_result",
      "test_result_type"
    };

    String highValQuery;
    String setValQuery;

    int highVal = 0;

    Statement postgresQuery = null;
    PreparedStatement postgresPrepStmt = null;

    ResultSet rs = null;

    try {

      postgresQuery = postgresConn.createStatement();

      for (int i = 0; i < seqBaseNameArray.length; i++) {

        highValQuery = "select max(id) from " + seqBaseNameArray[i];
        setValQuery = "select setval('" + seqBaseNameArray[i] + "_seq', ?)";

        System.out.println(highValQuery);
        System.out.println(setValQuery);

        rs = postgresQuery.executeQuery(highValQuery);

        while (rs.next()) {
          highVal = rs.getInt("max");
        }

        System.out.println("Value of max(id) in table: " + seqBaseNameArray[i] + " is: " + highVal);

        if (highVal > 0) {
          postgresPrepStmt = postgresConn.prepareStatement(setValQuery);
          postgresPrepStmt.setInt(1, highVal);
          postgresPrepStmt.execute();
        }

      }

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in doFetchAndInsert");
      e.printStackTrace();
      throw e;
    } finally {
//Close connection
      if (postgresQuery != null) {
        try {
          postgresQuery.close();
          postgresQuery = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresQuery in doFetchAndInsert");
        }
      }
      if (rs != null) {
        try {
          rs.close();
          rs = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing rs in doFetchAndInsert");
        }
      }
    }

  }

  public static void processDllFile(File f, DdlBean ddlb) {

    FileReader fr = null;
    BufferedReader br = null;

    String line;
    String fixedLine;

    StringBuilder sb = new StringBuilder();

    try {

      fr = new FileReader(f);
      br = new BufferedReader(fr);

      String formattedStr;
      String modifiedStr;

      while ((line = br.readLine()) != null) {

        fixedLine = line.replaceAll("\t", " ");
        sb.append(" ").append(fixedLine).append(" ");

        if (fixedLine.contains(";")) {

          formattedStr = sb.toString().replaceAll("\n", " ").replaceAll(" +", " ");

          if (formattedStr.contains("create table")) {
            ddlb.createTableStatements.add(formattedStr);
          } else if (formattedStr.contains("drop table")) {
            modifiedStr = formattedStr.replaceAll("alter table", "alter table if exists").replaceAll("drop table", "drop table if exists");
            ddlb.dropTableStatements.add(modifiedStr);
          } else if (formattedStr.contains("create index")) {
            ddlb.createIndexStatements.add(formattedStr);
          } else if (formattedStr.contains("drop index")) {
            modifiedStr = formattedStr.replaceAll("alter table", "alter table if exists").replaceAll("drop index", "drop index if exists");
            ddlb.dropIndexStatements.add(modifiedStr);
          } else if (formattedStr.contains("add constraint")) {
            ddlb.createConstraintStatements.add(formattedStr);
          } else if (formattedStr.contains("drop constraint")) {
            modifiedStr = formattedStr.replaceAll("alter table", "alter table if exists").replaceAll("drop constraint", "drop constraint if exists");
            ddlb.dropConstraintStatements.add(modifiedStr);
          } else if (formattedStr.contains("create sequence")) {
            if (!formattedStr.contains("hibernate_sequence")) {
              ddlb.createSequenceStatements.add(formattedStr);
            }
          } else if (formattedStr.contains("drop sequence")) {
            modifiedStr = formattedStr.replaceAll("alter table", "alter table if exists").replaceAll("drop sequence", "drop sequence if exists");
            ddlb.dropSequenceStatements.add(modifiedStr);
          } else {
            ddlb.remnantStatements.add(formattedStr);
          }

          sb = new StringBuilder();
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
      try {
        br.close();
        fr.close();
      } catch (Exception e) {

      }
    }
  }

  public static void fetchPublicRelease(Connection destConn) {

    Statement destStmt = null;

    Connection sourceConn = null;
    Statement sourceStmt = null;

    try {

      DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
      DriverManager.registerDriver(new org.postgresql.Driver());

      sourceConn = DriverManager.getConnection("jdbc:oracle:thin:@//dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");

      destConn.setAutoCommit(true);
      destStmt = destConn.createStatement();

      int result = destStmt.executeUpdate("drop table if exists publicrelease");
      result = destStmt.executeUpdate("create table publicrelease(nsc int)");

      Replicator.useMetadata(sourceConn, destConn, "publicrelease", "");

      System.out.println("Done! in Main");

      sourceConn.close();

      sourceConn = null;

    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in GetPublicRelease.main");
      e.printStackTrace();
    } finally {
      if (sourceConn != null) {
        try {
          sourceConn.close();
          sourceConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing sourceConn");
        }
      }
    }
  }

  static Map<String, ConnectionInfo> connectionMap = new HashMap<String, ConnectionInfo>();

  static {

    connectionMap.put("microxenodb_local", new ConnectionInfo(
            "microxenodb_local",
            "jdbc:postgresql://localhost:5432/microxenodb",
            "mwkunkel",
            "donkie11",
            Boolean.FALSE,
            Boolean.FALSE
    ));

    connectionMap.put("microxenodb_dev", new ConnectionInfo(
            "microxenodb_dev",
            "jdbc:postgresql://ncidb-d115-d.nci.nih.gov:5473/microxeno",
            "microxeno",
            "M1cr0x0293025en0",
            Boolean.FALSE,
            Boolean.FALSE
    ));

    connectionMap.put("datasystemdb_local", new ConnectionInfo(
            "datasystemdb_local",
            "jdbc:postgresql://localhost:5432/datasystemdb",
            "mwkunkel",
            "donkie11",
            Boolean.FALSE,
            Boolean.TRUE
    ));

    connectionMap.put("oncologydrugsdb_local", new ConnectionInfo(
            "oncologydrugsdb_local",
            "jdbc:postgresql://localhost:5432/oncologydrugsdb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.TRUE
    ));

    connectionMap.put("oncologydrugsdb_dev", new ConnectionInfo(
            "oncologydrugsdb_dev",
            "jdbc:postgresql://ncidb-d115-d:5474/oncology",
            "oncology",
            "OnC0L029302802K1t",
            Boolean.TRUE,
            Boolean.TRUE
    ));

    connectionMap.put("publiccomparedb_local", new ConnectionInfo(
            "publiccomparedb_local",
            "jdbc:postgresql://localhost:5432/publiccomparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("publiccomparedb_dev", new ConnectionInfo(
            "publiccomparedb_dev",
            "jdbc:postgresql://ncidb-d115-d:5473/pubcompare",
            "publiccompare",
            "P3b092094wlC0m3",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("privatecomparedb_local", new ConnectionInfo(
            "privatecomparedb_local",
            "jdbc:postgresql://localhost:5432/privatecomparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("comparedb_local", new ConnectionInfo(
            "privatecomparedb_local",
            "jdbc:postgresql://localhost:5432/comparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("sarcomacomparedb_local", new ConnectionInfo(
            "sarcomacomparedb_local",
            "jdbc:postgresql://localhost:5432/sarcomacomparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("zcomparedb_local", new ConnectionInfo(
            "zcomparedb_local",
            "jdbc:postgresql://localhost:5432/zcomparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    connectionMap.put("nineconccomparedb_local", new ConnectionInfo(
            "nineconccomparedb_local",
            "jdbc:postgresql://localhost:5432/nineconccomparedb",
            "mwkunkel",
            "donkie11",
            Boolean.TRUE,
            Boolean.FALSE
    ));

    Set<String> dbNameSet = connectionMap.keySet();
    ArrayList<String> dbNameList = new ArrayList<String>(dbNameSet);
    Collections.sort(dbNameList);

    for (String dbName : dbNameList) {
      connectionMap.get(dbName).asProperties();
    }

  }

  static ArrayList<TblInfo> mx_tawc = new ArrayList<TblInfo>();

  static {

    mx_tawc.add(new TblInfo("affymetrix_Identifier", ""));
    mx_tawc.add(new TblInfo("tumor", ""));
    mx_tawc.add(new TblInfo("tumor_type", ""));
    mx_tawc.add(new TblInfo("flat_data", ""));

  }

  static ArrayList<TblInfo> dataSystemTawc = new ArrayList<TblInfo>();

  static {

    dataSystemTawc.add(new TblInfo("ad_hoc_cmpd", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("ad_hoc_cmpd_fragment", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("ad_hoc_cmpd_fragment_p_chem", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("ad_hoc_cmpd_fragment_structure", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("cmpd_known_salt", ""));
    dataSystemTawc.add(new TblInfo("nsc_cmpd_type", ""));
    dataSystemTawc.add(new TblInfo("cmpd_alias_type", ""));
    dataSystemTawc.add(new TblInfo("cmpd_relation_type", ""));
    dataSystemTawc.add(new TblInfo("cmpd_fragment_type", ""));
    dataSystemTawc.add(new TblInfo("cmpd_inventory", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_annotation", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_bio_assay", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_legacy_cmpd", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_table", " where nsc in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd", " where id in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("nsc_cmpd", " where nsc in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_fragment", " where nsc_cmpd_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_fragment_p_chem", " where id in (select cmpd_fragment_p_chem_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_fragment_structure", " where id in (select cmpd_fragment_structure_fk from cmpd_fragment where nsc_cmpd_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_alias", " where id in (select cmpd_aliases_fk from cmpd_aliases2nsc_cmpds where cmpd_aliases2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_aliases2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_list", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("cmpd_list_member", "DO NOT REPLICATE"));
    dataSystemTawc.add(new TblInfo("cmpd_named_set", " where id in (select cmpd_named_sets_fk from cmpd_named_sets2nsc_cmpds where cmpd_named_sets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_named_sets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_plate", " where id in (select cmpd_plates_fk from cmpd_plates2nsc_cmpds where cmpd_plates2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_plates2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_project", " where id in (select cmpd_projects_fk from cmpd_projects2nsc_cmpds where cmpd_projects2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_projects2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_pub_chem_sid", " where id in (select cmpd_pub_chem_sids_fk from cmpd_pub_chem_sids2nsc_cmpds where cmpd_pub_chem_sids2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_pub_chem_sids2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_target", " where id in (select cmpd_targets_fk from cmpd_targets2nsc_cmpds where cmpd_targets2nsc_cmpds.nsc_cmpds_fk in (select nsc from nsc_for_export))"));
    dataSystemTawc.add(new TblInfo("cmpd_targets2nsc_cmpds", " where nsc_cmpds_fk in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("cmpd_related", " where nsc_cmpd_fk in (select nsc from nsc_for_export)"));

    // rdkit_mol has to be handled separately
//        dataSystemTawc.add(new TblInfo("rdkit_mol", " where nsc in (select nsc from nsc_for_export)"));
    dataSystemTawc.add(new TblInfo("tanimoto_scores", " where nsc1 in (select nsc from nsc_for_export) and nsc2 in (select nsc from nsc_for_export)"));
    /*        
   curated
     */
    dataSystemTawc.add(new TblInfo("curated_name", ""));
    dataSystemTawc.add(new TblInfo("curated_nsc", ""));
    dataSystemTawc.add(new TblInfo("curated_nsc_to_secondary_targe", ""));
    dataSystemTawc.add(new TblInfo("curated_nscs2projects", ""));
    dataSystemTawc.add(new TblInfo("curated_originator", ""));
    dataSystemTawc.add(new TblInfo("curated_project", ""));
    dataSystemTawc.add(new TblInfo("curated_target", ""));

  }

  static ArrayList<TblInfo> compareTawc = new ArrayList<TblInfo>();

  static {

    compareTawc.add(new TblInfo("affy_dna_ident", " , cell_line_data_set_ident_for_export where affy_dna_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("affy_exon_ident", " , cell_line_data_set_ident_for_export where affy_exon_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("build_date", ""));
    compareTawc.add(new TblInfo("cell_line_data_sets2named_targ", " , named_target_set, target_set_names_for_export where cell_line_data_sets2named_targ.named_target_sets_fk = named_target_set.id and named_target_set.target_set_name = target_set_names_for_export.target_set_name"));
    compareTawc.add(new TblInfo("named_target_set", " , target_set_names_for_export where named_target_set.target_set_name = target_set_names_for_export.target_set_name"));
    compareTawc.add(new TblInfo("cell_line_data_set", " , cell_line_data_set_for_export where cell_line_data_set.id = cell_line_data_set_for_export.id"));
    compareTawc.add(new TblInfo("micro_array_ident", " , cell_line_data_set_ident_for_export where micro_array_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("micro_rna_ident", " , cell_line_data_set_ident_for_export where micro_rna_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("mol_targ_catch_all_ident", " , cell_line_data_set_ident_for_export where mol_targ_catch_all_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("mol_targ_ident", " , cell_line_data_set_ident_for_export where mol_targ_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("nano_string_ident", " , cell_line_data_set_ident_for_export where nano_string_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("nat_prod_ident", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("nsc_ident", " , cell_line_data_set_ident_for_export where nsc_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("synthetic_ident", " , cell_line_data_set_ident_for_export where synthetic_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("cell_line_data_set_ident", " , cell_line_data_set_ident_for_export where cell_line_data_set_ident.id = cell_line_data_set_ident_for_export.id"));
    compareTawc.add(new TblInfo("compare_cell_line", ""));
    compareTawc.add(new TblInfo("compare_result", "DO NOT REPLICATE"));

    // open-ended by nsc
    compareTawc.add(new TblInfo("nsc_compound", " , nsc_for_export where nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc"));
    compareTawc.add(new TblInfo("conc_resp_assay", " , nsc_compound, nsc_for_export where nsc_compound_fk = nsc_compound.id and nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc and exp_id = 'AVGDATA'"));
    compareTawc.add(new TblInfo("conc_resp_element", " , conc_resp_assay, nsc_compound, nsc_for_export where conc_resp_element.conc_resp_assay_fk = conc_resp_assay.id and conc_resp_assay.nsc_compound_fk = nsc_compound.id and nsc_compound.prefix = nsc_for_export.prefix and nsc_compound.nsc = nsc_for_export.nsc and exp_id = 'AVGDATA'"));
    compareTawc.add(new TblInfo("dtp_cell_line_data_set", " , cell_line_data_set_for_export where dtp_cell_line_data_set.id = cell_line_data_set_for_export.id"));
    compareTawc.add(new TblInfo("dtp_test_result", " , test_result, cell_line_data_set_for_export where dtp_test_result.id = test_result.id and test_result.cell_line_data_set_fk = cell_line_data_set_for_export.id"));
    compareTawc.add(new TblInfo("grid_compare_columns", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("grid_compare_job", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("grid_compare_rows", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("job", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("job_for_req_cell_lines2require", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("ignore_cell_lines2job_for_ign_", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("standard_compare_job", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("test_result", " , cell_line_data_set_for_export where test_result.cell_line_data_set_fk = cell_line_data_set_for_export.id"));
    compareTawc.add(new TblInfo("test_result_type", ""));
    compareTawc.add(new TblInfo("uploaded_cell_line_data_set", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("uploaded_ident", "DO NOT REPLICATE"));
    compareTawc.add(new TblInfo("uploaded_test_result", "DO NOT REPLICATE"));
  }

  public static GrantBean dataSystemGrants() {

    GrantBean dsGrants = new GrantBean();

//grant all
    dsGrants.tableGrantSelectInsertUpdate = Arrays.asList(new String[]{
      "aliases2curated_nsc_to_aliases",
      "curated_name",
      "curated_nsc",
      "curated_nsc_to_secondary_targe",
      "curated_nscs2projects",
      "curated_originator",
      "curated_project",
      "curated_target",
      "curated_nsc_seq",
      "curated_name_seq",
      "curated_originator_seq",
      "curated_project_seq",
      "curated_target_seq",
      "cmpd",
      "ad_hoc_cmpd",
      "ad_hoc_cmpd_fragment",
      "ad_hoc_cmpd_fragment_p_chem",
      "ad_hoc_cmpd_fragment_structure",
      "cmpd_list",
      "cmpd_list_member",
      "cmpd_table"});

//grant select
    dsGrants.tableGrantSelect = Arrays.asList(new String[]{
      "cmpd_fragment",
      "cmpd_known_salt",
      "cmpd_legacy_cmpd",
      "cmpd_alias",
      "cmpd_alias_type",
      "cmpd_aliases2nsc_cmpds",
      "cmpd_annotation",
      "cmpd_bio_assay",
      "cmpd_fragment_p_chem",
      "cmpd_fragment_structure",
      "cmpd_fragment_type",
      "cmpd_inventory",
      "cmpd_known_salt",
      "cmpd_named_set",
      "cmpd_named_sets2nsc_cmpds",
      "cmpd_plate",
      "cmpd_plates2nsc_cmpds",
      "cmpd_project",
      "cmpd_projects2nsc_cmpds",
      "cmpd_pub_chem_sid",
      "cmpd_pub_chem_sids2nsc_cmpds",
      "cmpd_related",
      "cmpd_relation_type",
      "cmpd_target",
      "cmpd_targets2nsc_cmpds",
      "nsc_cmpd",
      "nsc_cmpd_type",
      "rdkit_mol"});

    // grant select,update
    dsGrants.tableGrantSelectInsertUpdate = Arrays.asList(new String[]{
      "ad_hoc_cmpd_fragment_p_che_seq",
      "ad_hoc_cmpd_fragment_seq",
      "ad_hoc_cmpd_fragment_struc_seq",
      "cmpd_legacy_cmpd_seq",
      "cmpd_list_member_seq",
      "cmpd_list_seq",
      "cmpd_seq"});

    return dsGrants;

  }

  public static GrantBean compareGrants() {

    GrantBean cGrants = new GrantBean();

    cGrants.tableGrantSelect = Arrays.asList(new String[]{
      "affy_dna_ident",
      "affy_exon_ident",
      "build_date",
      "cell_line_data_sets2named_targ",
      "compare_cell_line",
      "conc_resp_assay",
      "conc_resp_element",
      "dtp_cell_line_data_set",
      "dtp_test_result",
      "hgu133",
      "micro_array_ident",
      "micro_rna_ident",
      "israel_ident",
      "protein_ident",
      "davies_garraway_ident",
      "petricoin_ident",
      "weinstein_croce_ident",
      "mol_targ_ident",
      "mol_targ_catch_all_ident",
      "named_target_set",
      "nano_string_ident",
      "nat_prod_ident",
      "nsc_alias",
      "nsc_compound",
      "nsc_ident",
      "synthetic_ident",
      "test_result_type"});

    cGrants.tableGrantSelectInsertUpdate = Arrays.asList(new String[]{
      "job",
      "grid_compare_job",
      "standard_compare_job"});

    cGrants.tableGrantSelectInsert = Arrays.asList(new String[]{
      "cell_line_data_set",
      "cell_line_data_set_ident",
      "compare_result",
      "grid_compare_columns",
      "grid_compare_rows",
      "ignore_cell_lines2job_for_ign_",
      "job_for_req_cell_lines2require",
      "test_result",
      "uploaded_cell_line_data_set",
      "uploaded_ident",
      "uploaded_test_result"});

    cGrants.sequenceGrantNone = Arrays.asList(new String[]{
      "compare_cell_line_seq",
      "conc_resp_assay_seq",
      "conc_resp_element_seq",
      "hibernate_sequence",
      "named_target_set_seq",
      "nsc_compound_seq",
      "test_result_type_seq"});

    cGrants.sequenceGrantSelectUsage = Arrays.asList(new String[]{
      "cell_line_data_set_seq",
      "cell_line_data_set_ident_seq",
      "compare_result_seq",
      "job_seq",
      "test_result_seq"});

    return cGrants;

  }

}
