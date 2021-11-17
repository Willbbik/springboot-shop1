$(document).ready(function(){
    $(document).on("click", "#replySend", function(){

        let itemId = $("#itemId").val();

        let span = $(this).parent();
        let parent = span.find("#parent").val();
        let content = span.find("#replyContent").val();

        let param = {
            itemId : itemId,
            content : content,
            parent : parent
        };

        $.ajax({
            type : "post",
            url  : "/item/reply/send",
            data : param,
            success : function(result){
                if(result === "Y"){

                  span.find("#replyContent").val("");
                  alert("성공");
                  getQnAList();

                }else{
                  alert("권한이 없습니다");
                }

                },
            error : function(result){
                alert("실패했습니다");
            }
        });
    });


    // QnA 리스트 가져오기
    function getQnAList(){

       let productId = $("#productId").val();

       $.ajax({
          type : "get",
          url  : "/product/getqnaList?page=0",
          data : { productId : productId },
          success : function(result){
              $("#info_container_3").html(result);
          },
          error : function(result){
               alert("QnA 에러입니다. 잠시후에 다시 시도해보고 안되면 문의사항에 남겨주시면 감사하겠습니다.");
          }
       });
    }


});