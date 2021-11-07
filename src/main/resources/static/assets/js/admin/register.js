
$(function(){

    // 옵션 합치기
    function combine_option(color, size){

       let result = new Array();
       let html = "";
       let k = 0;



       for (var i=0; i<color.length; i++){
            for(var j=0; j<size.length; j++){
                html += "<div><span>";
                html += "<input type='checkbox' class='form-check-input' />";
                html += "<input type='text' name='color' value='"+ color[i] +"'/>";
                html += "<input type='text' name='size' value='"+ size[j] + "'/>";
                html += "<input type='text' name='quantity' value='0'>";
                html += "</span></div>";
            }
       }

       let modal = $("#options");
       modal.html(html);

       return result;

    }

    $("#makeOption").on("click", function(){

        let result = new Array();
        let sizeList = new Array();
    	let colorList = new Array();
    	let tmp;

        tmp = $("textarea[name='color']").val();
        colorList = tmp.split(",");

        tmp = $("textarea[name='size']").val();
        sizeList = tmp.split(",");

//        if(Array.isArray(colorList) && colorList.length == 0){
//            let optionModal = $("#option_check").val();
//            optionModal.empty();
//            alert("success");
//        }

        result = combine_option(colorList, sizeList);


    });


    //파일 업로드시 썸네일
    $("#upload_image").change(function(e){
        const images = e.target.files;
        $(".img-box").empty();
        for(let i=0; i<images.length; i++){
            const Reader = new FileReader();
            Reader.readAsDataURL(images[i]);
            Reader.onload = function(){
                const img = '<img src="'+Reader.result +'" alt="사진">';
                $('.img-box').append(img);
            }
        }
    });




});

