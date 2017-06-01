/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkhistoscat;

import com.flaptor.hist4j.mwkdbl.AdaptiveHistogram;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.TreeMap;
import java.util.List;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author mwkunkel
 */
public class ScatterPlotUtil<X> {

    public final Boolean DEBUG = Boolean.FALSE;

    private static NumberFormat intf = new DecimalFormat();

    private static NumberFormat nf2 = new DecimalFormat();

    static {
        intf.setParseIntegerOnly(true);
        nf2.setMaximumFractionDigits(2);
    }

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

}
