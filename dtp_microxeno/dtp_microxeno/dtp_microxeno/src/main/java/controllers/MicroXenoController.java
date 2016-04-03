/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import compactvo.MinPassageVO;
import compactvo.MinPassageAvgVO;
import heatmap.HeatMap;
import heatmap.HeatMapUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import mwk.microxeno.vo.PassageVO;
import mwk.microxeno.vo.AffymetrixIdentifierVO;
import mwk.microxeno.vo.PassageAvgVO;
import mwk.microxeno.vo.PassageAvgSetVO;
import mwk.microxeno.vo.PassageIdentifierVO;
import mwk.microxeno.vo.TumorVO;
import org.primefaces.event.SelectEvent;
import utils.CollateDataSetUtil;
import utils.CrosstabModel;
import utils.CrosstabUtil;
import utils.HelperFlatData;
import utils.SerializeDeSerialize;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class MicroXenoController implements Serializable {

    private static final long serialVersionUID = 7816871787968798179L;

    private String probeSetIdTextArea;
    private String geneTextArea;
    private String tumorTextArea;

    private ArrayList<String> probeSetIdList;
    private ArrayList<String> geneList;
    private ArrayList<String> tumorList;

    private Double thePvalue;
    private List<String> passageList;
    private Boolean averageReplicates;

    private List<PassageVO> passageDataList;
    private List<PassageVO> selectedPassageDataList;

    private List<PassageAvgVO> passageAggregateList;
    private List<PassageAvgVO> selectedPassageAggregateList;

    private List<PassageAvgSetVO> passageDataSetList;
    private CrosstabModel<PassageIdentifierVO, TumorVO, PassageAvgVO> crosstabModel;

    private HeatMap heatMap;

    private String passageJson;
    private String passageAggregateJson;

    String delimiters = "[\\n\\r\\t,]+";

    public String performSearch() {

        try {

            geneList = new ArrayList<String>();
            tumorList = new ArrayList<String>();
            probeSetIdList = new ArrayList<String>();

            if (probeSetIdTextArea != null && !probeSetIdTextArea.isEmpty()) {
                String[] splitString = probeSetIdTextArea.split(delimiters);
                for (int i = 0; i < splitString.length; i++) {
                    probeSetIdList.add(splitString[i]);
                }
            }

            if (geneTextArea != null && !geneTextArea.isEmpty()) {
                String[] splitString = geneTextArea.split(delimiters);
                for (int i = 0; i < splitString.length; i++) {
                    geneList.add(splitString[i]);
                }
            }

            if (tumorTextArea != null && !tumorTextArea.isEmpty()) {
                String[] splitString = tumorTextArea.split(delimiters);
                for (int i = 0; i < splitString.length; i++) {
                    tumorList.add(splitString[i]);
                }
            }

            if (geneTextArea.isEmpty() && tumorTextArea.isEmpty()) {

                String[] gArr = new String[]{
                    "A1BG",
                    "A1BG-AS1",
                    "A1CF",
                    "A2LD1",
                    "A2M",
                    "A2M-AS1",
                    "A2ML1",
                    "A2MP1",
                    "A4GALT",
                    "A4GNT",
                    "AA06",
                    "AAAS",
                    "AACS",
                    "AACSP1",
                    "AADAC"
                };

                geneList.addAll(Arrays.asList(gArr));

                StringBuilder sb = new StringBuilder();
                for (String s : geneList) {
                    sb.append(s);
                    sb.append("\n");
                }
                geneTextArea = sb.toString();

                String[] tumArr = new String[]{
                    "OVCAR-5",
                    "RXF 393",
                    "SW-620",
                    "HL-60(TB)",
                    "HOP-92",
                    "HuH-7",
                    "SN12C",
                    "EKVX",
                    "MOLT-4",
                    "NCI-H460"
                };

                tumorList.addAll(Arrays.asList(tumArr));

                sb = new StringBuilder();
                for (String s : tumorList) {
                    sb.append(s);
                    sb.append("\n");
                }
                tumorTextArea = sb.toString();

            }

            HelperFlatData helper = new HelperFlatData();

            List<PassageVO> dataList = helper.searchAffymetrixData(probeSetIdList, geneList, tumorList, passageList);

            SerializeDeSerialize<List<PassageVO>> sd = new SerializeDeSerialize<List<PassageVO>>("/tmp/dataList.ser");
            sd.serialize(dataList);

            passageDataList = dataList;

            passageAggregateList = CollateDataSetUtil.aggregateByPassage(dataList);

            passageDataSetList = CollateDataSetUtil.collateDataSet(passageAggregateList);

            crosstabModel = CrosstabUtil.makeCrosstabModel(passageAggregateList);

            heatMap = HeatMapUtil.heatMapFromCrosstabModel(crosstabModel);

            System.out.println("Size of dataList: " + passageDataList.size());
            System.out.println("Size of passageDataList: " + passageAggregateList.size());

            System.out.println("Size of crosstabModel xDim: " + crosstabModel.getGridXheaders().size());
            System.out.println("Size of crosstabModel yDim: " + crosstabModel.getGridYheaders().size());

            System.out.println("Size of heatMap xDim: " + heatMap.getGridXheaderList().size());
            System.out.println("Size of heatMap yDim: " + heatMap.getGridYheaderList().size());

        } catch (Exception e) {
            e.printStackTrace();
        }

        ArrayList<MinPassageVO> minPassageDataList = new ArrayList<MinPassageVO>();
        for (PassageVO adVO : passageDataList) {
            minPassageDataList.add(new MinPassageVO(adVO));
        }

        ArrayList<MinPassageAvgVO> minPassageAggregateList = new ArrayList<MinPassageAvgVO>();
        for (PassageAvgVO paVO : passageAggregateList) {
            minPassageAggregateList.add(new MinPassageAvgVO(paVO));
        }

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        
        passageJson = gson.toJson(minPassageDataList);
        passageAggregateJson = gson.toJson(minPassageAggregateList);

        System.out.println("passageJson: ");
        System.out.println(passageJson);
        
        System.out.println("passageAggregateJson: ");
        System.out.println(passageAggregateJson);
        
        return "/webpages/affymetrixDataTable?faces-redirect=true";

    }

    public void appendProbeSetId(SelectEvent event) {
        Object item = event.getObject();
        String s = (String) item;
        if (probeSetIdTextArea == null) {
            probeSetIdTextArea = "";
        }
        probeSetIdTextArea = s + "\n" + probeSetIdTextArea;
    }

    public void appendGene(SelectEvent event) {
        Object item = event.getObject();
        String s = (String) item;
        if (geneTextArea == null) {
            geneTextArea = "";
        }
        geneTextArea = s + "\n" + geneTextArea;
    }

    public void appendGeneTitle(SelectEvent event) {
        Object item = event.getObject();
        String s = (String) item;
        if (geneTextArea == null) {
            geneTextArea = "";
        }
        geneTextArea = s + "\n" + geneTextArea;
    }

    public void appendTumor(SelectEvent event) {
        Object item = event.getObject();
        String s = (String) item;
        if (tumorTextArea == null) {
            tumorTextArea = "";
        }
        tumorTextArea = s + "\n" + tumorTextArea;
    }

    public void appendTumorType(SelectEvent event) {
        Object item = event.getObject();
        String s = (String) item;
        if (tumorTextArea == null) {
            tumorTextArea = "";
        }
        tumorTextArea = s + "\n" + tumorTextArea;
    }

//    
//    
// object-based append methods
//    
//         
    public void appProbeSetId(SelectEvent event) {
        Object item = event.getObject();
        AffymetrixIdentifierVO aiVO = (AffymetrixIdentifierVO) item;
        if (probeSetIdTextArea == null) {
            probeSetIdTextArea = "";
        }
        probeSetIdTextArea = aiVO.getProbeSetId() + "\n" + probeSetIdTextArea;
    }

    public void appGene(SelectEvent event) {
        Object item = event.getObject();
        AffymetrixIdentifierVO aiVO = (AffymetrixIdentifierVO) item;
        if (geneTextArea == null) {
            geneTextArea = "";
        }
        geneTextArea = aiVO.getGeneSymbol() + "\n" + geneTextArea;
    }

    public void appGeneTitle(SelectEvent event) {
        Object item = event.getObject();
        AffymetrixIdentifierVO aiVO = (AffymetrixIdentifierVO) item;
        if (geneTextArea == null) {
            geneTextArea = "";
        }
        geneTextArea = aiVO.getGeneSymbol() + "\n" + geneTextArea;
    }

    public void appTumor(SelectEvent event) {
        Object item = event.getObject();
        TumorVO tVO = (TumorVO) item;
        if (tumorTextArea == null) {
            tumorTextArea = "";
        }
        tumorTextArea = tVO.getTumorName() + "\n" + tumorTextArea;
    }

    public void appTumorType(SelectEvent event) {
        Object item = event.getObject();
        TumorVO tVO = (TumorVO) item;
        if (tumorTextArea == null) {
            tumorTextArea = "";
        }
        tumorTextArea = tVO.getTumorType() + "\n" + tumorTextArea;
    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public String getTumorTextArea() {
        return tumorTextArea;
    }

    public void setTumorTextArea(String tumorTextArea) {
        this.tumorTextArea = tumorTextArea;
    }

    public String getGeneTextArea() {
        return geneTextArea;
    }

    public void setGeneTextArea(String geneTextArea) {
        this.geneTextArea = geneTextArea;
    }

    public String getProbeSetIdTextArea() {
        return probeSetIdTextArea;
    }

    public void setProbeSetIdTextArea(String probeSetIdTextArea) {
        this.probeSetIdTextArea = probeSetIdTextArea;
    }

    public ArrayList<String> getProbeSetIdList() {
        return probeSetIdList;
    }

    public void setProbeSetIdList(ArrayList<String> probeSetIdList) {
        this.probeSetIdList = probeSetIdList;
    }

    public ArrayList<String> getTumorList() {
        return tumorList;
    }

    public void setTumorList(ArrayList<String> tumorList) {
        this.tumorList = tumorList;
    }

    public ArrayList<String> getGeneList() {
        return geneList;
    }

    public void setGeneList(ArrayList<String> geneList) {
        this.geneList = geneList;
    }

    public Double getThePvalue() {
        return thePvalue;
    }

    public void setThePvalue(Double thePvalue) {
        this.thePvalue = thePvalue;
    }

    public List<String> getPassageList() {
        return passageList;
    }

    public void setPassageList(List<String> passageList) {
        this.passageList = passageList;
    }

    public List<PassageAvgVO> getSelectedPassageAggregateList() {
        return selectedPassageAggregateList;
    }

    public void setSelectedPassageAggregateList(List<PassageAvgVO> selectedPassageAggregateList) {
        this.selectedPassageAggregateList = selectedPassageAggregateList;
    }

    public Boolean isAverageReplicates() {
        return averageReplicates;
    }

    public void setAverageReplicates(Boolean averageReplicates) {
        this.averageReplicates = averageReplicates;
    }

    public List<PassageVO> getPassageDataList() {
        return passageDataList;
    }

    public void setPassageDataList(List<PassageVO> passageDataList) {
        this.passageDataList = passageDataList;
    }

    public List<PassageVO> getSelectedPassageDataList() {
        return selectedPassageDataList;
    }

    public void setSelectedPassageDataList(List<PassageVO> selectedPassageDataList) {
        this.selectedPassageDataList = selectedPassageDataList;
    }

    public List<PassageAvgVO> getPassageAggregateList() {
        return passageAggregateList;
    }

    public void setPassageAggregateList(List<PassageAvgVO> passageAggregateList) {
        this.passageAggregateList = passageAggregateList;
    }

    public List<PassageAvgSetVO> getPassageDataSetList() {
        return passageDataSetList;
    }

    public void setPassageDataSetList(List<PassageAvgSetVO> passageDataSetList) {
        this.passageDataSetList = passageDataSetList;
    }

    public CrosstabModel<PassageIdentifierVO, TumorVO, PassageAvgVO> getCrosstabModel() {
        return crosstabModel;
    }

    public void setCrosstabModel(CrosstabModel<PassageIdentifierVO, TumorVO, PassageAvgVO> crosstabModel) {
        this.crosstabModel = crosstabModel;
    }

    public HeatMap getHeatMap() {
        return heatMap;
    }

    public void setHeatMap(HeatMap heatMap) {
        this.heatMap = heatMap;
    }

    public String getPassageJson() {
        return passageJson;
    }

    public void setPassageJson(String passageJson) {
        this.passageJson = passageJson;
    }

    public String getPassageAggregateJson() {
        return passageAggregateJson;
    }

    public void setPassageAggregateJson(String passageAggregateJson) {
        this.passageAggregateJson = passageAggregateJson;
    }

    //</editor-fold>
}
