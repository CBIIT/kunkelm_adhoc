/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package legacyimageservlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;
import javassist.bytecode.ByteArray;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mwk.datasystem.util.HelperCmpdLegacyCmpd;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;

/**
 *
 * @author mwkunkel
 */
public class LegacyImageServlet extends HttpServlet {

    private byte[] getTextImage(String incomingText) {

        byte[] rtn = new byte[0];

        int referenceSize = 20;

        int strLength = incomingText.length() * referenceSize;

        try {

            BufferedImage bufferedImg = new BufferedImage(200, 200, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bufferedImg.createGraphics();

            g2.setPaint(Color.GRAY);
            g2.fill(new Rectangle.Double(0, 0, 200, 200));

            Font font = new Font("sans-serif", Font.PLAIN, referenceSize);
            g2.setFont(font);
            g2.setPaint(Color.RED);

            g2.drawString(incomingText, referenceSize, referenceSize);

            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            ImageIO.write(bufferedImg, "png", baos);

            rtn = baos.toByteArray();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String nscString = null;

        List<String> smilesList = null;

        if (request.getParameter("nsc") != null) {
            nscString = request.getParameter("nsc");
        }

        // fail by default
        Integer nscInt = null;

        if (nscString != null && nscString.length() > 0) {
            try {
                nscInt = Integer.parseInt(nscString);
            } catch (Exception e) {

            }
        }

        // fail by default
        byte[] byteArray = getTextImage("NSC not an Integer or no structure for NSC");

        if (nscInt != null) {

            HelperCmpdLegacyCmpd helper = new HelperCmpdLegacyCmpd();
            CmpdLegacyCmpdVO rtn = helper.getLegacyCmpdByNsc(nscInt, "PUBLIC");

            // have to catch nulls and empties...
            if (rtn != null && rtn.getJpg512() != null && rtn.getJpg512().length > 0) {
                byteArray = rtn.getJpg512();
            }

        }

        response.setContentType("image/png");
        response.setContentLength(byteArray.length);
        response.setHeader("Content-Disposition", "inline; filename=legacyStructure.png");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");

        response.getOutputStream().write(byteArray);

        response.getOutputStream().flush();
        response.getOutputStream().close();

    }

// <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
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
     * Handles the HTTP <code>POST</code> method.
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
