$(function(){

    let csrfHeader = $("meta[name=_csrf_header]").attr("content");
    let csrfToken = $("meta[name=_csrf]").attr("content");

    $("#review_post").on("click", function(){

        let content = $("#review_content").val();
        let itemId = $("#itemId").val();

        $.ajax({
            url : "/item/review/write",
            type: "post",
            data : { content : content, itemId: itemId },
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(result){
            alert("리뷰 작성 완료");
        }).fail(function(result ){
            alert("리뷰작성에 실패하였습니다.");
        });
    });



});