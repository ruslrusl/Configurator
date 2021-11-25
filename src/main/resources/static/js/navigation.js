$(document).ready(function () {
    let hrefname = window.location.href.split('/')[3];
    if (hrefname) {
        $(".navbar-nav .nav-item").removeClass("active");
        let elem = $(".navbar-nav .nav-item a[href='/"+hrefname+"']");
        if (elem) {
            elem.parent().addClass('active');
        }
    }
});