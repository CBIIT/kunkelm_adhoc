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
import mwk.datasystem.domain.NscCmpdImpl;
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
public class TransformAndroToVO {

    // not needed for now, translations to CmpdVO are made in separate transformer
//  public static List<CmpdViewVO> translateListOfCmpdView(List<CmpdView> entityListIn) {
//
//    ArrayList<CmpdViewVO> rtnList = new ArrayList<CmpdViewVO>();
//    
//    for (CmpdView cv : entityListIn){
//      rtnList.add(translateCmpdView(cv));
//    }
//
//    return rtnList;
//
//  }
//
//  public static CmpdViewVO translateCmpdView(CmpdView entityIn) {
//
//    CmpdViewVO rtnVO = new CmpdViewVO();
//
//    if (entityIn != null) {
//      rtnVO.setMw(entityIn.getMw());
//      rtnVO.setMf(entityIn.getMf());
//      rtnVO.setAlogp(entityIn.getAlogp());
//      rtnVO.setLogd(entityIn.getLogd());
//      rtnVO.setHba(entityIn.getHba());
//      rtnVO.setHbd(entityIn.getHbd());
//      rtnVO.setSa(entityIn.getSa());
//      rtnVO.setPsa(entityIn.getPsa());
//      rtnVO.setSmiles(entityIn.getSmiles());
//      rtnVO.setInchi(entityIn.getInchi());
//      rtnVO.setMol(entityIn.getMol());
//      rtnVO.setInchiAux(entityIn.getInchiAux());
//      rtnVO.setName(entityIn.getName());
//      rtnVO.setCmpdId(entityIn.getCmpdId());
//      rtnVO.setPrefix(entityIn.getPrefix());
//      rtnVO.setNsc(entityIn.getNsc());
//      rtnVO.setConf(entityIn.getConf());
//      rtnVO.setDistribution(entityIn.getDistribution());
//      rtnVO.setCas(entityIn.getCas());
//      rtnVO.setNci60(entityIn.getNci60());
//      rtnVO.setHf(entityIn.getHf());
//      rtnVO.setXeno(entityIn.getXeno());
//      rtnVO.setCmpdOwner(entityIn.getCmpdOwner());
//      rtnVO.setAdHocCmpdId(entityIn.getAdHocCmpdId());
//      rtnVO.setTargets(parseFormattedString(entityIn.getFormattedTargetsString()));
//      rtnVO.setSets(parseFormattedString(entityIn.getFormattedSetsString()));
//      rtnVO.setProjects(parseFormattedString(entityIn.getFormattedProjectsString()));
//      rtnVO.setPlates(parseFormattedString(entityIn.getFormattedPlatesString()));
//      rtnVO.setAliases(parseFormattedString(entityIn.getFormattedAliasesString()));
//    }
//
//    return rtnVO;
//
//  }
//  public static ArrayList<String> parseFormattedString(String fmtdString) {
//
//    ArrayList<String> rtnList = new ArrayList<String>();
//
//    if (fmtdString != null && fmtdString.length() > 0) {
//
//      String[] splitStrings = null;
//      String fixedString = null;
//      int i;
//
//      String delimiters = "xxx";
//
//      splitStrings = fmtdString.split(delimiters);
//      for (i = 0; i < splitStrings.length; i++) {
//        rtnList.add(splitStrings[i]);
//      }
//
//    }
//
//    return rtnList;
//
//  }
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

        rtn.setMw(entityIn.getMw());
        rtn.setMf(entityIn.getMf());
        rtn.setAlogp(entityIn.getAlogp());
        rtn.setLogd(entityIn.getLogd());
        rtn.setHba(entityIn.getHba());
        rtn.setHbd(entityIn.getHbd());
        rtn.setSa(entityIn.getSa());
        rtn.setPsa(entityIn.getPsa());
        rtn.setId(entityIn.getId());

