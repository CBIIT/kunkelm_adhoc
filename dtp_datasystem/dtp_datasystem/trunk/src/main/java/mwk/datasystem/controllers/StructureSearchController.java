/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    private String nscForLoad;
    private String ctabForLoad;
    private String smilesForLoad;

    private String ctabFromEditor;
    private String smilesFromCtabFromEditor;

    private String listName;

    private CmpdListVO listToSearch;

    public StructureSearchController() {
    }

    public String performExactMatchSearch() {

        List<Integer> nscIntList = HelperStructure.findNSCsByExactMatch(smilesFromCtabFromEditor);

        List<CmpdVO> structureSearchResults = HelperCmpd.getCmpdsByNsc(nscIntList, sessionController.getLoggedUser());

        List<CmpdListMemberVO> clmList = new ArrayList<CmpdListMemberVO>();

        for (CmpdVO cVO : structureSearchResults) {
            CmpdListMemberVO clmVO = new CmpdListMemberVO();
            clmVO.setCmpd(cVO);
            clmList.add(clmVO);
        }

        CmpdListVO clVO = new CmpdListVO();

        clVO.setListName("Exact Match Search Results");
        clVO.setListOwner(sessionController.getLoggedUser());

        clVO.setCmpdListMembers(clmList);

        listManagerController.getListManagerBean().tempList = clVO;

        return null;

    }

    public String performSubstructureSearchCtab() {

        System.out.println("Now in performSubstructureSearchCtab()");

        // aromaticity set to true so that 
        // SMARTSPattern.match() will work during structure display
        smilesFromCtabFromEditor = MoleculeWrangling.toSmilesFromCtab(ctabFromEditor, true);

        List<Integer> nscIntList = HelperStructure.findNSCsByCtabSubstructure(ctabFromEditor);

        System.out.println("Size of nscIntList: " + nscIntList.size());

        Date now = new Date();

        if (listName == null || listName.length() == 0) {
            listName = "structureSearchResults " + now.toString();
        }

        Long cmpdListId = HelperCmpd.createCmpdListByNscs(listName, nscIntList, smilesFromCtabFromEditor, sessionController.getLoggedUser());

        // have to fetch the list so that the compound details will be populated
        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, sessionController.getLoggedUser());

        listManagerController.getListManagerBean().availableLists.add(clVO);
        listManagerController.getListManagerBean().activeList = clVO;

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable.xhtml?faces-redirect=true";

    }

    public String performSubstructureSearch() {

        System.out.println("Now in performSubstructureSearch()");

        List<Integer> nscIntList = HelperStructure.findNSCsBySmilesSubstructure(smilesFromCtabFromEditor);

        System.out.println("Size of nscIntList: " + nscIntList.size());

        Date now = new Date();

        if (listName == null || listName.length() == 0) {
            listName = "structureSearchResults " + now.toString();
        }

        Long cmpdListId = HelperCmpd.createCmpdListByNscs(listName, nscIntList, smilesFromCtabFromEditor, sessionController.getLoggedUser());

        // have to fetch the list so that the compound details will be populated
        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, sessionController.getLoggedUser());

        listManagerController.getListManagerBean().availableLists.add(clVO);
        listManagerController.getListManagerBean().activeList = clVO;

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable.xhtml?faces-redirect=true";

    }

    public String performSmartsSearch() {

        String smartsString = smilesFromCtabFromEditor;

        // double bonds to any bond
        smartsString = smartsString.replaceAll("=", "~");

        // disguise chlorine as HIDDEN (no "C" or "c"), replace C and c then restore chlorine
        smartsString = smartsString.replaceAll("Cl", "HIDDEN").replaceAll("C", "[#6]").replaceAll("c", "[#6]").replaceAll("HIDDEN", "Cl");

        // same exercise for nitrogen, oxygen, sulfur, phosphorus, but no need to disguise other elements
        smartsString = smartsString.replaceAll("N", "[#7]").replaceAll("n", "[#7]");
        smartsString = smartsString.replaceAll("O", "[#8]").replaceAll("o", "[#8]");

        smartsString = smartsString.replaceAll("P", "[#15]").replaceAll("p", "[#15]");
        smartsString = smartsString.replaceAll("S", "[#16]").replaceAll("s", "[#16]");

        FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "SMILES string and SMARTS string: ",
                smilesFromCtabFromEditor + " " + smartsString);

        FacesContext.getCurrentInstance().addMessage(null, msg);

        System.out.println("smilesString: " + smilesFromCtabFromEditor);
        System.out.println("smartsString: " + smartsString);

        List<Integer> nscIntList = HelperStructure.findNSCsBySmartsSubstructure(smartsString);

        List<CmpdVO> cmpds = HelperCmpd.getCmpdsByNsc(nscIntList, sessionController.getLoggedUser());

        List<CmpdListMemberVO> clmList = new ArrayList<CmpdListMemberVO>();

        for (CmpdVO cVO : cmpds) {
            CmpdListMemberVO clmVO = new CmpdListMemberVO();
            clmVO.setCmpd(cVO);
            clmList.add(clmVO);
        }

        CmpdListVO clVO = new CmpdListVO();

        clVO.setListName("SMARTS Search Results");
        clVO.setListOwner(sessionController.getLoggedUser());

        clVO.setCmpdListMembers(clmList);

        listManagerController.getListManagerBean().tempList = clVO;

        return null;

    }

    public String performLoadEditorByNsc() {

        System.out.println("In performLoadEditorByNsc in StructureSearchController");

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

                ctabForLoad = "No ctab or smiles for : " + nscForLoad;
                smilesForLoad = "No ctab or smiles for : " + nscForLoad;

            }
        } else {

            ctabForLoad = "Not a valid NSC: " + nscForLoad;
            smilesForLoad = "NOo a valid NSC: " + nscForLoad;

        }

        return null;
    }

    // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
    public String getNscForLoad() {
        return nscForLoad;
    }

    public void setNscForLoad(String nscForLoad) {
        this.nscForLoad = nscForLoad;
    }

    public String getSmilesForLoad() {
        return smilesForLoad;
    }

    public void setSmilesForLoad(String smilesForLoad) {
        this.smilesForLoad = smilesForLoad;
    }

    public String getCtabForLoad() {
        return ctabForLoad;
    }

    public void setCtabForLoad(String ctabForLoad) {
        this.ctabForLoad = ctabForLoad;
    }

    public String getSmilesFromCtabFromEditor() {
        return smilesFromCtabFromEditor;
    }

    public void setSmilesFromCtabFromEditor(String smilesFromCtabFromEditor) {
        this.smilesFromCtabFromEditor = smilesFromCtabFromEditor;
    }

    public String getCtabFromEditor() {
        return ctabFromEditor;
    }

    public void setCtabFromEditor(String ctabFromEditor) {
        this.ctabFromEditor = ctabFromEditor;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public CmpdListVO getListToSearch() {
        return listToSearch;
    }

    public void setListToSearch(CmpdListVO listToSearch) {
        this.listToSearch = listToSearch;
    }

    // </editor-fold>  
}
