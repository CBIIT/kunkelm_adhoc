/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import com.flaptor.hist4j.AdaptiveHistogram;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import mwk.datasystem.mwkcharting.Histogram;
import org.primefaces.model.chart.ChartSeries;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;

/**
 *
 * @author mwkunkel
 */
public class ScatterPlotChartUtil {

    public static ArrayList<ExtendedCartesianChartModel> generateScatter(Collection<CmpdListMemberVO> incoming, List<String> propertyNameList) {

        // MWK 07Dec2014 added sort step to propertyNameList
      
        ArrayList<String> sortList = new ArrayList<String>(propertyNameList);
        Collections.sort(sortList);
      
        ArrayList<ExtendedCartesianChartModel> scatterModel = new ArrayList<ExtendedCartesianChartModel>();

        try {

            for (int outerCnt = 0; outerCnt < sortList.size(); outerCnt++) {

                String outerParam = sortList.get(outerCnt);

                //for (int yCnt = 0; yCnt < xCnt; yCnt++) {
                
                for (int innterCnt = 0; innterCnt < sortList.size(); innterCnt++) {

                    ExtendedCartesianChartModel thisModel = new ExtendedCartesianChartModel();

                    String innerParam = sortList.get(innterCnt);

                    thisModel.setTitle(outerParam + " vs " + innerParam);
                    thisModel.setLabelAxisX(innerParam);
                    thisModel.setLabelAxisY(outerParam);

                    ChartSeries series = new ChartSeries();
                    series.setLabel(outerParam + " vs " + innerParam);

                    ChartSeries selectedSeries = new ChartSeries();
                    selectedSeries.setLabel(outerParam + " vs " + innerParam + " selected");

                    NumberFormat nf = new DecimalFormat();
                    nf.setMaximumFractionDigits(2);

                    for (CmpdListMemberVO clmVO : incoming) {
                      
                        if (clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem() != null) {
                            CmpdFragmentPChemVO pChem = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

                            double xProp = getProperty(clmVO, innerParam);
                            double yProp = getProperty(clmVO, outerParam);

                            if (xProp != Double.NaN && yProp != Double.NaN) {
                                if (clmVO.getIsSelected() != null && clmVO.getIsSelected()) {
                                    selectedSeries.set(xProp, yProp);                                    
                                } else {
                                    series.set(xProp, yProp);
                                }
                            }
                        }
                    }
                    
                    if (!series.getData().isEmpty()) {
                        thisModel.addSeries(series);
                    }
                    
                    if (!selectedSeries.getData().isEmpty()) {
                        thisModel.addSeries(selectedSeries);
                    }

                    scatterModel.add(thisModel);

                }

            }
 
        } catch (Exception e) {
            e.printStackTrace();
        }

        return scatterModel;

    }

    public static double getProperty(CmpdListMemberVO clmVO, String propertyName) {

        // these are hard-coded since reflection is problematic...

        double rtn = Double.NaN;

        CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

        if (propertyName.equals("alogp")) {
            if (pchemVO.getTheALogP() != null) {
                if (!Double.isNaN(pchemVO.getTheALogP())) {
                    rtn = pchemVO.getTheALogP();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("logd")) {
            if (pchemVO.getLogD() != null) {
                if (!Double.isNaN(pchemVO.getLogD())) {
                    rtn = pchemVO.getLogD();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("hba")) {
            if (pchemVO.getCountHydBondAcceptors() != null) {
                if (!Double.isNaN(pchemVO.getCountHydBondAcceptors())) {
                    rtn = pchemVO.getCountHydBondAcceptors();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("hbd")) {
            if (pchemVO.getCountHydBondDonors() != null) {
                if (!Double.isNaN(pchemVO.getCountHydBondDonors())) {
                    rtn = pchemVO.getCountHydBondDonors();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("sa")) {
            if (pchemVO.getSurfaceArea() != null) {
                if (!Double.isNaN(pchemVO.getSurfaceArea())) {
                    rtn = pchemVO.getSurfaceArea();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("mw")) {
            if (pchemVO.getMolecularWeight() != null) {
                if (!Double.isNaN(pchemVO.getMolecularWeight())) {
                    rtn = pchemVO.getMolecularWeight();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        return rtn;

    }
}
