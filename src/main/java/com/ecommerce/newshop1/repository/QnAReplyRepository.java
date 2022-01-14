package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.ItemQnAReply;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface QnAReplyRepository extends JpaRepository<ItemQnAReply, Long> {

    Optional<ItemQnAReply> findByItemQnAId(Long qnaId);

}
