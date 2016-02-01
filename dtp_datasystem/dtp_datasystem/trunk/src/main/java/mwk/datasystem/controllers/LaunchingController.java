/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import mwk.compare.vo.NscCompoundVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import org.apache.log4j.Logger;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ViewScoped
public class LaunchingController implements Serializable {

  //http://localhost:8080/sarcomacompare/landing.xhtml?landingNscSet=740xxx750xxx752xxx755xxx762
  private static final long serialVersionUID = 8716877816987879179L;

  private static final Boolean DEBUG = Boolean.TRUE;

  private static final Logger lgr = Logger.getLogger("GLOBAL");

  // reach-through to applicationScopeBean
  @ManagedProperty(value = "#{applicationScopeBean}")
  private ApplicationScopeBean applicationScopeBean;

  public void setApplicationScopeBean(ApplicationScopeBean applicationScopeBean) {
    this.applicationScopeBean = applicationScopeBean;
  }

  @ManagedProperty("#{listManagerController}")
  private ListManagerController listManagerController;

  public void setListManagerController(ListManagerController listManagerController) {
    this.listManagerController = listManagerController;
  }

  public String performConnectToLandingJson() {

    ArrayList<NscCompoundVO> nscList = new ArrayList<NscCompoundVO>();

    for (CmpdListMemberVO clmVO : listManagerController.getListManagerBean().selectedActiveListMembers) {
    
      if (clmVO.getCmpd() != null && clmVO.getCmpd().getNsc() != null) {
        
        NscCompoundVO ncVO = new NscCompoundVO();
        
        ncVO.setNsc(clmVO.getCmpd().getNsc());
        ncVO.setDrugName("fakeDrugName");
        ncVO.setPrefix("fakePrefix");
        ncVO.setSmiles("fakeSmiles");
        ncVO.setTarget("fakeTarget");

        nscList.add(ncVO);
      }
    }

    Gson gson = new Gson();
    String json = gson.toJson(nscList);

    System.out.println("In LaunchingController.performConnectToLandingJson: json: " + json);

    String siteString = applicationScopeBean.getCompareUrl() + "/landingJson.xhtml";

    String encJson = "";
    try {
      encJson = URLEncoder.encode(json, "UTF-8");
    } catch (Exception e) {
    }

    String paramString = "landingJson=" + encJson;
    String urlString = siteString + "?" + paramString;

    FacesContext ctx = FacesContext.getCurrentInstance();
    ExternalContext extCtx = ctx.getExternalContext();

    if (DEBUG) {
      System.out.println("Generated URL is: " + urlString);
    }

    try {
      extCtx.redirect(urlString);
    } catch (IOException ioe) {
      throw new FacesException(ioe);
    }

    return null;

  }

  //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
  //</editor-fold>
}
