function barChartTooltipContentEditor(str, seriesIndex, pointIndex, plot) {
  return plot.axes.yaxis.ticks[pointIndex];
}

function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {
  // display series_label, x-axis_tick, y-axis value
  //return plot.series[seriesIndex]["label"] + ", " + plot.data[seriesIndex][pointIndex];
  return plot.series[seriesIndex]["label"];
    
}

function histogramExtender() {
  
  this.cfg.seriesColors = ["red","blue"];
  
}