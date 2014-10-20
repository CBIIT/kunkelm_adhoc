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

        Object rtn = new CmpdListVO();

        if (submittedValue.trim().equals("")) {

            //

        } else {
            
            try {
              
                // THIS COULD FAIL FOR LISTS WITH THE SAME NAME
                // SINCE IT RETURNS THE FIRST MATCH

                for (CmpdListVO clVO : this.listManagerController.getAvailableLists()) {
                    if (clVO.getListName().equals(submittedValue)) {
                        rtn = clVO;
                        break;
                    }
                }

            } catch (Exception exception) {
                exception.printStackTrace();
            }
        }

        return rtn;
    }

    public String getAsString(FacesContext context, UIComponent component, Object modelValue) {
         
        String rtn = "";

        if (modelValue == null) {
            
        } else if (modelValue instanceof CmpdListVO) {
            rtn = ((CmpdListVO) modelValue).getListName();
        } else {
            System.out.println("Conversion Error: Not a valid CmpdListVO instance");
        }

        return rtn;
    }
}
