/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.TreeMap;

/**
 *
 * @author mwkunkel
 * @param <X> first dimension header object
 * @param <Y> second dimension header object
 * @param <V> values to be crosstabulated
 */
public class TemplatedGrid<X, Y, V> implements Serializable {

    private static final long serialVersionUID = 3499434040403497809L;

    private ArrayList<X> xList;
    private Comparator<X> xComp;
    private TreeMap<Long, X> xLookup = new TreeMap<Long, X>();

    private ArrayList<Y> yList;
    private Comparator<Y> yComp;
    private TreeMap<Long, Y> yLookup = new TreeMap<Long, Y>();

    private ArrayList<ArrayList<V>> grid;

    public TemplatedGrid(ArrayList<X> xListIn, Comparator<X> xCompIn, ArrayList<Y> yListIn, Comparator<Y> yCompIn) {

        xList = xListIn;
        xComp = xCompIn;
        xLookup = new TreeMap<Long, X>();

        yList = yListIn;
        yComp = yCompIn;
        yLookup = new TreeMap<Long, Y>();

        Collections.sort(xList, xComp);
        Collections.sort(yList, yComp);

        for (X x : xList) {
            Long xId = idGet(x);
            xLookup.put(xId, x);
        }

        for (Y y : yList) {
            Long yId = idGet(y);
            yLookup.put(yId, y);
        }

        grid = new ArrayList<ArrayList<V>>(xList.size());

        // init to null!
        for (int i = 0; i < xList.size(); i++) {
            ArrayList<V> vObjList = new ArrayList<V>(yList.size());
            for (int j = 0; j < yList.size(); j++) {
                vObjList.add(null);
            }
            grid.add(vObjList);
        }
    }

    public void printStats() {
        System.out.println();
        System.out.println("TemplatedGrid.printStats:");
        System.out.println("xList.size(): " + xList.size());
        System.out.println("yList.size(): " + yList.size());
        System.out.println();
    }

//    public TemplatedGrid() {
//
//        xList = new ArrayList<X>();
//        xComp = null;
//        xLookup = new TreeMap<Long, X>();
//
//        yList = new ArrayList<Y>();
//        yComp = null;
//        yLookup = new TreeMap<Long, Y>();
//
//        grid = new ArrayList<ArrayList<V>>();
//
//    }
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

    public Long idGet(Object o) {

        Long rtn = null;

        try {

            Class clz = o.getClass();

//            printFieldsAndMethods(clz);
//
//            Class parent = clz.getSuperclass();
//            while (parent != null) {
//                printFieldsAndMethods(parent);
//                parent = parent.getSuperclass();
//            }
            Field f = clz.getDeclaredField("id");
            f.setAccessible(true);

            Object rtnObj = f.get(o);
            rtn = (Long) rtnObj;

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    public ArrayList<X> getXlistByXIdList(ArrayList<Long> xIdList) {

        ArrayList<X> rtnList = new ArrayList<X>();

        for (Long l : xIdList) {
            X x = getXbyXid(l);
            if (x != null) {
                rtnList.add(x);
            }
        }

        Collections.sort(rtnList, xComp);

        return rtnList;
    }

    public X getXbyXid(Long xId) {
        return xLookup.get(xId);
    }

    public boolean setById(Long xId, Long yId, V vObjArg) {

        boolean rtn = false;

        X xObj = xLookup.get(xId);
        Y yObj = yLookup.get(yId);

        return set(xObj, yObj, vObjArg);

    }

    public boolean set(X xObjArg, Y yObjArg, V vObjArg) {

        boolean rtn = false;

        int xObjIdx = Collections.binarySearch(xList, xObjArg, xComp);
        int yObjIdx = Collections.binarySearch(yList, yObjArg, yComp);

        if (xObjIdx >= 0 && yObjIdx >= 0) {
            rtn = true;
            grid.get(xObjIdx).set(yObjIdx, vObjArg);
        }

        return rtn;

    }

    public V get(X xObjArg, Y yObjArg) {

        V rtn = null;

        int xObjIdx = Collections.binarySearch(xList, xObjArg, xComp);
        int yObjIdx = Collections.binarySearch(yList, yObjArg, yComp);

        if (xObjIdx >= 0 && yObjIdx >= 0) {
            rtn = grid.get(xObjIdx).get(yObjIdx);
        }

        return rtn;

    }

    public ArrayList<V> getAllVByX(X xObjArg) {

        ArrayList<V> rtnArrayList = null;

        int xObjIdx = Collections.binarySearch(xList, xObjArg, xComp);

        if (xObjIdx >= 0) {
            rtnArrayList = new ArrayList<V>(grid.get(xObjIdx));
        }

        return rtnArrayList;

    }

    public ArrayList<V> getAllVByY(Y yObjArg) {

        ArrayList<V> rtnArrayList = null;

        int yObjIdx = Collections.binarySearch(yList, yObjArg, yComp);

        if (yObjIdx >= 0) {
            rtnArrayList = new ArrayList<V>();
            for (int xObjIdx = 0; xObjIdx < xList.size(); xObjIdx++) {
                rtnArrayList.add(grid.get(xObjIdx).get(yObjIdx));
            }
        }

        return rtnArrayList;

    }

    public boolean exists(X xObjArg, Y yObjArg) {

        boolean rtn = false;

        int xObjIdx = Collections.binarySearch(xList, xObjArg, xComp);
        int yObjIdx = Collections.binarySearch(yList, yObjArg, yComp);

        if (xObjIdx >= 0 && yObjIdx >= 0) {
            rtn = true;
        }

        return rtn;

    }

    //<editor-fold defaultstate="collapsed" desc="GETTER/SETTER">
    public ArrayList<X> getxList() {
        return xList;
    }

    public ArrayList<Y> getyList() {
        return yList;
    }

    public ArrayList<ArrayList<V>> getGrid() {
        return grid;
    }

//</editor-fold>
}
