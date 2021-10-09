
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
	
	var optName = new Array();
	var optValue = new Array();
	var result = new Array();	
	var values = new Array();
	var tmp;	
	var num;
	var tag;
	
	$("#optionMakePopup table tbody tr").each(function(idx){		

		tmp = $(this).find("input[name='optionMakeValue[]']").val();
		optValue[idx] = tmp.split(',');
		
		tmp = $(this).find("input[name='optionMakeName[]']").val();		
		optName[idx] = tmp;	
				
	}); 		
		
	
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
			
	
	for (var i=0; i<result.length; i++){
		key = i + 1;
		values.splice(0, values.length);
		values = result[i].split(',');
		
		tag += '<tr class="optionTr">';						
	 	tag += '<td class="center"><input type="checkbox" name="check" "/></td>';
		for (var j=0; j<values.length; j++){					
			num = j+1;						
			tag += '<td class="center"><input type="text" size="10" name="option'+ num +'" value="' + values[j] + '" class="line input-box-default-text-code "/>';
			key++;							
		}
		
		tag += '<td class="center"><input type="text" size="10" name="stock" value="0" />';
		tag += '</tr>';		
	}
	
	var tbody = $("#optionFinalValues");
	tbody.empty();	
	tbody.append(tag);
	
});


// 선택한 옵션 지우기
function removeOption(){
	
	$("input:checkbox[name=check]").each(function() {
          if (this.checked)
          {
                var tr = $(this).parent().parent();
                tr.remove(); // 삭제하기
          }
      }); 
}



    