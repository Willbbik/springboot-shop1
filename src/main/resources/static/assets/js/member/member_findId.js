$(function(){

    let csrfToken = $("meta[name=_csrf]").attr("content");
    let csrfHeader = $("meta[name=_csrf_header]").attr("content");

    $("#phone1").on("keyup", function(){

          var mch = $(this).val();
          if( mch != null && mch.length == 3 )
          {
              $("#phone2").focus();
          }
    });

    $("#phone2").on("keyup", function(){

          var mch = $(this).val();
          if( mch != null && mch.length == 4 )
          {
              $("#phone3").focus();
          }
    });

    $("#sendMessage").on("click", function(){

        let phone1 = $("#phone1").val();
        let phone2 = $("#phone2").val();
        let phone3 = $("#phone3").val();

        if(phone1 == "" || phone2 == "" || phone3 == ""){
            alert("입력하신 번호를 다시 확인해주세요.");
            return false;
        }

        let result = confirm("입력하신 휴대폰 번호로 인증번호를 받으시겠습니까?");
        if(result){

            $.ajax({
                url : "/member/findId/sendMessage",
                type : "post",
                data : { phone1 : phone1,
                         phone2 : phone2,
                         phone3 : phone3
                       },
                beforeSend : function(xhr){
                    xhr.setRequestHeader(csrfHeader, csrfToken);
                }
            }).done(function(){
                alert("인증번호를 발송했습니다. \n인증번호가 오지 않는 경우 정보를 다시 확인해 주세요. ");

            }).fail(function(){

            });
        }
    });



});

function makeAuthInput(){



}
