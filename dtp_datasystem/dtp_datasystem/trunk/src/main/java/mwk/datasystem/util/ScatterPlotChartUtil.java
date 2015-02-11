/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import mwk.datasystem.mwkcharting.PropertyUtilities;
import org.primefaces.model.chart.ChartSeries;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;

/**
 *
 * @author mwkunkel
 */
public class ScatterPlotChartUtil {

    public static ArrayList<LineChartModel> generateScatter(Collection<CmpdListMemberVO> incoming, List<String> propertyNameList) {

    // MWK 07Dec2014 added sort step to propertyNameList
        // MWK 30Jan2015 refactoring to LineChartModel
        ArrayList<String> sortList = new ArrayList<String>(propertyNameList);
        Collections.sort(sortList);

        ArrayList<LineChartModel> scatChartList = new ArrayList<LineChartModel>();

        try {

            for (int outerCnt = 0; outerCnt < sortList.size(); outerCnt++) {

                String outerParam = sortList.get(outerCnt);

                for (int innterCnt = 0; innterCnt < sortList.size(); innterCnt++) {

                    LineChartModel thisModel = new LineChartModel();

                    String innerParam = sortList.get(innterCnt);

                    thisModel.setTitle(outerParam + " vs " + innerParam);
                    thisModel.setExtender("scatterPlotExtender");
                    thisModel.setZoom(true);

                    Axis xAxis = thisModel.getAxis(AxisType.X);
                    xAxis.setLabel(innerParam);
                    xAxis.setTickAngle(-90);

                    Axis yAxis = thisModel.getAxis(AxisType.Y);
                    yAxis.setLabel(outerParam);

                    ChartSeries series = new ChartSeries();
                    series.setLabel(outerParam + " vs " + innerParam);

                    ChartSeries selectedSeries = new ChartSeries();
                    selectedSeries.setLabel(outerParam + " vs " + innerParam + " selected");

                    NumberFormat nf = new DecimalFormat();
                    nf.setMaximumFractionDigits(2);

                    for (CmpdListMemberVO clmVO : incoming) {

                        if (clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem() != null) {
                            CmpdFragmentPChemVO pChem = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

                            Double xProp = getProperty(clmVO, innerParam);
                            Double yProp = getProperty(clmVO, outerParam);

                            if (xProp != null && yProp != null) {
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

                    scatChartList.add(thisModel);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return scatChartList;

    }

    public static Double getProperty(CmpdListMemberVO clmVO, String propertyName) {

        // these are hard-coded since reflection is problematic...
        Double rtn = null;

        CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

        // these are hard-coded since reflection is problematic...
        if (propertyName.equals("alogp")) {
            rtn = PropertyUtilities.getDoubleProperty(clmVO, "alogp");
        }

        if (propertyName.equals("logd")) {
            rtn = PropertyUtilities.getDoubleProperty(clmVO, "logd");
        }

        if (propertyName.equals("hba")) {
            Integer tempRtn = PropertyUtilities.getIntegerProperty(clmVO, "hba");
            if (tempRtn != null) {
                rtn = tempRtn.doubleValue();
            }
        }

        if (propertyName.equals("hbd")) {
            Integer tempRtn = PropertyUtilities.getIntegerProperty(clmVO, "hbd");
            if (tempRtn != null) {
                rtn = tempRtn.doubleValue();
            }
        }

        if (propertyName.equals("sa")) {
            rtn = PropertyUtilities.getDoubleProperty(clmVO, "sa");
        }

        if (propertyName.equals("mw")) {
            rtn = PropertyUtilities.getDoubleProperty(clmVO, "mw");
        }

        return rtn;

    }

}
