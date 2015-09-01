var propMap = {};
var pairedPropMap = {};

var selIntProps = [];
var selDblProps = [];

function loadData() {

    // initial properties from the form	
    d3.select("#configForm\\:intCbxGrd").selectAll("input").filter(function(d, i) {
        if (this.checked)
            selIntProps.push(this.value);
    });

    d3.select("#configForm\\:dblCbxGrd").selectAll("input").filter(function(d, i) {
        if (this.checked)
            selDblProps.push(this.value);
    });

    // change listeners
    d3.select("#configForm\\:intCbxGrd").on("change", function() {
        selIntProps = [];
        d3.select(this).selectAll("input").filter(function(d, i) {
            if (this.checked)
                selIntProps.push(this.value);
        });
        doHisto();
        doScat();
    });

    d3.select("#configForm\\:dblCbxGrd").on("change", function() {
        selDblProps = [];
        d3.select(this).selectAll("input").filter(function(d, i) {
            if (this.checked)
                selDblProps.push(this.value);
        });
        doHisto();
        doScat();
    });

    // 
    doHisto();
    doScat();

}



function tileHistos(thisIdx) {
	
    var histoTotalCnt = selIntProps.length;
    var perRow = 4;
    
    var tileRow = Math.floor(thisIdx / perRow);
    var tileCol = thisIdx % perRow;
    
    var rtn = {row: tileRow, col: tileCol};
    console.log('tileHistos rtn: tileRow: ' + tileRow + ' tileCol: ' + tileCol);
    
    return rtn;
}

function tileScats(thisIdx) {
	
    var perRow = selIntProps.length;
    var scatTotalCnt = Math.pow(perRow, 2);
    
    var tileRow = Math.floor(thisIdx / perRow);
    var tileCol = thisIdx % perRow;
    
    var rtn = {row: tileRow, col: tileCol};
    console.log('tileScats rtn: tileRow: ' + tileRow + ' tileCol: ' + tileCol);
    
    return rtn;
}

function doHisto() {

    d3.select("#histoForm\\:histoDiv").selectAll("*").remove();

    var formField = d3.select("#configForm\\:jsonStr");

    var j = JSON.parse(formField[0][0].value);

    // set up the map...
    selIntProps.forEach(function(p) {
        var data = [];
        propMap[p] = data;
    });

    j.forEach(function(d, i) {
        var nsc = d.cmpd.nsc;
        var pChem = d.cmpd.parentFragment.cmpdFragmentPChem;
        selIntProps.forEach(function(p) {
            propMap[p].push({
                nsc: nsc,
                prop: p,
                value: pChem[p]});
        });
    });

    selIntProps.forEach(function(prop, idx) {

        tileHistos(idx);

        var values = [];

        propMap[prop].forEach(function(d) {
            values.push(d.value);
        });

        var theTitle = prop;
        // A formatter for counts.
        var formatCount = d3.format(",.0f");

        var unitDim = 20;

        var margin = {top: unitDim, right: unitDim, bottom: unitDim, left: unitDim};
        var width = 24 * unitDim - margin.left - margin.right;
        var height = 18 * unitDim - margin.top - margin.bottom;
        
        var maxProp = d3.max(values);
        console.log('prop: ' + prop + ' maxProp: ' + maxProp);

        var x = d3.scale.linear()
                .domain([0, maxProp])
                .range([0, width]);

        var data = d3.layout.histogram()
                .bins(x.ticks(maxProp))
                (values);
                
        var y = d3.scale.linear()
                .domain([0, d3.max(data, function(d) {
                        return d.y;
                    })])
                .range([height, 0]);

        var xAxis = d3.svg.axis()
                .scale(x)
                .orient("bottom")
                .ticks(maxProp);

        var yAxis = d3.svg.axis()
                .scale(y)
                .orient("left");

        var svg = d3.select("#histoForm\\:histoDiv").append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom)
                .append("g")
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        var bar = svg.selectAll(".bar")
                .data(data)
                .enter().append("g")
                .attr("class", "bar")
                .attr("transform", function(d) {
                    return "translate(" + x(d.x) + "," + y(d.y) + ")";
                });

        bar.append("rect")
                .attr("x", 1)
                .attr("width", x(data[0].dx) - 1)
                .attr("height", function(d) {
                    return height - y(d.y);
                })
                .on("click", function(d) {
                    console.log('property: ' + prop + ' bar x: ' + d.x + ' bar dx: ' + d.dx);
                });

        bar.append("text")
                .attr("dy", ".75em")
                .attr("y", 6)
                .attr("x", x(data[0].dx) / 2)
                .attr("text-anchor", "middle")
                .style("font", unitDim / 2 + "px courier")
                .text(function(d) {
                    return formatCount(d.y);
                });

        svg.append("g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + height + ")")
                .call(xAxis);

        svg.append("g")
                .attr("class", "y axis")
                .call(yAxis);

        svg.append("text")
                .attr("x", width / 2)
                .attr("text-anchor", "middle")
                .style("font", unitDim + "px courier")
                .style("stroke", "black")
                .style("fill", "black")
                .text(theTitle);
    });
}

