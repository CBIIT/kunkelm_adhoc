package newstructureservlet;

import de.erichseifert.vectorgraphics2d.PDFGraphics2D;
import de.erichseifert.vectorgraphics2d.SVGGraphics2D;
import java.awt.Color;
import java.awt.Font;
import org.openscience.cdk.geometry.GeometryUtil;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.elements.Bounds;
import org.openscience.cdk.renderer.elements.ElementGroup;
import org.openscience.cdk.renderer.elements.IRenderingElement;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.renderer.visitor.IDrawVisitor;

import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

/**
 * Holds on to rendering primitives and diagram bounds allow an structure
 * diagram to be generated to several output formats.
 *
 * @author John May
 */
final class Depiction {

    public static final double ACS_1996_BOND_LENGTH_MM = 5.08;
    public static final double ACS_1996_MARGIN_MM = 0.56;

    private final IAtomContainer container;
    private final IRenderingElement primitives;
    private final RendererModel parameters;
    private final double scale, margin;
    private final Rectangle2D diagramBounds;

    Depiction(IRenderingElement primitives, IAtomContainer container, RendererModel params) {
        this.primitives = primitives;
        this.container = container;
        this.parameters = params;
        this.scale = params.get(BasicSceneGenerator.Scale.class);
        this.margin = params.get(BasicSceneGenerator.Margin.class);
        this.diagramBounds = getBounds(primitives, container);
    }

    private Rectangle2D getBounds(IRenderingElement primitives, IAtomContainer container) {
        // does the IRenderingElement contain a bounds hint?
        List<Bounds> listOfBounds = new ArrayList<Bounds>();
        unpackBounds(primitives, listOfBounds);

        double[] xyLimits = new double[4];

        if (listOfBounds.isEmpty()) {
            xyLimits = GeometryUtil.getMinMax(container);
        } else {
            xyLimits[0] = xyLimits[1] = Double.MAX_VALUE;
            xyLimits[2] = xyLimits[3] = Double.MIN_VALUE;
            for (Bounds bounds : listOfBounds) {
                xyLimits[0] = Math.min(xyLimits[0], bounds.minX);
                xyLimits[1] = Math.min(xyLimits[1], bounds.minY);
                xyLimits[2] = Math.max(xyLimits[2], bounds.maxX);
                xyLimits[3] = Math.max(xyLimits[3], bounds.maxY);
            }
        }

        return new Rectangle2D.Double(xyLimits[0],
                xyLimits[1],
                xyLimits[2] - xyLimits[0],
                xyLimits[3] - xyLimits[1]);
    }

    /**
     * Access the width of the diagram.
     *
     * @return diagram width
     */
    double width() {
        return diagramBounds.getWidth() * scale;
    }

    /**
     * Access the height of the diagram.
     *
     * @return diagram width
     */
    double height() {
        return diagramBounds.getHeight() * scale;
    }

    /**
     * Low-level draw method used by other rendering methods.
     *
     * @param visitor the draw visitor
     * @param view the view bounds - the diagram will be centered in the bounds
     */
    void draw(IDrawVisitor visitor, Rectangle2D view) {

        double zoomToFit = Math.min(view.getWidth() / width(),
                view.getHeight() / height());

        AffineTransform transform = new AffineTransform();
        transform.translate(view.getCenterX(), view.getCenterY());
        transform.scale(scale, -scale);
        transform.scale(zoomToFit, zoomToFit);
        transform.translate(-diagramBounds.getCenterX(), -diagramBounds.getCenterY());

        // not always needed
        AWTFontManager fontManager = new AWTFontManager();
        fontManager.setFontForZoom(zoomToFit);

        visitor.setRendererModel(parameters);
        visitor.setFontManager(fontManager);
        visitor.setTransform(transform);

        // setup up transform
        visitor.visit(primitives);
    }

    /**
     * Generate a raster image at default scale.
     *
     * @return The generated image.
     */
    BufferedImage toBufferedImage() {
        return toBufferedImage(1);
    }

    /**
     * Generate a scaled raster image.
     *
     * @param scale resize the image by this amount
     * @return The generated image.
     */
    BufferedImage toBufferedImage(double scale) {
        // MWK modified
        return toBufferedImage(width() * scale, height() * scale, null);
    }

