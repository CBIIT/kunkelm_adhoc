/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuElement;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class MenuBean implements Serializable {

  private MenuModel model;

  public MenuBean() {

    try {

      model = new DefaultMenuModel();

      DefaultMenuItem item = new DefaultMenuItem();

      // Data System Navigation
      //
      DefaultSubMenu datSysOps = new DefaultSubMenu("Data System Operations");
      datSysOps.setIcon("fa fa-navicon");
      datSysOps.setId("datSysOps");
      datSysOps.setStyle("width: 20%;");

      item = new DefaultMenuItem("Show Available Lists");
      item.setTitle("setTitle test for Show Available Lists");
      item.setIcon("fa fa-eye");
      item.setAjax(false);
      item.setOutcome("/webpages/availableLists?faces-redirect=true");
      datSysOps.addElement(item);

      item = new DefaultMenuItem("Go To Active List");
      item.setTitle("setTitle test for Go To Active List");
      item.setIcon("fa fa-list");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListTable?faces-redirect=true");
      datSysOps.addElement(item);

      item = new DefaultMenuItem("New List by Search");
      item.setTitle("setTitle test for New List by Search");
      item.setIcon("fa fa-search");
      item.setAjax(false);
      item.setOutcome("/webpages/createList?faces-redirect=true");
      datSysOps.addElement(item);

      item = new DefaultMenuItem("New List from File");
      item.setTitle("setTitle test for New List from File");
      item.setIcon("fa fa-upload");
      item.setAjax(false);
      item.setOutcome("/webpages/uploadList?faces-redirect=true");
      datSysOps.addElement(item);

      item = new DefaultMenuItem("List Logic");
      item.setTitle("setTitle test for List Logic");
      item.setIcon("fa fa-lightbulb-o");
      item.setAjax(false);
      item.setOutcome("/webpages/listLogic?faces-redirect=true");
      datSysOps.addElement(item);

      item = new DefaultMenuItem("New List by Structure");
      item.setTitle("setTitle test for New List by Structure");
      item.setIcon("fa fa-search");
      item.setAjax(false);
      item.setOutcome("/webpages/chemDoodle?faces-redirect=true");
      datSysOps.addElement(item);

      item = new DefaultMenuItem("Salts Workbench");
      item.setTitle("setTitle test for Salts Workbench");
      item.setIcon("fa fa-cogs");
      item.setCommand("#{saltController.handleListAllSalts}");
      datSysOps.addElement(item);

      model.addElement(datSysOps);

      // Active List Navigation
      //
      DefaultSubMenu actLisNav = new DefaultSubMenu("Active List Navigation");
      actLisNav.setIcon("fa fa-bicycle");
      actLisNav.setId("actLisNav");
      actLisNav.setStyle("width: 20%;");

      item = new DefaultMenuItem("Table");
      item.setTitle("setTitle test for Table");
      item.setIcon("fa fa-table");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListTable?faces-redirect=true");
      actLisNav.addElement(item);

      item = new DefaultMenuItem("Grid");
      item.setTitle("setTitle test for Grid");
      item.setIcon("fa fa-th");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListGrid?faces-redirect=true");
      actLisNav.addElement(item);

      item = new DefaultMenuItem("Form");
      item.setTitle("setTitle test for Form");
      item.setIcon("fa fa-square-o");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListForm?faces-redirect=true");
      actLisNav.addElement(item);

      item = new DefaultMenuItem("Property Histograms");
      item.setTitle("setTitle test for Property Histograms");
      item.setIcon("fa fa-bar-chart");
      item.setCommand("#{histogramController.handleLoadActiveList}");
      actLisNav.addElement(item);

      model.addElement(actLisNav);

//      // Active List Manipulation
//      DefaultSubMenu actLisManip = new DefaultSubMenu("Active List Manipulation");
//      actLisManip.setIcon("fa fa-wrench");
//      actLisManip.setId("exporters");
//      actLisManip.setStyle("width: 20%;");
//
//      item = new DefaultMenuItem("Delete Selected");
//      item.setTitle("setTitle test for Delete Selected");
//      item.setIcon("fa fa-cut");
//      item.setCommand("");
//      actLisManip.addElement(item);
//
//      item = new DefaultMenuItem("Selected to New List");
//      item.setTitle("setTitle test for Selected copied to New List");
//      item.setIcon("fa fa-copy");
//      item.setCommand("");
//      actLisManip.addElement(item);
//
//      item = new DefaultMenuItem("Selected added to Existing List");
//      item.setTitle("setTitle test for Selected added to Existing List");
//      item.setIcon("fa fa-plus");
//      item.setCommand("");
//      actLisManip.addElement(item);
//      
//      model.addElement(actLisManip);

      // Export
      //
      DefaultSubMenu exporters = new DefaultSubMenu("Export Data");
      exporters.setIcon("fa fa-download");
      exporters.setId("exporters");
      exporters.setStyle("width: 20%;");

      item = new DefaultMenuItem("Export to Excel");
      item.setTitle("setTitle test for Export to Excel");
      item.setIcon("fa fa-file-excel-o");
      item.setAjax(false);
      item.setCommand("#{listManagerController.handleExcelExport}");
      exporters.addElement(item);

      item = new DefaultMenuItem("Export to CSV");
      item.setTitle("setTitle test for Export to CSV");
      item.setIcon("fa fa-file-text-o");
      item.setAjax(false);
      item.setCommand("#{listManagerController.handleCsvExport}");
      exporters.addElement(item);

      item = new DefaultMenuItem("Export Structures ");
      item.setTitle("setTitle test for Export Structures ");
      item.setIcon("fa fa-file-text-o");
      item.setAjax(true);
      item.setOnclick("structuresAsGridImage()");
      exporters.addElement(item);

      model.addElement(exporters);

//      // Logout
//      //
//      item = new DefaultMenuItem("Logout");
      item.setTitle("setTitle test for Logout");
//      item.setId("logout");
//      item.setIcon("fa fa-chain-broken");
//      item.setCommand("#{sessionController.handleLogout}");
//      
//      model.addElement(item);
    } catch (Exception e) {
      System.out.println("Exception in MenuBean");
      e.printStackTrace();
    }

    manipulateModel();

  }

  public MenuModel getModel() {
    return model;
  }

  private void manipulateModel() {

    List<MenuElement> menuElementList = this.model.getElements();

    for (MenuElement me : menuElementList) {
      System.out.println("me: " + me.getId());
      System.out.println("me: " + me.getClass().getName());

      if (me instanceof DefaultSubMenu) {
        DefaultSubMenu dsm = (DefaultSubMenu) me;
        if (dsm.getId().equals("datSysOps")) {
          System.out.println("This is datSysOps");
        } else if (dsm.getId().equals("actLisNav")) {
          System.out.println("This is actLisNav");
        } else {
          System.out.println("This is some other submenu");
          System.out.println(dsm.getId());
        }

      }

    }

  }

}
