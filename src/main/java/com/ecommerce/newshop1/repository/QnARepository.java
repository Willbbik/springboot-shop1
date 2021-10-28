package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.ProductEntity;
import com.ecommerce.newshop1.entity.QnAEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QnARepository extends JpaRepository<QnAEntity, Long> {

    Optional<QnAEntity> findByParent(Long parent);
    List<QnAEntity> findAllByProductId(ProductEntity productId, Pageable pageable);
    List<QnAEntity> findAllByProductId(ProductEntity productId);

}
