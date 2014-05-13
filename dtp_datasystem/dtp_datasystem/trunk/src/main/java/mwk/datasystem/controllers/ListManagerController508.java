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
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.faces.model.SelectItem;
import mwk.datasystem.util.HelperCmpdList;
import mwk.datasystem.vo.CmpdListMemberVO;
import mwk.datasystem.vo.CmpdListVO;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Drawing;
import org.apache.poi.ss.usermodel.Picture;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Workbook;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class ListManagerController508 implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;
    private List<CmpdListVO> availableLists;
    private ArrayList<SelectItem> availableListSelectItems;
    private CmpdListVO selectedList;
    private CmpdListVO[] selectedLists;
    private CmpdListMemberVO selectedListMember;
    private CmpdListMemberVO[] selectedListMembers;
    private CmpdListVO listForDelete;
    private String loggedUser;

    public ListManagerController508() {
        this.loggedUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
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

    public String performUpdateAvailableLists() {

        this.availableLists = new ArrayList<CmpdListVO>();
        this.availableListSelectItems = new ArrayList<SelectItem>();
        this.selectedList = new CmpdListVO();

        try {

            HelperCmpdList helper = new HelperCmpdList();
            List<CmpdListVO> lists = helper.showAvailableCmpdLists(this.loggedUser);

            this.availableLists = lists;

            for (CmpdListVO clVO : this.availableLists) {
                this.availableListSelectItems.add(new SelectItem(clVO.getCmpdListId(), clVO.getListName() + ", " + clVO.getCountListMembers() + " members"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "/pages508/availableLists?faces-redirect=true";

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

        return "/pages508/selectedListTable.xhtml?faces-redirect=true";
    }

    public String performMakeListPublic() {

        HelperCmpdList helper = new HelperCmpdList();

        // only if owner

        if (this.selectedList.getListOwner().equals(this.loggedUser)) {
            helper.makeCmpdListPublic(this.selectedList.getCmpdListId());
            this.selectedList.setShareWith("PUBLIC");
        }

        return "/pages508/availableLists.xhtml";

    }

    public String initiateDeleteList() {

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
                    this.listForDelete = clVO;
                }
            }
        }

        return "/pages508/confirmDeleteList.xhtml";

    }

    public String performDeleteList() {

        System.out.println("Now in performDeleteList in listManagerController508.");

        if (this.listForDelete.getListOwner().equals(this.loggedUser)) {
            HelperCmpdList helper = new HelperCmpdList();
            helper.deleteCmpdListByCmpdListId(this.listForDelete.getCmpdListId());
            this.availableLists.remove(this.listForDelete);
        }

        return "/pages508/availableLists.xhtml";

    }
    
      public byte[] getStructureImage(String smiles, String title) throws Exception {

        String servletString = "notSet";
        java.net.URL servletURL = null;

        java.net.HttpURLConnection servletConn = null;

        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        try {

            servletURL = new java.net.URL("/StructureServlet");

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
                if (row.getCell(12).getStringCellValue() != null) {

                    row.setHeightInPoints(200);

                    String smiles = row.getCell(13).getStringCellValue();
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

    public List<CmpdListVO> getAvailableLists() {
        return availableLists;
    }

    public void setAvailableLists(List<CmpdListVO> availableLists) {
        this.availableLists = availableLists;
    }

    public ArrayList<SelectItem> getAvailableListSelectItems() {
        return availableListSelectItems;
    }

    public void setAvailableListSelectItems(ArrayList<SelectItem> availableListSelectItems) {
        this.availableListSelectItems = availableListSelectItems;
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

    public CmpdListVO getListForDelete() {
        return listForDelete;
    }

    public void setListForDelete(CmpdListVO listForDelete) {
        this.listForDelete = listForDelete;
    }

    public String getLoggedUser() {
        return loggedUser;
    }

    public void setLoggedUser(String loggedUser) {
        this.loggedUser = loggedUser;
    }
}
