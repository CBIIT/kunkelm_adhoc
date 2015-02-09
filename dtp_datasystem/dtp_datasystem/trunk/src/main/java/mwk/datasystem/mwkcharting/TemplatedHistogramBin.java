/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkcharting;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import mwk.datasystem.vo.HistogramDataInterface;


/**
 *
 * @author mwkunkel bins can be either discrete or continuous if discrete then
 * the bin value is matched
 */
public class TemplatedHistogramBin<T extends HistogramDataInterface> {

    private String label;
    private int count;
    private int countSelected;
    private double minCut;
    private double maxCut;
    private double minVal;
    private double maxVal;
    private ArrayList<T> binList;

    // a continuous bin must have min and max specified up front
    public TemplatedHistogramBin(double min, double max, String label) {

        this.label=label;
        //
        this.count = 0;
        this.countSelected = 0;
        //
        this.minCut = min;
        this.maxCut = max;
        // min and max are set to inverse limits to that the FIRST added object will set them
        this.minVal = Double.MAX_VALUE;
        this.maxVal = Double.MIN_VALUE;
        //
        this.binList = new ArrayList<T>();
    }

    public void debugBin() {

        NumberFormat nf2 = new DecimalFormat();
        nf2.setMaximumFractionDigits(2);

        System.out.println("debug bin: minCut: " + nf2.format(this.minCut) + " maxCut: " + nf2.format(this.maxCut) + " count: " + this.count + " countSelected: " + this.countSelected);
    }

    public boolean add(T incoming, double val) {

        //System.out.println("Value of incoming.getIsSelected() is: " + incoming.getIsSelected());
        boolean rtn = false;

        if (val >= minCut && val <= maxCut) {
            rtn = true;
            count++;
            if (incoming.getIsSelected() != null && incoming.getIsSelected()) {
                countSelected++;
            }
            binList.add(incoming);
            if (val < minVal) {
                minVal = val;
            }
            if (val > maxVal) {
                maxVal = val;
            }
        } else {
            rtn = false;
        }

        return rtn;

    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
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

    public double getMinCut() {
        return minCut;
    }

    public void setMinCut(double minCut) {
        this.minCut = minCut;
    }

    public double getMaxCut() {
        return maxCut;
    }

    public void setMaxCut(double maxCut) {
        this.maxCut = maxCut;
    }

    public double getMinVal() {
        return minVal;
    }

    public void setMinVal(double minVal) {
        this.minVal = minVal;
    }

    public double getMaxVal() {
        return maxVal;
    }

    public void setMaxVal(double maxVal) {
        this.maxVal = maxVal;
    }

    public ArrayList<T> getBinList() {
        return binList;
    }

    public void setBinList(ArrayList<T> binList) {
        this.binList = binList;
    }

}
