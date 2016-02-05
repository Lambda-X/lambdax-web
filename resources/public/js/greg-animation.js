var top, svgObj, hiddentext;
window.onload = function() {
    svgObj = document.getElementById("greg-svg");

    svgObj.style.width = "140%";


    var s = Snap(Snap("#greg-svg").node);


    hiddentext = s.select("#hidden-text")
        .attr({
            opacity: 0
        });


}
window.addEventListener("scroll",function(e) {
    top = window.pageYOffset;
    if(top.pageYOffset > 0) {
        svgObj.style.width = "100%";
        hiddentext.attr({opacity: 1});
    } else if(top.pageYOffset == 0) {
        svgObj.style.width = "140%";
        hiddentext.attr({opacity: 0});
    }


}, false);





