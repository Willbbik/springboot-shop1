package com.ecommerce.newshop1.entity;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "board")
public class  Board extends TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "board")
    private List<BoardComment> boardCommentList = new ArrayList<>();

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "board")
    private List<BoardReComment> boardReCommentList = new ArrayList<>();

    public void addBoardCommentList(BoardComment comment){
        this.getBoardCommentList().add(comment);
        comment.setBoard(this);
    }

    public void addBoardReCommentList(BoardReComment reComment) {
        this.getBoardReCommentList().add(reComment);
        reComment.setBoard(this);
    }

}
