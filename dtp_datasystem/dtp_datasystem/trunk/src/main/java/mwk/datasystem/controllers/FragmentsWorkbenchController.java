/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdLegacyCmpd;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdLegacyCmpdVO;
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

    private CmpdLegacyCmpdVO legacyCmpd;
    private CmpdVO cmpd;
    private List<CmpdFragmentVO> fragments;
    private CmpdFragmentVO selectedFragment;

    public String performLoadCmpd(Integer nsc) {

        this.fragments = new ArrayList<CmpdFragmentVO>();

        cmpd = HelperCmpd.fetchFullCmpd(nsc);
        
        legacyCmpd = HelperCmpdLegacyCmpd.getLegacyCmpdByNsc(nsc, null);

        fragments = new ArrayList<CmpdFragmentVO>(cmpd.getCmpdFragments());

        return "/webpages/fragmentsWorkbench?faces-redirect=true";

    }

    public void onRowEdit(RowEditEvent event) {

        FacesMessage msg = new FacesMessage("Fragment Edited", ((CmpdFragmentVO) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        HelperCmpd.updateCmpdFragment((CmpdFragmentVO) event.getObject());

    }

    public void onRowCancel(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("Edit Cancelled", ((CmpdFragmentVO) event.getObject()).getId().toString());
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

    public CmpdVO getCmpd() {
        return cmpd;
    }

    public void setCmpd(CmpdVO cmpd) {
        this.cmpd = cmpd;
    }

    public List<CmpdFragmentVO> getFragments() {
        return fragments;
    }

    public void setFragments(List<CmpdFragmentVO> fragments) {
        this.fragments = fragments;
    }

    public CmpdFragmentVO getSelectedFragment() {
        return selectedFragment;
    }

    public void setSelectedFragment(CmpdFragmentVO selectedFragment) {
        this.selectedFragment = selectedFragment;
    }

    public CmpdLegacyCmpdVO getLegacyCmpd() {
        return legacyCmpd;
    }

    public void setLegacyCmpd(CmpdLegacyCmpdVO legacyCmpd) {
        this.legacyCmpd = legacyCmpd;
    }

}
