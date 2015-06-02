/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meangraph;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import mwk.microxeno.vo.PassageAggregateVO;
import mwk.microxeno.vo.PassageDataSetVO;
import mwk.microxeno.vo.PassageIdentifierVO;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.HorizontalBarChartModel;
import utils.Comparators;

/**
 *
 * @author mwkunkel
 */
public class NewMeanGraphUtil {

    public static NumberFormat nf2;

    static {
        nf2 = new DecimalFormat();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
    }

    public static HorizontalBarChartModel renderMeanGraph(PassageDataSetVO dsVO) {

        HorizontalBarChartModel mnGrChMdl = new HorizontalBarChartModel();

//                         _       _       
// _ __   ___  _ __  _   _| | __ _| |_ ___ 
//| '_ \ / _ \| '_ \| | | | |/ _` | __/ _ \
//| |_) | (_) | |_) | |_| | | (_| | ||  __/
//| .__/ \___/| .__/ \__,_|_|\__,_|\__\___|
//|_|         |_|                          
//__     __    _            ____  _           _   _   _           
//\ \   / /_ _| |_   _  ___/ ___|| |__  _   _| |_| |_| | ___  ___ 
// \ \ / / _` | | | | |/ _ \___ \| '_ \| | | | __| __| |/ _ \/ __|
//  \ V / (_| | | |_| |  __/___) | | | | |_| | |_| |_| |  __/\__ \
//   \_/ \__,_|_|\__,_|\___|____/|_| |_|\__,_|\__|\__|_|\___||___/
//
        ArrayList<MeanGraphDataShuttle> shuttleList = new ArrayList<MeanGraphDataShuttle>();

        PassageIdentifierVO piVO = dsVO.getPassageIdentifier();

        String formattedMean = (dsVO.getMean() != null) ? nf2.format(dsVO.getMean()) : "NULL";

        mnGrChMdl.setTitle(piVO.getAffymetrixIdentifier().getGenecard() + " " + piVO.getPassage());
        mnGrChMdl.setStacked(true);
        mnGrChMdl.setExtender("forwardMeanGraphChartExtender");
        Axis xAxis = mnGrChMdl.getAxis(AxisType.X);
        xAxis.setLabel("Delta from Mean " + formattedMean);
        xAxis.setTickAngle(-90);

        ArrayList<PassageAggregateVO> affyData = new ArrayList<PassageAggregateVO>(dsVO.getTumorDatas());
        Collections.sort(affyData, new Comparators.PassageDataSetComparator());

        Double mean = dsVO.getMean();
        Double minDelta = dsVO.getMinDelta();
        Double maxDelta = dsVO.getMaxDelta();

        String panelName;
        String cellLineName;
        Double val = 0d;
        String valFlag;
        Double delta = 0d;

        for (PassageAggregateVO adVO : affyData) {
            panelName = adVO.getTumor().getTumorType();
            cellLineName = adVO.getTumor().getTumorName();
            val = null;
            valFlag = null;
            if (adVO.getMean() != null) {
                val = adVO.getMean();
                valFlag = null;
                delta = adVO.getUnitDeltaValue();
            }
            shuttleList.add(new MeanGraphDataShuttle(panelName, cellLineName, val, valFlag, mean, delta, minDelta, maxDelta));
        }
        shuttleList.add(new MeanGraphDataShuttle("SUMMARY", "PANEL SUMMARY", null, null, null, null, null, null));
        shuttleList.add(new MeanGraphDataShuttle("SUMMARY", "mean", null, null, mean, null, null, null));
        shuttleList.add(new MeanGraphDataShuttle("SUMMARY", "min delta", null, null, null, null, minDelta, null));
        shuttleList.add(new MeanGraphDataShuttle("SUMMARY", "max delta", null, null, null, null, null, maxDelta));

//           _     _ 
//  __ _  __| | __| |
// / _` |/ _` |/ _` |
//| (_| | (_| | (_| |
// \__,_|\__,_|\__,_|
//                   
//__     __    _            ____  _           _   _   _           
//\ \   / /_ _| |_   _  ___/ ___|| |__  _   _| |_| |_| | ___  ___ 
// \ \ / / _` | | | | |/ _ \___ \| '_ \| | | | __| __| |/ _ \/ __|
//  \ V / (_| | | |_| |  __/___) | | | | |_| | |_| |_| |  __/\__ \
//   \_/ \__,_|_|\__,_|\___|____/|_| |_|\__,_|\__|\__|_|\___||___/
//                                                                
//  __              ____   _    _   _ _____ _           _    _       
// / _| ___  _ __  |  _ \ / \  | \ | | ____| |      ___| | _(_)_ __  
//| |_ / _ \| '__| | |_) / _ \ |  \| |  _| | |     / __| |/ / | '_ \ 
//|  _| (_) | |    |  __/ ___ \| |\  | |___| |___  \__ \   <| | |_) |
//|_|  \___/|_|    |_| /_/   \_\_| \_|_____|_____| |___/_|\_\_| .__/ 
//                                                            |_| 
//        
        // shared code for all data types to add panels and summaries
        ArrayList<MeanGraphDataShuttle> withPanels = new ArrayList<MeanGraphDataShuttle>();

        String currentPanel = "ALWAYS FAILS FIRST MATCH";

        for (MeanGraphDataShuttle vs : shuttleList) {

            // if new panel, add entry for it and then add the real data            
            if (!vs.panelName.equals(currentPanel)) {
                String holder = "PANEL " + vs.panelName;
                withPanels.add(new MeanGraphDataShuttle("PANEL", holder, null, null, null, null, null, null));
                withPanels.add(vs);
                // set new value for currentPanel
                currentPanel = vs.panelName;
            } else {
                withPanels.add(vs);
            }
        }

        // linked set to identify the panelNames, keep panel order
        HashSet<String> panelNameSet = new LinkedHashSet<String>();
        for (MeanGraphDataShuttle vs : withPanels) {
            panelNameSet.add(vs.panelName);
        }

        // panelMap to build up parallel chartSeries
        HashMap<String, ChartSeries> panelNameMap = new LinkedHashMap<String, ChartSeries>();

        // hard code fake series
        ChartSeries fakeSeries = new ChartSeries("PANEL FAKE");
        fakeSeries.setLabel("PANEL FAKE");

        // initialize the Chart Series-es in the map
        for (String pn : panelNameSet) {
            ChartSeries panelSeries = new ChartSeries("PANEL " + pn);
            panelSeries.setLabel("PANEL " + pn);
            panelNameMap.put(pn, panelSeries);
        }

        // horizontal bar charts render from the bottom up; so, have 
        // REVERSE the order here
        Collections.reverse(withPanels);

        for (MeanGraphDataShuttle vs : withPanels) {
            for (String pn : panelNameMap.keySet()) {
                ChartSeries curSer = panelNameMap.get(pn);
                Boolean isCurrentPanel = pn.equals(vs.panelName);
                // two sets of code, one for summaries, one for "real" data entries
                if (pn.equals("SUMMARY")) {
                    summarize(isCurrentPanel, fakeSeries, curSer, vs);
                } else {
                    // NB!  manipulate catches the "PANEL" entries
                    manipulate(isCurrentPanel, fakeSeries, curSer, vs);
                }
            }

        }
        // add the series-es to the model      
        mnGrChMdl.addSeries(fakeSeries);
        for (String s : panelNameMap.keySet()) {
            mnGrChMdl.addSeries(panelNameMap.get(s));
        }

        return mnGrChMdl;

    }

