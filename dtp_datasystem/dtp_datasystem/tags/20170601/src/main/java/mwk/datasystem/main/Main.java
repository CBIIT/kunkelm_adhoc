/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.main;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mwk.datasystem.controllers.SearchCriteriaBean;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCuratedNsc;
import mwk.datasystem.util.HelperStructure;
import mwk.datasystem.util.HelperTanimoto;
import mwk.datasystem.util.TemplPropUtil;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.CuratedNameVO;
import mwk.datasystem.vo.CuratedNscVO;
import mwk.datasystem.vo.CuratedOriginatorVO;
import mwk.datasystem.vo.CuratedProjectVO;
import mwk.datasystem.vo.CuratedTargetVO;
import mwk.datasystem.vo.TanimotoScoresVO;
import mwk.datasystem.vo.TanimotoScoresWithCmpdObjectsVO;

/**
 *
 * @author mwkunkel
 */
public class Main {

    public static final Boolean DEBUG = Boolean.TRUE;

    public static void main(String[] args) {
        testSubstructureSearch();
    }

    public static void tanimoto() {

        Integer[] intArr = new Integer[]{740, 750, 752, 755, 762, 1390, 3053, 3088, 6396, 8806};

        ArrayList<Integer> nscIntList = new ArrayList<Integer>(Arrays.asList(intArr));

        List<CmpdVO> cVOlist = HelperCmpd.getCmpdsByNsc(nscIntList, "PUBLIC");
        ArrayList<CmpdVO> cl = new ArrayList<CmpdVO>(cVOlist);

        ArrayList<TanimotoScoresWithCmpdObjectsVO> resList = HelperTanimoto.doTanimoto(cl);

        System.out.println("resList.size(): " + resList.size());

        for (TanimotoScoresWithCmpdObjectsVO tswcoVO : resList) {
            System.out.println("nsc1: " + tswcoVO.getCmpd1().getNsc() + " nsc2: " + tswcoVO.getCmpd2().getNsc() + " atompairbv_fp" + tswcoVO.getAtomPairBv());
        }

    }

    public static void testSubstructureSearch() {

        String str743380 = "c1cn(Cc2ccccc2)c3ccccc13";
        String str163027 = "OC1C2C(CC1(C)C)C(=O)C34OC3C(=O)C5(CO5)C24C";

        List<Integer> nscIntList = HelperStructure.findNSCsBySmilesSubstructure(str163027);
        System.out.println("nscIntList.size(): " + nscIntList.size());

    }

    public static void testTemplatedPropertyUtility() {

        String[] ignArr = new String[]{"id", "serialVersionUID"};
        ArrayList<String> ignList = new ArrayList<String>(Arrays.asList(ignArr));

        TemplPropUtil<CmpdVO> tpo = new TemplPropUtil<CmpdVO>(new CmpdVO(), ignList);

        tpo.printUniqProps();

    }

    public static void testUpdateCuratedNsc() {

        CuratedNscVO valObj = new CuratedNscVO();

        valObj.setId(723l);
        valObj.setNsc(10101010);
        valObj.setCas("MWKchanged");

        CuratedNameVO cnamVO = new CuratedNameVO();
        cnamVO.setId(75l);
        valObj.setPreferredName(cnamVO);

        valObj.setGenericName(cnamVO);

        ArrayList<CuratedNameVO> aliases = new ArrayList<CuratedNameVO>();
        for (long i = 50; i < 80; i++) {
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

        CuratedTargetVO prim = new CuratedTargetVO();
        prim.setId(35l);
        valObj.setPrimaryTarget(prim);

        List<CuratedTargetVO> targets = new ArrayList<CuratedTargetVO>();
        for (long i = 35; i < 75; i++) {
            CuratedTargetVO ctVO = new CuratedTargetVO();
            ctVO.setId(i);
            targets.add(ctVO);
        }
        valObj.setSecondaryTargets(targets);

        HelperCuratedNsc.updateCuratedNsc(valObj);

    }

    public static void testDeleteCuratedNsc() {

        CuratedNscVO valObj = new CuratedNscVO();

        valObj.setId(723l);

        HelperCuratedNsc.deleteCuratedNsc(valObj);

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
        ArrayList<TanimotoScoresVO> scores = HelperTanimoto.fetch();
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

    private static void printFieldsAndMethods(Class c) {

        System.out.println("Fields from : " + c);
        Field[] fields = c.getDeclaredFields();
        if (fields.length != 0) {
            for (Field f : fields) {
                System.out.println("  Field: " + f.toGenericString());
            }
        } else {
            System.out.println("  -- no fields --");
        }
        System.out.println();

        System.out.println("Methods from : " + c);
        Method[] meths = c.getDeclaredMethods();
        if (meths.length != 0) {
            for (Method m : meths) {
                System.out.println("  Method: " + m.toGenericString());
            }
        } else {
            System.out.println("  -- no methods --");
        }
        System.out.println();

    }

}
