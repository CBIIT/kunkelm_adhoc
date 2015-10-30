/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HelperCmpdListMember;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import org.apache.commons.io.IOUtils;
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

    private SearchCriteriaBean listContentBean;

    public SearchCriteriaBean getListContentBean() {
        return listContentBean;
    }

    public ListContentController() {
        this.listContentBean = new SearchCriteriaBean();
    }

    private String listName;
    UploadedFile uploadedFile;
    CmpdListVO targetList;

    /**
     *
     * @return For checkboxes outside of dataTable
     */
    public String performDeleteFromActiveList() {

        HelperCmpdListMember.deleteCmpdListMembers(this.targetList, listManagerController.getListManagerBean().getSelectedActiveListMembers(), this.sessionController.getLoggedUser());

        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(listManagerController.getListManagerBean().activeList.getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());

        listManagerController.getListManagerBean().activeList = clVO;

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";
    }

    public String performCreateNewListFromActiveList() {
        return null;
    }

    public String performCreateNewListFromSelectedListMembers() {

        // first, create an empty list
        Long cmpdListId = HelperCmpdListMember.createEmptyList(this.listName, this.sessionController.getLoggedUser());

        System.out.println("cmpdListId is: " + cmpdListId + " after createEmptyList");

        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, this.sessionController.getLoggedUser());

        // append the selected
        HelperCmpdListMember.appendCmpdListMembers(clVO, listManagerController.getListManagerBean().getSelectedActiveListMembers(), this.sessionController.getLoggedUser());

        // have to UPDATE the list   
        CmpdListVO updatedClVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, this.sessionController.getLoggedUser());

        // have to add to the session
        // and move to the new list        
        listManagerController.getListManagerBean().availableLists.add(updatedClVO);
        listManagerController.getListManagerBean().activeList = updatedClVO;

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performAppendSelectedToExistingList() {

        HelperCmpdListMember.appendCmpdListMembers(this.targetList, listManagerController.getListManagerBean().selectedActiveListMembers, this.sessionController.getLoggedUser());

        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(this.targetList.getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());

        listManagerController.getListManagerBean().activeList = clVO;

        listManagerController.performUpdateAvailableLists();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performConnectToLandingSpotfire() {

        StringBuilder sb = new StringBuilder();

        boolean isFirst = true;

        for (CmpdListMemberVO clmVO : this.listManagerController.getListManagerBean().selectedActiveListMembers) {
            if (clmVO.getCmpd() != null && clmVO.getCmpd().getNsc() != null) {
                if (!isFirst) {
                    sb.append("xxx");
                }
                sb.append(clmVO.getCmpd().getNsc());
                isFirst = false;
            }
        }

        String siteString = "http://localhost:8080/sarcomacompare/landingSpotfire.xhtml";
        String paramString = "landingSpotfireNscSet=" + sb.toString();
        String urlString = siteString + "?" + paramString;

//        landingSpotfireNscSet
//        landingSpotfireEndpointSet
//        landingSpotfireIncludeExperiments
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = ctx.getExternalContext();

        System.out.println("Generated URL is: " + urlString);

        try {
            extCtx.redirect(urlString);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }

        return null;

    }

    public String performSmilesFileUpload() {

        try {

            Random randomGenerator = new Random();

            String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(this.uploadedFile.getFileName());

            System.out.println(" fileName: " + this.uploadedFile.getFileName() + " fileSize: " + this.uploadedFile.getSize() + " realPath: " + realPath);

            File systemFile = new File(realPath);

            FileOutputStream fos = new FileOutputStream(systemFile, false);

            byte[] byteArray = IOUtils.toByteArray(this.uploadedFile.getInputstream());

            fos.write(byteArray);

            fos.flush();
            fos.close();

            FacesMessage msg = new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Uploaded File",
                    "fileName: " + this.uploadedFile.getFileName() + " fileSize: " + this.uploadedFile.getSize());

            FacesContext.getCurrentInstance().addMessage(null, msg);

            MoleculeParser mp = new MoleculeParser();

            File smilesFile = new File(realPath);

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

            String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(this.uploadedFile.getFileName());

            System.out.println(" fileName: " + this.uploadedFile.getFileName() + " fileSize: " + this.uploadedFile.getSize() + " realPath: " + realPath);

            File systemFile = new File(realPath);

            FileOutputStream fos = new FileOutputStream(systemFile, false);

            byte[] byteArray = IOUtils.toByteArray(this.uploadedFile.getInputstream());

            fos.write(byteArray);

            fos.flush();
            fos.close();

            FacesMessage msg = new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Uploaded File",
                    "fileName: " + this.uploadedFile.getFileName() + " fileSize: " + this.uploadedFile.getSize());

            FacesContext.getCurrentInstance().addMessage(null, msg);

            MoleculeParser mp = new MoleculeParser();

            File sdFile = new File(realPath);

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

    public String performCreateListBySearch() {

        // parse text areas
        String[] splitStrings = null;
        String fixedString = null;
        int i;

        String delimiters = "[\\n\\r\\t\\s,]+";

        splitStrings = this.listContentBean.getNscTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].replaceAll("[^0-9]", "");
            if (fixedString.length() > 0) {
                this.listContentBean.getNscs().add(fixedString);
            }
        }

        splitStrings = this.listContentBean.getCasTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                this.listContentBean.getCases().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getProjectCodeTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                this.listContentBean.getProjectCodes().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getDrugNameTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                this.listContentBean.getDrugNames().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getAliasTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                this.listContentBean.getAliases().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getPlateTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                this.listContentBean.getPlates().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getTargetTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                this.listContentBean.getTargets().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getCmpdNamedSetTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                this.listContentBean.getCmpdNamedSets().add(splitStrings[i]);
            }
        }

        System.out.println("After populating SearchCriteriaBean in performCreateListBySearch in ListContentController.");
        
        System.out.println("Content of listContentBean:");
        this.listContentBean.printCriteriaLists();
        
        Long cmpdListId = HelperCmpd.createCmpdListFromQueryObject(this.listName, this.listContentBean, null, this.sessionController.getLoggedUser());

        // now fetch the list            
        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, this.sessionController.getLoggedUser());

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

    public void appendToaliasTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (listContentBean.getAliasTextArea() == null) {
            listContentBean.setAliasTextArea("");
        }
        listContentBean.setAliasTextArea(item + " " + listContentBean.getAliasTextArea());
    }

    public void appendTotargetTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (listContentBean.getTargetTextArea() == null) {
            listContentBean.setTargetTextArea("");
        }
        listContentBean.setTargetTextArea(item + " " + listContentBean.getTargetTextArea());
    }

    public void appendToprojectCodeTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (listContentBean.getProjectCodeTextArea() == null) {
            listContentBean.setProjectCodeTextArea("");
        }
        listContentBean.setProjectCodeTextArea(item + " " + listContentBean.getProjectCodeTextArea());
    }

    public void appendToplateTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (listContentBean.getPlateTextArea() == null) {
            listContentBean.setPlateTextArea("");
        }
        listContentBean.setPlateTextArea(item + " " + listContentBean.getPlateTextArea());
    }

    public void appendTocasTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (listContentBean.getCasTextArea() == null) {
            listContentBean.setCasTextArea("");
        }
        listContentBean.setCasTextArea(item + " " + listContentBean.getCasTextArea());
    }

    public void appendTodrugNameTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (listContentBean.getDrugNameTextArea() == null) {
            listContentBean.setDrugNameTextArea("");
        }
        listContentBean.setDrugNameTextArea(item + " " + listContentBean.getDrugNameTextArea());
    }

    public void appendTonscTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (listContentBean.getNscTextArea() == null) {
            listContentBean.setNscTextArea("");
        }
        listContentBean.setNscTextArea(item + " " + listContentBean.getNscTextArea());
    }

    public void appendTocmpdNamedSetTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (listContentBean.getCmpdNamedSetTextArea() == null) {
            listContentBean.setCmpdNamedSetTextArea("");
        }
        listContentBean.setCmpdNamedSetTextArea(item + " " + listContentBean.getCmpdNamedSetTextArea());
    }

}
