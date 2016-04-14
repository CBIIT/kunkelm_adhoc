/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package meangraph;

/**
 *
 * @author mwkunkel
 */
public class MeanGraphDataShuttle {

    public String panelName;
    public String cellLineName;
    public Double val;
    public String valFlag;
    public Double mean;
    public Double delta;
    public Double minDelta;
    public Double maxDelta;

    public MeanGraphDataShuttle(String panelName, String cellLineName, Double val, String valFlag, Double mean, Double delta, Double minDelta, Double maxDelta) {
        this.panelName = panelName;
        this.cellLineName = cellLineName;
        this.val = val;
        this.valFlag = valFlag;
        this.mean = mean;
        this.delta = delta;
        this.minDelta = minDelta;
        this.maxDelta = maxDelta;
    }

    @Override
    public String toString() {

        return panelName + " " + cellLineName + " " + val + " " + valFlag + " " + mean + " " + delta + " " + minDelta + " " + maxDelta;

    }
}
