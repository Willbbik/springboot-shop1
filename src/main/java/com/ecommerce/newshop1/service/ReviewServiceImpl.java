package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ReviewDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Review;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@Service
@RequiredArgsConstructor
public class ReviewServiceImpl implements ReviewService{

    private final ReviewRepository reviewRepository;
    private final MemberService memberService;
    private final ItemService itemService;

    ModelMapper mapper = new ModelMapper();


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

}
