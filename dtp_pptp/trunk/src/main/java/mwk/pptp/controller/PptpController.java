/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.pptp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import mwk.pptp.util.HelperCellLineGroup;
import mwk.pptp.vo.CellLineGroupVO;
import mwk.pptp.vo.MouseGraphShuttleVO;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ViewScoped
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

  public String performShowGraphs() {

    List<Long> cellLineGroupIds = new ArrayList<Long>();

    for (CellLineGroupVO clgVO : this.selectedCellLineGroups) {
      cellLineGroupIds.add(clgVO.getCellLineGroupId());
    }

    HelperCellLineGroup helper = new HelperCellLineGroup();

    this.graphShuttles = helper.getGraphs(cellLineGroupIds);
    
    for (MouseGraphShuttleVO thisG : this.graphShuttles){      
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

    return null;

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