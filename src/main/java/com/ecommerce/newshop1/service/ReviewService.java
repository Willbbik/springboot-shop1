package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ReviewDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Review;

import java.util.List;
import java.util.Optional;

public interface ReviewService {

    Long getLastReviewId(List<ReviewDto> reviewList, Long lastReviewId);
    void saveReview(ReviewDto reviewDto, Long itemId);
    Long countByItem(Item item);
    List<ReviewDto> searchAll(Long itemId, Long lastReviewId, String sort);
    ReviewDto editReview(ReviewDto reviewDto);
    Optional<Review> findByItem(Item item);

}
