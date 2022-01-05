let price = 0;

$(function(){

    $("#quantity").blur(function(){
        quantityChange();
    }).keyup(function(event) {
        quantityChange();
    }).keypress(function(event){
        quantityChange();
    }).keydown(function(event) {
        quantityChange();
    });

    $("#color").on("change", function(){
        makeOption();
    });

    $("#size").on("change", function(){
        makeOption();
    });


    $('.tab_default').click(function(){

        let active = $(".tab_active");
        active.attr('class', 'tab_default');

        $(this).attr('class', 'tab_default tab_active');
    });

    // tab1 상품정보
    $("#tab1").on("click", function(){

        $("#info_container_1").css("display", "");
        $("#info_container_2").css("display", "none");
        $("#info_container_3").css("display", "none");
    });


    $(document).on("click", ".quantity_plus", function(){

         let quantity = $("#quantity").val();

         $("#quantity").val(Number(quantity) + 1);
         quantityChange();
    });

    $(document).on("click", ".quantity_minus", function(){

         let quantity = $("#quantity").val();
         if(quantity <= 1){
            alert("상품 구매가능 최소 수량은 1개 입니다.")
            return false;
         }
         $("#quantity").val(Number(quantity) - 1);
         quantityChange();
    });

    $(document).on("click", ".btn_del_option", function(){

        $(".selected_options_box").html("");
        price = 0;
    });

    $(document).on("change", "#quantity", function() {
        quantityChange();
    });

});

// 수량 변경
function quantityChange(){

    let totalPrice = price * parseInt($("#quantity").val());

    $('.price_text').html(totalPrice.toString() + "원");
    $('.totalPrice').html(totalPrice.toString() + "원");
}


function sendorder(){

    if($(".selected_options_box div").length == 0){
        alert("상품을 선택해주세요.");
        return false;
    }

    const itemId = $("#itemId").val();
    const quantity = $("#quantity").val();
    const itemList = [{ itemId : itemId, quantity }];

    $("input[name='itemList']").val(JSON.stringify(itemList));
}

// 옵션 모두 선택시 선택한 옵션목록 띄워주기
function makeOption(){

    let optionSize = $(".detail_selected-options div").length;
    const colorValue = $("select[name=color]").val();
    const sizeValue = $("select[name=size]").val();
    let totalPrice = $("#price").val().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
    let html = "";

    if(colorValue != 'colorNone' &&  sizeValue != 'sizeNone'){
         if(optionSize == 0){
            price = parseInt($('#price').val());

            html += '<div class="detail_selected_options">';
            html += '<strong class="goods">' + colorValue + " / " + sizeValue + '</strong>';
            html += '<div class="wrap_quantity_price">';
            html += '<div class="quantity">';
            html += '<input type="button" class="quantity_minus" value="-">';
            html += '<input type="text" class="input_quantity" id="quantity" name="quantity" value="1">';
            html += '<input type="button" class="quantity_plus" value="+">';
            html += '</div>';
            html += '<div class="price_text">' + totalPrice.toString() + '원</div>';
            html += '</div>';
            html += '<button type="button" class="btn_del_option" aria-label="옵션삭제 버튼">X</button>';
            html += '</div>';

            $(".selected_options_box").html(html);
            $("select[name=color] option:eq(0)").prop("selected", true);
            $("select[name=size] option:eq(0)").prop("selected", true);
            $(".totalPrice").html(price.toString() + "원");
         }else{
            return false;
         }
    }
}