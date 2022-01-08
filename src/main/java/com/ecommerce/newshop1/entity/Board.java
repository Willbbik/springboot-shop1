package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "board")
public class Board extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @Column(length = 100, nullable = false)
    @Size(max = 100)
    private String title;

    @Column(length = 4096, nullable = false)
    @Size(max = 4096)
    private String content;

    @Column(length = 20, nullable = false)
    @Size(max = 20)
    private String writer;

    @Column(length = 10, nullable = false)
    @Size(max = 10)
    private String hide;


}
