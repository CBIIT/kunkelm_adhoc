package newstructureservlet;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import org.openscience.cdk.aromaticity.Aromaticity;
import org.openscience.cdk.aromaticity.ElectronDonation;
import org.openscience.cdk.exception.CDKException;
import org.openscience.cdk.exception.InvalidSmilesException;
import org.openscience.cdk.graph.CycleFinder;
import org.openscience.cdk.graph.Cycles;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IChemObjectBuilder;
import org.openscience.cdk.io.IChemObjectReader.Mode;
import org.openscience.cdk.io.MDLReader;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.silent.AtomContainer;
import org.openscience.cdk.silent.SilentChemObjectBuilder;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import uk.ac.cam.ch.wwmm.opsin.NameToStructure;

/**
 * A helper class for loading a single structure instance from various input.
 *
 * @author John May
 * @author MWK mods/extensions
 */
public class MoleculeWrangling {

    private static IChemObjectBuilder bldr = SilentChemObjectBuilder.getInstance();
    private static SmilesParser smipar = new SmilesParser(bldr);
    private static NameToStructure opsin = NameToStructure.getInstance();

    private static Aromaticity aromaticity = new Aromaticity(ElectronDonation.cdk(), Cycles.cdkAromaticSet());

    private static SmilesGenerator kekuleSmiGen = SmilesGenerator.generic();
    private static SmilesGenerator aromaticSmiGen = SmilesGenerator.generic().aromatic();

    public MoleculeWrangling() {
    }

    public static IAtomContainer fromSmiles(String smi) {
        try {
            return layout(smipar.parseSmiles(smi));
        } catch (InvalidSmilesException e) {
            throw new IllegalArgumentException("Invalid smiles:" + e.getMessage());
        }
    }

//    public static IAtomContainer fromInChI(String inchi) {
//        try {
//            InChIGeneratorFactory icipar = InChIGeneratorFactory.getInstance();
//            InChIToStructure itos = icipar.getInChIToStructure(inchi, bldr);
//            if (itos.getReturnStatus() != INCHI_RET.OKAY && itos.getReturnStatus() != INCHI_RET.WARNING) {
//                throw new IllegalArgumentException("Invalid InChI:" + itos.getMessage());
//            }
//            return layout(itos.getAtomContainer());
//        } catch (CDKException e) {
//            throw new IllegalArgumentException("Invalid InChI:" + e.getMessage());
//        }
//    }
    public static IAtomContainer fromName(String name) {
        return fromSmiles(opsin.parseToSmiles(name));
    }

    public static IAtomContainer fromCtabString(String ctab) {
        MDLV2000Reader rdr = null;
        try {

            System.out.println("Trying Mode.RELAXED");

            rdr = new MDLV2000Reader(new ByteArrayInputStream(ctab.getBytes()), Mode.RELAXED);

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

    public static IAtomContainer fromCtabStringMWK(String ctab) {
        MDLReader rdr = null;
        try {

            //rdr = new MDLReader(new ByteArrayInputStream(ctab.getBytes()), Mode.RELAXED);
            rdr = new MDLReader(new ByteArrayInputStream(ctab.getBytes()));

            // return rdr.read(new AtomContainer(0, 0, 0, 0));
            IAtomContainer tempIac = rdr.read(new AtomContainer(0, 0, 0, 0));
            AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(tempIac);
            CDKHydrogenAdder adder = CDKHydrogenAdder.getInstance(SilentChemObjectBuilder.getInstance());
            adder.addImplicitHydrogens(tempIac);

            return tempIac;

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

    public static String toCtab(IAtomContainer iac) {

        String rtn = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        MDLV2000Writer writer = new MDLV2000Writer(baos);

        try {
            writer.write(iac);
            rtn = new String(baos.toByteArray());
        } catch (CDKException cdkE) {
            cdkE.printStackTrace();
        } finally {
            try {
                writer.close();
                baos.close();
            } catch (IOException iOException) {
            }
        }

        return rtn;
    }

    public static String toSmiles(IAtomContainer iac, Boolean aromatic) {

        String rtn = null;

        try {

            if (aromatic) {

                // apply configured model to each molecule
                // the CDK model requires that atom types are perceived
                AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(iac);
                aromaticity.apply(iac);

                rtn = aromaticSmiGen.create(iac);

            } else {

                rtn = kekuleSmiGen.create(iac);
            }

        } catch (CDKException cdkE) {
            cdkE.printStackTrace();
        }

        return rtn;
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

    public static String toSmilesFromCtab(String ctab, Boolean aromatic) {
        String rtn = null;
        IAtomContainer iac = fromCtabStringMWK(ctab);
        rtn = toSmiles(iac, aromatic);
        return rtn;
    }

    public static String toCtabFromSmiles(String smiles) {
        String rtn = null;
        IAtomContainer iac = fromSmiles(smiles);
        rtn = toCtab(iac);
        return rtn;
    }

}
