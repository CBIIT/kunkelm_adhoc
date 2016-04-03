/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package meangraph;

import java.io.Serializable;
import mwk.microxeno.vo.AffymetrixIdentifierVO;
import mwk.microxeno.vo.PassageAvgSetVO;

import org.apache.log4j.Logger;
import org.primefaces.model.chart.HorizontalBarChartModel;

/**
 *
 * @author mwkunkel Extend primefaces model with some useful parameters to hand
 * to the primefaces line and bar charts and includes references to VO objects
 */
public class MeanGraph implements Serializable {

    private static final Logger lgr = Logger.getLogger("GLOBAL");

    private HorizontalBarChartModel meanGraphChart;

    private String title1;
    private String title2;
    private String title3;

    private String resultLabel;
    private Double correlation;
    private Integer countCommonCellLines;
    //
    private PassageAvgSetVO dataSet;
    private AffymetrixIdentifierVO affymetrixIdentifier;

    private String json;

    /**
     *
     */
    public MeanGraph() {
        this.meanGraphChart = new HorizontalBarChartModel();
    }

    public HorizontalBarChartModel getMeanGraphChart() {
        return meanGraphChart;
    }

    public void setMeanGraphChart(HorizontalBarChartModel meanGraphChart) {
        this.meanGraphChart = meanGraphChart;
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

    public String getResultLabel() {
        return resultLabel;
    }

    public void setResultLabel(String resultLabel) {
        this.resultLabel = resultLabel;
    }

    public Double getCorrelation() {
        return correlation;
    }

    public void setCorrelation(Double correlation) {
        this.correlation = correlation;
    }

    public Integer getCountCommonCellLines() {
        return countCommonCellLines;
    }

    public void setCountCommonCellLines(Integer countCommonCellLines) {
        this.countCommonCellLines = countCommonCellLines;
    }

    public PassageAvgSetVO getDataSet() {
        return dataSet;
    }

    public void setDataSet(PassageAvgSetVO dataSet) {
        this.dataSet = dataSet;
    }

    public AffymetrixIdentifierVO getAffymetrixIdentifier() {
        return affymetrixIdentifier;
    }

    public void setAffymetrixIdentifier(AffymetrixIdentifierVO affymetrixIdentifier) {
        this.affymetrixIdentifier = affymetrixIdentifier;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

}
