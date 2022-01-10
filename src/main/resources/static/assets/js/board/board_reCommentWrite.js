$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header").attr("content");


    // 대댓글 전송
    $(document).on("click", ".btn_submit", function(){

        let commentId = $(this).attr("data-commentId");
        let content = $("#content").val();
        let hide = $("input:radio[name=hide]:checked").val();

        let data = {
            commentId : commentId,
            content : content,
            hide : hide
        }

        $.ajax({
            url : "/board/reComment/write",
            type : "post",
            data : data,
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(response, status, xhr){

//            let ct = xhr.getResponseHeader("content-type") || "";
//            if(ct.indexOf("html") > -1){
//                self.close();
//                opener.location.href = "javascript:getCommentList();";
//            }else{
//                alert(response);
//            }
           if(response === "success"){
                self.close();
                opener.location.href = "javascript:getCommentList();";
           }else if(response === "login"){
                alert("로그인 후 대댓글작성이 가능합니다.");
                return false;
           }else{
                alert(response);
                return false;
           }
        }).fail(function(result){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });
    });


});

function test(){

//    self.close();
//    $("#content", parent.opener.document).val("success");

    if(!opener.closed){


    }

}