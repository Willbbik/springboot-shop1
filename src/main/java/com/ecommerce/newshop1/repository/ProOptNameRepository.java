package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.ProOptNameEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProOptNameRepository extends JpaRepository<ProOptNameEntity, Long> {
}
