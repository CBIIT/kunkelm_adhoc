/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HelperStructure;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.atomtype.CDKAtomTypeMatcher;
import org.openscience.cdk.interfaces.IAtom;
import org.openscience.cdk.interfaces.IAtomType;
import org.openscience.cdk.io.MDLV2000Reader;
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.FixBondOrdersTool;
import org.openscience.cdk.smiles.SmilesGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import org.openscience.cdk.tools.CDKHydrogenAdder;
import org.openscience.cdk.tools.manipulator.AtomContainerManipulator;
import org.openscience.cdk.tools.manipulator.AtomTypeManipulator;

@ManagedBean
@SessionScoped
public class StructureSearchController implements Serializable {

  static final long serialVersionUID = -8653468638698142855l;

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

  private String nscForLoad;
  private String smilesForLoad;
  private String ctabForLoad;

  private String smilesFromEditor;
  private String ctabFromEditor;

  private String listName;

  private CmpdListVO listToSearch;

  public String performExactMatchSearch() {

    HelperStructure strcHelper = new HelperStructure();

    List<Integer> nscIntList = strcHelper.findNSCsByExactMatch(this.smilesFromEditor);

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

  public String performSubstructureSearchCtab() {

    System.out.println("Now in performSubstructureSearchCtab()");

    this.smilesFromEditor = smilesFromCtab(this.ctabFromEditor);

    HelperStructure strcHelper = new HelperStructure();

    List<Integer> nscIntList = strcHelper.findNSCsBySmilesSubstructure(this.smilesFromEditor);

    System.out.println("Size of nscIntList: " + nscIntList.size());

    Date now = new Date();

    if (this.listName == null || this.listName.length() == 0) {
      this.listName = "structureSearchResults " + now.toString();
    }

    HelperCmpd cmpdHelper = new HelperCmpd();
    Long cmpdListId = cmpdHelper.createCmpdListByNscs(this.listName, nscIntList, this.smilesFromEditor, this.sessionController.getLoggedUser());

    // have to fetch the list so that the compound details will be populated
    // 
    HelperCmpdList listHelper = new HelperCmpdList();
    CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, this.sessionController.getLoggedUser());

    listManagerController.getAvailableLists().add(clVO);
    listManagerController.setActiveList(clVO);

    return "/webpages/activeListTable.xhtml?faces-redirect=true";

  }

  public String performSubstructureSearch() {

    System.out.println("Now in performSubstructureSearch()");

    HelperStructure strcHelper = new HelperStructure();

    List<Integer> nscIntList = strcHelper.findNSCsBySmilesSubstructure(this.smilesFromEditor);

    System.out.println("Size of nscIntList: " + nscIntList.size());

    Date now = new Date();

    if (this.listName == null || this.listName.length() == 0) {
      this.listName = "structureSearchResults " + now.toString();
    }

    HelperCmpd cmpdHelper = new HelperCmpd();
    Long cmpdListId = cmpdHelper.createCmpdListByNscs(this.listName, nscIntList, this.smilesFromEditor, this.sessionController.getLoggedUser());

    // have to fetch the list so that the compound details will be populated
    // 
    HelperCmpdList listHelper = new HelperCmpdList();
    CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(cmpdListId, Boolean.TRUE, this.sessionController.getLoggedUser());

    listManagerController.getAvailableLists().add(clVO);
    listManagerController.setActiveList(clVO);

    return "/webpages/activeListTable.xhtml?faces-redirect=true";

  }

  public String performSmartsSearch() {

    String smartsString = this.smilesFromEditor;

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
            smilesFromEditor + " " + smartsString);

    FacesContext.getCurrentInstance().addMessage(null, msg);

    System.out.println("smilesString: " + this.smilesFromEditor);
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

