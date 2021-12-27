let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

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

// 주문하기 버튼
$(document).on("click", "#order-button", function(){

    // 결제 타입
    let payMethod = $("input[name='paymethod']:checked").val();
    // 배송정보 ( 이름, 전화번호, 주소 )
    let addressAndPayMethod = {
         customerName : $("#customerName").val(),
         customerPhoneNum : $("#customerPhoneNum").val(),
         recipientName : $("#recipientName").val(),
         recipientPhoneNum : $("#recipientPhoneNum").val(),
         zipcode : $("#zipcode").val(),
         address : $("#address").val(),
         detailAddress : $("#detailAddress").val(),
         payMethod : payMethod
    }

    if(payMethod === '가상계좌'){
        virtualAccount(addressAndPayMethod);
    }else if(payMethod === '카카오페이'){
        kakaoPay(addressAndPayMethod);
    }
});

// 카카오페이 결제
function kakaoPay(data){

    $.ajax({
        url  : "/order/kakaoPay",
        type : "post",
        data : data,
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        }
        }).done(function(result){
            if(result === "fail"){
                alert("필수정보를 입력해주세요.");
                return false;
            }else{
                window.open(result, 'google', 'width=500,height=500');
            }
        }).fail(function(result){
            alert("에러입니다.");
        });
}

//가상계좌 결제
function virtualAccount(data){

    let tossPayments = TossPayments("test_ck_OyL0qZ4G1VO5jv2azY8oWb2MQYgm");
    let paymentData = {
            amount: $("#totalPrice").val(),
            orderId: $("#orderId").val(),
            orderName: $("#orderName").val(),
            customerName: $("#customerName").val(),
            successUrl: window.location.origin + "/success",
            failUrl: window.location.origin + "/fail",
    };

     $.ajax({
        url : "/order/virtualAccount",
        type : "post",
        data : data,
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


