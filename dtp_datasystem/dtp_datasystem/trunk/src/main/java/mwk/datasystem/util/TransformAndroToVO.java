/*
 * To change this template, choose Tools | Templates
 
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
import mwk.datasystem.domain.CmpdKnownSalt;
import mwk.datasystem.domain.CmpdLegacyCmpd;
import mwk.datasystem.domain.CmpdNamedSet;
import mwk.datasystem.domain.CmpdPlate;
import mwk.datasystem.domain.CmpdProject;
import mwk.datasystem.domain.CuratedName;
import mwk.datasystem.domain.CuratedNsc;
import mwk.datasystem.domain.CuratedOriginator;
import mwk.datasystem.domain.CuratedProject;
import mwk.datasystem.domain.CuratedTarget;
import mwk.datasystem.domain.NscCmpdImpl;
import mwk.datasystem.domain.TanimotoScores;
import mwk.datasystem.vo.CmpdAliasVO;
import mwk.datasystem.vo.CmpdAnnotationVO;
import mwk.datasystem.vo.CmpdBioAssayVO;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdFragmentStructureVO;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdKnownSaltVO;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdNamedSetVO;
import mwk.datasystem.vo.CmpdPlateVO;
import mwk.datasystem.vo.CmpdProjectVO;
import mwk.datasystem.vo.CmpdTargetVO;
import mwk.datasystem.vo.CuratedNscVO;
import mwk.datasystem.vo.CuratedNameVO;
import mwk.datasystem.vo.CuratedOriginatorVO;
import mwk.datasystem.vo.CuratedProjectVO;
import mwk.datasystem.vo.CuratedTargetVO;

import mwk.datasystem.vo.TanimotoScoresVO;

/**
 *
 * @author mwkunkel
 */
public class TransformAndroToVO {

    public static final Boolean DEBUG = Boolean.FALSE;

    public static List<CuratedNscVO> translateCuratedNscs(Collection<CuratedNsc> listIn) {
        ArrayList<CuratedNscVO> rtnList = new ArrayList<CuratedNscVO>();
        for (CuratedNsc cn : listIn) {
            rtnList.add(translateCuratedNsc(cn));
        }
        return rtnList;
    }

    public static CuratedNscVO translateCuratedNsc(CuratedNsc ent) {

        CuratedNscVO rtn = new CuratedNscVO();

        rtn.setId(ent.getId());
        rtn.setAliases(translateCuratedNames(ent.getAliases()));
        rtn.setCas(ent.getCas());
        rtn.setGenericName(translateCuratedName(ent.getGenericName()));
        rtn.setNsc(ent.getNsc());
        rtn.setOriginator(translateCuratedOriginator(ent.getOriginator()));
        rtn.setPreferredName(translateCuratedName(ent.getPreferredName()));
        rtn.setPrimaryTarget(translateCuratedTarget(ent.getPrimaryTarget()));
        rtn.setProjects(translateCuratedProjects(ent.getProjects()));
        rtn.setSecondaryTargets(translateCuratedTargets(ent.getSecondaryTargets()));

        return rtn;

    }

    public static List<CuratedNameVO> translateCuratedNames(Collection<CuratedName> listIn) {
        ArrayList<CuratedNameVO> rtnList = new ArrayList<CuratedNameVO>();
        for (CuratedName cn : listIn) {
            rtnList.add(translateCuratedName(cn));
        }
        return rtnList;
    }

    public static CuratedNameVO translateCuratedName(CuratedName ent) {
        CuratedNameVO rtn = null; //new CuratedNameVO();
        if (ent != null) {
            rtn = new CuratedNameVO();
            rtn.setId(ent.getId());
            rtn.setValue(ent.getValue());
            rtn.setDescription(ent.getDescription());
            rtn.setReference(ent.getReference());
        }
        return rtn;
    }

    public static List<CuratedProjectVO> translateCuratedProjects(Collection<CuratedProject> listIn) {
        ArrayList<CuratedProjectVO> rtnList = new ArrayList<CuratedProjectVO>();
        for (CuratedProject cn : listIn) {
            rtnList.add(translateCuratedProject(cn));
        }
        return rtnList;
    }

