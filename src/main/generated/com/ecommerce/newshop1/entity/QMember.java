package com.ecommerce.newshop1.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 1132988173L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final QTimeEntity _super = new QTimeEntity(this);

    public final QCart cart;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ListPath<MemberAddress, QMemberAddress> memberAddresses = this.<MemberAddress, QMemberAddress>createList("memberAddresses", MemberAddress.class, QMemberAddress.class, PathInits.DIRECT2);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final ListPath<Order, QOrder> orderList = this.<Order, QOrder>createList("orderList", Order.class, QOrder.class, PathInits.DIRECT2);

    public final StringPath password = createString("password");

    public final StringPath phoneNum = createString("phoneNum");

    public final ListPath<QnAEntity, QQnAEntity> qnaList = this.<QnAEntity, QQnAEntity>createList("qnaList", QnAEntity.class, QQnAEntity.class, PathInits.DIRECT2);

    public final ListPath<Review, QReview> reviewList = this.<Review, QReview>createList("reviewList", Review.class, QReview.class, PathInits.DIRECT2);

    public final EnumPath<com.ecommerce.newshop1.enums.Role> role = createEnum("role", com.ecommerce.newshop1.enums.Role.class);

    public final EnumPath<com.ecommerce.newshop1.enums.Sns> sns = createEnum("sns", com.ecommerce.newshop1.enums.Sns.class);

    public final StringPath userId = createString("userId");

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.cart = inits.isInitialized("cart") ? new QCart(forProperty("cart"), inits.get("cart")) : null;
    }

}

