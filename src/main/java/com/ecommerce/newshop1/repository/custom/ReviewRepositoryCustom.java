package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.ReviewDto;

import java.util.List;

public interface ReviewRepositoryCustom {

    List<ReviewDto> searchAll(Long itemId, Long lastReviewId, String sort);

}
