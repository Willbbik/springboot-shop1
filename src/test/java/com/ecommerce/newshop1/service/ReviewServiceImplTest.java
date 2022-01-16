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

//    @Test
//    @DisplayName("리뷰 작성 테스트")
//    void saveReview() {
//
//        //given
//        Member member = Member.builder()
//                .userId("wqdwqd")
//                .password("asdasd")
//                .role(Role.MEMBER)
//                .sns(Sns.NONE)
//                .build();
//        Item item = Item.builder()
//                .itemName("testitem")
//                .build();
//
//        memberService.joinNormal(member);
//        itemService.saveItem(item);
//
//        ReviewDto reviewDto = new ReviewDto();
//
//        // when
//        reviewService.saveReview(reviewDto, item.getId());
//
//        // then
//        Review findReview = reviewRepository.findByMember(member);
//        Item findItem = itemService.findById(item.getId());
//
//        assertAll(
//                () -> assertNotNull(findReview),
//                () -> assertTrue(findItem.getReviewList().size() != 0)
//        );
//    }

    @Test
    void searchAll(){

        // given
        Member member = Member.builder()
                .userId("wqdwqd")
                .password("asdasd")
                .role(Role.MEMBER)
                .sns(Sns.NONE)
                .build();
        Item item = Item.builder()
                .itemName("testitem")
                .build();
        memberRepository.save(member);
        itemRepository.save(item);


        Item findItem = itemService.findById(item.getId());
        Member findMember = memberService.findById(member.getId());

        Review review = Review.builder()
                .content("test")
                .writer(member.getUserId())
                .build();
        findItem.addReviewList(review);
        findMember.addReviewList(review);

        reviewRepository.save(review);

        // when
        List<ReviewDto> reviewList = reviewService.searchAll(item.getId(),null, null);

        // then
        assertNotNull(reviewList);

    }


}