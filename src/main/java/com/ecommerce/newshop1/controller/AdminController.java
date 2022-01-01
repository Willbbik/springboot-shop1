package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemImage;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.service.ItemService;
import com.ecommerce.newshop1.service.MemberService;
import com.ecommerce.newshop1.service.OrderService;
import com.ecommerce.newshop1.service.AwsS3Service;
import com.ecommerce.newshop1.utils.ItemPagination;
import com.ecommerce.newshop1.utils.ValidationSequence;
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
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
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
    private final MemberService memberService;
    private final AwsS3Service awsS3Service;

    @GetMapping("/admin/main")
    public String adminMain(Model model) {

        Pageable pageable = PageRequest.ofSize(3);
        List<OrderItemDto> ingOrderItems = orderService.searchByDeliveryStatus(DeliveryStatus.DELIVERY_ING, pageable);    // 배송중 상품
        List<OrderDto> depositOrderItems = orderService.searchByDepositSuccess(DeliveryStatus.DEPOSIT_SUCCESS, pageable); // 입금완료된 상품
        List<MemberDto> memberDtos = memberService.findAll(pageable);                                                     // 회원정보

        model.addAttribute("ingOrderItems", ingOrderItems);
        model.addAttribute("depositOrderItems", depositOrderItems);
        model.addAttribute("memberDtos", memberDtos);

        return "admin/admin_main";
    }

    @GetMapping("/admin/write/notice")
    @ApiOperation(value = "공지사항 페이지")
    public String write(){
        return "admin/admin_writenotice";
    }


    @PostMapping("/admin/write/notice")
    @ApiOperation(value = "공지사항 저장")
    public String writeNotice(BoardDto boardDto){

        System.out.println(boardDto.getSubject());
        return "redirect:/admin/write/notice";
    }

    @GetMapping("/admin/register")
    @ApiOperation(value = "상품 등록 페이지")
    public String itemRegister(){

        return "admin/admin_registerItem";
    }

    @GetMapping("/admin/send/orderItem")
    @ApiOperation(value = "상품 배송 페이지")
    public String sendOrderItemPage(Model model, @RequestParam(name = "page", defaultValue = "1") int page, SearchDto searchDto){

        // 배달해야하는 상품 총 개수와 그걸로 페이징처리
        Long total = orderService.searchTotalOrderItem(DeliveryStatus.DEPOSIT_SUCCESS, searchDto);
        ItemPagination pagination = new ItemPagination(total, page);

        Pageable pageable = PageRequest.of(pagination.getCurPage() - 1, 10, Sort.Direction.DESC, "id");
        List<OrderItemDto> orderItems = orderService.searchAllByDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS, pageable, searchDto);

        model.addAttribute("page", pagination);
        model.addAttribute("curPage", pagination.getCurPage());
        model.addAttribute("startPage", pagination.getStartPage());
        model.addAttribute("endPage", pagination.getEndPage());

        model.addAttribute("orderItems", orderItems);
        model.addAttribute("searchDto", searchDto);
        return "admin/admin_sendOrderItem";
    }


    @GetMapping("/admin/itemList")
    @ApiOperation(value = "상품 목록 페이지")
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


    @PostMapping("/admin/register")
    @ApiOperation(value = "관리자페이지에서 상품 등록")
    public String itemSave(MultipartHttpServletRequest mtfRequest, @Validated(ValidationSequence.class) ItemDto itemDto, BindingResult errors) throws Exception {

        // 상품 정보 저장
        Item item = mapper.map(itemDto, Item.class);

        // 상품 이미지 저장
        List<MultipartFile> fileList =  mtfRequest.getFiles("upload_image");
        List<ItemImage> itemImageList = new ArrayList<>();

        if (fileList.size() != 0) {
            for (int i = 0; i < fileList.size(); i++) {

                String originImageName = fileList.get(i).getOriginalFilename();
                String imageName = awsS3Service.createFileName(originImageName);

                String filePath = "static/images/" + itemDto.getCategory() + "/" + itemDto.getItemName() + "/" + imageName;

                // s3에 이미지 저장
                String s3ImageUrl = awsS3Service.upload(fileList.get(i), filePath);
                // 첫번째 사진이 대표 이미지
                if (i == 0) item.setImageUrl(s3ImageUrl);

                ItemImage itemImage = ItemImage.builder()
                        .imageUrl(filePath)
                        .imageName(originImageName)
                        .build();
                itemImageList.add(itemImage);

            }
            for (ItemImage itemImage : itemImageList) {
                item.setItemImageList(itemImage);
            }
        }
        itemRepository.save(item);
        return "redirect:/admin/itemList";
    }

    @DeleteMapping("/admin/item/delete")
    @ApiOperation(value = "관리자페이지에서 단일 상품 삭제")
    public @ResponseBody String itemDelete (@RequestParam List <Long> itemIdList) {
        itemRepository.deleteAllById(itemIdList);
        return "상품 삭제 완료";
    }

}
