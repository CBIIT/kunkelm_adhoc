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
// import javax.xml.datatype.XMLGregorianCalendar;
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
                    ct.setMolecularWeight(ahcfpc.getMolecularWeight());
                    ct.setMolecularFormula(ahcfpc.getMolecularFormula());
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

                CmpdVO ctVO = TransformCmpdTableToVO.toCmpdVO(ct);

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
}