package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.LocalDate;

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

    private LocalDate createdDate;
    private LocalDate modifiedDate;


}
