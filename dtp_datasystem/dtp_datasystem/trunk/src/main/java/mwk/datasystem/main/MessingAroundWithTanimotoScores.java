/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import com.google.gson.Gson;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import mwk.d3.ForceGraph;
import mwk.d3.Info;
import mwk.d3.Link;
import mwk.d3.Node;
import mwk.d3.TanimotoForceGraph;
import mwk.d3.TanimotoLink;
import org.apache.commons.lang.math.NumberUtils;

/**
 *
 * @author mwkunkel
 */
public class MessingAroundWithTanimotoScores {

    public static void main(String[] args) {
        collatedTanimotoSets();
    }

    public static void collatedTanimotoSets() {

        NumberFormat nf2 = new DecimalFormat();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);

        try {

            Gson gson = new Gson();

            File f = new File("/tmp/temp_tanimoto_collate.csv");

            System.out.println("Processing: " + f.getName());

            FileReader fr = new FileReader(f);
            BufferedReader br = new BufferedReader(fr);

            String fn = f.getName();

            TanimotoForceGraph fg = new TanimotoForceGraph();

            Info i = new Info();
            i.fingerprintType = "collated fingerprints";
            fg.info = i;

            // ignore the first line
            br.readLine();

            String line = "";
            String[] splitLine;

            HashSet<Integer> nscSet = new HashSet<Integer>();

            ArrayList<Node> nodeList = new ArrayList<Node>();
            List<TanimotoLink> linkList = new ArrayList<TanimotoLink>();

            while ((line = br.readLine()) != null) {

                splitLine = line.split(",");

                Integer uno = NumberUtils.isNumber(splitLine[0]) ? Integer.valueOf(splitLine[0]) : null;
                Integer duo = NumberUtils.isNumber(splitLine[1]) ? Integer.valueOf(splitLine[1]) : null;

                if (uno != null) {
                    nscSet.add(uno);
                }
                if (duo != null) {
                    nscSet.add(duo);
                }

            }

            // add nscSet members as nodes, and then do the lookups        
            ArrayList<Integer> nscList = new ArrayList<Integer>(nscSet);
            Collections.sort(nscList);

            for (Integer nscInt : nscList) {
                Node n = new Node();
                n.nsc = nscInt;
                nodeList.add(n);
            }

            // reset readers
            br.close();
            fr.close();

            fr = new FileReader(f);
            br = new BufferedReader(fr);

            int lineCnt = 0;

            br.readLine();

            while ((line = br.readLine()) != null) {

                lineCnt++;

                // System.out.println("lineCnt: " + lineCnt);
                // System.out.println("lineCnt: " + lineCnt + " line: " + line);
                // splitLine -1 -> include nulls (nothing between commas in csv file)
                splitLine = line.split(",", -1);

                Integer uno = NumberUtils.isNumber(splitLine[0]) ? Integer.valueOf(splitLine[0]) : null;
                Integer duo = NumberUtils.isNumber(splitLine[1]) ? Integer.valueOf(splitLine[1]) : null;

                Double atompairbv = splitLine[2].isEmpty() ? 0 : NumberUtils.isNumber(splitLine[2]) ? Double.valueOf(splitLine[2]) : 0;
                Double featmorganbv = splitLine[3].isEmpty() ? 0 : NumberUtils.isNumber(splitLine[3]) ? Double.valueOf(splitLine[3]) : 0;
                Double layered = splitLine[4].isEmpty() ? 0 : NumberUtils.isNumber(splitLine[4]) ? Double.valueOf(splitLine[4]) : 0;
                Double maccs = splitLine[5].isEmpty() ? 0 : NumberUtils.isNumber(splitLine[5]) ? Double.valueOf(splitLine[5]) : 0;
                Double morganbv = splitLine[6].isEmpty() ? 0 : NumberUtils.isNumber(splitLine[6]) ? Double.valueOf(splitLine[6]) : 0;
                Double rdkit = splitLine[7].isEmpty() ? 0 : NumberUtils.isNumber(splitLine[7]) ? Double.valueOf(splitLine[7]) : 0;
                Double torsionbv = splitLine[8].isEmpty() ? 0 : NumberUtils.isNumber(splitLine[8]) ? Double.valueOf(splitLine[8]) : 0;

                if (uno != null && duo != null) {
                    if (uno.intValue() < duo.intValue()) {

                        TanimotoLink l = new TanimotoLink();

                        l.source = nscList.indexOf(uno);
                        l.target = nscList.indexOf(duo);

//                        l.fm = nf2.format(featmorganbv);
//                        l.l = nf2.format(layered);
//                        l.mc = nf2.format(maccs);
//                        l.m = nf2.format(morganbv);
//                        l.r = nf2.format(rdkit);
//                        l.to = nf2.format(torsionbv);

                        linkList.add(l);
                    }
                }
            }

            fg.nodes = nodeList;
            fg.links = linkList;

            String json = gson.toJson(fg);
            File out = new File("/tmp/collatedFingerprints.json");
            FileWriter fw = new FileWriter(out);
            fw.write(json);
            fw.close();

            System.out.println("lineCnt: " + lineCnt);
            System.out.println("Done!");

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static void individualTanimotoSets() {

        try {

            Gson gson = new Gson();

            File where = new File("/tmp");

            FilenameFilter theFilter = new FilenameFilter() {
                public boolean accept(File dir, String name) {
                    if (name.startsWith("temp_tanimoto")) {
                        return true;
                    } else {
                        return false;
                    }
                }
            };

            File[] filesToProcess = where.listFiles(theFilter);

            for (File f : filesToProcess) {

                System.out.println("Processing: " + f.getName());

                HashSet<Integer> nscSet = new HashSet<Integer>();

                FileReader fr = new FileReader(f);
                BufferedReader br = new BufferedReader(fr);

                String fn = f.getName();
                String fp = fn.replaceAll("temp_tanimoto_", "").replaceAll(".csv", "");

                System.out.println("fp extracted from f.getName() is : " + fp);

                ForceGraph fg = new ForceGraph();

                Info i = new Info();
                i.fingerprintType = fp;
                fg.info = i;

                // ignore the first line
                br.readLine();

                String line = "";
                String[] splitLine;

                while ((line = br.readLine()) != null) {
                    splitLine = line.split(",");
                    nscSet.add(Integer.valueOf(splitLine[0]));
                    nscSet.add(Integer.valueOf(splitLine[1]));
                }

                ArrayList<Node> nodeList = new ArrayList<Node>();
                ArrayList<Link> linkList = new ArrayList<Link>();

                // add nscSet members as nodes, and then do the lookups        
                ArrayList<Integer> nscList = new ArrayList<Integer>(nscSet);
                Collections.sort(nscList);

                for (Integer nscInt : nscList) {
                    Node n = new Node();
                    n.nsc = nscInt;
                    nodeList.add(n);
                }

                // reset readers
                br.close();
                fr.close();

                fr = new FileReader(f);
                br = new BufferedReader(fr);

                br.readLine();

                while ((line = br.readLine()) != null) {

                    //System.out.println("line: " + line);
                    splitLine = line.split(",");
                    //System.out.println("splitLine: " + splitLine[0] + " " + splitLine[1] + " " + splitLine[2]);

                    if (Integer.valueOf(splitLine[0]).intValue() < Integer.valueOf(splitLine[1]).intValue()) {
                        Link l = new Link();
                        l.source = nscList.indexOf(Integer.valueOf(splitLine[0]));
                        l.target = nscList.indexOf(Integer.valueOf(splitLine[1]));
                        l.value = Double.valueOf(splitLine[2]);
                        linkList.add(l);
                    }
                }

                fg.nodes = nodeList;
                fg.links = linkList;

                String json = gson.toJson(fg);
                File out = new File("/tmp/" + fp + ".json");
                FileWriter fw = new FileWriter(out);
                fw.write(json);
                fw.close();

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
