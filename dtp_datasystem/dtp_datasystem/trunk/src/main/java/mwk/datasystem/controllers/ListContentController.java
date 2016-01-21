/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
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

  private SearchCriteriaBean scb;

  public SearchCriteriaBean getSearchCriteriaBean() {
    return scb;
  }

  public ListContentController() {
    if (this.scb == null) {
      this.scb = new SearchCriteriaBean();
    }
  }

  private String listName;
  UploadedFile uploadedFile;
  CmpdListVO targetList;

  /**
   *
   * @return For checkboxes outside of dataTable
   */
  public String performDeleteFromActiveList() {

    HelperCmpdListMember.deleteCmpdListMembers(targetList, listManagerController.getListManagerBean().getSelectedActiveListMembers(), sessionController.getLoggedUser());

    CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(listManagerController.getListManagerBean().activeList.getCmpdListId(), Boolean.TRUE, sessionController.getLoggedUser());

    listManagerController.getListManagerBean().activeList = clVO;

    sessionController.configurationBean.performUpdateColumns();

    return "/webpages/activeListTable?faces-redirect=true";
  }

  public String performCreateNewListFromActiveList() {
    return null;
  }

  public String performCreateNewListFromSelectedListMembers() {

    // first, create an empty list
    Long cmpdListId = HelperCmpdListMember.createEmptyList(listName, sessionController.getLoggedUser());

    System.out.println("cmpdListId is: " + cmpdListId + " after createEmptyList");

    CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, sessionController.getLoggedUser());

    // append the selected
    HelperCmpdListMember.appendCmpdListMembers(clVO, listManagerController.getListManagerBean().getSelectedActiveListMembers(), sessionController.getLoggedUser());

    // have to UPDATE the list   
    CmpdListVO updatedClVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, sessionController.getLoggedUser());

        // have to add to the session
    // and move to the new list        
    listManagerController.getListManagerBean().availableLists.add(updatedClVO);
    listManagerController.getListManagerBean().activeList = updatedClVO;

    sessionController.configurationBean.performUpdateColumns();

    return "/webpages/activeListTable?faces-redirect=true";

  }

  public String performAppendSelectedToExistingList() {

    HelperCmpdListMember.appendCmpdListMembers(targetList, listManagerController.getListManagerBean().selectedActiveListMembers, sessionController.getLoggedUser());

    CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(targetList.getCmpdListId(), Boolean.TRUE, sessionController.getLoggedUser());

    listManagerController.getListManagerBean().activeList = clVO;

    listManagerController.performUpdateAvailableLists();

    return "/webpages/activeListTable?faces-redirect=true";

  }

  public String performConnectToLandingSpotfire() {

    StringBuilder sb = new StringBuilder();

    boolean isFirst = true;

    for (CmpdListMemberVO clmVO : listManagerController.getListManagerBean().selectedActiveListMembers) {
      if (clmVO.getCmpd() != null && clmVO.getCmpd().getNsc() != null) {
        if (!isFirst) {
          sb.append("xxx");
        }
        sb.append(clmVO.getCmpd().getNsc());
        isFirst = false;
      }
    }

        String siteString = applicationScopeBean.getLandingSpotfireUrl();        
        
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

      String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(uploadedFile.getFileName());

      System.out.println(" fileName: " + uploadedFile.getFileName() + " fileSize: " + uploadedFile.getSize() + " realPath: " + realPath);

      File systemFile = new File(realPath);

      FileOutputStream fos = new FileOutputStream(systemFile, false);

      byte[] byteArray = IOUtils.toByteArray(uploadedFile.getInputstream());

      fos.write(byteArray);

      fos.flush();
      fos.close();

      FacesMessage msg = new FacesMessage(
              FacesMessage.SEVERITY_INFO,
              "Uploaded File",
              "fileName: " + uploadedFile.getFileName() + " fileSize: " + uploadedFile.getSize());

      FacesContext.getCurrentInstance().addMessage(null, msg);

      MoleculeParser mp = new MoleculeParser();

      File smilesFile = new File(realPath);

      ArrayList<AdHocCmpd> adHocCmpdList = mp.parseSMILESFile(smilesFile);

      CmpdListVO clVO_sparse = HelperCmpdList.deNovoCmpdListFromAdHocCmpds(adHocCmpdList, listName, sessionController.getLoggedUser());

      // new fetch the list
      CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(clVO_sparse.getCmpdListId(), Boolean.TRUE, sessionController.getLoggedUser());

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

      String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(uploadedFile.getFileName());

      System.out.println(" fileName: " + uploadedFile.getFileName() + " fileSize: " + uploadedFile.getSize() + " realPath: " + realPath);

      File systemFile = new File(realPath);

      FileOutputStream fos = new FileOutputStream(systemFile, false);

      byte[] byteArray = IOUtils.toByteArray(uploadedFile.getInputstream());

      fos.write(byteArray);

      fos.flush();
      fos.close();

      FacesMessage msg = new FacesMessage(
              FacesMessage.SEVERITY_INFO,
              "Uploaded File",
              "fileName: " + uploadedFile.getFileName() + " fileSize: " + uploadedFile.getSize());

      FacesContext.getCurrentInstance().addMessage(null, msg);

      MoleculeParser mp = new MoleculeParser();

      File sdFile = new File(realPath);

      ArrayList<AdHocCmpd> adHocCmpdList = mp.parseSDF(sdFile);

      CmpdListVO clVO_sparse = HelperCmpdList.deNovoCmpdListFromAdHocCmpds(adHocCmpdList, listName, sessionController.getLoggedUser());

      // new fetch the list
      CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(clVO_sparse.getCmpdListId(), Boolean.TRUE, sessionController.getLoggedUser());

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

    splitStrings = scb.getNscTextArea().split(delimiters);
    for (i = 0; i < splitStrings.length; i++) {
      fixedString = splitStrings[i].replaceAll("[^0-9]", "");
      if (fixedString.length() > 0) {
        scb.getNscs().add(fixedString);
      }
    }

    splitStrings = scb.getCasTextArea().split(delimiters);
    for (i = 0; i < splitStrings.length; i++) {
      if (splitStrings[i].length() > 0) {
        scb.getCases().add(splitStrings[i]);
      }
    }

    splitStrings = scb.getProjectCodeTextArea().split(delimiters);
    for (i = 0; i < splitStrings.length; i++) {
      if (splitStrings[i].length() > 0) {
        scb.getProjectCodes().add(splitStrings[i]);
      }
    }

    splitStrings = scb.getDrugNameTextArea().split(delimiters);
    for (i = 0; i < splitStrings.length; i++) {
      if (splitStrings[i].length() > 0) {
        scb.getDrugNames().add(splitStrings[i]);
      }
    }

    splitStrings = scb.getAliasTextArea().split(delimiters);
    for (i = 0; i < splitStrings.length; i++) {
      if (splitStrings[i].length() > 0) {
        scb.getAliases().add(splitStrings[i]);
      }
    }

    splitStrings = scb.getPlateTextArea().split(delimiters);
    for (i = 0; i < splitStrings.length; i++) {
      if (splitStrings[i].length() > 0) {
        scb.getPlates().add(splitStrings[i]);
      }
    }

    splitStrings = scb.getTargetTextArea().split(delimiters);
    for (i = 0; i < splitStrings.length; i++) {
      if (splitStrings[i].length() > 0) {
        scb.getTargets().add(splitStrings[i]);
      }
    }

    splitStrings = scb.getCmpdNamedSetTextArea().split(delimiters);
    for (i = 0; i < splitStrings.length; i++) {
      if (splitStrings[i].length() > 0) {
        scb.getCmpdNamedSets().add(splitStrings[i]);
      }
    }

    System.out.println("After populating SearchCriteriaBean in performCreateListBySearch in ListContentController.");

    System.out.println("Content of searchCriteriaBean:");
    scb.printCriteriaLists();

    Long cmpdListId = HelperCmpd.createCmpdListFromSearchCriteriaBean(listName, scb, null, sessionController.getLoggedUser());

    // now fetch the list            
    CmpdListVO clVO = HelperCmpdList.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, sessionController.getLoggedUser());

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
    if (scb.getAliasTextArea() == null) {
      scb.setAliasTextArea("");
    }
    scb.setAliasTextArea(item + " " + scb.getAliasTextArea());
  }

  public void appendTotargetTextArea(SelectEvent event) {
    Object item = event.getObject();
    if (scb.getTargetTextArea() == null) {
      scb.setTargetTextArea("");
    }
    scb.setTargetTextArea(item + " " + scb.getTargetTextArea());
  }

  public void appendToprojectCodeTextArea(SelectEvent event) {
    Object item = event.getObject();
    if (scb.getProjectCodeTextArea() == null) {
      scb.setProjectCodeTextArea("");
    }
    scb.setProjectCodeTextArea(item + " " + scb.getProjectCodeTextArea());
  }

  public void appendToplateTextArea(SelectEvent event) {
    Object item = event.getObject();
    if (scb.getPlateTextArea() == null) {
      scb.setPlateTextArea("");
    }
    scb.setPlateTextArea(item + " " + scb.getPlateTextArea());
  }

  public void appendTocasTextArea(SelectEvent event) {
    Object item = event.getObject();
    if (scb.getCasTextArea() == null) {
      scb.setCasTextArea("");
    }
    scb.setCasTextArea(item + " " + scb.getCasTextArea());
  }

  public void appendTodrugNameTextArea(SelectEvent event) {
    Object item = event.getObject();
    if (scb.getDrugNameTextArea() == null) {
      scb.setDrugNameTextArea("");
    }
    scb.setDrugNameTextArea(item + " " + scb.getDrugNameTextArea());
  }

  public void appendTonscTextArea(SelectEvent event) {
    Object item = event.getObject();
    if (scb.getNscTextArea() == null) {
      scb.setNscTextArea("");
    }
    scb.setNscTextArea(item + " " + scb.getNscTextArea());
  }

  public void appendTocmpdNamedSetTextArea(SelectEvent event) {
    Object item = event.getObject();
    if (scb.getCmpdNamedSetTextArea() == null) {
      scb.setCmpdNamedSetTextArea("");
    }
    scb.setCmpdNamedSetTextArea(item + " " + scb.getCmpdNamedSetTextArea());
  }

}
