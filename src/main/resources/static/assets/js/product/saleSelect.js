

/* $(document).ready(function(){
	$("#container li select").change(function(){
		if(!$(this).hasClass('nomore-option')){
			var val = $(this).val();
			var str = $(this).attr('id').split('-');
			var id  = str[0] + '-' + str[1].substr(0,1);
			
			var val = $(this).val();
			var idx = $('select[id^=' + id + ']').index($(this));
			
			if(val == ''){
				var $el = $('select[id^=' + id + ']:gt(' + idx + ')');
				
				$el.val('');
				$el.attr('disabled', true);
			}
			
			$el.val('');
			$el.attr('disabled', true);
		}		
	});		
}); */

/*
$(document).ready(function() {
	
 $('#select-1').change(function() {
    
	var keyboard = ["갈축","청축","적축"];
	var mouse = ["광마우스","유선마우스","비싼마우스","미키마우스"];
	var monitor = ["17인치","22인치","24인치","26인치"];
	 
	var selectItem = $("#select-1").val();
	 
	var changeItem;
	  
	  
	if(selectItem == "키보드"){
	  changeItem = keyboard;
	}else if(selectItem == "마우스"){
	  changeItem = mouse;
	}else if(selectItem == "모니터"){
	  changeItem =  monitor;
	}
	
	
	$('#select2').empty();
	 
	for(var count = 0; count < changeItem.length; count++){                
        var option = $("<option>"+changeItem[count]+"</option>");
        $('#select2').append(option);
    }
  });
}); */

function itemChange(){
 
		var keyboard = ["갈축","청축","적축"];
		var mouse = ["광마우스","유선마우스","비싼마우스","미키마우스"];
		var monitor = ["17인치","22인치","24인치","26인치"];
		 
		var selectItem = $("#select1").val();
		 
		var changeItem;
		  
		if(selectItem == "키보드"){
		  changeItem = keyboard;
		}
		else if(selectItem == "마우스"){
		  changeItem = mouse;
		}
		else if(selectItem == "모니터"){
		  changeItem =  monitor;
		}
 
		$('#select2').empty();
 
		for(var count = 0; count < changeItem.length; count++){                
                var option = $("<option>"+changeItem[count]+"</option>");
                $('#select2').append(option);
         }
 
}


