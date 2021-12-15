$(document).ready(function(){

    let token = $("meta[name=_csrf]").attr("content");
    let header = $("meta[name=_csrf_header]").attr("content");

    $(document).on("click", ".qna_content", function(){
        if($(this).hasClass("qna_content_on")){
            $(this).removeClass("qna_content_on");
        }else{
            $(this).addClass("qna_content_on");
        }
    });

    // qna 더보기
    $(document).on("click", "#qnamore", function(){

        let lastQnAId = $("#lastQnAId").val();

        $.ajax({
            type : "get",
            url  : "/mypage/qnaList?lastQnAId=" + lastQnAId +"&more=more"
        }).done(function(result){
            $("#lastQnAId").remove();
            $(".myqna_list").append(result);
        }).fail(function(){
            alert("에러가 발생했습니다.");
        });
    });

    // qna 전체 선택 or 전체 해제
    $(document).on("click", ".qna_select_all", function(){

        if($(this).is(":checked")){
            $(".qna_select").prop("checked", true);
        }else{
            $(".qna_select").prop("checked", false);
        }
    });

    // qna 보기
    $("#mypageqna").on("click", function(){

        $.ajax({
            type : "get",
            url  : "/mypage/qnaList"
        }).done(function(result){
            $(".orderpage").html(result);
        }).fail(function(){
            alert("에러가 발생했습니다.");
        });

    });

     // qna 삭제
     $(document).on("click", "#qnaDelete", function(){

        let qnaIdList = new Array();

        $(".qna_select").each(function(){
            if($(this).is(":checked")){
                let qna_content = $(this).closest(".qna_content");
                qnaIdList.push(qna_content.find("input[name=qnaId]").val());
            }
        });

        console.log(qnaIdList);
        if(qnaIdList.length < 1){
            alert("댓글을 선택해주세요");
            return false;
        }

        let result = confirm("선택한 댓글들을 삭제 하시겠습니까?");
        if(result){
            $.ajax({
                type : "delete",
                url  : "/mypage/qna/delete",
                data : { qnaIdList : qnaIdList },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(header, token);
                },
                traditional: true
            }).done(function(result){
                alert("질문 삭제에 성공하셨습니다.");
                location.reload();
            }).fail(function(){
                alert("에러가 발생했습니다. 잠시후에 다시 시도해보시기 바랍니다.");
                return false;
            });
        }
    });

    // 주문상품 보기
    $("#orderListBtn").on("click", function(){

        $.ajax({
            type : "get",
            url  : "/mypage/orderList",
        }).done(function(result){
            $(".orderpage").html(result);
        }).fail(function(){
            alert("에러가 발생했습니다.");
        });
    });


    // 주문상품 더보기 버튼
    $(document).on("click", "#moreOrder", function(){

          let lastOrderId = $("#lastOrderId").val();

          $.ajax({
                type : "get",
                url  : "/mypage/orderList?lastOrderId=" + lastOrderId + "&more=more"
            }).done(function(result){
                $("#lastOrderId").remove();
                $(".orderpage").append(result);
            }).fail(function(){
                alert("에러가 발생했습니다.");
            });
    });


});


