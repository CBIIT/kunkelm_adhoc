/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.mwkcharting;

import mwk.datasystem.vo.CmpdFragmentPChemVO;
import mwk.datasystem.vo.CmpdListMemberVO;

/**
 *
 * @author mwkunkel
 */
public class PropertyUtilities {

    public static float getPropertyAsFloat(CmpdListMemberVO clmVO, String propertyName) {

        Double dblRtn = Double.NaN;

        CmpdFragmentPChemVO pchemVO = clmVO.getCmpd().getParentFragment().getCmpdFragmentPChem();

        if (propertyName.equals("alogp")) {
            if (pchemVO.getTheALogP() != null) {
                if (!Double.isNaN(pchemVO.getTheALogP())) {
                    dblRtn = pchemVO.getTheALogP();
                }
            }
        }

        if (propertyName.equals("logd")) {
            if (pchemVO.getLogD() != null) {
                if (!Double.isNaN(pchemVO.getLogD())) {
                    dblRtn = pchemVO.getLogD();
                }
            }
        }

        if (propertyName.equals("hba")) {
            if (pchemVO.getCountHydBondAcceptors() != null) {
                if (!Double.isNaN(pchemVO.getCountHydBondAcceptors())) {
                    dblRtn = pchemVO.getCountHydBondAcceptors().doubleValue();
                }
            }
        }

        if (propertyName.equals("hbd")) {
            if (pchemVO.getCountHydBondDonors() != null) {
                if (!Double.isNaN(pchemVO.getCountHydBondDonors())) {
                    dblRtn = pchemVO.getCountHydBondDonors().doubleValue();
                }
            }
        }

        if (propertyName.equals("sa")) {
            if (pchemVO.getSurfaceArea() != null) {
                if (!Double.isNaN(pchemVO.getSurfaceArea())) {
                    dblRtn = pchemVO.getSurfaceArea();
                }
            }
        }

        if (propertyName.equals("mw")) {
            if (pchemVO.getMolecularWeight() != null) {
                if (!Double.isNaN(pchemVO.getMolecularWeight())) {
                    dblRtn = pchemVO.getMolecularWeight();
                }
            }
        }

        if (Double.isNaN(dblRtn)) {
            return Float.NaN;
        } else {
            return dblRtn.floatValue();
        }

    }

}
