package mwk.datasystem.util;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.exception.ExceptionUtils;

/**
 *
 * @author mwkunkel
 */
public class TemplPropUtil<T> {

  public static final Boolean DEBUG = Boolean.FALSE;
  public static final Boolean SUMMARIZE = Boolean.TRUE;

  // processing
  private ArrayList<StringTriplet> rawProps;
  private HashMap<String, ArrayList<StringPair>> collatedProps;
  private HashMap<String, ArrayList<StringPair>> dupProps;
  private HashMap<String, ArrayList<StringPair>> successDupProps;

  // the final product(s)
  private HashMap<String, StringPair> uniqProps;

  private ArrayList<String> strProps;
  private ArrayList<String> intProps;
  private ArrayList<String> dblProps;
  private ArrayList<String> longProps;
  private ArrayList<String> boolProps;
  private ArrayList<String> othProps;

  // incoming
  private T t;
  ArrayList<String> ignList;
  ArrayList<String> reqList;

  private String join(String sep, String[] strArr) {

    StringBuilder sb = new StringBuilder();
    boolean isFirst = true;

    for (String s : strArr) {
      if (isFirst) {
        sb.append(s);
        isFirst = false;
      } else {
        sb.append(sep);
        sb.append(s);
      }
    }

    return sb.toString();
  }

  public void printUniqProps() {
    for (String k : this.uniqProps.keySet()) {
      System.out.printf("%30s%90s%30s%n", k, this.uniqProps.get(k).fullName, this.uniqProps.get(k).dataType);
    }
  }

  private void printProps(String when) {
    System.out.println();
    System.out.println(when + "----------");
    System.out.printf("%30s%30s%30s%n", when, "rawProps", rawProps.size());
    System.out.printf("%30s%30s%30s%n", when, "collatedProps", collatedProps.size());
    System.out.printf("%30s%30s%30s%n", when, "dupProps", dupProps.size());
    System.out.printf("%30s%30s%30s%n", when, "successDupProps", successDupProps.size());
    System.out.printf("%30s%30s%30s%n", when, "uniqProps", uniqProps.size());
    System.out.println();
  }

