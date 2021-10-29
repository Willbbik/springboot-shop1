$(document).ready(function(){

    $('.tab_default').click(function(){

        let active = $(".tab_active");
        active.attr('class', 'tab_default');

        $(this).attr('class', 'tab_default tab_active');
    });

    $("#tab1").on("click", function(){

        let info2 = $("#info_container_2");
        let info3 = $("#info_container_3");

        info2.css("display", "");
        info3.css("display", "none");
    });

    // tab2 Review
    $("#tab2").on("click", function(){

        let info2 = $("#info_container_2");
        let info3 = $("#info_container_3");

        info2.css("display", "");
        info3.css("display", "none");

    });

    // tab3 QnA 클릭시
    $(document).on("click", "#tab3", function(){

        let info2 = $("#info_container_2");
        let info3 = $("#info_container_3");

        info2.css("display", "none");
        info3.css("display", "");

        getQnAList();
    });

    // tab4 주문정보
    $("#tab4").on("click", function(){

        let info2 = $("#info_container_2");
        let info3 = $("#info_container_3");

        info2.css("display", "none");
        info3.css("display", "");
    });


    // QnA작성 취소버튼 클릭
    $(document).on('click', '#qna_write_cancel', function(){
        $(".qna_write").css('display', 'none');
    });

    // QnA작성하기 버튼 클릭
    $(document).on('click', '#btn_write_qna', function(){
        $(".qna_write").css('display', '');
    });


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


    // QnA 리스트 가져오기
    function getQnAList(){

       let productId = $("#productId").val();

       $.ajax({
          type : "post",
          url  : "/product/getqnaList",
          data : { productId : productId },
          success : function(result){
              $("#info_container_3").html(result);
          },
          error : function(result){
               alert("에러입니다. 잠시후에 다시 시도해보고 안되면 문의사항에 남겨주시면 감사하겠습니다.");
          }
       });

    }

    getQnAList();

    // QnA 질문 전송
    $(document).on("click", "#qna_post", function(){

        let productId = $("#productId").val();
        let content = $("#content").val();
        let hide = $('input:radio[name="hide"]:checked').val();

        if(content == ""){
            alert("내용을 입력해주세요.");
            return false;
        }

        let param = {
            productId : productId,
            content : content,
            hide : hide
        };

        $.ajax({
           type : "post",
           url  : "/product/qna/send",
           data : param,
           success : function(result){
                if(result === "Y"){
                   $(".qna_write").css('display', 'none');
                   $("#content").val("");
                   getQnAList();

                   alert("등록되었습니다.");
                }else if(result === "login"){
                   alert("로그인이 필요한 서비스입니다.");
                }else{
                   alert("내용을 다시 확인해주세요.");
                }
           },
           error : function(result){
                alert("에러입니다. 잠시후에 다시 시도해보신 후 안되면 문의사항에 남겨주시면 감사하겠습니다.");
           }
        });
    });

});




//function afterSendReview(dto){
//    let html = "";
//
//    html =  "<ul class='detail_review_select'>";
//    html += "  <li class='active_review_select col-sm'>"+ 여기다가 최신순 +"</li>";
//    html += "  <li>사진리뷰</li>";
//    html += "  <li>텍스트 리뷰</li>";
//    html += "</ul>";
//
//    for(var i = 0; i < dto.length; i++){
//        html += "<div class='Detail-review-box'>";
//        html += "  <dl class='detail_review_default'>";
//        html += "     <dt class='review_profile'>";
//        html += "        <strong>";
//        html += "            <div class='user_name'>"+ 여기다이름 +"</div>";
//        html += "            <div class='date'>"+ 여기다 날짜 +"</div>";
//        html += "        <strong>";
//        html += "     </dt>";
//        html += "     <dd class='review_text'>"+ 여기다 댓글내용 +"</dd>";
//        html += "  </dl>";
//        html += "</div>";
//    }
//
//    let info_2 = $("#info_container_2");
//    info_2.empty();
//    info_2.append(html);
//
//}
//
//
//function afterSendQnA(dto){
//
//    let html = "";
//
//    html += "<div class='details_qna_container'>";
//    html += "  <div class='qna_list_chk'>";
//    html += "     <div>";
//    html += "        QnA";
//    html += "        <span>"+여기다 개수+"</span>";
//    html += "     </div>";
//    html += "     <a id='btn_write_qna'>문의 내용 작성하기</a>";
//    html += "  </div>";
//    html += "   <dl class='qna_write' style='display: none;'>";
//    html += "     <dt>내용</dt>";
//    html += "     <dd><textarea placeholder='내용을 입력해 주세요.' id='content'></textarea></dd>";
//    html += "     <dt>공개여부</dt>";
//    html += "     <dd>";
//    html += "         <span>";
//    html += "              <input type='radio' id='public' name='hide' value='private' checked>";
//    html += "              <label for='public'>비공개</label>";
//    html += "              <input type='radio' id='private' name='hide' value='public'>";
//    html += "              <label for='private'>공개</label>";
//    html += "          </span>";
//    html += "     </dd>";
//    html += "     <dd>";
//    html += "           <input type='button' value='취소하기' id='qna_write_cancel' />";
//    html += "           <input type='button' value='등록하기' id='qna_post'/>";
//    html += "     </dd>";
//    html += "   </dl>";
//    html += "   <ul class='qna_list'>";
//    html += "     <div class='qna_list_head'>";
//    html += "          <div class='qna_head_status'>답변상태</div>";
//    html += "          <div class='qna_head_content'>내용</div>";
//    html += "           <div>작성자</div>";
//    html += "           <div class='qna_head_created'>작성일</div>";
//    html += "     </div>";
//
//    for(var i = 0; i<dto.length; i++){
//        html += "<li class='qna_list_body'>";
//        html += "  <div class='qna_status'>"+ 답변대기 쓰면됨 +"</div>";
//        html += "  <div class='qna_text'>"+ 내용 +"</div>";
//        html += "  <div>"+ 아이디 +"</div>";
//        html += "  <div>"+ 날짜 +"</div>";
//        html += "</li>";
//    }
//
//    html += "  </ul>";
//    html += "</div>";
//
//}




//
//function getQnAList(){
//
//   let productId = $("#productId").val();
//
//
//}


