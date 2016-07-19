function dynamicLinkColor(tanimotoScore) {

    var lowCut = 0.6;
    var midCut = 0.8;
    var highCut = 1;

    var redPart = 255;
    var greenPart = 255;
    var bluePart = 255;

    if (tanimotoScore == null) {
        // default return is 255, 255, 255
    } else if (String(tanimotoScore).substring(0, 4) === 'NULL') {
    	    
        redPart = 0;
        greenPart = 0;
        bluePart = 0;

    } else {

        var dVal = Number(tanimotoScore);

        if (dVal == null) {
        } else {

            if (dVal < lowCut) {
                dVal = lowCut;
            }
            if (dVal > highCut) {
                dVal = highCut;
            }
            if (dVal > midCut) {
                var fracDist = (dVal - midCut) / (highCut - midCut);
                redPart = 255;
                greenPart = 255 - Math.round(255 * fracDist);
                bluePart = 0;
            } else {
                var fracDist = (dVal - lowCut) / (midCut - lowCut);
                redPart = Math.round(255 * fracDist);
                greenPart = 255;
                bluePart = 0;
            }
        }
    }

    return "rgb(" + redPart + "," + greenPart + "," + bluePart + ")";
}



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
        if (propertyName.lastIndexOf('widgetHistogramChart', 0) === 0) {
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
