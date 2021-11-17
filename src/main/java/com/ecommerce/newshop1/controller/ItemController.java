package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.ItemImageDto;
import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.exception.ItemNotFoundException;
import com.ecommerce.newshop1.repository.ItemImageRepository;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.QnARepository;
import com.ecommerce.newshop1.service.ItemService;
import com.ecommerce.newshop1.service.QnAService;
import com.ecommerce.newshop1.service.QnAServiceImpl;
import com.ecommerce.newshop1.utils.SecurityService;
import com.ecommerce.newshop1.utils.enums.Role;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemService itemService;
    private final ItemRepository itemRepository;
    private final ItemImageRepository itemImageRepository;
    private final QnARepository qnARepository;
    private final QnAService qnAService;
    private final SecurityService security;

    ModelMapper mapper = new ModelMapper();

    @GetMapping("/category/all")
    public String categoryAllPage(Model model){

        List<ItemDto> items = itemService.searchAllNoOffset(null);

        model.addAttribute("items", items);

        return "item/categoryAll";
    }

    @GetMapping("/category/top")
    public String categoryTopPage(){
        return "item/categoryTop";
    }

    @GetMapping("/category/bottom")
    public String categoryBottomPage(){
        return "item/categoryBottom";
    }

    @GetMapping("/category/cap")
    public String categoryCapPage(){
        return "item/categoryCap";
    }


    @ApiOperation(value = "상품 상세보기")
    @GetMapping("/items/{id}")
    public String itemDetails(@PathVariable Long id, Model model) throws Exception {

        // 상품
        Item item = itemRepository.findById(id)
                .orElseThrow(() -> new ItemNotFoundException("해당 상품이 존재하지 않습니다. itemId = " + id));
        ItemDto itemDto = mapper.map(item, ItemDto.class);

        // 상품 이미지
        boolean imageExists = itemImageRepository.existsByItemId(item);
        List<ItemImageDto> images = (!imageExists) ? null : itemService.searchAllItemImage(item);

        Long qnaSize = qnARepository.countByItemId(item);

        model.addAttribute("item", itemDto);  // 상품
        model.addAttribute("images", images);  // 상품 이미지
        model.addAttribute("qnaSize", qnaSize);  // qna 개수


        return "item/itemDetails";
    }


    @ApiOperation(value = "상품 상세보기에 QnA html 리턴", notes = "ajax 용도")
    @GetMapping("/item/get/qnaList")
    public String getQnaList (@RequestParam(name = "itemId") Long itemId, Model model,
                          @RequestParam(name = "page", defaultValue = "1") int curPage) throws Exception {

        return qnAService.getQnAHtml(itemId, model, curPage);
    }



    @ApiOperation(value = "Q&A 저장")
    @PostMapping("/item/qna/send")
    public @ResponseBody String saveItemQnA(QnADto dto, Long itemId) throws Exception {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("해당 상품이 존재하지 않습니다. itemId : " + itemId));
        dto.setItemId(item);

        int result = qnAService.checkValidationQnA(dto);
        if(result == 0){
            qnAService.saveQnA(dto);
            return "Y";     // 저장에 성공하면
        }else if(result == -2){
            return "login"; // 비로그인일때
        }else{
            return "N";     // 유효성 검사에 실패했을때
        }
    }

    @ApiOperation(value = "QnA답글 저장")
    @PostMapping("/item/reply/send")
    public @ResponseBody String saveItemQnaReply(QnADto dto, Long itemId) throws Exception {

        Item item = itemRepository.findById(itemId)
                .orElseThrow(() -> new ItemNotFoundException("해당 상품이 존재하지 않습니다. itemId : " + itemId));
        dto.setItemId(item);

        if(!security.checkHasRole(Role.ADMIN.getValue())){
            return "N";
        }else{
            // 여기다 답글 저장 메소드
            qnAService.saveQnAReply(dto);
            return "Y";
        }
    }

}
