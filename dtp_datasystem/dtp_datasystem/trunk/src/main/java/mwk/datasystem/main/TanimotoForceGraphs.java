/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.main;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import mwk.d3.ForceGraph;
import mwk.d3.Info;
import mwk.d3.Link;
import mwk.d3.Node;

/**
 *
 * @author mwkunkel
 */
public class TanimotoForceGraphs {

    public static final Boolean DEBUG = Boolean.TRUE;

    public static void main(String[] args) {

        try {

            Gson gson = new GsonBuilder().setPrettyPrinting().create();

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

                    System.out.println("line: " + line);
                    splitLine = line.split(",");
                    System.out.println("splitLine: " + splitLine[0] + " " + splitLine[1] + " " + splitLine[2]);

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
