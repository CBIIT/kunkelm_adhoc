/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import java.io.File;
import java.io.FileOutputStream;
import java.sql.Timestamp;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import mwk.datasystem.domain.AdHocCmpdFragment;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.apache.commons.io.IOUtils;
import org.hibernate.FetchMode;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpdList {

    public static final Boolean DEBUG = Boolean.FALSE;

    public CmpdList createCmpdListFromCmpds(List<Cmpd> listOfCmpds, String currentUser) {

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

    public CmpdList createCmpdListFromCmpdListMembers(List<CmpdListMember> listOfCmpdListMember, String currentUser) {

        CmpdList rtn = CmpdList.Factory.newInstance();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            Date now = new Date();
            Timestamp ts = new Timestamp(now.getTime());

            Long cmpdListId = null;

            java.util.Random generator = new Random();
            long randomId = generator.nextLong();
            if (randomId < 0) {
                randomId = -1 * randomId;
            }
            cmpdListId = new Long(randomId);

            tx = session.beginTransaction();

            // create a new list

            CmpdList cl = CmpdList.Factory.newInstance();

            cl.setCmpdListId(cmpdListId);
            cl.setListName(currentUser + " " + now);
            cl.setDateCreated(ts);
            cl.setListOwner(currentUser);
            cl.setShareWith(currentUser);

            session.persist(cl);

            for (CmpdListMember clm : listOfCmpdListMember) {
                CmpdListMember newClm = CmpdListMember.Factory.newInstance();

                newClm.setCmpd(clm.getCmpd());

                newClm.setCmpdList(cl);
                session.persist(newClm);
                // not needed for persistence, added for quick(er) conversion to VO
                cl.getCmpdListMembers().add(newClm);
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

    /**
     *
     * @param adHocCmpdList In order to pass this off to HelperCmpdTable have to
     * update it dynamically IN ADDITION to creating new ahc that are persisted
     * as "Cmpd"
     * @param listName
     * @param currentUser
     * @return
     */
    public CmpdListVO deNovoCmpdListFromAdHocCmpds(ArrayList<AdHocCmpd> adHocCmpdList, String listName, String currentUser) {

        Random randomGenerator = new Random();

        CmpdListVO clVO = new CmpdListVO();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Date now = new Date();

            ArrayList<Cmpd> entityCmpdList = new ArrayList<Cmpd>();

            for (AdHocCmpd ahc : adHocCmpdList) {

                long randomId = randomGenerator.nextLong();
                if (randomId < 0) {
                    randomId = -1 * randomId;
                }
                Long newRandomId = new Long(randomId);

                // new AdHocCmpd:  originalAdHocCmpdId = adHocCmpdId
                ahc.setAdHocCmpdId(newRandomId);

                if (DEBUG) {
                    System.out.println("In HelperCmpdList deNovCmpdListFromAdHocCmpds.  Setting originalAdHocCmpdId to: " + newRandomId);
                }
                ahc.setOriginalAdHocCmpdId(newRandomId);

                ahc.setCmpdOwner(currentUser);

                session.persist("Cmpd", ahc);

                for (AdHocCmpdFragment ahcf : ahc.getAdHocCmpdFragments()) {
                    // persist the struc and pchem
                    session.persist(ahcf.getAdHocCmpdFragmentPChem());
                    session.persist(ahcf.getAdHocCmpdFragmentStructure());
                    // persist the fragment
                    session.persist(ahcf);
                }

                // if only one fragment, make it the parent, otherwise sort by size

                ArrayList<AdHocCmpdFragment> fragList = new ArrayList<AdHocCmpdFragment>(ahc.getAdHocCmpdFragments());
                Collections.sort(fragList, new Comparators.AdHocCmpdFragmentSizeComparator());
                Collections.reverse(fragList);
                ahc.setAdHocCmpdParentFragment(fragList.get(0));

                // track the cmpds for addition to CmpdList
                // each ahc has been persisted as Cmpd
                entityCmpdList.add(ahc);

            }

            // create a new list
            long randomId = randomGenerator.nextLong();
            if (randomId < 0) {
                randomId = -1 * randomId;
            }
            Long cmpdListId = new Long(randomId);

            CmpdList cl = CmpdList.Factory.newInstance();

            cl.setCmpdListId(cmpdListId);
            cl.setListName(listName);
            cl.setDateCreated(now);
            cl.setListOwner(currentUser);
            cl.setShareWith(currentUser);
            cl.setCountListMembers(entityCmpdList.size());

            // id from GenerateSequence in Entity class
            session.persist(cl);

            // create the list members
            for (Cmpd c : entityCmpdList) {
                CmpdListMember clm = CmpdListMember.Factory.newInstance();
                // bidirectional
                clm.setCmpd(c);
                clm.setCmpdList(cl);
                session.persist(clm);
            }

            tx.commit();

            // writes to CmpdTable so that subsequent fetch will work

            List<CmpdVO> cList = HelperCmpdTable.adHocCmpdsToCmpdTable(adHocCmpdList);

            clVO = this.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, currentUser);

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return clVO;

    }

    public CmpdListVO getCmpdListByCmpdListId(Long cmpdListId, Boolean includeListMembers, String currentUser) {

        CmpdListVO rtnVO = new CmpdListVO();
        CmpdList entityCL = null;

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Criteria clCrit = session.createCriteria(CmpdList.class);
            clCrit.add(Restrictions.eq("cmpdListId", cmpdListId));
            clCrit.add(Restrictions.disjunction()
                    .add(Restrictions.eq("listOwner", currentUser))
                    .add(Restrictions.eq("shareWith", "PUBLIC")));

            if (includeListMembers) {
                clCrit.setFetchMode("listMembers", FetchMode.JOIN);
            }


            entityCL = (CmpdList) clCrit.uniqueResult();

            // just populate the top-level stuff

            // MWK WHY/HOW does using the TransformCmpdTable method speed things up so much?

            rtnVO = TransformCmpdTableToVO.toCmpdListVO(entityCL, includeListMembers);

            // HashMap for holding cmpdId numbers and their respective CmpdListMemberVO
            // for resolution AFTER fetching CmpdTable by cmpdIdList

            HashMap<Long, CmpdListMemberVO> map = new HashMap<Long, CmpdListMemberVO>();

            List<Long> cmpdIdList = new ArrayList<Long>();
            Long cmpdId = null;

            for (CmpdListMember clm : entityCL.getCmpdListMembers()) {
                cmpdId = (Long) session.getIdentifier(clm.getCmpd());
                cmpdIdList.add(cmpdId);
                map.put(cmpdId, TransformCmpdTableToVO.toCmpdListMemberVO(clm));
            }

            // fetch a list of cmpdViews
            Criteria cvCrit = session.createCriteria(CmpdTable.class);
            cvCrit.add(Restrictions.in("id", cmpdIdList));
            List<CmpdTable> entityCVlist = (List<CmpdTable>) cvCrit.list();

            for (CmpdTable cv : entityCVlist) {
                // translate to VO
                CmpdVO cVO = TransformCmpdTableToVO.toCmpdVO(cv);
                // add the VO to appropriate HashMap (CellLineMemberVO)        
                map.get(cVO.getId()).setCmpd(cVO);
            }

            ArrayList<CmpdListMemberVO> memberList = new ArrayList<CmpdListMemberVO>(map.values());

            rtnVO.setCmpdListMembers(memberList);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }


        return rtnVO;

    }

    public List<CmpdListVO> showAvailableCmpdLists(String currentUser) {

        List<CmpdListVO> voList = new ArrayList<CmpdListVO>();
        List<CmpdList> entityList = null;

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            Criteria c = session.createCriteria(CmpdList.class);
            c.add(Restrictions.disjunction()
                    .add(Restrictions.eq("listOwner", currentUser))
                    .add(Restrictions.eq("shareWith", "PUBLIC")));
            entityList = (List<CmpdList>) c.list();

            voList = TransformAndroToVO.translateCmpdLists(entityList, Boolean.FALSE);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return voList;

    }

    public void updateCmpdList(CmpdListVO cL, String currentUser) {

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            Criteria c = session.createCriteria(CmpdList.class);
            c.add(Restrictions.eq("cmpdListId", cL.getCmpdListId()));

            // ONLY listOwner can update, but only if not PUBLIC
            c.add(Restrictions.eq("listOwner", currentUser));
            c.add(Restrictions.ne("shareWith", "PUBLIC"));

            CmpdList cl = (CmpdList) c.uniqueResult();

            if (cL.getListName() != null) {
                cl.setListName(cL.getListName());
            }
            if (cL.getListComment() != null) {
                cl.setListComment(cL.getListComment());
            }
            if (cL.getAnchorComment() != null) {
                cl.setAnchorComment(cL.getAnchorComment());
            }
            if (cL.getAnchorSmiles() != null) {
                cl.setAnchorSmiles(cL.getAnchorSmiles());
            }

            session.update(cl);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void makeCmpdListPublic(Long cmpdListId, String currentUser) {

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            Criteria c = session.createCriteria(CmpdList.class);
            c.add(Restrictions.eq("cmpdListId", cmpdListId));
            c.add(Restrictions.eq("listOwner", currentUser));

            CmpdList cl = (CmpdList) c.uniqueResult();

            cl.setShareWith("PUBLIC");
            session.update(cl);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    public void deleteCmpdListByCmpdListId(Long cmpdListId, String currentUser) {

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Criteria clCriteria = session.createCriteria(CmpdList.class);
            clCriteria.add(Restrictions.eq("cmpdListId", cmpdListId));

            // onlylistOwner can delete, but not if PUBLIC
            clCriteria.add(Restrictions.eq("listOwner", currentUser));
            clCriteria.add(Restrictions.ne("shareWith", "PUBLIC"));

            CmpdList cl = (CmpdList) clCriteria.uniqueResult();

            //cascade="delete" is enabled from cl->clm
            //so the clm will be deleted,
            //but I set changed to cascade="none" on Cmpd

            //have to programatically determine whether any ad_hoc_cmpds
            //and delete those

            List<Long> idListForDelete = new ArrayList<Long>();
            Long cmpdId = null;

            for (CmpdListMember clm : cl.getCmpdListMembers()) {
                cmpdId = (Long) session.getIdentifier(clm.getCmpd());
                idListForDelete.add(cmpdId);
            }

            // delete the CmpdList        
            session.delete(cl);

            // find and delete those cmpd with join to ahc
            // is this a kludgey way to handle empty lists?

            if (idListForDelete.size() > 0) {

                Criteria ahcCriteria = session.createCriteria(AdHocCmpd.class);
                ahcCriteria.add(Restrictions.in("id", idListForDelete));
                ahcCriteria.add(Restrictions.eq("cmpdOwner", currentUser));

                List<AdHocCmpd> ahcList = (List<AdHocCmpd>) ahcCriteria.list();

                for (AdHocCmpd ahc : ahcList) {
                    session.delete(ahc);
                }

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