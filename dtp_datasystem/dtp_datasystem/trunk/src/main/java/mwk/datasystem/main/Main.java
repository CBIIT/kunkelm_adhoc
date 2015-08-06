/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
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

    }

    public static void testTemplPropUtil() {

        CmpdListMemberVO fake = new CmpdListMemberVO();

        TemplPropUtil<CmpdListMemberVO> util = new TemplPropUtil<CmpdListMemberVO>(fake);

        if (DEBUG) {

            System.out.println("----------------knownStringProperties");

            for (String propertyName : util.knownStringProperties) {
                System.out.println(propertyName + ": " + util.getStringProperty(fake, propertyName));
            }

            System.out.println("----------------knownIntegerProperties");

            for (String propertyName : util.knownIntegerProperties) {
                System.out.println(propertyName + ": " + util.getIntegerProperty(fake, propertyName));
            }

            System.out.println("----------------knownDoubleProperties");

            for (String propertyName : util.knownDoubleProperties) {
                System.out.println(propertyName + ": " + util.getDoubleProperty(fake, propertyName));
            }

            System.out.println("----------------unmanagedProperties");

            for (String propertyName : util.unmanagedProperties) {
                System.out.println(propertyName);
            }
        }

        CmpdListMemberVO clmVO = new CmpdListMemberVO();
        CmpdVO cVO = new CmpdVO();
        cVO.setName("fakeName");
        clmVO.setCmpd(cVO);

        CmpdFragmentPChemVO pChemVO = new CmpdFragmentPChemVO();
        pChemVO.setSurfaceArea(-101.01);

        CmpdFragmentVO fragVO = new CmpdFragmentVO();
        fragVO.setCmpdFragmentPChem(pChemVO);

        cVO.setParentFragment(fragVO);

        // retrieve
        String propertyString = "";

        propertyString = "cmpd.name";
        Object rtn = util.get(clmVO, propertyString);
        if (rtn != null) {
            System.out.println(propertyString + " rtn is: " + rtn.getClass().getName() + " toString:" + rtn.toString());
        } else {
            System.out.println(propertyString + " rtn is null");
        }

        propertyString = "cmpd.parentFragment.cmpdFragmentPChem.surfaceArea";
        rtn = util.get(clmVO, propertyString);
        if (rtn != null) {
            System.out.println(propertyString + " rtn is: " + rtn.getClass().getName() + " toString:" + rtn.toString());
        } else {
            System.out.println(propertyString + " rtn is null");
        }

        propertyString = "cmpd.parentFragment.cmpdFragmentStructure.surfaceArea";
        rtn = util.get(clmVO, propertyString);
        if (rtn != null) {
            System.out.println(propertyString + " rtn is: " + rtn.getClass().getName() + " toString:" + rtn.toString());
        } else {
            System.out.println(propertyString + " rtn is null");
        }

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

}
