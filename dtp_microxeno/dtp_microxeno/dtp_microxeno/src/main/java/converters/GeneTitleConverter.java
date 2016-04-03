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

/**
 *
 * @author mwkunkel
 */
@FacesConverter("geneTitleConverter")
public class GeneTitleConverter implements Converter {

    @Override
    public Object getAsObject(FacesContext context, UIComponent component, String value) {
        
        AffymetrixIdentifierVO rtn = null;
        
        ApplicationScopeBean asb = (ApplicationScopeBean) context.getExternalContext().getApplicationMap().get("applicationScopeBean");
        for (AffymetrixIdentifierVO aiVO : asb.getAffyIdentList()) {
            if (aiVO.getGeneTitle().equals(value)) {
                rtn = aiVO;
            }
        }
        
        return rtn;
        
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object value) {
        
        String rtn = null;
        
        if (value instanceof AffymetrixIdentifierVO) {
            AffymetrixIdentifierVO aiVO = (AffymetrixIdentifierVO) value;
            rtn = aiVO.getGeneTitle();
        }
        
        return rtn;
        
    }

}
