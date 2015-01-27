/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.NscCmpdImpl;
import static mwk.datasystem.util.HelperCmpdList.DEBUG;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.Unproxy;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

/**
 *
 * @author mwkunkel
 */
public class Main {

  public static void main(String[] args) {

    deleteCmpdListByCmpdListId(1101505437942198950l, "kunkelm");

    CmpdListVO clVO = new CmpdListVO();
    clVO.setCmpdListId(7139189152107868606l);

    ArrayList<CmpdListMemberVO> clmVOlist = new ArrayList<CmpdListMemberVO>();
    CmpdListMemberVO clmVO = new CmpdListMemberVO();
    clmVO.setId(120024l);
    clmVOlist.add(clmVO);
    clmVO = new CmpdListMemberVO();
    clmVO.setId(120025l);
    clmVOlist.add(clmVO);
    clmVO = new CmpdListMemberVO();
    clmVO.setId(120026l);
    clmVOlist.add(clmVO);
    clmVO = new CmpdListMemberVO();
    clmVO.setId(120027l);
    clmVOlist.add(clmVO);
    clmVO = new CmpdListMemberVO();
    clmVO.setId(120028l);
    clmVOlist.add(clmVO);
    clmVO = new CmpdListMemberVO();
    clmVO.setId(120029l);
    clmVOlist.add(clmVO);
    clmVO = new CmpdListMemberVO();
    clmVO.setId(120030l);
    clmVOlist.add(clmVO);
    clmVO = new CmpdListMemberVO();
    clmVO.setId(120031l);
    clmVOlist.add(clmVO);
    clmVO = new CmpdListMemberVO();
    clmVO.setId(120032l);
    clmVOlist.add(clmVO);
    
    deleteCmpdListMembers(clVO, clmVOlist, "kunkelm");

  }

  public static void deleteCmpdListMembers(
          CmpdListVO targetList,
          List<CmpdListMemberVO> forDelete,
          String currentUser) {

    Session session = null;
    Transaction tx = null;
    Transaction tx2 = null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();

      tx = session.beginTransaction();

      Criteria clCrit = session.createCriteria(CmpdList.class);
      clCrit.add(Restrictions.eq("cmpdListId", targetList.getCmpdListId()));
      clCrit.add(Restrictions.eq("listOwner", currentUser));
      // can NOT delete PUBLIC 
      clCrit.add(Restrictions.ne("shareWith", "PUBLIC"));

      CmpdList target = (CmpdList) clCrit.uniqueResult();

      Long targetListId = target.getId();

      ArrayList<Long> clmIdsForDelete = new ArrayList<Long>();
      ArrayList<Long> cmpdIdsForDelete = new ArrayList<Long>();

      for (CmpdListMemberVO clmVO : forDelete) {
        clmIdsForDelete.add(clmVO.getId());
        if (clmVO.getCmpd().getAdHocCmpdId() != null) {
          cmpdIdsForDelete.add(clmVO.getCmpd().getId());
        }
      }

      // delete the cmpdListMembers
      Query q = session.createSQLQuery("delete from cmpd_list_member where id = :clmId");
      q.setParameter("clmId", clmIdsForDelete);
      q.executeUpdate();

      // if there were any adHocCmpds, delete them
      if (!cmpdIdsForDelete.isEmpty()) {

        q = session.createSQLQuery("delete from ad_hoc_cmpd_fragment_p_chem "
                + "where id in ("
                + "select ad_hoc_cmpd_fragment_p_chem_fk "
                + "from ad_hoc_cmpd_fragment "
                + "where ad_hoc_cmpd_fk in (:idList)"
                + ")");
        q.setParameterList("idList", cmpdIdsForDelete);
        q.executeUpdate();
        
        q = session.createSQLQuery("delete from ad_hoc_cmpd_fragment_structure "
                + "where id in ("
                + "select ad_hoc_cmpd_fragment_struct_fk "
                + "from ad_hoc_cmpd_fragment "
                + "where ad_hoc_cmpd_fk in (:idList)"
                + ")");
        q.setParameterList("idList", cmpdIdsForDelete);
        q.executeUpdate();
        
        q = session.createSQLQuery("delete from ad_hoc_cmpd_fragment "
                + "where ad_hoc_cmpd_fk in (:idList)");
        q.setParameterList("idList", cmpdIdsForDelete);
        q.executeUpdate();
        
        q = session.createSQLQuery("delete from ad_hoc_cmpd where id in (:idList)");
        q.setParameterList("idList", cmpdIdsForDelete);
        q.executeUpdate();

        // also delete from cmpd_table
        q = session.createSQLQuery("delete from cmpd_table where id in (:idList)");
        q.setParameterList("idList", cmpdIdsForDelete);
        q.executeUpdate();

      }

      tx.commit();

      // update the cmpdList size            
      tx2 = session.beginTransaction();

      q = session.createSQLQuery("update cmpd_list set count_list_members = (select count(*) from cmpd_list_member where cmpd_list_fk = :clId) where id = :clId");
      q.setParameterList("clId", cmpdIdsForDelete);
      q.executeUpdate();

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

  public static void deleteCmpdListByCmpdListId(Long cmpdListId, String currentUser) {

    Session session = null;
    Transaction tx = null;

    try {

      session = HibernateUtil.getSessionFactory().openSession();

      tx = session.beginTransaction();

      Criteria clCrit = session.createCriteria(CmpdList.class);
      clCrit.add(Restrictions.eq("cmpdListId", cmpdListId));
      clCrit.add(Restrictions.eq("listOwner", currentUser));
      // can NOT delete a PUBLIC list
      clCrit.add(Restrictions.ne("shareWith", "PUBLIC"));

      CmpdList target = (CmpdList) clCrit.uniqueResult();

      Long actualListId = target.getId();

      Collection<CmpdListMember> clml = target.getCmpdListMembers();

      ArrayList<Long> cmpdIdsForDelete = new ArrayList<Long>();

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

          cmpdIdsForDelete.add(c.getId());

        }

      }

      // delete the cmpdListMembers
      Query q = session.createSQLQuery("delete from cmpd_list_member where cmpd_list_fk = :clId");
      q.setParameter("clId", actualListId);
      q.executeUpdate();

      // if there were any adHocCmpds, delete them
      if (!cmpdIdsForDelete.isEmpty()) {

        q = session.createSQLQuery("delete from ad_hoc_cmpd where id in (:idList)");
        q.setParameterList("idList", cmpdIdsForDelete);
        q.executeUpdate();

        q = session.createSQLQuery("delete from cmpd_table where id in (:idList)");
        q.setParameterList("idList", cmpdIdsForDelete);
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

}
