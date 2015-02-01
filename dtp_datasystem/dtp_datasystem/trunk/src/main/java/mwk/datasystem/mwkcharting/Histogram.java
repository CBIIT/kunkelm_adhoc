/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkcharting;

import com.flaptor.hist4j.AdaptiveHistogram;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import org.primefaces.model.chart.ChartSeries;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
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

  public Histogram(String titleIn, String propertyNameIn, Collection<CmpdListMemberVO> clmListIn, AdaptiveHistogram ahIn) {

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
    if (this.propertyName.equals("hba") || this.propertyName.equals("hbd")) {

      double min = ah.getValueForPercentile(0);
      double max = ah.getValueForPercentile(100);

      int intMin = (int) Math.floor(min);
      int intMax = (int) Math.ceil(max);

      for (int i = intMin; i <= intMax; i++) {
        HistogramBin bin = new HistogramBin(i, i);
        this.binList.add(bin);
      }

    } else {

      for (int pct = 0; pct < 100; pct += 5) {

        // to ACTUALLY bin the min and max values (because of rounding errors, have to add an underflow and overflow
        // experience shows that 0.05 isn't sufficient, bump up to 0.1
        if (pct == 0) {
          HistogramBin bin = new HistogramBin(ah.getValueForPercentile(0) - 0.10 * ah.getValueForPercentile(0), ah.getValueForPercentile(5));
          this.binList.add(bin);
        } else if (pct == 95) {
          HistogramBin bin = new HistogramBin(ah.getValueForPercentile(95), ah.getValueForPercentile(100) + 0.10 * ah.getValueForPercentile(100));
          this.binList.add(bin);
        } else {
          HistogramBin bin = new HistogramBin(ah.getValueForPercentile(pct), ah.getValueForPercentile(pct + 5));
          this.binList.add(bin);
        }

      }

    }

//    DEBUG bins
//    for (HistogramBin hb : this.binList) {
//      System.out.println("bin minCut, maxCut: " + hb.getMinCut() + " " + hb.getMaxCut());
//    }
    
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
    
//    <p:barChart value="#{histo.chartModel}"
//                            title="#{histo.chartModel.title}"                      
//                            extender="histogramExtender"
//                            zoom="false"
//                            showDatatip="true"
//                            
//                            barPadding="1"
//                            barMargin="1"
//                            xaxisAngle="-90"
//                            animate="true"
//                            stacked="true">
    
    

    ChartSeries countSeries = new ChartSeries("count");
    ChartSeries countSelectedSeries = new ChartSeries("countSelected");

    for (HistogramBin bin : this.binList) {

            // for discrete distributions minCut = maxCut = binVal;
      String label = "notSet";

      if (bin.getMinCut() == bin.getMaxCut()) {
        label = intf.format(bin.getMinCut());
      } else {
        double midVal = bin.getMinCut() + ((bin.getMaxCut() - bin.getMinCut()) / 2);
        label = nf2.format(midVal);
      }

      //countSelectedSeries.set(label, bin.getCountSelected());
      int selCnt = bin.getCountSelected();
      countSelectedSeries.set(label, selCnt);

      int incrCnt = bin.getCount() - selCnt;
      countSeries.set(label, incrCnt);

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
    
    NumberFormat nf2 = new DecimalFormat();
    nf2.setMaximumFractionDigits(2);

    double val = getProperty(clmVO);

    if (!Double.isNaN(val)) {
      boolean found = false;
      for (HistogramBin b : binList) {
                // each bin returns a boolean 
        // continue until the right bin
        if (b.add(clmVO, val)) {
          found = true;
          break;
        }
      }
      if (!found) {
        System.out.println("No bin found in putInAppropriateBin: " + val + " for property: " + this.propertyName);
        for (HistogramBin b : binList) {
          System.out.println(" bin minCut, maxCut: " + nf2.format(b.getMinCut()) + " " + nf2.format(b.getMaxCut()));
        }
      }
    }

  }

  public double getProperty(CmpdListMemberVO clmVO) {

        // these are hard-coded since reflection is problematic...
    double rtn = Double.NaN;

    CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

    if (this.propertyName.equals("alogp")) {
      if (pchemVO.getTheALogP() != null) {
        if (!Double.isNaN(pchemVO.getTheALogP())) {
          rtn = pchemVO.getTheALogP();
        }
      }
    }

    if (this.propertyName.equals("logd")) {
      if (pchemVO.getLogD() != null) {
        if (!Double.isNaN(pchemVO.getLogD())) {
          rtn = pchemVO.getLogD();
        }
      }
    }

    if (this.propertyName.equals("hba")) {
      if (pchemVO.getCountHydBondAcceptors() != null) {
        if (!Double.isNaN(pchemVO.getCountHydBondAcceptors())) {
          rtn = pchemVO.getCountHydBondAcceptors();
        }
      }
    }

    if (this.propertyName.equals("hbd")) {
      if (pchemVO.getCountHydBondDonors() != null) {
        if (!Double.isNaN(pchemVO.getCountHydBondDonors())) {
          rtn = pchemVO.getCountHydBondDonors();
        }
      }
    }

    if (this.propertyName.equals("sa")) {
      if (pchemVO.getSurfaceArea() != null) {
        if (!Double.isNaN(pchemVO.getSurfaceArea())) {
          rtn = pchemVO.getSurfaceArea();
        }
      }
    }

    if (this.propertyName.equals("mw")) {
      if (pchemVO.getMolecularWeight() != null) {
        if (!Double.isNaN(pchemVO.getMolecularWeight())) {
          rtn = pchemVO.getMolecularWeight();
        }
      }
    }

    return rtn;

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
