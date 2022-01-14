package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.ItemQnA;
import com.ecommerce.newshop1.repository.custom.QnARepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QnARepository extends JpaRepository<ItemQnA, Long>, QnARepositoryCustom {

    Long countByItem(Item item);

    boolean existsByMember(Member member);

    boolean existsByParent(Long id);

}
