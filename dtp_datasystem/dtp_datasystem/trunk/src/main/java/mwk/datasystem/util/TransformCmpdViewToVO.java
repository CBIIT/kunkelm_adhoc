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
import mwk.datasystem.domain.CmpdView;
import mwk.datasystem.domain.NscCmpd;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mwk.datasystem.vo.CmpdBioAssayVO;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdFragmentStructureVO;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdViewVO;

/**
 *
 * @author mwkunkel
 */
public class TransformCmpdViewToVO {

    public static List<CmpdViewVO> translateListOfCmpdView(List<CmpdView> entityListIn) {

        ArrayList<CmpdViewVO> rtnList = new ArrayList<CmpdViewVO>();

        try {
            for (CmpdView cv : entityListIn) {
                rtnList.add(translateCmpdView(cv));
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return rtnList;

    }

    public static CmpdViewVO translateCmpdView(CmpdView entityIn) {

        CmpdViewVO rtnVO = new CmpdViewVO();
        try {
            if (entityIn != null) {
                rtnVO.setId(entityIn.getId());
                rtnVO.setMw(entityIn.getMw());
                rtnVO.setMf(entityIn.getMf());
                rtnVO.setAlogp(entityIn.getAlogp());
                rtnVO.setLogd(entityIn.getLogd());
                rtnVO.setHba(entityIn.getHba());
                rtnVO.setHbd(entityIn.getHbd());
                rtnVO.setSa(entityIn.getSa());
                rtnVO.setPsa(entityIn.getPsa());
                rtnVO.setSmiles(entityIn.getSmiles());
                rtnVO.setInchi(entityIn.getInchi());
                rtnVO.setMol(entityIn.getMol());
                rtnVO.setInchiAux(entityIn.getInchiAux());
                rtnVO.setName(entityIn.getName());
                rtnVO.setNscCmpdId(entityIn.getNscCmpdId());
                rtnVO.setPrefix(entityIn.getPrefix());
                rtnVO.setNsc(entityIn.getNsc());
                rtnVO.setConf(entityIn.getConf());
                rtnVO.setDistribution(entityIn.getDistribution());
                rtnVO.setCas(entityIn.getCas());
                rtnVO.setNci60(entityIn.getNci60());
                rtnVO.setHf(entityIn.getHf());
                rtnVO.setXeno(entityIn.getXeno());
                rtnVO.setCmpdOwner(entityIn.getCmpdOwner());
                rtnVO.setAdHocCmpdId(entityIn.getAdHocCmpdId());

                rtnVO.setInventory(entityIn.getInventory());

                rtnVO.setTargets(parseFormattedString(entityIn.getFormattedTargetsString()));
                rtnVO.setSets(parseFormattedString(entityIn.getFormattedSetsString()));
                rtnVO.setProjects(parseFormattedString(entityIn.getFormattedProjectsString()));
                rtnVO.setPlates(parseFormattedString(entityIn.getFormattedPlatesString()));
                rtnVO.setAliases(parseFormattedString(entityIn.getFormattedAliasesString()));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtnVO;

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
                returnList.add(toCmpdListVO(nl, includeListMembers));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnList;

    }

    public static CmpdFragmentVO toCmpdFragmentVO(CmpdView entityIn) {

        CmpdFragmentVO rtn = new CmpdFragmentVO();

        try {
            rtn.setId(entityIn.getId());
            rtn.setCmpdFragmentStructure(toCmpdFragmentStructureVO(entityIn));
            rtn.setCmpdFragmentPChem(toCmpdFragmentPChemVO(entityIn));
        } catch (Exception e) {
            e.printStackTrace();
        }


        return rtn;

    }

    public static CmpdFragmentPChemVO toCmpdFragmentPChemVO(CmpdView entityIn) {

        CmpdFragmentPChemVO rtn = new CmpdFragmentPChemVO();

        try {
            rtn.setMw(entityIn.getMw());
            rtn.setMf(entityIn.getMf());
            rtn.setAlogp(entityIn.getAlogp());
            rtn.setLogd(entityIn.getLogd());
            rtn.setHba(entityIn.getHba());
            rtn.setHbd(entityIn.getHbd());
            rtn.setSa(entityIn.getSa());
            rtn.setPsa(entityIn.getPsa());
            rtn.setId(entityIn.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rtn;

    }

    public static CmpdFragmentStructureVO toCmpdFragmentStructureVO(CmpdView entityIn) {

        CmpdFragmentStructureVO rtn = new CmpdFragmentStructureVO();

        try {
            rtn.setSmiles(entityIn.getSmiles());
            rtn.setInchi(entityIn.getInchi());
            rtn.setMol(entityIn.getSmiles());
            rtn.setInchiAux(entityIn.getInchiAux());
            // no cTab in CmpdView
            //rtn.setCtab(entityIn.getCtab());
            rtn.setId(entityIn.getId());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    public static CmpdBioAssayVO toCmpdBioAssayVO(CmpdView entityIn) {

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

    public static CmpdVO toCmpdVO(CmpdView entityIn) {

        CmpdVO cmpdVO = new CmpdVO();

        try {

            try {
                cmpdVO.setId(entityIn.getId());
                cmpdVO.setNscCmpdId(entityIn.getNscCmpdId());
                cmpdVO.setAdHocCmpdId(entityIn.getAdHocCmpdId());
                cmpdVO.setName(entityIn.getName());
                cmpdVO.setPrefix(entityIn.getPrefix());
                cmpdVO.setNsc(entityIn.getNsc());
                cmpdVO.setDistribution(entityIn.getDistribution());
                cmpdVO.setConf(entityIn.getConf());
                cmpdVO.setCas(entityIn.getCas());

                cmpdVO.setCmpdBioAssay(toCmpdBioAssayVO(entityIn));

                cmpdVO.setParentFragment(toCmpdFragmentVO(entityIn));

                cmpdVO.setInventory(entityIn.getInventory());

                // need fragments

                cmpdVO.setTargets(parseFormattedString(entityIn.getFormattedTargetsString()));
                cmpdVO.setSets(parseFormattedString(entityIn.getFormattedSetsString()));
                cmpdVO.getTargets().addAll(parseFormattedString(entityIn.getFormattedProjectsString()));
                cmpdVO.setPlates(parseFormattedString(entityIn.getFormattedPlatesString()));
                cmpdVO.setAliases(parseFormattedString(entityIn.getFormattedAliasesString()));

            } catch (Exception e) {
                e.printStackTrace();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return cmpdVO;
    }

    public static List<CmpdVO> translateListOfCmpdViews(List<CmpdView> entityListIn) {
        List<CmpdVO> returnList = new ArrayList<CmpdVO>();
        for (CmpdView cv : entityListIn) {
            returnList.add(toCmpdVO(cv));
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

        try {
            rtn.setId(entityIn.getId());
            //rtn.setCmpd(toCmpdVO(entityIn.getCmpdView()));
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return rtn;

    }
}