        return rtn;

    }

    public static CmpdFragmentPChemVO toCmpdFragmentPChemVO(CmpdFragmentPChem entityIn) {

        CmpdFragmentPChemVO rtn = new CmpdFragmentPChemVO();

        rtn.setMw(entityIn.getMw());
        rtn.setMf(entityIn.getMf());
        rtn.setAlogp(entityIn.getAlogp());
        rtn.setLogd(entityIn.getLogd());
        rtn.setHba(entityIn.getHba());
        rtn.setHbd(entityIn.getHbd());
        rtn.setSa(entityIn.getSa());
        rtn.setPsa(entityIn.getPsa());
        rtn.setId(entityIn.getId());

        return rtn;

    }

    public static CmpdFragmentStructureVO adHocToCmpdFragmentStructureVO(AdHocCmpdFragmentStructure entityIn) {

        CmpdFragmentStructureVO rtn = new CmpdFragmentStructureVO();

        rtn.setSmiles(entityIn.getSmiles());

        rtn.setInchi(entityIn.getInchi());
        rtn.setMol(entityIn.getMol());
        rtn.setInchiAux(entityIn.getInchiAux());
        rtn.setCtab(entityIn.getCtab());
        rtn.setId(entityIn.getId());

        return rtn;

    }

    public static CmpdFragmentStructureVO toCmpdFragmentStructureVO(CmpdFragmentStructure entityIn) {

        CmpdFragmentStructureVO rtn = new CmpdFragmentStructureVO();

        rtn.setSmiles(entityIn.getSmiles());
        
        rtn.setInchi(entityIn.getInchi());
        rtn.setMol(entityIn.getSmiles());
        rtn.setInchiAux(entityIn.getInchiAux());
        rtn.setCtab(entityIn.getCtab());
        rtn.setId(entityIn.getId());

        return rtn;

    }

    public static CmpdBioAssayVO toCmpdBioAssayVO(CmpdBioAssay entityIn) {

        CmpdBioAssayVO rtn = new CmpdBioAssayVO();

        rtn.setId(entityIn.getId());
        rtn.setHf(entityIn.getHf());
        rtn.setNci60(entityIn.getNci60());
        rtn.setXeno(entityIn.getXeno());

        return rtn;
    }

    public static CmpdVO toCmpdVO(Cmpd incomingCmpd) {

        CmpdVO cmpdVO = new CmpdVO();

        Cmpd entityIn = Unproxy.initializeAndUnproxy(incomingCmpd);

        System.out.println("incomingCmpd in toCmpdVO is class: " + entityIn.getClass().getName());

        if (entityIn instanceof NscCmpdImpl) {

            NscCmpd nscc = (NscCmpd) entityIn;

            cmpdVO.setId(nscc.getId());
            cmpdVO.setName(nscc.getName());
            cmpdVO.setPrefix(nscc.getPrefix());
            cmpdVO.setNsc(nscc.getNsc());
            cmpdVO.setDistribution(nscc.getDistribution());
            cmpdVO.setConf(nscc.getConf());
            cmpdVO.setCas(nscc.getCas());

            cmpdVO.setCmpdBioAssay(toCmpdBioAssayVO(nscc.getCmpdBioAssay()));

            Collection<CmpdFragment> fragColl = nscc.getCmpdFragments();
            ArrayList<CmpdFragmentVO> voFragColl = new ArrayList<CmpdFragmentVO>();
            for (CmpdFragment cf : fragColl) {
                voFragColl.add(toCmpdFragmentVO(cf));
            }
            cmpdVO.setCmpdFragments(voFragColl);
            
            cmpdVO.setInventory(nscc.getCmpdInventory().getInventory());

            // is there a parent fragment

            if (nscc.getCmpdParentFragment() != null) {
                cmpdVO.setParentFragment(toCmpdFragmentVO(nscc.getCmpdParentFragment()));
            } else {

                // nsc_cmpd should already have a parent fragment
                // if not, calculation on-the-fly by size

                ArrayList<CmpdFragmentVO> fragList = new ArrayList<CmpdFragmentVO>(cmpdVO.getCmpdFragments());

                // some cmpds will have no fragments

                if (fragList.size() > 0) {
                    Collections.sort(fragList, new Comparators.CmpdFragmentSizeComparator());
                    Collections.reverse(fragList);
                    cmpdVO.setParentFragment(fragList.get(0));
                }

            }

            Collection<CmpdAlias> aliasColl = nscc.getCmpdAliases();
            ArrayList<String> strColl = new ArrayList<String>();
            for (CmpdAlias ca : aliasColl) {
                strColl.add(ca.getAlias());
            }
            cmpdVO.setAliases(strColl);

            Collection<CmpdTarget> targetColl = nscc.getCmpdTargets();
            strColl = new ArrayList<String>();
            for (CmpdTarget ct : targetColl) {
                strColl.add(ct.getTarget());
            }
            cmpdVO.setTargets(strColl);

        } else {

            AdHocCmpd ahc = (AdHocCmpd) entityIn;

            cmpdVO.setId(ahc.getId());
            cmpdVO.setAdHocCmpdId(ahc.getAdHocCmpdId());
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

        return rtn;
    }

    public static CmpdListMemberVO toCmpdListMemberVO(CmpdListMember entityIn) {

        CmpdListMemberVO rtn = new CmpdListMemberVO();

        rtn.setId(entityIn.getId());
        rtn.setCmpd(toCmpdVO(entityIn.getCmpd()));

        return rtn;

    }
}
