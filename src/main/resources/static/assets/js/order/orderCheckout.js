$(document).ready(function(){

    setOrderDataSession();

});


// 상품 정보들 세션 스토리지에 저장
function setOrderDataSession(){

    let urlParams = new URLSearchParams(window.location.search);

    let jsonArray = new Array();
    let itemList = new Array();
    let quantity = new Array();
    let data = new Object();

    // 쿼리스트링의 상품번호와 수량을 가져온다
    itemList = urlParams.getAll('itemId');
    quantity = urlParams.getAll('quantity');

    // key - value로 만들어준다
    data.itemId = itemList;
    data.quantity = quantity;

    jsonArray.push(data);
    sessionStorage.setItem("에게모니 상품정보", JSON.stringify(jsonArray));
}
