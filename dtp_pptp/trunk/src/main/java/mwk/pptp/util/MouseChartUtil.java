/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.pptp.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import mwk.pptp.vo.MouseDataShuttleVO;
import mwk.pptp.vo.MouseGraphShuttleVO;
import mwk.pptp.vo.MouseRTVShuttleVO;
import mwk.pptp.vo.MouseSurvivalShuttleVO;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author mwkunkel
 */
public class MouseChartUtil {

    public LineChartModel renderMouseData(MouseGraphShuttleVO mgShut) {

        LineChartModel rtn = new LineChartModel();

        rtn.setTitle(mgShut.getCompoundName() + " " + mgShut.getCellLineName());

        Axis xAxis = rtn.getAxes().get(AxisType.X);
        Axis yAxis = rtn.getAxes().get(AxisType.Y);

        xAxis.setLabel("Day");
        xAxis.setMin(0d);

        // Leukemias 
        if (mgShut.getCellLine().startsWith("G")) {
            yAxis.setLabel("Percent Human CD45");
            yAxis.setMin(0d);
            yAxis.setMax(25d);
        } else {
            yAxis.setLabel("Log10 Tumor Volume (cc)");
            yAxis.setMin(-1d);
            yAxis.setMax(1d);
        }

        HashMap<String, List<MouseDataShuttleVO>> theMap = new HashMap<String, List<MouseDataShuttleVO>>();

        for (MouseDataShuttleVO d : mgShut.getDatas()) {
            if (theMap.containsKey(d.getGroupRole() + " " + d.getMouseNumber())) {
                //System.out.println("Adding to existing ArrayList for: " + d.getGroupRole() + " " + d.getMouseNumber());
                theMap.get(d.getGroupRole() + " " + d.getMouseNumber()).add(d);
            } else {
                //System.out.println("Creating new ArrayList for: " + d.getGroupRole() + " " + d.getMouseNumber());
                ArrayList<MouseDataShuttleVO> newList = new ArrayList<MouseDataShuttleVO>();
                newList.add(d);
                theMap.put(d.getGroupRole() + " " + d.getMouseNumber(), newList);
            }
        }

        // series for each entry in the map
        System.out.println("Size of theMap in renderMouseData in PptpController: " + theMap.size());

        for (Map.Entry<String, List<MouseDataShuttleVO>> thisMap : theMap.entrySet()) {

            //System.out.println("Creating LineChartSeries for: " + thisMap.getKey());
            // a chart series
            LineChartSeries thisSeries = new LineChartSeries();
            thisSeries.setLabel(thisMap.getKey());
            thisSeries.setShowLine(true);

            // sort by day
            Collections.sort(thisMap.getValue(), new Comparators.MouseDataShuttleComparator());

            for (MouseDataShuttleVO mds : thisMap.getValue()) {
                thisSeries.set(mds.getDay(), mds.getValue());
            }

            rtn.addSeries(thisSeries);

        }

        return rtn;

    }

    public LineChartModel renderMouseRtv(MouseGraphShuttleVO mgShut) {

        LineChartModel rtn = new LineChartModel();
        rtn.setTitle(mgShut.getCompoundName() + " " + mgShut.getCellLineName());

        Axis xAxis = rtn.getAxes().get(AxisType.X);
        Axis yAxis = rtn.getAxes().get(AxisType.Y);

        xAxis.setLabel("Day");
        xAxis.setMin(0d);

        // Leukemias 
        if (mgShut.getCellLine().startsWith("G")) {
            yAxis.setLabel("Median Percent Human CD45");
            yAxis.setMin(0d);
            yAxis.setMax(25d);
        } else {
            yAxis.setLabel("Relative Median Tumor Volume");
            yAxis.setMin(0d);
            yAxis.setMax(4d);
        }

        HashMap<String, List<MouseRTVShuttleVO>> theMap = new HashMap<String, List<MouseRTVShuttleVO>>();

        for (MouseRTVShuttleVO d : mgShut.getRtvs()) {
            if (theMap.containsKey(d.getGroupRole())) {
                theMap.get(d.getGroupRole()).add(d);
            } else {
                ArrayList<MouseRTVShuttleVO> newList = new ArrayList<MouseRTVShuttleVO>();
                newList.add(d);
                theMap.put(d.getGroupRole(), newList);
            }
        }

        // series for each entry in the map
        for (Map.Entry<String, List<MouseRTVShuttleVO>> thisMap : theMap.entrySet()) {

            //System.out.println("Creating LineChartSeries for: " + thisMap.getKey());
            // a chart series
            LineChartSeries thisSeries = new LineChartSeries();
            thisSeries.setLabel(thisMap.getKey());
            thisSeries.setShowLine(true);

            // sort by day
            Collections.sort(thisMap.getValue(), new Comparators.MouseRTVShuttleComparator());

            // initialize the series with time 0 FOR SOLID TUMORS
            for (MouseRTVShuttleVO mds : thisMap.getValue()) {
                thisSeries.set(mds.getDay(), mds.getRtv());
            }

            rtn.addSeries(thisSeries);

        }

        return rtn;

    }

    public LineChartModel renderMouseTimeToEvent(MouseGraphShuttleVO mgShut) {

        LineChartModel rtn = new LineChartModel();
        rtn.setTitle(mgShut.getCompoundName() + " " + mgShut.getCellLineName());

        Axis xAxis = rtn.getAxes().get(AxisType.X);
        Axis yAxis = rtn.getAxes().get(AxisType.Y);

        xAxis.setLabel("Day");
        xAxis.setMin(0d);

        yAxis.setLabel("Percent Event Free");
        yAxis.setMin(0d);
        yAxis.setMax(100d);

        HashMap<String, List<MouseSurvivalShuttleVO>> theMap = new HashMap<String, List<MouseSurvivalShuttleVO>>();

        for (MouseSurvivalShuttleVO d : mgShut.getSurvivals()) {
            if (theMap.containsKey(d.getGroupRole())) {
                theMap.get(d.getGroupRole()).add(d);
            } else {
                ArrayList<MouseSurvivalShuttleVO> newList = new ArrayList<MouseSurvivalShuttleVO>();
                newList.add(d);
                theMap.put(d.getGroupRole(), newList);
            }
        }

        // series for each entry in the map
        for (Map.Entry<String, List<MouseSurvivalShuttleVO>> thisMap : theMap.entrySet()) {

            //System.out.println("Creating LineChartSeries for: " + thisMap.getKey());
            // a chart series
            LineChartSeries thisSeries = new LineChartSeries();
            thisSeries.setLabel(thisMap.getKey());
            thisSeries.setShowLine(true);

            // sort by time-to-event
            Collections.sort(thisMap.getValue(), new Comparators.MouseSurvivalShuttleComparator());

            // start with 100% at time 0
            double curPct = 100;
            double curTime = 0;

            thisSeries.set(curTime, curPct);

            for (MouseSurvivalShuttleVO mds : thisMap.getValue()) {
                // connect to the next time                
                curTime = mds.getTimeToEvent();
                // have to back of just a smidge so that jqplot will drop the line,
                // it plots the most-recent data when there are multiple entries with the same x in a series
                thisSeries.set(curTime - 0.01, curPct);
                // drop down to the next value
                curPct = 100 * mds.getPercentEventFree();
                thisSeries.set(curTime, curPct);
            }

            rtn.addSeries(thisSeries);

        }

        return rtn;

    }

}
