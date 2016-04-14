/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.util.ArrayList;
import java.util.List;
import mwk.microxeno.domain.AffymetrixData;
import mwk.microxeno.domain.AffymetrixIdentifier;
import mwk.microxeno.domain.Tumor;
import mwk.microxeno.vo.PassageVO;
import mwk.microxeno.vo.AffymetrixIdentifierVO;
import mwk.microxeno.vo.PassageIdentifierVO;
import mwk.microxeno.vo.TumorVO;

/**
 *
 * @author mwkunkel
 */
public class TranslateAndroToVO {

    public static ArrayList<PassageVO> translateAffymetrixDatas(List<AffymetrixData> incoming) {
        ArrayList<PassageVO> rtnList = new ArrayList<PassageVO>();
        for (AffymetrixData aed : incoming) {
            rtnList.add(translateAffymetrixData(aed));
        }
        return rtnList;
    }

    public static PassageVO translateAffymetrixData(AffymetrixData entity) {
        PassageVO rtnVO = new PassageVO();
        if (entity != null) {
            rtnVO.setAffymetrixIdentifier(translateAffymetrixIdentifier(entity.getAffymetrixIdentifier()));
            rtnVO.setTumor(translateTumor(entity.getTumor()));
            rtnVO.setEaFile(entity.getEaFile());
            rtnVO.setPassage(entity.getPassage());
            rtnVO.setReplicate(entity.getReplicate());
            rtnVO.setValue(entity.getValue());
        }
        return rtnVO;
    }

    public static TumorVO translateTumor(Tumor entity) {
        TumorVO rtn = new TumorVO();
        if (entity != null) {
            rtn.setTumorName(entity.getTumorName());
            rtn.setTumorType(entity.getTumorType().getTumorType());
        }
        return rtn;
    }

    public static AffymetrixIdentifierVO translateAffymetrixIdentifier(AffymetrixIdentifier entity) {
        AffymetrixIdentifierVO rtn = new AffymetrixIdentifierVO();
        if (entity != null) {
            rtn.setAccession(entity.getAccession());
            rtn.setGeneSymbol(entity.getGeneSymbol());
            rtn.setProbeSetId(entity.getProbeSetId());
            rtn.setGeneTitle(entity.getGeneTitle());
        }
        return rtn;
    }

    public static PassageIdentifierVO translatePassageIdentifier(AffymetrixData entity) {
        PassageIdentifierVO rtn = new PassageIdentifierVO();
        if (entity != null) {
            rtn.setAffymetrixIdentifier(translateAffymetrixIdentifier(entity.getAffymetrixIdentifier()));
            rtn.setPassage(entity.getPassage());
        }
        return rtn;
    }

}
