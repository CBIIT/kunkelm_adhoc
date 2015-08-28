/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import com.google.gson.Gson;
import java.util.Arrays;
import java.util.List;
import mwk.datasystem.domain.CmpdLegacyCmpd;
import mwk.datasystem.domain.CmpdLegacyCmpdImpl;
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

    public static CmpdLegacyCmpdVO getLegacyCmpdByNsc(Integer nsc, String currentUser) {

        //MWK TODO this doesn't call currentUser
        CmpdLegacyCmpdVO rtn = new CmpdLegacyCmpdVO();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            
            Criteria cmpdCrit = session.createCriteria(CmpdLegacyCmpd.class);
            cmpdCrit.add(Restrictions.eq("nsc", nsc));
            
            CmpdLegacyCmpd cmpdLegacyCmpd = (CmpdLegacyCmpd) cmpdCrit.uniqueResult();

            rtn = TransformAndroToVO.translateCmpdLegacyCmpd(cmpdLegacyCmpd);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }

    public static CmpdLegacyCmpdVO insertLegacyCmpds(Integer nsc, byte[] imageBytes) {

        CmpdLegacyCmpdVO rtn = new CmpdLegacyCmpdVO();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            CmpdLegacyCmpd clc = CmpdLegacyCmpd.Factory.newInstance();

            // id is set by hibernate sequence generator clc.setId(nsc.longValue());
            clc.setNsc(nsc);            
            clc.setJpg512(imageBytes);

            session.save(clc);

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
