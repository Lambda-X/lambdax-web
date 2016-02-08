var top, svgObj, hiddentext,comein,ltext;
window.onload = function() {
    svgObj = document.getElementById("greg-svg");

    svgObj.style.width = "140%";


    var s = Snap(Snap("#greg-svg").node);


    hiddentext = s.select("#hidden-text")
        .attr({
            opacity: 0
        });

    comein = s.select("#comein")
        .attr({
            opacity: 0
        });

    ltext = s.select("lambda-text").attr({
        display: none
    });

    //comein.animate({ transform: 't100,300' }, 1000, mina.bounce );
}
window.addEventListener("scroll",function(e) {
    top = window.pageYOffset;
    if(top.pageYOffset > 0) {
        svgObj.style.width = "100%";
        hiddentext.attr({opacity: 1});
        comein.attr({opacity:1});
        comein.animate({ transform: 't50,-500' }, 1000, mina.bounce );

    } else if(top.pageYOffset == 0) {
        svgObj.style.width = "140%";
        hiddentext.attr({opacity: 0});
        comein.animate({ transform: 't50,500' }, 1000, mina.bounce );
        comein.attr({opacity:0});

    }


}, false);





