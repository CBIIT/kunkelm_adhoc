/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import com.flaptor.hist4j.AdaptiveHistogram;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mwk.datasystem.mwkcharting.Histogram;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;

/**
 *
 * @author mwkunkel
 */
public class HistogramChartUtil {

    public static ArrayList<Histogram> doHistograms(CmpdListVO incoming, List<String> propertyNameList) {

        ArrayList<Histogram> rtn = new ArrayList<Histogram>();

//    ArrayList<String> propertyNameList = new ArrayList<String>();
//    propertyNameList.add("mw");
//    propertyNameList.add("hba");
//    propertyNameList.add("hbd");
//    propertyNameList.add("alogp");
//    propertyNameList.add("logd");
//    propertyNameList.add("sa");
//    propertyNameList.add("psa");

        HashMap<String, AdaptiveHistogram> ahMap = new HashMap<String, AdaptiveHistogram>();

        // initialize the ah map
        for (String propertyName : propertyNameList) {
            ahMap.put(propertyName, new AdaptiveHistogram());
        }

        for (CmpdListMemberVO clmVO : incoming.getCmpdListMembers()) {

            CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

            // these are hard-coded since reflection is problematic...
            if (propertyNameList.contains("alogp")) {
                if (pchemVO.getTheALogP() != null) {
                    if (!Float.isNaN(pchemVO.getTheALogP().floatValue())) {
                        ahMap.get("alogp").addValue(pchemVO.getTheALogP().floatValue());
                    }
                }
            }

            if (propertyNameList.contains("logd")) {
                if (pchemVO.getLogD() != null) {
                    if (!Float.isNaN(pchemVO.getLogD().floatValue())) {
                        ahMap.get("logd").addValue(pchemVO.getLogD().floatValue());
                    }
                }
            }

            if (propertyNameList.contains("hba")) {
                if (pchemVO.getCountHydBondAcceptors() != null) {
                    if (!Float.isNaN(pchemVO.getCountHydBondAcceptors())) {
                        ahMap.get("hba").addValue(pchemVO.getCountHydBondAcceptors());
                    }
                }
            }

            if (propertyNameList.contains("hbd")) {
                if (pchemVO.getCountHydBondDonors() != null) {
                    if (!Float.isNaN(pchemVO.getCountHydBondDonors())) {
                        ahMap.get("hbd").addValue(pchemVO.getCountHydBondDonors());
                    }
                }
            }

            if (propertyNameList.contains("sa")) {
                if (pchemVO.getSurfaceArea() != null) {
                    if (!Float.isNaN(pchemVO.getSurfaceArea().floatValue())) {
                        ahMap.get("sa").addValue(pchemVO.getSurfaceArea().floatValue());
                    }
                }
            }

            if (propertyNameList.contains("mw")) {
                if (pchemVO.getMolecularWeight() != null) {
                    if (!Float.isNaN(pchemVO.getMolecularWeight().floatValue())) {
                        ahMap.get("mw").addValue(pchemVO.getMolecularWeight().floatValue());
                    }
                }
            }

        }

        for (String key : ahMap.keySet()) {
            String title = key;
            Histogram h = new Histogram(title, key, incoming, ahMap.get(key));
            rtn.add(h);
        }

        return rtn;

    }
}
