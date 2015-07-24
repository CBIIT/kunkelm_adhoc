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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
        directToFile();
    }

    public static void viaHashMap() {

        try {

            String fileNameFilter = "NCT.*xml";
            Pattern p = Pattern.compile(fileNameFilter);

            File dir = new File("/home/mwkunkel/Downloads/tmp");

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

            // global lists across ALL xml
            HashSet<String> globalCancerSet = new HashSet<String>();
            HashSet<String> globalNotCancerSet = new HashSet<String>();

            int cnt = 0;

            for (String fileName : list) {

                cnt++;

                JAXBContext jc = JAXBContext.newInstance("clinicaltrialsgov.mwk.clinicaltrials");
                Unmarshaller u = jc.createUnmarshaller();

                ClinicalStudy cs = (ClinicalStudy) u.unmarshal(new FileInputStream("/home/mwkunkel/Downloads/tmp/" + fileName));

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

                System.out.println("cnt: " + cnt + " nctId: " + nctId);

                // multiples
                HashSet<String> localDrugSet = new HashSet<String>();
                HashSet<String> localCancerSet = new HashSet<>();
                HashSet<String> localNotCancerSet = new HashSet<>();

                for (InterventionStruct is : cs.getIntervention()) {

                    // System.out.println("intervention: " + is.getInterventionType() + " " + is.getInterventionName());
                    if (is.getInterventionType().equals("Drug")) {

                        // System.out.println("-------recognized as Drug: " + is.getInterventionName());
                        localDrugSet.add(is.getInterventionName());

                        if (is.getInterventionName().toUpperCase().contains("COMB")
                                || is.getInterventionName().toUpperCase().contains(" WITH ")
                                || is.getInterventionName().toUpperCase().contains(" AND ")
                                || is.getInterventionName().toUpperCase().contains(" PLUS ")) {

                            // System.out.println("-------possible combo: " + is.getInterventionName());
                        }

                    }
                }

                for (String condition : cs.getCondition()) {
                    if (condition.toUpperCase().contains("CANCER")
                            || condition.toUpperCase().contains("TUMOR")
                            || condition.toUpperCase().contains("TUMOUR")
                            || condition.toUpperCase().contains("CARCIN")
                            || condition.toUpperCase().contains("METAST")
                            || condition.toUpperCase().contains("BLAST")
                            || condition.toUpperCase().contains("NOMA")
                            || condition.toUpperCase().contains("TOMA")
                            || condition.toUpperCase().contains("LYMPHO")
                            || condition.toUpperCase().contains("MYELO")
                            || condition.toUpperCase().contains("SARCO")
                            || condition.toUpperCase().contains("LEUK")
                            || condition.toUpperCase().contains("MELANO")
                            || condition.toUpperCase().contains("ASTRO")
                            || condition.toUpperCase().contains("MALIG")
                            || condition.toUpperCase().contains("NEOPLA")
                            || condition.toUpperCase().contains("ONCO")
                            || condition.toUpperCase().contains("GLIO")
                            || condition.toUpperCase().contains("NEUROMA")) {
                        // System.out.println("-------condition (possibly) recognized as CANCER: " + condition);
                        localCancerSet.add(condition);
                        globalCancerSet.add(condition);
                    } else {
                        localNotCancerSet.add(condition);
                    }
                }

                if (localCancerSet.isEmpty()) {
                    for (String c : localNotCancerSet) {
                        globalNotCancerSet.add(c);
                    }
                }

                for (String d : localDrugSet) {

                    parsedContent pc = null;

                    if (contentMap.containsKey(d)) {
                        pc = contentMap.get(d);
                    } else {
                        pc = new parsedContent();
                        contentMap.put(d, pc);
                    }

                    pc.singletonStrings.add(singletonString);
                    for (String d2 : localDrugSet) {
                        pc.drugs.add(d2);
                    }
                    for (String c : localCancerSet) {
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

            ArrayList<String> canList = new ArrayList<String>(globalCancerSet);
            ArrayList<String> notCanList = new ArrayList<String>(globalNotCancerSet);

            Collections.sort(canList);
            Collections.sort(notCanList);

            for (String c : canList) {
                System.out.println("canList: " + c);
            }

            for (String c : notCanList) {
                System.out.println("notCanList: " + c);
            }

        } catch (UnmarshalException ue) {
            //System.out.println("Caught UnmarshalException");
        } catch (JAXBException je) {
            je.printStackTrace();
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

    public static void directToFile() {

//delete from clinical_trials;
//alter table clinical_trials drop column formatted_date;
//\copy clinical_trials from /tmp/clinicalTrials.output csv header delimiter as E'\t';
//alter table clinical_trials add formatted_date;
//alter table clinical_trials add formatted_date date;
//update clinical_trials set date = first_received::date;
//drop table if exists temp;
//create table temp as select drug, count(*), array_to_string(array_agg(distinct phase), ',') as phase, max(formatted_date) as first_received from clinical_trials group by 1 order by 2 desc;
        try {

            String fileNameFilter = "NCT.*xml";
            Pattern p = Pattern.compile(fileNameFilter);

            File dir = new File("/home/mwkunkel/Downloads/tmp");

            String[] list = dir.list(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String name) {
//                    if (name.matches("NCT.*\\.xml")) {
//                        //System.out.println("File matches: " + name);
//                    }
                    return name.matches("NCT.*\\.xml");
                }
            });

            // global lists across ALL xml
            HashSet<String> globalCancerSet = new HashSet<String>();
            HashSet<String> globalNotCancerSet = new HashSet<String>();

            PrintWriter drugPhaseFirstReceived = new PrintWriter(new BufferedWriter(new FileWriter("/tmp/clinicalTrials.output")));
            drugPhaseFirstReceived.println("drug\tphase\tfirstReceived");

            int cnt = 0;

            for (String fileName : list) {

                cnt++;

                JAXBContext jc = JAXBContext.newInstance("clinicaltrialsgov.mwk.clinicaltrials");
                Unmarshaller u = jc.createUnmarshaller();

                ClinicalStudy cs = (ClinicalStudy) u.unmarshal(new FileInputStream("/home/mwkunkel/Downloads/tmp/" + fileName));

                // singletons
                String nctId = cs.getIdInfo().getNctId();
                String phase = cs.getPhase();
                String status = cs.getOverallStatus();
                String sponsor = cs.getSponsors().getLeadSponsor().getAgency();
                String firstReceived = cs.getFirstreceivedDate().getContent();

                System.out.println("cnt: " + cnt + " nctId: " + nctId);

                // multiples
                HashSet<String> localDrugSet = new HashSet<String>();
                HashSet<String> localCancerSet = new HashSet<>();
                HashSet<String> localNotCancerSet = new HashSet<>();

                for (InterventionStruct is : cs.getIntervention()) {

                    // System.out.println("intervention: " + is.getInterventionType() + " " + is.getInterventionName());
                    if (is.getInterventionType().equals("Drug")) {

                        // System.out.println("-------recognized as Drug: " + is.getInterventionName());
                        localDrugSet.add(is.getInterventionName());

                        if (is.getInterventionName().toUpperCase().contains("COMB")
                                || is.getInterventionName().toUpperCase().contains(" WITH ")
                                || is.getInterventionName().toUpperCase().contains(" AND ")
                                || is.getInterventionName().toUpperCase().contains(" PLUS ")) {

                            // System.out.println("-------possible combo: " + is.getInterventionName());
                        }

                    }
                }

                for (String condition : cs.getCondition()) {
                    if (condition.toUpperCase().contains("CANCER")
                            || condition.toUpperCase().contains("TUMOR")
                            || condition.toUpperCase().contains("TUMOUR")
                            || condition.toUpperCase().contains("CARCIN")
                            || condition.toUpperCase().contains("METAST")
                            || condition.toUpperCase().contains("BLAST")
                            || condition.toUpperCase().contains("NOMA")
                            || condition.toUpperCase().contains("TOMA")
                            || condition.toUpperCase().contains("MOMA")
                            || condition.toUpperCase().contains("ALL")
                            || condition.toUpperCase().contains("CML")
                            || condition.toUpperCase().contains("LYMPHO")
                            || condition.toUpperCase().contains("MYELO")
                            || condition.toUpperCase().contains("SARCO")
                            || condition.toUpperCase().contains("LEUK")
                            || condition.toUpperCase().contains("MELANO")
                            || condition.toUpperCase().contains("ASTRO")
                            || condition.toUpperCase().contains("MALIG")
                            || condition.toUpperCase().contains("NEOPLA")
                            || condition.toUpperCase().contains("ONCO")
                            || condition.toUpperCase().contains("GLIO")
                            || condition.toUpperCase().contains("NEUROMA")) {
                        // System.out.println("-------condition (possibly) recognized as CANCER: " + condition);
                        localCancerSet.add(condition);
                        globalCancerSet.add(condition);
                    } else {
                        localNotCancerSet.add(condition);
                    }
                }

                if (localCancerSet.isEmpty()) {
                    for (String c : localNotCancerSet) {
                        globalNotCancerSet.add(c);
                    }
                } else { // this is a drug for cancer
                    for (String d : localDrugSet) {

                        System.out.println("Drug: " + d);

                        String[] splitDrug = d.split(" ");

                        for (int i = 0; i < splitDrug.length; i++) {

                            System.out.println("splitDrug: " + splitDrug[i]);

                            String fixedDrug = splitDrug[i].replaceAll("[^a-zA-Z0-9-]", "").toUpperCase();

                            System.out.println("fixedDrug: " + fixedDrug);

                            drugPhaseFirstReceived.println(fixedDrug + "\t" + phase + "\t" + firstReceived);
                        }
                    }
                }
            }

            drugPhaseFirstReceived.flush();
            drugPhaseFirstReceived.close();

            // write the lists of conditions recognized and not recognized as cancer
            ArrayList<String> canList = new ArrayList<String>(globalCancerSet);
            ArrayList<String> notCanList = new ArrayList<String>(globalNotCancerSet);

            Collections.sort(canList);
            Collections.sort(notCanList);

            PrintWriter cancerListWriter = new PrintWriter(new BufferedWriter(new FileWriter("/tmp/cancerList.output")));
            for (String c : canList) {
                cancerListWriter.println(c);
            }
            cancerListWriter.flush();
            cancerListWriter.close();

            PrintWriter notCancerListWriter = new PrintWriter(new BufferedWriter(new FileWriter("/tmp/notCancerList.output")));
            for (String notC : notCanList) {
                notCancerListWriter.println(notC);
            }
            notCancerListWriter.flush();
            notCancerListWriter.close();

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
