package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.WithdrawalMember;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WithdrawalMemberRepository extends JpaRepository<WithdrawalMember, Long> {

    boolean existsByUserId(String userId);

}
