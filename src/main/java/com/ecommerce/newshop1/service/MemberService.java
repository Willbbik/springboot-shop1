package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.JoinMemberDto;
import com.ecommerce.newshop1.dto.MemberDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.enums.Sns;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;

import java.util.List;
import java.util.Map;

public interface MemberService {

    Model createJoinDtoErrorMsg(JoinMemberDto dto, Model model);

    Map<String, String> getErrorMsg(Errors errors);

    int joinValidationCheck(JoinMemberDto dto);

    boolean existsItem(Item item, String userId);

    boolean existsByUserId(String userId);

    boolean existsWithdrawalByUserId(String userId);

    boolean checkAuthNum(String phoneNum, String authNum);

    void setAuthCheck(String phoneNum) throws Exception;

    void saveWithdrawalMember(String userId);

    void login(String userId);

    void withdrawal(String userId);

    Member joinNormal(Member member);

    Member joinOAuth(String id, Sns sns);

    Member findByUserId(String userId);

    Member findById(Long id);

    Member getCurrentMember();

    List<MemberDto> findAll(Pageable pageable);

    List<String> findAllByPhoneNum(String phoneNum);


}
