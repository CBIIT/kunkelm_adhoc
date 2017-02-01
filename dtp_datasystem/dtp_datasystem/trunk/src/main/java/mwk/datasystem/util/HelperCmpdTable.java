/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdTable;
import java.util.ArrayList;
import java.util.List;
// import javax.xml.datatype.XMLGregorianCalendar;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.AdHocCmpdFragmentPChem;
import mwk.datasystem.domain.AdHocCmpdFragmentStructure;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mwkunkel
 */
public class HelperCmpdTable {

    public static final Boolean DEBUG = Boolean.FALSE;

    public static List<CmpdVO> adHocCmpdsToCmpdTable(List<AdHocCmpd> entityList) {

        List<CmpdVO> rtn = new ArrayList<CmpdVO>();

        Session session = null;
        Transaction tx = null;

        try {

            List<CmpdTable> ctList = new ArrayList<CmpdTable>();

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            for (AdHocCmpd ahc : entityList) {

                CmpdTable ct = CmpdTable.Factory.newInstance();

                // identifier stuff
                ct.setId(ahc.getId());
                ct.setAdHocCmpdId(ahc.getAdHocCmpdId());
                ct.setOriginalAdHocCmpdId(ahc.getOriginalAdHocCmpdId());
                ct.setName(ahc.getName());

                StringBuilder ffsb = new StringBuilder();
                for (AdHocCmpdFragment frag : ahc.getAdHocCmpdFragments()) {
                    ffsb.append(frag.getAdHocCmpdFragmentStructure().getCanSmi());
                    ffsb.append("xxx");
                }
                ct.setFormattedFragmentsString(ffsb.toString());

                AdHocCmpdFragmentPChem ahcfpc = ahc.getAdHocCmpdParentFragment().getAdHocCmpdFragmentPChem();

                if (null != ahcfpc) {

                    ct.setFormulaWeight(ahcfpc.getMolecularWeight());
                    ct.setFormulaMolecularFormula(ahcfpc.getMolecularFormula());

                    ct.setParentMolecularWeight(ahcfpc.getMolecularWeight());
                    ct.setParentMolecularFormula(ahcfpc.getMolecularFormula());

                    ct.setLogD(ahcfpc.getLogD());
                    ct.setCountHydBondAcceptors(ahcfpc.getCountHydBondAcceptors());
                    ct.setCountHydBondDonors(ahcfpc.getCountHydBondDonors());
                    ct.setSurfaceArea(ahcfpc.getSurfaceArea());
                    ct.setSolubility(ahcfpc.getSolubility());
                    ct.setCountRings(ahcfpc.getCountRings());
                    ct.setCountAtoms(ahcfpc.getCountAtoms());
                    ct.setCountBonds(ahcfpc.getCountBonds());
                    ct.setCountSingleBonds(ahcfpc.getCountSingleBonds());
                    ct.setCountDoubleBonds(ahcfpc.getCountDoubleBonds());
                    ct.setCountTripleBonds(ahcfpc.getCountTripleBonds());
                    ct.setCountRotatableBonds(ahcfpc.getCountRotatableBonds());
                    ct.setCountHydrogenAtoms(ahcfpc.getCountHydrogenAtoms());
                    ct.setCountMetalAtoms(ahcfpc.getCountMetalAtoms());
                    ct.setCountHeavyAtoms(ahcfpc.getCountHeavyAtoms());
                    ct.setCountPositiveAtoms(ahcfpc.getCountPositiveAtoms());
                    ct.setCountNegativeAtoms(ahcfpc.getCountNegativeAtoms());
                    ct.setCountRingBonds(ahcfpc.getCountRingBonds());
                    ct.setCountStereoAtoms(ahcfpc.getCountStereoAtoms());
                    ct.setCountStereoBonds(ahcfpc.getCountStereoBonds());
                    ct.setCountRingAssemblies(ahcfpc.getCountRingAssemblies());
                    ct.setCountAromaticBonds(ahcfpc.getCountAromaticBonds());
                    ct.setCountAromaticRings(ahcfpc.getCountAromaticRings());
                    ct.setFormalCharge(ahcfpc.getFormalCharge());
                    ct.setTheALogP(ahcfpc.getTheALogP());
                }

                AdHocCmpdFragmentStructure ahcfps = ahc.getAdHocCmpdParentFragment().getAdHocCmpdFragmentStructure();

                if (null != ahcfps) {
                    ct.setInchi(ahcfps.getInchi());
                    ct.setInchiAux(ahcfps.getInchiAux());
                    ct.setCtab(ahcfps.getCtab());
                    ct.setCanSmi(ahcfps.getCanSmi());
                    ct.setCanTaut(ahcfps.getCanTaut());
                    ct.setCanTautStripStereo(ahcfps.getCanTautStripStereo());
                }

                session.merge(ct);

                ctList.add(ct);

            }

            tx.commit();

            for (CmpdTable ct : ctList) {

                CmpdVO ctVO = TransformCmpdTableToVO.translateCmpd(ct);

                rtn.add(ctVO);

            }

        } catch (Exception e) {
            e.printStackTrace();
            if (tx.isActive()) {
                tx.rollback();
            }
        } finally {
            session.close();
        }

        return rtn;

    }

    public static List<CmpdVO> fetchCmpdsByNscs(List<Integer> nscIntList) {

        ArrayList<CmpdVO> rtn = new ArrayList<CmpdVO>();

        Session session = null;
        Transaction tx = null;

        try {

            session = HibernateUtil.getSessionFactory().openSession();

            tx = session.beginTransaction();

            Criteria crit = session.createCriteria(CmpdTable.class);
            crit.setMaxResults(2000);
            crit.add(Restrictions.in("nsc", nscIntList));
            List<CmpdTable> ctList = (List<CmpdTable>) crit.list();

            if (DEBUG) {
                System.out.println("Size of cmpdList in HelperCmpdTable.fetchCmpdsByNscs is: " + ctList.size());
            }

            if (ctList != null && !ctList.isEmpty()) {
                for (CmpdTable ct : ctList) {
                    rtn.add(TransformCmpdTableToVO.translateCmpd(ct));
                }
            }

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
