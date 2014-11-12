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
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpd {

    public Long createCmpdListByNscs(
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

    public Long createCmpdListByListContentBean(
            String listName,
            QueryObject qo,
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

            // root crit
            Criteria c = session.createCriteria(Cmpd.class);

            Disjunction disj = Restrictions.disjunction();

            if (qo.getNscs() != null && qo.getNscs().size() > 0) {
                // nscs to Integers
                ArrayList<Integer> nscIntList = new ArrayList<Integer>();
                for (String s : qo.getNscs()) {
                    try {
                        nscIntList.add(Integer.valueOf(s));
                    } catch (Exception e) {

                    }
                }
                if (nscIntList.size() > 0) {
                    disj.add(Restrictions.in("nsc", nscIntList));
                }
            }

            if (qo.getCases() != null && qo.getCases().size() > 0) {
                disj.add(Restrictions.in("cas", qo.getCases()));
            }

            if ((qo.getDrugNames() != null && qo.getDrugNames().size() > 0) || (qo.getAliases() != null && qo.getAliases().size() > 0)) {
                c.createAlias("cmpdAliases", "aliases", CriteriaSpecification.LEFT_JOIN);
                ArrayList<String> combinedList = new ArrayList<String>();
                if (qo.getAliases() != null && qo.getAliases().size() > 0) {
                    combinedList.addAll(qo.getAliases());
                }
                if (qo.getDrugNames() != null && qo.getDrugNames().size() > 0) {
                    combinedList.addAll(qo.getDrugNames());
                }
                disj.add(Restrictions.in("aliases.alias", combinedList));
            }

            if (qo.getCmpdNamedSets() != null && qo.getCmpdNamedSets().size() > 0) {
                c.createAlias("cmpdNamedSets", "namedSets", CriteriaSpecification.LEFT_JOIN);
                disj.add(Restrictions.in("namedSets.setName", qo.getCmpdNamedSets()));
            }

            if (qo.getProjectCodes() != null && qo.getProjectCodes().size() > 0) {
                c.createAlias("cmpdProjects", "projects", CriteriaSpecification.LEFT_JOIN);
                disj.add(Restrictions.in("projects.projectCode", qo.getProjectCodes()));
            }

            if (qo.getPlates() != null && qo.getPlates().size() > 0) {
                c.createAlias("cmpdPlates", "plates", CriteriaSpecification.LEFT_JOIN);
                disj.add(Restrictions.in("plates.plateName", qo.getPlates()));
            }

            if (qo.getTargets() != null && qo.getTargets().size() > 0) {
                c.createAlias("cmpdTargets", "targets", CriteriaSpecification.LEFT_JOIN);
                disj.add(Restrictions.in("targets.target", qo.getTargets()));
            }

            // add the disjunction
            c.add(disj);

            // have to handle multiplicities by plate, alias, etc.
            c.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            List<Cmpd> cmpdList = (List<Cmpd>) c.list();

            System.out.println("Size of cmpdList in createCmpdListByListContentBeanin HelperCmpd is: " + cmpdList.size());

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

    public List<CmpdVO> getCmpdsByNsc(List<Integer> nscIntList, String currentUser) {

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

    public CmpdVO getSingleCmpdByNsc(Integer nsc, String currentUser) {

        //MWK TODO this doesn't call currentUser
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
