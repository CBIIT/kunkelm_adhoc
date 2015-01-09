/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasystembuilder;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.SDFWriter;

/**
 *
 * @author mwkunkel
 */
public class Main {

    static int BATCH_FETCH_SIZE = 1000;

    public static void main(String[] args) {

        String[] smilesArray = {
            "3124", "Oc1ccc(cc1)N=O",
            "36938", "O=C1C=CC(=NO)C=C1",
            "239647", "O=C1C=CC(=NO)C=C1"
        };


        createSdFile();
        
    }
    
      public static void createSdFile() {

        Connection postgresConn = null;
        Statement postgresStmt = null;

        MDLV2000Reader mdlReader = new MDLV2000Reader();

        try {

            File f = new File("/tmp/twoTautomers.sdf");
            FileOutputStream fos = new FileOutputStream(f);
            SDFWriter sdfw = new SDFWriter(fos);

            FileWriter fw = new FileWriter(f);

            DriverManager.registerDriver(new org.postgresql.Driver());
            postgresConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/datasystemdb", "mwkunkel", "donkie11");
            postgresStmt = postgresConn.createStatement();

            String sqlString = "select nsc, strc from prod_src where nsc in (3124, 36938)";
            ResultSet rs = postgresStmt.executeQuery(sqlString);
            rs.setFetchDirection(ResultSet.FETCH_FORWARD);

            String strc = "";
            String nsc = "";

            int molCnt = 0;

            while (rs.next()) {

                molCnt++;

                try {

                    Molecule mol = new Molecule();

                    strc = rs.getString("strc");
                    nsc = rs.getString("nsc");

                    mdlReader.setReader(new ByteArrayInputStream(strc.getBytes()));
                    mdlReader.read(mol);

                    mol.setProperty("NSC", nsc);

                    sdfw.write(mol);

                    System.out.println("molCnt: " + molCnt + " NSC: " + nsc);

                } catch (Exception e) {
                    System.out.println("ERROR processing strc from prod: molCnt: " + molCnt + " NSC: " + nsc);
                    //e.printStackTrace();
                    continue;
                }
            }

            sdfw.close();
            fos.close();


        } catch (Exception e) {
            e.printStackTrace();
            try {
                postgresStmt.close();
                postgresConn.close();
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }

    }
      
      /*
       * 
       * cactvs tcl script
       * 
set inFileName "/tmp/dtp.sdf"
set outFileName "/tmp/smilesFromCactvs.smi"
set smilesFile [open $outFileName "w"]
prop setparam E_SMILES unique 1
prop set E_NSC origname “NSC”
set fh [molfile open $inFileName r readflags [list aroresolver ignoreempty ignoreerrors]]
molfile loop $fh eh {

	puts "[ens get [ens get [ens create $eh] E_CANONIC_TAUTOMER] E_SMILES] [ens get $eh E_NSC]"
	puts $smilesFile "[ens get [ens get [ens create $eh] E_CANONIC_TAUTOMER] E_SMILES] [ens get $eh E_NSC]"
}
close $smilesFile

       */

   
}