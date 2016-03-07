/*
 
 
 
 */
package mwk.datasystem.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
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

    private CuratedNscVO ref;
    private CuratedNscVO work;
    private Boolean changesMade;

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
        ref = new CuratedNscVO();
        work = new CuratedNscVO();

        newCuratedName = new CuratedNameVO();
        newCuratedOriginator = new CuratedOriginatorVO();
        newCuratedProject = new CuratedProjectVO();
        newCuratedTarget = new CuratedTargetVO();
    }

    public static Object deepCopy(Object orig) {
        Object obj = null;
        try {
            // Write the object out to a byte array
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(bos);
            out.writeObject(orig);
            out.flush();
            out.close();

            // Make an input stream from the byte array and read
            // a copy of the object back in.
            ObjectInputStream in = new ObjectInputStream(
                    new ByteArrayInputStream(bos.toByteArray()));
            obj = in.readObject();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        }
        return obj;
    }

    public String performConfigureEdit(CuratedNscVO cnVO) {

        changesMade = Boolean.FALSE;

        ref = cnVO;

        // DEEP COPY!         
        work = (CuratedNscVO) deepCopy(ref);

        return "/webpages/curatedNscWorkbench?faces-redirect=true";
    }

    public String performUpdate() {

        if (changesMade) {
            HelperCuratedNsc.updateCuratedNsc(work);
        }
        
        curatedNscController.loadCuratedNscs();

        return "/webpages/curatedNscTable?faces-redirect=true";

    }

    //<editor-fold defaultstate="collapsed" desc="CreateNewXxx Methods">
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

//</editor-fold>
    //<editor-fold defaultstate="collapsed" desc="APPEND/REMOVE">
    public void appendPreferredName(SelectEvent event) {
        changesMade = Boolean.TRUE;
        Object item = event.getObject();
        CuratedNameVO clVO = (CuratedNameVO) item;
        work.setPreferredName(clVO);
    }

    public void removePreferredName() {
        changesMade = Boolean.TRUE;
        work.setPreferredName(null);
    }

    public void appendGenericName(SelectEvent event) {
        changesMade = Boolean.TRUE;
        Object item = event.getObject();
        CuratedNameVO clVO = (CuratedNameVO) item;
        work.setGenericName(clVO);
    }

    public void removeGenericName() {
        changesMade = Boolean.TRUE;
        work.setGenericName(null);
    }

    public void appendAlias(SelectEvent event) {
        changesMade = Boolean.TRUE;
        Object item = event.getObject();
        CuratedNameVO clVO = (CuratedNameVO) item;
        if (!work.getAliases().contains(clVO)) {
            work.getAliases().add(clVO);
        }
    }

    public void removeAlias(CuratedNameVO cnVO) {
        changesMade = Boolean.TRUE;
        if (work.getAliases().contains(cnVO)) {
            work.getAliases().remove(cnVO);
        }
    }

    public void appendOriginator(SelectEvent event) {
        changesMade = Boolean.TRUE;
        Object item = event.getObject();
        CuratedOriginatorVO coVO = (CuratedOriginatorVO) item;
        work.setOriginator(coVO);
    }

    public void removeOriginator() {
        changesMade = Boolean.TRUE;
        work.setOriginator(null);
    }

    public void appendProject(SelectEvent event) {
        changesMade = Boolean.TRUE;
        Object item = event.getObject();
        CuratedProjectVO cpVO = (CuratedProjectVO) item;
        if (!work.getProjects().contains(cpVO)) {
            work.getProjects().add(cpVO);
        }
    }

    public void removeProject(CuratedProjectVO cpVO) {
        changesMade = Boolean.TRUE;
        if (work.getProjects().contains(cpVO)) {
            work.getProjects().remove(cpVO);
        }
    }

    public void appendPrimaryTarget(SelectEvent event) {
        changesMade = Boolean.TRUE;
        Object item = event.getObject();
        CuratedTargetVO ctVO = (CuratedTargetVO) item;
        work.setPrimaryTarget(ctVO);
    }

    public void removePrimaryTarget() {
        changesMade = Boolean.TRUE;
        work.setPrimaryTarget(null);
    }

    public void appendSecondaryTarget(SelectEvent event) {
        changesMade = Boolean.TRUE;
        Object item = event.getObject();
        CuratedTargetVO clVO = (CuratedTargetVO) item;
        if (!work.getSecondaryTargets().contains(clVO)) {
            work.getSecondaryTargets().add(clVO);
        }
    }

    public void removeSecondaryTarget(CuratedTargetVO ctVO) {
        changesMade = Boolean.TRUE;
        if (work.getSecondaryTargets().contains(ctVO)) {
            work.getSecondaryTargets().remove(ctVO);
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
    public CuratedNscVO getRef() {
        return ref;
    }

    public void setRef(CuratedNscVO ref) {
        this.ref = ref;
    }

    public CuratedNscVO getWork() {
        return work;
    }

    public void setWork(CuratedNscVO work) {
        this.work = work;
    }

    public Boolean getChangesMade() {
        return changesMade;
    }

    public void setChangesMade(Boolean changesMade) {
        this.changesMade = changesMade;
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
