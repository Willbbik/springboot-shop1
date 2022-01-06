$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header]").attr("content");

    $(".startDelivery").on("click", function(){

        let tr = $(this).closest(".item_box");
        let orderItemId = tr.attr("data-orderItemId");
        let orderNum = tr.find("#orderNum").attr("data-orderNum");
        let wayBillNum = tr.find("#wayBillNum").val();

        let result = confirm("해당 상품을 배송하시겠습니까?");
        if(result){
            $.ajax({

                url : "/admin/delivery/item",
                type : "post",
                data : {
                       orderItemId : orderItemId,
                       orderNum : orderNum,
                       wayBillNum : wayBillNum
                },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            }).done(function(result){
                if(result === "success"){
                    tr.remove();
                    alert("상품이 배송처리 되었습니다.");
                }else{
                    alert("상품 배송 실패");
                    return false;
                }
            }).fail(function(result){
                alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
                return false;
            });
        }

    });



});