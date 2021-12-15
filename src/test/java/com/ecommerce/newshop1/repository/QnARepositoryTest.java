package com.ecommerce.newshop1.repository;

import com.ecommerce.newshop1.dto.QnADto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.QnAEntity;
import com.ecommerce.newshop1.utils.enums.Role;
import com.ecommerce.newshop1.utils.enums.Sns;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class QnARepositoryTest {

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    QnARepository qnARepository;

    @Test
    void searchAllByMember(){

        //given
        Member member = Member.builder()
                .userId("test")
                .password("password")
                .phoneNum("01012345678")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .build();
        memberRepository.save(member);

        Item item = Item.builder()
                .itemName("testItem")
                .build();

        itemRepository.save(item);

        for (int i = 1; i <= 10; i++) {
            QnAEntity qna = QnAEntity.builder()
                    .member(member)
                    .item(item)
                    .content("test" + i)
                    .build();
            qnARepository.save(qna);
        }

        //when
        List<QnADto> qnADtoList = qnARepository.searchAllByMember(null, member);

        //then
        assertEquals("test10", qnADtoList.get(0).getContent());


    }


    @Test
    void deleteByParent() {

        //given
        QnAEntity qna = QnAEntity.builder()
                .content("test")
                .depth(1)
                .build();
        qna = qnARepository.save(qna);

        QnAEntity reply = QnAEntity.builder()
                .parent(qna.getId())
                .content("testAnswer")
                .build();
        qnARepository.save(reply);

        //when
        qnARepository.deleteByParent(qna.getId());

        //then
        Optional<QnAEntity> result = qnARepository.findByParent(qna.getId());

        assertEquals(Optional.empty(), result);
    }

}