/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.sarcoma.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 *
 * @author mwkunkel
 * @param <X> first dimension header object
 * @param <Y> second dimension header object
 * @param <V> values to be crosstabulated
 */
public class TwoDHashMap<X, Y, V> {

    private final HashMap<X, HashMap<Y, V>> mMap;
    private final HashSet<X> xKeys;
    private final HashSet<Y> yKeys;

    /**
     *
     */
    public TwoDHashMap() {
        mMap = new HashMap<X, HashMap<Y, V>>();
        xKeys = new HashSet<X>();
        yKeys = new HashSet<Y>();
    }

    public ArrayList<X> getXkeys() {
        ArrayList<X> rtn = new ArrayList<X>();
        rtn.addAll(xKeys);
        return rtn;
    }

    public ArrayList<Y> getYkeys() {
        ArrayList<Y> rtn = new ArrayList<Y>();
        rtn.addAll(yKeys);
        return rtn;
    }

    public V put(X x, Y y, V value) {

        xKeys.add(x);
        yKeys.add(y);

        HashMap<Y, V> map;

        if (mMap.containsKey(x)) {

            map = mMap.get(x);

            if (map.containsKey(y)) {
                // this is a duplicate!
            }

        } else {

            map = new HashMap<Y, V>();

            mMap.put(x, map);
        }

        return map.put(y, value);
    }

    public V get(X x, Y y) {
        if (mMap.containsKey(x)) {
            return mMap.get(x).get(y);
        } else {
            return null;
        }
    }

    public boolean containsKeys(X x, Y y) {
        return mMap.containsKey(x) && mMap.get(x).containsKey(y);
    }

    /**
     *
     */
    public void clear() {
        mMap.clear();
    }
}
