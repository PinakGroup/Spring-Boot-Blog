$("#about-user").click(function (event) {
    event.preventDefault();
    $(".articles").hide();
    $("#account").show();
});

$("#my-articles").click(function (event) {
    event.preventDefault();
    $(".articles").show();
    $("#account").hide();
});
