/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.AdHocCmpdFragmentPChem;
import mwk.datasystem.domain.AdHocCmpdFragmentStructure;
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
public class HelperCmpd {
    
    public static void replicatePchem(AdHocCmpdFragmentPChem cur, AdHocCmpdFragmentPChem nw){        
        nw.setAlogp(cur.getAlogp());
        nw.setHba(cur.getHba());
        nw.setHbd(cur.getHbd());
        nw.setLogd(cur.getLogd());
        nw.setMf(cur.getMf());
        nw.setPsa(cur.getPsa());
        nw.setSa(cur.getSa());        
    }
    
    public static void replicateStructure(AdHocCmpdFragmentStructure cur, AdHocCmpdFragmentStructure nw){        
        nw.setCtab(cur.getCtab());
        nw.setInchi(cur.getInchi());
        nw.setInchiAux(cur.getInchiAux());
        nw.setMol(cur.getMol());
        nw.setSmiles(cur.getSmiles());
    }
    
//    public static AdHocCmpd createNewAdHocCmpd(AdHocCmpd ahc, String currentUser) {
//
//        Random randomGenerator = new Random();
//
//        Session session = null;
//        Transaction tx = null;
//
//        AdHocCmpd newAhc = null;
//
//        try {
//
//            session = HibernateUtil.getSessionFactory().openSession();
//
//            long randomId = randomGenerator.nextLong();
//            if (randomId < 0) {
//                randomId = -1 * randomId;
//            }
//            Long adHocCmpdId = new Long(randomId);
//
//            tx = session.beginTransaction();
//
//            newAhc = AdHocCmpd.Factory.newInstance();
//            
//            newAhc.setAdHocCmpdId(adHocCmpdId);
//            newAhc.setCmpdOwner(currentUser);
//            newAhc.setName(ahc.getName());
//            newAhc.setOriginalAdHocCmpdId(ahc.getAdHocCmpdId());
//
//            for (AdHocCmpdFragment ahcf : ahc.getAdHocCmpdFragments()) {
//                
//                AdHocCmpdFragment frag = AdHocCmpdFragment.Factory.newInstance();
//
//                AdHocCmpdFragmentPChem pchem = AdHocCmpdFragmentPChem.Factory.newInstance();                
//                replicatePchem(ahcf.getAdHocCmpdFragmentPChem(), pchem);                
//                session.persist(pchem);
//                
//                frag.setAdHocCmpdFragmentPChem(pchem);
//                                
//                AdHocCmpdFragmentStructure struc = AdHocCmpdFragmentStructure.Factory.newInstance();
//                replicateStructure(ahcf.getAdHocCmpdFragmentStructure(), struc);
//                session.persist(struc);
//                
//                frag.setAdHocCmpdFragmentStructure(struc);
//                
//                // persist the fragment                
//                session.persist(frag);
//                
//                // check whether this is parent, and set that while we're at it
//                
//                if (ahc.getAdHocCmpdParentFragment().equals(ahcf)){
//                    newAhc.setAdHocCmpdParentFragment(frag);
//                }
//                
//            }
//            
//            session.persist("Cmpd", newAhc);
//            
//            tx.commit();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//            tx.rollback();            
//        } finally {
//            session.close();
//        }
//
//        return ahc;
//
//    }

    public CmpdList createCmpdList(List<Cmpd> listOfCmpds, String currentUser) {

        CmpdList rtn = CmpdList.Factory.newInstance();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            Date now = new Date();
            Timestamp ts = new Timestamp(now.getTime());

            Long cmpdListId = null;

            //do {

            java.util.Random generator = new Random();
            long randomId = generator.nextLong();

            if (randomId < 0) {
                randomId = -1 * randomId;
            }

            cmpdListId = new Long(randomId);

            //} while (this.getNovumListDao().searchUniqueNovumListId(novumListId) != null);

            tx = session.beginTransaction();

            // create a new list

            CmpdList cl = CmpdList.Factory.newInstance();

            cl.setCmpdListId(cmpdListId);
            cl.setListName(currentUser + " " + now);
            cl.setDateCreated(ts);
            cl.setListOwner(currentUser);
            cl.setShareWith(currentUser);

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
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }

    public Long createCmpdListByNscs(String listName, List<Integer> nscIntList, String smilesString, String currentUser) {

        Long rtn = Long.valueOf(-1);

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

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

            tx = session.beginTransaction();

            Criteria c = session.createCriteria(Cmpd.class);
            c.add(Restrictions.in("nsc", nscIntList));
            List<Cmpd> cmpdList = (List<Cmpd>) c.list();

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