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
import mwk.datasystem.mwkcharting.TemplPropUtil;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;

/**
 *
 * @author mwkunkel
 */
public class HistogramChartUtil {

    public static final Boolean DEBUG = Boolean.TRUE;

    public static List<Histogram> doHistograms(Collection<CmpdListMemberVO> incoming, List<String> propertyNameList) {

        TemplPropUtil<CmpdListMemberVO> propUtils = new TemplPropUtil<CmpdListMemberVO>(new CmpdListMemberVO());

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
            for (String propertyName : propertyNameList) {
                if (propUtils.isDblProp(propertyName)) {
                    if (propUtils.get(clmVO, propertyName) != null) {
                        ahMap.get(propertyName).addValue(propUtils.getDbl(clmVO, propertyName));
                    }
                } else if (propUtils.isIntProp(propertyName)) {
                    if (propUtils.get(clmVO, propertyName) != null) {
                        ahMap.get(propertyName).addValue(propUtils.getInt(clmVO, propertyName));
                    }
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
