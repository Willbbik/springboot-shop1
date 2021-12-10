package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.QnAEntity;
import com.ecommerce.newshop1.repository.custom.QnARepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


public interface QnARepository extends JpaRepository<QnAEntity, Long>, QnARepositoryCustom {

    Optional<QnAEntity> findByParent(Long parent);

    Long countByItemId(Item itemId);

    boolean existsByItemId(Item itemId);

}
