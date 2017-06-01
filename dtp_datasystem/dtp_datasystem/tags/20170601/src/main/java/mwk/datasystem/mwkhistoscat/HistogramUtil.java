/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkhistoscat;

import com.flaptor.hist4j.mwkdbl.AdaptiveHistogram;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.List;
import mwk.datasystem.vo.CmpdFragmentPChemVO;

/**
 *
 * @author mwkunkel
 */
public class HistogramUtil<X> {

    public final Boolean DEBUG = Boolean.FALSE;

    private static void printFieldsAndMethods(Class c) {

        System.out.println("Fields from : " + c);
        Field[] fields = c.getDeclaredFields();
        if (fields.length != 0) {
            for (Field f : fields) {
                System.out.println("  Field: " + f.toGenericString());
            }
        } else {
            System.out.println("  -- no fields --");
        }
        System.out.println();

        System.out.println("Methods from : " + c);
        Method[] meths = c.getDeclaredMethods();
        if (meths.length != 0) {
            for (Method m : meths) {
                System.out.println("  Method: " + m.toGenericString());
            }
        } else {
            System.out.println("  -- no methods --");
        }
        System.out.println();

    }

    public static Boolean getIsSelected(Object o) {

        Boolean rtn = Boolean.FALSE;

        try {

            Class clz = o.getClass();

//            printFieldsAndMethods(clz);
//
//            Class parent = clz.getSuperclass();
//            while (parent != null) {
//                printFieldsAndMethods(parent);
//                parent = parent.getSuperclass();
//            }
            Field f = clz.getDeclaredField("isSelected");
            f.setAccessible(true);

            Object rtnObj = f.get(o);
            rtn = (Boolean) rtnObj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    public static Double getDoubleProp(Object o, String propertyName) {

        Double rtn = null;

        try {

            Class clz = o.getClass();

//            printFieldsAndMethods(clz);
//
//            Class parent = clz.getSuperclass();
//            while (parent != null) {
//                printFieldsAndMethods(parent);
//                parent = parent.getSuperclass();
//            }
            Field f = clz.getDeclaredField(propertyName);
            f.setAccessible(true);

            Object rtnObj = f.get(o);
            rtn = (Double) rtnObj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    public List<Histogram<X>> doHistograms(Collection<X> xList, List<String> propertyNameList) {

        if (DEBUG) {
            System.out.println("In TemplatedHistogram<X>ChartUtil.doHistograms()");
            System.out.println("Size of incomding: " + xList.size());
        }

        ArrayList<Histogram<X>> rtn = new ArrayList<Histogram<X>>();

        TreeMap<String, AdaptiveHistogram> ahMap = new TreeMap<String, AdaptiveHistogram>();

        // initialize the ah map
        for (String propertyName : propertyNameList) {
            ahMap.put(propertyName, new AdaptiveHistogram());
        }

        for (X x : xList) {

            for (String propName : propertyNameList) {
                if (!Double.isNaN(getDoubleProp(x, propName))) {
                    ahMap.get(propName).addValue(getDoubleProp(x, propName));
                }
            }

        }

        for (String key : ahMap.keySet()) {
            String title = key;
            Histogram<X> h = new Histogram<X>(title, key, xList, ahMap.get(key));
            rtn.add(h);
        }

        return rtn;

    }

}
