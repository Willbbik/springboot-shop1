package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ReviewDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Review;

import java.util.List;


public interface ReviewService {

    Long getLastReviewId(List<ReviewDto> reviewList, Long lastReviewId, String sort);

    Long saveReview(ReviewDto reviewDto, Long itemId);

    Long countByItem(Item item);

    List<ReviewDto> searchAll(Long itemId, Long lastReviewId, String sort);

    ReviewDto editReview(ReviewDto reviewDto);

}
