/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import mwk.datasystem.mwkcharting.Histogram;
import mwk.datasystem.mwkcharting.HistogramBin;
import mwk.datasystem.util.HistogramChartUtil;
import mwk.datasystem.util.ScatterPlotChartUtil;
import mwk.datasystem.vo.CmpdListMemberVO;
import org.primefaces.component.chart.Chart;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class HistogramController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    // reach-through to sessionController
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    // reach-through to listManagerController
    @ManagedProperty(value = "#{listManagerController}")
    private ListManagerController listManagerController;

    public void setListManagerController(ListManagerController listManagerController) {
        this.listManagerController = listManagerController;
    }

    private List<Histogram> histoModel;
    private ArrayList<LineChartModel> scatterPlotModel;
    private List<String> parametersPchem;

    private String histogramSizeString;
    private String scatterPlotSizeString;
    private String structureSizeString;

    private Integer histogramSize;
    private Integer scatterPlotSize;
    private Integer structureSize;

    public HistogramController() {

        this.histogramSizeString = "medium";
        this.scatterPlotSizeString = "medium";
        this.structureSizeString = "medium";
        this.histogramSize = 300;
        this.scatterPlotSize = 300;
        this.structureSize = 200;

    }

    //
    public void itemSelectHistogram(ItemSelectEvent event) {

        System.out.println("Now in itemSelectHistogram in HistogramController");

        Object eventSrc = event.getSource();

        System.out.println("eventSrc is: " + eventSrc.getClass().toString());

        Chart eventChart = (Chart) event.getSource();

        BarChartModel bcm = (BarChartModel) eventChart.getModel();

        String histoTitle = bcm.getTitle();
        
        ChartSeries selSeries = bcm.getSeries().get(event.getSeriesIndex());
        
        String seriesLabel = selSeries.getLabel();

        Set<Object> keySet = selSeries.getData().keySet();

        String[] keyArray = keySet.toArray(new String[keySet.size()]);

        String histoBarLabel = keyArray[event.getItemIndex()];
        
        
        
    //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_INFO, "Item selected ",
                "Event Item Index: " + event.getItemIndex()
                + "\n" + ", Series Index: " + event.getSeriesIndex()
                + "\n" + ", Histogram Ident: " + histoTitle
                + "\n" + ", Histogram Bar Ident: " + histoBarLabel);

        FacesContext.getCurrentInstance().addMessage(null, msg);

        // fetch the included compounds using the indexes
        Histogram selHisto = null;

        boolean found = false;

        for (Histogram h : this.histoModel) {
            if (h.getPropertyName().equals(histoTitle)) {
                selHisto = h;
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Couldn't find histogram for property: " + histoTitle);
        }
        
        //--------------------DEBUG
        
        System.out.println("HistogramBins: ");
        System.out.println("size: " + selHisto.getBinList().size());        
        for (HistogramBin hb : selHisto.getBinList()){
            hb.debugBin();
        }
        
        System.out.println("BarChart bars: ");
        System.out.println("selSeries label: " + seriesLabel);
        System.out.println("size: " + keyArray.length);
        for (int i = 0; i < keyArray.length; i++){
            System.out.println(keyArray[i]);
        }
                
        //--------------------DEBUG
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        
        

        // get the appropriate bin
        HistogramBin curBin = selHisto.getBinList().get(event.getItemIndex());
        
        System.out.println("histogramBarLabel: " + histoBarLabel);
        curBin.debugBin();

        if (curBin.getBinList() == null || curBin.getBinList().isEmpty()) {
            System.out.println("curBin has an empty binList");
            System.out.println("event.itemIndex: " + event.getItemIndex());
            selHisto.debugBins();
        }

        ArrayList<CmpdListMemberVO> clmList = curBin.getBinList();

        // reset flags on all listMembers in selectedList    
        for (CmpdListMemberVO clmVO : listManagerController.getActiveList().getCmpdListMembers()) {
            clmVO.setIsSelected(Boolean.FALSE);
        }

        // this is the new selection
        listManagerController.setSelectedActiveListMembers(clmList);

        // which needs updating
        for (CmpdListMemberVO clmVO : listManagerController.getSelectedActiveListMembers()) {
            clmVO.setIsSelected(new Boolean(Boolean.TRUE));
        }

        // regenerate the histoModel
        this.histoModel = HistogramChartUtil.doHistograms(listManagerController.getActiveList().getCmpdListMembers(), this.parametersPchem);
        this.scatterPlotModel = ScatterPlotChartUtil.generateScatter(listManagerController.getActiveList().getCmpdListMembers(), this.parametersPchem);

    }

    public void itemSelectScatterPlot(ItemSelectEvent event) {

        System.out.println("Now in itemSelectScatterPlot in HistogramController");

        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);

        Chart eventChart = (Chart) event.getSource();

        LineChartModel lcm = (LineChartModel) eventChart.getModel();

        ChartSeries selectedSeries = lcm.getSeries().get(event.getSeriesIndex());

        String lineChartTitle = lcm.getTitle();

        Set<Object> keySet = selectedSeries.getData().keySet();

        Double[] keyArray = keySet.toArray(new Double[keySet.size()]);

        Double pointIdent = keyArray[event.getItemIndex()];

    //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_INFO, "Item selected ",
                "Event Item Index: " + event.getItemIndex()
                + "\n" + ", seriesIndex: " + event.getSeriesIndex()
                + "\n" + ", lineChartIdent: " + lineChartTitle
                + "\n" + ", pointIdent: " + pointIdent);

        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public String renderHistoAndScatter() {

        // reset the selections
        listManagerController.setSelectedActiveListMembers(new ArrayList<CmpdListMemberVO>());

        System.out.println("Entering renderHistoAndScatter()");

        for (String s : this.parametersPchem) {
            System.out.println("Selected pChemParam: " + s);
        }

        this.histoModel = HistogramChartUtil.doHistograms(listManagerController.getActiveList().getCmpdListMembers(), this.parametersPchem);
        this.scatterPlotModel = ScatterPlotChartUtil.generateScatter(listManagerController.getActiveList().getCmpdListMembers(), this.parametersPchem);

        System.out.println("Count of histograms: " + this.histoModel.size());
        System.out.println("Count of scatterplots: " + this.scatterPlotModel.size());

        listManagerController.setSelectedActiveListMembers(new ArrayList<CmpdListMemberVO>());

        return null;

    }

    // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
    public String getHistogramSizeString() {
        return histogramSizeString;
    }

    public void setHistogramSizeString(String histogramSizeString) {
        this.histogramSizeString = histogramSizeString;
    }

    public String getScatterPlotSizeString() {
        return scatterPlotSizeString;
    }

    public void setScatterPlotSizeString(String scatterPlotSizeString) {
        this.scatterPlotSizeString = scatterPlotSizeString;
    }

    public String getStructureSizeString() {
        return structureSizeString;
    }

    public void setStructureSizeString(String structureSizeString) {
        this.structureSizeString = structureSizeString;
    }

    public Integer getHistogramSize() {

        if (this.histogramSizeString == null || this.histogramSizeString.equals("medium")) {
            this.histogramSize = 300;
        } else if (this.histogramSizeString.equals("small")) {
            this.histogramSize = 150;
        } else if (this.histogramSizeString.equals("large")) {
            this.histogramSize = 450;
        } else {
            this.histogramSize = 300;
        }

        return histogramSize;
    }

