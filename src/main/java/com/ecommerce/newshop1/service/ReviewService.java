package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ReviewDto;
import com.ecommerce.newshop1.entity.Item;

import java.util.List;

public interface ReviewService {

    Long getLastReviewId(List<ReviewDto> reviewList, Long lastReviewId);
    void saveReview(ReviewDto reviewDto, Long itemId);
    Long countByItem(Item item);
    List<ReviewDto> searchAll(Long itemId, Long lastReviewId);
    ReviewDto editReview(ReviewDto reviewDto);


}
