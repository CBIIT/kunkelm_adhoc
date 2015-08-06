/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.main;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 *
 * @author mwkunkel
 */
public class TemplPropUtil<T> {

    public static final Boolean DEBUG = Boolean.FALSE;

    public ArrayList<String> knownStringProperties;
    public ArrayList<String> knownIntegerProperties;
    public ArrayList<String> knownDoubleProperties;
    public ArrayList<String> knownLongProperties;
    public ArrayList<String> knownBooleanProperties;
    public ArrayList<String> unmanagedProperties;
    public T t;

    public TemplPropUtil(T tIn) {

        System.out.println("In TemplPropUtil constructor");

        t = tIn;

        knownStringProperties = new ArrayList<String>();
        knownIntegerProperties = new ArrayList<String>();
        knownDoubleProperties = new ArrayList<String>();
        knownLongProperties = new ArrayList<String>();
        knownBooleanProperties = new ArrayList<String>();
        unmanagedProperties = new ArrayList<String>();

        props(t.getClass(), "");

        if (DEBUG) {

            System.out.println("");
            System.out.println("knownStringProperties");
            for (String s : knownStringProperties) {
                System.out.println(s);
            }

            System.out.println("");
            System.out.println("knownIntegerProperties");
            for (String s : knownIntegerProperties) {
                System.out.println(s);
            }

            System.out.println("");
            System.out.println("knownDoubleProperties");
            for (String s : knownDoubleProperties) {
                System.out.println(s);
            }

            System.out.println("");
            System.out.println("knownLongProperties");
            for (String s : knownLongProperties) {
                System.out.println(s);
            }

            System.out.println("");
            System.out.println("knownBooleanProperties");
            for (String s : knownBooleanProperties) {
                System.out.println(s);
            }

            System.out.println("");
            System.out.println("unmanagedProperties");
            for (String s : unmanagedProperties) {
                System.out.println(s);
            }

        }
    }

    public void props(Class<?> c, String cumulative) {

        System.out.println("c.class is: " + c.getName());

        Field[] fArr = c.getDeclaredFields();

        List<Field> fList = Arrays.asList(fArr);

        for (Field f : fList) {

            f.setAccessible(true);

            if (DEBUG) {
                System.out.println(f.getName() + " " + f.getType());
            }

            String cumStr = cumulative.isEmpty() ? f.getName() : cumulative + "." + f.getName();

            if (f.getType().equals(String.class)) {
                knownStringProperties.add(cumStr);
            } else if (f.getType().equals(Integer.class) || f.getType().equals(int.class)) {
                knownIntegerProperties.add(cumStr);
            } else if (f.getType().equals(Double.class) || f.getType().equals(double.class)) {
                knownDoubleProperties.add(cumStr);
            } else if (f.getType().equals(Long.class) || f.getType().equals(long.class)) {
                knownLongProperties.add(cumStr);
            } else if (f.getType().equals(Boolean.class) || f.getType().equals(boolean.class)) {
                knownLongProperties.add(cumStr);
            } else if (Collection.class.isAssignableFrom(f.getType())) {
                System.out.println("cumulative: " + cumulative + " is Collection: " + f.getType());
            } else {

                System.out.println("Calling props with f.getType(): " + f.getType() + " and cumulative: " + cumStr);
                props(f.getType(), cumStr);

                //unmanagedProperties.add("fieldName: " + f.getName() + " " + f.getType());
            }

        }

    }

    public Object get(Object objIn, String propertyName) {

        Object rtn = null;

        String[] strArr = propertyName.split("\\.");
        ArrayList<String> fieldNameList = new ArrayList<String>(Arrays.asList(strArr));

        if (DEBUG) {
            System.out.println("TempPropUtil DEBUG In get.  objIn is: " + objIn.getClass().getName());
            System.out.println("TempPropUtil DEBUG fieldNameList: size: " + fieldNameList.size() + " toString(): " + fieldNameList.toString());
        }

        rtn = recursiveGet(objIn, fieldNameList);

        return rtn;

    }

    public Object recursiveGet(Object objIn, ArrayList<String> fieldNameList) {

        if (DEBUG) {
            System.out.println("TempPropUtil DEBUG In recursiveGet.  objIn is: " + objIn.getClass().getName());
            System.out.println("TempPropUtil DEBUG fieldNameList: size: " + fieldNameList.size() + " toString(): " + fieldNameList.toString());
        }

        Object rtn = null;

        try {

            String fieldName = fieldNameList.get(0);

            if (DEBUG) {
                System.out.println("TempPropUtil DEBUG fieldName: " + fieldName);
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
            System.out.println("NullPointerException in recursiveGet in TempPropUtil");
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

    public Boolean knownStringProperty(String propertyName) {
        return knownStringProperties.contains(propertyName);
    }

    public Boolean knownIntegerProperty(String propertyName) {
        return knownIntegerProperties.contains(propertyName);
    }

    public Boolean knownDoubleProperty(String propertyName) {
        return knownDoubleProperties.contains(propertyName);
    }

    public String getStringProperty(T tIn, String propertyName) {

        return (String) get(tIn, propertyName);

    }

    public Integer getIntegerProperty(T tIn, String propertyName) {

        return (Integer) get(tIn, propertyName);
        
    }

    public Double getDoubleProperty(T tIn, String propertyName) {

        return (Double) get(tIn, propertyName);
    }

}
