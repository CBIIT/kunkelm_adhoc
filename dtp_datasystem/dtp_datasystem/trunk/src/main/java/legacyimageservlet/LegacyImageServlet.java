
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package legacyimageservlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import javassist.bytecode.ByteArray;
import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import mwk.datasystem.util.HelperCmpdLegacyCmpd;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;
import org.openscience.cdk.renderer.font.AWTFontManager;
import org.openscience.cdk.renderer.visitor.AWTDrawVisitor;
import org.openscience.cdk.renderer.visitor.IDrawVisitor;

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

            BufferedImage bufferedImg = new BufferedImage(512, 512, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = bufferedImg.createGraphics();

            g2.setPaint(Color.GRAY);
            g2.fill(new Rectangle.Double(0, 0, 512, 512));

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
        Integer nscInt = null;
        byte[] rtnByteArray = null;

        if (request.getParameter("nsc") != null) {
            nscString = request.getParameter("nsc");
            if (nscString != null && nscString.length() > 0) {
                try {
                    nscInt = Integer.parseInt(nscString);
                } catch (Exception e) {
                    rtnByteArray = getTextImage(nscString + " is not an Integer");
                }
            }
        } else {
            rtnByteArray = getTextImage("NSC was not specified");
        }

        if (nscInt != null) {
           
            CmpdLegacyCmpdVO rtn = HelperCmpdLegacyCmpd.getLegacyCmpdByNsc(nscInt, "PUBLIC");
           
            if (rtn != null && rtn.getJpg512() != null && rtn.getJpg512().length > 0) {
               
                byte[] byteArray = rtn.getJpg512();

                ByteArrayInputStream is = new ByteArrayInputStream(byteArray);
                BufferedImage imgFromDb = ImageIO.read(is);
               
                int height = imgFromDb.getHeight();
                int width = imgFromDb.getWidth();
               
                System.out.println("bi.height" + imgFromDb.getHeight());
                System.out.println("bi.width" + imgFromDb.getWidth());
               
                BufferedImage rtnImage = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);               
                Graphics2D g2 = rtnImage.createGraphics();
                g2.drawImage(imgFromDb, 0, 0, null);
               
                g2.setPaint(Color.LIGHT_GRAY);
                Font font = new Font("Verdana", Font.PLAIN, (int) height / 10);
                g2.setFont(font);
               
                // set slightly higher than the font size       
                int referenceSize = (int) ((height / 10) + 0.1 * (height / 10));

                String[] titleArray = new String[]{"NSC: " + nscInt.toString()};

                for (int tCnt = 0; tCnt < titleArray.length; tCnt++) {
                    String curTitle = titleArray[tCnt];
                    g2.drawString(curTitle, 0, (tCnt + 1) * referenceSize);
                }

                g2.dispose();
               
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                ImageIO.write(rtnImage, "jpg", baos);
                baos.flush();

                rtnByteArray = baos.toByteArray();

                baos.close();

            } else {
                rtnByteArray = getTextImage("No legacy image for NSC: " + nscInt);
            }
        }

        // add the NSC as a title
        response.setContentType("image/png");
        response.setContentLength(rtnByteArray.length);
        response.setHeader("Content-Disposition", "inline; filename=legacyStructure.png");
        response.setHeader("Expires", "0");
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setHeader("Pragma", "public");

        response.getOutputStream().write(rtnByteArray);

        response.getOutputStream().flush();
        response.getOutputStream().close();

    }
   
    /*
   
    http://stackoverflow.com/questions/2658554/using-graphics2d-to-overlay-text-on-a-bufferedimage-and-return-a-bufferedimage
   
     private BufferedImage process(BufferedImage old) {
        int w = old.getWidth();
        int h = old.getHeight();
        BufferedImage img = new BufferedImage(
                w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = img.createGraphics();
        g2d.drawImage(old, 0, 0, null);
        g2d.setPaint(Color.red);
        g2d.setFont(new Font("Serif", Font.BOLD, 20));
        String s = "Hello, world!";
        FontMetrics fm = g2d.getFontMetrics();
        int x = img.getWidth() - fm.stringWidth(s) - 5;
        int y = fm.getHeight();
        g2d.drawString(s, x, y);
        g2d.dispose();
        return img;
    }

    */

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
        return "Legacy Kekule-rendered image by NSC.";
    }// </editor-fold>
}
