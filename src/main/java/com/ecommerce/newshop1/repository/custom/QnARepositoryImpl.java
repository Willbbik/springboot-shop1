package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.ItemQnADto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.QItemQnA;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import java.util.List;

public class QnARepositoryImpl implements QnARepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<ItemQnADto> searchAll(Item item, Pageable pageable) {

        return queryFactory
                .select(Projections.fields(ItemQnADto.class,
                        QItemQnA.itemQnA.id,
                        QItemQnA.itemQnA.writer,
                        QItemQnA.itemQnA.content,
                        QItemQnA.itemQnA.hide,
                        QItemQnA.itemQnA.replyEmpty,
                        QItemQnA.itemQnA.createdDate
                        ))
                .from(QItemQnA.itemQnA)
                .where(QItemQnA.itemQnA.item.eq(item))
                .orderBy(QItemQnA.itemQnA.id.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();
    }

    @Override
    public List<ItemQnADto> searchAllByMember(Long id, Member member, Pageable pageable) {

        return queryFactory.select(Projections.fields(ItemQnADto.class,
                        QItemQnA.itemQnA.id,
                        QItemQnA.itemQnA.writer,
                        QItemQnA.itemQnA.content,
                        QItemQnA.itemQnA.hide,
                        QItemQnA.itemQnA.replyEmpty,
                        QItemQnA.itemQnA.createdDate
                            ))
                .from(QItemQnA.itemQnA)
                .where(
                        QItemQnA.itemQnA.member.eq(member),
                        ltQnAId(id)
                )
                .orderBy(QItemQnA.itemQnA.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }


    private BooleanExpression ltQnAId(Long qnaId){

        if(qnaId == null){
            return null;
        }
        return QItemQnA.itemQnA.id.lt(qnaId);
    }

}
