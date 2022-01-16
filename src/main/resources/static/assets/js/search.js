$(function(){


    // 정렬 버튼
    $(document).on("click", ".sort", function(){

        $(".list_sort_active").attr("class", "list_sort_default sort");
        $(this).attr("class", "list_sort_active sort");

        let sort = $(this).attr("data-sort");
        let itemName = $("#itemName").val();
        let value = $("#more").attr("data-value");

        $.ajax({
            url : "/search",
            type : "get",
            data : {
                itemName : itemName,
                sort : sort,
                value : value,
                more : "more"
            }
        }).done(function(result){
            $("#more").remove();
            $(".item_list").html(result);
        }).fail(function(result){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false
        });
    });

    // 더보기 버튼
    $(document).on("click", "#more", function(){

        let sort = $(".list_sort_active").attr("data-sort");
        let itemName = $("#itemName").val();
        let value = $("#more").attr("data-value");

         $.ajax({
            url : "/search",
            type : "get",
            data : {
                itemName : itemName,
                sort : sort,
                value : value,
                more : "more"
            }
        }).done(function(result){
            $(".btn-box").remove();
            $(".item_list").append(result);
        }).fail(function(result){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false
        });

    });






});