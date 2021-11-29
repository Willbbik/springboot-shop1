$(document).ready(function(){
    $(document).on("click", "#qna_reply_post", function(){

        let itemId = $("#itemId").val();

        let span = $(this).closest("span");
        let parent = span.find("#parent").val();
        let content = span.find("#replyContent").val();
        let hide = span.find("input[type='radio']:checked").val();

        let token = $("meta[name='_csrf']").attr("content");
        let header = $("meta[name='_csrf_header']").attr("content");

        let param = {
            itemId : itemId,
            content : content,
            parent : parent,
            hide : hide
        };

        $.ajax({
            type : "post",
            url  : "/item/reply/send",
            data : param,
            beforeSend : function(xhr){
                xhr.setRequestHeader(header, token);
            },
            success : function(result){
                if(result === "Y"){

                  span.find("#replyContent").val("");
                  alert("답변 작성에 성공하셨습니다.");
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

       let itemId = $("#itemId").val();

       $.ajax({
          type : "get",
          url  : "/item/get/qnaList",
          data : { itemId : itemId,
                   page : 1
                   },
          success : function(result){
              $("#info_container_3").html(result);
          },
          error : function(result){
               alert("QnA 에러입니다. 잠시후에 다시 시도해보고 안되면 문의사항에 남겨주시면 감사하겠습니다.");
          }
       });
    }


    // QnA작성 취소버튼 클릭
    $(document).on('click', '#qna_reply_write_cancel', function(){

        $(this).closest(".qna_reply_write").css('display', 'none');
        $(this).closest(".qna_list_body").find(".qna_title").css('display', 'none');
    });




});
