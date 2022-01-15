package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.ItemQnADto;
import com.ecommerce.newshop1.entity.Item;

import com.ecommerce.newshop1.entity.Member;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ItemQnARepositoryCustom {

    List<ItemQnADto> searchAllByItem(Item item, Pageable pageable);

    List<ItemQnADto> searchAllByMember(Long id, Member member, Pageable pageable);

}
