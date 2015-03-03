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
import mwk.datasystem.vo.CmpdKnownSaltVO;
import mwk.datasystem.vo.CmpdListVO;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class SaltsWorkbenchController implements Serializable {

  static final long serialVersionUID = -8653468638698142855l;

  private List<CmpdKnownSaltVO> salts;
  private List<CmpdKnownSaltVO> filteredSalts;
  private CmpdKnownSaltVO selectedSalt;

  public void handleListAllSalts() {

    String rtn = performListAllSalts();

    FacesContext ctx = FacesContext.getCurrentInstance();

    ExternalContext extCtx = ctx.getExternalContext();
    
    String url = extCtx.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/webpages/saltsWorkbench.xhtml"));
    
    try {
      extCtx.redirect(url);
    } catch (IOException ioe) {
      throw new FacesException(ioe);
    }

  }

  public String performListAllSalts() {

    this.salts = new ArrayList<CmpdKnownSaltVO>();
    this.salts = HelperCmpdKnownSalt.loadAllSalts();

    return "/webpages/saltsWorkbench?faces-redirect=true";

  }

  public void onRowEdit(RowEditEvent event) {

    FacesMessage msg = new FacesMessage("Salt Edited", ((CmpdKnownSaltVO) event.getObject()).getId().toString());
    FacesContext.getCurrentInstance().addMessage(null, msg);

    HelperCmpdKnownSalt.updateSalt((CmpdKnownSaltVO) event.getObject());

  }

  public void onRowCancel(RowEditEvent event) {
    FacesMessage msg = new FacesMessage("Edit Cancelled", ((CmpdKnownSaltVO) event.getObject()).getId().toString());
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

  public List<CmpdKnownSaltVO> getSalts() {
    return salts;
  }

  public void setSalts(List<CmpdKnownSaltVO> salts) {
    this.salts = salts;
  }

  public List<CmpdKnownSaltVO> getFilteredSalts() {
    return filteredSalts;
  }

  public void setFilteredSalts(List<CmpdKnownSaltVO> filteredSalts) {
    this.filteredSalts = filteredSalts;
  }

  public CmpdKnownSaltVO getSelectedSalt() {
    return selectedSalt;
  }

  public void setSelectedSalt(CmpdKnownSaltVO selectedSalt) {
    this.selectedSalt = selectedSalt;
  }

}
