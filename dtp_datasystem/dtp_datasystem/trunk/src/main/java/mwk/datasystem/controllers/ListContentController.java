/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HelperCmpdListMember;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.apache.commons.io.IOUtils;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class ListContentController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    static final Boolean DEBUG = Boolean.TRUE;

// reach-through to searchCriteriaBean
    @ManagedProperty(value = "#{searchCriteriaBean}")
    private SearchCriteriaBean searchCriteriaBean;

    public void setSearchCriteriaBean(SearchCriteriaBean searchCriteriaBean) {
        this.searchCriteriaBean = searchCriteriaBean;
    }

    // reach-through to applicationScopeBean
    @ManagedProperty(value = "#{applicationScopeBean}")
    private ApplicationScopeBean applicationScopeBean;

    public void setApplicationScopeBean(ApplicationScopeBean applicationScopeBean) {
        this.applicationScopeBean = applicationScopeBean;
    }

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

    // reach-through to tanimotoController
    @ManagedProperty(value = "#{tanimotoController}")
    private TanimotoController tanimotoController;

    public void setTanimotoController(TanimotoController tanimotoController) {
        this.tanimotoController = tanimotoController;
    }

    public ListContentController() {
        if (this.searchCriteriaBean == null) {
            System.out.println("searchCriteriaBean is null in ListContentController.constructor");
            this.searchCriteriaBean = new SearchCriteriaBean();
        }
    }

    private String listName;
    UploadedFile uploadedFile;
    CmpdListVO targetList;

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("List Member Edited", ((CmpdListMemberVO) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        // HelperCmpdList.updateCmpdList((CmpdListVO) event.getObject(), sessionController.getLoggedUser());
    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("List Member Edit Cancelled", ((CmpdListMemberVO) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }

    public void onCellEdit(CellEditEvent event) {

        String colHeader = event.getColumn().getFacet("header").toString();

        Object oldValue = event.getOldValue();
        Object newValue = event.getNewValue();

        if (newValue != null && !newValue.equals(oldValue)) {
            FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, colHeader + "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
            FacesContext.getCurrentInstance().addMessage(null, msg);
        }

    }

    public String performConfigureDelete() {
        // placeholder action to populate selectedListMembers
        return "/webpages/configureDeleteFromList.xhtml?faces-redirect=true";
    }

    /**
     *
     * @return For checkboxes outside of dataTable
     */
    public String performDeleteFromActiveList() {

//        HelperCmpdListMember.deleteCmpdListMembers(listManagerController.getListManagerBean().activeList, listManagerController.getListManagerBean().getSelectedActiveListMembers(), sessionController.getLoggedUser());
//        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(listManagerController.getListManagerBean().activeList.getCmpdListId(), Boolean.TRUE, sessionController.getLoggedUser());
//        
        CmpdListVO actLis = listManagerController.getListManagerBean().activeList;

        listManagerController.getListManagerBean().activeList.getCmpdListMembers().removeAll(listManagerController.getListManagerBean().selectedActiveListMembers);
        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";
    }

    public String performCopyAll() {

        CmpdListVO rtn = new CmpdListVO();

        ArrayList<CmpdVO> listOfCmpds = new ArrayList<CmpdVO>();

        for (CmpdListMemberVO clmVO : listManagerController.getListManagerBean().activeList.getCmpdListMembers()) {
            listOfCmpds.add(clmVO.getCmpd());
        }

        rtn = ApplicationScopeBean.cmpdListFromListOfCmpds(listOfCmpds, "Copy of " + listManagerController.getListManagerBean().activeList.getListName(), sessionController.getLoggedUser());

        listManagerController.getListManagerBean().availableLists.add(rtn);
        listManagerController.getListManagerBean().activeList = rtn;

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performCopySelected() {

        CmpdListVO rtn = new CmpdListVO();

        ArrayList<CmpdVO> listOfCmpds = new ArrayList<CmpdVO>();

        for (CmpdListMemberVO clmVO : listManagerController.getListManagerBean().selectedActiveListMembers) {
            listOfCmpds.add(clmVO.getCmpd());
        }

        rtn = ApplicationScopeBean.cmpdListFromListOfCmpds(listOfCmpds, "Copy of " + listManagerController.getListManagerBean().activeList.getListName(), sessionController.getLoggedUser());

        listManagerController.getListManagerBean().availableLists.add(rtn);
        listManagerController.getListManagerBean().activeList = rtn;

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performConfigureAppend() {
        // placeholder action to populate selectedListMembers
        return "/webpages/configureAppendToExistingList.xhtml?faces-redirect=true";
    }

    public String performAppendSelectedToExistingList() {

        HelperCmpdListMember.appendCmpdListMembers(targetList, listManagerController.getListManagerBean().selectedActiveListMembers, sessionController.getLoggedUser());

        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(targetList.getCmpdListId(), Boolean.TRUE, sessionController.getLoggedUser());

        listManagerController.getListManagerBean().activeList = clVO;
        listManagerController.performUpdateAvailableLists();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performSmilesFileUpload() {

        try {

            Random randomGenerator = new Random();

            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            File tmpDir = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            System.out.println("javax.servlet.context.tempdir: " + tmpDir.getAbsolutePath());

            String systemFilePath = tmpDir + "/" + uploadedFile.getFileName();

            File systemFile = new File(systemFilePath);
            FileOutputStream fos = new FileOutputStream(systemFile, false);

            byte[] byteArray = IOUtils.toByteArray(this.uploadedFile.getInputstream());

            fos.write(byteArray);

            fos.flush();
            fos.close();

            MoleculeParser mp = new MoleculeParser();

            File smilesFile = new File(systemFilePath);

            ArrayList<AdHocCmpd> adHocCmpdList = mp.parseSMILESFile(smilesFile);

            CmpdListVO clVO_sparse = HelperCmpdList.deNovoCmpdListFromAdHocCmpds(adHocCmpdList, this.listName, this.sessionController.getLoggedUser());

            // new fetch the list
            CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(clVO_sparse.getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());

            listManagerController.getListManagerBean().availableLists.add(clVO);
            listManagerController.getListManagerBean().activeList = clVO;

            System.out.println("UploadCmpds contains: " + clVO_sparse.getCountListMembers() + " cmpds");

        } catch (Exception e) {
            e.printStackTrace();
        }

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performFileUpload() {

        try {

            Random randomGenerator = new Random();

            ServletContext servletContext = (ServletContext) FacesContext.getCurrentInstance().getExternalContext().getContext();

            File tmpDir = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
            System.out.println("javax.servlet.context.tempdir: " + tmpDir.getAbsolutePath());

            String systemFilePath = tmpDir + "/" + uploadedFile.getFileName();

            File systemFile = new File(systemFilePath);
            FileOutputStream fos = new FileOutputStream(systemFile, false);

            byte[] byteArray = IOUtils.toByteArray(this.uploadedFile.getInputstream());

            fos.write(byteArray);

            fos.flush();
            fos.close();

            System.out.println(" fileName: " + this.uploadedFile.getFileName() + " fileSize: " + this.uploadedFile.getSize() + " systemFilePath: " + systemFilePath);

            MoleculeParser mp = new MoleculeParser();

            File sdFile = new File(systemFilePath);

            ArrayList<AdHocCmpd> adHocCmpdList = mp.parseSDF(sdFile);

            CmpdListVO clVO_sparse = HelperCmpdList.deNovoCmpdListFromAdHocCmpds(adHocCmpdList, this.listName, this.sessionController.getLoggedUser());

            // new fetch the list
            CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(clVO_sparse.getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());

            listManagerController.getListManagerBean().availableLists.add(clVO);
            listManagerController.getListManagerBean().activeList = clVO;

            System.out.println("UploadCmpds contains: " + clVO_sparse.getCountListMembers() + " cmpds");

        } catch (Exception e) {
            e.printStackTrace();
        }

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performStartOver() {
        searchCriteriaBean.reset();
        return "/webpages/searchCmpds?faces-redirect=true";
    }

    public String performCreateListBySearch() {

        searchCriteriaBean.newSearch();

        // parse text areas
        String[] splitStrings = null;
        String fixedString = null;
        int i;

        String delimiters = "[\\n\\r\\t,]+";

//  #####  ######  #    #   #####            ##    #####   ######    ##     ####
//    #    #        #  #      #             #  #   #    #  #        #  #   #
//    #    #####     ##       #            #    #  #    #  #####   #    #   ####
//    #    #         ##       #            ######  #####   #       ######       #
//    #    #        #  #      #            #    #  #   #   #       #    #  #    #
//    #    ######  #    #     #            #    #  #    #  ######  #    #   ####
//
        splitStrings = searchCriteriaBean.getAliasTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].trim();
            if (fixedString.length() > 0) {
                searchCriteriaBean.getAliases().add(fixedString);
            }
        }
        splitStrings = searchCriteriaBean.getCasTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].trim();
            if (fixedString.length() > 0) {
                searchCriteriaBean.getCases().add(fixedString);
            }
        }
        splitStrings = searchCriteriaBean.getCmpdNamedSetTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].trim();
            if (fixedString.length() > 0) {
                searchCriteriaBean.getCmpdNamedSets().add(fixedString);
            }
        }
        splitStrings = searchCriteriaBean.getDrugNameTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].trim();
            if (fixedString.length() > 0) {
                searchCriteriaBean.getDrugNames().add(fixedString);
            }
        }
        splitStrings = searchCriteriaBean.getMtxtTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].trim();
            if (fixedString.length() > 0) {
                searchCriteriaBean.getMtxtPieces().add(fixedString);
            }
        }
        splitStrings = searchCriteriaBean.getNscTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].replaceAll("[^0-9]", "");
            if (fixedString.length() > 0) {
                searchCriteriaBean.getNscs().add(fixedString);
            }
        }
        splitStrings = searchCriteriaBean.getPlateTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].trim();
            if (fixedString.length() > 0) {
                searchCriteriaBean.getPlates().add(fixedString);
            }
        }
        splitStrings = searchCriteriaBean.getProjectCodeTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].trim();
            if (fixedString.length() > 0) {
                searchCriteriaBean.getProjectCodes().add(fixedString);
            }
        }
        splitStrings = searchCriteriaBean.getPseudoAtomsTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].trim();
            if (fixedString.length() > 0) {
                searchCriteriaBean.getPseudoAtomsPieces().add(fixedString);
            }
        }
        splitStrings = searchCriteriaBean.getTargetTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].trim();
            if (fixedString.length() > 0) {
                searchCriteriaBean.getTargets().add(fixedString);
            }
        }

