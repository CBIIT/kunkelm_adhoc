/*
 
 
 
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import mwk.datasystem.util.HelperCuratedNsc;
import mwk.datasystem.vo.CuratedNameVO;
import mwk.datasystem.vo.CuratedNscVO;
import mwk.datasystem.vo.CuratedOriginatorVO;
import mwk.datasystem.vo.CuratedProjectVO;
import mwk.datasystem.vo.CuratedTargetVO;
import org.apache.commons.lang.StringUtils;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class CuratedNscWorkbenchController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    private CuratedNameVO preferredName;
    private CuratedNameVO genericName;
    private List<CuratedNameVO> aliases;
    private CuratedOriginatorVO originator;
    private List<CuratedProjectVO> projects;
    private CuratedTargetVO primaryTarget;
    private List<CuratedTargetVO> secondaryTargets;

    private CuratedNscVO selectedCuratedNsc;

    private CuratedNameVO newCuratedName;
    private CuratedOriginatorVO newCuratedOriginator;
    private CuratedProjectVO newCuratedProject;
    private CuratedTargetVO newCuratedTarget;

    // reach-through to the CuratedNscController
    @ManagedProperty(value = "#{curatedNscController}")
    private CuratedNscController curatedNscController;

    public void setCuratedNscController(CuratedNscController curatedNscController) {
        this.curatedNscController = curatedNscController;
    }

    public CuratedNscWorkbenchController() {

    }

    @PostConstruct
    public void init() {
        preferredName = null; //new CuratedNameVO();
        genericName = null; //new CuratedNameVO();
        aliases = new ArrayList<CuratedNameVO>();
        originator = null; //new CuratedOriginatorVO();
        projects = new ArrayList<CuratedProjectVO>();
        primaryTarget = null; //new CuratedTargetVO();
        secondaryTargets = new ArrayList<CuratedTargetVO>();

        newCuratedName = new CuratedNameVO();
        newCuratedOriginator = new CuratedOriginatorVO();
        newCuratedProject = new CuratedProjectVO();
        newCuratedTarget = new CuratedTargetVO();
    }

    public void loadCuratedNsc() {
        preferredName = selectedCuratedNsc.getPreferredName();
        genericName = selectedCuratedNsc.getGenericName();
        aliases = selectedCuratedNsc.getAliases();
        originator = selectedCuratedNsc.getOriginator();
        projects = selectedCuratedNsc.getProjects();
        primaryTarget = selectedCuratedNsc.getPrimaryTarget();
        secondaryTargets = selectedCuratedNsc.getSecondaryTargets();
    }

    public void clearNew() {
        newCuratedName = new CuratedNameVO();
        newCuratedOriginator = new CuratedOriginatorVO();
        newCuratedProject = new CuratedProjectVO();
        newCuratedTarget = new CuratedTargetVO();
    }
    
    public String createNewCuratedName() {

        CuratedNameVO cnVO = HelperCuratedNsc.createNewCuratedName(newCuratedName);

        System.out.println("After createNewCuratedName in CuratedNscWorkbenchController");
        if (cnVO != null) {
            System.out.println(cnVO);
            // reload         
            curatedNscController.loadCuratedNames();
        } else {
            System.out.println("null return from createNewCuratedName");
        }

        return "/webpages/curatedNscWorkbench?faces-redirect=true";

    }
    
     public String createNewCuratedOriginator() {

        CuratedOriginatorVO cnVO = HelperCuratedNsc.createNewCuratedOriginator(newCuratedOriginator);

        System.out.println("After createNewCuratedOriginator in CuratedNscWorkbenchController");
        if (cnVO != null) {
            System.out.println(cnVO);
            // reload         
            curatedNscController.loadCuratedOriginators();
        } else {
            System.out.println("null return from createNewCuratedOriginator");
        }

        return "/webpages/curatedNscWorkbench?faces-redirect=true";

    }
     
      public String createNewCuratedProject() {

        CuratedProjectVO cnVO = HelperCuratedNsc.createNewCuratedProject(newCuratedProject);

        System.out.println("After createNewCuratedProject in CuratedNscWorkbenchController");
        if (cnVO != null) {
            System.out.println(cnVO);
            // reload         
            curatedNscController.loadCuratedProjects();
        } else {
            System.out.println("null return from createNewCuratedProject");
        }

        return "/webpages/curatedNscWorkbench?faces-redirect=true";

    }
      
       public String createNewCuratedTarget() {

        CuratedTargetVO cnVO = HelperCuratedNsc.createNewCuratedTarget(newCuratedTarget);

        System.out.println("After createNewCuratedTarget in CuratedNscWorkbenchController");
        if (cnVO != null) {
            System.out.println(cnVO);
            // reload         
            curatedNscController.loadCuratedTargets();
        } else {
            System.out.println("null return from createNewCuratedTarget");
        }

        return "/webpages/curatedNscWorkbench?faces-redirect=true";

    }

    //<editor-fold defaultstate="collapsed" desc="APPEND/REMOVE">
    public void appendPreferredName(SelectEvent event) {
        Object item = event.getObject();
        CuratedNameVO clVO = (CuratedNameVO) item;
        preferredName = clVO;
    }

    public void removePreferredName() {
        preferredName = null;
    }

    public void appendGenericName(SelectEvent event) {
        Object item = event.getObject();
        CuratedNameVO clVO = (CuratedNameVO) item;
        genericName = clVO;
    }

    public void removeGenericName() {
        genericName = null;
    }

    public void appendAlias(SelectEvent event) {
        Object item = event.getObject();
        CuratedNameVO clVO = (CuratedNameVO) item;
        if (!aliases.contains(clVO)) {
            aliases.add(clVO);
        }
    }

    public void removeAlias(CuratedNameVO ali) {
        if (aliases.contains(ali)) {
            aliases.remove(ali);
        }
    }

    public void appendOriginator(SelectEvent event) {
        Object item = event.getObject();
        CuratedOriginatorVO coVO = (CuratedOriginatorVO) item;
        originator = coVO;
    }

    public void removeOriginator() {
        originator = null;
    }

    public void appendProject(SelectEvent event) {
        Object item = event.getObject();
        CuratedProjectVO cpVO = (CuratedProjectVO) item;
        if (!projects.contains(cpVO)) {
            projects.add(cpVO);
        }
    }

    public void removeProject(CuratedProjectVO proj) {
        if (projects.contains(proj)) {
            projects.remove(proj);
        }
    }

    public void appendPrimaryTarget(SelectEvent event) {
        Object item = event.getObject();
        CuratedTargetVO ctVO = (CuratedTargetVO) item;
        primaryTarget = ctVO;
    }

    public void removePrimaryTarget() {
        primaryTarget = null;
    }

    public void appendSecondaryTarget(SelectEvent event) {
        Object item = event.getObject();
        CuratedTargetVO clVO = (CuratedTargetVO) item;
        if (!secondaryTargets.contains(clVO)) {
            secondaryTargets.add(clVO);
        }
    }

    public void removeSecondaryTarget(CuratedTargetVO sec) {
        if (secondaryTargets.contains(sec)) {
            secondaryTargets.remove(sec);
        }
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="COMPLETE">
    public List<CuratedNameVO> completeCuratedName(String query) {
        List<CuratedNameVO> suggestions = new ArrayList<CuratedNameVO>();
        for (CuratedNameVO cnVO : curatedNscController.getKnownNamesMap().values()) {
            if (StringUtils.containsIgnoreCase(cnVO.getValue(), query.trim())) {
                suggestions.add(cnVO);
            }
        }
        return suggestions;
    }

    public List<CuratedOriginatorVO> completeCuratedOriginator(String query) {
        List<CuratedOriginatorVO> suggestions = new ArrayList<CuratedOriginatorVO>();
        for (CuratedOriginatorVO cnVO : curatedNscController.getKnownOriginatorsMap().values()) {
            if (StringUtils.containsIgnoreCase(cnVO.getValue(), query.trim())) {
                suggestions.add(cnVO);
            }
        }
        return suggestions;
    }

    public List<CuratedProjectVO> completeCuratedProject(String query) {
        List<CuratedProjectVO> suggestions = new ArrayList<CuratedProjectVO>();
        for (CuratedProjectVO cnVO : curatedNscController.getKnownProjectsMap().values()) {
            if (StringUtils.containsIgnoreCase(cnVO.getValue(), query.trim())) {
                suggestions.add(cnVO);
            }
        }
        return suggestions;
    }

    public List<CuratedTargetVO> completeCuratedTarget(String query) {
        List<CuratedTargetVO> suggestions = new ArrayList<CuratedTargetVO>();
        for (CuratedTargetVO cnVO : curatedNscController.getKnownTargetsMap().values()) {
            if (StringUtils.containsIgnoreCase(cnVO.getValue(), query.trim())) {
                suggestions.add(cnVO);
            }
        }
        return suggestions;
    }

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public CuratedNameVO getPreferredName() {
        return preferredName;
    }

    public void setPreferredName(CuratedNameVO preferredName) {
        this.preferredName = preferredName;
    }

    public CuratedNameVO getGenericName() {
        return genericName;
    }

    public void setGenericName(CuratedNameVO genericName) {
        this.genericName = genericName;
    }

    public List<CuratedNameVO> getAliases() {
        return aliases;
    }

    public void setAliases(List<CuratedNameVO> aliases) {
        this.aliases = aliases;
    }

    public CuratedOriginatorVO getOriginator() {
        return originator;
    }

    public void setOriginator(CuratedOriginatorVO originator) {
        this.originator = originator;
    }

    public List<CuratedProjectVO> getProjects() {
        return projects;
    }

    public void setProjects(List<CuratedProjectVO> projects) {
        this.projects = projects;
    }

    public CuratedTargetVO getPrimaryTarget() {
        return primaryTarget;
    }

    public void setPrimaryTarget(CuratedTargetVO primaryTarget) {
        this.primaryTarget = primaryTarget;
    }

    public List<CuratedTargetVO> getSecondaryTargets() {
        return secondaryTargets;
    }

    public void setSecondaryTargets(List<CuratedTargetVO> secondaryTargets) {
        this.secondaryTargets = secondaryTargets;
    }

    public CuratedNscVO getSelectedCuratedNsc() {
        return selectedCuratedNsc;
    }

    public void setSelectedCuratedNsc(CuratedNscVO selectedCuratedNsc) {
        this.selectedCuratedNsc = selectedCuratedNsc;
    }

    public CuratedNameVO getNewCuratedName() {
        return newCuratedName;
    }

    public void setNewCuratedName(CuratedNameVO newCuratedName) {
        this.newCuratedName = newCuratedName;
    }

    public CuratedOriginatorVO getNewCuratedOriginator() {
        return newCuratedOriginator;
    }

    public void setNewCuratedOriginator(CuratedOriginatorVO newCuratedOriginator) {
        this.newCuratedOriginator = newCuratedOriginator;
    }

    public CuratedProjectVO getNewCuratedProject() {
        return newCuratedProject;
    }

    public void setNewCuratedProject(CuratedProjectVO newCuratedProject) {
        this.newCuratedProject = newCuratedProject;
    }

    public CuratedTargetVO getNewCuratedTarget() {
        return newCuratedTarget;
    }

    public void setNewCuratedTarget(CuratedTargetVO newCuratedTarget) {
        this.newCuratedTarget = newCuratedTarget;
    }

//</editor-fold>
}
