/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.structureservlet.structureservlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mwk.structureservlet.util.HelperStructure;
import mwk.structureservlet.vo.StructureVO;
import org.openscience.cdk.AtomContainer;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IBond;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.RendererModel;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.ExternalHighlightGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.generators.RingGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.smiles.smarts.SMARTSQueryTool;

/**
 *
 * @author mwkunkel
 */
public class StructureServletCDK extends HttpServlet {

    private byte[] getTextImage(String incomingText) {

        byte[] rtn = new byte[0];

        int referenceSize = 20;

        int strLength = incomingText.length() * referenceSize;

        try {

            BufferedImage bufferedImg = new BufferedImage(referenceSize, strLength, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bufferedImg.createGraphics();

            g2.setPaint(Color.white);
            g2.fill(new Rectangle.Double(0, 0, strLength, referenceSize));

            Font font = new Font("sans-serif", Font.PLAIN, referenceSize);
            g2.setFont(font);
            g2.setPaint(Color.black);

            g2.drawString(incomingText, 0, 0);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImg, "png", baos);

            rtn = baos.toByteArray();


        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    private byte[] renderImage(String title, String smiles, String querySmiles, Integer structureDim) throws Exception {

        byte[] byteArray = new byte[0];

        try {

            // parse mol
            SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
            Molecule theMol = (Molecule) sp.parseSmiles(smiles);

//      // fix bonds
//      DeduceBondSystemTool dbst = new DeduceBondSystemTool(new AllRingsFinder());      
//      // the bondOrder tool times out after 10 sec
//      // proceed without it...
//      try{
//      theMol = (Molecule) dbst.fixAromaticBondOrders(theMol);      
//      } catch (Exception e){
//        System.out.println(e);
//      }

            // generate coordinates
            StructureDiagramGenerator sdg = new StructureDiagramGenerator();
            sdg.setMolecule(theMol);

            // NEED TO CATCH org.openscience.cdk.exception.CDKException: Molecule not connected.      
            try {
                sdg.generateCoordinates();
            } catch (Exception e) {
                System.out.println(e);
                System.out.println(smiles);
            }

            theMol = (Molecule) sdg.getMolecule();

            // set up for rendering
            Rectangle drawArea = new Rectangle(structureDim, structureDim);
            Image image = new BufferedImage(structureDim, structureDim, BufferedImage.TYPE_INT_RGB);

            List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();
            generators.add(new BasicSceneGenerator());
            generators.add(new ExternalHighlightGenerator());
            generators.add(new BasicBondGenerator());
            generators.add(new BasicAtomGenerator());
            generators.add(new RingGenerator());

            AtomContainerRenderer renderer = new AtomContainerRenderer(generators, new AWTFontManager());

            renderer.setup(theMol, drawArea);

            IAtomContainer selection = new AtomContainer();

            if (querySmiles != null && querySmiles.length() > 0) {

                SMARTSQueryTool queryTool = new SMARTSQueryTool(querySmiles);
                queryTool.matches(theMol);

                if (queryTool.countMatches() > 0) {
                    List<List<Integer>> mappings = queryTool.getUniqueMatchingAtoms();
                    for (List<Integer> mapping : mappings) {
                        for (Integer i : mapping) {
                            IAtom theAtom = theMol.getAtom(i);
                            selection.addAtom(theAtom);
                        }
                    }

                    // go through theMol and find the bonds that include other selection atoms
                    for (int bondCnt = 0; bondCnt <= theMol.getBondCount() - 1; bondCnt++) {
                        IBond b = theMol.getBond(bondCnt);
                        IAtom a1 = b.getAtom(0);
                        IAtom a2 = b.getAtom(1);
                        if (selection.contains(a1) && selection.contains(a2)) {
                            selection.addBond(b);
                        }
                    }
                }
            }

            // calculate zoom to fit
            Rectangle diagram = renderer.calculateDiagramBounds(theMol);
            renderer.setZoomToFit(drawArea.width, drawArea.height, diagram.width, diagram.height);

            RendererModel model = renderer.getRenderer2DModel();



            if (!selection.isEmpty()) {
                model.set(ExternalHighlightGenerator.ExternalHighlightDistance.class, (double) 12);
                model.set(RendererModel.ExternalHighlightColor.class, Color.PINK);
                model.setExternalSelectedPart(selection);
            }
            Graphics2D g2 = (Graphics2D) image.getGraphics();

            g2.setColor(Color.WHITE);

            g2.fillRect(
                    0, 0, structureDim, structureDim);

            // render title if specified      
            if (title
                    != null && title.length()
                    > 0) {

                int referenceSize = Math.round(structureDim / 20);

                int titleLength = title.length();

                g2.setPaint(Color.white);
                g2.fill(new Rectangle.Double(0, 0, titleLength, referenceSize));

                Font font = new Font("sans-serif", Font.PLAIN, referenceSize);
                g2.setFont(font);
                g2.setPaint(Color.LIGHT_GRAY);

                g2.drawString(title, 0, referenceSize);

            }

            renderer.paint(theMol,
                    new AWTDrawVisitor(g2));

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            ImageIO.write(
                    (RenderedImage) image, "png", baos);
            baos.flush();

            baos.close();
            byteArray = baos.toByteArray();
            return byteArray;
        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception(e);
        }
    }

    protected final void renderNsc(Integer nsc, Integer structureDim, HttpServletResponse response) throws Exception {

        try {

            StringBuilder sb = new StringBuilder();

            HelperStructure helper = new HelperStructure();

            StructureVO sVO = helper.findStructureByNsc(nsc);

            String smiles = null;

            if (sVO.getSmiles() != null && !sVO.getSmiles().isEmpty()) {
                smiles = sVO.getSmiles();
                System.out.println("SMILES for NSC" + nsc + " is: " + smiles);
            } else {
                sb.insert(0, "No structure for NSC" + nsc);
            }

            if (smiles != null && smiles.length() > 0) {

                String title = "NSC" + nsc;
                String querySmiles = "";

                byte[] byteArray = renderImage(title, smiles, querySmiles, structureDim);

                response.setContentType("image/png");
                response.setContentLength(byteArray.length);
                response.setHeader("Content-Disposition", "inline; filename=NSC" + nsc + ".png");
                response.setHeader("Expires", "0");
                response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
                response.setHeader("Pragma", "public");

                response.getOutputStream().write(byteArray);

            } else {

                sb.insert(0, "Unable to create molecule for smiles: " + smiles + " from NSC" + nsc);
                response.sendError(HttpServletResponse.SC_BAD_REQUEST, sb.toString());
            }

            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    protected final void renderSmiles(String title, String smiles, Integer structureDim, HttpServletResponse response) throws Exception {

        try {

            String querySmiles = "";

            byte[] byteArray = renderImage(title, smiles, querySmiles, structureDim);

            response.setContentType("image/png");
            response.setContentLength(byteArray.length);
            response.setHeader("Content-Disposition", "inline; filename=structure.png");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");

            response.getOutputStream().write(byteArray);

            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    protected final void renderSmilesSubstructure(String title, String smiles, String querySmiles, Integer structureDim, HttpServletResponse response) throws Exception {

        try {

            byte[] byteArray = renderImage(title, smiles, querySmiles, structureDim);

            response.setContentType("image/png");
            response.setContentLength(byteArray.length);
            response.setHeader("Content-Disposition", "inline; filename=structure.png");
            response.setHeader("Expires", "0");
            response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
            response.setHeader("Pragma", "public");

            response.getOutputStream().write(byteArray);

            response.getOutputStream().flush();
            response.getOutputStream().close();

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Processes requests for both HTTP
     * <code>GET</code> and
     * <code>POST</code> methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        Integer nsc = null;
        String title = null;
        String smiles = null;
        String querySmiles = null;
        Integer structureDim = null;

        if (request.getParameter("title") != null) {
            title = request.getParameter("title");
        }

        if (request.getParameter("smiles") != null) {
            smiles = request.getParameter("smiles");
        }

        if (request.getParameter("querySmiles") != null) {
            querySmiles = request.getParameter("querySmiles");
        }

        if (request.getParameter("nsc") != null) {
            String nscString = request.getParameter("nsc");
            nsc = Integer.valueOf(nscString);
        }

        if (request.getParameter("structureDim") != null) {
            String structureDimString = request.getParameter("structureDim");
            structureDim = Integer.valueOf(structureDimString);
        }

        if (structureDim == null) {
            structureDim = 200;
        }

        if (smiles != null && smiles.length() > 0) {

            if (querySmiles != null && querySmiles.length() > 0) {
                try {
                    renderSmilesSubstructure(title, smiles, querySmiles, structureDim, response);
                } catch (Exception e) {
                }

            } else {

                try {
                    renderSmiles(title, smiles, structureDim, response);
                } catch (Exception e) {
                }

            }

        } else if (nsc != null) {

            try {
                renderNsc(nsc, structureDim, response);
            } catch (Exception e) {
            }

        } else {

            throw new ServletException("nsc or smiles must be specified");

        }

//        response.setContentType("text/html;charset=UTF-8");
//        PrintWriter out = response.getWriter();
//        try {
//            /* TODO output your page here. You may use following sample code. */
//            out.println("<html>");
//            out.println("<head>");
//            out.println("<title>Servlet StructureServlet</title>");
//            out.println("</head>");
//            out.println("<body>");
//            out.println("<h1>Servlet StructureServlet at " + request.getContextPath() + "</h1>");
//            out.println("</body>");
//            out.println("</html>");
//        } finally {
//            out.close();
//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP
     * <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP
     * <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "PNG graphic image, 200X200, for a specified nsc number or smiles string";
    }// </editor-fold>
}
