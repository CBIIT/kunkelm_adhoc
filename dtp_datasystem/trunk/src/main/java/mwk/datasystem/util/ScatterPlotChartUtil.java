/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import histogram.AdaptiveHistogram;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import mwk.datasystem.mwkhisto.Histogram;
import org.primefaces.model.chart.ChartSeries;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;

/**
 *
 * @author mwkunkel
 */
public class ScatterPlotChartUtil {
    
    public static ExtendedCartesianChartModel generateScatter(CmpdListVO incoming) {
        
        ExtendedCartesianChartModel scatterModel = new ExtendedCartesianChartModel();
        
        try {            
            
            scatterModel.setTitle("mw vs psa");
            
            ChartSeries series = new ChartSeries();
            series.setLabel("mw vs psa");
            
            ChartSeries selectedSeries = new ChartSeries();
            selectedSeries.setLabel("mw vs psa - selected");
            
            NumberFormat nf = new DecimalFormat();
            nf.setMaximumFractionDigits(2);
            
            for (CmpdListMemberVO clmVO : incoming.getCmpdListMembers()) {                
                if (clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem() != null) {
                    CmpdFragmentPChemVO pChem = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();                    
                    if (pChem.getMw() != null && pChem.getPsa() != null) {                        
                        if (clmVO.getIsSelected() != null && clmVO.getIsSelected()) {                            
                            selectedSeries.set(pChem.getMw(), pChem.getPsa());
                        } else {
                            series.set(pChem.getMw(), pChem.getPsa());
                        }
                    }
                }
            }
            
            scatterModel.addSeries(series);
            scatterModel.addSeries(selectedSeries);
            
        } catch (Exception e) {
            e.printStackTrace();
            
        }
        
        return scatterModel;
        
    }
}
