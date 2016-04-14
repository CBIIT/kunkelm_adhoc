var myPopupWindow = '';

//onclick="openPopupWindow('http://www.wordpress.com','WP', 450, 600); return false;"

function openPopupWindow(url, name, width, height)
{
    //Remove special characters from name
    name = name.replace(/\/|\-|\./gi, "");

    //Remove whitespaces from name
    var whitespace = new RegExp("\\s", "g");
    name = name.replace(whitespace, "");

    //If it is already open
    if (!myPopupWindow.closed && myPopupWindow.location)
    {
        myPopupWindow.location.href = encodeUrl(url);
    }
    else
    {
        myPopupWindow = window.open(encodeUrl(url), name, "location=no, scrollbars=yes, resizable=yes, toolbar=no, menubar=no, width=" + width + ", height=" + height);
        if (!myPopupWindow.opener)
            myPopupWindow.opener = self;
    }

    //If my main window has focus - set it to the popup
    if (window.focus) {
        myPopupWindow.focus()
    }

}

function encodeUrl(url)
{
    if (url.indexOf("?") > 0)
    {
        encodedParams = "?";
        parts = url.split("?");
        params = parts[1].split("&");
        for (i = 0; i < params.length; i++)
        {
            if (i > 0)
            {
                encodedParams += "&";
            }
            if (params[i].indexOf("=") > 0) //Avoid null values
            {
                p = params[i].split("=");
                encodedParams += (p[0] + "=" + escape(encodeURI(p[1])));
            }
            else
            {
                encodedParams += params[i];
            }
        }
        url = parts[0] + encodedParams;
    }
    return url;
}

function tooltipContentEditor(str, seriesIndex, pointIndex, plot) {
    // display series_label, x-axis_tick, y-axis value
    //return plot.series[seriesIndex]["label"] + ", " + plot.data[seriesIndex][pointIndex];
    return plot.series[seriesIndex]["label"];

}

function distributionPlotExtender() {
    this.cfg.axes = {
        xaxis: {
            label: "Rank",
            labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
            tickInterval: 10,
            min: 0
        }
    },
    this.cfg.seriesDefaults = {
        showLine: true,
        showMarker: false,
        shadow: false,
        rendererOptions: {
            smooth: true
        }
    }
}

function allPlotChartExtender() {
    
    console.log('this.cfg in allPlotChartExtender in sarcomaJavaScript.js');
    console.log(this.cfg);

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

    var titleString = this.cfg.title;

    this.cfg.title = {
        text: titleString,
        textColor: 'black'
    };

    // labelOptions textColor to black

    this.cfg.axes.xaxis.labelOptions = {
        color: 'black',
        textColor: 'black'
    };

    this.cfg.axes.yaxis.labelOptions = {
        color: 'black',
        textColor: 'black'
    };

    // tickOptions textColor to black

    this.cfg.axes.xaxis.tickOptions = {
        color: 'black',
        textColor: 'black',
        angle: -90
    };

    this.cfg.axes.yaxis.tickOptions = {
        color: 'black',
        textColor: 'black'
    };

    this.cfg.legend = {
        renderer: $.jqplot.EnhancedLegendRenderer,
        show: true,
        location: 's',
        yoffset: 75,
        fontSize: '6pt',
        placement: 'outside',
        showSwatches: true,
        showLabels: true,
        rendererOptions: {
            showMarkerStyle: true,
            showLineStyle: true,
            numberColumns: 5
        }
    };
    
    var serDflt = this.cfg.seriesDefaults;
    serDflt.lineWidth=1;
    
    var seriesArray = this.cfg.series;

    for (var i = 0; i < seriesArray.length; i++) {
    	    
    	    var mrkrOptions = seriesArray[i].markerOptions;
    	    mrkrOptions.lineWidth=2;
    	    mrkrOptions.size=6;
    } 

}

