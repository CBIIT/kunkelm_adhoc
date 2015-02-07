/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkcharting;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;

/**
 *
 * @author mwkunkel
 */
public class PropertyUtilities {

  private static List<String> knownStrProps;
  private static List<String> knownIntProps;
  private static List<String> knownDblProps;

  static {
    knownStrProps = new ArrayList<String>(Arrays.asList(new String[]{"prefix"}));
    knownIntProps = new ArrayList<String>(Arrays.asList(new String[]{"nsc", "hba", "hbd"}));
    knownDblProps = new ArrayList<String>(Arrays.asList(new String[]{"alogp", "logd", "sa", "mw"}));
  }

  public static Boolean knownStringProperty(String propertyName) {
    return (knownStrProps.contains(propertyName));
  }

  public static Boolean knownIntegerProperty(String propertyName) {
    return (knownIntProps.contains(propertyName));
  }

  public static Boolean knownDoubleProperty(String propertyName) {
    return (knownDblProps.contains(propertyName));
  }

  public static String getStringProperty(CmpdListMemberVO clmVO, String propertyName) {

    String rtn = null;

    if (knownStringProperty(propertyName)) {
      if (propertyName.equals("prefix")) {
        if (clmVO.getCmpd() != null && clmVO.getCmpd().getPrefix() != null) {
          rtn = clmVO.getCmpd().getPrefix();
        }
      }
    }

    return rtn;

  }

  public static Integer getIntegerProperty(CmpdListMemberVO clmVO, String propertyName) {

    Integer rtn = null;

    if (knownIntegerProperty(propertyName)) {

      if (propertyName.equals("nsc")) {
        if (clmVO.getCmpd() != null && clmVO.getCmpd().getNsc() != null) {
          rtn = clmVO.getCmpd().getNsc();
        }
      }

      CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

      if (propertyName.equals("hba")) {
        if (pchemVO.getCountHydBondAcceptors() != null) {
          rtn = pchemVO.getCountHydBondAcceptors();
        }
      }

      if (propertyName.equals("hbd")) {
        if (pchemVO.getCountHydBondDonors() != null) {
          rtn = pchemVO.getCountHydBondDonors();
        }
      }

    }

    return rtn;

  }

  public static Double getDoubleProperty(CmpdListMemberVO clmVO, String propertyName) {

    Double rtn = null;

    if (knownDoubleProperty(propertyName)) {

      CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

      if (propertyName.equals("alogp")) {
        if (pchemVO.getTheALogP() != null) {
          rtn = pchemVO.getTheALogP();
        }
      }

      if (propertyName.equals("logd")) {
        if (pchemVO.getLogD() != null) {
          rtn = pchemVO.getLogD();
        }
      }

      if (propertyName.equals("sa")) {
        if (pchemVO.getSurfaceArea() != null) {
          rtn = pchemVO.getSurfaceArea();
        }
      }

      if (propertyName.equals("mw")) {
        if (pchemVO.getMolecularWeight() != null) {
          rtn = pchemVO.getMolecularWeight();
        }
      }

    }

    return rtn;

  }

}
