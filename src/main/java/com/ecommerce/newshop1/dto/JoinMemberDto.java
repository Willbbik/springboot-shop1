package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.utils.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JoinMemberDto {

    private Long id;

    @NotBlank(message = "아이디는 필수정보 입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[a-z0-9]{5,20}$", message = "5~20자의 영문 소문자, 숫자만 사용 가능합니다. (띄어쓰기 불가능)", groups = ValidationGroups.PatternGroup.class)
    private String userId;

    @NotBlank(message = "비밀번호는 필수정보 입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[A-Za-z0-9~`!@#$%\\^&*()-]{8,25}$", message = "8~25자 영문 대 소문자, 숫자를 사용하세요. (특수문자 사용가능) (띄어쓰기 불가능)", groups = ValidationGroups.PatternGroup.class)
    private String password;

    @NotBlank(message = "비밀번호 재확인은 필수정보 입니다", groups = ValidationGroups.NotBlankGroup.class)
    private String pswdCheck;

    @NotBlank(message = "전화번호는 필수정보 입니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^(010[1-9][0-9]{7})$", message = "형식에 맞지 않는 번호입니다.", groups = ValidationGroups.PatternGroup.class)
    private String phoneNum;

    @NotBlank(message = "인증이 필요합니다.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "^[0-9]{6}$", message = "인증번호를 다시 확인해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String authNum;

}
