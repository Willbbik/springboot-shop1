package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.BoardDto;
import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import com.ecommerce.newshop1.repository.BoardRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    BoardRepository boardRepository;

    @Autowired
    BoardServiceImpl boardService;

    @Autowired
    MemberService memberService;

    @Test
    void save(){

        // given
        Member member = new Member();
        member.setUserId("t");
        member.setPassword("password");
        member.setRole(Role.MEMBER);
        member.setSns(Sns.NONE);
        member.setPhoneNum("01012341234");
        memberService.joinNormal(member);

        BoardDto boardDto = BoardDto.builder()
                .title("title")
                .content("content")
                .hide("private")
                .build();

        Long id = boardService.save(boardDto, member.getUserId());

        Optional<Board> findBoard = boardRepository.findById(id);
        Assertions.assertTrue(findBoard.isPresent());
    }


}
