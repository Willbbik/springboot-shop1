package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.ItemOptDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemImage;
import com.ecommerce.newshop1.entity.ItemOption;
import com.ecommerce.newshop1.service.ProductServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ProductServiceImpl productService;

    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    @GetMapping("/admin/main")
    public String adminMain(){
        return "admin/admin_main";
    }

    @GetMapping("/admin/register")
    public String itemRegister(){
        return "admin/admin_registerProduct";
    }

    @PostMapping("/admin/register")
    public String itemSave(MultipartHttpServletRequest mtfRequest, ItemDto itemDto, List<ItemOptDto> itemOptDto) throws Exception {

        // 디렉토리 만들기
        String FolderPath = "/Users/min/Documents/쇼핑몰/newshop1/src/main/resources/static/assets/images/Item/"
                          + itemDto.getCategory() + "/" + itemDto.getProductName();
        // view에 뿌려줄 이미지 경로
        String getFolderPath = "/resources/static/assets/images/Item/" + itemDto.getCategory() + "/" + itemDto.getProductName();

        File newFile = new File(FolderPath);
        if(newFile.mkdirs()){
            logger.info("directory make ok");
        }else{
            logger.warn("directory can't make");
            // 예외 던져야 할 수도
        }

        // 상품 정보 저장
        Item item = Item.builder()
                .category(itemDto.getCategory())
                .model(itemDto.getModel())
                .price(itemDto.getPrice())
                .productInfo(itemDto.getProductInfo())
                .productName(itemDto.getProductName())
                .build();
        productService.saveItem(item);

        // 상품 이미지 저장
        List<MultipartFile> fileList =  mtfRequest.getFiles("upload_image");
        for (int i = 0; i < fileList.size(); i++) {
            String uuid = UUID.randomUUID().toString();

            String originFileName = fileList.get(i).getOriginalFilename();
            String saveImage = FolderPath + "/" + uuid + "_" + originFileName;
            String imageUrl = getFolderPath + "/" + uuid + "_" + originFileName ;

            try{
                ItemImage itemImage = ItemImage.builder()
                        .item(item)
                        .imageUrl(imageUrl)
                        .imageName(originFileName)
                        .build();
                productService.saveItemImage(itemImage);

                fileList.get(i).transferTo(new File(saveImage));
            } catch (Exception e) {
                throw new Exception("save ItemImage exception");
            }
        }
        

        return "admin/admin_registerProduct";
    }

//    @PostMapping("/admin/register")
//    public String itemSave(MultipartHttpServletRequest mtfRequest, ItemDto itemDto){
//
//        // 디렉토리 만들기
//        String folderPath =
//                "/Users/min/Documents/쇼핑몰/newshop1/src/main/resources/static/assets/images/Item"
//                        + itemDto.getCategory() + "/" + itemDto.getProductName();
//        File newFile = new File(folderPath);
//        if(newFile.mkdirs()){
//            logger.info("directory make ok");
//        }else{
//            logger.warn("directory can't make");
//            // 예외 던져야 할 수도
//        }
//
//        List<MultipartFile> fileList =  mtfRequest.getFiles("upload_image");
//        for (int i = 0; i < fileList.size(); i++) {
//            String originFileName = fileList.get(i).getOriginalFilename();
//            String safeFile = folderPath + originFileName;
//
//            String upperCategory = itemDto.getCategory().toUpperCase(Locale.ROOT);
//            String newUrl = "/images/Item/" + upperCategory + "/" + itemDto.getProductName() + "/" + originFileName;
//
//            try{
//
//            }catch{
//
//            }
//        }
//
//
//
//        return "admin/admin_registerProduct";
//    }





}
