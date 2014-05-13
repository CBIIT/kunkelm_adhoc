// jChemPaint functions

function showMolFile() {
    var jcped = document.JChemPaintEditor;
    var molFile = jcped.getMolFile();
    document.getElementById('form508:molfile').value = molFile;
}

function showSmilesChiral() {
    var jcped = document.JChemPaintEditor;
    var smiles = jcped.getSmilesChiral();
    document.getElementById('form508:chiralsmilesstring').value = smiles;
}

function showSmiles() {
    var jcped = document.JChemPaintEditor;
    var smiles = jcped.getSmiles();
    document.getElementById('form508:smilesstring').value = smiles;
}

/*     
             Reading from the applet
             getMolFile()
             getSmiles()
             getSmilesChiral()
             getParameter()
             getParameterInfo()
             getAppletInfo()
             getLocale()
             getImage()
             getTheJcpp()
       
             Writing to the applet
             setMolFile()
             setMolFileWithReplace()
             addMolFileWithReplace()
             setSmiles()
             loadModelFromUrl()
             loadModelFromSmiles()
             clear()
             selectAtom()
             makeHydrogensExplicit()
             */

function loadFromSmiles() {
    
    var jcped = document.JChemPaintEditor;

    var smiles = document.getElementById('form508:smilesforload').value;
    jcped.clear();
    if (smiles) {
        jcped.loadModelFromSmiles(smiles);
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
    this.cfg.seriesColors = ["red","blue"];  
}

function scatterPlotExtender(){
    this.cfg.seriesColors = ["blue","red"];      
    this.cfg.seriesDefaults = {
        showLine: false,
        showMarker: true
    };
}