function concRespChartExtender() {

    console.log(this.cfg);

    var colorArray = ["#4bb2c5", "#EAA228", "#c5b47f", "#579575", "#839557", "#958c12", "#953579", "#4b5de4", "#d8b83f", "#ff5800", "#0085cc", "#c747a3", "#cddf54", "#FBD178", "#26B4E3", "#bd70c7"];
    var colorCnt = -1;

    // if the series label starts with "ExpId Curve", or AvgExpId: don't show markers
    var seriesArray = this.cfg.series;

    for (var i = 0; i < seriesArray.length; i++) {
        //console.log(seriesArray[i]);      
        if (seriesArray[i].label.substring(0, 11) == "ExpId Curve" || seriesArray[i].label.substring(0, 8) == "AvgExpId") {
            seriesArray[i].markerOptions.show = false;
            seriesArray[i].markerOptions.showMarker = false;
        }
        // set color by pairs, increment counter if odd entry since the data come in points, curve, points, curve, etc.
        if (i % 2 == 0) {
            colorCnt++;
        }
        seriesArray[i].color = colorArray[colorCnt];
        // finally, if it was the AvgExpId curve, set it to black
        if (seriesArray[i].label.substring(0, 8) == "AvgExpId") {
            seriesArray[i].color = 'black';
        }
    }

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

    //this.cfg.cursor = {
    //    show: true,
    //    zoom: true,
    //    tooltipLocation:'e'
    //}

    this.cfg.axes = {
        xaxis: {
            label: "log Concentration (M)"
        },
        yaxis: {
            label: "Mean Percent of Control",
            labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
            min: 0,
            //max: 125,
            tickInterval: 10
        }
    };

    var titleString = this.cfg.title;

    this.cfg.title = {
        text: titleString,
        textColor: 'black'
    };

    // labelOptions textColor to black

    this.cfg.axes.xaxis.labelOptions = {
        color: 'black',
        textColor: 'black'
    };

    this.cfg.axes.yaxis.labelOptions = {
        color: 'black',
        textColor: 'black'
    };

    // tickOptions textColor to black

    this.cfg.axes.xaxis.tickOptions = {
        color: 'black',
        textColor: 'black',
        angle: -90
    };

    this.cfg.axes.yaxis.tickOptions = {
        color: 'black',
        textColor: 'black'
    };

}

function cumulativePlotChartExtender() {
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
    },
    //this.cfg.cursor = {
    //   show: true,
    //    tooltipLocation:'e'
    //}
    this.cfg.axes = {
        xaxis: {
            label: "log Concentration (M)"
        },
        yaxis: {
            label: "Mean Percent of Control",
            labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
            min: 0,
            //max: 125,
            tickInterval: 10
        }
    },
    this.cfg.legend = {
        renderer: $.jqplot.EnhancedLegendRenderer,
        show: true,
        location: 's',
        yoffset: 75,
        fontSize: '8pt',
        placement: 'outside',
        showSwatches: true,
        showLabels: true,
        showMarkerStyle: true,
        showLineStyle: true,
        rendererOptions: {
            numberColumns: 2
        }
    }

}

function panelPlotChartExtender() {

    // console.log(this);

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
    //this.cfg.cursor = {
    //   show: true,
    //    tooltipLocation:'e'
    //}
    this.cfg.axes = {
        xaxis: {
            label: "log Concentration (M)"
        },
        yaxis: {
            label: "Mean Percent of Control",
            labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
            min: 0,
            //max: 125,
            tickInterval: 10
        }
    };

    var titleString = this.cfg.title;

    this.cfg.title = {
        text: titleString,
        textColor: 'black'
    };

    // labelOptions textColor to black

    this.cfg.axes.xaxis.labelOptions = {
        color: 'black',
        textColor: 'black'
    };

    this.cfg.axes.yaxis.labelOptions = {
        color: 'black',
        textColor: 'black'
    };

    // tickOptions textColor to black

    this.cfg.axes.xaxis.tickOptions = {
        color: 'black',
        textColor: 'black',
        angle: -90
    };

    this.cfg.axes.yaxis.tickOptions = {
        color: 'black',
        textColor: 'black'
    };

    this.cfg.legend = {
        renderer: $.jqplot.EnhancedLegendRenderer,
        show: true,
        location: 's',
        yoffset: 75,
        fontSize: '6pt',
        placement: 'outside',
        showSwatches: true,
        showLabels: true,
        showMarkerStyle: true,
        showLineStyle: true,
        rendererOptions: {
            numberColumns: 4
        }
    };

}

