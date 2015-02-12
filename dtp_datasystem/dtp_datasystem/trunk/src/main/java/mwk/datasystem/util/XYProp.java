/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

/**
 *
 * @author mwkunkel
 */
public class XYProp implements Comparable {

    public Double xProp;
    public Double yProp;
    public String label;

    public XYProp(Double xProp, Double yProp, String label) {
        this.xProp = xProp;
        this.yProp = yProp;
        this.label = label;
    }

    public int compareTo(Object otherObject) {

        int rtn = 0;

        if (!(otherObject instanceof XYProp)) {

            rtn = 0;

        } else {

            XYProp other = (XYProp) otherObject;

            if (xProp == Double.NaN && yProp == Double.NaN){
                rtn = 0;
            }
            if (xProp != Double.NaN && yProp == Double.NaN){
                rtn = -1;
            }
            if (xProp == Double.NaN && yProp != Double.NaN){
                rtn = 1;
            }
            
            rtn = this.xProp.compareTo(other.xProp);

            if (rtn == 0) {
                rtn = this.yProp.compareTo(other.yProp);
            }
            
            // catch doubles and 

        }

        return rtn;
    }
}
