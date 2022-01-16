package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String userId;
    private String password;
    private Role role;
    private Sns sns;
    private String phoneNum;
    private LocalDate createdDate;
    private LocalDate modifiedDate;

}