function selectedPlotChartExtender() {
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
    },
    this.cfg.axes = {
        xaxis: {
            label: "log Concentration (M)"
        },
        yaxis: {
            label: "Mean Percent of Control",
            labelRenderer: $.jqplot.CanvasAxisLabelRenderer,
            min: 0,
            //max: 125,
            tickInterval: 10
        }
    },
    this.cfg.legend = {
        renderer: $.jqplot.EnhancedLegendRenderer,
        show: true,
        location: 's',
        yoffset: 75,
        fontSize: '6pt',
        placement: 'outside',
        showSwatches: true,
        showLabels: true,
        showMarkerStyle: true,
        showLineStyle: true,
        rendererOptions: {
            numberColumns: 2
        }
    }

}

function barChartTooltipContentEditor(str, seriesIndex, pointIndex, plot) {
    return plot.axes.yaxis.ticks[pointIndex];
}

function forwardMeanGraphChartExtender() {

    //console.log('forwardMeanGraphChartExtender');
    //console.log(this.cfg);

    // title textColor to black

    var titleString = this.cfg.title;

    this.cfg.title = {
        text: titleString,
        textColor: 'black'
    };

    // labelOptions textColor to black

    this.cfg.axes.xaxis.labelOptions = {
        color: 'black',
        textColor: 'black'
    };

    this.cfg.axes.yaxis.labelOptions = {
        color: 'black',
        textColor: 'black'
    };

    // tickOptions textColor to black

    this.cfg.axes.xaxis.tickOptions = {
        color: 'black',
        textColor: 'black',
        angle: -90
    };

    this.cfg.axes.yaxis.tickOptions = {
        color: 'black',
        textColor: 'black'
    };

    // add a line to the canvas if this is a panel divider

    var ticks = this.cfg.ticks;

    var horizLineArray = Array();

    for (serCnt = 0; serCnt < ticks.length; serCnt++) {

        var tickName = ticks[serCnt];

        if (tickName.substring(0, 5) == "PANEL") {
            horizLineArray.push({
                horizontalLine: {
                    color: 'rgba(128,128,128,0.5)',
                    y: serCnt + 1,
                    lineWidth: 5
                }
            });
        }

    }

    this.cfg.highlighter = {
        show: true,
        sizeAdjust: 25,
        //lineWidthAdjust: 25,
        tooltipLocation: 'e',
        bringSeriesToFront: true,
        //useAxesFormatters: true,
        tooltipContentEditor: barChartTooltipContentEditor,
        //tooltipAxes: 'y',
        showMarker: true
    }

    // series colors on-the-fly

    var colorsArray = Array();

    var series = this.cfg.series;

    for (var serCnt = 0; serCnt < series.length; serCnt++) {
        var serLbl = series[serCnt].label;
        if (serLbl.substring(0, 5) == "PANEL") {
            colorsArray.push(colorByPanelName(serLbl.substring(6)));
        }
    }

    this.cfg.seriesColors = colorsArray;

    this.cfg.seriesDefaults = {
        renderer: $.jqplot.BarRenderer,
        pointLabels: {
            show: false
        },
        rendererOptions: {
            barDirection: 'horizontal',
            //fillToZero: true,
            shadow: false,
            barMargin: 1,
            barPadding: 1
        }
    }

    this.cfg.canvasOverlay = {
        name: 'mgCO',
        show: true,
        objects: horizLineArray
    }

}

function exportChart(widgetVar) {
    $("[id='exportableImageOutputPanel']").empty();
    $("[id='exportableImageOutputPanel']").append(widgetVar.exportAsImage());
    PF('exportImageDlg').show();
}

