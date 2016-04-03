/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import mwk.microxeno.vo.PassageVO;
import mwk.microxeno.vo.AffymetrixIdentifierVO;
import mwk.microxeno.vo.PassageAvgSetVO;
import mwk.microxeno.vo.PassageAvgVO;
import mwk.microxeno.vo.PassageIdentifierVO;
import org.apache.commons.math3.stat.StatUtils;
import org.apache.commons.math3.stat.descriptive.moment.StandardDeviation;
import org.apache.log4j.Logger;

/**
 *
 * @author mwkunkel
 */
public class CollateDataSetUtil implements Serializable {

    private static final Logger lgr = Logger.getLogger("GLOBAL");
    private static final long serialVersionUID = 3337556889416886488L;
    private static NumberFormat nf2;

    static {
        nf2 = DecimalFormat.getInstance();
        nf2.setMinimumFractionDigits(2);
        nf2.setMaximumFractionDigits(2);
    }

    public static PassageIdentifierVO createPassageIdentifier(PassageVO incoming) {
        PassageIdentifierVO rtn = new PassageIdentifierVO();
        rtn.setAffymetrixIdentifier(incoming.getAffymetrixIdentifier());
        rtn.setPassage(incoming.getPassage());
        return rtn;
    }

    public static ArrayList<PassageAvgVO> aggregateByPassage(List<PassageVO> incomingList) {
        ArrayList<PassageAvgVO> rtnList = new ArrayList<PassageAvgVO>();
        HashMap<ReplicateAggregator, PassageAvgVO> map = new HashMap<ReplicateAggregator, PassageAvgVO>();
        for (PassageVO adVO : incomingList) {
            ReplicateAggregator ra = new ReplicateAggregator(
                    adVO.getAffymetrixIdentifier(),
                    adVO.getTumor(),
                    adVO.getPassage(),
                    adVO);
            if (map.containsKey(ra)) {
                map.get(ra).getReplicates().add(adVO);
            } else {
                PassageAvgVO paVO = new PassageAvgVO();
                paVO.setPassageIdentifier(createPassageIdentifier(adVO));
                paVO.setTumor(adVO.getTumor());
                ArrayList<PassageVO> dataList = new ArrayList<PassageVO>();
                dataList.add(adVO);
                paVO.setReplicates(dataList);
                map.put(ra, paVO);
            }
        }
        StandardDeviation sd = new StandardDeviation();
        // step through the map and update
        for (ReplicateAggregator ra : map.keySet()) {
            ArrayList<Double> values = new ArrayList<Double>();
            PassageAvgVO paVO = map.get(ra);
            for (PassageVO adVO : paVO.getReplicates()) {
                values.add(adVO.getValue());
            }
            double[] valArr = new double[values.size()];
            for (int i = 0; i < values.size(); i++) {
                valArr[i] = values.get(i).doubleValue();
            }
            paVO.setCountReplicates(valArr.length);
            paVO.setMean(StatUtils.mean(valArr));
            paVO.setStandardDeviation(sd.evaluate(valArr));
            rtnList.add(paVO);
        }
        return rtnList;
    }

    public static ArrayList<PassageAvgSetVO> collateDataSet(List<PassageAvgVO> passageAggregateList) {

        ArrayList<PassageAvgSetVO> aedsList = new ArrayList<PassageAvgSetVO>();

        try {
            HashMap<PassageIdentifierVO, PassageAvgSetVO> map = new HashMap<PassageIdentifierVO, PassageAvgSetVO>();
            for (PassageAvgVO paVO : passageAggregateList) {
                if (map.containsKey(paVO.getPassageIdentifier())) {
                    map.get(paVO.getPassageIdentifier()).getTumorDatas().add(paVO);
                } else {
                    PassageAvgSetVO dsVO = new PassageAvgSetVO();
                    dsVO.setPassageIdentifier(paVO.getPassageIdentifier());
                    ArrayList<PassageAvgVO> passages = new ArrayList<PassageAvgVO>();
                    passages.add(paVO);
                    dsVO.setTumorDatas(passages);
                    map.put(dsVO.getPassageIdentifier(), dsVO);
                }
            }
            
            // convert the map to list
            aedsList = new ArrayList<PassageAvgSetVO>(map.values());
            
            // do calculations
            for (PassageAvgSetVO pdsVO : aedsList) {
                doCalculate(pdsVO);
            }
            
            // sort
            Collections.sort(aedsList, new Comparators.PassageAvgComparator());
            
            
        } catch (Exception e) {
            lgr.error("Exception in CollateDataSetUtil");
            e.printStackTrace();
        }
        return aedsList;
    }

    public static void doCalculate(PassageAvgSetVO dsVO) {
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
            dsVO.setFormattedMean(nf2.format(mean));
            dsVO.setStandardDeviation(Math.sqrt((sum_sqd / count) - Math.pow(sum / count, 2)));
            dsVO.setCountTumors(count);
            dsVO.setMinVal(minVal);
            dsVO.setMaxVal(maxVal);
            //for handling unit scaling
            Double max_diff = maxVal - minVal;
            ArrayList<Double> deltaColl = new ArrayList<Double>();
            for (PassageAvgVO paVO : dsVO.getTumorDatas()) {
                if (paVO.getMean() != null) {
                    the_val = paVO.getMean();
                    delta = the_val - mean;
                    deltaColl.add(delta);
                    //set delta
                    paVO.setDelta(delta);
                    //scale to -1 to 1
                    if (minVal == maxVal) {
                        paVO.setUnitDeltaValue(0d);
                    } else {
                        paVO.setUnitDeltaValue(-1 + ((the_val - minVal) / max_diff) * 2);
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
