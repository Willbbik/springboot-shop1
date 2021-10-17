package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.ProOptNameEntity;
import com.ecommerce.newshop1.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProOptNameRepository extends JpaRepository<ProOptNameEntity, Long> {

    ProOptNameEntity findByProductId(ProductEntity ProductId);


}
