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
import mwk.datasystem.vo.CuratedNameVO;

/**
 *
 * @author mwkunkel
 */
@SessionScoped
@ManagedBean(name = "curatedNameConverter")
@FacesConverter("curatedNameConverter")
public class CuratedNameConverter implements Converter, Serializable {

    static final long serialVersionUID = -8653468638698142855l;
// reach-through to curatedNscController

    @ManagedProperty(value = "#{curatedNscController}")
    private CuratedNscController curatedNscController;

    public void setCuratedNscController(CuratedNscController curatedNscController) {
        this.curatedNscController = curatedNscController;
    }

    @Override
    public Object getAsObject(FacesContext facesContext, UIComponent component, String stringIn) throws ConverterException {

        CuratedNameVO rtn = null;

        if (stringIn != null && stringIn.trim().length() > 0) {

            try {
                if (curatedNscController.getKnownNamesMap().containsKey(stringIn)) {
                    rtn = curatedNscController.getKnownNamesMap().get(stringIn);
                } else {
                    FacesMessage errMsg = new FacesMessage(CONVERSION_ERROR_MESSAGE_ID);
                    FacesContext.getCurrentInstance().addMessage(null, errMsg);
                    throw new ConverterException(errMsg.getSummary());
                }
            } catch (Exception exception) {
                exception.printStackTrace();
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid CuratedNameVO."));
            }
        }

        return rtn;
    }

    @Override
    public String getAsString(FacesContext context, UIComponent component, Object modelValue) throws ConverterException {
        String rtn = null;
        if (modelValue != null && modelValue instanceof CuratedNameVO) {
            rtn = ((CuratedNameVO) modelValue).getValue();
        } else {
            FacesMessage errMsg = new FacesMessage(CONVERSION_ERROR_MESSAGE_ID);
            FacesContext.getCurrentInstance().addMessage(null, errMsg);
            throw new ConverterException(errMsg.getSummary());
        }
        return rtn;
    }
}
