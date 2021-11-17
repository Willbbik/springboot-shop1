package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.Item;

import org.springframework.data.domain.Pageable;
import java.util.List;

public interface QnARepositoryCustom {

    List<QnADto> searchQnA(Item item, Pageable pageable);


}
