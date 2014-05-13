/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class SessionController508 implements Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    private String loggedUser;
    
    public SessionController508() {
        this.loggedUser = FacesContext.getCurrentInstance().getExternalContext().getRemoteUser();
    }
    
    
    public void logout(ActionEvent actionEvent) {
        System.out.println("Now in logout in SessionController508");
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        request.getSession().invalidate();
    }

    public String getLoggedUser() {
        return loggedUser;
    }

}
