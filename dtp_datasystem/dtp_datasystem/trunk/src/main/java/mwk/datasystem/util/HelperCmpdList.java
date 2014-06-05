/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdView;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.FetchMode;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpdList {

    Session session = null;
    List<Cmpd> CMPD_ENTITY_LIST = null;

    public HelperCmpdList() {
        this.session = HibernateUtil.getSessionFactory().getCurrentSession();
    }

    //MWK TODO! javadoc this TODO TODO TODO
    public CmpdListVO getCmpdListByCmpdListId(Long cmpdListId, Boolean includeListMembers, String currentUser) {

        CmpdListVO rtnVO = new CmpdListVO();

        CmpdList entityCL = null;

        try {

            org.hibernate.Transaction tx = session.beginTransaction();

            Criteria clCrit = session.createCriteria(CmpdList.class);
            clCrit.add(Restrictions.eq("cmpdListId", cmpdListId));
            clCrit.add(Restrictions.eq("listOwner", currentUser));
            
            if (includeListMembers) {
                clCrit.setFetchMode("listMembers", FetchMode.JOIN);
            }

            
            entityCL = (CmpdList) clCrit.uniqueResult();

            // just populate the top-level stuff
            
            // MWK WHY/HOW does using the TransformCmpdView method speed things up so much?
            
            rtnVO = TransformCmpdViewToVO.toCmpdListVO(entityCL, includeListMembers);

            // HashMap for holding cmpdId numbers and their respective CmpdListMemberVO
            // for resolution AFTER fetching CmpdView by cmpdIdList

            HashMap<Long, CmpdListMemberVO> map = new HashMap<Long, CmpdListMemberVO>();

            List<Long> cmpdIdList = new ArrayList<Long>();
            Long cmpdId = null;

            for (CmpdListMember clm : entityCL.getCmpdListMembers()) {
                cmpdId = (Long) session.getIdentifier(clm.getCmpd());
                cmpdIdList.add(cmpdId);
                map.put(cmpdId, TransformCmpdViewToVO.toCmpdListMemberVO(clm));
            }

            // fetch a list of cmpdViews
            Criteria cvCrit = session.createCriteria(CmpdView.class);
            cvCrit.add(Restrictions.in("id", cmpdIdList));
            List<CmpdView> entityCVlist = (List<CmpdView>) cvCrit.list();

            for (CmpdView cv : entityCVlist) {
                // translate to VO
                CmpdVO cVO = TransformCmpdViewToVO.toCmpdVO(cv);
                // add the VO to appropriate HashMap (CellLineMemberVO)        
                map.get(cVO.getId()).setCmpd(cVO);
            }

            ArrayList<CmpdListMemberVO> memberList = new ArrayList<CmpdListMemberVO>(map.values());

            rtnVO.setCmpdListMembers(memberList);

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtnVO;

    }

    public List<CmpdListVO> showAvailableCmpdLists(String currentUser) {

        List<CmpdListVO> voList = new ArrayList<CmpdListVO>();
        List<CmpdList> entityList = null;

        try {

            Transaction tx = session.beginTransaction();
            Criteria c = session.createCriteria(CmpdList.class);
            c.add(Restrictions.disjunction()
                    .add(Restrictions.eq("listOwner", currentUser))
                    .add(Restrictions.eq("shareWith", "PUBLIC")));
            entityList = (List<CmpdList>) c.list();

            voList = TransformAndroToVO.translateCmpdLists(entityList, Boolean.FALSE);

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return voList;

    }

    public void updateCmpdList(CmpdListVO cL, String currentUser) {

        try {

            Transaction tx = session.beginTransaction();
            Criteria c = session.createCriteria(CmpdList.class);
            c.add(Restrictions.eq("cmpdListId", cL.getCmpdListId()));
            c.add(Restrictions.eq("listOwner", currentUser));
            
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
            e.printStackTrace();
        }

    }

    public void makeCmpdListPublic(Long cmpdListId, String currentUser) {

        try {

            Transaction tx = session.beginTransaction();
            Criteria c = session.createCriteria(CmpdList.class);
            c.add(Restrictions.eq("cmpdListId", cmpdListId));
            c.add(Restrictions.eq("listOwner", currentUser));
            
            CmpdList cl = (CmpdList) c.uniqueResult();

            cl.setShareWith("PUBLIC");
            session.update(cl);

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void deleteCmpdListByCmpdListId(Long cmpdListId, String currentUser) {

        try {

            Transaction tx = session.beginTransaction();

            Criteria clCriteria = session.createCriteria(CmpdList.class);
            clCriteria.add(Restrictions.eq("cmpdListId", cmpdListId));
            clCriteria.add(Restrictions.eq("listOwner", currentUser));
            
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
            
            //
            // MWK TODO handle ownership of AdHocCmpds?

            // find and delete those cmpd with join to ahc
            Criteria ahcCriteria = session.createCriteria(AdHocCmpd.class);
            ahcCriteria.add(Restrictions.in("id", idListForDelete));

            List<AdHocCmpd> ahcList = (List<AdHocCmpd>) ahcCriteria.list();

            for (AdHocCmpd ahc : ahcList) {
                session.delete(ahc);
            }

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
