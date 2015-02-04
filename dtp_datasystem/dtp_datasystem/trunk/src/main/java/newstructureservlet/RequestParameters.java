/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package newstructureservlet;

import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author mwkunkel
 */
public class RequestParameters {

  private Map<String, String[]> reqParams;

  public RequestParameters(HttpServletRequest req) {
    this.reqParams = req.getParameterMap();
  }

  public Boolean hasParam(String paramName) {
    Boolean rtn = Boolean.FALSE;
    if (reqParams.get(paramName) != null) {
      rtn = Boolean.TRUE;
    }
    return rtn;
  }

  public String getParamValue(String paramName, String defaultValue) {
    String rtn = null;
    if (reqParams.get(paramName) != null) {
      String[] tempArray = reqParams.get(paramName);
      if (tempArray.length > 0) {
        // Exception
      } else {
        rtn = tempArray[0];
      }
    } else {
      rtn = defaultValue;
    }
    return rtn;
  }

}
