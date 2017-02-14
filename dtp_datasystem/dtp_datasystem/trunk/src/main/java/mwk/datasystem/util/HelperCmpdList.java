/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import java.sql.Timestamp;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.controllers.ApplicationScopeBean;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.CmpdImpl;
import mwk.datasystem.domain.NscCmpdImpl;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.FetchMode;
import org.hibernate.Query;
import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Disjunction;

// MWK TODO do these need setMaxResults or will that percolate from fetchers elsewhere?
/**
 *
 * @author mwkunkel
 */
public class HelperCmpdList {

    public static final Boolean DEBUG = Boolean.TRUE;

    /**
     *
     * @param adHocCmpdList In order to pass this off to HelperCmpdTable have to
     * update it dynamically IN ADDITION to creating new ahc that are persisted
     * as "Cmpd"
     * @param listName
     * @param currentUser
     * @return
     */
    public static CmpdListVO deNovoCmpdListFromAdHocCmpds(ArrayList<AdHocCmpd> adHocCmpdList, String listName, String currentUser) {

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
                    System.out.println("adHocCmpd name is: " + ahc.getName());
                    System.out.println("In HelperCmpdList deNovCmpdListFromAdHocCmpds.  Setting originalAdHocCmpdId to: " + newRandomId);
                }
                ahc.setOriginalAdHocCmpdId(newRandomId);

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

                // LOTS of possible issues with adHoc cmpds --> could end up with no fragments
                if (fragList.size() == 0) {
                } else {
                    ahc.setAdHocCmpdParentFragment(fragList.get(0));
                }

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

