/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.util.List;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mwkunkel
 */
public class ListLogicContainer {

    private CmpdListVO listA;
    private CmpdListVO listB;

    private List<CmpdVO> cmpdsListAnotListB;
    private List<CmpdVO> cmpdsListAandListB;
    private List<CmpdVO> cmpdsListAorListB;

    public ListLogicContainer(CmpdListVO listA, CmpdListVO listB) {
        this.listA = listA;
        this.listB = listB;
    }

//<editor-fold defaultstate="collapsed" desc="comment">
    public CmpdListVO getListA() {
        return listA;
    }

    public void setListA(CmpdListVO listA) {
        this.listA = listA;
    }

    public CmpdListVO getListB() {
        return listB;
    }

    public void setListB(CmpdListVO listB) {
        this.listB = listB;
    }

    public List<CmpdVO> getCmpdsListAnotListB() {
        return cmpdsListAnotListB;
    }

    public void setCmpdsListAnotListB(List<CmpdVO> cmpdsListAnotListB) {
        this.cmpdsListAnotListB = cmpdsListAnotListB;
    }

    public List<CmpdVO> getCmpdsListAandListB() {
        return cmpdsListAandListB;
    }

    public void setCmpdsListAandListB(List<CmpdVO> cmpdsListAandListB) {
        this.cmpdsListAandListB = cmpdsListAandListB;
    }

    public List<CmpdVO> getCmpdsListAorListB() {
        return cmpdsListAorListB;
    }

    public void setCmpdsListAorListB(List<CmpdVO> cmpdsListAorListB) {
        this.cmpdsListAorListB = cmpdsListAorListB;
    }

//</editor-fold>    
}
