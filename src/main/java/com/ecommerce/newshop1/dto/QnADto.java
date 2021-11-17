package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import lombok.*;

import java.util.Date;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnADto {

    private Long id;
    private Item itemId;
    private String writer;
    private String content;
    private Long parent;
    private String hide;
    private String replyEmpty;
    private Integer depth;
    private Date createdDate;
    private Date modifiedDate;
    private String title;

}