function meanGraphChartExtender() {

    console.log(this.cfg);

    // title textColor to black

    var titleString = this.cfg.title;

    this.cfg.title = {
        text: titleString,
        textColor: 'black'
    };

    // labelOptions textColor to black

    this.cfg.axes.xaxis.labelOptions = {
        color: 'black',
        textColor: 'black'
    };

    this.cfg.axes.yaxis.labelOptions = {
        color: 'black',
        textColor: 'black'
    };

    // tickOptions textColor to black

    this.cfg.axes.xaxis.tickOptions = {
        color: 'black',
        textColor: 'black',
        angle: -90
    };

    this.cfg.axes.yaxis.tickOptions = {
        color: 'black',
        textColor: 'black'
    };

    // identify PANEL entries and create line
    // for the canvasOverlay

    var ticks = this.cfg.ticks;

    var horizLineArray = Array();

    for (var tickCnt = 0; tickCnt < ticks.length; tickCnt++) {
        var tickName = ticks[tickCnt];
        if (tickName.substring(0, 5) == "PANEL") {
            horizLineArray.push({
                horizontalLine: {
                    color: 'rgba(128,128,128,0.5)',
                    y: tickCnt + 1,
                    lineWidth: 5
                }
            });
        }
    }

    this.cfg.highlighter = {
        show: true,
        sizeAdjust: 25,
        //lineWidthAdjust: 25,
        tooltipLocation: 'e',
        bringSeriesToFront: true,
        //useAxesFormatters: true,
        tooltipContentEditor: barChartTooltipContentEditor,
        //tooltipAxes: 'y',
        showMarker: true
    };

    // series colors on-the-fly

    var colorsArray = Array();

    var series = this.cfg.series;

    for (var serCnt = 0; serCnt < series.length; serCnt++) {
        var serLabel = series[serCnt].label;
        if (serLabel.substring(0, 5) == "PANEL") {
            colorsArray.push(colorByPanelName(serLabel.substring(6)));
        }
    }

    this.cfg.seriesColors = colorsArray;

    this.cfg.seriesDefaults = {
        renderer: $.jqplot.BarRenderer,
        pointLabels: {
            show: false
        },
        rendererOptions: {
            barDirection: 'horizontal',
            shadow: false,
            barMargin: 1,
            barPadding: 1
        }
    };

    this.cfg.canvasOverlay = {
        name: 'mgCO',
        show: true,
        objects: horizLineArray
    };

}

function colorByPanelCde(panelCde) {

    var colorStr;

    colorStr = "rgb(0,0,0)";

    switch (panelCde) {
        case "ASP":
            colorStr = 'rgba(215,171,83,0.5)';
            break;
        case "BRE":
            colorStr = 'rgba(255,175,175,0.5)';
            break;
        case "CHO":
            colorStr = 'rgba(149,53,121,0.5)';
            break;
        case "CNS":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "COL":
            colorStr = 'rgba(0,255,0,0.5)';
            break;
        case "DLS":
            colorStr = 'rgba(154,170,118,0.5)';
            break;
        case "EPS":
            colorStr = 'rgba(112,73,174,0.5)';
            break;
        case "EWS":
            colorStr = 'rgba(197,180,127,0.5)';
            break;
        case "FIS":
            colorStr = 'rgba(149,96,69,0.5)';
            break;
        case "GCS":
            colorStr = 'rgba(149,140,18,0.5)';
            break;
        case "LEU":
            colorStr = 'rgba(255,0,0,0.5)';
            break;
        case "LMS":
            colorStr = 'rgba(109,149,102,0.5)';
            break;
        case "LNS":
            colorStr = 'rgba(0,0,255,0.5)';
            break;
        case "MEL":
            colorStr = 'rgba(255,200,0,0.5)';
            break;
        case "MES":
            colorStr = 'rgba(216,184,63,0.5)';
            break;
        case "MPN":
            colorStr = 'rgba(142,164,122,0.5)';
            break;
        case "NDF":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "NHC":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "NMS":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "NOS":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "FIB":
            colorStr = 'rgba(255,88,0,0.5)';
            break;
        case "NLF":
            colorStr = 'rgba(127,110,102,0.5)';
            break;
        case "NAD":
            colorStr = 'rgba(0,133,204,0.5)';
            break;
        case "NSM":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "OST":
            colorStr = 'rgba(234,162,40,0.5)';
            break;
        case "OVA":
            colorStr = 'rgba(255,0,255,0.5)';
            break;
        case "PRO":
            colorStr = 'rgba(72,209,204,0.5)';
            break;
        case "REN":
            colorStr = 'rgba(255,255,0,0.5)';
            break;
        case "RHM":
            colorStr = 'rgba(75,93,228,0.5)';
            break;
        case "RHM":
            colorStr = 'rgba(75,93,228,0.5)';
            break;
        case "RHT":
            colorStr = 'rgba(87,149,117,0.5)';
            break;
        case "SAR":
            colorStr = 'rgba(75,178,197,0.5)';
            break;
        case "SCL":
            colorStr = 'rgba(235,136,31,0.5)';
            break;
        case "SCS":
            colorStr = 'rgba(145,138,145,0.5)';
            break;
        case "SYS":
            colorStr = 'rgba(131,149,87,0.5)';
            break;
        case "UTS":
            colorStr = 'rgba(140,144,52,0.5)';
            break;
        case "FAK":
            colorStr = 'rgba(255,255,255,0)';
            break;
        case "SUM":
            colorStr = 'rgb(0,0,0)';
            break;
        case "OTH":
            colorStr = 'rgb(0,0,0)';
            break;
    }

    return colorStr;

}

