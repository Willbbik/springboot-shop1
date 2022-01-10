$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header").attr("content");

    // 대댓글 작성
    $(document).on("click", ".btn_write", function(){

        let commentId = $(this).attr("data-commentId");
        let content = $("#content").val();
        let hide = $("input:radio[name=hide]:checked").val();

        if(content == ""){
            alert("내용을 입력해주세요.");
            return false;
        }

        let data = {
            id : commentId,
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

    // 대댓글 수정
    $(document).on("click", ".btn_update", function(){

           let reCommentId = $(this).attr("data-reCommentId");
           let content = $("#content").val();
           let hide = $("input:radio[name=hide]:checked").val();

           if(content == ""){
               alert("내용을 입력해주세요.");
               return false;
           }

           let data = {
               id : reCommentId,
               content : content,
               hide : hide
           }

           $.ajax({
               url : "/board/reComment",
               type : "patch",
               data : data,
               beforeSend : function(xhr){
                   xhr.setRequestHeader(csrfHeader, csrfToken);
               }
           }).done(function(result){
              if(result === "success"){
                   self.close();
                   opener.location.href = "javascript:getCommentList();";
              }else if(result === "login"){
                   alert("로그인 후 대댓글작성이 수정이 가능합니다.");
                   return false;
              }else if(result === "role"){
                    alert("작성자만 수정 가능합니다.");
                    return false;
              }else{
                   alert(result);
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