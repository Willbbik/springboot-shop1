package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ItemQnADto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemQnA;
import com.ecommerce.newshop1.entity.Member;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ItemQnAService {

    ItemQnA findById(Long qnaId);

    Long countByItem(Item item);

    Long save(ItemQnADto dto, Long itemId);

    Long getLastQnAId(List<ItemQnADto> qnaList, Long lastQnAId);

    List<ItemQnADto> searchAllByItem(Item item, Pageable pageable);

    List<ItemQnADto> searchAllByMember(Long id, Member member, Pageable pageable);

    List<ItemQnADto> edit(List<ItemQnADto> qnaList);

    void deleteAllById(List<Long> id);

}
