/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Comparator;
import mwk.microxeno.vo.AffymetrixDataVO;
import mwk.microxeno.vo.AffymetrixIdentifierVO;
import mwk.microxeno.vo.PassageDataSetVO;
import mwk.microxeno.vo.PassageIdentifierVO;
import mwk.microxeno.vo.TumorTypeVO;
import mwk.microxeno.vo.TumorVO;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 *
 * @author mwkunkel
 */
public class Comparators {

    public static class PassageDataSetComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            PassageDataSetVO ds1 = (PassageDataSetVO) o1;
            PassageDataSetVO ds2 = (PassageDataSetVO) o2;
            PassageIdentifierVO ai1 = ds1.getPassageIdentifier();
            PassageIdentifierVO ai2 = ds2.getPassageIdentifier();
            return new CompareToBuilder()
                    .append(ds1.getPassageIdentifier(), ds2.getPassageIdentifier())
                    .toComparison();
        }
    }

    public static class AffymetrixDataComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            AffymetrixDataVO ad1 = (AffymetrixDataVO) o1;
            AffymetrixDataVO ad2 = (AffymetrixDataVO) o2;
            return new CompareToBuilder()
                    .append(ad1.getAffymetrixIdentifier(), ad2.getAffymetrixIdentifier())
                    .append(ad1.getTumor(), ad2.getTumor())
                    .append(ad1.getPassage(), ad2.getPassage())
                    .toComparison();
        }
    }

}
