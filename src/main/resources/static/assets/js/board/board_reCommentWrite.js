$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header").attr("content");

    $(document).on("click", ".btn_submit", function(){

        let parentId = $(this).attr("data-parentId");
        let content = $("#content").val();
        let hide = $("input:radio[name=hide]:checked").val();

        let data = {
            parent : parentId,
            content : content,
            hide : hide
        }

        $.ajax({
            url : "/board/reComment/write",
            type : "post",
            data : data,
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(response, status, xhr){

            let ct = xhr.getResponseHeader("content-type") || "";
            if(ct.indexOf("html") > -1){
                $(".wrap_content").html(response);
            }else{
                alert(response);
            }

//            if(result != null){o
//                $(".wrap_content").html(result);
////                self.close();
////                opener.location.href = "/login";
//            }else{
//                alert(result);
//                return false;
//            }
        }).fail(function(result){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });
    });


});

function test(){

//    self.close();
//    $("#content", parent.opener.document).val("success");

    if(!opener.closed){


    }

}