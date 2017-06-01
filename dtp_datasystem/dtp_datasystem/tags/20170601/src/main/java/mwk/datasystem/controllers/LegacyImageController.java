/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.util.ArrayList;
import java.util.List;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

/**
 *
 * @author mwkunkel
 */
@ManagedBean
@SessionScoped
public class LegacyImageController {

    String nscTextArea;
    List<Integer> nscIntList;

    public String performLegacyImages() {

        nscIntList = new ArrayList<Integer>();

        String[] splitStrings = null;
        String fixedString = null;

        String delimiters = "[\\s\\n\\r\\t,]+";

        if (nscTextArea == null || nscTextArea.isEmpty()) {
            System.out.println("nscTextArea is null or isEmpty");
        } else {
            splitStrings = nscTextArea.split(delimiters);
            for (int i = 0; i < splitStrings.length; i++) {
                fixedString = splitStrings[i].replaceAll("[^0-9]", "");
                if (fixedString.length() > 0) {
                    try {
                        nscIntList.add(Integer.valueOf(fixedString));
                    } catch (Exception e) {
                        
                    }
                }
            }
        }

        return "/webpages/legacyImages?faces-redirect=true";

    }

    public String getNscTextArea() {
        return nscTextArea;
    }

    public void setNscTextArea(String nscTextArea) {
        this.nscTextArea = nscTextArea;
    }

    public List<Integer> getNscIntList() {
        return nscIntList;
    }

    public void setNscIntList(List<Integer> nscIntList) {
        this.nscIntList = nscIntList;
    }
    
}
