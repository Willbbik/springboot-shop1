$(document).ready(function(){


    // 상품번호와 옵션값으로 다음 옵션값 가져오기
    $(".detail_options").on("change", "select", function(event) {

        const product_id = $("#productId").val();
        const option_value = $(this).val();
        const next_select = $(this).index() + 1;
        const select = $(".detail_options").find("select").eq(next_select);

        let param = { productId : product_id,
                      value : option_value,
                      nextIndex : next_select
                    };

        if(select.length){
           // ajax
            $.ajax({
                type : "get",
                url  : "/product/getOption",
                data : param,
                success : function(result){
                    alert("성공");
                },
                error : function(result){
                    alert("에러");
                }
            });
        }else{
            // 계산
            alert("없음");
        }

    });



    getQnAList();

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

    // tab3 QnA 클릭
    $(document).on("click", "#tab3", function(){

        let info2 = $("#info_container_2");
        let info3 = $("#info_container_3");

        info2.css("display", "none");
        info3.css("display", "");

        getQnAList();
    });

    // tab4 주문정보 클릭
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
                   return false;
                }else{
                   alert("내용을 다시 확인해주세요.");
                   return false;
                }
           },
           error : function(result){
                alert("에러입니다. 잠시후에 다시 시도해보신 후 안되면 문의사항에 남겨주시면 감사하겠습니다.");
           }
        });
    });




});



