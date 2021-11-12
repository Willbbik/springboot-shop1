
$(document).ready(function(){

    $(document).on("click", "#pageBtn", function(){

        pageBtn();
    });


});


function pageBtn(){

        let curPage = $("#curPage").val();
        let itemName = $("input[name='itemName']").val();
        let category = $("#category").find(":selected").val();
        category = (category === "whole") ? null : category;
        let saleStatus = $("input[name='saleStatus']:checked").val();

        let param = {
                page : curPage,
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
            },
            error : function(error){
                alert(error);
            }
        });
    }
