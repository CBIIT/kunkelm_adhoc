/*
 
 
 
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import mwk.datasystem.util.HelperCmpdKnownSalt;
import mwk.datasystem.util.HelperCuratedNsc;
import mwk.datasystem.vo.CmpdKnownSaltVO;
import mwk.datasystem.vo.CuratedNameVO;
import mwk.datasystem.vo.CuratedNscVO;
import mwk.datasystem.vo.CuratedOriginatorVO;
import mwk.datasystem.vo.CuratedProjectVO;
import mwk.datasystem.vo.CuratedTargetVO;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class CuratedNscController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    private List<CuratedNscVO> curatedNscs;
    private List<CuratedNameVO> names;
    private List<CuratedOriginatorVO> originators;
    private List<CuratedProjectVO> projects;
    private List<CuratedTargetVO> targets;

    @PostConstruct
    public void init() {
        curatedNscs = HelperCuratedNsc.loadAllCuratedNsc();
        names = HelperCuratedNsc.loadAllNames();
        originators = HelperCuratedNsc.loadAllOriginators();
        projects = HelperCuratedNsc.loadAllProjects();
        targets = HelperCuratedNsc.loadAllTargets();
    }

    public CuratedNscController() {
        init();
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

    public List<CuratedNscVO> getCuratedNscs() {
        return curatedNscs;
    }

    public void setCuratedNscs(List<CuratedNscVO> curatedNscs) {
        this.curatedNscs = curatedNscs;
    }

    public List<CuratedNameVO> getNames() {
        return names;
    }

    public void setNames(List<CuratedNameVO> names) {
        this.names = names;
    }

    public List<CuratedOriginatorVO> getOriginators() {
        return originators;
    }

    public void setOriginators(List<CuratedOriginatorVO> originators) {
        this.originators = originators;
    }

    public List<CuratedProjectVO> getProjects() {
        return projects;
    }

    public void setProjects(List<CuratedProjectVO> projects) {
        this.projects = projects;
    }

    public List<CuratedTargetVO> getTargets() {
        return targets;
    }

    public void setTargets(List<CuratedTargetVO> targets) {
        this.targets = targets;
    }

}
