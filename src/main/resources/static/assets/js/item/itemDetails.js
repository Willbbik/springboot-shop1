$(document).ready(function(){

    calculateTotalPrice();

    $("#quantity").change(function(){
        calculateTotalPrice();
    });

});

function sendorder(){

    const itemId = $("#itemId").val();
    const quantity = $("#quantity").val();
    const itemList = [{ itemId : itemId, quantity }];

    $("input[name='itemList']").val(JSON.stringify(itemList));
}

// 최종 금액 계산
function calculateTotalPrice(){

    let quantity = $("#quantity").val();
    let price = $("#price").val();
    let totalPrice = (quantity * price).toString();

    totalPrice = totalPrice.replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    $("#totalPrice").html(totalPrice + '원')
}

function addCart(){

    let param = {
        itemId : $("#itemId").val(),
        quantity : $("#quantity").val()
    };

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    $.ajax({
        type : "POST",
        url : "/cart/add",
        data : param,
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success : function(result){
         switch(result){
            case 200 :
                alert("상품을 장바구니에 담았습니다.");
                break;
            case 403 :
                let result = confirm("로그인이 필요한 서비스입니다. \n로그인 페이지로 이동하시겠습니까?");
                if(result){
                    window.location.href = "https://egemony.tk/login";
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
            alert("에러가 발생했습니다. 불편을 드려 죄송합니다.\n잠시후에 다시 시도해보시기 바랍니다.")
        }
    })
}


