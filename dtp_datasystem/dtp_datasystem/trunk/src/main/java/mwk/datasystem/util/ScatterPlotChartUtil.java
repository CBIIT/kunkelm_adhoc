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
import mwk.datasystem.mwkcharting.TemplPropUtil;
import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.LineChartModel;
import org.primefaces.model.chart.LineChartSeries;

/**
 *
 * @author mwkunkel
 */
public class ScatterPlotChartUtil {

    public static ArrayList<LineChartModel> generateScatter(Collection<CmpdListMemberVO> incoming, List<String> propertyNameList) {

        // MWK 07Dec2014 added sort step to propertyNameList
        // MWK 30Jan2015 refactoring to LineChartModel
        ArrayList<String> sortedPropertyNames = new ArrayList<String>(propertyNameList);
        Collections.sort(sortedPropertyNames);

        ArrayList<CmpdListMemberVO> clmList = new ArrayList<CmpdListMemberVO>(incoming);
        Collections.sort(clmList);

        ArrayList<LineChartModel> scatChartList = new ArrayList<LineChartModel>();

        try {

            for (int outerCnt = 0; outerCnt < sortedPropertyNames.size(); outerCnt++) {

                String outerParam = sortedPropertyNames.get(outerCnt);

                for (int innterCnt = 0; innterCnt < sortedPropertyNames.size(); innterCnt++) {

                    LineChartModel scatPlotMdl = new LineChartModel();

                    String innerParam = sortedPropertyNames.get(innterCnt);

                    scatPlotMdl.setTitle(outerParam + " vs " + innerParam);
                    scatPlotMdl.setExtender("scatterPlotExtender");
                    scatPlotMdl.setZoom(true);
                    Axis xAxis = scatPlotMdl.getAxis(AxisType.X);
                    xAxis.setLabel(innerParam);
                    xAxis.setTickAngle(-90);
                    Axis yAxis = scatPlotMdl.getAxis(AxisType.Y);
                    yAxis.setLabel(outerParam);

                    LineChartSeries chSer = new LineChartSeries();
                    chSer.setLabel(outerParam + " vs " + innerParam);
                    chSer.setShowLine(false);
                    chSer.setShowMarker(true);

                    LineChartSeries selChSer = new LineChartSeries();
                    selChSer.setLabel(outerParam + " vs " + innerParam + " selected");
                    selChSer.setShowLine(false);
                    selChSer.setShowMarker(true);

                    NumberFormat nf = new DecimalFormat();
                    nf.setMaximumFractionDigits(2);

                    // MWK 12Feb2015 - have to pre-sort the data so that
                    // I don't step on the default setting of sortData in jqPlot
                    ArrayList<XYProp> serPropList = new ArrayList<XYProp>();
                    ArrayList<XYProp> selSerPropList = new ArrayList<XYProp>();

                    for (CmpdListMemberVO clmVO : clmList) {

                        if (clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem() != null) {

                            CmpdFragmentPChemVO pChem = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

                            Double xProp = getProperty(clmVO, innerParam);
                            Double yProp = getProperty(clmVO, outerParam);

                            // create lists of XYProp for sorting
                            if (xProp != null && yProp != null) {
                                if (clmVO.getIsSelected() != null && clmVO.getIsSelected()) {
                                    XYProp p = new XYProp(xProp, yProp, clmVO.getCmpd().getName());
                                    selSerPropList.add(p);
                                } else {
                                    XYProp p = new XYProp(xProp, yProp, clmVO.getCmpd().getName());
                                    serPropList.add(p);
                                }
                            }
                        }
                    }

                    // sort the lists
                    Collections.sort(selSerPropList);
                    Collections.sort(serPropList);

                    // write the series
                    int ptIdx = 0;
                    int selPtIdx = 0;

                    for (XYProp xyp : serPropList) {
                        chSer.set(xyp.xProp, xyp.yProp, xyp.label);
                    }

                    for (XYProp xyp : selSerPropList) {

                        selChSer.set(xyp.xProp, xyp.yProp, xyp.label);
                    }

                    if (!chSer.getData().isEmpty()) {
                        scatPlotMdl.addSeries(chSer);
                    }

                    if (!selChSer.getData().isEmpty()) {
                        scatPlotMdl.addSeries(selChSer);
                    }

                    scatChartList.add(scatPlotMdl);

                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return scatChartList;

    }

    public static Double getProperty(CmpdListMemberVO clmVO, String propertyName) {

        TemplPropUtil<CmpdListMemberVO> propUtils = new TemplPropUtil<CmpdListMemberVO>(clmVO);

        // these are hard-coded since reflection is problematic...
        Double rtn = null;

        CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

        if (propUtils.knownDoubleProperty(propertyName)) {
            if (propUtils.getDoubleProperty(clmVO, propertyName) != null) {
                rtn = propUtils.getDoubleProperty(clmVO, propertyName);
            }
        } else if (propUtils.knownIntegerProperty(propertyName)) {
            if (propUtils.getIntegerProperty(clmVO, propertyName) != null) {
                rtn = propUtils.getIntegerProperty(clmVO, propertyName).doubleValue();
            }
        }

        return rtn;

    }

}
