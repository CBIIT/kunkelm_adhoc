/*
 
 
 
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import javax.faces.bean.ApplicationScoped;
import javax.faces.bean.ManagedBean;
import org.primefaces.component.datatable.DataTable;

@ManagedBean
@ApplicationScoped
public class CustomFilter implements Serializable {

    private static final Logger LOG = Logger.getLogger(CustomFilter.class.getName());

    public boolean customFilter(Object value, Object filter, Locale locale) {

        boolean rtn = false;
        
        String filterText = (filter == null) ? null : filter.toString().trim();

        if (filterText == null || filterText.equals("")) {
            rtn = true;
        }

        if (value == null) {
            
            rtn = false;
            
        } else if (value instanceof Collection) {

            Collection coll = (Collection) value;

            Iterator itr = coll.iterator();
            while (itr.hasNext()) {
                String str = (String) itr.next();
                if (str.contains(filterText)) {
                    rtn = true;
                    break;
                }
            }

        } else if (value instanceof String) {

            String str = (String) value;
            rtn = (str.contains(filterText));

        }

        return rtn;

    }
}