  public TemplPropUtil(
          T tIn,
          ArrayList<String> ignListIn,
          ArrayList<String> reqListIn) {

    SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");;
    System.out.println("In TemplPropUtil constructor. System.time: " + sdf.format(Calendar.getInstance().getTime()));

    if (tIn == null) {
      System.out.println("T tIn is null.");
    } else {
      System.out.println("T tIn.class is: " + tIn.getClass().getName());
    }

    System.out.println(ExceptionUtils.getFullStackTrace(new Throwable()));

    t = tIn;
    if (ignListIn
            != null) {
      this.ignList = ignListIn;
    } else {
      this.ignList = new ArrayList<String>();
    }

    if (reqListIn
            != null) {
      this.reqList = reqListIn;
    } else {
      this.reqList = new ArrayList<String>();
    }

    rawProps = new ArrayList<StringTriplet>();
    collatedProps = new HashMap<String, ArrayList<StringPair>>();
    dupProps = new HashMap<String, ArrayList<StringPair>>();
    successDupProps = new HashMap<String, ArrayList<StringPair>>();
    uniqProps = new HashMap<String, StringPair>();

    // these should (could?) be done later AFTER resolution of dups
    strProps = new ArrayList<String>();
    intProps = new ArrayList<String>();
    dblProps = new ArrayList<String>();
    longProps = new ArrayList<String>();
    boolProps = new ArrayList<String>();
    othProps = new ArrayList<String>();

        // determine the fully-qualified props
    // props will call against the ignList reqList
    classWalker(t.getClass(), "");

        // collate to collatedProps by the last short name
    // this is where filtering is applied
    for (StringTriplet trip : rawProps) {

      String[] strArr = trip.fullName.split("\\.");
      List<String> strList = Arrays.asList(strArr);

      boolean ok = true;

      if (this.ignList == null || this.ignList.size() == 0) {
        // ok remains true;
      } else {
        if (CollectionUtils.containsAny(strList, this.ignList)) {

          Collection intersect = CollectionUtils.intersection(strList, this.ignList);
          System.out.printf("%15s%30s%30s%n", "IGNORE", intersect.toString(), trip.fullName);
          ok = false;
        }
      }

      if (ok) {

        if (this.reqList == null || this.reqList.size() == 0) {

        } else {
          if (CollectionUtils.containsAny(strList, this.reqList)) {
            Collection intersect = CollectionUtils.intersection(strList, this.reqList);
            System.out.printf("%15s%30s%30s%n", "MEETS REQ", intersect.toString(), trip.fullName);
          } else {
            System.out.printf("%15s%30s%30s%n", "FAILS REQ", reqList.toString(), trip.fullName);
            ok = false;
          }
        }

      }

      if (ok) {

        // create ArrayList if needed            
        if (collatedProps.containsKey(trip.shortName)) {
          //
        } else {
          collatedProps.put(trip.shortName, new ArrayList<StringPair>());
        }

        collatedProps.get(trip.shortName).add(new StringPair(trip.fullName, trip.dataType));

      }

    }

    if (DEBUG) {
      printProps("after collate");
    }

    // determine dups by size of ArrayList and copy dups to dupProps
    for (String s
            : collatedProps.keySet()) {
      if (collatedProps.get(s).size() > 1) {
        dupProps.put(s, collatedProps.get(s));
      }
    }

    // remove dups from collatedProps
    for (String s
            : dupProps.keySet()) {
      collatedProps.remove(s);
    }

    if (SUMMARIZE) {
      printProps("after move to dupProps");
    }

    // copy non-dup properties to uniqProps        
    for (String s
            : collatedProps.keySet()) {
      uniqProps.put(s, collatedProps.get(s).get(0));
    }

    if (SUMMARIZE) {
      printProps("after copy from collated to uniq");
    }

    // work through the sets of dups
    for (String curDupKey
            : dupProps.keySet()) {

      ArrayList<StringPair> theseProps = dupProps.get(curDupKey);

      for (int tryCnt = 1; tryCnt < 10; tryCnt++) {

        HashMap<String, StringPair> tryProps = new HashMap<String, StringPair>();

        for (StringPair sp : theseProps) {

          String[] strArr = sp.fullName.split("\\.");

          if (DEBUG) {
            System.out.println("strArr.length: " + strArr.length + " tryCnt: " + tryCnt);
          }

          int startIdx = strArr.length > tryCnt ? strArr.length - tryCnt : 0;
          String[] tryArr = Arrays.copyOfRange(strArr, startIdx, strArr.length);
          String tryStr = join(".", tryArr);
          tryProps.put(tryStr, sp);

        }

        // success indicated when the size of the tryProps and pairs match
        if (theseProps.size() == tryProps.size()) {

          if (DEBUG) {
            System.out.println("dupProps: key: " + curDupKey + " resolved with tryCnt: " + tryCnt);
            for (String k : tryProps.keySet()) {
              System.out.printf("%30s%90s%30s%n", k, tryProps.get(k).fullName, tryProps.get(k).dataType);
            }
          }

          // copy the success to uniqProps
          for (String k : tryProps.keySet()) {
            uniqProps.put(k, tryProps.get(k));
          }

                    // flag the successfully-resolved dupProp
          // ConcurrentModificationException if try to remove now
          successDupProps.put(curDupKey, theseProps);

          break;
        }
      }
    }

    if (SUMMARIZE) {
      printProps("final sanity check");
    }

    // populate the various prop categories
    for (String s
            : uniqProps.keySet()) {
      if (uniqProps.get(s).dataType.equals("String")) {
        strProps.add(s);
      } else if (uniqProps.get(s).dataType.equals("Integer")) {
        intProps.add(s);
      } else if (uniqProps.get(s).dataType.equals("Double")) {
        dblProps.add(s);
      } else if (uniqProps.get(s).dataType.equals("Long")) {
        longProps.add(s);
      } else if (uniqProps.get(s).dataType.equals("Boolean")) {
        boolProps.add(s);
      } else {
        othProps.add(s);
      }
    }

    Collections.sort(strProps);

    Collections.sort(intProps);

    Collections.sort(dblProps);

    Collections.sort(longProps);

    Collections.sort(boolProps);

    Collections.sort(othProps);

  }

