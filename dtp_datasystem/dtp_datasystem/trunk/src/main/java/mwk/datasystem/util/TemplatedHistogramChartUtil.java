/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import com.flaptor.hist4j.mwkdbl.AdaptiveHistogram;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import mwk.datasystem.mwkcharting.Histogram;
import mwk.datasystem.mwkcharting.PropertyUtilities;
import mwk.datasystem.mwkcharting.TemplatedHistogram;
import mwk.datasystem.vo.CmpdListMemberVO;

/**
 *
 * @author mwkunkel
 */
public class TemplatedHistogramChartUtil {

    public static final Boolean DEBUG = Boolean.TRUE;

    public static List<TemplatedHistogram<CmpdListMemberVO>> doHistograms(Collection<CmpdListMemberVO> incoming, List<String> propertyNameList) {

        if (DEBUG) {
            System.out.println("In HistogramChartUtil.doHistograms()");
            System.out.println("Size of incomding: " + incoming.size());
        }

        ArrayList<TemplatedHistogram<CmpdListMemberVO>> rtn = new ArrayList<TemplatedHistogram<CmpdListMemberVO>>();

        // Histograms looked up by property
        HashMap<String, AdaptiveHistogram> ahMap = new HashMap<String, AdaptiveHistogram>();

        // initialize the ah map
        for (String propertyName : propertyNameList) {
            ahMap.put(propertyName, new AdaptiveHistogram());
        }

        for (CmpdListMemberVO clmVO : incoming) {

            // these are hard-coded since reflection is problematic...
            if (propertyNameList.contains("alogp")) {
                if (PropertyUtilities.getDoubleProperty(clmVO, "alogp") != null) {
                    ahMap.get("alogp").addValue(PropertyUtilities.getDoubleProperty(clmVO, "alogp"));
                }
            }

            if (propertyNameList.contains("logd")) {
                if (PropertyUtilities.getDoubleProperty(clmVO, "logd") != null) {
                    ahMap.get("logd").addValue(PropertyUtilities.getDoubleProperty(clmVO, "logd"));
                }
            }

            if (propertyNameList.contains("hba")) {
                if (PropertyUtilities.getIntegerProperty(clmVO, "hba")!=null) {
                    ahMap.get("hba").addValue(PropertyUtilities.getIntegerProperty(clmVO, "hba"));
                }
            }

            if (propertyNameList.contains("hbd")) {
                if (PropertyUtilities.getIntegerProperty(clmVO, "hbd")!= null) {
                    ahMap.get("hbd").addValue(PropertyUtilities.getIntegerProperty(clmVO, "hbd"));
                }
            }

            if (propertyNameList.contains("sa")) {
                if (PropertyUtilities.getDoubleProperty(clmVO, "sa") != null) {
                    ahMap.get("sa").addValue(PropertyUtilities.getDoubleProperty(clmVO, "sa"));
                }
            }

            if (propertyNameList.contains("mw")) {
                if (PropertyUtilities.getDoubleProperty(clmVO, "mw")!= null) {
                    ahMap.get("mw").addValue(PropertyUtilities.getDoubleProperty(clmVO, "mw"));
                }
            }

        }

    // MWK 07Dec2014 adding sort step
        ArrayList<String> sortList = new ArrayList<String>(ahMap.keySet());
        Collections.sort(sortList);

        for (String key : sortList) {
            String title = key;
            TemplatedHistogram<CmpdListMemberVO> h = new TemplatedHistogram<CmpdListMemberVO> (title, key, incoming, ahMap.get(key));
            rtn.add(h);
        }

        return rtn;

    }

}
