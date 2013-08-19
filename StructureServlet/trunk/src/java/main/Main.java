/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package main;

import java.util.List;
import java.awt.*;
import java.awt.image.*;
import java.io.File;
import java.util.ArrayList;
import java.util.zip.GZIPInputStream;
import org.openscience.cdk.*;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.io.*;
import org.openscience.cdk.io.iterator.*;
import org.openscience.cdk.layout.*;
import org.openscience.cdk.renderer.generators.*;
import org.openscience.cdk.silent.*;
import org.openscience.cdk.smiles.smarts.*;
import org.openscience.cdk.tools.manipulator.*;

/**
 *
 * @author mwkunkel
 */
public class Main {

    public static void main(String[] args) {
    }
    

//    public void highlightSubstructure() {
//
//        int WIDTH = 250;
//        int HEIGHT = 200;
//
//// the draw area and the image should be the same size
//        Rectangle drawArea = new Rectangle(WIDTH, HEIGHT);
//        Image image = new BufferedImage(
//                WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
//        IteratingMDLReader iterator = new IteratingMDLReader(
//                new GZIPInputStream(
//                new File("ctr/benzodiazepine.sdf.gz").newInputStream()), SilentChemObjectBuilder.getInstance());
//        iterator.setReaderMode(IChemObjectReader.Mode.STRICT);
//
//        Molecule compound3016 = null;
//        while (iterator.hasNext() && compound3016 == null) {
//            Molecule mol = (Molecule) iterator.next();
//            if ("3016".equals(mol.getProperty(CDKConstants.TITLE))) {
//                compound3016 = mol;
//            }
//        }
//        compound3016 = AtomContainerManipulator.removeHydrogens(compound3016);
//        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
//        sdg.setMolecule(compound3016);
//        sdg.generateCoordinates();
//        compound3016 = (Molecule) sdg.getMolecule();
//// generators make the image elements
//        List generators = new ArrayList();
//        generators.add(new BasicSceneGenerator());
//        generators.add(new ExternalHighlightGenerator());
//        generators.add(new BasicBondGenerator());
//        generators.add(new BasicAtomGenerator());
//        selection = new AtomContainer();
//        SMARTSQueryTool querytool = new SMARTSQueryTool("c1ccc2c(c1)C(=NCCN2)c3ccccc3");
//        querytool.matches(compound3016);
//        if (querytool.countMatches() > 0) {
//            List<List<Integer>> mappings = querytool.getUniqueMatchingAtoms();
//            List<Integer> mapping = mappings.get(0);
//            for (int i = 0; i  {
//            }
//        }
}