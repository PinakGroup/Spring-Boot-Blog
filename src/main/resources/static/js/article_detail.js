//解析markdown文本
$(function () {
    editormd.emoji = {
        path: "https://www.webpagefx.com/tools/emoji-cheat-sheet/graphics/emojis/",
        ext: ".png"
    };

    editormd.twemoji = {
        path: "http://twemoji.maxcdn.com/72x72/",
        ext: ".png"
    };

    editormd.markdownToHTML("article-content", {
        // markdown: $("#markdown-text").text(),
        htmlDecode: "style,script,iframe",  // you can filter tags decode
        emoji: true,
        taskList: true,
        tex: true,  // 默认不解析
        flowChart: true,  // 默认不解析
        sequenceDiagram: true  // 默认不解析
    });
});

function updateLikeNum(obj, isAuthenticated) {
    if (!isAuthenticated) {
        // alert("请先登录！");
        $("#login-Modal").modal('show');
    } else {
        var articleId = $("#article-id").val();
        var likeNum = $("#auth-like-num").text();
        var author = $("#author").text();
        var hasLike = obj.getElementsByClassName("active").length > 0;
        // alert(hasLike);
        var data = {
            'articleId': articleId,
            'likeNum': likeNum,
            'author': author
        };
        $.ajax({
            url: "/article/likeNum/" + hasLike,
            method: "post",
            data: data,
            success: function (data) {
                if (data['success'] === true) {
                    likeNum = data['likeNum'];
                    var span = $(obj.getElementsByClassName("glyphicon-heart"));
                    if (hasLike) {
                        span.removeClass("active");
                    } else {
                        span.addClass("active");
                    }
                    $("#auth-like-num").text(likeNum);
                }
            },
            error: function (xhr) {
                alert("服务器错误！请稍后重试！");
                console.log(xhr);
            }
        })
    }
}

$("#login-form").submit(function (event) {
    event.preventDefault();
    $("#message").text("");
    $("#tips").hide();
    $.ajax({
        url: "/auth/login",
        type: "post",
        data: $(this).serialize(),
        success: function () {
            window.location.reload();
        },
        error: function (data) {
            console.log("error: " + data['status']);
            if (data['status'] === 401) {
                $("#message").text("用户名或者密码错误");
                $("#tips").show();
            } else {
                $("#message").text("服务器繁忙！");
                $("#tips").show();
            }
        }
    })
});

//评论框
let commentEditor;

//评论编辑器
function comment() {
    editormd.emoji = {
        path: "https://www.webpagefx.com/tools/emoji-cheat-sheet/graphics/emojis/",
        ext: ".png"
    };

    editormd.twemoji = {
        path: "http://twemoji.maxcdn.com/72x72/",
        ext: ".png"
    };

    commentEditor = editormd("comment-editormd", {
        width: "100%",
        height: 300,
        syncScrolling: "single",
        path: "/static/editor/lib/",
        saveHTMLToTextarea: true,
        // emoji: true,
        toolbarIcons: function () {
            return ["undo", "redo", "|", "bold", "hr", "|", "link", "reference-link", "code", "preformatted-text", "code-block", "|", "preview", "watch", "fullscreen"]
        }
    });

}

//弹出富文本编辑器
$("#comment").click(function () {
    $("#comment-form").show();
    comment();
});

//评论提交
$("#comment-form").submit(function (event) {
    event.preventDefault();
    let content = $("#commentContent").val();
    if ($.trim(content) === "") {
        $(".error-message").text("评论内容不能为空！");
        $("#error-tips").show();
        return false;
    }
    $.ajax({
        url: "/comment/add",
        type: "post",
        data: $(this).serialize(),
        success: function (data) {
            if (data['success'] === true) {
                $("#commentContent").val("");
                $("#comment-form").hide();
                let username = data['comment']['username'];
                let content = data['comment']['commentContent'];
                let time = datetimeFormat(data['comment']['commentTime']);
                //动态添加评论到底部
                let comment_html =
                    `<div class="comment-detail">
                        <div class="author">
                            <a href="#" class="comment-user">${username}</a>
                            <span class="comment-time">${time}</span>
                        </div>
                        <p class="comment-content">${content}</p>
                        <a class="good" href="#">赞</a>
                        <a class="reply-comment" href="#">回复</a>
                    </div>`;
                console.log(comment_html);
                $("#history-comments").append(comment_html);
                $(".comment-message").text("评论成功！");
                $("#comment-tips").show();
            }else if (data['success'] === false) {
                $(".error-message").text(data['message']);
                $("#error-tips").show();
            }
        }
    });
    return false;
});

//评论区登录按钮弹出模态框
function login() {
    $("#login-Modal").modal('show');
}

//日期格式化
function datetimeFormat(date) {
    date = new Date(date);
    let year = date.getFullYear();
    let month = date.getMonth() >= 9 ? date.getMonth() + 1 : '0' + (date.getMonth() + 1) ;
    let day = date.getDate() > 10 ? date.getDate(): '0' + date.getDate();
    let hour = date.getHours() > 10 ? date.getHours() : '0' + date.getHours();
    let minute = date.getMinutes() > 10 ? date.getMinutes() : '0' + date.getMinutes();
    let second = date.getSeconds() > 10 ? date.getSeconds() : '0' + date.getSeconds();
    return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
}

//删除评论
function deleteComment(obj) {
    let commentId = $(obj).parent().parent().parent().attr("id");
    let comment_Id = commentId.split("-")[1];
    let articleId = $("#articleId").val();
    $.ajax({
        url: "/comment/delete/" + comment_Id + "/" + articleId,
        type: "delete",
        success: function (data) {
            if (data['success'] === true) {
                $("#" + commentId).hide();
            }
            $(".comment-message").text(data['message']);
            $("#comment-tips").show();
        }
    });
}