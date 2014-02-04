/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.InputStream;
import java.net.URLEncoder;
import java.util.Properties;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@ApplicationScoped
public class ApplicationScopeBean {

    private String structureServletUrl;

    public String getStructureServletUrl() {
        return structureServletUrl;
    }

    public ApplicationScopeBean() {
        Properties props = new Properties();
        try {
            InputStream is = this.getClass().getResourceAsStream("/deployment.properties");
            props.load(is);
            this.structureServletUrl = props.getProperty("structure.servlet.url");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String urlEncode(String in) {
        String rtn = "";
        try {
            rtn = URLEncoder.encode(in, "UTF-8");
        } catch (Exception e) {
        }
        return rtn;
    }
}
