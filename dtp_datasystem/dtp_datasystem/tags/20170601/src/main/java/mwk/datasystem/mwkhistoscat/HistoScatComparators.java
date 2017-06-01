/*
 * To change this template, choose Tools | Templates
 
 */
package mwk.datasystem.mwkhistoscat;

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
import org.apache.commons.lang3.builder.CompareToBuilder;

/**
 *
 * @author mwkunkel
 */
public class HistoScatComparators {

    public static class ScatterPlotPointXComparator implements Comparator<ScatterPlotPoint> {

        public int compare(ScatterPlotPoint spp1, ScatterPlotPoint spp2) {

            return new CompareToBuilder()
                    .append(spp1.getxVal(), spp2.getxVal())
                    .toComparison();

        }
    }

    public static class ScatterPlotPointYComparator implements Comparator<ScatterPlotPoint> {

        public int compare(ScatterPlotPoint spp1, ScatterPlotPoint spp2) {

            return new CompareToBuilder()
                    .append(spp1.getyVal(), spp2.getyVal())
                    .toComparison();

        }
    }

}
