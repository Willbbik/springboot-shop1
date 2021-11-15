package com.ecommerce.newshop1.querydsl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@SpringBootTest
@Transactional
public class QuerydslTest {

    @Autowired
    EntityManager em;




}
