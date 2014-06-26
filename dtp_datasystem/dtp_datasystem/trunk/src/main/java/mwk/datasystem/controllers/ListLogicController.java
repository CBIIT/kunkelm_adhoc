/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.type.StandardBasicTypes;

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

    public String performListLogic_Java() {

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

    public String performListLogic() {

        ArrayList<Integer> AnotBintList = doSingleQuery("except", this.listAid, this.listBid);
        ArrayList<Integer> AandBintList = doSingleQuery("intersect", this.listAid, this.listBid);
        ArrayList<Integer> AorBintList = doSingleQuery("union", this.listAid, this.listBid);

        HelperCmpd helper = new HelperCmpd();

        this.cmpdsListAnotListB = helper.getCmpdsByNsc(AnotBintList, this.sessionController.getLoggedUser());
        this.cmpdsListAorListB = helper.getCmpdsByNsc(AorBintList, this.sessionController.getLoggedUser());
        this.cmpdsListAandListB = helper.getCmpdsByNsc(AandBintList, this.sessionController.getLoggedUser());

        return "/webpages/listLogic?faces-redirect=true";

    }

    private ArrayList<Integer> doSingleQuery(
            String keyword,
            Long aId,
            Long bId) {

        ArrayList<Integer> nscIntList = new ArrayList<Integer>();

        String templatedQuery = " select n.nsc "
                + " from cmpd_list cl, cmpd_list_member clm, nsc_cmpd n "
                + " where cl.cmpd_list_id = :aId "
                + " and clm.cmpd_list_fk = cl.id "
                + " and clm.cmpd_fk = n.id "
                + " " + keyword + " "
                + " select n.nsc "
                + " from cmpd_list cl, cmpd_list_member clm, nsc_cmpd n "
                + " where cl.cmpd_list_id = :bId "
                + " and clm.cmpd_list_fk = cl.id "
                + " and clm.cmpd_fk = n.id ";


        Session s = null;
        Transaction t = null;

        try {

            s = HibernateUtil.getSessionFactory().openSession();

            t = s.beginTransaction();

            Query q = s.createSQLQuery(templatedQuery);
            q.setParameter("aId", aId);
            q.setParameter("bId", bId);

            List results = q.list();

            int i = 0;
            for (Iterator itr = results.iterator(); itr.hasNext();) {
                Integer nsc = (Integer) itr.next();
                nscIntList.add(nsc);
                // System.out.println("i: " + ++i + " nsc: " + nsc);
            }

            t.commit();
            s.close();

        } catch (Exception e) {
            t.rollback();
            s.close();
            e.printStackTrace();
        }

        return nscIntList;

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
