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
import java.util.HashSet;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import mwk.d3.Info;
import mwk.d3.Node;
import mwk.d3.TanimotoForceGraph;
import mwk.d3.TanimotoLink;
import mwk.datasystem.util.HelperTanimotoScores;
import static mwk.datasystem.util.HelperTanimotoScores.fetch;
import mwk.datasystem.vo.TanimotoScoresVO;

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
    private ArrayList<Integer> nscList;
    private ArrayList<String> drugNameList;
    private ArrayList<String> targetList;

    @PostConstruct
    public void init() {

        HashSet<Integer> nscSet = new HashSet<Integer>();
        
        // fetch the data
        ArrayList<TanimotoScoresVO> scores = HelperTanimotoScores.fetch();
        
        for (TanimotoScoresVO tsVO : scores){
            nscSet.add(tsVO.getNsc1());
            nscSet.add(tsVO.getNsc2());
        }
        
        this.nscList = new ArrayList<Integer>(nscSet);
        Collections.sort(this.nscList);
        
        this.json = generateJson(scores);

        this.minTan = 0.5d;

        String[] fpArr = new String[]{"atompairbv_fp",
            "featmorganbv_fp",
            "layered_fp",
            "maccs_fp",
            "morganbv_fp",
            "rdkit_fp",
            "torsionbv_fp"};
        
        this.fingerprintList = new ArrayList<String>(Arrays.asList(fpArr));
        
        this.drugNameList = new ArrayList<String>();
        this.targetList = new ArrayList<String>();
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

//            l.ap = tsVO.getAtomPair();
//            l.fm = tsVO.getFeatMorgan();
//            l.l = tsVO.getLayered();
//            l.mc = tsVO.getMacss();
//            l.m = tsVO.getMorganBv();
//            l.r = tsVO.getRdkit();
//            l.to = tsVO.getTorsionBv();
            
            l.ap = round(tsVO.getAtomPair(), 2);
            l.fm = round(tsVO.getFeatMorgan(), 2);
            l.l = round(tsVO.getLayered(), 2);
            l.mc = round(tsVO.getMacss(), 2);
            l.m = round(tsVO.getMorganBv(), 2);
            l.r = round(tsVO.getRdkit(), 2);
            l.to = round(tsVO.getTorsionBv(), 2);

            linkList.add(l);

            // System.out.println(l.source + " " + l.target + " " + l.ap + " " + l.fm + " " + l.l + " " + l.mc + " " + l.m + " " + l.r + " " + l.to);
        }

        fg.nodes = nodeList;
        fg.links = linkList;

        Gson gson = new Gson();
        rtn = gson.toJson(fg);

        return rtn;
    }

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

}
