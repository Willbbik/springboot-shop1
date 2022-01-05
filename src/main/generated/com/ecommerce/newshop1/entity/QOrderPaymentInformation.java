package com.ecommerce.newshop1.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QOrderPaymentInformation is a Querydsl query type for OrderPaymentInformation
 */
@Generated("com.querydsl.codegen.EntitySerializer")
public class QOrderPaymentInformation extends EntityPathBase<OrderPaymentInformation> {

    private static final long serialVersionUID = -1714393311L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QOrderPaymentInformation orderPaymentInformation = new QOrderPaymentInformation("orderPaymentInformation");

    public final StringPath accountNumber = createString("accountNumber");

    public final StringPath bank = createString("bank");

    public final StringPath company = createString("company");

    public final StringPath customerName = createString("customerName");

    public final StringPath dueDate = createString("dueDate");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath number = createString("number");

    public final QOrder order;

    public final StringPath payType = createString("payType");

    public QOrderPaymentInformation(String variable) {
        this(OrderPaymentInformation.class, forVariable(variable), INITS);
    }

    public QOrderPaymentInformation(Path<? extends OrderPaymentInformation> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QOrderPaymentInformation(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QOrderPaymentInformation(PathMetadata metadata, PathInits inits) {
        this(OrderPaymentInformation.class, metadata, inits);
    }

    public QOrderPaymentInformation(Class<? extends OrderPaymentInformation> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.order = inits.isInitialized("order") ? new QOrder(forProperty("order"), inits.get("order")) : null;
    }

}

