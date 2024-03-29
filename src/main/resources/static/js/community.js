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
        var subCommentContainer = $("#comment-" + id);//额，上面获取过了呀
        if (subCommentContainer.children().length != 1) {//如果点击了一次，打开子评论，关掉后打开，就会又重复一遍，所以需要确定是否已经加载过
            //了，避免重复加载;此处就是如果已经有了子评论的元素【因为原本就只有"评论一下哟~~"回复框那一个子元素】，直接展开即可，不用再次加载
            comments.addClass("in");//加上in这个class【应该是js自带的】，使产生折叠被打开的效果
            e.classList.add("active");//community.css下写了一个.menu.active{}，使comment icon被点击展开时保持蓝色
        } else {//相当于直接写在了html中啊,写的是question.html中注释掉的68-84行代码，就是照着它写的呀
            $.getJSON("/comment/" + id, function (data) {//data为拿到的二级评论，是ResultDTO类型的json格式，好像

                comments.addClass("in");//加上in这个class【应该是js自带的】，使产生折叠被打开的效果
                e.classList.add("active");//community.css下写了一个.menu.active{}，使comment icon被点击展开时保持蓝色

                $.each(data.data.reverse(), function (index, comment) {//data.data的后面一个data是ResultDTO的属性哦，为commentDTO类型
                    var mediaLeftElement = $("<div/>", {
                        "class": "media-left"
                    }).append($("<img/>", {
                        "class": "media-object img-rounded media-body",
                        "src": comment.user.avatarUrl
                    }));

                    var mediaBodyElement = $("<div/>", {
                        "class": "media-body"
                    }).append($("<h5/>", {
                        "class": "media-heading",
                        "html": comment.user.name
                    })).append($("<div/>", {
                        "html": comment.content
                    })).append($("<div/>", {
                        "class": "menu"
                    }).append($("<span/>", {//注意层级，此处是该div里的span
                        "class": "pull-right",
                        "html": moment(comment.gmtCreate).format('YYYY-MM-DD')//引入了momment.js，通过该网站https://momentjs.com/，得知该格式与一般格式不同，这部分是大写的
                    })));

                    var mediaElement = $("<div/>", {
                        "class": "media"
                    }).append(mediaLeftElement).append(mediaBodyElement);

                    var commentElement = $("<div/>", {
                        "class": "col-lg-12 col-md-12 col-sm-12 col-xs-12 comments",
                    }).append(mediaElement);

                    subCommentContainer.prepend(commentElement);//prepend() 方法在被选元素的开头(仍位于内部)插入指定内容；
                    // 如果直接用append，那么就会在"评论一下哟~~"那个框的后面了，不符合要求。但是这样也存
                    // 在一个问题：拿到的评论内容是按时间在后的倒序排列的，一旦再如此倒过来，就不是最近发表的言论在最上面了，所以在data.data后加个reverse来解决

                });
            });

        }

    }
}

function selectTags(e) {
    var value = e.getAttribute("data-tag");
    var previous = $("#tag").val();
    //为了防止重复增加某一标签
    if (previous.indexOf(value)==-1){//没有该标签时才添加
        if (previous){//已经有了用户自己写的标签时
            $("#tag").val(previous+','+value);
        }else{
            $("#tag").val(value);
        }
    }


}
 
function showSelectTag() {
    $("#select-tag").show();
}
