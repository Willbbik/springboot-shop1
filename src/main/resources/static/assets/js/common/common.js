$(function(){

    // 검색 기능
    $(document).on("click", ".searchBtn", function(){

        let itemName = $(".searchForm").val();
        location.href = "/search?itemName="+itemName;

    });


});