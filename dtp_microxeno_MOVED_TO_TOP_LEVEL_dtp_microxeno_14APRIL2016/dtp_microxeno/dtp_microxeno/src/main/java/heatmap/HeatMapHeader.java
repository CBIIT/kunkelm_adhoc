/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heatmap;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;

/**
 *
 * @author mwkunkel
 */
public class HeatMapHeader implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Object clsObj;
    
    private String label1;
    private String label2;
    private String dataType;
    
    private String identString;
    private String rgbColor;
    
    public HeatMapHeader(Object objIn) {
        
        this.clsObj = objIn;
        
        this.label1 = "";
        this.label2 = "";
        this.dataType = "";
        
        this.identString = "";
        this.rgbColor = "white";
    }
    
    public HeatMapHeader(String label1, String label2) {
        this.label1 = label1;
        this.label2 = label2;
        this.dataType = "";
        this.rgbColor = "white";
        
    }
    
    public int getLabelLength() {
        return label1.length() + label2.length() + dataType.length();
    }
    
    public Object getClsObj() {
        return clsObj;
    }
    
    public void setClsObj(Object clsObj) {
        this.clsObj = clsObj;
    }
    
    public String getLabel1() {
        return label1;
    }
    
    public void setLabel1(String label1) {
        this.label1 = (label1 != null) ? label1 : "";
    }
    
    public String getLabel2() {
        return label2;
    }
    
    public void setLabel2(String label2) {
        this.label2 = (label2 != null) ? label2 : "";
    }
    
    public String getDataType() {
        return dataType;
    }
    
    public void setDataType(String dataType) {
        this.dataType = (dataType != null) ? dataType : "";
    }
    
    public String getIdentString() {
        return identString;
    }
    
    public void setIdentString(String identString) {
        this.identString = (identString != null) ? identString : "";
    }
    
    public String getRgbColor() {
        return rgbColor;
    }
    
    public void setRgbColor(String rgbColor) {
        this.rgbColor = rgbColor;
    }
    
}
