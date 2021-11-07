package com.ecommerce.newshop1.entity;

import com.ecommerce.newshop1.utils.enums.Role;
import lombok.*;
import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@AllArgsConstructor
@Table(name = "member_1")
public class MemberEntity extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 20, nullable = false)
    private String userid;

    @Column(length = 100)
    private String password;

    @Column(length = 30, nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(length = 15, nullable = false)
    private String sns;

    @Column(length = 11)
    private String phonenum;

}
