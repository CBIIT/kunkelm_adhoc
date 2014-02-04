/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
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
import org.openscience.cdk.io.MDLV2000Writer;
import org.openscience.cdk.layout.StructureDiagramGenerator;
import org.openscience.cdk.smiles.SmilesParser;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HelperStructure;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class CmpdController implements Serializable {

  private String nscForJChemPaintLoad;
  private String smilesForJChemPaintLoad;
  private String ctabForJChemPaintLoad;
  private List<String> agents;
  private List<String> aliases;
  private List<String> cases;
  private List<String> drugStatuses;
  private List<String> nscs;
  private List<String> originators;
  private List<String> plates;
  private List<String> targets;
  //
  private List<CmpdVO> cmpds;
  private CmpdVO selectedCmpd;
  private CmpdVO[] selectedCmpds;
  //
  private String smilesString;
  private String molFile;
  // substructure searches
  private List<CmpdVO> substructureSearchResults;
  private String substructureResultsListName;
  //
  @ManagedProperty(value = "#{listManagerController}")
  private ListManagerController listManagerController;

  public ListManagerController getListManagerController() {
    return listManagerController;
  }

  public void setListManagerController(ListManagerController listManagerController) {
    this.listManagerController = listManagerController;
  }
  //
  public List<CmpdVO> getSubstructureSearchResults() {
    return substructureSearchResults;
  }

  public void setSubstructureSearchResults(List<CmpdVO> substructureSearchResults) {
    this.substructureSearchResults = substructureSearchResults;
  }

  public String getSubstructureResultsListName() {
    return substructureResultsListName;
  }

  public void setSubstructureResultsListName(String substructureResultsListName) {
    this.substructureResultsListName = substructureResultsListName;
  }

  //
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

  public String performExactMatchSearch() {

    this.substructureSearchResults = new ArrayList<CmpdVO>();

    HelperStructure strcHelper = new HelperStructure();

    List<Integer> nscIntList = strcHelper.findNSCsByExactMatch(this.smilesString);

    HelperCmpd cmpdHelper = new HelperCmpd();

    List<CmpdVO> cVOlist = cmpdHelper.getCmpdsByNsc(nscIntList);

    this.substructureSearchResults = cVOlist;

    return null;

  }

  public String performSubstructureSearch() {

    System.out.println("Now in performSubstructureSearch()");

    this.substructureSearchResults = new ArrayList<CmpdVO>();

    HelperStructure strcHelper = new HelperStructure();

    List<Integer> nscIntList = strcHelper.findNSCsBySmilesSubstructure(this.smilesString);

    System.out.println("Size of nscIntList: " + nscIntList.size());

    Date now = new Date();

    if (this.substructureResultsListName == null || this.substructureResultsListName.length() == 0) {
      this.substructureResultsListName = "substructureSearchResults " + now.toString();
    }

    HelperCmpd cmpdHelper = new HelperCmpd();

    CmpdListVO tempCLvo = cmpdHelper.createCmpdListByNscs(this.substructureResultsListName, nscIntList, "kunkelm");

    // have to fetch the list so that the compound details will be populated

    HelperCmpdList listHelper = new HelperCmpdList();
    CmpdListVO realList = listHelper.getCmpdListByCmpdListId(tempCLvo.getCmpdListId(), Boolean.TRUE);

    List<CmpdVO> cVOlist = new ArrayList<CmpdVO>();
    for (CmpdListMemberVO clmVO : realList.getCmpdListMembers()) {
      cVOlist.add(clmVO.getCmpd());
    }

    this.substructureSearchResults = cVOlist;
    
    listManagerController.getAvailableLists().add(realList);

    return null;
  }

  public String performSmartsSearch() {

    this.substructureSearchResults = new ArrayList<CmpdVO>();

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

    List<CmpdVO> cVOlist = cmpdHelper.getCmpdsByNsc(nscIntList);

    this.substructureSearchResults = cVOlist;

    return null;

  }

  /**
   * Creates a new instance of CmpdController
   */
  public CmpdController() {

    agents = new ArrayList<String>();
    aliases = new ArrayList<String>();
    cases = new ArrayList<String>();
    drugStatuses = new ArrayList<String>();
    nscs = new ArrayList<String>();
    originators = new ArrayList<String>();
    plates = new ArrayList<String>();
    targets = new ArrayList<String>();

    this.cmpds = new ArrayList<CmpdVO>();
    this.selectedCmpd = new CmpdVO();
    this.selectedCmpds = new CmpdVO[0];

  }

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

    CmpdVO cVO = helper.getSingleCmpdByNsc(nscInt);

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

  public String resetSearch() {

    // clear out previous search
    this.cmpds = new ArrayList<CmpdVO>();

    agents = new ArrayList<String>();
    aliases = new ArrayList<String>();
    cases = new ArrayList<String>();
    drugStatuses = new ArrayList<String>();
    nscs = new ArrayList<String>();
    originators = new ArrayList<String>();
    plates = new ArrayList<String>();
    targets = new ArrayList<String>();

    cmpds = new ArrayList<CmpdVO>();
    selectedCmpd = new CmpdVO();
    selectedCmpds = new CmpdVO[0];

    return null;

  }

//  public String performSearch() {
//
//    try {
//
//      // clear out previous search
//      this.drugTrackers = new ArrayList<CmpdVO>();
//
//      EntityManagerFactory emf = javax.persistence.Persistence.createEntityManagerFactory("DCTDPU");
//      EntityManager em = emf.createEntityManager();
//      CriteriaBuilder criteriaBuilder = em.getCriteriaBuilder();
//
//      CriteriaQuery<Cmpd> criteriaQuery = criteriaBuilder.createQuery(Cmpd.class);
//
//      Root<Cmpd> rdt = criteriaQuery.from(Cmpd.class);
//
//      // this is the conjunctin of compounds with plates and/or sets and/or targets
//      List<Predicate> conjPredList = new ArrayList<Predicate>();
//
//      List<Long> nscLongList = new ArrayList<Long>();
//
//      if (nscs == null) {
//        nscs = new ArrayList<String>();
//      }
//
//      // nsc, cas, agent, alias, originator as DISJUNCTION
//
//      List<Predicate> disjPredList = new ArrayList<Predicate>();
//
//      for (String s : nscs) {
//        nscLongList.add(Long.valueOf(s));
//      }
//
//      if (!nscLongList.isEmpty()) {
//        Join<Cmpd, NscCmpd> nscDrugT = rdt.join(Cmpd_.nscCmpd);
//        disjPredList.add(nscDrugT.get("nsc").in(nscLongList));
//      };
//
//      if (!this.cases.isEmpty()) {
//        disjPredList.add(rdt.get("cas").in(this.cases));
//      };
//
//      if (!this.agents.isEmpty()) {
//        disjPredList.add(rdt.get("agent").in(this.agents));
//      };
//
//      if (!this.originators.isEmpty()) {
//        disjPredList.add(rdt.get("originator").in(this.originators));
//      };
//
//      if (!this.aliases.isEmpty()) {
//        Join<Cmpd, Alias> alia = rdt.join(Cmpd_.aliasCollection);
//        disjPredList.add(alia.get("alias").in(this.aliases));
//      };
//
//      // if any specs, set the disjunction as first term of overall conjunction
//
//      if (!disjPredList.isEmpty()) {
//        Predicate disj = criteriaBuilder.or(disjPredList.toArray(new Predicate[disjPredList.size()]));
//        conjPredList.add(disj);
//      }
//
//      // plates/sets futher filter
//
//      if (!this.plates.isEmpty()) {
//        Join<Cmpd, CmpdPlate> drugPlate = rdt.join(Cmpd_.drugTrackerPlateFk);
//        conjPredList.add(drugPlate.get("plateName").in(this.plates));
//      };
//
//      if (!this.drugStatuses.isEmpty()) {
//        Join<Cmpd, DrugStatus> drugStatus = rdt.join(Cmpd_.drugStatusFk);
//        conjPredList.add(drugStatus.get("drugStatus").in(this.drugStatuses));
//      };
//
//      // targets
//      // this is auto-complete; so, have to check for null
//
//      if (this.targets != null && !this.targets.isEmpty()) {
//        Join<Cmpd, Target> targ = rdt.join(Cmpd_.targetCollection);
//        conjPredList.add(targ.get("target").in(this.targets));
//      };
//
//      //
//
//      Predicate conj = criteriaBuilder.and(conjPredList.toArray(new Predicate[conjPredList.size()]));
//
//      criteriaQuery.where(conj);
//
//      TypedQuery<Cmpd> q = em.createQuery(criteriaQuery);
//      List<Cmpd> resultList = q.getResultList();
//      System.out.println(" resultList contains: " + resultList.size() + " Cmpds");
//      for (Cmpd dt : resultList) {
//        System.out.println(dt.getCas() + " " + dt.getAgent() + " " + dt.getOriginator());
//      }
//
//      ArrayList<CmpdVO> voList = new ArrayList<CmpdVO>();
//
//      for (Cmpd dt : resultList) {
//        voList.add(CmpdServiceTranslators.toCmpdVO(dt));
//      }
//
//      this.drugTrackers = voList;
//
////        List<drug_tracker_client_pkg.CmpdVO> wsList = searchByCriteria(
////                nscLongList,
////                cases,
////                originators,
////                aliases,
////                targets,
////                drugStatuses,
////                agents,
////                plates);
////
////        List<CmpdVO> appList = CmpdServiceTranslators.transformCmpdList(wsList);
////
////        this.setCmpds(appList);
//
//    } catch (Exception e) {
//      e.printStackTrace();
//    }
//
//    return "success";
//
//  }
  // GETTERS AND SETTERS
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

  public List<String> getAgents() {
    return agents;
  }

  public void setAgents(List<String> agents) {
    this.agents = agents;
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

  public List<String> getDrugStatuses() {
    return drugStatuses;
  }

  public void setDrugStatuses(List<String> drugStatuses) {
    this.drugStatuses = drugStatuses;
  }

  public List<String> getNscs() {
    return nscs;
  }

  public void setNscs(List<String> nscs) {
    this.nscs = nscs;
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

  public List<CmpdVO> getCmpds() {
    return cmpds;
  }

  public void setCmpds(List<CmpdVO> cmpds) {
    this.cmpds = cmpds;
  }

  public CmpdVO getSelectedCmpd() {
    return selectedCmpd;
  }

  public void setSelectedCmpd(CmpdVO selectedCmpd) {
    this.selectedCmpd = selectedCmpd;
  }

  public CmpdVO[] getSelectedCmpds() {
    return selectedCmpds;
  }

  public void setSelectedCmpds(CmpdVO[] selectedCmpds) {
    this.selectedCmpds = selectedCmpds;
  }
}
