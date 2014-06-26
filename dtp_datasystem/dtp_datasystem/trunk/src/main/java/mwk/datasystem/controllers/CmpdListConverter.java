/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import java.io.Serializable;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.ConverterException;
import mwk.datasystem.vo.CmpdListVO;

/**
 *
 * @author mwkunkel
 */
//@FacesConverter(value = "cmpdListConverter")

@ManagedBean
@SessionScoped
public class CmpdListConverter implements Converter, Serializable {

    // reach-through to listManagerController
    @ManagedProperty(value = "#{listManagerController}")
    private ListManagerController listManagerController;

    public void setListManagerController(ListManagerController listManagerController) {
        this.listManagerController = listManagerController;
    }

    public Object getAsObject(FacesContext facesContext, UIComponent component, String submittedValue) {

        Object rtn = null;

        if (submittedValue.trim().equals("")) {

            return null;

        } else {
            try {

                long number = Long.valueOf(submittedValue).longValue();

                for (CmpdListVO clVO : this.listManagerController.getAvailableLists()) {
                    if (clVO.getCmpdListId() == number) {
                        rtn = clVO;
                    }
                }

            } catch (NumberFormatException exception) {
                exception.printStackTrace();
                throw new ConverterException(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Conversion Error", "Not a valid player"));
            }
        }

        return rtn;
    }

    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
         
        String rtn = "";

        if (modelValue == null) {
            
        } else if (modelValue instanceof CmpdListVO) {
            rtn = String.valueOf(((CmpdListVO) modelValue).getCmpdListId());
        } else {
            System.out.println("Conversion Error: Not a valid CmpdListVO instance");
        }

        return rtn;
    }
}
