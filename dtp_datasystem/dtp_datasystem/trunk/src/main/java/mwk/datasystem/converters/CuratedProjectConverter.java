/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.converters;

import static com.sun.faces.util.MessageUtils.CONVERSION_ERROR_MESSAGE_ID;
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
import mwk.datasystem.vo.CuratedProjectVO;

/**
 *
 * @author mwkunkel
 */
@SessionScoped
@ManagedBean(name = "curatedProjectConverter")
@FacesConverter("curatedProjectConverter")
public class CuratedProjectConverter implements Converter {
// reach-through to curatedNscController

    @ManagedProperty(value = "#{curatedNscController}")
    private CuratedNscController curatedNscController;

    public void setCuratedNscController(CuratedNscController curatedNscController) {
        this.curatedNscController = curatedNscController;
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) throws ConverterException {
        CuratedProjectVO rtn = null;
        if (submittedValue != null && submittedValue.trim().length() > 0) {
            try {
                if (curatedNscController.getKnownProjectsMap().containsKey(submittedValue)) {
                    rtn = curatedNscController.getKnownProjectsMap().get(submittedValue);
                } else {
                    FacesMessage errMsg = new FacesMessage(CONVERSION_ERROR_MESSAGE_ID);
                    FacesContext.getCurrentInstance().addMessage(null, errMsg);
                    throw new ConverterException(errMsg.getSummary());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid CuratedProjectVO."));
            }
        }
        return rtn;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) throws ConverterException {
        String rtn = null;
        if (modelValue != null && modelValue instanceof CuratedProjectVO) {
            rtn = ((CuratedProjectVO) modelValue).getValue();
        } else {
            FacesMessage errMsg = new FacesMessage(CONVERSION_ERROR_MESSAGE_ID);
            FacesContext.getCurrentInstance().addMessage(null, errMsg);
            throw new ConverterException(errMsg.getSummary());
        }
        return rtn;
    }
}
