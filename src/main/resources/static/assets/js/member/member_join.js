let idFlag = false;
let pwFlag = false;
let authFlag = false;
let submitFlag = false;

// input이 포커스됐다가 취소되면 함수실행
$(document).ready(function(){

	$("#userId").blur(function(){
		idFlag = false;
		checkId();
	});

	$("#pswd1").blur(function(){
		pwFlag = false;
		checkPswd1();
	}).keyup(function(event) {
		checkPswd2();
        checkShiftUp(event);
    }).keypress(function(event){
		checkPswd2();
		checkCapslock(event);
	}).keydown(function(event) {
		checkPswd2();
        checkShiftDown(event);
    });


	$("#pswd2").blur(function(){
		checkPswd2();
	}).keyup(function(event) {
		checkPswd2();
        checkShiftUp(event);
    }).keypress(function(event){
		checkPswd2();
		checkCapslock2(event);
	}).keydown(function(event) {
		checkPswd2();
        checkShiftDown(event);
    });


	$("#phoneNum").blur(function() {
        checkPhoneNum();
    });

	$("#authNum").blur(function() {
        authFlag = false;
        checkAuthNum();
    });

    // 인증번호 전송 버튼
    $("#authSendBtn").click(function(){
		sendSmsButton();
		return false;
	});

    $("#btnJoin").click(function(event){
        submitOpen();
        mainSubmit();
    });

});

// 가입하기 버튼
function mainSubmit(){

	if(!checkUnrealInput()){
		submitOpen();
		return false;
	}

	if(idFlag && pwFlag && authFlag){
		try{
			$("#join_form").submit();
		} catch (e){
			$("#join_form").submit();
		}
	} else {
		submitOpen();
		return false;
	}
}

// 가입하기 클릭시 모든 input 검사
function checkUnrealInput() {
    if (checkId()
        & checkPswd1()
        & checkPswd2()
        & checkPhoneNum()
        & checkAuthNum()
    ) {
        return true;
    } else {
        return false;
    }
}


// 가입하기 버튼 활성화
function submitOpen() {
    $("#btnJoin").attr("disabled",false);
}


// 가입하기 버튼 비활성화
function submitClose(){
	submitFlag = true;
	$("#btnJoin").attr("disabled",true);
}


// 아이디 유효성검사
function checkId(){

    if(idFlag) return true;

	let userId = $("#userId").val();
	let oMsg = $("#idMsg");
	let oInput = $("#userId");

	if ( userId == "" ){
		showErrorMsg(oMsg, "아이디는 필수정보 입니다.");
		setFocusToInputObject(oInput);
		return false;
	}

	const isID = /^[a-z0-9]{5,20}$/;
	if (!isID.test(userId)){
		showErrorMsg(oMsg, "5~20자의 영문 소문자, 숫자만 사용 가능합니다. (띄어쓰기 불가능)");
		setFocusToInputObject(oInput);
		return false;
	}

	idFlag = false;
	$.ajax({
		type : "GET",
		url : "/member/idConfirm",
		data : { userId : userId },
		success : function(result){
			if(result == "Y"){
				showSuccessMsg(oMsg, "");
				idFlag = true;
			}
			else{
				showErrorMsg(oMsg, "이미 사용중이거나 탈퇴한 아이디입니다.");
				return false;
			}
		},
		error : function(error){
			alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
			return false;
		}
	});

	return true;
}


// 비밀번호 검사
function checkPswd1(){

    pwFlag = false;

	let pw = $("#pswd1").val();
	let oInput = $("#pswd1");
	let oMsg = $("#pswd1Msg");

	if( pw == "" ){
		showErrorMsg(oMsg, "비밀번호는 필수정보 입니다.");
		setFocusToInputObject(oInput);
		return false;
	}

	if(isValidPswd(pw) != true){
		showErrorMsg(oMsg, "8~25자 영문 소문자, 숫자를 사용하세요. (특수문자 사용가능) (띄어쓰기 불가능)");
		setFocusToInputObject(oInput);
		return false;
	}

    pwFlag = true;
	return true;
}

// 비밀번호 재확인 검사
function checkPswd2(){

	let pswd1 = $("#pswd1").val();
	let pswd2 = $("#pswd2").val();
	let oMsg = $("#pswd2Msg");
	let oInput = $("#pswd2");

	if(pswd2 == ""){
		showErrorMsg(oMsg, "비밀번호 재확인은 필수정보 입니다");
		setFocusToInputObject(oInput);
		return false;
	}

	if(pswd1 != pswd2){
		showErrorMsg(oMsg, "비밀번호가 일치하지 않습니다");
		setFocusToInputObject(oInput);
		return false;
	} else{
		showErrorMsg(oMsg, "");
		setFocusToInputObject(oInput);
		return true;
	}

	return true;
}

// 전화번호 검사
function checkPhoneNum() {

	let phoneNum = $("#phoneNum").val();
	let oMsg = $("#phoneNumMsg");
	let oInput = $("#phoneNum");

	if (phoneNum == "") {
        showErrorMsg(oMsg, "전화번호는 필수정보 입니다.");
        setFocusToInputObject(oInput);
        return false;
    }

    if(!isCellPhone(phoneNum)) {
        showErrorMsg(oMsg,"형식에 맞지 않는 번호입니다.");
        return false;
    }

    hideMsg(oMsg);
    return true;
}


