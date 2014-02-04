/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import com.flaptor.hist4j.AdaptiveHistogram;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
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

    public static ArrayList<ExtendedCartesianChartModel> generateScatter(CmpdListVO incoming, List<String> propertyNameList) {

        ArrayList<ExtendedCartesianChartModel> scatterModel = new ArrayList<ExtendedCartesianChartModel>();

        try {

            for (int xCnt = 0; xCnt < propertyNameList.size(); xCnt++) {

                String xParam = propertyNameList.get(xCnt);

                for (int yCnt = 0; yCnt < xCnt; yCnt++) {

                    ExtendedCartesianChartModel thisModel = new ExtendedCartesianChartModel();

                    String yParam = propertyNameList.get(yCnt);

                    thisModel.setTitle(yParam + " vs " + xParam);
                    thisModel.setLabelAxisX(xParam);
                    thisModel.setLabelAxisY(yParam);

                    ChartSeries series = new ChartSeries();
                    series.setLabel(yParam + " vs " + xParam);

                    ChartSeries selectedSeries = new ChartSeries();
                    selectedSeries.setLabel(yParam + " vs " + xParam + " selected");

                    NumberFormat nf = new DecimalFormat();
                    nf.setMaximumFractionDigits(2);

                    for (CmpdListMemberVO clmVO : incoming.getCmpdListMembers()) {
                        if (clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem() != null) {
                            CmpdFragmentPChemVO pChem = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

                            double xProp = getProperty(clmVO, xParam);
                            double yProp = getProperty(clmVO, yParam);

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
            if (pchemVO.getAlogp() != null) {
                if (!Double.isNaN(pchemVO.getAlogp())) {
                    rtn = pchemVO.getAlogp();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("logd")) {
            if (pchemVO.getLogd() != null) {
                if (!Double.isNaN(pchemVO.getLogd())) {
                    rtn = pchemVO.getLogd();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("hba")) {
            if (pchemVO.getHba() != null) {
                if (!Double.isNaN(pchemVO.getHba())) {
                    rtn = pchemVO.getHba();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("hbd")) {
            if (pchemVO.getHbd() != null) {
                if (!Double.isNaN(pchemVO.getHbd())) {
                    rtn = pchemVO.getHbd();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("sa")) {
            if (pchemVO.getSa() != null) {
                if (!Double.isNaN(pchemVO.getSa())) {
                    rtn = pchemVO.getSa();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("psa")) {
            if (pchemVO.getPsa() != null) {
                if (!Double.isNaN(pchemVO.getPsa())) {
                    rtn = pchemVO.getPsa();
                } else {
                    rtn = Double.NaN;
                }
            } else {
                rtn = Double.NaN;
            }
        }

        if (propertyName.equals("mw")) {
            if (pchemVO.getMw() != null) {
                if (!Double.isNaN(pchemVO.getMw())) {
                    rtn = pchemVO.getMw();
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