    // private BufferedImage toBufferedImage(double width, double height) {
    // MWK made access public, added String[] for titles
    BufferedImage toBufferedImage(double width, double height, String titleString) {

        BufferedImage img = new BufferedImage((int) (margin + width + margin),
                (int) (margin + height + margin),
                BufferedImage.TYPE_3BYTE_BGR);
        Graphics2D g2 = img.createGraphics();
        g2.setBackground(parameters.get(BasicSceneGenerator.BackgroundColor.class));
        g2.clearRect(0, 0, img.getWidth(), img.getHeight());

        if (titleString != null) {
            String[] titleArray;
            if (titleString.contains("xxx")) {
                titleArray = titleString.split("xxx");
            } else {
                titleArray = new String[1];
                titleArray[0] = titleString;
            }

            final Font font = new Font("Verdana", Font.PLAIN, (int) height / 10);
            g2.setFont(font);
            g2.setPaint(Color.LIGHT_GRAY);

            // set slightly higher than the font size        
            int referenceSize = (int) ((height / 10) + 0.1 * (height / 10));

            for (int tCnt = 0; tCnt < titleArray.length; tCnt++) {
                String curTitle = titleArray[tCnt];
                g2.drawString(curTitle, 0, (tCnt + 1) * referenceSize);
            }
        }

        draw(new AWTDrawVisitor(g2),
                new Rectangle2D.Double(margin, margin, width, height));
        g2.dispose();
        return img;
    }

    /**
     * Convert the depiction to PDF. The depiction is scaled to match the
     * recommended bond length of the ACS 1996 style.
     *
     * @return content of the PDF
     * @see #toPdf(double, double)
     */
    String toPdf() {
        return toPdf(1d);
    }

    /**
     * Convert the depiction to PDF.
     *
     * @param scale resize the image by this amount
     * @return content of the PDF
     * @see #toPdf(double, double)
     */
    String toPdf(double scale) {
        return toPdf(scale * ACS_1996_BOND_LENGTH_MM,
                scale * ACS_1996_MARGIN_MM);
    }

    /**
     * Convert the depiction to PDF. The bond lengths are rescaled to be an
     * appropriate length in 'mm'.
     *
     * @param bondLengthInMM the length of a bond (in mm)
     * @param marginInMM the width of a margin (in mm)
     * @return content of the PDF
     */
    String toPdf(double bondLengthInMM, double marginInMM) {
        final double rescale = rescaleForBondLength(bondLengthInMM);
        return toPdf(width() * rescale, height() * rescale, marginInMM);
    }

    private String toPdf(double width, double height, double margin) {
        Graphics2D g2 = new PDFGraphics2D(0, 0,
                margin + width + margin,
                margin + height + margin);
        draw(AWTDrawVisitor.forVectorGraphics(g2),
                new Rectangle2D.Double(margin, margin, width, height));
        g2.dispose();
        return g2.toString();
    }

    /**
     * Convert the depiction to SVG. The depiction is scaled to match the
     * recommended bond length of the ACS 1996 style.
     *
     * @return content of the SVG
     * @see #toPdf(double, double)
     */
    String toSvg() {
        return toSvg(1d);
    }

    /**
     * Convert the depiction to SVG.
     *
     * @param scale resize the image by this amount
     * @return content of the SVG
     * @see #toPdf(double, double)
     */
    String toSvg(double scale) {
        return toSvg(scale * ACS_1996_BOND_LENGTH_MM,
                scale * ACS_1996_MARGIN_MM);
    }

    /**
     * Convert the depiction to SVG. The bond lengths are rescaled to be an
     * appropriate length in 'mm'.
     *
     * @param bondLengthInMM the length of a bond (in mm)
     * @param marginInMM the width of a margin (in mm)
     * @return content of the SVG
     */
    String toSvg(double bondLengthInMM, double marginInMM) {
        final double rescale = rescaleForBondLength(bondLengthInMM);
        return toSvg(width() * rescale, height() * rescale, marginInMM);
    }

    private String toSvg(double width, double height, double margin) {
        Graphics2D g2 = new SVGGraphics2D(0, 0,
                margin + width + margin,
                margin + height + margin);
        draw(AWTDrawVisitor.forVectorGraphics(g2),
                new Rectangle2D.Double(margin, margin, width, height));
        g2.dispose();
        return g2.toString();
    }

    private double rescaleForBondLength(double length) {
        return length / parameters.get(BasicSceneGenerator.BondLength.class);
    }

    // helper method traverses the nested rendering elements and finds all 'Bounds' instances
    private static void unpackBounds(IRenderingElement element, List<Bounds> listOfBounds) {
        if (element instanceof ElementGroup) {
            for (IRenderingElement child : (ElementGroup) element) {
                unpackBounds(child, listOfBounds);
            }
        } else if (element instanceof Bounds) {
            listOfBounds.add((Bounds) element);
        }
    }
}
