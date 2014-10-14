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
import mwk.datasystem.domain.NscCmpd;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mwk.datasystem.domain.CmpdAnnotation;
import mwk.datasystem.domain.CmpdLegacyCmpd;
import mwk.datasystem.domain.CmpdNamedSet;
import mwk.datasystem.domain.CmpdPlate;
import mwk.datasystem.domain.CmpdProject;
import mwk.datasystem.domain.DataSystemRole;
import mwk.datasystem.domain.DataSystemUser;
import mwk.datasystem.domain.NscCmpdImpl;
import mwk.datasystem.vo.CmpdAliasVO;
import mwk.datasystem.vo.CmpdAnnotationVO;
import mwk.datasystem.vo.CmpdBioAssayVO;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdFragmentStructureVO;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdNamedSetVO;
import mwk.datasystem.vo.CmpdPlateVO;
import mwk.datasystem.vo.CmpdProjectVO;
import mwk.datasystem.vo.CmpdTargetVO;
import mwk.datasystem.vo.DataSystemRoleVO;
import mwk.datasystem.vo.DataSystemUserVO;

/**
 *
 * @author mwkunkel
 */
public class TransformAndroToVO {

    public static final Boolean DEBUG = Boolean.FALSE;
    //

    public static CmpdAliasVO toCmpdAliasVO(CmpdAlias entityIn) {
        CmpdAliasVO rtn = new CmpdAliasVO();
        rtn.setAlias(entityIn.getAlias());        
        return rtn;
    }

    public static CmpdTargetVO toCmpdTargetVO(CmpdTarget entityIn) {
        CmpdTargetVO rtn = new CmpdTargetVO();
        rtn.setTarget(entityIn.getTarget());
        return rtn;
    }

    public static CmpdNamedSetVO toCmpdNamedSetVO(CmpdNamedSet entityIn) {
        CmpdNamedSetVO rtn = new CmpdNamedSetVO();
        rtn.setSetName(entityIn.getSetName());
        return rtn;
    }

    public static CmpdProjectVO toCmpdProjectVO(CmpdProject entityIn) {
        CmpdProjectVO rtn = new CmpdProjectVO();
        rtn.setProjectCode(entityIn.getProjectCode());
        rtn.setProjectName(entityIn.getProjectName());
        return rtn;
    }

    public static CmpdPlateVO toCmpdPlateVO(CmpdPlate entityIn) {
        CmpdPlateVO rtn = new CmpdPlateVO();
        rtn.setPlateName(entityIn.getPlateName());
        return rtn;
    }

    public static CmpdLegacyCmpdVO toCmpdLegacyCmpdVO(CmpdLegacyCmpd entityIn) {

        CmpdLegacyCmpdVO rtn = new CmpdLegacyCmpdVO();

        rtn.setId(entityIn.getId());
        rtn.setNsc(entityIn.getId().intValue());
        rtn.setCtab(entityIn.getCtab());
        rtn.setJpg512(entityIn.getJpg512());
        rtn.setMolecularFormula(entityIn.getMolecularFormula());
        rtn.setMolecularWeight(entityIn.getMolecularWeight());

        return rtn;
    }

    public static DataSystemUserVO translateDataSystemUser(DataSystemUser entityIn) {

        DataSystemUserVO rtn = new DataSystemUserVO();

        rtn.setId(entityIn.getId());
        rtn.setRealName(entityIn.getRealName());
        rtn.setUserName(entityIn.getUserName());
        rtn.setDataSystemRoles(translateDataSystemRoles(entityIn.getDataSystemRoles()));

        return rtn;

    }

    public static List<DataSystemRoleVO> translateDataSystemRoles(Collection<DataSystemRole> entityListIn) {

        List<DataSystemRoleVO> rtnList = new ArrayList<DataSystemRoleVO>();

        for (DataSystemRole dsr : entityListIn) {
            rtnList.add(translateDataSystemRole(dsr));
        }

        return rtnList;

    }

    public static DataSystemRoleVO translateDataSystemRole(DataSystemRole entityIn) {

        DataSystemRoleVO rtn = new DataSystemRoleVO();

        rtn.setId(entityIn.getId());
        rtn.setRoleName(entityIn.getRoleName());
        rtn.setRoleDescription(entityIn.getRoleDescription());

        return rtn;

    }

    public static List<CmpdListVO> translateCmpdLists(List<CmpdList> entityListIn, Boolean includeListMembers) {

        List<CmpdListVO> returnList = new ArrayList<CmpdListVO>();
        for (CmpdList nl : entityListIn) {
            returnList.add(toCmpdListVO(nl, includeListMembers));
        }
        return returnList;

    }