    public static CuratedProjectVO translateCuratedProject(CuratedProject ent) {
        CuratedProjectVO rtn = null; //new CuratedProjectVO();
        if (ent != null) {
            rtn = new CuratedProjectVO();
            rtn.setId(ent.getId());
            rtn.setValue(ent.getValue());
            rtn.setDescription(ent.getDescription());
            rtn.setReference(ent.getReference());
        }
        return rtn;
    }

    public static List<CuratedTargetVO> translateCuratedTargets(Collection<CuratedTarget> listIn) {
        ArrayList<CuratedTargetVO> rtnList = new ArrayList<CuratedTargetVO>();
        for (CuratedTarget cn : listIn) {
            rtnList.add(translateCuratedTarget(cn));
        }
        return rtnList;
    }

    public static CuratedTargetVO translateCuratedTarget(CuratedTarget ent) {
        CuratedTargetVO rtn = null; //new CuratedTargetVO();
        if (ent != null) {
            rtn = new CuratedTargetVO();
            rtn.setId(ent.getId());
            rtn.setValue(ent.getValue());
            rtn.setDescription(ent.getDescription());
            rtn.setReference(ent.getReference());
        }
        return rtn;
    }

    public static List<CuratedOriginatorVO> translateCuratedOriginators(List<CuratedOriginator> listIn) {
        ArrayList<CuratedOriginatorVO> rtnList = new ArrayList<CuratedOriginatorVO>();
        for (CuratedOriginator cn : listIn) {
            rtnList.add(translateCuratedOriginator(cn));
        }
        return rtnList;
    }

    public static CuratedOriginatorVO translateCuratedOriginator(CuratedOriginator ent) {
        CuratedOriginatorVO rtn = null; //new CuratedOriginatorVO();
        if (ent != null) {
            rtn = new CuratedOriginatorVO();
            rtn.setId(ent.getId());
            rtn.setValue(ent.getValue());
            rtn.setDescription(ent.getDescription());
            rtn.setReference(ent.getReference());
        }
        return rtn;
    }

    public static TanimotoScoresVO translateTanimotoScores(TanimotoScores entityIn) {

        TanimotoScoresVO rtn = new TanimotoScoresVO();

        rtn.setNsc1(entityIn.getNsc1());
        rtn.setNsc2(entityIn.getNsc2());
        rtn.setAtomPairBv(entityIn.getAtomPairBv());
        rtn.setFeatMorganBv(entityIn.getFeatMorganBv());
        rtn.setLayered(entityIn.getLayered());
        rtn.setMaccs(entityIn.getMaccs());
        rtn.setMorganBv(entityIn.getMorganBv());
        rtn.setRdkit(entityIn.getRdkit());
        rtn.setTorsionBv(entityIn.getTorsionBv());

        return rtn;

    }

    public static CmpdKnownSaltVO translateCmpdKnownSalt(CmpdKnownSalt entityIn) {
        CmpdKnownSaltVO rtn = new CmpdKnownSaltVO();
        if (entityIn != null) {
            rtn.setId(entityIn.getId());
            rtn.setCountOccurences(entityIn.getCountOccurences());
            rtn.setSaltComment(entityIn.getSaltComment());
            rtn.setCanSmi(entityIn.getCanSmi());
            rtn.setCanTaut(entityIn.getCanTaut());
            rtn.setCanTautStripStereo(entityIn.getCanTautStripStereo());
            rtn.setSaltMf(entityIn.getSaltMf());
            rtn.setSaltMw(entityIn.getSaltMw());
            rtn.setSaltCharge(entityIn.getSaltCharge());
            rtn.setSaltName(entityIn.getSaltName());
        }
        return rtn;
    }

    public static CmpdAliasVO translateCmpdAlias(CmpdAlias entityIn) {
        CmpdAliasVO rtn = new CmpdAliasVO();
        rtn.setAlias(entityIn.getAlias());
        return rtn;
    }

    public static CmpdTargetVO translateCmpdTarget(CmpdTarget entityIn) {
        CmpdTargetVO rtn = new CmpdTargetVO();
        rtn.setTarget(entityIn.getTarget());
        return rtn;
    }

