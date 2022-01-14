$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header").attr("content");

    //파일 업로드시 썸네일
    $("#upload_image").change(function(e){
        const images = e.target.files;
        $(".img-box").empty();
        for(let i=0; i<images.length; i++){
            const Reader = new FileReader();
            Reader.readAsDataURL(images[i]);
            Reader.onload = function(){
                const img = '<img src="'+Reader.result +'" alt="사진">';
                $('.img-box').append(img);
            }
        }
    });

    // 상품 등록
    $(document).on("click", "#itemPost", function(){

        let form = $("#itemForm")[0];
        let formData = new FormData(form);

        $.ajax({
            type : "post",
            url  : "/admin/item/register",
            enctype : 'multipart/form-data',
            data : formData,
            contentType : false,
            processData : false,
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(result){
            if(result === "success"){
                alert("상품 등록에 성공하였습니다.")
                window.location.replace("/");
            }else if(result === "role"){
                alert("관리자 권한이 필요합니다");
                return false;
            }else{
               alert(result);
               return false;
            }
        }).fail(function(result){
            alert("에러가 발생하였습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });
    });

});

