function post() {
    var questionId = $("#question_id").val();
    var content = $("#comment_content").val();
    $.ajax({
        type: "POST",
        url: "/comment",
        contentType:"application/json",
        data: JSON.stringify({
            "parentId":questionId,
            "content":content,
            "type":1
        }),
        success: function (response){
            if (response.code==200){//如果回复成功，则将回复功能的框及相关事物隐藏
                $("#comment_section").hide();
            }else{
                if (response.code==2003){//如果是没有登录的情况
                    var isAccepted = confirm(response.message);//如果用户点击确定按钮即代表要登录，故要跳转到登录页面
                    if (isAccepted){//跳转到登录页面【此处弹出的是一个新页面】
                        window.open("https://github.com/login/oauth/authorize?client_id=8f4ed2938845b3183737&redirect_uri=http://localhost:8877/callback&scope=user&state=1");
                        window.localStorage.setItem("closable", true);//登录成功后将此新打开的登录页面移除，配合index.html的12-22行【因为登录成功后会直接跳转到index页面啊】
                    }
                }else {
                    alert(response.message)
                }
            }

            console.log(response);
        },

        dataType: "json"
    });
}