function exportChart(widget) {
  console.log("In exportChart.");
  console.log("widget is: " + widget);
  console.log("Calling enumerateWidgets()");
  enumerateWidgets();
  $("[id='exportableImageOutputPanel']").empty();
  $("[id='exportableImageOutputPanel']").append(widget.exportAsImage());
  PF('exportImageDlg').show();
}

function inspectWidgets() {
  for (var propertyName in PrimeFaces.widgets) {
    if (PrimeFaces.widgets[propertyName] instanceof PrimeFaces.widget.SelectBooleanCheckbox) {
      PrimeFaces.widgets[propertyName].check();// to check       
      PrimeFaces.widgets[propertyName].jq; //jQuery reference
    }
  }
}

function enumerateWidgets() {
  for (var propertyName in PrimeFaces.widgets) {    
    console.log("Found widget: " + propertyName); 
    if (propertyName.lastIndexOf('widgetHistogramChart', 0) === 0){
      console.log(propertyName + ' is a histogramChart ------------------');
    }
    console.log(PrimeFaces.widgets[propertyName]);
  }    
}


// chemDoodle functions

function getMolFromEditor() {

  //alert('in getMolFromEditor()');

  var mol = sketcher.getMolecule();
  var stringMol = ChemDoodle.writeMOL(mol);
  document.getElementById('datasystemForm:ctabfromeditor').value = stringMol;

}

function loadEditorFromMol() {

  //alert('in loadEditorFromMol()');

  var ctab = document.getElementById('datasystemForm:ctabforload').value;

  //replace car rtn with \n
  var replStr = String.fromCharCode(92, 110);
  var fixed = ctab.replace(/[\n\r|\r|\n]/g, '\n');

  // have to prepend a line for ChemDoodle molecule from mol file
  var prepended = 'prepended line\n' + fixed;

  var mol = ChemDoodle.readMOL(prepended);

  if (mol) {
    sketcher.loadMolecule(mol);
  } else {
    alert('NSC not found or no parent fragment defined.');
  }

}

function barChartTooltipContentEditor(str, seriesIndex, pointIndex, plot) {
  return plot.axes.yaxis.ticks[pointIndex];
}

function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {
  // display series_label, x-axis_tick, y-axis value
  return plot.series[seriesIndex]["label"] + ", " + plot.data[seriesIndex][pointIndex];
  // return plot.series[seriesIndex]["label"];

}

function barChartTooltipContentEditor(str, seriesIndex, pointIndex, plot) {
  return plot.axes.yaxis.ticks[pointIndex];
}

function histogramExtender() {
  this.cfg.seriesColors = ["red", "blue"];
//  this.cfg.highlighter = {
//    show: true,
//    sizeAdjust: 25,
//    //lineWidthAdjust: 25,
//    tooltipLocation: 'e',
//    bringSeriesToFront: true,
//    //useAxesFormatters: true,
//    tooltipContentEditor: barChartTooltipContentEditor,
//    //tooltipAxes: 'y',
//    showMarker: true
//  };

}

function scatterPlotExtender() {
  this.cfg.seriesColors = ["blue", "red"];
  this.cfg.seriesDefaults = {
    showLine: false,
    showMarker: true
  };
  this.cfg.highlighter = {
    show: true,
    sizeAdjust: 25,
    //lineWidthAdjust: 25,
    tooltipLocation: 'e',
    bringSeriesToFront: true,
    //useAxesFormatters: true,
    tooltipContentEditor: tooltipContentEditor,
    //tooltipAxes: 'y',
    showMarker: true
  };
}