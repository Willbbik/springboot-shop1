$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header]").attr("content");

    // 단일 상품 삭제
    $(".item_delete").on("click", function(){

        let idList = new Array();

        let tr = $(this).closest("tr");
        let itemId = tr.find("input[name=itemId]").val();
        idList.push(itemId);

        let result = confirm("해당 상품을 삭제 하시겠습니까? (한번이라도 구매된 상품은 삭제 불가능합니다.) ");
        if(result){
            $.ajax({
               type : "delete",
               url  : "/admin/item/delete",
               data : { itemIdList : idList },
               beforeSend : function(xhr){
                    xhr.setRequestHeader(csrfHeader, csrfToken);
               },
               traditional: true
            }).done(function(result){
                alert(result);
                location.reload();
            }).fail(function(){
                alert("상품 삭제가 불가능합니다.");
                return false;
            });
        }
    });

    // 상품 전체 선택 or 전체 해제
    $(".checkbox-selectall").on("click", function(){

        if($(this).is(":checked")){
            $(".checkbox-select").prop("checked", true);
        }else{
            $(".checkbox-select").prop("checked", false);
        }
    });


    // 선택 상품 삭제
    $("#delete_select").on("click", function(){

        let idList = new Array();

        $(".checkbox-select").each(function(){
            if($(this).is(":checked")){
                let tr = $(this).closest("tr");
                idList.push(tr.find("input[name=itemId]").val());
            }
        });

        if(idList.length == 0){
            alert("상품을 선택해주세요");
            return false;
        }

        let result = confirm("선택한 상품들을 삭제 하시겠습니까?");
        if(result){

            $.ajax({
                type : "delete",
                url  : "/admin/item/delete",
                data : { itemIdList : idList },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                },
                traditional : true
            }).done(function(result){
                if(result === "success"){
                    location.reload();
                }
            }).fail(function(result){
                alert("오류가 발생했습니다. 잠시후 다시 시도해보시기 바랍니다.");
                return false;
            });
        }

    });

});
