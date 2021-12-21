package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemImage;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.OrderRepository;
import com.ecommerce.newshop1.service.ItemService;
import com.ecommerce.newshop1.service.OrderService;
import com.ecommerce.newshop1.utils.ItemPagination;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


@Controller
@RequiredArgsConstructor
public class AdminController {

    private final static Logger logger = LoggerFactory.getLogger(AdminController.class);
    ModelMapper mapper = new ModelMapper();

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final OrderService orderService;



    @GetMapping("/admin/main")
    public String adminMain(Model model) {

        Pageable pageable = PageRequest.ofSize(3);
        List<OrderItemDto> ingOrderItems = orderService.searchByDeliveryStatus(DeliveryStatus.DELIVERY_ING, pageable);    // 배송중 상품
        List<OrderDto> depositOrderItems = orderService.searchByDepositSuccess(DeliveryStatus.DEPOSIT_SUCCESS, pageable); // 입금완료된 상품

        model.addAttribute("ingOrderItems", ingOrderItems);
        model.addAttribute("depositOrderItems", depositOrderItems);

        return "admin/admin_main";
    }

    @GetMapping("/admin/write/notice")
    public String write(){
        return "admin/admin_writenotice";
    }

    @ApiOperation(value = "공지사항 저장")
    @PostMapping("/admin/write/notice")
    public String writeNotice(BoardDto boardDto){

        System.out.println(boardDto.getSubject());
        return "redirect:/admin/write/notice";
    }

    @GetMapping("/admin/register")
    public String itemRegister(){

        return "admin/admin_registerItem";
    }

    @GetMapping("/admin/itemList")
    public String itemListPage(Model model, @RequestParam(name = "page", defaultValue = "1") int page, SearchDto searchDto){

        // 상품 총 개수
        Long totalPost = itemService.searchTotal(searchDto);

        // 페이징기능. 상품 개수와 현재 페이지 저장 후 계산
        ItemPagination pagination = new ItemPagination(totalPost, page);
        int curPage = pagination.getCurPage();

        // 상품 가져오기
        Pageable pageable = PageRequest.of(curPage - 1, 10, Sort.Direction.DESC, "createdDate");
        List<ItemDto> items = itemService.searchAll(searchDto, pageable);

        model.addAttribute("page", pagination);
        model.addAttribute("curPage", curPage);
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());
        
        model.addAttribute("items", items);
        model.addAttribute("itemName", searchDto.getItemName());
        model.addAttribute("category", searchDto.getCategory());
        model.addAttribute("saleStatus", searchDto.getSaleStatus());

        return "admin/admin_itemList";
    }

//    @PostMapping("/admin/register")
//    @ApiOperation(value = "관리자페이지에서 상품 등록")
//    public String itemSave(MultipartHttpServletRequest mtfRequest, ItemDto itemDto) throws Exception {
//
//        String itemCode = UUID.randomUUID().toString();
//
//        // 상품 정보 저장
//        Item item = Item.builder()
//                .itemName(itemDto.getItemName())
//                .itemCode(itemCode)
//                .category(itemDto.getCategory())
//                .color(itemDto.getColor())
//                .size(itemDto.getSize())
//                .price(itemDto.getPrice())
//                .model(itemDto.getModel())
//                .itemInfo(itemDto.getItemInfo())
//                .saleStatus(itemDto.getSaleStatus())
//                .build();
//
//        itemService.saveItem(item);
//
//
//        // 디렉토리 만들기
//        String folderPath = "/Users/min/Documents/쇼핑몰/newshop1/src/main/resources/static/assets/images/Item/"
//                + itemDto.getCategory() + "/" + itemDto.getItemName();
//        // view에 뿌려줄 이미지 경로
//        String getFolderPath = "/assets/images/Item/" + itemDto.getCategory() + "/" + itemDto.getItemName();
//
//        File newFile = new File(folderPath);
//        if(newFile.mkdirs()){
//            logger.info("directory make ok");
//        }else{
//            logger.warn("directory can't make");
//            // 예외 던져야 할 수도
//        }
//
//        // 상품 이미지 저장
//        List<MultipartFile> fileList =  mtfRequest.getFiles("upload_image");
//        for (int i = 0; i < fileList.size(); i++) {
//            String uuid = UUID.randomUUID().toString();
//
//            String originFileName = fileList.get(i).getOriginalFilename();
//            String finalFolderUrl = folderPath + "/" + uuid + "_" + originFileName;
//            String localImageUrl = getFolderPath + "/" + uuid + "_" + originFileName ;
//
//            if(i == 0){
//                item.setImageUrl(localImageUrl);
//                itemService.saveItem(item);
//            }
//
//            try{
//                ItemImage itemImage = ItemImage.builder()
//                        .item(item)
//                        .imageUrl(localImageUrl)
//                        .imageName(originFileName)
//                        .build();
//                itemService.saveItemImage(itemImage);
//
//                fileList.get(i).transferTo(new File(finalFolderUrl));
//            } catch (Exception e) {
//                logger.warn("when save ItemImage exception");
//                throw new Exception("when save ItemImage exception");
//            }
//        }
//        // 옵션 저장 해야함
//
//        return "redirect:/admin/itemList";
//    }

    @PostMapping("/admin/register")
    @ApiOperation(value = "관리자페이지에서 상품 등록")
    public String itemSave(MultipartHttpServletRequest mtfRequest, ItemDto itemDto) throws Exception {

        String itemCode = UUID.randomUUID().toString();

        // 상품 정보 저장
        Item item = mapper.map(itemDto, Item.class);

        // 디렉토리 만들기
        String folderPath = "/Users/min/Documents/쇼핑몰/newshop1/src/main/resources/static/assets/images/Item/"
                + itemDto.getCategory() + "/" + itemDto.getItemName();
        // view에서 사용할 이미지 경로
        String getFolderPath = "/assets/images/Item/" + itemDto.getCategory() + "/" + itemDto.getItemName();

        File newFile = new File(folderPath);
        if(newFile.mkdirs()){
            logger.info("directory make ok");
        }else{
            logger.warn("directory can't make");
            // 예외 던져야 할 수도
        }

        // 상품 이미지 저장
        List<MultipartFile> fileList =  mtfRequest.getFiles("upload_image");
        List<ItemImage> itemImageList = new ArrayList<>();
        for (int i = 0; i < fileList.size(); i++) {
            String uuid = UUID.randomUUID().toString();

            String originFileName = fileList.get(i).getOriginalFilename();
            String finalFolderUrl = folderPath + "/" + uuid + "_" + originFileName;
            String localImageUrl = getFolderPath + "/" + uuid + "_" + originFileName ;

            if(i == 0){
                item.setImageUrl(localImageUrl);
            }

            try{
                ItemImage itemImage = ItemImage.builder()
                        .imageUrl(localImageUrl)
                        .imageName(originFileName)
                        .build();
                itemImageList.add(itemImage);
                fileList.get(i).transferTo(new File(finalFolderUrl));
            } catch (Exception e) {
                logger.warn("when save ItemImage exception");
                throw new Exception("when save ItemImage exception");
            }
        }

        for(ItemImage itemImage : itemImageList){
            item.setItemImageList(itemImage);
        }
        itemRepository.save(item);

        return "redirect:/admin/itemList";
    }


    @DeleteMapping("/admin/item/delete")
    @ApiOperation(value = "관리자페이지에서 단일 상품 삭제")
    public @ResponseBody String itemDelete(@RequestParam List<Long> itemIdList){
        itemRepository.deleteAllById(itemIdList);
        return "상품 삭제 완료";
    }


}
