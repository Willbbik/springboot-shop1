$(document).ready(function(){

    let info;
    let html;

    $('.tab_default').click(function(){

        let active = $(".tab_active");
        active.attr('class', 'tab_default');

        $(this).attr('class', 'tab_default tab_active');
    });

    $("#tab1").on("click", function(){

        info = $(".detail_info_container");
        info.empty();

        html = "<p>tab1</p>";
        info.append(html);
    });

    // Review
    $("#tab2").on("click", function(){
        info = $(".detail_info_container");
        info.empty();

        html = "<p>tab2</p>";
        info.append(html);
    });

    // QnA
    $("#tab3").on("click", function(){

    });

    // 주문정보
    $("#tab4").on("click", function(){
        info = $(".detail_info_container");
        info.empty();

        html = "<p>tab4</p>";
        info.append(html);
    });

    // QnA작성 취소버튼 클릭
    $("#qna_write_cancel").on("click", function(){
        $(".qna_write").css('display', 'none');
    });

    // QnA작성하기 버튼 클릭
    $("#btn_write_qna").on("click", function(){
        $(".qna_write").css('display', '');
    });


});




