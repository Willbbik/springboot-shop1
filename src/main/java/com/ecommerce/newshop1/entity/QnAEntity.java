package com.ecommerce.newshop1.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "item_QnA_1", indexes = @Index(name ="qnaidx", columnList = "item_id"))
public class QnAEntity{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "item_id", nullable = false)
    private Item itemId;

    @Column(length = 20, nullable = false)
    private String writer;

    @Column(length = 2048, nullable = false)
    private String content;

    @Column
    private Long parent;

    @Column(length = 10)
    private String hide;

    @Column(length = 10)
    private String replyEmpty;

    @Column(length = 1)
    private Integer depth;

    @CreatedDate
    @Column(nullable = false)
    private Date createdDate;

    @LastModifiedDate
    private Date modifiedDate;

    public void setReplyEmpty (String replyEmpty){
        this.replyEmpty = replyEmpty;
    }

}
