/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.controllers.SearchCriteriaBean;
import mwk.datasystem.domain.CmpdFragment;
import mwk.datasystem.domain.CmpdImpl;
import mwk.datasystem.vo.CmpdFragmentVO;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.Query;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;
import org.hibernate.impl.SessionImpl;
import org.hibernate.jdbc.Work;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpd {

    public static void updateCmpdFragment(CmpdFragmentVO cfVO) {

        System.out.println("In updateCmpdFragment in HelperCmpd");

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            Criteria crit = session.createCriteria(CmpdFragment.class);
            crit.setMaxResults(2000);
            crit.add(Restrictions.eq("id", cfVO.getId()));

            CmpdFragment cfEntity = (CmpdFragment) crit.uniqueResult();

            if (cfEntity != null) {

                if (cfVO.getStoichiometry() != null) {
                    cfEntity.setStoichiometry(cfVO.getStoichiometry());
                }

                session.update(cfEntity);

            } else {
                System.out.println("cfEntity is null in updateCmpdFragment in HelperCmpd.");
            }

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

    }

    public static CmpdVO fetchFullCmpd(Integer id, String currentUser) {

        CmpdVO rtn = new CmpdVO();

        System.out.println("In fetchFullCmpd in HelperCmpd.");

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Criteria crit = session.createCriteria(Cmpd.class);
            crit.setMaxResults(2000);

            crit.add(Restrictions.eq("nsc", id));

            Cmpd cmpd = (Cmpd) crit.uniqueResult();

            rtn = TransformAndroToVO.translateCmpd(cmpd);

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
     * @param listName
     * @param nscIntList
     * @param smilesString
     * @param currentUser
     * @return Called by structure and similarity search
     */
    public static Long createCmpdListByNscs(
            String listName,
            List<Integer> nscIntList,
            String smilesString,
            String currentUser) {

        Long rtn = Long.valueOf(-1);

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            Date now = new Date();
            XMLGregorianCalendar xmlNow = TransformXMLGregorianCalendar.asXMLGregorianCalendar(now);

            Long cmpdListId = null;

            java.util.Random generator = new Random();
            long randomId = generator.nextLong();
            if (randomId < 0) {
                randomId = -1 * randomId;
            }
            cmpdListId = new Long(randomId);

            tx = session.beginTransaction();

            Criteria crit = session.createCriteria(Cmpd.class);
            crit.setMaxResults(2000);
            crit.add(Restrictions.in("nsc", nscIntList));
            List<Cmpd> cmpdList = (List<Cmpd>) crit.list();

            System.out.println("Size of cmpdList in createCmpdListByNscs in HelperCmpd is: " + cmpdList.size());

            if (!cmpdList.isEmpty()) {

                // create a new cmpdlist
                CmpdList cl = CmpdList.Factory.newInstance();

                cl.setCmpdListId(cmpdListId);
                cl.setListName(listName);
                cl.setDateCreated(now);
                cl.setListOwner(currentUser);
                cl.setShareWith(currentUser);
                cl.setAnchorSmiles(smilesString);

                session.persist(cl);

                for (Cmpd cmpd : cmpdList) {
                    CmpdListMember clm = CmpdListMember.Factory.newInstance();
                    clm.setCmpd(cmpd);
                    clm.setCmpdList(cl);
                    session.persist(clm);
                    // not needed for persistence, added for quick(er) conversion to VO
                    cl.getCmpdListMembers().add(clm);
                }

                cl.setCountListMembers(cl.getCmpdListMembers().size());

                rtn = cl.getCmpdListId();

                tx.commit();

            }

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }

    private static Boolean listIsUsable(List theList) {

        Boolean rtn = Boolean.FALSE;

        if ((theList != null) && (!theList.isEmpty())) {
            rtn = Boolean.TRUE;
        }

        return rtn;

    }

    public static List<Long> searchCmpdsBySearchCriteriaBean(
            SearchCriteriaBean scb,
            String currentUser) {

        System.out.println("In searchCmpdsBySearchCriteriaBean in HelperCmpd.");

        List<Long> rtn = new ArrayList<Long>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            // just fetch the projection of those cmpd.id that meet the criteria
            Criteria crit = session.createCriteria(Cmpd.class)
                    .setProjection(Projections.property("id"))
                    .setMaxResults(2000);

            Disjunction disj = Restrictions.disjunction();

            if (listIsUsable(scb.getNscs())) {
                // nscs to Integers
                ArrayList<Integer> nscIntList = new ArrayList<Integer>();
                for (String s : scb.getNscs()) {
                    try {
                        nscIntList.add(Integer.valueOf(s));
                    } catch (Exception e) {

                    }
                }
                if (!nscIntList.isEmpty()) {
                    disj.add(Restrictions.in("nsc", nscIntList));
                }
            }

            if (listIsUsable(scb.getCases())) {
                disj.add(Restrictions.in("cas", scb.getCases()));
            }

            if ((listIsUsable(scb.getDrugNames())) || (listIsUsable(scb.getAliases()))) {
                crit.createAlias("cmpdAliases", "aliases");
                ArrayList<String> combinedList = new ArrayList<String>();
                if ((scb.getAliases() != null) && !scb.getAliases().isEmpty()) {
                    combinedList.addAll(scb.getAliases());
                }
                if ((scb.getDrugNames() != null) && !scb.getDrugNames().isEmpty()) {
                    combinedList.addAll(scb.getDrugNames());
                }
                disj.add(Restrictions.in("aliases.alias", combinedList));
            }

            if (listIsUsable(scb.getCmpdNamedSets())) {
                crit.createAlias("cmpdNamedSets", "namedSets");
                disj.add(Restrictions.in("namedSets.setName", scb.getCmpdNamedSets()));
            }

            if (listIsUsable(scb.getProjectCodes())) {
                crit.createAlias("cmpdProjects", "projects");
                disj.add(Restrictions.in("projects.projectCode", scb.getProjectCodes()));
            }

            if (listIsUsable(scb.getPlates())) {
                crit.createAlias("cmpdPlates", "plates");
                disj.add(Restrictions.in("plates.plateName", scb.getPlates()));
            }

            if (listIsUsable(scb.getTargets())) {
                crit.createAlias("cmpdTargets", "targets");
                disj.add(Restrictions.in("targets.target", scb.getTargets()));
            }

            // add the disjunction
            crit.add(disj);

      //
            // pChem start
            //
            // using reflection for sanity in PERL scripts that autogen the code (in SCRIPTS directory)
            crit.createAlias("cmpdFragments", "frag");
            crit.createAlias("frag.cmpdFragmentPChem", "pchem");

            Class scbClass = scb.getClass();

            Field f = null;
            Integer minInt = null;
            Integer maxInt = null;
            Double minDbl = null;
            Double maxDbl = null;

            f = scbClass.getDeclaredField("min_molecularWeight");
            f.setAccessible(true);
            minDbl = (Double) f.get(scb);

            f = scbClass.getDeclaredField("max_molecularWeight");
            f.setAccessible(true);
            maxDbl = (Double) f.get(scb);

            if (minDbl != null && maxDbl != null) {
                if (minDbl.compareTo(maxDbl) == 0) {
                    if (minDbl - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.molecularWeight", minDbl));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.molecularWeight", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.molecularWeight", minDbl));
            } else if (maxDbl != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.molecularWeight", maxDbl));
            }

            f = scbClass.getDeclaredField("min_logD");
            f.setAccessible(true);
            minDbl = (Double) f.get(scb);

            f = scbClass.getDeclaredField("max_logD");
            f.setAccessible(true);
            maxDbl = (Double) f.get(scb);

            if (minDbl != null && maxDbl != null) {
                if (minDbl.compareTo(maxDbl) == 0) {
                    if (minDbl - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.logD", minDbl));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.logD", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.logD", minDbl));
            } else if (maxDbl != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.logD", maxDbl));
            }

            f = scbClass.getDeclaredField("min_countHydBondAcceptors");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countHydBondAcceptors");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countHydBondAcceptors", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countHydBondAcceptors", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countHydBondAcceptors", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countHydBondAcceptors", maxInt));
            }

            f = scbClass.getDeclaredField("min_countHydBondDonors");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countHydBondDonors");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countHydBondDonors", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countHydBondDonors", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countHydBondDonors", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countHydBondDonors", maxInt));
            }

            f = scbClass.getDeclaredField("min_surfaceArea");
            f.setAccessible(true);
            minDbl = (Double) f.get(scb);

            f = scbClass.getDeclaredField("max_surfaceArea");
            f.setAccessible(true);
            maxDbl = (Double) f.get(scb);

            if (minDbl != null && maxDbl != null) {
                if (minDbl.compareTo(maxDbl) == 0) {
                    if (minDbl - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.surfaceArea", minDbl));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.surfaceArea", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.surfaceArea", minDbl));
            } else if (maxDbl != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.surfaceArea", maxDbl));
            }

            f = scbClass.getDeclaredField("min_solubility");
            f.setAccessible(true);
            minDbl = (Double) f.get(scb);

            f = scbClass.getDeclaredField("max_solubility");
            f.setAccessible(true);
            maxDbl = (Double) f.get(scb);

            if (minDbl != null && maxDbl != null) {
                if (minDbl.compareTo(maxDbl) == 0) {
                    if (minDbl - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.solubility", minDbl));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.solubility", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.solubility", minDbl));
            } else if (maxDbl != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.solubility", maxDbl));
            }

            f = scbClass.getDeclaredField("min_countRings");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countRings");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countRings", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countRings", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countRings", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countRings", maxInt));
            }

            f = scbClass.getDeclaredField("min_countAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countSingleBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countSingleBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countSingleBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countSingleBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countSingleBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countSingleBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countDoubleBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countDoubleBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countDoubleBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countDoubleBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countDoubleBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countDoubleBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countTripleBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countTripleBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countTripleBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countTripleBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countTripleBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countTripleBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countRotatableBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countRotatableBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countRotatableBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countRotatableBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countRotatableBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countRotatableBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countHydrogenAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countHydrogenAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countHydrogenAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countHydrogenAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countHydrogenAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countHydrogenAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countMetalAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countMetalAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countMetalAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countMetalAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countMetalAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countMetalAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countHeavyAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countHeavyAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countHeavyAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countHeavyAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countHeavyAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countHeavyAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countPositiveAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countPositiveAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countPositiveAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countPositiveAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countPositiveAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countPositiveAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countNegativeAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countNegativeAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countNegativeAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countNegativeAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countNegativeAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countNegativeAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countRingBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countRingBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countRingBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countRingBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countRingBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countRingBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countStereoAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countStereoAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countStereoAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countStereoAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countStereoAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countStereoAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countStereoBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countStereoBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countStereoBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countStereoBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countStereoBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countStereoBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countRingAssemblies");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countRingAssemblies");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countRingAssemblies", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countRingAssemblies", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countRingAssemblies", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countRingAssemblies", maxInt));
            }

            f = scbClass.getDeclaredField("min_countAromaticBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countAromaticBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countAromaticBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countAromaticBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countAromaticBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countAromaticBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countAromaticRings");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_countAromaticRings");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countAromaticRings", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countAromaticRings", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countAromaticRings", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countAromaticRings", maxInt));
            }

            f = scbClass.getDeclaredField("min_formalCharge");
            f.setAccessible(true);
            minInt = (Integer) f.get(scb);

            f = scbClass.getDeclaredField("max_formalCharge");
            f.setAccessible(true);
            maxInt = (Integer) f.get(scb);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.formalCharge", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.formalCharge", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.formalCharge", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.formalCharge", maxInt));
            }

            f = scbClass.getDeclaredField("min_theALogP");
            f.setAccessible(true);
            minDbl = (Double) f.get(scb);

            f = scbClass.getDeclaredField("max_theALogP");
            f.setAccessible(true);
            maxDbl = (Double) f.get(scb);

            if (minDbl != null && maxDbl != null) {
                if (minDbl.compareTo(maxDbl) == 0) {
                    if (minDbl - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.theALogP", minDbl));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.theALogP", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.theALogP", minDbl));
            } else if (maxDbl != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.theALogP", maxDbl));
            }

      //
            // pChem end
            //
            // criteria has projection of cmpd.id
            rtn = crit.list();

            System.out.println("Size of cmpdIdList in searchCmpdsBySearchCriteriaBean in HelperCmpd is: " + rtn.size());

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }

    public static Long NEW_createCmpdListFromSearchCriteriaBean(
            String listName,
            SearchCriteriaBean scb,
            String smilesString,
            String currentUser) throws Exception {

        System.out.println("In NEW_createCmpdListFromSearchCriteriaBean in HelperCmpd.");

        Long idOfNewCmpdList = Long.valueOf(-1);

        Session session = null;
        Transaction tx = null;
        Connection conn = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Query query = session.createSQLQuery("SELECT nextval('cmpd_list_member_seq')");
            BigInteger tempId = (BigInteger) (query.uniqueResult());
            Long clmId = tempId.longValue();

            System.out.println("First clmId is: " + clmId);

            Date now = new Date();
            XMLGregorianCalendar xmlNow = TransformXMLGregorianCalendar.asXMLGregorianCalendar(now);

            Long cmpdListId = null;

            java.util.Random generator = new Random();
            long randomId = generator.nextLong();
            if (randomId < 0) {
                randomId = -1 * randomId;
            }
            cmpdListId = new Long(randomId);

            // call search method
            List<Long> cmpdIdList = searchCmpdsBySearchCriteriaBean(scb, currentUser);

            System.out.println("Size of cmpdIdList in createCmpdListFromSearchCriteriaBeanin HelperCmpd is: " + cmpdIdList.size());

            // create a new cmpdlist
            CmpdList cl = CmpdList.Factory.newInstance();

            cl.setCmpdListId(cmpdListId);
            cl.setListName(listName);
            cl.setDateCreated(now);
            cl.setListOwner(currentUser);
            cl.setShareWith(currentUser);
            cl.setAnchorSmiles(smilesString);

            session.persist(cl);

            tx.commit();

            tx = session.beginTransaction();

            for (Long l : cmpdIdList) {
                // System.out.println("Calling CmpdListMember.Factory.newInstance()");
                CmpdListMember clm = CmpdListMember.Factory.newInstance();
                clm.setCmpd((Cmpd) session.load(CmpdImpl.class, l));
                clm.setCmpdList(cl);

                // not needed for persistence, added for quick(er) conversion to VO
                cl.getCmpdListMembers().add(clm);
            }

            System.out.println("Size of cl.getCmpdListMembers: " + cl.getCmpdListMembers().size());

            cl.setCountListMembers(cl.getCmpdListMembers().size());

      // write the clm to the table, batch
            // Connection conn = session.connection();
            SessionImpl sessionImpl = (SessionImpl) session;
            conn = sessionImpl.connection();

            PreparedStatement pStmt = conn.prepareStatement("insert into CMPD_LIST_MEMBER (ID, LIST_MEMBER_COMMENT, CMPD_LIST_FK, CMPD_FK) values (?, ?, ?, ?)");

            for (CmpdListMember clm : cl.getCmpdListMembers()) {
                pStmt.setLong(1, clmId.longValue());
                pStmt.setString(2, null);
                pStmt.setLong(3, cl.getId());
                pStmt.setLong(4, clm.getCmpd().getId());
                clmId++;
                pStmt.addBatch();
            }

            pStmt.executeBatch();

            idOfNewCmpdList = cl.getCmpdListId();

            // update the sequence
            clmId++;

            query = session.createSQLQuery("SELECT setval('cmpd_list_member_seq', :clmId)");
            query.setLong("clmId", clmId);
            query.uniqueResult();

            query = session.createSQLQuery("SELECT nextval('cmpd_list_member_seq')");
            tempId = (BigInteger) query.uniqueResult();
            clmId = tempId.longValue();

            System.out.println("Final clmId is: " + clmId);

            conn.commit();
            conn.close();

            tx.commit();

        } catch (SQLException sqle) {
            while (sqle.getNextException() != null) {
                Exception e = sqle.getNextException();
                e.printStackTrace();
            }
            throw sqle;
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException sqle) {

                    sqle.printStackTrace();
                }
            }
            session.close();
        }

        return idOfNewCmpdList;

    }

    /**
     * @param listName
     * @param qo
     * @param smilesString
     * @param currentUser
     * @return
     */
    public static Long createCmpdListFromSearchCriteriaBean(
            String listName,
            SearchCriteriaBean qo,
            String smilesString,
            String currentUser) {

        System.out.println("In createCmpdListFromSearchCriteriaBean in HelperCmpd.");

        Long rtn = Long.valueOf(-1);

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Query query = session.createSQLQuery("SELECT nextval('cmpd_list_member_seq')");
            BigInteger tempId = (BigInteger) (query.uniqueResult());
            Long clmId = tempId.longValue();

            System.out.println("First clmId is: " + clmId);

            Date now = new Date();
            XMLGregorianCalendar xmlNow = TransformXMLGregorianCalendar.asXMLGregorianCalendar(now);

            Long cmpdListId = null;

            java.util.Random generator = new Random();
            long randomId = generator.nextLong();
            if (randomId < 0) {
                randomId = -1 * randomId;
            }
            cmpdListId = new Long(randomId);

            // just fetch the projection of the cmpd.id that meets criteria
            Criteria crit = session.createCriteria(Cmpd.class)
                    .setProjection(Projections.property("id"))
                    .setMaxResults(2000);

            Disjunction disj = Restrictions.disjunction();

            if (listIsUsable(qo.getNscs())) {
                // nscs to Integers
                ArrayList<Integer> nscIntList = new ArrayList<Integer>();
                for (String s : qo.getNscs()) {
                    try {
                        nscIntList.add(Integer.valueOf(s));
                    } catch (Exception e) {

                    }
                }
                if (!nscIntList.isEmpty()) {
                    disj.add(Restrictions.in("nsc", nscIntList));
                }
            }

            if (listIsUsable(qo.getCases())) {
                disj.add(Restrictions.in("cas", qo.getCases()));
            }

            if ((listIsUsable(qo.getDrugNames()))
                    || (listIsUsable(qo.getAliases()))) {
                crit.createAlias("cmpdAliases", "aliases");
                ArrayList<String> combinedList = new ArrayList<String>();
                if ((qo.getAliases() != null) && !qo.getAliases().isEmpty()) {
                    combinedList.addAll(qo.getAliases());
                }
                if ((qo.getDrugNames() != null) && !qo.getDrugNames().isEmpty()) {
                    combinedList.addAll(qo.getDrugNames());
                }
                disj.add(Restrictions.in("aliases.alias", combinedList));
            }

            if (listIsUsable(qo.getCmpdNamedSets())) {
                crit.createAlias("cmpdNamedSets", "namedSets");
                disj.add(Restrictions.in("namedSets.setName", qo.getCmpdNamedSets()));
            }

            if (listIsUsable(qo.getProjectCodes())) {
                crit.createAlias("cmpdProjects", "projects");
                disj.add(Restrictions.in("projects.projectCode", qo.getProjectCodes()));
            }

            if (listIsUsable(qo.getPlates())) {
                crit.createAlias("cmpdPlates", "plates");
                disj.add(Restrictions.in("plates.plateName", qo.getPlates()));
            }

            if (listIsUsable(qo.getTargets())) {
                crit.createAlias("cmpdTargets", "targets");
                disj.add(Restrictions.in("targets.target", qo.getTargets()));
            }

            // add the disjunction
            crit.add(disj);

      //
            // pChem start
            //
            crit.createAlias("cmpdFragments", "frag");
            crit.createAlias("frag.cmpdFragmentPChem", "pchem");

            Class scbClass = qo.getClass();

            Field f = null;
            Integer minInt = null;
            Integer maxInt = null;
            Double minDbl = null;
            Double maxDbl = null;

            f = scbClass.getDeclaredField("min_molecularWeight");
            f.setAccessible(true);
            minDbl = (Double) f.get(qo);

            f = scbClass.getDeclaredField("max_molecularWeight");
            f.setAccessible(true);
            maxDbl = (Double) f.get(qo);

            if (minDbl != null && maxDbl != null) {
                if (minDbl.compareTo(maxDbl) == 0) {
                    if (minDbl - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.molecularWeight", minDbl));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.molecularWeight", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.molecularWeight", minDbl));
            } else if (maxDbl != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.molecularWeight", maxDbl));
            }

            f = scbClass.getDeclaredField("min_logD");
            f.setAccessible(true);
            minDbl = (Double) f.get(qo);

            f = scbClass.getDeclaredField("max_logD");
            f.setAccessible(true);
            maxDbl = (Double) f.get(qo);

            if (minDbl != null && maxDbl != null) {
                if (minDbl.compareTo(maxDbl) == 0) {
                    if (minDbl - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.logD", minDbl));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.logD", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.logD", minDbl));
            } else if (maxDbl != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.logD", maxDbl));
            }

            f = scbClass.getDeclaredField("min_countHydBondAcceptors");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countHydBondAcceptors");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countHydBondAcceptors", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countHydBondAcceptors", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countHydBondAcceptors", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countHydBondAcceptors", maxInt));
            }

            f = scbClass.getDeclaredField("min_countHydBondDonors");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countHydBondDonors");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countHydBondDonors", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countHydBondDonors", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countHydBondDonors", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countHydBondDonors", maxInt));
            }

            f = scbClass.getDeclaredField("min_surfaceArea");
            f.setAccessible(true);
            minDbl = (Double) f.get(qo);

            f = scbClass.getDeclaredField("max_surfaceArea");
            f.setAccessible(true);
            maxDbl = (Double) f.get(qo);

            if (minDbl != null && maxDbl != null) {
                if (minDbl.compareTo(maxDbl) == 0) {
                    if (minDbl - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.surfaceArea", minDbl));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.surfaceArea", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.surfaceArea", minDbl));
            } else if (maxDbl != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.surfaceArea", maxDbl));
            }

            f = scbClass.getDeclaredField("min_solubility");
            f.setAccessible(true);
            minDbl = (Double) f.get(qo);

            f = scbClass.getDeclaredField("max_solubility");
            f.setAccessible(true);
            maxDbl = (Double) f.get(qo);

            if (minDbl != null && maxDbl != null) {
                if (minDbl.compareTo(maxDbl) == 0) {
                    if (minDbl - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.solubility", minDbl));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.solubility", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.solubility", minDbl));
            } else if (maxDbl != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.solubility", maxDbl));
            }

            f = scbClass.getDeclaredField("min_countRings");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countRings");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countRings", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countRings", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countRings", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countRings", maxInt));
            }

            f = scbClass.getDeclaredField("min_countAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countSingleBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countSingleBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countSingleBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countSingleBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countSingleBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countSingleBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countDoubleBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countDoubleBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countDoubleBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countDoubleBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countDoubleBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countDoubleBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countTripleBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countTripleBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countTripleBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countTripleBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countTripleBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countTripleBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countRotatableBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countRotatableBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countRotatableBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countRotatableBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countRotatableBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countRotatableBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countHydrogenAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countHydrogenAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countHydrogenAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countHydrogenAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countHydrogenAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countHydrogenAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countMetalAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countMetalAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countMetalAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countMetalAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countMetalAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countMetalAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countHeavyAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countHeavyAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countHeavyAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countHeavyAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countHeavyAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countHeavyAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countPositiveAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countPositiveAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countPositiveAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countPositiveAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countPositiveAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countPositiveAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countNegativeAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countNegativeAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countNegativeAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countNegativeAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countNegativeAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countNegativeAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countRingBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countRingBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countRingBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countRingBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countRingBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countRingBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countStereoAtoms");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countStereoAtoms");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countStereoAtoms", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countStereoAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countStereoAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countStereoAtoms", maxInt));
            }

            f = scbClass.getDeclaredField("min_countStereoBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countStereoBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countStereoBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countStereoBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countStereoBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countStereoBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countRingAssemblies");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countRingAssemblies");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countRingAssemblies", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countRingAssemblies", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countRingAssemblies", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countRingAssemblies", maxInt));
            }

            f = scbClass.getDeclaredField("min_countAromaticBonds");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countAromaticBonds");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countAromaticBonds", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countAromaticBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countAromaticBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countAromaticBonds", maxInt));
            }

            f = scbClass.getDeclaredField("min_countAromaticRings");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_countAromaticRings");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.countAromaticRings", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.countAromaticRings", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.countAromaticRings", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.countAromaticRings", maxInt));
            }

            f = scbClass.getDeclaredField("min_formalCharge");
            f.setAccessible(true);
            minInt = (Integer) f.get(qo);

            f = scbClass.getDeclaredField("max_formalCharge");
            f.setAccessible(true);
            maxInt = (Integer) f.get(qo);

            if (minInt != null && maxInt != null) {
                if (minInt.compareTo(maxInt) == 0) {
                    if (minInt - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.formalCharge", minInt));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.formalCharge", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.formalCharge", minInt));
            } else if (maxInt != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.formalCharge", maxInt));
            }

            f = scbClass.getDeclaredField("min_theALogP");
            f.setAccessible(true);
            minDbl = (Double) f.get(qo);

            f = scbClass.getDeclaredField("max_theALogP");
            f.setAccessible(true);
            maxDbl = (Double) f.get(qo);

            if (minDbl != null && maxDbl != null) {
                if (minDbl.compareTo(maxDbl) == 0) {
                    if (minDbl - 0 != 0) {
                        crit.add(Restrictions.eq("pchem.theALogP", minDbl));
                    }
                } else {
                    // between
                    crit.add(Restrictions.between("pchem.theALogP", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                crit.add(Restrictions.gt("pchem.theALogP", minDbl));
            } else if (maxDbl != null) {
                // lt max
                crit.add(Restrictions.lt("pchem.theALogP", maxDbl));
            }

      //
            // pChem end
            //
            List<Long> cmpdIdList = crit.list();

            if (!cmpdIdList.isEmpty()) {

                System.out.println("Size of cmpdIdList in createCmpdListFromSearchCriteriaBeanin HelperCmpd is: " + cmpdIdList.size());

                // create a new cmpdlist
                CmpdList cl = CmpdList.Factory.newInstance();

                cl.setCmpdListId(cmpdListId);
                cl.setListName(listName);
                cl.setDateCreated(now);
                cl.setListOwner(currentUser);
                cl.setShareWith(currentUser);
                cl.setAnchorSmiles(smilesString);

                session.persist(cl);

                tx.commit();

                tx = session.beginTransaction();

                for (Long l : cmpdIdList) {
                    // System.out.println("Calling CmpdListMember.Factory.newInstance()");
                    CmpdListMember clm = CmpdListMember.Factory.newInstance();
                    clm.setCmpd((Cmpd) session.load(CmpdImpl.class, l));
                    clm.setCmpdList(cl);

                    // not needed for persistence, added for quick(er) conversion to VO
                    cl.getCmpdListMembers().add(clm);
                }

                System.out.println("Size of cl.getCmpdListMembers: " + cl.getCmpdListMembers().size());

                cl.setCountListMembers(cl.getCmpdListMembers().size());

                // write the clm to the table, batch
                Connection conn = session.connection();

                PreparedStatement pStmt = conn.prepareStatement("insert into CMPD_LIST_MEMBER (ID, LIST_MEMBER_COMMENT, CMPD_LIST_FK, CMPD_FK) values (?, ?, ?, ?)");

                for (CmpdListMember clm : cl.getCmpdListMembers()) {
                    pStmt.setLong(1, clmId.longValue());
                    pStmt.setString(2, null);
                    pStmt.setLong(3, cl.getId());
                    pStmt.setLong(4, clm.getCmpd().getId());
                    clmId++;
                    pStmt.addBatch();
                }

                pStmt.executeBatch();

                rtn = cl.getCmpdListId();

                // update the sequence
                clmId++;

                query = session.createSQLQuery("SELECT setval('cmpd_list_member_seq', :clmId)");
                query.setLong("clmId", clmId);
                query.uniqueResult();

                query = session.createSQLQuery("SELECT nextval('cmpd_list_member_seq')");
                tempId = (BigInteger) query.uniqueResult();
                clmId = tempId.longValue();

                System.out.println("Final clmId is: " + clmId);

                tx.commit();

            }

        } catch (SQLException sqle) {
            while (sqle.getNextException() != null) {
                Exception e = sqle.getNextException();
                e.printStackTrace();
            }
        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }

    /**
     * Called by structure and similarity searches
     *
     * @param nscIntList
     * @param currentUser
     * @return
     */
    public static List<CmpdVO> getCmpdsByNsc(List<Integer> nscIntList, String currentUser) {

        //MWK TODO this doesn't call currentUser
        List<CmpdVO> rtnList = new ArrayList<CmpdVO>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            Criteria cmpdCrit = session.createCriteria(Cmpd.class);
            cmpdCrit.setMaxResults(2000);
            cmpdCrit.add(Restrictions.in("nsc", nscIntList));
            List<Cmpd> cmpdList = (List<Cmpd>) cmpdCrit.list();

            if (!cmpdList.isEmpty()) {

                List<Long> cmpdIdList = new ArrayList<Long>();
                Long cmpdId = null;

                for (Cmpd cmpd : cmpdList) {
                    cmpdId = (Long) session.getIdentifier(cmpd);
                    cmpdIdList.add(cmpdId);
                }

// fetch a list of cmpdViews
                Criteria cvCrit = session.createCriteria(CmpdTable.class);
                cvCrit.add(Restrictions.in("id", cmpdIdList));
                cvCrit.setMaxResults(2000);
                List<CmpdTable> entityCVlist = (List<CmpdTable>) cvCrit.list();

                if (!entityCVlist.isEmpty()) {

                    for (CmpdTable cv : entityCVlist) {
                        CmpdVO cVO = TransformCmpdTableToVO.translateCmpd(cv);
                        rtnList.add(cVO);
                    }

                }

                tx.commit();

            }

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtnList;

    }

    /**
     * Called to load structure editor and called (callable?) by
     * StructureServlet
     *
     * @param nsc
     * @param currentUser
     * @return
     */
    public static CmpdVO getSingleCmpdByNsc(Integer nsc, String currentUser) {

        //MWK TODO this doesn't call currentUser!
        CmpdVO rtn = new CmpdVO();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();
            Criteria cmpdCrit = session.createCriteria(Cmpd.class);
            cmpdCrit.setMaxResults(2000);
            cmpdCrit.add(Restrictions.eq("nsc", nsc));
            Cmpd cmpd = (Cmpd) cmpdCrit.uniqueResult();

            Long cmpdId = (Long) session.getIdentifier(cmpd);

            Criteria cvCrit = session.createCriteria(CmpdTable.class);
            cvCrit.setMaxResults(2000);
            cvCrit.add(Restrictions.eq("id", cmpdId));
            CmpdTable entityCV = (CmpdTable) cvCrit.uniqueResult();

            rtn = TransformCmpdTableToVO.translateCmpd(entityCV);

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }

}
