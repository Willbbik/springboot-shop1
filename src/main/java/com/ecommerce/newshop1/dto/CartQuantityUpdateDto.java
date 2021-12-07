package com.ecommerce.newshop1.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CartQuantityUpdateDto {

    @Positive
    private Long id;

    @Positive
    @Min(1)
    @Max(100)
    private int quantity;

}
