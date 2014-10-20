/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.vo.CmpdFragmentVO;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;
import org.primefaces.event.CellEditEvent;
import org.primefaces.event.RowEditEvent;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class ListManagerController implements Serializable {

  static final long serialVersionUID = -8653468638698142855l;

  // reach-through to sessionController
  @ManagedProperty(value = "#{sessionController}")
  private SessionController sessionController;

  public void setSessionController(SessionController sessionController) {
    this.sessionController = sessionController;
  }

  private List<CmpdListVO> availableLists;
  private CmpdListVO selectedAvailableList;
  private List<CmpdListVO> selectedAvailableLists;
  private List<CmpdListVO> filteredAvailableLists;
  //
  private ArrayList<SelectItem> availableListSelectItems;
  //    
  private CmpdListVO agnosticList;
  //
  private CmpdListVO activeList;
  // Are these still needed, or are the ones in ListContentController sufficient?     
  private CmpdListMemberVO selectedActiveListMember;
  private List<CmpdListMemberVO> selectedActiveListMembers;
    //
  //
  private CmpdFragmentVO selectedCmpdFragment;
  //
  private CmpdListVO listForDelete;
  //
  private CmpdListVO tempList;
  private CmpdListMemberVO selectedTempListMember;
  private List<CmpdListMemberVO> selectedTempListMembers;

  @PostConstruct
  public void init() {
    this.performUpdateAvailableLists();
  }

  public ListManagerController() {
  }

  /**
   * Nubbin while trying to allow checkbox select in dataGrid
   *
   * @param o1
   * @param o2
   * @return
   */
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

//  // previous version before refactor
  public List<String> completeListName(String query) {
    List<String> suggestions = new ArrayList<String>();
    for (CmpdListVO clVO : this.availableLists) {
      if (StringUtils.containsIgnoreCase(clVO.getListName(), query)) {
        suggestions.add(clVO.getListName());
      }
    }
    return suggestions;
  }

  public void onRowEdit(RowEditEvent event) {

    FacesMessage msg = new FacesMessage("List Edited", ((CmpdListVO) event.getObject()).getId().toString());
    FacesContext.getCurrentInstance().addMessage(null, msg);

    HelperCmpdList helper = new HelperCmpdList();
    helper.updateCmpdList((CmpdListVO) event.getObject(), this.sessionController.getLoggedUser());

  }

  public void onRowCancel(RowEditEvent event) {
    FacesMessage msg = new FacesMessage("Edit Cancelled", ((CmpdListVO) event.getObject()).getId().toString());
    FacesContext.getCurrentInstance().addMessage(null, msg);
  }

  public void onCellEdit(CellEditEvent event) {

    String colHeader = event.getColumn().getFacet("header").toString();

    Object oldValue = event.getOldValue();
    Object newValue = event.getNewValue();

    if (newValue != null && !newValue.equals(oldValue)) {
      FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, colHeader + "Cell Changed", "Old: " + oldValue + ", New:" + newValue);
      FacesContext.getCurrentInstance().addMessage(null, msg);
    }

  }

  public String performUpdateAvailableLists() {

    this.availableLists = new ArrayList<CmpdListVO>();
    this.availableListSelectItems = new ArrayList<SelectItem>();

    try {

      HelperCmpdList helper = new HelperCmpdList();
      List<CmpdListVO> justFetchedLists = helper.showAvailableCmpdLists(this.sessionController.getLoggedUser());

            //
      // check for change in size of lists
      // null out the listMembers if there has been a change to force
      // re-load next time it is called
      // OTHERWISE just update list-level info
      //
      HashMap<Long, CmpdListVO> listMap = new HashMap<Long, CmpdListVO>();
      for (CmpdListVO clVO : this.availableLists) {
        listMap.put(clVO.getId(), clVO);
      }

      for (CmpdListVO fetchedList : justFetchedLists) {

        if (listMap.containsKey(fetchedList.getId())) {

                    // existing list
          // update list-level info and fetch data if count has changed
          CmpdListVO curList = listMap.get(fetchedList.getId());
          // how many current members
          Integer curCountMembers = curList.getCountListMembers();

                    // if the number of members has changed
          // then null it out listMembers force re-fetch the next time it is called
          if (fetchedList.getCountListMembers().intValue() != curList.getCountListMembers().intValue()) {

            curList.setCmpdListMembers(null);

          } else {

            curList.setId(fetchedList.getId());
            curList.setCmpdListId(fetchedList.getCmpdListId());
            curList.setListName(fetchedList.getListName());
            curList.setDateCreated(fetchedList.getDateCreated());
            curList.setListOwner(fetchedList.getListOwner());
            curList.setShareWith(fetchedList.getShareWith());

            curList.setCountListMembers(fetchedList.getCountListMembers());

          }

        } else {
          // a brand new list
          this.availableLists.add(fetchedList);
          this.availableListSelectItems.add(new SelectItem(fetchedList.getCmpdListId(), fetchedList.getListName() + ", " + fetchedList.getCountListMembers() + " members"));
        }

      }

    } catch (Exception e) {
      e.printStackTrace();
    }

    return "/webpages/availableLists?faces-redirect=true";

  }

  public String performLoadSelectedList() {

    System.out.println("Entering performLoadSelectedList()");

    performLoadList(this.activeList);

    return "/webpages/activeListTable.xhtml?faces-redirect=true";
  }

  /**
   *
   * @param listId
   * @return This is used by the ListLogicController until I can figure out
   * coverters for selectItems
   */
  public CmpdListVO performLoadList(Long listId) {

    System.out.println("Entering performLoadList(Long listId)");

    CmpdListVO rtn = null;

    for (CmpdListVO clVO : this.availableLists) {
      if (clVO.getCmpdListId().longValue() == listId.longValue()) {
        performLoadList(clVO);
        rtn = clVO;
        break;
      }
    }

    return rtn;

  }

  public String performLoadList(CmpdListVO clVO) {
    
    System.out.println("Entering performLoadList(CmpdListVO clVO)");

    HelperCmpdList helper = new HelperCmpdList();
    CmpdListVO voList = helper.getCmpdListByCmpdListId(clVO.getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());
    clVO.setCmpdListMembers(voList.getCmpdListMembers());

    return "/webpages/activeListTable?faces-redirect=true";

  }

  public String performMakeListPublic() {

    HelperCmpdList helper = new HelperCmpdList();

    // only if owner
    if (this.activeList.getListOwner().equals(this.sessionController.getLoggedUser())) {
      helper.makeCmpdListPublic(this.activeList.getCmpdListId(), this.sessionController.getLoggedUser());
      this.activeList.setShareWith("PUBLIC");
    }

    return "/webpages/availableLists.xhtml?faces-redirect=true";

  }

  public String performInitiateDeleteList() {

    this.listForDelete = new CmpdListVO();

    FacesContext context = FacesContext.getCurrentInstance();
    String cmpdListIdForDelete = context.getExternalContext().getRequestParameterMap().get("cmpdListIdForDelete");

    System.out.println("cmpdListIdForDelete is: " + cmpdListIdForDelete);

    Long testLong = null;

    try {
      testLong = Long.parseLong(cmpdListIdForDelete);
    } catch (Exception e) {
      e.printStackTrace();
    }

    if (testLong != null) {
      for (CmpdListVO clVO : this.availableLists) {
        if (clVO.getCmpdListId().longValue() == testLong.longValue()) {
          // only if owner
          if (clVO.getListOwner().equals(this.sessionController.getLoggedUser())) {
            this.listForDelete = clVO;
          }
        }
      }
    }

    return "/webpages/confirmDeleteList.xhtml";

  }

  public String performDeleteList() {

    System.out.println("Now in performDeleteList in listManagerController.");
    // only if owner
    if (this.listForDelete.getListOwner().equals(this.sessionController.getLoggedUser())) {
      HelperCmpdList helper = new HelperCmpdList();
      helper.deleteCmpdListByCmpdListId(this.listForDelete.getCmpdListId(), this.sessionController.getLoggedUser());
      this.availableLists.remove(this.listForDelete);
    }

    return "/webpages/availableLists.xhtml?faces-redirect=true";

  }

  public byte[] getStructureImage(String smiles, String title) throws Exception {

    String servletString = "notSet";
    java.net.URL servletURL = null;

    java.net.HttpURLConnection servletConn = null;

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {

      servletURL = new java.net.URL("http://localhost:8080/datasystem/StructureServlet");

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
        if (row.getCell(17).getStringCellValue() != null) {

          row.setHeightInPoints(200);

          String smiles = row.getCell(17).getStringCellValue();
          System.out.println("SMILES is: " + smiles);

          if (row.getCell(3).getStringCellValue() != null) {
            title = row.getCell(3).getStringCellValue();
            System.out.println("title is: " + title);
          }

          if (title == null) {
            title = "";
          }

          byte[] bytes = getStructureImage(smiles, title);
          int pictureIdx = wb.addPicture(bytes, Workbook.PICTURE_TYPE_PNG);

          //add a picture shape
          ClientAnchor anchor = helper.createClientAnchor();

                    //set top-left corner of the picture,
          //subsequent call of Picture#resize() will operate relative to it
          anchor.setCol1(18);
          anchor.setRow1(row.getRowNum());

          anchor.setDx1(10);
          anchor.setDx2(10);
          anchor.setDy1(10);
          anchor.setDy2(10);

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

  // <editor-fold defaultstate="collapsed" desc="GETTERS and SETTERS.">
  public List<CmpdListVO> getAvailableLists() {
    return availableLists;
  }

  public void setAvailableLists(List<CmpdListVO> availableLists) {
    this.availableLists = availableLists;
  }

  public CmpdListVO getSelectedAvailableList() {
    return selectedAvailableList;
  }

  public void setSelectedAvailableList(CmpdListVO selectedAvailableList) {
    this.selectedAvailableList = selectedAvailableList;
  }

  public List<CmpdListVO> getSelectedAvailableLists() {
    return selectedAvailableLists;
  }

  public void setSelectedAvailableLists(List<CmpdListVO> selectedAvailableLists) {
    this.selectedAvailableLists = selectedAvailableLists;
  }

  public List<CmpdListVO> getFilteredAvailableLists() {
    return filteredAvailableLists;
  }

  public void setFilteredAvailableLists(List<CmpdListVO> filteredAvailableLists) {
    this.filteredAvailableLists = filteredAvailableLists;
  }

  public ArrayList<SelectItem> getAvailableListSelectItems() {
    return availableListSelectItems;
  }

  public void setAvailableListSelectItems(ArrayList<SelectItem> availableListSelectItems) {
    this.availableListSelectItems = availableListSelectItems;
  }

  public CmpdListVO getAgnosticList() {
    return agnosticList;
  }

  public void setAgnosticList(CmpdListVO agnosticList) {
    this.agnosticList = agnosticList;
  }

  public CmpdListVO getActiveList() {
    return activeList;
  }

  public void setActiveList(CmpdListVO activeList) {
    this.activeList = activeList;
  }

  public CmpdListMemberVO getSelectedActiveListMember() {
    return selectedActiveListMember;
  }

  public void setSelectedActiveListMember(CmpdListMemberVO selectedActiveListMember) {
    this.selectedActiveListMember = selectedActiveListMember;
  }

  public CmpdFragmentVO getSelectedCmpdFragment() {
    return selectedCmpdFragment;
  }

  public void setSelectedCmpdFragment(CmpdFragmentVO selectedCmpdFragment) {
    this.selectedCmpdFragment = selectedCmpdFragment;
  }

  public CmpdListVO getListForDelete() {
    return listForDelete;
  }

  public void setListForDelete(CmpdListVO listForDelete) {
    this.listForDelete = listForDelete;
  }

  public CmpdListVO getTempList() {
    return tempList;
  }

  public void setTempList(CmpdListVO tempCmpdList) {
    this.tempList = tempCmpdList;
  }

  public CmpdListMemberVO getSelectedTempListMember() {
    return selectedTempListMember;
  }

  public void setSelectedTempListMember(CmpdListMemberVO selectedTempCmpd) {
    this.selectedTempListMember = selectedTempCmpd;
  }

  public List<CmpdListMemberVO> getSelectedTempListMembers() {
    return selectedTempListMembers;
  }

  public void setSelectedTempListMembers(List<CmpdListMemberVO> selectedTempCmpds) {
    this.selectedTempListMembers = selectedTempCmpds;
  }

  public List<CmpdListMemberVO> getSelectedActiveListMembers() {
    return selectedActiveListMembers;
  }

  public void setSelectedActiveListMembers(List<CmpdListMemberVO> selectedActiveListMembers) {
    this.selectedActiveListMembers = selectedActiveListMembers;
  }
    // </editor-fold>
}