    public static CmpdNamedSetVO translateCmpdNamedSet(CmpdNamedSet entityIn) {
        CmpdNamedSetVO rtn = new CmpdNamedSetVO();
        rtn.setSetName(entityIn.getSetName());
        return rtn;
    }

    public static CmpdProjectVO translateCmpdProject(CmpdProject entityIn) {
        CmpdProjectVO rtn = new CmpdProjectVO();
        rtn.setProjectCode(entityIn.getProjectCode());
        rtn.setProjectName(entityIn.getProjectName());
        return rtn;
    }

    public static CmpdPlateVO translateCmpdPlate(CmpdPlate entityIn) {
        CmpdPlateVO rtn = new CmpdPlateVO();
        rtn.setPlateName(entityIn.getPlateName());
        return rtn;
    }

    public static CmpdLegacyCmpdVO translateCmpdLegacyCmpd(CmpdLegacyCmpd entityIn) {

        System.out.println("In translateCmpdLegacyCmpd in TransformAndroToVO");

        CmpdLegacyCmpdVO rtn = new CmpdLegacyCmpdVO();

        if (entityIn != null) {
            rtn.setId(entityIn.getId());
            rtn.setNsc(entityIn.getNsc());
            rtn.setProdCtab(entityIn.getProdCtab());
            rtn.setGif512(entityIn.getGif512());
            rtn.setProdMolecularFormula(entityIn.getProdMolecularFormula());
            rtn.setProdFormulaWeight(entityIn.getProdFormulaWeight());
        }

        return rtn;
    }

    public static List<CmpdListVO> translateCmpdLists(List<CmpdList> entityListIn, Boolean includeListMembers) {

        List<CmpdListVO> returnList = new ArrayList<CmpdListVO>();
        for (CmpdList nl : entityListIn) {
            returnList.add(translateCmpdList(nl, includeListMembers));
        }
        return returnList;

    }

    public static CmpdFragmentVO translateCmpdFragment(CmpdFragment entityIn) {

        CmpdFragmentVO rtn = new CmpdFragmentVO();

        if (entityIn != null) {

            rtn.setId(entityIn.getId());
            rtn.setStoichiometry(entityIn.getStoichiometry());

            if (entityIn.getCmpdFragmentStructure() != null) {
                rtn.setCmpdFragmentStructure(translateCmpdFragmentStructure(entityIn.getCmpdFragmentStructure()));
            }

            if (entityIn.getCmpdFragmentPChem() != null) {
                rtn.setCmpdFragmentPChem(translateCmpdFragmentPChem(entityIn.getCmpdFragmentPChem()));
            }

        }

        return rtn;

    }

