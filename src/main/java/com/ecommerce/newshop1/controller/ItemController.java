package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.ItemDto;
import com.ecommerce.newshop1.dto.ItemImageDto;
import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.dto.ReviewDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.ItemImageRepository;
import com.ecommerce.newshop1.repository.QnARepository;
import com.ecommerce.newshop1.repository.ReviewRepository;
import com.ecommerce.newshop1.service.*;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.utils.CommonService;
import com.ecommerce.newshop1.utils.PaginationShowSizeThree;
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

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ItemImageRepository itemImageRepository;
    private final QnARepository qnARepository;
    private final ReviewRepository reviewRepository;
    private final QnAService qnAService;
    private final ReviewService reviewService;
    private final ItemService itemService;
    private final CommonService commonService;
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
        List<ItemImageDto> images = itemService.searchAllItemImage(item);

        // qna, 리뷰 개수
        Long qnaSize = qnAService.countQnAByItem(item);
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
                              @RequestParam(name = "page", defaultValue = "1") int curPage){

        Item item = itemService.findById(itemId);
        Long qnaSize = qnARepository.countQnAByItem(item);

        // 페이징
        PaginationShowSizeThree page = new PaginationShowSizeThree(qnaSize, curPage);
        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowMaxSize());

        // qna와 답글 가져오고 값 편집
        List<QnADto> qnaList = qnARepository.searchQnA(item, pageable); // qna
        List<QnADto> qnaReplyList = qnAService.getQnAReply(qnaList);    // qna 답글

        qnaList = qnAService.editQna(qnaList);               // QnA 편집
        qnaReplyList = qnAService.editReply(qnaReplyList);   // QnA 답글 편집

        model.addAttribute("page", page);
        model.addAttribute("qnaSize", qnaSize);
        model.addAttribute("qnaList", qnaList);
        model.addAttribute("qnaReplyList", qnaReplyList);

        return "item/tab/tab3QnA";
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
        if(!security.isAuthenticated()) {        // 로그인한 상태인지
            return "-1";
        }

        Item item = itemService.findById(itemId);
        Member member = memberService.getCurrentMember();
        boolean result = reviewRepository.existsByItemAndMember(item, member);

        if(result) {                       // 이미 리뷰를 작성했는지
            return "-2";
        }else if(!memberService.existsItem(item)){ // 해당 상품을 구매했는지
            return "-3";
        } else {
            reviewService.saveReview(reviewDto, itemId);
            return "0";
        }
    }

    @GetMapping("/item/reviewList/get")
    @ApiOperation(value = "리뷰 반환", notes = "ajax 전용")
    public String getReviewList(@RequestParam(name = "itemId") Long itemId,
                                @RequestParam(name = "lastReviewId", required = false) Long lastReviewId,
                                @RequestParam(name = "sort", required = false, defaultValue = "recent") String sort,
                                @RequestParam(name = "more", required = false) String more, Model model){

        List<ReviewDto> reviewList = reviewService.searchAll(itemId, lastReviewId, sort);
        Long reviewSize = reviewService.countByItem(itemService.findById(itemId));
        lastReviewId = reviewService.getLastReviewId(reviewList, lastReviewId, sort);

        model.addAttribute("reviewList", reviewList);
        model.addAttribute("reviewSize", reviewSize);
        model.addAttribute("lastReviewId", lastReviewId);

        if(more != null) return "item/tab/tab2ReviewMore";
        return "item/tab/tab2Review";
    }



}
