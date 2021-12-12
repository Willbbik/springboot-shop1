$(document).ready(function(){



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


$("#mypageqna").on("click", function(){

   $(".orderpage").empty();
});