//    ##    #    #   #####   ####            ####    ####   #    #  #####
//   #  #   #    #     #    #    #          #    #  #    #  ##  ##  #    #
//  #    #  #    #     #    #    #          #       #    #  # ## #  #    #
//  ######  #    #     #    #    #          #       #    #  #    #  #####
//  #    #  #    #     #    #    #          #    #  #    #  #    #  #
//  #    #   ####      #     ####            ####    ####   #    #  #
//
        if (searchCriteriaBean.getAliasesFromAutoComp() != null && !searchCriteriaBean.getAliasesFromAutoComp().isEmpty()) {
            for (String s : searchCriteriaBean.getAliasesFromAutoComp()) {
                searchCriteriaBean.getAliases().add(s);
            }
        }

        if (searchCriteriaBean.getCasesFromAutoComp() != null && !searchCriteriaBean.getCasesFromAutoComp().isEmpty()) {
            for (String s : searchCriteriaBean.getCasesFromAutoComp()) {
                searchCriteriaBean.getCases().add(s);
            }
        }

        if (searchCriteriaBean.getCmpdNamedSetsFromAutoComp() != null && !searchCriteriaBean.getCmpdNamedSetsFromAutoComp().isEmpty()) {
            for (String s : searchCriteriaBean.getCmpdNamedSetsFromAutoComp()) {
                searchCriteriaBean.getCmpdNamedSets().add(s);
            }
        }

        if (searchCriteriaBean.getDrugNamesFromAutoComp() != null && !searchCriteriaBean.getDrugNamesFromAutoComp().isEmpty()) {
            for (String s : searchCriteriaBean.getDrugNamesFromAutoComp()) {
                searchCriteriaBean.getDrugNames().add(s);
            }
        }

        if (searchCriteriaBean.getMtxtPiecesFromAutoComp() != null && !searchCriteriaBean.getMtxtPiecesFromAutoComp().isEmpty()) {
            for (String s : searchCriteriaBean.getMtxtPiecesFromAutoComp()) {
                searchCriteriaBean.getMtxtPieces().add(s);
            }
        }

        if (searchCriteriaBean.getNscsFromAutoComp() != null && !searchCriteriaBean.getNscsFromAutoComp().isEmpty()) {
            for (String s : searchCriteriaBean.getNscsFromAutoComp()) {
                searchCriteriaBean.getNscs().add(s);
            }
        }

        if (searchCriteriaBean.getPlatesFromAutoComp() != null && !searchCriteriaBean.getPlatesFromAutoComp().isEmpty()) {
            for (String s : searchCriteriaBean.getPlatesFromAutoComp()) {
                searchCriteriaBean.getPlates().add(s);
            }
        }

        if (searchCriteriaBean.getProjectCodesFromAutoComp() != null && !searchCriteriaBean.getProjectCodesFromAutoComp().isEmpty()) {
            for (String s : searchCriteriaBean.getProjectCodesFromAutoComp()) {
                searchCriteriaBean.getProjectCodes().add(s);
            }
        }

        if (searchCriteriaBean.getTargetsFromAutoComp() != null && !searchCriteriaBean.getTargetsFromAutoComp().isEmpty()) {
            for (String s : searchCriteriaBean.getTargetsFromAutoComp()) {
                searchCriteriaBean.getTargets().add(s);
            }
        }

        if (searchCriteriaBean.getPseudoAtomsPiecesFromAutoComp() != null && !searchCriteriaBean.getPseudoAtomsPiecesFromAutoComp().isEmpty()) {
            for (String s : searchCriteriaBean.getPseudoAtomsPiecesFromAutoComp()) {
                searchCriteriaBean.getPseudoAtomsPieces().add(s);
            }
        }

        if (DEBUG) {
            System.out.println("After populating SearchCriteriaBean in performCreateListBySearch in ListContentController.");
            System.out.println("Content of searchCriteriaBean:");
            searchCriteriaBean.printCriteriaLists();
        }

