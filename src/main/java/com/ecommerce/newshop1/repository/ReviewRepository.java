package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Review findByMember(Member member);

}
