package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.utils.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FindIdDto {

    @NotBlank(message = "입력하신 번호를 다시 확인해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "010", message = "입력하신 번호를 다시 확인해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String phone1;

    @NotBlank(message = "입력하신 번호를 다시 확인해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "[0-9]{4}", message = "입력하신 번호를 다시 확인해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String phone2;

    @NotBlank(message = "입력하신 번호를 다시 확인해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "[0-9]{4}", message = "입력하신 번호를 다시 확인해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String phone3;


}
