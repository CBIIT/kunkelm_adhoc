/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.AdHocCmpdFragmentPChem;
import mwk.datasystem.domain.AdHocCmpdFragmentStructure;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdAlias;
import mwk.datasystem.domain.CmpdBioAssay;
import mwk.datasystem.domain.CmpdFragment;
import mwk.datasystem.domain.CmpdFragmentPChem;
import mwk.datasystem.domain.CmpdFragmentStructure;
import mwk.datasystem.domain.CmpdTarget;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.domain.CmpdTable;
import mwk.datasystem.domain.NscCmpd;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mwk.datasystem.domain.CmpdAnnotation;
import mwk.datasystem.vo.CmpdAnnotationVO;
import mwk.datasystem.vo.CmpdBioAssayVO;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdFragmentStructureVO;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;

/**
 *
 * @author mwkunkel
 */
public class TransformCmpdTableToVO {

    public static final Boolean DEBUG = Boolean.FALSE;

    public static ArrayList<CmpdFragmentVO> parseFormattedFragmentsString(String fmtdString) {

        ArrayList<CmpdFragmentVO> rtnList = new ArrayList<CmpdFragmentVO>();

        ArrayList<String> fragSmilesList = parseFormattedString(fmtdString);

        for (String fragSmiles : fragSmilesList) {

            CmpdFragmentVO cfVO = new CmpdFragmentVO();

            CmpdFragmentStructureVO cfsVO = new CmpdFragmentStructureVO();
            cfsVO.setCanSmi(fragSmiles);

            cfVO.setCmpdFragmentStructure(cfsVO);

            rtnList.add(cfVO);

        }

        return rtnList;

    }

    public static ArrayList<String> parseFormattedString(String fmtdString) {

        ArrayList<String> rtnList = new ArrayList<String>();

        if (fmtdString != null && fmtdString.length() > 0) {

            String[] splitStrings = null;
            String fixedString = null;
            int i;

            String delimiters = "xxx";

            splitStrings = fmtdString.split(delimiters);
            for (i = 0; i < splitStrings.length; i++) {
                rtnList.add(splitStrings[i]);
            }

        }

        return rtnList;

    }

