$(function(){

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

})
