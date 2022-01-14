package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemQnA;
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
public class ItemQnAReplyDto {

    private Long id;
    private Item item;
    private Member member;
    private ItemQnA qnaDto;
    private String writer;
    private String content;
    private String hide;
    private boolean replyEmpty;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
