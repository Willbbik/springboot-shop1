package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "QnA_1")
public class QnAEntity extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "productId")
    private ProductEntity productId;

    @Column(length = 20)
    private String writer;

    @Column(length = 2048)
    private String content;

    @Column
    private Long parent;

    @Column(length = 10)
    private String hide;

    @Column(length = 10)
    private String replyEmpty;

    @Column(length = 1)
    private Integer depth;


}
