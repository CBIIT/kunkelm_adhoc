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
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Random;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import jena.version;
import mwk.datasystem.domain.AdHocCmpd;
import mwk.datasystem.domain.AdHocCmpdFragment;
import mwk.datasystem.domain.Cmpd;
import mwk.datasystem.domain.CmpdList;
import mwk.datasystem.domain.CmpdListMember;
import mwk.datasystem.util.Comparators;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HelperCmpdListMember;
import mwk.datasystem.util.HelperStructure;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.apache.commons.io.IOUtils;
import org.hibernate.Session;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.UploadedFile;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class ListContentController implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;
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
    private String smilesString;
    private String molFile;
    // molecular properties
    private Double min_molecularWeight;
    private Double max_molecularWeight;
    private String molecularFormula;
    private Double min_logD;
    private Double max_logD;
    private Integer min_countHydBondAcceptors;
    private Integer max_countHydBondAcceptors;
    private Integer min_countHydBondDonors;
    private Integer max_countHydBondDonors;
    private Double min_surfaceArea;
    private Double max_surfaceArea;
    private Double min_solubility;
    private Double max_solubility;
    private Integer min_countRings;
    private Integer max_countRings;
    private Integer min_countAtoms;
    private Integer max_countAtoms;
    private Integer min_countBonds;
    private Integer max_countBonds;
    private Integer min_countSingleBonds;
    private Integer max_countSingleBonds;
    private Integer min_countDoubleBonds;
    private Integer max_countDoubleBonds;
    private Integer min_countTripleBonds;
    private Integer max_countTripleBonds;
    private Integer min_countRotatableBonds;
    private Integer max_countRotatableBonds;
    private Integer min_countHydrogenAtoms;
    private Integer max_countHydrogenAtoms;
    private Integer min_countMetalAtoms;
    private Integer max_countMetalAtoms;
    private Integer min_countHeavyAtoms;
    private Integer max_countHeavyAtoms;
    private Integer min_countPositiveAtoms;
    private Integer max_countPositiveAtoms;
    private Integer min_countNegativeAtoms;
    private Integer max_countNegativeAtoms;
    private Integer min_countRingBonds;
    private Integer max_countRingBonds;
    private Integer min_countStereoAtoms;
    private Integer max_countStereoAtoms;
    private Integer min_countStereoBonds;
    private Integer max_countStereoBonds;
    private Integer min_countRingAssemblies;
    private Integer max_countRingAssemblies;
    private Integer min_countAromaticBonds;
    private Integer max_countAromaticBonds;
    private Integer min_countAromaticRings;
    private Integer max_countAromaticRings;
    private Integer min_formalCharge;
    private Integer max_formalCharge;
    private Double min_theALogP;
    private Double max_theALogP;
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

            Molecule molecule = (Molecule) sp.parseSmiles(smiles);

            StructureDiagramGenerator sdg = new StructureDiagramGenerator();

            sdg.setMolecule(molecule);
            try {
                sdg.generateCoordinates();
            } catch (Exception ex) {
                ex.printStackTrace();
            }

            Molecule fixedMol = (Molecule) sdg.getMolecule();

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

        if (cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi().isEmpty()) {
            this.smilesForJChemPaintLoad = null;
            this.ctabForJChemPaintLoad = null;
        } else {
            String smiles = cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi();
            this.smilesForJChemPaintLoad = smiles;
            this.ctabForJChemPaintLoad = molFromSmiles(smiles);
        }

        return null;

    }

    public String performSmilesFileUpload() {

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

            File smilesFile = new File(realPath);

            ArrayList<AdHocCmpd> adHocCmpdList = mp.parseSMILESFile(smilesFile);

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

        // QC on pChemBean, etc.
        System.out.println("QC on settings for molecularPropertiesCriteriaBean");
        System.out.println("min_molecularWeight: " + this.min_molecularWeight);
        System.out.println("max_molecularWeight: " + this.max_molecularWeight);
        System.out.println("molecularFormula: " + this.molecularFormula);
        System.out.println("min_logD: " + this.min_logD);
        System.out.println("max_logD: " + this.max_logD);
        System.out.println("min_countHydBondAcceptors: " + this.min_countHydBondAcceptors);
        System.out.println("max_countHydBondAcceptors: " + this.max_countHydBondAcceptors);
        System.out.println("min_countHydBondDonors: " + this.min_countHydBondDonors);
        System.out.println("max_countHydBondDonors: " + this.max_countHydBondDonors);
        System.out.println("min_surfaceArea: " + this.min_surfaceArea);
        System.out.println("max_surfaceArea: " + this.max_surfaceArea);
        System.out.println("min_solubility: " + this.min_solubility);
        System.out.println("max_solubility: " + this.max_solubility);
        System.out.println("min_countRings: " + this.min_countRings);
        System.out.println("max_countRings: " + this.max_countRings);
        System.out.println("min_countAtoms: " + this.min_countAtoms);
        System.out.println("max_countAtoms: " + this.max_countAtoms);
        System.out.println("min_countBonds: " + this.min_countBonds);
        System.out.println("max_countBonds: " + this.max_countBonds);
        System.out.println("min_countSingleBonds: " + this.min_countSingleBonds);
        System.out.println("max_countSingleBonds: " + this.max_countSingleBonds);
        System.out.println("min_countDoubleBonds: " + this.min_countDoubleBonds);
        System.out.println("max_countDoubleBonds: " + this.max_countDoubleBonds);
        System.out.println("min_countTripleBonds: " + this.min_countTripleBonds);
        System.out.println("max_countTripleBonds: " + this.max_countTripleBonds);
        System.out.println("min_countRotatableBonds: " + this.min_countRotatableBonds);
        System.out.println("max_countRotatableBonds: " + this.max_countRotatableBonds);
        System.out.println("min_countHydrogenAtoms: " + this.min_countHydrogenAtoms);
        System.out.println("max_countHydrogenAtoms: " + this.max_countHydrogenAtoms);
        System.out.println("min_countMetalAtoms: " + this.min_countMetalAtoms);
        System.out.println("max_countMetalAtoms: " + this.max_countMetalAtoms);
        System.out.println("min_countHeavyAtoms: " + this.min_countHeavyAtoms);
        System.out.println("max_countHeavyAtoms: " + this.max_countHeavyAtoms);
        System.out.println("min_countPositiveAtoms: " + this.min_countPositiveAtoms);
        System.out.println("max_countPositiveAtoms: " + this.max_countPositiveAtoms);
        System.out.println("min_countNegativeAtoms: " + this.min_countNegativeAtoms);
        System.out.println("max_countNegativeAtoms: " + this.max_countNegativeAtoms);
        System.out.println("min_countRingBonds: " + this.min_countRingBonds);
        System.out.println("max_countRingBonds: " + this.max_countRingBonds);
        System.out.println("min_countStereoAtoms: " + this.min_countStereoAtoms);
        System.out.println("max_countStereoAtoms: " + this.max_countStereoAtoms);
        System.out.println("min_countStereoBonds: " + this.min_countStereoBonds);
        System.out.println("max_countStereoBonds: " + this.max_countStereoBonds);
        System.out.println("min_countRingAssemblies: " + this.min_countRingAssemblies);
        System.out.println("max_countRingAssemblies: " + this.max_countRingAssemblies);
        System.out.println("min_countAromaticBonds: " + this.min_countAromaticBonds);
        System.out.println("max_countAromaticBonds: " + this.max_countAromaticBonds);
        System.out.println("min_countAromaticRings: " + this.min_countAromaticRings);
        System.out.println("max_countAromaticRings: " + this.max_countAromaticRings);
        System.out.println("min_formalCharge: " + this.min_formalCharge);
        System.out.println("max_formalCharge: " + this.max_formalCharge);
        System.out.println("min_theALogP: " + this.min_theALogP);
        System.out.println("max_theALogP: " + this.max_theALogP);

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

        System.out.println("Calling createCmpdLIstByNscs in HelperCmpd from performCreateListBySearch in ListContentController.");

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

    public Double getMin_molecularWeight() {
        return min_molecularWeight;
    }

    public void setMin_molecularWeight(Double min_molecularWeight) {
        this.min_molecularWeight = min_molecularWeight;
    }

    public Double getMax_molecularWeight() {
        return max_molecularWeight;
    }

    public void setMax_molecularWeight(Double max_molecularWeight) {
        this.max_molecularWeight = max_molecularWeight;
    }

    public String getMolecularFormula() {
        return molecularFormula;
    }

    public void setMolecularFormula(String molecularFormula) {
        this.molecularFormula = molecularFormula;
    }

    public Double getMin_logD() {
        return min_logD;
    }

    public void setMin_logD(Double min_logD) {
        this.min_logD = min_logD;
    }

    public Double getMax_logD() {
        return max_logD;
    }

    public void setMax_logD(Double max_logD) {
        this.max_logD = max_logD;
    }

    public Integer getMin_countHydBondAcceptors() {
        return min_countHydBondAcceptors;
    }

    public void setMin_countHydBondAcceptors(Integer min_countHydBondAcceptors) {
        this.min_countHydBondAcceptors = min_countHydBondAcceptors;
    }

    public Integer getMax_countHydBondAcceptors() {
        return max_countHydBondAcceptors;
    }

    public void setMax_countHydBondAcceptors(Integer max_countHydBondAcceptors) {
        this.max_countHydBondAcceptors = max_countHydBondAcceptors;
    }

    public Integer getMin_countHydBondDonors() {
        return min_countHydBondDonors;
    }

    public void setMin_countHydBondDonors(Integer min_countHydBondDonors) {
        this.min_countHydBondDonors = min_countHydBondDonors;
    }

    public Integer getMax_countHydBondDonors() {
        return max_countHydBondDonors;
    }

    public void setMax_countHydBondDonors(Integer max_countHydBondDonors) {
        this.max_countHydBondDonors = max_countHydBondDonors;
    }

    public Double getMin_surfaceArea() {
        return min_surfaceArea;
    }

    public void setMin_surfaceArea(Double min_surfaceArea) {
        this.min_surfaceArea = min_surfaceArea;
    }

    public Double getMax_surfaceArea() {
        return max_surfaceArea;
    }

    public void setMax_surfaceArea(Double max_surfaceArea) {
        this.max_surfaceArea = max_surfaceArea;
    }

    public Double getMin_solubility() {
        return min_solubility;
    }

    public void setMin_solubility(Double min_solubility) {
        this.min_solubility = min_solubility;
    }

    public Double getMax_solubility() {
        return max_solubility;
    }

    public void setMax_solubility(Double max_solubility) {
        this.max_solubility = max_solubility;
    }

    public Integer getMin_countRings() {
        return min_countRings;
    }

    public void setMin_countRings(Integer min_countRings) {
        this.min_countRings = min_countRings;
    }

    public Integer getMax_countRings() {
        return max_countRings;
    }

    public void setMax_countRings(Integer max_countRings) {
        this.max_countRings = max_countRings;
    }

    public Integer getMin_countAtoms() {
        return min_countAtoms;
    }

    public void setMin_countAtoms(Integer min_countAtoms) {
        this.min_countAtoms = min_countAtoms;
    }

    public Integer getMax_countAtoms() {
        return max_countAtoms;
    }

    public void setMax_countAtoms(Integer max_countAtoms) {
        this.max_countAtoms = max_countAtoms;
    }

    public Integer getMin_countBonds() {
        return min_countBonds;
    }

    public void setMin_countBonds(Integer min_countBonds) {
        this.min_countBonds = min_countBonds;
    }

    public Integer getMax_countBonds() {
        return max_countBonds;
    }

    public void setMax_countBonds(Integer max_countBonds) {
        this.max_countBonds = max_countBonds;
    }

    public Integer getMin_countSingleBonds() {
        return min_countSingleBonds;
    }

    public void setMin_countSingleBonds(Integer min_countSingleBonds) {
        this.min_countSingleBonds = min_countSingleBonds;
    }

    public Integer getMax_countSingleBonds() {
        return max_countSingleBonds;
    }

    public void setMax_countSingleBonds(Integer max_countSingleBonds) {
        this.max_countSingleBonds = max_countSingleBonds;
    }

    public Integer getMin_countDoubleBonds() {
        return min_countDoubleBonds;
    }

    public void setMin_countDoubleBonds(Integer min_countDoubleBonds) {
        this.min_countDoubleBonds = min_countDoubleBonds;
    }

    public Integer getMax_countDoubleBonds() {
        return max_countDoubleBonds;
    }

    public void setMax_countDoubleBonds(Integer max_countDoubleBonds) {
        this.max_countDoubleBonds = max_countDoubleBonds;
    }

    public Integer getMin_countTripleBonds() {
        return min_countTripleBonds;
    }

    public void setMin_countTripleBonds(Integer min_countTripleBonds) {
        this.min_countTripleBonds = min_countTripleBonds;
    }

    public Integer getMax_countTripleBonds() {
        return max_countTripleBonds;
    }

    public void setMax_countTripleBonds(Integer max_countTripleBonds) {
        this.max_countTripleBonds = max_countTripleBonds;
    }

    public Integer getMin_countRotatableBonds() {
        return min_countRotatableBonds;
    }

    public void setMin_countRotatableBonds(Integer min_countRotatableBonds) {
        this.min_countRotatableBonds = min_countRotatableBonds;
    }

    public Integer getMax_countRotatableBonds() {
        return max_countRotatableBonds;
    }

    public void setMax_countRotatableBonds(Integer max_countRotatableBonds) {
        this.max_countRotatableBonds = max_countRotatableBonds;
    }

    public Integer getMin_countHydrogenAtoms() {
        return min_countHydrogenAtoms;
    }

    public void setMin_countHydrogenAtoms(Integer min_countHydrogenAtoms) {
        this.min_countHydrogenAtoms = min_countHydrogenAtoms;
    }

    public Integer getMax_countHydrogenAtoms() {
        return max_countHydrogenAtoms;
    }

    public void setMax_countHydrogenAtoms(Integer max_countHydrogenAtoms) {
        this.max_countHydrogenAtoms = max_countHydrogenAtoms;
    }

    public Integer getMin_countMetalAtoms() {
        return min_countMetalAtoms;
    }

    public void setMin_countMetalAtoms(Integer min_countMetalAtoms) {
        this.min_countMetalAtoms = min_countMetalAtoms;
    }

    public Integer getMax_countMetalAtoms() {
        return max_countMetalAtoms;
    }

    public void setMax_countMetalAtoms(Integer max_countMetalAtoms) {
        this.max_countMetalAtoms = max_countMetalAtoms;
    }

    public Integer getMin_countHeavyAtoms() {
        return min_countHeavyAtoms;
    }

    public void setMin_countHeavyAtoms(Integer min_countHeavyAtoms) {
        this.min_countHeavyAtoms = min_countHeavyAtoms;
    }

    public Integer getMax_countHeavyAtoms() {
        return max_countHeavyAtoms;
    }

    public void setMax_countHeavyAtoms(Integer max_countHeavyAtoms) {
        this.max_countHeavyAtoms = max_countHeavyAtoms;
    }

    public Integer getMin_countPositiveAtoms() {
        return min_countPositiveAtoms;
    }

    public void setMin_countPositiveAtoms(Integer min_countPositiveAtoms) {
        this.min_countPositiveAtoms = min_countPositiveAtoms;
    }

    public Integer getMax_countPositiveAtoms() {
        return max_countPositiveAtoms;
    }

    public void setMax_countPositiveAtoms(Integer max_countPositiveAtoms) {
        this.max_countPositiveAtoms = max_countPositiveAtoms;
    }

    public Integer getMin_countNegativeAtoms() {
        return min_countNegativeAtoms;
    }

    public void setMin_countNegativeAtoms(Integer min_countNegativeAtoms) {
        this.min_countNegativeAtoms = min_countNegativeAtoms;
    }

    public Integer getMax_countNegativeAtoms() {
        return max_countNegativeAtoms;
    }

    public void setMax_countNegativeAtoms(Integer max_countNegativeAtoms) {
        this.max_countNegativeAtoms = max_countNegativeAtoms;
    }

    public Integer getMin_countRingBonds() {
        return min_countRingBonds;
    }

    public void setMin_countRingBonds(Integer min_countRingBonds) {
        this.min_countRingBonds = min_countRingBonds;
    }

    public Integer getMax_countRingBonds() {
        return max_countRingBonds;
    }

    public void setMax_countRingBonds(Integer max_countRingBonds) {
        this.max_countRingBonds = max_countRingBonds;
    }

    public Integer getMin_countStereoAtoms() {
        return min_countStereoAtoms;
    }

    public void setMin_countStereoAtoms(Integer min_countStereoAtoms) {
        this.min_countStereoAtoms = min_countStereoAtoms;
    }

    public Integer getMax_countStereoAtoms() {
        return max_countStereoAtoms;
    }

    public void setMax_countStereoAtoms(Integer max_countStereoAtoms) {
        this.max_countStereoAtoms = max_countStereoAtoms;
    }

    public Integer getMin_countStereoBonds() {
        return min_countStereoBonds;
    }

    public void setMin_countStereoBonds(Integer min_countStereoBonds) {
        this.min_countStereoBonds = min_countStereoBonds;
    }

    public Integer getMax_countStereoBonds() {
        return max_countStereoBonds;
    }

    public void setMax_countStereoBonds(Integer max_countStereoBonds) {
        this.max_countStereoBonds = max_countStereoBonds;
    }

    public Integer getMin_countRingAssemblies() {
        return min_countRingAssemblies;
    }

    public void setMin_countRingAssemblies(Integer min_countRingAssemblies) {
        this.min_countRingAssemblies = min_countRingAssemblies;
    }

    public Integer getMax_countRingAssemblies() {
        return max_countRingAssemblies;
    }

    public void setMax_countRingAssemblies(Integer max_countRingAssemblies) {
        this.max_countRingAssemblies = max_countRingAssemblies;
    }

    public Integer getMin_countAromaticBonds() {
        return min_countAromaticBonds;
    }

    public void setMin_countAromaticBonds(Integer min_countAromaticBonds) {
        this.min_countAromaticBonds = min_countAromaticBonds;
    }

    public Integer getMax_countAromaticBonds() {
        return max_countAromaticBonds;
    }

    public void setMax_countAromaticBonds(Integer max_countAromaticBonds) {
        this.max_countAromaticBonds = max_countAromaticBonds;
    }

    public Integer getMin_countAromaticRings() {
        return min_countAromaticRings;
    }

    public void setMin_countAromaticRings(Integer min_countAromaticRings) {
        this.min_countAromaticRings = min_countAromaticRings;
    }

    public Integer getMax_countAromaticRings() {
        return max_countAromaticRings;
    }

    public void setMax_countAromaticRings(Integer max_countAromaticRings) {
        this.max_countAromaticRings = max_countAromaticRings;
    }

    public Integer getMin_formalCharge() {
        return min_formalCharge;
    }

    public void setMin_formalCharge(Integer min_formalCharge) {
        this.min_formalCharge = min_formalCharge;
    }

    public Integer getMax_formalCharge() {
        return max_formalCharge;
    }

    public void setMax_formalCharge(Integer max_formalCharge) {
        this.max_formalCharge = max_formalCharge;
    }

    public Double getMin_theALogP() {
        return min_theALogP;
    }

    public void setMin_theALogP(Double min_theALogP) {
        this.min_theALogP = min_theALogP;
    }

    public Double getMax_theALogP() {
        return max_theALogP;
    }

    public void setMax_theALogP(Double max_theALogP) {
        this.max_theALogP = max_theALogP;
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
