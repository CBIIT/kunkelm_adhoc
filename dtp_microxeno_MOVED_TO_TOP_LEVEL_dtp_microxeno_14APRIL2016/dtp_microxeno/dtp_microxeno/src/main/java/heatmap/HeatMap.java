/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heatmap;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mwkunkel
 */
public class HeatMap implements Serializable {

    private static final long serialVersionUID = 1L;
    //
    protected String title1;
    protected String title2;
    protected String title3;
    protected String gson;
    //
    protected String dataType1;
    protected String dataType2;
    protected String dataType3;
    //
    protected Double greenValue;
    protected Double yellowValue;
    protected Double redValue;
    //
    protected Double greenUnitDeltaValue;
    protected Double yellowUnitDeltaValue;
    protected Double redUnitDeltaValue;
    //
    protected Double lowCut3;
    protected Double midCut3;
    protected Double highCut3;
    //  
    protected List<HeatMapHeader> gridXheaderList;        
    protected List<HeatMapHeader> gridYheaderList;
    // 
    protected Integer maxLenXhead;
    protected Integer maxLenYhead;
    //
    protected HeatMapCell[][] gridXY;

    public HeatMap() {
        this.gridXheaderList = new ArrayList<HeatMapHeader>();
        this.gridYheaderList = new ArrayList<HeatMapHeader>();
        this.gridXY = new HeatMapCell[0][0];
    }

    public String getTitle1() {
        return title1;
    }

    public void setTitle1(String title1) {
        this.title1 = title1;
    }

    public String getTitle2() {
        return title2;
    }

    public void setTitle2(String title2) {
        this.title2 = title2;
    }

    public String getTitle3() {
        return title3;
    }

    public void setTitle3(String title3) {
        this.title3 = title3;
    }

    public String getGson() {
        return gson;
    }

    public void setGson(String gson) {
        this.gson = gson;
    }

    public String getDataType1() {
        return dataType1;
    }

    public void setDataType1(String dataType1) {
        this.dataType1 = dataType1;
    }

    public String getDataType2() {
        return dataType2;
    }

    public void setDataType2(String dataType2) {
        this.dataType2 = dataType2;
    }

    public String getDataType3() {
        return dataType3;
    }

    public void setDataType3(String dataType3) {
        this.dataType3 = dataType3;
    }

    public Double getGreenValue() {
        return greenValue;
    }

    public void setGreenValue(Double greenValue) {
        this.greenValue = greenValue;
    }

    public Double getYellowValue() {
        return yellowValue;
    }

    public void setYellowValue(Double yellowValue) {
        this.yellowValue = yellowValue;
    }

    public Double getRedValue() {
        return redValue;
    }

    public void setRedValue(Double redValue) {
        this.redValue = redValue;
    }

    public Double getGreenUnitDeltaValue() {
        return greenUnitDeltaValue;
    }

    public void setGreenUnitDeltaValue(Double greenUnitDeltaValue) {
        this.greenUnitDeltaValue = greenUnitDeltaValue;
    }

    public Double getYellowUnitDeltaValue() {
        return yellowUnitDeltaValue;
    }

    public void setYellowUnitDeltaValue(Double yellowUnitDeltaValue) {
        this.yellowUnitDeltaValue = yellowUnitDeltaValue;
    }

    public Double getRedUnitDeltaValue() {
        return redUnitDeltaValue;
    }

    public void setRedUnitDeltaValue(Double redUnitDeltaValue) {
        this.redUnitDeltaValue = redUnitDeltaValue;
    }

    public Double getLowCut3() {
        return lowCut3;
    }

    public void setLowCut3(Double lowCut3) {
        this.lowCut3 = lowCut3;
    }

    public Double getMidCut3() {
        return midCut3;
    }

    public void setMidCut3(Double midCut3) {
        this.midCut3 = midCut3;
    }

    public Double getHighCut3() {
        return highCut3;
    }

    public void setHighCut3(Double highCut3) {
        this.highCut3 = highCut3;
    }

    public List<HeatMapHeader> getGridXheaderList() {
        return gridXheaderList;
    }

    public void setGridXheaderList(List<HeatMapHeader> gridXheaderList) {
        this.gridXheaderList = gridXheaderList;
    }

    public List<HeatMapHeader> getGridYheaderList() {
        return gridYheaderList;
    }

    public void setGridYheaderList(List<HeatMapHeader> gridYheaderList) {
        this.gridYheaderList = gridYheaderList;
    }

    public Integer getMaxLenXhead() {
        return maxLenXhead;
    }

    public void setMaxLenXhead(Integer maxLenXhead) {
        this.maxLenXhead = maxLenXhead;
    }

    public Integer getMaxLenYhead() {
        return maxLenYhead;
    }

    public void setMaxLenYhead(Integer maxLenYhead) {
        this.maxLenYhead = maxLenYhead;
    }

    public HeatMapCell[][] getGridXY() {
        return gridXY;
    }

    public void setGridXY(HeatMapCell[][] gridXY) {
        this.gridXY = gridXY;
    }

}
