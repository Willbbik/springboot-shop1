package com.ecommerce.newshop1.dto;

import com.ecommerce.newshop1.entity.MemberEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class MemberDto {

    private Long id;
    private String userid;
    private String pswd;
    private String role;
    private String sns;
    private String phonenum;

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
