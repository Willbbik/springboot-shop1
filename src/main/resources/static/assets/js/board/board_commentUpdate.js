$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header").attr("content");

    // 댓글 수정
    $(document).on("click", ".btn_submit", function(){

        let commentId = $(this).attr("data-commentId");
        let content = $("#content").val();
        let hide = $("input:radio[name=hide]:checked").val();

        if(content == ""){
            alert("내용을 입력해주세요.");
            return false;
        }

        $.ajax({
            url : "/board/comment",
            type : "patch",
            data : {
                id : commentId,
                content : content,
                hide : hide,
            },
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(result){
            if(result === "success"){
                self.close();
                opener.location.href = "javascript:getCommentList();";
            }else if(result === "login"){
                alert("로그인 후 이용가능합니다.");
                return false;
            }else if(result === "role"){
                alert("작성자만 수정 가능합니다.");
                return false;
            }else{
                alert(result);
                return false;
            }
        }).fail(function(){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });
    });

});