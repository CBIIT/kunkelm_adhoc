/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.CmpdLegacyCmpd;
import mwk.datasystem.domain.CmpdLegacyCmpdImpl;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;
import newstructureservlet.MolInput;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.openscience.cdk.interfaces.IAtomContainer;

/**
 *
 * @author mwkunkel
 */
public class Main {

  public static void main(String[] args) {

    // insertLegacyCmpds();
    // testCtabParse();
    testMoleculeParser();

  }

  public static void testMoleculeParser() {
    MoleculeParser mp = new MoleculeParser();

    File sdFile = new File("/home/mwkunkel/tiny.sdf");

    ArrayList<AdHocCmpd> ahcList = mp.parseSDF(sdFile);

  }

  public static void testCtabParse() {

    String ctab = "Molecule from ChemDoodle Web Components" + "\n"
            + "" + "\n"
            + "http://www.ichemlabs.com" + "\n"
            + " 16 18  0  0  0  0            999 V2000" + "\n"
            + "   -4.4550    2.3176    0.0000 C   0  0  0  0  0  0" + "\n"
            + "   -4.4550    0.8225    0.0000 C   0  0  0  0  0  0" + "\n"
            + "   -3.1423    0.0568    0.0000 C   0  0  0  0  0  0" + "\n"
            + "   -1.8478    0.8225    0.0000 C   0  0  0  0  0  0" + "\n"
            + "   -1.8478    2.3176    0.0000 C   0  0  0  0  0  0" + "\n"
            + "   -3.1423    3.0833    0.0000 C   0  0  0  0  0  0" + "\n"
            + "   -0.4257    0.3668    0.0000 N   0  0  0  0  0  0" + "\n"
            + "    0.4494    1.5883    0.0000 C   0  0  0  0  0  0" + "\n"
            + "   -0.4257    2.7733    0.0000 C   0  0  0  0  0  0" + "\n"
            + "    0.0460   -1.0553    0.0000 C   0  0  0  0  0  0" + "\n"
            + "    1.5157   -1.3593    0.0000 C   0  0  0  0  0  0" + "\n"
            + "    1.9900   -2.7824    0.0000 C   0  0  0  0  0  0" + "\n"
            + "    3.4596   -3.0832    0.0000 C   0  0  0  0  0  0" + "\n"
            + "    4.4550   -1.9609    0.0000 C   0  0  0  0  0  0" + "\n"
            + "    3.9806   -0.5379    0.0000 C   0  0  0  0  0  0" + "\n"
            + "    2.5112   -0.2371    0.0000 C   0  0  0  0  0  0" + "\n"
            + "  1  22  0  0" + "\n"
            + "  1  61  0  0" + "\n"
            + "  2  31  0  0" + "\n"
            + "  3  42  0  0" + "\n"
            + "  4  51  0  0" + "\n"
            + "  4  71  0  0" + "\n"
            + "  5  62  0  0" + "\n"
            + "  5  91  0  0" + "\n"
            + "  7  81  0  0" + "\n"
            + "  7 101  0  0" + "\n"
            + "  8  92  0  0" + "\n"
            + " 10 111  0  0" + "\n"
            + " 11 122  0  0" + "\n"
            + " 11 161  0  0" + "\n"
            + " 12 131  0  0" + "\n"
            + " 13 142  0  0" + "\n"
            + " 14 151  0  0" + "\n"
            + " 15 162  0  0" + "\n"
            + "M  END";

    String ctab2 = " SciTegic01131513202D" + "\n"
            + "" + "\n"
            + "" + "\n"
            + " 19 21  0  0  0  0            999 V2000" + "\n"
            + "   -2.3155    0.7475    0.0000 C   0  0" + "\n"
            + "   -2.3155   -0.7475    0.0000 C   0  0" + "\n"
            + "   -1.0028   -1.5132    0.0000 C   0  0" + "\n"
            + "    0.2917   -0.7475    0.0000 C   0  0" + "\n"
            + "    0.2917    0.7475    0.0000 C   0  0" + "\n"
            + "   -1.0028    1.5132    0.0000 C   0  0" + "\n"
            + "    1.7138   -1.2033    0.0000 N   0  0" + "\n"
            + "    2.5889    0.0182    0.0000 C   0  0" + "\n"
            + "    1.7138    1.2033    0.0000 C   0  0" + "\n"
            + "    2.1812    2.6271    0.0000 C   0  0" + "\n"
            + "    2.1855   -2.6254    0.0000 C   0  0" + "\n"
            + "    3.6552   -2.9294    0.0000 C   0  0" + "\n"
            + "    4.1295   -4.3524    0.0000 C   0  0" + "\n"
            + "    5.5991   -4.6533    0.0000 C   0  0" + "\n"
            + "    6.5944   -3.5310    0.0000 C   0  0" + "\n"
            + "    6.1201   -2.1080    0.0000 C   0  0" + "\n"
            + "    4.6506   -1.8071    0.0000 C   0  0" + "\n"
            + "    6.9164   -1.2102    0.0000 Cl  0  0" + "\n"
            + "    3.3556    2.8737    0.0000 O   0  0" + "\n"
            + "  1  2  2  0" + "\n"
            + "  1  6  1  0" + "\n"
            + "  2  3  1  0" + "\n"
            + "  3  4  2  0" + "\n"
            + "  4  5  1  0" + "\n"
            + "  4  7  1  0" + "\n"
            + "  5  6  2  0" + "\n"
            + "  5  9  1  0" + "\n"
            + "  7  8  1  0" + "\n"
            + "  7 11  1  0" + "\n"
            + "  8  9  2  0" + "\n"
            + "  9 10  1  0" + "\n"
            + " 10 19  1  0" + "\n"
            + " 11 12  1  0" + "\n"
            + " 12 13  2  0" + "\n"
            + " 12 17  1  0" + "\n"
            + " 13 14  1  0" + "\n"
            + " 14 15  2  0" + "\n"
            + " 15 16  1  0" + "\n"
            + " 16 17  2  0" + "\n"
            + " 16 18  1  0" + "\n"
            + "M  STY  1   1 SUP" + "\n"
            + "M  SLB  1   1   1" + "\n"
            + "M  SAL   1  1  19" + "\n"
            + "M  SBL   1  1  13" + "\n"
            + "M  SMT   1 OH" + "\n"
            + "M  SBV   1  13   -0.9399    0.0000" + "\n"
            + "M  END";

    try {

      IAtomContainer rtn = MolInput.fromCtabString(ctab2);

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public static CmpdLegacyCmpdVO insertLegacyCmpds() {

    CmpdLegacyCmpdVO rtn = new CmpdLegacyCmpdVO();

    Session session = null;
    Transaction tx = null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();

      tx = session.beginTransaction();

      List<Integer> nscIntList = Arrays.asList(new Integer[]{740, 3053, 123127, 163027, 401005, 705701, 743380});

      for (Integer nsc : nscIntList) {

        CmpdLegacyCmpd clc = new CmpdLegacyCmpdImpl();

        clc.setId(nsc.longValue());
        clc.setMolecularFormula("NSC" + nsc);
        clc.setMolecularWeight(-10101d);

        byte[] img = getStructureImage(null, nsc, 512, "NSC" + nsc);

        clc.setJpg512(img);

        session.save(clc);

      }

      tx.commit();

    } catch (Exception e) {
      tx.rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }

    return rtn;

  }

  public static byte[] getStructureImage(String smiles, Integer nsc, Integer structureDim, String title) throws Exception {

    java.net.URL servletURL = null;

    java.net.HttpURLConnection servletConn = null;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {

      servletURL = new java.net.URL("http://localhost:8080/datasystem/StructureServlet");

      servletConn = (java.net.HttpURLConnection) servletURL.openConnection();
      servletConn.setDoInput(true);
      servletConn.setDoOutput(true);
      servletConn.setUseCaches(false);
      servletConn.setRequestMethod("POST");
      servletConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      java.io.DataOutputStream outStream = new java.io.DataOutputStream(servletConn.getOutputStream());

      if (smiles != null) {
        outStream.writeBytes("smiles=" + URLEncoder.encode(smiles, "UTF-8"));
      }

      if (nsc != null) {
        outStream.writeBytes("nsc=" + URLEncoder.encode(nsc.toString(), "UTF-8"));
      }

      if (title != null) {
        outStream.writeBytes("&title=" + URLEncoder.encode(title, "UTF-8"));
      }

      if (structureDim != null) {
        outStream.writeBytes("&structureDim=" + URLEncoder.encode(structureDim.toString(), "UTF-8"));
      }

      outStream.flush();
      outStream.close();

      if (servletConn.getResponseCode() != servletConn.HTTP_OK) {
        throw new Exception("Exception from StructureServlet in getStructureImage in ListManagerController: " + servletConn.getResponseMessage());
      }

//      String tempString = new String();
//      java.io.BufferedReader theReader = new java.io.BufferedReader(new InputStreamReader(servletConn.getInputStream()));
//      while ((tempString = theReader.readLine()) != null) {
//        returnString += tempString;
//      }
      InputStream is = servletConn.getInputStream();

      byte[] buf = new byte[1000];
      for (int nChunk = is.read(buf); nChunk != -1; nChunk = is.read(buf)) {
        baos.write(buf, 0, nChunk);
      }

    } catch (Exception e) {
      System.out.println("Exception in getStructureImage in ListManagerController " + e);
      e.printStackTrace();
      throw new Exception(e);
    } finally {
      servletConn.disconnect();
      servletConn = null;
    }

    baos.flush();
    return baos.toByteArray();

  }

}
