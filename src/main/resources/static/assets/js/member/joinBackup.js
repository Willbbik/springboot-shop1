let idFlag = false;
let pwFlag = false;
let authFlag = false;
let submitFlag = false;

// input이 포커스됐다가 취소되면 함수실행
$(document).ready(function(){
	$("#userid").blur(function(){
		idFlag = false;
		checkId("first");
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

    $("#authSendBtn").click(function(){
		sendSmsButton();
		return false;
	});

    $("#btnjoin").click(function(event){
		submitClose();
		if(idFlag && pwFlag && authFlag){
			mainSubmit();
		} else {
			setTimeout(function(){
				mainSubmit();
			}, 200);
		}
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
    if (checkId('join')
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


// 가입하기 버튼 막기
function submitClose(){
	submitFlag = true;
	$("#btnJoin").attr("disabled",true);
}


// 아이디 유효성검사
function checkId(event){

	if(idFlag) return true;

	let userid = $("#userid").val();
	let oMsg = $("#idMsg");
	let oInput = $("#userid");

	if ( userid == "" ){
		showErrorMsg(oMsg, "아이디는 필수정보 입니다.");
		setFocusToInputObject(oInput);
		return false;
	}

	const isID = /^[a-z0-9][a-z0-9_\-]{4,20}$/;
	if (!isID.test(userid)){
		showErrorMsg(oMsg, "5~20자의 영문 소문자, 숫자와 특수기호(_),(-)만 사용 가능합니다. (띄어쓰기 불가능)");
		setFocusToInputObject(oInput);
		return false;
	}

	idFlag = false;
	$.ajax({
		type : "GET",
		url : "/member/idConfirm?userid=" + userid,
		encoding: "UTF-8",
		success : function(result){
			if(result == "Y"){
				if(event == "first"){
					showSuccessMsg(oMsg, "");
				} else{
					hideMsg(oMsg);
				}
				idFlag = true;
			}
			else{
				showErrorMsg(oMsg, "이미 사용중이거나 탈퇴한 아이디입니다.");
			}
		},
		error : function(error){
			alert("실패했습니다");
		}
	});

	return true;
}


// 비밀번호 검사
function checkPswd1(){
	if(pwFlag) return true;

	let pw = $("#pswd1").val();
	let oInput = $("#pswd1");
	let oMsg = $("#pswd1Msg");

	if( pw == "" ){
		showErrorMsg(oMsg, "비밀번호는 필수정보 입니다.");
		setFocusToInputObject(oInput);
		return false;
	}

	if(isValidPasswd(pw) != true){
		showErrorMsg(oMsg, "8~25자 영문 대 소문자, 숫자를 사용하세요. (특수문자 사용가능) (띄어쓰기 불가능)");
		setFocusToInputObject(oInput);
		return false;
	}

	pwFlag = false;
	$.ajax({
		type : "GET",
		url : "/member/pswdCheck?pswd=" + pw,
		encoding: "UTF-8",
		success : function(data){
			if(data == "Y"){
				showSuccessMsg(oMsg, "");
				setFocusToInputObject(oInput);
			}
			else if(data == "N"){
				showErrorMsg(oMsg, "8~25자 영문 대 소문자, 숫자를 사용하세요. (특수문자 사용가능) (띄어쓰기 불가능)");
				return false;
			}
			pwFlag = true;
		},
		error : function(error){
			alert("실패했습니다.");
		}
	});
	return true;

}

// 비밀번호 재확인 검사
function checkPswd2(){

	let pswd1 = $("#pswd1");
	let pswd2 = $("#pswd2");
	let oMsg = $("#pswd2Msg");
	let oInput = $("#pswd2");


	if(pswd2.val() == ""){
		showErrorMsg(oMsg, "비밀번호 재확인은 필수정보 입니다");
		setFocusToInputObject(oInput);
		return false;
	}

	if(pswd1.val() != pswd2.val()){
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

    //hideMsg(oMsg);
    return true;

}



// 인증번호 발송
function sendSmsButton() {

	let phoneNum = $("#phoneNum").val();
	let oMsg = $("#phoneNumMsg");

	// phoneNum = phoneNum.replace(/ /gi, "").replace(/-/gi, "");
	// $("#phoneNum").val(phoneNum);
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
			}
		},
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
		showSuccessMsg(oMsg, "");
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
				showSuccessMsg(oMsg, "");
				//$("#phoneNumMsg").hide();
				authFlag = true;
			} else if (result == "cnt"){
				showErrorMsg(oMsg, "인증을 다시 진행해주세요.");
				setFocusToInputObject(oInput);
			} else {
				showErrorMsg(oMsg, "인증번호를 다시 확인해주세요.");
				setFocusToInputObject(oInput);
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
    // const regPhone = /^((01[1|6|7|8|9])[1-9][0-9]{6,7})$|(010[1-9][0-9]{7})$/;
    const regPhone = /^(010[1-9][0-9]{7})$/;
    return regPhone.test(p);
}



// 비밀번호 유효성 검사
function isValidPasswd(str){

	let cnt = 0;
	if (str == ""){
		return false;
	}

	// 공백이 포함돼있는지 아닌지
	let retVal = checkSpace(str);
	if (retVal){
		return false;
	}

	// 길이 확인
	if(str.length < 8){
		return false;
	}

	// 똑같은 단어 계속 못쓰게끔
	for (let i = 0; i < str.length; ++i) {
        if (str.charAt(0) == str.substring(i, i + 1))
            ++cnt;
    }
	if(cnt == str.length){
		return false;
	}

	const isPW = /^[A-Za-z0-9`\-=\\\[\];',\./~!@#\$%\^&\*\(\)_\+|\{\}:"<>\?]{7,25}$/;

	if(!isPW.test(str)){
		return false;
	}


	return true;
}



// input 포커스해주는 함수
function setFocusToInputObject(obj){
	if(submitFlag){
		 submitFlag = false;
         obj.focus();
	}
}


// 띄어쓰기 검사
function checkSpace(str) {
    if (str.search(/\s/) != -1) {
        return true;
    } else {
        return false;
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

// 캡스록 검사
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


// 캡스록 검사
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








      
