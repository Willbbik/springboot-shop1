$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header]").attr("content");

    $(".changestatusbtn").on("click", function(){

        let tr = $(this).closest("tr");

        let orderItemId = tr.attr("data-orderItemId");
        let deliveryStatus = tr.find("select option:selected").val();

        $.ajax({
            url : "/admin/deliveryStatus/change",
            type : "patch",
            data : {
                orderItemId : orderItemId,
                deliveryStatus : deliveryStatus
            },
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
           }
        }).done(function(result){
            if(result === "success"){
                alert("주문 상태 변경에 성공하였습니다.");
            }else{
                alert("주문 상태 변경에 실패하였습니다.");
                return false;
            }
        }).fail(function(result){
            alert("에러가 발생하였습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });
    });

});