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
import mwk.datasystem.domain.DataSystemRole;
import mwk.datasystem.domain.DataSystemUser;
import mwk.datasystem.domain.NscCmpdImpl;
import mwk.datasystem.vo.CmpdBioAssayVO;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdFragmentStructureVO;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.DataSystemRoleVO;
import mwk.datasystem.vo.DataSystemUserVO;

/**
 *
 * @author mwkunkel
 */
public class TransformAndroToVO {

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
        rtn.setSarcoma(entityIn.getSarcoma());

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

<<<<<<< .mine


=======
        
        
>>>>>>> .r93
        CmpdListVO rtn = new CmpdListVO();

<<<<<<< .mine
        try {
=======
        try{
        
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
>>>>>>> .r93

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
        
        } catch (Exception e){
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
