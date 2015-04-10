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
/*
 * create table temp 
 as
 select b.*, c.*
 from chem_batch b,chem_one_to_one_with_ctab c
 where b.dtp_nsc = c.dtp_nsc;
 */
/**
 *
 * @author mwkunkel
 */
public class DataSystemBuilder {

    public static void main(String[] args) {

        Connection oracleConn = null;
        Connection postgresConn = null;
        Connection postgresInsertConn = null;

        try {

            DriverManager.registerDriver(new oracle.jdbc.OracleDriver());
            DriverManager.registerDriver(new org.postgresql.Driver());

            oracleConn = DriverManager.getConnection("jdbc:oracle:thin:@dtpiv1.ncifcrf.gov:1523/prod.ncifcrf.gov", "ops$kunkel", "donkie");

            postgresConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");
            postgresInsertConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");

            // MUST DISABLE AUTOCOMMIT for fetchForward cursor (batch fetches) to work
            // EACH ResultSet MUST also have fetchDirection set FETCH_FORWARD
            oracleConn.setAutoCommit(false);
            postgresConn.setAutoCommit(false);
            postgresInsertConn.setAutoCommit(false);

//            initial fetches from prod
            
            FetchFromProd.fetchCmpd(postgresConn, oracleConn);
            
//            FetchFromProd.fetchRS3(postgresConn, oracleConn);
            
            FetchFromProd.fetchKekule(postgresConn, oracleConn);

//            FetchFromProd.fetchStrc(postgresConn, oracleConn);
//            FetchFromProd.createUnifiedProdSrcTable(postgresConn);

//            FetchFromProd.fetchMtxt(postgresConn, oracleConn);
//
//            FetchFromProd.fetchLegacyImage(postgresConn, oracleConn);
//            FetchFromProd.fetchChemNames(postgresConn, oracleConn);
//            FetchFromProd.fetchInventory(postgresConn, oracleConn);
//            FetchFromProd.fetchPubChemId();
//            BioData
//            FetchBioDataCounts.fetchBioDataCounts(postgresConn, oracleConn);
//      
//            PlatedSets and Projects
//            FetchProjectsAndSets.fetchPlatedSets(postgresConn, oracleConn);
//            FetchProjectsAndSets.fetchProjects(postgresConn, oracleConn);
//            SaltsAndRelated.prepareSaltDefinitions(postgresConn, postgresInsertConn);
//
//            NewParseAndFragment npaf = new NewParseAndFragment();
//            npaf.proc XXX essFragments(postgresConn, postgresInsertConn);
//            InsertToRDKit workflow is worked out in Script1_RDKit.sql InsertToRDKit
//            .processFragmentsToRdkit(postgresConn, postgresInsertConn);
//            SaltsAndRelated.doRelated(postgresConn, postgresInsertConn);
//
//            //STRIPPING STEREO IS NO LONGER NEEDED RDKit_12_1
//            InsertToRDKit.processFragmentsStripStereo(postgresConn, postgresInsertConn);
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
