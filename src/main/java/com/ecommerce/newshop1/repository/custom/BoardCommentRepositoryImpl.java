package com.ecommerce.newshop1.repository.custom;

import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class BoardCommentRepositoryImpl implements BoardCommentRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;




}
