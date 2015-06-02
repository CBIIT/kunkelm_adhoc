/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package heatmap;

import java.io.Serializable;

/**
 *
 * @author mwkunkel
 */
public class HeatMapCell implements Serializable {

    private static final long serialVersionUID = 1L;

    protected Boolean isNullValue;
    //
    protected String valueLabel;
    protected Double valueValue;
    protected String valueColor;
    //
    protected String unitDeltaLabel;
    protected Double unitDeltaValue;
    protected String unitDeltaColor;
    //
    protected String correlationLabel;
    protected Double correlationValue;
    protected String correlationColor;
    //

   
    public Boolean getIsNullValue() {
        return isNullValue;
    }

    public void setIsNullValue(Boolean isNullValue) {
        this.isNullValue = isNullValue;
    }

    public String getValueLabel() {
        return valueLabel;
    }

    public void setValueLabel(String valueLabel) {
        this.valueLabel = valueLabel;
    }

    public Double getValueValue() {
        return valueValue;
    }

    public void setValueValue(Double valueValue) {
        this.valueValue = valueValue;
    }

    public String getValueColor() {
        return valueColor;
    }

    public void setValueColor(String valueColor) {
        this.valueColor = valueColor;
    }

    public String getUnitDeltaLabel() {
        return unitDeltaLabel;
    }

    public void setUnitDeltaLabel(String unitDeltaLabel) {
        this.unitDeltaLabel = unitDeltaLabel;
    }

    public Double getUnitDeltaValue() {
        return unitDeltaValue;
    }

    public void setUnitDeltaValue(Double unitDeltaValue) {
        this.unitDeltaValue = unitDeltaValue;
    }

    public String getUnitDeltaColor() {
        return unitDeltaColor;
    }

    public void setUnitDeltaColor(String unitDeltaColor) {
        this.unitDeltaColor = unitDeltaColor;
    }

    public String getCorrelationLabel() {
        return correlationLabel;
    }

    public void setCorrelationLabel(String correlationLabel) {
        this.correlationLabel = correlationLabel;
    }

    public Double getCorrelationValue() {
        return correlationValue;
    }

    public void setCorrelationValue(Double correlationValue) {
        this.correlationValue = correlationValue;
    }

    public String getCorrelationColor() {
        return correlationColor;
    }

    public void setCorrelationColor(String correlationColor) {
        this.correlationColor = correlationColor;
    }

}
