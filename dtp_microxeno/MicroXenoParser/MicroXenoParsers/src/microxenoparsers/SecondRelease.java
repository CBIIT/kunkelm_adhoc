/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package microxenoparsers;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Arrays;

/**
 *
 * @author mwkunkel
 */
public class SecondRelease {

    public static final String INPUTFILE = "/home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/rma-sketch.summary.txt";
    public static final String OUTPUTFILE = "/home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA/rma-sketch.summary.txt.processed";

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        parseData();
    }

    public static void parseData() {

        System.out.println("Starting parseData");

        File inputFile = null;
        FileReader fr = null;
        BufferedReader br = null;

        File outputFile = null;
        FileWriter fw = null;

        try {

            inputFile = new File(INPUTFILE);
            outputFile = new File(OUTPUTFILE);

            System.out.println("Processing file: " + inputFile.getName() + " " + inputFile.getPath());

            int lineCounter = 0;

            fr = new FileReader(inputFile);
            br = new BufferedReader(fr);

            fw = new FileWriter(outputFile);

            String currentLine;
            String tempString;
            String[] splitString;

            StringBuilder outputBuffer = new StringBuilder();

            currentLine = br.readLine();
            lineCounter++;

            while (!currentLine.startsWith("probeset_id")) {
                currentLine = br.readLine();
                lineCounter++;
            }

            // parse the EA_ID line
            String[] eaLine = currentLine.split("\t");
//            for (String s : eaLine) {
//                System.out.println(s);
//            }

            // parse the rest of the lines
            while (((currentLine = br.readLine()) != null)) {                
                lineCounter++;
                String[] splitLine = currentLine.split("\t");
                System.out.println("line: " + lineCounter + " probeset_id: " + splitLine[0]);
                for (int i = 1; i < splitLine.length; i++) {
                    if (splitLine[i] != null) {
                        fw.write(eaLine[i] + "," + splitLine[0] + "," + splitLine[i] + "\n");
                    }
                }
            }

            fr.close();
            fr = null;

            fw.flush();
            fw.close();
            fw = null;

        } catch (Exception e) {
            System.out.println("Caught Exception: " + e);
            e.printStackTrace();
        } finally {
            try {
                br.close();
                fr.close();
                fw.flush();
                fw.close();
            } catch (Exception e) {
            }
        }

    }
}
