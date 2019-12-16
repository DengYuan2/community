/**
 * 提交回复
 */
function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();

    comment2target(questionId, 1, content);
}

function comment2target(targetId, type, content) {
    if (!content) {//js可以直接如此判断是否内容为空
        alert("臣妾看不到隐身的评论啊~~");
        return;
    }
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType: "application/json",
        data: JSON.stringify({
            "parentId": targetId,
            "content": content,
            "type": type
        }),
        success: function (response) {
            if (response.code == 200) {//如果回复成功，刷新页面
                window.location.reload();
            } else {
                if (response.code == 2003) {//如果是没有登录的情况
                    var isAccepted = confirm(response.message);//如果用户点击确定按钮即代表要登录，故要跳转到登录页面
                    if (isAccepted) {//跳转到登录页面【此处弹出的是一个新页面】
                        window.open("https://github.com/login/oauth/authorize?client_id=8f4ed2938845b3183737&redirect_uri=http://localhost:8877/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);//登录成功后将此新打开的登录页面移除，配合index.html的12-22行【因为登录成功后会直接跳转到index页面啊】
                    }
                } else {
                    alert(response.message)
                }
            }

            console.log(response);
        },

        dataType: "json"
    });
}

function comment(e) {
    var commentId = e.getAttribute("data-id");
    var content = $("#input-" + commentId).val();

    comment2target(commentId, 2, content)
}

/**
 * 展开二级评论
 */
function collapseComments(e) {
    var id = e.getAttribute("data-id");
    var comments = $("#comment-" + id);
    //获取二级评论的展开状态
    // var collapse = e.getAttribute("data-collapse");
    // if (collapse){//如果展开了
    //     //折叠二级评论
    //     comments.removeClass("in");
    //     e.removeAttribute("data-collapse");
    // }else{
    //     //展开二级评论
    //     comments.addClass("in");//加上in这个class【应该是js自带的】，使产生折叠被打开的效果
    //     //标记二级评论展开状态，为了使再次点击评论icon时能够折叠
    //     e.setAttribute("data-collapse","in");
    // }

    //以上是老师的方法，以下是网友的建议，更简单
    if (comments.hasClass("in")) {
        comments.removeClass("in");//如果已经展开了，点击这个icon的时候会删除in这个class，使折叠回去
        e.classList.remove("active");
    } else {

        $.getJSON("/comment/" + id, function (data) {//data为拿到的二级评论，是ResultDTO类型的json格式，好像
            console.log(data);
            debugger
            var commentBody = $("comment-body-"+id);
            var items=[];
            $.each( data.data, function(comment) {//data.data的后面一个data是ResultDTO的属性哦，为commentDTO类型
            var c = $("<div/>",{
                "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                html:comment.content
                });

                items.push(c);
            });

            commentBody.append(
                $("<div/>",{
                    "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 collapse sub-comments",
                    "id":"comment-"+id,
                    html:items.join("")
                }));
            $("<div/>",{
                "class":"col-lg-12 col-md-12 col-sm-12 col-xs-12 collapse sub-comments",
                "id":"comment-"+id,
                html:items.join("")
            }).appendTo(commentBody);

            comments.addClass("in");//加上in这个class【应该是js自带的】，使产生折叠被打开的效果
            e.classList.add("active");//community.css下写了一个.menu.active{}，使comment icon被点击展开时保持蓝色

        });
    }
}
 