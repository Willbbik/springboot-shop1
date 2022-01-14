$(function(){

    // 상품 상세보기에 들어가면 바로 qna 가져오게끔
    getQnAList();

    // csrf
    let csrfToken = $("meta[name='_csrf']").attr("content");
    let csrfHeader = $("meta[name='_csrf_header']").attr("content");

    // tab3 QnA
    $(document).on("click", "#tab3", function(){
        $("#info_container_1").css("display", "none");
        $("#info_container_2").css("display", "none");
        $("#info_container_3").css("display", "");
        getQnAList();
    });


    // QnA작성 취소버튼 클릭
    $(document).on('click', '#qna_write_cancel', function(){
        $(".qna_write").css('display', 'none');
    });

    // QnA작성하기 버튼 클릭
    $(document).on('click', '#btn_write_qna', function(){
        $(".qna_write").css('display', '');
    });

    // 댓글 내용 클릭시 답글보이게
    $(document).on("click", ".qna_text", function(){

        let list = $(this).parent("li");
        let content = list.find("p");
        let dl = list.find("dl");

        if(content.css('display') == 'none'){
            content.css('display', 'block');
            dl.css('display', 'block');

        }else{
            content.css('display', 'none');
            dl.css('display', 'none');
        }
    });


    // QnA 질문 등록
    $(document).on("click", "#qna_post", function(){

        let itemId = $("#itemId").val();
        let content = $("#content").val();
        let hide = $('input:radio[name="hide"]:checked').val();

        if(content == ""){
            alert("내용을 입력해주세요.");
            return false;
        }

        let param = {
            itemId : itemId,
            content : content,
            hide : hide
        };

        $.ajax({
           type : "post",
           url  : "/item/qna/send",
           data : param,
           beforeSend : function(xhr){
               xhr.setRequestHeader(csrfHeader, csrfToken);
           }
        }).done(function(result){
             if(result === "success"){
                $(".qna_write").css('display', 'none');
                $("#content").val("");
                getQnAList();
                alert("등록되었습니다.");
             }else if(result === "login"){
                alert("로그인이 필요한 서비스입니다.");
                return false;
             }else{
                alert(result);
                return false;
             }
       }).fail(function(result){
           alert("에러가 발생하였습니다. \n잠시후 다시 시도해주시기 바랍니다.");
       });
    });

    // 페이지 버튼
    $(document).on("click", ".paging a", function(){
        let itemId = $("#itemId").val();
        let page = $(this).attr("data-num");

        $.ajax({
            url : "/item/get/qnaList",
            type : "get",
            data : { itemId : itemId, page : page },
        }).done(function(result){
            $(".details_qna_container").html(result);
        }).fail(function(result){
            alert("에러가 발생하였습니다. \n잠시후 다시 시도해주시기 바랍니다.");
        });
    });

});

// QnA 리스트 가져오기
function getQnAList(){

      let itemId = $("#itemId").val();

      $.ajax({
          type : "get",
          url  : "/item/get/qnaList",
          data : { itemId : itemId }
      }).done(function(result){
         $(".details_qna_container").html(result);
      }).fail(function(result){
         alert("에러가 발생하였습니다. \n잠시후 다시 시도해주시기 바랍니다.");
      });
}