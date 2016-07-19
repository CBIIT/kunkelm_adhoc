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
public class ScatterPlotPoint<X> {

    private X obj;
    private double xVal;
    private double yVal;

    private boolean isSelected;

    public ScatterPlotPoint(X obj, String xPropertyName, String yPropertyName) {

        Double xVal = ScatterPlotUtil.getDoubleProp(obj, xPropertyName);
        Double yVal = ScatterPlotUtil.getDoubleProp(obj, yPropertyName);
        boolean isSelected = ScatterPlotUtil.getIsSelected(obj);

        if (xVal != null && !xVal.isNaN() && yVal != null && !yVal.isNaN()) {
            this.obj = obj;
            this.xVal = xVal;
            this.yVal = yVal;
            this.isSelected = ScatterPlotUtil.getIsSelected(obj);
        } else {
            this.obj = null;
        }
    }
    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">

    public X getObj() {
        return obj;
    }

    public void setObj(X obj) {
        this.obj = obj;
    }

    public double getxVal() {
        return xVal;
    }

    public void setxVal(double xVal) {
        this.xVal = xVal;
    }

    public double getyVal() {
        return yVal;
    }

    public void setyVal(double yVal) {
        this.yVal = yVal;
    }

    public boolean isIsSelected() {
        return isSelected;
    }

    public void setIsSelected(boolean isSelected) {
        this.isSelected = isSelected;
    }

//</editor-fold>
}
