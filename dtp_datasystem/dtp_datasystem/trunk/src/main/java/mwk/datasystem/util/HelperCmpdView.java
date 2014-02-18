/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdView;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpdView {

  Session session = null;

  public HelperCmpdView() {
    this.session = HibernateUtil.getSessionFactory().getCurrentSession();
  }

  public CmpdList createCmpdList(List<Cmpd> listOfCmpds, String listOwner) {

    CmpdList rtn = CmpdList.Factory.newInstance();

    try {

      Date now = new Date();
      XMLGregorianCalendar xmlNow = TransformXMLGregorianCalendar.asXMLGregorianCalendar(now);

      String shareWith = listOwner;

      Long cmpdListId = null;

      //do {

      java.util.Random generator = new Random();
      long randomId = generator.nextLong();

      if (randomId < 0) {
        randomId = -1 * randomId;
      }

      cmpdListId = new Long(randomId);

      //} while (this.getNovumListDao().searchUniqueNovumListId(novumListId) != null);

      Transaction tx = session.beginTransaction();

      // create a new list

      CmpdList cl = CmpdList.Factory.newInstance();

      cl.setCmpdListId(cmpdListId);
      cl.setListName(listOwner + " " + now);
      cl.setDateCreated(now);
      cl.setListOwner(listOwner);
      cl.setShareWith(shareWith);

      session.persist(cl);

      for (Cmpd c : listOfCmpds) {
        CmpdListMember clm = CmpdListMember.Factory.newInstance();
        clm.setCmpd(c);
        clm.setCmpdList(cl);
        session.persist(clm);
        // not needed for persistence, added for quick(er) conversion to VO
        cl.getCmpdListMembers().add(clm);
      }

      cl.setCountListMembers(cl.getCmpdListMembers().size());

      tx.commit();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return rtn;

  }

  public CmpdListVO createCmpdListByNscs(String listName, List<Integer> nscIntList, String listOwner) {

    CmpdListVO rtn = new CmpdListVO();

    try {

      Date now = new Date();
      XMLGregorianCalendar xmlNow = TransformXMLGregorianCalendar.asXMLGregorianCalendar(now);

      Long cmpdListId = null;

      //do {
      java.util.Random generator = new Random();
      long randomId = generator.nextLong();
      if (randomId < 0) {
        randomId = -1 * randomId;
      }
      cmpdListId = new Long(randomId);
      //} while (this.getNovumListDao().searchUniqueNovumListId(novumListId) != null);

      Transaction tx = session.beginTransaction();

      Criteria c = session.createCriteria(Cmpd.class);
      c.add(Restrictions.in("nsc", nscIntList));
      List<Cmpd> cmpdList = (List<Cmpd>) c.list();

      // create a new cmpdlist

      CmpdList cl = CmpdList.Factory.newInstance();

      cl.setCmpdListId(cmpdListId);
      cl.setListName(listName);
      cl.setDateCreated(now);
      cl.setListOwner(listOwner);
      cl.setShareWith(listOwner);

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

      
      //rtn = util.TransformAndroToVO.toCmpdListVO(cl, Boolean.TRUE);
      //MWK switch to pre-flattened view
      rtn = TransformCmpdViewToVO.toCmpdListVO(cl, Boolean.TRUE);

      tx.commit();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return rtn;

  }

  public List<CmpdVO> getCmpdViewsByNsc(List<Integer> nscIntList) {

    List<CmpdVO> rtnList = new ArrayList<CmpdVO>();

    try {

      org.hibernate.Transaction tx = session.beginTransaction();
      Criteria c = session.createCriteria(CmpdView.class);
      c.add(Restrictions.in("nsc", nscIntList));
      List<CmpdView> cvList = (List<CmpdView>) c.list();
      
      for (CmpdView cv : cvList){
          rtnList.add(TransformCmpdViewToVO.toCmpdVO(cv));          
      }
      
      tx.commit();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return rtnList;

  }

  public Cmpd getCmpdByCmpdID(Long cmpdId) {

    List<Cmpd> cmpdList = null;

    try {

      org.hibernate.Transaction tx = session.beginTransaction();
      Query q = session.createQuery("from Cmpd as cmpd where cmpd.cmpdId =" + cmpdId);
      cmpdList = (List<Cmpd>) q.list();

    } catch (Exception e) {
      e.printStackTrace();
    }

    return cmpdList.get(0);

  }
  
}
