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


// 장바구니에 담기
function addCart(){

    let param = {
        itemId : $("#itemId").val(),
        quantity : $("#quantity").val()
    };

    console.log($("#itemId").val());
    console.log($("#quantity").val());

    $.ajax({
        type : "POST",
        url : "/cart/add",
        data : param,
        success : function(result){
            switch(result){
                case 200 :
                    alert("상품을 장바구니에 담았습니다.");
                    break;
                case 403 :
                    let result = confirm("로그인이 필요한 서비스입니다. \n로그인 페이지로 이동하시겠습니까?");
                    if(result){
                        window.location.href = "http://localhost:8080/login";
                        break;
                    }else{
                        break;
                    }
                case 401 :
                    alert("상품과 옵션을 정확히 입력해주세요.");
                    break;
                default:
            }
        },
        error : function(error){
            alert("에러입니다");
        }
    });
}