    public static CmpdFragmentVO toCmpdFragmentVO(CmpdFragment entityIn) {

        CmpdFragmentVO rtn = new CmpdFragmentVO();

        rtn.setId(entityIn.getId());

        if (entityIn.getCmpdFragmentStructure() != null) {
            rtn.setCmpdFragmentStructure(toCmpdFragmentStructureVO(entityIn.getCmpdFragmentStructure()));
        }

        if (entityIn.getCmpdFragmentPChem() != null) {
            rtn.setCmpdFragmentPChem(toCmpdFragmentPChemVO(entityIn.getCmpdFragmentPChem()));
        }

        return rtn;

    }

    public static CmpdFragmentVO adHocToCmpdFragmentVO(AdHocCmpdFragment entityIn) {

        CmpdFragmentVO rtn = new CmpdFragmentVO();

        rtn.setId(entityIn.getId());

        if (entityIn.getAdHocCmpdFragmentStructure() != null) {
            rtn.setCmpdFragmentStructure(adHocToCmpdFragmentStructureVO(entityIn.getAdHocCmpdFragmentStructure()));
        }

        if (entityIn.getAdHocCmpdFragmentPChem() != null) {
            rtn.setCmpdFragmentPChem(adHocToCmpdFragmentPChemVO(entityIn.getAdHocCmpdFragmentPChem()));
        }

        return rtn;
    }

    public static CmpdFragmentPChemVO adHocToCmpdFragmentPChemVO(AdHocCmpdFragmentPChem entityIn) {

        CmpdFragmentPChemVO rtn = new CmpdFragmentPChemVO();

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

        return rtn;

    }

    public static CmpdFragmentPChemVO toCmpdFragmentPChemVO(CmpdFragmentPChem entityIn) {

        CmpdFragmentPChemVO rtn = new CmpdFragmentPChemVO();

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

        return rtn;

    }

    public static CmpdFragmentStructureVO adHocToCmpdFragmentStructureVO(AdHocCmpdFragmentStructure entityIn) {

        CmpdFragmentStructureVO rtn = new CmpdFragmentStructureVO();

        if (entityIn != null) {
            rtn.setId(entityIn.getId());
            rtn.setInchi(entityIn.getInchi());
            rtn.setInchiAux(entityIn.getInchiAux());
            rtn.setCtab(entityIn.getCtab());
            rtn.setCanSmi(entityIn.getCanSmi());
            rtn.setCanTaut(entityIn.getCanTaut());
            rtn.setCanTautStripStereo(entityIn.getCanTautStripStereo());
        }

        return rtn;

    }

    public static CmpdFragmentStructureVO toCmpdFragmentStructureVO(CmpdFragmentStructure entityIn) {

        CmpdFragmentStructureVO rtn = new CmpdFragmentStructureVO();

        if (entityIn != null) {
            rtn.setId(entityIn.getId());
            rtn.setInchi(entityIn.getInchi());
            rtn.setInchiAux(entityIn.getInchiAux());
            rtn.setCtab(entityIn.getCtab());
            rtn.setCanSmi(entityIn.getCanSmi());
            rtn.setCanTaut(entityIn.getCanTaut());
            rtn.setCanTautStripStereo(entityIn.getCanTautStripStereo());
        }

        return rtn;

    }

    public static CmpdAnnotationVO toCmpdAnnotationVO(CmpdAnnotation entityIn) {

        CmpdAnnotationVO rtn = new CmpdAnnotationVO();

        rtn.setGeneralComment(entityIn.getGeneralComment());
        rtn.setMtxt(entityIn.getMtxt());
        rtn.setPseudoAtoms(entityIn.getPseudoAtoms());
        rtn.setPurityComment(entityIn.getPurityComment());
        rtn.setStereochemistryComment(entityIn.getStereochemistryComment());

        return rtn;

    }

    public static CmpdBioAssayVO toCmpdBioAssayVO(CmpdBioAssay entityIn) {

        CmpdBioAssayVO rtn = new CmpdBioAssayVO();

        rtn.setId(entityIn.getId());
        rtn.setHf(entityIn.getHf());
        rtn.setNci60(entityIn.getNci60());
        rtn.setXeno(entityIn.getXeno());
        rtn.setSarcoma(entityIn.getSarcoma());

        return rtn;
    }

