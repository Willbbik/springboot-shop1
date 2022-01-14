package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemQnADto;
import com.ecommerce.newshop1.dto.ItemQnAReplyDto;

import java.util.List;

public interface QnAReplyService {

    Long save(ItemQnAReplyDto replyDto, Long itemId, Long qnaId);

    List<ItemQnAReplyDto> findAllByQnA(List<ItemQnADto> qnaList);

    List<ItemQnAReplyDto> edit(List<ItemQnAReplyDto> replyList);


}
