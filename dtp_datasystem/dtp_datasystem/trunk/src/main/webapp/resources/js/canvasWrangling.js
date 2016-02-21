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

  console.log('maxWidth: ' + maxWidth);
  console.log('maxHeight: ' + maxHeight);

  var tileWidth = maxWidth + 0.1 * maxWidth;
  var tileHeight = maxHeight + 0.1 * maxHeight;

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
    
    // this is redundant while experimenting with 
    // declaring the dialog inside or outside of the form

    $("[id='datasystemForm:exportableImageOutputPanel']").empty();
    $("[id='datasystemForm:exportableImageOutputPanel']").append(imag);

    $("[id='exportableImageOutputPanel']").empty();
    $("[id='exportableImageOutputPanel']").append(imag);

  }

  PF('exportImageDlg').show();

}
