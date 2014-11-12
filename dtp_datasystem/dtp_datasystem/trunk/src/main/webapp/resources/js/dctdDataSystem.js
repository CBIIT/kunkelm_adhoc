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
  //return plot.series[seriesIndex]["label"] + ", " + plot.data[seriesIndex][pointIndex];
  return plot.series[seriesIndex]["label"];

}

function histogramExtender() {
  this.cfg.seriesColors = ["red", "blue"];
}

function scatterPlotExtender() {
  this.cfg.seriesColors = ["blue", "red"];
  this.cfg.seriesDefaults = {
    showLine: false,
    showMarker: true
  };
}