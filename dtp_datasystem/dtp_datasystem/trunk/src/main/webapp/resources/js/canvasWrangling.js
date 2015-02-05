//http://www.javascriptkit.com/javatutors/preloadimagesplus.shtml

function structuresAsGridImage(){
  
    // var images = $("[id$='strcImg']");
    
    var images = $('.parentStructure');
  
    var theCanvas = document.createElement('canvas');

    var xDim = 10;
    var yDim = 0;

    var maxWidth = 0; 
    var maxHeight = 0;

    for (i = 0; i < images.length; i++){  
        if (images[i].width > maxWidth) maxWidth = images[i].width
        if (images[i].height > maxHeight) maxHeight = images[i].height  
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

    //      console.log('Writing text to canvas');
    //      ctx.fillStyle = "blue";
    //      ctx.font = "bold 40px sans-serif";
    //      ctx.fillText("Test of bold 40px sans-serif", 100, 100);

    var imag = document.createElement("img");
  
    //imag.id = 'panelPlotImage'
    //imag.width = tileWidth * xDim;
    //imag.height = tileHeight * yDim;

    imag.src = theCanvas.toDataURL();

    imag.onload=function(){
      
        $("[id='datasystemForm:exportableImageOutputPanel']").empty();
        $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);
    
        $("[id='exportableImageOutputPanel']").empty();
        $("[id='exportableImageOutputPanel']").append(imag);
        
    }
    
    PF('exportImageDlg').show();
  
}

function imageGridFromSubstructures(){
  
    var images = $("[id$='strcImg']");
  
    var theCanvas = document.createElement('canvas');

    var xDim = 10;
    var yDim = 0;

    var maxWidth = 0; 
    var maxHeight = 0;
  
    var howMany = 0;
  
    if (images.length > 200){
        howMany = 200;
    } else {
        howMany = images.length;
    }

    for (i = 0; i < howMany; i++){  
        if (images[i].width > maxWidth) maxWidth = images[i].width
        if (images[i].height > maxHeight) maxHeight = images[i].height  
    }
    
    var tileWidth = maxWidth
    var tileHeight = maxHeight

    // calculate the yDim

    yDim = Math.ceil(howMany / xDim);

    theCanvas.width = tileWidth * xDim;
    theCanvas.height = tileHeight * yDim;

    var ctx = theCanvas.getContext("2d");
  
    var xCnt = -1;
    var yCnt = -1;

    for (var i = 0; i < howMany; i++) {

        if (i % xDim == 0) {
            xCnt = -1;
            yCnt++;
        }

        xCnt++;

        ctx.drawImage(images[i], xCnt * tileWidth, yCnt * tileHeight);

    }

    //      console.log('Writing text to canvas');
    //      ctx.fillStyle = "blue";
    //      ctx.font = "bold 40px sans-serif";
    //      ctx.fillText("Test of bold 40px sans-serif", 100, 100);

    var imag = document.createElement("img");
  
    //imag.id = 'panelPlotImage'
    //imag.width = tileWidth * xDim;
    //imag.height = tileHeight * yDim;

    imag.src = theCanvas.toDataURL();

    imag.onload=function(){
  
        $("[id='datasystemForm:exportableImageOutputPanel']").empty();
        $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);
    
    }
     
    PF('exportImageDlg').show();
  
}

function imagesFromAllWidgets(widgetArray){
  
    var arrayOfImages=[], countLoadedImages=0
    var postaction=function(){}
    //var widgetArray=(typeof arr!="object")? [widgetArray] : widgetArray
    function imageloadpost(){
        countLoadedImages++
        if (countLoadedImages==widgetArray.length){
            postaction(arrayOfImages) //call postaction and pass in newimages array as parameter
        }
    }
    for (var i=0; i<widgetArray.length; i++){
    
        arrayOfImages[i]=new Image()
        
        var tempImage = new Image()
        var widg = widgetArray[i]
        tempImage = widg.exportAsImage()        
        arrayOfImages[i].src=tempImage.src
        arrayOfImages[i].onload=function(){
            imageloadpost()
        }
        arrayOfImages[i].onerror=function(){
            imageloadpost()
        }
    }
    return { //return blank object with done() method
        done:function(f){
            postaction=f || postaction //remember user defined callback functions to be called when images load
        }
    }
}

