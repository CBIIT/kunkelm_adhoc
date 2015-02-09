/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkcharting;

import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;

/**
 *
 * @author mwkunkel
 */
public class TemplatedPropertyUtilities<T> {
  
   public static Boolean knownStringProperty(String propertyName) {
    return PropertyUtilities.knownStringProperty(propertyName);
  }

  public static Boolean knownIntegerProperty(String propertyName) {
    return PropertyUtilities.knownIntegerProperty(propertyName);
  }

  public static Boolean knownDoubleProperty(String propertyName) {
    return PropertyUtilities.knownDoubleProperty(propertyName);
  }

  public String getStringProperty(T t, String propertyName) {

        String rtn = null;
        
//        System.out.println("In getStringProperty T t");
//        System.out.println("t.getClass().getName: " + t.getClass().getName());

        if (t instanceof CmpdListMemberVO){
          CmpdListMemberVO clmVO = (CmpdListMemberVO) t;
          rtn = PropertyUtilities.getStringProperty(clmVO, propertyName);          
        }
        
        return rtn;
        
    }
  
  
  public Integer getIntegerProperty(T t, String propertyName) {

        Integer rtn = null;
        
//        System.out.println("In getIntegerProperty T t");
//        System.out.println("t.getClass().getName: " + t.getClass().getName());

        if (t instanceof CmpdListMemberVO){
          CmpdListMemberVO clmVO = (CmpdListMemberVO) t;
          rtn = PropertyUtilities.getIntegerProperty(clmVO, propertyName);          
        }
        
        return rtn;
        
    }
  
    public Double getDoubleProperty(T t, String propertyName) {

        Double rtn = null;
        
//        System.out.println("In getDoubleProperty T t");
//        System.out.println("t.getClass().getName: " + t.getClass().getName());

        if (t instanceof CmpdListMemberVO){
          CmpdListMemberVO clmVO = (CmpdListMemberVO) t;
          rtn = PropertyUtilities.getDoubleProperty(clmVO, propertyName);          
        }
        
        return rtn;
        
    }

}
