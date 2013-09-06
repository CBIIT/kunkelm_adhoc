package edu.scripps.fl.test;

import java.awt.Color;
import java.awt.Desktop;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.generators.IGeneratorParameter;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;

import edu.scripps.fl.cdk.CDKFiles;

public class ImageTest {

    public static void main(String[] args) throws Exception {
        int WIDTH = 600;
        int HEIGHT = 600;

        // the draw area and the image should be the same size
        Rectangle drawArea = new Rectangle(WIDTH, HEIGHT);
        Image image = new BufferedImage(
                WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);

//		IMolecule molecule = MoleculeFactory.make123Triazole();
        IMolecule molecule = CDKFiles.getPubChemMolecule("2519", "cid");
        StructureDiagramGenerator sdg = new StructureDiagramGenerator();
        sdg.setMolecule(molecule);
        sdg.generateCoordinates();
        molecule = sdg.getMolecule();

        // generators make the image elements
        List generators = new ArrayList();
        generators.add(new BasicSceneGenerator());
        generators.add(new BasicBondGenerator());
        generators.add(new BasicAtomGenerator());

        // the renderer needs to have a toolkit-specific font manager
        AtomContainerRenderer renderer = new AtomContainerRenderer(generators, new AWTFontManager());

        RendererModel model = renderer.getRenderer2DModel();
        model.getParameter(BasicSceneGenerator.FitToScreen.class).setValue(Boolean.TRUE);


        // the call to 'setup' only needs to be done on the first paint
        renderer.setup(molecule, drawArea);
        dumpParameters(System.out, renderer);

        // paint the background
        Graphics2D g2 = (Graphics2D) image.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, WIDTH, HEIGHT);

        // the paint method also needs a toolkit-specific renderer
        renderer.paint(molecule, new AWTDrawVisitor(g2));

        File file = File.createTempFile("cmp-", ".png");
        ImageIO.write((RenderedImage) image, "PNG", file);
        Desktop.getDesktop().open(file);
    }

    public static void dumpParameters(PrintStream os, AtomContainerRenderer renderer) {
        for (IGeneratorParameter parameter : renderer.getRenderer2DModel().getRenderingParameters()) {
            os.println(parameter.getClass().getName() + " -> " + parameter.getValue());
        }
    }
}