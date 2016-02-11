var top, svgObj, hiddentext,comein,ltext;
window.onload = function() {
    svgObj = document.getElementById("greg-svg");

    svgObj.style.width = "100%";



    //comein.animate({ transform: 't100,300' }, 1000, mina.bounce );
}
window.addEventListener("scroll",function(e) {
    top = window.pageYOffset;
    if(top.pageYOffset == 0) {
        svgObj.style.width = "100%";

    } else if(top.pageYOffset > 0) {
        svgObj.style.width = "100%";

    }


}, false);





