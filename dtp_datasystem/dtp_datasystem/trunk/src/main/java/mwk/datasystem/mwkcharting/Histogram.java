/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkcharting;

import com.flaptor.hist4j.mwkdbl.AdaptiveHistogram;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import mwk.datasystem.vo.CmpdListMemberVO;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author mwkunkel
 */
public class Histogram {

  private String propertyName;
  private String title;
  private AdaptiveHistogram ah;
  private Collection<CmpdListMemberVO> clmColl;
  private ArrayList<HistogramBin> binList;
  private BarChartModel chartModel;

  public void debugBins() {

    NumberFormat nf2 = new DecimalFormat();
    nf2.setMaximumFractionDigits(2);

    for (HistogramBin hb : this.binList) {
      System.out.println(this.propertyName + " bin minCut, maxCut: " + nf2.format(hb.getMinCut()) + " " + nf2.format(hb.getMaxCut()) + " cnt: " + hb.getCount() + " cntSel: " + hb.getCountSelected());
    }
  }

  public Histogram(String titleIn,
          String propertyNameIn,
          Collection<CmpdListMemberVO> clmListIn,
          AdaptiveHistogram ahIn) {

    NumberFormat intf = new DecimalFormat();
    intf.setParseIntegerOnly(true);

    NumberFormat nf2 = new DecimalFormat();
    nf2.setMaximumFractionDigits(2);

    this.propertyName = propertyNameIn;
    this.title = titleIn;
    this.ah = ahIn;
    this.clmColl = clmListIn;
    this.binList = new ArrayList<HistogramBin>();
    this.chartModel = new BarChartModel();

    // parse the ah and create a list of bins
    // for discrete distributions, minCut and maxCut are the same
    if (this.propertyName.equals("cmpd.parentFragment.cmpdFragmentPChem.countHydBondAcceptors") || this.propertyName.equals("cmpd.parentFragment.cmpdFragmentPChem.countHydBondDonors")) {

      double min = ah.getValueForPercentile(0);
      double max = ah.getValueForPercentile(100);

      int intMin = (int) Math.floor(min);
      int intMax = (int) Math.ceil(max);

      for (int i = intMin; i <= intMax; i++) {
        HistogramBin bin = new HistogramBin(i, i, Integer.valueOf(i).toString());
        this.binList.add(bin);
      }

    } else {

      double min = ah.getValueForPercentile(0);
      double max = ah.getValueForPercentile(100);

      System.out.println(this.propertyName + " min: " + min + " max:" + max);

      for (int pct = 0; pct < 100; pct += 5) {

        if (pct == 0) {

          double mn = ah.getValueForPercentile(0) - 0.10 * ah.getValueForPercentile(0);
          double mx = ah.getValueForPercentile(5);

          // String lbl = nf2.format(mn) + " to " + nf2.format(mx);
          String lbl = nf2.format(mn + ((mx - mn) / 2));

          HistogramBin bin = new HistogramBin(mn, mx, lbl);

          // have to intercept no-width bins
          // AdaptiveHistogram is OK with these,
          // but the PrimeFaces chart model will have problems
          if (mn != mx) {
            this.binList.add(bin);
          }

        } else if (pct == 95) {

          double mn = ah.getValueForPercentile(95);
          double mx = ah.getValueForPercentile(100) + 0.10 * ah.getValueForPercentile(100);

          // String lbl = nf2.format(mn) + " to " + nf2.format(mx);
          String lbl = nf2.format(mn + ((mx - mn) / 2));

          HistogramBin bin = new HistogramBin(mn, mx, lbl);

          // have to intercept no-width bins
          // AdaptiveHistogram is OK with these,
          // but the PrimeFaces chart model will have problems
          if (mn != mx) {
            this.binList.add(bin);
          }

        } else {

          double mn = ah.getValueForPercentile(pct);
          double mx = ah.getValueForPercentile(pct + 5);

          // String lbl = nf2.format(mn) + " to " + nf2.format(mx);
          String lbl = nf2.format(mn + ((mx - mn) / 2));

          HistogramBin bin = new HistogramBin(mn, mx, lbl);
          if (mn != mx) {
            this.binList.add(bin);
          }

        }

      }

    }

    // go through the list and add to the bins
    for (CmpdListMemberVO clmVO : this.clmColl) {
      putInAppropriateBin(clmVO);
    }

    // generate the chart
    // there are two series, count and countSelected
    this.chartModel = new BarChartModel();
    this.chartModel.setTitle(this.title);
    this.chartModel.setExtender("histogramExtender");
    this.chartModel.setShowDatatip(true);

    this.chartModel.setBarPadding(1);
    this.chartModel.setBarMargin(1);

    Axis xAxis = this.chartModel.getAxis(AxisType.X);
    xAxis.setTickAngle(-90);

    this.chartModel.setStacked(true);

    ChartSeries countSeries = new ChartSeries("count");
    ChartSeries countSelectedSeries = new ChartSeries("countSelected");

    for (HistogramBin bin : this.binList) {

      int selCnt = bin.getCountSelected();
      countSelectedSeries.set(bin.getLabel(), selCnt);

      int incrCnt = bin.getCount() - selCnt;
      countSeries.set(bin.getLabel(), incrCnt);

    }

    if (countSeries.getData().size() == 0) {
      System.out.println("countSeries was empty; so, added single FILLER");
      countSeries.set("FILLER", 0);
    }

    if (countSelectedSeries.getData().size() == 0) {
      System.out.println("countSelectedSeries was empty; so, added single FILLER");
      countSelectedSeries.set("FILLER", 0);
    }

    // have to add countSelectedFirst so that stacked series display correctly
    this.chartModel.addSeries(countSelectedSeries);
    this.chartModel.addSeries(countSeries);

  }

