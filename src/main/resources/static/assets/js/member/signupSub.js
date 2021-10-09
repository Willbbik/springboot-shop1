

// 비밀번호 타입을 text로, 눈모양 바꾸기
function togglePassword(_id, _eyeBtnId){
	
	let _password = $("#" + _id);
	
	
    if (_password.attr("type") == "password") {
        _password.attr("type", "text");
        document.getElementById(_eyeBtnId).className = 'XeyeBtn';
    } else {
        _password.attr("type", "password");
        document.getElementById(_eyeBtnId).className = 'eyeBtn';
    }
	
	
}

