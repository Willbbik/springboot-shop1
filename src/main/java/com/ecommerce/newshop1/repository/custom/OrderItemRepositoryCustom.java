package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.OrderItemDto;
import com.ecommerce.newshop1.dto.SearchDto;
import com.ecommerce.newshop1.enums.DeliveryStatus;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderItemRepositoryCustom {

    Long searchTotal(DeliveryStatus deliveryStatus, SearchDto searchDto);

    List<OrderItemDto> searchByDeliveryStatus(DeliveryStatus deliveryStatus, Pageable pageable);

    List<OrderItemDto> searchAllByDeliveryStatusAndSearchDto(DeliveryStatus deliveryStatus, SearchDto searchDto,  Pageable pageable);


}
