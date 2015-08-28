/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import java.io.Serializable;
import org.primefaces.model.chart.CartesianChartModel;

/**
 *
 * @author mwkunkel
 */
public class ExtendedCartesianChartModel extends CartesianChartModel implements Serializable {

    private String title;
    private String labelAxisX;
    private String labelAxisY;
    private String color;

    public ExtendedCartesianChartModel(ExtendedCartesianChartModel incoming) {
        this.title = "notSet";
        this.color = "black";
    }

    public ExtendedCartesianChartModel() {
        super();
        this.title = "notSet";
        this.color = "black";
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLabelAxisX() {
        return labelAxisX;
    }

    public void setLabelAxisX(String labelAxisX) {
        this.labelAxisX = labelAxisX;
    }

    public String getLabelAxisY() {
        return labelAxisY;
    }

    public void setLabelAxisY(String labelAxisY) {
        this.labelAxisY = labelAxisY;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }
}