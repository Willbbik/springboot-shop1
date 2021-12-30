package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.ItemImageDto;
import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.dto.ReviewDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Review;
import com.ecommerce.newshop1.repository.ItemImageRepository;
import com.ecommerce.newshop1.repository.QnARepository;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.utils.CommonService;
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
import java.util.Optional;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemImageRepository itemImageRepository;
    private final QnARepository qnARepository;
    private final QnAService qnAService;
    private final ReviewService reviewService;
    private final ItemService itemService;
    private final CommonService commonService;
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

        // 상품 리뷰 개수
        Long reviewSize = reviewService.countByItem(item);

        model.addAttribute("item", itemDto);     // 상품
        model.addAttribute("images", images);    // 상품 이미지
        model.addAttribute("qnaSize", qnaSize);  // qna 개수
        model.addAttribute("reviewSize", reviewSize);

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
    public @ResponseBody String saveItemQnA(@Validated(ValidationSequence.class) QnADto qnaDto, BindingResult errors, Long itemId){

        // 유효성 검사
        if(errors.hasErrors()){
            return commonService.getErrorMessage(errors);
        }

        if(security.isAuthenticated()){
            qnAService.saveQnA(qnaDto, itemId);
            return "success";
        }else{
            return "login";
        }
    }


    @PostMapping("/item/reply/send")
    @ApiOperation(value = "QnA답글 저장")
    public @ResponseBody String saveItemQnaReply(@Validated(ValidationSequence.class) QnADto dto, BindingResult errors, Long itemId) {

        // 유효성 검사
        if(errors.hasErrors()){
            return commonService.getErrorMessage(errors);
        }

        // 로그인 검사
        if(security.isAuthenticated() && security.checkHasRole(Role.ADMIN.getValue())){
            qnAService.saveQnAReply(dto, itemId);
            return "success";
        }else{
            return "login";
        }
    }


    @PostMapping("/item/review/write")
    @ApiOperation(value = "리뷰 작성")
    public @ResponseBody String reviewWrite(@Validated(ValidationSequence.class) ReviewDto reviewDto, BindingResult errors, Long itemId){

        // 유효성 검사
        if(errors.hasErrors()){
            return commonService.getErrorMessage(errors);
        }

        Item item = itemService.findById(itemId);
        Optional<Review> findReview = reviewService.findByItem(item);

        if(!security.isAuthenticated()) {
            return "login";
        }else if(findReview.isPresent()) {
            return "exists";
        }else {
            reviewService.saveReview(reviewDto, itemId);
            return "success";
        }
    }

    @GetMapping("/item/reviewList/get")
    @ApiOperation(value = "리뷰 반환", notes = "ajax 전용")
    public String getReviewList(@RequestParam(name = "itemId") Long itemId,
                                @RequestParam(name = "lastReviewId", required = false) Long lastReviewId,
                                @RequestParam(name = "more", required = false) String more, Model model){

        Long reviewSize = reviewService.countByItem(itemService.findById(itemId));
        List<ReviewDto> reviewList = reviewService.searchAll(itemId, lastReviewId);
        lastReviewId = reviewService.getLastReviewId(reviewList, lastReviewId);

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("reviewSize", reviewSize);
        model.addAttribute("lastReviewId", lastReviewId);

        if(more != null){
            return "item/tab/tab2ReviewMore";
        }
        return "item/tab/tab2Review";
    }



}
