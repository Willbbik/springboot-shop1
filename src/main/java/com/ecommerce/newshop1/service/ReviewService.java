package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ReviewDto;

public interface ReviewService {

    void saveReview(ReviewDto reviewDto, Long itemId);

}
