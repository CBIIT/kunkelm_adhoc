/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkhistoscat;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

/**
 *
 * @author mwkunkel bins can be either discrete or continuous if discrete then
 * the bin value is matched
 */
public class ScatterPlotSeries<X> {

    private String title;

    private String xPropertyName;
    private String yPropertyName;

    private int count;
    private int countSelected;

    private ArrayList<ScatterPlotPoint<X>> pointList;

    public ScatterPlotSeries(String title, String xPropertyName, String yPropertyName) {
        this.title = title;
        this.xPropertyName = xPropertyName;
        this.yPropertyName = yPropertyName;
        //
        this.count = 0;
        this.countSelected = 0;
        this.pointList = new ArrayList<ScatterPlotPoint<X>>();
    }

    public void add(ScatterPlotPoint scp) {

        count++;
        if (scp.isIsSelected()) {
            countSelected++;
        }
        pointList.add(scp);

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

    public ArrayList<ScatterPlotPoint<X>> getPointList() {
        return pointList;
    }

    public void setPointList(ArrayList<ScatterPlotPoint<X>> pointList) {
        this.pointList = pointList;
    }

//</editor-fold>    
}
