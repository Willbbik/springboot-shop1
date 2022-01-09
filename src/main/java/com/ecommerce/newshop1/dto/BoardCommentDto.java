package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.Member;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDto {

    private Long id;
    private Board board;
    private Member member;

    private String content;
    private String hide;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
