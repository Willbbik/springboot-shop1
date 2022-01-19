$(function(){

     // 카테고리 버튼
     $(document).on("click", ".category", function(){

        $(".list_sort_active").attr("class", "list_sort_default category");
        $(this).attr("class", "list_sort_active category");

        let category = $(this).attr("data-category");

        $.ajax({
            url : "/category/"+category,
            type : "get",
            data : { more : "more" }
        }).done(function(result){
            $(".item_list").html(result);
        }).fail(function(){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });
    });


    // 더보기 버튼
    $(document).on("click", ".itemMore", function(){

        let lastId = $(this).attr("data-lastId");
        let category = $(this).closest(".btn-box").attr("data-category");

        $.ajax({
            url : "/category/"+category,
            type : "get",
            data : { lastId : lastId,
                     more : "more"
             }
        }).done(function(result){
            $(".btn-box").remove();
            $(".item_list").append(result);
        }).fail(function(){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });
    });


});