var panelChartWidgetArray = [];

function clearPanelCanvas() {
    while (panelChartWidgetArray.length > 0) {
        panelChartWidgetArray.pop();
    }
}

function appendToPanelCanvas(widgetVar) {
    panelChartWidgetArray.push(widgetVar);
}

function addAllPanels() {
  
    clearPanelCanvas();
    // each button click calls appendToPanelCanvas to add the widget to the array
    $("[id$='appendToPanelCanvasButton']").click();
    
    imagesFromAllWidgets(panelChartWidgetArray).done(

        function(images){
  
            var theCanvas = document.createElement('canvas');

            var xDim = 5;
            var yDim = 0;

            var maxWidth = 0; 
            var maxHeight = 0;

            for (i = 0; i < images.length; i++){  
                if (images[i].width > maxWidth) maxWidth = images[i].width
                if (images[i].height > maxHeight) maxHeight = images[i].height  
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

            //      console.log('Writing text to canvas');
            //      ctx.fillStyle = "blue";
            //      ctx.font = "bold 40px sans-serif";
            //      ctx.fillText("Test of bold 40px sans-serif", 100, 100);

            var imag = document.createElement("img");
            //imag.id = 'panelPlotImage'
            //imag.width = tileWidth * xDim;
            //imag.height = tileHeight * yDim;

            imag.src = theCanvas.toDataURL();

            imag.onload=function(){
  
                $("[id='datasystemForm:exportableImageOutputPanel']").empty();
                $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);
    
            }
  
        })
    
    PF('exportImageDlg').show();
}

var meanGraphChartWidgetArray = [];

function clearMeanGraphCanvas() {  
    while (meanGraphChartWidgetArray.length > 0) {
        meanGraphChartWidgetArray.pop();
    }
}

function appendToMeanGraphCanvas(widgetVar) {  
    meanGraphChartWidgetArray.push(widgetVar);
}

function addAllMeanGraphs(arrayOfArray) {  
    
    if (arrayOfArray){  
        for (i = 0; i < arrayOfArray.length; i++){    
            var arr = arrayOfArray[i];    
            for (j = 0; j < arr.length; j++){
                //console.log(arr[j]);      
                }
        }  
    }
  
    /*
  NSC	740
DrugName	Methotrexate
Mean	-7.38
StandardDeviation	1.13
CountCellLines	62
MinVal	-8.63
MaxVal	-5
  
  ['740','Methotrexate','-7.38','62','-8.63', '-5']
  nsc, name, mean, sd, ccl, min, max
  
  http://stackoverflow.com/questions/7497750/how-do-you-pass-parametersfrom-javascript-into-remotecommand-then-send-it-to
  
  remoteCommandFunctionName([{name: 'name1', value: 'value1'}, {name: 'name2', value: 'value2'}]);
  
  http://forum.primefaces.org/viewtopic.php?f=3&t=3532 Can't get array data using Callback Parameters
  
  
  http://stackoverflow.com/questions/4730696/javascript-array-of-arrays
  
  
 */
  
    clearMeanGraphCanvas();
    $("[id$='appendToMeanGraphCanvasButton']").click();
  
    imagesFromAllWidgets(meanGraphChartWidgetArray).done(

        function(images){
  
            var theCanvas = document.createElement('canvas');

            var maxWidth = 0; 
            var maxHeight = 0;

            for (i = 0; i < images.length; i++){  
                if (images[i].width > maxWidth) maxWidth = images[i].width
                if (images[i].height > maxHeight) maxHeight = images[i].height  
            }
    
            var tileWidth = maxWidth
            var tileHeight = maxHeight

            theCanvas.width = images.length * tileWidth;
            theCanvas.height = tileHeight;

            var ctx = theCanvas.getContext("2d");
      
            for (var i = 0; i < images.length; i++) {

                ctx.drawImage(images[i], i * tileWidth, 0);

            }

            //      console.log('Writing text to canvas');
            //      ctx.fillStyle = "blue";
            //      ctx.font = "bold 40px sans-serif";
            //      ctx.fillText("Test of bold 40px sans-serif", 100, 100);

            var imag = document.createElement("img");
      
            imag.src = theCanvas.toDataURL();

            imag.onload=function(){
  
                $("[id='datasystemForm:exportableImageOutputPanel']").empty();
                $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);
    
            }
    
        })
    
    PF('exportImageDlg').show();
  
}

