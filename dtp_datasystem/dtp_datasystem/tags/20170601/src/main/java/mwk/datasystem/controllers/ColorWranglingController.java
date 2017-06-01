/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.controllers;

import java.util.HashMap;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class ColorWranglingController {

    /**
     *
     * @param panelCde
     * @return
     */
    public static String cellColorByPanelCde(String panelCde) {
        return "background-color:" + colorRgbByPanelCde(panelCde);
    }

    /**
     *
     * @param panelName
     * @return
     */
    public static String cellColorByPanelName(String panelName) {
        return "background-color:" + colorRgbByPanelName(panelName);
    }

    /**
     *
     * @param incoming
     * @return
     */
    public static String cellColorByCorrelation(String incoming) {
        return "background-color:" + colorByCorrelation(incoming);
    }

    /**
     *
     * @param incoming
     * @return
     */
    public static String cellColorByLogValue(String incoming) {
        return "background-color:" + colorByLogValue(incoming);
    }

    /**
     *
     * @param incoming
     * @return
     */
    public static String cellColorByUnitDelta(String incoming) {
        return "background-color:" + colorByUnitDelta(incoming);
    }

    /**
     *
     * @param incoming
     * @return
     */
    public static String cellColorByExpressionUnitDelta(String incoming) {
        return "background-color:" + colorByExpressionUnitDelta(incoming);
    }

    public static String colorRgbByPanelCde(String panelCde) {
        String rtn = "rgba(0,0,0,0.5)";
        HashMap<String, String> rgbaMap = new HashMap<String, String>();
        rgbaMap.put("ASP", "rgba(215,171,83,0.5)");
        rgbaMap.put("BRE", "rgba(255,175,175,0.5)");
        rgbaMap.put("CHO", "rgba(149,53,121,0.5)");
        rgbaMap.put("CNS", "rgba(128,128,128,0.5)");
        rgbaMap.put("COL", "rgba(0,255,0,0.5)");
        rgbaMap.put("DLS", "rgba(154,170,118,0.5)");
        rgbaMap.put("EPS", "rgba(112,73,174,0.5)");
        rgbaMap.put("EWS", "rgba(197,180,127,0.5)");
        rgbaMap.put("FIS", "rgba(149,96,69,0.5)");
        rgbaMap.put("GCS", "rgba(149,140,18,0.5)");
        rgbaMap.put("LEU", "rgba(255,0,0,0.5)");
        rgbaMap.put("LMS", "rgba(109,149,102,0.5)");
        rgbaMap.put("LNS", "rgba(0,0,255,0.5)");
        rgbaMap.put("MEL", "rgba(255,200,0,0.5)");
        rgbaMap.put("MES", "rgba(216,184,63,0.5)");
        rgbaMap.put("MPN", "rgba(142,164,122,0.5)");
        rgbaMap.put("NDF", "rgba(128,128,128,0.5)");
        rgbaMap.put("NHC", "rgba(128,128,128,0.5)");
        rgbaMap.put("NMS", "rgba(128,128,128,0.5)");
        rgbaMap.put("NOS", "rgba(128,128,128,0.5)");
        rgbaMap.put("FIB", "rgba(255,88,0,0.5)");
        rgbaMap.put("NLF", "rgba(127,110,102,0.5)");
        rgbaMap.put("NAD", "rgba(0,133,204,0.5)");
        rgbaMap.put("NSM", "rgba(128,128,128,0.5)");
        rgbaMap.put("OST", "rgba(234,162,40,0.5)");
        rgbaMap.put("OVA", "rgba(255,0,255,0.5)");
        rgbaMap.put("PRO", "rgba(72,209,204,0.5)");
        rgbaMap.put("REN", "rgba(255,255,0,0.5)");
        rgbaMap.put("RHM", "rgba(75,93,228,0.5)");
        rgbaMap.put("RHM", "rgba(75,93,228,0.5)");
        rgbaMap.put("RHT", "rgba(87,149,117,0.5)");
        rgbaMap.put("SAR", "rgba(75,178,197,0.5)");
        rgbaMap.put("SCL", "rgba(235,136,31,0.5)");
        rgbaMap.put("SCS", "rgba(145,138,145,0.5)");
        rgbaMap.put("SYS", "rgba(131,149,87,0.5)");
        rgbaMap.put("UTS", "rgba(140,144,52,0.5)");
        if (rgbaMap.containsKey(panelCde)) {
            rtn = rgbaMap.get(panelCde);
        }
        return rtn;
    }

    public static String colorRgbByPanelName(String panelName) {
        String rtn = "rgb(0,0,0,0.5)";
        HashMap<String, String> rgbaMap = new HashMap<String, String>();
        rgbaMap.put("Alveolar Soft Part Sarcoma", "rgba(215,171,83,0.5)");
        rgbaMap.put("Breast", "rgba(255,175,175,0.5)");
        rgbaMap.put("Chondrosarcoma", "rgba(149,53,121,0.5)");
        rgbaMap.put("CNS", "rgba(128,128,128,0.5)");
        rgbaMap.put("Colon", "rgba(0,255,0,0.5)");
        rgbaMap.put("Dedifferentiated Liposar", "rgba(154,170,118,0.5)");
        rgbaMap.put("Epithelioid Sarcoma", "rgba(112,73,174,0.5)");
        rgbaMap.put("Ewing Sarcoma", "rgba(197,180,127,0.5)");
        rgbaMap.put("Fibrosarcoma", "rgba(149,96,69,0.5)");
        rgbaMap.put("Giant Cell Sarcoma", "rgba(149,140,18,0.5)");
        rgbaMap.put("Leukemia", "rgba(255,0,0,0.5)");
        rgbaMap.put("Leiomyosarcoma", "rgba(109,149,102,0.5)");
        rgbaMap.put("Non-Small Cell Lung Cancer", "rgba(0,0,255,0.5)");
        rgbaMap.put("Melanoma", "rgba(255,200,0,0.5)");
        rgbaMap.put("Mesothelioma", "rgba(216,184,63,0.5)");
        rgbaMap.put("MPNS", "rgba(142,164,122,0.5)");
        rgbaMap.put("Normal Dermal Fibroblast", "rgba(128,128,128,0.5)");
        rgbaMap.put("Normal Human Chondrocyte", "rgba(128,128,128,0.5)");
        rgbaMap.put("Norm Mesenchymal Stem Cell", "rgba(128,128,128,0.5)");
        rgbaMap.put("Normal Human Osteoblast", "rgba(128,128,128,0.5)");
        rgbaMap.put("Fibroblast", "rgba(255,88,0,0.5)");
        rgbaMap.put("Normal Lung Fibroblast", "rgba(127,110,102,0.5)");
        rgbaMap.put("Normal Adrenal", "rgba(0,133,204,0.5)");
        rgbaMap.put("Norm Human Skeletal Muscle", "rgba(128,128,128,0.5)");
        rgbaMap.put("Osteosarcoma", "rgba(234,162,40,0.5)");
        rgbaMap.put("Ovarian", "rgba(255,0,255,0.5)");
        rgbaMap.put("Prostate", "rgba(72,209,204,0.5)");
        rgbaMap.put("Renal", "rgba(255,255,0,0.5)");
        rgbaMap.put("Rhabdomyosarcoma", "rgba(75,93,228,0.5)");
        rgbaMap.put("Rhabdomyosarcoma ", "rgba(75,93,228,0.5)");
        rgbaMap.put("Rhabdoid CellLine", "rgba(87,149,117,0.5)");
        rgbaMap.put("Bone/Muscle", "rgba(75,178,197,0.5)");
        rgbaMap.put("Small Cell Lung Cancer", "rgba(235,136,31,0.5)");
        rgbaMap.put("Spindle Cell Sarcoma", "rgba(145,138,145,0.5)");
        rgbaMap.put("Synovial Sarcoma", "rgba(131,149,87,0.5)");
        rgbaMap.put("Uterine Sarcoma", "rgba(140,144,52,0.5)");
        if (rgbaMap.containsKey(panelName)) {
            rtn = rgbaMap.get(panelName);
        }
        return rtn;
    }

    public static String colorByCorrelation(String incoming) {
        //
        double lowCut = 0.6;
        double midCut = 0.8;
        double highCut = 1;
        //
        int redPart = 255;
        int greenPart = 255;
        int bluePart = 255;

        if (incoming == null) {
            // default return is 255, 255, 255
        } else if (incoming.startsWith("NULL")) {

            redPart = 0;
            greenPart = 0;
            bluePart = 0;

        } else {

            //
            Double testVal = null;
            //
            try {
                testVal = Double.parseDouble(incoming);
            } catch (Exception e) {
                testVal = null;
            }

            if (testVal == null) {
            } else {
                double dVal = testVal.doubleValue();
                if (dVal < lowCut) {
                    dVal = lowCut;
                }
                if (dVal > highCut) {
                    dVal = highCut;
                }
                if (dVal > midCut) {
                    double fracDist = (dVal - midCut) / (highCut - midCut);
                    redPart = 255;
                    greenPart = 255 - (int) Math.ceil(255 * fracDist);
                    bluePart = 0;
                } else {
                    double fracDist = (dVal - lowCut) / (midCut - lowCut);
                    redPart = (int) Math.ceil(255 * fracDist);
                    greenPart = 255;
                    bluePart = 0;
                }
            }
        }

        return "rgb(" + redPart + "," + greenPart + "," + bluePart + ")";
    }

    public static String colorByLogValue(String incoming) {
        return colorByLogValue(incoming, -9d, -7d, -5d);
    }

    public static String colorByLogValue(String incoming, double lowCut, double midCut, double highCut) {
        //
//        double lowCut = -9;
//        double midCut = -7;
//        double highCut = -5;
        //
        int redPart = 255;
        int greenPart = 255;
        int bluePart = 255;

        if (incoming == null) {
            // default return is 255, 255, 255
        } else if (incoming.startsWith("NULL")) {

            redPart = 0;
            greenPart = 0;
            bluePart = 0;

        } else {

            //
            Double testVal = null;
            //
            try {
                testVal = Double.valueOf(incoming);
            } catch (Exception e) {
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
            }

        }

        return "rgb(" + redPart + "," + greenPart + "," + bluePart + ")";
    }

    public static String colorByExpressionUnitDelta(String incoming) {

        String rtnString = "";

        Double testVal = null;
        //
        try {
            testVal = Double.parseDouble(incoming);
        } catch (Exception e) {
            testVal = null;
        }

        if (testVal != null) {
            testVal = -1 * testVal;
            rtnString = colorByUnitDelta(testVal.toString());
        } else {
            rtnString = colorByUnitDelta(null);
        }

        return rtnString;

    }

    public static String colorByUnitDelta(String incoming) {
        //
        double lowCut = -1;
        double midCut = 0;
        double highCut = 1;
        //
        int redPart = 255;
        int greenPart = 255;
        int bluePart = 255;

        if (incoming == null) {
            // default return is 255, 255, 255
        } else if (incoming.startsWith("NULL")) {

            redPart = 0;
            greenPart = 0;
            bluePart = 0;

        } else {

            //
            Double testVal = null;
            //
            try {
                testVal = Double.parseDouble(incoming);
            } catch (Exception e) {
                testVal = null;
            }

            if (testVal == null) {
            } else {
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
            }
        }

        return "rgb(" + redPart + "," + greenPart + "," + bluePart + ")";
    }
}
