package com.ecommerce.newshop1.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QItemQnAReply is a Querydsl query type for ItemQnAReply
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QItemQnAReply extends EntityPathBase<ItemQnAReply> {

    private static final long serialVersionUID = 1384816108L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemQnAReply itemQnAReply = new QItemQnAReply("itemQnAReply");

    public final QTimeEntity _super = new QTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final StringPath hide = createString("hide");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final QItemQnA qna;

    public final BooleanPath replyEmpty = createBoolean("replyEmpty");

    public final StringPath writer = createString("writer");

    public QItemQnAReply(String variable) {
        this(ItemQnAReply.class, forVariable(variable), INITS);
    }

    public QItemQnAReply(Path<? extends ItemQnAReply> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QItemQnAReply(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QItemQnAReply(PathMetadata metadata, PathInits inits) {
        this(ItemQnAReply.class, metadata, inits);
    }

    public QItemQnAReply(Class<? extends ItemQnAReply> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
        this.qna = inits.isInitialized("qna") ? new QItemQnA(forProperty("qna"), inits.get("qna")) : null;
    }

}

