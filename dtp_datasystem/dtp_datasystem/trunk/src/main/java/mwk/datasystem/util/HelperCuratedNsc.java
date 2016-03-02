/*
 * To change this template, choose Tools | Templates
 */
package mwk.datasystem.util;

import java.util.ArrayList;
import java.util.List;
import mwk.datasystem.domain.CuratedName;
import mwk.datasystem.domain.CuratedNsc;
import mwk.datasystem.domain.CuratedOriginator;
import mwk.datasystem.domain.CuratedProject;
import mwk.datasystem.domain.CuratedTarget;
import mwk.datasystem.vo.CuratedNameVO;
import mwk.datasystem.vo.CuratedNscVO;
import mwk.datasystem.vo.CuratedOriginatorVO;
import mwk.datasystem.vo.CuratedProjectVO;
import mwk.datasystem.vo.CuratedTargetVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author mwkunkel
 */
public class HelperCuratedNsc {

    public static List<CuratedNscVO> loadAllCuratedNsc() {

        System.out.println("In loadAllCuratedNsc.");

        ArrayList<CuratedNscVO> rtnList = new ArrayList<CuratedNscVO>();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria crit = session.createCriteria(CuratedNsc.class);
            List<CuratedNsc> entList = (List<CuratedNsc>) crit.list();
            rtnList.addAll(TransformAndroToVO.translateCuratedNscs(entList));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rtnList;
    }

    public static List<CuratedNameVO> loadAllNames() {

        System.out.println("In loadAllNames.");

        ArrayList<CuratedNameVO> rtnList = new ArrayList<CuratedNameVO>();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria crit = session.createCriteria(CuratedName.class);
            List<CuratedName> entList = (List<CuratedName>) crit.list();
            rtnList.addAll(TransformAndroToVO.translateCuratedNames(entList));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rtnList;
    }

    public static List<CuratedOriginatorVO> loadAllOriginators() {

        System.out.println("In loadAllOriginators.");

        ArrayList<CuratedOriginatorVO> rtnList = new ArrayList<CuratedOriginatorVO>();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria crit = session.createCriteria(CuratedOriginator.class);
            List<CuratedOriginator> entList = (List<CuratedOriginator>) crit.list();
            rtnList.addAll(TransformAndroToVO.translateCuratedOriginators(entList));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rtnList;
    }

    public static List<CuratedProjectVO> loadAllProjects() {

        System.out.println("In loadAllProjects.");

        ArrayList<CuratedProjectVO> rtnList = new ArrayList<CuratedProjectVO>();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria crit = session.createCriteria(CuratedProject.class);
            List<CuratedProject> entList = (List<CuratedProject>) crit.list();            
                rtnList.addAll(TransformAndroToVO.translateCuratedProjects(entList));            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rtnList;
    }

    public static List<CuratedTargetVO> loadAllTargets() {

        System.out.println("In loadAllTargets.");

        ArrayList<CuratedTargetVO> rtnList = new ArrayList<CuratedTargetVO>();
        Session session = null;
        Transaction tx = null;
        try {
            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();
            Criteria crit = session.createCriteria(CuratedTarget.class);
            List<CuratedTarget> entList = (List<CuratedTarget>) crit.list();            
                rtnList.addAll(TransformAndroToVO.translateCuratedTargets(entList));            
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rtnList;
    }
}
