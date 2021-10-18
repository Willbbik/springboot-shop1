package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.ProductEntity;
import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnADto {


    private Long id;
    private ProductEntity productId;
    private String writer;
    private String content;
    private Long parent;
    private String hide;
    private String replyEmpty;
    private Integer depth;

}
