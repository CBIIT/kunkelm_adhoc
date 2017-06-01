/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkhistoscat;

import static java.lang.System.getProperty;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.TreeMap;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author mwkunkel
 */
public class ScatterPlot<X> {

    private String title;
    private String xPropertyName;
    private String yPropertyName;

    private ScatterPlotSeries<X> series;
    private ScatterPlotSeries<X> selectedSeries;

    private int count;
    private int countSelected;

    public ScatterPlot(String title, String xPropertyName, String yPropertyName) {
        this.title = title;
        this.xPropertyName = xPropertyName;
        this.yPropertyName = yPropertyName;
        this.series = new ScatterPlotSeries<X>("not selected", xPropertyName, yPropertyName);
        this.selectedSeries = new ScatterPlotSeries<X>("selected", xPropertyName, yPropertyName);
    }

    public void add(X x) {

        ScatterPlotPoint scp = new ScatterPlotPoint(x, xPropertyName, yPropertyName);

        if (scp.getObj() != null) {
            count++;
            if (scp.isIsSelected()) {
                countSelected++;
                selectedSeries.add(new ScatterPlotPoint<X>(x, xPropertyName, yPropertyName));
            } else {
                series.add(new ScatterPlotPoint<X>(x, xPropertyName, yPropertyName));
            }
        }

    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getxPropertyName() {
        return xPropertyName;
    }

    public void setxPropertyName(String xPropertyName) {
        this.xPropertyName = xPropertyName;
    }

    public String getyPropertyName() {
        return yPropertyName;
    }

    public void setyPropertyName(String yPropertyName) {
        this.yPropertyName = yPropertyName;
    }

    public ScatterPlotSeries<X> getSeries() {
        return series;
    }

    public void setSeries(ScatterPlotSeries<X> series) {
        this.series = series;
    }

    public ScatterPlotSeries<X> getSelectedSeries() {
        return selectedSeries;
    }

    public void setSelectedSeries(ScatterPlotSeries<X> selectedSeries) {
        this.selectedSeries = selectedSeries;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getCountSelected() {
        return countSelected;
    }

    public void setCountSelected(int countSelected) {
        this.countSelected = countSelected;
    }

//</editor-fold>   
}
