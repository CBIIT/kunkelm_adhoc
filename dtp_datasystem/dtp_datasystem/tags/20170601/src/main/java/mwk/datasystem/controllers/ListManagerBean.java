/*
 
 
 
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;

@ManagedBean
@SessionScoped
public class ListManagerBean implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    // available lists    
    protected List<CmpdListVO> availableLists;
    protected CmpdListVO selectedAvailableList;
    protected List<CmpdListVO> selectedAvailableLists;
    protected List<CmpdListVO> filteredAvailableLists;
    // active list
    protected CmpdListVO activeList;
    protected CmpdListMemberVO selectedActiveListMember;
    protected List<CmpdListMemberVO> selectedActiveListMembers;
    protected List<CmpdListMemberVO> filteredActiveListMembers;
    //
    protected CmpdListVO listForDelete;   
    protected CmpdFragmentVO selectedCmpdFragment;

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
        availableLists = new ArrayList<CmpdListVO>();
        //Do NOT instantiate lists used as backend filters for PF!
        //filteredAvailableLists = filteredAvailableLists;
        selectedCmpdFragment = selectedCmpdFragment;
        selectedActiveListMembers = new ArrayList<CmpdListMemberVO>();
    }

    //<editor-fold defaultstate="collapsed" desc="comment">
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

//</editor-fold>
}
