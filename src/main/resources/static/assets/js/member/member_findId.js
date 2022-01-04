let x;
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
            }).done(function(result){
                if(result === "success"){
                    alert("인증번호를 발송했습니다. \n인증번호가 오지 않는 경우 정보를 다시 확인해 주세요. ");
                    makeAuthInput();
                    timer();
                }else{
                    alert(result);
                    return false;
                }
            }).fail(function(result){
                    alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
                    return false;
            });
        }
    });

    $(document).on("click", "#checkAuthNum", function(){

        let phone1 = $("#phone1").val();
        let phone2 = $("#phone2").val();
        let phone3 = $("#phone3").val();
        let authNum = $("#authNum").val();

        $.ajax({
            url : "/member/findId/authNum",
            type : "post",
            data : { phone1 : phone1,
                     phone2 : phone2,
                     phone3 : phone3,
                     authNum : authNum
                   },
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(result){
            if(result === "success"){
                alert("인증번호이 완료되었습니다.");
            }else if(result === "fail"){
                alert("인증번호가 유효하지 않습니다.");
                return false;
            }else{
                alert(result);
                return false;
            }
        }).fail(function(result){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false;
        });


    });


    $(document).on("click", "#findId", function(){

        $.ajax({
            url : "/member/findId",
            type : "post",
            beforeSend : function(xhr){
                xhr.setRequestHeader(csrfHeader, csrfToken);
            }
        }).done(function(result){
            if(result === "success"){
                alert("성공 ㅋㅋ");
            }else{
                alert("실패 ㅠㅠ");
            }
        }).fail(function(result){
            alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
            return false
        });
    });


});

function makeAuthInput(){

        let length = $(".input-wrap #authNum").length;
        if(length == 0){
            let html = "";
            html += '<div class="flex-form">'
            html += '<div class="flex-input-area">'
            html += '<div class="input-wrap">'
            html += '<input type="tel" id="authNum" maxlength="6" placeholder="6자리 입력">'
            html += '<span class="countdown">00:00</span>'
            html += '</div>'
            html += '</div>'
            html += '<div class="flex-btn-area">'
            html += '<button type="button" id="checkAuthNum" class="btn btn-outline-black">확인</button>'
            html += '</div>'
            html += '</div>'

            $(".certification-area").append(html);
        }
}


function timer(){

    let time = 180;
    let min = "";
    let sec = "";

    let startTimer = function(){
         x = setInterval(function(){
            min = parseInt(time/60);
            sec = time % 60;

            $(".countdown").html(min + ":" + sec);
            time--;

            if(time < 0){
                clearInterval(x);
                x = null;
                $(".countdown").html("00:00");
                alert("유효시간이 종료되었습니다.");
            }
        }, 1000);
    }


    if(!x){
        startTimer();
    }else{
        clearInterval(x);
        startTimer();
    }

}