function addAllNanoStringMeanGraphs(arrayOfArray) {  
  
    if (arrayOfArray){
        for (i = 0; i < arrayOfArray.length; i++){    
            var arr = arrayOfArray[i]    
            for (j = 0; j < arr.length; j++){
                //console.log(arr[j]);      
                }
        }  
    }
    
    clearMeanGraphCanvas();
    $("[id$='appendNanoStringToMeanGraphCanvasButton']").click();
  
    imagesFromAllWidgets(meanGraphChartWidgetArray).done(

        function(images){
  
            var theCanvas = document.createElement('canvas');

            var maxWidth = 0; 
            var maxHeight = 0;

            for (i = 0; i < images.length; i++){  
                if (images[i].width > maxWidth) maxWidth = images[i].width
                if (images[i].height > maxHeight) maxHeight = images[i].height  
            }
    
            var tileWidth = maxWidth
            var tileHeight = maxHeight

            theCanvas.width = images.length * tileWidth;
            theCanvas.height = tileHeight;

            var ctx = theCanvas.getContext("2d");
      
            for (var i = 0; i < images.length; i++) {

                ctx.drawImage(images[i], i * tileWidth, 0);

            }

            //      console.log('Writing text to canvas');
            //      ctx.fillStyle = "blue";
            //      ctx.font = "bold 40px sans-serif";
            //      ctx.fillText("Test of bold 40px sans-serif", 100, 100);

            var imag = document.createElement("img");
      
            imag.src = theCanvas.toDataURL();

            imag.onload=function(){
  
                $("[id='datasystemForm:exportableImageOutputPanel']").empty();
                $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);
    
            }
    
        })
    
    PF('exportImageDlg').show();
  
}

function addAllAffymetrixMeanGraphs(arrayOfArray) {  
    
    if (arrayOfArray){
        for (i = 0; i < arrayOfArray.length; i++){    
            var arr = arrayOfArray[i];    
            for (j = 0; j < arr.length; j++){
                //console.log(arr[j]);      
                }
        }  
    }
  
    /*
  NSC	740
DrugName	Methotrexate
Mean	-7.38
StandardDeviation	1.13
CountCellLines	62
MinVal	-8.63
MaxVal	-5
  
  ['740','Methotrexate','-7.38','62','-8.63', '-5']
  nsc, name, mean, sd, ccl, min, max
  
  http://stackoverflow.com/questions/7497750/how-do-you-pass-parametersfrom-javascript-into-remotecommand-then-send-it-to
  
  remoteCommandFunctionName([{name: 'name1', value: 'value1'}, {name: 'name2', value: 'value2'}]);
  
  http://forum.primefaces.org/viewtopic.php?f=3&t=3532 Can't get array data using Callback Parameters
  
  
  http://stackoverflow.com/questions/4730696/javascript-array-of-arrays
  
  
 */
  
    clearMeanGraphCanvas();
    $("[id$='appendAffymetrixToMeanGraphCanvasButton']").click();
  
    imagesFromAllWidgets(meanGraphChartWidgetArray).done(

        function(images){
  
            var theCanvas = document.createElement('canvas');

            var maxWidth = 0; 
            var maxHeight = 0;

            for (i = 0; i < images.length; i++){  
                if (images[i].width > maxWidth) maxWidth = images[i].width
                if (images[i].height > maxHeight) maxHeight = images[i].height  
            }
    
            var tileWidth = maxWidth
            var tileHeight = maxHeight

            theCanvas.width = images.length * tileWidth;
            theCanvas.height = tileHeight;

            var ctx = theCanvas.getContext("2d");
      
            for (var i = 0; i < images.length; i++) {

                ctx.drawImage(images[i], i * tileWidth, 0);

            }

            //      console.log('Writing text to canvas');
            //      ctx.fillStyle = "blue";
            //      ctx.font = "bold 40px sans-serif";
            //      ctx.fillText("Test of bold 40px sans-serif", 100, 100);

            var imag = document.createElement("img");
      
            imag.src = theCanvas.toDataURL();

            imag.onload=function(){
  
                $("[id='datasystemForm:exportableImageOutputPanel']").empty();
                $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);
    
            }
    
        })
    
    PF('exportImageDlg').show();
  
}

