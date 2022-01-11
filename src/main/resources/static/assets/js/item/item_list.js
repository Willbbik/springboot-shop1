$(function(){

    $(document).on("click", ".itemMore", function(){
        let lastId = $(this).attr("data-lastId");
        let category = $(this).closest(".btn-box").attr("data-category");

        $.ajax({
            url : "/category/"+category+"/more",
            type : "get",
            data : { lastId : lastId }
        }).done(function(result){
            $(".btn-box").remove();
            $(".item_list").append(result);
        }).fail(function(){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });
    });


});