    public static CmpdVO toCmpdVO(Cmpd incomingCmpd) {

        CmpdVO cmpdVO = new CmpdVO();

        Cmpd entityIn = Unproxy.initializeAndUnproxy(incomingCmpd);

        System.out.println("incomingCmpd in toCmpdVO is class: " + entityIn.getClass().getName());

        if (entityIn instanceof NscCmpdImpl) {

            NscCmpd nscc = (NscCmpd) entityIn;

            if (null != nscc) {

                // identifier
                cmpdVO.setId(nscc.getId());
                cmpdVO.setNscCmpdId(nscc.getNscCmpdId());
                cmpdVO.setPrefix(nscc.getPrefix());
                cmpdVO.setNsc(nscc.getNsc());
                cmpdVO.setCas(nscc.getCas());
                cmpdVO.setName("NSC " + nscc.getPrefix() + nscc.getNsc());
                cmpdVO.setDistribution(nscc.getDistribution());
                cmpdVO.setConf(nscc.getConf());
                cmpdVO.setDiscreet(nscc.getDiscreet());

                cmpdVO.setNscCmpdType(nscc.getNscCmpdType().getNscCmpdType());

                cmpdVO.setInventory(nscc.getCmpdInventory().getInventory());

                cmpdVO.setCmpdBioAssay(toCmpdBioAssayVO(nscc.getCmpdBioAssay()));

                cmpdVO.setParentFragment(toCmpdFragmentVO(nscc.getCmpdParentFragment()));

                cmpdVO.setCountCmpdFragments(nscc.getCountFragments());

//                cmpdVO.setIsSelected(nscc.getIsSelected());
//                cmpdVO.setSaltSmiles(nscc.getSaltSmiles()); 
//                cmpdVO.setSaltName(nscc.getSaltName());
//                cmpdVO.setSaltMf(nscc.getSaltMf());
//                cmpdVO.setSaltMw(nscc.getSaltMw());
//                cmpdVO.setParentStoichiometry(nscc.getParentStoichiometry());
//                cmpdVO.setSaltStoichiometry(nscc.getSaltStoichiometry());

                cmpdVO.setNscCmpdType(nscc.getNscCmpdType().getNscCmpdType());
                cmpdVO.setIdentifierString(nscc.getIdentifierString());
                cmpdVO.setDescriptorString(nscc.getDescriptorString());
                cmpdVO.setCountHydrogenAtoms(nscc.getCmpdParentFragment().getCmpdFragmentPChem().getCountHydrogenAtoms());
                cmpdVO.setCountMetalAtoms(nscc.getCmpdParentFragment().getCmpdFragmentPChem().getCountMetalAtoms());
                cmpdVO.setCountHeavyAtoms(nscc.getCmpdParentFragment().getCmpdFragmentPChem().getCountHeavyAtoms());

                cmpdVO.setCmpdAnnotation(toCmpdAnnotationVO(nscc.getCmpdAnnotation()));

                cmpdVO.setIdentifierString(nscc.getIdentifierString());
                cmpdVO.setDescriptorString(nscc.getDescriptorString());
                cmpdVO.setMolecularWeight(nscc.getMolecularWeight());
                cmpdVO.setMolecularFormula(nscc.getMolecularFormula());

                Collection<CmpdTarget> entityColl = nscc.getCmpdTargets();
                ArrayList<String> strColl = new ArrayList<String>();
                for (CmpdTarget ct : entityColl) {
                    strColl.add(ct.getTarget());
                }
                cmpdVO.setTargets(strColl);

                Collection<CmpdAlias> aliasColl = nscc.getCmpdAliases();
                strColl = new ArrayList<String>();
                for (CmpdAlias ca : aliasColl) {
                    strColl.add(ca.getAlias());
                }
                cmpdVO.setAliases(strColl);

                Collection<CmpdNamedSet> setColl = nscc.getCmpdNamedSets();
                strColl = new ArrayList<String>();
                for (CmpdNamedSet cs : setColl) {
                    strColl.add(cs.getSetName());
                }
                cmpdVO.setNamedSets(strColl);

                Collection<CmpdProject> projColl = nscc.getCmpdProjects();
                strColl = new ArrayList<String>();
                for (CmpdProject cp : projColl) {
                    strColl.add(cp.getProjectName());
                }
                cmpdVO.setProjects(strColl);

                Collection<CmpdPlate> plateColl = nscc.getCmpdPlates();
                strColl = new ArrayList<String>();
                for (CmpdPlate cp : plateColl) {
                    strColl.add(cp.getPlateName());
                }
                cmpdVO.setPlates(strColl);

                Collection<CmpdTarget> targetColl = nscc.getCmpdTargets();
                strColl = new ArrayList<String>();
                for (CmpdTarget ct : targetColl) {
                    strColl.add(ct.getTarget());
                }
                cmpdVO.setTargets(strColl);

                Collection<CmpdFragment> fragColl = nscc.getCmpdFragments();
                ArrayList<CmpdFragmentVO> voFragColl = new ArrayList<CmpdFragmentVO>();
                ArrayList<String> fragList = new ArrayList<String>();
                for (CmpdFragment cf : fragColl) {
                    voFragColl.add(toCmpdFragmentVO(cf));
                    fragList.add(cf.getCmpdFragmentStructure().getCanSmi());
                }
                cmpdVO.setCmpdFragments(voFragColl);
                cmpdVO.setCmpdFragmentSmilesStrings(fragList);

            }

            // is there a parent fragment

            if (nscc.getCmpdParentFragment() != null) {

                cmpdVO.setParentFragment(toCmpdFragmentVO(nscc.getCmpdParentFragment()));

            } else {

                // nsc_cmpd should already have a parent fragment
                // if not, calculation on-the-fly by size

                ArrayList<CmpdFragmentVO> fragList = new ArrayList<CmpdFragmentVO>(cmpdVO.getCmpdFragments());

                // some cmpds will have no fragments
                // or no parentFragment

                if (fragList.size() > 0) {
                    Collections.sort(fragList, new Comparators.CmpdFragmentSizeComparator());
                    Collections.reverse(fragList);
                    cmpdVO.setParentFragment(fragList.get(0));

                    ArrayList<String> smilesStrings = new ArrayList<String>();
                    for (CmpdFragmentVO cf : fragList) {
                        smilesStrings.add(cf.getCmpdFragmentStructure().getCanSmi());
                    }

                    cmpdVO.setCmpdFragmentSmilesStrings(smilesStrings);
                    cmpdVO.setCountCmpdFragments(cmpdVO.getCmpdFragmentSmilesStrings().size());

                    if (DEBUG) {
                        System.out.println("setting countCmpdFragments to: " + cmpdVO.getCmpdFragmentSmilesStrings().size());
                    }

                }

            }

        } else {

            AdHocCmpd ahc = (AdHocCmpd) entityIn;

            cmpdVO.setId(ahc.getId());
            cmpdVO.setAdHocCmpdId(ahc.getAdHocCmpdId());
            cmpdVO.setOriginalAdHocCmpdId(ahc.getOriginalAdHocCmpdId());
            cmpdVO.setName(ahc.getName());

            // no other cmpd properties
            // no CmpdBioAssay

            Collection<AdHocCmpdFragment> fragColl = ahc.getAdHocCmpdFragments();
            ArrayList<CmpdFragmentVO> voFragColl = new ArrayList<CmpdFragmentVO>();
            for (AdHocCmpdFragment ahcf : fragColl) {
                voFragColl.add(adHocToCmpdFragmentVO(ahcf));
            }
            cmpdVO.setCmpdFragments(voFragColl);

            // AdHoc Cmpd needs parent fragment

            ArrayList<CmpdFragmentVO> fragList = new ArrayList<CmpdFragmentVO>(cmpdVO.getCmpdFragments());

            Collections.sort(fragList, new Comparators.CmpdFragmentSizeComparator());
            Collections.reverse(fragList);

            cmpdVO.setParentFragment(fragList.get(0));
        }

        return cmpdVO;
    }

    public static List<CmpdVO> translateListOfCmpds(List<Cmpd> entityListIn) {
        List<CmpdVO> returnList = new ArrayList<CmpdVO>();
        for (Cmpd c : entityListIn) {
            returnList.add(toCmpdVO(c));
        }
        return returnList;
    }

    public static CmpdListVO toCmpdListVO(CmpdList entityList, Boolean includeListMembers) {

        CmpdListVO rtn = new CmpdListVO();

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
                    voList.add(toCmpdListMemberVO(lm));
                }

                rtn.setCmpdListMembers(voList);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;
    }

    public static CmpdListMemberVO toCmpdListMemberVO(CmpdListMember entityIn) {

        CmpdListMemberVO rtn = new CmpdListMemberVO();

        rtn.setId(entityIn.getId());
        rtn.setListMemberComment(entityIn.getListMemberComment());
        rtn.setCmpd(toCmpdVO(entityIn.getCmpd()));


        return rtn;

    }
}
