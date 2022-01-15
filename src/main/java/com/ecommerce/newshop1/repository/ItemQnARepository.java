package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.ItemQnA;
import com.ecommerce.newshop1.repository.custom.ItemQnARepositoryCustom;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface ItemQnARepository extends JpaRepository<ItemQnA, Long>, ItemQnARepositoryCustom {

    Long countByItem(Item item);

    boolean existsByMember(Member member);

}
