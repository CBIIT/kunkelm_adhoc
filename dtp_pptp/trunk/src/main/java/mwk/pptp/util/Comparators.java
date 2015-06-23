/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.pptp.util;

import java.util.Comparator;
import mwk.pptp.vo.MouseDataShuttleVO;
import mwk.pptp.vo.MouseRTVShuttleVO;
import mwk.pptp.vo.MouseSurvivalShuttleVO;
import org.apache.commons.lang.builder.CompareToBuilder;

/**
 *
 * @author mwkunkel
 */
public class Comparators {

    public static class MouseDataShuttleComparator implements Comparator {

        public int compare(Object o1, Object o2) {

            MouseDataShuttleVO dat1 = (MouseDataShuttleVO) o1;
            MouseDataShuttleVO dat2 = (MouseDataShuttleVO) o2;

            return new CompareToBuilder()
                    .append(dat1.getDay(), dat2.getDay())
                    .toComparison();
        }
    }

    public static class MouseRTVShuttleComparator implements Comparator {

        public int compare(Object o1, Object o2) {

            MouseRTVShuttleVO rtv1 = (MouseRTVShuttleVO) o1;
            MouseRTVShuttleVO rtv2 = (MouseRTVShuttleVO) o2;

            return new CompareToBuilder()
                    .append(rtv1.getDay(), rtv2.getDay())
                    .toComparison();
        }
    }

    public static class MouseSurvivalShuttleComparator implements Comparator {

        public int compare(Object o1, Object o2) {

            MouseSurvivalShuttleVO surv1 = (MouseSurvivalShuttleVO) o1;
            MouseSurvivalShuttleVO surv2 = (MouseSurvivalShuttleVO) o2;

            return new CompareToBuilder()
                    .append(surv1.getTimeToEvent(), surv2.getTimeToEvent())
                    .toComparison();
        }
    }

}
