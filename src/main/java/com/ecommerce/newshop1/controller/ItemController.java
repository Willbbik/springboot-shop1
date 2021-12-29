package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.ItemImageDto;
import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.dto.ReviewDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.ItemImageRepository;
import com.ecommerce.newshop1.repository.QnARepository;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.utils.ValidationSequence;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemImageRepository itemImageRepository;
    private final QnARepository qnARepository;
    private final QnAService qnAService;
    private final ReviewService reviewService;
    private final ItemService itemService;
    private final MemberService memberService;
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


    @GetMapping("/items/{id}")
    @ApiOperation(value = "상품 상세보기")
    public String itemDetails(@PathVariable Long id, Model model) {

        // 상품
        Item item = itemService.findById(id);
        ItemDto itemDto = mapper.map(item, ItemDto.class);

        // 상품 이미지
        boolean imageExists = itemImageRepository.existsByItem(item);
        List<ItemImageDto> images = (!imageExists) ? null : itemService.searchAllItemImage(item);

        // 상품 QnA 질문 개수
        Long qnaSize = qnARepository.countByItem(item);

        model.addAttribute("item", itemDto);     // 상품
        model.addAttribute("images", images);    // 상품 이미지
        model.addAttribute("qnaSize", qnaSize);  // qna 개수

        return "item/itemDetails";
    }


    @GetMapping("/item/get/qnaList")
    @ApiOperation(value = "상품 상세보기에 QnA html 리턴", notes = "ajax 용도")
    public String getQnaList (@RequestParam(name = "itemId") Long itemId, Model model,
                          @RequestParam(name = "page", defaultValue = "1") int curPage) throws Exception {

        return qnAService.getQnAHtml(itemId, model, curPage);
    }


    @PostMapping("/item/qna/send")
    @ApiOperation(value = "Q&A 저장")
    public @ResponseBody String saveItemQnA(QnADto qnaDto, Long itemId) throws Exception {

        qnaDto.setItem(itemService.findById(itemId));

        int result = qnAService.checkValidationQnA(qnaDto);
        if(result == 0){
            qnAService.saveQnA(qnaDto);
            return "Y";     // 저장에 성공하면
        }else if(result == -2){
            return "login"; // 비로그인일때
        }else{
            return "N";     // 유효성 검사에 실패했을때
        }
    }

    @PostMapping("/item/reply/send")
    @ApiOperation(value = "QnA답글 저장")
    public @ResponseBody String saveItemQnaReply(QnADto dto, Long itemId) throws Exception {

        dto.setItem(itemService.findById(itemId));

        int result = qnAService.checkValidationQnA(dto);
        if(!security.checkHasRole(Role.ADMIN.getValue())){
            return "fail";
        }
        else if(result != 0){
            return "N";
        }

        // 여기다 답글 저장 메소드
        qnAService.saveQnAReply(dto);
        return "success";
    }


    @PostMapping("/item/review/write")
    @ApiOperation(value = "리뷰 작성")
    public @ResponseBody String reviewWrite(@Validated(ValidationSequence.class) ReviewDto reviewDto, BindingResult errors, Long itemId){

        if(errors.hasErrors()){
            String message = "";
            for(FieldError error : errors.getFieldErrors()){
                message = error.getDefaultMessage();
            }
            return message;
        }
        reviewService.saveReview(reviewDto, itemId);

        return "success";
    }

    @GetMapping("/item/reviewList/get")
    @ApiOperation(value = "리뷰 반환", notes = "ajax 전용")
    public String getReviewList(@RequestParam(name = "itemId") Long itemId,
                                @RequestParam(name = "lastReviewId", required = false) Long lastReviewId,
                                @RequestParam(name = "reviewMore", required = false) String more, Model model){

        Long reviewSize = reviewService.countByItem(itemService.findById(itemId));
        List<ReviewDto> reviewList = reviewService.searchAll(itemId, lastReviewId);

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("reviewSize", reviewSize);
        model.addAttribute("lastReviewId", lastReviewId);

        if(more != null){
            return "item/tab/tab2ReviewMore";
        }
        return "item/tab/tab2Review";
    }



}
