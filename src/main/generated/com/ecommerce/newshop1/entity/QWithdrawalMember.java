package com.ecommerce.newshop1.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;


/**
 * QWithdrawalMember is a Querydsl query type for WithdrawalMember
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QWithdrawalMember extends EntityPathBase<WithdrawalMember> {

    private static final long serialVersionUID = -271081374L;

    public static final QWithdrawalMember withdrawalMember = new QWithdrawalMember("withdrawalMember");

    public final QTimeEntity _super = new QTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDate = _super.createdDate;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifiedDate = _super.modifiedDate;

    public final StringPath userId = createString("userId");

    public QWithdrawalMember(String variable) {
        super(WithdrawalMember.class, forVariable(variable));
    }

    public QWithdrawalMember(Path<? extends WithdrawalMember> path) {
        super(path.getType(), path.getMetadata());
    }

    public QWithdrawalMember(PathMetadata metadata) {
        super(WithdrawalMember.class, metadata);
    }

}

