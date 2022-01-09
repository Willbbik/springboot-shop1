$(function(){

    $(".boardView").on("click", function(){

        let boardId = $(this).attr("data-num");
        location.href = "/board/view/"+boardId;
    });


});