  /**
   *
   * @param smiles
   * @return Helper method for loading ctab UNTIL ctabs are stored in cmpd_table
   */
  public static String ctabFromSmiles(String smiles) {

    String rtn = "";

    try {

      SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());
      Molecule molecule = (Molecule) sp.parseSmiles(smiles);
      
      try {
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(molecule.getBuilder());
        Iterator<IAtom> atoms = molecule.atoms().iterator();
        while (atoms.hasNext()) {
          IAtom atom = atoms.next();
          IAtomType type = matcher.findMatchingAtomType(molecule, atom);
          AtomTypeManipulator.configure(atom, type);
        }
        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(molecule.getBuilder());
        hAdder.addImplicitHydrogens(molecule);
      } catch (Exception e) {
        e.printStackTrace();
      }

      AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(molecule);

      FixBondOrdersTool fbot = new FixBondOrdersTool();
      molecule = (Molecule) fbot.kekuliseAromaticRings(molecule);

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

      rtn = baos.toString();

    } catch (Exception e) {
      e.printStackTrace();
    }
    return rtn;
  }

  private String smilesFromCtab(String ctab) {

    String rtn = "";

    Molecule mol = new Molecule();

    try {

      MDLV2000Reader mdlReader = new MDLV2000Reader(new ByteArrayInputStream(ctab.getBytes()));
      mdlReader.read(mol);

      // have to fix this up...
      try {
        CDKAtomTypeMatcher matcher = CDKAtomTypeMatcher.getInstance(mol.getBuilder());
        Iterator<IAtom> atoms = mol.atoms().iterator();
        while (atoms.hasNext()) {
          IAtom atom = atoms.next();
          IAtomType type = matcher.findMatchingAtomType(mol, atom);
          AtomTypeManipulator.configure(atom, type);
        }
        CDKHydrogenAdder hAdder = CDKHydrogenAdder.getInstance(mol.getBuilder());
        hAdder.addImplicitHydrogens(mol);
      } catch (Exception e) {
        e.printStackTrace();
      }

      AtomContainerManipulator.percieveAtomTypesAndConfigureAtoms(mol);

      FixBondOrdersTool fbot = new FixBondOrdersTool();
      mol = (Molecule) fbot.kekuliseAromaticRings(mol);

      StructureDiagramGenerator sdg = new StructureDiagramGenerator();
      sdg.setMolecule(mol);

      try {
        sdg.generateCoordinates();
      } catch (Exception ex) {
        ex.printStackTrace();
      }

      Molecule fixedMol = (Molecule) sdg.getMolecule();

      SmilesGenerator sg = new SmilesGenerator();
      sg.setUseAromaticityFlag(true);
      rtn = sg.createSMILES(mol);

    } catch (Exception e) {
      e.printStackTrace();
    }

    return rtn;

  }

  public String performLoadEditorByNsc() {

    HelperCmpd helper = new HelperCmpd();

    Integer nscInt = Integer.valueOf(this.nscForLoad);

    CmpdVO cVO = helper.getSingleCmpdByNsc(nscInt, this.sessionController.getLoggedUser());

    if (cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi().isEmpty()) {
      this.smilesForLoad = null;
      this.ctabForLoad = null;
    } else {

      String smiles = cVO.getParentFragment().getCmpdFragmentStructure().getCanSmi();
      this.smilesForLoad = smiles;

      String ctab = cVO.getParentFragment().getCmpdFragmentStructure().getCtab();
      //this.ctabForLoad = ctabFromSmiles(smiles);
      this.ctabForLoad = ctab;

    }

    return null;
  }

  // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
  public String getSmilesFromEditor() {
    return smilesFromEditor;
  }

  public void setSmilesFromEditor(String smilesFromEditor) {
    this.smilesFromEditor = smilesFromEditor;
  }

  public String getCtabFromEditor() {
    return ctabFromEditor;
  }

  public void setCtabFromEditor(String ctabFromEditor) {
    this.ctabFromEditor = ctabFromEditor;
  }

  public String getListName() {
    return listName;
  }

  public void setListName(String listName) {
    this.listName = listName;
  }

  public String getNscForLoad() {
    return nscForLoad;
  }

  public void setNscForLoad(String nscForLoad) {
    this.nscForLoad = nscForLoad;
  }

  public String getCtabForLoad() {
    return ctabForLoad;
  }

  public void setCtabForLoad(String ctabForLoad) {
    this.ctabForLoad = ctabForLoad;
  }

  public String getSmilesForLoad() {
    return smilesForLoad;
  }

  public void setSmilesForLoad(String smilesForLoad) {
    this.smilesForLoad = smilesForLoad;
  }

  public CmpdListVO getListToSearch() {
    return listToSearch;
  }

  public void setListToSearch(CmpdListVO listToSearch) {
    this.listToSearch = listToSearch;
  }

    // </editor-fold>  
}