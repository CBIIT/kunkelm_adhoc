//http://www.javascriptkit.com/javatutors/preloadimagesplus.shtml
function imagesFromAllWidgets(widgetArray) {
    var arrayOfImages = [], countLoadedImages = 0
    var postaction = function () {
    }
//var widgetArray=(typeof arr!="object")? [widgetArray] : widgetArray
    function imageloadpost() {
        countLoadedImages++
        if (countLoadedImages == widgetArray.length) {
            postaction(arrayOfImages) //call postaction and pass in newimages array as parameter
        }
    }
    for (var i = 0; i < widgetArray.length; i++) {
        arrayOfImages[i] = new Image()
        var tempImage = new Image()
        var widg = widgetArray[i]
        tempImage = widg.exportAsImage()
        arrayOfImages[i].src = tempImage.src
        arrayOfImages[i].onload = function () {
            imageloadpost()
        }
        arrayOfImages[i].onerror = function () {
            imageloadpost()
        }
    }
    return {//return blank object with done() method
        done: function (f) {
            postaction = f || postaction //remember user defined callback functions to be called when images load
        }
    }
}
var widgetArray = [];
function clearWidgetArray() {
    while (widgetArray.length > 0) {
        widgetArray.pop();
    }
}
function addAllCharts(propertyNameStartsWith, countColumns) {
    clearWidgetArray();
    for (var propertyName in PrimeFaces.widgets) {
        if (propertyName.lastIndexOf(propertyNameStartsWith, 0) === 0) {
            widgetArray.push(PrimeFaces.widgets[propertyName]);
        }
    }
    imagesFromAllWidgets(widgetArray).done(
            function (images) {
                var theCanvas = document.createElement('canvas');
                var xDim;
                if (countColumns !== undefined && countColumns !== null && countColumns !== '' && !isNaN(countColumns)) {
                    xDim = countColumns;
                } else {
                    xDim = images.length;
                }
                var yDim = 0;
                var maxWidth = 0;
                var maxHeight = 0;
                for (i = 0; i < images.length; i++) {
                    if (images[i].width > maxWidth)
                        maxWidth = images[i].width
                    if (images[i].height > maxHeight)
                        maxHeight = images[i].height
                }
                var tileWidth = maxWidth
                var tileHeight = maxHeight
// calculate the yDim
                yDim = Math.ceil(images.length / xDim);
                theCanvas.width = tileWidth * xDim;
                theCanvas.height = tileHeight * yDim;
                var ctx = theCanvas.getContext("2d");
                var xCnt = -1;
                var yCnt = -1;
                for (var i = 0; i < images.length; i++) {
                    if (i % xDim == 0) {
                        xCnt = -1;
                        yCnt++;
                    }
                    xCnt++;
                    ctx.drawImage(images[i], xCnt * tileWidth, yCnt * tileHeight);
                }
//            console.log('Writing text to canvas');
//            ctx.fillStyle = "blue";
//            ctx.font = "bold 40px sans-serif";
//            ctx.fillText("Test of bold 40px sans-serif", 100, 100);
                var imag = document.createElement("img");
                imag.src = theCanvas.toDataURL();
                imag.onload = function () {
                    $("[id='exportableImageOutputPanel']").empty();
                    $("[id='exportableImageOutputPanel']").append(imag);
                }
            })
    PF('exportImageDlg').show();
}
function exportHeatMap(incomingJson, TRANSPOSE, UNITDELTA) {
//    console.log(' TRANSPOSE: ' + TRANSPOSE);
//	
//    if (TRANSPOSE){
//        console.log(' TRANSPOSE is true');
//    } else {
//        console.log(' TRANSPOSE is false');
//    }
//	
//    console.log(' UNITDELTA: ' + UNITDELTA);
//	
//    if (UNITDELTA){
//        console.log(' UNITDELTA is true');
//    } else {
//        console.log(' UNITDELTA is false');
//    }
    var gsonFromSarcoma = JSON.stringify(incomingJson);
    var parsedObject = JSON.parse(gsonFromSarcoma);
    var title1 = parsedObject.title1;
    var title2 = parsedObject.title2;
    var title3 = parsedObject.title3;
    var gridXheaderList = parsedObject.gridXheaderList;
    var gridYheaderList = parsedObject.gridYheaderList;
    var gridXY = parsedObject.gridXY;
    
    // MWK, 21Feb2015 trying to fix overlapping labels by adding a little white space
    
    var maxLenXhead = parsedObject.maxLenXhead;
    maxLenXhead = maxLenXhead + 5;
    var maxLenYhead = parsedObject.maxLenYhead;
    maxLenYhead = maxLenYhead + 5;
    
// TRANSPOSE => horizontal layout with row labels to the left and column labels below
// gridXY[][] is [col][row] 
// if TRANSPOSE use [row][col]

    if (TRANSPOSE) {
        var rowHeaderList = gridXheaderList;
        var colHeaderList = gridYheaderList;
        var maxColLabelLength = maxLenYhead;
        var maxRowLabelLength = maxLenXhead;
    } else {
        var rowHeaderList = gridYheaderList;
        var colHeaderList = gridXheaderList;
        var maxColLabelLength = maxLenXhead;
        var maxRowLabelLength = maxLenYhead;
    }
    var unitSize = 20;
    var colLabelWidth = ((maxColLabelLength + 4) * unitSize) / 2;
    var rowLabelWidth = ((maxRowLabelLength + 4) * unitSize) / 2;
    var gutterVertical = 10 * unitSize;
    var gutterHorizontal = 10 * unitSize;
    var cntRows = rowHeaderList.length;
    var cntCols = colHeaderList.length;
    var gridTopLeftX = gutterHorizontal + rowLabelWidth;
    var gridTopLeftY = gutterVertical + colLabelWidth;
    var gridXsize = cntCols * unitSize;
    var gridYsize = cntRows * unitSize;
    var prettyGap = 0.5 * unitSize;
    var xTrans;
    var yTrans;
    var rowCnt;
    var colCnt;
    var theCanvas = document.createElement('canvas');
// labels to the left and/or right
    theCanvas.width = gutterHorizontal + rowLabelWidth + gridXsize + rowLabelWidth + gutterHorizontal;
// labels abover and/or below 
    theCanvas.height = gutterVertical + colLabelWidth + gridYsize + colLabelWidth + gutterVertical;
    var ctx = theCanvas.getContext("2d");
    ctx.font = "normal " + unitSize + "px sans-serif";
// titles
    ctx.fillStyle = "black";
    ctx.fillText(title1, unitSize, unitSize);
    ctx.fillText(title2, unitSize, 2 * unitSize);
    ctx.fillText(title3, unitSize, 3 * unitSize);
// GRID
// translate to the beginning of grid
    xTrans = gridTopLeftX;
    yTrans = gridTopLeftY;
    ctx.translate(xTrans, yTrans);
    for (var rowCnt = 0; rowCnt < cntRows; rowCnt++) {
        for (var colCnt = 0; colCnt < cntCols; colCnt++) {
            if (TRANSPOSE) {
                var thisCell = gridXY[rowCnt][colCnt];
            } else {
                var thisCell = gridXY[colCnt][rowCnt];
            }
            ctx.beginPath();
// rect(x, y, w, h)  x,y is top-left
// have to back off of x and y by the unitSize
            ctx.rect(colCnt * unitSize, (rowCnt - 1) * unitSize, unitSize, unitSize);
            if (thisCell.isNullValue) {
                ctx.fillStyle = 'black';
            } else {
                if (UNITDELTA) {
                    ctx.fillStyle = thisCell.unitDeltaColor;
                } else {
                    ctx.fillStyle = thisCell.valueColor;
                }
            }
            ctx.fill();
            ctx.lineWidth = 1;
            ctx.strokeStyle = 'black';
            ctx.stroke();
            ctx.closePath();
        }
    }
// translate back    
    ctx.translate(-1 * xTrans, -1 * yTrans);
// COL and ROW HEADERS
    if (TRANSPOSE) {
        var colHeaderBelow = true;
        var colHeaderAbove = false;
        var rowHeaderLeft = true;
        var rowHeaderRight = false;
    } else {
        var colHeaderBelow = false;
        var colHeaderAbove = true;
        var rowHeaderLeft = true;
        var rowHeaderRight = false;
    }
// BELOW
    if (colHeaderBelow) {
        xTrans = gridTopLeftX + unitSize;
// +colLabelWidth so that pivot is relative to bottom of the rectangle
        yTrans = gridTopLeftY + gridYsize + colLabelWidth - unitSize + prettyGap;
        ctx.translate(xTrans, yTrans);
// col headers, rotated
        for (var colCnt = 0; colCnt < cntCols; colCnt++) {
            var thisHeader = colHeaderList[colCnt];
            ctx.save();
            ctx.translate(colCnt * unitSize, 0);
            ctx.rotate(-Math.PI / 2);
            ctx.beginPath();
            ctx.rect(0, -1 * unitSize, colLabelWidth, unitSize);
            ctx.fillStyle = thisHeader.rgbColor;
            ctx.strokeStyle = "black";
            ctx.fill();
            ctx.fillStyle = "black";
// panelCde "bottom" justified
            ctx.textAlign = "start";
            ctx.fillText(thisHeader.label2, 0.15 * unitSize, -0.15 * unitSize);
// cellLine "top" justified        
            ctx.textAlign = "end";
            ctx.fillText(thisHeader.label1, colLabelWidth - 0.15 * unitSize, -0.15 * unitSize);
            ctx.closePath();
            ctx.restore();
        }
// translate back
        ctx.translate(-1 * xTrans, -1 * yTrans);
    }
// ABOVE
    if (colHeaderAbove) {
        xTrans = gridTopLeftX + unitSize;
        yTrans = gridTopLeftY - unitSize - prettyGap;
        ctx.translate(xTrans, yTrans);
        for (var colCnt = 0; colCnt < cntCols; colCnt++) {
            var thisHeader = colHeaderList[colCnt];
            ctx.save();
            ctx.translate(colCnt * unitSize, 0);
            ctx.rotate(-Math.PI / 2);
            ctx.beginPath();
            ctx.rect(0, -1 * unitSize, colLabelWidth, unitSize);
            ctx.fillStyle = thisHeader.rgbColor;
            ctx.strokeStyle = "black";
            ctx.fill();
            ctx.fillStyle = "black";
            ctx.textAlign = "start";
            ctx.fillText(thisHeader.label1, 0.15 * unitSize, -0.15 * unitSize);
            ctx.textAlign = "end";
            ctx.fillText(thisHeader.label2, colLabelWidth - 0.15 * unitSize, -0.15 * unitSize);
            ctx.closePath();
            ctx.restore();
        }
// translate back
        ctx.translate(-1 * xTrans, -1 * yTrans);
    }
// LEFT
    if (rowHeaderLeft) {
        xTrans = gridTopLeftX - rowLabelWidth - prettyGap;
        yTrans = gridTopLeftY;
        ctx.translate(xTrans, yTrans);
        for (var rowCnt = 0; rowCnt < cntRows; rowCnt++) {
            var thisHeader = rowHeaderList[rowCnt];
            ctx.beginPath();
            ctx.rect(0, (rowCnt - 1) * unitSize, rowLabelWidth, unitSize);
            ctx.fillStyle = thisHeader.rgbColor;
            ctx.strokeStyle = "black";
            ctx.fill();
            ctx.fillStyle = "black";
            ctx.textAlign = "start";
            ctx.fillText(thisHeader.label2, 0.15 * unitSize, rowCnt * unitSize - 0.15 * unitSize);
            ctx.textAlign = "end";
            ctx.fillText(thisHeader.label1, rowLabelWidth - 0.15 * unitSize, rowCnt * unitSize - 0.15 * unitSize);
            ctx.closePath();
        }
// translate back
        ctx.translate(-1 * xTrans, -1 * yTrans);
    }
// RIGHT 
    if (rowHeaderRight) {
        xTrans = gridTopLeftX + gridXsize + prettyGap;
        yTrans = gridTopLeftY;
        ctx.translate(xTrans, yTrans);
        for (var rowCnt = 0; rowCnt < cntRows; rowCnt++) {
            var thisHeader = rowHeaderList[rowCnt];
            ctx.beginPath();
            ctx.rect(0, (rowCnt - 1) * unitSize, rowLabelWidth, unitSize);
            ctx.fillStyle = thisHeader.rgbColor;
            ctx.strokeStyle = "black";
            ctx.fill();
            ctx.fillStyle = "black";
            ctx.textAlign = "start";
            ctx.fillText(thisHeader.label1, 0.15 * unitSize, rowCnt * unitSize - 0.15 * unitSize);
            ctx.textAlign = "end";
            ctx.fillText(thisHeader.label2, rowLabelWidth - 0.15 * unitSize, rowCnt * unitSize - 0.15 * unitSize);
            ctx.closePath();
        }
// translate back
        ctx.translate(-1 * xTrans, -1 * yTrans);
    }
    var imag = document.createElement("img");
    imag.src = theCanvas.toDataURL();
    imag.onload = function () {
        $("[id='exportableImageOutputPanel']").empty();
        $("[id='exportableImageOutputPanel']").append(imag);
    }
    PF('exportImageDlg').show();
}