/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.IOException;
import java.io.Serializable;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class SessionController implements Serializable {

  static final long serialVersionUID = -8653468638698142855l;
  //    
  private String loggedUser;
  //

  @PostConstruct
  public void init() {
    if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() == null) {
      this.loggedUser = "PUBLIC";
    } else {
      this.loggedUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
  }

  public SessionController() {

    if (FacesContext.getCurrentInstance().getExternalContext().getUserPrincipal() == null) {
      this.loggedUser = "PUBLIC";
    } else {
      this.loggedUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }

  }

  public String logout() {
    System.out.println("Now in logout in SessionController");
    FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    return "/webpages/availableLists.xhtml?faces-redirect=true";
  }

  public void handleLogout() {

    String rtn = this.logout();
    
    FacesContext ctx = FacesContext.getCurrentInstance();

    ExternalContext extCtx = ctx.getExternalContext();

    String url = extCtx.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/webpages/availableLists.xhtml"));

    try {
      extCtx.redirect(url);
    } catch (IOException ioe) {
      throw new FacesException(ioe);
    }

  }

  public String getLoggedUser() {
    return this.loggedUser;
  }

}