    public static List<CmpdListVO> translateCmpdLists(List<CmpdList> entityListIn, Boolean includeListMembers) {

        List<CmpdListVO> returnList = new ArrayList<CmpdListVO>();

        try {
            for (CmpdList nl : entityListIn) {
                returnList.add(translateCmpdList(nl, includeListMembers));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;

    }

    private static CmpdFragmentVO translateCmpdFragment(CmpdTable entityIn) {

        CmpdFragmentVO rtn = new CmpdFragmentVO();

        // Does NOT propagate to CmpdTable rtn.setComment(entityIn.getComment());
        try {
            rtn.setId(entityIn.getId());
            rtn.setCmpdFragmentStructure(translateCmpdFragmentStructure(entityIn));
            rtn.setCmpdFragmentPChem(translateCmpdFragmentPChem(entityIn));
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    private static CmpdFragmentPChemVO translateCmpdFragmentPChem(CmpdTable entityIn) {

        CmpdFragmentPChemVO rtn = new CmpdFragmentPChemVO();

        try {

            if (entityIn != null) {
                rtn.setId(entityIn.getId());
                rtn.setMolecularWeight(entityIn.getMolecularWeight());
                rtn.setMolecularFormula(entityIn.getMolecularFormula());
                rtn.setLogD(entityIn.getLogD());
                rtn.setCountHydBondAcceptors(entityIn.getCountHydBondAcceptors());
                rtn.setCountHydBondDonors(entityIn.getCountHydBondDonors());
                rtn.setSurfaceArea(entityIn.getSurfaceArea());
                rtn.setSolubility(entityIn.getSolubility());
                rtn.setCountRings(entityIn.getCountRings());
                rtn.setCountAtoms(entityIn.getCountAtoms());
                rtn.setCountBonds(entityIn.getCountBonds());
                rtn.setCountSingleBonds(entityIn.getCountSingleBonds());
                rtn.setCountDoubleBonds(entityIn.getCountDoubleBonds());
                rtn.setCountTripleBonds(entityIn.getCountTripleBonds());
                rtn.setCountRotatableBonds(entityIn.getCountRotatableBonds());
                rtn.setCountHydrogenAtoms(entityIn.getCountHydrogenAtoms());
                rtn.setCountMetalAtoms(entityIn.getCountMetalAtoms());
                rtn.setCountHeavyAtoms(entityIn.getCountHeavyAtoms());
                rtn.setCountPositiveAtoms(entityIn.getCountPositiveAtoms());
                rtn.setCountNegativeAtoms(entityIn.getCountNegativeAtoms());
                rtn.setCountRingBonds(entityIn.getCountRingBonds());
                rtn.setCountStereoAtoms(entityIn.getCountStereoAtoms());
                rtn.setCountStereoBonds(entityIn.getCountStereoBonds());
                rtn.setCountRingAssemblies(entityIn.getCountRingAssemblies());
                rtn.setCountAromaticBonds(entityIn.getCountAromaticBonds());
                rtn.setCountAromaticRings(entityIn.getCountAromaticRings());
                rtn.setFormalCharge(entityIn.getFormalCharge());
                rtn.setTheALogP(entityIn.getTheALogP());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;

    }

    private static CmpdFragmentStructureVO translateCmpdFragmentStructure(CmpdTable entityIn) {

        CmpdFragmentStructureVO rtn = new CmpdFragmentStructureVO();

        try {

            if (entityIn != null) {
                rtn.setId(entityIn.getId());
                rtn.setInchi(entityIn.getInchi());
                rtn.setInchiAux(entityIn.getInchiAux());
                rtn.setCtab(entityIn.getCtab());
                rtn.setCanSmi(entityIn.getCanSmi());
                rtn.setCanTaut(entityIn.getCanTaut());
                rtn.setCanTautStripStereo(entityIn.getCanTautStripStereo());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    public static CmpdAnnotationVO translateCmpdAnnotation(CmpdTable entityIn) {

        CmpdAnnotationVO rtn = new CmpdAnnotationVO();

        rtn.setGeneralComment(entityIn.getGeneralComment());
        rtn.setMtxt(entityIn.getMtxt());
        rtn.setPseudoAtoms(entityIn.getPseudoAtoms());
        rtn.setPurityComment(entityIn.getPurityComment());
        rtn.setStereochemistryComment(entityIn.getStereochemistryComment());

        return rtn;

    }

    private static CmpdBioAssayVO translateCmpdBioAssay(CmpdTable entityIn) {

        CmpdBioAssayVO rtn = new CmpdBioAssayVO();

        try {
            rtn.setId(entityIn.getId());
            rtn.setHf(entityIn.getHf());
            rtn.setNci60(entityIn.getNci60());
            rtn.setXeno(entityIn.getXeno());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;
    }

    public static CmpdVO translateCmpd(CmpdTable entityIn) {

        CmpdVO cmpdVO = new CmpdVO();

        // Does NOT propagate to CmpdTable cmpdVO.setComment(entityIn.getComment());
        try {

            cmpdVO.setConf(entityIn.getConf());
            cmpdVO.setDistribution(entityIn.getDistribution());
            cmpdVO.setDiscreet(entityIn.getDiscreet());
            cmpdVO.setId(entityIn.getId());
            cmpdVO.setNscCmpdId(entityIn.getNscCmpdId());
            cmpdVO.setAdHocCmpdId(entityIn.getAdHocCmpdId());

            cmpdVO.setNscCmpdType(entityIn.getNscCmpdType());

            if (DEBUG) {
                System.out.println("In TransformCmpdTableToVO.toCmpdVo.  Setting originalAdHocCmpdId to: " + entityIn.getOriginalAdHocCmpdId());
            }
            cmpdVO.setOriginalAdHocCmpdId(entityIn.getOriginalAdHocCmpdId());

            cmpdVO.setPrefix(entityIn.getPrefix());
            cmpdVO.setNsc(entityIn.getNsc());
            if (entityIn.getNsc() != null && entityIn.getNsc() > 0) {
                cmpdVO.setName("NSC " + entityIn.getPrefix() + entityIn.getNsc());
            } else {
                cmpdVO.setName(entityIn.getName());
            }
            cmpdVO.setDistribution(entityIn.getDistribution());
            cmpdVO.setConf(entityIn.getConf());
            cmpdVO.setCas(entityIn.getCas());

            cmpdVO.setParentFragment(translateCmpdFragment(entityIn));
            cmpdVO.setSaltMf(entityIn.getSaltMf());
            cmpdVO.setSaltMw(entityIn.getSaltMw());
            cmpdVO.setSaltName(entityIn.getName());
            cmpdVO.setSaltSmiles(entityIn.getSaltSmiles());

            cmpdVO.setParentStoichiometry(entityIn.getParentStoichiometry());
            cmpdVO.setSaltStoichiometry(entityIn.getSaltStoichiometry());
            
            cmpdVO.setCmpdBioAssay(translateCmpdBioAssay(entityIn));

            cmpdVO.setInventory(entityIn.getInventory());


            cmpdVO.setAliases(parseFormattedString(entityIn.getFormattedAliasesString()));
            cmpdVO.setNamedSets(parseFormattedString(entityIn.getFormattedSetsString()));
            cmpdVO.setPlates(parseFormattedString(entityIn.getFormattedPlatesString()));
            cmpdVO.setProjects(parseFormattedString(entityIn.getFormattedProjectsString()));
            cmpdVO.setTargets(parseFormattedString(entityIn.getFormattedTargetsString()));

            cmpdVO.setCmpdFragmentSmilesStrings(parseFormattedString(entityIn.getFormattedFragmentsString()));

            if (DEBUG) {
                System.out.println("Setting countCmpdFragments to: " + cmpdVO.getCmpdFragmentSmilesStrings().size());
            }

            cmpdVO.setCountCmpdFragments(cmpdVO.getCmpdFragmentSmilesStrings().size());

            cmpdVO.setCmpdFragments(parseFormattedFragmentsString(entityIn.getFormattedFragmentsString()));


        } catch (Exception e) {
            e.printStackTrace();
        }

        return cmpdVO;
    }

    public static List<CmpdVO> translateListOfCmpdTables(List<CmpdTable> entityListIn) {
        List<CmpdVO> returnList = new ArrayList<CmpdVO>();
        for (CmpdTable cv : entityListIn) {
            returnList.add(translateCmpd(cv));
        }
        return returnList;
    }

    public static CmpdListVO translateCmpdList(CmpdList entityList, Boolean includeListMembers) {

        CmpdListVO rtn = new CmpdListVO();

        if (entityList == null) {
            System.out.println("Entity list is null in TransformCmpdTableToVO.java");
        }

        try {
            rtn.setId(entityList.getId());
            rtn.setCmpdListId(entityList.getCmpdListId());
            rtn.setListName(entityList.getListName());
            rtn.setDateCreated(entityList.getDateCreated());
            rtn.setListOwner(entityList.getListOwner());
            rtn.setShareWith(entityList.getShareWith());
            rtn.setCountListMembers(entityList.getCountListMembers());

            rtn.setAnchorComment(entityList.getAnchorComment());
            rtn.setAnchorSmiles(entityList.getAnchorSmiles());
            rtn.setListComment(entityList.getListComment());

            if (includeListMembers.booleanValue()) {

                ArrayList<CmpdListMemberVO> voList = new ArrayList<CmpdListMemberVO>();

                Collection<CmpdListMember> entityColl = entityList.getCmpdListMembers();

                for (CmpdListMember lm : entityColl) {
                    voList.add(translateCmpdListMember(lm));
                }

                rtn.setCmpdListMembers(voList);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    public static CmpdListMemberVO translateCmpdListMember(CmpdListMember entityIn) {

        CmpdListMemberVO rtn = new CmpdListMemberVO();

        try {
            rtn.setId(entityIn.getId());
            //rtn.setCmpd(toCmpdVO(entityIn.getCmpdTable()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return rtn;

    }
}
