/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpServletRequest;
import mwk.datasystem.androdomain.AdHocCmpd;
import mwk.datasystem.androdomain.AdHocCmpdFragment;
import mwk.datasystem.androdomain.Cmpd;
import mwk.datasystem.androdomain.CmpdList;
import mwk.datasystem.androdomain.CmpdListMember;
import mwk.datasystem.mwkhisto.Histogram;
import mwk.datasystem.mwkhisto.HistogramBin;
import mwk.datasystem.util.Comparators;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.HistogramChartUtil;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.hibernate.Session;
import org.openscience.cdk.Atom;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.interfaces.IMolecule;
import org.primefaces.component.chart.bar.BarChart;
import org.primefaces.event.FileUploadEvent;
import org.primefaces.event.ItemSelectEvent;
import org.primefaces.model.UploadedFile;
import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class ListManagerController implements Serializable {

  static final long serialVersionUID = -8653468638698142855l;
  private Long listAid;
  private Long listBid;
  //  
  private Collection<CmpdVO> cmpdsListAnotListB;
  private Collection<CmpdVO> cmpdsListAandListB;
  private Collection<CmpdVO> cmpdsListAorListB;
  //
  private ArrayList<SelectItem> availableListSelectItems;
  //
  private String listName;
  //
  private String nscTextArea;
  private String projectCodeTextArea;
  //
  private List<String> nscs;
  private List<String> projectCodes;
  //
  private List<CmpdListVO> availableLists;
  //
  private CmpdListVO selectedList;
  private CmpdListVO[] selectedLists;
  //
  private CmpdListMemberVO selectedListMember;
  private CmpdListMemberVO[] selectedListMembers;
  //
  private CmpdVO selectedCmpd;
  private CmpdVO[] selectedCmpds;
//
  private List<CmpdVO> listOfCmpds;

  public ListManagerController() {
    this.performUpdateAvailableLists();
  }

  public String placeholderAction() {
    return null;
  }
  private ArrayList<Histogram> histoModel;

  public ArrayList<Histogram> getHistoModel() {
    return histoModel;
  }

  public void setHistoModel(ArrayList<Histogram> histoModel) {
    this.histoModel = histoModel;
  }

  public void itemSelectHistogram(ItemSelectEvent event) {

    System.out.println("Now in itemSelectHistogram in ListManagerController");

    NumberFormat nf = new DecimalFormat();
    nf.setMaximumFractionDigits(2);

    BarChart selectedBarChart = (BarChart) event.getSource();

    CartesianChartModel selectedModel = (CartesianChartModel) selectedBarChart.getValue();

    ChartSeries selectedSeries = selectedModel.getSeries().get(event.getSeriesIndex());

    String histogramIdent = selectedBarChart.getTitle();

    Set<Object> keySet = selectedSeries.getData().keySet();

    String[] keyArray = keySet.toArray(new String[keySet.size()]);

    String histogramBarIdent = keyArray[event.getItemIndex()];

    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------
    //--------------------------------------------------------------------------


    FacesMessage msg = new FacesMessage(
            FacesMessage.SEVERITY_INFO, "Item selected ",
            "Event Item Index: " + event.getItemIndex()
            + "\n" + ", Series Index: " + event.getSeriesIndex()
            + "\n" + ", Histogram Ident: " + histogramIdent
            + "\n" + ", Histogram Bar Ident: " + histogramBarIdent);

    FacesContext.getCurrentInstance().addMessage(null, msg);

    // fetch the included compounds using the indexes

    Histogram selHisto = null;

    for (Histogram h : this.histoModel) {
      if (h.getPropertyName().equals(histogramIdent)) {
        selHisto = h;
        break;
      }
    }

    // get the appropriate bin

    HistogramBin curBin = selHisto.getBinList().get(event.getItemIndex());

    ArrayList<CmpdListMemberVO> clmList = curBin.getBinList();

    // reset flags on selectedList and then replace it
    if (this.selectedListMembers != null) {
      for (CmpdListMemberVO clmVO : this.selectedListMembers) {
        clmVO.setIsSelected(new Boolean(Boolean.FALSE));
      }
    }

    this.selectedListMembers = clmList.toArray(new CmpdListMemberVO[clmList.size()]);

    for (CmpdListMemberVO clmVO : this.selectedListMembers) {
      clmVO.setIsSelected(new Boolean(Boolean.TRUE));
    }

    // regenerate the histoModel
    this.histoModel = HistogramChartUtil.doHistograms(this.selectedList);


  }

  public String renderListHistograms() {

    System.out.println("Entering renderListHistograms()");

    // only fetch if not already fetched    
    if (this.selectedList.getCmpdListMembers() == null || this.selectedList.getCmpdListMembers().size() == 0) {
      System.out.println("------------------------List was not populated. Now fetching listMembers");
      HelperCmpdList helper = new HelperCmpdList();
      CmpdListVO voList = helper.getCmpdListByCmpdListId(this.selectedList.getCmpdListId(), Boolean.TRUE);
      this.selectedList.setCmpdListMembers(voList.getCmpdListMembers());
    } else {
      System.out.println("------------------------List WAS populated. No need to fetch");
    }

    this.histoModel = HistogramChartUtil.doHistograms(this.selectedList);

    return null;

  }

  public void logout(ActionEvent actionEvent) {
    System.out.println("Now in logout in ListManagerController");
    HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
    request.getSession().invalidate();
  }
  private String loggedUser;

  public String getLoggedUser() {
    return FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
  }

  public String performMakeListPublic() {

    HelperCmpdList helper = new HelperCmpdList();

    // only if owner

    if (this.selectedList.getListOwner().equals("kunkelm")) {
      helper.makeCmpdListPublic(this.selectedList.getCmpdListId());
      this.selectedList.setShareWith("PUBLIC");
    }

    return null;

  }

  //public String performDeleteList() {
  public void performDeleteList(ActionEvent actionEvent) {

    System.out.println("Now in PerformDeleteList in listManagerController.");

    if (this.selectedList.getListOwner().equals("kunkelm")) {
      HelperCmpdList helper = new HelperCmpdList();
      helper.deleteCmpdListByCmpdListId(this.selectedList.getCmpdListId());
      this.availableLists.remove(this.selectedList);
      selectedList = new CmpdListVO();
    }

  }

  public String performUpdateAvailableLists() {

    this.availableLists = new ArrayList<CmpdListVO>();
    this.selectedList = new CmpdListVO();

    HelperCmpdList helper = new HelperCmpdList();
    List<CmpdListVO> lists = helper.showAvailableCmpdLists("kunkelm");

    // MWK TODO! add new lists to availableLists, DON'T REMOVE LISTS that might have loaded data

    this.availableLists = lists;

    return null;

  }

  public String performListLogic() {

    HelperCmpdList helperA = new HelperCmpdList();
    CmpdListVO a = helperA.getCmpdListByCmpdListId(this.listAid, Boolean.TRUE);

    HelperCmpdList helperB = new HelperCmpdList();
    CmpdListVO b = helperB.getCmpdListByCmpdListId(this.listBid, Boolean.TRUE);

    ArrayList<CmpdVO> cmpdCollA = new ArrayList<CmpdVO>();
    for (CmpdListMemberVO clm : a.getCmpdListMembers()) {
      cmpdCollA.add(clm.getCmpd());
    }

    System.out.println("Size of cmpdCollA: " + cmpdCollA.size());

    ArrayList<CmpdVO> cmpdCollB = new ArrayList<CmpdVO>();
    for (CmpdListMemberVO clm : b.getCmpdListMembers()) {
      cmpdCollB.add(clm.getCmpd());
    }

    System.out.println("Size of cmpdCollB: " + cmpdCollB.size());

    // union via HashSet
    HashSet<CmpdVO> unionSet = new HashSet<CmpdVO>();
    unionSet.addAll(cmpdCollA);
    unionSet.addAll(cmpdCollB);
    ArrayList<CmpdVO> unionList = new ArrayList<CmpdVO>();
    unionList.addAll(unionSet);
    this.cmpdsListAorListB = unionList;

    ArrayList<CmpdVO> tempListA = new ArrayList<CmpdVO>(cmpdCollA);
    tempListA.retainAll(cmpdCollB);
    System.out.println("Size of tempListA after retainAll: " + tempListA.size());
    this.cmpdsListAandListB = new ArrayList<CmpdVO>(tempListA);

    tempListA = new ArrayList<CmpdVO>(cmpdCollA);
    tempListA.removeAll(cmpdCollB);
    System.out.println("Size of tempListA after removeAll: " + tempListA.size());
    this.cmpdsListAnotListB = new ArrayList<CmpdVO>(tempListA);

    return null;

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
      nscIntList.add(Integer.valueOf(s));
    }

    HelperCmpd cmpdHelper = new HelperCmpd();

    CmpdListVO tempCLvo = cmpdHelper.createCmpdListByNscs(this.listName, nscIntList, "kunkelm");

    // new fetch the list

    HelperCmpdList listHelper = new HelperCmpdList();

    CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(tempCLvo.getCmpdListId(), Boolean.TRUE);

    this.availableLists.add(clVO);

    this.selectedList = clVO;

    // MWK set the parentFragment to the LARGEST fragment by MW
    // THIS is busted while transitioning to CmpdView

//    for (CmpdListMemberVO nlmVO : this.selectedList.getCmpdListMembers()) {
//      CmpdVO cmpdVO = nlmVO.getCmpd();
//      ArrayList<CmpdFragmentVO> fragList = new ArrayList<CmpdFragmentVO>(cmpdVO.getCmpdFragments());
//      Collections.sort(fragList, new Comparators.CmpdFragmentSizeComparator());
//      Collections.reverse(fragList);
//      cmpdVO.setParentFragment(fragList.get(0));
//    }

    return null;

  }

  public String performSaveListLogicCmpds() {

    System.out.println("Now in performSaveListLogicCmpds()");

    ArrayList<Integer> nscIntList = new ArrayList<Integer>();

    for (CmpdVO c : this.listOfCmpds) {
      nscIntList.add(c.getNsc());
    }

    HelperCmpd cmpdHelper = new HelperCmpd();
    
    this.listName= "fillerTestFiller";

    CmpdListVO tempCLvo = cmpdHelper.createCmpdListByNscs(this.listName, nscIntList, "kunkelm");

    HelperCmpdList listHelper = new HelperCmpdList();

    CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(tempCLvo.getCmpdListId(), Boolean.TRUE);

    this.availableLists.add(clVO);

    this.selectedList = clVO;

    // MWK set the parentFragment to the LARGEST fragment by MW
    // THIS is busted while transitioning to CmpdView

//    for (CmpdListMemberVO nlmVO : this.selectedList.getCmpdListMembers()) {
//      CmpdVO cmpdVO = nlmVO.getCmpd();
//      ArrayList<CmpdFragmentVO> fragList = new ArrayList<CmpdFragmentVO>(cmpdVO.getCmpdFragments());
//      Collections.sort(fragList, new Comparators.CmpdFragmentSizeComparator());
//      Collections.reverse(fragList);
//      cmpdVO.setParentFragment(fragList.get(0));
//    }

    return null;

  }

  private static boolean doCheckForPseudoAtoms(IMolecule im) {
    boolean rtn = false;
    for (int atomCnt = 0; atomCnt
            < im.getAtomCount(); atomCnt++) {
      Atom thisAtom = (Atom) im.getAtom(atomCnt);
      if (thisAtom instanceof PseudoAtom) {
        rtn = true;
      }
    }
    return rtn;
  }

  public void handleFileUpload(FileUploadEvent event) {

    try {

      Random randomGenerator = new Random();

      UploadedFile file = event.getFile();

      String realPath = FacesContext.getCurrentInstance().getExternalContext().getRealPath(file.getFileName());

      FileOutputStream fos = new FileOutputStream(realPath, false);
      fos.write(file.getContents());
      fos.flush();
      fos.close();

      System.out.println(" fileName: " + file.getFileName() + " fileSize: " + file.getSize() + " realPath: " + realPath);

      FacesMessage msg = new FacesMessage(
              FacesMessage.SEVERITY_INFO,
              "Uploaded File",
              "fileName: " + file.getFileName() + " fileSize: " + file.getSize());

      FacesContext.getCurrentInstance().addMessage(null, msg);

      MoleculeParser mp = new MoleculeParser();

      File sdFile = new File(realPath);

      ArrayList<AdHocCmpd> adHocCmpdList = mp.parseSDF(sdFile);

      // have to persist the compounds
      // then associate with listMembers
      // then create list

      Session session = HibernateUtil.getSessionFactory().getCurrentSession();

      session.beginTransaction();

      Date now = new Date();

      String listName = "upload " + file.getFileName() + " " + now.toString();
      String listOwner = "kunkelm";
      String shareWith = "kunkelm";

      ArrayList<Cmpd> entityCmpdList = new ArrayList<Cmpd>();

      for (AdHocCmpd ahc : adHocCmpdList) {

        // come up with a unique adHocListId
        //do {      
        long randomId = randomGenerator.nextLong();
        if (randomId < 0) {
          randomId = -1 * randomId;
        }
        Long adHocCmpdId = new Long(randomId);
        //} while (this.getNovumListDao().searchUniqueNovumListId(novumListId) != null);

        ahc.setAdHocCmpdId(adHocCmpdId);
        ahc.setCmpdOwner(listOwner);
        ahc.setName(listName);

        session.persist("Cmpd", ahc);

        for (AdHocCmpdFragment ahcf : ahc.getAdHocCmpdFragments()) {
          // persist the struc and pchem
          session.persist(ahcf.getAdHocCmpdFragmentPChem());
          session.persist(ahcf.getAdHocCmpdFragmentStructure());
          // persist the fragment
          session.persist(ahcf);
        }

        // if only one fragment, make it the parent, otherwise sort by size

        ArrayList<AdHocCmpdFragment> fragList = new ArrayList<AdHocCmpdFragment>(ahc.getAdHocCmpdFragments());
        Collections.sort(fragList, new Comparators.AdHocCmpdFragmentSizeComparator());
        Collections.reverse(fragList);
        ahc.setAdHocCmpdParentFragment(fragList.get(0));

        // track the cmpds for addition to CmpdList
        entityCmpdList.add(ahc);

      }

      // create a new list

      //do {    
      long randomId = randomGenerator.nextLong();
      if (randomId < 0) {
        randomId = -1 * randomId;
      }
      Long cmpdListId = new Long(randomId);
      //} while (this.getNovumListDao().searchUniqueNovumListId(novumListId) != null);

      CmpdList cl = CmpdList.Factory.newInstance();

      cl.setCmpdListId(cmpdListId);
      cl.setListName(listName);
      cl.setDateCreated(now);
      cl.setListOwner(listOwner);
      cl.setShareWith(shareWith);
      cl.setCountListMembers(entityCmpdList.size());

      // id from GenerateSequence in Entity class
      session.persist(cl);

      //Set<CmpdListMember> listMemberSet = new HashSet<CmpdListMember>();

      for (Cmpd c : entityCmpdList) {
        CmpdListMember clm = CmpdListMember.Factory.newInstance();
        clm.setCmpd(c);
        clm.setCmpdList(cl);
        session.persist(clm);
      }

      // commit the new entries
      session.getTransaction().commit();

      // new fetch the list

      HelperCmpdList listHelper = new HelperCmpdList();

      CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(cl.getCmpdListId(), Boolean.TRUE);

      this.availableLists.add(clVO);
      this.selectedList = clVO;

      ArrayList<CmpdVO> uploadedCmpdList = new ArrayList<CmpdVO>();
      for (CmpdListMemberVO clmVO : clVO.getCmpdListMembers()) {
        uploadedCmpdList.add(clmVO.getCmpd());
      }

      this.listOfCmpds = uploadedCmpdList;

      System.out.println("UploadCmpds contains: " + this.listOfCmpds.size() + " cmpds");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public String performLoadSelectedList() {

    System.out.println("Entering performLoadSelectedList()");

    if (this.selectedList == null || this.selectedList.getCmpdListMembers().size() == 0) {
      System.out.println("List is null");
    }

    if (this.selectedList.getCmpdListMembers().size() == 0) {
      System.out.println("List is 0 length");
    }

    HelperCmpdList helper = new HelperCmpdList();
    CmpdListVO voList = helper.getCmpdListByCmpdListId(this.selectedList.getCmpdListId(), Boolean.TRUE);
    this.selectedList = voList;
    return null;
  }

//
//
// GETTERS AND SETTERS
//
//
  public Long getListAid() {
    return listAid;
  }

  public void setListAid(Long listAid) {
    this.listAid = listAid;
  }

  public Long getListBid() {
    return listBid;
  }

  public void setListBid(Long listBid) {
    this.listBid = listBid;
  }

  public String getListName() {
    return listName;
  }

  public void setListName(String listName) {
    this.listName = listName;
  }

  public Collection<CmpdVO> getCmpdsListAnotListB() {
    return cmpdsListAnotListB;
  }

  public void setCmpdsListAnotListB(Collection<CmpdVO> cmpdsListAnotListB) {
    this.cmpdsListAnotListB = cmpdsListAnotListB;
  }

  public Collection<CmpdVO> getCmpdsListAandListB() {
    return cmpdsListAandListB;
  }

  public void setCmpdsListAandListB(Collection<CmpdVO> cmpdsListAandListB) {
    this.cmpdsListAandListB = cmpdsListAandListB;
  }

  public Collection<CmpdVO> getCmpdsListAorListB() {
    return cmpdsListAorListB;
  }

  public void setCmpdsListAorListB(Collection<CmpdVO> cmpdsListAorListB) {
    this.cmpdsListAorListB = cmpdsListAorListB;
  }

  public ArrayList<SelectItem> getAvailableListSelectItems() {

    this.availableListSelectItems = new ArrayList<SelectItem>();

    for (CmpdListVO clVO : this.availableLists) {
      this.availableListSelectItems.add(new SelectItem(clVO.getCmpdListId(), clVO.getListName() + ", " + clVO.getCountListMembers() + " members"));
    }

    return availableListSelectItems;
  }

  public void setAvailableListSelectItems(ArrayList<SelectItem> availableListSelectItems) {
    this.availableListSelectItems = availableListSelectItems;
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

  public List<CmpdListVO> getAvailableLists() {
    return availableLists;
  }

  public void setAvailableLists(List<CmpdListVO> availableLists) {
    this.availableLists = availableLists;
  }

  public CmpdListVO getSelectedList() {
    return selectedList;
  }

  public void setSelectedList(CmpdListVO selectedList) {
    this.selectedList = selectedList;
  }

  public CmpdListVO[] getSelectedLists() {
    return selectedLists;
  }

  public void setSelectedLists(CmpdListVO[] selectedLists) {
    this.selectedLists = selectedLists;
  }

  public CmpdListMemberVO getSelectedListMember() {
    return selectedListMember;
  }

  public void setSelectedListMember(CmpdListMemberVO selectedListMember) {
    this.selectedListMember = selectedListMember;
  }

  public CmpdListMemberVO[] getSelectedListMembers() {
    return selectedListMembers;
  }

  public void setSelectedListMembers(CmpdListMemberVO[] selectedListMembers) {
    this.selectedListMembers = selectedListMembers;
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

  public List<CmpdVO> getListOfCmpds() {
    return listOfCmpds;
  }

  public void setListOfCmpds(List<CmpdVO> listOfCmpds) {
    this.listOfCmpds = listOfCmpds;
  }
}
