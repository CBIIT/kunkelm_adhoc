/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

//        File[] files = new File[]{
//            new File("172122.GIF"),
//            new File("232822.GIF"),
//            new File("296108.GIF"),
//            new File("369447.GIF"),
//            new File("600300.GIF"),
//            new File("663277.GIF"),
//            new File("742310.GIF"),
//            new File("904466.GIF"),
//            new File("914041.GIF"),
//            new File("923547.GIF"),
//            new File("99959.GIF"),
//            new File("172127.GIF"),
//            new File("232874.GIF"),
//            new File("296109.GIF"),
//            new File("369449.GIF"),
//            new File("600301.GIF"),
//            new File("663279.GIF"),
//            new File("742311.GIF"),
//            new File("904467.GIF"),
//            new File("914042.GIF"),
//            new File("923548.GIF"),
//            new File("9995.GIF")
//        };

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

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

    }

}
