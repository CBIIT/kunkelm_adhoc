package newstructureservlet;

import java.io.ByteArrayInputStream;
import net.sf.jniinchi.INCHI_RET;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.inchi.InChIGeneratorFactory;
import org.openscience.cdk.inchi.InChIToStructure;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesParser;
import uk.ac.cam.ch.wwmm.opsin.NameToStructure;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A helper class for loading a single structure instance from various input.
 *
 * @author John May
 */
public class MolInput {

  private static IChemObjectBuilder bldr = SilentChemObjectBuilder.getInstance();
  private static SmilesParser smipar = new SmilesParser(bldr);
  private static NameToStructure opsin = NameToStructure.getInstance();

  public MolInput() {
  }

  public static IAtomContainer fromSmiles(String smi) {
    try {
      return layout(smipar.parseSmiles(smi));
    } catch (InvalidSmilesException e) {
      throw new IllegalArgumentException("Invalid smiles:" + e.getMessage());
    }
  }

  public static IAtomContainer fromInChI(String inchi) {
    try {
      InChIGeneratorFactory icipar = InChIGeneratorFactory.getInstance();
      InChIToStructure itos = icipar.getInChIToStructure(inchi, bldr);
      if (itos.getReturnStatus() != INCHI_RET.OKAY && itos.getReturnStatus() != INCHI_RET.WARNING) {
        throw new IllegalArgumentException("Invalid InChI:" + itos.getMessage());
      }
      return layout(itos.getAtomContainer());
    } catch (CDKException e) {
      throw new IllegalArgumentException("Invalid InChI:" + e.getMessage());
    }
  }

  public static IAtomContainer fromName(String name) {
    return fromSmiles(opsin.parseToSmiles(name));
  }

  public static IAtomContainer fromCtabString(String ctab) {
    MDLV2000Reader rdr = null;
    try {
      rdr = new MDLV2000Reader(new ByteArrayInputStream(ctab.getBytes()));
      return rdr.read(new AtomContainer(0, 0, 0, 0));
    } catch (CDKException e) {
      e.printStackTrace();
      throw new IllegalArgumentException("Could not read molfile:" + e.getMessage());
    } finally {
      try {
        if (rdr != null) {
          rdr.close();
        }
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  public static IAtomContainer fromMolfile(String path) {
    MDLV2000Reader rdr = null;
    try {
      rdr = new MDLV2000Reader(new FileReader(path));
      return rdr.read(new AtomContainer(0, 0, 0, 0));
    } catch (FileNotFoundException e) {
      throw new IllegalArgumentException("No such file:" + e.getMessage());
    } catch (CDKException e) {
      throw new IllegalArgumentException("Could not read molfile:" + e.getMessage());
    } finally {
      try {
        if (rdr != null) {
          rdr.close();
        }
      } catch (IOException e) {
        System.err.println(e.getMessage());
      }
    }
  }

  public static IAtomContainer layout(IAtomContainer container) {
    StructureDiagramGenerator sdg = new StructureDiagramGenerator();
    sdg.setMolecule(container, false);
    sdg.setUseTemplates(false);
    try {
      sdg.generateCoordinates();
    } catch (CDKException e) {
      throw new IllegalArgumentException("Could not layout molecule");
    }
    return container;
  }

}
