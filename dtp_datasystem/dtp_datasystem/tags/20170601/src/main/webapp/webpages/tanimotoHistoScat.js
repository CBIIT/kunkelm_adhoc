function fpLookup(fpAbbrev) {
    switch (fpAbbrev) {
        case "ap":
            return "atompairbv_fp";
            break;

        case "fm":
            return "featmorganbv_fp";
            break;

        case "l":
            return "layered_fp";
            break;

        case "mc":
            return "maccs_fp";
            break;

        case "m":
            return "morganbv_fp";
            break;

        case "r":
            return "rdkit_fp";
            break;

        case "to":
            return "torsionbv_fp";
            break;
    }
}


function histosToPng() {
    var $container = $('#datasystemForm\\:histoDiv');
    // Canvg requires trimmed content
    var content = $container.html().trim();
    var theCanvas = document.createElement('canvas');
    canvg(theCanvas, content, {renderCallback: function () {
            var imag = document.createElement("img");
            imag.src = theCanvas.toDataURL("image/png");
            $("[id='exportableImageOutputPanel']").empty();
            $("[id='exportableImageOutputPanel']").append(imag);
            PF('exportImageDlg').show();
        }});
}

function scatsToPng() {
    var $container = $('#datasystemForm\\:scatDiv');
    // Canvg requires trimmed content
    var content = $container.html().trim();
    var theCanvas = document.createElement('canvas');
    canvg(theCanvas, content, {renderCallback: function () {
            var imag = document.createElement("img");
            imag.src = theCanvas.toDataURL("image/png");
            $("[id='exportableImageOutputPanel']").empty();
            $("[id='exportableImageOutputPanel']").append(imag);
            PF('exportImageDlg').show();
        }});
}

var theData = [];
var theNodes = [];

var histosPerRow = 4;

var allFingerprints = ['ap', 'fm', 'l', 'mc', 'm', 'r', 'to'];

var unitDim = 20;
var theFontSize = unitDim;
var unitWidth = 40 * unitDim;
var unitHeight = 30 * unitDim;

var margin = {top: 4 * unitDim, right: 4 * unitDim, bottom: 4 * unitDim, left: 4 * unitDim};

var plotAreaWidth = unitWidth - margin.left - margin.right;
var plotAreaHeight = unitHeight - margin.top - margin.bottom;

var overallMargin = {top: 4 * unitDim, right: 4 * unitDim, bottom: 4 * unitDim, left: 4 * unitDim};

function updateDimensions() {

    theFontSize = unitDim;
    unitWidth = 40 * unitDim;
    unitHeight = 30 * unitDim;

    margin = {top: 4 * unitDim, right: 4 * unitDim, bottom: 4 * unitDim, left: 4 * unitDim};

    plotAreaWidth = unitWidth - margin.left - margin.right;
    plotAreaHeight = unitHeight - margin.top - margin.bottom;

    overallMargin = {top: 4 * unitDim, right: 4 * unitDim, bottom: 4 * unitDim, left: 4 * unitDim};

}

function clearHistos() {
    d3.select("#datasystemForm\\:histoDiv").selectAll("*").remove();
}

function clearScats() {
    d3.select("#datasystemForm\\:scatDiv").selectAll("*").remove();
}

function loadData() {

    var j = hidddden;

    console.log('j');
    console.log(j);

    var modDiv = 0;
    if (j.links.length < 2000) {
    } else if (j.links.length < 1000000) {
        modDiv = Math.floor(j.links.length / 1000);
        console.log('Math.floor value of modDiv: ' + modDiv);
    } else {
        // THIS SHOULD FAIL
    }

    console.log('modDiv before links.forEach: ' + modDiv);

    j.links.forEach(function (d, i) {

        if (modDiv === 0) {
            theData.push(d);
        } else {
            if (i % modDiv === 0) {
                theData.push(d);
            }
        }

    });

    j.nodes.forEach(function (d) {
        theNodes.push(d);
    });

    // change listeners
    d3.select("#datasystemForm\\:fp").on("change", function () {
        doHistos();
        doScats();
    });

    d3.select("#datasystemForm\\:histoSize").on("change", function () {
        unitDim = this.value;
        updateDimensions();
        console.log('histoSize chagned.  unitDim: ' + unitDim);
        doHistos();
    });

    d3.select("#datasystemForm\\:scatSize").on("change", function () {
        unitDim = this.value;
        updateDimensions();
        console.log('scatSize chagned.  unitDim: ' + unitDim);
        doScats();
    });

    doHistos();
    doScats();

}

