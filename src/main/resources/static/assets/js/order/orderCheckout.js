let token = $("meta[name='_csrf']").attr("content");
let header = $("meta[name='_csrf_header']").attr("content");

$(document).ready(function(){

    // 주문하기 버튼
    $(document).on("click", "#order-button", function(){

        // 결제 타입
        let payMethod = $("input[name=paymethod]:checked").val();
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

});

// 카카오페이 결제
function kakaoPay(address){

    $.ajax({
        url  : "/kakaoPay/order",
        type : "post",
        data : address,
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        }
        }).done(function(result){
            if(result === "fail"){
                alert("필수정보를 입력해주세요.");
                return false;
            }else if(result === "validation"){
                alert("필수 값을 입력해주세요.");
                return false;
            } else{
                window.location.href = result;
                // window.open(result, 'google', 'width=500,height=500');
            }
        }).fail(function(result){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });
}
//function kakaoPay(address){
//
//    $.ajax({
//        url  : "/kakaoPay/order",
//        type : "post",
//        data : address,
//        beforeSend : function(xhr){
//            xhr.setRequestHeader(header, token);
//        }
//        }).done(function(result){
//            if(result === "fail"){
//                alert("필수정보를 입력해주세요.");
//                return false;
//            }else{
//                window.open(result, 'google', 'width=500,height=500');
//            }
//        }).fail(function(result){
//            alert("에러입니다.");
//            return false;
//        });
//}

//가상계좌 결제
function virtualAccount(address){

    let tossPayments = TossPayments("test_ck_OyL0qZ4G1VO5jv2azY8oWb2MQYgm");
    let paymentData = {
            amount : $("#totalPrice").val(),
            orderId : $("#orderNum").val(),
            orderName : $("#orderName").val(),
            customerName : $("#customerName").val(),
            successUrl : window.location.origin + "/success",
            failUrl : window.location.origin + "/fail",
    };

     $.ajax({
        url : "/order/virtualAccount",
        type : "post",
        data : address,
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

// 배송지 생성
function createAddress() {
    new daum.Postcode({
        oncomplete: function(data) {
            // 팝업에서 검색결과 항목을 클릭했을때 실행할 코드를 작성하는 부분.

            // 각 주소의 노출 규칙에 따라 주소를 조합한다.
            // 내려오는 변수가 값이 없는 경우엔 공백('')값을 가지므로, 이를 참고하여 분기 한다.
            let addr = ''; // 주소 변수
            let extraAddr = ''; // 참고항목 변수

            //사용자가 선택한 주소 타입에 따라 해당 주소 값을 가져온다.
            if (data.userSelectedType === 'R') { // 사용자가 도로명 주소를 선택했을 경우
                addr = data.roadAddress;
            } else { // 사용자가 지번 주소를 선택했을 경우(J)
                addr = data.jibunAddress;
            }

            // 우편번호와 주소 정보를 해당 필드에 넣는다.
            $("#zipcode").val(data.zonecode);
            $("#address").val(addr);

            // 상세주소 포커스
            $("#detailAddress").focus();
        }
    }).open();
}