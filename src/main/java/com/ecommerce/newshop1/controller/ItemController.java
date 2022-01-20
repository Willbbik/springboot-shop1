package com.ecommerce.newshop1.controller;

import com.ecommerce.newshop1.dto.*;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
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

import java.security.Principal;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class ItemController {

    private final ReviewRepository reviewRepository;
    private final ItemQnAService qnaService;
    private final ItemQnAReplyService qnaReplyService;
    private final ReviewService reviewService;
    private final ItemService itemService;
    private final CommonService commonService;
    private final MemberService memberService;
    private final SecurityService security;
    private final ModelMapper mapper;

    @GetMapping("/category/{category}")
    @ApiOperation(value = "카테고리별 상품 반환")
    public String itemListPage(@PathVariable String category,
                               @RequestParam(name = "lastId", required = false) Long lastId,
                               @RequestParam(name = "more", required = false) String more, Model model){

        Pageable pageable = PageRequest.ofSize(12);

        List<ItemDto> itemList = itemService.searchAllNoOffset(category, lastId, pageable);
        lastId = itemService.getLastId(itemList, lastId);

        model.addAttribute("itemList", itemList);
        model.addAttribute("lastId", lastId);
        model.addAttribute("category", category);

        if(more != null) return "item/tab/tab_itemMore";
        return "item/itemList";
    }


    @GetMapping("/items/{id}")
    @ApiOperation(value = "상품 상세보기")
    public String itemDetails(@PathVariable Long id, Model model) {

        Item item = itemService.findById(id);
        ItemDto itemDto = mapper.map(item, ItemDto.class);

        List<ItemImageDto> images = itemService.searchAllItemImage(item);

        Long qnaSize = qnaService.countByItem(item);
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
        Long qnaSize = qnaService.countByItem(item);

        PaginationShowSizeThree page = new PaginationShowSizeThree(qnaSize, curPage);
        Pageable pageable = PageRequest.of(page.getCurPage() - 1, page.getShowMaxSize());

        List<ItemQnADto> qnaList = qnaService.searchAllByItem(item, pageable);
        List<ItemQnAReplyDto> qnaReplyList = qnaReplyService.findAllByQnA(qnaList);
        qnaList = qnaService.edit(qnaList);
        qnaReplyList = qnaReplyService.edit(qnaReplyList);

        model.addAttribute("page", page);
        model.addAttribute("qnaSize", qnaSize);
        model.addAttribute("qnaList", qnaList);
        model.addAttribute("qnaReplyList", qnaReplyList);

        return "item/tab/tab_qna";
    }


    @PostMapping("/item/qna/send")
    @ApiOperation(value = "Q&A 저장")
    public @ResponseBody String saveItemQnA(@Validated(ValidationSequence.class) ItemQnADto qnaDto, BindingResult errors, Long itemId){

        // 유효성 검사
        if(errors.hasErrors()){
            return commonService.getErrorMessage(errors);
        }else if(!security.isAuthenticated()){
            return "login";
        }

        qnaService.save(qnaDto, itemId);
        return "success";
    }


    @PostMapping("/item/reply/send")
    @ApiOperation(value = "QnA답글 저장")
    public @ResponseBody String saveItemQnaReply(@Validated(ValidationSequence.class) ItemQnAReplyDto dto, BindingResult errors, Long itemId, Long qnaId) {

        // 유효성 검사
        if(errors.hasErrors()){
            return commonService.getErrorMessage(errors);
        }else if(!security.isAuthenticated()){
            return "login";
        }else if(!security.checkHasRole(Role.ADMIN.getValue())){
            return "role";
        }

        qnaReplyService.save(dto, itemId, qnaId);
        return "success";
    }


    @PostMapping("/item/review/write")
    @ApiOperation(value = "리뷰 작성")
    public @ResponseBody String reviewWrite(@Validated(ValidationSequence.class) ReviewDto reviewDto, BindingResult errors, Long itemId, Principal principal){

        // 유효성 검사
        if(errors.hasErrors()){
            return commonService.getErrorMessage(errors);
        } else if(!security.isAuthenticated()) {        // 로그인한 상태인지
            return "-1";
        }

        Item item = itemService.findById(itemId);
        Member member = memberService.getCurrentMember();
        boolean buyResult = reviewRepository.existsByItemAndMember(item, member);

        if(buyResult) {                                 // 이미 리뷰를 작성했다면
            return "-2";
        }else if(!memberService.existsItem(item, principal.getName())){   // 해당 상품을 구매안했다면
            return "-3";
        }

        reviewService.saveReview(reviewDto, itemId);
        return "0";
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

        if(more != null) return "item/tab/tab_reviewMore";
        return "item/tab/tab_review";
    }



}
