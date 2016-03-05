/*
 * To change this template, choose Tools | Templates
 */
package mwk.datasystem.util;

import java.util.ArrayList;
import java.util.List;
import mwk.datasystem.domain.AdHocCmpd;
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

    public static CuratedNameVO createNewCuratedName(CuratedNameVO voIn) {

        System.out.println("In createNewCuratedName in HelperCuratedNsc");
        System.out.println("voIn: " + voIn.toString());
        
        CuratedNameVO rtn = null;

        Session session = null;
        Transaction tx = null;
        
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            CuratedName newName = CuratedName.Factory.newInstance();

            newName.setValue(voIn.getValue());
            newName.setDescription(voIn.getDescription());
            newName.setReference(voIn.getReference());

            session.save(newName);
            Long theId = (Long) session.getIdentifier(newName);

            System.out.println("In HelperCuratedNsc theId: " + theId);
            
            session.flush();
            session.evict(newName);
            session.load(newName, theId);

            rtn = TransformAndroToVO.translateCuratedName(newName);
                        
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return rtn;
    }
    
    public static CuratedOriginatorVO createNewCuratedOriginator(CuratedOriginatorVO voIn) {

        System.out.println("In createNewCuratedOriginator in HelperCuratedNsc");
        System.out.println("voIn: " + voIn.toString());
        
        CuratedOriginatorVO rtn = null;

        Session session = null;
        Transaction tx = null;
        
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            CuratedOriginator newOriginator = CuratedOriginator.Factory.newInstance();

            newOriginator.setValue(voIn.getValue());
            newOriginator.setDescription(voIn.getDescription());
            newOriginator.setReference(voIn.getReference());

            session.save(newOriginator);
            Long theId = (Long) session.getIdentifier(newOriginator);

            System.out.println("In HelperCuratedNsc theId: " + theId);
            
            session.flush();
            session.evict(newOriginator);
            session.load(newOriginator, theId);

            rtn = TransformAndroToVO.translateCuratedOriginator(newOriginator);
                        
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return rtn;
    }
    
    public static CuratedProjectVO createNewCuratedProject(CuratedProjectVO voIn) {

        System.out.println("In createNewCuratedProject in HelperCuratedNsc");
        System.out.println("voIn: " + voIn.toString());
        
        CuratedProjectVO rtn = null;

        Session session = null;
        Transaction tx = null;
        
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            CuratedProject newProject = CuratedProject.Factory.newInstance();

            newProject.setValue(voIn.getValue());
            newProject.setDescription(voIn.getDescription());
            newProject.setReference(voIn.getReference());

            session.save(newProject);
            Long theId = (Long) session.getIdentifier(newProject);

            System.out.println("In HelperCuratedNsc theId: " + theId);
            
            session.flush();
            session.evict(newProject);
            session.load(newProject, theId);

            rtn = TransformAndroToVO.translateCuratedProject(newProject);
                        
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return rtn;
    }

    public static CuratedTargetVO createNewCuratedTarget(CuratedTargetVO voIn) {

        System.out.println("In createNewCuratedTarget in HelperCuratedNsc");
        System.out.println("voIn: " + voIn.toString());
        
        CuratedTargetVO rtn = null;

        Session session = null;
        Transaction tx = null;
        
        try {

            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            CuratedTarget newTarget = CuratedTarget.Factory.newInstance();

            newTarget.setValue(voIn.getValue());
            newTarget.setDescription(voIn.getDescription());
            newTarget.setReference(voIn.getReference());

            session.save(newTarget);
            Long theId = (Long) session.getIdentifier(newTarget);

            System.out.println("In HelperCuratedNsc theId: " + theId);
            
            session.flush();
            session.evict(newTarget);
            session.load(newTarget, theId);

            rtn = TransformAndroToVO.translateCuratedTarget(newTarget);
                        
            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return rtn;
    }
    
}
