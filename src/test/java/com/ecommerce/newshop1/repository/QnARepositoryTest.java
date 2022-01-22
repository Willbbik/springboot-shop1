package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.ItemQnADto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.ItemQnA;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class QnARepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ItemQnARepository qnaRepository;

    @Test
    @DisplayName("사용자가 작성한 qna 가져오기")
    void searchAllByMember(){

        //given
        Member member = new Member();
            member.setUserId("t");
            member.setPassword("password");
            member.setPhoneNum("01012341234");
            member.setSns(Sns.NONE);
            member.setRole(Role.MEMBER);
        memberRepository.save(member);

        Item item = Item.builder()
                .itemName("testItem")
                .build();
        itemRepository.save(item);

        for (int i = 1; i <= 10; i++) {
            ItemQnA qna = ItemQnA.builder()
                    .member(member)
                    .item(item)
                    .writer("testWriter" + i)
                    .title("testTitle" + i)
                    .content("testContent" + i)
                    .hide("private")
                    .build();
            qnaRepository.save(qna);
        }
        Pageable pageable = PageRequest.ofSize(3);

        //when
        Member findMember = memberRepository.findById(member.getId()).get();
        List<ItemQnADto> qnaList = qnaRepository.searchAllByMember(null, findMember, pageable);

        //then
        assertAll(
                () -> assertTrue(!qnaList.isEmpty())
        );

    }


}