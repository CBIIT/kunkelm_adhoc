/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import java.util.ArrayList;
import java.util.List;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListImpl;
import mwk.datasystem.domain.CmpdListMember;
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

    Session session = null;
    List<Cmpd> CMPD_ENTITY_LIST = null;

    public HelperCmpdListMember() {
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    public void deleteCmpdListMembers(List<CmpdListMemberVO> forDelete, String currentUser) {

        ArrayList<Long> idsForDelete = new ArrayList<Long>();

        for (CmpdListMemberVO clmVO : forDelete) {
            idsForDelete.add(clmVO.getId());
        }

        try {

            Transaction tx = session.beginTransaction();

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

                    cmpdId = (Long) session.getIdentifier(clm.getCmpd());
                    cmpdIdsForDelete.add(cmpdId);
                    // delete the CmpdListMember        
                    session.delete(clm);

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

            // MWK TODO updte countListMembers



            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void appendCmpdListMembers(CmpdListVO targetList, List<CmpdListMemberVO> forAppend, String currentUser) {

        // targetList

        org.hibernate.Transaction tx = session.beginTransaction();

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

                newClm.setCmpd(clm.getCmpd());
                newClm.setListMemberComment(clm.getListMemberComment());
                newClm.setCmpdList(target);
                
                session.persist(newClm);

            } else {
                System.out.println(currentUser + " doesn't own list containing listMember: " + clm.getId());
            }

        }

        tx.commit();

    }
}
