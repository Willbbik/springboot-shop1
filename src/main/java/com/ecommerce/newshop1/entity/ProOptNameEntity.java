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
    private ProductEntity productEntity;

    @Column
    private String option1Name;

    @Column
    private String option2Name;

    @Column
    private String option3Name;

    @Column
    private String option4Name;

    @Column
    private String option5Name;


}