// 인증번호 발송
function sendSmsButton() {

	let phoneNum = $("#phoneNum").val();
	let oMsg = $("#phoneNumMsg");

	authFlag = false;

	$("#authNumMsg").hide();
	if(!isCellPhone(phoneNum)) {
        showErrorMsg(oMsg,"형식에 맞지 않는 번호입니다.");
        return false;
    }

	$.ajax({
		type: "get",
		url: "/member/sendAuth?phoneNum=" + phoneNum,
		encoding: "UTF-8",
		success : function(result){
			if(result == "Y"){
				showSuccessMsg(oMsg, "인증번호를 발송했습니다.(유효시간 3분)");
			}else{
				showErrorMsg(oMsg, "전화번호를 다시 확인해주세요.");
				return false;
			}
		},error : function(){
		    alert("에러가 발생했습니다. \n잠시후 다시 시도해주세요.");
		    return false
		}
	});
	return false;
}

// 인증번호 기본 유효성 검사
function checkAuthNum(){

	let authNum = $("#authNum").val();
	let oMsg = $("#authNumMsg");
	let oInput = $("#authNum");

	if(authNum == ""){
		showErrorMsg(oMsg, "인증이 필요합니다.");
		setFocusToInputObject(oInput);
		return false;
	}

	if(!isAuthNum(authNum)){
		showErrorMsg(oMsg, "인증번호를 다시 확인해주세요.");
		return false;
	}

	if(authFlag){
		showSuccessMsg(oMsg, "인증에 성공 했습니다.");
		$("#phoneNumMsg").hide();
		return true;
	}else{
		checkAuthNumByAjax();
	}
	return true;

}


// 인증번호 확인
function checkAuthNumByAjax(){

	let authNum = $("#authNum").val();
	let phoneNum = $("#phoneNum").val();
	let oMsg = $("#authNumMsg");
	let oInput = $("#authNum");

	$.ajax({
		type: "get",
		url: "/member/authNumCheck?phoneNum=" + phoneNum + "&authNum=" + authNum,
		encoding: "UTF-8",
		success : function(result){
			if(result == "Y"){
				showSuccessMsg(oMsg, "인증에 성공 했습니다.");
				$("#phoneNumMsg").hide();
				authFlag = true;
			} else if (result == "cnt"){
				showErrorMsg(oMsg, "인증을 다시 진행해주세요.");
				setFocusToInputObject(oInput);
				return false;
			} else {
				showErrorMsg(oMsg, "인증번호를 다시 확인해주세요.");
				setFocusToInputObject(oInput);
				return false;
			}
		}
	});
	return true;
}


// 인증번호 유효성 검사
function isAuthNum(a){

	const regAuth = /^[0-9]{6}$/;
	return regAuth.test(a);
}


// 전화번호 유효성 검사
function isCellPhone(p) {
    const regPhone = /^(010[1-9][0-9]{7})$/;
    return regPhone.test(p);
}


// 비밀번호 유효성 검사
function isValidPswd(str){

	if (str == "") return false;

    // 공백이 포함돼있는지 아닌지
    if (str.search(/\s/) != -1) return false;

    const isPw = /^[a-z0-9~`!@#$%\^&*()-]{8,25}$/;
	return isPw.test(str);
}


// input 포커스해주는 함수
function setFocusToInputObject(obj){
	if(submitFlag){
		 submitFlag = false;
         obj.focus();
	}
}


let isShift = false;
function checkShiftUp(e){
	if (e.which && e.which == 16) {
        isShift = false;
    }
}

function checkShiftDown(e) {
    if (e.which && e.which == 16) {
        isShift = true;
    }
}

// 비밀번호 캡스록 검사
function checkCapslock(e){

	let myKeyCode = 0;
	let myShiftKey = false;

	if(window.event){  // IE
		myKeyCode = e.keyCode;
		myShiftKey = e.shiftKey;
	}else if(e.which){  // netscape ff opera
		myKeyCode = e.which;
		myShiftKey = isShift;
	}

	let oMsg = $("#pswd1Msg");
	if((myKeyCode >= 65 && myKeyCode <= 90) && !myShiftKey){
		showErrorMsg(oMsg, "Caps Lock이 켜져 있습니다.");
	}else if ((myKeyCode >= 97 && myKeyCode <= 122) && myShiftKey) {
        showErrorMsg(oMsg,"Caps Lock이 켜져 있습니다.");
    } else{
		hideMsg(oMsg);
	}
}


// 비밀번호 재확인 캡스록 검사
function checkCapslock2(e){

	let myKeyCode = 0;
	let myShiftKey = false;

	if(window.event){   // IE
		myKeyCode = e.keyCode;
		myShiftKey = e.shiftKey;
	}else if(e.which){  // netscape ff opera
		myKeyCode = e.which;
		myShiftKey = isShift;
	}

	let oMsg = $("#pswd2Msg");
	if((myKeyCode >= 65 && myKeyCode <= 90) && !myShiftKey){
		showErrorMsg(oMsg, "Caps Lock이 켜져 있습니다.");
	} else if ((myKeyCode >= 97 && myKeyCode <= 122) && myShiftKey) {
        showErrorMsg(oMsg,"Caps Lock이 켜져 있습니다.");
    } else{
		hideMsg(oMsg);
	}
}

// 메시지 색깔 초록색으로
function showSuccessMsg(obj, msg){

	obj.attr("class", "error_next_box_green");
	obj.html(msg);
	obj.show();
}

// 메시지 색깔 빨간색으로
function showErrorMsg(obj, msg){

	 obj.attr("class", "error_next_box");
     obj.html(msg);
     obj.show();
}


function hideMsg(obj) {
    obj.hide();
}


