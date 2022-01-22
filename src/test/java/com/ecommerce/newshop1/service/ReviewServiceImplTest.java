package com.ecommerce.newshop1.service;

import com.ecommerce.newshop1.dto.ReviewDto;
import com.ecommerce.newshop1.entity.Item;
import com.ecommerce.newshop1.entity.Member;
import com.ecommerce.newshop1.entity.Review;
import com.ecommerce.newshop1.enums.Role;
import com.ecommerce.newshop1.enums.Sns;
import com.ecommerce.newshop1.repository.ItemRepository;
import com.ecommerce.newshop1.repository.MemberRepository;
import com.ecommerce.newshop1.repository.ReviewRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ReviewServiceImplTest {

    @Autowired
    MemberServiceImpl memberService;

    @Autowired
    ItemService itemService;

    @Autowired
    ReviewServiceImpl reviewService;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ItemRepository itemRepository;

    @Autowired
    ReviewRepository reviewRepository;

    @Test
    @DisplayName("리뷰 작성 테스트")
    void saveReview() {

        //given
        Member member = Member.builder()
                .userId("t")
                .password("password")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .build();
        Item item = Item.builder()
                .itemName("testitem")
                .build();

        memberRepository.save(member);
        itemRepository.save(item);

        Review review = Review.builder()
                .member(member)
                .item(item)
                .writer("testWriter")
                .content("testContent")
                .build();
        // when
        Long id = reviewRepository.save(review).getId();

        // then
        Review findReview = reviewRepository.findById(id).get();

        assertAll(
                () -> assertNotNull(findReview),
                () -> assertTrue(findReview.getMember().equals(member))
        );
    }


    @Test
    @DisplayName("특정 상품에 작성된 리뷰 가져오기")
    void searchAll(){

        // given
        Member member = Member.builder()
                .userId("t")
                .password("password")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .build();
        Item item = Item.builder()
                .itemName("testitem")
                .build();

        memberRepository.save(member);
        itemRepository.save(item);

        Review review = Review.builder()
                .member(member)
                .item(item)
                .content("testContent")
                .writer("testWriter")
                .build();
        reviewRepository.save(review);

        // when
        List<ReviewDto> reviewList = reviewRepository.searchAll(item.getId(),null, null);

        // then
        assertAll(
                () -> assertFalse(reviewList.isEmpty())
        );

    }


}