/*
context.beginPath();
      context.rect(188, 50, 200, 100); x, y, w, h
      context.fillStyle = 'yellow';
      context.fill();
      context.lineWidth = 7;
      context.strokeStyle = 'black';
      context.stroke();
*/

function exportHeatMapImage(rowArray){
  
    //var rowArray = [['Cell Line','740 Methotrexate','750 Busulfan','752 Thioguanine'],['LNS A549/ATCC','-7.49','-5.00','-5.16'],['SAR MHM-8','-7.01','-5.00','-5.29'],['SAR MHM-25','-6.73','-5.00','-5.38'],['ASP ASPS-1','-5.00','-5.00','-5.00'],['CHO SW 1353','-7.99','-5.00','-6.04'],['DLS DDLS','-5.00','-5.00','-5.45'],['DLS LS141','-7.39','-5.00','-5.46'],['EPS VA-ES-BJ','-7.83','-5.00','-5.72'],['EWS A-673','-8.11','-5.00','-6.22'],['EWS CHLA-10','-8.01','-5.00','-5.47'],['EWS ES-1','-8.49','-5.00','-6.19'],['EWS ES-2','-8.06','-5.00','-6.02'],['EWS ES-3','-8.10','-5.00','-6.80'],['EWS ES-4','-6.93','-5.00','-6.32'],['EWS ES-6','-7.49','-5.00','-5.19'],['EWS ES-7','-8.13','-5.00','-6.00'],['EWS ES-8','-8.18','-5.00','-6.23'],['EWS EW8','-8.55','-5.00','-6.01'],['EWS RD-ES','-8.28','-5.00','-5.87'],['EWS SK-N-MC','-8.63','-5.00','-6.07'],['EWS TC-71','-8.14','-5.00','-6.34'],['EWS CHP-100','-8.40','-5.00','-6.54'],['EWS CHLA-25','-7.93','-5.00','-6.48'],['EWS CHLA-258','-7.84','-5.00','-6.11'],['EWS CHLA-32','-7.69','-5.00','-6.50'],['EWS COG-E-352','-8.03','-5.00','-6.19'],['EWS TC-32','-8.02','-5.36','-6.97'],['EWS SK-ES-1','-5.00','-5.00','-5.08'],['FIS Hs 913.T','-5.00','-5.00','-5.24'],['FIS HT-1080','-8.00','-5.00','-6.09'],['FIS SW 684','-5.00','-5.00','-5.24'],['GCS Hs 706.T','-5.00','-5.00','-5.00'],['LMS SK-LMS-1','-7.74','-5.00','-5.88'],['LMS SK-UT-1','-7.86','-5.00','-5.30'],['LMS SK-UT-1B','-8.03','-5.00','-5.49'],['MPN MPNST','-7.38','-5.00','-5.96'],['MPN ST8814','-7.28','-5.00','-5.79'],['OST HOS','-8.17','-5.00','-6.77'],['OST Hu09','-6.06','-5.00','-5.22'],['OST KHOS NP','-8.04','-5.00','-6.18'],['OST KHOS-240S','-7.97','-5.00','-6.22'],['OST KHOS-312H','-8.00','-5.00','-6.10'],['OST OHS','-8.51','-5.00','-6.34'],['OST SAOS-2','-7.88','-5.00','-5.82'],['OST SJSA-1','-7.55','-5.00','-5.46'],['OST U-2 OS','-7.41','NULL','-5.77'],['RHT G-401','-7.50','-5.00','-5.74'],['RHM A-204','-7.39','-5.00','-5.66'],['RHM Hs 729','-5.00','-5.00','-5.00'],['RHM RD','-7.95','-5.00','-5.00'],['RHM Rh18','-5.00','-5.00','-5.00'],['RHM Rh30','-7.67','-5.00','-5.64'],['RHM SJCRH30(RMS 13)','-7.66','-5.00','-5.00'],['RHM Rh28','-5.00','-5.00','-5.00'],['RHM Rh36','-7.93','-5.00','-5.00'],['RHM Rh41','-7.66','-5.00','-5.00'],['SYS HSSY-II','-8.23','-5.00','-6.51'],['SYS SW 982','-7.38','-5.00','-6.04'],['SYS SYO-1','-7.93','-5.00','-8.00'],['UTS MES-SA','-8.39','-5.00','-5.77'],['UTS MES-SA Dx5','-8.31','-5.00','-6.07'],['SCS Hs 132.T','-5.00','-5.00','-5.00']];
  
    //console.log('rowArray');
    //console.log(rowArray);
    
    var sizeAnchor = 20;
    var labelColWidth = 30 * sizeAnchor;  
    var topRowOffset = 20 * sizeAnchor;
  
    var cntRows = rowArray.length;
    var cntCols = rowArray[0].length;

    //console.log('cntRows ' + cntRows);
    //console.log('cntCols ' + cntCols);
  
    var theCanvas = document.createElement('canvas');
  
    theCanvas.width = labelColWidth + (cntCols - 1) * sizeAnchor;
    // theCanvas.height = cntRows * sizeAnchor + topRowOffset;
    // extend the height to include labels BELOW the grid
    theCanvas.height = cntRows * sizeAnchor + topRowOffset + topRowOffset;

    //console.log('width ' + theCanvas.width);
    //console.log('height ' + theCanvas.height);

    var ctx = theCanvas.getContext("2d");  
    
    // first row is a special case
    var rowCnt = 0;    
    var colCnt = 0;
    ctx.fillStyle = "black";
    ctx.font = "normal " + sizeAnchor + "px sans-serif";
    ctx.fillText(rowArray[rowCnt][colCnt], 0 * labelColWidth, rowCnt * sizeAnchor + topRowOffset);  
    // then rotate the remaining columns in the first row
    for (var colCnt = 1; colCnt < cntCols; colCnt++){
        
        // labels ABOVE the grid
        
        ctx.save();
        ctx.translate(labelColWidth + colCnt * sizeAnchor, rowCnt * sizeAnchor + topRowOffset - 0.5 * sizeAnchor);
        ctx.rotate(-Math.PI/2);  
        //ctx.textAlign = "center";
        ctx.fillText(rowArray[rowCnt][colCnt], 0, 0);
        ctx.restore();
        
    // write the same label BELOW the grid
        
    //        ctx.save();
    //        ctx.translate(labelColWidth + colCnt * sizeAnchor, rowCnt * sizeAnchor + topRowOffset - 0.5 * sizeAnchor + cntRows * sizeAnchor);
    //        ctx.rotate(-Math.PI/2);  
    //        ctx.textAlign = 'end';
    //        ctx.fillText(rowArray[rowCnt][colCnt], 0, 0);
    //        ctx.restore();
    //        
        
    }
  
    // then finish the rest of the rows
    // start rowCnt at 1
    for (var rowCnt = 1; rowCnt < cntRows; rowCnt++) {
        
        for (var colCnt = 0; colCnt < cntCols; colCnt++){
      
            if (colCnt == 0){
                
                ctx.fillStyle = "black";
                ctx.font = "normal " + sizeAnchor + "px sans-serif";    
                ctx.textAlign = "end";
                //ctx.fillText(rowArray[rowCnt][colCnt], colCnt * labelColWidth, rowCnt * sizeAnchor + topRowOffset);
                ctx.fillText(rowArray[rowCnt][colCnt], labelColWidth - 0.5 * sizeAnchor, rowCnt * sizeAnchor + topRowOffset);
                
            } else {
                
                ctx.beginPath();
                // rect(x, y, w, h)  x,y is top-left
                // have to back off of x and y by the sizeAnchor
                ctx.rect(labelColWidth + colCnt * sizeAnchor - sizeAnchor, rowCnt * sizeAnchor + topRowOffset - sizeAnchor, sizeAnchor, sizeAnchor);
                ctx.fillStyle = rowArray[rowCnt][colCnt];
                ctx.fill();
                ctx.lineWidth = 1;
                ctx.strokeStyle = 'black';
                ctx.stroke();
                
            }
      
        }
    }

    var imag = document.createElement("img");

    imag.src = theCanvas.toDataURL();

    imag.onload=function(){  
        $("[id='datasystemForm:exportableImageOutputPanel']").empty();
        $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);    
    }
 
    PF('exportImageDlg').show();

}

