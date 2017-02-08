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

public class ListLogicStatic {

    static final long serialVersionUID = -8653468638698142855l;

    private static final Boolean DEBUG = Boolean.TRUE;

    // 
    public static void performListLogic(ListLogicContainer llc) throws Exception {

        List<CmpdVO> cmpdListA = new ArrayList<CmpdVO>();
        List<CmpdVO> cmpdListB = new ArrayList<CmpdVO>();

        // lists not null
        if (llc.getListA() == null) {
            if (llc.getListB() == null) {
                throw new Exception("listA and listB are null");
            }
            throw new Exception("listA is null");
        } else if (llc.getListB() == null) {
            throw new Exception("listB is null");
        }

        // lists have listMembers
        if (llc.getListA().getCmpdListMembers() == null) {
            if (llc.getListB().getCmpdListMembers() == null) {
                throw new Exception("listA and listB have no listMembers.");
            }
            throw new Exception("listA has no listMembers.");
        } else if (llc.getListB().getCmpdListMembers() == null) {
            throw new Exception("listB has no listMembers.");
        }

        for (CmpdListMemberVO clmVO : llc.getListA().getCmpdListMembers()) {
            cmpdListA.add(clmVO.getCmpd());
        }

        for (CmpdListMemberVO clmVO : llc.getListB().getCmpdListMembers()) {
            cmpdListB.add(clmVO.getCmpd());
        }

        // overlap is based on a.nsc = b.nsc OR a.originalAdHocCmpdId = b.originalAdHocCmpdId
        HashMap<Integer, CmpdVO> nscMap = new HashMap<Integer, CmpdVO>();
        HashMap<Long, CmpdVO> ahcIdMap = new HashMap<Long, CmpdVO>();

        HashSet<Integer> nscSetA = new HashSet<Integer>();
        HashSet<Integer> nscSetB = new HashSet<Integer>();

        HashSet<Long> ahcIdSetA = new HashSet<Long>();
        HashSet<Long> ahcIdSetB = new HashSet<Long>();

        for (CmpdVO cVO : cmpdListA) {
            if (cVO.getNsc() != null) {
                nscMap.put(cVO.getNsc(), cVO);
                nscSetA.add(cVO.getNsc());
            } else {
                ahcIdMap.put(cVO.getOriginalAdHocCmpdId(), cVO);
                ahcIdSetA.add(cVO.getOriginalAdHocCmpdId());
            }
        }

        for (CmpdVO cVO : cmpdListB) {
            if (cVO.getNsc() != null) {
                nscMap.put(cVO.getNsc(), cVO);
                nscSetB.add(cVO.getNsc());
            } else {
                ahcIdMap.put(cVO.getOriginalAdHocCmpdId(), cVO);
                ahcIdSetB.add(cVO.getOriginalAdHocCmpdId());
            }
        }

        if (DEBUG) {
            System.out.println("a: " + llc.getListA().getListName() + " " + llc.getListA().getCmpdListMembers().size());
            System.out.println("b: " + llc.getListB().getListName() + " " + llc.getListB().getCmpdListMembers().size());
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

//            System.out.println("----------------------------");
//            System.out.println("AorB");
//            for (CmpdVO cVO : AorB) {
//                System.out.println(cVO.getNsc() + " " + cVO.getOriginalAdHocCmpdId());
//            }
//
//            System.out.println("----------------------------");
//            System.out.println("AandB");
//            for (CmpdVO cVO : AandB) {
//                System.out.println(cVO.getNsc() + " " + cVO.getOriginalAdHocCmpdId());
//            }
//
//            System.out.println("----------------------------");
//            System.out.println("AnotB");
//            for (CmpdVO cVO : AnotB) {
//                System.out.println(cVO.getNsc() + " " + cVO.getOriginalAdHocCmpdId());
//            }
        }

        llc.setCmpdsListAorListB(AorB);
        llc.setCmpdsListAandListB(AandB);
        llc.setCmpdsListAnotListB(AnotB);

    }

}
