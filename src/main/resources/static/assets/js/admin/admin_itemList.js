
$(document).ready(function(){

    $(document).on("click", "#pageBtn", function(){

        pageBtn();
    });

});


function queryStringAdd(){

//    let url = window.location.href;
//    let urlParam = new URLSearchParams(window.location.search);
//
//    $("#page").set(urlParam);
//    alert(urlParam);

    let url = $(location).attr('href');
    url = url.replace(/\&page=([0-9]+)/ig,'');
    url += "?page=" + 1;

    ChangeUrl(1, url);

}

function ChangeUrl(page, url) {
    if (typeof (history.pushState) != "undefined") {
        var obj = {Page: page, Url: url};
        history.pushState(null, null, url);
    } else {
        window.location.href = "homePage";
        // alert ( "브라우저는 HTML5를 지원하지 않습니다.");
    }
}



function pageBtn(){

        let itemName = $("input[name='itemName']").val();
        let category = $("input[name='category']:checked").val();
        let saleStatus = $("input[name='saleStatus']:checked").val();
        category = (category === "whole") ? null : category;
        itemName = (itemName === "") ? null : itemName;

        let param = {
                itemName : itemName,
                category : category,
                saleStatus : saleStatus
        }

        $.ajax({
            type : "get",
            url  : "/admin/get/itemList",
            data : param,
            success : function(result){
                $("#itemList").html(result);
                alert("good");

            },
            error : function(error){
                alert(error);
            }
        });
    }
