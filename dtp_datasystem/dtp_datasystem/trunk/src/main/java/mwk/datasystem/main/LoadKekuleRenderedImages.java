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

        File path = new File("/home/mwkunkel/DATA_DEPOT_IMPORTANT/KEKULE_GIF_FILES");
        File[] files = new File[]{};

        try {

            files = path.listFiles();

            for (int i = 0; i < files.length; i++) {

                String nscString = null;

                String path512 = null;
                String path340 = null;

                File file512 = null;
                File file340 = null;

                byte[] image512bytes = null;
                byte[] image340bytes = null;

                File thisFile = files[i];

                // modified to search only for the "full-sized" GIF files (512X512)
                if (thisFile.isFile()
                        && thisFile.getName().endsWith("GIF")
                        && !thisFile.getName().contains("TWO_THIRDS")) {

                    nscString = thisFile.getName().replace(".GIF", "");

                    path512 = "/home/mwkunkel/DATA_DEPOT_IMPORTANT/KEKULE_GIF_FILES/" + nscString + ".GIF";
                    path340 = "/home/mwkunkel/DATA_DEPOT_IMPORTANT/KEKULE_GIF_FILES/" + nscString + "_TWO_THIRDS.GIF";

                    file512 = new File(path512);
                    file340 = new File(path340);

                    System.out.println("processing NSC: " + nscString);
//                    System.out.println("full-sized image file: " + path512);
//                    System.out.println("two-thirds-sized image file: " + path340);
//                    System.out.println();

                    BufferedImage img512 = ImageIO.read(file512);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(img512, "gif", baos);
                    baos.flush();
                    image512bytes = baos.toByteArray();

                    BufferedImage img340 = ImageIO.read(file340);
                    baos = new ByteArrayOutputStream();
                    ImageIO.write(img340, "gif", baos);
                    baos.flush();
                    image340bytes = baos.toByteArray();

                }

                if (nscString != null && image512bytes != null && image340bytes != null) {
                    Integer nscInt = Integer.valueOf(nscString);
                    HelperCmpdLegacyCmpd.insertLegacyCmpds(nscInt, image512bytes, image340bytes);
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
