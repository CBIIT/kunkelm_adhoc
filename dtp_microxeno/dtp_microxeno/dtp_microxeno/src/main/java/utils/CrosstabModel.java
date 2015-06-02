/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mwkunkel
 */
public class CrosstabModel<X, Y, VAL> implements Serializable {

    private static final long serialVersionUID = 1L;
   
    private List<X> gridXheaders;       
    private List<Y> gridYheaders;      
    private VAL[][] gridXY;

    public CrosstabModel() {
        this.gridXheaders = new ArrayList<X>();
        this.gridYheaders = new ArrayList<Y>();
        this.gridXY = (VAL[][]) new Object[0][0];
    }

    public List<X> getGridXheaders() {
        return gridXheaders;
    }

    public void setGridXheaders(List<X> gridXheaders) {
        this.gridXheaders = gridXheaders;
    }

    public List<Y> getGridYheaders() {
        return gridYheaders;
    }

    public void setGridYheaders(List<Y> gridYheaders) {
        this.gridYheaders = gridYheaders;
    }

    public VAL[][] getGridXY() {
        return gridXY;
    }

    public void setGridXY(VAL[][] gridXY) {
        this.gridXY = gridXY;
    }
   
}
