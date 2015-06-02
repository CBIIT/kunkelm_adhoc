/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import heatmap.HeatMap;
import heatmap.HeatMapUtil;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import mwk.microxeno.vo.AffymetrixDataVO;
import mwk.microxeno.vo.PassageAggregateVO;
import mwk.microxeno.vo.PassageDataSetVO;
import mwk.microxeno.vo.PassageIdentifierVO;
import mwk.microxeno.vo.TumorVO;
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

    private String tumorTextArea;
    private String geneTextArea;

    private ArrayList<String> tumorList;
    private ArrayList<String> geneList;

    private Double thePvalue;
    private List<String> passageList;
    private Boolean averageReplicates;

    private List<AffymetrixDataVO> affymetrixDataList;
    private List<PassageAggregateVO> passageDataList;
    private List<PassageDataSetVO> passageDataSetList;
    private CrosstabModel<PassageIdentifierVO, TumorVO, PassageAggregateVO> crosstabModel;

    private HeatMap heatMap;

    String delimiters = "[\\n\\r\\t\\s,]+";

    public String performSearch() {

        this.geneList = new ArrayList<String>();
        this.tumorList = new ArrayList<String>();

        if (this.geneTextArea != null && !this.geneTextArea.isEmpty()) {
            String[] splitString = this.geneTextArea.split(delimiters);
            for (int i = 0; i < splitString.length; i++) {
                this.geneList.add(splitString[i]);
            }
        }

        if (this.tumorTextArea != null && !this.tumorTextArea.isEmpty()) {
            String[] splitString = this.tumorTextArea.split(delimiters);
            for (int i = 0; i < splitString.length; i++) {
                this.tumorList.add(splitString[i]);
            }
        }

        if (this.geneTextArea.isEmpty() && this.tumorTextArea.isEmpty()) {
            this.geneList.add("A1BG");
            this.geneList.add("A1BG-AS1");
            this.geneList.add("A1CF");
            this.geneList.add("A2LD1");
            this.geneList.add("A2M");
            this.geneList.add("A2M-AS1");
            this.geneList.add("A2ML1");
            this.geneList.add("A2MP1");
            this.geneList.add("A4GALT");
            this.geneList.add("A4GNT");
            this.geneList.add("AA06");
            this.geneList.add("AAAS");
            this.geneList.add("AACS");
            this.geneList.add("AACSP1");
            this.geneList.add("AADAC");

            this.tumorList.add("OVCAR-5");
            this.tumorList.add("RXF 393");
            this.tumorList.add("SW-620");
            this.tumorList.add("HL-60(TB)");
            this.tumorList.add("HOP-92");
            this.tumorList.add("HuH-7");
            this.tumorList.add("SN12C");
            this.tumorList.add("EKVX");
            this.tumorList.add("MOLT-4");
            this.tumorList.add("NCI-H460");

        }

        HelperFlatData helper = new HelperFlatData();
        
        List<AffymetrixDataVO> dataList = helper.searchAffymetrixData(geneList, tumorList);
        
        SerializeDeSerialize<List<AffymetrixDataVO>> sd = new SerializeDeSerialize<List<AffymetrixDataVO>>("/tmp/dataList.ser");
        sd.serialize(dataList);        

        this.affymetrixDataList = dataList;
        
        this.passageDataList = CollateDataSetUtil.aggregateByPassage(dataList);     
        
        this.passageDataSetList = CollateDataSetUtil.collateDataSet(passageDataList);
        
        this.crosstabModel = CrosstabUtil.makeCrosstabModel(passageDataList);
        
        this.heatMap = HeatMapUtil.heatMapFromCrosstabModel(crosstabModel);
        
        System.out.println("Size of dataList: " + this.affymetrixDataList.size());
        System.out.println("Size of passageDataList: " + this.passageDataList.size());
        System.out.println("Size of crosstabModel xDim: " + this.crosstabModel.getGridXheaders().size());
        System.out.println("Size of crosstabModel yDim: " + this.crosstabModel.getGridYheaders().size());        
        System.out.println("Size of heatMap xDim: " + this.heatMap.getGridXheaderList().size());
        System.out.println("Size of heatMap yDim: " + this.heatMap.getGridYheaderList().size());
        
        return "/webpages/affymetrixDataTable?faces-redirect=true";

    }

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

    public Boolean isAverageReplicates() {
        return averageReplicates;
    }

    public void setAverageReplicates(Boolean averageReplicates) {
        this.averageReplicates = averageReplicates;
    }

    public List<AffymetrixDataVO> getAffymetrixDataList() {
        return affymetrixDataList;
    }

    public void setAffymetrixDataList(List<AffymetrixDataVO> affymetrixDataList) {
        this.affymetrixDataList = affymetrixDataList;
    }

    public List<PassageAggregateVO> getPassageDataList() {
        return passageDataList;
    }

    public void setPassageDataList(List<PassageAggregateVO> passageDataList) {
        this.passageDataList = passageDataList;
    }

    public List<PassageDataSetVO> getPassageDataSetList() {
        return passageDataSetList;
    }

    public void setPassageDataSetList(List<PassageDataSetVO> passageDataSetList) {
        this.passageDataSetList = passageDataSetList;
    }

    public CrosstabModel<PassageIdentifierVO, TumorVO, PassageAggregateVO> getCrosstabModel() {
        return crosstabModel;
    }

    public void setCrosstabModel(CrosstabModel<PassageIdentifierVO, TumorVO, PassageAggregateVO> crosstabModel) {
        this.crosstabModel = crosstabModel;
    }

    public HeatMap getHeatMap() {
        return heatMap;
    }

    public void setHeatMap(HeatMap heatMap) {
        this.heatMap = heatMap;
    }

    public String getDelimiters() {
        return delimiters;
    }

    public void setDelimiters(String delimiters) {
        this.delimiters = delimiters;
    }

}
