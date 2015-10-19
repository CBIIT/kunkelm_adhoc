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
import mwk.datasystem.util.HelperTanimotoScores;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.TanimotoScoresVO;
import newstructureservlet.MoleculeWrangling;

/**
 *
 * @author mwkunkel
 */
public class Main {

    public static final Boolean DEBUG = Boolean.TRUE;

    public static void main(String[] args) {

        testTanimotoScores();
        
    }

    public static void testTanimotoScores(){        
        ArrayList<TanimotoScoresVO> scores = HelperTanimotoScores.fetch();        
        System.out.println("scores.size(): " + scores.size());
    }
 

}
