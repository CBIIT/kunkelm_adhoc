/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.main;

import java.util.ArrayList;
import java.util.List;
import mwk.datasystem.controllers.SearchCriteriaBean;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCuratedNsc;
import mwk.datasystem.util.HelperTanimotoScores;
import mwk.datasystem.vo.CuratedNameVO;
import mwk.datasystem.vo.CuratedNscVO;
import mwk.datasystem.vo.CuratedOriginatorVO;
import mwk.datasystem.vo.CuratedProjectVO;
import mwk.datasystem.vo.CuratedTargetVO;
import mwk.datasystem.vo.TanimotoScoresVO;

/**
 *
 * @author mwkunkel
 */
public class Main {

    public static final Boolean DEBUG = Boolean.TRUE;

    public static void main(String[] args) {
        //testTanimotoScores();
        // testSearchCriteriaBean();
        // testCuratedStuff();        
        //testCreateNewName();
        
        testUpdateCuratedNsc();

    }

    public static void testUpdateCuratedNsc() {

        CuratedNscVO valObj = new CuratedNscVO();
        
        valObj.setId(671l);
        valObj.setNsc(10000000);
        valObj.setCas("MWK");
        
        CuratedNameVO cnamVO = new CuratedNameVO();
        cnamVO.setId(75l);                
        valObj.setPreferredName(cnamVO);
        
        valObj.setGenericName(cnamVO);
        
        ArrayList<CuratedNameVO> aliases = new ArrayList<CuratedNameVO>();
        for (long i = 50; i < 80; i++){
            cnamVO = new CuratedNameVO();
            cnamVO.setId(i);
            aliases.add(cnamVO);
        }
        valObj.setAliases(aliases);
        
        CuratedOriginatorVO coVO = new CuratedOriginatorVO();
        coVO.setId(45l);
        valObj.setOriginator(coVO);
        
        List<CuratedProjectVO> projects = new ArrayList<CuratedProjectVO>();
        CuratedProjectVO cpVO = new CuratedProjectVO();
        cpVO.setId(2l);
        projects.add(cpVO);
        valObj.setProjects(projects);
                
        CuratedTargetVO prim=new CuratedTargetVO();
        prim.setId(35l);
        valObj.setPrimaryTarget(prim);
        
        List<CuratedTargetVO> targets = new ArrayList<CuratedTargetVO>();
        for (long i = 35; i < 75; i++){
            CuratedTargetVO ctVO = new CuratedTargetVO();
            ctVO.setId(i);
            targets.add(ctVO);
        }
        valObj.setSecondaryTargets(targets);
        
        
        HelperCuratedNsc.updateCuratedNsc(valObj);

    }

    public static void testCreateNewName() {

        CuratedNameVO newName = new CuratedNameVO();

        newName.setValue("MWK");
        newName.setDescription("MWK desc");
        newName.setReference("MWK ref");

        CuratedNameVO rtn = HelperCuratedNsc.createNewCuratedName(newName);

        System.out.println(rtn.toString());

    }

    public static void testCuratedStuff() {

        List<CuratedNscVO> NscList = HelperCuratedNsc.loadAllCuratedNsc();
        System.out.println("Size of NscList: " + NscList.size());

        for (CuratedNscVO cnVO : NscList) {
            System.out.println("NSC: " + cnVO.getNsc() + " size of aliases: " + cnVO.getAliases().size());

        }

//        List<CuratedNameVO> NameList = HelperCuratedNsc.loadAllNames();
//        System.out.println("Size of NameList: " + NameList.size());
//
//        List<CuratedOriginatorVO> OriginatorList = HelperCuratedNsc.loadAllOriginators();
//        System.out.println("Size of OriginatorList: " + OriginatorList.size());
//
//        List<CuratedProjectVO> ProjectList = HelperCuratedNsc.loadAllProjects();
//        System.out.println("Size of ProjectList: " + ProjectList.size());
//
//        List<CuratedTargetVO> TargetList = HelperCuratedNsc.loadAllTargets();
//        System.out.println("Size of TargetList: " + TargetList.size());
    }

    public static void testTanimotoScores() {
        ArrayList<TanimotoScoresVO> scores = HelperTanimotoScores.fetch();
        System.out.println("scores.size(): " + scores.size());
    }

    public static void testSearchCriteriaBean() {

        SearchCriteriaBean scb = new SearchCriteriaBean();
        scb.setProjectCodeTextArea("DTP103 DTP110");

        ArrayList<String> projectList = new ArrayList<String>();
        projectList.add("DTP-103");
        projectList.add("DTP-110");

        scb.setMin_molecularWeight(150d);
        scb.setMax_molecularWeight(250d);

        HelperCmpd.createCmpdListFromSearchCriteriaBean("testList", scb, null, "DTP_kunkelm");

    }

}
