let idFlag = false;
let pwFlag = false;

$(document).ready(function(){

	$("#loginbtn").click(function(){
		mainSubmit();
	});
});


function mainSubmit(){
    checkId();
    checkPassword();

    if(idFlag && pwFlag){
        try{
            $("#login_form").submit();
        }catch{
            $("#login_form").submit();
        }
    }
}


// 아이디 유효성검사
function checkId(event){

	let userId = $("#userId").val();
	let oMsg = $("#idMsg");
	
	if ( userId == "" ){
		showErrorMsg(oMsg, "아이디를 입력해 주세요.");
		idFlag = false;
		return false;
	} else {
	    hideMsg(oMsg);
	    idFlag = true;
        return true;
	}

}

// 비밀번호 공백검사
function checkPassword(event){
	
	let password = $("#password").val();
	let oMsg = $("#pswdMsg");

	if ( password == "" ){
		showErrorMsg(oMsg, "비밀번호를 입력해 주세요.");
		pwFlag = false;
		return false;
	} else {
	    hideMsg(oMsg);
	    pwFlag = true;
    	return true;
	}
}

// 에러메시지 띄우기
function showErrorMsg(obj, msg){
	 obj.attr("class", "error_next_box");
     obj.html(msg);
     obj.show();
}

function hideMsg(obj) {
    obj.hide();
}