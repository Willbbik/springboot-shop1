package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Review;
import com.ecommerce.newshop1.repository.custom.ReviewRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long>, ReviewRepositoryCustom {

    Long countByItem(Item item);
    Optional<Review> findByItem(Item item);

}
