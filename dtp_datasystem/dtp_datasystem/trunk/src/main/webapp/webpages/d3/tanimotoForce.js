function toPng() {

    var $container = $('#divForD3');

    // Canvg requires trimmed content
    var content = $container.html().trim();

    var theCanvas = document.createElement('canvas');

    canvg(theCanvas, content, {renderCallback: function() {
            var imag = document.createElement("img");
            imag.src = theCanvas.toDataURL("image/png");
            $("[id='exportableImageOutputPanel']").empty();
            $("[id='exportableImageOutputPanel']").append(imag);
            PF('exportImageDlg').show();
        }});

    /*
     canvg(theCanvas, content, { renderCallback: function () {
     var img = theCanvas.toDataURL("image/png");
     window.open(img);
     }});
     */
}

var width = 1600;
var height = 1200;
var catColor = d3.scale.category10().domain(d3.range(0, 10));

var allNodes = [];
var allLinks = [];

var filtNodes = [];
var filtLinks = [];

var whichFingerprint;
var globalMinTan;


var selNscs = [];
var selDrugNames = [];
var selTargets = [];

var labelAnchors = [];
var labelAnchorLinks = [];

var viz;
var force;
var force2;

function appendStruct(s, t) {

    var structPanelVar = d3.select("#theForm\\:structPanel_content");

    var imgUrl = function(s, t) {
        return 'http://localhost:8080/sarcomacompare/StructureServlet?smiles=' + s + '&title=' + t;
    }

    structPanelVar.append("img").attr("src", imgUrl(s, t)).attr("fill", "red").attr("width", 100).attr("height", 100);
}

function changeStruct(s, t) {

    var structVar = d3.select("#theForm\\:struct");

    console.log('structVar');
    console.log(structVar);
    console.log('structVar.src');
    console.log(structVar[0][0].src);
    structVar[0][0].src = 'http://localhost:8080/sarcomacompare/StructureServlet?structureDim=150&smiles=' + s + '&title=' + t;
}

function setGlobals() {

    viz = d3.select("#divForD3")
            .append("svg")
            .attr("width", width)
            .attr("height", height)
            .append("svg:g")
            .style("font-family", "Arial")
            .style("font-size", 24)
            .call(d3.behavior.zoom().scaleExtent([1, 8]).on("zoom", zoom));

    //force = d3.layout.force().charge(-120).linkDistance(80).size([width * 0.75, height * 0.75]);

    force = d3.layout.force()
            .charge(-120)
            .linkDistance(function(lnk, i) {
                return lnk.l * 200 + 100;
            })
            .size([width, height]);
            //.size([width * 0.75, height * 0.75]);

    force2 = d3.layout.force().gravity(0).linkDistance(100).linkStrength(8).charge(-100).size([width * 0.75, height * 0.75]);

    var mintanField = d3.select("#theForm\\:mintan");
    globalMinTan = Number(mintanField[0][0].value);
    console.log('init globalMinTan: ' + globalMinTan);

    var fpField = d3.select("#theForm\\:fp");
    whichFingerprint = fpField[0][0].value;
    console.log('init whichFingerprint: ' + whichFingerprint);

    d3.select("#theForm\\:fp").on("change", function() {

        whichFingerprint = this.value;
        console.log('new fp: ' + whichFingerprint);

        // update allLinks
        allLinks.forEach(function(lnk) {
            lnk.value = lnk[whichFingerprint];
            // console.log('lnk.value: ' + lnk.value);
        });

        filtLinks = [];

        allLinks.forEach(function(lnk) {
            if (Number(lnk.value) > globalMinTan)
                filtLinks.push(lnk);
        });

        viz.selectAll("*").remove();
        drawIt();

    });

    d3.select("#theForm\\:mintan").on("change", function() {

        globalMinTan = Number(this.value);

        console.log('new cutoff: ' + globalMinTan);

        filtLinks = [];

        allLinks.forEach(function(lnk) {
            // console.log('lnk.value: ' + lnk.value);
            // console.log('globalMinTan: ' + globalMinTan);    		
            if (Number(lnk.value) > globalMinTan)
                filtLinks.push(lnk);
        });

        viz.selectAll("*").remove();
        drawIt();
    });

    d3.select("#theForm\\:multinsc").on("change", function() {
        console.log('change in multinsc');
        selNscs = [];
        d3.select(this).selectAll("option").filter(function(d, i) {
            if (this.selected)
                selNscs.push(parseInt(this.value));
        });

        console.log('selNscs:');
        console.log(selNscs);

        viz.selectAll("*").remove();
        drawIt();
    });

    d3.select("#theForm\\:multidrugname").on("change", function() {
        console.log('change in multidrugname');
        selDrugNames = [];
        d3.select(this).selectAll("option").filter(function(d, i) {
            if (this.selected)
                selDrugNames.push(this.value);
        });
        
        console.log('selDrugNames:');
        console.log(selDrugNames);
        
        viz.selectAll("*").remove();
        drawIt();
    });

    d3.select("#theForm\\:multitarg").on("change", function() {
        console.log('change in multitarg');
        selTargets = [];
        d3.select(this).selectAll("option").filter(function(d, i) {
            if (this.selected)
                selTargets.push(this.value);
        });

        console.log('selTargets:');
        console.log(selTargets);

        viz.selectAll("*").remove();
        drawIt();
    });

    var formField = d3.select("#theForm\\:jsonStr");
    var j = JSON.parse(formField[0][0].value);

//    console.log('j');
//    console.log(j);

    j.nodes.forEach(function(entry) {
        allNodes.push(entry);
    });

    // this is the initial load
    // default to atompair => link.ap
    // whichFingerprint = 'ap';

    j.links.forEach(function(entry) {

        // console.log(entry);  		
        // console.log('whichFingerprint: ' + whichFingerprint);
        // console.log('entry[whichFingerprint]: ' + entry[whichFingerprint]);

        entry.value = entry[whichFingerprint];

        if (entry.value > 0)
            allLinks.push(entry);
    });

    j.nodes.forEach(function(entry) {
        filtNodes.push(entry);
    });

    j.links.forEach(function(entry) {
        if (entry.value > globalMinTan) {
            filtLinks.push(entry);
        }
    });

//     console.log('allNodes');
//     console.log(allNodes);
//     console.log('allLinks');
//     console.log(allLinks);
//     console.log('filtNodes');
//     console.log(filtNodes);
//       console.log('filtLinks');
//       console.log(filtLinks);

    // add labels to the viz

    var keys = Object.keys(j.info);

    var txtCnt = 0;

    keys.forEach(function(ky) {
        txtCnt++;
        if (Array.isArray(j.info[ky])) {
            viz.append("svg:text").text('Count of ' + ky + ': ' + j.info[ky].length).attr("x", 10).attr("y", txtCnt * 20).style("fill", "blue");
        } else {
            viz.append("svg:text").text(ky + ': ' + j.info[ky]).attr("x", 10).attr("y", txtCnt * 20).style("fill", "blue");
        }
    });

}

