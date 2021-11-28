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

    $(document).on("click", "#order-button", function(){

        const tossPayments = TossPayments("test_ck_OyL0qZ4G1VO5jv2azY8oWb2MQYgm");
        let method = "가상계좌";

        let paymentData = {
            amount: 30000,
            orderId: "xYpNG4a48IhLvoSIimnJh",
            orderName: "토스 티셔츠",
            customerName: "이토페",
            successUrl: window.location.origin + "/success",
            failUrl: window.location.origin + "/fail",
        };

        tossPayments.requestPayment(method, paymentData);

    });


