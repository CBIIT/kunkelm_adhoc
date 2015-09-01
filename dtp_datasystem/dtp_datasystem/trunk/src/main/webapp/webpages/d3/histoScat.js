var propMap = {};
var pairedPropMap = {};

var selIntProps = [];
var selDblProps = [];
var allSelProps = [];

function loadData() {

    // initial properties from the form	
    d3.select("#configForm\\:intCbxGrd").selectAll("input").filter(function(d, i) {
        if (this.checked){
            selIntProps.push(this.value);
          allSelProps.push(this.value);
        }
    });

    d3.select("#configForm\\:dblCbxGrd").selectAll("input").filter(function(d, i) {
        if (this.checked){
            selDblProps.push(this.value);
          allSelProps.push(this.value);
        }
    });

    // change listeners
    d3.select("#configForm\\:intCbxGrd").on("change", function() {
    		
        selIntProps = [];
				allSelProps = selDblProps;

        d3.select(this).selectAll("input").filter(function(d, i) {
            if (this.checked){
                selIntProps.push(this.value);
              allSelProps.push(this.value);
            }
        });
        
        console.log('allSelProps: ');
        console.log(allSelProps);
                
        doHistos();
        doScats();
    });

    d3.select("#configForm\\:dblCbxGrd").on("change", function() {
    		
        selDblProps = [];
        allSelProps = selIntProps;
        
        d3.select(this).selectAll("input").filter(function(d, i) {
            if (this.checked){
                selDblProps.push(this.value);
              allSelProps.push(this.value);
            }
        });
                
        console.log('allSelProps: ');
        console.log(allSelProps);
                
        doHistos();
        doScats();
    });

    console.log('selIntProps: ');
        console.log(selIntProps);
        
        console.log('selDblProps: ');
        console.log(selDblProps);
                
        console.log('allSelProps: ');
        console.log(allSelProps);
            
    
    doHistos();
    doScats();

}

var unitDim = 10;
var theFontSize = unitDim;

var unitWidth = 40 * unitDim;
var unitHeight = 30 * unitDim;

var margin = {top: 4 * unitDim, right: 4 * unitDim, bottom: 4 * unitDim, left: 4 * unitDim};

var plotAreaWidth = unitWidth - margin.left - margin.right;
var plotAreaHeight = unitHeight - margin.top - margin.bottom;

var histosPerRow = 4;

var overallMargin = {top: 4 * unitDim, right: 4 * unitDim, bottom: 4 * unitDim, left: 4 * unitDim};

function tileHistos(thisIdx) {
    var histoTotalCnt = allSelProps.length;
    var row = Math.floor(thisIdx / histosPerRow);
    var col = thisIdx % histosPerRow;
    var rtn = {row: row, col: col};
    // console.log('tileHistos rtn: row: ' + row + ' col: ' + col);
    return rtn;
}

function tileScats(thisIdx) {
    var perRow = allSelProps.length;
    var scatTotalCnt = Math.pow(perRow, 2);
    var row = Math.floor(thisIdx / perRow);
    var col = thisIdx % perRow;
    var rtn = {row: row, col: col};
    // console.log('thisIdx: ' + thisIdx + ' tileScats rtn: row: ' + row + ' col: ' + col);
    return rtn;
}

