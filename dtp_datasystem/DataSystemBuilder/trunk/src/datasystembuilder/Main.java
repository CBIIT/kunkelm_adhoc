/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 *//*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package datasystembuilder;

import net.sf.jniinchi.INCHI_RET;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.inchi.InChIGenerator;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smiles.SmilesParser;


/**
 *
 * @author mwkunkel
 */
public class Main {

  static int BATCH_FETCH_SIZE = 1000;

  public static void main(String[] args) {
    
    String smilesString = "Cl[H]";
    makeInchi(smilesString);
    
  }

  private static void makeInchi(String smilesString) {

    try {
      
      SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
      IMolecule m = sp.parseSmiles(smilesString);
      
      InChIGenerator gen = InChIGeneratorFactory.getInstance().getInChIGenerator(m);
      
      INCHI_RET ret = gen.getReturnStatus();
      if (ret == INCHI_RET.WARNING) {
        System.out.println("InChI warning: " + gen.getMessage());        
        String inchi = gen.getInchi();
        String inchiAuxInfo = gen.getAuxInfo();
        System.out.println("inchi: " + inchi);
        System.out.println("inchiAuxInfo: " + inchiAuxInfo);
      } else if (ret != INCHI_RET.OKAY) {
        String inchi = "InChiFailed ";
        String inchiAuxInfo = "InChI failed: " + ret.toString() + " [" + gen.getMessage() + "]";
        System.out.println("inchi: " + inchi);
        System.out.println("inchiAuxInfo: " + inchiAuxInfo);
      } else {
        String inchi = gen.getInchi();
        String inchiAuxInfo = gen.getAuxInfo();
        System.out.println("inchi: " + inchi);
        System.out.println("inchiAuxInfo: " + inchiAuxInfo);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
      
  }
}
