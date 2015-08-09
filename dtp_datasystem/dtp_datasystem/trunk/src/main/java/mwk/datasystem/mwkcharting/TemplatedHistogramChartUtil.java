/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkcharting;

import com.flaptor.hist4j.mwkdbl.AdaptiveHistogram;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import mwk.datasystem.mwkcharting.TemplPropUtil;
import mwk.datasystem.mwkcharting.TemplatedHistogram;
import mwk.datasystem.vo.HistogramDataInterface;

/**
 *
 * @author mwkunkel
 */
public class TemplatedHistogramChartUtil<T extends HistogramDataInterface> {

    public static final Boolean DEBUG = Boolean.TRUE;

    public List<TemplatedHistogram<T>> doHistograms(
            Collection<T> incoming,
            List<String> propertyNameList,
            TemplPropUtil<T> propUtil) {

        if (DEBUG) {
            System.out.println("In HistogramChartUtil.doHistograms()");
            System.out.println("Size of incomding: " + incoming.size());
        }

        ArrayList<TemplatedHistogram<T>> rtn = new ArrayList<TemplatedHistogram<T>>();

        // Histograms looked up by property
        HashMap<String, AdaptiveHistogram> ahMap = new HashMap<String, AdaptiveHistogram>();

        // initialize the ah map
        for (String propertyName : propertyNameList) {
            ahMap.put(propertyName, new AdaptiveHistogram());
        }

        for (T t : incoming) {
            for (String propertyName : propertyNameList) {

                Double thisDbl = null;

                if (propUtil.isIntProp(propertyName)) {
                    if (propUtil.getInt(t, propertyName) != null) {
                        thisDbl = propUtil.getInt(t, propertyName).doubleValue();
                    }
                } else if (propUtil.isDblProp(propertyName)) {
                    thisDbl = propUtil.getDbl(t, propertyName);
                } else {
                    System.out.println(propertyName + " is not an intProp or a dblProp in doHistograms in TemplatedHistogramChartUtil.");
                }

                if (thisDbl != null) {
                    ahMap.get(propertyName).addValue(thisDbl);
                } else {
                    System.out.println(propertyName + " is null in doHistograms in TemplatedHistogramChartUtil.");
                }

            }
        }

        // MWK 07Dec2014 adding sort step
        ArrayList<String> sortList = new ArrayList<String>(ahMap.keySet());
        Collections.sort(sortList);

        for (String key : sortList) {
            String title = key;
            TemplatedHistogram<T> h = new TemplatedHistogram<T>(title, key, incoming, ahMap.get(key), propUtil);
            rtn.add(h);
        }

        return rtn;

    }

}
