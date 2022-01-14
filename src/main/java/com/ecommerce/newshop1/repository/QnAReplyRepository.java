package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.ItemQnAReplyDto;
import com.ecommerce.newshop1.entity.ItemQnAReply;
import com.ecommerce.newshop1.repository.custom.QnAReplyRepositoryCustom;
import org.springframework.data.jpa.repository.JpaRepository;


public interface QnAReplyRepository extends JpaRepository<ItemQnAReply, Long>, QnAReplyRepositoryCustom {

    ItemQnAReplyDto findByQnA(Long qnaId);

}
