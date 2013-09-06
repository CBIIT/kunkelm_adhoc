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
//  private static ExtendedCartesianChartModel generateHistogramORIGINAL(String title, AdaptiveHistogram ah, String propertyName, CmpdListVO clVO) {
//
//    System.out.println("Entering generateHistogram in HistogramChartUtil with title: " + title);
//
//    // MWK Histogram
//
//    Histogram h = new Histogram(title, propertyName, clVO, ah);
//
//    return h.getChartModel();
//
//  }
//  
//  public static ArrayList<ExtendedCartesianChartModel> doHistogramsORIGINAL(CmpdListVO incoming) {
//
//    ArrayList<ExtendedCartesianChartModel> rtn = new ArrayList<ExtendedCartesianChartModel>();
//
//    ArrayList<String> propertyNameList = new ArrayList<String>();
//    propertyNameList.add("alogp");
//    propertyNameList.add("logd");
//    propertyNameList.add("hba");
//    propertyNameList.add("hbd");
//    propertyNameList.add("sa");
//    propertyNameList.add("psa");
//    propertyNameList.add("mw");
//
//    HashMap<String, AdaptiveHistogram> ahMap = new HashMap<String, AdaptiveHistogram>();
//
//    // initialize the ah map
//    for (String propertyName : propertyNameList) {
//      ahMap.put(propertyName, new AdaptiveHistogram());
//    }
//
//    for (CmpdListMemberVO clmVO : incoming.getCmpdListMembers()) {
//
//      CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();
//
//      // these are hard-coded since reflection is problematic...
//      if (pchemVO.getAlogp() != null) {
//        if (!Float.isNaN(pchemVO.getAlogp().floatValue())) {
//          ahMap.get("alogp").addValue(pchemVO.getAlogp().floatValue());
//        }
//      }
//
//      if (pchemVO.getLogd() != null) {
//        if (!Float.isNaN(pchemVO.getLogd().floatValue())) {
//          ahMap.get("logd").addValue(pchemVO.getLogd().floatValue());
//        }
//      }
//
//      if (pchemVO.getHba() != null) {
//        if (!Float.isNaN(pchemVO.getHba())) {
//          ahMap.get("hba").addValue(pchemVO.getHba());
//        }
//      }
//
//      if (pchemVO.getHbd() != null) {
//        if (!Float.isNaN(pchemVO.getHbd())) {
//          ahMap.get("hbd").addValue(pchemVO.getHbd());
//        }
//      }
//
//      if (pchemVO.getSa() != null) {
//        if (!Float.isNaN(pchemVO.getSa().floatValue())) {
//          ahMap.get("sa").addValue(pchemVO.getSa().floatValue());
//        }
//      }
//
//      if (pchemVO.getPsa() != null) {
//        if (!Float.isNaN(pchemVO.getPsa().floatValue())) {
//          ahMap.get("psa").addValue(pchemVO.getPsa().floatValue());
//        }
//      }
//
//      if (pchemVO.getMw() != null) {
//        if (!Float.isNaN(pchemVO.getMw().floatValue())) {
//          ahMap.get("mw").addValue(pchemVO.getMw().floatValue());
//        }
//      }
//    }
//
//    for (String key : ahMap.keySet()) {
//      String title = "Property: " + key;
//      //rtn.add(generateHistogram(title, ahMap.get(key)));
//      rtn.add(generateHistogram(title, ahMap.get(key), key, incoming));
//    }
//
//    return rtn;
//
//  }
//
//  
//  
//  
//  
//  ORIGINAL code just dealt with the values, not the underlying CmpdListMember
//  private static ExtendedCartesianChartModel generateHistogramORIGINAL(String title, AdaptiveHistogram ah) {
//
//    System.out.println("Entering generateHistogram in HistogramChartUtil with title: " + title);
//
//    ExtendedCartesianChartModel histogramModel = new ExtendedCartesianChartModel();
//    histogramModel.setTitle(title);
//
//    ChartSeries series = new ChartSeries();
//    series.setLabel(title);
//
//    NumberFormat nf = new DecimalFormat();
//    nf.setMaximumFractionDigits(2);
//
//    //contiuous distribution 
//    // step through by 5% increments and figure out the cumulate and interval counts
//
//    long lastCumCnt = 0;
//    long intervalCnt = 0;
//
//    for (int pct = 0; pct <= 100; pct += 5) {
//      float valForPct = ah.getValueForPercentile(pct);
//      String label = nf.format(valForPct);
//      intervalCnt = ah.getAccumCount(valForPct) - lastCumCnt;
//      lastCumCnt = ah.getAccumCount(valForPct);
//      //System.out.println(pct + "%: " + nf.format(valForPct) + " " + lastCumCnt + " " + intervalCnt);
//      System.out.println("Adding to series: " + label + " " + intervalCnt);
//      series.set(label, intervalCnt);
//    }
//
//    if (series.getData().size() == 0) {
//      System.out.println("Series was empty; so, added single FILLER");
//      series.set("FILLER", 0);
//    }
//
//    histogramModel.addSeries(series);
//
//    return histogramModel;
//
//  }
}
