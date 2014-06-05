/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class ListLogicController implements Serializable {

    private Long listAid;
    private Long listBid;
    //  
    private List<CmpdVO> cmpdsListAnotListB;
    private List<CmpdVO> cmpdsListAandListB;
    private List<CmpdVO> cmpdsListAorListB;
    //
    private List<CmpdVO> currentListOfCompounds;
    //
    // reach-through to listManagerController
    @ManagedProperty(value = "#{listManagerController}")
    private ListManagerController listManagerController;
    // reach-through to sessionController
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    public void setListManagerController(ListManagerController listManagerController) {
        this.listManagerController = listManagerController;
    }
    
    public String performListLogic() {
        
        // VASTLY SMARTER
        // leverage listManagerController to only fetch list members once
        
        CmpdListVO a = this.listManagerController.performLoadList(this.listAid);
        CmpdListVO b = this.listManagerController.performLoadList(listBid);
        
        ArrayList<CmpdVO> cmpdCollA = new ArrayList<CmpdVO>();
        for (CmpdListMemberVO clm : a.getCmpdListMembers()) {
            cmpdCollA.add(clm.getCmpd());
        }

        System.out.println("Size of cmpdCollA: " + cmpdCollA.size());

        ArrayList<CmpdVO> cmpdCollB = new ArrayList<CmpdVO>();
        for (CmpdListMemberVO clm : b.getCmpdListMembers()) {
            cmpdCollB.add(clm.getCmpd());
        }

        System.out.println("Size of cmpdCollB: " + cmpdCollB.size());

        // union via HashSet
        HashSet<CmpdVO> unionSet = new HashSet<CmpdVO>();
        unionSet.addAll(cmpdCollA);
        unionSet.addAll(cmpdCollB);
        ArrayList<CmpdVO> unionList = new ArrayList<CmpdVO>();
        unionList.addAll(unionSet);
        this.cmpdsListAorListB = unionList;

        ArrayList<CmpdVO> tempListA = new ArrayList<CmpdVO>(cmpdCollA);
        tempListA.retainAll(cmpdCollB);
        System.out.println("Size of tempListA after retainAll: " + tempListA.size());
        this.cmpdsListAandListB = new ArrayList<CmpdVO>(tempListA);

        tempListA = new ArrayList<CmpdVO>(cmpdCollA);
        tempListA.removeAll(cmpdCollB);
        System.out.println("Size of tempListA after removeAll: " + tempListA.size());
        this.cmpdsListAnotListB = new ArrayList<CmpdVO>(tempListA);

        return "/webpages/listLogic?faces-redirect=true";

    }
    
    public String performListLogic_ORIGINAL() {
        
        HelperCmpdList helperA = new HelperCmpdList();
        CmpdListVO a = helperA.getCmpdListByCmpdListId(this.listAid, Boolean.TRUE, this.sessionController.getLoggedUser());

        HelperCmpdList helperB = new HelperCmpdList();
        CmpdListVO b = helperB.getCmpdListByCmpdListId(this.listBid, Boolean.TRUE, this.sessionController.getLoggedUser());

        ArrayList<CmpdVO> cmpdCollA = new ArrayList<CmpdVO>();
        for (CmpdListMemberVO clm : a.getCmpdListMembers()) {
            cmpdCollA.add(clm.getCmpd());
        }

        System.out.println("Size of cmpdCollA: " + cmpdCollA.size());

        ArrayList<CmpdVO> cmpdCollB = new ArrayList<CmpdVO>();
        for (CmpdListMemberVO clm : b.getCmpdListMembers()) {
            cmpdCollB.add(clm.getCmpd());
        }

        System.out.println("Size of cmpdCollB: " + cmpdCollB.size());

        // union via HashSet
        HashSet<CmpdVO> unionSet = new HashSet<CmpdVO>();
        unionSet.addAll(cmpdCollA);
        unionSet.addAll(cmpdCollB);
        ArrayList<CmpdVO> unionList = new ArrayList<CmpdVO>();
        unionList.addAll(unionSet);
        this.cmpdsListAorListB = unionList;

        ArrayList<CmpdVO> tempListA = new ArrayList<CmpdVO>(cmpdCollA);
        tempListA.retainAll(cmpdCollB);
        System.out.println("Size of tempListA after retainAll: " + tempListA.size());
        this.cmpdsListAandListB = new ArrayList<CmpdVO>(tempListA);

        tempListA = new ArrayList<CmpdVO>(cmpdCollA);
        tempListA.removeAll(cmpdCollB);
        System.out.println("Size of tempListA after removeAll: " + tempListA.size());
        this.cmpdsListAnotListB = new ArrayList<CmpdVO>(tempListA);

        return "/webpages/listLogic?faces-redirect=true";

    }
    
    // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">

    public Long getListAid() {
        return listAid;
    }

    public void setListAid(Long listAid) {
        this.listAid = listAid;
    }

    public Long getListBid() {
        return listBid;
    }

    public void setListBid(Long listBid) {
        this.listBid = listBid;
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

    // </editor-fold>
}
