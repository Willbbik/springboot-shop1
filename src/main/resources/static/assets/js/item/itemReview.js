$(function(){

     // 상품 상세보기에 들어가면 바로 리뷰 가져오게끔
     getReviewList();

     // csrf
     let csrfHeader = $("meta[name=_csrf_header]").attr("content");
     let csrfToken = $("meta[name=_csrf]").attr("content");

    // tab1 상품정보
    $("#tab1").on("click", function(){

        let info2 = $("#info_container_2");
        let info3 = $("#info_container_3");

        info2.css("display", "");
        info3.css("display", "none");
        getReviewList();
    });

    // tab2 Review
    $("#tab2").on("click", function(){

        let info2 = $("#info_container_2");
        let info3 = $("#info_container_3");

        info2.css("display", "");
        info3.css("display", "none");
        getReviewList();
    });

    $(document).on("click", "ul.detail_review_select li", function(){

        $(".active_review_select").attr("class", "");
        $(this).addClass("active_review_select");
        getReviewList();
    });

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
            if(result === "0"){
                $("#review_content").val("");
                getReviewList();
                alert("리뷰를 등록하였습니다.");
            }else if(result === "-1"){
                alert("로그인이 필요한 서비스입니다.");
                return false;
            }else if(result === "-2"){
                alert("리뷰는 한 상품당 하나만 작성 가능합니다.");
                return false;
            }else if(result === "-3"){
                alert("상품 구매자만 리뷰 작성 가능합니다.");
                return false;
            }else{
                alert(result);
                return false;
            }
        }).fail(function(result){
            alert("리뷰 등록에 실패하였습니다. \n잠시후 다시 시도해주시기 바랍니다.");
        });
    });

    // 리뷰 더보기 버튼
    $(document).on("click", "#btn_more_review", function(){
        let lastReviewId = $("#lastReviewId").val();
        let itemId = $("#itemId").val();

        $.ajax({
            url : "/item/reviewList/get",
            type : "get",
            data : { itemId : itemId,
                     lastReviewId : lastReviewId,
                     more : "more" }
        }).done(function(result){
            $("#lastReviewId").remove();
            $(".review_content_main_box").append(result);
        }).fail(function(result){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해보시기 바랍니다.");
        });
    });


});


 function getReviewList(){

    let itemId = $("#itemId").val();
    let sort = $(".active_review_select").attr("id");

    $.ajax({
        url : "/item/reviewList/get",
        type : "get",
        data : { itemId : itemId , sort : sort },
    }).done(function(result){
        $("#review_content_main_box").html(result);
    }).fail(function(result){
        alert("에러가 발생했습니다. \n리뷰를 가져올 수 없습니다.");
    });
}