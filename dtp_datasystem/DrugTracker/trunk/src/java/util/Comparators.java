/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package util;

import java.util.Comparator;
import javax.faces.model.SelectItem;

/**
 *
 * @author mwkunkel
 */
public class Comparators {
 
  public static class SelectItemComparator implements Comparator {

    public int compare(Object o1, Object o2) {
      SelectItem mw1 = (SelectItem) o1;
      SelectItem mw2 = (SelectItem) o2;
      return mw1.getLabel().compareTo(mw2.getLabel());
    }
    
  }
  
}
