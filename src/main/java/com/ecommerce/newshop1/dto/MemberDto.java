package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;

    @NotNull
    private String userId;

    @NotNull
    private String password;

    private Role role;

    private Sns sns;

    @NotNull
    private String phoneNum;

    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;


}
