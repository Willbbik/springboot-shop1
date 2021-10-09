$(document).ready(function() {

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