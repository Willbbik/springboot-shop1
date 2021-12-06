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

    if(payMethod === '가상계좌'){

        let paymentData = {
            amount: $("#totalPrice").val(),
            orderId: $("#orderId").val(),
            orderName: $("#orderName").val(),
            customerName: $("#customerName").val(),
            successUrl: window.location.origin + "/success",
            failUrl: window.location.origin + "/fail",
        };

        sendAddress(paymentData);
//        tossPayments.requestPayment(payMethod, paymentData);

    }else if(paymethod === '카카오페이'){
        alert("카카오페이");
    }
});


function sendAddress(paymentData){

    let tossPayments = TossPayments("test_ck_OyL0qZ4G1VO5jv2azY8oWb2MQYgm");

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    let addressDto = {
         customerName : $("#customerName").val(),
         customerPhoneNum : $("#customerPhoneNum").val(),
         recipientName : $("#recipientName").val(),
         recipientPhoneNum : $("#recipientPhoneNum").val(),
         zipcode : $("#zipcode").val(),
         address : $("#address").val(),
         detailAddress : $("#detailAddress").val(),
         payMethod : $("input[name='paymethod']:checked").val()
    }

     $.ajax({
        url : "/order/saveAddress",
        type : "post",
        data : addressDto,
        beforeSend : function(xhr){
           xhr.setRequestHeader(header, token);
        },
        success : function(result){
            if(result === 'success'){
                tossPayments.requestPayment("가상계좌", paymentData);
            }else{
                alert("결제방법을 다시 확인해주세요.");
                return false;
            }
        },
        error : function(result){
            alert("필수 주문 정보를 입력해주세요.");
            console.log("주문 실패");
            return false;
        }
    });
}