function doHistos() {
    
    var histoOverallWidth = histosPerRow * unitWidth + overallMargin.left + overallMargin.right;
var histoOverallHeight = (Math.floor(allSelProps.length / histosPerRow) + 1) * unitHeight + overallMargin.top + overallMargin.bottom;

    d3.select("#histoForm\\:histoDiv").selectAll("*").remove();

    var formField = d3.select("#configForm\\:jsonStr");

    var j = JSON.parse(formField[0][0].value);

    // set up the map...
    allSelProps.forEach(function(p) {
        var data = [];
        propMap[p] = data;
    });

    j.forEach(function(d, i) {
        var nsc = d.cmpd.nsc;
        var pChem = d.cmpd.parentFragment.cmpdFragmentPChem;
        allSelProps.forEach(function(p) {
            propMap[p].push({
                nsc: nsc,
                prop: p,
                value: pChem[p]});
        });
    });

    // the all-encompassing svg
    var svg = d3.select("#histoForm\\:histoDiv").append("svg")
            .attr("width", histoOverallWidth)
            .attr("height", histoOverallHeight)
            .append("g")
                .attr("transform", "translate(" + overallMargin.left + "," + overallMargin.top + ")");
                
    allSelProps.forEach(function(prop, idx) {

        var theTitle = prop;
    		
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

        var values = [];

        propMap[prop].forEach(function(d) {
            values.push(d.value);
        });

        // A formatter for counts.
        var formatCount = d3.format(",.0f");

        var maxProp = d3.max(values);
        // console.log('prop: ' + prop + ' maxProp: ' + maxProp);

        var x = d3.scale.linear()
                .domain([0, maxProp])
                .range([0, plotAreaWidth]);
                
                var histogram = d3.layout.histogram(); //.bins(20);
                data = histogram(values);
                
        //var data = d3.layout.histogram()
        //        .bins(x.ticks(maxProp))
        //        (values);

        var y = d3.scale.linear()
                .domain([0, d3.max(data, function(d) {
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
                .attr("transform", function(d) {
                    return "translate(" + x(d.x) + "," + y(d.y) + ")";
                });

        bar.append("rect")
                .attr("x", 1)
                .attr("width", x(data[0].dx) - 1)
                .attr("stroke", "black")
                .attr("fill", "blue")
                .attr("shape-rendering", "crispEdges")
                .attr("height", function(d) {
                    return plotAreaHeight - y(d.y);
                })
                .on("click", function(d) {
                    console.log('property: ' + prop + ' bar x: ' + d.x + ' bar dx: ' + d.dx);
                });

        bar.append("text")
                .attr("dy", ".75em")
                .attr("y", -unitDim)
                .attr("x", x(data[0].dx) / 2)
                .attr("text-anchor", "middle")
                .style("font", theFontSize + "px courier")
                .style("stroke", "blue")
                .style("fill", "blue")                
                .text(function(d) {
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
    });
}

function doScats() {
    
var scatOverallWidth = allSelProps.length * unitWidth + overallMargin.left + overallMargin.right;
var scatOverallHeight = allSelProps.length * unitHeight + overallMargin.top + overallMargin.bottom;

    d3.select("#scatForm\\:scatDiv").selectAll("*").remove();

    var formField = d3.select("#configForm\\:jsonStr");

    var j = JSON.parse(formField[0][0].value);

    // set up the paired-prop map...
    allSelProps.forEach(function(outerProp, oIdx) {
        allSelProps.forEach(function(innerProp, iIdx) {
        		var key = outerProp + " vs " + innerProp;
            var index = oIdx * allSelProps.length + iIdx;
            var data = [];
            pairedPropMap[key] = {index: index, outerProp: outerProp, innerProp: innerProp, data: data};
        });
    });

    j.forEach(function(d, i) {
    		
        var nsc = d.cmpd.nsc;
        var pChem = d.cmpd.parentFragment.cmpdFragmentPChem;

        allSelProps.forEach(function(outerProp) {
            allSelProps.forEach(function(innerProp) {
                var key = outerProp + " vs " + innerProp;
                pairedPropMap[key].data.push({
                    nsc: nsc,
                    outerProp: outerProp,
                    outerValue: pChem[outerProp],
                    innerProp: innerProp,
                    innerValue: pChem[innerProp]
                });
            });
        });
    });
    
    // the all-encompassing svg
    var svg = d3.select("#scatForm\\:scatDiv").append("svg")
            .attr("width", scatOverallWidth)
            .attr("height", scatOverallHeight)            
            .append("g")
                .attr("transform", "translate(" + overallMargin.left + "," + overallMargin.top + ")");

    Object.keys(pairedPropMap).forEach(function(propStr) {
    		
    		var theTitle = propStr;
    		    			
    		//var tile = tileScats(pairedPropMap[propStr].index);
    		var tile = tileScats(pairedPropMap[propStr].index);

        // translate thisScat
        var thisScat = svg.append("g")
                .attr("transform", "translate(" + tile.col * unitWidth + "," + tile.row * unitHeight + ")");
                
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
                .text(pairedPropMap[propStr].innerProp);
                
                
                 drawingArea.append("g")
    .attr("transform", "translate(" + -margin.left / 2 + ", " + plotAreaHeight / 2 + ")")
    .append("text")
    .attr("text-anchor", "middle")
    .attr("transform", "rotate(-90)")
    .style("font", theFontSize + "px courier")
                .style("stroke", "black")
                .style("fill", "black")                
                .text(pairedPropMap[propStr].outerProp);
                       
        var data = pairedPropMap[propStr].data;

        var maxX = d3.max(data, function(d) {
            return d.innerValue;
        });
        
        var maxY = d3.max(data, function(d) {
            return d.outerValue;
        });

        var x = d3.scale.linear()
                .domain([0, maxX])
                .range([0, plotAreaWidth]);

        var y = d3.scale.linear()
                .domain([0, maxY])
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
                .data(data)
                .enter()
                .append("circle")
                .attr("cx", function(d) {
                    return x(d.innerValue);
                })
                .attr("cy", function(d) {
                    return y(d.outerValue);
                })
                .attr("r", unitDim / 4)
                .attr("fill", "red");

    });

}