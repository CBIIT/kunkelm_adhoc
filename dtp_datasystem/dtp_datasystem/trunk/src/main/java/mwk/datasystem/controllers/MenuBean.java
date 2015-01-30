/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import org.primefaces.model.menu.DefaultMenuItem;
import org.primefaces.model.menu.DefaultMenuModel;
import org.primefaces.model.menu.DefaultSubMenu;
import org.primefaces.model.menu.MenuModel;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class MenuBean {

    private MenuModel model;

    public MenuBean() {

        try {

            model = new DefaultMenuModel();

            //First submenu
            DefaultSubMenu firstSubmenu = new DefaultSubMenu("Data System Navigation");

            DefaultMenuItem item = new DefaultMenuItem();

            item = new DefaultMenuItem("Available Lists");
            item.setAjax(false);
            item.setOutcome("/webpages/availableLists?faces-redirect=true");
            firstSubmenu.addElement(item);

            item = new DefaultMenuItem("Active List");
            item.setAjax(false);
            item.setOutcome("/webpages/activeListTable?faces-redirect=true");
            firstSubmenu.addElement(item);

            item = new DefaultMenuItem("Create List");
            item.setAjax(false);
            item.setOutcome("/webpages/createList?faces-redirect=true");
            firstSubmenu.addElement(item);

            item = new DefaultMenuItem("Upload List");
            item.setAjax(false);
            item.setOutcome("/webpages/uploadList?faces-redirect=true");
            firstSubmenu.addElement(item);

            item = new DefaultMenuItem("List Logic");
            item.setAjax(false);
            item.setOutcome("/webpages/listLogic?faces-redirect=true");
            firstSubmenu.addElement(item);

            item = new DefaultMenuItem("chemDoodle");
            item.setAjax(false);
            item.setOutcome("/webpages/chemDoodle?faces-redirect=true");
            firstSubmenu.addElement(item);
            
//            <p
//            :menuitem ajax = "true" 
//            action = "#{saltController.performListAllSalts()}"
//            value = "Salts Workbench" /
//                    > <p
//            :menuitem ajax = "false"
//            action = "#{sessionController.logout()}"
//            value = "Logout" /
//                    > 
            
            model.addElement(firstSubmenu);

            //Second submenu
            DefaultSubMenu secondSubmenu = new DefaultSubMenu("Dynamic Actions");

            item = new DefaultMenuItem("Save");
            item.setIcon("ui-icon-disk");
            item.setCommand("#{menuBean.save}");
            item.setUpdate("msgs");
            secondSubmenu.addElement(item);

            item = new DefaultMenuItem("Delete");
            item.setIcon("ui-icon-close");
            item.setCommand("#{menuBean.delete}");
            item.setAjax(false);
            secondSubmenu.addElement(item);

            item = new DefaultMenuItem("Redirect");
            item.setIcon("ui-icon-search");
            item.setCommand("#{menuBean.redirect}");
            secondSubmenu.addElement(item);

            model.addElement(secondSubmenu);

        } catch (Exception e) {
            System.out.println("Exception in MenuBean");
            e.printStackTrace();
        }

    }

    public MenuModel getModel() {
        return model;
    }
}
