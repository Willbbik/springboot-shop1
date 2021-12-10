package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;

@Entity
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "board")
public class BoardEntity extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100, nullable = false)
    @Size(max = 200)
    private String subject;

    @Column(length = 2048, nullable = false)
    @Size(max = 2048)
    private String content;

    @Column(nullable = false)
    @Size(max = 100)
    private String category;

    @Column(nullable = false)
    @Size(max = 100)
    private String writer;

    @Column(nullable = false)
    @Size(max = 10)
    private String hide;


}
