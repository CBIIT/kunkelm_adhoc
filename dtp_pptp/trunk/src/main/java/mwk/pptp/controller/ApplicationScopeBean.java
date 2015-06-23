/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.pptp.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import javax.faces.model.SelectItem;
import mwk.pptp.domain.CellLineType;
import mwk.pptp.domain.CompoundType;
import mwk.pptp.util.HibernateUtil;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class ApplicationScopeBean implements Serializable {

  private static final long serialVersionUID = 4105761574565272777L;
  //
  protected List<SelectItem> pptpIdentifierItems;
  protected List<SelectItem> drugNameItems;
  protected List<SelectItem> cellTypeItems;  
  protected List<SelectItem> cellLineItems;
  //
  protected List<SelectItem> geneItems;
  protected List<SelectItem> geneNameItems;
  //
  protected List<SelectItem> snpItems;

  public ApplicationScopeBean() {

      pptpIdentifierItems = new ArrayList<SelectItem>();
      drugNameItems = new ArrayList<SelectItem>();      
      cellLineItems = new ArrayList<SelectItem>();
      cellTypeItems = new ArrayList<SelectItem>();
      geneItems = new ArrayList<SelectItem>();
      geneNameItems = new ArrayList<SelectItem>();
      snpItems = new ArrayList<SelectItem>();

      Session session = HibernateUtil.getSessionFactory().getCurrentSession();

      try {

        Transaction tx = session.beginTransaction();
        Criteria cmpdCrit = session.createCriteria(CompoundType.class);
        List<CompoundType> cmpdResultList = (List<CompoundType>) cmpdCrit.list();

        HashSet<String> nscSet = new HashSet<String>();
        HashSet<String> drugNameSet = new HashSet<String>();

        for (CompoundType compoundType : cmpdResultList) {
          if (compoundType.getPptpIdentifier() != null) {
            nscSet.add(compoundType.getPptpIdentifier());
          }
          if (compoundType.getName() != null) {
            drugNameSet.add(compoundType.getName());
          }
        }

        ArrayList<String> nscList = new ArrayList<String>(nscSet);
        Collections.sort(nscList);
        for (String s : nscList) {
          pptpIdentifierItems.add(new SelectItem(s.toString(), s.toString()));
        }

        ArrayList<String> drugNameList = new ArrayList<String>(drugNameSet);
        Collections.sort(drugNameList);
        for (String s : drugNameList) {
          drugNameItems.add(new SelectItem(s, s));
        }

        // CellLineType

        Criteria cellCrit = session.createCriteria(CellLineType.class);
        List<CellLineType> cellLineResultList = (List<CellLineType>) cellCrit.list();

        HashSet<String> cellLineSet = new HashSet<String>();
        HashSet<String> cellTypeSet = new HashSet<String>();

        for (CellLineType cl : cellLineResultList) {
          if (cl.getName() != null) {
            cellLineSet.add(cl.getName());
          }
          if (cl.getCellType() != null) {
            cellTypeSet.add(cl.getCellType().getDisplayName());
          }
        }

        ArrayList<String> cellLineList = new ArrayList<String>(cellLineSet);
        Collections.sort(cellLineList);
        for (String s : cellLineList) {
          cellLineItems.add(new SelectItem(s, s));
        }

        ArrayList<String> panelNameList = new ArrayList<String>(cellTypeSet);
        Collections.sort(panelNameList);
        for (String s : panelNameList) {
          cellTypeItems.add(new SelectItem(s, s));
        }

        // AffymetrixIdentifier

//        Criteria affyCrit = session.createCriteria(AffymetrixExonIdentifier.class);
//        List<AffymetrixExonIdentifier> affyResultList = (List<AffymetrixExonIdentifier>) affyCrit.list();
//
//        HashSet<String> geneSet = new HashSet<String>();
//        HashSet<String> geneNameSet = new HashSet<String>();
//
//        for (AffymetrixExonIdentifier aei : affyResultList) {
//          if (aei.getGeneSymbol() != null) {
//            geneSet.add(aei.getGeneSymbol());
//          }
//          if (aei.getGeneName() != null) {
//            geneNameSet.add(aei.getGeneName());
//          }
//        }
//
//        ArrayList<String> geneList = new ArrayList<String>(geneSet);
//        Collections.sort(geneList);
//        for (String s : geneList) {
//          geneItems.add(new SelectItem(s, s));
//        }
//
//        ArrayList<String> geneNameList = new ArrayList<String>(geneNameSet);
//        Collections.sort(geneNameList);
//        for (String s : geneNameList) {
//          geneNameItems.add(new SelectItem(s, s));
//        }
//
//        // NanoStringIdentifier
//
//        Criteria nanoCrit = session.createCriteria(NanoStringIdentifier.class);
//        List<NanoStringIdentifier> nanoStringResultList = (List<NanoStringIdentifier>) nanoCrit.list();
//
//        HashSet<String> mirSet = new HashSet<String>();
//
//        for (NanoStringIdentifier nsi : nanoStringResultList) {
//          if (nsi.getMiR() != null) {
//            mirSet.add(nsi.getMiR());
//          }
//        }
//
//        ArrayList<String> mirList = new ArrayList<String>(mirSet);
//        Collections.sort(mirList);
//        for (String s : mirList) {
//          mirItems.add(new SelectItem(s, s));
//        }

        tx.commit();

      } catch (Exception e) {
        e.printStackTrace();
      }

    } // end constructor

//    
// auto-complete
//
  

  public List<String> completePptpIdentifier(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : pptpIdentifierItems) {
      if (si.getLabel().startsWith(query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeDrugName(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : drugNameItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeCellLine(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : cellLineItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeCellType(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : cellTypeItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeGene(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : geneItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeGeneName(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : geneNameItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  public List<String> completeSnp(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (SelectItem si : snpItems) {
      if (StringUtils.containsIgnoreCase(si.getLabel(), query)) {
        suggestions.add(si.getLabel());
      }
    }
    return suggestions;
  }

  // GETTERS and SETTERS

    public List<SelectItem> getPptpIdentifierItems() {
        return pptpIdentifierItems;
    }

    public void setPptpIdentifierItems(List<SelectItem> pptpIdentifierItems) {
        this.pptpIdentifierItems = pptpIdentifierItems;
    }

    public List<SelectItem> getDrugNameItems() {
        return drugNameItems;
    }

    public void setDrugNameItems(List<SelectItem> drugNameItems) {
        this.drugNameItems = drugNameItems;
    }

    public List<SelectItem> getCellTypeItems() {
        return cellTypeItems;
    }

    public void setCellTypeItems(List<SelectItem> cellTypeItems) {
        this.cellTypeItems = cellTypeItems;
    }

    public List<SelectItem> getCellLineItems() {
        return cellLineItems;
    }

    public void setCellLineItems(List<SelectItem> cellLineItems) {
        this.cellLineItems = cellLineItems;
    }

    public List<SelectItem> getGeneItems() {
        return geneItems;
    }

    public void setGeneItems(List<SelectItem> geneItems) {
        this.geneItems = geneItems;
    }

    public List<SelectItem> getGeneNameItems() {
        return geneNameItems;
    }

    public void setGeneNameItems(List<SelectItem> geneNameItems) {
        this.geneNameItems = geneNameItems;
    }

    public List<SelectItem> getSnpItems() {
        return snpItems;
    }

    public void setSnpItems(List<SelectItem> snpItems) {
        this.snpItems = snpItems;
    }
  
}