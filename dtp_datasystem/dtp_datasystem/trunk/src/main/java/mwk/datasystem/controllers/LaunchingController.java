/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.Random;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
//import javax.faces.context.FacesContext;
import mwk.compare.jsonshuttle.CompareConfig;
import mwk.compare.vo.AffymetrixIdentVO;
import mwk.compare.vo.NanoStringIdentVO;
import mwk.compare.vo.SyntheticIdentVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import org.apache.log4j.Logger;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ViewScoped
public class LaunchingController implements Serializable {

  private static final long serialVersionUID = 8716877816987879179L;
  private static final Boolean DEBUG = Boolean.TRUE;
  private static final Logger lgr = Logger.getLogger("GLOBAL");
  private static Random randGen;

  static {
    randGen = new Random();
  }

  private String urlString;

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

  public String performConnectToLandingTEST() {

    System.out.println("In LaunchingController.performConnectToLandingTEST");

    CompareConfig compConf = new CompareConfig();

    // SyntheticIdentVO
    Integer[] nscArr = new Integer[]{
      740,
      750,
      752,
      755,
      3053
    };

    for (Integer n : nscArr) {
      SyntheticIdentVO siVO = new SyntheticIdentVO();
      siVO.setNsc(n);
      compConf.getSynthIdents().add(siVO);
    }

    // AffymetrixIdentVO
    String[] genSymArr = new String[]{
      "OR4F16",
      "FAM87A",
      "TTLL10",
      "B3GALT6",
      "SCNN1D"
    };

    for (String s : genSymArr) {
      AffymetrixIdentVO aiVO = new AffymetrixIdentVO();
      aiVO.setGeneSymbol(s);
      compConf.getAffyIdents().add(aiVO);
    }

    String[] moltIdArr = new String[]{
      "AFFY_EXON_UNIT_ID_10003",
      "AFFY_EXON_UNIT_ID_10004",
      "AFFY_EXON_UNIT_ID_10007",
      "AFFY_EXON_UNIT_ID_10010",
      "AFFY_EXON_UNIT_ID_10013"
    };

    for (String s : moltIdArr) {
      AffymetrixIdentVO aiVO = new AffymetrixIdentVO();
      aiVO.setMoltId(s);
      compConf.getAffyIdents().add(aiVO);
    }

    // NanoStringIdentVO
    String[] miRarr = new String[]{
      "hsa-miR-937",
      "hsa-miR-509-3p",
      "hsa-miR-548w",
      "hsa-miR-651",
      "hsa-miR-506-3p"
    };

    for (String s : miRarr) {
      NanoStringIdentVO nsiVO = new NanoStringIdentVO();
      nsiVO.setMiR(s);
      compConf.getNanoStringIdents().add(nsiVO);
    }

    Gson gson = new Gson();
    String json = gson.toJson(compConf);

    String siteString = applicationScopeBean.getCompareUrl() + "/landing.xhtml";

    String encJson = "";
    try {
      encJson = URLEncoder.encode(json, "UTF-8");
    } catch (Exception e) {
    }

    Random randGen = new Random();
    long randomId = randGen.nextLong();

    String paramString = "landingRandomId=" + randomId
            + "&landingAction=FETCH_DATA"
            + "&landingJson=" + encJson;

    urlString = siteString + "?" + paramString;

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

  public String performConnectToLanding() {

    System.out.println("In LaunchingController.performConnectToLanding");

    CompareConfig compConf = new CompareConfig();

    String action = "FETCH_DATA";

    compConf.setCompareAction(action);

    for (CmpdListMemberVO clmVO : listManagerController.getListManagerBean().selectedActiveListMembers) {
      if (clmVO.getCmpd() != null && clmVO.getCmpd().getNsc() != null) {
        SyntheticIdentVO siVO = new SyntheticIdentVO();
        siVO.setNsc(clmVO.getCmpd().getNsc());
        compConf.getSynthIdents().add(siVO);
      }
    }

    Gson gson = new Gson();
    String json = gson.toJson(compConf);

    String siteString = applicationScopeBean.getCompareUrl() + "/landing.xhtml";

    String encJson = "";
    try {
      encJson = URLEncoder.encode(json, "UTF-8");
    } catch (Exception e) {
    }

    long randomId = randGen.nextLong();

    String paramString = "landingRandomId=" + randomId
            + "&landingAction=" + action
            + "&landingJson=" + encJson;

    urlString = siteString + "?" + paramString;

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
  public String getUrlString() {
    
    return urlString;
  }

  public void setUrlString(String urlString) {
    this.urlString = urlString;
  }

  //</editor-fold>
}
