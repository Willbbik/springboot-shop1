package com.ecommerce.newshop1.dto;

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
    private Long productId;
    private String writer;
    private String content;
    private Long parent;
    private String hide;
    private String replyEmpty;
    private Integer depth;
    private Date createdDate;
    private Date modifiedDate;
    private String title;
    private String time;

}
