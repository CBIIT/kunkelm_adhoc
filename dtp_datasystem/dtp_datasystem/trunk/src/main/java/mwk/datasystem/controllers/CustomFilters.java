/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import mwk.datasystem.vo.CmpdAliasVO;

@ManagedBean
@ApplicationScoped
public class CustomFilters implements Serializable {

    private static final Logger LOG = Logger.getLogger(CustomFilters.class.getName());

     public boolean filterByAlias(Object value, Object filter, Locale locale) {
        String filterText = (filter == null) ? null : filter.toString().trim();
        if(filterText == null||filterText.equals("")) {
            return true;
        }
         
        if(value == null) {
            return false;
        }
         
        // return ((Comparable) value).compareTo(Integer.valueOf(filterText)) > 0;
        
        Collection aliases = (Collection)value;
        
        boolean found = false;
        
        Iterator itr = aliases.iterator();
        while (itr.hasNext()){
            String s = (String)itr.next();
            if (s.contains(filterText)){
                found = true;
                break;
            }
        }
        
        return found;
        
    }
}
