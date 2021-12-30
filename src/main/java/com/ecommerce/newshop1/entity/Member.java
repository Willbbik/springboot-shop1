package com.ecommerce.newshop1.entity;


import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "member")
public class Member extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "member_id")
    private Long id;

    @Column(length = 20, nullable = false)
    private String userId;

    @Column(length = 100)
    private String password;

    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 15, nullable = false)
    @Enumerated(EnumType.STRING)
    private Sns sns;

    @Column(length = 11)
    private String phoneNum;

    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private Cart cart;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<Review> reviewList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<QnAEntity> qnaList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<MemberAddress> memberAddresses = new ArrayList<>();

    @OneToMany(cascade = CascadeType.PERSIST, fetch = FetchType.EAGER)
    private Set<Item> itemList = new HashSet<>();

    public void addReviewList(Review review) {
        this.getReviewList().add(review);
        review.setMember(this);
    }

    public void addQnaList(QnAEntity qnaEntity) {
        this.getQnaList().add(qnaEntity);
        qnaEntity.setMember(this);
    }

}
