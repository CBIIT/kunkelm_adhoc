/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package structureservlet;

import com.ggasoftware.indigo.Indigo;
import com.ggasoftware.indigo.IndigoObject;
import com.ggasoftware.indigo.IndigoRenderer;
import java.io.IOException;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author mwkunkel
 */
public class StructureServletIndigo extends HttpServlet {

  public final void renderNsc(Integer nsc, String format, HttpServletResponse response) throws Exception {
    try {

      EntityManager entityManager = Persistence.createEntityManagerFactory("StructureServletPU").createEntityManager();
      Query query = entityManager.createNamedQuery("Structure.findByNsc");
      query.setParameter("nsc", nsc);

      Structure s = null;

      // catch NoResult exception
      try {
        s = (Structure) query.getSingleResult();
      } catch (Exception e) {
        System.out.println(e);
        e.printStackTrace();
      }

      //System.out.println("SMILES for NSC: " + nsc + " is: " + s.getSmiles());

      if (s != null) {

        Indigo indigo = new Indigo();
        IndigoObject mol = indigo.loadMolecule(s.getSmiles());

        mol.layout();
        mol.dearomatize();

        IndigoRenderer renderer = new IndigoRenderer(indigo);

        indigo.setOption("render-image-size", 256, 256);
        indigo.setOption("render-margins", 10, 10);

        indigo.setOption("render-implicit-hydrogens-visible", "true");
        indigo.setOption("render-label-mode", "hetero");
        indigo.setOption("render-coloring", "true");

        if (format.equalsIgnoreCase("png")) {
          indigo.setOption("render-output-format", "png");
        } else if (format.equalsIgnoreCase("svg")) {
          indigo.setOption("render-output-format", "svg");
        } else {
          throw new Exception("Unrecognized format.  Valid formats are png or svg.");
        }

        byte[] byteArray = renderer.renderToBuffer(mol);

        if (format.equalsIgnoreCase("png")) {

          response.setContentType("image/png");
          response.setContentLength(byteArray.length);
          response.setHeader("Content-Disposition", "inline; filename=structure.png");
          response.setHeader("Expires", "0");
          response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
          response.setHeader("Pragma", "public");

          response.getOutputStream().write(byteArray);

        } else if (format.equalsIgnoreCase("svg")) {

          String svgString = new String(byteArray);

          response.setContentType("image/svg+xml");
          response.setContentLength(svgString.length());
          response.setHeader("Content-Disposition", "inline; filesname=structure.svg");
          response.setHeader("Expires", "0");
          response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
          response.setHeader("Pragma", "public");

          response.getOutputStream().print(svgString);


        } else {

          throw new Exception("Unrecognized format.  Valid formats are png or svg.");

        }

      }

      response.getOutputStream().flush();
      response.getOutputStream().close();

    } catch (Exception e) {
      System.out.println("Exception in renderNsc in StructureServlet: " + e);
      System.out.println("NSC being processed: " + nsc);
      e.printStackTrace();
      throw new Exception("Exception in renderNsc in StructureServlet: " + e + " for NSC: " + nsc);
    }

  }

  public final void renderSmiles(String smiles, String format, HttpServletResponse response) throws Exception {

    System.out.println("-------------------------------SMILES at beginning of renderSmiles: " + smiles);

    try {

      Indigo indigo = new Indigo();
      IndigoObject mol = indigo.loadMolecule(smiles);

      mol.layout();
      mol.dearomatize();

      IndigoRenderer renderer = new IndigoRenderer(indigo);

      indigo.setOption("render-image-size", 256, 256);
      indigo.setOption("render-margins", 10, 10);

      indigo.setOption("render-implicit-hydrogens-visible", "true");
      indigo.setOption("render-label-mode", "hetero");
      indigo.setOption("render-coloring", "true");

      if (format.equalsIgnoreCase("png")) {
        indigo.setOption("render-output-format", "png");
      } else if (format.equalsIgnoreCase("svg")) {
        indigo.setOption("render-output-format", "svg");
      } else {
        throw new Exception("Unrecognized format.  Valid formats are png or svg.");
      }

      byte[] byteArray = renderer.renderToBuffer(mol);

      if (format.equalsIgnoreCase("png")) {

        response.setContentType("image/png");
        response.setContentLength(byteArray.length);
        response.setHeader("Content-Disposition", "inline; filename=structure.png");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");

        response.getOutputStream().write(byteArray);

      } else if (format.equalsIgnoreCase("svg")) {

        String svgString = new String(byteArray);

        response.setContentType("image/svg+xml");
        response.setContentLength(svgString.length());
        response.setHeader("Content-Disposition", "inline; filesname=structure.svg");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");

        response.getOutputStream().print(svgString);


      } else {

        throw new Exception("Unrecognized format. Valid formats are png or svg.");

      }

      response.getOutputStream().flush();
      response.getOutputStream().close();

    } catch (Exception e) {
      System.out.println("Exception in renderSmiles in StructureServlet: " + e);
      System.out.println("SMILES being processed: " + smiles);
      e.printStackTrace();
      throw new Exception("Exception in renderSmiles in StructureServlet: " + e + " for SMILES: " + smiles);
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

      String format = request.getParameter("format");
      String smiles = request.getParameter("smiles");

      //System.out.println("SMILES as parsed in processRequest: " + smiles);

      try {
        renderSmiles(smiles, format, response);
      } catch (Exception e) {
      }

    } else if (request.getParameter("nsc") != null) {

      String format = request.getParameter("format");
      Integer nsc = Integer.valueOf(request.getParameter("nsc"));

      try {
        renderNsc(nsc, format, response);
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
  protected void doGet(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
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
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
          throws ServletException, IOException {
    processRequest(request, response);
  }

  /**
   * Returns a short description of the servlet.
   *
   * @return a String containing servlet description
   */
  @Override
  public String getServletInfo() {
    return "Short description";
  }// </editor-fold>
}
