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
import mwk.datasystem.mwkcharting.TemplPropUtil;
import mwk.datasystem.mwkcharting.TemplatedHistogram;
import mwk.datasystem.vo.CmpdListMemberVO;

/**
 *
 * @author mwkunkel
 */
public class TemplatedHistogramChartUtil {

    public static final Boolean DEBUG = Boolean.TRUE;

    public static List<TemplatedHistogram<CmpdListMemberVO>> doHistograms(Collection<CmpdListMemberVO> incoming, List<String> propertyNameList) {

        TemplPropUtil<CmpdListMemberVO> propUtils = new TemplPropUtil<CmpdListMemberVO>(new CmpdListMemberVO());

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
            for (String propertyName : propertyNameList) {
                if (propUtils.knownDoubleProperty(propertyName)) {
                    if (propUtils.get(clmVO, propertyName) != null) {
                        ahMap.get(propertyName).addValue(propUtils.getDoubleProperty(clmVO, propertyName));
                    }
                } else if (propUtils.knownIntegerProperty(propertyName)) {
                    if (propUtils.get(clmVO, propertyName) != null) {
                        ahMap.get(propertyName).addValue(propUtils.getIntegerProperty(clmVO, propertyName));
                    }
                }
            }
        }

        // MWK 07Dec2014 adding sort step
        ArrayList<String> sortList = new ArrayList<String>(ahMap.keySet());
        Collections.sort(sortList);

        for (String key : sortList) {
            String title = key;
            TemplatedHistogram<CmpdListMemberVO> h = new TemplatedHistogram<CmpdListMemberVO>(title, key, incoming, ahMap.get(key));
            rtn.add(h);
        }

        return rtn;

    }

}
