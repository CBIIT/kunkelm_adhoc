/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import com.flaptor.hist4j.AdaptiveHistogram;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import mwk.datasystem.mwkcharting.Histogram;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;

/**
 *
 * @author mwkunkel
 */
public class HistogramChartUtil {

  public static final Boolean DEBUG = Boolean.FALSE;

  public static List<Histogram> doHistograms(Collection<CmpdListMemberVO> incoming, List<String> propertyNameList) {

    if (DEBUG) {
      System.out.println("In HistogramChartUtil.doHistograms()");
      System.out.println("Size of incomding: " + incoming.size());
    }

    ArrayList<Histogram> rtn = new ArrayList<Histogram>();

    // Histograms looked up by property
    HashMap<String, AdaptiveHistogram> ahMap = new HashMap<String, AdaptiveHistogram>();

    // initialize the ah map
    for (String propertyName : propertyNameList) {
      ahMap.put(propertyName, new AdaptiveHistogram());
    }

    for (CmpdListMemberVO clmVO : incoming) {

      CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

      // these are hard-coded since reflection is problematic...
      if (propertyNameList.contains("alogp")) {
        if (DEBUG) {
          System.out.println("Processing param alogp.");
        }
        if (pchemVO.getTheALogP() != null) {
          if (!Float.isNaN(pchemVO.getTheALogP().floatValue())) {
            ahMap.get("alogp").addValue(pchemVO.getTheALogP().floatValue());
          }
        }
      }

      if (propertyNameList.contains("logd")) {
        if (DEBUG) {
          System.out.println("Processing param logd.");
        }
        if (pchemVO.getLogD() != null) {
          if (!Float.isNaN(pchemVO.getLogD().floatValue())) {
            ahMap.get("logd").addValue(pchemVO.getLogD().floatValue());
          }
        }
      }

      if (propertyNameList.contains("hba")) {
        if (DEBUG) {
          System.out.println("Processing param hba.");
        }
        if (pchemVO.getCountHydBondAcceptors() != null) {
          if (!Float.isNaN(pchemVO.getCountHydBondAcceptors())) {
            ahMap.get("hba").addValue(pchemVO.getCountHydBondAcceptors());
          }
        }
      }

      if (propertyNameList.contains("hbd")) {
        if (DEBUG) {
          System.out.println("Processing param hbd.");
        }
        if (pchemVO.getCountHydBondDonors() != null) {
          if (!Float.isNaN(pchemVO.getCountHydBondDonors())) {
            ahMap.get("hbd").addValue(pchemVO.getCountHydBondDonors());
          }
        }
      }

      if (propertyNameList.contains("sa")) {
        if (DEBUG) {
          System.out.println("Processing param sa.");
        }
        if (pchemVO.getSurfaceArea() != null) {
          if (!Float.isNaN(pchemVO.getSurfaceArea().floatValue())) {
            ahMap.get("sa").addValue(pchemVO.getSurfaceArea().floatValue());
          }
        }
      }

      if (propertyNameList.contains("mw")) {
        if (DEBUG) {
          System.out.println("Processing param mw.");
        }
        if (pchemVO.getMolecularWeight() != null) {
          if (!Float.isNaN(pchemVO.getMolecularWeight().floatValue())) {
            ahMap.get("mw").addValue(pchemVO.getMolecularWeight().floatValue());
          }
        }
      }

    }

    // MWK 07Dec2014 adding sort step
    
    ArrayList<String> sortList = new ArrayList<String>(ahMap.keySet());
    Collections.sort(sortList);
    
    for (String key : sortList) {
      String title = key;
      Histogram h = new Histogram(title, key, incoming, ahMap.get(key));
      rtn.add(h);
    }

    return rtn;

  }
  
 }
