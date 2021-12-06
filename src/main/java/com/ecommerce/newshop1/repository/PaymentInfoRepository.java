package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.entity.OrderPaymentInformation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentInfoRepository extends JpaRepository<OrderPaymentInformation, Long> {

}
