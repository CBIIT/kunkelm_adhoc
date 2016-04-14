/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import heatmap.HeatMapCell;
import static heatmap.HeatMapUtil.makeHeatMapCell;
import java.util.Collections;
import java.util.ArrayList;
import java.util.List;
import mwk.microxeno.vo.PassageAvgVO;
import mwk.microxeno.vo.PassageAvgSetVO;
import mwk.microxeno.vo.PassageIdentifierVO;
import mwk.microxeno.vo.TumorVO;

/**
 *
 * searches each set of TestResults for all possible cell lines and adds nulls
 * when needed this avoids losing metadata (e.g., countExperiments)
 *
 * @author mwkunkel
 */
public class CrosstabUtil {
  
    public static CrosstabModel<PassageIdentifierVO, TumorVO, PassageAvgVO> makeCrosstabModel(List<PassageAvgVO> incomingList) {
       
        CrosstabModel<PassageIdentifierVO, TumorVO, PassageAvgVO> rtn = new CrosstabModel<PassageIdentifierVO, TumorVO, PassageAvgVO>();
        
        TwoDHashMap<PassageIdentifierVO, TumorVO, PassageAvgVO> map = new TwoDHashMap<PassageIdentifierVO, TumorVO, PassageAvgVO>();
        
        for (PassageAvgVO paVO : incomingList) {
            PassageIdentifierVO piVO = paVO.getPassageIdentifier();
            map.put(piVO, paVO.getTumor(), paVO);
        }
        
        ArrayList<PassageIdentifierVO> xList = map.getXkeys();
        ArrayList<TumorVO> yList = map.getYkeys();
        
        // sort the data objects and then maintain that order while rendering heatMap
        // THIS is the disconnect between data and rendering...
        Collections.sort(xList);
        Collections.sort(yList);
        
        // set up the headers and the grid
        rtn.setGridXheaders(xList);
        rtn.setGridYheaders(yList);

        rtn.setGridXY(new PassageAvgVO[xList.size()][yList.size()]);

        int rowCnt = 0;
        for (TumorVO tVO : yList) {
            int colCnt = 0;
            for (PassageIdentifierVO piVO : xList) {
                PassageAvgVO paVO = map.get(piVO, tVO);
                HeatMapCell hmc = makeHeatMapCell(paVO);
                rtn.getGridXY()[colCnt][rowCnt] = paVO;
                colCnt++;
            }
            rowCnt++;
        }
        
        
        return rtn;
    }

     /**
     *
     * @param dsVO
     */
    private static void doCalculate(PassageAvgSetVO dsVO) {
        Double the_val;
        Double mean;
        Double sd;
        Double minVal;
        Double maxVal;
        Double sum = 0d;
        Double sum_sqd = 0d;
        int count = 0;
        Double delta;
        Double minDelta;
        Double maxDelta;
        ArrayList<Double> valColl = new ArrayList<Double>();
        for (PassageAvgVO paVO : dsVO.getTumorDatas()) {
            if (paVO.getMean() != null) {
                valColl.add(paVO.getMean());
                sum += paVO.getMean();
                sum_sqd += Math.pow(paVO.getMean(), 2);
                count++;
            }
        }
        if (valColl.size() > 0) {
            minVal = Collections.min(valColl);
            maxVal = Collections.max(valColl);
            mean = sum / count;
            dsVO.setMean(mean);
            dsVO.setStandardDeviation(Math.sqrt((sum_sqd / count) - Math.pow(sum / count, 2)));
            dsVO.setCountTumors(count);
            dsVO.setMinVal(minVal);
            dsVO.setMaxVal(maxVal);
            //for handling unit scaling
            Double max_diff = maxVal - minVal;
            ArrayList<Double> deltaColl = new ArrayList<Double>();
            for (PassageAvgVO trVO : dsVO.getTumorDatas()) {
                if (trVO.getMean() != null) {
                    the_val = trVO.getMean();
                    delta = the_val - mean;
                    deltaColl.add(delta);
                    //set delta
                    trVO.setDelta(delta);
                    //scale to -1 to 1
                    if (minVal == maxVal) {
                        trVO.setUnitDeltaValue(0d);
                    } else {
                        trVO.setUnitDeltaValue(-1 + ((the_val - minVal) / max_diff) * 2);
                    }
                }
            }
            minDelta = Collections.min(deltaColl);
            maxDelta = Collections.max(deltaColl);
            dsVO.setMinDelta(minDelta);
            dsVO.setMaxDelta(maxDelta);
        }
    }
}
