package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.MemberEntity;
import com.ecommerce.newshop1.utils.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String userid;
    private String pswd;
    private Role role;
    private String sns;
    private String phonenum;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public MemberEntity toEntity(){
        return MemberEntity.builder()
                .id(id)
                .userid(userid)
                .password(pswd)
                .role(role)
                .sns(sns)
                .phonenum(phonenum)
                .build();
    }


}
