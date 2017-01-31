/*
 * To change this template, choose Tools | Templates
 
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
import mwk.datasystem.util.HelperCmpdList;
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
import org.primefaces.component.column.Column;
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

    private ListManagerBean listManagerBean;

    public ListManagerBean getListManagerBean() {
        return listManagerBean;
    }

    private String listNamesTextArea;
    private String cmpdListIdsTextArea;

    private ArrayList<String> listNames;
    private ArrayList<Long> cmpdListIds;

    @PostConstruct
    public void init() {
        listManagerBean = new ListManagerBean();
        listNames = new ArrayList<String>();
        cmpdListIds = new ArrayList<Long>();
        performUpdateAvailableLists();

        // APP&INV APP & INV APPROVED AND INVESTIGATIONAL
        // default activeList is set to APPROVED and INVESTIGATIONAL which is forced to be CmpdList.id = 1
        loadToActiveList(1l);
    }

    public ListManagerController() {
        init();
    }

    public List<String> completeListName(String query) {
        List<String> suggestions = new ArrayList<String>();
        for (CmpdListVO clVO : listManagerBean.availableLists) {
            if (StringUtils.containsIgnoreCase(clVO.getListName(), query)) {
                suggestions.add(clVO.getListName());
            }
        }
        return suggestions;
    }

    public void onRowEdit(RowEditEvent event) {
        FacesMessage msg = new FacesMessage("List Edited", ((CmpdListVO) event.getObject()).getId().toString());
        FacesContext.getCurrentInstance().addMessage(null, msg);
        HelperCmpdList.updateCmpdList((CmpdListVO) event.getObject(), sessionController.getLoggedUser());
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

    public String performNewSearchForAvailableLists() {
        listNames.clear();
        cmpdListIds.clear();

        return performUpdateAvailableLists();
    }

    public String performUpdateAvailableLists() {

        List<CmpdListVO> justFetchedLists = new ArrayList<CmpdListVO>();

        // MWK 26FEB2016 Why is this here?  A fix for ancient problems, long forgotten?
        if (listManagerBean == null) {
            System.out.println("listManagerBean is null");
        } else {
            listManagerBean.availableLists = new ArrayList<CmpdListVO>();
        }

        try {

            // parse text areas
            String[] splitStrings = null;
            String fixedString = null;
            int i;

            String delimiters = "[\\n\\r\\t,]+";

            if (listNamesTextArea != null && !listNamesTextArea.isEmpty()) {
                splitStrings = listNamesTextArea.split(delimiters);
                for (i = 0; i < splitStrings.length; i++) {
                    fixedString = splitStrings[i].trim();
                    if (fixedString.length() > 0) {
                        listNames.add(fixedString);
                    }
                }
            }

            if (cmpdListIdsTextArea != null && !cmpdListIdsTextArea.isEmpty()) {
                splitStrings = cmpdListIdsTextArea.split(delimiters);
                for (i = 0; i < splitStrings.length; i++) {
                    fixedString = splitStrings[i].replaceAll("[^0-9]", "");
                    if (fixedString.length() > 0) {
                        cmpdListIds.add(Long.valueOf(fixedString));
                    }
                }
            }

            try {
                justFetchedLists = HelperCmpdList.searchCmpdLists(listNames, cmpdListIds, sessionController.getLoggedUser());
            } catch (Exception e) {

            }

            // check for change in size of lists
            // null out the listMembers if there has been a change to force
            // re-load next time it is called
            // OTHERWISE just update list-level info
            // this hoop-protects any list-level data that may have already been
            // fetched 
            HashMap<Long, CmpdListVO> listMap = new HashMap<Long, CmpdListVO>();
            for (CmpdListVO clVO : listManagerBean.availableLists) {
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
                    // then null out listMembers to force re-fetch the next time it is called
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
                    listManagerBean.availableLists.add(fetchedList);
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/webpages/availableLists?faces-redirect=true";

    }

    public void loadToActiveList(Long listId) {

        System.out.println("Entering loadToActiveList(Long listId)");

        for (CmpdListVO clVO : listManagerBean.availableLists) {
            if (clVO.getCmpdListId().longValue() == listId.longValue()) {
                listManagerBean.activeList = clVO;
                performLoadSelectedList();
                break;
            }
        }

    }

    public String performLoadSelectedList() {

        System.out.println("Entering performLoadSelectedList()");

        performLoadList(listManagerBean.activeList);

        listManagerBean.setFilteredActiveListMembers(new ArrayList<CmpdListMemberVO>(listManagerBean.activeList.getCmpdListMembers()));

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable.xhtml?faces-redirect=true";
    }

    // this is called everytime the activeListTable page is reloaded
    public void handleSynchronizeFilters() {
        listManagerBean.setFilteredActiveListMembers(new ArrayList<CmpdListMemberVO>(listManagerBean.activeList.getCmpdListMembers()));
    }

    /**
     *
     * @param listId
     * @return This is used by the ListLogicController until I can figure out
     * coverters for selectItems
     */
    public CmpdListVO fetchList(Long listId) {

        System.out.println("Entering performLoadList(Long listId)");

        CmpdListVO rtn = null;

        for (CmpdListVO clVO : listManagerBean.availableLists) {
            if (clVO.getCmpdListId().longValue() == listId.longValue()) {
                performLoadList(clVO);
                rtn = clVO;
                break;
            }
        }

        return rtn;

    }

    public String performPersistList(CmpdListVO clVO) {

        CmpdListVO rtn = null;

        System.out.println("Entering performPersistList(CmpdListVO clVO)");

        if (clVO != null) {
            rtn = HelperCmpdList.persistCmpdList(clVO, sessionController.getLoggedUser());
        }

        CmpdListVO voList = null;

        if (rtn != null) {
            voList = HelperCmpdList.getCmpdListByCmpdListId(rtn.getCmpdListId(), Boolean.TRUE, sessionController.getLoggedUser());
        }

        if (voList != null) {
            clVO.setCmpdListMembers(voList.getCmpdListMembers());
        }

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performLoadList(CmpdListVO clVO) {

        System.out.println("Entering performLoadList(CmpdListVO clVO)");

        CmpdListVO voList = null;

        if (clVO != null) {
            voList = HelperCmpdList.getCmpdListByCmpdListId(clVO.getCmpdListId(), Boolean.TRUE, sessionController.getLoggedUser());
        }

        if (voList != null) {
            clVO.setCmpdListMembers(voList.getCmpdListMembers());
        }

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performShareList() {

        // only if owner
        if (listManagerBean.activeList.getListOwner().equals(sessionController.getLoggedUser())) {
            HelperCmpdList.shareCmpdList(listManagerBean.activeList.getCmpdListId(), sessionController.getLoggedUser());
            listManagerBean.activeList.setShareWith("EVERYONE");
        }

        return "/webpages/availableLists.xhtml?faces-redirect=true";

    }

    public String performInitiateDeleteList() {

        listManagerBean.listForDelete = new CmpdListVO();

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
            for (CmpdListVO clVO : listManagerBean.availableLists) {
                if (clVO.getCmpdListId().longValue() == testLong.longValue()) {
                    // only if owner
                    if (clVO.getListOwner().equals(sessionController.getLoggedUser())) {
                        listManagerBean.listForDelete = clVO;
                    }
                }
            }
        }

        return "/webpages/confirmDeleteList.xhtml";

    }

    public String performDeleteList() {

        System.out.println("Now in performDeleteList in listManagerController.");
        // only if owner
        if (listManagerBean.listForDelete.getListOwner().equals(sessionController.getLoggedUser())) {
            HelperCmpdList.deleteCmpdListByCmpdListId(listManagerBean.listForDelete.getCmpdListId(), sessionController.getLoggedUser());
            listManagerBean.availableLists.remove(listManagerBean.listForDelete);
        }

        return "/webpages/availableLists.xhtml?faces-redirect=true";

    }

    /**
     *
     * @param smiles
     * @param title
     * @return
     * @throws Exception used to render structure images for postProcessXLS
     */
    public byte[] getStructureImage(String smiles, String title) throws Exception {

        java.net.URL servletURL = null;

        java.net.HttpURLConnection servletConn = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            servletURL = new java.net.URL("http://localhost:8080/oncologydrugs/StructureServlet");

            servletConn = (java.net.HttpURLConnection) servletURL.openConnection();
            servletConn.setDoInput(true);
            servletConn.setDoOutput(true);
            servletConn.setUseCaches(false);
            servletConn.setRequestMethod("POST");
            servletConn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");

            java.io.DataOutputStream outStream = new java.io.DataOutputStream(servletConn.getOutputStream());

            outStream.writeBytes("structureDim=200");

            outStream.writeBytes("&smiles=" + URLEncoder.encode(smiles, "UTF-8"));

            if (title != null) {
                outStream.writeBytes("&title=" + URLEncoder.encode(title, "UTF-8"));
            }

            outStream.flush();
            outStream.close();

            if (servletConn.getResponseCode() != servletConn.HTTP_OK) {
                System.out.println("Exception from StructureServlet in getStructureImage in ListManagerController: " + servletConn.getResponseMessage());
//                throw new Exception("Exception from StructureServlet in getStructureImage in ListManagerController: " + servletConn.getResponseMessage());
            } else {

                InputStream is = servletConn.getInputStream();

                byte[] buf = new byte[1000];
                for (int nChunk = is.read(buf); nChunk != -1; nChunk = is.read(buf)) {
                    baos.write(buf, 0, nChunk);
                }

            }

        } catch (Exception e) {
            System.out.println("Exception in getStructureImage in ListManagerController " + e);
            e.printStackTrace();
//            throw new Exception(e);
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

            sheet.setColumnWidth(18, 10000);

            CreationHelper helper = wb.getCreationHelper();

            // Create the drawing patriarch.  This is the top level container for all shapes. 
            Drawing drawing = sheet.createDrawingPatriarch();

            for (Row row : sheet) {

                String nsc = null;

                System.out.println("In row: " + row.getRowNum());

                // get the SMILES column
                if (row.getRowNum() > 0 && row.getCell(4).getStringCellValue() != null) {

                    row.setHeightInPoints(200);

                    String smiles = row.getCell(4).getStringCellValue();
                    System.out.println("SMILES is: " + smiles);

                    if (row.getCell(0).getStringCellValue() != null) {
                        nsc = row.getCell(1).getStringCellValue();
                        System.out.println("NSC is: " + nsc);
                    }

                    if (nsc == null) {
                        nsc = "";
                    }

                    byte[] bytes = getStructureImage(smiles, nsc);
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

                    anchor.setAnchorType(ClientAnchor.AnchorType.MOVE_AND_RESIZE);

                    Picture pict = drawing.createPicture(anchor, pictureIdx);

                    //auto-size picture relative to its top-left corner
                    pict.resize();

                }

//                sheet.autoSizeColumn(18);
//        for (Cell cell : row) {
//          //cell.setCellValue(cell.getStringCellValue().toUpperCase());        
//          cell.setCellStyle(style);
//        }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public String getListNamesTextArea() {
        return listNamesTextArea;
    }

    public void setListNamesTextArea(String listNamesTextArea) {
        this.listNamesTextArea = listNamesTextArea;
    }

    public String getCmpdListIdsTextArea() {
        return cmpdListIdsTextArea;
    }

    public void setCmpdListIdsTextArea(String cmpdListIdsTextArea) {
        this.cmpdListIdsTextArea = cmpdListIdsTextArea;
    }

}
