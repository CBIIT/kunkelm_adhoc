/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.AdHocCmpdFragmentPChem;
import mwk.datasystem.domain.AdHocCmpdFragmentStructure;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.NscCmpd;
import mwk.datasystem.domain.NscCmpdImpl;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpdListMember {

    private static Boolean DEBUG = Boolean.TRUE;

    public void deleteCmpdListMembers(CmpdListVO targetList, List<CmpdListMemberVO> forDelete, String currentUser) {

        ArrayList<Long> idsForDelete = new ArrayList<Long>();
        for (CmpdListMemberVO clmVO : forDelete) {
            idsForDelete.add(clmVO.getId());
        }

        Session session = null;
        Transaction tx = null;
        Transaction tx2 = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Criteria clCrit = session.createCriteria(CmpdList.class);
            clCrit.add(Restrictions.eq("cmpdListId", targetList.getCmpdListId()));
            clCrit.add(Restrictions.eq("listOwner", currentUser));
            clCrit.add(Restrictions.ne("shareWith", "PUBLIC"));

            CmpdList target = (CmpdList) clCrit.uniqueResult();

            Criteria clmCriteria = session.createCriteria(CmpdListMember.class);
            clmCriteria.add(Restrictions.in("id", idsForDelete));

            List<CmpdListMember> clml = clmCriteria.list();

            //have to programatically determine whether any ad_hoc_cmpds
            //and delete those

            Long cmpdId;
            ArrayList<Long> cmpdIdsForDelete = new ArrayList<Long>();

            for (CmpdListMember clm : clml) {

                // check ownership

                if (clm.getCmpdList().getListOwner().equals(currentUser)) {

                    Cmpd c = Unproxy.initializeAndUnproxy(clm.getCmpd());
                    if (DEBUG) {
                        System.out.println("c: " + c.getClass());
                    }

                    // NSC compounds -> just delete the list member

                    if (c instanceof NscCmpdImpl) {

                        session.delete(clm);

                    } else {

                        session.delete(clm);
                        session.delete(c);

                    }

                } else {
                    System.out.println(currentUser + " doesn't own list containing listMember: " + clm.getId());
                }

            }

            // MWK TODO handle ownership of AdHocCmpds?

            if (cmpdIdsForDelete.size() > 0) {

                // find and delete those cmpd with join to ahc
                Criteria ahcCriteria = session.createCriteria(AdHocCmpd.class);
                ahcCriteria.add(Restrictions.in("id", cmpdIdsForDelete));

                List<AdHocCmpd> ahcList = (List<AdHocCmpd>) ahcCriteria.list();

                for (AdHocCmpd ahc : ahcList) {
                    session.delete(ahc);
                }

            }

            tx.commit();

            // update the cmpdList size            

            tx2 = session.beginTransaction();

            int newSize = target.getCmpdListMembers().size();
            target.setCountListMembers(newSize);
            session.update(target);

            tx2.commit();


        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            if (tx2.isActive()) {
                tx2.rollback();
            }
        } finally {
            session.close();
        }

    }

    public void appendCmpdListMembers(CmpdListVO targetList, List<CmpdListMemberVO> forAppend, String currentUser) {

        Random randomGenerator = new Random();

        Session session = null;
        Transaction tx = null;
        Transaction tx2 = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Criteria clCrit = session.createCriteria(CmpdList.class);
            clCrit.add(Restrictions.eq("cmpdListId", targetList.getCmpdListId()));
            clCrit.add(Restrictions.eq("listOwner", currentUser));
            clCrit.add(Restrictions.ne("shareWith", "PUBLIC"));

            CmpdList target = (CmpdList) clCrit.uniqueResult();

            int beginningSize = target.getCountListMembers();

            // does NOT allow duplicates
            // by either NSC for NscCmpd or original_ad_hoc_cmpd_id

            HashSet<Integer> nscSet = new HashSet<Integer>();
            HashSet<Long> origAhcIdSet = new HashSet<Long>();

            for (CmpdListMember clm : target.getCmpdListMembers()) {
                Cmpd c = Unproxy.initializeAndUnproxy(clm.getCmpd());
                if (DEBUG) {
                    System.out.println("c: " + c.getClass());
                }
                if (c instanceof NscCmpdImpl) {
                    NscCmpd n = (NscCmpd) c;
                    nscSet.add(n.getNsc());
                } else {
                    AdHocCmpd ahc = (AdHocCmpd) c;
                    origAhcIdSet.add(ahc.getOriginalAdHocCmpdId());
                }
            }

            // members for appending
            ArrayList<Long> clmIdsForAppend = new ArrayList<Long>();

            ArrayList<Long> clmIdsThatAreDups = new ArrayList<Long>();

            for (CmpdListMemberVO clmVO : forAppend) {

                if (clmVO.getCmpd().getNsc() != null) {

                    if (!nscSet.contains(clmVO.getCmpd().getNsc())) {
                        clmIdsForAppend.add(clmVO.getId());
                    } else {
                        clmIdsThatAreDups.add(clmVO.getId());
                    }

                } else {

                    if (!origAhcIdSet.contains(clmVO.getCmpd().getOriginalAdHocCmpdId())) {
                        clmIdsForAppend.add(clmVO.getId());
                    } else {
                        clmIdsThatAreDups.add(clmVO.getId());
                    }
                }

            }

            // update the size of the list

            int newSize = beginningSize + clmIdsForAppend.size();

            // get the list members            
            Criteria clmCriteria = session.createCriteria(CmpdListMember.class);
            clmCriteria.add(Restrictions.in("id", clmIdsForAppend));
            List<CmpdListMember> clml = clmCriteria.list();

            // set up for tracking            
            List<AdHocCmpd> ahcForCmpdTable = new ArrayList<AdHocCmpd>();

            // NscCmpd is just referenced by the clm
            // AdHocCmpd have to be replicated first, AND they have to be persisted to CmpdTable

            for (CmpdListMember clm : clml) {

                // check ownership
                if (clm.getCmpdList().getListOwner().equals(currentUser)) {

                    Cmpd c = Unproxy.initializeAndUnproxy(clm.getCmpd());
                    if (DEBUG) {
                        System.out.println("c: " + c.getClass());
                    }

                    CmpdListMember newClm = CmpdListMember.Factory.newInstance();

                    // nsc compounds are just added
                    if (c instanceof NscCmpdImpl) {

                        newClm.setCmpd(c);
                        newClm.setListMemberComment(clm.getListMemberComment());
                        newClm.setCmpdList(target);

                        session.persist(newClm);

                    } else {

                        AdHocCmpd ahc = (AdHocCmpd) c;

                        AdHocCmpd newAhc = AdHocCmpd.Factory.newInstance();

                        long randomId = randomGenerator.nextLong();
                        if (randomId < 0) {
                            randomId = -1 * randomId;
                        }
                        Long newRandomId = new Long(randomId);

                        newAhc = AdHocCmpd.Factory.newInstance();

                        // this is an existing AdHocCmpd
                        // harvest existing originalAdHocCmpdId                        
                        newAhc.setAdHocCmpdId(newRandomId);
                        newAhc.setOriginalAdHocCmpdId(ahc.getOriginalAdHocCmpdId());
                        
                        newAhc.setCmpdOwner(currentUser);
                        newAhc.setName(ahc.getName());

                        if (DEBUG) {
                            System.out.println("cmpdOwner: " + newAhc.getCmpdOwner());
                        }

                        session.persist("Cmpd", newAhc);

                        Set<AdHocCmpdFragment> entityFragSet = new HashSet<AdHocCmpdFragment>();

                        for (AdHocCmpdFragment ahcf : ahc.getAdHocCmpdFragments()) {

                            AdHocCmpdFragmentPChem pchem = AdHocCmpdFragmentPChem.Factory.newInstance();
                            HelperAdHocCmpd.replicatePchem(ahcf.getAdHocCmpdFragmentPChem(), pchem);
                            session.persist(pchem);

                            AdHocCmpdFragmentStructure struc = AdHocCmpdFragmentStructure.Factory.newInstance();
                            HelperAdHocCmpd.replicateStructure(ahcf.getAdHocCmpdFragmentStructure(), struc);
                            session.persist(struc);

                            AdHocCmpdFragment frag = AdHocCmpdFragment.Factory.newInstance();
                            frag.setAdHocCmpdFragmentPChem(pchem);
                            frag.setAdHocCmpdFragmentStructure(struc);

                            // persist the fragment                
                            session.persist(frag);

                            // check whether this is parent, and set that while we're at it

                            if (ahc.getAdHocCmpdParentFragment().equals(ahcf)) {
                                newAhc.setAdHocCmpdParentFragment(frag);
                            }

                            entityFragSet.add(frag);

                        }

                        newAhc.setAdHocCmpdFragments(entityFragSet);

                        // add it to the list to be inserted to CmpdTable
                        ahcForCmpdTable.add(newAhc);

                        if (DEBUG) {
                            System.out.println("ahc id: " + ahc.getId());
                        }
                        if (DEBUG) {
                            System.out.println("newAhc id: " + newAhc.getId());
                        }

                        newClm.setCmpd(newAhc);
                        newClm.setListMemberComment(clm.getListMemberComment());
                        newClm.setCmpdList(target);

                        if (DEBUG) {
                            System.out.println("checkpoint: 3");
                        }

                        session.persist(newClm);

                        if (DEBUG) {
                            System.out.println("checkpoint: 4");
                        }

                    }

                } else {
                    System.out.println(currentUser + " doesn't own list containing listMember: " + clm.getId());
                }

            }

            // update the cmpdList size       

            if (DEBUG) {
                System.out.println("checkpoint: BEFORE size");
                System.out.println("beginningSize was: " + beginningSize);
                System.out.println("newSize is: " + newSize);
            }

            target.setCountListMembers(newSize);
            session.merge(target);

            if (DEBUG) {
                System.out.println("checkpoint: AFTER size");
            }

            if (DEBUG) {
                System.out.println("checkpoint: BEFORE commit");
            }

            tx.commit();

            if (DEBUG) {
                System.out.println("checkpoint: AFTER commit");
            }

            // now persist new ahc to CmpdTable
            // transaction is managed from HelperCmpdTable

            if (DEBUG) {
                System.out.println("checkpoint: BEFORE adHocCmpdsToCmpdTable");
            }

            HelperCmpdTable.adHocCmpdsToCmpdTable(ahcForCmpdTable);

            if (DEBUG) {
                System.out.println("checkpoint: AFTER adHocCmpdsToCmpdTable");
            }


        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
            if (tx2.isActive()) {
                tx2.rollback();
            }
        } finally {
            session.close();
        }
    }
}
