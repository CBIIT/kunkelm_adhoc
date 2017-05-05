/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package databasemetadatareplicator;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mwkunkel
 */
public class GrantBean {

  public static final String TBLNONE = "none";
  public static final String TBLSEL = "select";
  public static final String TBLSELINS = "select, insert";
  public static final String TBLSELINSUPD = "select, insert, update";

  public static final String SEQNONE = "none";
  public static final String SEQSELUSE = "select, usage";
  public static final String SEQSELUSEUPD = "select, usage, update";

  public List<String> tableGrantSelect;
  public List<String> tableGrantSelectInsert;
  public List<String> tableGrantSelectInsertUpdate;
  public List<String> tableGrantNone;

  public List<String> sequenceGrantSelectUsage;
  public List<String> sequenceGrantSelectUsageUpdate;
  public List<String> sequenceGrantNone;

  public GrantBean() {
    tableGrantSelect = new ArrayList<String>();
    tableGrantSelectInsert = new ArrayList<String>();
    tableGrantSelectInsertUpdate = new ArrayList<String>();
    tableGrantNone = new ArrayList<String>();

    sequenceGrantSelectUsage = new ArrayList<String>();
    sequenceGrantSelectUsageUpdate = new ArrayList<String>();
    sequenceGrantNone = new ArrayList<String>();
  }

  public void printGrants(String whom) {
    
    for (String s : tableGrantSelect) {
      System.out.println("revoke all on " + s + " from " + whom);
      System.out.println("grant select on " + s + " to " + whom);
    }
    for (String s : tableGrantSelectInsert) {
      System.out.println("revoke all on " + s + " from " + whom);
      System.out.println("grant select, insert on " + s + " to " + whom);
    }
    for (String s : tableGrantSelectInsertUpdate) {
      System.out.println("revoke all on " + s + " from " + whom);
      System.out.println("grant select, insert, update on " + s + " to " + whom);
    }
    for (String s : tableGrantNone) {
      System.out.println("revoke all on " + s + " from " + whom);      
    }

    for (String s : sequenceGrantSelectUsage) {
      System.out.println("revoke all on " + s + " from " + whom);
      System.out.println("grant select, usage on " + s + " to " + whom);
    }
    for (String s : sequenceGrantSelectUsageUpdate) {
      System.out.println("revoke all on " + s + " from " + whom);
      System.out.println("grant select, usage, update on " + s + " to " + whom);
    }
    for (String s : sequenceGrantNone) {
      System.out.println("revoke all on " + s + " from " + whom);      
    }

  }
  
  public List<String> listGrants(String whom) {
    
    List<String> grantList = new ArrayList<String>();
    
    for (String s : tableGrantSelect) {
      grantList.add("revoke all on " + s + " from " + whom);
      grantList.add("grant select on " + s + " to " + whom);
    }
    for (String s : tableGrantSelectInsert) {
      grantList.add("revoke all on " + s + " from " + whom);
      grantList.add("grant select, insert on " + s + " to " + whom);
    }
    for (String s : tableGrantSelectInsertUpdate) {
      grantList.add("revoke all on " + s + " from " + whom);
      grantList.add("grant select, insert, update on " + s + " to " + whom);
    }
    for (String s : tableGrantNone) {
      grantList.add("revoke all on " + s + " from " + whom);
    }

    for (String s : sequenceGrantSelectUsage) {
      grantList.add("revoke all on " + s + " from " + whom);
      grantList.add("grant select, usage on " + s + " to " + whom);
    }
    for (String s : sequenceGrantSelectUsageUpdate) {
      grantList.add("revoke all on " + s + " from " + whom);
      grantList.add("grant select, usage, update on " + s + " to " + whom);
    }
    for (String s : sequenceGrantNone) {
      grantList.add("revoke all on " + s + " from " + whom);
    }

    return grantList;
    
  }

}
