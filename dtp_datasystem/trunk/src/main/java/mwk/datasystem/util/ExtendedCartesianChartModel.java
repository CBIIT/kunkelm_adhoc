/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mwk.datasystem.util;

import java.io.Serializable;
import org.primefaces.model.chart.CartesianChartModel;

/**
 *
 * @author mwkunkel
 */
public class ExtendedCartesianChartModel extends CartesianChartModel implements Serializable {

  private String title;
  private String color;

  public ExtendedCartesianChartModel(ExtendedCartesianChartModel incoming) {
    this.title = "notSet";
    this.color = "black";
  }

  public ExtendedCartesianChartModel() {
    super();
    this.title = "notSet";
    this.color = "black";
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getColor() {
    return color;
  }

  public void setColor(String color) {
    this.color = color;
  }
}