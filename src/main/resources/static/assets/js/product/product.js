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

    $("#tab2").on("click", function(){
        info = $(".detail_info_container");
        info.empty();

        html = "<p>tab2</p>";
        info.append(html);
    });

    $("#tab3").on("click", function(){
        info = $(".detail_info_container");
        info.empty();

        html = "<p>tab3</p>";
        info.append(html);
    });

    $("#tab4").on("click", function(){
        info = $(".detail_info_container");
        info.empty();

        html = "<p>tab4</p>";
        info.append(html);
    });


});




