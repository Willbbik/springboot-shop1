package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "product_opt_name_2")
public class ProOptNameEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "productId")
    private ProductEntity productId;

    @Column
    private String optionName1;

    @Column
    private String optionName2;

    @Column
    private String optionName3;

    @Column
    private String optionName4;

    @Column
    private String optionName5;


}
