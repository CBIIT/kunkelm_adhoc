/*
 
 
 
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HelperCmpdTable;
import mwk.datasystem.util.HelperStructure;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import newstructureservlet.MoleculeWrangling;

@ManagedBean
@SessionScoped
public class StructureSearchController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    // reach-through to sessionController
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    // reach-through to listManagerController
    @ManagedProperty(value = "#{listManagerController}")
    private ListManagerController listManagerController;

    public void setListManagerController(ListManagerController listManagerController) {
        this.listManagerController = listManagerController;
    }

    private String message;
    private String errorMessage;

    private String nscForLoad;
    private String ctabForLoad;
    private String smilesForLoad;

    private String ctabFromEditor;
    private String smilesFromCtabFromEditor;
    
    private CmpdListVO listToSearch;

    public StructureSearchController() {
    }

    public String performSubstructureSearch() {

        System.out.println("Now in StructureSearchController.findCmpdsBySubstructure()");

        // aromaticity set to true so that 
        // SMARTSPattern.match() will work during structure display
        this.smilesFromCtabFromEditor = MoleculeWrangling.toSmilesFromCtab(ctabFromEditor, true);

        List<Integer> nscIntList = HelperStructure.findNSCsBySmilesSubstructure(this.smilesFromCtabFromEditor);

        System.out.println("Size of nscIntList: " + nscIntList.size());

        List<CmpdVO> cmpdVOlist = HelperCmpdTable.fetchCmpdsByNscs(nscIntList);

        System.out.println("Size of cmpdVOlist: " + cmpdVOlist.size());

        Date now = new Date();

        CmpdListVO clVO = ApplicationScopeBean.cmpdListFromListOfCmpds(cmpdVOlist, "Substructure Search Results " + now.toString(), sessionController.getLoggedUser());

        clVO.setAnchorSmiles(smilesFromCtabFromEditor);
        
        listManagerController.getListManagerBean().availableLists.add(clVO);
        listManagerController.getListManagerBean().activeList = clVO;

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable.xhtml?faces-redirect=true";

    }

//    public String performExactMatchSearch() {
//
//        List<Integer> nscIntList = HelperStructure.findNSCsByExactMatch(this.smilesFromCtabFromEditor);
//
//        List<CmpdVO> structureSearchResults = HelperCmpd.getCmpdsByNsc(nscIntList, sessionController.getLoggedUser());
//
//        List<CmpdListMemberVO> clmList = new ArrayList<CmpdListMemberVO>();
//
//        for (CmpdVO cVO : structureSearchResults) {
//            CmpdListMemberVO clmVO = new CmpdListMemberVO();
//            clmVO.setCmpd(cVO);
//            clmList.add(clmVO);
//        }
//
//        CmpdListVO clVO = new CmpdListVO();
//
//        clVO.setListName("Exact Match Search Results");
//        clVO.setListOwner(sessionController.getLoggedUser());
//
//        clVO.setCmpdListMembers(clmList);
//
//        listManagerController.getListManagerBean().tempList = clVO;
//
//        return null;
//
//    }
//
//    public String performSubstructureSearchCtab() {
//
//        System.out.println("Now in performSubstructureSearchCtab()");
//
//        // aromaticity set to true so that 
//        // SMARTSPattern.match() will work during structure display
//        this.smilesFromCtabFromEditor = MoleculeWrangling.toSmilesFromCtab(ctabFromEditor, true);
//
//        List<Integer> nscIntList = HelperStructure.findNSCsByCtabSubstructure(ctabFromEditor);
//
//        System.out.println("Size of nscIntList: " + nscIntList.size());
//
//        Date now = new Date();
//
//        if (listName == null || listName.length() == 0) {
//            listName = "structureSearchResults " + now.toString();
//        }
//
//        Long cmpdListId = HelperCmpd.createCmpdListByNscs(listName, nscIntList, this.smilesFromCtabFromEditor, sessionController.getLoggedUser());
//
//        // have to fetch the list so that the compound details will be populated
//        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, sessionController.getLoggedUser());
//
//        listManagerController.getListManagerBean().availableLists.add(clVO);
//        listManagerController.getListManagerBean().activeList = clVO;
//
//        sessionController.configurationBean.performUpdateColumns();
//
//        return "/webpages/activeListTable.xhtml?faces-redirect=true";
//
//    }
//
//    public String performSubstructureSearch() {
//
//        System.out.println("Now in performSubstructureSearch()");
//
//        // aromaticity set to true so that 
//        // SMARTSPattern.match() will work during structure display
//        this.smilesFromCtabFromEditor = MoleculeWrangling.toSmilesFromCtab(ctabFromEditor, true);
//
//        List<Integer> nscIntList = HelperStructure.findNSCsBySmilesSubstructure(this.smilesFromCtabFromEditor);
//
//        System.out.println("Size of nscIntList: " + nscIntList.size());
//
//        Date now = new Date();
//
//        if (listName == null || listName.length() == 0) {
//            listName = "structureSearchResults " + now.toString();
//        }
//
//        Long cmpdListId = HelperCmpd.createCmpdListByNscs(listName, nscIntList, this.smilesFromCtabFromEditor, sessionController.getLoggedUser());
//
//        // have to fetch the list so that the compound details will be populated
//        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, sessionController.getLoggedUser());
//
//        listManagerController.getListManagerBean().availableLists.add(clVO);
//        listManagerController.getListManagerBean().activeList = clVO;
//
//        sessionController.configurationBean.performUpdateColumns();
//
//        return "/webpages/activeListTable.xhtml?faces-redirect=true";
//
//    }
    public String performLandingLoadEditor() {

        System.out.println("In performLandingLoadEditor in StructureSearchController.");

        if (nscForLoad != null && nscForLoad.length() > 0) {

            performLoadEditorByNsc();

        } else if (smilesForLoad != null && smilesForLoad.length() > 0) {

            performLoadEditorBySmiles();

        } else {

            message = "landingNscForLoad and landingSmilesForLoad are not specified.";
            errorMessage = "landingNscForLoad and landingSmilesForLoad are not specified.";

        }

        return null;

    }

    public String performLoadEditorBySmiles() {

        System.out.println("In performLoadEditorBySmiles in StructureSearchController");

        ctabForLoad = "";

        String ctab = null;

        if (smilesForLoad != null) {
            ctab = MoleculeWrangling.toCtabFromSmiles(smilesForLoad);
        } else {
            message = "smilesForLoad is null";
            errorMessage = "smilesForLoad is null";
        }

        if (ctab != null) {
            ctabForLoad = ctab;
            message = "PerformLoadEditorBySmiles success.";
            errorMessage = "";
        } else {
            message = "Unable to create ctab from smilesForLoad: " + smilesForLoad;
            errorMessage = "Unable to create ctab from smilesForLoad: " + smilesForLoad;
        }

        return null;

    }

    public String performLoadEditorByNsc() {

        System.out.println("In performLoadEditorByNsc in StructureSearchController");
        System.out.println("In performLoadEditorByNsc in nscForLoad is: " + nscForLoad);

        ctabForLoad = "";
        smilesForLoad = "";

        // MWK 16Feb2015 calls to local methods which delegate to MolInput
        Integer nscInt = null;

        try {
            nscInt = Integer.parseInt(nscForLoad);
        } catch (NumberFormatException e) {
        }

        if (nscInt != null) {

            CmpdVO cVO = HelperCmpd.getSingleCmpdByNsc(nscInt, sessionController.getLoggedUser());

            String ctab = null;
            String smiles = null;

            try {
                ctab = cVO.getParentFragment().getCmpdFragmentStructure().getCtab();
            } catch (Exception e) {

            }

            try {
                smiles = cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi();
            } catch (Exception e) {

            }

            if (ctab != null) {

                ctabForLoad = ctab;

                if (smiles != null) {
                    smilesForLoad = smiles;
                } else {
                    smilesForLoad = MoleculeWrangling.toSmilesFromCtab(ctabForLoad, true);
                }

            } else if (smiles != null) {

                smilesForLoad = smiles;
                ctabForLoad = MoleculeWrangling.toCtabFromSmiles(smiles);

            } else {

                message = "No ctab or smiles for : " + nscForLoad;
                errorMessage = "No ctab or smiles for : " + nscForLoad;

            }
        } else {

            message = "Not a valid NSC: " + nscForLoad;
            errorMessage = "Not a valid NSC: " + nscForLoad;

        }

        return null;
    }

    public String landingPerformLoadEditorByNsc() {

        System.out.println("In landingPerformLoadEditorByNsc in StructureSearchController");
        System.out.println("In landingPerformLoadEditorByNsc in nscForLoad is: " + nscForLoad);

        ctabForLoad = "";
        smilesForLoad = "";

        // MWK 16Feb2015 calls to local methods which delegate to MolInput
        Integer nscInt = null;

        try {
            nscInt = Integer.parseInt(nscForLoad);
        } catch (NumberFormatException e) {
        }

        if (nscInt != null) {

            CmpdVO cVO = HelperCmpd.getSingleCmpdByNsc(nscInt, sessionController.getLoggedUser());

            String ctab = null;
            String smiles = null;

            try {
                ctab = cVO.getParentFragment().getCmpdFragmentStructure().getCtab();
            } catch (Exception e) {

            }

            try {
                smiles = cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi();
            } catch (Exception e) {

            }

            if (ctab != null) {

                ctabForLoad = ctab;

                if (smiles != null) {
                    smilesForLoad = smiles;
                } else {
                    smilesForLoad = MoleculeWrangling.toSmilesFromCtab(ctabForLoad, true);
                }

            } else if (smiles != null) {

                smilesForLoad = smiles;
                ctabForLoad = MoleculeWrangling.toCtabFromSmiles(smiles);

            } else {

                message = "No ctab or smiles for : " + nscForLoad;
                errorMessage = "No ctab or smiles for : " + nscForLoad;

            }
        } else {

            message = "Not a valid NSC: " + nscForLoad;
            errorMessage = "Not a valid NSC: " + nscForLoad;

        }

        return "/webpages/chemDoodle.xhtml?faces-redirect=true";
    }

    // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getNscForLoad() {
        return nscForLoad;
    }

    public void setNscForLoad(String nscForLoad) {
        this.nscForLoad = nscForLoad;
    }

    public String getCtabForLoad() {
        return ctabForLoad;
    }

    public void setCtabForLoad(String ctabForLoad) {
        this.ctabForLoad = ctabForLoad;
    }

    public String getSmilesForLoad() {
        return smilesForLoad;
    }

    public void setSmilesForLoad(String smilesForLoad) {
        this.smilesForLoad = smilesForLoad;
    }

    public String getCtabFromEditor() {
        return ctabFromEditor;
    }

    public void setCtabFromEditor(String ctabFromEditor) {
        this.ctabFromEditor = ctabFromEditor;
    }

    public String getSmilesFromCtabFromEditor() {
        return smilesFromCtabFromEditor;
    }

    public void setSmilesFromCtabFromEditor(String smilesFromCtabFromEditor) {
        this.smilesFromCtabFromEditor = smilesFromCtabFromEditor;
    }

    public CmpdListVO getListToSearch() {
        return listToSearch;
    }

    public void setListToSearch(CmpdListVO listToSearch) {
        this.listToSearch = listToSearch;
    }

// </editor-fold>  
}
