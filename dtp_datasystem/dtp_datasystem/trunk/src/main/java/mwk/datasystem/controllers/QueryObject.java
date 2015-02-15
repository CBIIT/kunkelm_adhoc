/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mwkunkel
 */
public class QueryObject implements Serializable {

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
  public QueryObject() {
    this.drugNames = new ArrayList<String>();
    this.aliases = new ArrayList<String>();
    this.cases = new ArrayList<String>();
    this.cmpdNamedSets = new ArrayList<String>();
    this.nscs = new ArrayList<String>();
    this.projectCodes = new ArrayList<String>();
    this.plates = new ArrayList<String>();
    this.targets = new ArrayList<String>();
  }
  
  public void printCriteriaLists(){
    
    System.out.println("drugNames");
    for (String s : this.drugNames){
      System.out.println("-------->" + s + "<--------");      
    }
    
    System.out.println("aliases");
    for (String s : this.aliases){
      System.out.println("-------->" + s + "<--------");      
    }
    
    System.out.println("cases");
    for (String s : this.cases){
      System.out.println("-------->" + s + "<--------");      
    }
    
    System.out.println("cmpdNamedSets");
    for (String s : this.cmpdNamedSets){
      System.out.println("-------->" + s + "<--------");      
    }
    
    System.out.println("nscs");
    for (String s : this.nscs){
      System.out.println("-------->" + s + "<--------");      
    }
    
    System.out.println("projectCodes");
    for (String s : this.projectCodes){
      System.out.println("-------->" + s + "<--------");      
    }
    
    System.out.println("plates");
    for (String s : this.plates){
      System.out.println("-------->" + s + "<--------");      
    }
    
    System.out.println("targets");
    for (String s : this.targets){
      System.out.println("-------->" + s + "<--------");      
    }
    
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

}
