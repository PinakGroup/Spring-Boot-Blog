function login() {
    $("#register-container").hide();
    $("#resetPassword-container").hide();
    $("#login-container").show();
}

function register() {
    $("#resetPassword-container").hide();
    $("#login-container").hide();
    $("#register-container").show();
}

function resetPassword() {
    $("#login-container").hide();
    $("#register-container").hide();
    $("#resetPassword-container").show();
}

$(".auth").click(function (event) {
    event.preventDefault();
    var href = $(this).attr("href");
    console.log(href);
    if (href==="/user/login") {
        login();
    }else if (href==="/user/register") {
        register();
    }else if (href==="/user/forgetPassword") {
        resetPassword();
    }
});

$("#register-form").submit(function (event) {
    event.preventDefault();
    $(".alert").hide();
    var data = $(this).serialize();
    $.ajax({
        url: "/register",
        method: "post",
        data: data,
        success: function (data) {
            console.log(data);
            if (data["success"] === true) {
                $("#register-container").hide();
                $("#login-container").show();
                $("#login-tips").text("注册成功！请登录！");
                $("#login-tips-alert").show();
            }else if (data["success"] === false) {
                if ($.trim(data["UsernameExistError"]) !== "") {
                    $("#UsernameExistError").text(data["UsernameExistError"]);
                    $(".UsernameExistError").show();
                }
                if ($.trim(data["PassWordNotMatchError"]) !== "") {
                    $("#PassWordNotMatchError").text(data["PassWordNotMatchError"]);
                    $(".PassWordNotMatchError").show();
                }
                if ($.trim(data["EmailExistError"]) !== "") {
                    $("#EmailExistError").text(data["EmailExistError"]);
                    $(".EmailExistError").show();
                }
                if ($.trim(data["ValidationCodeError"]) !== "") {
                    $("#ValidationCodeError").text(data["ValidationCodeError"]);
                    $(".ValidationCodeError").show();
                }
            } else {
                $("#register-tips").text(data["ServerError"]);
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    })

});

$("#register-sendCode").click(function (event) {
    event.preventDefault();
    var email = $("#register-email").val().trim();
    if (email === "") {
        $("#register-tips").text("邮箱不能为空！");
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
            if (data["isSuccess"] === true) {
                $("#register-tips").text(data["tips"]);
            } else {
                $("#register-tips").text(data["tips"]);
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
});

$("#resetPassword-form").submit(function (event) {
    event.preventDefault();
    $(".alert").hide();
    var data = $(this).serialize();
    $.ajax({
        url: "/resetPassword",
        method: "post",
        data: data,
        success: function (data) {
            console.log(data);
            if (data["success"] === true) {
                $("#resetPassword-container").hide();
                $("#login-container").show();
                $("#login-tips").text(data["ResetPasswordSuccess"]);
                $("#login-tips-alert").show();
            }else if (data["success"] === false) {
                if ($.trim(data["EmailNotExistError"]) !== "") {
                    $("#EmailNotExistError").text(data["EmailNotExistError"]);
                    $(".resetPassword-email-alert").show();
                }
                if ($.trim(data["RepeatPasswordNotMatchError"]) !== "") {
                    $("#RepeatPasswordNotMatchError").text(data["RepeatPasswordNotMatchError"]);
                    $(".resetPassword-password-alert").show();
                }
                if ($.trim(data["ResetPasswordValidationCodeError"]) !== "") {
                    $("#ResetPasswordValidationCodeError").text(data["ResetPasswordValidationCodeError"]);
                    $(".resetPassword-code-alert").show();
                }
            } else {
                $("#resetPassword-tips").text(data["ServerError"]);
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    })

});

$("#resetPassword-sendCode").click(function (event) {
    event.preventDefault();
    var email = $("#resetPassword-email").val().trim();
    if (email === "") {
        $("#resetPassword-tips").text("邮箱不能为空！");
        return false;
    }
    $.ajax({
        url: "/sendValidationCode",
        type: "POST",
        data: {
            "email": email,
            "codeForWhat": "resetPasswordValidationCode"
        },
        success: function (data) {
            console.log(data);
            if (data["isSuccess"] === true) {
                $("#resetPassword-tips").text(data["tips"]);
            } else {
                $("#resetPassword-tips").text(data["tips"]);
            }
        },
        error: function (xhr) {
            console.log(xhr);
        }
    });
});
