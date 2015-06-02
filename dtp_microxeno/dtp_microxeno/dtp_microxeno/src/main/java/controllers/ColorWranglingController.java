/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package controllers;

import java.util.HashMap;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.apache.log4j.Logger;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class ColorWranglingController {

    private static final Logger lgr = Logger.getLogger("GLOBAL");

    public static String colorByExpressionValue(String incoming, double lowCut, double midCut, double highCut) {

        // red      255     0   0  highCut
        // yellow   255     255 0  midCut
        // green    0       128 0  lowCut
        int redPart = 255;
        int greenPart = 255;
        int bluePart = 255;

        Double testVal = null;

        try {
            testVal = Double.valueOf(incoming);
        } catch (Exception e) {
            testVal = null;

            System.out.println("incoming: " + incoming);
            System.out.println("testVal: " + testVal);
            //lgr.error(" Exception in cellColor creating Double testVal from incoming: " + incoming);
        }

        if (testVal != null) {

            double dVal = testVal.doubleValue();

            if (dVal < lowCut) {
                dVal = lowCut;
            }
            if (dVal > highCut) {
                dVal = highCut;
            }

            if (dVal < midCut) {

                double fracDist = (dVal - lowCut) / (midCut - lowCut);
                // redPart is frag distance to red-for-yellow(255)
                redPart = (int) Math.ceil(255 * fracDist);
                greenPart = 128 + (int) Math.ceil(128 * fracDist);
                bluePart = 0;

            } else {

                double fracDist = (dVal - midCut) / (highCut - midCut);
                redPart = 255;
                greenPart = 255 - (int) Math.ceil(255 * fracDist);
                bluePart = 0;
            }

        } else {

            if (incoming.equals("NULL")) {

                redPart = 0;
                greenPart = 0;
                bluePart = 0;

            } else {

                redPart = 255;
                greenPart = 255;
                bluePart = 255;

            }

        }

        return "rgb(" + redPart + "," + greenPart + "," + bluePart + ")";
    }

    public static String colorByExpressionUnitDelta(String incoming) {

        double lowCut = -1;
        double midCut = 0;
        double highCut = 1;

        int redPart = 255;
        int greenPart = 255;
        int bluePart = 255;

        Double testVal = null;

        try {
            testVal = Double.parseDouble(incoming);
        } catch (Exception e) {
            lgr.error(" Exception in unitDeltaCellColor creating Double testVal from incoming: " + incoming);
            testVal = null;
        }

        if (testVal != null) {

            double dVal = testVal.doubleValue();

            if (dVal < lowCut) {
                dVal = lowCut;
            }

            if (dVal > highCut) {
                dVal = highCut;
            }

            if (dVal < midCut) {

                double fracDist = (dVal - lowCut) / (midCut - lowCut);
                redPart = (int) Math.ceil(255 * fracDist);
                greenPart = 128 + (int) Math.ceil(128 * fracDist);
                bluePart = 0;

            } else {

                double fracDist = (dVal - midCut) / (highCut - midCut);
                redPart = 255;
                greenPart = 255 - (int) Math.ceil(255 * fracDist);
                bluePart = 0;
            }

        } else {

            if (incoming.equals("NULL")) {

                redPart = 0;
                greenPart = 0;
                bluePart = 0;

            } else {

                redPart = 255;
                greenPart = 255;
                bluePart = 255;

            }
        }

        return "rgb(" + redPart + "," + greenPart + "," + bluePart + ")";
    }

    public static String colorByLogValue(String incoming) {

        double lowCut = -9;
        double midCut = -7;
        double highCut = -5;

        int redPart = 255;
        int greenPart = 255;
        int bluePart = 255;

        Double testVal = null;

        try {
            testVal = Double.valueOf(incoming);
        } catch (Exception e) {
            testVal = null;
            lgr.error(" Exception in cellColor creating Double testVal from incoming: " + incoming);
        }

        if (testVal != null) {

            double dVal = testVal.doubleValue();

            if (dVal < lowCut) {
                dVal = lowCut;
            }
            if (dVal > highCut) {
                dVal = highCut;
            }

            if (dVal < midCut) {

                double fracDist = (dVal - lowCut) / (midCut - lowCut);
                redPart = 255;
                greenPart = (int) Math.ceil(255 * fracDist);
                bluePart = 0;

            } else {

                double fracDist = (dVal - midCut) / (highCut - midCut);
                redPart = 255 - (int) Math.ceil(255 * fracDist);
                greenPart = 255 - (int) Math.ceil(128 * fracDist);
                bluePart = 0;
            }

        } else {

            if (incoming.equals("NULL")) {

                redPart = 0;
                greenPart = 0;
                bluePart = 0;

            } else {

                lgr.debug("incoming.valueOf was null and incoming != NULL so, return white");

                redPart = 255;
                greenPart = 255;
                bluePart = 255;

            }

        }

        return "rgb(" + redPart + "," + greenPart + "," + bluePart + ")";
    }

    public static String colorByUnitDelta(String incoming) {

        double lowCut = -1;
        double midCut = 0;
        double highCut = 1;

        int redPart = 255;
        int greenPart = 255;
        int bluePart = 255;

        Double testVal = null;

        try {
            testVal = Double.parseDouble(incoming);
        } catch (Exception e) {
            lgr.error(" Exception in unitDeltaCellColor creating Double testVal from incoming: " + incoming);
            testVal = null;
        }

        if (testVal != null) {

            double dVal = testVal.doubleValue();

            if (dVal < lowCut) {
                dVal = lowCut;
            }

            if (dVal > highCut) {
                dVal = highCut;
            }

            if (dVal < midCut) {

                double fracDist = (dVal - lowCut) / (midCut - lowCut);
                redPart = 255;
                greenPart = (int) Math.ceil(255 * fracDist);
                bluePart = 0;

            } else {

                double fracDist = (dVal - midCut) / (highCut - midCut);
                redPart = 255 - (int) Math.ceil(255 * fracDist);
                greenPart = 255 - (int) Math.ceil(128 * fracDist);
                bluePart = 0;
            }

        } else {

            if (incoming.equals("NULL")) {

                redPart = 0;
                greenPart = 0;
                bluePart = 0;

            } else {

                redPart = 255;
                greenPart = 255;
                bluePart = 255;

            }
        }

        return "rgb(" + redPart + "," + greenPart + "," + bluePart + ")";
    }

    public static String colorRgbByPanelCde(String panelCde) {
        String rtn = "rgba(0,0,0,0.5)";
        HashMap<String, String> rgbMap = new HashMap<String, String>();
        rgbMap.put("ASP", "rgba(215,171,83,0.5)");
        rgbMap.put("BRE", "rgba(255,175,175,0.5)");
        rgbMap.put("CHO", "rgba(149,53,121,0.5)");
        rgbMap.put("CNS", "rgba(128,128,128,0.5)");
        rgbMap.put("COL", "rgba(0,255,0,0.5)");
        rgbMap.put("DLS", "rgba(154,170,118,0.5)");
        rgbMap.put("EPS", "rgba(112,73,174,0.5)");
        rgbMap.put("EWS", "rgba(197,180,127,0.5)");
        rgbMap.put("FIS", "rgba(149,96,69,0.5)");
        rgbMap.put("GCS", "rgba(149,140,18,0.5)");
        rgbMap.put("LEU", "rgba(255,0,0,0.5)");
        rgbMap.put("LMS", "rgba(109,149,102,0.5)");
        rgbMap.put("LNS", "rgba(0,0,255,0.5)");
        rgbMap.put("MEL", "rgba(255,200,0,0.5)");
        rgbMap.put("MES", "rgba(216,184,63,0.5)");
        rgbMap.put("MPN", "rgba(142,164,122,0.5)");
        rgbMap.put("NDF", "rgba(128,128,128,0.5)");
        rgbMap.put("NHC", "rgba(128,128,128,0.5)");
        rgbMap.put("NMS", "rgba(128,128,128,0.5)");
        rgbMap.put("NOS", "rgba(128,128,128,0.5)");
        rgbMap.put("FIB", "rgba(255,88,0,0.5)");
        rgbMap.put("NLF", "rgba(127,110,102,0.5)");
        rgbMap.put("NAD", "rgba(0,133,204,0.5)");
        rgbMap.put("NSM", "rgba(128,128,128,0.5)");
        rgbMap.put("OST", "rgba(234,162,40,0.5)");
        rgbMap.put("OVA", "rgba(255,0,255,0.5)");
        rgbMap.put("PRO", "rgba(72,209,204,0.5)");
        rgbMap.put("REN", "rgba(255,255,0,0.5)");
        rgbMap.put("RHM", "rgba(75,93,228,0.5)");
        rgbMap.put("RHM", "rgba(75,93,228,0.5)");
        rgbMap.put("RHT", "rgba(87,149,117,0.5)");
        rgbMap.put("SAR", "rgba(75,178,197,0.5)");
        rgbMap.put("SCL", "rgba(235,136,31,0.5)");
        rgbMap.put("SCS", "rgba(145,138,145,0.5)");
        rgbMap.put("SYS", "rgba(131,149,87,0.5)");
        rgbMap.put("UTS", "rgba(140,144,52,0.5)");
        if (rgbMap.containsKey(panelCde)) {
            rtn = rgbMap.get(panelCde);
        }
        return rtn;
    }

    public static String colorRgbByPanelName(String panelName) {
        String rtn = "rgb(0,0,0,0.5)";
        HashMap<String, String> rgbMap = new HashMap<String, String>();
        rgbMap.put("Alveolar Soft Part Sarcoma", "rgba(215,171,83,0.5)");
        rgbMap.put("Breast", "rgba(255,175,175,0.5)");
        rgbMap.put("Chondrosarcoma", "rgba(149,53,121,0.5)");
        rgbMap.put("CNS", "rgba(128,128,128,0.5)");
        rgbMap.put("Colon", "rgba(0,255,0,0.5)");
        rgbMap.put("Dedifferentiated Liposar", "rgba(154,170,118,0.5)");
        rgbMap.put("Epithelioid Sarcoma", "rgba(112,73,174,0.5)");
        rgbMap.put("Ewing Sarcoma", "rgba(197,180,127,0.5)");
        rgbMap.put("Fibrosarcoma", "rgba(149,96,69,0.5)");
        rgbMap.put("Giant Cell Sarcoma", "rgba(149,140,18,0.5)");
        rgbMap.put("Leukemia", "rgba(255,0,0,0.5)");
        rgbMap.put("Leiomyosarcoma", "rgba(109,149,102,0.5)");
        rgbMap.put("Non-Small Cell Lung Cancer", "rgba(0,0,255,0.5)");
        rgbMap.put("Melanoma", "rgba(255,200,0,0.5)");
        rgbMap.put("Mesothelioma", "rgba(216,184,63,0.5)");
        rgbMap.put("MPNS", "rgba(142,164,122,0.5)");
        rgbMap.put("Normal Dermal Fibroblast", "rgba(128,128,128,0.5)");
        rgbMap.put("Normal Human Chondrocyte", "rgba(128,128,128,0.5)");
        rgbMap.put("Norm Mesenchymal Stem Cell", "rgba(128,128,128,0.5)");
        rgbMap.put("Normal Human Osteoblast", "rgba(128,128,128,0.5)");
        rgbMap.put("Fibroblast", "rgba(255,88,0,0.5)");
        rgbMap.put("Normal Lung Fibroblast", "rgba(127,110,102,0.5)");
        rgbMap.put("Normal Adrenal", "rgba(0,133,204,0.5)");
        rgbMap.put("Norm Human Skeletal Muscle", "rgba(128,128,128,0.5)");
        rgbMap.put("Osteosarcoma", "rgba(234,162,40,0.5)");
        rgbMap.put("Ovarian", "rgba(255,0,255,0.5)");
        rgbMap.put("Prostate", "rgba(72,209,204,0.5)");
        rgbMap.put("Renal", "rgba(255,255,0,0.5)");
        rgbMap.put("Rhabdomyosarcoma", "rgba(75,93,228,0.5)");
        rgbMap.put("Rhabdomyosarcoma ", "rgba(75,93,228,0.5)");
        rgbMap.put("Rhabdoid Tumor", "rgba(87,149,117,0.5)");
        rgbMap.put("Bone/Muscle", "rgba(75,178,197,0.5)");
        rgbMap.put("Small Cell Lung Cancer", "rgba(235,136,31,0.5)");
        rgbMap.put("Spindle Cell Sarcoma", "rgba(145,138,145,0.5)");
        rgbMap.put("Synovial Sarcoma", "rgba(131,149,87,0.5)");
        rgbMap.put("Uterine Sarcoma", "rgba(140,144,52,0.5)");
        if (rgbMap.containsKey(panelName)) {
            rtn = rgbMap.get(panelName);
        }
        return rtn;
    }
}