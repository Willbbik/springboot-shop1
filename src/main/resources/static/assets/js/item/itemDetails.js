function sendorder(){

    const itemId = $("#itemId").val();
    const quantity = $("#quantity").val();
    const itemList = [{ item : itemId, quantity }];

    $("input[name='itemList']").val(JSON.stringify(itemList));
}