package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.utils.ValidationGroups;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.time.LocalDateTime;


@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QnADto {

    private Long id;
    private Item item;
    private Member member;
    private String writer;

    @NotBlank(message = "내용을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Length(max = 2048, message = "최대 길이는 2048자입니다.", groups = ValidationGroups.MaxGroup.class)
    private String content;
    private Long parent;

    @NotBlank(message = "정확한 비공개여부 값을 입력해주세요.", groups = ValidationGroups.NotBlankGroup.class)
    @Pattern(regexp = "public|private", message = "정확한 비공개여부 값을 입력해주세요.", groups = ValidationGroups.PatternGroup.class)
    private String hide;
    private String replyEmpty;
    private Integer depth;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;
    private String title;

}
