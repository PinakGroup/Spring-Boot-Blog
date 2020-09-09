$("#sendCode").click(function (event) {
    event.preventDefault();
    const email = $("#email").val().trim();
    if (email === "") {
        $("#tips").text("邮箱不能为空！");
        return false;
    }
    $.ajax({
        url: "/sendValidationCode",
        type: "POST",
        data: {
            "email": email,
            "codeForWhat": "registerValidationCode"
        },
        success: function (data) {
            console.log(data);
            console.log(typeof data["isSuccess"]);
            if (data["isSuccess"] === true) {
                $("#tips").text(data['tips']);
            } else {
                $("#tips").text(data['tips']);
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });

    //倒计时
    $(this).addClass("disabled");
    $(this).attr("disabled", true);
    let time = 60;
    $(this).text(time + "s");
    setInterval(() => {
        if (time <= 0) {
            $(this).removeClass("disabled");
            $(this).attr("disabled", false);
            $(this).text("获取验证码");
            return false;
        }
        time--;
        $(this).text(time + 's');
    }, 1000);
});