/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import mwk.datasystem.util.HelperCmpdKnownSalt;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdKnownSaltVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class FragmentsWorkbenchController implements Serializable {

  static final long serialVersionUID = -8653468638698142855l;
  
  private CmpdVO cmpd;
  private List<CmpdFragmentVO> fragments;
  private CmpdFragmentVO selectedFragment;
  
  public void handleListAllSalts() {

    String rtn = performListAllSalts();

    FacesContext ctx = FacesContext.getCurrentInstance();

    ExternalContext extCtx = ctx.getExternalContext();
    
    String url = extCtx.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/webpages/saltWorkbench.xhtml"));
    
    try {
      extCtx.redirect(url);
    } catch (IOException ioe) {
      throw new FacesException(ioe);
    }

  }

  public String performListAllSalts() {

    this.fragments = new ArrayList<CmpdFragmentVO>();
    
    return "/webpages/fragmentsWorkbench?faces-redirect=true";

  }

  public void onRowEdit(RowEditEvent event) {

    FacesMessage msg = new FacesMessage("Salt Edited", ((CmpdKnownSaltVO) event.getObject()).getId().toString());
    FacesContext.getCurrentInstance().addMessage(null, msg);

    HelperCmpdKnownSalt.updateSalt((CmpdKnownSaltVO) event.getObject());

  }

  public void onRowCancel(RowEditEvent event) {
    FacesMessage msg = new FacesMessage("Edit Cancelled", ((CmpdListVO) event.getObject()).getId().toString());
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

  
}
