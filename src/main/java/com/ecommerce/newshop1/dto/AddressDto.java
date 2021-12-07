package com.ecommerce.newshop1.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {

    @NotBlank(message = "주문자 성함은 필수입력 값입니다.")
    private String customerName;

    @NotBlank
    @Pattern(regexp = "^(010[1-9][0-9]{7})$")
    private String customerPhoneNum;

    @NotBlank
    private String recipientName;

    @NotBlank
    @Pattern(regexp = "^(010[1-9][0-9]{7})$")
    private String recipientPhoneNum;

    @NotBlank
    @Size(max = 6)
    private String zipcode;

    @NotBlank
    private String address;
    private String detailAddress;

}
