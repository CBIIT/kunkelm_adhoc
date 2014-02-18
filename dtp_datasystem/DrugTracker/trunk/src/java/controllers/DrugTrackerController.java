/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import jpa.entity.DrugTrackerAlias;
import jpa.entity.DrugTrackerSet;
import jpa.entity.DrugTracker;
import jpa.entity.DrugTrackerPlate;
import jpa.entity.DrugTracker_;
import jpa.entity.NscDrugTracker;
import jpa.entity.DrugTrackerTarget;
import util.Translators;
import vo.DrugTrackerVO;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class DrugTrackerController implements Serializable {

  private String nscForJChemPaintLoad;
  private String smilesForJChemPaintLoad;
  private List<String> agents;
  private List<String> aliases;
  private List<String> cases;
  private List<String> drugStatuses;
  private List<String> nscs;
  private List<String> originators;
  private List<String> plates;
  private List<String> targets;
  //
  private List<DrugTrackerVO> drugTrackers;
  private DrugTrackerVO selectedDrugTracker;
  private DrugTrackerVO[] selectedDrugTrackers;

  public String showSelectedDrugTrackers() {

    System.out.println("Now in showSelectedRows in DrugTrackerController.");

    System.out.println("Size of this.selectedDrugTrackers is: " + this.selectedDrugTrackers.length);

    for (int i = 0; i < this.selectedDrugTrackers.length; i++) {
      System.out.println("Clds Id: " + this.selectedDrugTrackers[i].getId() + " NSC: " + this.selectedDrugTrackers[i].getNsc());
    }

    StringBuilder sb = new StringBuilder();

    for (int i = 0; i < this.selectedDrugTrackers.length; i++) {
      sb.append(this.selectedDrugTrackers[i].getId() + " NSC: " + this.selectedDrugTrackers[i].getNsc());
    }

    FacesMessage msg = new FacesMessage(
            FacesMessage.SEVERITY_INFO,
            "Selected DrugTrackers: ",
            sb.toString());

    FacesContext.getCurrentInstance().addMessage(null, msg);

    return null;

  }

  /**
   * Creates a new instance of DrugTrackerController
   */
  public DrugTrackerController() {

    agents = new ArrayList<String>();
    aliases = new ArrayList<String>();
    cases = new ArrayList<String>();
    drugStatuses = new ArrayList<String>();
    nscs = new ArrayList<String>();
    originators = new ArrayList<String>();
    plates = new ArrayList<String>();
    targets = new ArrayList<String>();

    drugTrackers = new ArrayList<DrugTrackerVO>();
    selectedDrugTracker = new DrugTrackerVO();
    selectedDrugTrackers = new DrugTrackerVO[0];

  }

  public String resetSearch() {

    // clear out previous search
    this.drugTrackers = new ArrayList<DrugTrackerVO>();

    agents = new ArrayList<String>();
    aliases = new ArrayList<String>();
    cases = new ArrayList<String>();
    drugStatuses = new ArrayList<String>();
    nscs = new ArrayList<String>();
    originators = new ArrayList<String>();
    plates = new ArrayList<String>();
    targets = new ArrayList<String>();

    drugTrackers = new ArrayList<DrugTrackerVO>();
    selectedDrugTracker = new DrugTrackerVO();
    selectedDrugTrackers = new DrugTrackerVO[0];

    return null;

  }

  public String performSearch() {

    try {

      // clear out previous search
      this.drugTrackers = new ArrayList<DrugTrackerVO>();

      EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("DrugTrackerPU");
      EntityManager em = emf.createEntityManager();
      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

      CriteriaQuery<DrugTracker> criteriaQuery = criteriaBuilder.createQuery(DrugTracker.class);

      Root<DrugTracker> rdt = criteriaQuery.from(DrugTracker.class);

      // this is the conjunctin of compounds with plates and/or sets and/or targets
      List<Predicate> conjPredList = new ArrayList<Predicate>();

      List<Long> nscLongList = new ArrayList<Long>();

      if (nscs == null) {
        nscs = new ArrayList<String>();
      }

      // nsc, cas, agent, alias, originator as DISJUNCTION

      List<Predicate> disjPredList = new ArrayList<Predicate>();

      for (String s : nscs) {
        nscLongList.add(Long.valueOf(s));
      }

      if (!nscLongList.isEmpty()) {
        Join<DrugTracker, NscDrugTracker> nscDrugT = rdt.join(DrugTracker_.nscDrugTracker);
        disjPredList.add(nscDrugT.get("nsc").in(nscLongList));
      };

      if (!this.cases.isEmpty()) {
        disjPredList.add(rdt.get("cas").in(this.cases));
      };

      if (!this.agents.isEmpty()) {
        disjPredList.add(rdt.get("agent").in(this.agents));
      };

      if (!this.originators.isEmpty()) {
        disjPredList.add(rdt.get("originator").in(this.originators));
      };

      if (!this.aliases.isEmpty()) {
        Join<DrugTracker, DrugTrackerAlias> alia = rdt.join(DrugTracker_.drugTrackerAliasCollection);
        disjPredList.add(alia.get("alias").in(this.aliases));
      };

      // if any specs, set the disjunction as first term of overall conjunction

      if (!disjPredList.isEmpty()) {
        Predicate disj = criteriaBuilder.or(disjPredList.toArray(new Predicate[disjPredList.size()]));
        conjPredList.add(disj);
      }

      // plates/sets futher filter

      if (!this.plates.isEmpty()) {
        Join<DrugTracker, DrugTrackerPlate> drugPlate = rdt.join(DrugTracker_.drugTrackerPlateCollection);
        conjPredList.add(drugPlate.get("plateName").in(this.plates));
      };

      if (!this.drugStatuses.isEmpty()) {
        Join<DrugTracker, DrugTrackerSet> drugStatus = rdt.join(DrugTracker_.drugTrackerSetCollection);
        conjPredList.add(drugStatus.get("drugStatus").in(this.drugStatuses));
      };

      // targets
      // this is auto-complete; so, have to check for null

      if (this.targets != null && !this.targets.isEmpty()) {
        Join<DrugTracker, DrugTrackerTarget> targ = rdt.join(DrugTracker_.drugTrackerTargetCollection);
        conjPredList.add(targ.get("target").in(this.targets));
      };

      //

      Predicate conj = criteriaBuilder.and(conjPredList.toArray(new Predicate[conjPredList.size()]));

      criteriaQuery.where(conj);

      TypedQuery<DrugTracker> q = em.createQuery(criteriaQuery);
      List<DrugTracker> resultList = q.getResultList();
      
      System.out.println(" resultList contains: " + resultList.size() + " DrugTrackers");
      
//      for (DrugTracker dt : resultList) {
//        System.out.println(dt.getCas() + " " + dt.getAgent() + " " + dt.getOriginator());
//      }

      ArrayList<DrugTrackerVO> voList = new ArrayList<DrugTrackerVO>();

      for (DrugTracker dt : resultList) {
        voList.add(Translators.toDrugTrackerVO(dt));
      }

      this.drugTrackers = voList;

    } catch (Exception e) {
      e.printStackTrace();
    }

    return "success";

  }

  // GETTERS/SETTERS

  public List<String> getAgents() {
    return agents;
  }

  public void setAgents(List<String> agents) {
    this.agents = agents;
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

  public List<DrugTrackerVO> getDrugTrackers() {
    return drugTrackers;
  }

  public void setDrugTrackers(List<DrugTrackerVO> drugTrackers) {
    this.drugTrackers = drugTrackers;
  }

  public DrugTrackerVO getSelectedDrugTracker() {
    return selectedDrugTracker;
  }

  public void setSelectedDrugTracker(DrugTrackerVO selectedDrugTracker) {
    this.selectedDrugTracker = selectedDrugTracker;
  }

  public DrugTrackerVO[] getSelectedDrugTrackers() {
    return selectedDrugTrackers;
  }

  public void setSelectedDrugTrackers(DrugTrackerVO[] selectedDrugTrackers) {
    this.selectedDrugTrackers = selectedDrugTrackers;
  }

}