function barSelect(fp, minVal, delta) {

    console.log('barSelect: fp: ' + fp + ' minVal: ' + minVal + ' delta: ' + delta);

    // reload the data, filtering by fp values

    theData.forEach(function (d) {
        if (d[fp] >= minVal && d[fp] < minVal + delta) {
            d.isSelected = true;
        } else {
            d.isSelected = false;
        }

        // if (d.isSelected) {
        //    console.log('d.isSelected: ');
        //    console.log(d);
        // }

    });

    doHistos();
    doScats();

}

function tileHistos(thisIdx) {
    var histoTotalCnt = allFingerprints.length;
    var row = Math.floor(thisIdx / histosPerRow);
    var col = thisIdx % histosPerRow;
    var rtn = {row: row, col: col};
    // console.log('tileHistos thisIdx: ' + thisIdx + ' row: ' + row + ' col: ' + col);
    return rtn;
}

function doHistos() {

    var histoOverallWidth = histosPerRow * unitWidth + overallMargin.left + overallMargin.right;
    var histoOverallHeight = (Math.floor(allFingerprints.length / histosPerRow) + 1) * unitHeight + overallMargin.top + overallMargin.bottom;

    clearHistos();

    // the all-encompassing svg
    var svg = d3.select("#datasystemForm\\:histoDiv").append("svg")
            .attr("width", histoOverallWidth)
            .attr("height", histoOverallHeight)
            .append("g")
            .attr("transform", "translate(" + overallMargin.left + "," + overallMargin.top + ")");

    allFingerprints.sort().forEach(function (prop, idx) {

        var theTitle = fpLookup(prop);

        var tile = tileHistos(idx);

        // translate thisHisto
        var thisHisto = svg.append("g")
                .attr("transform", "translate(" + tile.col * unitWidth + "," + tile.row * unitHeight + ")");

        thisHisto.append("rect")
                .attr("x", 0)
                .attr("y", 0)
                .attr("width", unitWidth)
                .attr("height", unitHeight)
                .attr("stroke", "red")
                .attr("fill", "none");

        // the drawing area
        var drawingArea = thisHisto.append("g")
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        drawingArea.append("rect")
                .attr("x", 0)
                .attr("y", 0)
                .attr("width", plotAreaWidth)
                .attr("height", plotAreaHeight)
                .attr("stroke", "green")
                .attr("fill", "none");

        drawingArea.append("g")
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        // A formatter for counts.
        var formatCount = d3.format(",.0f");

        var minProp = d3.min(theData, function (d) {
            return d[prop];
        });

        var maxProp = d3.max(theData, function (d) {
            return d[prop];
        });

        //console.log('prop: ' + prop + ' minProp: ' + minProp + ' maxProp: ' + maxProp);

        var x = d3.scale.linear()
                .domain([minProp, maxProp])
                .range([0, plotAreaWidth]);

        var histogram = d3.layout.histogram();

        var data = histogram.value(function (d) {
            return d[prop];
        })(theData);

        var y = d3.scale.linear()
                .domain([0, d3.max(data, function (d) {
                        return d.y;
                    })])
                .range([plotAreaHeight, 0]);

        var xAxis = d3.svg.axis()
                .scale(x)
                .orient("bottom");

        var yAxis = d3.svg.axis()
                .scale(y)
                .orient("left");

        var bar = drawingArea.selectAll(".bar")
                .data(data)
                .enter().append("g")
                .attr("class", "bar")
                .attr("transform", function (d) {
                    //console.log('d inside .bar');
                    //console.log(d);
                    return "translate(" + x(d.x) + "," + y(d.y) + ")";
                });


        bar.append("rect")
                .attr("x", 1)
                //.attr("width", x(data[0].dx) - 1)  << DOESN'T HANDLE NEGATIVE VALUES
                .attr("width", x(data[0].x + data[0].dx) - 1)
                .attr("stroke", "black")
                .attr("fill", "red")
                .attr("shape-rendering", "crispEdges")
                .attr("height", function (d) {
                    return plotAreaHeight - y(d.y);
                })
                .on("click", function (d) {
                    console.log('red bar');
                    console.log('red bar property: ' + prop + ' bar x: ' + d.x + ' bar dx: ' + d.dx);
                    barSelect(prop, d.x, d.dx);
                });

        bar.append("rect")
                .attr("x", 1)
                //.attr("width", x(data[0].dx) - 1)  << DOESN'T HANDLE NEGATIVE VALUES
                .attr("width", x(data[0].x + data[0].dx) - 1)
                .attr("stroke", "black")
                .attr("fill", "blue")
                .attr("shape-rendering", "crispEdges")
                .attr("height", function (d) {
                    var selCnt = 0;
                    d.forEach(function (d) {
                        if (d.isSelected) {
                            selCnt++;
                        }
                    });

                    //console.log('selCnt: ' + selCnt);

                    d.selCnt = selCnt;

                    //console.log('d inside rect');
                    //console.log(d);

                    //console.log('d.y inside rect');
                    //console.log(d.y - selCnt);

                    return plotAreaHeight - y(d.y - selCnt);
                })
                .on("click", function (d) {
                    console.log('blue bar');
                    console.log('blue bar property: ' + prop + ' bar x: ' + d.x + ' bar dx: ' + d.dx);
                    barSelect(prop, d.x, d.dx);
                });

        bar.append("text")
                .attr("dy", ".75em")
                .attr("y", -unitDim)
                //.attr("x", x(data[0].dx) / 2)
                .attr("x", (x(data[0].x + data[0].dx) - 1) / 2)
                .attr("text-anchor", "end")
                .style("font", theFontSize + "px courier")
                .style("stroke", "red")
                .style("fill", "red")
                .text(function (d) {
                    return formatCount(d.selCnt) + '/';
                });

        bar.append("text")
                .attr("dy", ".75em")
                .attr("y", -unitDim)
                //.attr("x", x(data[0].dx) / 2)
                .attr("x", (x(data[0].x + data[0].dx) - 1) / 2)
                .attr("text-anchor", "start")
                .style("font", theFontSize + "px courier")
                .style("stroke", "blue")
                .style("fill", "blue")
                .text(function (d) {
                    return formatCount(d.y);
                });

        drawingArea.append("g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + plotAreaHeight + ")")
                .call(xAxis);

        drawingArea.append("g")
                .attr("class", "y axis")
                .call(yAxis);

        drawingArea.append("text")
                .attr("x", plotAreaWidth / 2)
                .attr("y", -unitDim)
                .attr("text-anchor", "middle")
                .style("font", theFontSize + "px courier")
                .style("stroke", "black")
                .style("fill", "black")
                .text(theTitle);

    });// allFingerprints.forEach
}

