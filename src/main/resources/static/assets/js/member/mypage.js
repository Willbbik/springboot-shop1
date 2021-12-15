$(document).ready(function(){

    $(document).on("click", ".qna_content", function(){
        if($(this).hasClass("qna_content_on")){
            $(this).removeClass("qna_content_on");
        }else{
            $(this).addClass("qna_content_on");
        }
    });

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


});


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


$("#moreOrder").on("click", function(){

      let lastOrderId = $("#lastOrderId").val();

      $.ajax({
            type : "get",
            url  : "/mypage/orderList?lastOrderId=" + lastOrderId
        }).done(function(result){
            $(".orderpage").html(result);
        }).fail(function(){
            alert("에러가 발생했습니다.");
        });
});


$("#mypageqna").on("click", function(){

    // let lastQnAId = $("#lastQnAId").val();

    $.ajax({
        type : "get",
        url  : "/mypage/qnaList"
    }).done(function(result){

        $(".orderpage").html(result);
    }).fail(function(){
        alert("에러가 발생했습니다.");
    });

});


