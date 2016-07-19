/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkhistoscat;

import com.flaptor.hist4j.mwkdbl.AdaptiveHistogram;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import org.primefaces.model.chart.ChartSeries;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author mwkunkel
 */
public class Histogram<X> {

    private String propertyName;
    private String title;
    private AdaptiveHistogram ah;
    private Collection<X> clmColl;
    private ArrayList<HistogramBin<X>> binList;

    private static NumberFormat intf = new DecimalFormat();

    private static NumberFormat nf2 = new DecimalFormat();

    static {
        intf.setParseIntegerOnly(true);
        nf2.setMaximumFractionDigits(2);
    }

    public Histogram(String titleIn, String propertyNameIn, Collection<X> clmListIn, AdaptiveHistogram ahIn) {

        this.propertyName = propertyNameIn;
        this.title = titleIn;
        this.ah = ahIn;
        this.clmColl = clmListIn;
        this.binList = new ArrayList<HistogramBin<X>>();

        // parse the ah and create a list of bins
        // for discrete distributions, minCut and maxCut are the same
        if (this.propertyName.equals("hba") || this.propertyName.equals("hbd")) {

            double min = ah.getValueForPercentile(0);
            double max = ah.getValueForPercentile(100);

            int intMin = (int) Math.floor(min);
            int intMax = (int) Math.ceil(max);

            for (int i = intMin; i <= intMax; i++) {
                HistogramBin<X> bin = new HistogramBin<X>(i, i);
                this.binList.add(bin);
            }

        } else {

            for (int pct = 0; pct < 100; pct += 5) {

                // to ACTUALLY bin the min and max values (because of rounding errors, have to add an underflow and overflow
                // experience shows that 0.05 isn't sufficient, bump up to 0.1
                if (pct == 0) {
                    HistogramBin<X> bin = new HistogramBin<X>(ah.getValueForPercentile(0) - 0.10 * ah.getValueForPercentile(0), ah.getValueForPercentile(5));
                    this.binList.add(bin);
                } else if (pct == 95) {
                    HistogramBin<X> bin = new HistogramBin<X>(ah.getValueForPercentile(95), ah.getValueForPercentile(100) + 0.10 * ah.getValueForPercentile(100));
                    this.binList.add(bin);
                } else {
                    HistogramBin<X> bin = new HistogramBin<X>(ah.getValueForPercentile(pct), ah.getValueForPercentile(pct + 5));
                    this.binList.add(bin);
                }

            }

        }

//    DEBUG bins
//    for (TemplatedHistogramBin<X> hb : this.binList) {
//      System.out.println("bin minCut, maxCut: " + hb.getMinCut() + " " + hb.getMaxCut());
//    }
        // go through the list and add to the bins
        for (X clmVO : this.clmColl) {
            putInAppropriateBin(clmVO);
        }

    }

    private void putInAppropriateBin(X x) {

        double val = getDoubleProp(x);

        if (!Double.isNaN(val)) {
            boolean found = false;
            for (HistogramBin<X> b : binList) {
                // each bin returns a boolean 
                // continue until the right bin
                if (b.add(x, val)) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                System.out.println("No bin found in putInAppropriateBin: " + val + " for property: " + this.propertyName);
                for (HistogramBin<X> b : binList) {
                    System.out.println(" bin minCut, maxCut: " + nf2.format(b.getMinCut()) + " " + nf2.format(b.getMaxCut()));
                }
            }
        }

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

    public Double getDoubleProp(Object o) {

        Double rtn = null;

        try {

            Class clz = o.getClass();

//            printFieldsAndMethods(clz);
//
//            Class parent = clz.getSuperclass();
//            while (parent != null) {
//                printFieldsAndMethods(parent);
//                parent = parent.getSuperclass();
//            }
            Field f = clz.getDeclaredField(propertyName);
            f.setAccessible(true);

            Object rtnObj = f.get(o);
            rtn = (Double) rtnObj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    public BarChartModel getBarChartModel() {
        
        BarChartModel rtn = new BarChartModel();

        // generate the chart
        // there are two series, count and countSelected
        rtn.setTitle(this.title);
        rtn.setExtender("histogramExtender");
        rtn.setShowDatatip(true);
        rtn.setBarPadding(1);
        rtn.setBarMargin(1);

        Axis xAxis = rtn.getAxis(AxisType.X);
        xAxis.setTickAngle(-90);

        rtn.setStacked(true);

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

        for (HistogramBin<X> bin : this.binList) {

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
        rtn.addSeries(countSelectedSeries);
        rtn.addSeries(countSeries);

        return rtn;
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
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

    public Collection<X> getClmColl() {
        return clmColl;
    }

    public void setClmColl(Collection<X> clmColl) {
        this.clmColl = clmColl;
    }

    public ArrayList<HistogramBin<X>> getBinList() {
        return binList;
    }

    public void setBinList(ArrayList<HistogramBin<X>> binList) {
        this.binList = binList;
    }

//</editor-fold>
}
