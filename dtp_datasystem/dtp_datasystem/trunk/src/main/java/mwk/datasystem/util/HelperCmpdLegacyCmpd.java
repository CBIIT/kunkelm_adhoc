/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import com.google.gson.Gson;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.domain.CmpdLegacyCmpd;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpdLegacyCmpd {

    public Long createCmpdListByNscs(String listName, List<Integer> nscIntList, String smilesString, String currentUser) {

        Long rtn = Long.valueOf(-1);

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            Date now = new Date();
            XMLGregorianCalendar xmlNow = TransformXMLGregorianCalendar.asXMLGregorianCalendar(now);

            Long cmpdListId = null;

            java.util.Random generator = new Random();
            long randomId = generator.nextLong();
            if (randomId < 0) {
                randomId = -1 * randomId;
            }
            cmpdListId = new Long(randomId);

            tx = session.beginTransaction();

            Criteria c = session.createCriteria(Cmpd.class);
            c.add(Restrictions.in("nsc", nscIntList));
            List<Cmpd> cmpdList = (List<Cmpd>) c.list();

            System.out.println("Size of cmpdList in createCmpdListByNscs in HelperCmpd is: " + cmpdList.size());

            // create a new cmpdlist

            CmpdList cl = CmpdList.Factory.newInstance();

            cl.setCmpdListId(cmpdListId);
            cl.setListName(listName);
            cl.setDateCreated(now);
            cl.setListOwner(currentUser);
            cl.setShareWith(currentUser);
            cl.setAnchorSmiles(smilesString);

            session.persist(cl);

            for (Cmpd cmpd : cmpdList) {
                CmpdListMember clm = CmpdListMember.Factory.newInstance();
                clm.setCmpd(cmpd);
                clm.setCmpdList(cl);
                session.persist(clm);
                // not needed for persistence, added for quick(er) conversion to VO
                cl.getCmpdListMembers().add(clm);
            }

            cl.setCountListMembers(cl.getCmpdListMembers().size());

            rtn = cl.getCmpdListId();

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }

    public static String makeGson(CmpdLegacyCmpdVO hmm) {


        String rtn = "";
        Gson gson = new Gson();
        String json = gson.toJson(hmm);

        rtn = json;

        return rtn;

    }

    public static String parseGson(String gsonStr) {

        String rtn = "";
        Gson gson = new Gson();

        CmpdLegacyCmpdVO obj = gson.fromJson(gsonStr, CmpdLegacyCmpdVO.class);


        return rtn;

    }
    
    public CmpdLegacyCmpdVO getLegacyCmpdByNsc(Integer nsc, String currentUser) {

        //MWK TODO this doesn't call currentUser

        CmpdLegacyCmpdVO rtn = new CmpdLegacyCmpdVO();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            Criteria cmpdCrit = session.createCriteria(CmpdLegacyCmpd.class);
            cmpdCrit.add(Restrictions.eq("id", nsc));
            CmpdLegacyCmpd cmpdLegacyCmpd = (CmpdLegacyCmpd) cmpdCrit.uniqueResult();

            rtn = TransformAndroToVO.toCmpdLegacyCmpdVO(cmpdLegacyCmpd);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }
}