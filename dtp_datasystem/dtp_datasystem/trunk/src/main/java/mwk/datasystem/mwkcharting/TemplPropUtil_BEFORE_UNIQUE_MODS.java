/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkcharting;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author mwkunkel
 */
public class TemplPropUtil_BEFORE_UNIQUE_MODS<T> {

    public static final Boolean DEBUG = Boolean.FALSE;

    public Map<String, TemplatedPair<String, String>> shortNameLookup;

    public ArrayList<String> strProps;
    public ArrayList<String> intProps;
    public ArrayList<String> dblProps;
    public ArrayList<String> longProps;
    public ArrayList<String> boolProps;
    public ArrayList<String> othProps;
    private T t;

    public TemplPropUtil_BEFORE_UNIQUE_MODS(T tIn) {

        System.out.println("In TemplPropUtil constructor");

        t = tIn;

        shortNameLookup = new HashMap<String, TemplatedPair<String, String>>();

        strProps = new ArrayList<String>();
        intProps = new ArrayList<String>();
        dblProps = new ArrayList<String>();
        longProps = new ArrayList<String>();
        boolProps = new ArrayList<String>();
        othProps = new ArrayList<String>();

        props(t.getClass(), "");

        if (DEBUG) {

            System.out.println();;
            System.out.println("strProps");
            for (String s : strProps) {
                System.out.println(s);
            }

            System.out.println();;
            System.out.println("intProps");
            for (String s : intProps) {
                System.out.println(s);
            }

            System.out.println();;
            System.out.println("dblProps");
            for (String s : dblProps) {
                System.out.println(s);
            }

            System.out.println();;
            System.out.println("longProps");
            for (String s : longProps) {
                System.out.println(s);
            }

            System.out.println();;
            System.out.println("boolProps");
            for (String s : boolProps) {
                System.out.println(s);
            }

            System.out.println();;
            System.out.println("othProps");
            for (String s : othProps) {
                System.out.println(s);
            }

        }
    }

    private void props(Class<?> c, String cumulative) {

        if (DEBUG) {
            System.out.println("c.class is: " + c.getName());
        }

        Field[] fArr = c.getDeclaredFields();

        List<Field> fList = Arrays.asList(fArr);

        for (Field f : fList) {

            f.setAccessible(true);

            if (DEBUG) {
                System.out.println(f.getName() + " " + f.getType());
            }

            String cumStr = cumulative.isEmpty() ? f.getName() : cumulative + "." + f.getName();

            if (f.getType().equals(String.class)) {
                strProps.add(cumStr);
                shortNameLookup.put(f.getName(), TemplatedPair.of(cumStr, f.getType().getName()));
            } else if (f.getType().equals(Integer.class) || f.getType().equals(int.class)) {
                intProps.add(cumStr);
                shortNameLookup.put(f.getName(), TemplatedPair.of(cumStr, f.getType().getName()));
            } else if (f.getType().equals(Double.class) || f.getType().equals(double.class)) {
                dblProps.add(cumStr);
                shortNameLookup.put(f.getName(), TemplatedPair.of(cumStr, f.getType().getName()));
            } else if (f.getType().equals(Long.class) || f.getType().equals(long.class)) {
                longProps.add(cumStr);
                shortNameLookup.put(f.getName(), TemplatedPair.of(cumStr, f.getType().getName()));
            } else if (f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)) {
                longProps.add(cumStr);
                shortNameLookup.put(f.getName(), TemplatedPair.of(cumStr, f.getType().getName()));
            } else if (Collection.class.isAssignableFrom(f.getType())) {
                System.out.println(f.getName() + " is Collection: " + f.getType());
            } else {

                if (DEBUG) {
                    System.out.println("Calling props with f.getType(): " + f.getType() + " and cumulative: " + cumStr);
                }
                props(f.getType(), cumStr);

            }

        }

    }

    public Object get(Object objIn, String propertyName) {

        Object rtn = null;

        String[] strArr = propertyName.split("\\.");
        ArrayList<String> fieldNameList = new ArrayList<String>(Arrays.asList(strArr));

        if (DEBUG) {
            System.out.println("TemplPropUtil DEBUG In get.  objIn is: " + objIn.getClass().getName());
            System.out.println("TemplPropUtil DEBUG fieldNameList: size: " + fieldNameList.size() + " toString(): " + fieldNameList.toString());
        }

        rtn = recursiveGet(objIn, fieldNameList);

        return rtn;

    }

    private Object recursiveGet(Object objIn, ArrayList<String> fieldNameList) {

        if (DEBUG) {
            System.out.println("TemplPropUtil DEBUG In recursiveGet.  objIn is: " + objIn.getClass().getName());
            System.out.println("TemplPropUtil DEBUG fieldNameList: size: " + fieldNameList.size() + " toString(): " + fieldNameList.toString());
        }

        Object rtn = null;

        try {

            String fieldName = fieldNameList.get(0);

            if (DEBUG) {
                System.out.println("TemplPropUtil DEBUG fieldName: " + fieldName);
            }

            Field f = objIn.getClass().getDeclaredField(fieldName);
            f.setAccessible(true);

            // if this is the only fieldName, then return it
            // else recurse
            if (fieldNameList.size() == 1) {
                rtn = f.get(objIn);
            } else if (f.get(objIn) == null) {
                // fall through, will return null;
            } else {
                fieldNameList.remove(0);
                rtn = recursiveGet(f.get(objIn), fieldNameList);
            }

        } catch (NullPointerException e) {
            System.out.println("NullPointerException in recursiveGet in TemplPropUtil");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    public Boolean isStrProp(String propertyName) {
        return strProps.contains(propertyName);
    }

    public Boolean isIntProp(String propertyName) {
        return intProps.contains(propertyName);
    }

    public Boolean isDblProp(String propertyName) {
        return dblProps.contains(propertyName);
    }

    public Boolean isLongProp(String propertyName) {
        return longProps.contains(propertyName);
    }

    public Boolean isBoolProp(String propertyName) {
        return boolProps.contains(propertyName);
    }

    public String getStr(T tIn, String propertyName) {
        return (String) get(tIn, propertyName);
    }

    public Integer getInt(T tIn, String propertyName) {
        return (Integer) get(tIn, propertyName);
    }

    public Double getDbl(T tIn, String propertyName) {
        return (Double) get(tIn, propertyName);
    }

    public Long getLong(T tIn, String propertyName) {
        return (Long) get(tIn, propertyName);
    }

    public Boolean getBool(T tIn, String propertyName) {
        return (Boolean) get(tIn, propertyName);
    }

}
