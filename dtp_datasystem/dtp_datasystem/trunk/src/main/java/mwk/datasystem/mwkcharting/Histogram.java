/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkcharting;


import com.flaptor.hist4j.AdaptiveHistogram;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import mwk.datasystem.util.ExtendedCartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;

/**
 *
 * @author mwkunkel
 */
public class Histogram {

    private String propertyName;
    private String title;
    private AdaptiveHistogram ah;
    private CmpdListVO cmpdList;
    private ArrayList<HistogramBin> binList;
    private ExtendedCartesianChartModel chartModel;

    public Histogram(String titleIn, String propertyNameIn, CmpdListVO cmpdListIn, AdaptiveHistogram ahIn) {

        NumberFormat intf = new DecimalFormat();
        intf.setParseIntegerOnly(true);

        NumberFormat nf2 = new DecimalFormat();
        nf2.setMaximumFractionDigits(2);

        this.propertyName = propertyNameIn;
        this.title = titleIn;
        this.ah = ahIn;
        this.cmpdList = cmpdListIn;
        this.binList = new ArrayList<HistogramBin>();
        this.chartModel = new ExtendedCartesianChartModel();

//    parse the ah and create a list of bins

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
                if (pct == 0) {
                    HistogramBin bin = new HistogramBin(ah.getValueForPercentile(0) - 0.05 * ah.getValueForPercentile(0), ah.getValueForPercentile(5));
                    this.binList.add(bin);
                } else if (pct == 95) {
                    HistogramBin bin = new HistogramBin(ah.getValueForPercentile(95), ah.getValueForPercentile(100) + 0.05 * ah.getValueForPercentile(100));
                    this.binList.add(bin);
                } else {
                    HistogramBin bin = new HistogramBin(ah.getValueForPercentile(pct), ah.getValueForPercentile(pct + 5));
                    this.binList.add(bin);
                }

            }

        }

        // DEBUG bins
//    for (HistogramBin hb : this.binList) {
//      System.out.println("bin minCut, maxCut: " + hb.getMinCut() + " " + hb.getMaxCut());
//    }

        // go through the list and add to the bins

        for (CmpdListMemberVO clmVO : this.cmpdList.getCmpdListMembers()) {
            putInAppropriateBin(clmVO);
        }

        // generate the chart
        // there are two series, count and countSelected

        this.chartModel = new ExtendedCartesianChartModel();
        this.chartModel.setTitle(this.title);

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
                    System.out.println(" bin minCut, maxCut: " + b.getMinCut() + " " + b.getMaxCut());
                }
            }
        }

    }

    public double getProperty(CmpdListMemberVO clmVO) {

        // these are hard-coded since reflection is problematic...

        double rtn = Double.NaN;

        CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

        if (this.propertyName.equals("alogp")) {
            if (pchemVO.getAlogp() != null) {
                if (!Double.isNaN(pchemVO.getAlogp())) {
                    rtn = pchemVO.getAlogp();
                }
            }
        }

        if (this.propertyName.equals("logd")) {
            if (pchemVO.getLogd() != null) {
                if (!Double.isNaN(pchemVO.getLogd())) {
                    rtn = pchemVO.getLogd();
                }
            }
        }

        if (this.propertyName.equals("hba")) {
            if (pchemVO.getHba() != null) {
                if (!Double.isNaN(pchemVO.getHba())) {
                    rtn = pchemVO.getHba();
                }
            }
        }

        if (this.propertyName.equals("hbd")) {
            if (pchemVO.getHbd() != null) {
                if (!Double.isNaN(pchemVO.getHbd())) {
                    rtn = pchemVO.getHbd();
                }
            }
        }

        if (this.propertyName.equals("sa")) {
            if (pchemVO.getSa() != null) {
                if (!Double.isNaN(pchemVO.getSa())) {
                    rtn = pchemVO.getSa();
                }
            }
        }

        if (this.propertyName.equals("psa")) {
            if (pchemVO.getPsa() != null) {
                if (!Double.isNaN(pchemVO.getPsa())) {
                    rtn = pchemVO.getPsa();
                }
            }
        }

        if (this.propertyName.equals("mw")) {
            if (pchemVO.getMw() != null) {
                if (!Double.isNaN(pchemVO.getMw())) {
                    rtn = pchemVO.getMw();
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

    public CmpdListVO getCmpdList() {
        return cmpdList;
    }

    public void setCmpdList(CmpdListVO cmpdList) {
        this.cmpdList = cmpdList;
    }

    public ArrayList<HistogramBin> getBinList() {
        return binList;
    }

    public void setBinList(ArrayList<HistogramBin> binList) {
        this.binList = binList;
    }

    public ExtendedCartesianChartModel getChartModel() {
        return chartModel;
    }

    public void setChartModel(ExtendedCartesianChartModel chartModel) {
        this.chartModel = chartModel;
    }
}
