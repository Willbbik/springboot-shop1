$(function(){

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    // 전체 선택 or 전체 해제
    $(".checkbox-selectall").on("click", function(){
        if($(this).is(':checked')){
            $('.check').prop('checked', true);
        }else{
            $('.check').prop('checked', false);
        }
    });

    // 단일 상품 주문
    $(".order_single_btn").on("click", function(){

        let cartList = new Array();

        let tr = $(this).closest("tr");
        let cartId = tr.find("input[name='cartItemId']").val();
        let cartJson = {cartItemId : cartId};

        cartList.push(cartJson);

        const form = $("<form method='post'></form>");
        form.attr("action", "/order/checkout");
        form.append($('<input>', {type: "hidden", name: "itemList", value: JSON.stringify(cartList)}));
        form.append($('<input>', {type: "hidden", name: "where", value: "cart"}));
        form.append($("#form_csrf_token"));
        form.appendTo("body");
        form.submit();

    });

    // 선택한 상품 주문
    $("#order_select_btn").on("click", function(){

        const cartItemId = $(".cartItemId").get();
        let cartList = new Array();

        for(let i=0; i<cartItemId.length; i++){
            if($(cartItemId[i]).closest('tr').find('.check').is(":checked") == true){
                let c_id = $(cartItemId[i]).val()
                let cart_json = {cartItemId : c_id};
                cartList.push(cart_json);
            }
        }

        const form = $("<form method='post'></form>");
        form.attr("action", "/order/checkout");
        form.append($('<input>', {type: "hidden", name: "itemList", value: JSON.stringify(cartList)}));
        form.append($('<input>', {type: "hidden", name: "where", value: "cart"}));
        form.append($("#form_csrf_token"));
        form.appendTo("body");
        form.submit();
    });

    // 상품 수량 업데이트
    $(".cartQuantityUpdate").on("click", function(){

        let tr = $(this).closest("tr");
        let cartId = tr.find("input[name=cartItemId]").val();
        let quantity = tr.find("input[name=cartQuantity]").val();

        let param = { id : cartId, quantity : quantity };

        $.ajax({
            url : "/cart/update/quantity",
            type : "patch",
            data : param,
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            }
        }).done(function(result){
            alert(result);
            location.reload();
        }).fail(function(result){
            alert("오류가 발생했습니다. 잠시후 다시 시도해보시기 바랍니다.");
        });
    });

    // 단일 상품 삭제
    $(".cart_delete").on("click", function(){

        let tr = $(this).closest("tr");
        let cartId = tr.find("input[name=cartItemId]").val();

        let param = { id : cartId };

        $.ajax({
            url : "/cart/delete/item",
            type : "delete",
            data : param,
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            }
        }).done(function(result){
            alert(result);
            location.reload();
        }).fail(function(result){
            alert("오류가 발생했습니다. 잠시후 다시 시도해보시기 바랍니다.");
        });
    });


    // 선택 상품 삭제
    $(".delete_select_item").on("click", function(){

            let itemList = new Array();
            $(".check").each(function(){
                if($(this).is(":checked")){
                    let tr = $(this).closest("tr");
                    itemList.push(tr.find("input[name=cartItemId]").val());
                }
            });

            $.ajax({
                url : "/cart/delete/itemList",
                type : "delete",
                data : { itemList : itemList },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(header, token);
                },
                traditional: true
            }).done(function(result){
                alert(result);
                location.reload();
            }).fail(function(result){
                alert("오류가 발생했습니다. 잠시후 다시 시도해보시기 바랍니다.");
            });
        });


})
