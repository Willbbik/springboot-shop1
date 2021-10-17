
$('.tab_default').click(function(){

    let active = $(".tab_active");
    active.attr('class', 'tab_default');

    $(this).attr('class', 'tab_default tab_active');
});