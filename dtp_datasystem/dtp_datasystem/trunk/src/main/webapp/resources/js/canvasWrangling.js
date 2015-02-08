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

function appendToWidgetArray(widgetVar) {
  widgetArray.push(widgetVar);
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