/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class SearchCriteriaBean implements Serializable {

  static final long serialVersionUID = -8653468638698142855l;
  
  private List<String> drugNames;
  private List<String> aliases;
  private List<String> cases;
  private List<String> cmpdNamedSets;
  private List<String> nscs;
  private List<String> projectCodes;
  private List<String> plates;
  private List<String> targets;
  //
  private String drugNameTextArea;
  private String aliasTextArea;
  private String casTextArea;
  private String cmpdNamedSetTextArea;
  private String nscTextArea;
  private String projectCodeTextArea;
  private String plateTextArea;
  private String targetTextArea;
  // molecular properties
  private Double min_molecularWeight;
  private Double max_molecularWeight;
  private String molecularFormula;
  private Double min_logD;
  private Double max_logD;
  private Integer min_countHydBondAcceptors;
  private Integer max_countHydBondAcceptors;
  private Integer min_countHydBondDonors;
  private Integer max_countHydBondDonors;
  private Double min_surfaceArea;
  private Double max_surfaceArea;
  private Double min_solubility;
  private Double max_solubility;
  private Integer min_countRings;
  private Integer max_countRings;
  private Integer min_countAtoms;
  private Integer max_countAtoms;
  private Integer min_countBonds;
  private Integer max_countBonds;
  private Integer min_countSingleBonds;
  private Integer max_countSingleBonds;
  private Integer min_countDoubleBonds;
  private Integer max_countDoubleBonds;
  private Integer min_countTripleBonds;
  private Integer max_countTripleBonds;
  private Integer min_countRotatableBonds;
  private Integer max_countRotatableBonds;
  private Integer min_countHydrogenAtoms;
  private Integer max_countHydrogenAtoms;
  private Integer min_countMetalAtoms;
  private Integer max_countMetalAtoms;
  private Integer min_countHeavyAtoms;
  private Integer max_countHeavyAtoms;
  private Integer min_countPositiveAtoms;
  private Integer max_countPositiveAtoms;
  private Integer min_countNegativeAtoms;
  private Integer max_countNegativeAtoms;
  private Integer min_countRingBonds;
  private Integer max_countRingBonds;
  private Integer min_countStereoAtoms;
  private Integer max_countStereoAtoms;
  private Integer min_countStereoBonds;
  private Integer max_countStereoBonds;
  private Integer min_countRingAssemblies;
  private Integer max_countRingAssemblies;
  private Integer min_countAromaticBonds;
  private Integer max_countAromaticBonds;
  private Integer min_countAromaticRings;
  private Integer max_countAromaticRings;
  private Integer min_formalCharge;
  private Integer max_formalCharge;
  private Double min_theALogP;
  private Double max_theALogP;

  public SearchCriteriaBean() {
      reset();
  }

  public void reset() {
    this.drugNames = new ArrayList<String>();
    this.aliases = new ArrayList<String>();
    this.cases = new ArrayList<String>();
    this.cmpdNamedSets = new ArrayList<String>();
    this.nscs = new ArrayList<String>();
    this.projectCodes = new ArrayList<String>();
    this.plates = new ArrayList<String>();
    this.targets = new ArrayList<String>();
  }

  public void printCriteriaLists() {

    System.out.println("aliasTextArea: " + this.aliasTextArea);
    System.out.println("casTextArea: " + this.casTextArea);
    System.out.println("cmpdNamedSetTextArea: " + this.cmpdNamedSetTextArea);
    System.out.println("drugNameTextArea: " + this.drugNameTextArea);
    System.out.println("nscTextArea: " + this.nscTextArea);
    System.out.println("plateTextArea: " + this.plateTextArea);
    System.out.println("targetTextArea: " + this.targetTextArea);
    System.out.println("projectCodeTextArea: " + this.projectCodeTextArea);

    System.out.println("aliases:");
    for (String s : this.aliases) {
      System.out.println("-------->" + s + "<--------");
    }

    System.out.println("cases:");
    for (String s : this.cases) {
      System.out.println("-------->" + s + "<--------");
    }

    System.out.println("cmpdNamedSets:");
    for (String s : this.cmpdNamedSets) {
      System.out.println("-------->" + s + "<--------");
    }

    System.out.println("drugNames:");
    for (String s : this.drugNames) {
      System.out.println("-------->" + s + "<--------");
    }

    System.out.println("nscs:");
    for (String s : this.nscs) {
      System.out.println("-------->" + s + "<--------");
    }

    System.out.println("plates:");
    for (String s : this.plates) {
      System.out.println("-------->" + s + "<--------");
    }

    System.out.println("projectCodes:");
    for (String s : this.projectCodes) {
      System.out.println("-------->" + s + "<--------");
    }

    System.out.println("targets:");
    for (String s : this.targets) {
      System.out.println("-------->" + s + "<--------");
    }

  }

  public void printPchemCriteria() {
    // QC on pChemBean, etc.
    System.out.println("QC on settings for molecularPropertiesCriteriaBean");
    System.out.println("min_molecularWeight: " + this.getMin_molecularWeight());
    System.out.println("max_molecularWeight: " + this.getMax_molecularWeight());
    System.out.println("molecularFormula: " + this.getMolecularFormula());
    System.out.println("min_logD: " + this.getMin_logD());
    System.out.println("max_logD: " + this.getMax_logD());
    System.out.println("min_countHydBondAcceptors: " + this.getMin_countHydBondAcceptors());
    System.out.println("max_countHydBondAcceptors: " + this.getMax_countHydBondAcceptors());
    System.out.println("min_countHydBondDonors: " + this.getMin_countHydBondDonors());
    System.out.println("max_countHydBondDonors: " + this.getMax_countHydBondDonors());
    System.out.println("min_surfaceArea: " + this.getMin_surfaceArea());
    System.out.println("max_surfaceArea: " + this.getMax_surfaceArea());
    System.out.println("min_solubility: " + this.getMin_solubility());
    System.out.println("max_solubility: " + this.getMax_solubility());
    System.out.println("min_countRings: " + this.getMin_countRings());
    System.out.println("max_countRings: " + this.getMax_countRings());
    System.out.println("min_countAtoms: " + this.getMin_countAtoms());
    System.out.println("max_countAtoms: " + this.getMax_countAtoms());
    System.out.println("min_countBonds: " + this.getMin_countBonds());
    System.out.println("max_countBonds: " + this.getMax_countBonds());
    System.out.println("min_countSingleBonds: " + this.getMin_countSingleBonds());
    System.out.println("max_countSingleBonds: " + this.getMax_countSingleBonds());
    System.out.println("min_countDoubleBonds: " + this.getMin_countDoubleBonds());
    System.out.println("max_countDoubleBonds: " + this.getMax_countDoubleBonds());
    System.out.println("min_countTripleBonds: " + this.getMin_countTripleBonds());
    System.out.println("max_countTripleBonds: " + this.getMax_countTripleBonds());
    System.out.println("min_countRotatableBonds: " + this.getMin_countRotatableBonds());
    System.out.println("max_countRotatableBonds: " + this.getMax_countRotatableBonds());
    System.out.println("min_countHydrogenAtoms: " + this.getMin_countHydrogenAtoms());
    System.out.println("max_countHydrogenAtoms: " + this.getMax_countHydrogenAtoms());
    System.out.println("min_countMetalAtoms: " + this.getMin_countMetalAtoms());
    System.out.println("max_countMetalAtoms: " + this.getMax_countMetalAtoms());
    System.out.println("min_countHeavyAtoms: " + this.getMin_countHeavyAtoms());
    System.out.println("max_countHeavyAtoms: " + this.getMax_countHeavyAtoms());
    System.out.println("min_countPositiveAtoms: " + this.getMin_countPositiveAtoms());
    System.out.println("max_countPositiveAtoms: " + this.getMax_countPositiveAtoms());
    System.out.println("min_countNegativeAtoms: " + this.getMin_countNegativeAtoms());
    System.out.println("max_countNegativeAtoms: " + this.getMax_countNegativeAtoms());
    System.out.println("min_countRingBonds: " + this.getMin_countRingBonds());
    System.out.println("max_countRingBonds: " + this.getMax_countRingBonds());
    System.out.println("min_countStereoAtoms: " + this.getMin_countStereoAtoms());
    System.out.println("max_countStereoAtoms: " + this.getMax_countStereoAtoms());
    System.out.println("min_countStereoBonds: " + this.getMin_countStereoBonds());
    System.out.println("max_countStereoBonds: " + this.getMax_countStereoBonds());
    System.out.println("min_countRingAssemblies: " + this.getMin_countRingAssemblies());
    System.out.println("max_countRingAssemblies: " + this.getMax_countRingAssemblies());
    System.out.println("min_countAromaticBonds: " + this.getMin_countAromaticBonds());
    System.out.println("max_countAromaticBonds: " + this.getMax_countAromaticBonds());
    System.out.println("min_countAromaticRings: " + this.getMin_countAromaticRings());
    System.out.println("max_countAromaticRings: " + this.getMax_countAromaticRings());
    System.out.println("min_formalCharge: " + this.getMin_formalCharge());
    System.out.println("max_formalCharge: " + this.getMax_formalCharge());
    System.out.println("min_theALogP: " + this.getMin_theALogP());
    System.out.println("max_theALogP: " + this.getMax_theALogP());

  }
 

  // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
  // </editor-fold>
  /**
   * @return the drugNames
   */
  public List<String> getDrugNames() {
    return drugNames;
  }

  /**
   * @param drugNames the drugNames to set
   */
  public void setDrugNames(List<String> drugNames) {
    this.drugNames = drugNames;
  }

  /**
   * @return the aliases
   */
  public List<String> getAliases() {
    return aliases;
  }

  /**
   * @param aliases the aliases to set
   */
  public void setAliases(List<String> aliases) {
    this.aliases = aliases;
  }

  /**
   * @return the cases
   */
  public List<String> getCases() {
    return cases;
  }

  /**
   * @param cases the cases to set
   */
  public void setCases(List<String> cases) {
    this.cases = cases;
  }

  /**
   * @return the cmpdNamedSets
   */
  public List<String> getCmpdNamedSets() {
    return cmpdNamedSets;
  }

  /**
   * @param cmpdNamedSets the cmpdNamedSets to set
   */
  public void setCmpdNamedSets(List<String> cmpdNamedSets) {
    this.cmpdNamedSets = cmpdNamedSets;
  }

  /**
   * @return the nscs
   */
  public List<String> getNscs() {
    return nscs;
  }

  /**
   * @param nscs the nscs to set
   */
  public void setNscs(List<String> nscs) {
    this.nscs = nscs;
  }

  /**
   * @return the projectCodes
   */
  public List<String> getProjectCodes() {
    return projectCodes;
  }

  /**
   * @param projectCodes the projectCodes to set
   */
  public void setProjectCodes(List<String> projectCodes) {
    this.projectCodes = projectCodes;
  }

  /**
   * @return the plates
   */
  public List<String> getPlates() {
    return plates;
  }

  /**
   * @param plates the plates to set
   */
  public void setPlates(List<String> plates) {
    this.plates = plates;
  }

  /**
   * @return the targets
   */
  public List<String> getTargets() {
    return targets;
  }

  /**
   * @param targets the targets to set
   */
  public void setTargets(List<String> targets) {
    this.targets = targets;
  }

  /**
   * @return the drugNameTextArea
   */
  public String getDrugNameTextArea() {
    return drugNameTextArea;
  }

  /**
   * @param drugNameTextArea the drugNameTextArea to set
   */
  public void setDrugNameTextArea(String drugNameTextArea) {
    this.drugNameTextArea = drugNameTextArea;
  }

  /**
   * @return the aliasTextArea
   */
  public String getAliasTextArea() {
    return aliasTextArea;
  }

  /**
   * @param aliasTextArea the aliasTextArea to set
   */
  public void setAliasTextArea(String aliasTextArea) {
    this.aliasTextArea = aliasTextArea;
  }

  /**
   * @return the casTextArea
   */
  public String getCasTextArea() {
    return casTextArea;
  }

  /**
   * @param casTextArea the casTextArea to set
   */
  public void setCasTextArea(String casTextArea) {
    this.casTextArea = casTextArea;
  }

  /**
   * @return the cmpdNamedSetTextArea
   */
  public String getCmpdNamedSetTextArea() {
    return cmpdNamedSetTextArea;
  }

  /**
   * @param cmpdNamedSetTextArea the cmpdNamedSetTextArea to set
   */
  public void setCmpdNamedSetTextArea(String cmpdNamedSetTextArea) {
    this.cmpdNamedSetTextArea = cmpdNamedSetTextArea;
  }

  /**
   * @return the nscTextArea
   */
  public String getNscTextArea() {
    return nscTextArea;
  }

  /**
   * @param nscTextArea the nscTextArea to set
   */
  public void setNscTextArea(String nscTextArea) {
    this.nscTextArea = nscTextArea;
  }

  /**
   * @return the projectCodeTextArea
   */
  public String getProjectCodeTextArea() {
    return projectCodeTextArea;
  }

  /**
   * @param projectCodeTextArea the projectCodeTextArea to set
   */
  public void setProjectCodeTextArea(String projectCodeTextArea) {
    this.projectCodeTextArea = projectCodeTextArea;
  }

  /**
   * @return the plateTextArea
   */
  public String getPlateTextArea() {
    return plateTextArea;
  }

  /**
   * @param plateTextArea the plateTextArea to set
   */
  public void setPlateTextArea(String plateTextArea) {
    this.plateTextArea = plateTextArea;
  }

  /**
   * @return the targetTextArea
   */
  public String getTargetTextArea() {
    return targetTextArea;
  }

  /**
   * @param targetTextArea the targetTextArea to set
   */
  public void setTargetTextArea(String targetTextArea) {
    this.targetTextArea = targetTextArea;
  }

  /**
   * @return the min_molecularWeight
   */
  public Double getMin_molecularWeight() {
    return min_molecularWeight;
  }

  /**
   * @param min_molecularWeight the min_molecularWeight to set
   */
  public void setMin_molecularWeight(Double min_molecularWeight) {
    this.min_molecularWeight = min_molecularWeight;
  }

  /**
   * @return the max_molecularWeight
   */
  public Double getMax_molecularWeight() {
    return max_molecularWeight;
  }

  /**
   * @param max_molecularWeight the max_molecularWeight to set
   */
  public void setMax_molecularWeight(Double max_molecularWeight) {
    this.max_molecularWeight = max_molecularWeight;
  }

  /**
   * @return the molecularFormula
   */
  public String getMolecularFormula() {
    return molecularFormula;
  }

  /**
   * @param molecularFormula the molecularFormula to set
   */
  public void setMolecularFormula(String molecularFormula) {
    this.molecularFormula = molecularFormula;
  }

  /**
   * @return the min_logD
   */
  public Double getMin_logD() {
    return min_logD;
  }

  /**
   * @param min_logD the min_logD to set
   */
  public void setMin_logD(Double min_logD) {
    this.min_logD = min_logD;
  }

  /**
   * @return the max_logD
   */
  public Double getMax_logD() {
    return max_logD;
  }

  /**
   * @param max_logD the max_logD to set
   */
  public void setMax_logD(Double max_logD) {
    this.max_logD = max_logD;
  }

  /**
   * @return the min_countHydBondAcceptors
   */
  public Integer getMin_countHydBondAcceptors() {
    return min_countHydBondAcceptors;
  }

  /**
   * @param min_countHydBondAcceptors the min_countHydBondAcceptors to set
   */
  public void setMin_countHydBondAcceptors(Integer min_countHydBondAcceptors) {
    this.min_countHydBondAcceptors = min_countHydBondAcceptors;
  }

  /**
   * @return the max_countHydBondAcceptors
   */
  public Integer getMax_countHydBondAcceptors() {
    return max_countHydBondAcceptors;
  }

  /**
   * @param max_countHydBondAcceptors the max_countHydBondAcceptors to set
   */
  public void setMax_countHydBondAcceptors(Integer max_countHydBondAcceptors) {
    this.max_countHydBondAcceptors = max_countHydBondAcceptors;
  }

  /**
   * @return the min_countHydBondDonors
   */
  public Integer getMin_countHydBondDonors() {
    return min_countHydBondDonors;
  }

  /**
   * @param min_countHydBondDonors the min_countHydBondDonors to set
   */
  public void setMin_countHydBondDonors(Integer min_countHydBondDonors) {
    this.min_countHydBondDonors = min_countHydBondDonors;
  }

  /**
   * @return the max_countHydBondDonors
   */
  public Integer getMax_countHydBondDonors() {
    return max_countHydBondDonors;
  }

  /**
   * @param max_countHydBondDonors the max_countHydBondDonors to set
   */
  public void setMax_countHydBondDonors(Integer max_countHydBondDonors) {
    this.max_countHydBondDonors = max_countHydBondDonors;
  }

  /**
   * @return the min_surfaceArea
   */
  public Double getMin_surfaceArea() {
    return min_surfaceArea;
  }

  /**
   * @param min_surfaceArea the min_surfaceArea to set
   */
  public void setMin_surfaceArea(Double min_surfaceArea) {
    this.min_surfaceArea = min_surfaceArea;
  }

  /**
   * @return the max_surfaceArea
   */
  public Double getMax_surfaceArea() {
    return max_surfaceArea;
  }

  /**
   * @param max_surfaceArea the max_surfaceArea to set
   */
  public void setMax_surfaceArea(Double max_surfaceArea) {
    this.max_surfaceArea = max_surfaceArea;
  }

  /**
   * @return the min_solubility
   */
  public Double getMin_solubility() {
    return min_solubility;
  }

  /**
   * @param min_solubility the min_solubility to set
   */
  public void setMin_solubility(Double min_solubility) {
    this.min_solubility = min_solubility;
  }

  /**
   * @return the max_solubility
   */
  public Double getMax_solubility() {
    return max_solubility;
  }

  /**
   * @param max_solubility the max_solubility to set
   */
  public void setMax_solubility(Double max_solubility) {
    this.max_solubility = max_solubility;
  }

  /**
   * @return the min_countRings
   */
  public Integer getMin_countRings() {
    return min_countRings;
  }

  /**
   * @param min_countRings the min_countRings to set
   */
  public void setMin_countRings(Integer min_countRings) {
    this.min_countRings = min_countRings;
  }

  /**
   * @return the max_countRings
   */
  public Integer getMax_countRings() {
    return max_countRings;
  }

  /**
   * @param max_countRings the max_countRings to set
   */
  public void setMax_countRings(Integer max_countRings) {
    this.max_countRings = max_countRings;
  }

  /**
   * @return the min_countAtoms
   */
  public Integer getMin_countAtoms() {
    return min_countAtoms;
  }

  /**
   * @param min_countAtoms the min_countAtoms to set
   */
  public void setMin_countAtoms(Integer min_countAtoms) {
    this.min_countAtoms = min_countAtoms;
  }

  /**
   * @return the max_countAtoms
   */
  public Integer getMax_countAtoms() {
    return max_countAtoms;
  }

  /**
   * @param max_countAtoms the max_countAtoms to set
   */
  public void setMax_countAtoms(Integer max_countAtoms) {
    this.max_countAtoms = max_countAtoms;
  }

  /**
   * @return the min_countBonds
   */
  public Integer getMin_countBonds() {
    return min_countBonds;
  }

  /**
   * @param min_countBonds the min_countBonds to set
   */
  public void setMin_countBonds(Integer min_countBonds) {
    this.min_countBonds = min_countBonds;
  }

  /**
   * @return the max_countBonds
   */
  public Integer getMax_countBonds() {
    return max_countBonds;
  }

  /**
   * @param max_countBonds the max_countBonds to set
   */
  public void setMax_countBonds(Integer max_countBonds) {
    this.max_countBonds = max_countBonds;
  }

  /**
   * @return the min_countSingleBonds
   */
  public Integer getMin_countSingleBonds() {
    return min_countSingleBonds;
  }

  /**
   * @param min_countSingleBonds the min_countSingleBonds to set
   */
  public void setMin_countSingleBonds(Integer min_countSingleBonds) {
    this.min_countSingleBonds = min_countSingleBonds;
  }

  /**
   * @return the max_countSingleBonds
   */
  public Integer getMax_countSingleBonds() {
    return max_countSingleBonds;
  }

  /**
   * @param max_countSingleBonds the max_countSingleBonds to set
   */
  public void setMax_countSingleBonds(Integer max_countSingleBonds) {
    this.max_countSingleBonds = max_countSingleBonds;
  }

  /**
   * @return the min_countDoubleBonds
   */
  public Integer getMin_countDoubleBonds() {
    return min_countDoubleBonds;
  }

  /**
   * @param min_countDoubleBonds the min_countDoubleBonds to set
   */
  public void setMin_countDoubleBonds(Integer min_countDoubleBonds) {
    this.min_countDoubleBonds = min_countDoubleBonds;
  }

  /**
   * @return the max_countDoubleBonds
   */
  public Integer getMax_countDoubleBonds() {
    return max_countDoubleBonds;
  }

  /**
   * @param max_countDoubleBonds the max_countDoubleBonds to set
   */
  public void setMax_countDoubleBonds(Integer max_countDoubleBonds) {
    this.max_countDoubleBonds = max_countDoubleBonds;
  }

  /**
   * @return the min_countTripleBonds
   */
  public Integer getMin_countTripleBonds() {
    return min_countTripleBonds;
  }

  /**
   * @param min_countTripleBonds the min_countTripleBonds to set
   */
  public void setMin_countTripleBonds(Integer min_countTripleBonds) {
    this.min_countTripleBonds = min_countTripleBonds;
  }

  /**
   * @return the max_countTripleBonds
   */
  public Integer getMax_countTripleBonds() {
    return max_countTripleBonds;
  }

  /**
   * @param max_countTripleBonds the max_countTripleBonds to set
   */
  public void setMax_countTripleBonds(Integer max_countTripleBonds) {
    this.max_countTripleBonds = max_countTripleBonds;
  }

  /**
   * @return the min_countRotatableBonds
   */
  public Integer getMin_countRotatableBonds() {
    return min_countRotatableBonds;
  }

  /**
   * @param min_countRotatableBonds the min_countRotatableBonds to set
   */
  public void setMin_countRotatableBonds(Integer min_countRotatableBonds) {
    this.min_countRotatableBonds = min_countRotatableBonds;
  }

  /**
   * @return the max_countRotatableBonds
   */
  public Integer getMax_countRotatableBonds() {
    return max_countRotatableBonds;
  }

  /**
   * @param max_countRotatableBonds the max_countRotatableBonds to set
   */
  public void setMax_countRotatableBonds(Integer max_countRotatableBonds) {
    this.max_countRotatableBonds = max_countRotatableBonds;
  }

  /**
   * @return the min_countHydrogenAtoms
   */
  public Integer getMin_countHydrogenAtoms() {
    return min_countHydrogenAtoms;
  }

  /**
   * @param min_countHydrogenAtoms the min_countHydrogenAtoms to set
   */
  public void setMin_countHydrogenAtoms(Integer min_countHydrogenAtoms) {
    this.min_countHydrogenAtoms = min_countHydrogenAtoms;
  }

  /**
   * @return the max_countHydrogenAtoms
   */
  public Integer getMax_countHydrogenAtoms() {
    return max_countHydrogenAtoms;
  }

  /**
   * @param max_countHydrogenAtoms the max_countHydrogenAtoms to set
   */
  public void setMax_countHydrogenAtoms(Integer max_countHydrogenAtoms) {
    this.max_countHydrogenAtoms = max_countHydrogenAtoms;
  }

  /**
   * @return the min_countMetalAtoms
   */
  public Integer getMin_countMetalAtoms() {
    return min_countMetalAtoms;
  }

  /**
   * @param min_countMetalAtoms the min_countMetalAtoms to set
   */
  public void setMin_countMetalAtoms(Integer min_countMetalAtoms) {
    this.min_countMetalAtoms = min_countMetalAtoms;
  }

  /**
   * @return the max_countMetalAtoms
   */
  public Integer getMax_countMetalAtoms() {
    return max_countMetalAtoms;
  }

  /**
   * @param max_countMetalAtoms the max_countMetalAtoms to set
   */
  public void setMax_countMetalAtoms(Integer max_countMetalAtoms) {
    this.max_countMetalAtoms = max_countMetalAtoms;
  }

  /**
   * @return the min_countHeavyAtoms
   */
  public Integer getMin_countHeavyAtoms() {
    return min_countHeavyAtoms;
  }

  /**
   * @param min_countHeavyAtoms the min_countHeavyAtoms to set
   */
  public void setMin_countHeavyAtoms(Integer min_countHeavyAtoms) {
    this.min_countHeavyAtoms = min_countHeavyAtoms;
  }

  /**
   * @return the max_countHeavyAtoms
   */
  public Integer getMax_countHeavyAtoms() {
    return max_countHeavyAtoms;
  }

  /**
   * @param max_countHeavyAtoms the max_countHeavyAtoms to set
   */
  public void setMax_countHeavyAtoms(Integer max_countHeavyAtoms) {
    this.max_countHeavyAtoms = max_countHeavyAtoms;
  }

  /**
   * @return the min_countPositiveAtoms
   */
  public Integer getMin_countPositiveAtoms() {
    return min_countPositiveAtoms;
  }

  /**
   * @param min_countPositiveAtoms the min_countPositiveAtoms to set
   */
  public void setMin_countPositiveAtoms(Integer min_countPositiveAtoms) {
    this.min_countPositiveAtoms = min_countPositiveAtoms;
  }

  /**
   * @return the max_countPositiveAtoms
   */
  public Integer getMax_countPositiveAtoms() {
    return max_countPositiveAtoms;
  }

  /**
   * @param max_countPositiveAtoms the max_countPositiveAtoms to set
   */
  public void setMax_countPositiveAtoms(Integer max_countPositiveAtoms) {
    this.max_countPositiveAtoms = max_countPositiveAtoms;
  }

  /**
   * @return the min_countNegativeAtoms
   */
  public Integer getMin_countNegativeAtoms() {
    return min_countNegativeAtoms;
  }

  /**
   * @param min_countNegativeAtoms the min_countNegativeAtoms to set
   */
  public void setMin_countNegativeAtoms(Integer min_countNegativeAtoms) {
    this.min_countNegativeAtoms = min_countNegativeAtoms;
  }

  /**
   * @return the max_countNegativeAtoms
   */
  public Integer getMax_countNegativeAtoms() {
    return max_countNegativeAtoms;
  }

  /**
   * @param max_countNegativeAtoms the max_countNegativeAtoms to set
   */
  public void setMax_countNegativeAtoms(Integer max_countNegativeAtoms) {
    this.max_countNegativeAtoms = max_countNegativeAtoms;
  }

  /**
   * @return the min_countRingBonds
   */
  public Integer getMin_countRingBonds() {
    return min_countRingBonds;
  }

  /**
   * @param min_countRingBonds the min_countRingBonds to set
   */
  public void setMin_countRingBonds(Integer min_countRingBonds) {
    this.min_countRingBonds = min_countRingBonds;
  }

  /**
   * @return the max_countRingBonds
   */
  public Integer getMax_countRingBonds() {
    return max_countRingBonds;
  }

  /**
   * @param max_countRingBonds the max_countRingBonds to set
   */
  public void setMax_countRingBonds(Integer max_countRingBonds) {
    this.max_countRingBonds = max_countRingBonds;
  }

  /**
   * @return the min_countStereoAtoms
   */
  public Integer getMin_countStereoAtoms() {
    return min_countStereoAtoms;
  }

  /**
   * @param min_countStereoAtoms the min_countStereoAtoms to set
   */
  public void setMin_countStereoAtoms(Integer min_countStereoAtoms) {
    this.min_countStereoAtoms = min_countStereoAtoms;
  }

  /**
   * @return the max_countStereoAtoms
   */
  public Integer getMax_countStereoAtoms() {
    return max_countStereoAtoms;
  }

  /**
   * @param max_countStereoAtoms the max_countStereoAtoms to set
   */
  public void setMax_countStereoAtoms(Integer max_countStereoAtoms) {
    this.max_countStereoAtoms = max_countStereoAtoms;
  }

  /**
   * @return the min_countStereoBonds
   */
  public Integer getMin_countStereoBonds() {
    return min_countStereoBonds;
  }

  /**
   * @param min_countStereoBonds the min_countStereoBonds to set
   */
  public void setMin_countStereoBonds(Integer min_countStereoBonds) {
    this.min_countStereoBonds = min_countStereoBonds;
  }

  /**
   * @return the max_countStereoBonds
   */
  public Integer getMax_countStereoBonds() {
    return max_countStereoBonds;
  }

  /**
   * @param max_countStereoBonds the max_countStereoBonds to set
   */
  public void setMax_countStereoBonds(Integer max_countStereoBonds) {
    this.max_countStereoBonds = max_countStereoBonds;
  }

  /**
   * @return the min_countRingAssemblies
   */
  public Integer getMin_countRingAssemblies() {
    return min_countRingAssemblies;
  }

  /**
   * @param min_countRingAssemblies the min_countRingAssemblies to set
   */
  public void setMin_countRingAssemblies(Integer min_countRingAssemblies) {
    this.min_countRingAssemblies = min_countRingAssemblies;
  }

  /**
   * @return the max_countRingAssemblies
   */
  public Integer getMax_countRingAssemblies() {
    return max_countRingAssemblies;
  }

  /**
   * @param max_countRingAssemblies the max_countRingAssemblies to set
   */
  public void setMax_countRingAssemblies(Integer max_countRingAssemblies) {
    this.max_countRingAssemblies = max_countRingAssemblies;
  }

  /**
   * @return the min_countAromaticBonds
   */
  public Integer getMin_countAromaticBonds() {
    return min_countAromaticBonds;
  }

  /**
   * @param min_countAromaticBonds the min_countAromaticBonds to set
   */
  public void setMin_countAromaticBonds(Integer min_countAromaticBonds) {
    this.min_countAromaticBonds = min_countAromaticBonds;
  }

  /**
   * @return the max_countAromaticBonds
   */
  public Integer getMax_countAromaticBonds() {
    return max_countAromaticBonds;
  }

  /**
   * @param max_countAromaticBonds the max_countAromaticBonds to set
   */
  public void setMax_countAromaticBonds(Integer max_countAromaticBonds) {
    this.max_countAromaticBonds = max_countAromaticBonds;
  }

  /**
   * @return the min_countAromaticRings
   */
  public Integer getMin_countAromaticRings() {
    return min_countAromaticRings;
  }

  /**
   * @param min_countAromaticRings the min_countAromaticRings to set
   */
  public void setMin_countAromaticRings(Integer min_countAromaticRings) {
    this.min_countAromaticRings = min_countAromaticRings;
  }

  /**
   * @return the max_countAromaticRings
   */
  public Integer getMax_countAromaticRings() {
    return max_countAromaticRings;
  }

  /**
   * @param max_countAromaticRings the max_countAromaticRings to set
   */
  public void setMax_countAromaticRings(Integer max_countAromaticRings) {
    this.max_countAromaticRings = max_countAromaticRings;
  }

  /**
   * @return the min_formalCharge
   */
  public Integer getMin_formalCharge() {
    return min_formalCharge;
  }

  /**
   * @param min_formalCharge the min_formalCharge to set
   */
  public void setMin_formalCharge(Integer min_formalCharge) {
    this.min_formalCharge = min_formalCharge;
  }

  /**
   * @return the max_formalCharge
   */
  public Integer getMax_formalCharge() {
    return max_formalCharge;
  }

  /**
   * @param max_formalCharge the max_formalCharge to set
   */
  public void setMax_formalCharge(Integer max_formalCharge) {
    this.max_formalCharge = max_formalCharge;
  }

  /**
   * @return the min_theALogP
   */
  public Double getMin_theALogP() {
    return min_theALogP;
  }

  /**
   * @param min_theALogP the min_theALogP to set
   */
  public void setMin_theALogP(Double min_theALogP) {
    this.min_theALogP = min_theALogP;
  }

  /**
   * @return the max_theALogP
   */
  public Double getMax_theALogP() {
    return max_theALogP;
  }

  /**
   * @param max_theALogP the max_theALogP to set
   */
  public void setMax_theALogP(Double max_theALogP) {
    this.max_theALogP = max_theALogP;
  }

}
