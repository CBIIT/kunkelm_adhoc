/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import jpa.entity.DrugTrackerAlias;
import jpa.entity.DrugTracker;
import jpa.entity.NscDrugTracker;
import jpa.entity.DrugTrackerTarget;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class DrugTrackerApplicationBean implements Serializable {

  private static final long serialVersionUID = 3750764015565272777L;
  private List<SelectItem> agentItems;
  private List<SelectItem> aliasItems;
  private List<SelectItem> casItems;
  private List<SelectItem> nscItems;
  private List<SelectItem> drugStatusItems;
  private List<SelectItem> originatorItems;
  private List<SelectItem> plateItems;
  private List<SelectItem> targetItems;

  public DrugTrackerApplicationBean() {

    agentItems = new ArrayList<SelectItem>();
    aliasItems = new ArrayList<SelectItem>();
    casItems = new ArrayList<SelectItem>();
    nscItems = new ArrayList<SelectItem>();
    drugStatusItems = new ArrayList<SelectItem>();
    originatorItems = new ArrayList<SelectItem>();
    plateItems = new ArrayList<SelectItem>();
    targetItems = new ArrayList<SelectItem>();

    EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("DrugTrackerPU");
    EntityManager em = emf.createEntityManager();
    CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();

    // DrugTracker loads agent, originator, cas

    CriteriaQuery<DrugTracker> dtQuery = criteriaBuilder.createQuery(DrugTracker.class);
    TypedQuery<DrugTracker> dtTQ = em.createQuery(dtQuery);
    List<DrugTracker> dtResultList = dtTQ.getResultList();

    for (DrugTracker dt : dtResultList) {
      if (dt.getAgent() != null) {
        agentItems.add(new SelectItem(dt.getAgent(), dt.getAgent()));
      }
      if (dt.getCas() != null) {
        casItems.add(new SelectItem(dt.getCas(), dt.getCas()));
      }
      if (dt.getOriginator() != null) {
        originatorItems.add(new SelectItem(dt.getOriginator(), dt.getOriginator()));
      }
    }

    // sort the lists

    Collections.sort(agentItems, new util.Comparators.SelectItemComparator());
    Collections.sort(casItems, new util.Comparators.SelectItemComparator());
    Collections.sort(originatorItems, new util.Comparators.SelectItemComparator());

    // aliases

    CriteriaQuery<DrugTrackerAlias> aliaQuery = criteriaBuilder.createQuery(DrugTrackerAlias.class);
    TypedQuery<DrugTrackerAlias> aliaTQ = em.createQuery(aliaQuery);
    List<DrugTrackerAlias> aliaResultList = aliaTQ.getResultList();


    for (DrugTrackerAlias a : aliaResultList) {
      if (a.getAlias() != null) {
        aliasItems.add(new SelectItem(a.getAlias(), a.getAlias()));
      }
    }
    Collections.sort(aliasItems, new util.Comparators.SelectItemComparator());

    // APPROVED OR INVESIGATIONAL    
    drugStatusItems.add(new SelectItem("APPROVED", "APPROVED"));
    drugStatusItems.add(new SelectItem("INVESTIGATIONAL", "INVESTIGATIONAL"));

    plateItems.add(new SelectItem("APPROVED", "APPROVED"));
    plateItems.add(new SelectItem("INVESTIGATIONAL", "INVESTIGATIONAL"));
    plateItems.add(new SelectItem("NOT PLATED", "NOT PLATED"));


    // sort the NSC in the query
    CriteriaQuery<NscDrugTracker> nscDtQuery = criteriaBuilder.createQuery(NscDrugTracker.class);
    Root e = nscDtQuery.from(NscDrugTracker.class);
    nscDtQuery.orderBy(criteriaBuilder.asc(e.get("nsc")));
    TypedQuery<NscDrugTracker> nscDtTQ = em.createQuery(nscDtQuery);
    List<NscDrugTracker> nscResultList = nscDtTQ.getResultList();

    for (NscDrugTracker nscDt : nscResultList) {
      if (nscDt.getNsc() != null) {
        nscItems.add(new SelectItem(nscDt.getNsc().toString(), nscDt.getNsc().toString()));
      }
    }

    CriteriaQuery<DrugTrackerTarget> targQuery = criteriaBuilder.createQuery(DrugTrackerTarget.class);
    TypedQuery<DrugTrackerTarget> targTQ = em.createQuery(targQuery);
    List<DrugTrackerTarget> targResultList = targTQ.getResultList();

    for (DrugTrackerTarget t : targResultList) {
      if (t.getTarget() != null) {
        targetItems.add(new SelectItem(t.getTarget(), t.getTarget()));
      }
    }
    Collections.sort(targetItems, new util.Comparators.SelectItemComparator());
    
  }

  //
  // auto-complete
  //
  public List<String> completeAgent(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : agentItems) {

      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeAlias(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : aliasItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeCas(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : casItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeDrugStatus(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : drugStatusItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeNsc(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : nscItems) {
      if (si.getLabel().startsWith(query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeOriginator(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : originatorItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completePlate(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : plateItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeTarget(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : targetItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  //
  //
  //
  public List<SelectItem> getAgentItems() {
    return agentItems;
  }

  public void setAgentItems(List<SelectItem> agentItems) {
    agentItems = agentItems;
  }

  public List<SelectItem> getAliasItems() {
    return aliasItems;
  }

  public void setAliasItems(List<SelectItem> aliasItems) {
    aliasItems = aliasItems;
  }

  public List<SelectItem> getCasItems() {
    return casItems;
  }

  public void setCasItems(List<SelectItem> casItems) {
    casItems = casItems;
  }

  public List<SelectItem> getNscItems() {
    return nscItems;
  }

  public void setNscItems(List<SelectItem> nscItems) {
    nscItems = nscItems;
  }

  public List<SelectItem> getDrugStatusItems() {
    return drugStatusItems;
  }

  public void setDrugStatusItems(List<SelectItem> drugStatusItems) {
    drugStatusItems = drugStatusItems;
  }

  public List<SelectItem> getOriginatorItems() {
    return originatorItems;
  }

  public void setOriginatorItems(List<SelectItem> originatorItems) {
    originatorItems = originatorItems;
  }

  public List<SelectItem> getPlateItems() {
    return plateItems;
  }

  public void setPlateItems(List<SelectItem> plateItems) {
    plateItems = plateItems;
  }

  public List<SelectItem> getTargetItems() {
    return targetItems;
  }

  public void setTargetItems(List<SelectItem> targetItems) {
    targetItems = targetItems;
  }
}