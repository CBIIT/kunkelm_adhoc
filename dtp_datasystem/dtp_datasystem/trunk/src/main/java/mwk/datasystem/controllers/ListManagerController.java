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
import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.application.Application;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
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
import org.primefaces.component.datatable.DataTable;
import org.primefaces.component.export.DataExporter;
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

    @PostConstruct
    public void init() {
        this.listManagerBean = new ListManagerBean();
        performUpdateAvailableLists();

        // TESTING
        loadToActiveList(619205300997564186l);
    }

    public ListManagerController() {
        init();
    }

    public void handleExcelExport(ActionEvent event) {
        handleAnyExport(event, "xls");
    }

    public void handlePdfExport(ActionEvent event) {
        handleAnyExport(event, "pdf");
    }

    public void handleCsvExport(ActionEvent event) {
        handleAnyExport(event, "csv");
    }

    public void handleXmlExport(ActionEvent event) {
        handleAnyExport(event, "xml");
    }

    // called from menuBar commands
    private void handleAnyExport(ActionEvent event, String exportType) {

        try {

            FacesContext fc = FacesContext.getCurrentInstance();
            Application application = fc.getApplication();
            ExpressionFactory ef = application.getExpressionFactory();
            ELContext elc = fc.getELContext();

            ValueExpression target = ef.createValueExpression(elc, ":datasystemForm:activeListTbl", String.class);
            ValueExpression type = ef.createValueExpression(elc, exportType, String.class);
            ValueExpression fileName = ef.createValueExpression(elc, "datasystemDataExport", String.class);
            ValueExpression pageOnly = ef.createValueExpression(elc, "true", String.class);
            ValueExpression selectionOnly = ef.createValueExpression(elc, "false", String.class);
            ValueExpression encoding = ef.createValueExpression(elc, "UTF-8", String.class);
            MethodExpression preProcessor = FacesAccessor.createMethodExpression("#{eventManager.preProcessor}", Void.class, new Class[2]);
            MethodExpression postProcessor = FacesAccessor.createMethodExpression("#{eventManager.postProcessor}", Void.class, new Class[2]);

//    DataExporter de = new DataExporter(target,
//            type,
//            fileName,
//            pageOnly,
//            selectionOnly,
//            encoding,
//            preProcessor,
//            postProcessor);
            DataExporter exporter = new DataExporter(target,
                    type,
                    fileName,
                    pageOnly,
                    selectionOnly,
                    encoding,
                    null,
                    null);

            exporter.processAction(event);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

//  // previous version before refactor
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

        HelperCmpdList.updateCmpdList((CmpdListVO) event.getObject(), this.sessionController.getLoggedUser());

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

        if (listManagerBean == null) {
            System.out.println("listManagerBean is null");
        } else {
            listManagerBean.availableLists = new ArrayList<CmpdListVO>();
        }

        try {

            List<CmpdListVO> justFetchedLists = new ArrayList<CmpdListVO>();

            if (this.sessionController != null && this.sessionController.getLoggedUser() != null) {
                justFetchedLists = HelperCmpdList.showAvailableCmpdLists(this.sessionController.getLoggedUser());
            } else {
                System.out.println("this.sessionController.getLoggedUser is null in peformUpdateAvailableLists in ListManagerController");
            }

            // check for change in size of lists
            // null out the listMembers if there has been a change to force
            // re-load next time it is called
            // OTHERWISE just update list-level info
            //
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
    
    public void handleSynchronizeFilters(){
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

    public String performLoadList(CmpdListVO clVO) {

        System.out.println("Entering performLoadList(CmpdListVO clVO)");

        CmpdListVO voList = HelperCmpdList.getCmpdListByCmpdListId(clVO.getCmpdListId(), Boolean.TRUE, this.sessionController.getLoggedUser());
        clVO.setCmpdListMembers(voList.getCmpdListMembers());

        sessionController.configurationBean.performUpdateColumns();

        return "/webpages/activeListTable?faces-redirect=true";

    }

    public String performMakeListPublic() {

        // only if owner
        if (listManagerBean.activeList.getListOwner().equals(this.sessionController.getLoggedUser())) {
            HelperCmpdList.makeCmpdListPublic(listManagerBean.activeList.getCmpdListId(), this.sessionController.getLoggedUser());
            listManagerBean.activeList.setShareWith("PUBLIC");
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
                    if (clVO.getListOwner().equals(this.sessionController.getLoggedUser())) {
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
        if (listManagerBean.listForDelete.getListOwner().equals(this.sessionController.getLoggedUser())) {
            HelperCmpdList.deleteCmpdListByCmpdListId(listManagerBean.listForDelete.getCmpdListId(), this.sessionController.getLoggedUser());
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

}
