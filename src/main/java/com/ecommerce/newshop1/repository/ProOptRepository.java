package com.ecommerce.newshop1.repository;


import com.ecommerce.newshop1.entity.ProOptEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProOptRepository extends JpaRepository<ProOptEntity, Long> {



}
