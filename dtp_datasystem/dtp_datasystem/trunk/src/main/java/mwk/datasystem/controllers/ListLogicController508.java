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
import javax.faces.bean.SessionScoped;
import javax.faces.model.SelectItem;
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
public class ListLogicController508 implements Serializable {

    private Long listAid;
    private Long listBid;
    //  
    private List<CmpdVO> cmpdsListAnotListB;
    private List<CmpdVO> cmpdsListAandListB;
    private List<CmpdVO> cmpdsListAorListB;
    //
    private List<CmpdVO> currentListOfCompounds;
 
    public String performListLogic() {

        HelperCmpdList helperA = new HelperCmpdList();
        CmpdListVO a = helperA.getCmpdListByCmpdListId(this.listAid, Boolean.TRUE);

        HelperCmpdList helperB = new HelperCmpdList();
        CmpdListVO b = helperB.getCmpdListByCmpdListId(this.listBid, Boolean.TRUE);

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

        return "/pages508/listLogic?faces-redirect=true";

    }

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

}
