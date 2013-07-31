/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.pptp.util;

import java.io.Serializable;
import org.primefaces.model.chart.CartesianChartModel;

/**
 *
 * @author mwkunkel
 */
public class ExtendedCartesianChartModel extends CartesianChartModel implements Serializable {

    private String title;
    private String xAxisLabel;
    private String yAxisLabel;
    private Double xAxisMin;
    private Double xAxisMax;
    private Double yAxisMin;
    private Double yAxisMax;

    public ExtendedCartesianChartModel() {
    }

    public ExtendedCartesianChartModel(String title, String xAxisLabel, String yAxisLabel, Double xAxisMin, Double xAxisMax, Double yAxisMin, Double yAxisMax) {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
        this.yAxisLabel = yAxisLabel;
        this.xAxisMin = xAxisMin;
        this.xAxisMax = xAxisMax;
        this.yAxisMin = yAxisMin;
        this.yAxisMax = yAxisMax;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    public String getyAxisLabel() {
        return yAxisLabel;
    }

    public void setyAxisLabel(String yAxisLabel) {
        this.yAxisLabel = yAxisLabel;
    }

    public Double getxAxisMin() {
        return xAxisMin;
    }

    public void setxAxisMin(Double xAxisMin) {
        this.xAxisMin = xAxisMin;
    }

    public Double getxAxisMax() {
        return xAxisMax;
    }

    public void setxAxisMax(Double xAxisMax) {
        this.xAxisMax = xAxisMax;
    }

    public Double getyAxisMin() {
        return yAxisMin;
    }

    public void setyAxisMin(Double yAxisMin) {
        this.yAxisMin = yAxisMin;
    }

    public Double getyAxisMax() {
        return yAxisMax;
    }

    public void setyAxisMax(Double yAxisMax) {
        this.yAxisMax = yAxisMax;
    }
    
}
