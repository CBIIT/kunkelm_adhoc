//http://www.javascriptkit.com/javatutors/preloadimagesplus.shtml

function structuresAsGridImage() {

    // var images = $("[id$='strcImg']");

    var images = $('.parentStructure');

    var theCanvas = document.createElement('canvas');

    var xDim = 10;
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

    var ctx = theCanvas.getContext('2d');

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

    imag.onload = function () {

        $("[id='datasystemForm:exportableImageOutputPanel']").empty();
        $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);

        $("[id='exportableImageOutputPanel']").empty();
        $("[id='exportableImageOutputPanel']").append(imag);

    }

    PF('exportImageDlg').show();

}

function imageGridFromSubstructures() {

    var images = $("[id$='strcImg']");

    var theCanvas = document.createElement('canvas');

    var xDim = 10;
    var yDim = 0;

    var maxWidth = 0;
    var maxHeight = 0;

    var howMany = 0;

    if (images.length > 200) {
        howMany = 200;
    } else {
        howMany = images.length;
    }

    for (i = 0; i < howMany; i++) {
        if (images[i].width > maxWidth)
            maxWidth = images[i].width
        if (images[i].height > maxHeight)
            maxHeight = images[i].height
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

    imag.onload = function () {

        $("[id='datasystemForm:exportableImageOutputPanel']").empty();
        $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);

    }

    PF('exportImageDlg').show();

}
