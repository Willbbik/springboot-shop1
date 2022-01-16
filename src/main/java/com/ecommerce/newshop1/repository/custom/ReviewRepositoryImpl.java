package com.ecommerce.newshop1.repository.custom;


import com.ecommerce.newshop1.dto.ReviewDto;
import com.ecommerce.newshop1.entity.QReview;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ReviewRepositoryImpl implements ReviewRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<ReviewDto> searchAll(Long itemId, Long lastReviewId, String sort) {

        if(sort.equals("old")){
            return queryFactory
                    .select(Projections.fields(ReviewDto.class,
                            QReview.review.id,
                            QReview.review.content,
                            QReview.review.writer,
                            QReview.review.createdDate
                    ))
                    .from(QReview.review)
                    .where(
                            QReview.review.item.id.eq(itemId),
                            gtReviewId(lastReviewId)
                    )
                    .orderBy(QReview.review.id.asc())
                    .limit(3)
                    .fetch();
        }else{
            return queryFactory
                    .select(Projections.fields(ReviewDto.class,
                            QReview.review.id,
                            QReview.review.content,
                            QReview.review.writer,
                            QReview.review.createdDate
                    ))
                    .from(QReview.review)
                    .where(
                            QReview.review.item.id.eq(itemId),
                            ltReviewId(lastReviewId)
                    )
                    .orderBy(QReview.review.id.desc())
                    .limit(3)
                    .fetch();
        }
    }


    private BooleanExpression ltReviewId(Long lastReviewId){
        if(lastReviewId == null){
            return null;
        }
        return QReview.review.id.lt(lastReviewId);
    }

    private BooleanExpression gtReviewId(Long lastReviewId){
        if(lastReviewId == null){
            return null;
        }
        return QReview.review.id.gt(lastReviewId);
    }


}
