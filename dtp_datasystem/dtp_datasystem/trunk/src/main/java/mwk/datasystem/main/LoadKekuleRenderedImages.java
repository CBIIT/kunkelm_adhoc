/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.main;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;
import javax.imageio.ImageIO;
import mwk.datasystem.domain.CmpdLegacyCmpd;
import mwk.datasystem.domain.CmpdLegacyCmpdImpl;
import mwk.datasystem.util.HelperCmpdLegacyCmpd;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author mwkunkel
 */
public class LoadKekuleRenderedImages {

  public static void main(String[] args) {

    insertLegacyCmpds();

  }

  public static void insertLegacyCmpds() {

    // File path = new File("/home/mwkunkel/DATA_DEPOT_IMPORTANT/KEKULE_GIF_FILES");
    File path = new File("/home/mwkunkel/GIFFILES");

    try {

      File[] files = path.listFiles();

      for (int i = 0; i < files.length; i++) {

        String nscString = null;
        byte[] imageBytes = null;

        File thisFile = files[i];

        if (thisFile.isFile() && thisFile.getName().endsWith("GIF")) {

          nscString = thisFile.getName().replace(".GIF", "");

          System.out.println("processing: " + thisFile.getName() + " for NSC: " + nscString);

          BufferedImage originalImage = ImageIO.read(thisFile);

          ByteArrayOutputStream baos = new ByteArrayOutputStream();
          ImageIO.write(originalImage, "gif", baos);
          baos.flush();
          imageBytes = baos.toByteArray();

        }

        if (nscString != null && imageBytes != null) {
          Integer nscInt = Integer.valueOf(nscString);
          HelperCmpdLegacyCmpd.insertLegacyCmpds(nscInt, imageBytes);
        }
      }

    } catch (NullPointerException npe) {
      npe.printStackTrace();
    } catch (Exception e) {
      e.printStackTrace();
    } finally {
    }

  }

}
