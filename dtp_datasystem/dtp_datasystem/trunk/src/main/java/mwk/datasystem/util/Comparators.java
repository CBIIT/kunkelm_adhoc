/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import java.util.Comparator;
import javax.faces.model.SelectItem;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.vo.CmpdFragmentVO;


/**
 *
 * @author mwkunkel
 */
public class Comparators {
  
  public static class AdHocCmpdFragmentSizeComparator implements Comparator {

    public int compare(Object o1, Object o2) {
      Double mw1 = ((AdHocCmpdFragment) o1).getAdHocCmpdFragmentPChem().getMolecularWeight();
      Double mw2 = ((AdHocCmpdFragment) o2).getAdHocCmpdFragmentPChem().getMolecularWeight();
      return mw1.compareTo(mw2);
    }
  }
  
  public static class CmpdFragmentSizeComparator implements Comparator {

    public int compare(Object o1, Object o2) {
      Double mw1 = ((CmpdFragmentVO) o1).getCmpdFragmentPChem().getMolecularWeight();
      Double mw2 = ((CmpdFragmentVO) o2).getCmpdFragmentPChem().getMolecularWeight();
      return mw1.compareTo(mw2);
    }
  }
  
  public static class SelectItemComparator implements Comparator {

    public int compare(Object o1, Object o2) {
      SelectItem mw1 = (SelectItem) o1;
      SelectItem mw2 = (SelectItem) o2;
      return mw1.getLabel().compareTo(mw2.getLabel());
    }
    
  }
  
}
