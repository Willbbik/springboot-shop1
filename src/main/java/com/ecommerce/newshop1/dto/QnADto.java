package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnADto {

    private Long id;
    private Item item;
    private String writer;
    private String content;
    private Long parent;
    private String hide;
    private String replyEmpty;
    private Integer depth;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String title;

}
