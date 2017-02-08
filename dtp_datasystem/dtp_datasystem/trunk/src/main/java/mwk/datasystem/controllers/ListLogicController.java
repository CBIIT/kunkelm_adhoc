/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import mwk.datasystem.domain.CmpdTable;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.TransformCmpdTableToVO;
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

    private static final Boolean DEBUG = Boolean.TRUE;
    // 
    private List<CmpdListVO> pickableLists;
    //
    private CmpdListVO listA;
    private CmpdListVO listB;
    //
    private CmpdListVO cmpdsListAnotListB;
    private CmpdListVO cmpdsListAandListB;
    private CmpdListVO cmpdsListAorListB;

    // reach-through to sessionController
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }

    // reach-through to sessionController
    @ManagedProperty(value = "#{listManagerController}")
    private ListManagerController listManagerController;

    public void setListManagerController(ListManagerController listManagerController) {
        this.listManagerController = listManagerController;
    }

    public String performListLogic() {

        CmpdListVO aList = null;
        CmpdListVO bList = null;

        if (this.listA != null) {
            if (this.listA.getCmpdListMembers() == null || this.listA.getCmpdListMembers().isEmpty()) {
                if (this.listA.getCmpdListId() != null && this.listA.getCmpdListId() > 0) {
                    aList = listManagerController.fetchList(this.listA.getCmpdListId());
                }
            }
        }

        if (this.listB != null) {
            if (this.listB.getCmpdListMembers() == null || this.listB.getCmpdListMembers().isEmpty()) {
                if (this.listB.getCmpdListId() != null && this.listB.getCmpdListId() > 0) {
                    bList = listManagerController.fetchList(this.listB.getCmpdListId());
                }
            }
        }

        ListLogicContainer cont = new ListLogicContainer(aList, bList);

        try{
        
        ListLogicStatic.performListLogic(cont);

        this.cmpdsListAorListB = ApplicationScopeBean.cmpdListFromListOfCmpds(cont.getCmpdsListAorListB(), "AorB", sessionController.getLoggedUser());
        this.cmpdsListAandListB = ApplicationScopeBean.cmpdListFromListOfCmpds(cont.getCmpdsListAandListB(), "AandB", sessionController.getLoggedUser());
        this.cmpdsListAnotListB = ApplicationScopeBean.cmpdListFromListOfCmpds(cont.getCmpdsListAnotListB(), "AnotB", sessionController.getLoggedUser());
        
        } catch (Exception e){
            e.printStackTrace();
        }

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

    public CmpdListVO getCmpdsListAnotListB() {
        return cmpdsListAnotListB;
    }

    public void setCmpdsListAnotListB(CmpdListVO cmpdsListAnotListB) {
        this.cmpdsListAnotListB = cmpdsListAnotListB;
    }

    public CmpdListVO getCmpdsListAandListB() {
        return cmpdsListAandListB;
    }

    public void setCmpdsListAandListB(CmpdListVO cmpdsListAandListB) {
        this.cmpdsListAandListB = cmpdsListAandListB;
    }

    public CmpdListVO getCmpdsListAorListB() {
        return cmpdsListAorListB;
    }

    public void setCmpdsListAorListB(CmpdListVO cmpdsListAorListB) {
        this.cmpdsListAorListB = cmpdsListAorListB;
    }

    // </editor-fold>
}