    public static void manipulate(
            Boolean pnl,
            ChartSeries fakSer,
            ChartSeries curSer,
            MeanGraphDataShuttle vs) {

        if (vs.cellLineName.startsWith("PANEL")) {

            String label = vs.cellLineName;
            // completely faked out
            if (pnl) {
                fakSer.set(label, 0);
                curSer.set(label, 0);
            } else {
                curSer.set(label, 0);
            }

            // use valFlag as short circuit indicator
            // for compound data
        } else if (vs.valFlag != null && vs.val != null) {

            // evaluate flags
            if (vs.valFlag.equals("=")) {
                String label = vs.cellLineName + " =" + nf2.format(vs.val);

                if (pnl) {
                    fakSer.set(label, 0);
                    curSer.set(label, vs.delta);
                } else {
                    curSer.set(label, 0);
                }
            } else if (vs.valFlag.equals(">")) {
                String label = vs.cellLineName + " >" + nf2.format(vs.val);
                // bars to the left
                // ic50 is greater than max tested
                if (pnl) {
                    fakSer.set(label, 0);
                    curSer.set(label, 4);
                } else {
                    curSer.set(label, 0);
                }
            } else if (vs.valFlag.equals("<")) {
                String label = vs.cellLineName + " <" + nf2.format(vs.val);
                // bars to the right 
                // ic50 is less than the min tested tested
                if (pnl) {
                    fakSer.set(label, 0);
                    curSer.set(label, -4);
                } else {
                    curSer.set(label, 0);
                }
            }

            // val without valFlag => molecular expression data
        } else if (vs.val != null) {
            String label = vs.cellLineName + " " + nf2.format(vs.val);
            if (pnl) {
                fakSer.set(label, 0);
                curSer.set(label, vs.delta);
            } else {
                curSer.set(label, 0);
            }

        } else {

            String label = vs.cellLineName + " NO DATA";
            // there is no data
            if (pnl) {
                fakSer.set(label, 0);
                curSer.set(label, 0);
            } else {
                curSer.set(label, 0);
            }
        }
    }

    public static void summarize(
            Boolean pnl,
            ChartSeries fakSer,
            ChartSeries curSer,
            MeanGraphDataShuttle vs) {

        if (vs.cellLineName.equals("mean")) {

            String label = "mean " + nf2.format(vs.mean);
            // no bar for the mean
            // there is no data
            if (pnl) {
                fakSer.set(label, 0);
                curSer.set(label, 0);
            } else {
                curSer.set(label, 0);
            }

        } else if (vs.cellLineName.equals("min delta")) {

            Double deltaToPlot = new Double(0d);
            if (vs.minDelta != null) {
                deltaToPlot = vs.minDelta;
            }
            String label = "min delta " + nf2.format(deltaToPlot);
            if (pnl) {
                fakSer.set(label, 0);
                curSer.set(label, deltaToPlot);
            } else {
                curSer.set(label, 0);
            }

        } else if (vs.cellLineName.equals("max delta")) {

            Double deltaToPlot = new Double(0d);
            if (vs.maxDelta != null) {
                deltaToPlot = vs.maxDelta;
            }
            String label = "max delta " + nf2.format(deltaToPlot);
            if (pnl) {
                fakSer.set(label, 0);
                curSer.set(label, deltaToPlot);
            } else {
                curSer.set(label, 0);
            }

        } else if (vs.cellLineName.startsWith("PANEL")) {

            String label = vs.cellLineName;
            if (pnl) {
                fakSer.set(label, 0);
                curSer.set(label, 0);
            } else {
                curSer.set(label, 0);
            }

        } else {

            String label = vs.cellLineName;
            if (pnl) {
                // MWK, 22Feb2015, is this correct, NOTHING to be done here?
            } else {
                curSer.set(label, 0);
            }
        }
    }

}
