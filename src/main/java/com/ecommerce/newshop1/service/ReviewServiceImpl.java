package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ReviewDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Review;
import com.ecommerce.newshop1.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    ModelMapper mapper = new ModelMapper();


    public Long getLastReviewId(List<ReviewDto> reviewList, Long lastReviewId, String sort){

        if(reviewList.isEmpty()){
            return lastReviewId;
        }else if(sort.equals("old")){   // 오래된순일 때
            return reviewList.stream()
                    .max(Comparator.comparingLong(ReviewDto::getId))
                    .get().getId();
        }else {                         // 최신순일 때 혹은 sort값이 조작됐을 때
            return reviewList.stream()
                    .min(Comparator.comparingLong(ReviewDto::getId))
                    .get().getId();
        }
    }

    @Override
    @Transactional
    public Long saveReview(ReviewDto reviewDto, Long itemId) {

        Item item = itemService.findById(itemId);
        Member member = memberService.getCurrentMember();
        Review review = mapper.map(reviewDto, Review.class);

        review.setWriter(member.getUserId());
        item.addReviewList(review);
        member.addReviewList(review);

        return reviewRepository.save(review).getId();
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByItem(Item item) {
        return reviewRepository.countByItem(item);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> searchAll(Long itemId, Long lastReviewId, String sort) {

        List<ReviewDto> reviewDtos = reviewRepository.searchAll(itemId, lastReviewId, sort);
        if(!reviewDtos.isEmpty()) {
            for (ReviewDto reviewDto : reviewDtos) editReview(reviewDto);
        }
        return reviewDtos;
    }

    @Override
    public ReviewDto editReview(ReviewDto reviewDto) {

        String maskingWriter = reviewDto.getWriter().substring(0, 3) + "***";
        reviewDto.setWriter(maskingWriter);
        return reviewDto;
    }


}


