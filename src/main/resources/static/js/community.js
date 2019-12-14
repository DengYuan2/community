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
                alert(response.message)
            }

            console.log(response);
        },

        dataType: "json"
    });
}