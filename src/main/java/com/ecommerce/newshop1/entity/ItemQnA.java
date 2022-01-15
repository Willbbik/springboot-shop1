package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "item_qna", indexes = @Index(name ="qnaidx", columnList = "item_id"))
public class ItemQnA extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "item_qna_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true, mappedBy = "itemQnA")
    private ItemQnAReply itemQnAReply;

    @Column(length = 20, nullable = false)
    private String writer;

    @Column(length = 30, nullable = false)
    private String title;

    @Column(length = 2048, nullable = false)
    private String content;

    @Column(length = 10)
    private String hide;

    @Column
    private boolean replyEmpty;

    public void setQnAReply(ItemQnAReply qnaReply){
        this.setItemQnAReply(qnaReply);
        qnaReply.setItemQnA(this);
    }

}
