/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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

    private ListContentBean listContentBean;

    public ListContentBean getListContentBean() {
        return listContentBean;
    }

    public ListContentController() {
        this.listContentBean = new ListContentBean();
    }

    private String listName;
    UploadedFile uploadedFile;
    List<CmpdListMemberVO> selectedActiveListMembers;
    CmpdListVO targetList;

    public void onRowSelect(SelectEvent evt) {

        try {

            CmpdListMemberVO clmVO = (CmpdListMemberVO) evt.getObject();

            System.out.println("Select: " + clmVO.getCmpd().getNsc() + " " + clmVO.getCmpd().getAdHocCmpdId());

            clmVO.setIsSelected(Boolean.TRUE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onRowDeSelect(SelectEvent evt) {

        try {

            CmpdListMemberVO clmVO = (CmpdListMemberVO) evt.getObject();

            System.out.println("Delect: " + clmVO.getCmpd().getNsc() + " " + clmVO.getCmpd().getAdHocCmpdId());

            clmVO.setIsSelected(Boolean.FALSE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @return For checkboxes outside of dataTable
     */
    public String performMySelect() {

        StringBuilder msgBuilder = new StringBuilder();

        // check the activeList for selectedMembers        
        if (this.selectedActiveListMembers == null) {
            msgBuilder.append("selectedActiveListMembers is null.  new ArrayList().");
            System.out.println("selectedActiveListMembers is null.  new ArrayList().");
            this.selectedActiveListMembers = new ArrayList<CmpdListMemberVO>();
        } else {
            msgBuilder.append("selectedActiveListMembers is NOT null.  list.clear().");
            System.out.println("selectedActiveListMembers is NOT null.  list.clear().");
            this.selectedActiveListMembers.clear();
        }

        for (CmpdListMemberVO clmVO : listManagerController.getListManagerBean().activeList.getCmpdListMembers()) {
            if (clmVO.getIsSelected() != null && clmVO.getIsSelected()) {
                msgBuilder.append("selected: " + clmVO.getCmpd().getNsc() + " " + clmVO.getCmpd().getAdHocCmpdId());
                System.out.println("selected: " + clmVO.getCmpd().getNsc() + " " + clmVO.getCmpd().getAdHocCmpdId());
                this.selectedActiveListMembers.add(clmVO);
            }
        }

        FacesMessage msg = new FacesMessage("Selected List Members: ", msgBuilder.toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        return "/webpages/activeListTable?faces-redirect=true";

    }

    //
    public String performDeleteFromActiveList() {

        HelperCmpdListMember.deleteCmpdListMembers(this.targetList, this.selectedActiveListMembers, this.sessionController.getLoggedUser());

        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(listManagerController.getListManagerBean().activeList.getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());

        listManagerController.getListManagerBean().activeList = clVO;

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
        HelperCmpdListMember.appendCmpdListMembers(clVO, this.selectedActiveListMembers, this.sessionController.getLoggedUser());

        // have to UPDATE the list   
        CmpdListVO updatedClVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, this.sessionController.getLoggedUser());

    // have to add to the session
        // and move to the new list        
        listManagerController.getListManagerBean().availableLists.add(updatedClVO);
        listManagerController.getListManagerBean().activeList = updatedClVO;

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performAppendSelectedToExistingList() {

        HelperCmpdListMember.appendCmpdListMembers(this.targetList, this.selectedActiveListMembers, this.sessionController.getLoggedUser());

        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(this.targetList.getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());

        listManagerController.getListManagerBean().activeList = clVO;

        // is this really the way to do this?
        listManagerController.performUpdateAvailableLists();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performConnectToLandingSpotfire() {

        StringBuilder sb = new StringBuilder();

        boolean isFirst = true;

        for (CmpdListMemberVO clmVO : this.selectedActiveListMembers) {
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

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performCreateListBySearch() {

        // parse text areas
        String[] splitStrings = null;
        String fixedString = null;
        int i;

        String delimiters = "[\\n\\r\\t\\s,]+";

        QueryObject qo = new QueryObject();

        splitStrings = this.listContentBean.getNscTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].replaceAll("[^0-9]", "");
            if (fixedString.length() > 0) {
                qo.getNscs().add(fixedString);
            }
        }

        splitStrings = this.listContentBean.getCasTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                qo.getCases().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getProjectCodeTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                qo.getProjectCodes().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getDrugNameTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                qo.getDrugNames().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getAliasTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                qo.getAliases().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getPlateTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                qo.getPlates().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getTargetTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                qo.getTargets().add(splitStrings[i]);
            }
        }

        splitStrings = this.listContentBean.getCmpdNamedSetTextArea().split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            if (splitStrings[i].length() > 0) {
                qo.getCmpdNamedSets().add(splitStrings[i]);
            }
        }

        System.out.println("Content of listContentBean:");
        this.listContentBean.printCriteriaLists();
        System.out.println("Content of QueryObject:");
        qo.printCriteriaLists();

        System.out.println("Calling createCmpdListFromQueryObject in HelperCmpd from performCreateListBySearch in ListContentController.");
        Long cmpdListId = HelperCmpd.createCmpdListFromQueryObject(this.listName, qo, null, this.sessionController.getLoggedUser());

        // now fetch the list            
        CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, this.sessionController.getLoggedUser());

        listManagerController.getListManagerBean().availableLists.add(clVO);
        listManagerController.getListManagerBean().activeList = clVO;

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

    public List<CmpdListMemberVO> getSelectedActiveListMembers() {
        return selectedActiveListMembers;
    }

    public void setSelectedActiveListMembers(List<CmpdListMemberVO> selectedActiveListMembers) {
        this.selectedActiveListMembers = selectedActiveListMembers;
    }

    public CmpdListVO getTargetList() {
        return targetList;
    }

    public void setTargetList(CmpdListVO targetList) {
        this.targetList = targetList;
    }
    // </editor-fold>

}
