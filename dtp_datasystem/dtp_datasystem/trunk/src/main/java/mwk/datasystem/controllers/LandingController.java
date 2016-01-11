/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ViewScoped
public class LandingController implements Serializable {
    
    //http://localhost:8080/sarcomacompare/landing.xhtml?landingNscSet=740xxx750xxx752xxx755xxx762

  private static final long serialVersionUID = 8716877816987879179L;

  private static final Logger lgr = Logger.getLogger("GLOBAL");

  @ManagedProperty("#{sessionController}")
  private SessionController sessionController;

  public void setSessionController(SessionController sessionController) {
    this.sessionController = sessionController;
  }

  @ManagedProperty("#{listContentController}")
  private ListContentController listContentController;

  public void setListContentController(ListContentController listContentController) {
    this.listContentController = listContentController;
  }

  @ManagedProperty("#{listManagerController}")
  private ListManagerController listManagerController;

  public void setListManagerController(ListManagerController listManagerController) {
    this.listManagerController = listManagerController;
  }

  //
  private String landingNscSet;
  private List<Integer> nscIntList;

  /**
   *
   * @return
   */
  public String performLanding() {

    lgr.debug("In performLanding in landingController.");

    try {
      Thread.sleep(5000);
    } catch (Exception e) {
      lgr.error("Exception in LandingController " + e);
    }

    // nsc numbers
    // defaults to 740
    nscIntList = new ArrayList<Integer>();

    if (landingNscSet != null && landingNscSet.length() > 0) {

      String[] splitNscSet = landingNscSet.split("xxx");
      for (String s : splitNscSet) {          
          System.out.println("In landingController nscString: " + s);          
        if (NumberUtils.isNumber(s)) {
          nscIntList.add(Integer.parseInt(s));
        }
      }

    } else {
      nscIntList.add(Integer.valueOf(740));
      nscIntList.add(Integer.valueOf(750));
      nscIntList.add(Integer.valueOf(752));
      nscIntList.add(Integer.valueOf(755));
      nscIntList.add(Integer.valueOf(3053));
    }

    if (!nscIntList.isEmpty()) {

      // format to pass to ListContentController as textArea content
      // THIS IS BRAIN DEAD.  TODO: rework ListContentController.
      StringBuilder sb = new StringBuilder();
      for (Integer nsc : nscIntList) {
        sb.append(nsc.toString());
        sb.append("\n");
      }

      
      System.out.println("In landingController sb.toString: " + sb.toString());
      
      listContentController.getSearchCriteriaBean().setNscTextArea(sb.toString());
      listContentController.setListName("List created by LandingController");
      
      String rtn = listContentController.performCreateListBySearch();

    }

    return "/webpages/activeListTable.xhtml?faces-redirect=true";

  }

  //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
  public List<Integer> getNscIntList() {
    return nscIntList;
  }

  public void setNscIntList(List<Integer> nscIntList) {
    this.nscIntList = nscIntList;
  }

  /**
   *
   * @return
   */
  public String getLandingNscSet() {
    return landingNscSet;
  }

  /**
   *
   * @param landingNscSet
   */
  public void setLandingNscSet(String landingNscSet) {
    this.landingNscSet = landingNscSet;
  }

    //</editor-fold>
}
