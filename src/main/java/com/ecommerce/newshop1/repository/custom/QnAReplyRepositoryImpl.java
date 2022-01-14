package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.ItemQnAReplyDto;
import com.ecommerce.newshop1.entity.QItemQnAReply;
import com.querydsl.core.QueryResults;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;

public class QnAReplyRepositoryImpl implements QnAReplyRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public ItemQnAReplyDto searchAllByQnA(Long qnaId) {

        QueryResults<ItemQnAReplyDto> result = queryFactory
                .select(Projections.fields(ItemQnAReplyDto.class,
                        QItemQnAReply.itemQnAReply.id,
                        QItemQnAReply.itemQnAReply.member,
                        QItemQnAReply.itemQnAReply.content,
                        QItemQnAReply.itemQnAReply.writer,
                        QItemQnAReply.itemQnAReply.createdDate
                        ))
                .from(QItemQnAReply.itemQnAReply)
                .where(QItemQnAReply.itemQnAReply.qna.id.eq(qnaId))
                .fetchResults();

        return result.getResults().get(0);
    }


}