function doScats() {

    var scatOverallWidth = allFingerprints.length * unitWidth + overallMargin.left + overallMargin.right;
    var scatOverallHeight = allFingerprints.length * unitHeight + overallMargin.top + overallMargin.bottom;

    clearScats();

    // the all-encompassing svg
    var svg = d3.select("#datasystemForm\\:scatDiv").append("svg")
            .attr("width", scatOverallWidth)
            .attr("height", scatOverallHeight)
            .append("g")
            .attr("transform", "translate(" + overallMargin.left + "," + overallMargin.top + ")");

    allFingerprints.sort().forEach(function (outerProp, outerIdx) {

        allFingerprints.sort().forEach(function (innerProp, innerIdx) {

            var theTitle = fpLookup(outerProp) + " vs " + fpLookup(innerProp);

            // translate thisScat
            var thisScat = svg.append("g")
                    .attr("transform", "translate(" + outerIdx * unitWidth + "," + innerIdx * unitHeight + ")");

            thisScat.append("rect")
                    .attr("x", 0)
                    .attr("y", 0)
                    .attr("width", unitWidth)
                    .attr("height", unitHeight)
                    .attr("stroke", "red")
                    .attr("fill", "none");

            // the drawing area
            var drawingArea = thisScat.append("g")
                    .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

            drawingArea.append("rect")
                    .attr("x", 0)
                    .attr("y", 0)
                    .attr("width", plotAreaWidth)
                    .attr("height", plotAreaHeight)
                    .attr("stroke", "green")
                    .attr("fill", "none");

            // titles and axis labels         

            drawingArea.append("text")
                    .attr("x", plotAreaWidth / 2)
                    .attr("y", -unitDim)
                    .attr("text-anchor", "middle")
                    .style("font", theFontSize + "px courier")
                    .style("stroke", "black")
                    .style("fill", "black")
                    .text(theTitle);

            drawingArea.append("text")
                    .attr("x", plotAreaWidth / 2)
                    .attr("y", plotAreaHeight + margin.bottom / 2)
                    .style("text-anchor", "middle")
                    .style("font", theFontSize + "px courier")
                    .style("stroke", "black")
                    .style("fill", "black")
                    .text(innerProp);
            drawingArea.append("g")
                    .attr("transform", "translate(" + -margin.left / 2 + ", " + plotAreaHeight / 2 + ")")
                    .append("text")
                    .attr("text-anchor", "middle")
                    .attr("transform", "rotate(-90)")
                    .style("font", theFontSize + "px courier")
                    .style("stroke", "black")
                    .style("fill", "black")
                    .text(outerProp);
            var minX = d3.min(theData, function (d) {
                return d[innerProp];
            });
            var maxX = d3.max(theData, function (d) {
                return d[innerProp];
            });
            var minY = d3.min(theData, function (d) {
                return d[outerProp];
            });
            var maxY = d3.max(theData, function (d) {
                return d[outerProp];
            });
            var x = d3.scale.linear()
                    .domain([minX, maxX])
                    .range([0, plotAreaWidth]);
            var y = d3.scale.linear()
                    .domain([minY, maxY])
                    .range([plotAreaHeight, 0]);
            var xAxis = d3.svg.axis()
                    .scale(x)
                    .orient("bottom");
            var yAxis = d3.svg.axis()
                    .scale(y)
                    .orient("left");
            drawingArea.append("g")
                    .attr("class", "x axis")
                    .attr("transform", "translate(0," + plotAreaHeight + ")")
                    .call(xAxis);
            drawingArea.append("g")
                    .attr("class", "y axis")
                    .call(yAxis);
            drawingArea.selectAll("circle")
                    .data(theData)
                    .enter()
                    .append("circle")
                    .attr("cx", function (d) {
                        return d[innerProp] && d[outerProp] ? x(d[innerProp]) : 0;
                    })
                    .attr("cy", function (d) {
                        return d[innerProp] && d[outerProp] ? y(d[outerProp]) : 0;
                    })
                    .attr("r", function (d) {
                        return d[innerProp] && d[outerProp] ? unitDim / 4 : 0;
                    })
                    .attr("fill", function (d) {
                        return d.isSelected ? "red" : "blue";
                    })
                    .on("click", function (d) {
                        console.log('circle.d: ');
                        console.log(d);
                        console.log('theNodes[d.source]');
                        console.log(theNodes[d.source]);
                        console.log('theNodes[d.target]');
                        console.log(theNodes[d.target]);
                    });

        }); // innerProp
    }); // outerProp

}