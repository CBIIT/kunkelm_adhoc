/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.controllers;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.MethodExpression;
import javax.el.ValueExpression;
import javax.faces.FacesException;
import javax.faces.context.FacesContext;
import javax.faces.event.MethodExpressionActionListener;

/**
 *
 * @author mwkunkel based on code from
 * http://www.javacodegeeks.com/2012/04/5-useful-methods-jsf-developers-should.html
 */
public class FacesAccessor {

  public static Object getManagedBean(final String beanName) {
    FacesContext fc = FacesContext.getCurrentInstance();
    Object bean;

    try {
      ELContext elContext = fc.getELContext();
      bean = elContext.getELResolver().getValue(elContext, null, beanName);
    } catch (RuntimeException e) {
      throw new FacesException(e.getMessage(), e);
    }

    if (bean == null) {
      throw new FacesException("Managed bean with name '" + beanName
              + "' was not found. Check your faces-config.xml or @ManagedBean annotation.");
    }

    return bean;
  }

  public static void setValue2ValueExpression(final Object value, final String expression) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ELContext elContext = facesContext.getELContext();

    ValueExpression targetExpression
            = facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, expression, Object.class);
    targetExpression.setValue(elContext, value);
  }

  public static void mapVariable2ValueExpression(final String variable, final String expression) {
    FacesContext facesContext = FacesContext.getCurrentInstance();
    ELContext elContext = facesContext.getELContext();

    ValueExpression targetExpression
            = facesContext.getApplication().getExpressionFactory().createValueExpression(elContext, expression, Object.class);
    elContext.getVariableMapper().setVariable(variable, targetExpression);
  }

  public static MethodExpression createMethodExpression(String valueExpression,
          Class<?> expectedReturnType,
          Class<?>[] expectedParamTypes) {
    MethodExpression methodExpression = null;
    try {
      FacesContext fc = FacesContext.getCurrentInstance();
      ExpressionFactory factory = fc.getApplication().getExpressionFactory();
      methodExpression = factory.
              createMethodExpression(fc.getELContext(), valueExpression, expectedReturnType, expectedParamTypes);
    } catch (Exception e) {
      throw new FacesException("Method expression '" + valueExpression + "' could not be created.");
    }

    return methodExpression;
  }

  public static MethodExpressionActionListener createMethodActionListener(String valueExpression,
          Class<?> expectedReturnType,
          Class<?>[] expectedParamTypes) {
    MethodExpressionActionListener actionListener = null;
    try {
      actionListener = new MethodExpressionActionListener(createMethodExpression(
              valueExpression, expectedReturnType, expectedParamTypes));
    } catch (Exception e) {
      throw new FacesException("Method expression for ActionListener '" + valueExpression
              + "' could not be created.");
    }

    return actionListener;
  }

}
