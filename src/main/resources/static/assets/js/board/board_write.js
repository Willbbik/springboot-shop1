$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header]").attr("content");

    $("#boardWrite").on("click", function(){

        let data = $("form[name=board]").serialize();

        $.ajax({
            url : "/board/write",
            type : "post",
            data : data,
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(result){

            if(result === "success"){
                alert("게시글 작성에 성공했습니다.");
                window.location.replace("/board/freeBoard")
            } else if(result === "login"){
                alert("로그인이 필요한 서비스입니다.");
                let c = confirm("로그인 페이지로 이동하시겠습니까? (작성하던 내용은 모두 사라집니다.)");
                if(c){
                    window.location.replace("/login");
                }
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