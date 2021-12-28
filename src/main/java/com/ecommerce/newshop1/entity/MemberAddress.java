package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "member_address")
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(length = 20)
    private String name;

    @Column(length = 11)
    private String phoneNum;

    @Column(length = 6, nullable = false)
    private String zipcode;

    @Column(length = 100, nullable = false)
    private String address;

    @Column(length = 100)
    private String detailAddress;


}
