/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.ValueChangeEvent;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class SessionController implements Serializable {

  static final long serialVersionUID = -8653468638698142855l;

  private String loggedUser;

  private static List<String> validNavigationModes;

  static {
    String[] navArr = new String[]{"ICONS", "LINKS", "MENUBARS"};
    validNavigationModes = new ArrayList<String>(Arrays.asList(navArr));
  }

  String navigationMode;

  protected ConfigurationBean configurationBean;

  @PostConstruct
  public void init() {

    System.out.println("In @PostConstruct init() in SessionController");
    
    navigationMode = "ICONS";

    this.loggedUser = "PUBLIC";

    if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() == null) {
      this.loggedUser = "PUBLIC";
    } else {
      this.loggedUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
      if (FacesContext.getCurrentInstance().getExternalContext().isUserInRole("NCI DCTD_PLP_DEVELOPER")) {
        this.loggedUser = "DTP_" + this.loggedUser;
      } else {
        this.loggedUser = "PUBLIC";
      }
    }

    configurationBean = new ConfigurationBean();
  }

  public SessionController() {

    System.out.println("In Constructor in SessionController");

    this.loggedUser = "PUBLIC";

    init();

  }

  public String goPublic() {
    if (this.loggedUser.startsWith("DTP_")) {
      this.loggedUser = "PUBLIC";
    }
    return "/webpages/searchCmpds.xhtml?faces-redirect=true";
  }

  public String logout() {
    System.out.println("Now in logout in SessionController");
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    return "/webpages/searchCmpds.xhtml?faces-redirect=true";
  }

  public void handleLogout() {
    String rtn = this.logout();
    FacesContext ctx = FacesContext.getCurrentInstance();
    ExternalContext extCtx = ctx.getExternalContext();
    String url = extCtx.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/webpages/s.xhtml"));
    try {
      extCtx.redirect(url);
    } catch (IOException ioe) {
      throw new FacesException(ioe);
    }
  }

   public void selectNavigationMode(ValueChangeEvent event) {
    navigationMode = (String) event.getNewValue();
  }
  
  //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
  public String getLoggedUser() {
    return this.loggedUser;
  }

  public String getNavigationMode() {
    return navigationMode;
  }

  public void setNavigationMode(String navModIn) {

    if (validNavigationModes.contains(navModIn)) {
      this.navigationMode = navModIn;
    } else {
      this.navigationMode = "ICONS";
    }

  }

  public ConfigurationBean getConfigurationBean() {
    return configurationBean;
  }

  public void setConfigurationBean(ConfigurationBean configurationBean) {
    this.configurationBean = configurationBean;
  }

//</editor-fold>
}
