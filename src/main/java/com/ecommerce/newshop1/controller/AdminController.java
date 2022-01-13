package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemImage;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.utils.CommonService;
import com.ecommerce.newshop1.utils.PaginationShowSizeTen;
import com.ecommerce.newshop1.utils.ValidationSequence;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class AdminController {

    private final ItemRepository itemRepository;
    private final ItemService itemService;
    private final OrderService orderService;
    private final OrderItemService orderItemService;
    private final MemberService memberService;
    private final AwsS3Service awsS3Service;
    private final CommonService commonService;
    private final SecurityService security;
    private final ModelMapper mapper;

    @GetMapping("/admin/main")
    @ApiOperation(value = "관리자 메인 페이지")
    public String adminMain(Model model) {

        Pageable pageable = PageRequest.ofSize(3);
        Long ingOrderItemTotal = orderItemService.searchTotal(DeliveryStatus.DELIVERY_ING, new SearchDto());
        Long depositOrderTotal = orderService.countByDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS);

        List<OrderItemDto> ingOrderItems = orderItemService.searchByDeliveryStatus(DeliveryStatus.DELIVERY_ING, pageable);  // 배송중 상품
        List<OrderDto> depositOrderItems = orderService.searchByDeliveryStatus(DeliveryStatus.DEPOSIT_SUCCESS, pageable);   // 입금완료된 주문
        List<MemberDto> members = memberService.findAll(pageable);                                                       // 회원가입한 유저

        model.addAttribute("ingOrderItems", ingOrderItems);
        model.addAttribute("depositOrderItems", depositOrderItems);
        model.addAttribute("members", members);

        model.addAttribute("ingOrderItemTotal", ingOrderItemTotal);
        model.addAttribute("depositOrderTotal", depositOrderTotal);

        return "admin/admin_main";
    }

    @GetMapping("/admin/register")
    @ApiOperation(value = "상품 등록 페이지")
    public String itemRegister(){

        return "admin/admin_registerItem";
    }

    @GetMapping("/admin/itemList")
    @ApiOperation(value = "상품 목록 페이지")
    public String itemListPage(@RequestParam(name = "page", defaultValue = "1") int curPage, SearchDto searchDto, Model model){

        Long totalPost = itemService.searchTotal(searchDto);
        PaginationShowSizeTen page = new PaginationShowSizeTen(totalPost, curPage);

        // 상품 가져오기
        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowMaxSize());
        List<ItemDto> items = itemService.searchAll(searchDto, pageable);

        model.addAttribute("page", page);
        model.addAttribute("items", items);
        model.addAttribute("searchDto", searchDto);

        return "admin/admin_itemList";
    }

    @GetMapping("/admin/send/orderItem")
    @ApiOperation(value = "상품 배송 페이지")
    public String sendOrderItemPage(@RequestParam(name = "page", defaultValue = "1") int curPage, SearchDto searchDto, Model model){

        // 배달해야하는 상품 총 개수와 그걸로 페이징처리
        Long total = orderItemService.searchTotal(DeliveryStatus.DELIVERY_READY, searchDto);
        PaginationShowSizeTen page = new PaginationShowSizeTen(total, curPage);

        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowMaxSize());
        List<OrderItemDto> orderItems = orderItemService.searchAllByDeliveryStatusAndSearchDto(DeliveryStatus.DELIVERY_READY, searchDto, pageable);

        model.addAttribute("page", page);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("searchDto", searchDto);
        return "admin/admin_sendOrderItem";
    }


    @GetMapping("/admin/orderList")
    @ApiOperation(value = "주문 상품 목록", notes = "여기서 상품 배송상태를 입금완료 > 배송중으로 변경")
    public String depositItemList(@RequestParam(name = "page", defaultValue = "1") int curPage, SearchDto searchDto, Model model){

        DeliveryStatus deliveryStatus = DeliveryStatus.findByDeliveryStatus(searchDto.getDeliveryStatus());

        Long total = orderItemService.searchTotal(deliveryStatus, searchDto);
        PaginationShowSizeTen page = new PaginationShowSizeTen(total, curPage);

        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowMaxSize());
        List<OrderItemDto> orderItems = orderItemService.searchAllByDeliveryStatusAndSearchDto(deliveryStatus, searchDto, pageable);


        model.addAttribute("page", page);
        model.addAttribute("orderItems", orderItems);
        model.addAttribute("searchDto", searchDto);
        return "admin/admin_orderList";
    }


    @PatchMapping("/admin/deliveryStatus/change")
    @ApiOperation(value = "주문 상태 변경")
    public @ResponseBody String deliveryStatusChange(@RequestParam Long orderItemId, @RequestParam DeliveryStatus deliveryStatus){

        boolean result = orderService.changeOrderItemStatus(orderItemId, deliveryStatus);
        if(!result) {
            return "fail";
        }

        return "success";
    }

    @PostMapping("/admin/delivery/item")
    @ApiOperation(value = "운송장 번호 입력 후 상품 배송처리", notes = "상품 배송")
    public @ResponseBody String deliveryItem(Long orderItemId, String orderNum, String wayBillNum){

        boolean result = orderItemService.existsOrderItem(orderNum, orderItemId);
        if (!result) {
            return "fail";
        }

        orderItemService.saveWayBillNum(orderItemId, wayBillNum);
        return "success";
    }


    @PostMapping("/admin/item/register")
    @ApiOperation(value = "관리자페이지에서 상품 등록")
    public @ResponseBody String itemSave(MultipartHttpServletRequest mtfRequest, @Validated(ValidationSequence.class) ItemDto itemDto, BindingResult errors) throws Exception {

        // 상품 유효성 검사 && 권한 검사
        if(errors.hasErrors()){
            return commonService.getErrorMessage(errors);
        }
        if(!security.checkHasRole(Role.ADMIN.getValue())){
            return "관리자 권한이 필요합니다";
        }

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
        return "success";
    }

    @DeleteMapping("/admin/item/delete")
    @ApiOperation(value = "관리자페이지에서 상품 삭제")
    public @ResponseBody String itemDelete (@RequestParam List <Long> itemIdList) {
        itemRepository.deleteAllById(itemIdList);
        return "상품 삭제 완료";
    }

}
