$(".home-li").click(function () {
    $(".articles-li").removeClass("active");
    $(".home-li").addClass("active");
});

$(".articles-li").click(function () {
    $(".home-li").removeClass("active");
    $(".articles-li").addClass("active");
});