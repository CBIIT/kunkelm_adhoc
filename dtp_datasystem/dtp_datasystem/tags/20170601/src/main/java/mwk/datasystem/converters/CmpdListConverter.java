/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.converters;

import static com.sun.faces.util.MessageUtils.CONVERSION_ERROR_MESSAGE_ID;
import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import javax.faces.convert.FacesConverter;
import mwk.datasystem.controllers.CuratedNscController;
import mwk.datasystem.controllers.ListManagerController;
import mwk.datasystem.vo.CmpdListVO;

/**
 *
 * @author mwkunkel
 */
@SessionScoped
@ManagedBean(name = "cmpdListConverter")
@FacesConverter("cmpdListConverter")
public class CmpdListConverter implements Converter, Serializable {

    static final long serialVersionUID = -8653468638698142855l;

    // reach-through to listManagerController
    @ManagedProperty(value = "#{listManagerController}")
    private ListManagerController listManagerController;

    public void setListManagerController(ListManagerController listManagerController) {
        this.listManagerController = listManagerController;
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String stringIn) throws ConverterException {

        CmpdListVO rtn = null;

        if (stringIn != null && stringIn.trim().length() > 0) {

            try {

                for (CmpdListVO clVO : listManagerController.getListManagerBean().getAvailableLists()) {
                    if (clVO.getCmpdListId().toString().equals(stringIn)) {
                        rtn = clVO;
                        break;
                    }
                }

                if (rtn != null) {
                    
                } else {
                    FacesMessage errMsg = new FacesMessage(CONVERSION_ERROR_MESSAGE_ID);
                    FacesContext.getCurrentInstance().addMessage(null, errMsg);
                    throw new ConverterException(errMsg.getSummary());
                }
                
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error: getAsObject", "Not a valid CmpdListVO."));
            }
        }

        return rtn;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) throws ConverterException {
        
        String rtn = "";
        
        if (modelValue == null){
            
        } else if (modelValue instanceof CmpdListVO) {
            rtn = ((CmpdListVO) modelValue).getCmpdListId().toString();
        } else {
            FacesMessage errMsg = new FacesMessage(CONVERSION_ERROR_MESSAGE_ID);
            FacesContext.getCurrentInstance().addMessage(null, errMsg);
            throw new ConverterException(errMsg.getSummary());
        }
        
        return rtn;
        
    }
}
