package mwk.datasystem.controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import javax.annotation.PostConstruct;
import javax.faces.FacesException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import mwk.datasystem.mwkcharting.TemplPropUtil;
import mwk.datasystem.mwkcharting.TemplatedHistogram;
import mwk.datasystem.mwkcharting.TemplatedHistogramBin;
import mwk.datasystem.util.ScatterPlotChartUtil;
import mwk.datasystem.mwkcharting.TemplatedHistogramChartUtil;
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
    
    private String json;

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

    private List<TemplatedHistogram<CmpdListMemberVO>> histogramList;
    private ArrayList<LineChartModel> scatterPlotList;

    private List<String> intProps;
    private List<String> dblProps;
    private List<String> intParams;
    private List<String> dblParams;

    // private List<String> combinedParams; accessible ONLY via getMethod
    private String histogramSizeString;
    private String scatterPlotSizeString;
    private String structureSizeString;

    private Integer histogramSize;
    private Integer scatterPlotSize;
    private Integer structureSize;

    private List<CmpdListMemberVO> cmpdListMembers;
    private List<CmpdListMemberVO> selectedCmpdListMembers;

    private TemplPropUtil propUtil;

    public HistogramController() {
        init();
    }

    // don't do this for now 'cuz calls it too often... @PostConstruct
    public void init() {

        this.histogramList = new ArrayList<TemplatedHistogram<CmpdListMemberVO>>();
        this.scatterPlotList = new ArrayList<LineChartModel>();

        ArrayList<String> ignList = new ArrayList<String>();
        ignList.add("id");
        ignList.add("serialVersionUID");

        ArrayList<String> reqList = new ArrayList<String>();
        reqList.add("name");
        reqList.add("cmpdFragmentPChem");

        this.propUtil = new TemplPropUtil<CmpdListMemberVO>(new CmpdListMemberVO(), ignList, reqList);
        this.intProps = this.propUtil.getIntProps();
        this.dblProps = this.propUtil.getDblProps();

        this.intParams = new ArrayList<String>(Arrays.asList(new String[]{
            "countHydBondAcceptors",
            "countHydBondDonors"}));

        this.dblParams = new ArrayList<String>(Arrays.asList(new String[]{
            "molecularWeight",
            "surfaceArea"}));

        this.histogramSizeString = "medium";
        this.scatterPlotSizeString = "medium";
        this.structureSizeString = "medium";
        this.histogramSize = 300;
        this.scatterPlotSize = 300;
        this.structureSize = 200;

        this.cmpdListMembers = new ArrayList<CmpdListMemberVO>();
        this.selectedCmpdListMembers = new ArrayList<CmpdListMemberVO>();

    }

    public void handleLoadActiveList() {

        String rtn = performLoadActiveList();
        FacesContext ctx = FacesContext.getCurrentInstance();
        ExternalContext extCtx = ctx.getExternalContext();
        String url = extCtx.encodeActionURL(ctx.getApplication().getViewHandler().getActionURL(ctx, "/webpages/activeListHistograms.xhtml"));

        try {
            extCtx.redirect(url);
        } catch (IOException ioe) {
            throw new FacesException(ioe);
        }

    }

    public String performLoadActiveList() {

        this.cmpdListMembers = new ArrayList<CmpdListMemberVO>(listManagerController.getListManagerBean().activeList.getCmpdListMembers());
        this.selectedCmpdListMembers = new ArrayList<CmpdListMemberVO>();

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(this.cmpdListMembers);
        try {
            File f = new File("/tmp/cmpdListMembers.json");
            FileWriter fw = new FileWriter(f);
            fw.write(json);
            fw.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        this.json = json;

        renderHistoAndScatter();

        return "/webpages/activeListHistograms?faces-redirect=true";
    }

    public String performClearSelections() {

        for (CmpdListMemberVO clmVO : this.cmpdListMembers) {
            clmVO.setIsSelected(Boolean.FALSE);
        }

        this.selectedCmpdListMembers.clear();

        renderHistoAndScatter();

        return "/webpages/activeListHistograms?faces-redirect=true";
    }

    //
    public void itemSelectHistogram(ItemSelectEvent event) {

        System.out.println("Now in itemSelectHistogram in HistogramController");

        Object eventSrc = event.getSource();

        System.out.println("eventSrc is: " + eventSrc.getClass().toString());

        Chart eventChart = (Chart) event.getSource();
        BarChartModel bcm = (BarChartModel) eventChart.getModel();
        String histoTitle = bcm.getTitle();
        int seriesIdx = event.getSeriesIndex();
        ChartSeries selSeries = bcm.getSeries().get(seriesIdx);
        String seriesLabel = selSeries.getLabel();
        Set<Object> keySet = selSeries.getData().keySet();
        String[] keyArray = keySet.toArray(new String[keySet.size()]);
        int itemIdx = event.getItemIndex();
        String histoBarLabel = keyArray[itemIdx];

        //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        String details = "Event Item Index: " + itemIdx
                + ", Histogram Title: " + histoTitle
                + ", Series Index: " + seriesIdx
                + ", Series Label: " + seriesLabel
                + ", Histogram Bar Ident: " + histoBarLabel;

        FacesMessage fm = new FacesMessage();
        fm.setDetail(details);
        fm.setSummary("In itemSelectHistogram: ");
        fm.setSeverity(FacesMessage.SEVERITY_INFO);

        FacesContext fctx = FacesContext.getCurrentInstance();
        fctx.addMessage(null, fm);

        //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        // find the source histogram
        TemplatedHistogram<CmpdListMemberVO> selHisto = null;

        boolean found = false;

        for (TemplatedHistogram<CmpdListMemberVO> h : this.histogramList) {
            if (h.getPropertyName().equals(histoTitle)) {
                selHisto = h;
                found = true;
                break;
            }
        }

        if (!found) {
            System.out.println("Couldn't find histogram for property: " + histoTitle);
        }

        // get the appropriate bin
        TemplatedHistogramBin<CmpdListMemberVO> curBin = selHisto.getBinList().get(itemIdx);

        System.out.println("histogramBarLabel: " + histoBarLabel);
        curBin.debugBin();

        if (curBin.getBinList() == null || curBin.getBinList().isEmpty()) {
            System.out.println("curBin has an empty binList");
            System.out.println("event.itemIndex: " + itemIdx);
            selHisto.debugBins();
        }

        ArrayList<CmpdListMemberVO> allMembers = curBin.getBinList();
        ArrayList<CmpdListMemberVO> selectedMembers = new ArrayList<CmpdListMemberVO>();

        for (CmpdListMemberVO clmVO : curBin.getBinList()) {
            if (clmVO.getIsSelected() != null && clmVO.getIsSelected()) {
                selectedMembers.add(clmVO);
            }
        }

        // if click on series 0 => this was the selected (red) bar;
        // so remove only selectedMembers from the bin
        // if click on series 1 => this was the full (blue) bar;
        // so add those IF not previously selected
        if (seriesIdx == 0) {

            for (CmpdListMemberVO clmVO : selectedMembers) {
                if (this.selectedCmpdListMembers.contains(clmVO)) {
                    clmVO.setIsSelected(Boolean.FALSE);
                    this.selectedCmpdListMembers.remove(clmVO);
                }
            }

        } else {

            for (CmpdListMemberVO clmVO : allMembers) {
                if (!this.selectedCmpdListMembers.contains(clmVO)) {
                    clmVO.setIsSelected(Boolean.TRUE);
                    this.selectedCmpdListMembers.add(clmVO);
                }
            }

        }

        // regenerate the histogramList
        TemplatedHistogramChartUtil histoUtil = new TemplatedHistogramChartUtil<CmpdListMemberVO>();

        this.histogramList = histoUtil.doHistograms(this.cmpdListMembers, getCombinedParams(), this.propUtil);

        this.scatterPlotList = ScatterPlotChartUtil.generateScatter(this.cmpdListMembers, getCombinedParams(), this.propUtil);

    }

    public void itemSelectScatterPlot(ItemSelectEvent event) {

        System.out.println("Now in itemSelectScatterPlot in HistogramController");

        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);

        Chart eventChart = (Chart) event.getSource();
        LineChartModel lcm = (LineChartModel) eventChart.getModel();
        String lineChartTitle = lcm.getTitle();
        int seriesIdx = event.getSeriesIndex();
        ChartSeries selectedSeries = lcm.getSeries().get(seriesIdx);
        String seriesLabel = selectedSeries.getLabel();

        int itemIdx = event.getItemIndex();

        //Set<Object> keySet = selectedSeries.getData().keySet();
        //Double[] keyArray = keySet.toArray(new Double[keySet.size()]);    
        //Double pointIdent = keyArray[itemIdx];
        ArrayList<String> labelList = new ArrayList<String>(selectedSeries.getDataLabels().values());
        String pointLabel = labelList.get(event.getItemIndex());

//    FacesMessage msg = new FacesMessage(
//            FacesMessage.SEVERITY_INFO, "Item selected ",
//            "Event Item Index: " + itemIdx
//            + ", ScatterPlot Title: " + lineChartTitle
//            + ", seriesIndex: " + seriesIdx
//            + ", Series Label: " + seriesLabel
//            + ", pointLabel: " + pointLabel);
        FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_INFO, "ScatterPlot ItemSelectEvent: ",
                "ser: " + seriesIdx + ", item: " + itemIdx + " " + pointLabel);

        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public void itemSelectChemicalStructure(String identifierString) {

        System.out.println("Now in itemSelectChemicalStructure in HistogramController");

        NumberFormat nf = new DecimalFormat();
        nf.setMaximumFractionDigits(2);

        //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        //--------------------------------------------------------------------------
        FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_INFO, "Item selected ",
                "Event identifierString: " + identifierString);

        FacesContext.getCurrentInstance().addMessage(null, msg);

    }

    public String renderHistoAndScatter() {

        System.out.println("Entering renderHistoAndScatter()");

        for (String s : getCombinedParams()) {
            System.out.println("combinedParams: " + s);
        }

        // serialize the data
        // SerializeDeSerialize<Collection<CmpdListMemberVO>> sds = new SerializeDeSerialize<Collection<CmpdListMemberVO>>("/tmp/clmVOlist.ser");
        // sds.serialize(this.cmpdListMembers);
        // reset the selections
        for (CmpdListMemberVO clmVO : this.cmpdListMembers) {
            clmVO.setIsSelected(Boolean.FALSE);
        }

        this.selectedCmpdListMembers.clear();

        TemplatedHistogramChartUtil histoUtil = new TemplatedHistogramChartUtil<CmpdListMemberVO>();
        this.histogramList = histoUtil.doHistograms(this.cmpdListMembers, getCombinedParams(), this.propUtil);

        this.scatterPlotList = ScatterPlotChartUtil.generateScatter(this.cmpdListMembers, getCombinedParams(), this.propUtil);

        System.out.println("Count of histograms: " + this.histogramList.size());
        System.out.println("Count of scatterplots: " + this.scatterPlotList.size());

        return null;

    }

    // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }
    
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

    public List<TemplatedHistogram<CmpdListMemberVO>> getHistogramList() {
        return histogramList;
    }

    public void setHistogramList(List<TemplatedHistogram<CmpdListMemberVO>> histogramList) {
        this.histogramList = histogramList;
    }

    public ArrayList<LineChartModel> getScatterPlotList() {
        return scatterPlotList;
    }

    public void setScatterPlotList(ArrayList<LineChartModel> scatterPlotList) {
        this.scatterPlotList = scatterPlotList;
    }

    public List<String> getIntProps() {
        return intProps;
    }

    public void setIntProps(List<String> intProps) {
        this.intProps = intProps;
    }

    public List<String> getDblProps() {
        return dblProps;
    }

    public void setDblProps(List<String> dblProps) {
        this.dblProps = dblProps;
    }

    public List<String> getIntParams() {
        return intParams;
    }

    public void setIntParams(List<String> intParams) {
        this.intParams = intParams;
    }

    public List<String> getDblParams() {
        return dblParams;
    }

    public void setDblParams(List<String> dblParams) {
        this.dblParams = dblParams;
    }

    public List<String> getCombinedParams() {
        ArrayList<String> rtn = new ArrayList<String>();

        rtn.addAll(this.getIntParams());
        rtn.addAll(this.getDblParams());

        return rtn;
    }

    public List<CmpdListMemberVO> getCmpdListMembers() {
        return cmpdListMembers;
    }

    public void setCmpdListMembers(List<CmpdListMemberVO> cmpdListMembers) {
        this.cmpdListMembers = cmpdListMembers;
    }

    public List<CmpdListMemberVO> getSelectedCmpdListMembers() {
        return selectedCmpdListMembers;
    }

    public void setSelectedCmpdListMembers(List<CmpdListMemberVO> selectedCmpdListMembers) {
        this.selectedCmpdListMembers = selectedCmpdListMembers;
    }

      // </editor-fold>
}