//        // persist the list
//        Long cmpdListId = HelperCmpd.createCmpdListFromSearchCriteriaBean(listName, scb, null, sessionController.getLoggedUser());
//        // fetch the list            
//        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, sessionController.getLoggedUser());

        List<CmpdVO> cList = HelperCmpd.searchBySearchCriteriaBean(searchCriteriaBean, sessionController.getLoggedUser());

        Date now = new Date();
        
        CmpdListVO clVO = ApplicationScopeBean.cmpdListFromListOfCmpds(cList, "Search Results " + now.toString(), sessionController.getLoggedUser());
        
        listManagerController.getListManagerBean().availableLists.add(clVO);
        listManagerController.getListManagerBean().activeList = clVO;

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public CmpdListVO getTargetList() {
        return targetList;
    }

    public void setTargetList(CmpdListVO targetList) {
        this.targetList = targetList;
    }
    // </editor-fold>

//    public void appendToaliasTextArea(SelectEvent event) {
//        Object item = event.getObject();
//        if (searchCriteriaBean.getAliasTextArea() == null) {
//            searchCriteriaBean.setAliasTextArea("");
//        }
//        searchCriteriaBean.setAliasTextArea(item + "\n" + searchCriteriaBean.getAliasTextArea());
//    }
//
//    public void appendTotargetTextArea(SelectEvent event) {
//        Object item = event.getObject();
//        if (searchCriteriaBean.getTargetTextArea() == null) {
//            searchCriteriaBean.setTargetTextArea("");
//        }
//        searchCriteriaBean.setTargetTextArea(item + "\n" + searchCriteriaBean.getTargetTextArea());
//    }
//
//    public void appendToprojectCodeTextArea(SelectEvent event) {
//        Object item = event.getObject();
//        if (searchCriteriaBean.getProjectCodeTextArea() == null) {
//            searchCriteriaBean.setProjectCodeTextArea("");
//        }
//        searchCriteriaBean.setProjectCodeTextArea(item + "\n" + searchCriteriaBean.getProjectCodeTextArea());
//    }
//
//    public void appendToplateTextArea(SelectEvent event) {
//        Object item = event.getObject();
//        if (searchCriteriaBean.getPlateTextArea() == null) {
//            searchCriteriaBean.setPlateTextArea("");
//        }
//        searchCriteriaBean.setPlateTextArea(item + "\n" + searchCriteriaBean.getPlateTextArea());
//    }
//
//    public void appendTocasTextArea(SelectEvent event) {
//        Object item = event.getObject();
//        if (searchCriteriaBean.getCasTextArea() == null) {
//            searchCriteriaBean.setCasTextArea("");
//        }
//        searchCriteriaBean.setCasTextArea(item + "\n" + searchCriteriaBean.getCasTextArea());
//    }
//
//    public void appendTodrugNameTextArea(SelectEvent event) {
//        Object item = event.getObject();
//        if (searchCriteriaBean.getDrugNameTextArea() == null) {
//            searchCriteriaBean.setDrugNameTextArea("");
//        }
//        searchCriteriaBean.setDrugNameTextArea(item + "\n" + searchCriteriaBean.getDrugNameTextArea());
//    }
//
//    public void appendTonscTextArea(SelectEvent event) {
//        Object item = event.getObject();
//        if (searchCriteriaBean.getNscTextArea() == null) {
//            searchCriteriaBean.setNscTextArea("");
//        }
//        searchCriteriaBean.setNscTextArea(item + "\n" + searchCriteriaBean.getNscTextArea());
//    }
    public void appendTocmpdNamedSetTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (searchCriteriaBean.getCmpdNamedSetTextArea() == null) {
            searchCriteriaBean.setCmpdNamedSetTextArea("");
        }
        searchCriteriaBean.setCmpdNamedSetTextArea(item + "\n" + searchCriteriaBean.getCmpdNamedSetTextArea());
    }

}
