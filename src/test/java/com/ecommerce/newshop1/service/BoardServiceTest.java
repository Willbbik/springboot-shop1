package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.entity.Board;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import com.ecommerce.newshop1.repository.BoardRepository;
import com.ecommerce.newshop1.repository.MemberRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class BoardServiceTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    BoardRepository boardRepository;


    @Test
    @DisplayName("게시글 작성")
    void save(){

        // given
        Member member = new Member();
        member.setUserId("t");
        member.setPassword("password");
        member.setRole(Role.MEMBER);
        member.setSns(Sns.NONE);
        member.setPhoneNum("01012341234");
        memberRepository.save(member);

        Board board = Board.builder()
                .member(member)
                .writer("testWriter")
                .title("testTitle")
                .content("testContent")
                .hide("private")
                .build();

        // when
        Long id = boardRepository.save(board).getId();
        Optional<Board> findBoard = boardRepository.findById(id);

        // then
        assertAll(
                () -> assertTrue(findBoard.isPresent()),
                () -> assertTrue(findBoard.get().getMember().equals(member))
        );


    }


}
