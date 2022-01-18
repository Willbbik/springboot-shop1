package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.utils.ValidationGroups;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemDto {

    private Long id;

    @NotBlank(message = "카테고리는 빈값일 수 없습니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "top|bottom|cap", message = "정확한 카테고리를 입력해주세요.", groups = ValidationGroups.PatternGroup.class)
    @Length(max = 10, groups = ValidationGroups.LengthGroup.class)
    private String category;

    @NotBlank(message = "상품 이름을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 100, message = "상품 이름은 최대 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String itemName;

    @NotNull(message = "가격을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @PositiveOrZero(message = "0원 이상만 가능합니다.", groups = ValidationGroups.PositiveOrZero.class)
    private Integer price;

    @NotBlank(message = "색상을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 100, message = "색상 이름은 최대 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String color;

    @NotBlank(message = "사이즈를 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 100, message = "사이즈 이름은 최대 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String size;

    @Length(max = 2048, message = "상품 정보의 내용은 최대 2048자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String itemInfo;

    @Length(max = 100, message = "모델 정보의 내용은 최대 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String model;

    @NotBlank(message = "정확한 품절 상태를 선택해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "onsale|soldout", message = "정확한 품절 상태를 선택해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String saleStatus;

    private String imageUrl;

    private String noOffset;

    private LocalDateTime createdDate;

    private LocalDateTime modifiedDate;

    private int quantity;

    private int totalPrice;

}


