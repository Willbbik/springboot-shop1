package com.ecommerce.newshop1.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QQnAEntity is a Querydsl query type for QnAEntity
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QQnAEntity extends EntityPathBase<QnAEntity> {

    private static final long serialVersionUID = 1645964660L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QQnAEntity qnAEntity = new QQnAEntity("qnAEntity");

    public final QTimeEntity _super = new QTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Integer> depth = createNumber("depth", Integer.class);

    public final StringPath hide = createString("hide");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final QItem item;

    public final QMember member;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final NumberPath<Long> parent = createNumber("parent", Long.class);

    public final StringPath replyEmpty = createString("replyEmpty");

    public final StringPath writer = createString("writer");

    public QQnAEntity(String variable) {
        this(QnAEntity.class, forVariable(variable), INITS);
    }

    public QQnAEntity(Path<? extends QnAEntity> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QQnAEntity(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QQnAEntity(PathMetadata metadata, PathInits inits) {
        this(QnAEntity.class, metadata, inits);
    }

    public QQnAEntity(Class<? extends QnAEntity> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.item = inits.isInitialized("item") ? new QItem(forProperty("item")) : null;
        this.member = inits.isInitialized("member") ? new QMember(forProperty("member"), inits.get("member")) : null;
    }

}

