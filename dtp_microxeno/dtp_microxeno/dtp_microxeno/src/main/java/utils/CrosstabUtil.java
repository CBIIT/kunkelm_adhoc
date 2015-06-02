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
import mwk.microxeno.vo.PassageAggregateVO;
import mwk.microxeno.vo.PassageDataSetVO;
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
  
    public static CrosstabModel<PassageIdentifierVO, TumorVO, PassageAggregateVO> makeCrosstabModel(List<PassageAggregateVO> incomingList) {
       
        CrosstabModel<PassageIdentifierVO, TumorVO, PassageAggregateVO> rtn = new CrosstabModel<PassageIdentifierVO, TumorVO, PassageAggregateVO>();
        
        TwoDHashMap<PassageIdentifierVO, TumorVO, PassageAggregateVO> map = new TwoDHashMap<PassageIdentifierVO, TumorVO, PassageAggregateVO>();
        
        for (PassageAggregateVO paVO : incomingList) {
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

        rtn.setGridXY(new PassageAggregateVO[xList.size()][yList.size()]);

        int rowCnt = 0;
        for (TumorVO tVO : yList) {
            int colCnt = 0;
            for (PassageIdentifierVO piVO : xList) {
                PassageAggregateVO paVO = map.get(piVO, tVO);
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
    private static void doCalculate(PassageDataSetVO dsVO) {
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
        for (PassageAggregateVO paVO : dsVO.getTumorDatas()) {
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
            for (PassageAggregateVO trVO : dsVO.getTumorDatas()) {
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
