package com.ecommerce.newshop1.repository;


import com.ecommerce.newshop1.entity.ProOptEntity;
import com.ecommerce.newshop1.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProOptionRepository extends JpaRepository<ProOptEntity, Long> {

    List<ProOptEntity> findAllByProductId(ProductEntity ProductId);

}
