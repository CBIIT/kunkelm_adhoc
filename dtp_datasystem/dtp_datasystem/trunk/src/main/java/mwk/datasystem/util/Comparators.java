/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.util;

import java.util.Comparator;
import javax.faces.model.SelectItem;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdVO;
import mwk.datasystem.vo.CuratedNameVO;
import mwk.datasystem.vo.CuratedNscVO;
import mwk.datasystem.vo.CuratedOriginatorVO;
import mwk.datasystem.vo.CuratedProjectVO;
import mwk.datasystem.vo.CuratedTargetVO;

/**
 *
 * @author mwkunkel
 */
public class Comparators {

        public static class CmpdNscComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Integer nsc1 = ((CmpdVO) o1).getNsc();
            Integer nsc2 = ((CmpdVO) o2).getNsc();
            return nsc1.compareTo(nsc2);
        }
    }

    
    public static class CuratedNscComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            Integer nsc1 = ((CuratedNscVO) o1).getNsc();
            Integer nsc2 = ((CuratedNscVO) o2).getNsc();
            return nsc1.compareTo(nsc2);
        }
    }

    public static class CuratedNameComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            String val1 = ((CuratedNameVO) o1).getValue();
            String val2 = ((CuratedNameVO) o2).getValue();
            return val1.compareTo(val2);
        }
    }

    public static class CuratedOriginatorComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            String val1 = ((CuratedOriginatorVO) o1).getValue();
            String val2 = ((CuratedOriginatorVO) o2).getValue();
            return val1.compareTo(val2);
        }
    }

    public static class CuratedProjectComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            String val1 = ((CuratedProjectVO) o1).getValue();
            String val2 = ((CuratedProjectVO) o2).getValue();
            return val1.compareTo(val2);
        }
    }

    public static class CuratedTargetComparator implements Comparator {

        public int compare(Object o1, Object o2) {
            String val1 = ((CuratedTargetVO) o1).getValue();
            String val2 = ((CuratedTargetVO) o2).getValue();
            return val1.compareTo(val2);
        }
    }

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
