/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.controllers.QueryObject;
import mwk.datasystem.domain.CmpdImpl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpd {

  public static Long createCmpdListByNscs(
          String listName,
          List<Integer> nscIntList,
          String smilesString,
          String currentUser) {

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

  private static Boolean listNotNullAndNotEmpty(List theList) {

    Boolean rtn = Boolean.FALSE;

    if ((theList != null) && (!theList.isEmpty())) {
      rtn = Boolean.TRUE;
    }

    return rtn;

  }

  public static Long createCmpdListFromQueryObject(
          String listName,
          QueryObject qo,
          String smilesString,
          String currentUser) {

    System.out.println("In createCmpdListFromQueryObject in HelperCmpd.");

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

      Criteria c = session.createCriteria(Cmpd.class)
              .setProjection(Projections.property("id"));

      Disjunction disj = Restrictions.disjunction();

      if (listNotNullAndNotEmpty(qo.getNscs())) {       
        // nscs to Integers
        ArrayList<Integer> nscIntList = new ArrayList<Integer>();
        for (String s : qo.getNscs()) {
          try {
            nscIntList.add(Integer.valueOf(s));
          } catch (Exception e) {

          }
        }
        if (!nscIntList.isEmpty()) {
          disj.add(Restrictions.in("nsc", nscIntList));
        }
      }

      if (listNotNullAndNotEmpty(qo.getCases())) {        
        disj.add(Restrictions.in("cas", qo.getCases()));
      }

      if ((listNotNullAndNotEmpty(qo.getDrugNames()))
              || (listNotNullAndNotEmpty(qo.getAliases()))) {       
        c.createAlias("cmpdAliases", "aliases");
        ArrayList<String> combinedList = new ArrayList<String>();
        if ((qo.getAliases() != null) && !qo.getAliases().isEmpty()) {
          combinedList.addAll(qo.getAliases());
        }
        if ((qo.getDrugNames() != null) && !qo.getDrugNames().isEmpty()) {
          combinedList.addAll(qo.getDrugNames());
        }
        disj.add(Restrictions.in("aliases.alias", combinedList));
      }

      if (listNotNullAndNotEmpty(qo.getCmpdNamedSets())) {
        c.createAlias("cmpdNamedSets", "namedSets");
        disj.add(Restrictions.in("namedSets.setName", qo.getCmpdNamedSets()));
      }

      if (listNotNullAndNotEmpty(qo.getProjectCodes())) {
        c.createAlias("cmpdProjects", "projects");
        disj.add(Restrictions.in("projects.projectCode", qo.getProjectCodes()));
      }

      if (listNotNullAndNotEmpty(qo.getPlates())) {
        c.createAlias("cmpdPlates", "plates");
        disj.add(Restrictions.in("plates.plateName", qo.getPlates()));
      }

      if (listNotNullAndNotEmpty(qo.getTargets())) {
        c.createAlias("cmpdTargets", "targets");
        disj.add(Restrictions.in("targets.target", qo.getTargets()));
      }

      // add the disjunction
      c.add(disj);

      // have to handle multiplicities by plate, alias, etc.
      c.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

      // List<Cmpd> cmpdList = (List<Cmpd>) c.list();
      List<Long> projList = c.list();

      System.out.println("Size of projList in createCmpdListFromQueryObjectin HelperCmpd is: " + projList.size());

      // create a new cmpdlist
      CmpdList cl = CmpdList.Factory.newInstance();

      cl.setCmpdListId(cmpdListId);
      cl.setListName(listName);
      cl.setDateCreated(now);
      cl.setListOwner(currentUser);
      cl.setShareWith(currentUser);
      cl.setAnchorSmiles(smilesString);

      session.persist(cl);

      for (Long l : projList) {
        CmpdListMember clm = CmpdListMember.Factory.newInstance();
        clm.setCmpd((Cmpd) session.load(CmpdImpl.class, l));
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

  public static List<CmpdVO> getCmpdsByNsc(List<Integer> nscIntList, String currentUser) {

    //MWK TODO this doesn't call currentUser
    List<CmpdVO> rtnList = new ArrayList<CmpdVO>();

    Session session = null;
    Transaction tx = null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();

      tx = session.beginTransaction();
      Criteria cmpdCrit = session.createCriteria(Cmpd.class);
      cmpdCrit.add(Restrictions.in("nsc", nscIntList));
      List<Cmpd> cmpdList = (List<Cmpd>) cmpdCrit.list();

      List<Long> cmpdIdList = new ArrayList<Long>();
      Long cmpdId = null;

      for (Cmpd cmpd : cmpdList) {
        cmpdId = (Long) session.getIdentifier(cmpd);
        cmpdIdList.add(cmpdId);
      }

// fetch a list of cmpdViews
      Criteria cvCrit = session.createCriteria(CmpdTable.class);
      cvCrit.add(Restrictions.in("id", cmpdIdList));
      List<CmpdTable> entityCVlist = (List<CmpdTable>) cvCrit.list();

      for (CmpdTable cv : entityCVlist) {
        CmpdVO cVO = TransformCmpdTableToVO.toCmpdVO(cv);
        rtnList.add(cVO);
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

  public static CmpdVO getSingleCmpdByNsc(Integer nsc, String currentUser) {

    //MWK TODO this doesn't call currentUser!
    
    CmpdVO rtn = new CmpdVO();

    Session session = null;
    Transaction tx = null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();

      tx = session.beginTransaction();
      Criteria cmpdCrit = session.createCriteria(Cmpd.class);
      cmpdCrit.add(Restrictions.eq("nsc", nsc));
      Cmpd cmpd = (Cmpd) cmpdCrit.uniqueResult();

      Long cmpdId = (Long) session.getIdentifier(cmpd);

      Criteria cvCrit = session.createCriteria(CmpdTable.class);
      cvCrit.add(Restrictions.eq("id", cmpdId));
      CmpdTable entityCV = (CmpdTable) cvCrit.uniqueResult();

      rtn = TransformCmpdTableToVO.toCmpdVO(entityCV);

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
