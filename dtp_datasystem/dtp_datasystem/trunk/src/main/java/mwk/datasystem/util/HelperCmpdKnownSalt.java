/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import java.util.ArrayList;
import java.util.List;
import mwk.datasystem.domain.CmpdKnownSalt;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.vo.CmpdKnownSaltVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpdKnownSalt {

  public static List<CmpdKnownSaltVO> loadAllSalts() {

    ArrayList<CmpdKnownSaltVO> rtnList = new ArrayList<CmpdKnownSaltVO>();

    Session session = null;
    Transaction tx = null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();

      tx = session.beginTransaction();

      Criteria c = session.createCriteria(CmpdKnownSalt.class);
      List<CmpdKnownSalt> saltList = (List<CmpdKnownSalt>) c.list();

      for (CmpdKnownSalt cks : saltList) {
        rtnList.add(TransformAndroToVO.toCmpdKnownSaltVO(cks));
      }
      
      tx.commit();

    } catch (Exception e) {
      tx.rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }

    return rtnList;

  }

  public static void updateSalt(CmpdKnownSaltVO cksVO) {

    Session session = null;
    Transaction tx = null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();

      tx = session.beginTransaction();
      Criteria c = session.createCriteria(CmpdKnownSalt.class);
      c.add(Restrictions.eq("id", cksVO.getId()));

      CmpdKnownSalt cksEntity = (CmpdKnownSalt) c.uniqueResult();

      if (cksEntity != null){
      
      if (cksVO.getSaltName() != null) {
        cksEntity.setSaltName(cksVO.getSaltName());
      }
      if (cksVO.getSaltMf() != null) {
        cksEntity.setSaltMf(cksVO.getSaltMf());
      }
      if (cksVO.getSaltMw() != null) {
        cksEntity.setSaltMw(cksVO.getSaltMw());
      }
      if (cksVO.getCanSmi() != null) {
        cksEntity.setCanSmi(cksVO.getCanSmi());
      }
      if (cksVO.getCanTaut() != null) {
        cksEntity.setCanTaut(cksVO.getCanTaut());
      }
      if (cksVO.getCanTautStripStereo() != null) {
        cksEntity.setCanTautStripStereo(cksVO.getCanTautStripStereo());
      }

      session.update(cksEntity);
      
      } else {
        // TODO handle failure
      }

      tx.commit();

    } catch (Exception e) {
      tx.rollback();
      e.printStackTrace();
    } finally {
      session.close();
    }

  }

}
