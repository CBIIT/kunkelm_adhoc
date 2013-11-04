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

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class DrugTrackerController implements Serializable {

  private List<String> drugNames;
  private List<String> aliases;
  private List<String> cases;
  private List<String> drugStatuses;
  private List<String> nscs;
  private List<String> originators;
  private List<String> plates;
  private List<String> targets;

  /**
   * Creates a new instance of DrugTrackerController
   */
  public DrugTrackerController() {

    drugNames = new ArrayList<String>();
    aliases = new ArrayList<String>();
    cases = new ArrayList<String>();
    drugStatuses = new ArrayList<String>();
    nscs = new ArrayList<String>();
    originators = new ArrayList<String>();
    plates = new ArrayList<String>();
    targets = new ArrayList<String>();

  }

  // GETTERS/SETTERS
  public List<String> getDrugNames() {
    return drugNames;
  }

  public void setDrugNames(List<String> drugNames) {
    this.drugNames = drugNames;
  }

  public List<String> getAliases() {
    return aliases;
  }

  public void setAliases(List<String> aliases) {
    this.aliases = aliases;
  }

  public List<String> getCases() {
    return cases;
  }

  public void setCases(List<String> cases) {
    this.cases = cases;
  }

  public List<String> getDrugStatuses() {
    return drugStatuses;
  }

  public void setDrugStatuses(List<String> drugStatuses) {
    this.drugStatuses = drugStatuses;
  }

  public List<String> getNscs() {
    return nscs;
  }

  public void setNscs(List<String> nscs) {
    this.nscs = nscs;
  }

  public List<String> getOriginators() {
    return originators;
  }

  public void setOriginators(List<String> originators) {
    this.originators = originators;
  }

  public List<String> getPlates() {
    return plates;
  }

  public void setPlates(List<String> plates) {
    this.plates = plates;
  }

  public List<String> getTargets() {
    return targets;
  }

  public void setTargets(List<String> targets) {
    this.targets = targets;
  }
}
