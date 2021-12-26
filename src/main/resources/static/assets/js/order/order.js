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

        order(paymentData);
    }else if(payMethod === '카카오페이'){
        kakaoPay();
    }
});

// 카카오페이 결제
function kakaoPay(){

    $.ajax({
        type : "get",
        url  : "/order/kakaoPay",
        success : function(result){
            location.href(result);

        },
        error : function(){
            alert("에러입니다.");
        }
    });
}


function order(paymentData){

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
        url : "/order/order",
        type : "post",
        data : addressDto,
        beforeSend : function(xhr){
           xhr.setRequestHeader(header, token);
        },
        success : function(result){
            if(result === 'success'){
                tossPayments.requestPayment("가상계좌", paymentData);
            }else if(result === "fail"){
                alert("결제방법을 다시 확인해주세요.");
                return false;
            }else{
                alert(result);
                return false;
            }
        },
        error : function(){
            alert("필수 주문 정보를 입력해주세요.");
            return false;
        }
    });
}


