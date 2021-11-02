package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.ProductEntity;
import com.ecommerce.newshop1.entity.QnAEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QnARepository extends JpaRepository<QnAEntity, Long> {

    Optional<QnAEntity> findByParent(Long parent);

    @Query("select a from QnAEntity a where a.depth = 1 and a.productId = :productId")
    List<QnAEntity> findAllQnASize(@Param("productId") ProductEntity productId);

    @Query("select a from QnAEntity a where a.depth = 1 and a.productId = :productId order by a.createdDate desc")
    List<QnAEntity> findAllQnA(@Param("productId") ProductEntity productId, Pageable pageable);



}
