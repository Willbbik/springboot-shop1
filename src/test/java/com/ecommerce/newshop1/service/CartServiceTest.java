package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@Transactional
public class CartServiceTest {

    @Autowired
    CartService cartService;

    @Autowired
    MemberRepository memberRepository;


}
