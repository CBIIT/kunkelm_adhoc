/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.net.URLEncoder;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class ApplicationScopeBean {

    public static String urlEncode(String in) {
        String rtn = "";
        try {
            rtn = URLEncoder.encode(in, "UTF-8");
        } catch (Exception e) {
        }
        return rtn;
    }
}
