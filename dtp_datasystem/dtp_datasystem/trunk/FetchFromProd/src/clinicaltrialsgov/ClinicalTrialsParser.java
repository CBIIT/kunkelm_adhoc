/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package clinicaltrialsgov;

import java.io.FileInputStream;
import java.io.IOException;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.UnmarshalException;
import javax.xml.bind.Unmarshaller;
import clinicaltrialsgov.mwk.clinicaltrials.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.FilenameFilter;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.regex.Pattern;

/**
 *
 * @author mwkunkel
 */
public class ClinicalTrialsParser {

    static class parsedContent {

        public HashSet<String> drugs;
        public HashSet<String> cancers;
        public HashSet<String> singletonStrings;

        public parsedContent() {
            drugs = new HashSet<String>();
            cancers = new HashSet<String>();
            singletonStrings = new HashSet<>();
        }

    }

    public static void main(String[] args) {
        testFileNameFilter();
    }

    public static void testFileNameFilter() {

        try {

            String fileNameFilter = "NCT.*xml";
            Pattern p = Pattern.compile(fileNameFilter);

            File dir = new File("/tmp");

            String[] list = dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
//                    if (name.matches("NCT.*\\.xml")) {
//                        //System.out.println("File matches: " + name);
//                    }
                    return name.matches("NCT.*\\.xml");
                }
            });

            HashMap<String, parsedContent> contentMap = new HashMap<String, parsedContent>();

            for (String fileName : list) {

                JAXBContext jc = JAXBContext.newInstance("clinicaltrialsgov.mwk.clinicaltrials");
                Unmarshaller u = jc.createUnmarshaller();

                ClinicalStudy cs = (ClinicalStudy) u.unmarshal(new FileInputStream("/tmp/" + fileName));

                // singletons
                String nctId = cs.getIdInfo().getNctId();
                String phase = cs.getPhase();
                String status = cs.getOverallStatus();
                String sponsor = cs.getSponsors().getLeadSponsor().getAgency();
                String firstReceived = cs.getFirstreceivedDate().getContent();

//                String singletonString = "nctId: " + nctId + "\n"
//                        + " sponsor: " + sponsor + "\n"
//                        + " phase: " + phase + "\n"
//                        + " status: " + status + "\n"
//                        + " firstReceivedDate: " + firstReceived;
                String singletonString = "nctId: " + nctId
                        + " sponsor: " + sponsor
                        + " phase: " + phase
                        + " status: " + status
                        + " firstReceivedDate: " + firstReceived;

                //System.out.println(singletonString);
                // multiples
                HashSet<String> drugSet = new HashSet<String>();
                HashSet<String> cancerSet = new HashSet<>();

                for (InterventionStruct is : cs.getIntervention()) {
                    //System.out.println("intervention: " + is.getInterventionType() + " " + is.getInterventionName());
                    if (is.getInterventionType().equals("Drug")) {
                        //System.out.println("-------recognized as Drug: " + is.getInterventionName());
                        drugSet.add(is.getInterventionName());

                        if (is.getInterventionName().toUpperCase().contains("COMB")
                                || is.getInterventionName().toUpperCase().contains(" WITH ")
                                || is.getInterventionName().toUpperCase().contains(" AND ")
                                || is.getInterventionName().toUpperCase().contains(" PLUS ")) {

                            System.out.println("-------possible combo: " + is.getInterventionName());
                        }

                    }
                }

                for (String condition : cs.getCondition()) {
                    // //System.out.println("condition: " + condition);
                    if (condition.toUpperCase().contains("CANCER")
                            || condition.toUpperCase().contains("TUMOR")
                            || condition.toUpperCase().contains("CARCIN")
                            || condition.toUpperCase().contains("LYMPYHO")
                            || condition.toUpperCase().contains("MYELO")
                            || condition.toUpperCase().contains("SARCO")
                            || condition.toUpperCase().contains("LEUK")
                            || condition.toUpperCase().contains("MELANO")
                            || condition.toUpperCase().contains("ASTRO")
                            || condition.toUpperCase().contains("GLIOMA")
                            || condition.toUpperCase().contains("NEUROMA")) {
                        // //System.out.println("-------condition (possibly) recognized as CANCER: " + condition);
                        cancerSet.add(condition);
                    }
                }

                for (String d : drugSet) {

                    parsedContent pc = null;

                    if (contentMap.containsKey(d)) {
                        pc = contentMap.get(d);
                    } else {
                        pc = new parsedContent();
                        contentMap.put(d, pc);
                    }

                    pc.singletonStrings.add(singletonString);
                    for (String d2 : drugSet) {
                        pc.drugs.add(d2);
                    }
                    for (String c : cancerSet) {
                        pc.cancers.add(c);
                    }

                }
            }

            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("/tmp/clinicalTrials.output")));

            out.println("drug\tsingletonString\tdrugs\tcancers");

            for (String key : contentMap.keySet()) {
                parsedContent pc = contentMap.get(key);
                out.println(
                        key + "\t"
                        + join(", ", pc.singletonStrings) + "\t"
                        + join(", ", pc.drugs) + "\t"
                        + join(", ", pc.cancers)
                );
            }

            out.flush();
            out.close();

        } catch (UnmarshalException ue) {
            //System.out.println("Caught UnmarshalException");
        } catch (JAXBException je) {
            je.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static String join(String sep, Collection col) {
        StringBuffer buf = new StringBuffer();
        Iterator anIterator = col.iterator();
        boolean isFirst = true;
        while (anIterator.hasNext()) {
            if (!isFirst) {
                buf.append(sep);
            }
            buf.append(anIterator.next().toString());
            isFirst = false;
        }
        return buf.toString();
    }

}
