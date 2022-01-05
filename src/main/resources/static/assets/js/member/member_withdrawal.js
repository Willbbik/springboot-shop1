$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header]").attr("content");

    $("#withdrawal").on("click", function(){
        let password = $("#password").val();

        $.ajax({
            url : "/member/withdrawal",
            type : "delete",
            data : { password : password },
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(result){
            if(result === "success"){
                alert("회원탈퇴에 성공했습니다.");
                window.location.replace("/logout");
            }else{
                alert("비밀번호가 일치하지 않습니다.");
                return false;
            }
        }).fail(function(result){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });
    });


});