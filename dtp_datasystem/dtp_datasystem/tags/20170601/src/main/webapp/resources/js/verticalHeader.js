(function($) {
    $.fn.rotateTableCellContent = function(options) {
        /* MWK modified 20Feb2016 for all four webkits
         * and tweaks to .css
         */
        /* based on 
         Version 1.0
         7/2011
         Written by David Votrubec (davidjs.com) and
         Michal Tehnik (@Mictech) for ST-Software.com
         */
        var cssClass = ((options) ? options.className : false) || "vertical";
        var cellsToRotate = $('.' + cssClass, this);
// find the max label length and use to set the height for all rotations
        var maxLength = 0;
        cellsToRotate.each(
                function() {
                    var cell = $(this);
                    var newText = cell.text();
                    if (newText.length > maxLength) {
                        maxLength = newText.length;
                    }
                }
        );
        var betterCells = [];
        cellsToRotate.each(
                function() {
                    var cell = $(this);
                    var newText = cell.text();
// all sized by maxLength of header
// with a buffer
                    var cw = maxLength * 10 + 50;
                    var ch = 10;
                    var newDiv = $('<div>', {
                        height: cw,
                        width: ch
                    });
                    var newInnerDiv = $('<div>', {
                        text: newText,
                        'class': 'rotated'
                    });
                    newInnerDiv.css('-webkit-transform-origin', (cw / 2) + 'px ' + (cw / 2) + 'px');
                    newInnerDiv.css('-moz-transform-origin', (cw / 2) + 'px ' + (cw / 2) + 'px');
                    newInnerDiv.css('-ms-transform-origin', (cw / 2) + 'px ' + (cw / 2) + 'px');
                    newInnerDiv.css('-o-transform-origin', (cw / 2) + 'px ' + (cw / 2) + 'px');
                    // newInnerDiv.css('transform-origin', (cw / 2) + 'px ' + (cw / 2) + 'px');
                    newDiv.append(newInnerDiv);
                    
                    console.log('pushing to betterCells');
                    
                    betterCells.push(newDiv);
                }
        );
        cellsToRotate.each(function(i) {
            $(this).html(betterCells[i]);
        });
    };
})(jQuery);