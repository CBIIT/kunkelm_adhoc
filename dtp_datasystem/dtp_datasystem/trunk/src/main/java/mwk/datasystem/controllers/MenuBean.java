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

      //First submenu
      DefaultSubMenu datSysNav = new DefaultSubMenu("Data System Navigation");
      datSysNav.setIcon("fa-navicon");
      datSysNav.setId("datasystemNavMenu");

      item = new DefaultMenuItem("Show Available Lists");
      item.setIcon("fa-eye");
      item.setAjax(false);
      item.setOutcome("/webpages/availableLists?faces-redirect=true");
      datSysNav.addElement(item);

      item = new DefaultMenuItem("Go To Active List");
      item.setIcon("fa-list");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListTable?faces-redirect=true");
      datSysNav.addElement(item);

      item = new DefaultMenuItem("Create List by Search");
      item.setIcon("fa-search");
      item.setAjax(false);
      item.setOutcome("/webpages/createList?faces-redirect=true");
      datSysNav.addElement(item);

      item = new DefaultMenuItem("Create List from File Upload");
      item.setIcon("fa-upload");
      item.setAjax(false);      
      item.setOutcome("/webpages/uploadList?faces-redirect=true");
      datSysNav.addElement(item);

      item = new DefaultMenuItem("List Logic");
      item.setAjax(false);
      item.setOutcome("/webpages/listLogic?faces-redirect=true");
      datSysNav.addElement(item);

      item = new DefaultMenuItem("Create List by Structure Search");
      item.setIcon("fa-search");
      item.setAjax(false);
      item.setOutcome("/webpages/chemDoodle?faces-redirect=true");
      datSysNav.addElement(item);

      item = new DefaultMenuItem("Salts Workbench");
      item.setIcon("fa-cogs");
      item.setCommand("#{saltController.handleListAllSalts}");
      datSysNav.addElement(item);

      model.addElement(datSysNav);

      // ActiveList
      DefaultSubMenu actLisNav = new DefaultSubMenu("Active List Navigation");
      actLisNav.setIcon("fa-bicycle");
      datSysNav.setId("actLisNav");

      item = new DefaultMenuItem("View as Table");
      item.setIcon("fa-table");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListTable?faces-redirect=true");
      actLisNav.addElement(item);

      item = new DefaultMenuItem("View as Table with Fragments");
      item.setIcon("fa-table");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListFragmentsTable?faces-redirect=true");
      actLisNav.addElement(item);

      item = new DefaultMenuItem("View as Form");
      item.setIcon("fa-square-o");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListForm?faces-redirect=true");
      actLisNav.addElement(item);

      item = new DefaultMenuItem("View As Grid");
      item.setIcon("fa-th");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListGrid?faces-redirect=true");
      actLisNav.addElement(item);

      item = new DefaultMenuItem("View as Grid with Fragments");
      item.setIcon("fa-th");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListFragmentsGrid?faces-redirect=true");
      actLisNav.addElement(item);

      item = new DefaultMenuItem("Histograms and ScatterPlots");
      item.setIcon("fa-bar-chart");
      item.setAjax(false);
      item.setOutcome("/webpages/activeListHistograms?faces-redirect=true");
      actLisNav.addElement(item);

      model.addElement(actLisNav);

      // top-level logout
      item = new DefaultMenuItem("Logout");
      item.setCommand("#{sessionController.handleLogout}");
      model.addElement(item);

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
        if (dsm.getLabel().equals("datSysNav")) {
          System.out.println("This is datSysNav");
        } else if (dsm.getLabel().equals("actLisNav")) {
          System.out.println("This is actLisNav");
        } else {
          System.out.println("This is some other submenu");
          System.out.println(dsm.getLabel());
        }

      }

    }

  }

}
