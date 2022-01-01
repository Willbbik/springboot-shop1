package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.QQnAEntity;
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
    public List<QnADto> searchQnA(Item item, Pageable pageable) {

        return queryFactory
                .select(Projections.fields(QnADto.class,
                        QQnAEntity.qnAEntity.id,
                        QQnAEntity.qnAEntity.writer,
                        QQnAEntity.qnAEntity.content,
                        QQnAEntity.qnAEntity.hide,
                        QQnAEntity.qnAEntity.replyEmpty,
                        QQnAEntity.qnAEntity.createdDate
                        ))
                .from(QQnAEntity.qnAEntity)
                .where(QQnAEntity.qnAEntity.depth.eq(1),
                        QQnAEntity.qnAEntity.item.id.eq(item.getId()))
                .orderBy(QQnAEntity.qnAEntity.id.desc())
                .limit(3)
                .offset(pageable.getOffset())
                .fetch();
    }

    @Override
    public List<QnADto> searchAllByMember(Long id, Member member) {

        return queryFactory.select(Projections.fields(QnADto.class,
                            QQnAEntity.qnAEntity.id,
                            QQnAEntity.qnAEntity.writer,
                            QQnAEntity.qnAEntity.content,
                            QQnAEntity.qnAEntity.hide,
                            QQnAEntity.qnAEntity.replyEmpty,
                            QQnAEntity.qnAEntity.createdDate
                            ))
                .from(QQnAEntity.qnAEntity)
                .where(
                        QQnAEntity.qnAEntity.member.eq(member),
                        ltQnAId(id)
                )
                .orderBy(QQnAEntity.qnAEntity.id.desc())
                .limit(3)
                .fetch();
    }

    @Override
    public Long countQnAByItem(Item item) {

        return queryFactory
                .select(Projections.fields(QnADto.class,
                        QQnAEntity.qnAEntity.id
                ))
                .from(QQnAEntity.qnAEntity)
                .where(
                      QQnAEntity.qnAEntity.item.eq(item),
                      QQnAEntity.qnAEntity.depth.eq(1)
                ).fetchCount();
    }

    private BooleanExpression ltQnAId(Long qnaId){

        if(qnaId == null){
            return null;
        }
        return QQnAEntity.qnAEntity.id.lt(qnaId);
    }

}
