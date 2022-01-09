package com.ecommerce.newshop1.repository.custom;

import com.ecommerce.newshop1.dto.BoardCommentDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.QBoardComment;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import java.util.List;

public class BoardCommentRepositoryImpl implements BoardCommentRepositoryCustom{

    @Autowired
    private JPAQueryFactory queryFactory;

    @Override
    public List<BoardCommentDto> searchAll(Board board, Long lastCommentId) {

        return queryFactory
                .select(Projections.fields(BoardCommentDto.class,
                            QBoardComment.boardComment.id,
                            QBoardComment.boardComment.content,
                            QBoardComment.boardComment.writer,
                            QBoardComment.boardComment.parent,
                            QBoardComment.boardComment.depth,
                            QBoardComment.boardComment.hide,
                            QBoardComment.boardComment.createdDate,
                            QBoardComment.boardComment.modifiedDate
                        ))
                .from(QBoardComment.boardComment)
                .where(QBoardComment.boardComment.board.eq(board))
                .orderBy(QBoardComment.boardComment.id.desc())
                .fetch();
    }

    @Override
    public List<BoardCommentDto> searchAll(Long parent) {
        return queryFactory
                .select(Projections.fields(BoardCommentDto.class,
                        QBoardComment.boardComment.id,
                        QBoardComment.boardComment.content,
                        QBoardComment.boardComment.writer,
                        QBoardComment.boardComment.parent,
                        QBoardComment.boardComment.depth,
                        QBoardComment.boardComment.hide,
                        QBoardComment.boardComment.createdDate,
                        QBoardComment.boardComment.modifiedDate
                ))
                .from(QBoardComment.boardComment)
                .where(QBoardComment.boardComment.parent.eq(parent),
                        QBoardComment.boardComment.depth.eq(2)
                        )
                .orderBy(QBoardComment.boardComment.id.desc())
                .fetch();
    }
}
