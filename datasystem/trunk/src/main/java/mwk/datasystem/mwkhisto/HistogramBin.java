/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkhisto;

import java.util.ArrayList;
import mwk.datasystem.vo.CmpdListMemberVO;

/**
 *
 * @author mwkunkel
 * bins can be either discrete or continuous
 * if discrete then the bin value is matched
 */
public class HistogramBin {
  
  private int count;
  private int countSelected;
  private double minCut;
  private double maxCut;
  private double minVal;
  private double maxVal;
  private ArrayList<CmpdListMemberVO> binList;

  // a continuous bin must have min and max specified up front
  public HistogramBin(double min, double max) {
    
    this.count = 0;
    this.countSelected = 0;
    this.minCut = min;
    this.maxCut = max;
    // min and max are set to inverse limits to that the FIRST added object will set them
    this.minVal = Double.MAX_VALUE;
    this.maxVal = Double.MIN_VALUE;
//
    this.binList = new ArrayList<CmpdListMemberVO>();
  }

  public boolean add(CmpdListMemberVO incoming, double val) {
    
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

  public ArrayList<CmpdListMemberVO> getBinList() {
    return binList;
  }

  public void setBinList(ArrayList<CmpdListMemberVO> binList) {
    this.binList = binList;
  }
  
}
