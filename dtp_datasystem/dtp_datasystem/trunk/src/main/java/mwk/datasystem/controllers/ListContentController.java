/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HelperCmpdListMember;
import mwk.datasystem.util.HelperStructure;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.apache.commons.io.IOUtils;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.interfaces.IAtomContainer;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class ListContentController implements Serializable {

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
    private String smilesString;
    private String molFile;
    //
    private String nscForJChemPaintLoad;
    private String smilesForJChemPaintLoad;
    private String ctabForJChemPaintLoad;
    // file uploading
    UploadedFile uploadedFile;
    //
    // list manipulation
    //
    List<CmpdListMemberVO> selectedActiveListMembers;
    CmpdListVO targetList;
    //
    // reach-through to sessionController
    @ManagedProperty(value = "#{sessionController}")
    private SessionController sessionController;

    public void setSessionController(SessionController sessionController) {
        this.sessionController = sessionController;
    }
    // reach-through to listManagerController
    @ManagedProperty(value = "#{listManagerController}")
    private ListManagerController listManagerController;

    public void setListManagerController(ListManagerController listManagerController) {
        this.listManagerController = listManagerController;
    }

    public void onRowSelect(SelectEvent evt) {

        try {

            CmpdListMemberVO clmVO = (CmpdListMemberVO) evt.getObject();

            System.out.println("Select: " + clmVO.getCmpd().getNsc() + " " + clmVO.getCmpd().getAdHocCmpdId());

            clmVO.setIsSelected(Boolean.TRUE);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void onRowDeSelect(SelectEvent evt) {

        try {

            CmpdListMemberVO clmVO = (CmpdListMemberVO) evt.getObject();

            System.out.println("Delect: " + clmVO.getCmpd().getNsc() + " " + clmVO.getCmpd().getAdHocCmpdId());

            clmVO.setIsSelected(Boolean.FALSE);


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     *
     * @return For checkboxes outside of dataTable
     */
    public String performMySelect() {

        StringBuilder msgBuilder = new StringBuilder();

        // check the activeList for selectedMembers        
        if (this.selectedActiveListMembers == null) {
            msgBuilder.append("selectedActiveListMembers is null.  new ArrayList().");
            System.out.println("selectedActiveListMembers is null.  new ArrayList().");
            this.selectedActiveListMembers = new ArrayList<CmpdListMemberVO>();
        } else {
            msgBuilder.append("selectedActiveListMembers is NOT null.  list.clear().");
            System.out.println("selectedActiveListMembers is NOT null.  list.clear().");
            this.selectedActiveListMembers.clear();
        }

        for (CmpdListMemberVO clmVO : this.listManagerController.getActiveList().getCmpdListMembers()) {
            if (clmVO.getIsSelected() != null && clmVO.getIsSelected()) {
                msgBuilder.append("selected: " + clmVO.getCmpd().getNsc() + " " + clmVO.getCmpd().getAdHocCmpdId());
                System.out.println("selected: " + clmVO.getCmpd().getNsc() + " " + clmVO.getCmpd().getAdHocCmpdId());
                this.selectedActiveListMembers.add(clmVO);
            }
        }

        FacesMessage msg = new FacesMessage("Selected List Members: ", msgBuilder.toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);

        return "/webpages/activeListTable?faces-redirect=true";

    }

    //
    public String performDeleteFromActiveList() {

        HelperCmpdListMember helper = new HelperCmpdListMember();

        helper.deleteCmpdListMembers(this.targetList, this.selectedActiveListMembers, this.sessionController.getLoggedUser());

        // now fetch the list        
        HelperCmpdList listHelper = new HelperCmpdList();
        CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(this.listManagerController.getActiveList().getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());

        this.listManagerController.setActiveList(clVO);

        return "/webpages/activeListTable?faces-redirect=true";
    }

    public String performCreateNewListFromActiveList() {
        return null;
    }

    public String performCreateNewListFromSelectedListMembers() {

        HelperCmpdList helper = new HelperCmpdList();

        return null;

    }

    public String performAppendSelectedToExistingList() {

        HelperCmpdListMember helper = new HelperCmpdListMember();

        helper.appendCmpdListMembers(this.targetList, this.selectedActiveListMembers, this.sessionController.getLoggedUser());

        // have to UPDATE the list   
        HelperCmpdList listHelper = new HelperCmpdList();
        CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(this.targetList.getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());

        this.listManagerController.setActiveList(clVO);

        // is this really the way to do this?
        this.listManagerController.performUpdateAvailableLists();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    /**
     *
     * @param smiles
     * @return Helper method for loading ctab to jChemPaint
     */
    private String molFromSmiles(String smiles) {

        String rtn = "";

        try {

            SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
            
            IMolecule molecule = sp.parseSmiles(smiles);

            StructureDiagramGenerator sdg = new StructureDiagramGenerator();

            sdg.setMolecule(molecule);
            try {
                sdg.generateCoordinates();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            IMolecule fixedMol = sdg.getMolecule();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            MDLV2000Writer ctabWriter = new MDLV2000Writer(baos);

            ctabWriter.write(fixedMol);

            ctabWriter.close();
            baos.close();

            //System.out.println("ctab is: " + baos.toString());

            rtn = baos.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return rtn;

    }

    public String performLoadJChemPaintByNsc() {

        HelperCmpd helper = new HelperCmpd();

        Integer nscInt = Integer.valueOf(this.nscForJChemPaintLoad);

        CmpdVO cVO = helper.getSingleCmpdByNsc(nscInt, this.sessionController.getLoggedUser());

        if (cVO.getParentFragment().getCmpdFragmentStructure().getSmiles().isEmpty()) {
            this.smilesForJChemPaintLoad = null;
            this.ctabForJChemPaintLoad = null;
        } else {
            String smiles = cVO.getParentFragment().getCmpdFragmentStructure().getSmiles();
            this.smilesForJChemPaintLoad = smiles;
            this.ctabForJChemPaintLoad = molFromSmiles(smiles);
        }

        return null;

    }

    public String performFileUpload() {

        try {

            Random randomGenerator = new Random();

            String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(this.uploadedFile.getFileName());

            System.out.println(" fileName: " + this.uploadedFile.getFileName() + " fileSize: " + this.uploadedFile.getSize() + " realPath: " + realPath);

            File systemFile = new File(realPath);

            FileOutputStream fos = new FileOutputStream(systemFile, false);

            byte[] byteArray = IOUtils.toByteArray(this.uploadedFile.getInputstream());

            fos.write(byteArray);

            fos.flush();
            fos.close();

            FacesMessage msg = new FacesMessage(
                    FacesMessage.SEVERITY_INFO,
                    "Uploaded File",
                    "fileName: " + this.uploadedFile.getFileName() + " fileSize: " + this.uploadedFile.getSize());

            FacesContext.getCurrentInstance().addMessage(null, msg);

            MoleculeParser mp = new MoleculeParser();

            File sdFile = new File(realPath);

            ArrayList<AdHocCmpd> adHocCmpdList = mp.parseSDF(sdFile);

            HelperCmpdList listHelper = new HelperCmpdList();

            CmpdListVO clVO_sparse = listHelper.deNovoCmpdListFromAdHocCmpds(adHocCmpdList, this.listName, this.sessionController.getLoggedUser());

            // new fetch the list

            CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(clVO_sparse.getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());

            this.listManagerController.getAvailableLists().add(clVO);
            this.listManagerController.setActiveList(clVO);

            System.out.println("UploadCmpds contains: " + clVO_sparse.getCountListMembers() + " cmpds");

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/webpages/activeListTable?faces-redirect=true";

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

        // MWK TODO catch negative cmpdListId => problem

        Long cmpdListId = cmpdHelper.createCmpdListByNscs(this.listName, nscIntList, null, this.sessionController.getLoggedUser());

        // now fetch the list        
        HelperCmpdList listHelper = new HelperCmpdList();
        CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, this.sessionController.getLoggedUser());

        this.listManagerController.getAvailableLists().add(clVO);
        this.listManagerController.setActiveList(clVO);

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performExactMatchSearch() {

        HelperStructure strcHelper = new HelperStructure();

        List<Integer> nscIntList = strcHelper.findNSCsByExactMatch(this.smilesString);

        HelperCmpd cmpdHelper = new HelperCmpd();

        List<CmpdVO> structureSearchResults = cmpdHelper.getCmpdsByNsc(nscIntList, this.sessionController.getLoggedUser());

        List<CmpdListMemberVO> clmList = new ArrayList<CmpdListMemberVO>();

        for (CmpdVO cVO : structureSearchResults) {
            CmpdListMemberVO clmVO = new CmpdListMemberVO();
            clmVO.setCmpd(cVO);
            clmList.add(clmVO);
        }

        CmpdListVO clVO = new CmpdListVO();

        clVO.setListName("Exact Match Search Results");
        clVO.setListOwner(this.sessionController.getLoggedUser());

        clVO.setCmpdListMembers(clmList);

        this.listManagerController.setTempList(clVO);

        return null;

    }

    public String performSubstructureSearch() {

        System.out.println("Now in performSubstructureSearch()");

        HelperStructure strcHelper = new HelperStructure();

        List<Integer> nscIntList = strcHelper.findNSCsBySmilesSubstructure(this.smilesString);

        System.out.println("Size of nscIntList: " + nscIntList.size());

        Date now = new Date();

        if (this.listName == null || this.listName.length() == 0) {
            this.listName = "structureSearchResults " + now.toString();
        }

        HelperCmpd cmpdHelper = new HelperCmpd();
        Long cmpdListId = cmpdHelper.createCmpdListByNscs(this.listName, nscIntList, this.smilesString, this.sessionController.getLoggedUser());

        // have to fetch the list so that the compound details will be populated
        // 
        HelperCmpdList listHelper = new HelperCmpdList();
        CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, this.sessionController.getLoggedUser());

        listManagerController.getAvailableLists().add(clVO);
        listManagerController.setActiveList(clVO);

        return "/webpages/activeListTable.xhtml?faces-redirect=true";

    }

    public String performSmartsSearch() {

        String smartsString = this.smilesString;

        // double bonds to any bond
        smartsString = smartsString.replaceAll("=", "~");

        // disguise chlorine as HIDDEN (no "C" or "c"), replace C and c then restore chlorine
        smartsString = smartsString.replaceAll("Cl", "HIDDEN").replaceAll("C", "[#6]").replaceAll("c", "[#6]").replaceAll("HIDDEN", "Cl");

        // same exercise for nitrogen, oxygen, sulfur, phosphorus, but no need to disguise other elements
        smartsString = smartsString.replaceAll("N", "[#7]").replaceAll("n", "[#7]");
        smartsString = smartsString.replaceAll("O", "[#8]").replaceAll("o", "[#8]");

        smartsString = smartsString.replaceAll("P", "[#15]").replaceAll("p", "[#15]");
        smartsString = smartsString.replaceAll("S", "[#16]").replaceAll("s", "[#16]");

        FacesMessage msg = new FacesMessage(
                FacesMessage.SEVERITY_INFO,
                "SMILES string and SMARTS string: ",
                smilesString + " " + smartsString);

        FacesContext.getCurrentInstance().addMessage(null, msg);

        System.out.println("smilesString: " + this.smilesString);
        System.out.println("smartsString: " + smartsString);

        HelperStructure strcHelper = new HelperStructure();

        List<Integer> nscIntList = strcHelper.findNSCsBySmartsSubstructure(smartsString);

        HelperCmpd cmpdHelper = new HelperCmpd();
        List<CmpdVO> cmpds = cmpdHelper.getCmpdsByNsc(nscIntList, this.sessionController.getLoggedUser());

        List<CmpdListMemberVO> clmList = new ArrayList<CmpdListMemberVO>();

        for (CmpdVO cVO : cmpds) {
            CmpdListMemberVO clmVO = new CmpdListMemberVO();
            clmVO.setCmpd(cVO);
            clmList.add(clmVO);
        }

        CmpdListVO clVO = new CmpdListVO();

        clVO.setListName("SMARTS Search Results");
        clVO.setListOwner(this.sessionController.getLoggedUser());

        clVO.setCmpdListMembers(clmList);

        this.listManagerController.setTempList(clVO);

        return null;

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

    // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
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

    public String getSmilesString() {
        return smilesString;
    }

    public void setSmilesString(String smilesString) {
        this.smilesString = smilesString;
    }

    public String getMolFile() {
        return molFile;
    }

    public void setMolFile(String molFile) {
        this.molFile = molFile;
    }

    public String getNscForJChemPaintLoad() {
        return nscForJChemPaintLoad;
    }

    public void setNscForJChemPaintLoad(String nscForJChemPaintLoad) {
        this.nscForJChemPaintLoad = nscForJChemPaintLoad;
    }

    public String getSmilesForJChemPaintLoad() {
        return smilesForJChemPaintLoad;
    }

    public void setSmilesForJChemPaintLoad(String smilesForJChemPaintLoad) {
        this.smilesForJChemPaintLoad = smilesForJChemPaintLoad;
    }

    public String getCtabForJChemPaintLoad() {
        return ctabForJChemPaintLoad;
    }

    public void setCtabForJChemPaintLoad(String ctabForJChemPaintLoad) {
        this.ctabForJChemPaintLoad = ctabForJChemPaintLoad;
    }

    public UploadedFile getUploadedFile() {
        return uploadedFile;
    }

    public void setUploadedFile(UploadedFile uploadedFile) {
        this.uploadedFile = uploadedFile;
    }

    public List<CmpdListMemberVO> getSelectedActiveListMembers() {
        return selectedActiveListMembers;
    }

    public void setSelectedActiveListMembers(List<CmpdListMemberVO> selectedActiveListMembers) {
        this.selectedActiveListMembers = selectedActiveListMembers;
    }

    public CmpdListVO getTargetList() {
        return targetList;
    }

    public void setTargetList(CmpdListVO targetList) {
        this.targetList = targetList;
    }
    // </editor-fold>
}
