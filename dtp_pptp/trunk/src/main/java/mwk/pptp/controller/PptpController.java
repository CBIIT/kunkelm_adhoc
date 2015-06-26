/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.pptp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import mwk.pptp.util.Comparators.MouseDataShuttleComparator;
import mwk.pptp.util.Comparators.MouseRTVShuttleComparator;
import mwk.pptp.util.Comparators.MouseSurvivalShuttleComparator;
import mwk.pptp.util.ExtendedCartesianChartModel;
import mwk.pptp.util.HelperCellLineGroup;
import mwk.pptp.vo.CellLineGroupVO;
import mwk.pptp.vo.MouseDataShuttleVO;
import mwk.pptp.vo.MouseRTVShuttleVO;
import mwk.pptp.vo.MouseGraphShuttleVO;
import mwk.pptp.vo.MouseSurvivalShuttleVO;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class PptpController implements Serializable {

    private List<String> pptpIdentifiers;
    private List<String> drugNames;
    private List<String> cellTypes;
    private List<String> cellLines;
    private List<String> genes;
    private List<String> geneNames;
    private List<String> snps;
    // search results
    private Boolean disbleCellLineGroups;
    private List<CellLineGroupVO> cellLineGroups;
    private CellLineGroupVO[] selectedCellLineGroups;
    private List<CellLineGroupVO> filteredCellLineGroups;
//
    private List<MouseGraphShuttleVO> graphShuttles;
//
    private ExtendedCartesianChartModel mouseDataChart;
    private ExtendedCartesianChartModel mouseRtvChart;
    private ExtendedCartesianChartModel mouseTimesToEventChart;

    public PptpController() {

        this.disbleCellLineGroups = Boolean.TRUE;
        this.cellLineGroups = new ArrayList<CellLineGroupVO>();
        //this.filteredCellLineGroups = new ArrayList<CellLineGroupVO>();
        this.selectedCellLineGroups = new CellLineGroupVO[0];

        this.graphShuttles = new ArrayList<MouseGraphShuttleVO>();

        this.pptpIdentifiers = new ArrayList<String>();
        this.drugNames = new ArrayList<String>();
        this.cellTypes = new ArrayList<String>();
        this.cellLines = new ArrayList<String>();
        this.genes = new ArrayList<String>();
        this.geneNames = new ArrayList<String>();
        this.snps = new ArrayList<String>();

    }

    //
    //
    //
    public String reset() {

        this.disbleCellLineGroups = Boolean.TRUE;
        this.cellLineGroups = new ArrayList<CellLineGroupVO>();
        //this.filteredCellLineGroups = new ArrayList<CellLineGroupVO>();
        this.selectedCellLineGroups = new CellLineGroupVO[0];

        this.graphShuttles = new ArrayList<MouseGraphShuttleVO>();

        this.pptpIdentifiers = new ArrayList<String>();
        this.drugNames = new ArrayList<String>();
        this.cellTypes = new ArrayList<String>();
        this.cellLines = new ArrayList<String>();
        this.genes = new ArrayList<String>();
        this.geneNames = new ArrayList<String>();
        this.snps = new ArrayList<String>();

        return null;
    }

    public ExtendedCartesianChartModel renderMouseData(MouseGraphShuttleVO mgShut) {

        //System.out.println("IN renderMouseData in pptpController");
        ExtendedCartesianChartModel thisModel = new ExtendedCartesianChartModel();
        thisModel.setTitle(mgShut.getCompoundName() + " " + mgShut.getCellLineName());

        thisModel.setxAxisLabel("Day");
        thisModel.setxAxisMin(0d);

        // Leukemias 
        if (mgShut.getCellLine().startsWith("G")) {
            thisModel.setyAxisLabel("Percent Human CD45");
            thisModel.setyAxisMin(0d);
            thisModel.setyAxisMax(25d);
        } else {
            thisModel.setyAxisLabel("Log10 Tumor Volume (cc)");
            thisModel.setyAxisMin(-1d);
            thisModel.setyAxisMax(1d);
        }

        HashMap<String, List<MouseDataShuttleVO>> theMap = new HashMap<String, List<MouseDataShuttleVO>>();

        for (MouseDataShuttleVO d : mgShut.getDatas()) {
            if (theMap.containsKey(d.getGroupRole() + " " + d.getMouseNumber())) {
                //System.out.println("Adding to existing ArrayList for: " + d.getGroupRole() + " " + d.getMouseNumber());
                theMap.get(d.getGroupRole() + " " + d.getMouseNumber()).add(d);
            } else {
                //System.out.println("Creating new ArrayList for: " + d.getGroupRole() + " " + d.getMouseNumber());
                ArrayList<MouseDataShuttleVO> newList = new ArrayList<MouseDataShuttleVO>();
                newList.add(d);
                theMap.put(d.getGroupRole() + " " + d.getMouseNumber(), newList);
            }
        }

        // series for each entry in the map
        System.out.println("Size of theMap in renderMouseData in PptpController: " + theMap.size());

        for (Map.Entry<String, List<MouseDataShuttleVO>> thisMap : theMap.entrySet()) {

            //System.out.println("Creating LineChartSeries for: " + thisMap.getKey());
            // a chart series
            LineChartSeries thisSeries = new LineChartSeries();
            thisSeries.setLabel(thisMap.getKey());
            thisSeries.setShowLine(true);

            // sort by day
            Collections.sort(thisMap.getValue(), new MouseDataShuttleComparator());

            for (MouseDataShuttleVO mds : thisMap.getValue()) {
                thisSeries.set(mds.getDay(), mds.getValue());
            }

            thisModel.addSeries(thisSeries);

        }

        this.mouseDataChart = thisModel;

        return thisModel;

    }

    public ExtendedCartesianChartModel renderMouseRtv(MouseGraphShuttleVO mgShut) {

        ExtendedCartesianChartModel thisModel = new ExtendedCartesianChartModel();
        thisModel.setTitle(mgShut.getCompoundName() + " " + mgShut.getCellLineName());

        thisModel.setxAxisLabel("Day");
        thisModel.setxAxisMin(0d);

        // Leukemias 
        if (mgShut.getCellLine().startsWith("G")) {
            thisModel.setyAxisLabel("Median Percent Human CD45");
            thisModel.setyAxisMin(0d);
            thisModel.setyAxisMax(25d);
        } else {
            thisModel.setyAxisLabel("Relative Median Tumor Volume");
            thisModel.setyAxisMin(0d);
            thisModel.setyAxisMax(4d);
        }

        HashMap<String, List<MouseRTVShuttleVO>> theMap = new HashMap<String, List<MouseRTVShuttleVO>>();

        for (MouseRTVShuttleVO d : mgShut.getRtvs()) {
            if (theMap.containsKey(d.getGroupRole())) {
                theMap.get(d.getGroupRole()).add(d);
            } else {
                ArrayList<MouseRTVShuttleVO> newList = new ArrayList<MouseRTVShuttleVO>();
                newList.add(d);
                theMap.put(d.getGroupRole(), newList);
            }
        }

        // series for each entry in the map
        for (Map.Entry<String, List<MouseRTVShuttleVO>> thisMap : theMap.entrySet()) {

            //System.out.println("Creating LineChartSeries for: " + thisMap.getKey());
            // a chart series
            LineChartSeries thisSeries = new LineChartSeries();
            thisSeries.setLabel(thisMap.getKey());
            thisSeries.setShowLine(true);

            // sort by day
            Collections.sort(thisMap.getValue(), new MouseRTVShuttleComparator());

            // initialize the series with time 0 FOR SOLID TUMORS
            for (MouseRTVShuttleVO mds : thisMap.getValue()) {
                thisSeries.set(mds.getDay(), mds.getRtv());
            }

            thisModel.addSeries(thisSeries);

        }

        this.mouseRtvChart = thisModel;

        return thisModel;

    }

    public ExtendedCartesianChartModel renderMouseTimeToEvent(MouseGraphShuttleVO mgShut) {

        ExtendedCartesianChartModel thisModel = new ExtendedCartesianChartModel();
        thisModel.setTitle(mgShut.getCompoundName() + " " + mgShut.getCellLineName());

        thisModel.setxAxisLabel("Day");
        thisModel.setyAxisLabel("Percent Event Free");
        thisModel.setxAxisMin(0d);
        thisModel.setyAxisMin(0d);
        thisModel.setyAxisMax(100d);

        HashMap<String, List<MouseSurvivalShuttleVO>> theMap = new HashMap<String, List<MouseSurvivalShuttleVO>>();

        for (MouseSurvivalShuttleVO d : mgShut.getSurvivals()) {
            if (theMap.containsKey(d.getGroupRole())) {
                theMap.get(d.getGroupRole()).add(d);
            } else {
                ArrayList<MouseSurvivalShuttleVO> newList = new ArrayList<MouseSurvivalShuttleVO>();
                newList.add(d);
                theMap.put(d.getGroupRole(), newList);
            }
        }

        // series for each entry in the map
        for (Map.Entry<String, List<MouseSurvivalShuttleVO>> thisMap : theMap.entrySet()) {

            //System.out.println("Creating LineChartSeries for: " + thisMap.getKey());
            // a chart series
            LineChartSeries thisSeries = new LineChartSeries();
            thisSeries.setLabel(thisMap.getKey());
            thisSeries.setShowLine(true);

            // sort by time-to-event
            Collections.sort(thisMap.getValue(), new MouseSurvivalShuttleComparator());

//            proof of principle
//            thisSeries.set(0,100);
//            thisSeries.set(13.9,100);
//            thisSeries.set(14.1,80);
//            thisSeries.set(21.9,80);
//            thisSeries.set(22.1,70);
//            thisSeries.set(24.9,70);
//            thisSeries.set(25.1,60);
//            thisSeries.set(27.9,60);
//            thisSeries.set(28.1,50);
//            thisSeries.set(31.9,50);
//            thisSeries.set(32.1,40);
            // start with 100% at time 0
            double curPct = 100;
            double curTime = 0;

            thisSeries.set(curTime, curPct);

            for (MouseSurvivalShuttleVO mds : thisMap.getValue()) {
                // connect to the next time                
                curTime = mds.getTimeToEvent();
                // have to back of just a smidge so that jqplot will drop the line,
                // it plots the most-recent data when there are multiple entries with the same x in a series
                thisSeries.set(curTime - 0.01, curPct);
                // drop down to the next value
                curPct = 100 * mds.getPercentEventFree();
                thisSeries.set(curTime, curPct);
            }

            thisModel.addSeries(thisSeries);

        }

        this.mouseTimesToEventChart = thisModel;

        return thisModel;

    }

    public String performShowGraphs() {

        List<Long> cellLineGroupIds = new ArrayList<Long>();

        for (CellLineGroupVO clgVO : this.selectedCellLineGroups) {
            cellLineGroupIds.add(clgVO.getCellLineGroupId());
        }

        HelperCellLineGroup helper = new HelperCellLineGroup();

        this.graphShuttles = helper.getGraphs(cellLineGroupIds);

        for (MouseGraphShuttleVO thisG : this.graphShuttles) {
            System.out.println(thisG.getCellLineName() + " " + thisG.getCompoundName() + " datas:" + thisG.getDatas().size() + " survials:" + thisG.getSurvivals().size() + " RTVs:" + thisG.getRtvs().size());
        }

        return null;

    }

    public String performSearch() {
//
//    List<String> pptpIdentifierList = new ArrayList<String>();
////        pptpIdentifierList.add("0501");
//
//    List<String> drugNameList = new ArrayList<String>();
//    drugNameList.add("Cisplatin");
//
//    List<String> cellNameList = new ArrayList<String>();
////        cellNameList.add("ALL-4");
////        cellNameList.add("ALL-8");
//
//    List<String> cellTypeList = new ArrayList<String>();
//    cellTypeList.add("Ewing sarcoma");
//    cellTypeList.add("Glioblastoma");

        HelperCellLineGroup helper = new HelperCellLineGroup();

        this.cellLineGroups = helper.searchCellLineGroups(this.pptpIdentifiers, this.drugNames, this.cellLines, this.cellTypes);

        this.disbleCellLineGroups = Boolean.FALSE;

        return "/webpages/searchResults.xhtml?faces-redirect=true";

    }

    // GETTERS and SETTERS
    public List<String> getPptpIdentifiers() {
        return pptpIdentifiers;
    }

    public void setPptpIdentifiers(List<String> pptpIdentifiers) {
        this.pptpIdentifiers = pptpIdentifiers;
    }

    public List<String> getDrugNames() {
        return drugNames;
    }

    public void setDrugNames(List<String> drugNames) {
        this.drugNames = drugNames;
    }

    public List<String> getCellTypes() {
        return cellTypes;
    }

    public void setCellTypes(List<String> cellTypes) {
        this.cellTypes = cellTypes;
    }

    public List<String> getCellLines() {
        return cellLines;
    }

    public void setCellLines(List<String> cellLines) {
        this.cellLines = cellLines;
    }

    public List<String> getGenes() {
        return genes;
    }

    public void setGenes(List<String> genes) {
        this.genes = genes;
    }

    public List<String> getGeneNames() {
        return geneNames;
    }

    public void setGeneNames(List<String> geneNames) {
        this.geneNames = geneNames;
    }

    public List<String> getSnps() {
        return snps;
    }

    public void setSnps(List<String> snps) {
        this.snps = snps;
    }

    public Boolean getDisbleCellLineGroups() {
        return disbleCellLineGroups;
    }

    public void setDisbleCellLineGroups(Boolean disbleCellLineGroups) {
        this.disbleCellLineGroups = disbleCellLineGroups;
    }

    public List<CellLineGroupVO> getCellLineGroups() {
        return cellLineGroups;
    }

    public void setCellLineGroups(List<CellLineGroupVO> cellLineGroups) {
        this.cellLineGroups = cellLineGroups;
    }

    public CellLineGroupVO[] getSelectedCellLineGroups() {
        return selectedCellLineGroups;
    }

    public void setSelectedCellLineGroups(CellLineGroupVO[] selectedCellLineGroups) {
        this.selectedCellLineGroups = selectedCellLineGroups;
    }

    public List<CellLineGroupVO> getFilteredCellLineGroups() {
        return filteredCellLineGroups;
    }

    public ExtendedCartesianChartModel getMouseDataChart() {
        return mouseDataChart;
    }

    public void setMouseDataChart(ExtendedCartesianChartModel mouseDataChart) {
        this.mouseDataChart = mouseDataChart;
    }

    public ExtendedCartesianChartModel getMouseRtvChart() {
        return mouseRtvChart;
    }

    public void setMouseRtvChart(ExtendedCartesianChartModel mouseRtvChart) {
        this.mouseRtvChart = mouseRtvChart;
    }

    public ExtendedCartesianChartModel getMouseTimesToEventChart() {
        return mouseTimesToEventChart;
    }

    public void setMouseTimesToEventChart(ExtendedCartesianChartModel mouseTimesToEventChart) {
        this.mouseTimesToEventChart = mouseTimesToEventChart;
    }

    public void setFilteredCellLineGroups(List<CellLineGroupVO> filteredCellLineGroups) {
        this.filteredCellLineGroups = filteredCellLineGroups;
    }

    public List<MouseGraphShuttleVO> getGraphShuttles() {
        return graphShuttles;
    }

    public void setGraphShuttles(List<MouseGraphShuttleVO> graphShuttles) {
        this.graphShuttles = graphShuttles;
    }
}
