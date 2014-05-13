/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Root;
import mwk.datasystem.domain.CmpdAlias;
import mwk.datasystem.util.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.CriteriaQuery;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class DrugTrackerApplicationBean implements Serializable {

  private static final long serialVersionUID = 3750764015565272777L;
  //
  private List<SelectItem> drugNameItems;
  private List<SelectItem> aliasItems;
  private List<SelectItem> casItems;
  private List<SelectItem> nscItems;
  private List<SelectItem> drugStatusItems;
  private List<SelectItem> originatorItems;
  private List<SelectItem> plateItems;
  private List<SelectItem> targetItems;

  public DrugTrackerApplicationBean() {

    drugNameItems = new ArrayList<SelectItem>();
    aliasItems = new ArrayList<SelectItem>();
    casItems = new ArrayList<SelectItem>();
    nscItems = new ArrayList<SelectItem>();
    drugStatusItems = new ArrayList<SelectItem>();
    originatorItems = new ArrayList<SelectItem>();
    plateItems = new ArrayList<SelectItem>();
    targetItems = new ArrayList<SelectItem>();

    Integer[] nscArray = {163027, 401005, 705701};
    List<Integer> nscList = Arrays.asList(nscArray);
    Collections.sort(nscList);
    for (Integer nsc : nscList) {
      nscItems.add(new SelectItem(nsc.toString(), nsc.toString()));
    }

    String[] drugNameArray = {"dox", "mtx"};
    List<String> drugNameList = Arrays.asList(drugNameArray);
    Collections.sort(drugNameList);
    for (String dn : drugNameList) {
      drugNameItems.add(new SelectItem(dn, dn));
    }

    String[] casArray = {"12-34-56", "98-76-54"};
    List<String> casList = Arrays.asList(casArray);
    Collections.sort(casList);
    for (String cas : casList) {
      casItems.add(new SelectItem(cas, cas));
    }

    String[] originatorArray = {"Abbott", "BMS"};
    List<String> originatorList = Arrays.asList(originatorArray);
    Collections.sort(originatorList);
    for (String originator : originatorList) {
      originatorItems.add(new SelectItem(originator, originator));
    }

    String[] aliasArray = {"QTSS88d", "juuis"};
    List<String> aliasList = Arrays.asList(aliasArray);
    Collections.sort(aliasList);
    for (String alias : aliasList) {
      aliasItems.add(new SelectItem(alias, alias));
    }

    // APPROVED OR INVESIGATIONAL    
    drugStatusItems.add(new SelectItem("APPROVED", "APPROVED"));
    drugStatusItems.add(new SelectItem("INVESTIGATIONAL", "INVESTIGATIONAL"));

    // APPROVED OR INVESIGATIONAL    
    plateItems.add(new SelectItem("APPROVED", "APPROVED"));
    plateItems.add(new SelectItem("INVESTIGATIONAL", "INVESTIGATIONAL"));
    plateItems.add(new SelectItem("NOT PLATED", "NOT PLATED"));

    String[] targetArray = {"EGFR", "PKC"};
    List<String> targetList = Arrays.asList(targetArray);
    Collections.sort(targetList);
    for (String target : targetList) {
      targetItems.add(new SelectItem(target, target));
    }
  }

  //
  // auto-complete
  //
  public List<String> completeDrugName(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : drugNameItems) {
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
  public List<SelectItem> getDrugNameItems() {
    return drugNameItems;
  }

  public void setDrugNameItems(List<SelectItem> drugNameItems) {
    drugNameItems = drugNameItems;
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