  private void putInAppropriateBin(CmpdListMemberVO clmVO) {

    TemplPropUtil propUtils = new TemplPropUtil(clmVO);
      
    NumberFormat nf2 = new DecimalFormat();
    nf2.setMaximumFractionDigits(2);

    Double val = null;

    if (propUtils.isIntProp(this.propertyName)) {
      if (propUtils.getInt(clmVO, this.propertyName) != null) {
        val = propUtils.getInt(clmVO, this.propertyName).doubleValue();
      } else {
        System.out.println(this.propertyName + " was null in getIntegerProperty for NSC: " + clmVO.getCmpd().getNsc());
      }
    }

    if (propUtils.isDblProp(this.propertyName)) {
      if (propUtils.getDbl(clmVO, this.propertyName) != null) {
        val = propUtils.getDbl(clmVO, this.propertyName);
      } else {
        System.out.println(this.propertyName + " was null in getDoubleProperty for NSC: " + clmVO.getCmpd().getNsc());
      }
    }

    boolean found = false;

    if (val != null) {
      for (HistogramBin b : binList) {
        // each bin returns a boolean 
        // continue until the right bin
        if (b.add(clmVO, val)) {
          found = true;
          break;
        }
      }
    } else {
      System.out.println("val is null for: " + this.getPropertyName() + " for NSC: " + clmVO.getCmpd().getNsc());
    }
    if (!found) {
      System.out.println("No bin found in putInAppropriateBin: " + val + " for property: " + this.propertyName);
      for (HistogramBin b : binList) {
        System.out.println(" bin minCut, maxCut: " + nf2.format(b.getMinCut()) + " " + nf2.format(b.getMaxCut()));
      }
    }
  }

  public String getPropertyName() {
    return propertyName;
  }

  public void setPropertyName(String propertyName) {
    this.propertyName = propertyName;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public AdaptiveHistogram getAh() {
    return ah;
  }

  public void setAh(AdaptiveHistogram ah) {
    this.ah = ah;
  }

  public Collection<CmpdListMemberVO> getClmColl() {
    return clmColl;
  }

  public void setClmColl(Collection<CmpdListMemberVO> clmColl) {
    this.clmColl = clmColl;
  }

  public ArrayList<HistogramBin> getBinList() {
    return binList;
  }

  public void setBinList(ArrayList<HistogramBin> binList) {
    this.binList = binList;
  }

  public BarChartModel getChartModel() {
    return chartModel;
  }

  public void setChartModel(BarChartModel chartModel) {
    this.chartModel = chartModel;
  }

}
