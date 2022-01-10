let openWin;

$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header").attr("content");

    getCommentList();

    $(document).on("click", ".comment_delete", function(){

        let commentId = $(this).closest("li").attr("data-commentId");

        let c = confirm("해당 댓글을 삭제하시겠습니까?");
        if(c){
            $.ajax({
                 url : "/board/comment",
                 type : "delete",
                 data : { commentId : commentId },
                 beforeSend : function(xhr){
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                 },
                 traditional: true
            }).done(function(result){

                if(result === "success"){
                    getCommentList();
                }else if(result === "role"){
                    alert("작성자만 삭제가 가능합니다.");
                    return false;
                }else{
                    alert("로그인 후 삭제 가능합니다.");
                    let n = confirm("로그인 페이지로 이동하시겠습니까?");
                    if(n){
                        location.href = "/login";
                    }
                }
            }).fail(function(result){
                alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
                return false;
            });
        }

    });


    $(document).on("click", ".comment_update", function(){

           let commentId = $(this).closest(".list_reply").find(".rp_admin").attr("data-commentId");
           openWin = window.open("/board/comment/"+commentId, 'google', 'width=500,height=500');
    });

    // 댓글 작성
    $(document).on("click", ".btn_enter",  function(){

        let boardId = $("#mArticle").attr("data-boardId");
        let content = $("#content").val();
        let hide = $("input:radio[name=hide]:checked").val();

        let data = {
            content : content,
            hide : hide,
            boardId : boardId
        }

        $.ajax({
            url : "/board/comment/write",
            type : "post",
            data : data,
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(result){

            if(result === "success"){
                $("#content").val("");
                getCommentList();
            }else if(result === "login"){
                alert("로그인 후 댓글작성이 가능합니다.");
                let c = confirm("로그인 페이지로 이동하시겠습니까? (작성중이던 내용은 사라집니다.)");
                if(c){
                    location.href = "/login";
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



    // 대댓글 작성
    $(document).on("click", ".reComment_write", function(){

        let commentId = $(this).closest(".list_reply").find(".rp_admin").attr("data-commentId");
        openWin = window.open("/board/reComment/write/"+commentId, 'google', 'width=500,height=500');


    });




})

// 댓글 가져오기
function getCommentList(){

    let boardId = $("#mArticle").attr("data-boardId");

    $.ajax({
        url : "/board/commentList",
        type : "get",
        data : {
            boardId : boardId
        }
    }).done(function(result){
       $(".area_reply").html(result);
    }).fail(function(result){
       alert("에러가 발생했습니다. \n댓글을 가져올 수 없습니다.");
    });



}