//  public void setHistogramSize(Integer histogramSize) {
//    this.histogramSize = histogramSize;
//  }
    public Integer getScatterPlotSize() {

        if (this.scatterPlotSizeString == null || this.scatterPlotSizeString.equals("medium")) {
            this.scatterPlotSize = 300;
        } else if (this.scatterPlotSizeString.equals("small")) {
            this.scatterPlotSize = 150;
        } else if (this.scatterPlotSizeString.equals("large")) {
            this.scatterPlotSize = 450;
        } else {
            this.scatterPlotSize = 300;
        }

        return scatterPlotSize;
    }

//  public void setScatterPlotSize(Integer scatterPlotSize) {
//    this.scatterPlotSize = scatterPlotSize;
//  }
    public Integer getStructureSize() {

        if (this.structureSizeString == null || this.structureSizeString.equals("medium")) {
            this.structureSize = 200;
        } else if (this.structureSizeString.equals("small")) {
            this.structureSize = 100;
        } else if (this.structureSizeString.equals("large")) {
            this.structureSize = 300;
        } else {
            this.structureSize = 200;
        }

        return structureSize;
    }

    public void setStructureSize(Integer structureSize) {
        this.structureSize = structureSize;
    }

    public List<Histogram> getHistoModel() {
        return histoModel;
    }

    public void setHistoModel(List<Histogram> histoModel) {
        this.histoModel = histoModel;
    }

    public ArrayList<LineChartModel> getScatterPlotModel() {
        return scatterPlotModel;
    }

    public void setScatterPlotModel(ArrayList<LineChartModel> scatterPlotModel) {
        this.scatterPlotModel = scatterPlotModel;
    }

    public List<String> getParametersPchem() {
        return parametersPchem;
    }

    public void setParametersPchem(List<String> parametersPchem) {
        this.parametersPchem = parametersPchem;
    }

      // </editor-fold>
}
