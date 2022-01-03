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

//    $("#sendMessage").on("click", function(){
//
//        let phone1 = $("#phone1").val();
//        let phone2 = $("#phone2").val();
//        let phone3 = $("#phone3").val();
//        let count = $(".countdown");
//
//        if(phone1 == "" || phone2 == "" || phone3 == ""){
//            alert("입력하신 번호를 다시 확인해주세요.");
//            return false;
//        }
//
//        let result = confirm("입력하신 휴대폰 번호로 인증번호를 받으시겠습니까?");
//        if(result){
//
//            $.ajax({
//                url : "/member/findId/sendMessage",
//                type : "post",
//                data : { phone1 : phone1,
//                         phone2 : phone2,
//                         phone3 : phone3
//                       },
//                beforeSend : function(xhr){
//                    xhr.setRequestHeader(csrfHeader, csrfToken);
//                }
//            }).done(function(){
//                alert("인증번호를 발송했습니다. \n인증번호가 오지 않는 경우 정보를 다시 확인해 주세요. ");
//                makeAuthInput();
//                startTimer(3, count);
//            }).fail(function(){
//
//            });
//        }
//    });

    $(document).on("click", "#sendMessage", function(){

        let count = $(".countdown");
        let time = (60 * 3) - 1;

        startTimer(1, count);
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
            html += '<button type="button" class="btn btn-outline-black">확인</button>'
            html += '</div>'
            html += '</div>'

            $(".certification-area").append(html);
        }
}

function startTimer(duration, display){

    let timer = duration, minutes, seconds;

    let interval = setInterval(function (){
        minutes = parseInt(timer / 60, 10);
        seconds = parseInt(timer % 60, 10);

        minutes = minutes < 10 ? "0" + minutes : minutes;
        seconds = seconds < 10 ? "0" + seconds : seconds;

        display.html(minutes + ":" + seconds);

        if(--timer < -1){
            timer = duration;
        }
        if(timer === 0){
            clearInterval(interval);
            alert("유효시간이 종료되었습니다.");
        }

    }, 1000);
}
