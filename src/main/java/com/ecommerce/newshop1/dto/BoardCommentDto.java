package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.utils.ValidationGroups;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class BoardCommentDto {

    private Long id;
    private Board board;
    private Member member;

    @NotBlank(message = "내용을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(message = "내용의 최대 길이는 2048자 입니다.", groups = ValidationGroups.LengthGroup.class)
    private String content;

    @NotBlank(message = "정확한 비공개여부 값을 설정해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "private|public", message =  "정확한 비공개여부 값을 설정해주세요.", groups = ValidationGroups.PatternGroup.class)
    @Length(max = 10, message = "정확한 비공개여부 값을 설정해주세요.", groups = ValidationGroups.LengthGroup.class)
    private String hide;

    private int depth;
    private Long parent;
    private String writer;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

}