  private void classWalker(Class<?> c, String cumulative) {

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

      if (f.getType().equals(String.class
      )) {
        rawProps.add(
                new StringTriplet(f.getName(), cumStr, "String"));
      } else if (f.getType().equals(Integer.class
      ) || f.getType().equals(int.class
      )) {
        rawProps.add(
                new StringTriplet(f.getName(), cumStr, "Integer"));
      } else if (f.getType()
              .equals(Double.class
              ) || f.getType().equals(double.class
              )) {
        rawProps.add(
                new StringTriplet(f.getName(), cumStr, "Double"));
      } else if (f.getType()
              .equals(Long.class
              ) || f.getType().equals(long.class
              )) {
        rawProps.add(
                new StringTriplet(f.getName(), cumStr, "Long"));
      } else if (f.getType()
              .equals(Boolean.class
              ) || f.getType().equals(boolean.class
              )) {
        rawProps.add(
                new StringTriplet(f.getName(), cumStr, "Boolean"));
      } else if (Collection.class
              .isAssignableFrom(f.getType())) {
        System.out.println(f.getName() + " is Collection: " + f.getType());
      } else {
        if (DEBUG) {
          System.out.println("Calling props with f.getType(): " + f.getType() + " and cumulative: " + cumStr);
        }
        classWalker(f.getType(), cumStr);
      }

    }

  }

  public Object get(Object objIn, String propNameIn) {

    Object rtn = null;

    // lookup the ACTUAL property name
    String fullPropertyName = uniqProps.get(propNameIn).fullName;

    String[] strArr = fullPropertyName.split("\\.");
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
    String rtn = null;
    if (isStrProp(propertyName)) {
      rtn = (String) get(tIn, propertyName);
    } else {
      throw new IllegalArgumentException(propertyName + " is not a valid String field shortName");
    }
    return rtn;
  }

  public Integer getInt(T tIn, String propertyName) {
    Integer rtn = null;
    if (isIntProp(propertyName)) {
      rtn = (Integer) get(tIn, propertyName);
    } else {
      throw new IllegalArgumentException(propertyName + " is not a valid Integer field shortName");
    }
    return rtn;
  }

  public Double getDbl(T tIn, String propertyName) {
    Double rtn = null;
    if (isDblProp(propertyName)) {
      rtn = (Double) get(tIn, propertyName);
    } else {
      throw new IllegalArgumentException(propertyName + " is not a valid Double field shortName");
    }
    return rtn;
  }

  public Long getLong(T tIn, String propertyName) {
    Long rtn = null;
    if (isLongProp(propertyName)) {
      rtn = (Long) get(tIn, propertyName);
    } else {
      throw new IllegalArgumentException(propertyName + " is not a valid Long field shortName");
    }
    return rtn;
  }

  public Boolean getBool(T tIn, String propertyName) {
    Boolean rtn = null;
    if (isBoolProp(propertyName)) {
      rtn = (Boolean) get(tIn, propertyName);
    } else {
      throw new IllegalArgumentException(propertyName + " is not a valid Boolean field shortName");
    }
    return rtn;
  }

  public ArrayList<String> getStrProps() {
    return strProps;
  }

  public void setStrProps(ArrayList<String> strProps) {
    this.strProps = strProps;
  }

  public ArrayList<String> getIntProps() {
    return intProps;
  }

  public void setIntProps(ArrayList<String> intProps) {
    this.intProps = intProps;
  }

  public ArrayList<String> getDblProps() {
    return dblProps;
  }

  public void setDblProps(ArrayList<String> dblProps) {
    this.dblProps = dblProps;
  }

  public ArrayList<String> getLongProps() {
    return longProps;
  }

  public void setLongProps(ArrayList<String> longProps) {
    this.longProps = longProps;
  }

  public ArrayList<String> getBoolProps() {
    return boolProps;
  }

  public void setBoolProps(ArrayList<String> boolProps) {
    this.boolProps = boolProps;
  }

  public ArrayList<String> getOthProps() {
    return othProps;
  }

  public void setOthProps(ArrayList<String> othProps) {
    this.othProps = othProps;
  }

  private class StringTriplet {

    String shortName;
    String fullName;
    String dataType;

    public StringTriplet(String shortName, String fullName, String dataType) {
      this.shortName = shortName;
      this.fullName = fullName;
      this.dataType = dataType;
    }

  }

  private class StringPair {

    String fullName;
    String dataType;

    public StringPair(String fullName, String dataType) {
      this.fullName = fullName;
      this.dataType = dataType;
    }

  }

}
