/*
 
 
 
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import mwk.datasystem.util.HelperCuratedNsc;
import mwk.datasystem.vo.CuratedNameVO;
import mwk.datasystem.vo.CuratedNscVO;
import mwk.datasystem.vo.CuratedOriginatorVO;
import mwk.datasystem.vo.CuratedProjectVO;
import mwk.datasystem.vo.CuratedTargetVO;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class CuratedNscController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    private List<CuratedNscVO> curatedNscs;
    private CuratedNscVO selectedCuratedNsc;

    private List<CuratedNameVO> _knownNames;
    private List<CuratedOriginatorVO> _knownOriginators;
    private List<CuratedProjectVO> _knownProjects;
    private List<CuratedTargetVO> _knownTargets;

    private HashMap<String, CuratedNameVO> knownNamesMap;
    private HashMap<String, CuratedOriginatorVO> knownOriginatorsMap;
    private HashMap<String, CuratedProjectVO> knownProjectsMap;
    private HashMap<String, CuratedTargetVO> knownTargetsMap;

    @PostConstruct
    public void init() {

        loadCuratedNscs();

        loadCuratedNames();
        loadCuratedOriginators();
        loadCuratedProjects();
        loadCuratedTargets();

    }

    public CuratedNscController() {
        //init();
    }

    public void loadCuratedNscs() {
        curatedNscs = HelperCuratedNsc.loadAllCuratedNsc();
    }

    public void loadCuratedNames() {
        _knownNames = HelperCuratedNsc.loadAllNames();
        knownNamesMap = new HashMap<String, CuratedNameVO>();
        for (CuratedNameVO cnVO : _knownNames) {
            knownNamesMap.put(cnVO.getValue(), cnVO);
        }
    }

    public void loadCuratedOriginators() {
        _knownOriginators = HelperCuratedNsc.loadAllOriginators();
        knownOriginatorsMap = new HashMap<String, CuratedOriginatorVO>();
        for (CuratedOriginatorVO cnVO : _knownOriginators) {
            knownOriginatorsMap.put(cnVO.getValue(), cnVO);
        }
    }

    public void loadCuratedProjects() {
        _knownProjects = HelperCuratedNsc.loadAllProjects();
        knownProjectsMap = new HashMap<String, CuratedProjectVO>();
        for (CuratedProjectVO cnVO : _knownProjects) {
            knownProjectsMap.put(cnVO.getValue(), cnVO);
        }
    }

    public void loadCuratedTargets() {
        _knownTargets = HelperCuratedNsc.loadAllTargets();
        knownTargetsMap = new HashMap<String, CuratedTargetVO>();
        for (CuratedTargetVO cnVO : _knownTargets) {
            knownTargetsMap.put(cnVO.getValue(), cnVO);
        }
    }

    public List<CuratedNscVO> getCuratedNscs() {
        return curatedNscs;
    }

    public void setCuratedNscs(List<CuratedNscVO> curatedNscs) {
        this.curatedNscs = curatedNscs;
    }

    public CuratedNscVO getSelectedCuratedNsc() {
        return selectedCuratedNsc;
    }

    public void setSelectedCuratedNsc(CuratedNscVO selectedCuratedNsc) {
        this.selectedCuratedNsc = selectedCuratedNsc;
    }

    public HashMap<String, CuratedNameVO> getKnownNamesMap() {
        return knownNamesMap;
    }

    public void setKnownNamesMap(HashMap<String, CuratedNameVO> knownNamesMap) {
        this.knownNamesMap = knownNamesMap;
    }

    public HashMap<String, CuratedOriginatorVO> getKnownOriginatorsMap() {
        return knownOriginatorsMap;
    }

    public void setKnownOriginatorsMap(HashMap<String, CuratedOriginatorVO> knownOriginatorsMap) {
        this.knownOriginatorsMap = knownOriginatorsMap;
    }

    public HashMap<String, CuratedProjectVO> getKnownProjectsMap() {
        return knownProjectsMap;
    }

    public void setKnownProjectsMap(HashMap<String, CuratedProjectVO> knownProjectsMap) {
        this.knownProjectsMap = knownProjectsMap;
    }

    public HashMap<String, CuratedTargetVO> getKnownTargetsMap() {
        return knownTargetsMap;
    }

    public void setKnownTargetsMap(HashMap<String, CuratedTargetVO> knownTargetsMap) {
        this.knownTargetsMap = knownTargetsMap;
    }

}
