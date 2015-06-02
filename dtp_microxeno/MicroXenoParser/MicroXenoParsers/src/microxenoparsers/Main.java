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
public class Main {

  public static final String DATADIRECTORY = "/home/mwkunkel/DATA_DEPOT_IMPORTANT/MICROXENO_DATA";
  public static final String EADATADIRECTORY = DATADIRECTORY + "/EA_TXT_FILES";

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    parseAffyAnnotations();
    parseUnigene();
    // parseEAdataFiles();
    createSql();
  }

  public static void parseAffyAnnotations() {

    System.out.println("Starting parseAffyAnnotations");

    File inputFile = null;
    FileReader fr = null;
    BufferedReader br = null;

    File outputFile = null;
    FileWriter fw = null;

    try {

      inputFile = new File(DATADIRECTORY + "/HG-U133_Plus_2.na33.annot.csv");
      fr = new FileReader(inputFile);
      br = new BufferedReader(fr);

      outputFile = new File(DATADIRECTORY + "/HG_U133_Plus_2_PARSED.csv");
      fw = new FileWriter(outputFile);

      String inputLine;
      String[] splitLine;
      String tempString;

      String probeSetId;
      String gene;
      String representativePublicId;

      while ((inputLine = br.readLine()) != null) {
        if (!(inputLine.startsWith("#"))) {
          splitLine = inputLine.split("\",\"");

          tempString = splitLine[0];
          probeSetId = tempString.replace("\"", "");

          tempString = splitLine[14];
          gene = tempString.replace("\"", "");

          tempString = splitLine[8];
          representativePublicId = tempString.replace("\"", "");

          fw.write(probeSetId + "," + gene + "," + representativePublicId + "\n");

        }
      }

      System.out.println("Finished parseAffyAnnotations");

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

  public static void parseUnigene() {

    System.out.println("Starting parseUnigene");

    File inputFile = null;
    FileReader fr = null;
    BufferedReader br = null;

    File outputFile = null;
    FileWriter fw = null;

    try {

      inputFile = new File(DATADIRECTORY + "/Hs.236.data");
      fr = new FileReader(inputFile);
      br = new BufferedReader(fr);

      outputFile = new File(DATADIRECTORY + "/Unigene236.tab");
      fw = new FileWriter(outputFile);

      String inputLine;
      String[] splitLine;
      String tempString;

      String currentId = "";
      String currentTitle = "";
      String currentGene = "";
      String currentAccession = "";

      while ((inputLine = br.readLine()) != null) {
        if (inputLine.startsWith("ID")) {
          //ID          Hs.2
          tempString = inputLine.substring(2);
          currentId = tempString.trim();
          System.out.println("Now processing cluster: " + currentId);
        } else if (inputLine.startsWith("TITLE")) {
          //TITLE       N-acetyltransferase 2 (arylamine N-acetyltransferase)
          currentTitle = inputLine.substring(6).trim();
        } else if (inputLine.startsWith("GENE")) {
          //GENE        NAT2
          if (!(inputLine.startsWith("GENE_ID"))) {
            tempString = inputLine.substring(4);
            currentGene = tempString.trim();
          }
        } else if (inputLine.startsWith("SEQUENCE")) {
          //SEQUENCE    ACC=BC067218.1; NID=g45501306; PID=g45501307; SEQTYPE=mRNA
          tempString = inputLine.substring(8);
          tempString = tempString.trim();
          splitLine = tempString.split("; ");
          if (splitLine[0].startsWith("ACC=")) {
            currentAccession = splitLine[0].substring(4);
            //fetch only the root of any accessions that have version extensions
            splitLine = currentAccession.split("\\.");
            if (splitLine.length > 1) {
              currentAccession = splitLine[0];
            }
            fw.write(currentId + "\t" + currentTitle + "\t" + currentGene + "\t" + currentAccession + "\n");
          }
        }
      }

      System.out.println("Finished parseUnigene");

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

  public static void createSql() {

    System.out.println("Starting createSql");

    //File inputFile = null;
    FileReader fr = null;
    BufferedReader br = null;

    File outputFile = null;
    FileWriter fw = null;

    try {
      
      outputFile = new File("/tmp/script.sql");
      fw = new FileWriter(outputFile);
      
      fw.write("\\c microxenodb \n");
      
      // fetch a list of EA data files        
      File dataDirectory = new File(EADATADIRECTORY);
      File[] dataFiles = dataDirectory.listFiles();

      for (File inputFile : Arrays.asList(dataFiles)) {
        if (inputFile.getName().startsWith("EA") && inputFile.getName().endsWith(".txt")) {
          fw.write("\\copy ea_data_raw_upload from " + inputFile.getPath() + ".processed csv\n");
        }
      }
      
      fw.flush();
      fw.close();
      
      System.out.println("SQL script: " + outputFile.getPath());
      
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

  public static void parseEAdataFiles() {

    System.out.println("Starting parseEAdataFiles");

    //File inputFile = null;
    FileReader fr = null;
    BufferedReader br = null;

    File outputFile = null;
    FileWriter fw = null;

    try {

      // fetch a list of EA data files        
      File dataDirectory = new File(EADATADIRECTORY);
      File[] dataFiles = dataDirectory.listFiles();

      for (File inputFile : Arrays.asList(dataFiles)) {

        if (inputFile.getName().startsWith("EA") && inputFile.getName().endsWith(".txt")) {

          System.out.println("Processing EA data file: " + inputFile.getName() + " " + inputFile.getPath());

          int lineCounter = 0;

          fr = new FileReader(inputFile);
          br = new BufferedReader(fr);

          String currentLine;
          String tempString;
          String[] splitString;

          StringBuilder outputBuffer = new StringBuilder();

//check the first line

          currentLine = br.readLine();
          lineCounter++;

          boolean isNewFormat;

          if (currentLine.startsWith(("#%"))) {
            isNewFormat = true;
          } else {
            isNewFormat = false;
          }

          if (isNewFormat) {

            //if new file format, skip lines until find the ProbeSet line
            while (!currentLine.startsWith("Probe Set Name")) {
              currentLine = br.readLine();
              lineCounter++;
            }

          }

// parse out all of the data lines
// tab delimited, no option for missing data (will barf)

          thisloop:
          while ((currentLine = br.readLine()) != null) {

            lineCounter++;

            if (isNewFormat) {

              if (currentLine.startsWith("#%")) {
                //System.out.println("Found line: " + currentLine);
                break thisloop;
              }

              splitString = currentLine.split("\t");
              outputBuffer.append(inputFile.getName() + ",");
              outputBuffer.append(splitString[0] + ",");
              outputBuffer.append(splitString[4] + ",");
              outputBuffer.append(splitString[5] + ",");
              outputBuffer.append(splitString[3] + ",");
              outputBuffer.append(splitString[1] + ",");
              outputBuffer.append(splitString[2] + "\n");

            } else {

              splitString = currentLine.split("\t");
              outputBuffer.append(inputFile.getName() + ",");
              outputBuffer.append(splitString[0] + ",");
              outputBuffer.append(splitString[1] + ",");
              outputBuffer.append(splitString[2] + ",");
              outputBuffer.append(splitString[3] + ",");
              outputBuffer.append(splitString[4] + ",");
              outputBuffer.append(splitString[5] + "\n");

            }

          }

          fr.close();
          fr = null;

          outputFile = new File(EADATADIRECTORY + "/" + inputFile.getName() + ".processed");
          fw = new FileWriter(outputFile);
          fw.write(outputBuffer.toString());

          fw.close();
          fw = null;

        }

      }

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
