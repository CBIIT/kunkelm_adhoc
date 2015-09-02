/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.main;

import mwk.datasystem.util.TemplPropUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import mwk.datasystem.controllers.SearchCriteriaBean;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdVO;
import newstructureservlet.MoleculeWrangling;

/**
 *
 * @author mwkunkel
 */
public class Main {

    public static final Boolean DEBUG = Boolean.TRUE;

    public static void main(String[] args) {

        // testCtabFromSmiles();
        // testReflection();
        testTemplPropUtil();
        // testSplitting();

    }

    public static void testTemplPropUtil() {

        CmpdListMemberVO fake = new CmpdListMemberVO();

        ArrayList<String> ignList = new ArrayList<String>();
        ignList.add("id");
        ignList.add("serialVersionUID");
        
        ArrayList<String> reqList = new ArrayList<String>();
        reqList.add("name");
        reqList.add("cmpdFragmentPChem");        
        
        TemplPropUtil<CmpdListMemberVO> util = new TemplPropUtil<CmpdListMemberVO>(fake, ignList, reqList);

        if (DEBUG) {

            System.out.println("----------------strProps");

            for (String propertyName : util.getStrProps()) {
                System.out.println(propertyName + ": " + util.getStr(fake, propertyName));
            }

            System.out.println("----------------intProps");

            for (String propertyName : util.getIntProps()) {
                System.out.println(propertyName + ": " + util.getInt(fake, propertyName));
            }

            System.out.println("----------------dblProps");

            for (String propertyName : util.getDblProps()) {
                System.out.println(propertyName + ": " + util.getDbl(fake, propertyName));
            }

            System.out.println("----------------longProps");

            for (String propertyName : util.getLongProps()) {
                System.out.println(propertyName + ": " + util.getLong(fake, propertyName));
            }

            System.out.println("----------------boolProps");

            for (String propertyName : util.getBoolProps()) {
                System.out.println(propertyName + ": " + util.getBool(fake, propertyName));
            }

            System.out.println("----------------othProps");

            for (String propertyName : util.getOthProps()) {
                System.out.println(propertyName);
            }

        }
        
        util.printUniqProps();

//        Create object to test        
//        CmpdListMemberVO clmVO = new CmpdListMemberVO();
//        CmpdVO cVO = new CmpdVO();
//        cVO.setName("fakeName");
//        clmVO.setCmpd(cVO);
//
//        CmpdFragmentPChemVO pChemVO = new CmpdFragmentPChemVO();
//        pChemVO.setSurfaceArea(-101.01);
//
//        CmpdFragmentVO fragVO = new CmpdFragmentVO();
//        fragVO.setCmpdFragmentPChem(pChemVO);
//
//        cVO.setParentFragment(fragVO);
//
//        // retrieve
//        String propertyString = "";
//
//        propertyString = "cmpd.name";
//        Object rtn = util.get(clmVO, propertyString);
//        if (rtn != null) {
//            System.out.println(propertyString + " rtn is: " + rtn.getClass().getName() + " toString:" + rtn.toString());
//        } else {
//            System.out.println(propertyString + " rtn is null");
//        }
//
//        propertyString = "cmpd.parentFragment.cmpdFragmentPChem.surfaceArea";
//        rtn = util.get(clmVO, propertyString);
//        if (rtn != null) {
//            System.out.println(propertyString + " rtn is: " + rtn.getClass().getName() + " toString:" + rtn.toString());
//        } else {
//            System.out.println(propertyString + " rtn is null");
//        }
//
//        propertyString = "cmpd.parentFragment.cmpdFragmentStructure.surfaceArea";
//        rtn = util.get(clmVO, propertyString);
//        if (rtn != null) {
//            System.out.println(propertyString + " rtn is: " + rtn.getClass().getName() + " toString:" + rtn.toString());
//        } else {
//            System.out.println(propertyString + " rtn is null");
//        }
    }

    public static void testCtabFromSmiles() {

        String smiles = "CCCCCC";
        String ctab = MoleculeWrangling.toCtabFromSmiles(smiles);
        System.out.println(ctab);

    }

    public static void testReflection() {

        String[] textAreaFieldNames = new String[]{
            "drugNameTextArea",
            "aliasTextArea",
            "casTextArea",
            "cmpdNamedSetTextArea",
            "nscTextArea",
            "projectCodeTextArea",
            "plateTextArea",
            "targetTextArea",
            "mtxtTextArea",
            "pseudoAtomsTextArea"
        };

        String[] listFieldNames = new String[]{
            "drugNames",
            "aliases",
            "cases",
            "cmpdNamedSets",
            "nscs",
            "projectCodes",
            "plates",
            "targets",
            "mtxtPieces",
            "pseudoAtomsPieces"
        };

        SearchCriteriaBean scb = new SearchCriteriaBean();
        scb.setNscTextArea("740 123127 401005 705701 743380");

        try {

            Class scbClass = scb.getClass();

            for (String textAreaFieldName : Arrays.asList(textAreaFieldNames)) {

                Field f = scbClass.getDeclaredField(textAreaFieldName);
                f.setAccessible(true);

                System.out.println();
                System.out.println("fieldName: " + f.getName());
                System.out.println("fieldType: " + f.getType().getName());

                String str = (String) f.get(scb);

                System.out.println(textAreaFieldName);
                System.out.println(str);
            }

            for (String textAreaFieldName : Arrays.asList(textAreaFieldNames)) {

                Field f = scbClass.getDeclaredField(textAreaFieldName);
                f.setAccessible(true);

                System.out.println();
                System.out.println("fieldName: " + f.getName());
                System.out.println("fieldType: " + f.getType().getName());

                String str = (String) f.get(scb);

                System.out.println(textAreaFieldName);
                System.out.println(str);
            }

            for (String listFieldName : Arrays.asList(listFieldNames)) {

                Field f = scbClass.getDeclaredField(listFieldName);
                f.setAccessible(true);

                System.out.println();
                System.out.println("fieldName: " + f.getName());
                System.out.println("fieldType: " + f.getType().getName());

                ArrayList<String> al = (ArrayList< String>) f.get(scb);
                System.out.println(listFieldName);
                for (String s : al) {
                    System.out.println(s);
                }
            }

            // production code should handle these exceptions more gracefully
        } catch (NoSuchFieldException x) {
            x.printStackTrace();
        } catch (IllegalAccessException x) {
            x.printStackTrace();
        }

    }

    public static void testSplitting() {

        //String testStr = "cmpdFragmentStructurecmpd.parentFragment.cmpdFragmentStructure.serialVersionUID";
        String testStr = "cmpd.serialVersionUID";

        System.out.println(testStr);

        for (int tryCnt = 1; tryCnt < 10; tryCnt++) {

            String tryStr = "";

            String[] strArr = testStr.split("\\.");

            System.out.println("strArr.length: " + strArr.length + " tryCnt: " + tryCnt);

            int startIdx = strArr.length > tryCnt ? strArr.length - tryCnt : 0;

            //int startIdx = strArr.length - tryCnt;
            
            String[] tryArr = Arrays.copyOfRange(strArr, startIdx, strArr.length);
            tryStr = join(".", tryArr);

            System.out.println(tryStr);

        }

    }

    private static String join(String sep, String[] strArr) {

        StringBuilder sb = new StringBuilder();
        boolean isFirst = true;

        for (String s : strArr) {
            if (isFirst) {
                sb.append(s);
                isFirst = false;
            } else {
                sb.append(sep);
                sb.append(s);
            }
        }

        return sb.toString();
    }

}
