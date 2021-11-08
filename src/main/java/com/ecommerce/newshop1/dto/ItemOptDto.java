package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.*;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ItemOptDto {

    private Long id;

    private Item item;

    private String color;

    private String size;

    private int quantity;

}
