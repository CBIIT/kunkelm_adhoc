/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
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
import mwk.datasystem.mwkcharting.Histogram;
import mwk.datasystem.mwkcharting.HistogramBin;
import mwk.datasystem.util.Comparators;
import mwk.datasystem.util.ExtendedCartesianChartModel;
import mwk.datasystem.util.HelperCmpd;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.util.HibernateUtil;
import mwk.datasystem.util.HistogramChartUtil;
import mwk.datasystem.util.MoleculeParser;
import mwk.datasystem.util.ScatterPlotChartUtil;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import mwk.datasystem.vo.CmpdVO;
import org.apache.commons.io.IOUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.Session;
import org.openscience.cdk.Atom;
import org.openscience.cdk.DefaultChemObjectBuilder;
import org.openscience.cdk.Molecule;
import org.openscience.cdk.PseudoAtom;
import org.openscience.cdk.interfaces.IMolecule;
import org.openscience.cdk.smiles.SmilesParser;
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
  //
  private int outerTabViewActiveIndex;
  private int listManagerTabViewActiveIndex;
  private int activeListTabViewActiveIndex;
  private int substrctureTabViewActiveIndex;
  
  //
  private List<String> parametersPchem;
  //
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
// used when saving listLogic results
  private List<CmpdVO> listOfCmpds;
  // 
  private CmpdListVO listForDelete;

  //
  public ListManagerController() {
    this.performUpdateAvailableLists();
  }

  public int mySelectedSort(Object o1, Object o2) {

    int rtn = 0;

    if (o1 == null) {
      o1 = Boolean.FALSE;
    }
    if (o2 == null) {
      o2 = Boolean.FALSE;
    }

    Boolean sel1 = (Boolean) o1;
    Boolean sel2 = (Boolean) o2;

    if (sel1) {
      if (sel2) {
        rtn = 0;
      } else {
        rtn = 1;
      }
    } else {
      if (sel2) {
        rtn = -1;
      } else {
        rtn = 0;
      }
    }

    //System.out.println("sel1, sel2: " + sel1 + " " + sel2 + " rtn is: " + rtn);

    return rtn;

  }

  public String placeholderAction() {
    return null;
  }
  //  
  private ArrayList<Histogram> histoModel;
  // Scatterplots
  private ArrayList<ExtendedCartesianChartModel> scatterPlotModel;

  //
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

    // reset flags on all listMembers in selectedList    
    for (CmpdListMemberVO clmVO : this.selectedList.getCmpdListMembers()) {
      clmVO.setIsSelected(Boolean.FALSE);
    }

    // this is the new selection
    this.selectedListMembers = clmList.toArray(new CmpdListMemberVO[clmList.size()]);
    // which needs updating
    for (CmpdListMemberVO clmVO : this.selectedListMembers) {
      clmVO.setIsSelected(new Boolean(Boolean.TRUE));
    }

    // regenerate the histoModel
    this.histoModel = HistogramChartUtil.doHistograms(this.selectedList, this.parametersPchem);
    this.scatterPlotModel = ScatterPlotChartUtil.generateScatter(this.selectedList, this.parametersPchem);

  }

  public String renderHistoAndScatter() {

    System.out.println("Entering renderHistoAndScatter()");

    for (String s : this.parametersPchem) {
      System.out.println("Selected pChemParam: " + s);
    }

    // only fetch if not already fetched    
    if (this.selectedList.getCmpdListMembers() == null || this.selectedList.getCmpdListMembers().size() == 0) {
      System.out.println("------------------------List was not populated. Now fetching listMembers");
      HelperCmpdList helper = new HelperCmpdList();
      CmpdListVO voList = helper.getCmpdListByCmpdListId(this.selectedList.getCmpdListId(), Boolean.TRUE);
      this.selectedList.setCmpdListMembers(voList.getCmpdListMembers());
    } else {
      System.out.println("------------------------List WAS populated. No need to fetch");
    }

    this.histoModel = HistogramChartUtil.doHistograms(this.selectedList, this.parametersPchem);
    this.scatterPlotModel = ScatterPlotChartUtil.generateScatter(this.selectedList, this.parametersPchem);

    this.selectedListMembers = new CmpdListMemberVO[0];

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

  public void handleDeleteList(ActionEvent actionEvent) {

    System.out.println("Now in handleDeleteList in listManagerController.");

    if (this.listForDelete.getListOwner().equals("kunkelm")) {
      HelperCmpdList helper = new HelperCmpdList();
      helper.deleteCmpdListByCmpdListId(this.listForDelete.getCmpdListId());
      this.availableLists.remove(this.listForDelete);
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

    // now fetch the list

    HelperCmpdList listHelper = new HelperCmpdList();

    CmpdListVO clVO = listHelper.getCmpdListByCmpdListId(tempCLvo.getCmpdListId(), Boolean.TRUE);

    this.availableLists.add(clVO);

    this.selectedList = clVO;
    
    this.outerTabViewActiveIndex = 1;
    this.activeListTabViewActiveIndex = 1;

    return null;

  }

  public String performSaveListLogicCmpds() {

    System.out.println("Now in performSaveListLogicCmpds()");

    ArrayList<Integer> nscIntList = new ArrayList<Integer>();

    for (CmpdVO c : this.listOfCmpds) {
      nscIntList.add(c.getNsc());
    }

    HelperCmpd cmpdHelper = new HelperCmpd();

    this.listName = "fillerTestFiller";

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

      String listName = file.getFileName();
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

      this.outerTabViewActiveIndex = 1;
      this.activeListTabViewActiveIndex = 1;

      System.out.println("UploadCmpds contains: " + this.listOfCmpds.size() + " cmpds");

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public String performLoadSelectedList() {

    System.out.println("Entering performLoadSelectedList()");

    // only fetch if not already fetched    
    if (this.selectedList.getCmpdListMembers() == null || this.selectedList.getCmpdListMembers().size() == 0) {
      System.out.println("------------------------List was not populated. Now fetching listMembers");
      HelperCmpdList helper = new HelperCmpdList();
      CmpdListVO voList = helper.getCmpdListByCmpdListId(this.selectedList.getCmpdListId(), Boolean.TRUE);
      this.selectedList.setCmpdListMembers(voList.getCmpdListMembers());
    } else {
      System.out.println("------------------------List WAS populated. No need to fetch");
    }

    this.selectedListMembers = new CmpdListMemberVO[0];

    this.outerTabViewActiveIndex = 1;
    this.activeListTabViewActiveIndex = 1;

    return null;
  }

  // <editor-fold desc="GETTERS and SETTERS" defaultstate="collapsed">
  public int getOuterTabViewActiveIndex() {
    return outerTabViewActiveIndex;
  }

  public void setOuterTabViewActiveIndex(int outerTabViewActiveIndex) {
    this.outerTabViewActiveIndex = outerTabViewActiveIndex;
  }

  public int getListManagerTabViewActiveIndex() {
    return listManagerTabViewActiveIndex;
  }

  public void setListManagerTabViewActiveIndex(int listManagerTabViewActiveIndex) {
    this.listManagerTabViewActiveIndex = listManagerTabViewActiveIndex;
  }

  public int getActiveListTabViewActiveIndex() {
    return activeListTabViewActiveIndex;
  }

  public void setActiveListTabViewActiveIndex(int activeListTabViewActiveIndex) {
    this.activeListTabViewActiveIndex = activeListTabViewActiveIndex;
  }

  public int getSubstrctureTabViewActiveIndex() {
    return substrctureTabViewActiveIndex;
  }

  public void setSubstrctureTabViewActiveIndex(int substrctureTabViewActiveIndex) {
    this.substrctureTabViewActiveIndex = substrctureTabViewActiveIndex;
  }

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

  public List<String> getParametersPchem() {
    return parametersPchem;
  }

  public void setParametersPchem(List<String> parametersPchem) {
    this.parametersPchem = parametersPchem;
  }

  public CmpdListVO getListForDelete() {
    return listForDelete;
  }

  public void setListForDelete(CmpdListVO listForDelete) {
    this.listForDelete = listForDelete;
  }

  public ArrayList<Histogram> getHistoModel() {
    return histoModel;
  }

  public void setHistoModel(ArrayList<Histogram> histoModel) {
    this.histoModel = histoModel;
  }

  public ArrayList<ExtendedCartesianChartModel> getScatterplotModel() {
    return scatterPlotModel;
  }

  public void setScatterplotModel(ArrayList<ExtendedCartesianChartModel> scatterplotModel) {
    this.scatterPlotModel = scatterplotModel;
  }
// </editor-fold>

  public void postProcessXLS(Object document) {

    try {

      System.out.println("In postProcessXLS in listManagerController.");

      HSSFWorkbook wb = (HSSFWorkbook) document;
      HSSFSheet sheet = wb.getSheetAt(0);

      CreationHelper helper = wb.getCreationHelper();

      // Create the drawing patriarch.  This is the top level container for all shapes. 
      Drawing drawing = sheet.createDrawingPatriarch();

      for (Row row : sheet) {

        String title = null;

        System.out.println("In row: " + row.getRowNum());

        // get the SMILES column
        if (row.getCell(12).getStringCellValue() != null) {

          row.setHeightInPoints(200);

          String smiles = row.getCell(12).getStringCellValue();
          System.out.println("SMILES is: " + smiles);


          if (row.getCell(3).getStringCellValue() != null) {
            title = row.getCell(3).getStringCellValue();
          }

          byte[] bytes = getStructureImage(smiles, title);
          int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

          //add a picture shape
          ClientAnchor anchor = helper.createClientAnchor();

          //set top-left corner of the picture,
          //subsequent call of Picture#resize() will operate relative to it

          anchor.setCol1(13);
          anchor.setRow1(row.getRowNum());

          anchor.setAnchorType(ClientAnchor.MOVE_AND_RESIZE);

          Picture pict = drawing.createPicture(anchor, pictureIdx);

          //auto-size picture relative to its top-left corner
          pict.resize();

        }

//        for (Cell cell : row) {
//          //cell.setCellValue(cell.getStringCellValue().toUpperCase());        
//          cell.setCellStyle(style);
//        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    }

  }

  public byte[] getStructureImage(String smiles, String title) throws Exception {

    String servletString = "notSet";
    java.net.URL servletURL = null;

    java.net.HttpURLConnection servletConn = null;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {

      Properties props = new Properties();
      try {
        InputStream is = ListManagerController.class.getResourceAsStream("/deployment.properties");
        props.load(is);
        servletString = props.getProperty("structure.servlet.url");
      } catch (Exception e) {
        e.printStackTrace();
      }

      servletURL = new java.net.URL(servletString);

      servletConn = (java.net.HttpURLConnection) servletURL.openConnection();
      servletConn.setDoInput(true);
      servletConn.setDoOutput(true);
      servletConn.setUseCaches(false);
      servletConn.setRequestMethod("POST");
      servletConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

      java.io.DataOutputStream outStream = new java.io.DataOutputStream(servletConn.getOutputStream());

      outStream.writeBytes("smiles=" + URLEncoder.encode(smiles, "UTF-8"));

      if (title != null) {
        outStream.writeBytes("&title=" + URLEncoder.encode(title, "UTF-8"));
      }

      outStream.flush();
      outStream.close();

      if (servletConn.getResponseCode() != servletConn.HTTP_OK) {
        throw new Exception("Exception from StructureServlet in getStructureImage in ListManagerController: " + servletConn.getResponseMessage());
      }


//      String tempString = new String();
//      java.io.BufferedReader theReader = new java.io.BufferedReader(new InputStreamReader(servletConn.getInputStream()));
//      while ((tempString = theReader.readLine()) != null) {
//        returnString += tempString;
//      }

      InputStream is = servletConn.getInputStream();

      byte[] buf = new byte[1000];
      for (int nChunk = is.read(buf); nChunk != -1; nChunk = is.read(buf)) {
        baos.write(buf, 0, nChunk);
      }

    } catch (Exception e) {
      System.out.println("Exception in getStructureImage in ListManagerController " + e);
      e.printStackTrace();
      throw new Exception(e);
    } finally {
      servletConn.disconnect();
      servletConn = null;
    }

    baos.flush();
    return baos.toByteArray();

  }

  public String processPains() {

    String[] painsArray = {
      "thiaz_ene_A(128)", "[#6]-1(=[#6](-[$([#1]),$([#6](-[#1])-[#1]),$([#6]=[#8])])-[#16]-[#6](-[#7]-1-[$([#1]),$([#6]-[#1]),$([#6]:[#6])])=[#7;!R])-[$([#6](-[#1])-[#1]),$([#6]:[#6])]",
      "pyrrole_A(118)", "n2(-[#6]:1:[!#1]:[#6]:[#6]:[#6]:[#6]:1)c(cc(c2-[#6;X4])-[#1])-[#6;X4]",
      "catechol_A(92)", "c:1:c:c(:c(:c:c:1)-[#8]-[#1])-[#8]-[#1]",
      "ene_five_het_B(90)", "[#6]-1(=[#6])-[#6](-[#7]=[#6]-[#16]-1)=[#8]",
      "imine_one_fives(89)", "[#6]-1=[!#1]-[!#6&!#1]-[#6](-[#6]-1=[!#6&!#1;!R])=[#8]",
      "ene_five_het_C(85)", "[#6]-1(-[#6](-[#6]=[#6]-[!#6&!#1]-1)=[#6])=[!#6&!#1]",
      "hzone_pipzn(79)", "[#6]-[#7]-1-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])(-[#1])-[#6]-1(-[#1])-[#1])-[#7]=[#6](-[#1])-[#6]:[!#1]",
      "keto_keto_beta_A(68)", "c:1-2:c(:c:c:c:c:1)-[#6](=[#8])-[#6;X4]-[#6]-2=[#8]",
      "hzone_pyrrol(64)", "n1(-[#6])c(c(-[#1])c(c1-[#6]=[#7]-[#7])-[#1])-[#1]",
      "ene_one_ene_A(57)", "[#6]=!@[#6](-[!#1])-@[#6](=!@[!#6&!#1])-@[#6](=!@[#6])-[!#1]",
      "cyano_ene_amine_A(56)", "[#6](-[#6]#[#7])(-[#6]#[#7])-[#6](-[#7](-[#1])-[#1])=[#6]-[#6]#[#7]",
      "ene_five_one_A(55)", "c:1-2:c(:c:c:c:c:1)-[#6](=[#8])-[#6](=[#6])-[#6]-2=[#8]",
      "cyano_pyridone_A(54)", "[#6]-1(=[!#1]-[!#1]=[!#1]-[#7](-[#6]-1=[#16])-[#1])-[#6]#[#7]",
      "anil_alk_ene(51)", "c:1:c:c-2:c(:c:c:1)-[#6]-3-[#6](-[#6]-[#7]-2)-[#6]-[#6]=[#6]-3",
      "hzone_anil_di_alk(35)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])=[#7]-[#7]-[$([#6](=[#8])-[#6](-[#1])(-[#1])-[#16]-[#6]:[#7]),$([#6](=[#8])-[#6](-[#1])(-[#1])-[!#1]:[!#1]:[#7]),$([#6](=[#8])-[#6]:[#6]-[#8]-[#1]),$([#6]:[#7]),$([#6](-[#1])(-[#1])-[#6](-[#1])-[#8]-[#1])])-[#1])-[#1]",
      "rhod_sat_A(33)", "[#7]-1-[#6](=[#16])-[#16]-[#6;X4]-[#6]-1=[#8]",
      "amino_acridine_A(46)", "c:1:c:2:c(:c:c:c:1):n:c:3:c(:c:2-[#7]):c:c:c:c:3",
      "ene_five_het_D(46)", "[#6]-1(=[#6])-[#6](=[#8])-[#7]-[#7]-[#6]-1=[#8]",
      "thiophene_amino_Aa(45)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:s:1)-[!#1])-[!#1])-[#6]=[#8]",
      "ene_five_het_E(44)", "[#7]-[#6]=!@[#6]-2-[#6](=[#8])-c:1:c:c:c:c:c:1-[!#6&!#1]-2",
      "sulfonamide_A(43)", "c:1(:c(:c(:c(:c(:c:1-[#8]-[#1])-[F,Cl,Br,I])-[#1])-[F,Cl,Br,I])-[#1])-[#16](=[#8])(=[#8])-[#7]",
      "thio_ketone(43)", "[#6]-[#6](=[#16])-[#6]",
      "sulfonamide_B(41)", "c:1:c:c(:c:c:c:1-[#8]-[#1])-[#7](-[#1])-[#16](=[#8])=[#8]",
      "anil_OH_alk_A(8)", "c:1:c(:c:c:c:c:1)-[#6](-[#1])(-[#1])-[#7](-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#1])-[#1])-[#1]",
      "het_thio_656c(1)", "c:1:c(:c:c:c:c:1)-[#6]-4=[#7]-[#7]:2:[#6](:[#7+]:c:3:c:2:c:c:c:c:3)-[#16]-[#6;X4]-4",
      "anil_no_alk(40)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[$([#8]),$([#7]),$([#6](-[#1])-[#1])])-[#1])-[#1])-[#7](-[#1])-[#1]",
      "thiophene_amino_Ab(40)", "[$([#1]),$([#6](-[#1])-[#1]),$([#6]:[#6])]-c:1:c(:c(:c(:s:1)-[#7](-[#1])-[#6](=[#8])-[#6])-[#6](=[#8])-[#8])-[$([#6]:1:[#6]:[#6]:[#6]:[#6]:[#6]:1),$([#6]:1:[#16]:[#6]:[#6]:[#6]:1)]",
      "het_pyridiniums_A(39)", "[#7+]:1(:[#6]:[#6]:[!#1]:c:2:c:1:c(:c(-[$([#1]),$([#7])]):c:c:2)-[#1])-[$([#6](-[#1])(-[#1])-[#1]),$([#8;X1]),$([#6](-[#1])(-[#1])-[#6](-[#1])=[#6](-[#1])-[#1]),$([#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#8]-[#1]),$([#6](-[#1])(-[#1])-[#6](=[#8])-[#6]),$([#6](-[#1])(-[#1])-[#6](=[#8])-[#7](-[#1])-[#6]:[#6]),$([#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#1])]",
      "anthranil_one_A(38)", "c:1:c:c:c:c(:c:1-[#7](-[#1])-[!$([#6]=[#8])])-[#6](-[#6]:[#6])=[#8]",
      "cyano_imine_A(37)", "[#7](-[#1])-[#7]=[#6](-[#6]#[#7])-[#6]=[!#6&!#1;!R]",
      "diazox_sulfon_A(36)", "[#7](-c:1:c:c:c:c:c:1)-[#16](=[#8])(=[#8])-[#6]:2:[#6]:[#6]:[#6]:[#6]:3:[#7]:[$([#8]),$([#16])]:[#7]:[#6]:2:3",
      "het_5_ene(1)", "[#6]-2(=[#8])-[#6](=[#6](-[#6](-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])-[#7]=[#6](-c:1:c:c:c:c:c:1)-[#8]-2",
      "hzone_enamin(30)", "[#7](-[#1])-[#7]=[#6]-[#6](-[$([#1]),$([#6])])=[#6](-[#6])-!@[$([#7]),$([#8]-[#1])]",
      "pyrrole_B(29)", "n2(-[#6]:1:[!#1]:[#6]:[#6]:[#6]:[#6]:1)c(cc(c2-[#6]:[#6])-[#1])-[#6;X4]",
      "thiophene_hydroxy(28)", "s1ccc(c1)-[#8]-[#1]",
      "cyano_pyridone_B(27)", "[#6]-1(=[#6](-[#6](=[#8])-[#7]-[#6](=[#7]-1)-[!#6&!#1])-[#6]#[#7])-[#6]",
      "imine_one_sixes(27)", "[#6]-1(-[#6](=[#8])-[#7]-[#6](=[#8])-[#7]-[#6]-1=[#8])=[#7]",
      "dyes5A(27)", "[#6](-[#1])(-[#1])-[#7]([#6]:[#6])~[#6][#6]=,:[#6]-[#6]~[#6][#7]",
      "naphth_amino_A(25)", "c:2:c:1:c:c:c:c-3:c:1:c(:c:c:2)-[#7]-[#6]=[#7]-3",
      "naphth_amino_B(25)", "c:2:c:1:c:c:c:c-3:c:1:c(:c:c:2)-[#7](-[#6;X4]-[#7]-3-[#1])-[#1]",
      "ene_one_ester(24)", "[#6]-[#6](=[#8])-[#6](-[#1])=[#6](-[#7](-[#1])-[#6])-[#6](=[#8])-[#8]-[#6]",
      "thio_dibenzo(23)", "[#16]=[#6]-1-[#6]=,:[#6]-[!#6&!#1]-[#6]=,:[#6]-1",
      "cyano_cyano_A(23)", "[#6](-[#6]#[#7])(-[#6]#[#7])-[#6](-[$([#6]#[#7]),$([#6]=[#7])])-[#6]#[#7]",
      "hzone_acyl_naphthol(22)", "c:1:2:c(:c(:c(:c(:c:1:c(:c(:c(:c:2-[#1])-[#8]-[#1])-[#6](=[#8])-[#7](-[#1])-[#7]=[#6])-[#1])-[#1])-[#1])-[#1])-[#1]",
      "het_65_A(21)", "[#8]=[#6]-c2c1nc(-[#6](-[#1])-[#1])cc(-[#8]-[#1])n1nc2",
      "imidazole_A(19)", "n:1:c(:n(:c(:c:1-c:2:c:c:c:c:c:2)-c:3:c:c:c:c:c:3)-[#1])-[#6]:[!#1]",
      "anil_OC_alk_D(2)", "[#6](-[#1])(-[#1])-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:c:c:c:c:c:2-[$([#6](-[#1])-[#1]),$([#8]-[#6](-[#1])-[#1])]",
      "ene_cyano_A(19)", "[#6](-[#6]#[#7])(-[#6]#[#7])=[#6]-c:1:c:c:c:c:c:1",
      "anthranil_acid_A(19)", "c:1(:c:c:c:c:c:1-[#7](-[#1])-[#7]=[#6])-[#6](=[#8])-[#8]-[#1]",
      "dyes3A(19)", "[#7+]([#6]:[#6])=,:[#6]-[#6](-[#1])=[#6]-[#7](-[#6;X4])-[#6]",
      "dhp_bis_amino_CN(19)", "[#7](-[#1])(-[#1])-[#6]-1=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#6](=[#6](-[#7](-[#1])-[#1])-[#16]-1)-[#6]#[#7]",
      "het_6_tetrazine(18)", "[#7]~[#6]:1:[#7]:[#7]:[#6](:[$([#7]),$([#6]-[#1]),$([#6]-[#7]-[#1])]:[$([#7]),$([#6]-[#7])]:1)-[$([#7]-[#1]),$([#8]-[#6](-[#1])-[#1])]",
      "ene_one_hal(17)", "[#6]-[#6]=[#6](-[F,Cl,Br,I])-[#6](=[#8])-[#6]",
      "cyano_imine_B(17)", "[#6](-[#6]#[#7])(-[#6]#[#7])=[#7]-[#7](-[#1])-c:1:c:c:c:c:c:1",
      "thiaz_ene_B(17)", "[#6]-1(=[#6](-!@[#6](=[#8])-[#7]-[#6](-[#1])-[#1])-[#16]-[#6](-[#7]-1-[$([#6](-[#1])(-[#1])-[#6](-[#1])=[#6](-[#1])-[#1]),$([#6]:[#6])])=[#16])-[$([#7]-[#6](=[#8])-[#6]:[#6]),$([#7](-[#1])-[#1])]",
      "het_65_B(7)", "[!#1]:1:[!#1]-2:[!#1](:[!#1]:[!#1]:[!#1]:1)-[#7](-[#1])-[#7](-[#6]-2=[#8])-[#6]",
      "keto_keto_beta_C(7)", "c:1:c:c-2:c(:c:c:1)-[#6](=[#6](-[#6]-2=[#8])-[#6])-[#8]-[#1]",
      "ene_rhod_B(16)", "[#16]-1-[#6](=[#8])-[#7]-[#6](=[#8])-[#6]-1=[#6](-[#1])-[$([#6]-[#35]),$([#6]:[#6](-[#1]):[#6](-[F,Cl,Br,I]):[#6]:[#6]-[F,Cl,Br,I]),$([#6]:[#6](-[#1]):[#6](-[#1]):[#6]-[#16]-[#6](-[#1])-[#1]),$([#6]:[#6]:[#6]:[#6]:[#6]:[#6]:[#6]:[#6]:[#6]:[#6]-[#8]-[#6](-[#1])-[#1]),$([#6]:1:[#6](-[#6](-[#1])-[#1]):[#7](-[#6](-[#1])-[#1]):[#6](-[#6](-[#1])-[#1]):[#6]:1)]",
      "thio_carbonate_A(15)", "[#8]-1-[#6](-[#16]-c:2:c-1:c:c:c(:c:2)-[$([#7]),$([#8])])=[$([#8]),$([#16])]",
      "cyano_pyridone_C(11)", "[#6]-1(-[#6](=[#6](-[#6]#[#7])-[#6](~[#8])~[#7]~[#6]-1~[#8])-[#6](-[#1])-[#1])=[#6](-[#1])-[#6]:[#6]",
      "anil_di_alk_furan_A(15)", "[#7](-[#6](-[#1])-[#1])(-[#6](-[#1])-[#1])-c:1:c(:c(:c(:o:1)-[#6]=[#7]-[#7](-[#1])-[#6]=[!#6&!#1])-[#1])-[#1]",
      "ene_five_het_F(15)", "c:1(:c:c:c:c:c:1)-[#6](-[#1])=!@[#6]-3-[#6](=[#8])-c:2:c:c:c:c:c:2-[#16]-3",
      "anil_di_alk_F(14)", "c:1:c:c(:c:c:c:1-[#6;X4]-c:2:c:c:c(:c:c:2)-[#7](-[$([#1]),$([#6;X4])])-[$([#1]),$([#6;X4])])-[#7](-[$([#1]),$([#6;X4])])-[$([#1]),$([#6;X4])]",
      "hzone_anil(14)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[#7](-[#1])-[#1])-[#1])-[#1])-[#6]=[#7]-[#7]-[#1]",
      "het_5_pyrazole_OH(14)", "c1(nn(c(c1-[$([#1]),$([#6]-[#1])])-[#8]-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#1])-[#1])-[#1])-[#6;X4]",
      "het_thio_666_A(13)", "c:2(:c:1-[#16]-c:3:c(-[#7](-c:1:c(:c(:c:2-[#1])-[#1])-[#1])-[$([#1]),$([#6](-[#1])(-[#1])-[#1]),$([#6](-[#1])(-[#1])-[#6]-[#1])]):c(:c(~[$([#1]),$([#6]:[#6])]):c(:c:3-[#1])-[$([#1]),$([#7](-[#1])-[#1]),$([#8]-[#6;X4])])~[$([#1]),$([#7](-[#1])-[#6;X4]),$([#6]:[#6])])-[#1]",
      "ene_rhod_D(8)", "[#16]-1-[#6](=!@[#7]-[$([#1]),$([#7](-[#1])-[#6]:[#6])])-[#7](-[$([#1]),$([#6]:[#7]:[#6]:[#6]:[#16])])-[#6](=[#8])-[#6]-1=[#6](-[#1])-[#6]:[#6]-[$([#17]),$([#8]-[#6]-[#1])]",
      "styrene_A(13)", "[#6]-2-[#6]-c:1:c(:c:c:c:c:1)-[#6](-c:3:c:c:c:c:c-2:3)=[#6]-[#6]",
      "ene_rhod_C(13)", "[#16]-1-[#6](=[#7]-[#6]:[#6])-[#7](-[$([#1]),$([#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#8]),$([#6]:[#6])])-[#6](=[#8])-[#6]-1=[#6](-[#1])-[$([#6]:[#6]:[#6]-[#17]),$([#6]:[!#6&!#1])]",
      "dhp_amino_CN_A(13)", "[#7](-[#1])(-[#1])-[#6]-1=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#6](=[#6](-[#6]=[#6])-[#8]-1)-[#6](-[#1])-[#1]",
      "cyano_imine_C(12)", "[#8]=[#16](=[#8])-[#6](-[#6]#[#7])=[#7]-[#7]-[#1]",
      "thio_urea_A(12)", "c:1:c:c:c:c:c:1-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:2:c:c:c:c:c:2",
      "thiophene_amino_B(12)", "c:1:c(:c:c:c:c:1)-[#7](-[#1])-c:2:c(:c(:c(:s:2)-[$([#6]=[#8]),$([#6]#[#7]),$([#6](-[#8]-[#1])=[#6])])-[#7])-[$([#6]#[#7]),$([#6](:[#7]):[#7])]",
      "keto_keto_beta_B(12)", "[#6;X4]-1-[#6](=[#8])-[#7]-[#7]-[#6]-1=[#8]",
      "keto_phenone_A(11)", "c:1:c-3:c(:c:c:c:1)-[#6]:2:[#7]:[!#1]:[#6]:[#6]:[#6]:2-[#6]-3=[#8]",
      "thiaz_ene_C(11)", "[#6]-1(=[#6](-!@[#6]=[#7])-[#16]-[#6](-[#7]-1)=[#8])-[$([F,Cl,Br,I]),$([#7+](:[#6]):[#6])]",
      "hzone_thiophene_A(11)", "c:1:2:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1]):[!#6&!#1]:[#6](:[#6]:2-[#6](-[#1])=[#7]-[#7](-[#1])-[$([#6]:1:[#7]:[#6]:[#6](-[#1]):[#16]:1),$([#6]:[#6](-[#1]):[#6]-[#1]),$([#6]:[#7]:[#6]:[#7]:[#6]:[#7]),$([#6]:[#7]:[#7]:[#7]:[#7])])-[$([#1]),$([#8]-[#1]),$([#6](-[#1])-[#1])]",
      "ene_quin_methide(10)", "[!#1]:[!#1]-[#6](-[$([#1]),$([#6]#[#7])])=[#6]-1-[#6]=,:[#6]-[#6](=[$([#8]),$([#7;!R])])-[#6]=,:[#6]-1",
      "het_thio_676_A(10)", "c:1:c:c-2:c(:c:c:1)-[#6]-[#6](-c:3:c(-[#16]-2):c(:c(-[#1]):c(:c:3-[#1])-[$([#1]),$([#8]),$([#16;X2]),$([#6;X4]),$([#7](-[$([#1]),$([#6;X4])])-[$([#1]),$([#6;X4])])])-[#1])-[#7](-[$([#1]),$([#6;X4])])-[$([#1]),$([#6;X4])]",
      "ene_five_het_G(10)", "[#6]-1(=[#8])-[#6](=[#6](-[#1])-[$([#6]:1:[#6]:[#6]:[#6]:[#6]:[#6]:1),$([#6]:1:[#6]:[#6]:[#6]:[!#6&!#1]:1)])-[#7]=[#6](-[!#1]:[!#1]:[!#1])-[$([#16]),$([#7]-[!#1]:[!#1])]-1",
      "acyl_het_A(9)", "[#7+](:[!#1]:[!#1]:[!#1])-[!#1]=[#8]",
      "anil_di_alk_G(9)", "[#6;X4]-[#7](-[#6;X4])-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6]2=,:[#7][#6]:[#6]:[!#1]2)-[#1])-[#1]",
      "thio_imide_A(1)", "c:1:c(:c:c:c:c:1)-[#7]-2-[#6](=[#8])-[#6](=[#6](-[#1])-[#6]-2=[#8])-[#16]-c:3:c:c:c:c:c:3",
      "dhp_keto_A(9)", "[#7]-1(-[$([#6;X4]),$([#1])])-[#6]=,:[#6](-[#6](=[#8])-[#6]:[#6]:[#6])-[#6](-[#6])-[#6](=[#6]-1-[#6](-[#1])(-[#1])-[#1])-[$([#6]=[#8]),$([#6]#[#7])]",
      "thio_urea_B(9)", "c:1:c:c:c:c:c:1-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:2:c:c:c:c:c:2",
      "anil_alk_bim(9)", "c:1:3:c(:c(:c(:c(:c:1-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:c:c:c:c:c:2)-[#1]):n:c(-[#1]):n:3-[#6]",
      "imine_imine_A(9)", "c:1:c:c-2:c(:c:c:1)-[#7]=[#6]-[#6]-2=[#7;!R]",
      "thio_urea_C(9)", "c:1(:c:c:c:c:c:1)-[#7](-[#1])-[#6](=[#16])-[#7]-[#7](-[#1])-[#6](=[#8])-[#6]-2:[!#1]:[!#6&!#1]:[#6]:[#6]-2",
      "imine_one_fives_B(9)", "[#7;!R]=[#6]-2-[#6](=[#8])-c:1:c:c:c:c:c:1-[#16]-2",
      "ene_rhod_E(8)", "[#16]-1-[#6](=[#8])-[#7]-[#6](=[#16])-[#6]-1=[#6](-[#1])-[#6]:[#6]",
      "dhp_amino_CN_B(9)", "[$([#7](-[#1])-[#1]),$([#8]-[#1])]-[#6]-2=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-c:1:c(:n(-[#6]):n:c:1)-[#8]-2",
      "anil_OC_no_alk_A(8)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:n:c:1-[#1])-[#8]-c:2:c:c:c:c:c:2)-[#1])-[#1]",
      "het_thio_66_one(8)", "[#6](=[#8])-[#6]-1=[#6]-[#7]-c:2:c(-[#16]-1):c:c:c:c:2",
      "styrene_B(8)", "c:1:c:c-2:c(:c:c:1)-[#6](-c:3:c(-[$([#16;X2]),$([#6;X4])]-2):c:c:c(:c:3)-[$([#1]),$([#17]),$([#6;X4])])=[#6]-[#6]",
      "het_thio_5_A(8)", "[#6](-[#1])(-[#1])-[#16;X2]-c:1:n:c(:c(:n:1-!@[#6](-[#1])-[#1])-c:2:c:c:c:c:c:2)-[#1]",
      "anil_di_alk_ene_A(8)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6]-2=[#6](-[#1])-c:1:c(:c:c:c:c:1)-[#16;X2]-c:3:c-2:c:c:c:c:3",
      "pyrrole_C(8)", "n1(-[#6;X4])c(c(-[#1])c(c1-[#6]:[#6])-[#1])-[#6](-[#1])-[#1]",
      "thio_urea_D(8)", "c:1(:c:c:c:c:c:1)-[#7](-[#1])-[#6](=[#16])-[#7]-[#7](-[#1])-c:2:c:c:c:c:c:2",
      "thiaz_ene_D(8)", "[#7](-c:1:c:c:c:c:c:1)-c2[n+]c(cs2)-c:3:c:c:c:c:c:3",
      "ene_rhod_F(8)", "n:1:c:c:c(:c:1-[#6](-[#1])-[#1])-[#6](-[#1])=[#6]-2-[#6](=[#8])-[#7]-[#6](=[!#6&!#1])-[#7]-2",
      "thiaz_ene_E(8)", "[#6]-1(=[#6](-[#6](-[#1])(-[#6])-[#6])-[#16]-[#6](-[#7]-1-[$([#1]),$([#6](-[#1])-[#1])])=[#8])-[#16]-[#6;R]",
      "keto_keto_gamma(5)", "[#8]=[#6]-1-[#6;X4]-[#6]-[#6](=[#8])-c:2:c:c:c:c:c-1:2",
      "het_66_A(7)", "c:2:c:c:1:n:n:c(:n:c:1:c:c:2)-[#6](-[#1])(-[#1])-[#6]=[#8]",
      "thio_urea_E(7)", "c:1:c:c:c:c:c:1-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:n:c:c:c:c:2",
      "thiophene_amino_C(7)", "[#6](-[#1])-[#6](-[#1])(-[#1])-c:1:c(:c(:c(:s:1)-[#7](-[#1])-[#6](=[#8])-[#6]-[#6]-[#6]=[#8])-[$([#6](=[#8])-[#8]),$([#6]#[#7])])-[#6](-[#1])-[#1]",
      "hzone_phenone(7)", "[#6](-c:1:c(:c(:c(:c:c:1-[#1])-[$([#6;X4]),$([#1])])-[#1])-[#1])(-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[$([#1]),$([#17])])-[#1])-[#1])=[$([#7]-[#8]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]),$([#7]-[#8]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]),$([#7]-[#7](-[#1])-[#6](=[#7]-[#1])-[#7](-[#1])-[#1]),$([#6](-[#1])-[#7])]",
      "ene_rhod_G(7)", "[#8](-[#1])-[#6](=[#8])-c:1:c:c(:c:c:c:1)-[#6]:[!#1]:[#6]-[#6](-[#1])=[#6]-2-[#6](=[!#6&!#1])-[#7]-[#6](=[!#6&!#1])-[!#6&!#1]-2",
      "ene_cyano_B(7)", "[#6]-1(=[#6]-[#6](-c:2:c:c(:c(:n:c-1:2)-[#7](-[#1])-[#1])-[#6]#[#7])=[#6])-[#6]#[#7]",
      "dhp_amino_CN_C(7)", "[#7](-[#1])(-[#1])-[#6]-1=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#6](=[#6](-[#6]:[#6])-[#8]-1)-[#6]#[#7]",
      "quinone_B(5)", "c:1:c:c-2:c(:c:c:1)-[#6](-c3cccc4noc-2c34)=[#8]",
      "het_5_A(7)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#7]=[#6](-[#6]=[#8])-[#6;X4]-[#6]-2=[#8]",
      "ene_five_het_H(6)", "[#7]-1=[#6]-[#6](-[#6](-[#7]-1)=[#16])=[#6]",
      "thio_amide_A(6)", "c1(coc(c1-[#1])-[#6](=[#16])-[#7]-2-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[!#1]-[#6](-[#1])(-[#1])-[#6]-2(-[#1])-[#1])-[#1]",
      "ene_cyano_C(6)", "[#6]=[#6](-[#6]#[#7])-[#6](=[#7]-[#1])-[#7]-[#7]",
      "hzone_furan_A(6)", "c:1(:c(:c(:c(:o:1)-[$([#1]),$([#6](-[#1])-[#1])])-[#1])-[#1])-[#6](-[$([#1]),$([#6](-[#1])-[#1])])=[#7]-[#7](-[#1])-c:2:n:c:c:s:2",
      "anil_di_alk_H(6)", "c:1(:c(:c(:c(:c(:c:1-[#7](-[#1])-[#16](=[#8])(=[#8])-[#6]:2:[#6]:[!#1]:[#6]:[#6]:[#6]:2)-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#1]",
      "het_65_C(6)", "n2c1ccccn1c(c2-[$([#6](-[!#1])=[#6](-[#1])-[#6]:[#6]),$([#6]:[#8]:[#6])])-[#7]-[#6]:[#6]",
      "thio_urea_F(6)", "[#6]-1-[#7](-[#1])-[#7](-[#1])-[#6](=[#16])-[#7]-[#7]-1-[#1]",
      "ene_five_het_I(6)", "c:1(:c:c:c:o:1)-[#6](-[#1])=!@[#6]-3-[#6](=[#8])-c:2:c:c:c:c:c:2-[!#6&!#1]-3",
      "het_6_pyridone_OH(5)", "[#8](-[#1])-c:1:n:c(:c:c:c:1)-[#8]-[#1]",
      "hzone_naphth_A(5)", "c:1:2:c(:c(:c(:c(:c:1:c(:c(:c(:c:2-[#1])-[#1])-[#6]=[#7]-[#7](-[#1])-[$([#6]:[#6]),$([#6]=[#16])])-[#1])-[#1])-[#1])-[#1])-[#1]",
      "thio_ester_A(5)", "[#6]-1=[#6](-[#16]-[#6](-[#6]=[#6]-1)=[#16])-[#7]",
      "ene_misc_A(5)", "[#6]-1=[#6]-[#6](-[#8]-[#6]-1-[#8])(-[#8])-[#6]",
      "cyano_pyridone_D(5)", "[#8]=[#6]-1-[#6](=[#6]-[#6](=[#7]-[#7]-1)-[#6]=[#8])-[#6]#[#7]",
      "het_65_Db(5)", "c3cn1c(nc(c1-[#7]-[#6])-c:2:c:c:c:c:n:2)cc3",
      "het_666_A(5)", "[#7]-2-c:1:c:c:c:c:c:1-[#6](=[#7])-c:3:c-2:c:c:c:c:3",
      "diazox_sulfon_B(5)", "c:1:c(:c:c:c:c:1)-[#7]-2-[#6](-[#1])-[#6](-[#1])-[#7](-[#6](-[#1])-[#6]-2-[#1])-[#16](=[#8])(=[#8])-c:3:c:c:c:c:4:n:s:n:c:3:4",
      "dhp_amino_CN_D(5)", "[#7](-[#1])(-[#1])-[#6]-2=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-c:1:c(:c:c:s:1)-[#8]-2",
      "anil_NH_alk_A(5)", "c:1(:c(:c-2:c(:c(:c:1-[#1])-[#1])-[#7](-[#6](-[#7]-2-[#1])=[#8])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])-[#1]",
      "sulfonamide_C(5)", "c:1(:c(:c-3:c(:c(:c:1-[#7](-[#1])-[#16](=[#8])(=[#8])-c:2:c:c:c(:c:c:2)-[!#6&!#1])-[#1])-[#8]-[#6](-[#8]-3)(-[#1])-[#1])-[#1])-[#1]",
      "het_thio_N_55(5)", "[#6](-[#1])-[#6]:2:[#7]:[#7](-c:1:c:c:c:c:c:1):[#16]:3:[!#6&!#1]:[!#1]:[#6]:[#6]:2:3",
      "keto_keto_beta_D(5)", "[#8]=[#6]-[#6]=[#6](-[#1])-[#8]-[#1]",
      "ene_rhod_H(5)", "[#7]-1-2-[#6](=[#7]-[#6](=[#8])-[#6](=[#7]-1)-[#6](-[#1])-[#1])-[#16]-[#6](=[#6](-[#1])-[#6]:[#6])-[#6]-2=[#8]",
      "imine_ene_A(5)", "[#6]:[#6]-[#6](-[#1])=[#6](-[#1])-[#6](-[#1])=[#7]-[#7](-[#6;X4])-[#6;X4]",
      "het_thio_656a(5)", "c:1:3:c(:c:c:c:c:1):c:2:n:n:c(-[#16]-[#6](-[#1])(-[#1])-[#6]=[#8]):n:c:2:n:3-[#6](-[#1])(-[#1])-[#6](-[#1])=[#6](-[#1])-[#1]",
      "tert_butyl_A(2)", "[#6](-[#1])(-[#1])(-[#1])-[#6](-[#6](-[#1])(-[#1])-[#1])(-[#6](-[#1])(-[#1])-[#1])-c:1:c(:c:c(:c(:c:1-[#1])-[#6](-[#6](-[#1])(-[#1])-[#1])(-[#6](-[#1])(-[#1])-[#1])-[#6](-[#1])(-[#1])-[#1])-[#8]-[#6](-[#1])-[#7])-[#1]",
      "pyrrole_D(5)", "n1(-[#6])c(c(-[#1])c(c1-[#6](-[#1])(-[#1])-[#7](-[#1])-[#6](=[#16])-[#7]-[#1])-[#1])-[#1]",
      "pyrrole_E(5)", "n2(-[#6]:1:[!#1]:[!#6&!#1]:[!#1]:[#6]:1-[#1])c(c(-[#1])c(c2-[#6;X4])-[#1])-[#6;X4]",
      "thio_urea_G(5)", "c:1(:c:c:c:c:c:1)-[#7](-[#1])-[#6](=[#16])-[#7]-[#7](-[#1])-[#6]([#7;R])[#7;R]",
      "anisol_A(5)", "c:1(:c(:c(:c(:c(:c:1-[$([#1]),$([#6](-[#1])-[#1])])-[#1])-[#8]-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[$([#7](-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1]),$([#6](-[#1])(-[#6](-[#1])-[#1])-[#7](-[#1])-[#6](=[#16])-[#7]-[#1])])-[#1])-[#8]-[#6](-[#1])-[#1]",
      "pyrrole_F(5)", "n2(-[#6]:1:[#6](-[#6]#[#7]):[#6]:[#6]:[!#6&!#1]:1)c(c(-[#1])c(c2)-[#1])-[#1]",
      "coumarin_D(1)", "c:1:c:c(:c:c-2:c:1-[#6](=[#6](-[#1])-[#6](=[#8])-[#8]-2)-c:3:c:c:c:c:c:3)-[#8]-[#6](-[#1])(-[#1])-[#6]:[#8]:[#6]",
      "thiazole_amine_A(4)", "[#7](-[#1])-c:1:n:c(:c:s:1)-c:2:c:n:c(-[#7](-[#1])-[#1]):s:2",
      "het_6_imidate_A(4)", "[#7]=[#6]-1-[#7](-[#1])-[#6](=[#6](-[#7]-[#1])-[#7]=[#7]-1)-[#7]-[#1]",
      "anil_OC_no_alk_B(4)", "c:1:c(:c:2:c(:c:c:1):c:c:c:c:2)-[#8]-c:3:c(:c(:c(:c(:c:3-[#1])-[#1])-[#7]-[#1])-[#1])-[#1]",
      "styrene_C(4)", "c:1:c:c-2:c(:c:c:1)-[#6]-[#16]-c3c(-[#6]-2=[#6])ccs3",
      "azulene(4)", "c:2:c:c:c:1:c(:c:c:c:1):c:c:2",
      "furan_acid_A(4)", "c:1(:c(:c(:c(:o:1)-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#8]-[#6]:[#6])-[#1])-[#6](=[#8])-[#8]-[#1]",
      "cyano_pyridone_E(4)", "[!#1]:[#6]-[#6]-1=[#6](-[#1])-[#6](=[#6](-[#6]#[#7])-[#6](=[#8])-[#7]-1-[#1])-[#6]:[#8]",
      "anil_alk_thio(4)", "[#6]-1-3=[#6](-[#6](-[#7]-c:2:c:c:c:c:c-1:2)(-[#6])-[#6])-[#16]-[#16]-[#6]-3=[!#1]",
      "anil_di_alk_I(4)", "c:1(:c(:c(:c(:c(:c:1-[#7](-[#1])-[#6](=[#8])-c:2:c:c:c:c:c:2)-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#1]",
      "het_thio_6_furan(4)", "[#6](-[#1])(-[#1])-[#16;X2]-c:1:n:n:c(:c(:n:1)-c:2:c(:c(:c(:o:2)-[#1])-[#1])-[#1])-c:3:c(:c(:c(:o:3)-[#1])-[#1])-[#1]",
      "het_thio_N_65A(3)", "[#7]-2-[#16]-[#6]-1=[#6](-[#6]:[#6]-[#7]-[#6]-1)-[#6]-2=[#16]",
      "anil_di_alk_ene_B(4)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6]-2=[#6]-c:1:c(:c:c:c:c:1)-[#6]-2(-[#1])-[#1]",
      "thio_ester_B(4)", "[#6]:[#6]-[#6](=[#16;X1])-[#16;X2]-[#6](-[#1])-[$([#6](-[#1])-[#1]),$([#6]:[#6])]",
      "anil_di_alk_M(1)", "c:1(:c:4:c(:n:c(:c:1-[#6](-[#1])(-[#1])-[#7]-3-c:2:c(:c(:c(:c(:c:2-[#6](-[#1])(-[#1])-[#6]-3(-[#1])-[#1])-[#1])-[#1])-[#1])-[#1])-[#1]):c(:c(:c(:c:4-[#1])-[#1])-[#1])-[#1])-[#1]",
      "imine_one_B(4)", "[#7](-[#1])(-c:1:c:c:c:c:c:1)-[#7]=[#6](-[#6](=[#8])-[#6](-[#1])-[#1])-[#7](-[#1])-[$([#7]-[#1]),$([#6]:[#6])]",
      "anil_OC_alk_A(4)", "c:1:2:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1]):o:c:3:c(-[#1]):c(:c(-[#8]-[#6](-[#1])-[#1]):c(:c:2:3)-[#1])-[#7](-[#1])-[#6](-[#1])-[#1]",
      "ene_five_het_J(4)", "[#16]=[#6]-1-[#7](-[#1])-[#6]=[#6]-[#6]-2=[#6]-1-[#6](=[#8])-[#8]-[#6]-2=[#6]-[#1]",
      "pyrrole_G(4)", "n2(-c:1:c(:c:c(:c(:c:1)-[#1])-[$([#7](-[#1])-[#1]),$([#6]:[#7])])-[#1])c(c(-[#1])c(c2-[#1])-[#1])-[#1]",
      "ene_five_het_K(4)", "n1(-[#6])c(c(-[#1])c(c1-[#6](-[#1])=[#6]-2-[#6](=[#8])-[!#6&!#1]-[#6]=,:[!#1]-2)-[#1])-[#1]",
      "cyano_ene_amine_B(4)", "[#6]=[#6]-[#6](-[#6]#[#7])(-[#6]#[#7])-[#6](-[#6]#[#7])=[#6]-[#7](-[#1])-[#1]",
      "colchicine_A(3)", "[#6]-1(-[#6](=[#6]-[#6]=[#6]-[#6]=[#6]-1)-[#7]-[#1])=[#7]-[#6]",
      "ene_five_het_L(4)", "[#8]=[#6]-3-[#6](=!@[#6](-[#1])-c:1:c:n:c:c:1)-c:2:c:c:c:c:c:2-[#7]-3",
      "hzone_thiophene_B(4)", "c:1(:c(:c(:c(:s:1)-[#1])-[#1])-[$([#1]),$([#6](-[#1])-[#1])])-[#6](-[#1])=[#7]-[#7](-[#1])-c:2:c:c:c:c:c:2",
      "dhp_amino_CN_E(4)", "[#6](-[#1])(-[#1])-[#16;X2]-[#6]-1=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#6](-[#6]#[#7])-[#6](=[#8])-[#7]-1",
      "het_5_B(4)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#7]=[#6](-[#7](-[#1])-[#6]=[#8])-[#6](-[#1])(-[#1])-[#6]-2=[#8]",
      "imine_imine_B(3)", "[#6]:[#6]-[#6](-[#1])=[#6](-[#1])-[#6](-[#1])=[#7]-[#7]=[#6]",
      "thiazole_amine_B(3)", "c:1(:c:c:c(:c:c:1)-[#6](-[#1])-[#1])-c:2:c(:s:c(:n:2)-[#7](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#1]",
      "pyrrole_H(3)", "n1-2cccc1-[#6]=[#7](-[#6])-[#6]-[#6]-2",
      "imine_ene_one_A(3)", "[#6]-2(-[#6]=[#7]-c:1:c:c:c:c:c:1-[#7]-2)=[#6](-[#1])-[#6]=[#8]",
      "diazox_A(3)", "[#8](-c:1:c:c:c:c:c:1)-c:3:c:c:2:n:o:n:c:2:c:c:3",
      "ene_one_A(3)", "[!#1]:1:[!#1]:[!#1]:[!#1](:[!#1]:[!#1]:1)-[#6](-[#1])=[#6](-[#1])-[#6](-[#7]-c:2:c:c:c:3:c(:c:2):c:c:c(:n:3)-[#7](-[#6])-[#6])=[#8]",
      "anil_OC_no_alk_C(3)", "[#7](-[#1])(-[#1])-c:1:c(:c:c:c:n:1)-[#8]-[#6](-[#1])(-[#1])-[#6]:[#6]",
      "thiazol_SC_A(3)", "[#6]-[#16;X2]-c:1:n:c(:c:s:1)-[#1]",
      "het_666_B(3)", "c:1:c-3:c(:c:c:c:1)-[#7](-c:2:c:c:c:c:c:2-[#8]-3)-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1]",
      "furan_A(3)", "c:1(:c(:c(:c(:o:1)-[#6](-[#1])-[#1])-[#1])-[#1])-[#6](-[#1])(-[#8]-[#1])-[#6]#[#6]-[#6;X4]",
      "thiophene_C(3)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])=[#6]-[#6](=[#8])-c:1:c(-[#16;X2]):s:c(:c:1)-[$([#6]#[#7]),$([#6]=[#8])]",
      "anil_OC_alk_B(3)", "c:1:3:c(:c:c:c:c:1)-[#7]-2-[#6](=[#8])-[#6](=[#6](-[F,Cl,Br,I])-[#6]-2=[#8])-[#7](-[#1])-[#6]:[#6]:[#6]:[#6](-[#8]-[#6](-[#1])-[#1]):[#6]:[#6]:3",
      "het_thio_66_A(3)", "c:1-2:c(:c:c:c:c:1)-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7]=[#6]-2-[#16;X2]-[#6](-[#1])(-[#1])-[#6](=[#8])-c:3:c:c:c:c:c:3",
      "rhod_sat_B(3)", "[#7]-2(-c:1:c:c:c:c:c:1-[#6](-[#1])-[#1])-[#6](=[#16])-[#7](-[#6](-[#1])(-[#1])-[!#1]:[!#1]:[!#1]:[!#1]:[!#1])-[#6](-[#1])(-[#1])-[#6]-2=[#8]",
      "ene_rhod_I(3)", "[#7]-2(-[#6](-[#1])-[#1])-[#6](=[#16])-[#7](-[#1])-[#6](=[#6](-[#1])-c:1:c:c:c:c(:c:1)-[Br])-[#6]-2=[#8]",
      "keto_thiophene(3)", "c:1(:c(:c:2:c(:s:1):c:c:c:c:2)-[#6](-[#1])-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1]",
      "ene_cyano_D(3)", "[#6](-[#6]#[#7])(-[#6]#[#7])=[#6](-[#16])-[#16]",
      "imine_imine_C(3)", "[#7](-[#6](-[#1])-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])=[#7]-[#6](-[#6](-[#1])-[#1])=[#7]-[#7](-[#6](-[#1])-[#1])-[#6]:[#6]",
      "het_65_pyridone_A(3)", "[#6]:2(:[#6](-[#6](-[#1])-[#1]):[#6]-1:[#6](-[#7]=[#6](-[#7](-[#6]-1=[!#6&!#1;X1])-[#6](-[#1])-[$([#6](=[#8])-[#8]),$([#6]:[#6])])-[$([#1]),$([#16]-[#6](-[#1])-[#1])]):[!#6&!#1;X2]:2)-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1]",
      "thiazole_amine_C(3)", "c:1(:n:c(:c(-[#1]):s:1)-[!#1]:[!#1]:[!#1](-[$([#8]-[#6](-[#1])-[#1]),$([#6](-[#1])-[#1])]):[!#1]:[!#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:c(-[#1]):c(:c(-[#1]):o:2)-[#1]",
      "het_thio_pyr_A(3)", "n:1:c(:c(:c(:c(:c:1-[#16]-[#6]-[#1])-[#6]#[#7])-c:2:c:c:c(:c:c:2)-[#8]-[#6](-[#1])-[#1])-[#1])-[#6]:[#6]",
      "melamine_A(3)", "c:1:4:c(:n:c(:n:c:1-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:c(:c(:c(:o:2)-[#1])-[#1])-[#1])-[#7](-[#1])-c:3:c:c(:c(:c:c:3-[$([#1]),$([#6](-[#1])-[#1]),$([#16;X2]),$([#8]-[#6]-[#1]),$([#7;X3])])-[$([#1]),$([#6](-[#1])-[#1]),$([#16;X2]),$([#8]-[#6]-[#1]),$([#7;X3])])-[$([#1]),$([#6](-[#1])-[#1]),$([#16;X2]),$([#8]-[#6]-[#1]),$([#7;X3])]):c:c:c:c:4",
      "anil_NH_alk_B(3)", "[#7](-[#1])(-[#6]:1:[#6]:[#6]:[!#1]:[#6]:[#6]:1)-c:2:c:c:c(:c:c:2)-[#7](-[#1])-[#6]-[#1]",
      "rhod_sat_C(3)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#6](=[#7]-[#6]=[#8])-[#16]-[#6](-[#1])(-[#1])-[#6]-2=[#8]",
      "thiophene_amino_D(3)", "[#6]=[#6]-[#6](=[#8])-[#7]-c:1:c(:c(:c(:s:1)-[#6](=[#8])-[#8])-[#6]-[#1])-[#6]#[#7]",
      "anil_OC_alk_C(3)", "[$([#1]),$([#6](-[#1])-[#1])]-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-c:2:n:c:c:n:2",
      "het_thio_65_A(3)", "[#6](-[#1])(-[#1])-[#16;X2]-c3nc1c(n(nc1-[#6](-[#1])-[#1])-c:2:c:c:c:c:c:2)nn3",
      "het_thio_656b(3)", "[#6]-[#6](=[#8])-[#6](-[#1])(-[#1])-[#16;X2]-c:3:n:n:c:2:c:1:c(:c(:c(:c(:c:1:n(:c:2:n:3)-[#1])-[#1])-[#1])-[#1])-[#1]",
      "thiazole_amine_D(3)", "s:1:c(:[n+](-[#6](-[#1])-[#1]):c(:c:1-[#1])-[#6])-[#7](-[#1])-c:2:c:c:c:c:c:2[$([#6](-[#1])-[#1]),$([#6]:[#6])]",
      "thio_urea_H(3)", "[#6]-2(=[#16])-[#7](-[#6](-[#1])(-[#1])-c:1:c:c:c:o:1)-[#6](=[#7]-[#7]-2-[#1])-[#6]:[#6]",
      "cyano_pyridone_F(3)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#6](=[#8])-[#6](=[#6]-[#6](=[#7]-2)-[#6]#[#7])-[#6]#[#7]",
      "cyano_cyano_B(3)", "[#6]-1(-[#6]#[#7])(-[#6]#[#7])-[#6](-[#1])(-[#6](=[#8])-[#6])-[#6]-1-[#1]",
      "rhod_sat_D(3)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#6](=[#8])-[#16]-[#6](-[#1])(-[#6](-[#1])(-[#1])-[#6](=[#8])-[#7](-[#1])-[#6]:[#6])-[#6]-2=[#8]",
      "ene_rhod_J(3)", "[#6](-[#1])(-[#1])-[#7]-2-[#6](=[$([#16]),$([#7])])-[!#6&!#1]-[#6](=[#6]-1-[#6](=[#6](-[#1])-[#6]:[#6]-[#7]-1-[#6](-[#1])-[#1])-[#1])-[#6]-2=[#8]",
      "imine_phenol_A(3)", "[#6]=[#7;!R]-c:1:c:c:c:c:c:1-[#8]-[#1]",
      "thio_carbonate_B(3)", "[#8]=[#6]-2-[#16]-c:1:c(:c(:c:c:c:1)-[#8]-[#6](-[#1])-[#1])-[#8]-2",
      "het_thio_N_5A(3)", "[#7]=[#6]-1-[#7]=[#6]-[#7]-[#16]-1",
      "anil_di_alk_J(3)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])=[#7]-[#7]=[#6](-[#6])-[#6]:[#6])-[#1])-[#1]",
      "thio_aldehyd_A(3)", "[#6]-[#6](=[#16])-[#1]",
      "ene_five_het_M(3)", "[#6]-1=,:[#6]-[#6](-[#6](-[$([#8]),$([#16])]-1)=[#6]-[#6]=[#8])=[#8]",
      "cyano_ene_amine_C(3)", "[#6]:[#6]-[#6](=[#8])-[#7](-[#1])-[#6](=[#8])-[#6](-[#6]#[#7])=[#6](-[#1])-[#7](-[#1])-[#6]:[#6]",
      "thio_urea_I(3)", "c:1(:c:c:c:c:c:1)-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#7]=[#6]-c:2:c:n:c:c:2",
      "dhp_amino_CN_F(3)", "[#7](-[#1])(-[#1])-[#6]-2=[#6](-[#6]#[#7])-[#6](-[#1])(-c:1:c:c:c:s:1)-[#6](=[#6](-[#6](-[#1])-[#1])-[#8]-2)-[#6](=[#8])-[#8]-[#6]",
      "anthranil_acid_B(3)", "c:1:c-3:c(:c:c(:c:1)-[#6](=[#8])-[#7](-[#1])-c:2:c(:c:c:c:c:2)-[#6](=[#8])-[#8]-[#1])-[#6](-[#7](-[#6]-3=[#8])-[#6](-[#1])-[#1])=[#8]",
      "diazox_B(3)", "[Cl]-c:2:c:c:1:n:o:n:c:1:c:c:2",
      "thio_amide_B(2)", "[#6;X4]-[#7](-[#1])-[#6](-[#6]:[#6])=[#6](-[#1])-[#6](=[#16])-[#7](-[#1])-c:1:c:c:c:c:c:1",
      "imidazole_B(2)", "[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#16]-[#6](-[#1])(-[#1])-c1cn(cn1)-[#1]",
      "thiazole_amine_E(2)", "[#8]=[#6]-[#7](-[#1])-c:1:c(-[#6]:[#6]):n:c(-[#6](-[#1])(-[#1])-[#6]#[#7]):s:1",
      "thiazole_amine_F(2)", "[#6](-[#1])-[#7](-[#1])-c:1:n:c(:c:s:1)-c2cnc3n2ccs3",
      "thio_ester_C(2)", "[#7]-1-[#6](=[#8])-[#6](=[#6](-[#6])-[#16]-[#6]-1=[#16])-[#1]",
      "ene_one_B(2)", "[#6](-[#16])(-[#7])=[#6](-[#1])-[#6]=[#6](-[#1])-[#6]=[#8]",
      "quinone_C(2)", "[#8]=[#6]-3-c:1:c(:c:c:c:c:1)-[#6]-2=[#6](-[#8]-[#1])-[#6](=[#8])-[#7]-c:4:c-2:c-3:c:c:c:4",
      "hzide_naphth(2)", "c:2(:c:1:c(:c(:c(:c(:c:1:c(:c(:c:2-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#7](-[#1])-[#6]=[#8])-[#1])-[#1])-[#1]",
      "keto_naphthol_A(2)", "c:1:2:c:c:c:c(:c:1:c(:c:c:c:2)-[$([#8]-[#1]),$([#7](-[#1])-[#1])])-[#6](-[#6])=[#8]",
      "thio_amide_C(2)", "[#6](-[#1])(-c:1:c:c:c:c:c:1)(-c:2:c:c:c:c:c:2)-[#6](=[#16])-[#7]-[#1]",
      "phthalimide_misc(2)", "[#7]-2(-[#6](=[#8])-c:1:c(:c(:c(:c(:c:1-[#1])-[#6](=[#8])-[#8]-[#1])-[#1])-[#1])-[#6]-2=[#8])-c:3:c(:c:c(:c(:c:3)-[#1])-[#8])-[#1]",
      "sulfonamide_D(2)", "c:1:c:c(:c:c:c:1-[#7](-[#1])-[#16](=[#8])=[#8])-[#7](-[#1])-[#16](=[#8])=[#8]",
      "anil_NH_alk_C(2)", "[#6](-[#1])-[#7](-[#1])-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#6]-[#1]",
      "het_65_E(2)", "s1c(c(c-2c1-[#7](-[#1])-[#6](-[#6](=[#6]-2-[#1])-[#6](=[#8])-[#8]-[#1])=[#8])-[#7](-[#1])-[#1])-[#6](=[#8])-[#7]-[#1]",
      "anisol_B(2)", "[#6](-[#1])(-[#1])-c:1:c(:c(:c(:c(:c:1-[#8]-[#6](-[#1])-[#1])-[#1])-[#1])-[#6](-[#1])(-[#1])-[#7](-[#1])-[#6;X4])-[#1]",
      "thio_carbam_ene(2)", "[#6]-1=[#6]-[#7]-[#6](-[#16]-[#6;X4]-1)=[#16]",
      "thio_amide_D(2)", "[#6](-[#7](-[#6]-[#1])-[#6]-[#1]):[#6]-[#7](-[#1])-[#6](=[#16])-[#6]-[#1]",
      "het_65_Da(2)", "n2nc(c1cccc1c2-[#6])-[#6]",
      "thiophene_D(2)", "s:1:c(:c(-[#1]):c(:c:1-[#6](=[#8])-[#7](-[#1])-[#7]-[#1])-[#8]-[#6](-[#1])-[#1])-[#1]",
      "het_thio_6_ene(2)", "[#6]-1:[#6]-[#7]=[#6]-[#6](=[#6]-[#7]-[#6])-[#16]-1",
      "het_565_indole(1)", "c2(c(-[#1])n(-[#6](-[#1])-[#1])c:3:c(:c(:c:1n(c(c(c:1:c2:3)-[#1])-[#1])-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1]",
      "cyano_keto_A(2)", "[#6](-[#1])(-[#1])-[#6](-[#1])(-[#6]#[#7])-[#6](=[#8])-[#6]",
      "anthranil_acid_C(2)", "c2(c(-[#7](-[#1])-[#1])n(-c:1:c:c:c:c:c:1-[#6](=[#8])-[#8]-[#1])nc2-[#6]=[#8])-[$([#6]#[#7]),$([#6]=[#16])]",
      "naphth_amino_C(2)", "c:2:c:1:c:c:c:c-3:c:1:c(:c:c:2)-[#7](-[#7]=[#6]-3)-[#1]",
      "naphth_amino_D(2)", "c:2:c:1:c:c:c:c-3:c:1:c(:c:c:2)-[#7]-[#7]=[#7]-3",
      "thiazole_amine_G(2)", "c1csc(n1)-[#7]-[#7]-[#16](=[#8])=[#8]",
      "het_66_B(2)", "c:1:c:c:c:2:c(:c:1):n:c(:n:c:2)-[#7](-[#1])-[#6]-3=[#7]-[#6](-[#6]=[#6]-[#7]-3-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
      "coumarin_A(2)", "c:1-3:c(:c(:c(:c(:c:1)-[#8]-[#6]-[#1])-[#1])-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#6](=[#8])-[#8]-3",
      "het_thio_5_B(2)", "[#6]-1(-[#6]=[#8])(-[#6]:[#6])-[#16;X2]-[#6]=[#7]-[#7]-1-[#1]",
      "thiophene_amino_F(2)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:s:1)-[#7](-[#1])-[#6](=[#8])-c:2:c:c:c:c:c:2)-[#6]#[#7])-[#6]:3:[!#1]:[!#1]:[!#1]:[!#1]:[!#1]:3",
      "anthranil_acid_D(2)", "c:12:c(:c:c:c:n:1)c(c(-[#6](=[#8])~[#8;X1])s2)-[#7](-[#1])-[#1]",
      "het_66_C(2)", "c:1:2:n:c(:c(:n:c:1:[#6]:[#6]:[#6]:[!#1]:2)-[#6](-[#1])=[#6](-[#8]-[#1])-[#6])-[#6](-[#1])=[#6](-[#8]-[#1])-[#6]",
      "thiophene_amino_E(2)", "c1csc(c1-[#7](-[#1])-[#1])-[#6](-[#1])=[#6](-[#1])-c2cccs2",
      "het_6666_A(2)", "c:2:c:c:1:n:c:3:c(:n:c:1:c:c:2):c:c:c:4:c:3:c:c:c:c:4",
      "sulfonamide_E(2)", "[#6]:[#6]-[#7](-[#1])-[#16](=[#8])(=[#8])-[#7](-[#1])-[#6]:[#6]",
      "anil_di_alk_K(2)", "c:1:c:c(:c:c:c:1-[#7](-[#1])-[#1])-[#7](-[#6;X3])-[#6;X3]",
      "het_5_C(2)", "[#7]-2=[#6](-c:1:c:c:c:c:c:1)-[#6](-[#1])(-[#1])-[#6](-[#8]-[#1])(-[#6](-[#9])(-[#9])-[#9])-[#7]-2-[$([#6]:[#6]:[#6]:[#6]:[#6]:[#6]),$([#6](=[#16])-[#6]:[#6]:[#6]:[#6]:[#6]:[#6])]",
      "ene_six_het_B(2)", "c:1:c(:c:c:c:c:1)-[#6](=[#8])-[#6](-[#1])=[#6]-3-[#6](=[#8])-[#7](-[#1])-[#6](=[#8])-[#6](=[#6](-[#1])-c:2:c:c:c:c:c:2)-[#7]-3-[#1]",
      "steroid_A(2)", "[#8]=[#6]-4-[#6]-[#6]-[#6]-3-[#6]-2-[#6](=[#8])-[#6]-[#6]-1-[#6]-[#6]-[#6]-[#6]-1-[#6]-2-[#6]-[#6]-[#6]-3=[#6]-4",
      "het_565_A(2)", "c:1:2:c:3:c(:c(-[#8]-[#1]):c(:c:1:c(:c:n:2-[#6])-[#6]=[#8])-[#1]):n:c:n:3",
      "thio_imine_ium(2)", "[#6;X4]-[#7+](-[#6;X4]-[#8]-[#1])=[#6]-[#16]-[#6]-[#1]",
      "anthranil_acid_E(2)", "[#6]-3(=[#8])-[#6](=[#6](-[#1])-[#7](-[#1])-c:1:c:c:c:c:c:1-[#6](=[#8])-[#8]-[#1])-[#7]=[#6](-c:2:c:c:c:c:c:2)-[#8]-3",
      "hzone_furan_B(2)", "c:1(:c(:c(:c(:o:1)-[$([#1]),$([#6](-[#1])-[#1])])-[#1])-[#1])-[#6](-[$([#1]),$([#6](-[#1])-[#1])])=[#7]-[#7](-[#1])-c:2:c:c:n:c:c:2",
      "thiophene_E(2)", "c:1(:c(:c(:c(:s:1)-[$([#1]),$([#6](-[#1])-[#1])])-[#1])-[#1])-[#6](-[$([#1]),$([#6](-[#1])-[#1])])-[#6](=[#8])-[#7](-[#1])-c:2:n:c:c:s:2",
      "ene_misc_B(2)", "[#6]:[#6]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#6]=[#8])-[#7]-2-[#6](=[#8])-[#6]-1(-[#1])-[#6](-[#1])(-[#1])-[#6]=[#6]-[#6](-[#1])(-[#1])-[#6]-1(-[#1])-[#6]-2=[#8]",
      "thio_urea_J(2)", "c:1(:c(:o:c:c:1)-[#6]-[#1])-[#6]=[#7]-[#7](-[#1])-[#6](=[#16])-[#7]-[#1]",
      "het_thio_65_B(2)", "[#7](-[#1])-c1nc(nc2nnc(n12)-[#16]-[#6])-[#7](-[#1])-[#6]",
      "coumarin_B(2)", "c:1-2:c(:c:c:c:c:1-[#6](-[#1])(-[#1])-[#6](-[#1])=[#6](-[#1])-[#1])-[#6](=[#6](-[#6](=[#8])-[#7](-[#1])-[#6]:[#6])-[#6](=[#8])-[#8]-2)-[#1]",
      "thio_urea_K(2)", "[#6]-2(=[#16])-[#7]-1-[#6]:[#6]-[#7]=[#7]-[#6]-1=[#7]-[#7]-2-[#1]",
      "thiophene_amino_G(2)", "[#6]:[#6]:[#6]:[#6]:[#6]:[#6]-c:1:c:c(:c(:s:1)-[#7](-[#1])-[#6](=[#8])-[#6])-[#6](=[#8])-[#8]-[#1]",
      "pyrrole_I(2)", "n2(-[#6](-[#1])-[#1])c-1c(-[#6]:[#6]-[#6]-1=[#8])cc2-[#6](-[#1])-[#1]",
      "anil_NH_alk_D(2)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:c:c:1-[#7](-[#1])-[#6](-[#1])(-[#6])-[#6](-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#1]",
      "het_thio_5_C(2)", "[#16]=[#6]-2-[#7](-[#1])-[#7]=[#6](-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1])-[#8]-2",
      "thio_keto_het(2)", "[#16]=[#6]-c:1:c:c:c:2:c:c:c:c:n:1:2",
      "het_thio_N_5B(2)", "[#6]~1~[#6](~[#7]~[#7]~[#6](~[#6](-[#1])-[#1])~[#6](-[#1])-[#1])~[#7]~[#16]~[#6]~1",
      "quinone_D(2)", "[#6]-1(-[#6]=,:[#6]-[#6]=,:[#6]-[#6]-1=[!#6&!#1])=[!#6&!#1]",
      "misc_trityl_A(1)", "[#6](-[#6]:[#6])(-[#6]:[#6])(-[#6]:[#6])-[#16]-[#6]:[#6]-[#6](=[#8])-[#8]-[#1]",
      "anil_di_alk_furan_B(2)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:1:c(-[#1]):c(:c(:o:1)-[#6](-[#1])=[#6]-[#6]#[#7])-[#1]",
      "ene_six_het_C(2)", "[#8]=[#6]-1-[#6]:[#6]-[#6](-[#1])(-[#1])-[#7]-[#6]-1=[#6]-[#1]",
      "het_55_A(2)", "[#6]:[#6]-[#7]:2:[#7]:[#6]:1-[#6](-[#1])(-[#1])-[#16;X2]-[#6](-[#1])(-[#1])-[#6]:1-[#6]:2-[#7](-[#1])-[#6](=[#8])-[#6](-[#1])=[#6]-[#1]",
      "het_thio_65_C(2)", "n:1:c(:n(:c:2:c:1:c:c:c:c:2)-[#6](-[#1])-[#1])-[#16]-[#6](-[#1])(-[#1])-[#6](=[#8])-[#7](-[#1])-[#7]=[#6](-[#1])-[#6](-[#1])=[#6]-[#1]",
      "hydroquin_A(2)", "c:1(:c:c(:c(:c:c:1)-[#8]-[#1])-[#6](=!@[#6]-[#7])-[#6]=[#8])-[#8]-[#1]",
      "anthranil_acid_F(2)", "c:1(:c:c(:c(:c:c:1)-[#7](-[#1])-[#6](=[#8])-[#6]:[#6])-[#6](=[#8])-[#8]-[#1])-[#8]-[#1]",
      "thiophene_amino_H(2)", "[#6](-[#1])-[#7](-[#1])-c:1:c(:c(:c(:s:1)-[#6]-[#1])-[#6]-[#1])-[#6](=[#8])-[#7](-[#1])-[#6]:[#6]",
      "imine_one_fives_C(2)", "[#6]:[#6]-[#7;!R]=[#6]-2-[#6](=[!#6&!#1])-c:1:c:c:c:c:c:1-[#7]-2",
      "keto_phenone_zone_A(2)", "c:1:c:c:c:c:c:1-[#6](=[#8])-[#7](-[#1])-[#7]=[#6]-3-c:2:c:c:c:c:c:2-c:4:c:c:c:c:c-3:4",
      "dyes7A(2)", "c:1:c(:c:c:c:c:1)-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])=[#6](-[#1])-[#6]=!@[#6](-[#1])-[#6](-[#1])=[#6]-[#6]=@[#7]-c:2:c:c:c:c:c:2",
      "het_pyridiniums_B(2)", "[#6]:1:2:[!#1]:[#7+](:[!#1]:[#6](:[!#1]:1:[#6]:[#6]:[#6]:[#6]:2)-[*])~[#6]:[#6]",
      "het_5_D(2)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#7]=[#6](-[#6](-[#1])-[#1])-[#6](-[#1])(-[#16]-[#6])-[#6]-2=[#8]",
      "thiazole_amine_H(1)", "c:1:c:c:c(:c:c:1-[#7](-[#1])-c2nc(c(-[#1])s2)-c:3:c:c:c(:c:c:3)-[#6](-[#1])(-[#6]-[#1])-[#6]-[#1])-[#6](=[#8])-[#8]-[#1]",
      "misc_pyridine_OC(1)", "[#8]=[#6](-c:1:c(:c(:n:c(:c:1-[#1])-[#8]-[#6](-[#1])(-[#1])-[#1])-[#8]-[#6](-[#1])(-[#1])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
      "thiazole_amine_I(1)", "[#6](-[#1])(-[#1])-[#7](-[#1])-[#6]=[#7]-[#7](-[#1])-c1nc(c(-[#1])s1)-[#6]:[#6]",
      "het_thio_N_5C(1)", "[#6]:[#6]-[#7](-[#1])-[#6](=[#8])-c1c(snn1)-[#7](-[#1])-[#6]:[#6]",
      "sulfonamide_F(1)", "[#8]=[#16](=[#8])(-[#6]:[#6])-[#7](-[#1])-c1nc(cs1)-[#6]:[#6]",
      "thiazole_amine_J(1)", "[#8]=[#16](=[#8])(-[#6]:[#6])-[#7](-[#1])-[#7](-[#1])-c1nc(cs1)-[#6]:[#6]",
      "het_65_F(1)", "s2c:1:n:c:n:c(:c:1c(c2-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#7]-[#7]=[#6]-c3ccco3",
      "keto_keto_beta_E(1)", "[#6](=[#8])-[#6](-[#1])=[#6](-[#8]-[#1])-[#6](-[#8]-[#1])=[#6](-[#1])-[#6](=[#8])-[#6]",
      "ene_five_one_B(1)", "c:2(:c:1-[#6](-[#6](-[#6](-c:1:c(:c(:c:2-[#1])-[#1])-[#1])(-[#1])-[#1])=[#8])=[#6](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1]",
      "keto_keto_beta_zone(1)", "[#6]:[#6]-[#7](-[#1])-[#7]=[#6](-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#6](-[#1])-[#1])=[#7]-[#7](-[#1])-[#6]:[#6]",
      "thio_urea_L(1)", "[#6;X4]-[#16;X2]-[#6](=[#7]-[!#1]:[!#1]:[!#1]:[!#1])-[#7](-[#1])-[#7]=[#6]",
      "het_thio_urea_ene(1)", "[#6]-1(=[#7]-[#7](-[#6](-[#16]-1)=[#6](-[#1])-[#6]:[#6])-[#6]:[#6])-[#6]=[#8]",
      "cyano_amino_het_A(1)", "c:1(:c(:c:2:c(:n:c:1-[#7](-[#1])-[#1]):c:c:c(:c:2-[#7](-[#1])-[#1])-[#6]#[#7])-[#6]#[#7])-[#6]#[#7]",
      "tetrazole_hzide(1)", "[!#1]:1:[!#1]:[!#1]:[!#1](:[!#1]:[!#1]:1)-[#6](-[#1])=[#6](-[#1])-[#6](-[#7](-[#1])-[#7](-[#1])-c2nnnn2-[#6])=[#8]",
      "imine_naphthol_A(1)", "c:1:2:c(:c(:c(:c(:c:1:c(:c(:c(:c:2-[#1])-[#1])-[#6](=[#7]-[#6]:[#6])-[#6](-[#1])-[#1])-[#8]-[#1])-[#1])-[#1])-[#1])-[#1]",
      "misc_anisole_A(1)", "c:1(:c(:c:2:c(:c(:c:1-[#8]-[#6](-[#1])-[#1])-[#1]):c(:c(:c(:c:2-[#7](-[#1])-[#6](-[#1])(-[#1])-[#1])-[#1])-c:3:c(:c(:c(:c(:c:3-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1])-[#1])-[#8]-[#6](-[#1])-[#1]",
      "het_thio_665(1)", "c:1:c:c-2:c(:c:c:1)-[#16]-c3c(-[#7]-2)cc(s3)-[#6](-[#1])-[#1]",
      "anil_di_alk_L(1)", "c:1:c:c:c-2:c(:c:1)-[#6](-[#6](-[#7]-2-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7]-4-[#6](-c:3:c:c:c:c:c:3-[#6]-4=[#8])=[#8])(-[#1])-[#1])(-[#1])-[#1]",
      "colchicine_B(1)", "c:1(:c:c:c(:c:c:1)-[#6]-3=[#6]-[#6](-c2cocc2-[#6](=[#6]-3)-[#8]-[#1])=[#8])-[#16]-[#6](-[#1])-[#1]",
      "misc_aminoacid_A(1)", "[#6;X4]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#6](-[#1])(-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#16]-[#6](-[#1])(-[#1])-[#1])-[#6](=[#8])-[#8]-[#1])-[#1])-[#1]",
      "imidazole_amino_A(1)", "n:1:c(:n(:c(:c:1-c:2:c:c:c:c:c:2)-c:3:c:c:c:c:c:3)-[#7]=!@[#6])-[#7](-[#1])-[#1]",
      "phenol_sulfite_A(1)", "[#6](-c:1:c:c:c(:c:c:1)-[#8]-[#1])(-c:2:c:c:c(:c:c:2)-[#8]-[#1])-[#8]-[#16](=[#8])=[#8]",
      "het_66_D(1)", "c:2:c:c:1:n:c(:c(:n:c:1:c:c:2)-[#6](-[#1])(-[#1])-[#6](=[#8])-[#6]:[#6])-[#6](-[#1])(-[#1])-[#6](=[#8])-[#6]:[#6]",
      "misc_anisole_B(1)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:2:c:c:c(-[#6](-[#1])-[#1])c:c:2",
      "tetrazole_A(1)", "[#6](-[#1])(-[#1])-c1nnnn1-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#6](-[#1])(-[#1])-[#1])-[#1])-[#1]",
      "het_65_G(1)", "[#6]-2(=[#7]-c1c(c(nn1-[#6](-[#6]-2(-[#1])-[#1])=[#8])-[#7](-[#1])-[#1])-[#7](-[#1])-[#1])-[#6]",
      "het_6_hydropyridone(1)", "[#7]-1=[#6](-[#7](-[#6](-[#6](-[#6]-1(-[#1])-[#6]:[#6])(-[#1])-[#1])=[#8])-[#1])-[#7]-[#1]",
      "misc_stilbene(1)", "[#6]-1(=[#6](-[#6](-[#6](-[#6](-[#6]-1(-[#1])-[#1])(-[#1])-[#6](=[#8])-[#6])(-[#1])-[#6](=[#8])-[#8]-[#1])(-[#1])-[#1])-[#6]:[#6])-[#6]:[#6]",
      "misc_imidazole(1)", "[#6](-[#1])(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[Cl])-[#1])-[#1])(-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[Cl])-[#1])-[#1])-[#8]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-c3nc(c(n3-[#6](-[#1])(-[#1])-[#1])-[#1])-[#1]",
      "anil_NH_no_alk_A(1)", "n:1:c(:c(:c(:c(:c:1-[#1])-[#7](-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#6]:[#6]",
      "het_6_imidate_B(1)", "[#7](-[#1])(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#8]-[#1])-[#6]-2=[#6](-[#8]-[#6](-[#7]=[#7]-2)=[#7])-[#7](-[#1])-[#1]",
      "anil_alk_B(1)", "[#7](-[#1])(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1]",
      "anthranil_acid_G(1)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#6](=[#8])-[#8]-[#1])-[#7](-[#1])-[#6]:[#6]",
      "styrene_anil_A(1)", "c:1:c:c-3:c(:c:c:1)-c:2:c:c:c(:c:c:2-[#6]-3=[#6](-[#1])-[#6])-[#7](-[#1])-[#1]",
      "misc_aminal_acid(1)", "c:1:c:c-2:c(:c:c:1)-[#7](-[#6](-[#8]-[#6]-2)(-[#6](=[#8])-[#8]-[#1])-[#6](-[#1])-[#1])-[#6](=[#8])-[#6](-[#1])-[#1]",
      "anil_no_alk_D(1)", "n:1:c(:c(:c(:c(:c:1-[#7](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#6](-[#1])-[#1])-[#7](-[#1])-[#1]",
      "anil_alk_C(1)", "[#7](-[#1])(-c:1:c:c:c:c:c:1)-[#6](-[#6])(-[#6])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1]",
      "misc_anisole_C(1)", "[#7](-[#1])(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#8]-[#6](-[#1])(-[#1])-[#1])-[#8]-[#6]-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])(-[#1])-[#1])-[#6]:[#6]",
      "het_465_misc(1)", "c:1-2:c:c-3:c(:c:c:1-[#8]-[#6]-[#8]-2)-[#6]-[#6]-3",
      "anthranil_acid_H(1)", "c:1:c(:c2:c(:c:c:1)c(c(n2-[#1])-[#6]:[#6])-[#6]:[#6])-[#6](=[#8])-[#8]-[#1]",
      "thio_urea_M(1)", "[#6]:[#6]-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-c:1:c(:c(:c(:c(:c:1-[F,Cl,Br,I])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1]",
      "thiazole_amine_K(1)", "n:1:c3:c(:c:c2:c:1nc(s2)-[#7])sc(n3)-[#7]",
      "het_thio_5_imine_A(1)", "[#7]=[#6]-1-[#16]-[#6](=[#7])-[#7]=[#6]-1",
      "thio_amide_E(1)", "c:1:c(:n:c:c:c:1)-[#6](=[#16])-[#7](-[#1])-c:2:c(:c:c:c:c:2)-[#8]-[#6](-[#1])-[#1]",
      "het_thio_676_B(1)", "c:1-2:c(:c(:c(:c(:c:1-[#6](-c:3:c(-[#16]-[#6]-2(-[#1])-[#1]):c(:c(-[#1]):c(:c:3-[#1])-[#1])-[#1])-[#8]-[#6]:[#6])-[#1])-[#1])-[#1])-[#1]",
      "sulfonamide_G(1)", "[#6](-[#1])(-[#1])(-[#1])-c:1:c(:c(:c(:c(:n:1)-[#7](-[#1])-[#16](-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#8]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])(=[#8])=[#8])-[#1])-[#1])-[#1]",
      "thio_thiomorph_Z(1)", "[#6](=[#8])(-[#7]-1-[#6]-[#6]-[#16]-[#6]-[#6]-1)-c:2:c(:c(:c(:c(:c:2-[#16]-[#6](-[#1])-[#1])-[#1])-[#1])-[#1])-[#1]",
      "naphth_ene_one_A(1)", "c:1:c:c:3:c:2:c(:c:1)-[#6](-[#6]=[#6](-c:2:c:c:c:3)-[#8]-[#6](-[#1])-[#1])=[#8]",
      "naphth_ene_one_B(1)", "c:1-3:c:2:c(:c(:c:c:1)-[#7]):c:c:c:c:2-[#6](-[#6]=[#6]-3-[#6](-[F])(-[F])-[F])=[#8]",
      "amino_acridine_A(1)", "c:1:c:c:c:c:2:c:1:c:c:3:c(:n:2):n:c:4:c(:c:3-[#7]):c:c:c:c:4",
      "keto_phenone_B(1)", "c:1:c-3:c(:c:c:c:1)-[#6]-2=[#7]-[!#1]=[#6]-[#6]-[#6]-2-[#6]-3=[#8]",
      "hzone_acid_A(1)", "c:1-3:c(:c(:c(:c(:c:1-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#6](=[#7]-[#7](-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#6](=[#8])-[#8]-[#1])-[#1])-[#1])-c:4:c-3:c(:c(:c(:c:4-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#1]",
      "sulfonamide_H(1)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[#7](-[#1])-[#1])-[#1])-[#1])-[#16](=[#8])(=[#8])-[#7](-[#1])-c:2:n:n:c(:c(:c:2-[#1])-[#1])-[#1]",
      "pyrrole_J(1)", "c1(c-2c(c(n1-[#6](-[#8])=[#8])-[#6](-[#1])-[#1])-[#16]-[#6](-[#1])(-[#1])-[#16]-2)-[#6](-[#1])-[#1]",
      "pyrazole_amino_B(1)", "s1ccnc1-c2c(n(nc2-[#1])-[#1])-[#7](-[#1])-[#1]",
      "pyrrole_K(1)", "c1(c(c(c(n1-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[#1])-[#1])-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#6](=[#8])-[#8]-[#1]",
      "anthranil_acid_I(1)", "c:1:2(:c(:c(:c(:o:1)-[#6])-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#6]:[#6](-[#1]):[#6](-[#1]):[#6](-[#1]):[#6](-[#1]):[#6]:2-[#6](=[#8])-[#8]-[#1]",
      "thio_amide_F(1)", "[!#1]:[#6]-[#6](=[#16])-[#7](-[#1])-[#7](-[#1])-[#6]:[!#1]",
      "ene_one_C(1)", "[#6]-1(=[#8])-[#6](-[#6](-[#6]#[#7])=[#6](-[#1])-[#7])-[#6](-[#7])-[#6]=[#6]-1",
      "het_65_H(1)", "c2(c-1n(-[#6](-[#6]=[#6]-[#7]-1)=[#8])nc2-c3cccn3)-[#6]#[#7]",
      "cyano_imine_D(1)", "[#8]=[#6]-1-[#6](=[#7]-[#7]-[#6]-[#6]-1)-[#6]#[#7]",
      "cyano_misc_A(1)", "c:2(:c:1:c:c:c:c:c:1:n:n:c:2)-[#6](-[#6]:[#6])-[#6]#[#7]",
      "ene_misc_C(1)", "c:1:c:c-2:c(:c:c:1)-[#6]=[#6]-[#6](-[#7]-2-[#6](=[#8])-[#7](-[#1])-c:3:c:c(:c(:c:c:3)-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
      "het_66_E(1)", "c:2:c:c:1:n:c(:c(:n:c:1:c:c:2)-c:3:c:c:c:c:c:3)-c:4:c:c:c:c:c:4-[#8]-[#1]",
      "keto_keto_beta_F(1)", "[#6](-[#1])(-[#1])-[#6](-[#8]-[#1])=[#6](-[#6](=[#8])-[#6](-[#1])-[#1])-[#6](-[#1])-[#6]#[#6]",
      "misc_naphthimidazole(1)", "c:1:c:4:c(:c:c2:c:1nc(n2-[#1])-[#6]-[#8]-[#6](=[#8])-c:3:c:c(:c:c(:c:3)-[#7](-[#1])-[#1])-[#7](-[#1])-[#1]):c:c:c:c:4",
      "naphth_ene_one_C(1)", "c:2(:c:1:c:c:c:c-3:c:1:c(:c:c:2)-[#6]=[#6]-[#6]-3=[#7])-[#7]",
      "keto_phenone_C(1)", "c:2(:c:1:c:c:c:c:c:1:c-3:c(:c:2)-[#6](-c:4:c:c:c:c:c-3:4)=[#8])-[#8]-[#1]",
      "coumarin_C(1)", "[#6]-2(-[#6]=[#7]-c:1:c:c(:c:c:c:1-[#8]-2)-[Cl])=[#8]",
      "thio_est_cyano_A(1)", "[#6]-1=[#6]-[#7](-[#6](-c:2:c-1:c:c:c:c:2)(-[#6]#[#7])-[#6](=[#16])-[#16])-[#6]=[#8]",
      "het_65_imidazole(1)", "c2(nc:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])n2-[#6])-[#7](-[#1])-[#6](-[#7](-[#1])-c:3:c(:c:c:c:c:3-[#1])-[#1])=[#8]",
      "anthranil_acid_J(1)", "[#7](-[#1])(-[#6]:[#6])-c:1:c(-[#6](=[#8])-[#8]-[#1]):c:c:c(:n:1)-[#6]:[#6]",
      "colchicine_het(1)", "c:1-3:c(:c:c:c:c:1)-[#16]-[#6](=[#7]-[#7]=[#6]-2-[#6]=[#6]-[#6]=[#6]-[#6]=[#6]-2)-[#7]-3-[#6](-[#1])-[#1]",
      "ene_misc_D(1)", "c:1-2:c(:c(:c(:c(:c:1-[#1])-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#6](=[#6](-[#6])-[#16]-[#6]-2(-[#1])-[#1])-[#6]",
      "indole_3yl_alk_B(1)", "c:12:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])c(c(-[#6]:[#6])n2-!@[#6]:[#6])-[#6](-[#1])-[#1]",
      "anil_OH_no_alk_A(1)", "[#7](-[#1])(-[#1])-c:1:c:c:c(:c:c:1-[#8]-[#1])-[#16](=[#8])(=[#8])-[#8]-[#1]",
      "thiazole_amine_L(1)", "s:1:c:c:c(:c:1-[#1])-c:2:c:s:c(:n:2)-[#7](-[#1])-[#1]",
      "pyrazole_amino_A(1)", "c1c(-[#7](-[#1])-[#1])nnc1-c2c(-[#6](-[#1])-[#1])oc(c2-[#1])-[#1]",
      "het_thio_N_5D(1)", "n1nscc1-c2nc(no2)-[#6]:[#6]",
      "anil_alk_indane(1)", "c:1(:c:c-3:c(:c:c:1)-[#7]-[#6]-4-c:2:c:c:c:c:c:2-[#6]-[#6]-3-4)-[#6;X4]",
      "anil_di_alk_N(1)", "c:1-2:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#6](=[#6](-[#1])-[#6]-3-[#6](-[#6]#[#7])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#7]-2-3)-[#1]",
      "het_666_C(1)", "c:2-3:c(:c:c:1:c:c:c:c:c:1:c:2)-[#7](-[#6](-[#1])-[#1])-[#6](=[#8])-[#6](=[#7]-3)-[#6]:[#6]-[#7](-[#1])-[#6](-[#1])-[#1]",
      "ene_one_D(1)", "[#6](-[#8]-[#1]):[#6]-[#6](=[#8])-[#6](-[#1])=[#6](-[#6])-[#6]",
      "anil_di_alk_indol(1)", "c:1:2:c(:c(:c(:c(:c:1-[#1])-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1]):c(:c(-[#1]):n:2-[#1])-[#16](=[#8])=[#8]",
      "anil_no_alk_indol_A(1)", "c:1:2:c(:c(:c(:c(:c:1-[#1])-[#1])-[#7](-[#1])-[#1])-[#1]):c(:c(-[#1]):n:2-[#6](-[#1])-[#1])-[#1]",
      "dhp_amino_CN_G(1)", "[#16;X2]-1-[#6]=[#6](-[#6]#[#7])-[#6](-[#6])(-[#6]=[#8])-[#6](=[#6]-1-[#7](-[#1])-[#1])-[$([#6]=[#8]),$([#6]#[#7])]",
      "anil_di_alk_dhp(1)", "[#7]-2-[#6]=[#6](-[#6]=[#8])-[#6](-c:1:c:c:c(:c:c:1)-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#6]~3=[#6]-2~[#7]~[#6](~[#16])~[#7]~[#6]~3~[#7]",
      "anthranil_amide_A(1)", "c:1:c(:c:c:c:c:1)-[#6](=[#8])-[#7](-[#1])-c:2:c(:c:c:c:c:2)-[#6](=[#8])-[#7](-[#1])-[#7](-[#1])-c:3:n:c:c:s:3",
      "hzone_anthran_Z(1)", "c:1:c:2:c(:c:c:c:1):c(:c:3:c(:c:2):c:c:c:c:3)-[#6]=[#7]-[#7](-[#1])-c:4:c:c:c:c:c:4",
      "ene_one_amide_A(1)", "c:1:c(:c:c:c:c:1)-[#6](-[#1])-[#7]-[#6](=[#8])-[#6](-[#7](-[#1])-[#6](-[#1])-[#1])=[#6](-[#1])-[#6](=[#8])-c:2:c:c:c(:c:c:2)-[#8]-[#6](-[#1])-[#1]",
      "het_76_A(1)", "s:1:c(:c(-[#1]):c(:c:1-[#6]-3=[#7]-c:2:c:c:c:c:c:2-[#6](=[#7]-[#7]-3-[#1])-c:4:c:c:n:c:c:4)-[#1])-[#1]",
      "thio_urea_N(1)", "o:1:c(:c(-[#1]):c(:c:1-[#6](-[#1])(-[#1])-[#7](-[#1])-[#6](=[#16])-[#7](-[#6]-[#1])-[#6](-[#1])(-[#1])-c:2:c:c:c:c:c:2)-[#1])-[#1]",
      "anil_di_alk_coum(1)", "c:1:c(:c:c:c:c:1)-[#7](-[#6]-[#1])-[#6](-[#1])-[#6](-[#1])-[#6](-[#1])-[#7](-[#1])-[#6](=[#8])-[#6]-2=[#6](-[#8]-[#6](-[#6](=[#6]-2-[#6](-[#1])-[#1])-[#1])=[#8])-[#6](-[#1])-[#1]",
      "ene_one_amide_B(1)", "c2-3:c:c:c:1:c:c:c:c:c:1:c2-[#6](-[#1])-[#6;X4]-[#7]-[#6]-3=[#6](-[#1])-[#6](=[#8])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
      "dhp_amidine_A(1)", "[#7]-1(-[#1])-[#7]=[#6](-[#7]-[#1])-[#16]-[#6](=[#6]-1-[#6]:[#6])-[#6]:[#6]",
      "thio_urea_O(1)", "c:1(:c(:c-3:c(:c(:c:1-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#6](-[#1])-c:2:c(:c(:c(:o:2)-[#6]-[#1])-[#1])-[#1])-[#1])-[#8]-[#6](-[#8]-3)(-[#1])-[#1])-[#1])-[#1]",
      "anil_di_alk_O(1)", "c:1(:c(:c(:c(:c(:c:1-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-c:2:c:c:c:c:c:2)-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#1]",
      "thio_urea_P(1)", "[#8]=[#6]-!@n:1:c:c:c-2:c:1-[#7](-[#1])-[#6](=[#16])-[#7]-2-[#1]",
      "het_pyraz_misc(1)", "[#6](-[F])(-[F])-[#6](=[#8])-[#7](-[#1])-c:1:c(-[#1]):n(-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#8]-[#6](-[#1])(-[#1])-[#6]:[#6]):n:c:1-[#1]",
      "diazox_C(1)", "[#7]-2=[#7]-[#6]:1:[#7]:[!#6&!#1]:[#7]:[#6]:1-[#7]=[#7]-[#6]:[#6]-2",
      "diazox_D(1)", "[#6]-2(-[#1])(-[#8]-[#1])-[#6]:1:[#7]:[!#6&!#1]:[#7]:[#6]:1-[#6](-[#1])(-[#8]-[#1])-[#6]=[#6]-2",
      "misc_cyclopropane(1)", "[#6]-1(-[#6](-[#1])(-[#1])-[#6]-1(-[#1])-[#1])(-[#6](=[#8])-[#7](-[#1])-c:2:c:c:c(:c:c:2)-[#8]-[#6](-[#1])(-[#1])-[#8])-[#16](=[#8])(=[#8])-[#6]:[#6]",
      "imine_ene_one_B(1)", "[#6]-1:[#6]-[#6](=[#8])-[#6]=[#6]-1-[#7]=[#6](-[#1])-[#7](-[#6;X4])-[#6;X4]",
      "misc_furan_A(1)", "c:1:c(:o:c(:c:1-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#7]-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#8]-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#8]-c:2:c:c-3:c(:c:c:2)-[#8]-[#6](-[#8]-3)(-[#1])-[#1]",
      "rhod_sat_E(1)", "[#7]-4(-c:1:c:c:c:c:c:1)-[#6](=[#8])-[#16]-[#6](-[#1])(-[#7](-[#1])-c:2:c:c:c:c:3:c:c:c:c:c:2:3)-[#6]-4=[#8]",
      "rhod_sat_imine_A(1)", "[#7]-3(-[#6](=[#8])-c:1:c:c:c:c:c:1)-[#6](=[#7]-c:2:c:c:c:c:c:2)-[#16]-[#6](-[#1])(-[#1])-[#6]-3=[#8]",
      "rhod_sat_F(1)", "[#7]-2(-c:1:c:c:c:c:c:1)-[#6](=[#8])-[#16]-[#6](-[#1])(-[#1])-[#6]-2=[#16]",
      "het_thio_5_imine_B(1)", "[#7]-1(-[#6](-[#1])-[#1])-[#6](=[#16])-[#7](-[#6]:[#6])-[#6](=[#7]-[#6]:[#6])-[#6]-1=[#7]-[#6]:[#6]",
      "het_thio_5_imine_C(1)", "[#16]-1-[#6](=[#7]-[#7]-[#1])-[#16]-[#6](=[#7]-[#6]:[#6])-[#6]-1=[#7]-[#6]:[#6]",
      "ene_five_het_N(1)", "[#6]-2(=[#8])-[#6](=[#6](-[#1])-c:1:c(:c:c:c(:c:1)-[F,Cl,Br,I])-[#8]-[#6](-[#1])-[#1])-[#7]=[#6](-[#16]-[#6](-[#1])-[#1])-[#16]-2",
      "thio_carbam_A(1)", "[#6](-[#1])(-[#1])-[#16]-[#6](=[#16])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6]:[#6]",
      "misc_anilide_A(1)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])-[#1])-[#7](-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6]:[#6])-[#1])-[#7](-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6]:[#6]",
      "misc_anilide_B(1)", "c:1(:c(:c(:c(:c(:c:1-[#6](-[#1])-[#1])-[#1])-[Br])-[#1])-[#6](-[#1])-[#1])-[#7](-[#1])-[#6](=[#8])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1]",
      "mannich_B(1)", "c:1-2:c(:c:c:c(:c:1-[#8]-[#6](-[#1])(-[#1])-[#7](-[#6]:[#6]-[#8]-[#6](-[#1])-[#1])-[#6]-2(-[#1])-[#1])-[#1])-[#1]",
      "mannich_catechol_A(1)", "c:1-2:c(:c(:c(:c(:c:1-[#8]-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6]-2(-[#1])-[#1])-[#1])-[#8])-[#8])-[#1]",
      "anil_alk_D(1)", "[#7](-[#1])(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#6](-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
      "het_65_I(1)", "n:1:2:c:c:c(:c:c:1:c:c(:c:2-[#6](=[#8])-[#6]:[#6])-[#6]:[#6])-[#6](~[#8])~[#8]",
      "misc_urea_A(1)", "c:1(:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#6](=[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1])-[#6](-[#6;X4])(-[#6;X4])-[#7](-[#1])-[#6](=[#8])-[#7](-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#6](-[#1])(-[#1])-[#6]:[#6]",
      "imidazole_C(1)", "[#6]-3(-[#1])(-n:1:c(:n:c(:c:1-[#1])-[#1])-[#1])-c:2:c(:c(:c(:c(:c:2-[#1])-[Br])-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-c:4:c-3:c(:c(:c(:c:4-[#1])-[#1])-[#1])-[#1]",
      "styrene_imidazole_A(1)", "[#6](=[#6](-[#1])-[#6](-[#1])(-[#1])-n:1:c(:n:c(:c:1-[#1])-[#1])-[#1])(-[#6]:[#6])-[#6]:[#6]",
      "thiazole_amine_M(1)", "c:1(:n:c(:c(-[#1]):s:1)-c:2:c:c:n:c:c:2)-[#7](-[#1])-[#6]:[#6]-[#6](-[#1])-[#1]",
      "misc_pyrrole_thiaz(1)", "c:1(:n:c(:c(-[#1]):s:1)-c:2:c:c:c:c:c:2)-[#6](-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7]-[#6](-[#1])(-[#1])-c:3:c:c:c:n:3-[#1]",
      "pyrrole_L(1)", "n:1(-[#1]):c(:c(-[#6](-[#1])-[#1]):c(:c:1-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])-[#6](=[#8])-[#8]-[#6](-[#1])-[#1]",
      "het_thio_65_D(1)", "c:2(:n:c:1:c(:c(:c:c(:c:1-[#1])-[F,Cl,Br,I])-[#1]):n:2-[#1])-[#16]-[#6](-[#1])(-[#1])-[#6](=[#8])-[#7](-[#1])-[#6]:[#6]",
      "ene_misc_E(1)", "c:1(:c(:c-2:c(:c(:c:1-[#8]-[#6](-[#1])-[#1])-[#1])-[#6]=[#6]-[#6](-[#1])-[#16]-2)-[#1])-[#8]-[#6](-[#1])-[#1]",
      "anil_OC_alk_E(1)", "[#6](-[#1])(-[#1])-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#8]-[#1])-[#6](-[#1])-[#1]",
      "melamine_B(1)", "n:1:c(:n:c(:n:c:1-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#7](-[#6]-[#1])-[#6]=[#8]",
      "thio_cyano_A(1)", "[#7]-1(-[#1])-[#6](=[#16])-[#6](-[#1])(-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#6](=[#6]-1-[#6]:[#6])-[#1]",
      "cyano_amino_het_B(1)", "n:1:c(:c(:c(:c(:c:1-[#16;X2]-c:2:c:c:c:c:c:2-[#7](-[#1])-[#1])-[#6]#[#7])-c:3:c:c:c:c:c:3)-[#6]#[#7])-[#7](-[#1])-[#1]",
      "cyano_pyridone_G(1)", "[#7]-2(-c:1:c:c:c(:c:c:1)-[#8]-[#6](-[#1])-[#1])-[#6](=[#8])-[#6](=[#6]-[#6](=[#7]-2)-n:3:c:n:c:c:3)-[#6]#[#7]",
      "het_65_J(1)", "o:1:c(:c:c:2:c:1:c(:c(:c(:c:2-[#1])-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#6](~[#8])~[#8]",
      "ene_one_yne_A(1)", "[#6]#[#6]-[#6](=[#8])-[#6]#[#6]",
      "anil_OH_no_alk_B(1)", "c:2(:c:1:c(:c(:c(:c(:c:1:c(:c(:c:2-[#8]-[#1])-[#6]=[#8])-[#1])-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#1]",
      "hzone_acyl_misc_A(1)", "c:1(:c(:c(:c(:o:1)-[$([#1]),$([#6](-[#1])-[#1])])-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#7]=[#6](-[$([#1]),$([#6](-[#1])-[#1])])-c:2:c:c:c:c(:c:2)-[*]-[*]-[*]-c:3:c:c:c:o:3",
      "thiophene_F(1)", "[#16](=[#8])(=[#8])-[#7](-[#1])-c:1:c(:c(:c(:s:1)-[#6]-[#1])-[#6]-[#1])-[#6](=[#8])-[#7]-[#1]",
      "anil_OC_alk_F(1)", "[#6](-[#1])(-[#1])-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-[#6](-[#1])(-[#6]=[#8])-[#16]",
      "het_65_K(1)", "n1nnnc2cccc12",
      "het_65_L(1)", "c:1-2:c(-[#1]):s:c(:c:1-[#6](=[#8])-[#7]-[#7]=[#6]-2-[#7](-[#1])-[#1])-[#6]=[#8]",
      "coumarin_E(1)", "c:1-3:c(:c:2:c(:c:c:1-[Br]):o:c:c:2)-[#6](=[#6]-[#6](=[#8])-[#8]-3)-[#1]",
      "coumarin_F(1)", "c:1-3:c(:c:c:c:c:1)-[#6](=[#6](-[#6](=[#8])-[#7](-[#1])-c:2:n:o:c:c:2-[Br])-[#6](=[#8])-[#8]-3)-[#1]",
      "coumarin_G(1)", "c:1-2:c(:c:c(:c:c:1-[F,Cl,Br,I])-[F,Cl,Br,I])-[#6](=[#6](-[#6](=[#8])-[#7](-[#1])-[#1])-[#6](=[#7]-[#1])-[#8]-2)-[#1]",
      "coumarin_H(1)", "c:1-3:c(:c:c:c:c:1)-[#6](=[#6](-[#6](=[#8])-[#7](-[#1])-c:2:n:c(:c:s:2)-[#6]:[#16]:[#6]-[#1])-[#6](=[#8])-[#8]-3)-[#1]",
      "het_thio_67_A(1)", "[#6](-[#1])(-[#1])-[#16;X2]-c:2:n:n:c:1-[#6]:[#6]-[#7]=[#6]-[#8]-c:1:n:2",
      "sulfonamide_I(1)", "[#16](=[#8])(=[#8])(-c:1:c:n(-[#6](-[#1])-[#1]):c:n:1)-[#7](-[#1])-c:2:c:n(:n:c:2)-[#6](-[#1])(-[#1])-[#6]:[#6]-[#8]-[#6](-[#1])-[#1]",
      "het_65_mannich(1)", "c:1-2:c(:c(:c(:c(:c:1-[#8]-[#6](-[#1])(-[#1])-[#8]-2)-[#6](-[#1])(-[#1])-[#7]-3-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#6]:[#6]-3)-[#1])-[#1])-[#1]",
      "anil_alk_A(1)", "[#6](-[#1])(-[#1])-[#8]-[#6]:[#6]-[#6](-[#1])(-[#1])-[#7](-[#1])-c:2:c(:c(:c:1:n(:c(:n:c:1:c:2-[#1])-[#1])-[#6]-[#1])-[#1])-[#1]",
      "het_5_inium(1)", "[#7]-4(-c:1:c:c:c:c:c:1)-[#6](=[#7+](-c:2:c:c:c:c:c:2)-[#6](=[#7]-c:3:c:c:c:c:c:3)-[#7]-4)-[#1]",
      "anil_di_alk_P(1)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:2:c:c:c:1:s:c(:n:c:1:c:2)-[#16]-[#6](-[#1])-[#1]",
      "thio_urea_Q(1)", "c:1:2:c(:c(:c(:c(:c:1:c(:c(-[#1]):c(:c:2-[#1])-[#1])-[#6](-[#6](-[#1])-[#1])=[#7]-[#7](-[#1])-[#6](=[#16])-[#7](-[#1])-[#6]:[#6]:[#6])-[#1])-[#1])-[#1])-[#1]",
      "thio_pyridine_A(1)", "[#6]:1(:[#7]:[#6](:[#7]:[!#1]:[#7]:1)-c:2:c(:c(:c(:o:2)-[#1])-[#1])-[#1])-[#16]-[#6;X4]",
      "misc_phthal_thio_N(1)", "c:1(:n:s:c(:n:1)-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](-[#1])(-[#1])-[#7]-[#6](=[#8])-c:2:c:c:c:c:c:2-[#6](=[#8])-[#8]-[#1])-c:3:c:c:c:c:c:3",
      "hzone_acyl_misc_B(1)", "n:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#7]=[#6](-[#1])-c:2:c:c:c:c:c:2-[#8]-[#6](-[#1])(-[#1])-[#6](=[#8])-[#8]-[#1]",
      "tert_butyl_B(1)", "[#6](-[#1])(-[#1])(-[#1])-[#6](-[#6](-[#1])(-[#1])-[#1])(-[#6](-[#1])(-[#1])-[#1])-c:1:c(:c(:c(:c(:c:1-[#8]-[#1])-[#6](-[#6](-[#1])(-[#1])-[#1])(-[#6](-[#1])(-[#1])-[#1])-[#6](-[#1])(-[#1])-[#1])-[#1])-[#6](-[#1])(-[#1])-c:2:c:c:c(:c(:c:2-[#1])-[#1])-[#8]-[#1])-[#1]",
      "diazox_E(1)", "[#7](-[#1])(-[#1])-c:1:c(-[#7](-[#1])-[#1]):c(:c(-[#1]):c:2:n:o:n:c:1:2)-[#1]",
      "anil_NH_no_alk_B(1)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:c(:c:1-[#7](-[#1])-[#16](=[#8])=[#8])-[#1])-[#7](-[#1])-[#6](-[#1])-[#1])-[F,Cl,Br,I])-[#1]",
      "anil_no_alk_A(1)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:c(:c:1-[#7]=[#6]-2-[#6](=[#6]~[#6]~[#6]=[#6]-2)-[#1])-[#1])-[#1])-[#1])-[#1]",
      "anil_no_alk_B(1)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:c(:c:1-n:2:c:c:c:c:2)-[#1])-[#6](-[#1])-[#1])-[#6](-[#1])-[#1])-[#1]",
      "thio_ene_amine_A(1)", "[#16]=[#6]-[#6](-[#6](-[#1])-[#1])=[#6](-[#6](-[#1])-[#1])-[#7](-[#6](-[#1])-[#1])-[#6](-[#1])-[#1]",
      "het_55_B(1)", "[#6]-1:[#6]-[#8]-[#6]-2-[#6](-[#1])(-[#1])-[#6](=[#8])-[#8]-[#6]-1-2",
      "cyanamide_A(1)", "[#8]-[#6](=[#8])-[#6](-[#1])(-[#1])-[#16;X2]-[#6](=[#7]-[#6]#[#7])-[#7](-[#1])-c:1:c:c:c:c:c:1",
      "ene_one_one_A(1)", "[#8]=[#6]-[#6]-1=[#6](-[#16]-[#6](=[#6](-[#1])-[#6])-[#16]-1)-[#6]=[#8]",
      "ene_six_het_D(1)", "[#8]=[#6]-1-[#7]-[#7]-[#6](=[#7]-[#6]-1=[#6]-[#1])-[!#1]:[!#1]",
      "ene_cyano_E(1)", "[#8]=[#6]-[#6](-[#1])=[#6](-[#6]#[#7])-[#6]",
      "ene_cyano_F(1)", "[#8](-[#1])-[#6](=[#8])-c:1:c(:c(:c(:c(:c:1-[#8]-[#1])-[#1])-c:2:c(-[#1]):c(:c(:o:2)-[#6](-[#1])=[#6](-[#6]#[#7])-c:3:n:c:c:n:3)-[#1])-[#1])-[#1]",
      "hzone_furan_C(1)", "c:1:c(:c:c:c:c:1)-[#7](-c:2:c:c:c:c:c:2)-[#7]=[#6](-[#1])-[#6]:3:[#6](:[#6](:[#6](:[!#1]:3)-c:4:c:c:c:c(:c:4)-[#6](=[#8])-[#8]-[#1])-[#1])-[#1]",
      "anil_no_alk_C(1)", "[#7](-[#1])(-[#1])-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-c:2:c(-[#1]):c(:c(-[#6](-[#1])-[#1]):o:2)-[#6]=[#8])-[#1])-[#1]",
      "hzone_acid_D(1)", "[#8](-[#1])-[#6](=[#8])-c:1:c:c:c(:c:c:1)-[#7]-[#7]=[#6](-[#1])-[#6]:2:[#6](:[#6](:[#6](:[!#1]:2)-c:3:c:c:c:c:c:3)-[#1])-[#1]",
      "ene_cyano_G(1)", "n1(-[#6])c(c(-[#1])c(c1-[#6](-[#1])=[#6](-[#6]#[#7])-c:2:n:c:c:s:2)-[#1])-[#1]",
      "hzone_furan_E(1)", "[#8](-[#1])-[#6](=[#8])-c:1:c:c:c:c(:c:1)-[#6]:[!#1]:[#6]-[#6]=[#7]-[#7](-[#1])-[#6](=[#8])-[#6](-[#1])(-[#1])-[#8]",
      "het_6_pyridone_NH2(1)", "[#8](-[#1])-[#6]:1:[#6](:[#6]:[!#1]:[#6](:[#7]:1)-[#7](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6](=[#8])-[#8]",
      "imine_one_fives_D(1)", "[#6]-1(=[!#6&!#1])-[#6](-[#7]=[#6]-[#16]-1)=[#8]",
      "pyrrole_M(1)", "n2(-c:1:c:c:c:c:c:1)c(c(-[#1])c(c2-[#6]=[#7]-[#8]-[#1])-[#1])-[#1]",
      "pyrrole_N(1)", "n2(-[#6](-[#1])-c:1:c(:c(:c:c(:c:1-[#1])-[#1])-[#1])-[#1])c(c(-[#1])c(c2-[#6]-[#1])-[#1])-[#6]-[#1]",
      "pyrrole_O(1)", "n1(-[#6](-[#1])-[#1])c(c(-[#6](=[#8])-[#6])c(c1-[#6]:[#6])-[#6])-[#6](-[#1])-[#1]",
      "sulfonamide_J(1)", "n3(-c:1:c:c:c:c:c:1-[#7](-[#1])-[#16](=[#8])(=[#8])-c:2:c:c:c:s:2)c(c(-[#1])c(c3-[#1])-[#1])-[#1]",
      "misc_pyrrole_benz(1)", "n2(-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#6](=[#8])-[#7](-[#1])-[#6](-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#8]-[#6]:[#6])c(c(-[#1])c(c2-[#1])-[#1])-[#1]",
      "thio_urea_R(1)", "c:1(:c:c:c:c:c:1)-[#7](-[#1])-[#6](=[#16])-[#7]-[#7](-[#1])-[#6](-[#1])=[#6](-[#1])-[#6]=[#8]",
      "ene_one_one_B(1)", "[#6]-1(-[#6](=[#8])-[#6](-[#1])(-[#1])-[#6]-[#6](-[#1])(-[#1])-[#6]-1=[#8])=[#6](-[#7]-[#1])-[#6]=[#8]",
      "dhp_amino_CN_H(1)", "[#7](-[#1])(-[#1])-[#6]-1=[#6](-[#6]#[#7])-[#6](-[#1])(-[#6]:[#6])-[#16]-[#6;X4]-[#16]-1",
      "het_66_anisole(1)", "[#6](-[#1])(-[#1])-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#1])-[#1])-[#7](-[#1])-c:2:c:c:n:c:3:c(:c:c:c(:c:2:3)-[#8]-[#6](-[#1])-[#1])-[#8]-[#6](-[#1])-[#1]",
      "thiazole_amine_N(1)", "[#6](-[#1])(-[#1])-[#8]-c:1:c(:c(:c(:c(:c:1-[#1])-[#1])-[#8]-[#6](-[#1])-[#1])-[#1])-[#7](-[#1])-c:2:n:c(:c:s:2)-c:3:c:c:c(:c:c:3)-[#8]-[#6](-[#1])-[#1]",
      "het_pyridiniums_C(1)", "[#6]~1~3~[#7](-[#6]:[#6])~[#6]~[#6]~[#6]~[#6]~1~[#6]~2~[#7]~[#6]~[#6]~[#6]~[#7+]~2~[#7]~3",
      "het_5_E(1)", "[#7]-3(-c:2:c:1:c:c:c:c:c:1:c:c:c:2)-[#7]=[#6](-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#6]-3=[#8]",
      "ene_six_het_A(483)", "[#6]-1(-[#6](~[!#6&!#1]~[#6]-[!#6&!#1]-[#6]-1=[!#6&!#1])~[!#6&!#1])=[#6;!R]-[#1]",
      "hzone_phenol_A(479)", "c:1:c:c(:c(:c:c:1)-[#6]=[#7]-[#7])-[#8]-[#1]",
      "anil_di_alk_A(478)", "[#6](-[#1])(-[#1])-[#7](-[#6](-[#1])-[#1])-c:1:c:c(:c(:c(:c:1)-[$([#1]),$([#6](-[#1])-[#1]),$([#8]-[#6](-[#1])(-[#1])-[#6](-[#1])-[#1])])-[#7])-[#1]",
      "indol_3yl_alk(461)", "n:1(c(c(c:2:c:1:c:c:c:c:2-[#1])-[#6;X4]-[#1])-[$([#6](-[#1])-[#1]),$([#6]=,:[!#6&!#1]),$([#6](-[#1])-[#7]),$([#6](-[#1])(-[#6](-[#1])-[#1])-[#6](-[#1])(-[#1])-[#7](-[#1])-[#6](-[#1])-[#1])])-[$([#1]),$([#6](-[#1])-[#1])]",
      "quinone_A(370)", "[!#6&!#1]=[#6]-1-[#6]=,:[#6]-[#6](=[!#6&!#1])-[#6]=,:[#6]-1",
      "azo_A(324)", "[#7;!R]=[#7]",
      "imine_one_A(321)", "[#6]-[#6](=[!#6&!#1;!R])-[#6](=[!#6&!#1;!R])-[$([#6]),$([#16](=[#8])=[#8])]",
      "mannich_A(296)", "[#7]-[#6;X4]-c:1:c:c:c:c:c:1-[#8]-[#1]",
      "anil_di_alk_B(251)", "c:1:c:c(:c:c:c:1-[#7](-[#6;X4])-[#6;X4])-[#6]=[#6]",
      "anil_di_alk_C(246)", "c:1:c:c(:c:c:c:1-[#8]-[#6;X4])-[#7](-[#6;X4])-[$([#1]),$([#6;X4])]",
      "ene_rhod_A(235)", "[#7]-1-[#6](=[#16])-[#16]-[#6](=[#6])-[#6]-1=[#8]",
      "hzone_phenol_B(215)", "c:1(:c:c:c(:c:c:1)-[#6]=[#7]-[#7])-[#8]-[#1]",
      "ene_five_het_A(201)", "[#6]-1(=[#6])-[#6]=[#7]-[!#6&!#1]-[#6]-1=[#8]",
      "anil_di_alk_D(198)", "c:1:c:c(:c:c:c:1-[#7](-[#6;X4])-[#6;X4])-[#6;X4]-[$([#8]-[#1]),$([#6]=[#6]-[#1]),$([#7]-[#6;X4])]",
      "imine_one_isatin(189)", "[#8]=[#6]-2-[#6](=!@[#7]-[#7])-c:1:c:c:c:c:c:1-[#7]-2",
      "anil_di_alk_E(186)", "[#6](-[#1])-[#7](-[#6](-[#1])-[#1])-c:1:c(:c(:c(:c(:c:1-[#1])-[$([#1]),$([#6](-[#1])-[#1])])-[#6](-[#1])-[$([#1]),$([#6]-[#1])])-[#1])-[#1]"
    };

    try{
    
    ArrayList<Molecule> painsList = new ArrayList<Molecule>();

    // parse all pains strings to mol
    SmilesParser sp = new SmilesParser(DefaultChemObjectBuilder.getInstance());

    for (int j = 0; j < painsArray.length; j++) {
      if (j % 2 == 0) {
        Molecule theMol = (Molecule) sp.parseSmiles(painsArray[j]);
        painsList.add(theMol);
      }
    }

//    boolean commonsalt = false;
//
//    String[] sa = new String[10];
//
//    for (int j = 0; j < painsArray.length; j++) {
//      if (j % 2 == 0) {
//        if (sa[123456].equals(painsArray[j])) {
//          commonsalt = true;
//        }
//      }
//    }
    
    } catch (Exception e){
      e.printStackTrace();
    }

    return null;
    
  }
}
