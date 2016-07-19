/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import com.google.gson.Gson;
import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.TreeSet;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import mwk.d3.Info;
import mwk.d3.Node;
import mwk.d3.TanimotoForceGraph;
import mwk.d3.TanimotoLink;
import mwk.datasystem.util.Comparators;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperTanimoto;
import mwk.datasystem.util.TemplatedGrid;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.TanimotoScoresVO;
import mwk.datasystem.vo.TanimotoScoresWithCmpdObjectsVO;

/**
 *
 * @author mcwkunkel
 */
@ManagedBean
@SessionScoped
public class TanimotoController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    public static final Boolean DEBUG = Boolean.TRUE;

    private String json;

    private Double minTan;

    private ArrayList<String> fingerprintList;
    private String selectedFingerprint;

    private ArrayList<Integer> nscList;
    private ArrayList<String> drugNameList;
    private ArrayList<String> targetList;

    // reach-through to listManagerController
    @ManagedProperty(value = "#{listManagerController}")
    private ListManagerController listManagerController;

    public void setListManagerController(ListManagerController listManagerController) {
        this.listManagerController = listManagerController;
    }

    @PostConstruct
    public void init() {
    }

    public String reload() {
        init();
        return "/webpages/tanimotoForce?faces-redirect=true";
    }

    public static double round(Double value, int places) {

        if (places < 0) {
            throw new IllegalArgumentException();
        }

        BigDecimal bd = null;

        if (value == null) {
            value = 0d;
        }

        try {
            bd = new BigDecimal(value);
        } catch (NumberFormatException nfe) {
            bd = new BigDecimal(0d);
        }

        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();

    }

    public static String generateJson(ArrayList<TanimotoScoresWithCmpdObjectsVO> scores) {

        String rtn = "";

        TanimotoForceGraph fg = new TanimotoForceGraph();

        Info i = new Info();
        i.fingerprintType = "collated fingerprints";
        fg.info = i;

        ArrayList<Node> nodeList = new ArrayList<Node>();
        List<TanimotoLink> linkList = new ArrayList<TanimotoLink>();

        HashSet<CmpdVO> cmpdSet = new HashSet<CmpdVO>();

        for (TanimotoScoresWithCmpdObjectsVO tsVO : scores) {
            cmpdSet.add(tsVO.getCmpd1());
            cmpdSet.add(tsVO.getCmpd2());
        }

        // add nscSet members as nodes, and then do the lookups        
        ArrayList<CmpdVO> cmpdList = new ArrayList<CmpdVO>(cmpdSet);
        Collections.sort(cmpdList, new Comparators.CmpdComparator());

        for (CmpdVO nscInt : cmpdList) {
            Node n = new Node();
            n.nsc = nscInt.getNsc();
            n.drugName = nscInt.getName();
            n.smiles = nscInt.getParentFragment().getCmpdFragmentStructure().getCanSmi();

            StringBuilder targetSb = new StringBuilder();
            for (String t : nscInt.getTargets()) {
                targetSb.append(t);
                targetSb.append(" ");
            }
            if (targetSb.length() > 0) {
                //if anything was appended, remove the trailing space
                targetSb.deleteCharAt(targetSb.length() - 1);
                n.target = targetSb.toString();
            }

            nodeList.add(n);
        }

        for (TanimotoScoresWithCmpdObjectsVO tswco : scores) {

            TanimotoLink l = new TanimotoLink();

            l.source = cmpdList.indexOf(tswco.getCmpd1());
            l.target = cmpdList.indexOf(tswco.getCmpd2());

            l.ap = round(tswco.getAtomPairBv(), 3);
            l.fm = round(tswco.getFeatMorganBv(), 3);
            l.l = round(tswco.getLayered(), 3);
            l.mc = round(tswco.getMaccs(), 3);
            l.m = round(tswco.getMorganBv(), 3);
            l.r = round(tswco.getRdkit(), 3);
            l.to = round(tswco.getTorsionBv(), 3);

            l.color = ColorWranglingController.colorByCorrelation(l.mc.toString());

            linkList.add(l);

            // System.out.println(l.source + " " + l.target + " " + l.ap + " " + l.fm + " " + l.l + " " + l.mc + " " + l.m + " " + l.r + " " + l.to);
        }

        fg.nodes = nodeList;
        fg.links = linkList;

        Gson gson = new Gson();
        rtn = gson.toJson(fg);

        return rtn;
    }

    public String performRunTanimoto() {

        TreeSet<Integer> nscSet = new TreeSet<Integer>();
        TreeSet<String> drugNameSet = new TreeSet<String>();
        TreeSet<String> targetSet = new TreeSet<String>();

        List<CmpdListMemberVO> clmList = listManagerController.getListManagerBean().getSelectedActiveListMembers();

        ArrayList<CmpdVO> cVOlist = new ArrayList<CmpdVO>();
        for (CmpdListMemberVO clmVO : clmList) {

            CmpdVO cVO = clmVO.getCmpd();
            cVOlist.add(cVO);

            if (cVO.getNsc() != null) {
                nscSet.add(cVO.getNsc());
            }
            if (cVO.getName() != null) {
                drugNameSet.add(cVO.getName());
            }
            if (cVO.getTargets() != null) {
                for (String target : cVO.getTargets()) {
                    targetSet.add(target);
                }
            }

        }

        nscList = new ArrayList<Integer>(nscSet);
        Collections.sort(nscList);

        drugNameList = new ArrayList<String>(drugNameSet);
        Collections.sort(drugNameList);

        targetList = new ArrayList<String>(targetSet);
        Collections.sort(targetList);

        ArrayList<TanimotoScoresWithCmpdObjectsVO> tsList = HelperTanimoto.doTanimoto(cVOlist);

        System.out.println("Size of tsList: " + tsList.size());

        json = generateJson(tsList);

        minTan = 0.9d;

        String[] fpArr = new String[]{
            "atompairbv_fp",
            "featmorganbv_fp",
            "layered_fp",
            "maccs_fp",
            "morganbv_fp",
            "rdkit_fp",
            "torsionbv_fp"};

        fingerprintList = new ArrayList<String>(Arrays.asList(fpArr));
        selectedFingerprint = "mc";

        return "/webpages/tanimotoForce?faces-redirect=true";

    }

    //<editor-fold defaultstate="collapsed" desc="GETTERS/SETTERS">
    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Double getMinTan() {
        return minTan;
    }

    public void setMinTan(Double minTan) {
        this.minTan = minTan;
    }

    public ArrayList<String> getFingerprintList() {
        return fingerprintList;
    }

    public void setFingerprintList(ArrayList<String> fingerprintList) {
        this.fingerprintList = fingerprintList;
    }

    public String getSelectedFingerprint() {
        return selectedFingerprint;
    }

    public void setSelectedFingerprint(String selectedFingerprint) {
        this.selectedFingerprint = selectedFingerprint;
    }

    public ArrayList<Integer> getNscList() {
        return nscList;
    }

    public void setNscList(ArrayList<Integer> nscList) {
        this.nscList = nscList;
    }

    public ArrayList<String> getDrugNameList() {
        return drugNameList;
    }

    public void setDrugNameList(ArrayList<String> drugNameList) {
        this.drugNameList = drugNameList;
    }

    public ArrayList<String> getTargetList() {
        return targetList;
    }

    public void setTargetList(ArrayList<String> targetList) {
        this.targetList = targetList;
    }

//</editor-fold>
}