function colorByPanelName(panelName) {

    var colorStr;

    colorStr = "rgb(0,0,0)";

    switch (panelName) {
        case "Alveolar Soft Part Sarcoma":
            colorStr = 'rgba(215,171,83,0.5)';
            break;
        case "Breast":
            colorStr = 'rgba(255,175,175,0.5)';
            break;
        case "Chondrosarcoma":
            colorStr = 'rgba(149,53,121,0.5)';
            break;
        case "CNS":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "Colon":
            colorStr = 'rgba(0,255,0,0.5)';
            break;
        case "Dedifferentiated Liposar":
            colorStr = 'rgba(154,170,118,0.5)';
            break;
        case "Epithelioid Sarcoma":
            colorStr = 'rgba(112,73,174,0.5)';
            break;
        case "Ewing Sarcoma":
            colorStr = 'rgba(197,180,127,0.5)';
            break;
        case "Fibrosarcoma":
            colorStr = 'rgba(149,96,69,0.5)';
            break;
        case "Giant Cell Sarcoma":
            colorStr = 'rgba(149,140,18,0.5)';
            break;
        case "Leukemia":
            colorStr = 'rgba(255,0,0,0.5)';
            break;
        case "Leiomyosarcoma":
            colorStr = 'rgba(109,149,102,0.5)';
            break;
        case "Non-Small Cell Lung Cancer":
            colorStr = 'rgba(0,0,255,0.5)';
            break;
        case "Melanoma":
            colorStr = 'rgba(255,200,0,0.5)';
            break;
        case "Mesothelioma":
            colorStr = 'rgba(216,184,63,0.5)';
            break;
        case "MPNS":
            colorStr = 'rgba(142,164,122,0.5)';
            break;
        case "Normal Dermal Fibroblast":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "Normal Human Chondrocyte":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "Norm Mesenchymal Stem Cell":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "Normal Human Osteoblast":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "Fibroblast":
            colorStr = 'rgba(255,88,0,0.5)';
            break;
        case "Normal Lung Fibroblast":
            colorStr = 'rgba(127,110,102,0.5)';
            break;
        case "Normal Adrenal":
            colorStr = 'rgba(0,133,204,0.5)';
            break;
        case "Norm Human Skeletal Muscle":
            colorStr = 'rgba(128,128,128,0.5)';
            break;
        case "Osteosarcoma":
            colorStr = 'rgba(234,162,40,0.5)';
            break;
        case "Ovarian":
            colorStr = 'rgba(255,0,255,0.5)';
            break;
        case "Prostate":
            colorStr = 'rgba(72,209,204,0.5)';
            break;
        case "Renal":
            colorStr = 'rgba(255,255,0,0.5)';
            break;
        case "Rhabdomyosarcoma":
            colorStr = 'rgba(75,93,228,0.5)';
            break;
        case "Rhabdomyosarcoma ":
            colorStr = 'rgba(75,93,228,0.5)';
            break;
        case "Rhabdoid Tumor":
            colorStr = 'rgba(87,149,117,0.5)';
            break;
        case "Bone/Muscle":
            colorStr = 'rgba(75,178,197,0.5)';
            break;
        case "Small Cell Lung Cancer":
            colorStr = 'rgba(235,136,31,0.5)';
            break;
        case "Spindle Cell Sarcoma":
            colorStr = 'rgba(145,138,145,0.5)';
            break;
        case "Synovial Sarcoma":
            colorStr = 'rgba(131,149,87,0.5)';
            break;
        case "Uterine Sarcoma":
            colorStr = 'rgba(140,144,52,0.5)';
            break;
        case "FAKE":
            colorStr = 'rgba(255,255,255,0)';
            break;
        case "SUMMARY":
            colorStr = 'rgb(0,0,0)';
            break;
        case "OTHER":
            colorStr = 'rgb(0,0,0)';
            break;
    }

    return colorStr;

}