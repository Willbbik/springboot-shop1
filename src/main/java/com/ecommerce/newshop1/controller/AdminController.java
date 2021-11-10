package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemImage;
import com.ecommerce.newshop1.service.ItemService;
import com.ecommerce.newshop1.service.ProductServiceImpl;
import com.ecommerce.newshop1.utils.ItemPagination;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ProductServiceImpl productService;
    private final ItemService itemService;
    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);

    ModelMapper mapper = new ModelMapper();


    @GetMapping("/admin/main")
    public String adminMain(){

        return "admin/admin_main";
    }

    @GetMapping("/admin/register")
    public String itemRegister(){

        return "admin/admin_registerItem";
    }


    @GetMapping("/admin/itemList")
    public String itemListPage(Model model, @RequestParam(name = "page", defaultValue = "1") int page, SearchDto searchDto){

        page = (page < 1) ? 1 : page;
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.Direction.DESC, "createdDate");
        Page<ItemDto> result = itemService.searchAll(searchDto, pageable);
        List<ItemDto> items = result.getContent();          // 상품들

        // 페이징기능. 총 게시글 개수와 현재 페이지 저장 후 계산
        ItemPagination pagination = new ItemPagination();
        pagination.setTotalPost(result.getTotalElements());
        pagination.setCurPage(page);
        pagination.calculate();

        model.addAttribute("items", items);
        model.addAttribute("page", pagination);
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        return "admin/admin_itemList";
    }


    @PostMapping("/admin/register")
    public String itemSave(MultipartHttpServletRequest mtfRequest, ItemDto itemDto) throws Exception {

        // 디렉토리 만들기
        String folderPath = "/Users/min/Documents/쇼핑몰/newshop1/src/main/resources/static/assets/images/Item/"
                          + itemDto.getCategory() + "/" + itemDto.getItemName();
        // view에 뿌려줄 이미지 경로
        String getFolderPath = "/assets/images/Item/" + itemDto.getCategory() + "/" + itemDto.getItemName();

        File newFile = new File(folderPath);
        if(newFile.mkdirs()){
            logger.info("directory make ok");
        }else{
            logger.warn("directory can't make");
            // 예외 던져야 할 수도
        }

        // 상품 정보 저장
        Item item = Item.builder()
                .itemName(itemDto.getItemName())
                .category(itemDto.getCategory())
                .color(itemDto.getColor())
                .size(itemDto.getSize())
                .price(itemDto.getPrice())
                .model(itemDto.getModel())
                .itemInfo(itemDto.getItemInfo())
                .saleStatus(itemDto.getSaleStatus())
                .build();

        productService.saveItem(item);

        // 상품 이미지 저장
        List<MultipartFile> fileList =  mtfRequest.getFiles("upload_image");
        for (int i = 0; i < fileList.size(); i++) {
            String uuid = UUID.randomUUID().toString();

            String originFileName = fileList.get(i).getOriginalFilename();
            String finalFolderUrl = folderPath + "/" + uuid + "_" + originFileName;
            String localImageUrl = getFolderPath + "/" + uuid + "_" + originFileName ;

            if(i == 0){
                item.setImageUrl(localImageUrl);
                productService.saveItem(item);
            }

            try{
                ItemImage itemImage = ItemImage.builder()
                        .item(item)
                        .imageUrl(localImageUrl)
                        .imageName(originFileName)
                        .build();
                productService.saveItemImage(itemImage);

                fileList.get(i).transferTo(new File(finalFolderUrl));
            } catch (Exception e) {
                logger.warn("when save ItemImage exception");
                throw new Exception("when save ItemImage exception");
            }
        }
        // 옵션 저장 해야함

        return "redirect:/admin/itemList";
    }




}
