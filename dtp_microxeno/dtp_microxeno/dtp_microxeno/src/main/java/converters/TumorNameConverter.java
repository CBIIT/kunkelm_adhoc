/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package converters;

import controllers.ApplicationScopeBean;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import mwk.microxeno.vo.AffymetrixIdentifierVO;
import mwk.microxeno.vo.TumorVO;

/**
 *
 * @author mwkunkel
 */
@FacesConverter("tumorNameConverter")
public class TumorNameConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {

        System.out.println("In TumorNameConverter.getAsObject with value: " + value);
        
        TumorVO rtn = null;

        ApplicationScopeBean asb = (ApplicationScopeBean) context.getExternalContext().getApplicationMap().get("applicationScopeBean");
        
        for (TumorVO tVO : asb.getTumorList()) {
            if (tVO.getTumorName().equals(value)) {
                rtn = tVO;
            }
        }

        return rtn;

    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {

        String rtn = null;

        if (value instanceof TumorVO) {
            TumorVO tVO = (TumorVO) value;
            rtn = tVO.getTumorName();
        }

        return rtn;
    }

}
