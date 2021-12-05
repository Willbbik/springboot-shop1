$(document).ready(function(){

    calculateTotalPrice();

    $("#quantity").change(function(){
        calculateTotalPrice();
    });

});

// 최종 금액 계산
function calculateTotalPrice(){

    let quantity = $("#quantity").val();
    let price = $("#price").val();
    let totalPrice = (quantity * price).toString();

    totalPrice = totalPrice.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    $("#totalPrice").html(totalPrice + '원')
}


$(document).on("click", "#order-button", function(){

    let tossPayments = TossPayments("test_ck_OyL0qZ4G1VO5jv2azY8oWb2MQYgm");
    let payMethod = $("input[name='paymethod']:checked").val();

    if(payMethod === 'VIRTUAL_ACCOUNT'){

        let paymentData = {
            amount: $("#totalPrice").val(),
            orderId: $("#orderId").val(),
            orderName: $("#orderName").val(),
            customerName: $("#customerName").val(),
            successUrl: window.location.origin + "/success",
            failUrl: window.location.origin + "/fail",
        };

        sendAddress();
        tossPayments.requestPayment("가상계좌", paymentData);

    }else if(paymethod === 'KAKAO_PAY'){
        alert("카드결제");
    }
});


function sendAddress(){

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let param = {
         customerName : $("#customerName").val(),
         customerPhoneNum : $("#customerPhoneNum").val(),
         recipientName : $("#recipientName").val(),
         recipientPhoneNum : $("#recipientPhoneNum").val(),
         zipcode : $("#zipcode").val(),
         address : $("#address").val(),
         detailAddress : $("#detailAddress").val()
    }

     $.ajax({
        url : "/order/saveAddress",
        type : "post",
        data : param,
        beforeSend : function(xhr){
           xhr.setRequestHeader(header, token);
        },
        success : function(result){
            // 여기서 배송 정보 유효성검사 해주기
            console.log(result);
        },
        error : function(result){
            console.log("실패");
        }
    });
}


