package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, Long> {


    Optional<Member> findByuserId(String userId);


}
