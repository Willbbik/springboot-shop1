

$(document).ready(function(){

	$("#loginbtn").click(function(){
		checkId();
		checkPassword();
		mainSubmit();
	});	
	
});


// 로그인 값 전송
function mainSubmit(){
	
	if(checkId() && checkPassword()){
		try{
			$("#login_form").submit();
		} catch (e){
			$("#login_form").submit();
		}	
	}
}


// 아이디 유효성검사
function checkId(event){

	let userid = $("#userid").val();
	let oMsg = $("#idMsg");
	let oInput = $("#userid");
	
	if ( userid == "" ){
		showErrorMsg(oMsg, "아이디를 입력해 주세요.");		
		return false;
	} else {
	    hideMsg(oMsg);
        return true;
	}
	
//	if ( userid != "" ){
//		hideMsg(oMsg);
//		return true;
//	}

}


// 비밀번호 공백검사
function checkPassword(event){
	
	let password = $("#password").val();
	let oMsg = $("#pswdMsg");
	let oInput = $("#password");	
	
	if ( password == "" ){
		showErrorMsg(oMsg, "비밀번호를 입력해 주세요.");		
		return false;
	} else {
	    hideMsg(oMsg);
    	return true;
	}
	
//	if ( password != "" ){
//		hideMsg(oMsg);
//		return true;
//	}

}

// 성공메시지 함수
function showSuccessMsg(obj, msg){
	obj.attr("class", "error_next_box_green");
	obj.html(msg);
	obj.show();
}


// 에러메시지 함수
function showErrorMsg(obj, msg){
	 obj.attr("class", "error_next_box");
     obj.html(msg);
     obj.show();
}


function hideMsg(obj) {
    obj.hide();
}