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
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.Projections;

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
            Criteria c = session.createCriteria(CmpdFragment.class);
            c.setMaxResults(2000);
            c.add(Restrictions.eq("id", cfVO.getId()));

            CmpdFragment cfEntity = (CmpdFragment) c.uniqueResult();

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

            Criteria c = session.createCriteria(Cmpd.class);
            c.setMaxResults(2000);

            c.add(Restrictions.eq("nsc", id));

            Cmpd cmpd = (Cmpd) c.uniqueResult();

            rtn = TransformAndroToVO.translateCmpd(cmpd);

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtn;

    }

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

            Criteria c = session.createCriteria(Cmpd.class);
            c.setMaxResults(2000);
            c.add(Restrictions.in("nsc", nscIntList));
            List<Cmpd> cmpdList = (List<Cmpd>) c.list();

            System.out.println("Size of cmpdList in createCmpdListByNscs in HelperCmpd is: " + cmpdList.size());

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

    public static Long createCmpdListFromQueryObject(
            String listName,
            SearchCriteriaBean qo,
            String smilesString,
            String currentUser) {

        System.out.println("In createCmpdListFromQueryObject in HelperCmpd.");

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
            Criteria c = session.createCriteria(Cmpd.class)
                    .setProjection(Projections.property("id"));

            // MWK 06 July 2015, experimenting with native SQL inserts
            // c.setMaxResults(2000);
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
                c.createAlias("cmpdAliases", "aliases");
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
                c.createAlias("cmpdNamedSets", "namedSets");
                disj.add(Restrictions.in("namedSets.setName", qo.getCmpdNamedSets()));
            }

            if (listIsUsable(qo.getProjectCodes())) {
                c.createAlias("cmpdProjects", "projects");
                disj.add(Restrictions.in("projects.projectCode", qo.getProjectCodes()));
            }

            if (listIsUsable(qo.getPlates())) {
                c.createAlias("cmpdPlates", "plates");
                disj.add(Restrictions.in("plates.plateName", qo.getPlates()));
            }

            if (listIsUsable(qo.getTargets())) {
                c.createAlias("cmpdTargets", "targets");
                disj.add(Restrictions.in("targets.target", qo.getTargets()));
            }

            // add the disjunction
            c.add(disj);

            //
            // pChem start
            //
            c.createAlias("cmpdFragments", "frag");
            c.createAlias("frag.cmpdFragmentPChem", "pchem");

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
                        c.add(Restrictions.eq("pchem.molecularWeight", minDbl));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.molecularWeight", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                c.add(Restrictions.gt("pchem.molecularWeight", minDbl));
            } else if (maxDbl != null) {
                // lt max
                c.add(Restrictions.lt("pchem.molecularWeight", maxDbl));
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
                        c.add(Restrictions.eq("pchem.logD", minDbl));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.logD", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                c.add(Restrictions.gt("pchem.logD", minDbl));
            } else if (maxDbl != null) {
                // lt max
                c.add(Restrictions.lt("pchem.logD", maxDbl));
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
                        c.add(Restrictions.eq("pchem.countHydBondAcceptors", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countHydBondAcceptors", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countHydBondAcceptors", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countHydBondAcceptors", maxInt));
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
                        c.add(Restrictions.eq("pchem.countHydBondDonors", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countHydBondDonors", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countHydBondDonors", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countHydBondDonors", maxInt));
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
                        c.add(Restrictions.eq("pchem.surfaceArea", minDbl));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.surfaceArea", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                c.add(Restrictions.gt("pchem.surfaceArea", minDbl));
            } else if (maxDbl != null) {
                // lt max
                c.add(Restrictions.lt("pchem.surfaceArea", maxDbl));
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
                        c.add(Restrictions.eq("pchem.solubility", minDbl));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.solubility", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                c.add(Restrictions.gt("pchem.solubility", minDbl));
            } else if (maxDbl != null) {
                // lt max
                c.add(Restrictions.lt("pchem.solubility", maxDbl));
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
                        c.add(Restrictions.eq("pchem.countRings", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countRings", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countRings", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countRings", maxInt));
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
                        c.add(Restrictions.eq("pchem.countAtoms", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countAtoms", maxInt));
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
                        c.add(Restrictions.eq("pchem.countBonds", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countBonds", maxInt));
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
                        c.add(Restrictions.eq("pchem.countSingleBonds", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countSingleBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countSingleBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countSingleBonds", maxInt));
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
                        c.add(Restrictions.eq("pchem.countDoubleBonds", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countDoubleBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countDoubleBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countDoubleBonds", maxInt));
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
                        c.add(Restrictions.eq("pchem.countTripleBonds", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countTripleBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countTripleBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countTripleBonds", maxInt));
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
                        c.add(Restrictions.eq("pchem.countRotatableBonds", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countRotatableBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countRotatableBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countRotatableBonds", maxInt));
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
                        c.add(Restrictions.eq("pchem.countHydrogenAtoms", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countHydrogenAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countHydrogenAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countHydrogenAtoms", maxInt));
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
                        c.add(Restrictions.eq("pchem.countMetalAtoms", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countMetalAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countMetalAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countMetalAtoms", maxInt));
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
                        c.add(Restrictions.eq("pchem.countHeavyAtoms", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countHeavyAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countHeavyAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countHeavyAtoms", maxInt));
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
                        c.add(Restrictions.eq("pchem.countPositiveAtoms", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countPositiveAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countPositiveAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countPositiveAtoms", maxInt));
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
                        c.add(Restrictions.eq("pchem.countNegativeAtoms", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countNegativeAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countNegativeAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countNegativeAtoms", maxInt));
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
                        c.add(Restrictions.eq("pchem.countRingBonds", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countRingBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countRingBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countRingBonds", maxInt));
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
                        c.add(Restrictions.eq("pchem.countStereoAtoms", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countStereoAtoms", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countStereoAtoms", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countStereoAtoms", maxInt));
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
                        c.add(Restrictions.eq("pchem.countStereoBonds", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countStereoBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countStereoBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countStereoBonds", maxInt));
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
                        c.add(Restrictions.eq("pchem.countRingAssemblies", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countRingAssemblies", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countRingAssemblies", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countRingAssemblies", maxInt));
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
                        c.add(Restrictions.eq("pchem.countAromaticBonds", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countAromaticBonds", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countAromaticBonds", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countAromaticBonds", maxInt));
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
                        c.add(Restrictions.eq("pchem.countAromaticRings", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.countAromaticRings", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.countAromaticRings", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.countAromaticRings", maxInt));
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
                        c.add(Restrictions.eq("pchem.formalCharge", minInt));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.formalCharge", minInt, maxInt));
                }
            } else if (minInt != null) {
                // gt min
                c.add(Restrictions.gt("pchem.formalCharge", minInt));
            } else if (maxInt != null) {
                // lt max
                c.add(Restrictions.lt("pchem.formalCharge", maxInt));
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
                        c.add(Restrictions.eq("pchem.theALogP", minDbl));
                    }
                } else {
                    // between
                    c.add(Restrictions.between("pchem.theALogP", minDbl, maxDbl));
                }
            } else if (minDbl != null) {
                // gt min
                c.add(Restrictions.gt("pchem.theALogP", minDbl));
            } else if (maxDbl != null) {
                // lt max
                c.add(Restrictions.lt("pchem.theALogP", maxDbl));
            }

            //
            // pChem end
            //
            // have to handle multiplicities by plate, alias, etc.
            // c.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);
            // List<Cmpd> cmpdList = (List<Cmpd>) c.list();
            List<Long> projList = c.list();

            System.out.println("Size of projList in createCmpdListFromQueryObjectin HelperCmpd is: " + projList.size());

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

            for (Long l : projList) {
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

    public static Long createCmpdListFromQueryObject_BEFORE_MODS(
            String listName,
            SearchCriteriaBean qo,
            String smilesString,
            String currentUser) {

        System.out.println("In createCmpdListFromQueryObject in HelperCmpd.");

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
            Criteria c = session.createCriteria(Cmpd.class)
                    .setProjection(Projections.property("id"));

            // MWK 06 July 2015, experimenting with native SQL inserts
            // c.setMaxResults(2000);
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
                c.createAlias("cmpdAliases", "aliases");
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
                c.createAlias("cmpdNamedSets", "namedSets");
                disj.add(Restrictions.in("namedSets.setName", qo.getCmpdNamedSets()));
            }

            if (listIsUsable(qo.getProjectCodes())) {
                c.createAlias("cmpdProjects", "projects");
                disj.add(Restrictions.in("projects.projectCode", qo.getProjectCodes()));
            }

            if (listIsUsable(qo.getPlates())) {
                c.createAlias("cmpdPlates", "plates");
                disj.add(Restrictions.in("plates.plateName", qo.getPlates()));
            }

            if (listIsUsable(qo.getTargets())) {
                c.createAlias("cmpdTargets", "targets");
                disj.add(Restrictions.in("targets.target", qo.getTargets()));
            }

            // add the disjunction
            c.add(disj);

            // have to handle multiplicities by plate, alias, etc.
            c.setResultTransformer(CriteriaSpecification.DISTINCT_ROOT_ENTITY);

            // List<Cmpd> cmpdList = (List<Cmpd>) c.list();
            List<Long> projList = c.list();

            System.out.println("Size of projList in createCmpdListFromQueryObjectin HelperCmpd is: " + projList.size());

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

            for (Long l : projList) {
                // System.out.println("Calling CmpdListMember.Factory.newInstance()");
                CmpdListMember clm = CmpdListMember.Factory.newInstance();
                clm.setCmpd((Cmpd) session.load(CmpdImpl.class, l));
                clm.setCmpdList(cl);

                // MWK 05July2015 trying bulk persist of list?????
                // session.persist(clm);
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

            for (CmpdTable cv : entityCVlist) {
                CmpdVO cVO = TransformCmpdTableToVO.translateCmpd(cv);
                rtnList.add(cVO);
            }

            tx.commit();

        } catch (Exception e) {
            tx.rollback();
            e.printStackTrace();
        } finally {
            session.close();
        }

        return rtnList;

    }

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
