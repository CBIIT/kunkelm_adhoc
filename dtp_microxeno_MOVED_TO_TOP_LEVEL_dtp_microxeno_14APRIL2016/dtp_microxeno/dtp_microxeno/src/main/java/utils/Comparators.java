/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.Comparator;
import mwk.microxeno.vo.PassageVO;
import mwk.microxeno.vo.AffymetrixIdentifierVO;
import mwk.microxeno.vo.PassageAvgSetVO;
import mwk.microxeno.vo.PassageAvgVO;
import mwk.microxeno.vo.PassageIdentifierVO;
import mwk.microxeno.vo.TumorVO;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 *
 * @author mwkunkel
 */
public class Comparators {

    public static class TumorComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            TumorVO t1 = (TumorVO) o1;
            TumorVO t2 = (TumorVO) o2;
            return new CompareToBuilder()
                    .append(t1.getTumorName(), t2.getTumorName())
                    .toComparison();
        }
    }

    public static class PassageAvgDataSetComparator implements Comparator {

        public int compare(Object o1, Object o2) {

            PassageAvgSetVO ds1 = (PassageAvgSetVO) o1;
            PassageAvgSetVO ds2 = (PassageAvgSetVO) o2;

            return new CompareToBuilder()
                    .append(ds1.getPassageIdentifier().getAffymetrixIdentifier(), ds2.getPassageIdentifier().getAffymetrixIdentifier())
                    .toComparison();
        }
    }

    public static class PassageVOComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            PassageVO ad1 = (PassageVO) o1;
            PassageVO ad2 = (PassageVO) o2;
            return new CompareToBuilder()
                    .append(ad1.getAffymetrixIdentifier(), ad2.getAffymetrixIdentifier())
                    .append(ad1.getTumor(), ad2.getTumor())
                    .append(ad1.getPassage(), ad2.getPassage())
                    .toComparison();
        }
    }

        public static class PassageIdentComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            PassageIdentifierVO ad1 = (PassageIdentifierVO) o1;
            PassageIdentifierVO ad2 = (PassageIdentifierVO) o2;
            return new CompareToBuilder()
                    .append(ad1.getAffymetrixIdentifier(), ad2.getAffymetrixIdentifier())
                    .append(ad1.getPassage(), ad2.getPassage())
                    .toComparison();
        }
    }
    
    public static class PassageAvgComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            PassageAvgVO ad1 = (PassageAvgVO) o1;
            PassageAvgVO ad2 = (PassageAvgVO) o2;
            return new CompareToBuilder()
                    .append(ad1.getPassageIdentifier().getAffymetrixIdentifier(), ad2.getPassageIdentifier().getAffymetrixIdentifier())
                    .append(ad1.getTumor(), ad2.getTumor())
                    .append(ad1.getPassageIdentifier().getPassage(), ad2.getPassageIdentifier().getPassage())
                    .toComparison();
        }
    }

    public static class AffymetrixIdentifierComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            AffymetrixIdentifierVO ai1 = (AffymetrixIdentifierVO) o1;
            AffymetrixIdentifierVO ai2 = (AffymetrixIdentifierVO) o2;
            return new CompareToBuilder()
                    .append(ai1.getGeneSymbol(), ai2.getGeneSymbol())
                    .append(ai1.getProbeSetId(), ai2.getProbeSetId())
                    .toComparison();
        }
    }

}