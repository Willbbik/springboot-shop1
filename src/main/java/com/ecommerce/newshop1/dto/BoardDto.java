package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.utils.ValidationGroups;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BoardDto {

    private Long id;
    private Member member;

    @NotBlank(message = "제목을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 100, message = "제목의 최대 길이는 100자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String title;

    @NotBlank(message = "내용을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 4096, message = "내용의 최대 길이는 4096자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String content;

    private String writer;

    @NotBlank(message = "비공개여부를 제대로 설정해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 10, message = "비공개여부의 최대 길이는 10자 입니다.", groups = ValidationGroups.LengthGroup.class)
    @Pattern(regexp = "private|public", message = "비공개여부의 값을 제대로 설정해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String hide;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


}