function doScat() {

    d3.select("#scatForm\\:scatDiv").selectAll("*").remove();

    var formField = d3.select("#configForm\\:jsonStr");

    var j = JSON.parse(formField[0][0].value);

    // set up the paired-prop map...
    selIntProps.forEach(function(outerProp) {
        selIntProps.forEach(function(innerProp) {
            var key = outerProp + " vs " + innerProp;
            var data = [];
            pairedPropMap[key] = data;
        });
    });

    j.forEach(function(d, i) {
    		
        var nsc = d.cmpd.nsc;
        var pChem = d.cmpd.parentFragment.cmpdFragmentPChem;

        selIntProps.forEach(function(outerProp) {
            selIntProps.forEach(function(innerProp) {
                var key = outerProp + " vs " + innerProp;
                pairedPropMap[key].push({
                    nsc: nsc,
                    outerProp: outerProp,
                    outerValue: pChem[outerProp],
                    innerProp: innerProp,
                    innerValue: pChem[innerProp]
                });
            });
        });
    });

    var unitDim = 20;

    var margin = {top: unitDim, right: unitDim, bottom: unitDim, left: unitDim};

    var width = 24 * unitDim - margin.left - margin.right;
    var height = 18 * unitDim - margin.top - margin.bottom;

    Object.keys(pairedPropMap).forEach(function(propStr, idx) {
    		
    		tileScats(idx);

        var data = pairedPropMap[propStr];

        var maxX = d3.max(data, function(d) {
            return d.innerValue;
        });
        var maxY = d3.max(data, function(d) {
            return d.outerValue;
        });

        var x = d3.scale.linear()
                .domain([0, maxX])
                .range([0, width]);

        var y = d3.scale.linear()
                .domain([0, maxY])
                .range([height, 0]);

        var xAxis = d3.svg.axis()
                .scale(x)
                .orient("bottom");

        var yAxis = d3.svg.axis()
                .scale(y)
                .orient("left");

        var svg = d3.select("#scatForm\\:scatDiv").append("svg")
                .attr("width", width + margin.left + margin.right)
                .attr("height", height + margin.top + margin.bottom)
                .append("g")
                .attr("transform", "translate(" + margin.left + "," + margin.top + ")");

        svg.append("g")
                .attr("class", "x axis")
                .attr("transform", "translate(0," + height + ")")
                .call(xAxis);

        svg.append("g")
                .attr("class", "y axis")
                .call(yAxis);

        svg.selectAll("circle")
                .data(data)
                .enter()
                .append("circle")
                .attr("cx", function(d) {
                    return x(d.innerValue);
                })
                .attr("cy", function(d) {
                    return y(d.outerValue);
                })
                .attr("r", 5)
                .attr("fill", "red");

        svg.append("text")
                .attr("x", width / 2)
                .attr("text-anchor", "middle")
                .style("font", unitDim / 2 + "px courier")
                .style("stroke", "black")
                .style("fill", "black")
                .text(propStr);

    });

}