/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import com.flaptor.hist4j.AdaptiveHistogram;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import mwk.datasystem.mwkcharting.Histogram;
import mwk.datasystem.mwkcharting.PropertyUtilities;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;

/**
 *
 * @author mwkunkel
 */
public class HistogramChartUtil {

    public static final Boolean DEBUG = Boolean.TRUE;

    public static List<Histogram> doHistograms(Collection<CmpdListMemberVO> incoming, List<String> propertyNameList) {

        if (DEBUG) {
            System.out.println("In HistogramChartUtil.doHistograms()");
            System.out.println("Size of incomding: " + incoming.size());
        }

        ArrayList<Histogram> rtn = new ArrayList<Histogram>();

        // Histograms looked up by property
        HashMap<String, AdaptiveHistogram> ahMap = new HashMap<String, AdaptiveHistogram>();

        // initialize the ah map
        for (String propertyName : propertyNameList) {
            ahMap.put(propertyName, new AdaptiveHistogram());
        }

        for (CmpdListMemberVO clmVO : incoming) {

            // these are hard-coded since reflection is problematic...
            if (propertyNameList.contains("alogp")) {
                if (!Double.isNaN(PropertyUtilities.getPropertyAsFloat(clmVO, "alogp"))) {
                    ahMap.get("alogp").addValue(PropertyUtilities.getPropertyAsFloat(clmVO, "alogp"));
                }
            }

            if (propertyNameList.contains("logd")) {
                if (!Double.isNaN(PropertyUtilities.getPropertyAsFloat(clmVO, "logd"))) {
                    ahMap.get("logd").addValue(PropertyUtilities.getPropertyAsFloat(clmVO, "logd"));
                }
            }

            if (propertyNameList.contains("hba")) {
                if (!Double.isNaN(PropertyUtilities.getPropertyAsFloat(clmVO, "hba"))) {
                    ahMap.get("hba").addValue(PropertyUtilities.getPropertyAsFloat(clmVO, "hba"));
                }
            }

            if (propertyNameList.contains("hbd")) {
                if (!Double.isNaN(PropertyUtilities.getPropertyAsFloat(clmVO, "hbd"))) {
                    ahMap.get("hbd").addValue(PropertyUtilities.getPropertyAsFloat(clmVO, "hbd"));
                }
            }

            if (propertyNameList.contains("sa")) {
                if (!Double.isNaN(PropertyUtilities.getPropertyAsFloat(clmVO, "sa"))) {
                    ahMap.get("sa").addValue(PropertyUtilities.getPropertyAsFloat(clmVO, "sa"));
                }
            }

            if (propertyNameList.contains("mw")) {
                if (!Double.isNaN(PropertyUtilities.getPropertyAsFloat(clmVO, "mw"))) {
                    ahMap.get("mw").addValue(PropertyUtilities.getPropertyAsFloat(clmVO, "mw"));
                }
            }

        }

    // MWK 07Dec2014 adding sort step
        ArrayList<String> sortList = new ArrayList<String>(ahMap.keySet());
        Collections.sort(sortList);

        for (String key : sortList) {
            String title = key;
            Histogram h = new Histogram(title, key, incoming, ahMap.get(key));
            rtn.add(h);
        }

        return rtn;

    }

}