function drawIt() {

// set up for labels 
    labelAnchors = [];
    labelAnchorLinks = [];

// clear structures
    d3.select("#theForm\\:structPanel_content").selectAll("*").remove();

    var cnt = 0;

    filtNodes.forEach(function(nod) {

        if (selTargets.indexOf(nod.target) > -1 || selDrugNames.indexOf(nod.drugName) > -1 || selNscs.indexOf(nod.nsc) > -1) {

// update structure(s)
            if (nod.smiles && nod.nsc) {
                appendStruct(nod.smiles, nod.nsc);
            }

            labelAnchors.push({node: nod, type: 'par'});
            labelAnchors.push({node: nod, type: 'lbl'});

// link lbl to par 
            labelAnchorLinks.push({
                source: cnt,
                target: cnt + 1,
                weight: 1
            });

            cnt = cnt + 2;

        }
    });

    force.nodes(filtNodes).links(filtLinks).start();

    force2.nodes(labelAnchors).links(labelAnchorLinks).start();

    var link = viz.selectAll(".link")
            .data(filtLinks)
            .enter()
            .append("line")
            .attr("class", "link")
            .style("stroke-width", 4)
            .style("stroke", function(d) {
                return d.color ? d.color : 'gray'; // return d.color;
            })
            .style("stroke-opacity", 0.6);

    link.append("title").text(function(d) {
        return 'fp: ' + whichFingerprint + ' value: ' + d.value + ' nsc1: ' + d.source.nsc + ' nsc2: ' + d.target.nsc;
    });

    var node = viz.selectAll(".node").data(filtNodes).enter().append("svg:g").call(force.drag);

    // symbol based on extended node

    var circle = node.append("svg:path").attr("d", d3.svg.symbol().type(function(d) {
        return 'circle'; // return d.symbol;
    }).size(function(d) {
        return selTargets.indexOf(d.target) > -1 || selDrugNames.indexOf(d.drugName) > -1 || selNscs.indexOf(d.nsc) > -1 ? 300 : 100
    })).attr("fill", function(d) {
        d.mwkcolor = catColor(selTargets.indexOf(d.target));
        return selTargets.indexOf(d.target) > -1 || selDrugNames.indexOf(d.drugName) > -1 || selNscs.indexOf(d.nsc) > -1 ? d.mwkcolor : d.symbolColor;
    }).attr("stroke", "black");

    node.append("title").text(function(d) {
        return "NSC: " + d.nsc + " drugName: " + d.drugName + "target: " + d.target;
    });

    var anchorLink = viz.selectAll("line.anchorLink").data(labelAnchorLinks).enter().append("svg:line").attr("class", "anchorLink").style("stroke", "red");

    var anchorNode = viz.selectAll("g.anchorNode").data(force2.nodes()).enter().append("svg:g").attr("class", "anchorNode").each(function(d, i) {

        d3.select(this).append("svg:circle").attr("r", 0).style("fill", "red");

        var t = d3.select(this).append("svg:g");

// text if lbl node
        if (d.type === 'lbl') {
// if nsc with smiles, use a structure
// else if nsc with drug name, or moltId then do text

            if (d.node.nsc && d.node.smiles === 'ALWAYS FAILS') {

                var newSvg = d3.select(this).append("svg").attr("width", 100).attr("height", 100).append("svg:g");
                newSvg.append("svg:rect").attr("x", 0).attr("y", 0).attr("width", 100).attr("height", 100).attr("fill", "green");
                newSvg.append("svg:image").attr("x", 10).attr("y", 10).attr("xlink:href", function(d) {
                    return 'http://localhost:8080/sarcomacompare/StructureServlet?smiles=' + d.node.smiles + '&title=' + d.node.nsc;
                }).attr("width", 80).attr("height", 80);

                /* this only renders the structure
                 d3.select(this).append("image").attr("x", 10).attr("y", 10).attr("xlink:href", function(d) {
                 return 'http://localhost:8080/sarcomacompare/StructureServlet?smiles=' + d.node.smiles + '&title=' + d.node.nsc;
                 }).attr("width", 80).attr("height", 80);
                 */

            } else if (d.node.nsc) {

                console.log('mwkcolor: ' + d.node.mwkcolor);
                console.log('d.node:');
                console.log(d.node);

                t.append("svg:text").text(function(d) {
                    return d.node.target;
                }).style("fill", d.node.mwkcolor).attr("font-size", "40");

                t.append("svg:text").text(function(d) {
                    return d.node.drugName ? d.node.nsc + ' ' + d.node.drugName : d.node.nsc;
                }).style("fill", d.node.mwkcolor).attr("y", 20);

            } else if (d.node.moltId) {

//                console.log('mwkcolor: ' + d.node.mwkcolor);
//                console.log('d.node:');
//                console.log(d.node);

                t.append("svg:text").text(function(d) {
                    return d.node.geneSymbol ? d.node.moltId + ' ' + d.node.geneSymbol + ' ' + d.node.target : d.node.moltId + ' ' + d.node.target;
                }).style("fill", d.node.mwkcolor);


            } else {
// THIS SHOULDN'T HAPPEN - EITHER NSC or moltId
            }

        }

    });

    var updateLink = function() {
        this.attr("x1", function(d) {
            return d.source.x;
        }).attr("y1", function(d) {
            return d.source.y;
        }).attr("x2", function(d) {
            return d.target.x;
        }).attr("y2", function(d) {
            return d.target.y;
        });
    }

    var updateNode = function() {
        this.attr("transform", function(d) {
            return "translate(" + d.x + "," + d.y + ")";
        });
    }
    
//    http://bl.ocks.org/mbostock/1129492
//    node.attr("cx", function(d) { return d.x = Math.max(radius, Math.min(width - radius, d.x)); })
//        .attr("cy", function(d) { return d.y = Math.max(radius, Math.min(height - radius, d.y)); });

    force.on("tick", function() {

        force2.start();

        node.call(updateNode);

        anchorNode.each(function(d, i) {

            if (d.type === 'par') {
                d.x = d.node.x;
                d.y = d.node.y;
            } else {
                var b = this.childNodes[1].getBBox();
                var diffX = d.x - d.node.x;
                var diffY = d.y - d.node.y;
                var dist = Math.sqrt(diffX * diffX + diffY * diffY);
                var shiftX = b.width * (diffX - dist) / (dist * 2);
                shiftX = Math.max(-b.width, Math.min(0, shiftX));
                var shiftY = 5;
                this.childNodes[1].setAttribute("transform", "translate(" + shiftX + "," + shiftY + ")");
            }
        });

        anchorNode.call(updateNode);

        link.call(updateLink);

        anchorLink.call(updateLink);
    });



// debug links from nodes
    // go through all links

    //http://stackoverflow.com/questions/8739072/highlight-selected-node-its-links-and-its-children-in-a-d3-force-directed-grap

//http://stackoverflow.com/questions/22116909/selecting-path-within-g-element-and-change-style

    filtLinks.forEach(function(lnk) {
        if ((lnk.source.nsc === 756656 || lnk.target.nsc === 756656) && (lnk.source.nsc === 762382 || lnk.target.nsc === 762382)) {
            console.log('lnk.s: ' + lnk.source.nsc + ' lnk.t: ' + lnk.target.nsc);
        }
    });

    /*
     filtLinks.forEach(function(lnk1) {
     filtLinks.forEach(function(lnk2) {
     if (lnk1.source.nsc != lnk1.target.nsc && lnk1.source.nsc === lnk2.target.nsc && lnk1.target.nsc === lnk2.source.nsc) {
     console.log('dup link: ' + lnk1.source.nsc + ' ' + lnk1.target.nsc + ' to ' + lnk2.source.nsc + ' ' + lnk2.target.nsc);
     }
     });
     });
     */

    //viz.selectAll(".link").select("title").each(function(){
    //		console.log('select title');
    //		console.log(this);
    //});

}

function zoom() {
    viz.attr("transform", "translate(" + d3.event.translate + ")scale(" + d3.event.scale + ")");
}