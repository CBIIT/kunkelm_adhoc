/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.vo.CmpdListVO;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class ListContentController508 implements Serializable {

    private String listName;
    //
    private List<String> drugNames;
    private List<String> aliases;
    private List<String> cases;
    private List<String> cmpdSets;
    private List<String> nscs;
    private List<String> projectCodes;
    private List<String> originators;
    private List<String> plates;
    private List<String> targets;
    //
    private String drugNameTextArea;
    private String aliasTextArea;
    private String casTextArea;
    private String cmpdSetTextArea;
    private String nscTextArea;
    private String projectCodeTextArea;
    private String originatorTextArea;
    private String plateTextArea;
    private String targetTextArea;
    //
    // pChem all String since coming from the form, and handling manually in queries
    private String mw_min_form;
    private String mw_max_form;
    private String alogp_min_form;
    private String alogp_max_form;
    private String logd_min_form;
    private String logd_max_form;
    private String sa_min_form;
    private String sa_max_form;
    private String psa_min_form;
    private String psa_max_form;
    private String hba_min_form;
    private String hba_max_form;
    private String hbd_min_form;
    private String hbd_max_form;
    //
    // reach-through to sessionController
    @ManagedProperty(value = "#{sessionController508}")
    private SessionController508 sessionController;

    public void setSessionController(SessionController508 sessionController) {
        this.sessionController = sessionController;
    }
    // reach-through to listManagerController
    @ManagedProperty(value = "#{listManagerController508}")
    private ListManagerController508 listManagerController;

    public void setListManagerController(ListManagerController508 listManagerController) {
        this.listManagerController = listManagerController;
    }

    public String performCreateListBySearch() {

        // check for nulls, reset fields
        if (this.nscs == null) {
            this.nscs = new ArrayList<String>();
        }
        nscs.clear();

        if (this.projectCodes == null) {
            this.projectCodes = new ArrayList<String>();
        }
        projectCodes.clear();

        // parse text areas

        String[] splitStrings = null;
        String fixedString = null;
        int i;

        String delimiters = "[\\n\\r\\t\\s,]+";

        splitStrings = this.nscTextArea.split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            fixedString = splitStrings[i].replaceAll("[^0-9]", "");
            if (fixedString.length() > 0) {
                this.nscs.add(fixedString);
            }
        }

        splitStrings = this.projectCodeTextArea.split(delimiters);
        for (i = 0; i < splitStrings.length; i++) {
            this.projectCodes.add(splitStrings[i]);
        }

        // nscs to Integers
        ArrayList<Integer> nscIntList = new ArrayList<Integer>();
        for (String s : this.nscs) {
            try {
                nscIntList.add(Integer.valueOf(s));
            } catch (Exception e) {
            }
        }

        HelperCmpd cmpdHelper = new HelperCmpd();

        CmpdListVO tempCLvo = cmpdHelper.createCmpdListByNscs(this.listName, nscIntList, this.sessionController.getLoggedUser());

        // now fetch the list

        HelperCmpdList listHelper = new HelperCmpdList();

        CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(tempCLvo.getCmpdListId(), Boolean.TRUE);

        this.listManagerController.getAvailableLists().add(clVO);

        this.listManagerController.setSelectedList(clVO);

        return "/pages508/selectedListTable?faces-redirect=true";

    }

    public void appendTodrugNameTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (this.drugNameTextArea == null) {
            this.drugNameTextArea = "";
        }
        this.drugNameTextArea = item + " " + this.drugNameTextArea;
    }

    public void appendToaliasTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (this.aliasTextArea == null) {
            this.aliasTextArea = "";
        }
        this.aliasTextArea = item + " " + this.aliasTextArea;
    }

    public void appendTocasTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (this.casTextArea == null) {
            this.casTextArea = "";
        }
        this.casTextArea = item + " " + this.casTextArea;
    }

    public void appendTocmpdSetTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (this.cmpdSetTextArea == null) {
            this.cmpdSetTextArea = "";
        }
        this.cmpdSetTextArea = item + " " + this.cmpdSetTextArea;
    }

    public void appendTonscTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (this.nscTextArea == null) {
            this.nscTextArea = "";
        }
        this.nscTextArea = item + " " + this.nscTextArea;
    }

    public void appendToprojectCodeTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (this.projectCodeTextArea == null) {
            this.projectCodeTextArea = "";
        }
        this.projectCodeTextArea = item + " " + this.projectCodeTextArea;
    }

    public void appendTooriginatorTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (this.originatorTextArea == null) {
            this.originatorTextArea = "";
        }
        this.originatorTextArea = item + " " + this.originatorTextArea;
    }

    public void appendToplateTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (this.plateTextArea == null) {
            this.plateTextArea = "";
        }
        this.plateTextArea = item + " " + this.plateTextArea;
    }

    public void appendTotargetTextArea(SelectEvent event) {
        Object item = event.getObject();
        if (this.targetTextArea == null) {
            this.targetTextArea = "";
        }
        this.targetTextArea = item + " " + this.targetTextArea;
    }

    public String getListName() {
        return listName;
    }

    public void setListName(String listName) {
        this.listName = listName;
    }

    public List<String> getDrugNames() {
        return drugNames;
    }

    public void setDrugNames(List<String> drugNames) {
        this.drugNames = drugNames;
    }

    public List<String> getAliases() {
        return aliases;
    }

    public void setAliases(List<String> aliases) {
        this.aliases = aliases;
    }

    public List<String> getCases() {
        return cases;
    }

    public void setCases(List<String> cases) {
        this.cases = cases;
    }

    public List<String> getCmpdSets() {
        return cmpdSets;
    }

    public void setCmpdSets(List<String> cmpdSets) {
        this.cmpdSets = cmpdSets;
    }

    public List<String> getNscs() {
        return nscs;
    }

    public void setNscs(List<String> nscs) {
        this.nscs = nscs;
    }

    public List<String> getProjectCodes() {
        return projectCodes;
    }

    public void setProjectCodes(List<String> projectCodes) {
        this.projectCodes = projectCodes;
    }

    public List<String> getOriginators() {
        return originators;
    }

    public void setOriginators(List<String> originators) {
        this.originators = originators;
    }

    public List<String> getPlates() {
        return plates;
    }

    public void setPlates(List<String> plates) {
        this.plates = plates;
    }

    public List<String> getTargets() {
        return targets;
    }

    public void setTargets(List<String> targets) {
        this.targets = targets;
    }

    public String getDrugNameTextArea() {
        return drugNameTextArea;
    }

    public void setDrugNameTextArea(String drugNameTextArea) {
        this.drugNameTextArea = drugNameTextArea;
    }

    public String getAliasTextArea() {
        return aliasTextArea;
    }

    public void setAliasTextArea(String aliasTextArea) {
        this.aliasTextArea = aliasTextArea;
    }

    public String getCasTextArea() {
        return casTextArea;
    }

    public void setCasTextArea(String casTextArea) {
        this.casTextArea = casTextArea;
    }

    public String getCmpdSetTextArea() {
        return cmpdSetTextArea;
    }

    public void setCmpdSetTextArea(String cmpdSetTextArea) {
        this.cmpdSetTextArea = cmpdSetTextArea;
    }

    public String getNscTextArea() {
        return nscTextArea;
    }

    public void setNscTextArea(String nscTextArea) {
        this.nscTextArea = nscTextArea;
    }

    public String getProjectCodeTextArea() {
        return projectCodeTextArea;
    }

    public void setProjectCodeTextArea(String projectCodeTextArea) {
        this.projectCodeTextArea = projectCodeTextArea;
    }

    public String getOriginatorTextArea() {
        return originatorTextArea;
    }

    public void setOriginatorTextArea(String originatorTextArea) {
        this.originatorTextArea = originatorTextArea;
    }

    public String getPlateTextArea() {
        return plateTextArea;
    }

    public void setPlateTextArea(String plateTextArea) {
        this.plateTextArea = plateTextArea;
    }

    public String getTargetTextArea() {
        return targetTextArea;
    }

    public void setTargetTextArea(String targetTextArea) {
        this.targetTextArea = targetTextArea;
    }

    public String getMw_min_form() {
        return mw_min_form;
    }

    public void setMw_min_form(String mw_min_form) {
        this.mw_min_form = mw_min_form;
    }

    public String getMw_max_form() {
        return mw_max_form;
    }

    public void setMw_max_form(String mw_max_form) {
        this.mw_max_form = mw_max_form;
    }

    public String getAlogp_min_form() {
        return alogp_min_form;
    }

    public void setAlogp_min_form(String alogp_min_form) {
        this.alogp_min_form = alogp_min_form;
    }

    public String getAlogp_max_form() {
        return alogp_max_form;
    }

    public void setAlogp_max_form(String alogp_max_form) {
        this.alogp_max_form = alogp_max_form;
    }

    public String getLogd_min_form() {
        return logd_min_form;
    }

    public void setLogd_min_form(String logd_min_form) {
        this.logd_min_form = logd_min_form;
    }

    public String getLogd_max_form() {
        return logd_max_form;
    }

    public void setLogd_max_form(String logd_max_form) {
        this.logd_max_form = logd_max_form;
    }

    public String getSa_min_form() {
        return sa_min_form;
    }

    public void setSa_min_form(String sa_min_form) {
        this.sa_min_form = sa_min_form;
    }

    public String getSa_max_form() {
        return sa_max_form;
    }

    public void setSa_max_form(String sa_max_form) {
        this.sa_max_form = sa_max_form;
    }

    public String getPsa_min_form() {
        return psa_min_form;
    }

    public void setPsa_min_form(String psa_min_form) {
        this.psa_min_form = psa_min_form;
    }

    public String getPsa_max_form() {
        return psa_max_form;
    }

    public void setPsa_max_form(String psa_max_form) {
        this.psa_max_form = psa_max_form;
    }

    public String getHba_min_form() {
        return hba_min_form;
    }

    public void setHba_min_form(String hba_min_form) {
        this.hba_min_form = hba_min_form;
    }

    public String getHba_max_form() {
        return hba_max_form;
    }

    public void setHba_max_form(String hba_max_form) {
        this.hba_max_form = hba_max_form;
    }

    public String getHbd_min_form() {
        return hbd_min_form;
    }

    public void setHbd_min_form(String hbd_min_form) {
        this.hbd_min_form = hbd_min_form;
    }

    public String getHbd_max_form() {
        return hbd_max_form;
    }

    public void setHbd_max_form(String hbd_max_form) {
        this.hbd_max_form = hbd_max_form;
    }
}
