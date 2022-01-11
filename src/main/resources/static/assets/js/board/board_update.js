$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header]").attr("content");

    $("#boardUpdate").on("click", function(){

        let data = $("form[name=board]").serialize();
        let boardId = $(this).attr("data-boardId");

        $.ajax({
            url : "/board/write/"+boardId,
            type : "patch",
            data : data,
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(result){

            if(result === "success"){
                alert("게시글 수정에 성공했습니다.");
                history.back();
                // window.location.replace("/board/freeBoard")
            } else if(result === "login"){
                alert("로그인이 필요한 서비스입니다.");
                let c = confirm("로그인 페이지로 이동하시겠습니까? (작성하던 내용은 모두 사라집니다.)");
                if(c){
                    window.location.replace("/login");
                }
            } else if(result === "role"){
                alert("작성자만 수정 가능합니다.");
                history.back();
            } else{
                alert(result);
                return false;
            }
        }).fail(function(result){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });

    });

});