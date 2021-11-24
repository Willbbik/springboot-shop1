package com.ecommerce.newshop1.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "member_address")
public class MemberAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
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
