package com.ecommerce.newshop1.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QBoardReComment is a Querydsl query type for BoardReComment
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QBoardReComment extends EntityPathBase<BoardReComment> {

    private static final long serialVersionUID = 1266062809L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QBoardReComment boardReComment = new QBoardReComment("boardReComment");

    public final QTimeEntity _super = new QTimeEntity(this);

    public final QBoard board;

    public final QBoardComment comment;

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath hide = createString("hide");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath writer = createString("writer");

    public QBoardReComment(String variable) {
        this(BoardReComment.class, forVariable(variable), INITS);
    }

    public QBoardReComment(Path<? extends BoardReComment> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QBoardReComment(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QBoardReComment(PathMetadata metadata, PathInits inits) {
        this(BoardReComment.class, metadata, inits);
    }

    public QBoardReComment(Class<? extends BoardReComment> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.board = inits.isInitialized("board") ? new QBoard(forProperty("board"), inits.get("board")) : null;
        this.comment = inits.isInitialized("comment") ? new QBoardComment(forProperty("comment"), inits.get("comment")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

