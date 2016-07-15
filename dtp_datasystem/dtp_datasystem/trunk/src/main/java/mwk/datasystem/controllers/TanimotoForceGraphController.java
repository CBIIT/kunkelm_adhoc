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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import mwk.d3.Info;
import mwk.d3.Node;
import mwk.d3.TanimotoForceGraph;
import mwk.d3.TanimotoLink;
import mwk.datasystem.util.Comparators;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperTanimotoScores;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.TanimotoScoresVO;
import mwk.datasystem.vo.TanimotoScoresWithCmpdObjectsVO;

/**
 *
 * @author mcwkunkel
 */
@ManagedBean
@SessionScoped
public class TanimotoForceGraphController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    public static final Boolean DEBUG = Boolean.TRUE;

    private String json;

    private Double minTan;

    private ArrayList<String> fingerprintList;
    private String selectedFingerprint;

    private ArrayList<Integer> nscList;
    private ArrayList<String> drugNameList;
    private ArrayList<String> targetList;

    @PostConstruct
    public void init() {

        HashSet<Integer> nscSet = new HashSet<Integer>();
        HashSet<String> drugNameSet = new HashSet<String>();
        HashSet<String> targetSet = new HashSet<String>();

        ArrayList<TanimotoScoresVO> scores = HelperTanimotoScores.fetch();

        for (TanimotoScoresVO tsVO : scores) {
            nscSet.add(tsVO.getNsc1());
            nscSet.add(tsVO.getNsc2());
        }

        nscList = new ArrayList<Integer>(nscSet);
        Collections.sort(nscList);

        List<CmpdVO> cmpdVOlist = HelperCmpd.getCmpdsByNsc(nscList, "PUBLIC");
        HashMap<Integer, CmpdVO> cmpdLookup = new HashMap<Integer, CmpdVO>();
        for (CmpdVO cVO : cmpdVOlist) {
            cmpdLookup.put(cVO.getNsc(), cVO);
        }

        ArrayList<TanimotoScoresWithCmpdObjectsVO> scoresList = new ArrayList<TanimotoScoresWithCmpdObjectsVO>();

        for (TanimotoScoresVO tsVO : scores) {

            TanimotoScoresWithCmpdObjectsVO tswcoVO = new TanimotoScoresWithCmpdObjectsVO();

            CmpdVO c1 = cmpdLookup.get(tsVO.getNsc1());
            CmpdVO c2 = cmpdLookup.get(tsVO.getNsc2());

            drugNameSet.add(c1.getName());
            drugNameSet.add(c2.getName());

            // TODO: this is kludgey while the datasystem model doesn't include drugName, primaryTarget
            if (c1.getCmpdAnnotation() != null && c1.getCmpdAnnotation().getGeneralComment() != null) {
                targetSet.add(c1.getCmpdAnnotation().getGeneralComment());
            }
            if (c2.getCmpdAnnotation() != null && c2.getCmpdAnnotation().getGeneralComment() != null) {
                targetSet.add(c2.getCmpdAnnotation().getGeneralComment());
            }

            tswcoVO.setCmpd1(cmpdLookup.get(tsVO.getNsc1()));
            tswcoVO.setCmpd2(cmpdLookup.get(tsVO.getNsc2()));

            tswcoVO.setAtomPair(tsVO.getAtomPair());
            tswcoVO.setFeatMorgan(tsVO.getFeatMorgan());
            tswcoVO.setLayered(tsVO.getLayered());
            tswcoVO.setMacss(tsVO.getMacss());
            tswcoVO.setMorganBv(tsVO.getMorganBv());
            tswcoVO.setRdkit(tsVO.getRdkit());
            tswcoVO.setTorsionBv(tsVO.getTorsionBv());

            scoresList.add(tswcoVO);

        }

        drugNameList = new ArrayList<String>(drugNameSet);
        Collections.sort(drugNameList);

        targetList = new ArrayList<String>(targetSet);
        Collections.sort(targetList);

        //json = generateJson(scores);
        json = generateJsonNew(scoresList);

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

    }

    public String reload() {
        init();
        return "/webpages/d3/tanimotoForceGraph?faces-redirect=true";
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

    public static String generateJsonNew(ArrayList<TanimotoScoresWithCmpdObjectsVO> scores) {

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
        Collections.sort(cmpdList, new Comparators.CmpdNscComparator());

        for (CmpdVO nscInt : cmpdList) {
            Node n = new Node();
            n.nsc = nscInt.getNsc();
            n.drugName = nscInt.getName();
            n.smiles = nscInt.getParentFragment().getCmpdFragmentStructure().getCanSmi();
            n.target = nscInt.getCmpdAnnotation().getGeneralComment();
            nodeList.add(n);
        }

        for (TanimotoScoresWithCmpdObjectsVO tswco : scores) {

            TanimotoLink l = new TanimotoLink();

            l.source = cmpdList.indexOf(tswco.getCmpd1());
            l.target = cmpdList.indexOf(tswco.getCmpd2());

            l.ap = round(tswco.getAtomPair(), 3);
            l.fm = round(tswco.getFeatMorgan(), 3);
            l.l = round(tswco.getLayered(), 3);
            l.mc = round(tswco.getMacss(), 3);
            l.m = round(tswco.getMorganBv(), 3);
            l.r = round(tswco.getRdkit(), 3);
            l.to = round(tswco.getTorsionBv(), 3);

            linkList.add(l);

            // System.out.println(l.source + " " + l.target + " " + l.ap + " " + l.fm + " " + l.l + " " + l.mc + " " + l.m + " " + l.r + " " + l.to);
        }

        fg.nodes = nodeList;
        fg.links = linkList;

        Gson gson = new Gson();
        rtn = gson.toJson(fg);

        return rtn;
    }

    public static class LightWeightInfo {

        Integer nsc;
        String drugName;
        String target;
        String smiles;

        public LightWeightInfo(Integer nsc, String drugName, String target, String smiles) {
            this.nsc = nsc;
            this.drugName = drugName;
            this.target = target;
            this.smiles = smiles;
        }

    }

    public static String generateJson(ArrayList<TanimotoScoresVO> scores) {

        NumberFormat nf2 = new DecimalFormat();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);

        String rtn = "";

        TanimotoForceGraph fg = new TanimotoForceGraph();

        Info i = new Info();
        i.fingerprintType = "collated fingerprints";
        fg.info = i;

        ArrayList<Node> nodeList = new ArrayList<Node>();
        List<TanimotoLink> linkList = new ArrayList<TanimotoLink>();

        HashSet<Integer> nscSet = new HashSet<Integer>();
        for (TanimotoScoresVO tsVO : scores) {
            nscSet.add(tsVO.getNsc1());
            nscSet.add(tsVO.getNsc2());
        }

        HashSet<LightWeightInfo> lwiList = new HashSet<LightWeightInfo>();
        for (TanimotoScoresVO tsVO : scores) {
            nscSet.add(tsVO.getNsc1());
            nscSet.add(tsVO.getNsc2());
        }

        // add nscSet members as nodes, and then do the lookups        
        ArrayList<Integer> nscList = new ArrayList<Integer>(nscSet);
        Collections.sort(nscList);

        for (Integer nscInt : nscList) {
            Node n = new Node();
            n.nsc = nscInt;
            nodeList.add(n);
        }

        for (TanimotoScoresVO tsVO : scores) {

            TanimotoLink l = new TanimotoLink();

            l.source = nscList.indexOf(tsVO.getNsc1());
            l.target = nscList.indexOf(tsVO.getNsc2());

            l.ap = round(tsVO.getAtomPair(), 3);
            l.fm = round(tsVO.getFeatMorgan(), 3);
            l.l = round(tsVO.getLayered(), 3);
            l.mc = round(tsVO.getMacss(), 3);
            l.m = round(tsVO.getMorganBv(), 3);
            l.r = round(tsVO.getRdkit(), 3);
            l.to = round(tsVO.getTorsionBv(), 3);

            linkList.add(l);

            // System.out.println(l.source + " " + l.target + " " + l.ap + " " + l.fm + " " + l.l + " " + l.mc + " " + l.m + " " + l.r + " " + l.to);
        }

        fg.nodes = nodeList;
        fg.links = linkList;

        Gson gson = new Gson();
        rtn = gson.toJson(fg);

        return rtn;
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
