/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.faces.event.PhaseId;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class ApplicationScopeBean implements Serializable {

  private static final long serialVersionUID = 4105761574565272777L;
  //
  private List<SelectItem> panelNameItems;
  private List<SelectItem> cellLineItems;
  private List<SelectItem> geneItems;
  private List<SelectItem> geneNameItems;
  //

  public ApplicationScopeBean() {
  }

  public StreamedContent getTextImage() {

    int referenceSize = 20;

    StreamedContent rtn = new DefaultStreamedContent();

    FacesContext context = FacesContext.getCurrentInstance();

    System.out.println("In getTextImage with text");

    if (context.getCurrentPhaseId() == PhaseId.RENDER_RESPONSE) {

      // So, we're rendering the HTML. Return a stub StreamedContent so that it will generate right URL.
      return new DefaultStreamedContent();

    } else {

      String columnHeaderText = context.getExternalContext().getRequestParameterMap().get("columnHeaderText");

      System.out.println("columnHeaderText is: " + columnHeaderText);

      int strLength = columnHeaderText.length() * referenceSize;

      try {

        BufferedImage bufferedImg = new BufferedImage(referenceSize, strLength, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = bufferedImg.createGraphics();

        g2.setPaint(Color.white);
        g2.fill(new Rectangle2D.Double(0, 0, referenceSize, strLength));

        Font font = new Font("sans-serif", Font.PLAIN, referenceSize);
        g2.setFont(font);
        g2.setPaint(Color.black);

        g2.translate(referenceSize, strLength);
        g2.rotate(-Math.PI / 2.0);

        g2.drawString(columnHeaderText, 0, 0);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bufferedImg, "png", baos);

                //FileOutputStream fos = new FileOutputStream("/tmp/image" + columnHeaderText + ".png");
        //fos.write(baos.toByteArray());
        //fos.flush();
        //fos.close();
        rtn = new DefaultStreamedContent(new ByteArrayInputStream(baos.toByteArray()), "image/png", columnHeaderText + ".png");

      } catch (Exception e) {
        e.printStackTrace();
      }

    }

    return rtn;

  }

// <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS">
// </editor-fold>
}