function exportHeatMapImageLabelsBelow(rowArray){
  
    //var rowArray = [['Cell Line','740 Methotrexate','750 Busulfan','752 Thioguanine'],['LNS A549/ATCC','-7.49','-5.00','-5.16'],['SAR MHM-8','-7.01','-5.00','-5.29'],['SAR MHM-25','-6.73','-5.00','-5.38'],['ASP ASPS-1','-5.00','-5.00','-5.00'],['CHO SW 1353','-7.99','-5.00','-6.04'],['DLS DDLS','-5.00','-5.00','-5.45'],['DLS LS141','-7.39','-5.00','-5.46'],['EPS VA-ES-BJ','-7.83','-5.00','-5.72'],['EWS A-673','-8.11','-5.00','-6.22'],['EWS CHLA-10','-8.01','-5.00','-5.47'],['EWS ES-1','-8.49','-5.00','-6.19'],['EWS ES-2','-8.06','-5.00','-6.02'],['EWS ES-3','-8.10','-5.00','-6.80'],['EWS ES-4','-6.93','-5.00','-6.32'],['EWS ES-6','-7.49','-5.00','-5.19'],['EWS ES-7','-8.13','-5.00','-6.00'],['EWS ES-8','-8.18','-5.00','-6.23'],['EWS EW8','-8.55','-5.00','-6.01'],['EWS RD-ES','-8.28','-5.00','-5.87'],['EWS SK-N-MC','-8.63','-5.00','-6.07'],['EWS TC-71','-8.14','-5.00','-6.34'],['EWS CHP-100','-8.40','-5.00','-6.54'],['EWS CHLA-25','-7.93','-5.00','-6.48'],['EWS CHLA-258','-7.84','-5.00','-6.11'],['EWS CHLA-32','-7.69','-5.00','-6.50'],['EWS COG-E-352','-8.03','-5.00','-6.19'],['EWS TC-32','-8.02','-5.36','-6.97'],['EWS SK-ES-1','-5.00','-5.00','-5.08'],['FIS Hs 913.T','-5.00','-5.00','-5.24'],['FIS HT-1080','-8.00','-5.00','-6.09'],['FIS SW 684','-5.00','-5.00','-5.24'],['GCS Hs 706.T','-5.00','-5.00','-5.00'],['LMS SK-LMS-1','-7.74','-5.00','-5.88'],['LMS SK-UT-1','-7.86','-5.00','-5.30'],['LMS SK-UT-1B','-8.03','-5.00','-5.49'],['MPN MPNST','-7.38','-5.00','-5.96'],['MPN ST8814','-7.28','-5.00','-5.79'],['OST HOS','-8.17','-5.00','-6.77'],['OST Hu09','-6.06','-5.00','-5.22'],['OST KHOS NP','-8.04','-5.00','-6.18'],['OST KHOS-240S','-7.97','-5.00','-6.22'],['OST KHOS-312H','-8.00','-5.00','-6.10'],['OST OHS','-8.51','-5.00','-6.34'],['OST SAOS-2','-7.88','-5.00','-5.82'],['OST SJSA-1','-7.55','-5.00','-5.46'],['OST U-2 OS','-7.41','NULL','-5.77'],['RHT G-401','-7.50','-5.00','-5.74'],['RHM A-204','-7.39','-5.00','-5.66'],['RHM Hs 729','-5.00','-5.00','-5.00'],['RHM RD','-7.95','-5.00','-5.00'],['RHM Rh18','-5.00','-5.00','-5.00'],['RHM Rh30','-7.67','-5.00','-5.64'],['RHM SJCRH30(RMS 13)','-7.66','-5.00','-5.00'],['RHM Rh28','-5.00','-5.00','-5.00'],['RHM Rh36','-7.93','-5.00','-5.00'],['RHM Rh41','-7.66','-5.00','-5.00'],['SYS HSSY-II','-8.23','-5.00','-6.51'],['SYS SW 982','-7.38','-5.00','-6.04'],['SYS SYO-1','-7.93','-5.00','-8.00'],['UTS MES-SA','-8.39','-5.00','-5.77'],['UTS MES-SA Dx5','-8.31','-5.00','-6.07'],['SCS Hs 132.T','-5.00','-5.00','-5.00']];
  
    //console.log('rowArray');
    //console.log(rowArray);
    
    var sizeAnchor = 20;
    var labelColWidth = 30 * sizeAnchor;  
    var topRowOffset = 20 * sizeAnchor;
  
    var cntRows = rowArray.length;
    var cntCols = rowArray[0].length;

    //console.log('cntRows ' + cntRows);
    //console.log('cntCols ' + cntCols);
  
    var theCanvas = document.createElement('canvas');
  
    theCanvas.width = labelColWidth + (cntCols - 1) * sizeAnchor;
    // theCanvas.height = cntRows * sizeAnchor + topRowOffset;
    // extend the height to include labels BELOW the grid
    theCanvas.height = cntRows * sizeAnchor + topRowOffset + topRowOffset;

    //console.log('width ' + theCanvas.width);
    //console.log('height ' + theCanvas.height);

    var ctx = theCanvas.getContext("2d");  
    
    // first row is a special case
    var rowCnt = 0;    
    var colCnt = 0;
    ctx.fillStyle = "black";
    ctx.font = "normal " + sizeAnchor + "px sans-serif";
    ctx.fillText(rowArray[rowCnt][colCnt], 0 * labelColWidth, rowCnt * sizeAnchor + topRowOffset);  
    // then rotate the remaining columns in the first row
    for (var colCnt = 1; colCnt < cntCols; colCnt++){
        
        // labels ABOVE the grid
        
        //        ctx.save();
        //        ctx.translate(labelColWidth + colCnt * sizeAnchor, rowCnt * sizeAnchor + topRowOffset - 0.5 * sizeAnchor);
        //        ctx.rotate(-Math.PI/2);  
        //        //ctx.textAlign = "center";
        //        ctx.fillText(rowArray[rowCnt][colCnt], 0, 0);
        //        ctx.restore();
        
        // write the same label BELOW the grid
        
        ctx.save();
        ctx.translate(labelColWidth + colCnt * sizeAnchor, rowCnt * sizeAnchor + topRowOffset - 0.5 * sizeAnchor + cntRows * sizeAnchor);
        ctx.rotate(-Math.PI/2);  
        ctx.textAlign = 'end';
        ctx.fillText(rowArray[rowCnt][colCnt], 0, 0);
        ctx.restore();
        
        
    }
  
    // then finish the rest of the rows
    // start rowCnt at 1
    for (var rowCnt = 1; rowCnt < cntRows; rowCnt++) {
        
        for (var colCnt = 0; colCnt < cntCols; colCnt++){
      
            if (colCnt == 0){
                
                ctx.fillStyle = "black";
                ctx.font = "normal " + sizeAnchor + "px sans-serif";    
                ctx.textAlign = "end";
                //ctx.fillText(rowArray[rowCnt][colCnt], colCnt * labelColWidth, rowCnt * sizeAnchor + topRowOffset);
                ctx.fillText(rowArray[rowCnt][colCnt], labelColWidth - 0.5 * sizeAnchor, rowCnt * sizeAnchor + topRowOffset);
                
            } else {
                
                ctx.beginPath();
                // rect(x, y, w, h)  x,y is top-left
                // have to back off of x and y by the sizeAnchor
                ctx.rect(labelColWidth + colCnt * sizeAnchor - sizeAnchor, rowCnt * sizeAnchor + topRowOffset - sizeAnchor, sizeAnchor, sizeAnchor);
                ctx.fillStyle = rowArray[rowCnt][colCnt];
                ctx.fill();
                ctx.lineWidth = 1;
                ctx.strokeStyle = 'black';
                ctx.stroke();
                
            }
      
        }
    }

    var imag = document.createElement("img");

    imag.src = theCanvas.toDataURL();

    imag.onload=function(){  
        $("[id='datasystemForm:exportableImageOutputPanel']").empty();
        $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);    
    }
 
    PF('exportImageDlg').show();

}

