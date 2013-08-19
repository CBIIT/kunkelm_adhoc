/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structureservlet;

import java.awt.Color;
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
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openscience.cdk.CDKConstants;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.renderer.AtomContainerRenderer;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.generators.BasicAtomGenerator;
import org.openscience.cdk.renderer.generators.BasicBondGenerator;
import org.openscience.cdk.renderer.generators.BasicSceneGenerator;
import org.openscience.cdk.renderer.generators.IGenerator;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.smiles.SmilesParser;

/**
 *
 * @author mwkunkel
 */
public class StructureServlet extends HttpServlet {

  private byte[] renderImage(IMolecule molecule, int width, int height) throws Exception {

    Rectangle drawArea = new Rectangle(width, height);

    Image image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

    StructureDiagramGenerator sdg = new StructureDiagramGenerator();
    sdg.setMolecule(molecule);
    sdg.generateCoordinates();
    molecule = sdg.getMolecule();

    List<IGenerator<IAtomContainer>> generators = new ArrayList<IGenerator<IAtomContainer>>();
    generators.add(new BasicSceneGenerator());
    generators.add(new BasicBondGenerator());
    generators.add(new BasicAtomGenerator());

    AtomContainerRenderer renderer = new AtomContainerRenderer(generators, new AWTFontManager());

    /*
     RendererModel model = renderer.getRenderer2DModel();
     List rendParam = model.getRenderingParameters();
     for (int i = 0; i < rendParam.size(); i++) {
     IGeneratorParameter igp = (IGeneratorParameter) rendParam.get(i);
     System.out.println(igp.getClass().getName() + " " + igp.getValue().toString());
     }
     */

    renderer.setup(molecule, drawArea);

    //renderer.setScale(molecule);
    //renderer.setZoomToFit(width, height, 0.75 * width, 0.75 * height);

    renderer.setZoom(0.50);

    Graphics2D g2 = (Graphics2D) image.getGraphics();
    g2.setColor(Color.WHITE);
    g2.fillRect(0, 0, width, height);
    renderer.paint(molecule, new AWTDrawVisitor(g2));

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    ImageIO.write((RenderedImage) image, "png", baos);

    baos.flush();
    baos.close();

    return baos.toByteArray();
  }

  protected final void renderNsc(Integer nsc, HttpServletResponse response) throws Exception {

    try {

      EntityManager entityManager = Persistence.createEntityManagerFactory("StructureServletPU").createEntityManager();
      Query query = entityManager.createNamedQuery("Structure.findByNsc");
      query.setParameter("nsc", nsc);

      Structure s = null;
      List resultList = null;
      Molecule queryMolecule = null;

      StringBuffer sb = new StringBuffer();

      resultList = query.getResultList();

      if (resultList != null & resultList.size() > 0) {

        s = (Structure) resultList.get(0);
        System.out.println("SMILES for NSC" + nsc + " is: " + s.getSmiles());
        SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
        queryMolecule = (Molecule) sp.parseSmiles(s.getSmiles());

      } else {

        sb.insert(0, "No structure for NSC" + nsc);

      }

      if (queryMolecule != null) {

        queryMolecule.setProperty(CDKConstants.TITLE, nsc.toString());

        byte[] byteArray = renderImage(queryMolecule, 200, 200); //0.4, 150, 150);

        response.setContentType("image/png");
        response.setContentLength(byteArray.length);
        response.setHeader("Content-Disposition", "inline; filename=NSC" + nsc + ".png");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");

        response.getOutputStream().write(byteArray);

      } else {

        sb.insert(0, "Unable to create molecule for smiles: " + s.getSmiles() + " from NSC" + nsc);
        response.sendError(HttpServletResponse.SC_BAD_REQUEST, sb.toString());
      }

      response.getOutputStream().flush();
      response.getOutputStream().close();

    } catch (Exception e) {
      System.out.println("Unexpected exception in renderNsc in StructureServlet: " + e + " for NSC" + nsc);
      throw new Exception("Unexpected exception in renderNsc in StructureServlet: " + e + " for NSC" + nsc);
    }
  }

  protected final void renderSmiles(String smiles, String title, HttpServletResponse response) throws Exception {

    System.out.println("-------------------------------SMILES at beginning of renderSmiles: " + smiles);

    if (title == null || title.length() == 0) {
      title = "noTitle";
    }

    try {

      SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
      Molecule queryMolecule = (Molecule) sp.parseSmiles(smiles);

      if (queryMolecule != null) {

        queryMolecule.setProperty(CDKConstants.TITLE, title);

        byte[] byteArray = renderImage(queryMolecule, 200, 200); //0.4, 150, 150);

        response.setContentType("image/png");
        response.setContentLength(byteArray.length);
        response.setHeader("Content-Disposition", "inline; filename=structure.png");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");

        response.getOutputStream().write(byteArray);

      } else {

        response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Can't create molecule from SMILES " + smiles);
      }

      response.getOutputStream().flush();
      response.getOutputStream().close();

    } catch (Exception e) {
      System.out.println("Unexpected exception in renderSmiles in StructureServlet: " + e + " for SMILES: " + smiles);
      throw new Exception("Unexpected exception in renderSmiles in StructureServlet: " + e + " for SMILES: " + smiles);
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

    if (request.getParameter("smiles") != null) {

      if (request.getParameter("smiles").length() == 0) {
        throw new ServletException("smiles cannot be zero length");
      }

      String smiles = request.getParameter("smiles");

      //System.out.println("SMILES as parsed in processRequest: " + smiles);

      try {
        renderSmiles(smiles, "smilesString", response);
      } catch (Exception e) {
      }

    } else if (request.getParameter("nsc") != null) {

      Integer nsc = Integer.valueOf(request.getParameter("nsc"));

      try {
        renderNsc(nsc, response);
      } catch (Exception e) {
      }

    } else {

      throw new ServletException("nsc or smiles must be specified");

    }

//
//
//
//
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
