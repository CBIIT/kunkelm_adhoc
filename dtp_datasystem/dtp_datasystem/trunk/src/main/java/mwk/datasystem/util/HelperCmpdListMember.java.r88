/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.AdHocCmpdFragmentPChem;
import mwk.datasystem.domain.AdHocCmpdFragmentStructure;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.NscCmpdImpl;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpdListMember {

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
                    System.out.println("c: " + c.getClass());

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

            CmpdList target = (CmpdList) clCrit.uniqueResult();

            // members for appending

            ArrayList<Long> idsForAppend = new ArrayList<Long>();

            for (CmpdListMemberVO clmVO : forAppend) {
                idsForAppend.add(clmVO.getId());
            }

            Criteria clmCriteria = session.createCriteria(CmpdListMember.class);
            clmCriteria.add(Restrictions.in("id", idsForAppend));

            List<CmpdListMember> clml = clmCriteria.list();

            for (CmpdListMember clm : clml) {

                // check ownership
                if (clm.getCmpdList().getListOwner().equals(currentUser)) {

                    CmpdListMember newClm = CmpdListMember.Factory.newInstance();

                    // nsc compounds are just added

                    Cmpd c = Unproxy.initializeAndUnproxy(clm.getCmpd());

                    System.out.println("c: " + c.getClass());

                    if (c instanceof NscCmpdImpl) {

                        newClm.setCmpd(c);
                        newClm.setListMemberComment(clm.getListMemberComment());
                        newClm.setCmpdList(target);

                        session.persist(newClm);

                    } else {

                        // helper method, opens another session on-the-side
                        AdHocCmpd ahc = (AdHocCmpd) c;

                        long randomId = randomGenerator.nextLong();
                        if (randomId < 0) {
                            randomId = -1 * randomId;
                        }

                        Long adHocCmpdId = new Long(randomId);

                        AdHocCmpd newAhc = AdHocCmpd.Factory.newInstance();

                        newAhc.setAdHocCmpdId(adHocCmpdId);
                        newAhc.setCmpdOwner(currentUser);
                        newAhc.setName(ahc.getName());
                        newAhc.setOriginalAdHocCmpdId(ahc.getAdHocCmpdId());

                        for (AdHocCmpdFragment ahcf : ahc.getAdHocCmpdFragments()) {

                            AdHocCmpdFragment frag = AdHocCmpdFragment.Factory.newInstance();

                            AdHocCmpdFragmentPChem pchem = AdHocCmpdFragmentPChem.Factory.newInstance();
                            HelperCmpd.replicatePchem(ahcf.getAdHocCmpdFragmentPChem(), pchem);
                            session.persist(pchem);

                            frag.setAdHocCmpdFragmentPChem(pchem);

                            AdHocCmpdFragmentStructure struc = AdHocCmpdFragmentStructure.Factory.newInstance();
                            HelperCmpd.replicateStructure(ahcf.getAdHocCmpdFragmentStructure(), struc);
                            session.persist(struc);

                            frag.setAdHocCmpdFragmentStructure(struc);

                            // persist the fragment                
                            session.persist(frag);

                            // check whether this is parent, and set that while we're at it

                            if (ahc.getAdHocCmpdParentFragment().equals(ahcf)) {
                                newAhc.setAdHocCmpdParentFragment(frag);
                            }

                        }

                        session.persist("Cmpd", newAhc);

                        System.out.println("ahc id: " + ahc.getId());
                        System.out.println("newAhc id: " + newAhc.getId());

                        newClm.setCmpd(newAhc);
                        newClm.setListMemberComment(clm.getListMemberComment());
                        newClm.setCmpdList(target);

                        session.persist(newClm);

                    }

                } else {
                    System.out.println(currentUser + " doesn't own list containing listMember: " + clm.getId());
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
}