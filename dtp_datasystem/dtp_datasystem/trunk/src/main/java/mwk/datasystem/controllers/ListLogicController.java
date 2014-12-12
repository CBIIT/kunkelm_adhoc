/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class ListLogicController implements Serializable {

  static final long serialVersionUID = -8653468638698142855l;

  private static final Boolean DEBUG = Boolean.FALSE;
  //
  private CmpdListVO listA;
  private CmpdListVO listB;
  //
  private List<CmpdVO> cmpdsListAnotListB;
  private List<CmpdVO> cmpdsListAandListB;
  private List<CmpdVO> cmpdsListAorListB;
  //
  private List<CmpdVO> currentListOfCompounds;
  private List<CmpdVO> selectedCmpds;
  private CmpdVO selectedCmpd;
    //
  // reach-through to listManagerController
  @ManagedProperty(value = "#{listManagerController}")
  private ListManagerController listManagerController;

  public void setListManagerController(ListManagerController listManagerController) {
    this.listManagerController = listManagerController;
  }
  // reach-through to sessionController
  @ManagedProperty(value = "#{sessionController}")
  private SessionController sessionController;

  public void setSessionController(SessionController sessionController) {
    this.sessionController = sessionController;
  }

  public String performListLogic() {

    CmpdListVO aList = this.listManagerController.performLoadList(this.listA.getCmpdListId());
    CmpdListVO bList = this.listManagerController.performLoadList(this.listB.getCmpdListId());

    // overlap is based on a.nsc = b.nsc OR a.originalAdHocCmpdId = b.originalAdHocCmpdId
    HashMap<Integer, CmpdVO> nscMap = new HashMap<Integer, CmpdVO>();
    HashMap<Long, CmpdVO> ahcIdMap = new HashMap<Long, CmpdVO>();

    HashSet<Integer> nscSetA = new HashSet<Integer>();
    HashSet<Integer> nscSetB = new HashSet<Integer>();

    HashSet<Long> ahcIdSetA = new HashSet<Long>();
    HashSet<Long> ahcIdSetB = new HashSet<Long>();

    for (CmpdListMemberVO clmVO : aList.getCmpdListMembers()) {
      if (clmVO.getCmpd().getNsc() != null) {
        nscMap.put(clmVO.getCmpd().getNsc(), clmVO.getCmpd());
        nscSetA.add(clmVO.getCmpd().getNsc());
      } else {
        ahcIdMap.put(clmVO.getCmpd().getOriginalAdHocCmpdId(), clmVO.getCmpd());
        ahcIdSetA.add(clmVO.getCmpd().getOriginalAdHocCmpdId());
      }
    }

    for (CmpdListMemberVO clmVO : bList.getCmpdListMembers()) {
      if (clmVO.getCmpd().getNsc() != null) {
        nscMap.put(clmVO.getCmpd().getNsc(), clmVO.getCmpd());
        nscSetB.add(clmVO.getCmpd().getNsc());
      } else {
        ahcIdMap.put(clmVO.getCmpd().getOriginalAdHocCmpdId(), clmVO.getCmpd());
        ahcIdSetB.add(clmVO.getCmpd().getOriginalAdHocCmpdId());
      }
    }

    if (DEBUG) {
      System.out.println("a: " + aList.getListName() + " " + aList.getCmpdListMembers().size());
      System.out.println("b: " + bList.getListName() + " " + bList.getCmpdListMembers().size());
      System.out.println("----------------------------");
      System.out.println("nsc in a: " + nscSetA.size());
      System.out.println("ahc in a: " + ahcIdSetA.size());
      System.out.println("----------------------------");
      System.out.println("nsc in b: " + nscSetB.size());
      System.out.println("ahc in b: " + ahcIdSetB.size());
      System.out.println("----------------------------");
    }

        // union
    HashSet<Integer> tempNscA = new HashSet<Integer>(nscSetA);
    HashSet<Long> tempAhcA = new HashSet<Long>(ahcIdSetA);

    if (DEBUG) {
      System.out.println("nsc in a BEFORE addAll: " + tempNscA.size());
      System.out.println("ahc in a BEFORE addAll: " + tempAhcA.size());
      System.out.println("----------------------------");
    }

    tempNscA.addAll(nscSetB);
    tempAhcA.addAll(ahcIdSetB);

    if (DEBUG) {
      System.out.println("nsc in a AFTER addAll: " + tempNscA.size());
      System.out.println("ahc in a AFTER addAll: " + tempAhcA.size());
      System.out.println("----------------------------");
    }

    ArrayList<CmpdVO> AorB = new ArrayList<CmpdVO>();
    for (Integer i : tempNscA) {
      AorB.add(nscMap.get(i));
    }
    for (Long l : tempAhcA) {
      AorB.add(ahcIdMap.get(l));
    }

        // intersection via retainAll by nsc and then retainAll by adHocCmpdId
    tempNscA = new HashSet<Integer>(nscSetA);
    tempAhcA = new HashSet<Long>(ahcIdSetA);

    if (DEBUG) {
      System.out.println("nsc in a BEFORE retainAll: " + tempNscA.size());
      System.out.println("ahc in a BEFORE retainAll: " + tempAhcA.size());
      System.out.println("----------------------------");
    }

    tempNscA.retainAll(nscSetB);
    tempAhcA.retainAll(ahcIdSetB);

    if (DEBUG) {
      System.out.println("nsc in a AFTER retainAll: in a and b by nsc " + tempNscA.size());
      System.out.println("ahc in a AFTER retainAll: in a and b by ahc " + tempAhcA.size());
      System.out.println("----------------------------");
    }

    ArrayList<CmpdVO> AandB = new ArrayList<CmpdVO>();
    for (Integer i : tempNscA) {
      AandB.add(nscMap.get(i));
    }
    for (Long l : tempAhcA) {
      AandB.add(ahcIdMap.get(l));
    }

        // AnotB via removeAll of B from A
    tempNscA = new HashSet<Integer>(nscSetA);
    tempAhcA = new HashSet<Long>(ahcIdSetA);

    if (DEBUG) {
      System.out.println("nsc in a BEFORE removeAll: " + tempNscA.size());
      System.out.println("ahc in a BEFORE removeAll: " + tempAhcA.size());
      System.out.println("----------------------------");
    }

    tempNscA.removeAll(nscSetB);
    tempAhcA.removeAll(ahcIdSetB);

    if (DEBUG) {
      System.out.println("nsc in a AFTER removeAll: " + tempNscA.size());
      System.out.println("nsc in a AFTER removeAll: " + tempAhcA.size());
      System.out.println("----------------------------");
    }

    ArrayList<CmpdVO> AnotB = new ArrayList<CmpdVO>();
    for (Integer i : tempNscA) {
      AnotB.add(nscMap.get(i));
    }
    for (Long l : tempAhcA) {
      AnotB.add(ahcIdMap.get(l));
    }

    if (DEBUG) {
      System.out.println("AorB: " + AorB.size());
      System.out.println("AandB: " + AandB.size());
      System.out.println("AnotB: " + AnotB.size());

      System.out.println("----------------------------");
      System.out.println("AorB");
      for (CmpdVO cVO : AorB) {
        System.out.println(cVO.getNsc() + " " + cVO.getOriginalAdHocCmpdId());
      }

      System.out.println("----------------------------");
      System.out.println("AandB");
      for (CmpdVO cVO : AandB) {
        System.out.println(cVO.getNsc() + " " + cVO.getOriginalAdHocCmpdId());
      }

      System.out.println("----------------------------");
      System.out.println("AnotB");
      for (CmpdVO cVO : AnotB) {
        System.out.println(cVO.getNsc() + " " + cVO.getOriginalAdHocCmpdId());
      }
    }

    this.cmpdsListAorListB = AorB;
    this.cmpdsListAandListB = AandB;
    this.cmpdsListAnotListB = AnotB;

    return "/webpages/listLogic?faces-redirect=true";

  }

// <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
  public CmpdListVO getListA() {
    return listA;
  }

  public void setListA(CmpdListVO listA) {
    this.listA = listA;
  }

  public CmpdListVO getListB() {
    return listB;
  }

  public void setListB(CmpdListVO listB) {
    this.listB = listB;
  }

  public List<CmpdVO> getCmpdsListAnotListB() {
    return cmpdsListAnotListB;
  }

  public void setCmpdsListAnotListB(List<CmpdVO> cmpdsListAnotListB) {
    this.cmpdsListAnotListB = cmpdsListAnotListB;
  }

  public List<CmpdVO> getCmpdsListAandListB() {
    return cmpdsListAandListB;
  }

  public void setCmpdsListAandListB(List<CmpdVO> cmpdsListAandListB) {
    this.cmpdsListAandListB = cmpdsListAandListB;
  }

  public List<CmpdVO> getCmpdsListAorListB() {
    return cmpdsListAorListB;
  }

  public void setCmpdsListAorListB(List<CmpdVO> cmpdsListAorListB) {
    this.cmpdsListAorListB = cmpdsListAorListB;
  }

  public List<CmpdVO> getCurrentListOfCompounds() {
    return currentListOfCompounds;
  }

  public void setCurrentListOfCompounds(List<CmpdVO> currentListOfCompounds) {
    this.currentListOfCompounds = currentListOfCompounds;
  }

  public List<CmpdVO> getSelectedCmpds() {
    return selectedCmpds;
  }

  public void setSelectedCmpds(List<CmpdVO> selectedCmpds) {
    this.selectedCmpds = selectedCmpds;
  }

  public CmpdVO getSelectedCmpd() {
    return selectedCmpd;
  }

  public void setSelectedCmpd(CmpdVO selectedCmpd) {
    this.selectedCmpd = selectedCmpd;
  }
    // </editor-fold>
}