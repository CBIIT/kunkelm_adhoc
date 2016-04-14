/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compactvo;

import mwk.microxeno.vo.PassageVO;

/**
 *
 * @author mwkunkel
 */
public class MinPassageVO {

    String gs;  // geneSymbol
    String psi; // probeSetId
    String tn;  // tumorName
    String tt;  // tumorType
    String p;   // passage
    String r;   // replicate
    Double v;   // value

    public MinPassageVO(PassageVO pVO) {        
        this.gs = pVO.getAffymetrixIdentifier().getGeneSymbol();
        this.psi = pVO.getAffymetrixIdentifier().getProbeSetId();
        this.tn = pVO.getTumor().getTumorName();
        this.tt = pVO.getTumor().getTumorType();
        this.p = pVO.getPassage();
        this.r = pVO.getReplicate();
        this.v = pVO.getValue();        
    }

    
    
}