            clVO = getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, currentUser);

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return clVO;

    }

    public static CmpdListVO getCmpdListByCmpdListId(Long cmpdListId, Boolean includeListMembers, String currentUser) {

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
                    .add(Restrictions.eq("shareWith", "EVERYONE")));

            if (includeListMembers) {
                clCrit.setFetchMode("listMembers", FetchMode.JOIN);
            }

            entityCL = (CmpdList) clCrit.uniqueResult();

            // just populate the top-level stuff
            rtnVO = TransformCmpdTableToVO.translateCmpdList(entityCL, includeListMembers);

            // HashMap for holding cmpdId numbers and their respective CmpdListMemberVO
            // for resolution AFTER fetching CmpdTable by cmpdIdList
            HashMap<Long, CmpdListMemberVO> map = new HashMap<Long, CmpdListMemberVO>();

            List<Long> cmpdIdList = new ArrayList<Long>();
            Long cmpdId = null;

            for (CmpdListMember clm : entityCL.getCmpdListMembers()) {
                cmpdId = (Long) session.getIdentifier(clm.getCmpd());
                cmpdIdList.add(cmpdId);
                map.put(cmpdId, TransformCmpdTableToVO.translateCmpdListMember(clm));
            }

            // fetch a list of cmpdTable
            Criteria cvCrit = session.createCriteria(CmpdTable.class)
                    .setMaxResults(2000);
            cvCrit.add(Restrictions.in("id", cmpdIdList));
            List<CmpdTable> entityCVlist = (List<CmpdTable>) cvCrit.list();

            if (!entityCVlist.isEmpty()) {

                for (CmpdTable cv : entityCVlist) {
                    // translate to VO
                    CmpdVO cVO = TransformCmpdTableToVO.translateCmpd(cv);
                    // add the VO to appropriate HashMap (CellLineMemberVO)        
                    map.get(cVO.getId()).setCmpd(cVO);
                }

                ArrayList<CmpdListMemberVO> memberList = new ArrayList<CmpdListMemberVO>(map.values());

                rtnVO.setCmpdListMembers(memberList);

            }

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtnVO;

    }

    public static List<CmpdListVO> searchCmpdLists(
            List<String> listNames,
            List<Long> cmpdListIds,
            String currentUser) {

        List<CmpdListVO> voList = new ArrayList<CmpdListVO>();
        List<CmpdList> entityList = null;

        if (listNames == null) {
            listNames = new ArrayList<String>();
        }
        if (cmpdListIds == null) {
            cmpdListIds = new ArrayList<Long>();
        }
        if (currentUser == null) {
            System.out.println("currentUser is null. setting to \"PUBLIC\"");
            currentUser = "PUBLIC";
        }

        Session session = HibernateUtil.getSessionFactory().openSession();
        Transaction tx = session.beginTransaction();

        try {

            Criteria crit = session.createCriteria(CmpdList.class);

            // restrict to owned or shared
            Disjunction accessDisj = Restrictions.disjunction();
            accessDisj.add(Restrictions.eq("listOwner", currentUser));
            accessDisj.add(Restrictions.eq("shareWith", "EVERYONE"));

            Conjunction listNameConj = null;
            Conjunction cmpdListIdConj = null;

            if (!listNames.isEmpty()) {
                listNameConj = Restrictions.conjunction();
                listNameConj.add(accessDisj);
                listNameConj.add(Restrictions.in("listName", listNames));
            }

            if (!cmpdListIds.isEmpty()) {
                cmpdListIdConj = Restrictions.conjunction();
                cmpdListIdConj.add(accessDisj);
                cmpdListIdConj.add(Restrictions.in("cmpdListId", cmpdListIds));
            }

            if (listNameConj != null && cmpdListIdConj != null) {

                Disjunction disj = Restrictions.disjunction();
                disj.add(listNameConj).add(cmpdListIdConj);
                if (currentUser.equals("PUBLIC")) {
                    disj.add(Restrictions.eq("shareWith", "EVERYONE"));
                } else {
                    disj.add(accessDisj);
                }
                crit.add(disj);

            } else if (listNameConj != null) {

                Disjunction disj = Restrictions.disjunction();
                disj.add(listNameConj);
                if (currentUser.equals("PUBLIC")) {
                    disj.add(Restrictions.eq("shareWith", "EVERYONE"));
                } else {
                    disj.add(accessDisj);
                }
                crit.add(disj);

            } else if (cmpdListIdConj != null) {

                Disjunction disj = Restrictions.disjunction();
                disj.add(cmpdListIdConj);
                if (currentUser.equals("PUBLIC")) {
                    disj.add(Restrictions.eq("shareWith", "EVERYONE"));
                } else {
                    disj.add(accessDisj);
                }
                crit.add(disj);

            } else // if no seach criteria were specified, run default
            // seach for shared lists (if PUBLIC user) 
            // or shared AND owned lists if anyone else
            {
                if (currentUser.equals("PUBLIC")) {
                    crit.add(Restrictions.eq("shareWith", "EVERYONE"));
                } else {
                    crit.add(accessDisj);
                }
            }

            entityList = (List<CmpdList>) crit.list();

            if (!entityList.isEmpty()) {
                voList = TransformAndroToVO.translateCmpdLists(entityList, Boolean.FALSE);
            }

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return voList;

    }

    public static void updateCmpdList(CmpdListVO cL, String currentUser) {

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            Criteria crit = session.createCriteria(CmpdList.class);
            crit.add(Restrictions.eq("cmpdListId", cL.getCmpdListId()));

            // ONLY listOwner can update, but only if not shareWith EVERYONE      
            crit.add(Restrictions.eq("listOwner", currentUser));
            crit.add(Restrictions.ne("shareWith", "EVERYONE"));

            CmpdList cl = (CmpdList) crit.uniqueResult();

            if (cl != null) {

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

            }

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    public static void shareCmpdList(Long cmpdListId, String currentUser) {

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            Criteria crit = session.createCriteria(CmpdList.class);
            crit.add(Restrictions.eq("cmpdListId", cmpdListId));
            crit.add(Restrictions.eq("listOwner", currentUser));

            CmpdList cl = (CmpdList) crit.uniqueResult();

            cl.setShareWith("EVERYONE");

            session.update(cl);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    public static void deleteCmpdListByCmpdListId(Long cmpdListId, String currentUser) {

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Criteria clCrit = session.createCriteria(CmpdList.class);

            clCrit.add(Restrictions.eq("cmpdListId", cmpdListId));
            clCrit.add(Restrictions.eq("listOwner", currentUser));
            // can NOT delete a shared list
            clCrit.add(Restrictions.ne("shareWith", "EVERYONE"));

            CmpdList target = (CmpdList) clCrit.uniqueResult();

            Long actualListId = target.getId();

            Collection<CmpdListMember> clml = target.getCmpdListMembers();

            ArrayList<Long> adHocCmpdIdForDelete = new ArrayList<Long>();

            for (CmpdListMember clm : clml) {

                Cmpd c = Unproxy.initializeAndUnproxy(clm.getCmpd());

                if (DEBUG) {
                    System.out.println("c.getClass() is: " + c.getClass());
                }

                // NSC compounds -> just delete the list member
                if (c instanceof NscCmpdImpl) {

                    if (DEBUG) {
                        System.out.println("c.getClass() is: " + c.getClass() + " only deleting clm");
                    }

                } else {

                    if (DEBUG) {
                        System.out.println("c.getClass() is: " + c.getClass() + " also deleting cmpd");
                    }

                    adHocCmpdIdForDelete.add(c.getId());

                }

            }

            // delete the cmpdListMembers
            Query q = session.createSQLQuery("delete from cmpd_list_member where cmpd_list_fk = :clId");
            q.setParameter("clId", actualListId);
            q.executeUpdate();

            // if there were any adHocCmpds, delete them
            if (!adHocCmpdIdForDelete.isEmpty()) {

                // for SANITY, use session.delete
                for (Long id : adHocCmpdIdForDelete) {
                    Cmpd c = (Cmpd) session.load(CmpdImpl.class, id);
                    session.delete(c);
                }

                // also delete from cmpd_table
                q = session.createSQLQuery("delete from cmpd_table where id in (:idList)");
                q.setParameterList("idList", adHocCmpdIdForDelete);
                q.executeUpdate();

            }

            // delete the cmpdList
            q = session.createSQLQuery("delete from cmpd_list where id = :clId");
            q.setParameter("clId", actualListId);
            q.executeUpdate();

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

    }

    public static CmpdListVO persistCmpdList(CmpdListVO clVO, String currentUser) {

        CmpdListVO rtn = new CmpdListVO();

        try {

            List<CmpdVO> cVOlist = new ArrayList<CmpdVO>();

            for (CmpdListMemberVO clmVO : clVO.getCmpdListMembers()) {
                cVOlist.add(clmVO.getCmpd());
            }

            rtn = persistListOfCmpds(cVOlist, clVO.getListName(), currentUser);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return rtn;

    }

    public static CmpdListVO persistListOfCmpds(List<CmpdVO> cVOlist, String listName, String currentUser) {

        CmpdListVO rtn = new CmpdListVO();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            Date now = new Date();
            Timestamp ts = new Timestamp(now.getTime());
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

            // create a new list
            CmpdList clEnt = CmpdList.Factory.newInstance();

            clEnt.setCmpdListId(cmpdListId);

            if (listName == null || listName.length() == 0) {
                clEnt.setListName(currentUser + " " + now);
            } else {
                clEnt.setListName(listName);
            }

            clEnt.setDateCreated(ts);
            clEnt.setListOwner(currentUser);
            clEnt.setShareWith(currentUser);

            session.persist(clEnt);

            for (CmpdVO cVO : cVOlist) {
                CmpdListMember clm = CmpdListMember.Factory.newInstance();

                Cmpd entCmpd = (Cmpd) session.get(CmpdImpl.class, cVO.getId());

                clm.setCmpd(entCmpd);
                clm.setCmpdList(clEnt);
                session.persist(clm);

                // not needed for persistence, added for quick(er) conversion to VO
                clEnt.getCmpdListMembers().add(clm);
            }

            clEnt.setCountListMembers(clEnt.getCmpdListMembers().size());

            tx.commit();

            Long clId = (Long) session.getIdentifier(clEnt);
            rtn = getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, currentUser);

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }

}
