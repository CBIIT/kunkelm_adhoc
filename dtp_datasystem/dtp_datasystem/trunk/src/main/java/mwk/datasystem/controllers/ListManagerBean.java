/*
 
 
 
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;

public class ListManagerBean implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    protected List<CmpdListVO> selectedAvailableLists;
    protected CmpdListVO listForDelete;
    protected CmpdListMemberVO selectedActiveListMember;
    protected CmpdListVO selectedAvailableList;
    protected CmpdListVO activeList;
    protected CmpdListVO agnosticList;
    protected List<CmpdListVO> availableLists;
    protected List<CmpdListVO> filteredAvailableLists;
    protected CmpdListMemberVO selectedTempListMember;
    protected List<CmpdListMemberVO> selectedTempListMembers;
    protected CmpdFragmentVO selectedCmpdFragment;
    protected List<CmpdListMemberVO> selectedActiveListMembers;
    protected List<CmpdListMemberVO> filteredActiveListMembers;
    protected CmpdListVO tempList;

    /**
     * Creates a new instance of ListManagerBean
     */
    public ListManagerBean() {
        reset();
    }
 
    public void reset() {
        selectedAvailableLists = new ArrayList<CmpdListVO>();
        listForDelete = new CmpdListVO();
        selectedActiveListMember = new CmpdListMemberVO();
        selectedAvailableList = new CmpdListVO();
        activeList = new CmpdListVO();
        agnosticList = new CmpdListVO();
        availableLists = new ArrayList<CmpdListVO>();
        //Do NOT instantiate lists used as backend filters for PF!
        //filteredAvailableLists = filteredAvailableLists;
        selectedTempListMember = new CmpdListMemberVO();
        selectedTempListMembers = new ArrayList<CmpdListMemberVO>();
        selectedCmpdFragment = selectedCmpdFragment;
        selectedActiveListMembers = new ArrayList<CmpdListMemberVO>();
        tempList = new CmpdListVO();
    }

    public List<CmpdListVO> getSelectedAvailableLists() {
        return selectedAvailableLists;
    }

    public void setSelectedAvailableLists(List<CmpdListVO> selectedAvailableLists) {
        this.selectedAvailableLists = selectedAvailableLists;
    }

    public CmpdListVO getListForDelete() {
        return listForDelete;
    }

    public void setListForDelete(CmpdListVO listForDelete) {
        this.listForDelete = listForDelete;
    }

    public CmpdListMemberVO getSelectedActiveListMember() {
        return selectedActiveListMember;
    }

    public void setSelectedActiveListMember(CmpdListMemberVO selectedActiveListMember) {
        this.selectedActiveListMember = selectedActiveListMember;
    }

    public CmpdListVO getSelectedAvailableList() {
        return selectedAvailableList;
    }

    public void setSelectedAvailableList(CmpdListVO selectedAvailableList) {
        this.selectedAvailableList = selectedAvailableList;
    }

    public CmpdListVO getActiveList() {
        return activeList;
    }

    public void setActiveList(CmpdListVO activeList) {
        this.activeList = activeList;
    }

    public CmpdListVO getAgnosticList() {
        return agnosticList;
    }

    public void setAgnosticList(CmpdListVO agnosticList) {
        this.agnosticList = agnosticList;
    }

    public List<CmpdListVO> getAvailableLists() {
        return availableLists;
    }

    public void setAvailableLists(List<CmpdListVO> availableLists) {
        this.availableLists = availableLists;
    }

    public List<CmpdListVO> getFilteredAvailableLists() {
        return filteredAvailableLists;
    }

    public void setFilteredAvailableLists(List<CmpdListVO> filteredAvailableLists) {
        this.filteredAvailableLists = filteredAvailableLists;
    }

    public CmpdListMemberVO getSelectedTempListMember() {
        return selectedTempListMember;
    }

    public void setSelectedTempListMember(CmpdListMemberVO selectedTempListMember) {
        this.selectedTempListMember = selectedTempListMember;
    }

    public List<CmpdListMemberVO> getSelectedTempListMembers() {
        return selectedTempListMembers;
    }

    public void setSelectedTempListMembers(List<CmpdListMemberVO> selectedTempListMembers) {
        this.selectedTempListMembers = selectedTempListMembers;
    }

    public CmpdFragmentVO getSelectedCmpdFragment() {
        return selectedCmpdFragment;
    }

    public void setSelectedCmpdFragment(CmpdFragmentVO selectedCmpdFragment) {
        this.selectedCmpdFragment = selectedCmpdFragment;
    }

    public List<CmpdListMemberVO> getSelectedActiveListMembers() {
        return selectedActiveListMembers;
    }

    public void setSelectedActiveListMembers(List<CmpdListMemberVO> selectedActiveListMembers) {
        this.selectedActiveListMembers = selectedActiveListMembers;
    }

    public List<CmpdListMemberVO> getFilteredActiveListMembers() {
        return filteredActiveListMembers;
    }

    public void setFilteredActiveListMembers(List<CmpdListMemberVO> filteredActiveListMembers) {
        this.filteredActiveListMembers = filteredActiveListMembers;
    }

    public CmpdListVO getTempList() {
        return tempList;
    }

    public void setTempList(CmpdListVO tempList) {
        this.tempList = tempList;
    }

}
