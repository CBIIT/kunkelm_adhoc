/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import histogram.AdaptiveHistogram;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import mwk.datasystem.mwkhisto.Histogram;
import org.primefaces.model.chart.ChartSeries;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;

/**
 *
 * @author mwkunkel
 */
public class HistogramChartUtil {

  public static ArrayList<Histogram> doHistograms(CmpdListVO incoming) {

    ArrayList<Histogram> rtn = new ArrayList<Histogram>();

    ArrayList<String> propertyNameList = new ArrayList<String>();
    propertyNameList.add("mw");
    propertyNameList.add("hba");
    propertyNameList.add("hbd");
    propertyNameList.add("alogp");
    propertyNameList.add("logd");
    propertyNameList.add("sa");
    propertyNameList.add("psa");


    HashMap<String, AdaptiveHistogram> ahMap = new HashMap<String, AdaptiveHistogram>();

    // initialize the ah map
    for (String propertyName : propertyNameList) {
      ahMap.put(propertyName, new AdaptiveHistogram());
    }

    for (CmpdListMemberVO clmVO : incoming.getCmpdListMembers()) {

      CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

      // these are hard-coded since reflection is problematic...
      if (pchemVO.getAlogp() != null) {
        if (!Float.isNaN(pchemVO.getAlogp().floatValue())) {
          ahMap.get("alogp").addValue(pchemVO.getAlogp().floatValue());
        }
      }

      if (pchemVO.getLogd() != null) {
        if (!Float.isNaN(pchemVO.getLogd().floatValue())) {
          ahMap.get("logd").addValue(pchemVO.getLogd().floatValue());
        }
      }

      if (pchemVO.getHba() != null) {
        if (!Float.isNaN(pchemVO.getHba())) {
          ahMap.get("hba").addValue(pchemVO.getHba());
        }
      }

      if (pchemVO.getHbd() != null) {
        if (!Float.isNaN(pchemVO.getHbd())) {
          ahMap.get("hbd").addValue(pchemVO.getHbd());
        }
      }

      if (pchemVO.getSa() != null) {
        if (!Float.isNaN(pchemVO.getSa().floatValue())) {
          ahMap.get("sa").addValue(pchemVO.getSa().floatValue());
        }
      }

      if (pchemVO.getPsa() != null) {
        if (!Float.isNaN(pchemVO.getPsa().floatValue())) {
          ahMap.get("psa").addValue(pchemVO.getPsa().floatValue());
        }
      }

      if (pchemVO.getMw() != null) {
        if (!Float.isNaN(pchemVO.getMw().floatValue())) {
          ahMap.get("mw").addValue(pchemVO.getMw().floatValue());
        }
      }
    }

    for (String key : ahMap.keySet()) {
      String title = key;
      Histogram h = new Histogram(title, key, incoming, ahMap.get(key));
      rtn.add(h);
    }

    return rtn;

  }
}
