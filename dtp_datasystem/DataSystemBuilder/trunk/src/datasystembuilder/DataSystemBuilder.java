/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasystembuilder;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
/*
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * 
 * Convert dative bonds: e.g., [N+]([O-])=O to N(=O)=O .
 */

/**
 *
 * @author mwkunkel
 */
public class DataSystemBuilder {

  public static void main(String[] args) {

//        Connection oracleConn = null;
    Connection postgresConn = null;
    Connection postgresInsertConn = null;

    try {

//           DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
      DriverManager.registerDriver(new org.postgresql.Driver());

//            oracleConn = DriverManager.getConnection("jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523:prod", "ops$kunkel", "donkie");            
//            oracleConn = DriverManager.getConnection("jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");      
      
      postgresConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dctddb", "mwkunkel", "donkie11");
      postgresInsertConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/dctddb", "mwkunkel", "donkie11");

      // MUST DISABLE AUTOCOMMIT for fetchForward cursor (batch fetches) to work
      // EACH ResultSet MUST also have fetchDirection set FETCH_FORWARD
      
//          oracleConn.setAutoCommit(false);
      postgresConn.setAutoCommit(false);
      postgresInsertConn.setAutoCommit(false);

//            initial fetches from prod
//            FetchFromProd.fetchCmpd(postgresConn, oracleConn);
//            FetchFromProd.fetchStrc(postgresConn, oracleConn);
//            FetchFromProd.createCmpdTable(postgresConn);
//            
//            FetchFromProd.fetchChemNames(postgresConn, oracleConn);
//            FetchFromProd.fetchInventory(postgresConn, oracleConn);
//            FetchFromProd.fetchPubChemId();
//      
//            BioData
//            FetchBioDataCounts.fetchBioDataCounts(postgresConn, oracleConn);
//      
//            PlatedSets and Projects
//            FetchProjectsAndSets.fetchPlatedSets(postgresConn, oracleConn);
//            FetchProjectsAndSets.fetchProjects(postgresConn, oracleConn);
//          
      // salts first so that join can be run while processing fragments  
      SaltsAndRelated.prepareSaltDefinitions(postgresConn, postgresInsertConn);

//      NewParseAndFragment npaf = new NewParseAndFragment();
//      npaf.processFragments(postgresConn, postgresInsertConn);

      // ParseAndFragment.processFragments(postgresConn, postgresInsertConn);
      // InsertToRDKit.processFragmentsToRdkit(postgresConn, postgresInsertConn);
      // SaltsAndRelated.doRelated(postgresConn, postgresInsertConn);
      //
      // STRIPPING STEREO IS NO LONGER NEEDED RDKit_12_1
      // InsertToRDKit.processFragmentsStripStereo(postgresConn, postgresInsertConn);
      //
      // processing salts via string matching on smiles
    } catch (Exception e) {
      System.out.println("Caught Exception " + e + " in Main.main");
      e.printStackTrace();
    } finally {
//            if (oracleConn != null) {
//                try {
//                    oracleConn.close();
//                    oracleConn = null;
//                } catch (SQLException ex) {
//                    System.out.println("Error in closing oracleConn");
//                }
//            }
      if (postgresConn != null) {
        try {
          postgresConn.close();
          postgresConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresConn");
        }
      }

      if (postgresInsertConn != null) {
        try {
          postgresInsertConn.close();
          postgresInsertConn = null;
        } catch (SQLException ex) {
          System.out.println("Error in closing postgresInsertConn");
        }
      }
    }
  }
}
