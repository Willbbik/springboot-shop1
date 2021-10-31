$(document).ready(function(){
        $("#replySend").on("click", function(){

            let productId = $("#productId").val();

            let span = $(this).parent();
            let parent = span.find("#parent").val();
            let content = span.find("#replyContent").val();

            let param = {
                productId : productId,
                content : content,
                parent : parent
            };

            $.ajax({
                type : "post",
                url  : "/product/reply/send",
                data : param,
                success : function(result){

                    if(result === "Y"){
                        span.find("#replyContent").val("");
                        alert("성공");
                    }
                },
                error : function(result){
                    alert("실패했습니다");
                }
            });
        });


});