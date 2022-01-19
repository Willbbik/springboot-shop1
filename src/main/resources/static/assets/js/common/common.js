$(function(){

    // 검색 기능
    $(document).on("click", ".searchBtn", function(){

        let itemName = $(".searchForm").val();
        if(itemName == ""){
            alert("검색어를 입력해주세요.");
            return false;
        }

        location.href = "/search?itemName="+itemName;
    });

});