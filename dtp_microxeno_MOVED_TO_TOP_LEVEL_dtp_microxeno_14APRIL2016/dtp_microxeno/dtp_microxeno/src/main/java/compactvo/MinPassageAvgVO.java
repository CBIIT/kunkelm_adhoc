/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compactvo;

import mwk.microxeno.vo.PassageAvgVO;

/**
 *
 * @author mwkunkel
 */
public class MinPassageAvgVO {

    String gs;  // geneSymbol
    String psi; // probeSetId
    String tn;  // tumorName
    String tt;  // tumorType
    String p;   // passage
    Integer cr;     // countReplicates    
    Double m;   // mean
    Double sd;  // standardDeviation
    Double d;   // delta
    Double ud;  // unitDelta

    public MinPassageAvgVO(PassageAvgVO paVO) {
        this.gs = paVO.getPassageIdentifier().getAffymetrixIdentifier().getGeneSymbol();
        this.psi = paVO.getPassageIdentifier().getAffymetrixIdentifier().getProbeSetId();
        this.tn = paVO.getTumor().getTumorName();
        this.tt = paVO.getTumor().getTumorType();
        this.p = paVO.getPassageIdentifier().getPassage();
        this.cr = paVO.getCountReplicates();
        this.m = paVO.getMean();
        this.sd = paVO.getStandardDeviation();
        this.d = paVO.getDelta();
        this.ud = paVO.getUnitDeltaValue();
    }

}
