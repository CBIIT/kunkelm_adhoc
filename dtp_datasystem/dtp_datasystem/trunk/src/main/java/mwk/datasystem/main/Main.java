/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdLegacyCmpd;
import mwk.datasystem.domain.CmpdLegacyCmpdImpl;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.NscCmpdImpl;
import mwk.datasystem.util.HelperCmpdList;
import static mwk.datasystem.util.HelperCmpdList.DEBUG;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.Unproxy;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class Main {

  public static void main(String[] args) {

    insertLegacyCmpds();

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
