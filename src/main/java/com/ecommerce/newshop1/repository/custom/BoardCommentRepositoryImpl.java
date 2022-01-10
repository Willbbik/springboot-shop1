package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.QBoardComment;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BoardCommentRepositoryImpl implements BoardCommentRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<BoardCommentDto> searchAll(Board board, Long lastCommentId, Pageable pageable) {

        return queryFactory
                .select(Projections.fields(BoardCommentDto.class,
                            QBoardComment.boardComment.id,
                            QBoardComment.boardComment.content,
                            QBoardComment.boardComment.writer,
                            QBoardComment.boardComment.createdDate,
                            QBoardComment.boardComment.modifiedDate
                        ))
                .from(QBoardComment.boardComment)
                .where(
                        QBoardComment.boardComment.board.eq(board),
                        ltCommentId(lastCommentId)
                )
                .orderBy(QBoardComment.boardComment.id.desc())
                .limit(pageable.getPageSize())
                .fetch();
    }

    private BooleanExpression ltCommentId(Long lastCommentId){
        if(lastCommentId == null){
            return null;
        }
        return QBoardComment.boardComment.id.lt(lastCommentId);
    }


}