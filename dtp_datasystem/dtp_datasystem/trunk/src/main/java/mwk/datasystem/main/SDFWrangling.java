/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.main;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.io.IChemObjectReader;
import org.openscience.cdk.io.SDFWriter;
import org.openscience.cdk.io.iterator.IteratingSMILESReader;
import org.openscience.cdk.layout.StructureDiagramGenerator;

/**
 *
 * @author mwkunkel
 */
public class SDFWrangling {

  public static final Boolean DEBUG = Boolean.TRUE;

  public static void main(String[] args) {

    try {

      File sdFile = new File("/tmp/sdFile.sdf");
      FileOutputStream fos = new FileOutputStream(sdFile);
      SDFWriter sdWriter = new SDFWriter(fos);

      File smilesFile = new File("/tmp/smilesFile.smi");
      FileInputStream fis = new FileInputStream(smilesFile);

      IteratingSMILESReader smiReader = new IteratingSMILESReader(fis, DefaultChemObjectBuilder.getInstance());

      smiReader.setReaderMode(IChemObjectReader.Mode.RELAXED);

      int countMol = 0;

      while (smiReader.hasNext()) {

        IAtomContainer mol = smiReader.next();
        countMol++;
        System.out.println("Processing countMol: " + countMol);

        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(mol);
        sdg.generateCoordinates();
        IAtomContainer molCoord = sdg.getMolecule();
        sdWriter.write(molCoord);
        
      }

      System.out.println("Processed: " + countMol + " molecules");

      smiReader.close();
      sdWriter.close();

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

}
