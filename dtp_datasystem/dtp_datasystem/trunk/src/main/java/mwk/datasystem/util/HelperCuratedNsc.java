/*
 * To change this template, choose Tools | Templates
 */
package mwk.datasystem.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import mwk.datasystem.domain.CuratedName;
import mwk.datasystem.domain.CuratedNameImpl;
import mwk.datasystem.domain.CuratedNsc;
import mwk.datasystem.domain.CuratedNscImpl;
import mwk.datasystem.domain.CuratedOriginator;
import mwk.datasystem.domain.CuratedOriginatorImpl;
import mwk.datasystem.domain.CuratedProject;
import mwk.datasystem.domain.CuratedProjectImpl;
import mwk.datasystem.domain.CuratedTarget;
import mwk.datasystem.domain.CuratedTargetImpl;
import mwk.datasystem.vo.CuratedNameVO;
import mwk.datasystem.vo.CuratedNscVO;
import mwk.datasystem.vo.CuratedOriginatorVO;
import mwk.datasystem.vo.CuratedProjectVO;
import mwk.datasystem.vo.CuratedTargetVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;

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
            crit.setResultTransformer(Criteria.DISTINCT_ROOT_ENTITY);
            List<CuratedNsc> newEnts = (List<CuratedNsc>) crit.list();
            rtnList.addAll(TransformAndroToVO.translateCuratedNscs(newEnts));
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
            List<CuratedName> newEnts = (List<CuratedName>) crit.list();
            rtnList.addAll(TransformAndroToVO.translateCuratedNames(newEnts));
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
            List<CuratedOriginator> newEnts = (List<CuratedOriginator>) crit.list();
            rtnList.addAll(TransformAndroToVO.translateCuratedOriginators(newEnts));
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
            List<CuratedProject> newEnts = (List<CuratedProject>) crit.list();
            rtnList.addAll(TransformAndroToVO.translateCuratedProjects(newEnts));
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
            List<CuratedTarget> newEnts = (List<CuratedTarget>) crit.list();
            rtnList.addAll(TransformAndroToVO.translateCuratedTargets(newEnts));
            tx.commit();
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }
        return rtnList;
    }

    public static CuratedNscVO createNewCuratedNsc(CuratedNscVO voIn) {

        System.out.println("In createNewCuratedNsc in HelperCuratedNsc");
        System.out.println("voIn: " + voIn.toString());

        CuratedNscVO rtn = null;

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            CuratedNsc newNsc = CuratedNsc.Factory.newInstance();

            newNsc.setNsc(voIn.getNsc());
            newNsc.setCas(voIn.getCas());

            session.save(newNsc);
            Long theId = (Long) session.getIdentifier(newNsc);

            System.out.println("In HelperCuratedNsc theId: " + theId);

            session.flush();
            session.evict(newNsc);

            newNsc = (CuratedNsc) session.get(CuratedNscImpl.class, theId);

            rtn = TransformAndroToVO.translateCuratedNsc(newNsc);

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

        return rtn;
    }

    public static void updateCuratedNsc(CuratedNscVO voIn) {

        System.out.println("In updateCuratedNsc in HelperCuratedNsc");
        System.out.println("voIn: " + voIn.toString());

        CuratedNscVO rtn = null;

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            CuratedNsc ent = (CuratedNsc) session.get(CuratedNscImpl.class, voIn.getId());

            if (voIn.getPreferredName() != null) {
                CuratedName pn = (CuratedName) session.get(CuratedNameImpl.class, voIn.getPreferredName().getId());
                ent.setPreferredName(pn);
            } else {
                ent.setPreferredName(null);
            }

            if (voIn.getGenericName() != null) {
                CuratedName gn = (CuratedName) session.get(CuratedNameImpl.class, voIn.getGenericName().getId());
                ent.setGenericName(gn);
            } else {
                ent.setGenericName(null);
            }

            if (voIn.getAliases() != null && !voIn.getAliases().isEmpty()) {

                List<CuratedName> newEnts = new ArrayList<CuratedName>();
                for (CuratedNameVO cnVO : voIn.getAliases()) {
                    CuratedName cn = (CuratedName) session.get(CuratedNameImpl.class, cnVO.getId());
                    newEnts.add(cn);
                }

                List<CuratedName> toRemove = new ArrayList<CuratedName>(ent.getAliases());
                toRemove.removeAll(newEnts);

                List<CuratedName> toAdd = new ArrayList<CuratedName>(newEnts);
                toAdd.removeAll(ent.getAliases());

                for (CuratedName cn : toAdd) {
                    ent.getAliases().add(cn);
                    cn.getCuratedNscToAliases().add(ent);
                }

                for (CuratedName cn : toRemove) {
                    ent.getAliases().remove(cn);
                    cn.getCuratedNscToAliases().remove(ent);
                }

            } else {

                List<CuratedName> toRemove = new ArrayList<CuratedName>(ent.getAliases());
                for (CuratedName cn : toRemove) {
                    ent.getAliases().remove(cn);
                    cn.getCuratedNscToAliases().remove(ent);
                }

            }

            if (voIn.getOriginator() != null) {
                CuratedOriginator co = (CuratedOriginator) session.get(CuratedOriginatorImpl.class, voIn.getOriginator().getId());
                ent.setOriginator(co);
            } else {
                ent.setOriginator(null);
            }

            if (voIn.getProjects() != null && !voIn.getProjects().isEmpty()) {

                List<CuratedProject> newEnts = new ArrayList<CuratedProject>();
                for (CuratedProjectVO cpVO : voIn.getProjects()) {
                    CuratedProject cp = (CuratedProject) session.get(CuratedProjectImpl.class, cpVO.getId());
                    newEnts.add(cp);
                }

                List<CuratedProject> toRemove = new ArrayList<CuratedProject>(ent.getProjects());
                toRemove.removeAll(newEnts);

                List<CuratedProject> toAdd = new ArrayList<CuratedProject>(newEnts);
                toAdd.removeAll(ent.getProjects());

                for (CuratedProject cp : toAdd) {
                    ent.getProjects().add(cp);
                    cp.getCuratedNscs().add(ent);
                }

                for (CuratedProject cp : toRemove) {
                    ent.getProjects().remove(cp);
                    cp.getCuratedNscs().remove(ent);
                }

            } else {

                List<CuratedProject> toRemove = new ArrayList<CuratedProject>(ent.getProjects());
                for (CuratedProject cp : toRemove) {
                    ent.getProjects().remove(cp);
                    cp.getCuratedNscs().remove(ent);
                }

            }

            if (voIn.getPrimaryTarget() != null) {
                CuratedTarget ct = (CuratedTarget) session.get(CuratedTargetImpl.class, voIn.getPrimaryTarget().getId());
                ent.setPrimaryTarget(ct);
            } else {
                ent.setPrimaryTarget(null);
            }

            if (voIn.getSecondaryTargets() != null && !voIn.getSecondaryTargets().isEmpty()) {

                List<CuratedTarget> newEnts = new ArrayList<CuratedTarget>();
                for (CuratedTargetVO ctVO : voIn.getSecondaryTargets()) {
                    CuratedTarget ct = (CuratedTarget) session.get(CuratedTargetImpl.class, ctVO.getId());
                    newEnts.add(ct);
                }

                List<CuratedTarget> toRemove = new ArrayList<CuratedTarget>(ent.getSecondaryTargets());
                toRemove.removeAll(newEnts);

                List<CuratedTarget> toAdd = new ArrayList<CuratedTarget>(newEnts);
                toAdd.removeAll(ent.getSecondaryTargets());

                for (CuratedTarget ct : toAdd) {
                    ent.getSecondaryTargets().add(ct);
                    ct.getCuratedNscToSecondaryTargets().add(ent);
                }

                for (CuratedTarget ct : toRemove) {
                    ent.getSecondaryTargets().remove(ct);
                    ct.getCuratedNscToSecondaryTargets().remove(ent);
                }

            } else {

                List<CuratedTarget> toRemove = new ArrayList<CuratedTarget>(ent.getSecondaryTargets());
                for (CuratedTarget ct : toRemove) {
                    ent.getSecondaryTargets().remove(ct);
                    ct.getCuratedNscToSecondaryTargets().remove(ent);
                }

            }

            session.update(ent);

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }

    }

    public static void deleteCuratedNsc(CuratedNscVO cnVO) {

        System.out.println("In delete CuratedNsc in HelperCuratedNsc");
        System.out.println("cnVO: " + cnVO.toString());

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();
            tx = session.beginTransaction();

            Criteria cnCrit = session.createCriteria(CuratedNsc.class)
                    .add(Restrictions.eq("id", cnVO.getId()));

            CuratedNsc entity = (CuratedNsc) cnCrit.uniqueResult();
            session.delete(entity);

            tx.commit();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            session.close();
        }
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

            newName = (CuratedName) session.get(CuratedNameImpl.class, theId);

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

            newOriginator = (CuratedOriginator) session.get(CuratedOriginatorImpl.class, theId);

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

            newProject = (CuratedProject) session.get(CuratedProjectImpl.class, theId);

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

            newTarget = (CuratedTarget) session.get(CuratedTargetImpl.class, theId);

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