    public static CmpdFragmentVO translateAdHocCmpdFragment(AdHocCmpdFragment entityIn) {

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

    public static CmpdFragmentPChemVO translateCmpdFragmentPChem(CmpdFragmentPChem entityIn) {

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

    public static CmpdFragmentStructureVO translateCmpdFragmentStructure(CmpdFragmentStructure entityIn) {

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

    public static CmpdAnnotationVO translateCmpdAnnotation(CmpdAnnotation entityIn) {

        CmpdAnnotationVO rtn = new CmpdAnnotationVO();

        rtn.setGeneralComment(entityIn.getGeneralComment());
        rtn.setMtxt(entityIn.getMtxt());
        rtn.setPseudoAtoms(entityIn.getPseudoAtoms());
        rtn.setPurityComment(entityIn.getPurityComment());
        rtn.setStereochemistryComment(entityIn.getStereochemistryComment());

        return rtn;

    }

    public static CmpdBioAssayVO translateCmpdBioAssay(CmpdBioAssay entityIn) {

        CmpdBioAssayVO rtn = new CmpdBioAssayVO();

        rtn.setId(entityIn.getId());
        rtn.setHf(entityIn.getHf());
        rtn.setNci60(entityIn.getNci60());
        rtn.setXeno(entityIn.getXeno());
        rtn.setSarcoma(entityIn.getSarcoma());

        return rtn;
    }

    public static CmpdVO translateCmpd(Cmpd entityIn) {

        CmpdVO rtn = new CmpdVO();

        if (entityIn instanceof NscCmpdImpl) {

            NscCmpd nscc = (NscCmpd) entityIn;

            if (null != nscc) {

                rtn.setProdFormulaWeight(nscc.getFormulaWeight());
                rtn.setProdMolecularFormula(nscc.getProdMolecularFormula());
                rtn.setFormalCharge(nscc.getFormalCharge());

                // identifier
                rtn.setId(nscc.getId());
                rtn.setNscCmpdId(nscc.getNscCmpdId());
                rtn.setPrefix(nscc.getPrefix());
                rtn.setNsc(nscc.getNsc());
                rtn.setCas(nscc.getCas());
                rtn.setName(nscc.getName());
                rtn.setDistribution(nscc.getDistribution());
                rtn.setConf(nscc.getConf());
                rtn.setDiscreet(nscc.getDiscreet());

                System.out.println("Just before setNscCmpdType in translateCmpd in TransformAndroToVO");

                rtn.setNscCmpdType(nscc.getNscCmpdType().getNscCmpdType());

                rtn.setInventory(nscc.getCmpdInventory().getInventory());

                rtn.setCmpdBioAssay(translateCmpdBioAssay(nscc.getCmpdBioAssay()));

                if (nscc.getCmpdParentFragment() != null) {
                    rtn.setParentFragment(translateCmpdFragment(nscc.getCmpdParentFragment()));
                    rtn.setCountHydrogenAtoms(nscc.getCmpdParentFragment().getCmpdFragmentPChem().getCountHydrogenAtoms());
                    rtn.setCountMetalAtoms(nscc.getCmpdParentFragment().getCmpdFragmentPChem().getCountMetalAtoms());
                    rtn.setCountHeavyAtoms(nscc.getCmpdParentFragment().getCmpdFragmentPChem().getCountHeavyAtoms());
                }

                rtn.setCountCmpdFragments(nscc.getCountFragments());

                rtn.setNscCmpdType(nscc.getNscCmpdType().getNscCmpdType());
                rtn.setIdentifierString(nscc.getIdentifierString());
                rtn.setDescriptorString(nscc.getDescriptorString());

                rtn.setCmpdAnnotation(translateCmpdAnnotation(nscc.getCmpdAnnotation()));

                rtn.setIdentifierString(nscc.getIdentifierString());
                rtn.setDescriptorString(nscc.getDescriptorString());

                rtn.setFormulaWeight(nscc.getFormulaWeight());
                rtn.setFormulaMolecularFormula(nscc.getFormulaMolecularFormula());

                Collection<CmpdTarget> entityColl = nscc.getCmpdTargets();
                ArrayList<String> strColl = new ArrayList<String>();
                for (CmpdTarget ct : entityColl) {
                    strColl.add(ct.getTarget());
                }
                rtn.setTargets(strColl);

                Collection<CmpdAlias> aliasColl = nscc.getCmpdAliases();
                strColl = new ArrayList<String>();
                for (CmpdAlias ca : aliasColl) {
                    strColl.add(ca.getAlias());
                }
                rtn.setAliases(strColl);

                Collection<CmpdNamedSet> setColl = nscc.getCmpdNamedSets();
                strColl = new ArrayList<String>();
                for (CmpdNamedSet cs : setColl) {
                    strColl.add(cs.getSetName());
                }
                rtn.setNamedSets(strColl);

                Collection<CmpdProject> projColl = nscc.getCmpdProjects();
                strColl = new ArrayList<String>();
                for (CmpdProject cp : projColl) {
                    strColl.add(cp.getProjectName());
                }
                rtn.setProjects(strColl);

                Collection<CmpdPlate> plateColl = nscc.getCmpdPlates();
                strColl = new ArrayList<String>();
                for (CmpdPlate cp : plateColl) {
                    strColl.add(cp.getPlateName());
                }
                rtn.setPlates(strColl);

                Collection<CmpdTarget> targetColl = nscc.getCmpdTargets();
                strColl = new ArrayList<String>();
                for (CmpdTarget ct : targetColl) {
                    strColl.add(ct.getTarget());
                }
                rtn.setTargets(strColl);

                Collection<CmpdFragment> fragColl = nscc.getCmpdFragments();
                ArrayList<CmpdFragmentVO> voFragColl = new ArrayList<CmpdFragmentVO>();
                ArrayList<String> fragList = new ArrayList<String>();
                for (CmpdFragment cf : fragColl) {
                    voFragColl.add(translateCmpdFragment(cf));
                    fragList.add(cf.getCmpdFragmentStructure().getCanSmi());
                }
                rtn.setCmpdFragments(voFragColl);
                rtn.setCmpdFragmentSmilesStrings(fragList);

            }

            // parent fragment
            if (nscc.getCmpdParentFragment() != null) {

                rtn.setParentFragment(translateCmpdFragment(nscc.getCmpdParentFragment()));

                rtn.setParentMolecularWeight(nscc.getCmpdParentFragment().getCmpdFragmentPChem().getMolecularWeight());
                rtn.setParentMolecularFormula(nscc.getCmpdParentFragment().getCmpdFragmentPChem().getMolecularFormula());

                // check for salt fragment
                if (nscc.getCmpdSaltFragment() != null) {

                    rtn.setSaltFragment(translateCmpdFragment(nscc.getCmpdSaltFragment()));

                }

            } else {

                // nsc_cmpd should already have a parent fragment
                // if not, calculation on-the-fly by size
                ArrayList<CmpdFragmentVO> fragList = new ArrayList<CmpdFragmentVO>(rtn.getCmpdFragments());

                // some cmpds will have no fragments
                // or no parentFragment
                if (fragList.size() > 0) {
                    Collections.sort(fragList, new Comparators.CmpdFragmentSizeComparator());
                    Collections.reverse(fragList);
                    rtn.setParentFragment(fragList.get(0));

                    ArrayList<String> smilesStrings = new ArrayList<String>();
                    for (CmpdFragmentVO cf : fragList) {
                        smilesStrings.add(cf.getCmpdFragmentStructure().getCanSmi());
                    }

                    rtn.setCmpdFragmentSmilesStrings(smilesStrings);
                    rtn.setCountCmpdFragments(rtn.getCmpdFragmentSmilesStrings().size());

                    if (DEBUG) {
                        System.out.println("Setting countCmpdFragments to: " + rtn.getCmpdFragmentSmilesStrings().size());
                    }

                }

            }

        } else {

            AdHocCmpd ahc = (AdHocCmpd) entityIn;

            rtn.setId(ahc.getId());
            rtn.setAdHocCmpdId(ahc.getAdHocCmpdId());
            rtn.setOriginalAdHocCmpdId(ahc.getOriginalAdHocCmpdId());
            rtn.setName(ahc.getName());

            // no other cmpd properties
            // no CmpdBioAssay
            Collection<AdHocCmpdFragment> fragColl = ahc.getAdHocCmpdFragments();
            ArrayList<CmpdFragmentVO> voFragColl = new ArrayList<CmpdFragmentVO>();
            for (AdHocCmpdFragment ahcf : fragColl) {
                voFragColl.add(translateAdHocCmpdFragment(ahcf));
            }
            rtn.setCmpdFragments(voFragColl);

            // AdHoc Cmpd needs parent fragment
            ArrayList<CmpdFragmentVO> fragList = new ArrayList<CmpdFragmentVO>(rtn.getCmpdFragments());

            Collections.sort(fragList, new Comparators.CmpdFragmentSizeComparator());
            Collections.reverse(fragList);

            rtn.setParentFragment(fragList.get(0));
        }

        return rtn;
    }

    public static List<CmpdVO> translateListOfCmpds(List<Cmpd> entityListIn) {
        List<CmpdVO> returnList = new ArrayList<CmpdVO>();
        for (Cmpd c : entityListIn) {
            returnList.add(translateCmpd(c));
        }
        return returnList;
    }

    public static CmpdListVO translateCmpdList(CmpdList entityList, Boolean includeListMembers) {

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

        rtn.setId(entityIn.getId());
        rtn.setListMemberComment(entityIn.getListMemberComment());
        rtn.setCmpd(translateCmpd(entityIn.getCmpd()));

        return rtn;

    }
}
