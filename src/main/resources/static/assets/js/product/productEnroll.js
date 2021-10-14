$(document).ready(function() {

    // 옵션 추가
	$("#addOption").on("click", function(){
		var objTr = $(this).closest("table").find("tbody>tr").eq(0);
		var objTb = $(this).closest("table").find("tbody");

		var clone = objTr.clone();

		clone.find("button.btnplusminus").removeClass("btn_plus");
		clone.find("button.btnplusminus").addClass("btn_minus");

		clone.find("button.btnplusminus").parent().removeClass("hide");  // 삭제버튼 띄워주기 위해서
		clone.find("button.btnplusminus").click(function(){$(this).parents('tr').remove();});  // 삭제 클릭시 해당라인 삭제 버튼

		clone.find("input").val("");


		if( objTb.find("tr").length < 5) objTb.append(clone);
	});


});

// 옵션값들을 다 더해서 한 배열에 담기위해서
function merge_option(opt1,opt2){
	var result = new Array();
	var k = 0;
	for (var i=0;i<opt1.length;i++){
		for (var j=0;j<opt2.length;j++){
			result[k] = opt1[i]+","+opt2[j];
			k++;
		}
	}
	return result;
}

// 필수옵션 설정 완료
$("#optConfigOk").on("click", function(){
	
	let optName = new Array();
	let optValue = new Array();
	let result = new Array();
	let name = new Array();
	let values = new Array();
	let tmp;
	let num;
	let tag;
	
	$("#optionMakePopup table tbody tr").each(function(idx){		

		tmp = $(this).find("input[name='optionMakeValue[]']").val();
		optValue[idx] = tmp.split(',');
		
		tmp = $(this).find("input[name='optionMakeName[]']").val();		
		optName[idx] = tmp;
	}); 		
		
	// 옵션값 공백 검사
	for(var i=0;i<optValue.length;i++){
		for(var j=0;j<optValue[i].length;j++){
			if(optValue[i][j].length==0) {
				alert("옵션값을 입력해 주세요");
				return false;
			}
		}
	}
	
	// 1. 라인의 개수만큼 반복문을 돌린다.
	for (var i=0;i<optValue.length;i++){
		if(!optValue[i]) continue;
		if( i == 0 ){
			result = optValue[i];			
		} else {
			result = merge_option(result,optValue[i]);			
		}
	}

    optName.splice(0, name.length);

    tag += '<tr>'
    tag += '<td><input type="checkbox" class="form-check-input"/></td>';
    for(var i=0; i<optName.length; i++){
        var j = i+1;
        tag += '<td><input type="text" size="10" class="form-control" name="optionName'+j+'" value="' + optName[i] + '" />';
    }
    tag += '<td><input type="text" value="수량" size="10" class="form-control" /></td>';
    tag += '</tr></br>';

	// html 생성
	for (var i=0; i<result.length; i++){
		values.splice(0, values.length);
		values = result[i].split(',');

		tag += '<tr class="optionTr">';
	 	tag += '<td class="center"><input type="checkbox" name="check" class="form-check-input" /></td>';
		for (var j=0; j<values.length; j++){					
			num = j+1;						
			tag += '<td class="center"><input type="text" size="10" name="option'+ num +'" value="' + values[j] + '" class="form-control"/>';
		}
		
		tag += '<td class="center"><input type="text" size="10" name="stock" value="0" class="form-control" />';
		tag += '</tr>';		
	}
	
	let tbody = $("#optionFinalValues");
	tbody.empty();	
	tbody.append(tag);
	
});


// 선택한 옵션 지우기
function removeOption(){
	
	$("input:checkbox[name=check]").each(function() {
          if (this.checked)
          {
                let tr = $(this).parent().parent();
                tr.remove(); // 삭제하기
          }

      });
    let length = $("input:checkbox[name=check]").length;

    if(length == 0){
        var body = $("#optionFinalValues");
        body.empty();
    }

}
