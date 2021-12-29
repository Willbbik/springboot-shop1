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

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    ModelMapper mapper = new ModelMapper();

    public Long getLastReviewId(List<ReviewDto> reviewList, Long lastReviewId){
        if(reviewList.size() > 1){
            int lastIndex = reviewList.size() - 1;
            return reviewList.get(lastIndex).getId();
        }else if(reviewList.size() == 1){
            return reviewList.get(0).getId();
        }else{
            return lastReviewId;
        }
    }

    @Override
    @Transactional
    public void saveReview(ReviewDto reviewDto, Long itemId) {

        Item item = itemService.findById(itemId);
        Member member = memberService.getCurrentMember();
        Review review = mapper.map(reviewDto, Review.class);

        review.setWriter(member.getUserId());
        item.addReviewList(review);
        member.addReviewList(review);

        reviewRepository.save(review);
    }

    @Override
    @Transactional(readOnly = true)
    public Long countByItem(Item item) {
        return reviewRepository.countByItem(item);
    }


    @Override
    @Transactional(readOnly = true)
    public List<ReviewDto> searchAll(Long itemId, Long lastReviewId) {

        List<ReviewDto> reviewDtos = reviewRepository.searchAll(itemId, lastReviewId);
        if(!reviewDtos.isEmpty()) {
            for (ReviewDto reviewDto : reviewDtos)
                editReview(reviewDto);
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


