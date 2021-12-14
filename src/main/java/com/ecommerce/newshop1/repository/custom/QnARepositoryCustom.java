package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.Item;

import com.ecommerce.newshop1.entity.Member;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface QnARepositoryCustom {

    List<QnADto> searchQnA(Item item, Pageable pageable);

    List<QnADto> searchAllByMember(Long id, Member member);

}
