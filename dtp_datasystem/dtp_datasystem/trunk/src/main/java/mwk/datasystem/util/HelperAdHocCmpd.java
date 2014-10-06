/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.AdHocCmpdFragmentPChem;
import mwk.datasystem.domain.AdHocCmpdFragmentStructure;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mwkunkel
 */
public class HelperAdHocCmpd {

    public static void replicatePchem(AdHocCmpdFragmentPChem sourceEntity, AdHocCmpdFragmentPChem targetEntity) {

        try {

            if (null != sourceEntity) {
                // NO! NO! NO! NO! NO! targetEntity.setId(sourceEntity.getId());
                targetEntity.setMolecularWeight(sourceEntity.getMolecularWeight());
                targetEntity.setMolecularFormula(sourceEntity.getMolecularFormula());
                targetEntity.setLogD(sourceEntity.getLogD());
                targetEntity.setCountHydBondAcceptors(sourceEntity.getCountHydBondAcceptors());
                targetEntity.setCountHydBondDonors(sourceEntity.getCountHydBondDonors());
                targetEntity.setSurfaceArea(sourceEntity.getSurfaceArea());
                targetEntity.setSolubility(sourceEntity.getSolubility());
                targetEntity.setCountRings(sourceEntity.getCountRings());
                targetEntity.setCountAtoms(sourceEntity.getCountAtoms());
                targetEntity.setCountBonds(sourceEntity.getCountBonds());
                targetEntity.setCountSingleBonds(sourceEntity.getCountSingleBonds());
                targetEntity.setCountDoubleBonds(sourceEntity.getCountDoubleBonds());
                targetEntity.setCountTripleBonds(sourceEntity.getCountTripleBonds());
                targetEntity.setCountRotatableBonds(sourceEntity.getCountRotatableBonds());
                targetEntity.setCountHydrogenAtoms(sourceEntity.getCountHydrogenAtoms());
                targetEntity.setCountMetalAtoms(sourceEntity.getCountMetalAtoms());
                targetEntity.setCountHeavyAtoms(sourceEntity.getCountHeavyAtoms());
                targetEntity.setCountPositiveAtoms(sourceEntity.getCountPositiveAtoms());
                targetEntity.setCountNegativeAtoms(sourceEntity.getCountNegativeAtoms());
                targetEntity.setCountRingBonds(sourceEntity.getCountRingBonds());
                targetEntity.setCountStereoAtoms(sourceEntity.getCountStereoAtoms());
                targetEntity.setCountStereoBonds(sourceEntity.getCountStereoBonds());
                targetEntity.setCountRingAssemblies(sourceEntity.getCountRingAssemblies());
                targetEntity.setCountAromaticBonds(sourceEntity.getCountAromaticBonds());
                targetEntity.setCountAromaticRings(sourceEntity.getCountAromaticRings());
                targetEntity.setFormalCharge(sourceEntity.getFormalCharge());
                targetEntity.setTheALogP(sourceEntity.getTheALogP());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void replicateStructure(AdHocCmpdFragmentStructure otherBean, AdHocCmpdFragmentStructure targetEntity) {

        try {

            if (null != otherBean) {
                // NO! NO! NO! NO! NO! targetEntity.setId(otherBean.getId());
                targetEntity.setInchi(otherBean.getInchi());
                targetEntity.setInchiAux(otherBean.getInchiAux());
                targetEntity.setCtab(otherBean.getCtab());
                targetEntity.setCanSmi(otherBean.getCanSmi());
                targetEntity.setCanTaut(otherBean.getCanTaut());
                targetEntity.setCanTautStripStereo(otherBean.getCanTautStripStereo());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static AdHocCmpd replicateAdHocCmpdObject(AdHocCmpd ahc, String currentUser) {

        Random randomGenerator = new Random();

        AdHocCmpd rtn = AdHocCmpd.Factory.newInstance();

        try {

            long randomId = randomGenerator.nextLong();
            if (randomId < 0) {
                randomId = -1 * randomId;
            }
            Long newRandomId = new Long(randomId);

            rtn.setAdHocCmpdId(newRandomId);
            rtn.setOriginalAdHocCmpdId(ahc.getOriginalAdHocCmpdId());
            rtn.setName(ahc.getName());

            for (AdHocCmpdFragment ahcf : ahc.getAdHocCmpdFragments()) {

                AdHocCmpdFragment frag = AdHocCmpdFragment.Factory.newInstance();

                AdHocCmpdFragmentPChem pchem = AdHocCmpdFragmentPChem.Factory.newInstance();
                replicatePchem(ahcf.getAdHocCmpdFragmentPChem(), pchem);
                frag.setAdHocCmpdFragmentPChem(pchem);

                AdHocCmpdFragmentStructure struc = AdHocCmpdFragmentStructure.Factory.newInstance();
                replicateStructure(ahcf.getAdHocCmpdFragmentStructure(), struc);
                frag.setAdHocCmpdFragmentStructure(struc);

                if (ahc.getAdHocCmpdParentFragment().equals(ahcf)) {
                    rtn.setAdHocCmpdParentFragment(frag);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
        }

        return rtn;

    }

    public static AdHocCmpd createNewAdHocCmpd(AdHocCmpd ahc, String currentUser) {

        Random randomGenerator = new Random();

        Session session = null;
        Transaction tx = null;

        AdHocCmpd rtn = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            long randomId = randomGenerator.nextLong();
            if (randomId < 0) {
                randomId = -1 * randomId;
            }
            Long newRandomId = new Long(randomId);

            tx = session.beginTransaction();

            rtn = AdHocCmpd.Factory.newInstance();

            rtn.setAdHocCmpdId(newRandomId);
            rtn.setOriginalAdHocCmpdId(newRandomId);
            rtn.setName(ahc.getName());

            for (AdHocCmpdFragment ahcf : ahc.getAdHocCmpdFragments()) {

                AdHocCmpdFragment frag = AdHocCmpdFragment.Factory.newInstance();

                AdHocCmpdFragmentPChem pchem = AdHocCmpdFragmentPChem.Factory.newInstance();
                replicatePchem(ahcf.getAdHocCmpdFragmentPChem(), pchem);
                session.persist(pchem);

                frag.setAdHocCmpdFragmentPChem(pchem);

                AdHocCmpdFragmentStructure struc = AdHocCmpdFragmentStructure.Factory.newInstance();
                replicateStructure(ahcf.getAdHocCmpdFragmentStructure(), struc);
                session.persist(struc);

                frag.setAdHocCmpdFragmentStructure(struc);

                // persist the fragment                
                session.persist(frag);

                // check whether targetEntityis parent, and set that while we're at it

                if (ahc.getAdHocCmpdParentFragment().equals(ahcf)) {
                    rtn.setAdHocCmpdParentFragment(frag);
                }

            }

            session.persist("Cmpd", rtn);

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