$(function(){

    // 상품 상세보기에 들어가면 바로 리뷰 가져오게끔
    getReviewList();

    // csrf
    let csrfHeader = $("meta[name=_csrf_header]").attr("content");
    let csrfToken = $("meta[name=_csrf]").attr("content");

    // 리뷰 작성
    $(document).on("click", "#review_post", function(){

        let content = $("#review_content").val();
        let itemId = $("#itemId").val();

        if(content == ""){
            alert("내용을 입력해주세요.");
            return false;
        }

        $.ajax({
            url : "/item/review/write",
            type: "post",
            data : { content : content, itemId: itemId },
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(result){
            if(result == "success"){
                $("#review_content").val("");
                getReviewList();
                alert("리뷰를 등록하였습니다.");
            }else{
                alert(result);
            }
        }).fail(function(result){
            alert("리뷰 등록에 실패하였습니다. \n잠시후 다시 시도해주시기 바랍니다.");
        });
    });

});

function getReviewList(){
    let itemId = $("#itemId").val();

    $.ajax({
        url : "/item/reviewList/get",
        type : "get",
        data : { itemId : itemId },
    }).done(function(result){
        $("#info_container_2").html(result);
    }).fail(function(result){
        alert("에러가 발생했습니다. \n리뷰를 가져올 수 없습니다.");
    });
}